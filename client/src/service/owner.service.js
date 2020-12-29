import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/owner/';

class OwnerService {

    getMe(ownerId) {
        return axios.get(API_URL + ownerId, {headers: authHeader()});
    }

    acceptPayment(ownerId, reservation) {
        return axios.post(API_URL + `${ownerId}/accept-payment`, reservation, { headers: authHeader() });
    }

    createExpo(ownerId, expo) {
        return axios.post(API_URL + `${ownerId}/create-expo`, expo, { headers: authHeader() });
    }

    editExpo(ownerId, edit) {
        return axios.post(API_URL + `${ownerId}/edit-expo`, edit, { headers: authHeader() });
    }

    startExpo(ownerId, expo) {
        return axios.post(API_URL + `${ownerId}/start-expo`, expo, { headers: authHeader() });
    }

    closeExpo(ownerId, expo) {
        return axios.post(API_URL + `${ownerId}/close-expo`, expo, { headers: authHeader() });
    }

    payForExpo(ownerId, expo) {
        return axios.post(API_URL + `${ownerId}/pay-for-expo`, expo, { headers: authHeader() });
    }

    getAllExpos(ownerId) {
        return axios.get(API_URL + `${ownerId}/all-expos`, { headers: authHeader() });
    }

    getNewExpos(ownerId) {
        return axios.get(API_URL + `${ownerId}/new-expos`, { headers: authHeader() });
    }

    getOpenedExpos(ownerId) {
        return axios.get(API_URL + `${ownerId}/opened-expos`, { headers: authHeader() });
    }

    getClosedExpos(ownerId) {
        return axios.get(API_URL + `${ownerId}/closed-expos`, { headers: authHeader() });
    }

}

export default new OwnerService();