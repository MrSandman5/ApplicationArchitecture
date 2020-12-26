import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const state = {
    token: localStorage.getItem('user-token') || '',
    role: localStorage.getItem('user-role') || '',
    username: localStorage.getItem('user-name') || '',
    authorities: localStorage.getItem('authorities') || '',
};

const getters = {
    isAuthenticated: state => {
        return state.token != null && state.token !== '';
    },
    isAdmin: state => {
        return state.role === 'ROLE_ADMIN';
    },
    getUsername: state => {
        return state.username;
    },
    getAuthorities: state => {
        return state.authorities;
    },
    getToken: state => {
        return state.token;
    }
};

const mutations = {
    auth_login: (state, user) => {
        localStorage.setItem('user-token', user.token);
        localStorage.setItem('user-name', user.name);
        localStorage.setItem('user-authorities', user.roles);
        state.token = user.token;
        state.username = user.username;
        state.authorities = user.roles;
        let isAdmin = false;
        let isClient = false;
        let isOwner = false;
        let isArtist = false;
        for (let i = 0; i < user.roles.length; i++) {
            if (user.roles[i].authority === 'ROLE_ADMIN') {
                isAdmin = true;
            } else if (user.roles[i].authority === 'ROLE_CLIENT') {
                isClient = true;
            } else if (user.roles[i].authority === 'ROLE_OWNER') {
                isOwner = true;
            } else if (user.roles[i].authority === 'ROLE_ARTIST') {
                isArtist = true;
            }
        }
        if (isAdmin) {
            localStorage.setItem('user-role', 'ROLE_ADMIN');
            state.role = 'ROLE_ADMIN';
        } else if (isClient) {
            localStorage.setItem('user-role', 'ROLE_CLIENT');
            state.role = 'ROLE_CLIENT';
        } else if (isOwner) {
            localStorage.setItem('user-role', 'ROLE_OWNER');
            state.role = 'ROLE_OWNER';
        } else if (isArtist) {
            localStorage.setItem('user-role', 'ROLE_ARTIST');
            state.role = 'ROLE_ARTIST';
        }
    },
    auth_logout: () => {
        state.token = '';
        state.role = '';
        state.username = '';
        state.authorities = [];
        localStorage.removeItem('user-token');
        localStorage.removeItem('user-role');
        localStorage.removeItem('user-name');
        localStorage.removeItem('user-authorities');
    }
};

const actions = {
    login: (context, user) => {
        context.commit('auth_login', user)
    },
    logout: (context) => {
        context.commit('auth_logout');
    }
};

export const store = new Vuex.Store({
    state,
    getters,
    mutations,
    actions
});