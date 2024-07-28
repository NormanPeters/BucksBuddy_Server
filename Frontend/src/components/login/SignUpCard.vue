<!--SignUpCard-->
<script setup lang="ts">
import {ref} from 'vue';
import {useRouter} from 'vue-router';
import api from '@/services/api';

interface SignupData {
  email: string;
  password: string;
  confirmPassword: string;
}

const signupData = ref<SignupData>({
  email: '',
  password: '',
  confirmPassword: ''
});

const successMessage = ref<string | null>(null);
const errorMessage = ref<string | null>(null);
const router = useRouter();

const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

const signup = async () => {
  successMessage.value = null;
  errorMessage.value = null;

  if (!emailPattern.test(signupData.value.email)) {
    errorMessage.value = 'Invalid email format';
    return;
  }

  if (signupData.value.password !== signupData.value.confirmPassword) {
    errorMessage.value = 'Passwords do not match';
    return;
  }

  const newUser = {
    email: signupData.value.email,
    password: signupData.value.password
  };

  try {
    const response = await api.createUser(newUser);
    successMessage.value = 'User created successfully';
    setTimeout(() => {
      router.push('/login');
    }, 2000);
  } catch (error: any) {
    if (error.response && error.response.status === 400) {
      errorMessage.value = 'Invalid input: ' + JSON.stringify(error.response.data);
    } else if (error.response && error.response.status === 409) {
      errorMessage.value = 'E-Mail is already registered';
    } else {
      errorMessage.value = 'An error occurred';
    }
  }
};
</script>

<template>
  <div class="card shadow m-3 p-3">
    <div class="card-body">
      <div class="d-flex justify-content-center align-items-center">
        <img src="@/assets/logo.png" alt="Bootstrap" style="width: auto; height: auto;">
      </div>
      <form @submit.prevent="signup">
        <div class="mb-3">
          <label for="email" class="form-label">Email</label>
          <input type="email" class="form-control" id="email" v-model="signupData.email" required>
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">Password</label>
          <input type="password" class="form-control" id="password" v-model="signupData.password" required>
        </div>
        <div class="mb-3">
          <label for="confirmPassword" class="form-label">Confirm Password</label>
          <input type="password" class="form-control" id="confirmPassword" v-model="signupData.confirmPassword"
                 required>
        </div>
        <div class="text-center mb-3">
          <button type="submit" class="btn btn-primary custom-width-btn">Sign up</button>
        </div>
      </form>
      <hr>
      <div class="text-center">
        <a href="#/login" class="text-decoration-none">Already have an account? Log in</a>
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
