package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.service.SubscriberService;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;

@Service
@Slf4j
public class SubscribeCommand implements IBotCommand {
    private static final String PRICE_PATTERN = "\\d+(\\.\\d+)?";

    private final SubscriberService subscriberService;
    private final CryptoCurrencyService currencyService;

    public SubscribeCommand(SubscriberService subscriberService,
                            CryptoCurrencyService currencyService) {
        this.subscriberService = subscriberService;
        this.currencyService = currencyService;
    }

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        try {
            if (arguments.length != 1 || !arguments[0].matches(PRICE_PATTERN)) {
                send(absSender, message, "Укажите цену числом, например: /subscribe 34600");
                return;
            }
            double target = Double.parseDouble(arguments[0]);
            subscriberService.setPrice(message.getChatId(), target);
            send(absSender, message, "Текущая цена биткоина "
                    + TextUtil.toString(currencyService.getBitcoinPrice()) + " USD");
            send(absSender, message, "Новая подписка создана на стоимость "
                    + TextUtil.toString(target));
        } catch (IOException e) {
            log.error("Could not get bitcoin price in /subscribe", e);
        } catch (TelegramApiException e) {
            log.error("Could not send message in /subscribe", e);
        }
    }

    private void send(AbsSender absSender, Message message, String text) throws TelegramApiException {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        answer.setText(text);
        absSender.execute(answer);
    }
}