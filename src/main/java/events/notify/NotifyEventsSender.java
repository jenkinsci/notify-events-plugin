package events.notify;

import hudson.FilePath;
import hudson.model.*;
import hudson.util.Secret;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;
import org.jenkinsci.plugins.tokenmacro.TokenMacro;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class NotifyEventsSender {

    final static String NOTIFY_EVENTS_DISPLAY_NAME = "Notify.Events";

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static final Logger logger = LoggerFactory.getLogger(NotifyEventsSender.class.getName());

    private static NotifyEventsSender instance;

    private final OkHttpClient client;

    public NotifyEventsSender() {
        client = new OkHttpClient();
    }

    public synchronized static NotifyEventsSender getInstance() {
        if (instance == null) {
            instance = new NotifyEventsSender();
        }

        return instance;
    }

    public void send(Secret token, String type, String message, Run<?, ?> run, FilePath workspace, TaskListener listener) {
        List<TokenMacro> macros = new ArrayList<>();

        try {
            message = TokenMacro.expandAll(run, workspace, listener, message, false, macros);
        } catch (MacroEvaluationException e) {
            listener.getLogger().println("Error evaluating token: " + e.getMessage());
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(NotifyEventsSender.class.getName()).log(Level.SEVERE, null, e);
        }

        JSONObject json = new JSONObject();

        json.put("type",    type);
        json.put("message", message);

        if (run instanceof AbstractBuild) {
            AbstractProject<?, ?> project = ((AbstractBuild<?, ?>) run).getProject();

            JSONObject jsonProject = new JSONObject();

            jsonProject.put("name",     project.getDisplayName());
            jsonProject.put("fullName", project.getFullDisplayName());
            jsonProject.put("url",      project.getAbsoluteUrl());

            json.put("project", jsonProject);
        }

        JSONObject jsonBuild = new JSONObject();

        jsonBuild.put("id",       run.getId());
        jsonBuild.put("number",   run.getNumber());
        jsonBuild.put("name",     run.getDisplayName());
        jsonBuild.put("fullName", run.getFullDisplayName());
        jsonBuild.put("url",      run.getAbsoluteUrl());

        json.put("build", jsonBuild);

        String jsonString = json.toJSONString();

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, jsonString);

        String url = "https://notify.events/api/v1/channel/source/" + token.getPlainText() + "/execute";

        Request request = new Request.Builder().url(url).post(body).build();

        try {
            Response response = client.newCall(request).execute();

            if (response.code() != 200) {
                throw new Exception("Invalid response code: " + response.code());
            }

            response.close();

            logger.debug(String.format("Invocation of request '%s' successful", url));
        } catch (Exception e) {
            logger.error(String.format("Invocation of request '%s' failed", url), e);
        }
    }
}
