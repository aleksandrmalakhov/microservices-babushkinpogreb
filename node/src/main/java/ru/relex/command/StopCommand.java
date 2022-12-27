package ru.relex.command;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.dao.AppUserDAO;
import ru.relex.service.ProducerService;

public class StopCommand implements Command {
    private final AppUserDAO appUserDAO;
    private final ProducerService producerService;

    public StopCommand(AppUserDAO appUserDAO, ProducerService producerService) {
        this.appUserDAO = appUserDAO;
        this.producerService = producerService;
    }

    @Override
    public void execute(@NonNull Update update) {
        var text = "";
        var chatId = update.getMessage().getChatId();
        var telegramUser = update.getMessage().getFrom();
        var appUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());

        if (appUser != null) {
            appUser.setIsActive(false);
            appUserDAO.save(appUser);

            text = appUser.getFirstName() + ", до новых встреч!";
            producerService.producerAnswer(text, chatId);
        } else {
            text = telegramUser.getFirstName() + ", ваших данных нет в базе.\n Введи команду /start и начнем наше знакомство!";
            producerService.producerAnswer(text, chatId);
        }
    }
}