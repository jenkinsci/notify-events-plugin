# Notify.Events [![Build Status](https://ci.jenkins.io/job/Plugins/job/notify-events-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/notify-events-plugin/job/master/)

Этот плагин позволят отправлять нотификации из **Jenkins** в:

- Telegram
- Viber
- SMS
- Slack
- Jabber / XMPP
- Discord
- Web Push
- Microsoft Teams
- Голосовые вызовы

Смотрите полный список поддерживаемых мессенджеров [по ссылке](https://notify.events/ru-RU/features).

#### Инструкция на других языках

- [English](../README.md)

## Установка

### Способ 1. Менеджер плагинов
1. Перейдите в **Manage Jenkins** > **Manage plugins** > **Available**.
2. Найдите в поиске **Notify.Events**.
3. Установите флажок рядом с плагином и нажмите **Install without restart**.

### Способ 2. Файл hpi
1. Скачайте последний релиз **notify-events.hpi** [здесь](http://archives.jenkins-ci.org/plugins/notify-events/latest/notify-events.hpi)
2. Установите плагин в ваш Jenkins. Читайте полную инструкцию по продвинутой установке плагинов [здесь](https://jenkins.io/doc/book/managing/plugins/#advanced-installation).

### Способ 3. Сборка из исходников
1. Для сборки плагина запустите `mvn install`. Это действие создаст файл **./target/notify-events.hpi**.
2. Установите плагин в ваш Jenkins. Читайте полную инструкцию по продвинутой установке плагинов [здесь](https://jenkins.io/doc/book/managing/plugins/#advanced-installation).

## Использование

### Подготовка канала в Notify.Events
1. Зарегистрируйтесь в сервисе [Notify.Events](https://notify.events/user/sign-in)
2. Создайте канал
3. Добавьте источник Jenkins в ваш канал
4. Скопируйте токен и сохраните интеграцию 

### Настройка сборки в Jenkins

#### Build step
1. Добавьте новый "Build" шаг
2. Заполните поле "Token" (см. "Подготовка канала в Notify.Events" - шаг 4)
3. Заполните поле "Message"

#### Post-build Actions
1. Добавьте новый "Post-build Action"
2. Заполните поле "Token" (см. "Подготовка канала в Notify.Events" - шаг 4)
3. Заполните поле "Message" (опционально)
4. Заполните поле "Send when"

#### Pipeline
```
notifyEvents message: 'Привет <b>мир</b>', token: '01234567890123456789012345678901'
```

### Форматирование
Вы можете использовать переменные окружения (включая Token-Macro) и простое HTML-форматирование (теги `<b>`, `<i>`, `<a>`) для полей "Title" и "Message".

Например:
```html
<b>$BUILD_ID</b> - Успешно собран
```