<script setup lang="ts">
import {reactive, ref} from 'vue';
import api from "@/services/api";

interface UserData {
  email: string;
  newPassword: string;
  confirmPassword: string;
}

const userData = reactive<UserData>({
  email: localStorage.getItem('email') || '',
  newPassword: '',
  confirmPassword: ''
});

const showChangePassword = ref(false);
const passwordErrorMessage = ref<string | null>(null);
const passwordSuccessMessage = ref<string | null>(null);
const deleteErrorMessage = ref<string | null>(null);
const deleteSuccessMessage = ref<string | null>(null);

function toggleChangePassword() {
  showChangePassword.value = !showChangePassword.value;
}

const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;

const submitNewPassword = async () => {
  const uuid = localStorage.getItem('UUID');
  passwordErrorMessage.value = null;
  passwordSuccessMessage.value = null;

  if (uuid) {
    if (!passwordPattern.test(userData.newPassword)) {
      passwordErrorMessage.value = 'Invalid password format. Password must be at least 8 characters long, contain an uppercase letter, a lowercase letter, and a number.';
      return;
    }

    if (userData.newPassword !== userData.confirmPassword) {
      passwordErrorMessage.value = 'Passwords do not match';
      return;
    }

    try {
      const payload = {newPassword: userData.newPassword};
      await api.changePassword(uuid, payload);
      passwordSuccessMessage.value = 'Password changed successfully, please log in again!';
      setTimeout(() => {
        location.href = '/#/login';
      }, 2000);
    } catch (error: any) {
      passwordErrorMessage.value = 'Error changing password. Please try again.';
    }
  } else {
    passwordErrorMessage.value = 'UUID not found in localStorage';
  }
}

function confirmDeleteUser() {
  if (confirm("Are you sure you want to delete your account? This action cannot be undone.")) {
    deleteUser();
  }
}

const deleteUser = async () => {
  const uuid = localStorage.getItem('UUID');
  deleteErrorMessage.value = null;
  deleteSuccessMessage.value = null;

  if (uuid) {
    try {
      await api.deleteUser(uuid);
      deleteSuccessMessage.value = 'User deleted successfully';
      localStorage.clear();
      setTimeout(() => {
        location.href = '/#/login';
      }, 2000);
    } catch (error) {
      deleteErrorMessage.value = 'Error deleting user. Please try again.';
    }
  } else {
    deleteErrorMessage.value = 'UUID not found in localStorage';
  }
}
</script>

<template>
  <div class="card shadow m-3 p-3">
    <div class="card-body">
      <h3 class="text-center mb-4">User Settings</h3>
      <div class="mb-3">
        <label for="userEmail" class="form-label">Email</label>
        <input type="email" class="form-control" id="userEmail" v-model="userData.email" disabled>
      </div>
      <div>
        <button class="btn btn-secondary mb-3 custom-width-btn" @click="toggleChangePassword">
          {{ showChangePassword ? 'Hide' : 'Change Password' }}
        </button>
        <div v-if="showChangePassword">
          <form @submit.prevent="submitNewPassword">
            <div class="mb-3">
              <label for="newPassword" class="form-label">New Password</label>
              <input type="password" class="form-control" id="newPassword" v-model="userData.newPassword" required>
            </div>
            <div class="mb-3">
              <label for="confirmPassword" class="form-label">Confirm Password</label>
              <input type="password" class="form-control" id="confirmPassword" v-model="userData.confirmPassword"
                     required>
            </div>
            <div class="text-center mb-3">
              <button type="submit" class="btn btn-primary custom-width-btn">Change Password</button>
            </div>
          </form>
          <div v-if="passwordErrorMessage" class="alert alert-danger mt-3">{{ passwordErrorMessage }}</div>
          <div v-if="passwordSuccessMessage" class="alert alert-success mt-3">{{ passwordSuccessMessage }}</div>
        </div>
      </div>
      <hr>
      <div class="text-center text-danger">
        <h5>Danger Zone</h5>
        <p>Deleting your account is permanent and cannot be undone.</p>
      </div>
      <div class="text-center mb-3">
        <button @click="confirmDeleteUser" class="btn btn-danger custom-width-btn">Delete User</button>
      </div>
      <div v-if="deleteErrorMessage" class="alert alert-danger mt-3">{{ deleteErrorMessage }}</div>
      <div v-if="deleteSuccessMessage" class="alert alert-success mt-3">{{ deleteSuccessMessage }}</div>
    </div>
  </div>
</template>

<style>
.custom-width-btn {
  width: 100%;
}
</style>
