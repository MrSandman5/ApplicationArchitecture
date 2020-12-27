import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/artist/';

class ArtistService {

    addArtwork(artistId, artwork) {
        return axios.post(API_URL + `${artistId}/add-artwork`, {artwork}, { headers: authHeader() });
    }

    acceptRoyalties(artistId, expo) {
        return axios.post(API_URL + `${artistId}/accept-royalties`, {expo}, { headers: authHeader() });
    }

    getAllArtworks(artistId) {
        return axios.get(API_URL + `${artistId}/artworks`, { headers: authHeader() });
    }

}

export default new ArtistService();