package ru.relex.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.relex.entity.AppPhoto;
import ru.relex.entity.AppUser;

import java.util.List;

public interface AppPhotoDAO extends JpaRepository<AppPhoto, Long> {
    List<AppPhoto> findAppPhotosByAppUser(AppUser appUser);
}