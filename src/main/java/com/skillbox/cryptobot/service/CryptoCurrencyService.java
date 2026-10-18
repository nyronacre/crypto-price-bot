package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.client.BinanceClient;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CryptoCurrencyService {
    private final BinanceClient client;

    public CryptoCurrencyService(BinanceClient client) {
        this.client = client;
    }

    public double getBitcoinPrice() throws IOException {
        return client.getBitcoinPrice();
    }
}