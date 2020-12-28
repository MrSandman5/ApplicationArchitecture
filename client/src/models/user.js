export default class User {
    constructor(login, email, password, role = 'client') {
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}