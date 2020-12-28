import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/client/';

class ClientService {

    getMe(clientId) {
        return axios.get(API_URL + clientId, {headers: authHeader()});
    }

    addTicket(clientId, expo) {
        return axios.post(API_URL + `${clientId}/add-ticket`, {expo}, { headers: authHeader() });
    }

    createReservation(clientId) {
        return axios.post(API_URL + `${clientId}/createReservation`, null, { headers: authHeader() });
    }

    payForReservation(clientId, payment) {
        return axios.post(API_URL + `${clientId}/pay`, {payment}, { headers: authHeader() });
    }

    getTickets(clientId) {
        return axios.get(API_URL + `${clientId}/tickets`, { headers: authHeader() });
    }

    getNewReservation(clientId) {
        return axios.get(API_URL + `${clientId}/new-reservations`, { headers: authHeader() });
    }

    getPayedReservation(clientId) {
        return axios.get(API_URL + `${clientId}/payed-reservations`, { headers: authHeader() });
    }

    getExpos(clientId) {
        return axios.get(API_URL + `${clientId}/expos`, { headers: authHeader() });
    }

}

export default new ClientService();