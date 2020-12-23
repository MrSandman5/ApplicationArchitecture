package com.safonov.galleryservice.ArtGalleryApplication.controller.actor;

import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Artwork;
import com.safonov.galleryservice.ArtGalleryApplication.model.info.ArtworkModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.info.ExpoModel;
import com.safonov.galleryservice.ArtGalleryApplication.service.actor.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@RestController
public class ArtistController {

    private final ArtistService service;

    @Autowired
    public ArtistController(@NotNull final ArtistService service) {
        this.service = service;
    }

    @Secured("ROLE_ARTIST")
    @PostMapping("/{artistId}/add-artwork")
    public ResponseEntity<String> addArtwork(@PathVariable final Long artistId,
                                             @RequestBody final ArtworkModel model) {
        return service.addArtwork(artistId, model);
    }

    @Secured("ROLE_ARTIST")
    @PostMapping("/{artistId}/accept-royalties")
    public ResponseEntity<String> acceptRoyalties(@PathVariable final Long artistId,
                                                  @RequestBody final ExpoModel model) {
        return service.acceptRoyalties(artistId, model);
    }

    @Secured("ROLE_ARTIST")
    @GetMapping("/{artistId}/artworks")
    public ResponseEntity<Object> getAllArtworks(@PathVariable final Long artistId) {
        return service.getAllArtworks(artistId);
    }

    @Secured("ROLE_ARTIST")
    @GetMapping("/{artistId}/expo-artworks")
    public ResponseEntity<Object> getExpoArtworks(@PathVariable final Long artistId) {
        return service.getExpoArtworks(artistId);
    }
}
