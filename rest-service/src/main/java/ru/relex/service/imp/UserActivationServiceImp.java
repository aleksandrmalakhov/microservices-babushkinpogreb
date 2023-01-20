package ru.relex.service.imp;

import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.relex.dao.AppUserDAO;
import ru.relex.service.UserActivationService;
import ru.relex.utils.CryptoTool;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE)
public class UserActivationServiceImp implements UserActivationService {
    final AppUserDAO appUserDAO;
    final CryptoTool cryptoTool;

    public UserActivationServiceImp(AppUserDAO appUserDAO,
                                    CryptoTool cryptoTool) {
        this.appUserDAO = appUserDAO;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public boolean activation(String cryptoUserId) {
        System.out.println("Activation Service " + cryptoUserId);

        var userId = cryptoTool.idOf(cryptoUserId);

        System.out.println("ID of " + userId);

        var userOptional = appUserDAO.findById(userId);

        if (userOptional.isPresent()) {
            var user = userOptional.get();
            user.setIsActive(true);
            appUserDAO.save(user);
            return true;
        }
        return false;
    }
}