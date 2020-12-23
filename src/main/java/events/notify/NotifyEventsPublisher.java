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
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import hudson.util.Secret;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.Nonnull;
import java.io.IOException;

public class NotifyEventsPublisher extends Notifier implements SimpleBuildStep {

    private Secret token;
    private String message;

    private boolean onSuccess;
    private boolean onUnstable;
    private boolean onFailure;
    private boolean onAborted;

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @DataBoundConstructor
    public NotifyEventsPublisher(String token, String message, boolean onSuccess, boolean onUnstable, boolean onFailure, boolean onAborted) {
        this.token   = Secret.fromString(Util.fixEmptyAndTrim(token));
        this.message = Util.fixEmptyAndTrim(message);

        this.onSuccess  = onSuccess;
        this.onUnstable = onUnstable;
        this.onFailure  = onFailure;
        this.onAborted  = onAborted;
    }

    public String getToken() {
        return token.getPlainText();
    }

    public void setToken(String token) {
        this.token = Secret.fromString(token);
    }

    public void setToken(Secret token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getOnSuccess() {
        return onSuccess;
    }

    public void setOnSuccess(boolean onSuccess) {
        this.onSuccess = onSuccess;
    }

    public boolean getOnUnstable() {
        return onUnstable;
    }

    public void setOnUnstable(boolean onUnstable) {
        this.onUnstable = onUnstable;
    }

    public boolean getOnFailure() {
        return onFailure;
    }

    public void setOnFailure(boolean onFailure) {
        this.onFailure = onFailure;
    }

    public boolean getOnAborted() {
        return onAborted;
    }

    public void setOnAborted(boolean onAborted) {
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

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        private Secret token;
        private String message;

        private boolean onSuccess;
        private boolean onUnstable;
        private boolean onFailure;
        private boolean onAborted;

        public String getToken() {
            return token.getPlainText();
        }

        @DataBoundSetter
        public void setToken(String token) {
            this.token = Secret.fromString(token);
        }

        @DataBoundSetter
        public void setToken(Secret token) {
            this.token = token;
        }

        public String getMessage() {
            return message;
        }

        @DataBoundSetter
        public void setMessage(String message) {
            this.message = message;
        }

        public boolean getOnSuccess() {
            return onSuccess;
        }

        @DataBoundSetter
        public void setOnSuccess(boolean onSuccess) {
            this.onSuccess = onSuccess;
        }

        public boolean getOnUnstable() {
            return onUnstable;
        }

        @DataBoundSetter
        public void setOnUnstable(boolean onUnstable) {
            this.onUnstable = onUnstable;
        }

        public boolean getOnFailure() {
            return onFailure;
        }

        @DataBoundSetter
        public void setOnFailure(boolean onFailure) {
            this.onFailure = onFailure;
        }

        public boolean getOnAborted() {
            return onAborted;
        }

        @DataBoundSetter
        public void setOnAborted(boolean onAborted) {
            this.onAborted = onAborted;
        }

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
