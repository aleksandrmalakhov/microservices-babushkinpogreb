package ru.relex.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.relex.service.UserActivationService;

@RestController
@RequestMapping(value = "/user")
public class ActivationController {
    private final UserActivationService activationService;

    public ActivationController(UserActivationService activationService) {
        this.activationService = activationService;
    }

    @GetMapping(value = "/activation")
    public ResponseEntity<?> activation(@RequestParam("id") String id) {
        System.out.println("Activation Controller - " + id);
        var result = activationService.activation(id);

        if (result) {
            return ResponseEntity.ok().body("Регистрация пройдена успешно");
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
}