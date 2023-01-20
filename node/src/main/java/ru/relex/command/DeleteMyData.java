package ru.relex.command;

import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.dao.AppUserDAO;
import ru.relex.service.ProducerService;

import java.util.concurrent.atomic.AtomicReference;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
public class DeleteMyData implements Command {
    final AppUserDAO appUserDAO;
    final ProducerService producerService;

    public DeleteMyData(AppUserDAO appUserDAO,
                        ProducerService producerService) {
        this.appUserDAO = appUserDAO;
        this.producerService = producerService;
    }

    @Override
    public void execute(Update update) {
        AtomicReference<String> text = new AtomicReference<>();
        var chatId = update.getMessage().getChatId();
        var telegramUser = update.getMessage().getFrom();
        var persistentAppUser = appUserDAO.findByTelegramUserId(telegramUser.getId());

        persistentAppUser.ifPresentOrElse((user) -> {
            appUserDAO.delete(user);
            text.set("Ваши данные успешно удалены");
        }, () -> text.set("Ваши данные не найдены"));

        producerService.producerAnswer(text.get(), chatId);
    }
}