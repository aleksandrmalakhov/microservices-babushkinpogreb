package ru.relex.command;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.dao.AppUserDAO;
import ru.relex.service.ProducerService;

import static ru.relex.entity.enums.UserState.BASIC_STATE;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CancelCommand implements Command {
    final AppUserDAO appUserDAO;
    final ProducerService producerService;

    public CancelCommand(AppUserDAO appUserDAO, ProducerService producerService) {
        this.appUserDAO = appUserDAO;
        this.producerService = producerService;
    }

    @Override
    public void execute(Update update) {
        var text = "";
        var chatId = update.getMessage().getChatId();
        var telegramUser = update.getMessage().getFrom();
        var appUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());

        if (appUser != null) {
            appUser.setUserState(BASIC_STATE);
            appUserDAO.save(appUser);

            text = "Команда отменена.";
            producerService.producerAnswer(text, chatId);
        } else {
            text = telegramUser.getFirstName() + ", ваших данных нет в базе.\n Введи команду /start и начнем наше знакомство!";
            producerService.producerAnswer(text, chatId);
        }
    }
}