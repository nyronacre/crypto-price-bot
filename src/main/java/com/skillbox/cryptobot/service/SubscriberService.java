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
        if (repository.findByTelegramId(telegramId).isEmpty()) {
            Subscriber subscriber = new Subscriber();
            subscriber.setId(UUID.randomUUID());
            subscriber.setTelegramId(telegramId);
            repository.save(subscriber);
        }
    }
}