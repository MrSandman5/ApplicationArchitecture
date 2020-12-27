import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/admin/';

class AdminService {

    deleteUser(userId, userType) {
        return axios.delete(API_URL + `deleteUser/${userId}/${userType}`, { headers: authHeader() });
    }

    generate(count) {
        return axios.get(API_URL + `generate/${count}`, { headers: authHeader() });
    }
}

export default new AdminService();