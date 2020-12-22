package com.safonov.galleryservice.ArtGalleryApplication.security;

import com.safonov.galleryservice.ArtGalleryApplication.data.actor.CredentialsRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CredentialsRepository credentialsRepository;

    @Autowired
    public UserDetailsServiceImpl(@NotNull final CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NotNull final String username) throws UsernameNotFoundException {
        final Credentials credentials = credentialsRepository.findByLogin(username).orElse(null);
        if (credentials == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(credentials.getLogin(),
                credentials.getPassword(),
                Set.of(new SimpleGrantedAuthority(credentials.getRole().getName())));
    }
}
