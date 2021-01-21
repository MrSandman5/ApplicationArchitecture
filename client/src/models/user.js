export default class User {
    constructor(login, email, password, firstName, lastName, role = 'client') {
        this.login = login;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}