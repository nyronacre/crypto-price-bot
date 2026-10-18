# Crypto Price Bot

Telegram-бот, который следит за ценой биткоина (Binance) и уведомляет, когда цена опускается до желаемой.

## Команды

- `/start` — запуск бота, регистрация пользователя
- `/get_price` — текущая цена BTC в USD
- `/subscribe ЦЕНА` — подписка на цену, например `/subscribe 34600`
- `/get_subscription` — текущая подписка
- `/unsubscribe` — отмена подписки

Бот проверяет цену каждые 2 минуты и пишет «Пора покупать», если цена ниже желаемой. Одному пользователю не чаще раза в 10 минут. Интервалы настраиваются в `application.yml`.

## Стек

Java 17, Spring Boot 3.1, Spring Data JPA, PostgreSQL, Telegram Bots 6.8, Gradle, Docker.

## Запуск

1. Создайте бота у `@BotFather` и получите токен.
2. Поднимите базу: `docker compose up -d`
3. Создайте файл `src/main/resources/application-local.yml` (он в `.gitignore`):

```yaml
telegram:
  bot:
    username: ИМЯ_БОТА
    token: ТОКЕН
```

4. Запустите приложение с переменной окружения `SPRING_PROFILES_ACTIVE=local`.