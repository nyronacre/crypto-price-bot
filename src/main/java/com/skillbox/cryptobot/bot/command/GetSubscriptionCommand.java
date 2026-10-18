package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.SubscriberService;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class GetSubscriptionCommand implements IBotCommand {
    private final SubscriberService subscriberService;

    public GetSubscriptionCommand(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        Double price = subscriberService.getPrice(message.getChatId());
        String text = price == null
                ? "Активные подписки отсутствуют"
                : "Вы подписаны на стоимость биткоина " + TextUtil.toString(price) + " USD";

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        answer.setText(text);
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /get_subscription command", e);
        }
    }
}