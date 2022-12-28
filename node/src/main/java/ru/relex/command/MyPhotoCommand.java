package ru.relex.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.dao.AppPhotoDAO;
import ru.relex.dao.AppUserDAO;
import ru.relex.entity.AppPhoto;
import ru.relex.service.FileService;
import ru.relex.service.ProducerService;
import ru.relex.service.enums.LinkType;

public class MyPhotoCommand implements Command {
    private final AppUserDAO appUserDAO;
    private final AppPhotoDAO appPhotoDAO;
    private final FileService fileService;
    private final ProducerService producerService;

    public MyPhotoCommand(AppUserDAO appUserDAO,
                          AppPhotoDAO appPhotoDAO, ProducerService producerService,
                          FileService fileService) {
        this.appUserDAO = appUserDAO;
        this.appPhotoDAO = appPhotoDAO;
        this.producerService = producerService;
        this.fileService = fileService;
    }

    @Override
    public void execute(Update update) {
        StringBuilder builder = new StringBuilder();
        var chatId = update.getMessage().getChatId();
        var telegramUser = update.getMessage().getFrom();
        var appUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());

        for (AppPhoto photo : appPhotoDAO.findAppPhotosByAppUser(appUser)) {
            var link = fileService.generateLink(photo.getId(), LinkType.GET_PHOTO, "link");
            builder.append(link).append("\n");
        }

        producerService.producerAnswer(builder.toString().trim(), chatId);
    }
}