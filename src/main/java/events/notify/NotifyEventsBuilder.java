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
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.Secret;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.Nonnull;
import java.io.IOException;

public class NotifyEventsBuilder extends Builder implements SimpleBuildStep {

    private Secret token;
    private String message;

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @DataBoundConstructor
    public NotifyEventsBuilder(String token, String message) {
        this.token   = Secret.fromString(Util.fixEmptyAndTrim(token));
        this.message = Util.fixEmptyAndTrim(message);
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

        if (Strings.isNullOrEmpty(message) || (message.length() == 0)) {
            taskListener.error("Message can't be empty");
            run.setResult(Result.FAILURE);

            return;
        }

        NotifyEventsSender.getInstance().send(token, "message", message, run, filePath, taskListener);
    }

    @Extension
    @Symbol("notifyEvents")
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {

        private Secret token;
        private String message;

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

        public FormValidation doCheckMessage(@QueryParameter String value) {
            if (Util.fixEmptyAndTrim(value) == null) {
                return FormValidation.error("Message can't be empty");
            }

            return FormValidation.ok();
        }
    }
}
