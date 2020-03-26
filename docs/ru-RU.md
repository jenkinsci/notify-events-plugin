# Notify.Events [![Build Status](https://ci.jenkins.io/job/Plugins/job/notify-events-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/notify-events-plugin/job/master/)

Этот плагин позволят отправлять нотификации из **Jenkins** в:

- Skype
- Telegram
- Viber
- SMS
- Slack
- Jabber / XMPP
- Discord
- Webpush
- Microsoft Teams
- Голосовой вызов

Вы можете найти полный список поддерживаемых мессенджеров [по ссылке](https://notify.events/ru-RU/features).

#### Инструкция на других языках

- [Русский](docs/ru-RU.md)

## Установка

### Менеджер плагинов
1. Перейдите в "Manage Jenkins" > "Manage plugins" > "Available"
2. Найдите и установите плагин **Notify.Events**

### Скачайте hpi
1. Скачайте релиз _notify-events-*.hpi_ [здесь](https://github.com/jenkinsci/notify-events-plugin/releases)
2. [Установите](https://jenkins.io/doc/book/managing/plugins/#advanced-installation) плагин в ваш Jenkins

### Сборка из исходников
1. Для сборки плагина запустите `mvn install`. Это создаст файл *./target/notify-events.hpi*
2. [Установите](https://jenkins.io/doc/book/managing/plugins/#advanced-installation) плагин в ваш Jenkins

## Использование

### Подготовка канала в Notify.Events
1. Зарегистрируйтесь в сервисе [Notify.Events](https://notify.events/user/sign-in)
2. Создайте канал
3. Добавьте источник Jenkins в ваш канал
4. Скопируйте токен и сохраните интеграцию 

### Настройка сборки в Jenkins

#### Build step
1. Добавьте новый "Build" шаг
2. Заполните поле "Token" (Подготовка канала в Notify.Events, шаг 4)
3. Заполните поле "Message"

#### Post-build Actions
1. Добавьте новый "Post-build Action"
2. Заполните поле "Token" (Подготовка канала в Notify.Events, шаг 4)
3. Заполните поле "Message" (опционально)
4. Заполните поле "Send when"
