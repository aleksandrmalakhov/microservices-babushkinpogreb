package ru.relex.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.entity.AppDocument;
import ru.relex.entity.AppPhoto;
import ru.relex.entity.AppUser;
import ru.relex.service.enums.LinkType;

public interface FileService {
    AppDocument processDoc(Update update, AppUser appUser);

    AppPhoto processPhoto(Update update, AppUser appUser);

    String generateLink(Long docId, LinkType linkType, String linkName);
}