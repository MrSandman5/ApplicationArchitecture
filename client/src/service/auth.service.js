import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth/';

class AuthService {
    login(user) {
        return axios
            .post(API_URL + 'sign-in', {
                login: user.login,
                password: user.password
            })
            .then(response => {
                if (response.data.accessToken) {
                    localStorage.setItem('user', JSON.stringify(response.data));
                }

                return response.data;
            });
    }

    logout() {
        localStorage.removeItem('user');
    }

    register(user) {
        return axios.post(API_URL + 'sign-up', {
            login: user.login,
            password: user.password,
            email: user.email,
            roles: [user.role],
            firstName: user.firstName,
            lastName: user.lastName,
        });
    }
}

export default new AuthService();
