package ru.relex.command;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.dao.AppUserDAO;
import ru.relex.entity.AppUser;
import ru.relex.service.AppUserService;
import ru.relex.service.ProducerService;

import java.util.concurrent.atomic.AtomicReference;

import static ru.relex.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationCommand implements Command {
    final AppUserDAO userDAO;
    final AppUserService userService;
    final ProducerService producerService;

    public RegistrationCommand(AppUserDAO userDAO,
                               AppUserService userService,
                               ProducerService producerService) {
        this.userDAO = userDAO;
        this.producerService = producerService;
        this.userService = userService;
    }

    @Override
    public void execute(Update update) {
        AtomicReference<String> text = new AtomicReference<>();
        var chatId = update.getMessage().getChatId();
        var telegramUser = update.getMessage().getFrom();
        var appUser = userDAO.findByTelegramUserId(telegramUser.getId());

        appUser.ifPresentOrElse((user) -> text.set(userService.registerUser(user)),
                () -> {
                    var newUser = AppUser.builder()
                            .telegramUserId(telegramUser.getId())
                            .userName(telegramUser.getUserName())
                            .firstName(telegramUser.getFirstName())
                            .lastName(telegramUser.getLastName())
                            .isActive(false)
                            .userState(WAIT_FOR_EMAIL_STATE)
                            .build();
                    userDAO.save(newUser);
                    text.set("Введите, пожалуйста ваш email");
                });
        producerService.producerAnswer(text.get(), chatId);
    }
}