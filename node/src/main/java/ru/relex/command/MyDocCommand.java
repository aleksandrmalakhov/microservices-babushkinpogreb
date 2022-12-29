package ru.relex.command;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.dao.AppDocumentDAO;
import ru.relex.dao.AppUserDAO;
import ru.relex.entity.AppDocument;
import ru.relex.service.FileService;
import ru.relex.service.ProducerService;
import ru.relex.service.enums.LinkType;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class MyDocCommand implements Command {
    final AppUserDAO appUserDAO;
    final AppDocumentDAO appDocumentDAO;
    final FileService fileService;
    final ProducerService producerService;

    public MyDocCommand(AppUserDAO appUserDAO,
                        AppDocumentDAO appDocumentDAO,
                        FileService fileService,
                        ProducerService producerService) {
        this.appUserDAO = appUserDAO;
        this.appDocumentDAO = appDocumentDAO;
        this.fileService = fileService;
        this.producerService = producerService;
    }

    @Override
    public void execute(Update update) {
        StringBuilder builder = new StringBuilder();
        var chatId = update.getMessage().getChatId();
        var telegramUser = update.getMessage().getFrom();
        var appUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());
        List<AppDocument> documents = appDocumentDAO.findAppDocumentsByAppUser(appUser);

        for (int i = 0; i < documents.size(); i++) {
            var linkName = "Ссылка на документ № " + (i + 1);
            var link = fileService.generateLink(documents.get(i).getId(), LinkType.GET_DOC, linkName);
            builder.append(link).append("\n");
        }
        producerService.producerAnswer(builder.toString().trim(), chatId);
    }
}