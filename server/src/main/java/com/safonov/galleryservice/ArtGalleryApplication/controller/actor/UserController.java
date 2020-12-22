package com.safonov.galleryservice.ArtGalleryApplication.controller.actor;

import com.safonov.galleryservice.ArtGalleryApplication.model.response.ResponseOrMessage;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.RegistrationModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.SignInModel;
import com.safonov.galleryservice.ArtGalleryApplication.service.actor.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(@NotNull final UserService service) {
        this.service = service;
    }

    @PostMapping("/sign-up")
    public ResponseOrMessage<Boolean> signUp(@RequestBody RegistrationModel model) {
        return service.signUp(model);
    }

    @PostMapping("/sign-in")
    public ResponseOrMessage<SignInModel> signIn(@RequestBody Map<String, String> emailOrUserName) {
        return service.signIn(emailOrUserName);
    }
}
