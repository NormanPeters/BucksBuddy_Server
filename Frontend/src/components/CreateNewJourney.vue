<!--CreateNewJourney-->
<script setup lang="ts">
import {ref, watch} from 'vue';
import api from "@/services/api";
import type {Journey} from "@/types";
import {useRouter} from "vue-router";

const name = ref('');
const homeCurr = ref('');
const vacCurr = ref('');
const budget = ref(0);
const startDate = ref('');
const endDate = ref('');
const travelDuration = ref('');
const dateError = ref('');
const uuid = localStorage.getItem('UUID');
const router = useRouter();

const successMessage = ref<string | null>(null);
const errorMessage = ref<string | null>(null);

const addJourney = async () => {
  successMessage.value = null;
  errorMessage.value = null;

  const newJourney: Omit<Journey, 'id'> = {
    name: name.value,
    homeCurr: homeCurr.value,
    vacCurr: vacCurr.value,
    budget: budget.value,
    startDate: new Date(startDate.value),
    endDate: new Date(endDate.value),
    travelDuration: travelDuration.value,
  };
  try {
    if (uuid === null) {
      throw new Error('UUID is null');
    }
    const response = await api.createJourney(uuid, newJourney);
    successMessage.value = 'New journey created successfully!';
    setTimeout(async () => {
      await router.push('/main');
    }, 1000);
  } catch (error) {
    errorMessage.value = 'Failed to create new journey. Please try again.';
  }
};

const calculateDuration = () => {
  if (startDate.value && endDate.value) {
    const start = new Date(startDate.value);
    const end = new Date(endDate.value);

    if (start < end) {
      const difference = end.getTime() - start.getTime();
      const days = Math.ceil(difference / (1000 * 3600 * 24));
      travelDuration.value = `${days} day(s)`;
      dateError.value = '';
    } else {
      travelDuration.value = '';
      dateError.value = 'Start date must be before the end date.';
    }
  } else {
    travelDuration.value = '';
    dateError.value = '';
  }
};

watch([startDate, endDate], calculateDuration);
</script>

<template>
  <div class="card shadow m-3 p-3">
    <div class="card-body">
      <div class="row justify-content-center">
        <h3 class="text-center mb-4">Create New Journey</h3>
        <form @submit.prevent="addJourney">
          <div class="mb-3">
            <label for="name" class="form-label">Name</label>
            <input type="text" class="form-control" id="name" v-model="name" required>
          </div>
          <div class="mb-3">
            <label for="homeCurrency" class="form-label">Home Currency</label>
            <div class="col text-end">
              <select class="form-select" v-model="homeCurr">
                <option disabled value="">Please select one</option>
                <option value="EUR">Euro</option>
                <option value="USD">US Dollar</option>
                <option value="JPY">Japanese Yen</option>
                <option value="BGN">Bulgarian Lev</option>
                <option value="CZK">Czech Republic Koruna</option>
                <option value="DKK">Danish Krone</option>
                <option value="GBP">British Pound Sterling</option>
                <option value="HUF">Hungarian Forint</option>
                <option value="PLN">Polish Zloty</option>
                <option value="RON">Romanian Leu</option>
                <option value="SEK">Swedish Krona</option>
                <option value="CHF">Swiss Franc</option>
                <option value="ISK">Icelandic Króna</option>
                <option value="NOK">Norwegian Krone</option>
                <option value="HRK">Croatian Kuna</option>
                <option value="RUB">Russian Ruble</option>
                <option value="TRY">Turkish Lira</option>
                <option value="AUD">Australian Dollar</option>
                <option value="BRL">Brazilian Real</option>
                <option value="CAD">Canadian Dollar</option>
                <option value="CNY">Chinese Yuan</option>
                <option value="HKD">Hong Kong Dollar</option>
                <option value="IDR">Indonesian Rupiah</option>
                <option value="ILS">Israeli New Sheqel</option>
                <option value="INR">Indian Rupee</option>
                <option value="KRW">South Korean Won</option>
                <option value="MXN">Mexican Peso</option>
                <option value="MYR">Malaysian Ringgit</option>
                <option value="NZD">New Zealand Dollar</option>
                <option value="PHP">Philippine Peso</option>
                <option value="SGD">Singapore Dollar</option>
                <option value="THB">Thai Baht</option>
                <option value="ZAR">South African Rand</option>
              </select>
            </div>
          </div>
          <div class="mb-3">
            <label for="vacationCurrency" class="form-label">Vacation Currency</label>
            <div class="col text-end">
              <select class="form-select" v-model="vacCurr">
                <option disabled value="">Please select one</option>
                <option value="EUR">Euro</option>
                <option value="USD">US Dollar</option>
                <option value="JPY">Japanese Yen</option>
                <option value="BGN">Bulgarian Lev</option>
                <option value="CZK">Czech Republic Koruna</option>
                <option value="DKK">Danish Krone</option>
                <option value="GBP">British Pound Sterling</option>
                <option value="HUF">Hungarian Forint</option>
                <option value="PLN">Polish Zloty</option>
                <option value="RON">Romanian Leu</option>
                <option value="SEK">Swedish Krona</option>
                <option value="CHF">Swiss Franc</option>
                <option value="ISK">Icelandic Króna</option>
                <option value="NOK">Norwegian Krone</option>
                <option value="HRK">Croatian Kuna</option>
                <option value="RUB">Russian Ruble</option>
                <option value="TRY">Turkish Lira</option>
                <option value="AUD">Australian Dollar</option>
                <option value="BRL">Brazilian Real</option>
                <option value="CAD">Canadian Dollar</option>
                <option value="CNY">Chinese Yuan</option>
                <option value="HKD">Hong Kong Dollar</option>
                <option value="IDR">Indonesian Rupiah</option>
                <option value="ILS">Israeli New Sheqel</option>
                <option value="INR">Indian Rupee</option>
                <option value="KRW">South Korean Won</option>
                <option value="MXN">Mexican Peso</option>
                <option value="MYR">Malaysian Ringgit</option>
                <option value="NZD">New Zealand Dollar</option>
                <option value="PHP">Philippine Peso</option>
                <option value="SGD">Singapore Dollar</option>
                <option value="THB">Thai Baht</option>
                <option value="ZAR">South African Rand</option>
              </select>
            </div>
          </div>
          <div class="mb-3">
            <label for="budget" class="form-label">Budget (optional)</label>
            <input type="number" class="form-control" id="budget" v-model.number="budget">
          </div>
          <hr>
          <div class="mb-3">
            <label for="startDate" class="form-label">Start Date</label>
            <input type="date" class="form-control" id="startDate" v-model="startDate" required>
          </div>
          <div class="mb-3">
            <label for="endDate" class="form-label">End Date</label>
            <input type="date" class="form-control" id="endDate" v-model="endDate" required>
            <div v-if="dateError" class="text-danger">{{ dateError }}</div>
          </div>
          <div class="mb-3">
            <label class="form-label">Travel Duration</label>
            <input type="text" class="form-control" :value="travelDuration" disabled>
          </div>
          <div class="mb-3">
            <button type="submit" class="btn btn-primary">Submit</button>
          </div>
        </form>
        <div v-if="successMessage" class="alert alert-success mt-3">{{ successMessage }}</div>
        <div v-if="errorMessage" class="alert alert-danger mt-3">{{ errorMessage }}</div>
      </div>
    </div>
  </div>
</template>

