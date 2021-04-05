# Notify.Events [![Build Status](https://ci.jenkins.io/job/Plugins/job/notify-events-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/notify-events-plugin/job/master/)

This plugin allows **Jenkins** to send notification to:

- Telegram
- Viber
- SMS
- Slack
- Jabber / XMPP
- Discord
- Web Push
- Microsoft Teams
- Voice calls

See the full list of supported messengers list [here](https://notify.events/en-US/features).

#### Read the manual in other languages

- [Русский](docs/ru-RU.md)

## Installation

### Option 1. Plugin Manager
1. Go to **Manage Jenkins** > **Manage plugins** > **Available**.
2. Search for **Notify.Events**.
3. Check the box next to the found plugin and click **Install without restart**.

### Option 2. Downloading a .hpi file
1. Download the latest **notify-events.hpi** release [here](http://archives.jenkins-ci.org/plugins/notify-events/latest/notify-events.hpi).
2. Manually install the plugin on the Jenkins controller. Read full instructions for advanced plugin installation [here](https://jenkins.io/doc/book/managing/plugins/#advanced-installation).

### Option 3. Building from the source
1. To build the plugin, run `mvn install`. This will create the file **./target/notify-events.hpi** file.
2. Manually install the plugin on the Jenkins controller. Read full instructions for advanced plugin installation [here](https://jenkins.io/doc/book/managing/plugins/#advanced-installation).

## Basic usage

### Createing a channel in Notify.Events
1. Sign-up to the [Notify.Events](https://notify.events/user/sign-in) service.
2. Create a new channel.
3. Add Jenkins as a source to your channel.
4. Copy your token and save the integration.

### Jenkins build configuration

#### Build Step
1. Add a build step.
2. Paste the copied **Token** (see Create a Notify.Events channel, step 4).
3. Fill in the **Message** field.

#### Post-build Actions
1. Add a post-build step.
2. Paste the copied **Token** (see Create a Notify.Events channel, step 4).
3. Fill in the **Message** (optional).
4. Fill in the **Send when** field.

#### Pipeline
```
notifyEvents message: 'Hello <b>world</b>', token: '01234567890123456789012345678901'
```

### Formatting

You can use environment variables (including Token-Macro) and simple HTML formatting (`<b>`, `<i>`, `<a>` tags) for the Title and Message fields.

Example:
```html
<b>$BUILD_ID</b> - Built successfully
```
