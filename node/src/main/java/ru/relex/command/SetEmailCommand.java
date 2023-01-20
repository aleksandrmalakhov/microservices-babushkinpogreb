package ru.relex.command;

import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.dao.AppUserDAO;
import ru.relex.service.AppUserService;
import ru.relex.service.ProducerService;

import java.util.concurrent.atomic.AtomicReference;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
public class SetEmailCommand implements Command {
    final AppUserDAO userDAO;
    final AppUserService userService;
    final ProducerService producerService;

    public SetEmailCommand(AppUserDAO userDAO,
                           AppUserService userService,
                           ProducerService producerService) {
        this.userDAO = userDAO;
        this.userService = userService;
        this.producerService = producerService;
    }

    @Override
    public void execute(Update update) {
        AtomicReference<String> text = new AtomicReference<>();
        var message = update.getMessage();
        var chatId = message.getChatId();
        var telegramUser = update.getMessage().getFrom();
        var appUser = userDAO.findByTelegramUserId(telegramUser.getId());

        appUser.ifPresentOrElse((user) -> text.set(userService.setEmail(user, message.getText())),
                () -> text.set("Ваши данные не найдены. Введите /start для начала работы"));

        producerService.producerAnswer(text.get(), chatId);
    }
}