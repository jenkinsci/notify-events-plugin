package events.notify;

import com.google.common.base.Strings;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import hudson.util.Secret;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.Nonnull;
import java.io.IOException;

public class NotifyEventsPublisher extends Notifier implements SimpleBuildStep {

    private final Secret token;
    private final String message;

    private final boolean onSuccess;
    private final boolean onUnstable;
    private final boolean onFailure;
    private final boolean onAborted;

    @DataBoundConstructor
    public NotifyEventsPublisher(String token, String message, boolean onSuccess, boolean onUnstable, boolean onFailure, boolean onAborted) {
        this.token = Secret.fromString(Util.fixEmptyAndTrim(token));
        this.message = Util.fixEmptyAndTrim(message);

        this.onSuccess = onSuccess;
        this.onUnstable = onUnstable;
        this.onFailure = onFailure;
        this.onAborted = onAborted;
    }

    @Override
    public void perform(
            @Nonnull Run<?, ?> run,
            @Nonnull FilePath filePath,
            @Nonnull Launcher launcher,
            @Nonnull TaskListener taskListener) throws InterruptedException, IOException {

        if (Strings.isNullOrEmpty(token.getPlainText()) || (token.getPlainText().length() != 32)) {
            taskListener.error("Invalid token");
            run.setResult(Result.FAILURE);

            return;
        }

        Result result = run.getResult();

        if (((result == Result.SUCCESS) && onSuccess)
         || ((result == Result.UNSTABLE) && onUnstable)
         || ((result == Result.FAILURE) && onFailure)
         || ((result == Result.ABORTED) && onAborted)) {
            NotifyEventsSender.getInstance().send(token, result.toString().toLowerCase(), message, run, filePath, taskListener);
        }
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public NotifyEventsPublisherDescriptor getDescriptor() {
        return (NotifyEventsPublisherDescriptor) super.getDescriptor();
    }

    public Secret getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public boolean isOnSuccess() {
        return onSuccess;
    }

    public boolean isOnUnstable() {
        return onUnstable;
    }

    public boolean isOnFailure() {
        return onFailure;
    }

    public boolean isOnAborted() {
        return onAborted;
    }

    @Extension
    public static class NotifyEventsPublisherDescriptor extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        @NonNull
        public String getDisplayName() {
            return NotifyEventsSender.NOTIFY_EVENTS_DISPLAY_NAME;
        }

        public FormValidation doCheckToken(@QueryParameter String value) {
            if (Util.fixEmptyAndTrim(value) == null) {
                return FormValidation.error("Token can't be empty");
            }

            if (value.length() != 32) {
                return FormValidation.error("Invalid token format");
            }

            return FormValidation.ok();
        }
    }
}
