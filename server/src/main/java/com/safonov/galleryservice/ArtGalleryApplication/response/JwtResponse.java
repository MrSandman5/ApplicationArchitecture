package com.safonov.galleryservice.ArtGalleryApplication.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private final String tokenType = "Bearer";
}
