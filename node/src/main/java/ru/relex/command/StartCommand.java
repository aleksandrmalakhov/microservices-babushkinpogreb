package ru.relex.command;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.dao.AppUserDAO;
import ru.relex.entity.AppUser;
import ru.relex.service.ProducerService;

import java.util.concurrent.atomic.AtomicReference;

import static ru.relex.entity.enums.UserState.BASIC_STATE;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class StartCommand implements Command {
    final AppUserDAO appUserDAO;
    final ProducerService producerService;

    public StartCommand(AppUserDAO appUserDAO, ProducerService producerService) {
        this.appUserDAO = appUserDAO;
        this.producerService = producerService;
    }

    @Override
    public void execute(@NonNull Update update) {
        AtomicReference<String> text = new AtomicReference<>();
        var chatId = update.getMessage().getChatId();
        var telegramUser = update.getMessage().getFrom();
        var persistentAppUser = appUserDAO.findByTelegramUserId(telegramUser.getId());

        persistentAppUser.ifPresentOrElse((user) -> {
            text.set(user.getFirstName() + ", с возвращением!");
            user.setIsActive(true);
            user.setUserState(BASIC_STATE);
            appUserDAO.save(user);
        }, () -> {
            text.set(telegramUser.getFirstName() + ", добро пожаловать!");
            var transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .isActive(false)
                    .userState(BASIC_STATE)
                    .build();
            appUserDAO.save(transientAppUser);

        });
        producerService.producerAnswer(text.get(), chatId);
    }
}