package ru.relex.command;

import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.dao.AppUserDAO;
import ru.relex.service.ProducerService;

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
        var text = "";
        var chatId = update.getMessage().getChatId();
        var telegramUser = update.getMessage().getFrom();
        var persistentAppUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());

        if (persistentAppUser != null) {
            appUserDAO.delete(persistentAppUser);
            text = "Ваши данные успешно удалены";
        } else {
            text = "Ваши данные не найдены";
        }

        producerService.producerAnswer(text, chatId);
    }
}