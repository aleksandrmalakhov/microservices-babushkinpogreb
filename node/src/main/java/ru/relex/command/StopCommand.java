package ru.relex.command;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.dao.AppUserDAO;
import ru.relex.service.ProducerService;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class StopCommand implements Command {
    final AppUserDAO appUserDAO;
    final ProducerService producerService;

    public StopCommand(AppUserDAO appUserDAO, ProducerService producerService) {
        this.appUserDAO = appUserDAO;
        this.producerService = producerService;
    }

    @Override
    public void execute(@NonNull Update update) {
        var chatId = update.getMessage().getChatId();
        var telegramUser = update.getMessage().getFrom();
        var appUser = appUserDAO.findByTelegramUserId(telegramUser.getId());

        appUser.ifPresentOrElse((user) -> {
            user.setIsActive(false);
            appUserDAO.save(user);

            producerService.producerAnswer(user.getFirstName() + ", до новых встреч!", chatId);
        }, () -> {
            var text = telegramUser.getFirstName() + ", ваших данных нет в базе.\n Введи команду /start и начнем наше знакомство!";
            producerService.producerAnswer(text, chatId);
        });
    }
}