package events.notify;

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

public class NotifyEventsPublisher extends Notifier implements SimpleBuildStep {

    private Secret  token;
    private String  title;
    private String  message;
    private Boolean attachBuildLog;
    private String  attachment;

    private boolean onSuccess;
    private boolean onSuccessCustom;
    private String  onSuccessCustomTitle;
    private String  onSuccessCustomMessage;
    private String  onSuccessCustomPriority;
    private String  onSuccessCustomLevel;
    private Boolean onSuccessCustomAttachBuildLog;
    private String  onSuccessCustomAttachment;

    private boolean onUnstable;
    private boolean onUnstableCustom;
    private String  onUnstableCustomTitle;
    private String  onUnstableCustomMessage;
    private String  onUnstableCustomPriority;
    private String  onUnstableCustomLevel;
    private Boolean onUnstableCustomAttachBuildLog;
    private String  onUnstableCustomAttachment;
    
    private boolean onFailure;
    private boolean onFailureCustom;
    private String  onFailureCustomTitle;
    private String  onFailureCustomMessage;
    private String  onFailureCustomPriority;
    private String  onFailureCustomLevel;
    private Boolean onFailureCustomAttachBuildLog;
    private String  onFailureCustomAttachment;

    private boolean onAborted;
    private boolean onAbortedCustom;
    private String  onAbortedCustomTitle;
    private String  onAbortedCustomMessage;
    private String  onAbortedCustomPriority;
    private String  onAbortedCustomLevel;
    private Boolean onAbortedCustomAttachBuildLog;
    private String  onAbortedCustomAttachment;

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Token
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getToken() {
        return token.getPlainText();
    }

    public void setToken(final String token) {
        this.token = Secret.fromString(token);
    }

