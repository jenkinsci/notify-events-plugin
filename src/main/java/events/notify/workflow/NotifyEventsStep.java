package events.notify.workflow;

import com.google.common.collect.ImmutableSet;
import events.notify.NotifyEventsService;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import org.jenkinsci.plugins.workflow.steps.*;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class NotifyEventsStep extends Step {

    private Secret  token;
    private String  title;
    private String  message;
    private String  priority;
    private String  level;
    private Boolean attachBuildLog;
    private String  attachment;

    @Override
    public NotifyEventsStep.DescriptorImpl getDescriptor() {
        return (NotifyEventsStep.DescriptorImpl) super.getDescriptor();
    }

    @DataBoundConstructor
    public NotifyEventsStep() {
        this.token          = Secret.fromString("");
        this.title          = NotifyEventsStep.DescriptorImpl.DEFAULT_TITLE;
        this.message        = NotifyEventsStep.DescriptorImpl.DEFAULT_MESSAGE;
        this.priority       = NotifyEventsStep.DescriptorImpl.DEFAULT_PRIORITY;
        this.level          = NotifyEventsStep.DescriptorImpl.DEFAULT_LEVEL;
        this.attachBuildLog = NotifyEventsStep.DescriptorImpl.DEFAULT_ATTACH_BUILD_LOG;
        this.attachment     = NotifyEventsStep.DescriptorImpl.DEFAULT_ATTACHMENT;
    }

    public String getToken() {
        return token.getPlainText();
    }

    @DataBoundSetter
    public void setToken(final String token) {
        this.token = Secret.fromString(token);
    }

    public void setToken(final Secret token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    @DataBoundSetter
    public void setTitle(final String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    @DataBoundSetter
    public void setMessage(final String message) {
        this.message = message;
    }

    public String getPriority() {
        return priority;
    }

    @DataBoundSetter
    public void setPriority(final String priority) {
        this.priority = priority;
    }

    public String getLevel() {
        return level;
    }

    @DataBoundSetter
    public void setLevel(final String level) {
        this.level = level;
    }

    public Boolean getAttachBuildLog() {
        return attachBuildLog;
    }

    @DataBoundSetter
    public void setAttachBuildLog(final Boolean attachBuildLog) {
        this.attachBuildLog = attachBuildLog;
    }

    public String getAttachment() {
        return attachment;
    }

    @DataBoundSetter
    public void setAttachment(final String attachment) {
        this.attachment = attachment;
    }

    @Override
    public StepExecution start(StepContext context) {
        return new NotifyEventsStepExecution(this, context);
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {

        public final static String  DEFAULT_TITLE            = "$BUILD_TAG - Message";
        public final static String  DEFAULT_MESSAGE          = "";
        public final static String  DEFAULT_PRIORITY         = NotifyEventsService.PRIORITY_NORMAL;
        public final static String  DEFAULT_LEVEL            = NotifyEventsService.LEVEL_INFO;
        public final static Boolean DEFAULT_ATTACH_BUILD_LOG = false;
        public final static String  DEFAULT_ATTACHMENT       = "";

        private Secret  token;
        private String  title;
        private String  message;
        private String  priority;
        private String  level;
        private Boolean attachBuildLog;
        private String  attachment;

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

        public FormValidation doCheckToken(@QueryParameter String value) {
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

        public FormValidation doCheckMessage(@QueryParameter String value) {
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
        public ListBoxModel doFillPriorityItems(@QueryParameter("priority") String priority) {
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

        public ListBoxModel doFillLevelItems(@QueryParameter("level") final String level) {
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

        public FormValidation doCheckLevel(@QueryParameter final String value) {
            if (Util.fixEmptyAndTrim(value) == null) {
                return FormValidation.error("Level can't be empty");
            }

            if (!NotifyEventsService.getLevels().containsKey(value)) {
                return FormValidation.error("Level has invalid value");
            }

            return FormValidation.ok();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// Attach build log
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public Boolean getAttachBuildLog() {
            return attachBuildLog;
        }

        @DataBoundSetter
        public void setAttachBuildLog(final Boolean attachBuildLog) {
            this.attachBuildLog = attachBuildLog;
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
        public Set<? extends Class<?>> getRequiredContext() {
            return ImmutableSet.of(Run.class, TaskListener.class);
        }

        @Override
        public String getFunctionName() {
            return "notifyEvents";
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Send notification";
        }
    }

    public static class NotifyEventsStepExecution extends SynchronousNonBlockingStepExecution<Void> {

        private static final long serialVersionUID = 1L;

        private transient final NotifyEventsStep step;

        NotifyEventsStepExecution(NotifyEventsStep step, StepContext context) {
            super(context);

            this.step = step;
        }

        @Override
        protected Void run() throws Exception {
            final Run<?, ?> run         = getContext().get(Run.class);
            final FilePath workspace    = getContext().get(FilePath.class);
            final Launcher launcher     = getContext().get(Launcher.class);
            final TaskListener listener = getContext().get(TaskListener.class);

            Objects.requireNonNull(run, "Run is mandatory here");
            Objects.requireNonNull(listener, "Listener is mandatory here");

            NotifyEventsService.getInstance().send(step.token, step.title, step.message, step.priority, step.level, false, step.attachment, run, workspace, launcher, listener, null);

            return null;
        }
    }
}
