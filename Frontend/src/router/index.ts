// src/router/index.ts

import {createRouter, createWebHashHistory} from 'vue-router';
import NewJourney from '@/views/NewJourney.vue';
import User from '@/views/User.vue';
import Main from '@/views/Main.vue';
import Login from '@/views/Login.vue';
import SignUp from '@/views/SignUp.vue';

const router = createRouter({
    history: createWebHashHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/login',
            name: 'login',
            component: Login
        },
        {
            path: '/signup',
            name: 'signup',
            component: SignUp
        },
        {
            path: '/newjourney',
            name: 'newjourney',
            component: NewJourney,
            meta: {requiresAuth: true}
        },
        {
            path: '/main',
            name: 'main',
            component: Main,
            meta: {requiresAuth: true}
        },
        {
            path: '/user',
            name: 'user',
            component: User,
            meta: {requiresAuth: true}
        }
    ]
});

router.beforeEach((to, from, next) => {
    const isAuthenticated = !!localStorage.getItem('UUID');

    if (to.meta.requiresAuth && !isAuthenticated) {
        next({name: 'login'});
    } else {
        next();
    }
});

export default router;
