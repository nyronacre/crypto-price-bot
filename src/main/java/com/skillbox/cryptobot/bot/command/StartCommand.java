package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.SubscriberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class StartCommand implements IBotCommand {
    private final SubscriberService subscriberService;

    public StartCommand(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Запускает бота";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        subscriberService.registerIfAbsent(message.getChatId());

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        answer.setText("Привет! Данный бот помогает отслеживать стоимость биткоина.\n"
                + "Поддерживаемые команды:\n /get_price - получить стоимость биткоина\n"
                + " /subscribe ЦЕНА - подписаться на стоимость\n");

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /start command", e);
        }
    }
}