package events.notify;

import hudson.model.AbstractProject;
import hudson.model.Build;
import hudson.model.Run;
import hudson.util.Secret;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyEventsSender {

    final static String NOTIFY_EVENTS_DISPLAY_NAME = "Notify.Events";

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static final Logger log = LoggerFactory.getLogger(NotifyEventsSender.class.getName());

    private static NotifyEventsSender instance;

    private OkHttpClient client;

    public NotifyEventsSender() {
        client = new OkHttpClient();
    }

    public synchronized static NotifyEventsSender getInstance() {
        if (instance == null) {
            instance = new NotifyEventsSender();
        }

        return instance;
    }

    public void send(Secret token, String type, String message, Run<?, ?> run) {
        JSONObject json = new JSONObject();

        json.put("type", type);
        json.put("message", message);

        if (run instanceof Build) {
            AbstractProject<?, ?> project = ((Build<?, ?>) run).getProject();

            JSONObject jsonProject = new JSONObject();

            jsonProject.put("name", project.getDisplayName());
            jsonProject.put("fullName", project.getFullDisplayName());
            jsonProject.put("url", project.getAbsoluteUrl());

            json.put("project", jsonProject);
        }

        JSONObject jsonBuild = new JSONObject();

        jsonBuild.put("id", run.getId());
        jsonBuild.put("number", run.getNumber());
        jsonBuild.put("name", run.getDisplayName());
        jsonBuild.put("fullName", run.getFullDisplayName());
        jsonBuild.put("url", run.getAbsoluteUrl());

        json.put("build", jsonBuild);

        String jsonString = json.toJSONString();

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, jsonString);

        String url = "https://notify.events/api/v1/channel/source/" + token.getPlainText() + "/execute";

        Request request = new Request.Builder().url(url).post(body).build();

        try {
            client.newCall(request).execute();

            log.debug("Invocation of request {} successful", url);
        } catch (Exception e) {
            log.info("Invocation of request {} failed", url, e);
        }
    }
}