    public void setToken(final Secret token) {
        this.token = token;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Title
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Message
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Attach build log
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Boolean getAttachBuildLog() {
        return attachBuildLog;
    }

    public void setAttachBuildLog(final Boolean attachBuildLog) {
        this.attachBuildLog = attachBuildLog;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Attachment
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(final String attachment) {
        this.attachment = attachment;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// OnSuccess
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean getOnSuccess() {
        return onSuccess;
    }

    public void setOnSuccess(final boolean onSuccess) {
        this.onSuccess = onSuccess;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// OnSuccessCustom
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean getOnSuccessCustom() {
        return onSuccessCustom;
    }

    public void setOnSuccessCustom(final boolean custom) {
        this.onSuccessCustom = custom;
    }

    public String getOnSuccessCustomTitle() {
        return onSuccessCustomTitle;
    }

    public void setOnSuccessCustomTitle(final String title) {
        this.onSuccessCustomTitle = title;
    }

    public String getOnSuccessCustomMessage() {
        return onSuccessCustomMessage;
    }

    public void setOnSuccessCustomMessage(final String message) {
        this.onSuccessCustomMessage = message;
    }

    public String getOnSuccessCustomPriority() {
        return onSuccessCustomPriority;
    }

    public void setOnSuccessCustomPriority(final String priority) {
        this.onSuccessCustomPriority = priority;
    }

    public String getOnSuccessCustomLevel() {
        return onSuccessCustomLevel;
    }

    public void setOnSuccessCustomLevel(final String level) {
        this.onSuccessCustomLevel = level;
    }

    public Boolean getOnSuccessCustomAttachBuildLog() {
        return onSuccessCustomAttachBuildLog;
    }

    public void setOnSuccessCustomAttachBuildLog(final Boolean onSuccessCustomAttachBuildLog) {
        this.onSuccessCustomAttachBuildLog = onSuccessCustomAttachBuildLog;
    }

    public String getOnSuccessCustomAttachment() {
        return onSuccessCustomAttachment;
    }

    public void setOnSuccessCustomAttachment(final String onSuccessCustomAttachment) {
        this.onSuccessCustomAttachment = onSuccessCustomAttachment;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// OnUnstable
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean getOnUnstable() {
        return onUnstable;
    }

    public void setOnUnstable(final boolean onUnstable) {
        this.onUnstable = onUnstable;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// OnUnstableCustom
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean getOnUnstableCustom() {
        return onUnstableCustom;
    }

    public void setOnUnstableCustom(final boolean custom) {
        this.onUnstableCustom = custom;
    }

    public String getOnUnstableCustomTitle() {
        return onUnstableCustomTitle;
    }

    public void setOnUnstableCustomTitle(final String title) {
        this.onUnstableCustomTitle = title;
    }

    public String getOnUnstableCustomMessage() {
        return onUnstableCustomMessage;
    }

    public void setOnUnstableCustomMessage(final String message) {
        this.onUnstableCustomMessage = message;
    }

    public String getOnUnstableCustomPriority() {
        return onUnstableCustomPriority;
    }

    public void setOnUnstableCustomPriority(final String priority) {
        this.onUnstableCustomPriority = priority;
    }

    public String getOnUnstableCustomLevel() {
        return onUnstableCustomLevel;
    }

    public void setOnUnstableCustomLevel(final String level) {
        this.onUnstableCustomLevel = level;
    }

    public Boolean getOnUnstableCustomAttachBuildLog() {
        return onUnstableCustomAttachBuildLog;
    }

    public void setOnUnstableCustomAttachBuildLog(final Boolean onUnstableCustomAttachBuildLog) {
        this.onUnstableCustomAttachBuildLog = onUnstableCustomAttachBuildLog;
    }

    public String getOnUnstableCustomAttachment() {
        return onUnstableCustomAttachment;
    }

    public void setOnUnstableCustomAttachment(final String onUnstableCustomAttachment) {
        this.onUnstableCustomAttachment = onUnstableCustomAttachment;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// OnFailure
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean getOnFailure() {
        return onFailure;
    }

    public void setOnFailure(final boolean onFailure) {
        this.onFailure = onFailure;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// OnFailureCustom
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean getOnFailureCustom() {
        return onFailureCustom;
    }

    public void setOnFailureCustom(final boolean custom) {
        this.onFailureCustom = custom;
    }

    public String getOnFailureCustomTitle() {
        return onFailureCustomTitle;
    }

    public void setOnFailureCustomTitle(final String title) {
        this.onFailureCustomTitle = title;
    }

    public String getOnFailureCustomMessage() {
        return onFailureCustomMessage;
    }

    public void setOnFailureCustomMessage(final String message) {
        this.onFailureCustomMessage = message;
    }

    public String getOnFailureCustomPriority() {
        return onFailureCustomPriority;
    }

    public void setOnFailureCustomPriority(final String priority) {
        this.onFailureCustomPriority = priority;
    }

    public String getOnFailureCustomLevel() {
        return onFailureCustomLevel;
    }

    public void setOnFailureCustomLevel(final String level) {
        this.onFailureCustomLevel = level;
    }

    public Boolean getOnFailureCustomAttachBuildLog() {
        return onFailureCustomAttachBuildLog;
    }

    public void setOnFailureCustomAttachBuildLog(final Boolean onFailureCustomAttachBuildLog) {
        this.onFailureCustomAttachBuildLog = onFailureCustomAttachBuildLog;
    }

    public String getOnFailureCustomAttachment() {
        return onFailureCustomAttachment;
    }

    public void setOnFailureCustomAttachment(final String onFailureCustomAttachment) {
        this.onFailureCustomAttachment = onFailureCustomAttachment;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// OnAborted
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean getOnAborted() {
        return onAborted;
    }

    public void setOnAborted(final boolean onAborted) {
        this.onAborted = onAborted;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// OnAbortedCustom
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean getOnAbortedCustom() {
        return onAbortedCustom;
    }

    public void setOnAbortedCustom(final boolean custom) {
        this.onAbortedCustom = custom;
    }

    public String getOnAbortedCustomTitle() {
        return onAbortedCustomTitle;
    }

    public void setOnAbortedCustomTitle(final String title) {
        this.onAbortedCustomTitle = title;
    }

    public String getOnAbortedCustomMessage() {
        return onAbortedCustomMessage;
    }

    public void setOnAbortedCustomMessage(final String message) {
        this.onAbortedCustomMessage = message;
    }

    public String getOnAbortedCustomPriority() {
        return onAbortedCustomPriority;
    }

    public void setOnAbortedCustomPriority(final String priority) {
        this.onAbortedCustomPriority = priority;
    }

    public String getOnAbortedCustomLevel() {
        return onAbortedCustomLevel;
    }

    public void setOnAbortedCustomLevel(final String level) {
        this.onAbortedCustomLevel = level;
    }

    public Boolean getOnAbortedCustomAttachBuildLog() {
        return onFailureCustomAttachBuildLog;
    }

    public void setOnAbortedCustomAttachBuildLog(final Boolean onAbortedCustomAttachBuildLog) {
        this.onAbortedCustomAttachBuildLog = onAbortedCustomAttachBuildLog;
    }

    public String getOnAbortedCustomAttachment() {
        return onAbortedCustomAttachment;
    }

    public void setOnAbortedCustomAttachment(final String onAbortedCustomAttachment) {
        this.onAbortedCustomAttachment = onAbortedCustomAttachment;
    }
    
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @DataBoundConstructor
    public NotifyEventsPublisher(
            final String token, final String title, final String message, final Boolean attachBuildLog, final String attachment,
            final boolean onSuccess, final boolean onSuccessCustom, final String onSuccessCustomTitle, final String onSuccessCustomMessage, final String onSuccessCustomPriority, final String onSuccessCustomLevel, final Boolean onSuccessCustomAttachBuildLog, final String onSuccessCustomAttachment,
            final boolean onUnstable, final boolean onUnstableCustom, final String onUnstableCustomTitle, final String onUnstableCustomMessage, final String onUnstableCustomPriority, final String onUnstableCustomLevel, final Boolean onUnstableCustomAttachBuildLog, final String onUnstableCustomAttachment,
            final boolean onFailure, final boolean onFailureCustom, final String onFailureCustomTitle, final String onFailureCustomMessage, final String onFailureCustomPriority, final String onFailureCustomLevel, final Boolean onFailureCustomAttachBuildLog, final String onFailureCustomAttachment,
            final boolean onAborted, final boolean onAbortedCustom, final String onAbortedCustomTitle, final String onAbortedCustomMessage, final String onAbortedCustomPriority, final String onAbortedCustomLevel, final Boolean onAbortedCustomAttachBuildLog, final String onAbortedCustomAttachment) {
        this.token          = Secret.fromString(Util.fixEmptyAndTrim(token));
        this.title          = Util.fixNull(title);
        this.message        = Util.fixNull(message);
        this.attachBuildLog = attachBuildLog;
        this.attachment     = Util.fixNull(attachment);

        this.onSuccess                     = onSuccess;
        this.onSuccessCustom               = onSuccessCustom;
        this.onSuccessCustomTitle          = Util.fixNull(onSuccessCustomTitle);
        this.onSuccessCustomMessage        = Util.fixNull(onSuccessCustomMessage);
        this.onSuccessCustomPriority       = Util.fixNull(onSuccessCustomPriority);
        this.onSuccessCustomLevel          = Util.fixNull(onSuccessCustomLevel);
        this.onSuccessCustomAttachBuildLog = onSuccessCustomAttachBuildLog;
        this.onSuccessCustomAttachment     = Util.fixNull(onSuccessCustomAttachment);

        this.onUnstable                     = onUnstable;
        this.onUnstableCustom               = onUnstableCustom;
        this.onUnstableCustomTitle          = Util.fixNull(onUnstableCustomTitle);
        this.onUnstableCustomMessage        = Util.fixNull(onUnstableCustomMessage);
        this.onUnstableCustomPriority       = Util.fixNull(onUnstableCustomPriority);
        this.onUnstableCustomLevel          = Util.fixNull(onUnstableCustomLevel);
        this.onUnstableCustomAttachBuildLog = onUnstableCustomAttachBuildLog;
        this.onUnstableCustomAttachment     = Util.fixNull(onUnstableCustomAttachment);

        this.onFailure                     = onFailure;
        this.onFailureCustom               = onFailureCustom;
        this.onFailureCustomTitle          = Util.fixNull(onFailureCustomTitle);
        this.onFailureCustomMessage        = Util.fixNull(onFailureCustomMessage);
        this.onFailureCustomPriority       = Util.fixNull(onFailureCustomPriority);
        this.onFailureCustomAttachBuildLog = onFailureCustomAttachBuildLog;
        this.onFailureCustomAttachment     = Util.fixNull(onFailureCustomAttachment);

        this.onAborted                     = onAborted;
        this.onAbortedCustom               = onAbortedCustom;
        this.onAbortedCustomTitle          = Util.fixNull(onAbortedCustomTitle);
        this.onAbortedCustomMessage        = Util.fixNull(onAbortedCustomMessage);
        this.onAbortedCustomPriority       = Util.fixNull(onAbortedCustomPriority);
        this.onAbortedCustomLevel          = Util.fixNull(onAbortedCustomLevel);
        this.onAbortedCustomAttachBuildLog = onAbortedCustomAttachBuildLog;
        this.onAbortedCustomAttachment     = Util.fixNull(onAbortedCustomAttachment);
    }

    @Override
    public void perform(
            @Nonnull Run<?, ?> run,
            @Nonnull FilePath filePath,
            @Nonnull Launcher launcher,
            @Nonnull TaskListener taskListener) throws InterruptedException, IOException {

        Result result = run.getResult();

        if (!(((result == Result.SUCCESS) && onSuccess)
                || ((result == Result.UNSTABLE) && onUnstable)
                || ((result == Result.FAILURE) && onFailure)
                || ((result == Result.ABORTED) && onAborted))) {
            return;
        }

        String  title          = this.title;
        String  message        = this.message;
        Boolean attachBuildLog = this.attachBuildLog;
        String  attachment     = this.attachment;

        String priority;
        String level;

        if ((result == Result.SUCCESS) && onSuccess) {
            if (onSuccessCustom) {
                title          = onSuccessCustomTitle;
                message        = onSuccessCustomMessage;
                priority       = onSuccessCustomPriority;
                level          = onSuccessCustomLevel;
                attachBuildLog = onSuccessCustomAttachBuildLog;
                attachment     = onSuccessCustomAttachment;
            } else {
                priority = NotifyEventsService.PRIORITY_NORMAL;
                level    = NotifyEventsService.LEVEL_SUCCESS;
            }
        } else if ((result == Result.UNSTABLE) && onUnstable) {
            if (onUnstableCustom) {
                title          = onUnstableCustomTitle;
                message        = onUnstableCustomMessage;
                priority       = onUnstableCustomPriority;
                level          = onUnstableCustomLevel;
                attachBuildLog = onUnstableCustomAttachBuildLog;
                attachment     = onUnstableCustomAttachment;
            } else {
                priority = NotifyEventsService.PRIORITY_HIGH;
                level    = NotifyEventsService.LEVEL_WARNING;
            }
        } else if ((result == Result.FAILURE) && onFailure) {
            if (onFailureCustom) {
                title          = onFailureCustomTitle;
                message        = onFailureCustomMessage;
                priority       = onFailureCustomPriority;
                level          = onFailureCustomLevel;
                attachBuildLog = onFailureCustomAttachBuildLog;
                attachment     = onFailureCustomAttachment;
            } else {
                priority = NotifyEventsService.PRIORITY_HIGHEST;
                level    = NotifyEventsService.LEVEL_ERROR;
            }
        } else /*if ((result == Result.ABORTED) && onAborted)*/ {
            if (onAbortedCustom) {
                title          = onAbortedCustomTitle;
                message        = onAbortedCustomMessage;
                priority       = onAbortedCustomPriority;
                level          = onAbortedCustomLevel;
                attachBuildLog = onAbortedCustomAttachBuildLog;
                attachment     = onAbortedCustomAttachment;
            } else {
                priority = NotifyEventsService.PRIORITY_NORMAL;
                level    = NotifyEventsService.LEVEL_INFO;
            }
        }

        NotifyEventsService.getInstance().send(token, title, message, priority, level, attachBuildLog, attachment, run, filePath, launcher, taskListener, null);
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        public static final String DEFAULT_TITLE                = "$PROJECT_NAME: #$BUILD_NUMBER - $BUILD_STATUS";
        public static final String DEFAULT_MESSAGE              = "Build <a href=\"$PROJECT_URL\">$PROJECT_NAME</a>: #<a href=\"$BUILD_URL\">$BUILD_NUMBER</a> result with status: <b>$BUILD_STATUS</b>\n\n<a href=\"$BUILD_URL/console\">Build log</a>";
        public static final String DEFAULT_ATTACHMENT           = "";

        public static final String DEFAULT_ON_SUCCESS_TITLE      = "$PROJECT_NAME: #$BUILD_NUMBER - $BUILD_STATUS";
        public static final String DEFAULT_ON_SUCCESS_MESSAGE    = "Build <a href=\"$PROJECT_URL\">$PROJECT_NAME</a>: #<a href=\"$BUILD_URL\">$BUILD_NUMBER</a> result with status: <b>$BUILD_STATUS</b>\n\n<a href=\"$BUILD_URL/console\">Build log</a>";
        public static final String DEFAULT_ON_SUCCESS_PRIORITY   = NotifyEventsService.PRIORITY_NORMAL;
        public static final String DEFAULT_ON_SUCCESS_LEVEL      = NotifyEventsService.LEVEL_SUCCESS;
        public static final String DEFAULT_ON_SUCCESS_ATTACHMENT = "";

        public static final String DEFAULT_ON_UNSTABLE_TITLE      = "$PROJECT_NAME: #$BUILD_NUMBER - $BUILD_STATUS";
        public static final String DEFAULT_ON_UNSTABLE_MESSAGE    = "Build <a href=\"$PROJECT_URL\">$PROJECT_NAME</a>: #<a href=\"$BUILD_URL\">$BUILD_NUMBER</a> result with status: <b>$BUILD_STATUS</b>\n\n<a href=\"$BUILD_URL/console\">Build log</a>";
        public static final String DEFAULT_ON_UNSTABLE_PRIORITY   = NotifyEventsService.PRIORITY_HIGH;
        public static final String DEFAULT_ON_UNSTABLE_LEVEL      = NotifyEventsService.LEVEL_WARNING;
        public static final String DEFAULT_ON_UNSTABLE_ATTACHMENT = "";

        public static final String DEFAULT_ON_FAILURE_TITLE      = "$PROJECT_NAME: #$BUILD_NUMBER - $BUILD_STATUS";
        public static final String DEFAULT_ON_FAILURE_MESSAGE    = "Build <a href=\"$PROJECT_URL\">$PROJECT_NAME</a>: #<a href=\"$BUILD_URL\">$BUILD_NUMBER</a> result with status: <b>$BUILD_STATUS</b>\n\n<a href=\"$BUILD_URL/console\">Build log</a>";
        public static final String DEFAULT_ON_FAILURE_PRIORITY   = NotifyEventsService.PRIORITY_HIGHEST;
        public static final String DEFAULT_ON_FAILURE_LEVEL      = NotifyEventsService.LEVEL_ERROR;
        public static final String DEFAULT_ON_FAILURE_ATTACHMENT = "";

        public static final String DEFAULT_ON_ABORTED_TITLE      = "$PROJECT_NAME: #$BUILD_NUMBER - $BUILD_STATUS";
        public static final String DEFAULT_ON_ABORTED_MESSAGE    = "Build <a href=\"$PROJECT_URL\">$PROJECT_NAME</a>: #<a href=\"$BUILD_URL\">$BUILD_NUMBER</a> result with status: <b>$BUILD_STATUS</b>\n\n<a href=\"$BUILD_URL/console\">Build log</a>";
        public static final String DEFAULT_ON_ABORTED_PRIORITY   = NotifyEventsService.PRIORITY_NORMAL;
        public static final String DEFAULT_ON_ABORTED_LEVEL      = NotifyEventsService.LEVEL_INFO;
        public static final String DEFAULT_ON_ABORTED_ATTACHMENT = "";

        private Secret token;
        private String title;
        private String message;
        private String attachment;

        private boolean onSuccess;
        private boolean onSuccessCustom;
        private String  onSuccessCustomTitle;
        private String  onSuccessCustomMessage;
        private String  onSuccessCustomPriority;
        private String  onSuccessCustomLevel;
        private String  onSuccessCustomAttachment;

        private boolean onUnstable;
        private boolean onUnstableCustom;
        private String  onUnstableCustomTitle;
        private String  onUnstableCustomMessage;
        private String  onUnstableCustomPriority;
        private String  onUnstableCustomLevel;
        private String  onUnstableCustomAttachment;

        private boolean onFailure;
        private boolean onFailureCustom;
        private String  onFailureCustomTitle;
        private String  onFailureCustomMessage;
        private String  onFailureCustomPriority;
        private String  onFailureCustomLevel;
        private String  onFailureCustomAttachment;

        private boolean onAborted;
        private boolean onAbortedCustom;
        private String  onAbortedCustomTitle;
        private String  onAbortedCustomMessage;
        private String  onAbortedCustomPriority;
        private String  onAbortedCustomLevel;
        private String  onAbortedCustomAttachment;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// Token
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public String getToken() {
            return token.getPlainText();
        }

        @DataBoundSetter
        public void setToken(
                @QueryParameter("token") final String token) {

            this.token = Secret.fromString(token);
        }

        @DataBoundSetter
        public void setToken(final Secret token) {
            this.token = token;
        }

        public FormValidation doCheckToken(
                @QueryParameter("token") final String token) {

            if (Util.fixEmptyAndTrim(token) == null) {
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
        public void setTitle(
                @QueryParameter("title") final String title) {

            this.title = title;
        }

        public FormValidation doCheckTitle(
                @QueryParameter("title") final String title) {

            if (Util.fixEmptyAndTrim(title) == null) {
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
        public void setMessage(
                @QueryParameter("message") final String message) {

            this.message = message;
        }

        public FormValidation doCheckMessage(
                @QueryParameter("message") final String message) {

            if (Util.fixEmptyAndTrim(message) == null) {
                return FormValidation.error("Message can't be empty");
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
        public void setAttachment(
                @QueryParameter("attachment") final String attachment) {

            this.attachment = attachment;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// OnSuccess
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public boolean getOnSuccess() {
            return onSuccess;
        }

        @DataBoundSetter
        public void setOnSuccess(
                @QueryParameter("onSuccess") final Boolean onSuccess) {

            this.onSuccess = onSuccess;
        }

        public FormValidation doCheckOnSuccess(
                @QueryParameter("onSuccess") final Boolean onSuccess,
                @QueryParameter("onUnstable") final Boolean onUnstable,
                @QueryParameter("onFailure") final Boolean onFailure,
                @QueryParameter("onAborted") final Boolean onAborted) {

            if (!onSuccess && !onUnstable && !onFailure && !onAborted) {
                return FormValidation.error("Send when can't be empty");
            }

            return FormValidation.ok();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// OnSuccessCustom
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public boolean getOnSuccessCustom() {
            return onSuccessCustom;
        }

        public void setOnSuccessCustom(
                @QueryParameter("onSuccessCustom") final Boolean onSuccessCustom) {

            this.onSuccessCustom = onSuccessCustom;
        }

        public String getOnSuccessCustomTitle() {
            return onSuccessCustomTitle;
        }

        public void setOnSuccessCustomTitle(
                @QueryParameter("onSuccessCustomTitle") final String onSuccessCustomTitle) {

            this.onSuccessCustomTitle = onSuccessCustomTitle;
        }

        public FormValidation doCheckOnSuccessCustomTitle(
                @QueryParameter("onSuccessCustomTitle") final String onSuccessCustomTitle,
                @QueryParameter("onSuccess") final Boolean onSuccess,
                @QueryParameter("onSuccessCustom") final Boolean onSuccessCustom) {

            if (!onSuccess || !onSuccessCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onSuccessCustomTitle) == null) {
                return FormValidation.error("Title can't be empty");
            }

            return FormValidation.ok();
        }

        public String getOnSuccessCustomMessage() {
            return onSuccessCustomMessage;
        }

        public void setOnSuccessCustomMessage(
                @QueryParameter("onSuccessCustomMessage") final String onSuccessCustomMessage) {

            this.onSuccessCustomMessage = onSuccessCustomMessage;
        }

        public FormValidation doCheckOnSuccessCustomMessage(
                @QueryParameter("onSuccessCustomMessage") final String onSuccessCustomMessage,
                @QueryParameter("onSuccess") final Boolean onSuccess,
                @QueryParameter("onSuccessCustom") final Boolean onSuccessCustom) {

            if (!onSuccess || !onSuccessCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onSuccessCustomMessage) == null) {
                return FormValidation.error("Message can't be empty");
            }

            return FormValidation.ok();
        }

        public String getOnSuccessCustomPriority() {
            return onSuccessCustomPriority;
        }

        public void setOnSuccessCustomPriority(
                @QueryParameter("onSuccessCustomPriority") final String onSuccessCustomPriority) {

            this.onSuccessCustomPriority = onSuccessCustomPriority;
        }

        @Restricted(NoExternalUse.class)
        public ListBoxModel doFillOnSuccessCustomPriorityItems(
                @QueryParameter("onSuccessCustomPriority") final String onSuccessCustomPriority) {

            return doFillPriorityItems(onSuccessCustomPriority);
        }

        public FormValidation doCheckOnSuccessCustomPriority(
                @QueryParameter("onSuccessCustomPriority") final String onSuccessCustomPriority,
                @QueryParameter("onSuccess") final Boolean onSuccess,
                @QueryParameter("onSuccessCustom") final Boolean onSuccessCustom) {

            if (!onSuccess || !onSuccessCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onSuccessCustomPriority) == null) {
                return FormValidation.error("Priority can't be empty");
            }

            if (!NotifyEventsService.getPriorities().containsKey(onSuccessCustomPriority)) {
                return FormValidation.error("Priority has invalid value");
            }

            return FormValidation.ok();
        }

        public String getOnSuccessCustomLevel() {
            return onSuccessCustomLevel;
        }

        public void setOnSuccessCustomLevel(
                @QueryParameter("onSuccessCustomLevel") final String onSuccessCustomLevel) {

            this.onSuccessCustomLevel = onSuccessCustomLevel;
        }

        @Restricted(NoExternalUse.class)
        public ListBoxModel doFillOnSuccessCustomLevelItems(
                @QueryParameter("onSuccessCustomLevel") final String onSuccessCustomLevel) {

            return doFillLevelItems(onSuccessCustomLevel);
        }

        public FormValidation doCheckOnSuccessCustomLevel(
                @QueryParameter("onSuccessCustomLevel") final String onSuccessCustomLevel,
                @QueryParameter("onSuccess") final Boolean onSuccess,
                @QueryParameter("onSuccessCustom") final Boolean onSuccessCustom) {

            if (!onSuccess || !onSuccessCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onSuccessCustomLevel) == null) {
                return FormValidation.error("Level can't be empty");
            }

            if (!NotifyEventsService.getLevels().containsKey(onSuccessCustomLevel)) {
                return FormValidation.error("Level has invalid value");
            }

            return FormValidation.ok();
        }

        public String getOnSuccessCustomAttachment() {
            return onSuccessCustomAttachment;
        }

        @DataBoundSetter
        public void setOnSuccessCustomAttachment(
                @QueryParameter("onSuccessCustomAttachment") final String onSuccessCustomAttachment) {

            this.onSuccessCustomAttachment = onSuccessCustomAttachment;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// OnUnstable
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public boolean getOnUnstable() {
            return onUnstable;
        }

        @DataBoundSetter
        public void setOnUnstable(
                @QueryParameter("onUnstable") final Boolean onUnstable) {

            this.onUnstable = onUnstable;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// OnUnstableCustom
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public boolean getOnUnstableCustom() {
            return onUnstableCustom;
        }

        public void setOnUnstableCustom(
                @QueryParameter("onUnstableCustom") final Boolean onUnstableCustom) {

            this.onUnstableCustom = onUnstableCustom;
        }

        public String getOnUnstableCustomTitle() {
            return onUnstableCustomTitle;
        }

        public void setOnUnstableCustomTitle(
                @QueryParameter("onUnstableCustomTitle") final String onUnstableCustomTitle) {

            this.onUnstableCustomTitle = onUnstableCustomTitle;
        }

        public FormValidation doCheckOnUnstableCustomTitle(
                @QueryParameter("onUnstableCustomTitle") final String onUnstableCustomTitle,
                @QueryParameter("onUnstable") final Boolean onUnstable,
                @QueryParameter("onUnstableCustom") final Boolean onUnstableCustom) {

            if (!onUnstable || !onUnstableCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onUnstableCustomTitle) == null) {
                return FormValidation.error("Title can't be empty");
            }

            return FormValidation.ok();
        }

        public String getOnUnstableCustomMessage() {
            return onUnstableCustomMessage;
        }

        public void setOnUnstableCustomMessage(
                @QueryParameter("onUnstableCustomMessage") final String onUnstableCustomMessage) {

            this.onUnstableCustomMessage = onUnstableCustomMessage;
        }

        public FormValidation doCheckOnUnstableCustomMessage(
                @QueryParameter("onUnstableCustomMessage") final String onUnstableCustomMessage,
                @QueryParameter("onUnstable") final Boolean onUnstable,
                @QueryParameter("onUnstableCustom") final Boolean onUnstableCustom) {

            if (!onUnstable || !onUnstableCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onUnstableCustomMessage) == null) {
                return FormValidation.error("Message can't be empty");
            }

            return FormValidation.ok();
        }

        public String getOnUnstableCustomPriority() {
            return onUnstableCustomPriority;
        }

        public void setOnUnstableCustomPriority(
                @QueryParameter("onUnstableCustomPriority") final String onUnstableCustomPriority) {

            this.onUnstableCustomPriority = onUnstableCustomPriority;
        }

        @Restricted(NoExternalUse.class)
        public ListBoxModel doFillOnUnstableCustomPriorityItems(
                @QueryParameter("onUnstableCustomPriority") final String onUnstableCustomPriority) {

            return doFillPriorityItems(onUnstableCustomPriority);
        }

        public FormValidation doCheckOnUnstableCustomPriority(
                @QueryParameter("onUnstableCustomPriority") final String onUnstableCustomPriority,
                @QueryParameter("onUnstable") final Boolean onUnstable,
                @QueryParameter("onUnstableCustom") final Boolean onUnstableCustom) {

            if (!onUnstable || !onUnstableCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onUnstableCustomPriority) == null) {
                return FormValidation.error("Priority can't be empty");
            }

            if (!NotifyEventsService.getPriorities().containsKey(onUnstableCustomPriority)) {
                return FormValidation.error("Priority has invalid value");
            }

            return FormValidation.ok();
        }

        public String getOnUnstableCustomLevel() {
            return onUnstableCustomLevel;
        }

        public void setOnUnstableCustomLevel(
                @QueryParameter("onUnstableCustomLevel") final String onUnstableCustomLevel) {

            this.onUnstableCustomLevel = onUnstableCustomLevel;
        }

        @Restricted(NoExternalUse.class)
        public ListBoxModel doFillOnUnstableCustomLevelItems(
                @QueryParameter("onUnstableCustomLevel") final String onUnstableCustomLevel) {

            return doFillLevelItems(onUnstableCustomLevel);
        }

        public FormValidation doCheckOnUnstableCustomLevel(
                @QueryParameter("onUnstableCustomLevel") final String onUnstableCustomLevel,
                @QueryParameter("onUnstable") final Boolean onUnstable,
                @QueryParameter("onUnstableCustom") final Boolean onUnstableCustom) {

            if (!onUnstable || !onUnstableCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onUnstableCustomLevel) == null) {
                return FormValidation.error("Level can't be empty");
            }

            if (!NotifyEventsService.getLevels().containsKey(onUnstableCustomLevel)) {
                return FormValidation.error("Level has invalid value");
            }

            return FormValidation.ok();
        }

        public String getOnUnstableCustomAttachment() {
            return onUnstableCustomAttachment;
        }

        @DataBoundSetter
        public void setOnUnstableCustomAttachment(
                @QueryParameter("onUnstableCustomAttachment") final String onUnstableCustomAttachment) {

            this.onUnstableCustomAttachment = onUnstableCustomAttachment;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// OnFailure
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public boolean getOnFailure() {
            return onFailure;
        }

        public void setOnFailure(
                @QueryParameter("onFailure") final Boolean onFailure) {

            this.onFailure = onFailure;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// OnFailureCustom
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public boolean getOnFailureCustom() {
            return onFailureCustom;
        }

        public void setOnFailureCustom(
                @QueryParameter("onFailureCustom") final Boolean onFailureCustom) {

            this.onFailureCustom = onFailureCustom;
        }

        public String getOnFailureCustomTitle() {
            return onFailureCustomTitle;
        }

        public void setOnFailureCustomTitle(
                @QueryParameter("onFailureCustomTitle") final String onFailureCustomTitle) {

            this.onFailureCustomTitle = onFailureCustomTitle;
        }

        public FormValidation doCheckOnFailureCustomTitle(
                @QueryParameter("onFailureCustomTitle") final String onFailureCustomTitle,
                @QueryParameter("onFailure") final Boolean onFailure,
                @QueryParameter("onFailureCustom") final Boolean onFailureCustom) {

            if (!onFailure || !onFailureCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onFailureCustomTitle) == null) {
                return FormValidation.error("Title can't be empty");
            }

            return FormValidation.ok();
        }

        public String getOnFailureCustomMessage() {
            return onFailureCustomMessage;
        }

        public void setOnFailureCustomMessage(
                @QueryParameter("onFailureCustomMessage") final String onFailureCustomMessage) {

            this.onFailureCustomMessage = onFailureCustomMessage;
        }

        public FormValidation doCheckOnFailureCustomMessage(
                @QueryParameter("onFailureCustomMessage") final String onFailureCustomMessage,
                @QueryParameter("onFailure") final Boolean onFailure,
                @QueryParameter("onFailureCustom") final Boolean onFailureCustom) {

            if (!onFailure || !onFailureCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onFailureCustomMessage) == null) {
                return FormValidation.error("Message can't be empty");
            }

            return FormValidation.ok();
        }

        public String getOnFailureCustomPriority() {
            return onFailureCustomPriority;
        }

        public void setOnFailureCustomPriority(
                @QueryParameter("onFailureCustomPriority") final String onFailureCustomPriority) {

            this.onFailureCustomPriority = onFailureCustomPriority;
        }

        @Restricted(NoExternalUse.class)
        public ListBoxModel doFillOnFailureCustomPriorityItems(
                @QueryParameter("onFailureCustomPriority") final String onFailureCustomPriority) {

            return doFillPriorityItems(onFailureCustomPriority);
        }

        public FormValidation doCheckOnFailureCustomPriority(
                @QueryParameter("onFailureCustomPriority") final String onFailureCustomPriority,
                @QueryParameter("onFailure") final Boolean onFailure,
                @QueryParameter("onFailureCustom") final Boolean onFailureCustom) {

            if (!onFailure || !onFailureCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onFailureCustomPriority) == null) {
                return FormValidation.error("Priority can't be empty");
            }

            if (!NotifyEventsService.getPriorities().containsKey(onFailureCustomPriority)) {
                return FormValidation.error("Priority has invalid value");
            }

            return FormValidation.ok();
        }

        public String getOnFailureCustomLevel() {
            return onFailureCustomLevel;
        }

        public void setOnFailureCustomLevel(
                @QueryParameter("onFailureCustomLevel") final String onFailureCustomLevel) {

            this.onFailureCustomLevel = onFailureCustomLevel;
        }

        @Restricted(NoExternalUse.class)
        public ListBoxModel doFillOnFailureCustomLevelItems(
                @QueryParameter("onFailureCustomLevel") final String onFailureCustomLevel) {

            return doFillLevelItems(onFailureCustomLevel);
        }

        public FormValidation doCheckOnFailureCustomLevel(
                @QueryParameter("onFailureCustomLevel") final String onFailureCustomLevel,
                @QueryParameter("onFailure") final Boolean onFailure,
                @QueryParameter("onFailureCustom") final Boolean onFailureCustom) {

            if (!onFailure || !onFailureCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onFailureCustomLevel) == null) {
                return FormValidation.error("Level can't be empty");
            }

            if (!NotifyEventsService.getLevels().containsKey(onFailureCustomLevel)) {
                return FormValidation.error("Level has invalid value");
            }

            return FormValidation.ok();
        }

        public String getOnFailureCustomAttachment() {
            return onFailureCustomAttachment;
        }

        @DataBoundSetter
        public void setOnFailureCustomAttachment(
                @QueryParameter("onFailureCustomAttachment") final String onFailureCustomAttachment) {

            this.onFailureCustomAttachment = onFailureCustomAttachment;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// OnAborted
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public boolean getOnAborted() {
            return onAborted;
        }

        public void setOnAborted(
                @QueryParameter("onAborted") final Boolean onAborted) {

            this.onAborted = onAborted;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// OnAbortedCustom
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public boolean getOnAbortedCustom() {
            return onAbortedCustom;
        }

        public void setOnAbortedCustom(
                @QueryParameter("onAbortedCustom") final Boolean onAbortedCustom) {

            this.onAbortedCustom = onAbortedCustom;
        }

        public String getOnAbortedCustomTitle() {
            return onAbortedCustomTitle;
        }

        public void setOnAbortedCustomTitle(
                @QueryParameter("onAbortedCustomTitle") final String onAbortedCustomTitle) {

            this.onAbortedCustomTitle = onAbortedCustomTitle;
        }

        public FormValidation doCheckOnAbortedCustomTitle
                (@QueryParameter("onAbortedCustomTitle") final String onAbortedCustomTitle,
                 @QueryParameter("onAborted") final Boolean onAborted,
                 @QueryParameter("onAbortedCustom") final Boolean onAbortedCustom) {

            if (!onAborted || !onAbortedCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onAbortedCustomTitle) == null) {
                return FormValidation.error("Title can't be empty");
            }

            return FormValidation.ok();
        }

        public String getOnAbortedCustomMessage() {
            return onAbortedCustomMessage;
        }

        public void setOnAbortedCustomMessage(
                @QueryParameter("onAbortedCustomMessage") final String onAbortedCustomMessage) {

            this.onAbortedCustomMessage = onAbortedCustomMessage;
        }

        public FormValidation doCheckOnAbortedCustomMessage(
                @QueryParameter("onAbortedCustomMessage") final String onAbortedCustomMessage,
                @QueryParameter("onAborted") final Boolean onAborted,
                @QueryParameter("onAbortedCustom") final Boolean onAbortedCustom) {

            if (!onAborted || !onAbortedCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onAbortedCustomMessage) == null) {
                return FormValidation.error("Message can't be empty");
            }

            return FormValidation.ok();
        }

        public String getOnAbortedCustomPriority() {
            return onAbortedCustomPriority;
        }

        public void setOnAbortedCustomPriority(
                @QueryParameter("onAbortedCustomPriority") final String onAbortedCustomPriority) {

            this.onAbortedCustomPriority = onAbortedCustomPriority;
        }

        @Restricted(NoExternalUse.class)
        public ListBoxModel doFillOnAbortedCustomPriorityItems(
                @QueryParameter("onAbortedCustomPriority") final String priority) {

            return doFillPriorityItems(priority);
        }

        public FormValidation doCheckOnAbortedCustomPriority(
                @QueryParameter("onAbortedCustomPriority") final String onAbortedCustomPriority,
                @QueryParameter("onAborted") final Boolean onAborted,
                @QueryParameter("onAbortedCustom") final Boolean onAbortedCustom) {

            if (!onAborted || !onAbortedCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onAbortedCustomPriority) == null) {
                return FormValidation.error("Priority can't be empty");
            }

            if (!NotifyEventsService.getPriorities().containsKey(onAbortedCustomPriority)) {
                return FormValidation.error("Priority has invalid value");
            }

            return FormValidation.ok();
        }

        public String getOnAbortedCustomLevel() {
            return onAbortedCustomLevel;
        }

        public void setOnAbortedCustomLevel(
                @QueryParameter("onAbortedCustomLevel") final String onAbortedCustomLevel) {

            this.onAbortedCustomLevel = onAbortedCustomLevel;
        }

        @Restricted(NoExternalUse.class)
        public ListBoxModel doFillOnAbortedCustomLevelItems(
                @QueryParameter("onAbortedCustomLevel") final String onAbortedCustomLevel) {

            return doFillLevelItems(onAbortedCustomLevel);
        }

        public FormValidation doCheckOnAbortedCustomLevel(
                @QueryParameter("onAbortedCustomLevel") final String onAbortedCustomLevel,
                @QueryParameter("onAborted") final Boolean onAborted,
                @QueryParameter("onAbortedCustom") final Boolean onAbortedCustom) {

            if (!onAborted || !onAbortedCustom) {
                return FormValidation.ok();
            }

            if (Util.fixEmptyAndTrim(onAbortedCustomLevel) == null) {
                return FormValidation.error("Level can't be empty");
            }

            if (!NotifyEventsService.getLevels().containsKey(onAbortedCustomLevel)) {
                return FormValidation.error("Level has invalid value");
            }

            return FormValidation.ok();
        }

        public String getOnAbortedCustomAttachment() {
            return onAbortedCustomAttachment;
        }

        @DataBoundSetter
        public void setOnAbortedCustomAttachment(
                @QueryParameter("onAbortedCustomAttachment") final String onAbortedCustomAttachment) {

            this.onAbortedCustomAttachment = onAbortedCustomAttachment;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        private ListBoxModel doFillPriorityItems(final String priority) {
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

        private ListBoxModel doFillLevelItems(final String level) {
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
