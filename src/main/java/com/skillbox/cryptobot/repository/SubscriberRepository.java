package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {
    Optional<Subscriber> findByTelegramId(Long telegramId);
}