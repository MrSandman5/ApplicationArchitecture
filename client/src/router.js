import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import SignIn from "@/components/SignIn";
import SignUp from "@/components/SignUp";
import AdminPage from "@/components/AdminPage";
import ClientPage from "@/components/ClientPage";
import OwnerPage from "@/components/OwnerPage";
import ArtistPage from "@/components/ArtistPage";

Vue.use(Router)

export default new Router({
    mode: 'history',
    routes: [
        {
            path: '/',
            name: 'Home',
            component: Home
        },
        {
            path: '/home',
            name: 'Home',
            component: Home
        },
        {
            path: '/login',
            name: 'SignIn',
            component: SignIn
        },
        {
            path: '/sign-up',
            name: 'SignUp',
            component: SignUp
        },
        {
            path: '/admin',
            name: 'AdminPage',
            component: AdminPage
        },
        {
            path: '/client',
            name: 'ClientPage',
            component: ClientPage
        },
        {
            path: '/owner',
            name: 'OwnerPage',
            component: OwnerPage
        },
        {
            path: '/artist',
            name: 'ArtistPage',
            component: ArtistPage
        },
    ]
})