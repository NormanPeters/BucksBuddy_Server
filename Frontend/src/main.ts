// src/main.ts

import './assets/styles.css'

import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import {createPinia} from 'pinia'

createApp(App).use(createPinia()).use(router).mount("#app")

import {useThemeStore} from './stores/themeStore';

const themeStore = useThemeStore();
themeStore.loadTheme();
