package com.safonov.galleryservice.ArtGalleryApplication.controller.actor;

import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.User;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.RegistrationModel;
import com.safonov.galleryservice.ArtGalleryApplication.service.actor.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(@NotNull final UserService service) {
        this.service = service;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody final RegistrationModel model) {
        return service.signUp(model);
    }

    @Secured({"ROLE_CLIENT", "ROLE_OWNER", "ROLE_ARTIST"})
    @GetMapping("users/{login}")
    public ResponseEntity<User> getUserByLogin(@PathVariable final String login) {
        return service.getUserById(login);
    }
}
