package com.safonov.galleryservice.ArtGalleryApplication.service.actor;

import com.safonov.galleryservice.ArtGalleryApplication.data.actor.*;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.RegistrationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class UserService {
    private final ClientRepository clientRepository;
    private final OwnerRepository ownerRepository;
    private final ArtistRepository artistRepository;
    private final RoleRepository roleRepository;
    private final CredentialsRepository credentialsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(@NotNull final ClientRepository clientRepository,
                       @NotNull final OwnerRepository ownerRepository,
                       @NotNull final ArtistRepository artistRepository,
                       @NotNull final CredentialsRepository credentialsRepository,
                       @NotNull final RoleRepository roleRepository,
                       @NotNull final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.clientRepository = clientRepository;
        this.ownerRepository = ownerRepository;
        this.artistRepository = artistRepository;
        this.credentialsRepository = credentialsRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        if (this.credentialsRepository.findByLogin("admin").isEmpty()) {
            final Credentials admin = new Credentials();
            admin.setEmail("admin@mail.ru");
            admin.setLogin("admin");
            admin.setPassword(bCryptPasswordEncoder.encode("nimda"));
            final Role role = roleRepository.findRoleByName("ROLE_ADMIN").orElse(null);
            admin.setRole(role);
            this.credentialsRepository.save(admin);
        }
    }

    public ResponseEntity<String> signUp(@NotNull final RegistrationModel model) {
        final Role role = roleRepository.findRoleByName(model.getRole().getCode()).orElse(null);
        if (role == null) {
            return new ResponseEntity<>("Role doesnt exist", HttpStatus.NOT_FOUND);
        }
        final Credentials credentials = new Credentials(model.getEmail(), bCryptPasswordEncoder.encode(model.getPassword()), role);
        try {
            switch (model.getRole()) {
                case ROLE_CLIENT:
                    final Client client = new Client(model.getFirstName(), model.getLastName());
                    client.setCredentials(credentials);
                    clientRepository.save(client);
                    return new ResponseEntity<>("", HttpStatus.CREATED);
                case ROLE_OWNER:
                    final Owner owner = new Owner(model.getFirstName(), model.getLastName());
                    owner.setCredentials(credentials);
                    ownerRepository.save(owner);
                    return new ResponseEntity<>("", HttpStatus.CREATED);
                case ROLE_ARTIST:
                    final Artist artist = new Artist(model.getFirstName(), model.getLastName());
                    artist.setCredentials(credentials);
                    artistRepository.save(artist);
                    return new ResponseEntity<>("", HttpStatus.CREATED);
                default:
                    return new ResponseEntity<>("Wrong parameter", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Login already exist", HttpStatus.EXPECTATION_FAILED);
        }
    }

    /*public ResponseOrMessage<SignInModel> signIn(@NotNull final Map<String, String> emailOrUserName) {
        if (emailOrUserName.containsKey("emailOrUserName")) {
            final Credentials credentials = credentialsRepository.findByEmail(emailOrUserName.get("emailOrUserName"))
                    .orElseGet(() -> credentialsRepository.findByLogin(emailOrUserName.get("emailOrUserName")).orElse(null));
            if (credentials != null) {
                final SignInModel response = new SignInModel();
                User user = null;
                final Client client = clientRepository.findByCredentials(credentials).orElse(null);
                final Owner owner = ownerRepository.findByCredentials(credentials).orElse(null);
                final Artist artist = artistRepository.findByCredentials(credentials).orElse(null);
                for (final User elem : List.of(client, owner, artist)){
                    if (elem != null) {
                        switch (elem.getCredentials().getRole().getName()) {
                            case "ROLE_ADMIN":
                                final Client newClient = new Client(elem.getFirstName(), elem.getLastName());
                                user = clientRepository.save(newClient);
                                response.setRole(ROLE_CLIENT);
                                break;
                            case "ROLE_CLIENT":
                                final Client newClient = new Client(elem.getFirstName(), elem.getLastName());
                                user = clientRepository.save(newClient);
                                response.setRole(ROLE_CLIENT);
                                break;
                            case "ROLE_OWNER":
                                final Owner newOwner = new Owner(elem.getFirstName(), elem.getLastName());
                                user = ownerRepository.save(newOwner);
                                response.setRole(ROLE_OWNER);
                                break;
                            case "ROLE_ARTIST":
                                final Artist newArtist = new Artist(elem.getFirstName(), elem.getLastName());
                                user = artistRepository.save(newArtist);
                                response.setRole(ROLE_ARTIST);
                                break;
                            default:
                                return new ResponseOrMessage<>("Invalid user role");
                        }
                        break;
                    }
                }
                if (user == null) {
                    return new ResponseOrMessage<>("User not found");
                }
                response.setUserId(user.id);
                response.setPassword(user.getCredentials().getPassword());
                return new ResponseOrMessage<>(response);
            } else {
                return new ResponseOrMessage<>("Incorrect login");
            }
        } else {
            return new ResponseOrMessage<>("Wrong parameter");
        }
    }*/

    public ResponseEntity<User> getUserById(@NotNull final String login) {
        final Credentials credentials = credentialsRepository.findByLogin(login).orElse(null);
        if (credentials == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        User user;
        switch (credentials.getRole().getName()) {
            case "ROLE_CLIENT":
                user = clientRepository.findByCredentials(credentials).orElse(null);
                break;
            case "ROLE_OWNER":
                user = ownerRepository.findByCredentials(credentials).orElse(null);
                break;
            case "ROLE_ARTIST":
                user = artistRepository.findByCredentials(credentials).orElse(null);
                break;
            default:
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
