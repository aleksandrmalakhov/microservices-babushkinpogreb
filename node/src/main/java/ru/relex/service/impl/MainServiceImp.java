package ru.relex.service.impl;

import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.dao.AppUserDAO;
import ru.relex.dao.RawDataDAO;
import ru.relex.entity.AppDocument;
import ru.relex.entity.AppPhoto;
import ru.relex.entity.AppUser;
import ru.relex.entity.RawData;
import ru.relex.exceptions.UploadFileException;
import ru.relex.service.CommandContainerService;
import ru.relex.service.FileService;
import ru.relex.service.MainService;
import ru.relex.service.ProducerService;
import ru.relex.service.enums.LinkType;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;
import static ru.relex.entity.enums.UserState.BASIC_STATE;
import static ru.relex.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;

@Log4j
@Service
@FieldDefaults(level = PRIVATE)
public class MainServiceImp implements MainService {
    final RawDataDAO rawDataDAO;
    final AppUserDAO appUserDAO;
    final FileService fileService;
    final ProducerService producerService;
    final CommandContainerService commandContainer;

    public MainServiceImp(RawDataDAO rawDataDAO,
                          ProducerService producerService,
                          AppUserDAO appUserDAO,
                          FileService fileService,
                          CommandContainerService commandContainer) {
        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
        this.appUserDAO = appUserDAO;
        this.fileService = fileService;
        this.commandContainer = commandContainer;
    }

    @Override
    public void processTextMessage(Update update) {
        var commandPrefix = "/";
        var message = update.getMessage();
        var text = message.getText();

        saveRawData(update);

        if (text.startsWith(commandPrefix)) {
            var commandIdentifier = text.split(" ")[0].toLowerCase();
            commandContainer.retrieveCommand(commandIdentifier).execute(update);
        } else {
            var appUser = appUserDAO.findByTelegramUserId(update.getMessage().getFrom().getId());

            appUser.ifPresentOrElse((user) -> {
                var userState = user.getUserState();

                if (userState.equals(WAIT_FOR_EMAIL_STATE)) {
                    commandContainer.retrieveCommand("setEmail").execute(update);
                } else if (userState.equals(BASIC_STATE)) {
                    commandContainer.retrieveCommand("noCommand").execute(update);
                } else {
                    log.error("Unknown user state: " + userState);
                    commandContainer.retrieveCommand("error").execute(update);
                }
            }, () -> {
                var msg = "Ваших данных нет в базе. Введите /start для начала работы";
                var chatId = message.getChatId();
                producerService.producerAnswer(msg, chatId);
            });
        }
    }

    @Override
    public void processDocMessage(Update update) {
        var text = "";
        saveRawData(update);

        var chatId = update.getMessage().getChatId();
        var appUser = userValidated(chatId, update);

        if (appUser.isEmpty() || !appUser.get().getIsActive()) {
            return;
        }

        try {
            AppDocument doc = fileService.processDoc(update, appUser.get());
            String link = fileService.generateLink(doc.getId(), LinkType.GET_DOC, "doc");
            text = "Документ успешно загружен! Ссылка для скачивания: " + link;
        } catch (UploadFileException ex) {
            log.error(ex);
            text = "К сожалению, загрузка файла не удалась. Повторите попытку позже.";
        }
        producerService.producerAnswer(text, chatId);
    }

    @Override
    public void processPhotoMessage(Update update) {
        var text = "";
        saveRawData(update);

        var chatId = update.getMessage().getChatId();
        var appUser = userValidated(chatId, update);

        if (appUser.isEmpty() || !appUser.get().getIsActive()) {
            return;
        }

        try {
            AppPhoto photo = fileService.processPhoto(update, appUser.get());
            String link = fileService.generateLink(photo.getId(), LinkType.GET_PHOTO, "photo");
            text = "Фото успешно загружено! Ссылка для скачивания: " + link;
        } catch (UploadFileException ex) {
            log.error(ex);
            text = "К сожалению, загрузка фото не удалась. Повторите попытку позже.";
        }
        producerService.producerAnswer(text, chatId);
    }

    private Optional<AppUser> userValidated(Long chatId, Update update) {
        var telegramUser = update.getMessage().getFrom();
        var appUser = appUserDAO.findByTelegramUserId(telegramUser.getId());

        appUser.ifPresentOrElse((user) -> {
            if (!user.getIsActive()) {
                var error = "Активируйте свою учетную запись с помощью команды /registration для загрузки контента.";
                producerService.producerAnswer(error, chatId);
            }

            if (!user.getUserState().equals(BASIC_STATE)) {
                var error = "Отмените текущую команду с помощью /cancel для отправки файлов.";
                producerService.producerAnswer(error, chatId);
            }
        }, () -> {
            var error = "Зарегистрируйтесь с помощью команды /start для загрузки контента.";
            producerService.producerAnswer(error, chatId);
        });
        return appUser;
    }

    private void saveRawData(Update update) {
        var rawData = RawData.builder().event(update).build();
        rawDataDAO.save(rawData);
    }
}