package com.safonov.galleryservice.ArtGalleryApplication.controller.actor;

import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.ArtworkModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.ExpoModel;
import com.safonov.galleryservice.ArtGalleryApplication.service.actor.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/artist")
@PreAuthorize("hasRole('ARTIST')")
public class ArtistController {

    private final ArtistService service;

    @Autowired
    public ArtistController(@NotNull final ArtistService service) {
        this.service = service;
    }

    @PostMapping("/{artistId}/add-artwork")
    public ResponseEntity<String> addArtwork(@PathVariable final Long artistId,
                                             @Valid @RequestBody final ArtworkModel model) {
        return service.addArtwork(artistId, model);
    }

    @PostMapping("/{artistId}/accept-royalties")
    public ResponseEntity<String> acceptRoyalties(@PathVariable final Long artistId,
                                                  @Valid @RequestBody final ExpoModel model) {
        return service.acceptRoyalties(artistId, model);
    }

    @GetMapping("/{artistId}/artworks")
    public ResponseEntity<Object> getAllArtworks(@PathVariable final Long artistId) {
        return service.getAllArtworks(artistId);
    }
}
