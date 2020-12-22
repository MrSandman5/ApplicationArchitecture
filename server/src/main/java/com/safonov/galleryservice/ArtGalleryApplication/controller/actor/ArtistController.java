package com.safonov.galleryservice.ArtGalleryApplication.controller.actor;

import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.Artwork;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.AcceptRoyaltiesModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.AddArtworkModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.response.ApiResponse;
import com.safonov.galleryservice.ArtGalleryApplication.model.response.ResponseOrMessage;
import com.safonov.galleryservice.ArtGalleryApplication.service.actor.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistService service;

    @Autowired
    public ArtistController(@NotNull final ArtistService service) {
        this.service = service;
    }

    @PostMapping("/add-artwork")
    public ApiResponse addArtwork(@RequestBody AddArtworkModel model) {
        return service.addArtwork(model);
    }

    @PostMapping("/accept-royalties")
    public ApiResponse acceptRoyalties(@RequestBody AcceptRoyaltiesModel model) {
        return service.acceptRoyalties(model);
    }

    @GetMapping("/artworks")
    public ResponseOrMessage<List<Artwork>> getAllArtworks(@RequestBody Map<String, Long> artistId) {
        return service.getAllArtworks(artistId);
    }

    @GetMapping("/expo-artworks")
    public ResponseOrMessage<List<Artwork>> getExpoArtworks(@RequestBody Map<String, Long> artistId) {
        return service.getExpoArtworks(artistId);
    }
}
