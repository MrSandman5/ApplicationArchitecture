import Vue from 'vue';
import Vuex from 'vuex';

import {auth} from './auth.module';

Vue.use(Vuex);

export const store = Vuex.Store({
    modules: {
        auth
    }
});