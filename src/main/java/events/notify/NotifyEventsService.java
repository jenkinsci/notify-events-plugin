package events.notify;

import com.google.common.base.Strings;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.remoting.VirtualChannel;
import hudson.util.Secret;
import jenkins.MasterToSlaveFileCallable;
import okhttp3.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tools.ant.types.FileSet;
import org.jenkinsci.plugins.tokenmacro.TokenMacro;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class NotifyEventsService {

    public final static String NOTIFY_EVENTS_DISPLAY_NAME = "Notify.Events";

    private final static String BASE_URL = "https://notify.events/api/v1/channel/source/%s/execute";

    public final static String PRIORITY_LOWEST  = "lowest";
    public final static String PRIORITY_LOW     = "low";
    public final static String PRIORITY_NORMAL  = "normal";
    public final static String PRIORITY_HIGH    = "high";
    public final static String PRIORITY_HIGHEST = "highest";

    public final static String LEVEL_VERBOSE = "verbose";
    public final static String LEVEL_INFO    = "info";
    public final static String LEVEL_NOTICE  = "notice";
    public final static String LEVEL_WARNING = "warning";
    public final static String LEVEL_ERROR   = "error";
    public final static String LEVEL_SUCCESS = "success";

    public static Map<String, String> getPriorities() {
        Map<String, String> map = new HashMap<String, String>();

        map.put(PRIORITY_LOWEST,  "Lowest");
        map.put(PRIORITY_LOW,     "Low");
        map.put(PRIORITY_NORMAL,  "Normal");
        map.put(PRIORITY_HIGH,    "High");
        map.put(PRIORITY_HIGHEST, "Highest");

        return map;
    }

    public static Map<String, String> getLevels() {
        Map<String, String> map = new HashMap<String, String>();

        map.put(LEVEL_VERBOSE, "Verbose");
        map.put(LEVEL_INFO,    "Info");
        map.put(LEVEL_NOTICE,  "Notice");
        map.put(LEVEL_WARNING, "Warning");
        map.put(LEVEL_ERROR,   "Error");
        map.put(LEVEL_SUCCESS, "Success");

        return map;
    }

    private static final Logger logger = LoggerFactory.getLogger(NotifyEventsService.class.getName());

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static NotifyEventsService instance;

    private final OkHttpClient client;

    public NotifyEventsService() {
        client = new OkHttpClient();
    }

    public synchronized static NotifyEventsService getInstance() {
        if (instance == null) {
            instance = new NotifyEventsService();
        }

        return instance;
    }

    public void send(Secret token, String title, String message, String priority, String level, Boolean attachBuildLog, String attachment, Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener, List<TokenMacro> privateTokens) throws IOException {
        String plainToken = token.getPlainText();

        title    = Util.fixNull(title);
        message  = Util.fixNull(message);
        priority = Util.fixNull(priority);
        level    = Util.fixNull(level);

        try {
            plainToken = TokenMacro.expandAll(run, workspace, listener, plainToken, true, privateTokens);
            title      = TokenMacro.expandAll(run, workspace, listener, title, true, privateTokens);
            message    = TokenMacro.expandAll(run, workspace, listener, message, true, privateTokens);
        } catch (Exception e) {
            listener.error(String.format("Error evaluating token: %s", e.getMessage()));
            run.setResult(Result.FAILURE);

            return;
        }

        if (Strings.isNullOrEmpty(plainToken) || (plainToken.length() != 32)) {
            listener.error("Invalid token");
            run.setResult(Result.FAILURE);

            return;
        }

        if (title.isEmpty()) {
            title = "$BUILD_TAG - Message";
        }

        if (message.isEmpty()) {
            listener.error("Message can't be empty");
            run.setResult(Result.FAILURE);

            return;
        }

        // Backward compatible with version prior 1.4.0
        if (priority.isEmpty()) {
            priority = PRIORITY_NORMAL;
        }

        if (!NotifyEventsService.getPriorities().containsKey(priority)) {
            listener.error("Priority invalid value");
            run.setResult(Result.FAILURE);

            return;
        }

        // Backward compatible with version prior 1.4.0
        if (level.isEmpty()) {
            level = LEVEL_INFO;
        }

        if (!NotifyEventsService.getLevels().containsKey(level)) {
            listener.error("Level invalid value");
            run.setResult(Result.FAILURE);

            return;
        }

        JSONArray files = new JSONArray();

        int cnt = 0;

        if (attachBuildLog) {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();

            long logFileLength = run.getLogText().length();
            long pos = 0;

            while (pos < logFileLength) {
                pos = run.getLogText().writeLogTo(pos, bao);
            }

            byte[] content = bao.toByteArray();
            byte[] encoded = Base64.getEncoder().encode(content);

            String encodedString = new String(encoded, StandardCharsets.US_ASCII);

            JSONObject file = new JSONObject();

            file.put("name",    "build.log");
            file.put("content", encodedString);

            files.add(file);

            cnt++;
        }

        if (!attachment.trim().isEmpty()) {
            try {
                Map<String, String> filePaths = workspace.act(new ListFiles(attachment, ""));

                if (!filePaths.isEmpty()) {
                    for (Map.Entry<String, String> entry : filePaths.entrySet()) {
                        listener.getLogger().printf("File: %s%n", entry.getValue());

                        byte[] content = Files.readAllBytes(Paths.get(entry.getValue()));
                        byte[] encoded = Base64.getEncoder().encode(content);

                        String encodedString = new String(encoded, StandardCharsets.US_ASCII);

                        JSONObject file = new JSONObject();

                        file.put("name",    entry.getKey());
                        file.put("content", encodedString);

                        files.add(file);

                        cnt++;

                        if (cnt > 3) {
                            break;
                        }
                    }
                } else {
                    listener.getLogger().printf("Empty attachment list%n");
                }
            } catch (Exception e) {
                listener.error("Attachments handle error: %s%n%s%n", e.getMessage(), ExceptionUtils.getStackTrace(e));
                run.setResult(Result.FAILURE);

                return;
            }
        }

        JSONObject json = new JSONObject();

        json.put("title",    title);
        json.put("message",  message);
        json.put("priority", priority);
        json.put("level",    level);
        json.put("files",    files);

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, json.toJSONString());

        String url = String.format(BASE_URL, plainToken);

        Request request = new Request.Builder().url(url).post(body).build();

        try {
            Response response = client.newCall(request).execute();

            if (response.code() != 200) {
                throw new Exception(String.format("Invalid response code: %d", response.code()));
            }

            response.close();

            logger.info(String.format("Send message to channel: %s...", plainToken.substring(0, 6)));
            listener.getLogger().printf("Send message to channel: %s...%n", plainToken.substring(0, 6));

            logger.debug(String.format("Invocation of request '%s' successful", url));
        } catch (Exception e) {
            logger.error(String.format("Invocation of request '%s' failed", url), e);
        }
    }

    private static final class ListFiles extends MasterToSlaveFileCallable<Map<String, String>> {

        private static final long serialVersionUID = 1;

        private final String includes, excludes;

        ListFiles(String includes, String excludes) {
            this.includes = includes;
            this.excludes = excludes;
        }

        @Override
        public Map<String, String> invoke(File basedir, VirtualChannel channel) throws IOException, InterruptedException {
            Map<String, String> result = new HashMap<String, String>();

            FileSet fileSet = Util.createFileSet(basedir, includes, excludes);

            fileSet.setCaseSensitive(true);
            fileSet.setFollowSymlinks(true);
            
            for (String file : fileSet.getDirectoryScanner().getIncludedFiles()) {
                result.put(file, basedir.getAbsolutePath() + File.separatorChar + file);
            }

            return result;
        }
    }
}
