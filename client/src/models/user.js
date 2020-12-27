export default class User {
    constructor(username, email, password, role = 'candidate') {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}