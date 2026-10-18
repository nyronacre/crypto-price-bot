package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SubscriberService {
    private final SubscriberRepository repository;

    public SubscriberService(SubscriberRepository repository) {
        this.repository = repository;
    }

    public void registerIfAbsent(Long telegramId) {
        findOrCreate(telegramId);
    }

    public void setPrice(Long telegramId, Double price) {
        Subscriber subscriber = findOrCreate(telegramId);
        subscriber.setPrice(price);
        repository.save(subscriber);
    }

    public Double getPrice(Long telegramId) {
        return repository.findByTelegramId(telegramId)
                .map(Subscriber::getPrice)
                .orElse(null);
    }

    public boolean unsubscribe(Long telegramId) {
        Double current = getPrice(telegramId);
        if (current == null) {
            return false;
        }
        setPrice(telegramId, null);
        return true;
    }

    private Subscriber findOrCreate(Long telegramId) {
        return repository.findByTelegramId(telegramId).orElseGet(() -> {
            Subscriber subscriber = new Subscriber();
            subscriber.setId(UUID.randomUUID());
            subscriber.setTelegramId(telegramId);
            return repository.save(subscriber);
        });
    }
}