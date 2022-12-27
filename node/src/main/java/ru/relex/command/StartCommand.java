package ru.relex.command;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.dao.AppUserDAO;
import ru.relex.entity.AppUser;
import ru.relex.service.ProducerService;

import static ru.relex.entity.enums.UserState.BASIC_STATE;

public class StartCommand implements Command {
    private final AppUserDAO appUserDAO;
    private final ProducerService producerService;

    public StartCommand(AppUserDAO appUserDAO, ProducerService producerService) {
        this.appUserDAO = appUserDAO;
        this.producerService = producerService;
    }
    @Override
    public void execute(@NonNull Update update) {
        var text = "";
        var chatId = update.getMessage().getChatId();
        var telegramUser = update.getMessage().getFrom();
        var persistentAppUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());

        if (persistentAppUser == null) {
            text = telegramUser.getFirstName() + ", добро пожаловать!";
            var transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO изменить значение по умолчанию после добавдения регистрации
                    .isActive(true)
                    .userState(BASIC_STATE)
                    .build();
            appUserDAO.save(transientAppUser);
        } else {
            text = telegramUser.getFirstName() + ", с возвращением!";
            persistentAppUser.setIsActive(true);
            persistentAppUser.setUserState(BASIC_STATE);

            appUserDAO.save(persistentAppUser);
        }
        producerService.producerAnswer(text, chatId);
    }
}