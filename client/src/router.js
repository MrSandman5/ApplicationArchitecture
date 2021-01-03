import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/views/Home'
import Login from "@/views/user/Login";
import Register from "@/views/user/Register";
import Tickets from "@/views/client/Tickets";
import Reservations from "@/views/client/Reservations";
import AvailableExpos from "@/views/client/AvailableExpos";
import Artworks from "@/views/artist/Artworks";
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
    },
    {
      path: '/profile',
      name: 'profile',
      // lazy-loaded
      component: () => import('./views/user/Profile.vue')
    },
    {
      path: '/tickets',
      name: 'Tickets',
      component: Tickets
    },
    {
      path: '/reservations',
      name: 'Reservations',
      component: Reservations
    },
    {
      path: '/expos',
      name: 'Expos',
      component: AvailableExpos
    },
    {
      path: '/opened-expos',
      name: 'OpenedExpos',
      // lazy-loaded
      component: () => import('./views/owner/OpenedExpos.vue')
    },
    {
      path: '/closed-expos',
      name: 'ClosedExpos',
      // lazy-loaded
      component: () => import('./views/owner/ClosedExpos.vue')
    },
    {
      path: '/new-expos',
      name: 'NewExpos',
      // lazy-loaded
      component: () => import('./views/owner/NewExpos.vue')
    },
    {
      path: '/artworks',
      name: 'Artworks',
      component: Artworks
    }
    /*,
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
});

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