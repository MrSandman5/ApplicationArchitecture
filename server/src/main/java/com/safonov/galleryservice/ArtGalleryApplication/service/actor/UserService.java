package com.safonov.galleryservice.ArtGalleryApplication.service.actor;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.*;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.LoginModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.RegistrationModel;
import com.safonov.galleryservice.ArtGalleryApplication.response.JwtResponse;
import com.safonov.galleryservice.ArtGalleryApplication.response.MessageResponse;
import com.safonov.galleryservice.ArtGalleryApplication.security.jwt.JwtUtils;
import com.safonov.galleryservice.ArtGalleryApplication.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final ClientRepository clientRepository;
    private final OwnerRepository ownerRepository;
    private final ArtistRepository artistRepository;
    private final RoleRepository roleRepository;
    private final CredentialsRepository credentialsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserService(@NotNull final AuthenticationManager authenticationManager,
                       @NotNull final ClientRepository clientRepository,
                       @NotNull final OwnerRepository ownerRepository,
                       @NotNull final ArtistRepository artistRepository,
                       @NotNull final CredentialsRepository credentialsRepository,
                       @NotNull final RoleRepository roleRepository,
                       @NotNull final BCryptPasswordEncoder bCryptPasswordEncoder,
                       @NotNull final JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.clientRepository = clientRepository;
        this.ownerRepository = ownerRepository;
        this.artistRepository = artistRepository;
        this.credentialsRepository = credentialsRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtils = jwtUtils;
        if (this.credentialsRepository.findByLogin("admin").isEmpty()) {
            final Credentials admin = new Credentials();
            admin.setEmail("admin@mail.ru");
            admin.setLogin("admin");
            admin.setPassword(bCryptPasswordEncoder.encode("nimda"));
            final Set<Role> roles = new HashSet<>();
            final Role adminRole = roleRepository.findRoleByName(Constants.Roles.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
            admin.setRoles(roles);
            this.credentialsRepository.save(admin);
        }
    }

    public ResponseEntity<?> authenticateUser(@NotNull final LoginModel model) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(model.getLogin(), model.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwt = jwtUtils.generateJwtToken(authentication);

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        final List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        long id;
        if ("ROLE_CLIENT".equals(roles.get(0))) {
            id = clientRepository.findById(userDetails.getId()).orElse(null).getId();
        } else if ("ROLE_OWNER".equals(roles.get(0))) {
            id = clientRepository.findById(userDetails.getId()).orElse(null).getId();
        } else if ("ROLE_ARTIST".equals(roles.get(0))) {
            id = clientRepository.findById(userDetails.getId()).orElse(null).getId();
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }

        return ResponseEntity.ok(new JwtResponse(jwt,
                id,
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    public ResponseEntity<?> registerUser(@NotNull final RegistrationModel model) {
        if (credentialsRepository.existsCredentialsByLogin(model.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (credentialsRepository.existsCredentialsByEmail(model.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        final Credentials user = new Credentials(model.getLogin(),
                model.getEmail(),
                bCryptPasswordEncoder.encode(model.getPassword()));

        final Set<String> strRoles = model.getRoles();
        final Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            final Role userRole = roleRepository.findRoleByName(Constants.Roles.ROLE_CLIENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "owner":
                        final Role ownerRole = roleRepository.findRoleByName(Constants.Roles.ROLE_OWNER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(ownerRole);
                        user.setRoles(roles);
                        final Owner owner = new Owner(model.getFirstName(), model.getLastName());
                        owner.setCredentials(credentialsRepository.save(user));
                        ownerRepository.save(owner);
                        break;
                    case "artist":
                        final Role artistRole = roleRepository.findRoleByName(Constants.Roles.ROLE_ARTIST)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(artistRole);
                        user.setRoles(roles);
                        final Artist artist = new Artist(model.getFirstName(), model.getLastName());
                        artist.setCredentials(credentialsRepository.save(user));
                        artistRepository.save(artist);
                        break;
                    default:
                        final Role userRole = roleRepository.findRoleByName(Constants.Roles.ROLE_CLIENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                        user.setRoles(roles);
                        final Client client = new Client(model.getFirstName(), model.getLastName());
                        client.setCredentials(credentialsRepository.save(user));
                        clientRepository.save(client);
                }
            });
        }
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
