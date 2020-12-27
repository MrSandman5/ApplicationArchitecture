import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/';

class BackendService {

    getRoles() {
        return axios.get(API_URL + `roles`, { headers: authHeader() });
    }

    getUsers() {
        return axios.get(API_URL + `users`, { headers: authHeader() });
    }
}

export default new BackendService();