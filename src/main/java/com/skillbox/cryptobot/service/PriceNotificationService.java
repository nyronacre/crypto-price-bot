package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PriceNotificationService {
    private final SubscriberRepository repository;
    private final CryptoCurrencyService currencyService;
    private final CryptoBot bot;
    private final long delayMillis;

    public PriceNotificationService(
            SubscriberRepository repository,
            CryptoCurrencyService currencyService,
            CryptoBot bot,
            @Value("${telegram.bot.notify.delay.value}") long delayValue,
            @Value("${telegram.bot.notify.delay.unit}") TimeUnit delayUnit) {
        this.repository = repository;
        this.currencyService = currencyService;
        this.bot = bot;
        this.delayMillis = delayUnit.toMillis(delayValue);
    }

    @Scheduled(fixedRateString = "${telegram.bot.notify.check.interval}")
    public void checkPrices() {
        log.info("Checking prices");
        try {
            double price = currencyService.getBitcoinPrice();
            repository.findByPriceGreaterThan(price).stream()
                    .filter(this::isDue)
                    .forEach(subscriber -> sendNotification(subscriber, price));
        } catch (IOException e) {
            log.error("Could not get bitcoin price", e);
        }
    }

    private boolean isDue(Subscriber subscriber) {
        LocalDateTime last = subscriber.getLastNotifiedAt();
        return last == null
                || Duration.between(last, LocalDateTime.now()).toMillis() >= delayMillis;
    }

    private void sendNotification(Subscriber subscriber, double price) {
        SendMessage message = new SendMessage();
        message.setChatId(subscriber.getTelegramId());
        message.setText("Пора покупать, стоимость биткоина " + TextUtil.toString(price));
        try {
            bot.execute(message);
            subscriber.setLastNotifiedAt(LocalDateTime.now());
            repository.save(subscriber);
        } catch (TelegramApiException e) {
            log.error("Could not notify {}", subscriber.getTelegramId(), e);
        }
    }
}