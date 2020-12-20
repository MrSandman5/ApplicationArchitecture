package com.safonov.galleryservice.ArtGalleryApplication.service.actor;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.ArtistRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.ClientRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.CredentialsRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.OwnerRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.response.ResponseOrMessage;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.IdAndUserTypeModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.RegistrationModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.user.SignInModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final ClientRepository clientRepository;
    private final OwnerRepository ownerRepository;
    private final ArtistRepository artistRepository;
    private final CredentialsRepository credentialsRepository;

    @Autowired
    UserService(@NotNull final ClientRepository clientRepository,
                  @NotNull final OwnerRepository ownerRepository,
                  @NotNull final ArtistRepository artistRepository,
                  @NotNull final CredentialsRepository credentialsRepository) {
        this.clientRepository = clientRepository;
        this.ownerRepository = ownerRepository;
        this.artistRepository = artistRepository;
        this.credentialsRepository = credentialsRepository;
    }

    public ResponseOrMessage<Boolean> signUp(@NotNull final RegistrationModel model) {
        final Credentials credentials = new Credentials(model.getEmail(), model.getPassword());
        try {
            switch (model.getPersonType()) {
                case Client:
                    final Client client = new Client(model.getFirstName(), model.getLastName());
                    client.setCredentials(credentials);
                    clientRepository.save(client);
                    return new ResponseOrMessage<>(true);
                case Owner:
                    final Owner owner = new Owner(model.getFirstName(), model.getLastName());
                    owner.setCredentials(credentials);
                    ownerRepository.save(owner);
                    return new ResponseOrMessage<>(true);
                case Artist:
                    final Artist artist = new Artist(model.getFirstName(), model.getLastName());
                    artist.setCredentials(credentials);
                    artistRepository.save(artist);
                    return new ResponseOrMessage<>(true);
                default:
                    return new ResponseOrMessage<>("Wrong parameter");
            }
        } catch (Exception e) {
            return new ResponseOrMessage<>("Login already exist");
        }
    }

    public ResponseOrMessage<SignInModel> signIn(@NotNull final Map<String, String> emailOrUserName) {
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
                        switch (elem.getClass().getSimpleName()) {
                            case "Client":
                                final Client newClient = new Client(elem.getFirstName(), elem.getLastName());
                                newClient.setAuthenticated(true);
                                user = clientRepository.save(newClient);
                                response.setUserType(Constants.UserType.Client);
                                break;
                            case "Owner":
                                final Owner newOwner = new Owner(elem.getFirstName(), elem.getLastName());
                                newOwner.setAuthenticated(true);
                                user = ownerRepository.save(newOwner);
                                response.setUserType(Constants.UserType.Owner);
                                break;
                            case "Artist":
                                final Artist newArtist = new Artist(elem.getFirstName(), elem.getLastName());
                                newArtist.setAuthenticated(true);
                                user = artistRepository.save(newArtist);
                                response.setUserType(Constants.UserType.Artist);
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
                if (user.getDeleted()) {
                    return new ResponseOrMessage<>("User was deleted");
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
    }

    public ResponseOrMessage<User> getUserById(@NotNull final IdAndUserTypeModel model) {
        final Long personId = model.getPersonId();
        User user;
        switch (model.getPersonType()) {
            case Client:
                user = clientRepository.findById(personId).orElse(null);
                break;
            case Owner:
                user = ownerRepository.findById(personId).orElse(null);
                break;
            case Artist:
                user = artistRepository.findById(personId).orElse(null);
                break;
            default:
                return new ResponseOrMessage<>("Wrong with role parameter");
        }

        if (user == null) {
            return new ResponseOrMessage<>("User not found");
        }
        if (user.getDeleted()) {
            return new ResponseOrMessage<>("User was deleted");
        }
        return new ResponseOrMessage<>(user);
    }
}
