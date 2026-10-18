package com.skillbox.cryptobot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "subscribers")
@Getter
@Setter
@NoArgsConstructor
public class Subscriber {
    @Id
    private UUID id;

    @Column(name = "telegram_id", unique = true, nullable = false)
    private Long telegramId;

    @Column(name = "price")
    private Double price;

    @Column(name = "last_notified_at")
    private LocalDateTime lastNotifiedAt;
}