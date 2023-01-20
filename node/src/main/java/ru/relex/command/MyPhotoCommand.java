package ru.relex.command;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.dao.AppPhotoDAO;
import ru.relex.dao.AppUserDAO;
import ru.relex.entity.AppPhoto;
import ru.relex.service.FileService;
import ru.relex.service.ProducerService;
import ru.relex.service.enums.LinkType;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class MyPhotoCommand implements Command {
    final AppUserDAO appUserDAO;
    final AppPhotoDAO appPhotoDAO;
    final FileService fileService;
    final ProducerService producerService;

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
        var appUser = appUserDAO.findByTelegramUserId(telegramUser.getId());

        appUser.ifPresentOrElse((user) -> {
            List<AppPhoto> photoList = appPhotoDAO.findAppPhotosByAppUser(user);

            if (photoList == null || photoList.size() == 0) {
                builder.append("У вас нет сохраненных фото");
            } else {
                for (int i = 0; i < photoList.size(); i++) {
                    var linkName = "Ссылка на фото № " + (i + 1);
                    var link = fileService.generateLink(photoList.get(i).getId(), LinkType.GET_PHOTO, linkName);
                    builder.append(link).append("\n");
                }
            }
        }, () -> builder.append("Мы вас не нашли в нашей базе. Зарегистрируйтесь"));

//        if (appUser.isPresent())
//            List<AppPhoto> photoList = appPhotoDAO.findAppPhotosByAppUser(appUser.get());
//
//        for (int i = 0; i < photoList.size(); i++) {
//            var linkName = "Ссылка на фото № " + (i + 1);
//            var link = fileService.generateLink(photoList.get(i).getId(), LinkType.GET_PHOTO, linkName);
//            builder.append(link).append("\n");
//        }
        producerService.producerAnswer(builder.toString().trim(), chatId);
    }
}