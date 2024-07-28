<!--src/App.vue-->

<script setup lang="ts">
import {computed} from 'vue';
import {useRoute} from 'vue-router';
import NavBar from "@/components/NavBar.vue";

const route = useRoute();
const isAuthPage = computed(() => ['login', 'signup'].includes(route.name as string));
</script>

<template>
  <div class="main-content">
    <NavBar v-if="!isAuthPage"/>
    <router-view/>
  </div>
</template>
