// src/stores/themeStore.ts

import {defineStore} from 'pinia';
import {ref} from 'vue';

export const useThemeStore = defineStore('theme', () => {
    const theme = ref('dark');

    const applyTheme = (newTheme: string) => {
        theme.value = newTheme;
        document.documentElement.setAttribute('data-bs-theme', newTheme === 'dark' ? 'dark' : 'light');
        localStorage.setItem('theme', newTheme);
    };

    const loadTheme = () => {
        const savedTheme = localStorage.getItem('theme') || 'light';
        applyTheme(savedTheme);
    };

    return {theme, applyTheme, loadTheme};
});
