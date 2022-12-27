package ru.relex.service.imp;

import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import ru.relex.CryptoTool;
import ru.relex.dao.AppDocumentDAO;
import ru.relex.dao.AppPhotoDAO;
import ru.relex.entity.AppDocument;
import ru.relex.entity.AppPhoto;
import ru.relex.entity.BinaryContent;
import ru.relex.service.FileService;

import java.io.File;
import java.io.IOException;

@Log4j
@Service
public class FileServiceImp implements FileService {
    private final AppDocumentDAO appDocumentDAO;
    private final AppPhotoDAO appPhotoDAO;
    private final CryptoTool cryptoTool;

    public FileServiceImp(AppDocumentDAO appDocumentDAO,
                          AppPhotoDAO appPhotoDAO,
                          CryptoTool cryptoTool) {
        this.appDocumentDAO = appDocumentDAO;
        this.appPhotoDAO = appPhotoDAO;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public AppDocument getDocument(String docId) {
        var hash = cryptoTool.idOf(docId);

        if (hash == null) {
            return null;
        }

        return appDocumentDAO.findById(hash).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String photoId) {
        var hash = cryptoTool.idOf(photoId);

        if (hash == null) {
            return null;
        }

        return appPhotoDAO.findById(hash).orElse(null);
    }

    @Override
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
        try {
            //TODO добавить генерацию имени временного файла
            File temp = File.createTempFile("tempFile", ".bin");
            temp.deleteOnExit();
            FileUtils.writeByteArrayToFile(temp, binaryContent.getFileAsArrayOfBytes());
            return new FileSystemResource(temp);
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }
}