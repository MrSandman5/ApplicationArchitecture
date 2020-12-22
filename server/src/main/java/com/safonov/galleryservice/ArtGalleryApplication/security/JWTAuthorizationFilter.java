package com.safonov.galleryservice.ArtGalleryApplication.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.CredentialsRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Credentials;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.safonov.galleryservice.ArtGalleryApplication.security.SecurityConstants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    @Setter
    private CredentialsRepository credentialsRepository;

    public JWTAuthorizationFilter(@NotNull final AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(@NotNull final HttpServletRequest req,
                                    @NotNull final HttpServletResponse res,
                                    @NotNull final FilterChain chain) throws IOException, ServletException {
        final String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        final UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(req, res);
    }


    private UsernamePasswordAuthenticationToken getAuthentication(@NotNull final HttpServletRequest request) {
        final String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            //верификаця полученного в заголовке токена без префикса
            final String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();

            if (user != null) {
                final Credentials credentials = credentialsRepository.findByLogin(user).orElse(null);
                final Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
                if (credentials != null && credentials.getRole() != null) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(credentials.getRole().getName()));
                }

                return new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        grantedAuthorities);
            }
            return null;
        }
        return null;
    }
}
