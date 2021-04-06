package events.notify;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Map;

public class NotifyEventsBuilder extends Builder implements SimpleBuildStep {

    private Secret token;
    private String title;
    private String message;
    private String priority;
    private String level;
    private String attachment;

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @DataBoundConstructor
    public NotifyEventsBuilder(final String token, final String title, final String message, final String priority, final String level, final String attachment) {
        this.token      = Secret.fromString(Util.fixEmptyAndTrim(token));
        this.title      = Util.fixEmptyAndTrim(title);
        this.message    = Util.fixEmptyAndTrim(message);
        this.priority   = Util.fixEmptyAndTrim(priority);
        this.level      = Util.fixEmptyAndTrim(level);
        this.attachment = Util.fixEmptyAndTrim(attachment);
    }

    // Backward compatible with version prior 1.4.0
    public NotifyEventsBuilder(final String token, final String message) {
        this(token, DescriptorImpl.DEFAULT_TITLE, message, DescriptorImpl.DEFAULT_PRIORITY, DescriptorImpl.DEFAULT_LEVEL, "");
    }

    // Backward compatible with version prior 1.5.0
    public NotifyEventsBuilder(final String token, final String title, final String message, final String priority, final String level) {
        this(token, title, message, priority, level, "");
    }

    public NotifyEventsBuilder() {
        this.title      = DescriptorImpl.DEFAULT_TITLE;
        this.priority   = DescriptorImpl.DEFAULT_PRIORITY;
        this.level      = DescriptorImpl.DEFAULT_LEVEL;
        this.attachment = DescriptorImpl.DEFAULT_ATTACHMENT;
    }

    public String getToken() {
        return token.getPlainText();
    }

    public void setToken(final String token) {
        this.token = Secret.fromString(token);
    }

    public void setToken(final Secret token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(final String priority) {
        this.priority = priority;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(final String level) {
        this.level = level;
    }

    public String getAttachment() { return attachment; }

    public void setAttachment(final String attachment) {
        this.attachment = attachment;
    }

    @Override
    public void perform(
            @Nonnull Run<?, ?> run,
            @Nonnull FilePath filePath,
            @Nonnull Launcher launcher,
            @Nonnull TaskListener taskListener) throws InterruptedException, IOException {
        NotifyEventsService.getInstance().send(token, title, message, priority, level, attachment, run, filePath, launcher, taskListener, null);
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public final static String DEFAULT_TITLE      = "$BUILD_TAG - Message";
        public final static String DEFAULT_MESSAGE    = "";
        public final static String DEFAULT_PRIORITY   = NotifyEventsService.PRIORITY_NORMAL;
        public final static String DEFAULT_LEVEL      = NotifyEventsService.LEVEL_INFO;
        public final static String DEFAULT_ATTACHMENT = "";

        private Secret token;
        private String title;
        private String message;
        private String priority;
        private String level;
        private String attachment;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// Token
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public String getToken() {
            return token.getPlainText();
        }

        @DataBoundSetter
        public void setToken(final String token) {
            this.token = Secret.fromString(token);
        }

        @DataBoundSetter
        public void setToken(final Secret token) {
            this.token = token;
        }

        public FormValidation doCheckToken(@QueryParameter final String value) {
            if (Util.fixEmptyAndTrim(value) == null) {
                return FormValidation.error("Token can't be empty");
            }

            return FormValidation.ok();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// Title
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public String getTitle() {
            return title;
        }

        @DataBoundSetter
        public void setTitle(final String title) {
            this.title = title;
        }

        public FormValidation doCheckTitle(@QueryParameter String value) {
            if (Util.fixEmptyAndTrim(value) == null) {
                return FormValidation.error("Title can't be empty");
            }

            return FormValidation.ok();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// Message
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public String getMessage() {
            return message;
        }

        @DataBoundSetter
        public void setMessage(final String message) {
            this.message = message;
        }

        public FormValidation doCheckMessage(@QueryParameter final String value) {
            if (Util.fixEmptyAndTrim(value) == null) {
                return FormValidation.error("Message can't be empty");
            }

            return FormValidation.ok();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// Priority
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public String getPriority() {
            return priority;
        }

        @DataBoundSetter
        public void setPriority(final String priority) {
            this.priority = priority;
        }

        @Restricted(NoExternalUse.class)
        public ListBoxModel doFillPriorityItems(@QueryParameter("priority") final String priority) {
            ListBoxModel list = new ListBoxModel();

            for (Map.Entry<String, String> entry : NotifyEventsService.getPriorities().entrySet()) {
                if (entry.getKey().equals(priority)) {
                    list.add(new ListBoxModel.Option(entry.getValue(), entry.getKey(), true));
                } else {
                    list.add(entry.getValue(), entry.getKey());
                }
            }

            return list;
        }

        public FormValidation doCheckPriority(@QueryParameter final String value) {
            if (Util.fixEmptyAndTrim(value) == null) {
                return FormValidation.error("Priority can't be empty");
            }

            if (!NotifyEventsService.getPriorities().containsKey(value)) {
                return FormValidation.error("Priority has invalid value");
            }

            return FormValidation.ok();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// Level
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public String getLevel() {
            return level;
        }

        @DataBoundSetter
        public void setLevel(final String level) {
            this.level = level;
        }

        public ListBoxModel doFillLevelItems(@QueryParameter("level") String level) {
            ListBoxModel list = new ListBoxModel();

            for (Map.Entry<String, String> entry : NotifyEventsService.getLevels().entrySet()) {
                if (entry.getKey().equals(level)) {
                    list.add(new ListBoxModel.Option(entry.getValue(), entry.getKey(), true));
                } else {
                    list.add(entry.getValue(), entry.getKey());
                }
            }

            return list;
        }

        public FormValidation doCheckLevel(@QueryParameter String value) {
            if (Util.fixEmptyAndTrim(value) == null) {
                return FormValidation.error("Level can't be empty");
            }

            if (!NotifyEventsService.getLevels().containsKey(value)) {
                return FormValidation.error("Level has invalid value");
            }

            return FormValidation.ok();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// Attachment
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public String getAttachment() {
            return attachment;
        }

        @DataBoundSetter
        public void setAttachment(final String attachment) {
            this.attachment = attachment;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        @NonNull
        public String getDisplayName() {
            return NotifyEventsService.NOTIFY_EVENTS_DISPLAY_NAME;
        }
    }
}
