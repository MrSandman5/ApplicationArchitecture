import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/views/Home'
import Login from "@/views/user/Login";
import Register from "@/views/user/Register";
/*import AdminPage from "@/components/AdminPage";
import ClientPage from "@/components/ClientPage";
import OwnerPage from "@/components/OwnerPage";
import ArtistPage from "@/components/ArtistPage";*/

Vue.use(Router)

export const router = new Router({
    mode: 'history',
    routes: [
        {
            path: '/',
            name: 'Default',
            component: Home
        },
        {
            path: '/home',
            name: 'Home',
            component: Home
        },
        {
            path: '/login',
            name: 'Login',
            component: Login
        },
        {
            path: '/register',
            name: 'Register',
            component: Register
        }
        /*{
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
        },*/
    ]
})

/*router.beforeEach((to, from, next) => {
    const publicPages = ['/login', '/register', '/home'];
    const authRequired = !publicPages.includes(to.path);
    const loggedIn = localStorage.getItem('user');

    if (authRequired && !loggedIn) {
        next('/login');
    } else {
        next();
    }
});*/