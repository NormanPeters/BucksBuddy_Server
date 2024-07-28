<!--NavBar.vue -->
<script setup lang="ts">
import {useThemeStore} from '@/stores/themeStore';
import {useRouter} from "vue-router";

const themeStore = useThemeStore();
const toggleTheme = () => {
  themeStore.applyTheme(themeStore.theme === 'light' ? 'dark' : 'light');
}

const router = useRouter();
const logout = () => {
  localStorage.removeItem('UUID')
  localStorage.removeItem('selectedJourney')
  router.push('/login');
}
</script>

<template>
  <nav class="navbar shadow rounded-3 m-3 navbar-expand bg-body-tertiary">
    <div class="container row-cols-2">
      <div class="col-2 d-flex align-items-center">
        <RouterLink to="/main">
          <img src="@/assets/logo2.png" alt="Bootstrap" style="width: auto; height: 70px;">
        </RouterLink>
      </div>
      <div class="col-10 d-flex justify-content-end">
        <ul class="navbar-nav ms-auto">
          <li class="nav-item">
            <RouterLink to="/user"
                        :class="['bi bi-person-circle m-2 fs-3 icon-link-hover', themeStore.theme === 'light' ? 'text-dark' : 'text-white']"
                        title="User">
            </RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink to="/newjourney"
                        :class="['bi bi-plus-lg m-2 fs-3 icon-link-hover', themeStore.theme === 'light' ? 'text-dark' : 'text-white']"
                        title="Create a new Journey">
            </RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink to="/main"
                        :class="['bi bi-card-list m-2 fs-3 icon-link-hover', themeStore.theme === 'light' ? 'text-dark' : 'text-white']"
                        title="Main">
            </RouterLink>
          </li>
          <li class="nav-item">
            <a @click="logout"
               :class="['bi bi-box-arrow-right m-2 fs-3 icon-link-hover', themeStore.theme === 'light' ? 'text-dark' : 'text-white']"
               title="Logout"
               style="cursor: pointer;"></a>
          </li>
          <li class="nav-item ms-5 d-flex align-items-center">
            <i :class="[themeStore.theme === 'light' ? 'bi bi-sun fs-3 text-dark' : 'bi bi-moon-stars fs-3 text-white']"></i>
            <i :class="[themeStore.theme === 'light' ? 'bi bi-toggle-off fs-3 mx-2 text-dark' : 'bi bi-toggle-on fs-3 mx-2 text-white']"
               @click="toggleTheme"></i>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>
