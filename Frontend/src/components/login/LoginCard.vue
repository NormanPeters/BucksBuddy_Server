<!--LoginCard-->
<script setup lang="ts">
import {reactive, ref} from 'vue';
import {useRouter} from 'vue-router';
import api from '@/services/api';

const router = useRouter();
const loginData = reactive({
  email: '',
  password: ''
});

const errorMessage = ref<string | null>(null);
const successMessage = ref<string | null>(null);

const login = async () => {
  errorMessage.value = null;
  successMessage.value = null;

  try {
    const response = await api.login(loginData.email, loginData.password);
    localStorage.setItem('UUID', response.data);
    localStorage.setItem('email', loginData.email);
    successMessage.value = 'Login successful!';
    setTimeout(async () => {
      await router.push('/main');
    }, 1000);
  } catch (error) {
    errorMessage.value = 'Login failed. Please check your credentials and try again.';
  }
};
</script>

<template>
  <div class="card shadow m-3 p-3">
    <div class="card-body">
      <div class="d-flex justify-content-center align-items-center">
        <img src="@/assets/logo.png" alt="Bootstrap" style="width: auto; height: auto;">
      </div>
      <form @submit.prevent="login">
        <div class="mb-3">
          <label for="email" class="form-label">Email</label>
          <input type="email" class="form-control" id="email" v-model="loginData.email" required>
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">Password</label>
          <input type="password" class="form-control" id="password" v-model="loginData.password" required>
        </div>
        <div class="text-center mb-3">
          <button type="submit" class="btn btn-primary custom-width-btn">Log in</button>
        </div>
      </form>
      <hr>
      <div class="text-center mt-3">
        <a href="#/signup" class="btn btn-secondary custom-width-btn">Sign up</a>
      </div>
      <div v-if="successMessage" class="alert alert-success mt-3">{{ successMessage }}</div>
      <div v-if="errorMessage" class="alert alert-danger mt-3">{{ errorMessage }}</div>
    </div>
  </div>
</template>

<style scoped>
.custom-width-btn {
  width: 100%;
}
</style>
