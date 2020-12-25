package com.safonov.galleryservice.ArtGalleryApplication.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.CredentialsRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Credentials;
import com.safonov.galleryservice.ArtGalleryApplication.error.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.safonov.galleryservice.ArtGalleryApplication.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final CredentialsRepository credentialsRepository;

    public JWTAuthenticationFilter(@NotNull final AuthenticationManager authenticationManager,
                                   @NotNull final CredentialsRepository credentialsRepository) {
        this.authenticationManager = authenticationManager;
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    public Authentication attemptAuthentication(@NotNull final HttpServletRequest req,
                                                @NotNull final HttpServletResponse res) {
        try {
            final Credentials credential = new ObjectMapper()
                    .registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule())
                    .readValue(req.getInputStream(), Credentials.class);
            final Credentials user = credentialsRepository.findByLogin(credential.getLogin()).orElse(null);
            final Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            if (user != null) {
                grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
            }

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credential.getLogin(),
                            credential.getPassword(),
                            grantedAuthorities)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(@NotNull final HttpServletRequest request,
                                              @NotNull final HttpServletResponse response,
                                              @NotNull final AuthenticationException failed) throws IOException {
        SecurityContextHolder.clearContext();

        final ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED);
        apiError.setError(failed.getMessage().toUpperCase().replaceAll(" ", "_"));
        apiError.setMessage(failed.getMessage());

        final ObjectWriter ow = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .writer().withDefaultPrettyPrinter();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("Content-Type", "application/json");
        response.getWriter().write(ow.writeValueAsString(apiError));
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    protected void successfulAuthentication(@NotNull final HttpServletRequest req,
                                            @NotNull final HttpServletResponse res,
                                            @NotNull final FilterChain chain,
                                            @NotNull final Authentication auth) {
        final Credentials user = credentialsRepository.findByLogin(((User) auth.getPrincipal()).getUsername()).orElse(null);
        String grantedAuthorities = null;
        if (user != null) {
            grantedAuthorities = user.getRole().getName();
        }

        final String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("role", grantedAuthorities)
                .sign(HMAC512(SECRET.getBytes()));

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
