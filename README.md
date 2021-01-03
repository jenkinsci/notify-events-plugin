# Notify.Events [![Build Status](https://ci.jenkins.io/job/Plugins/job/notify-events-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/notify-events-plugin/job/master/)

This plugin allows **Jenkins** to send notification to:

- Skype
- Telegram
- Viber
- SMS
- Slack
- Jabber / XMPP
- Discord
- Webpush
- Microsoft Teams
- Voice call

You can find full supported messenger list [here](https://notify.events/en-US/features).

#### Instruction on another languages

- [Русский](docs/ru-RU.md)

## Installation

### Plugin Manager
1. Go to "Manage Jenkins" > "Manage plugins" > "Available"
2. Find and install **Notify.Events**

### Download an hpi
1. Download latest _notify-events.hpi_ release [here](http://archives.jenkins-ci.org/plugins/notify-events/latest/notify-events.hpi)
2. [Manually install](https://jenkins.io/doc/book/managing/plugins/#advanced-installation) plugin to your Jenkins

### Build from source
1. To build this plugin run `mvn install`. This will create file *./target/notify-events.hpi*
2. [Manually install](https://jenkins.io/doc/book/managing/plugins/#advanced-installation) plugin to your Jenkins

## Basic usage

### Create Notify.Events channel
1. Sign-up to [Notify.Events](https://notify.events/user/sign-in) service
2. Create new channel
3. Add Jenkins source to your channel
4. Copy your token and save integration 

### Jenkins build configuration

#### Build step
1. Add a build step
2. Fill the "Token" (Prepare Notify.Events channel, step 4)
3. Fill the "Message"

#### Post-build Actions
1. Add a post build step
2. Fill the "Token" (Prepare Notify.Events channel, step 4)
3. Fill the "Message" (optional)
4. Fill the "Send when"

#### Pipeline
```
notifyEvents message: 'Hello <b>world</b>', token: '01234567890123456789012345678901'
```

### Formatting

You can use environment variables (including Token-Macro) and simple html-formatting (`<b>`, `<i>`, `<a>` tags).

Example:
```html
<b>$BUILD_ID</b> - Built successfully
```
