package ru.relex.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.relex.entity.AppDocument;
import ru.relex.entity.AppUser;

import java.util.List;

public interface AppDocumentDAO extends JpaRepository<AppDocument, Long> {
    List<AppDocument> findAppDocumentsByAppUser(AppUser appUser);
}