<!--JourneyCard-->
<script setup lang="ts">
import {computed, onMounted, ref, watch} from 'vue';
import api from '@/services/api';
import type {Expenditure, Journey} from '@/types';
import Freecurrencyapi from "@everapi/freecurrencyapi-js";
import eventBus from '@/services/eventBus';

const expendituresList = ref<Expenditure[]>([]);
const currencyapi = new Freecurrencyapi(import.meta.env.VITE_API_KEY);
const journeys = ref<Journey[]>([]);
const selectedJourneyId = ref<number | null>(Number(localStorage.getItem('selectedJourney')));
const uuid = localStorage.getItem('UUID') || 'default-uuid';
const homeCurrency = ref<string>('');
const vacCurrency = ref<string>('');
const budget = ref<number>(0);
const exchangeRate = ref<number | null>(null);
const startDate = ref<Date | null>(null);
const endDate = ref<Date | null>(null);

const currencyNames: Record<string, string> = {
  EUR: 'Euro',
  USD: 'US Dollar',
  JPY: 'Japanese Yen',
  BGN: 'Bulgarian Lev',
  CZK: 'Czech Republic Koruna',
  DKK: 'Danish Krone',
  GBP: 'British Pound Sterling',
  HUF: 'Hungarian Forint',
  PLN: 'Polish Zloty',
  RON: 'Romanian Leu',
  SEK: 'Swedish Krona',
  CHF: 'Swiss Franc',
  ISK: 'Icelandic Króna',
  NOK: 'Norwegian Krone',
  HRK: 'Croatian Kuna',
  RUB: 'Russian Ruble',
  TRY: 'Turkish Lira',
  AUD: 'Australian Dollar',
  BRL: 'Brazilian Real',
  CAD: 'Canadian Dollar',
  CNY: 'Chinese Yuan',
  HKD: 'Hong Kong Dollar',
  IDR: 'Indonesian Rupiah',
  ILS: 'Israeli New Sheqel',
  INR: 'Indian Rupee',
  KRW: 'South Korean Won',
  MXN: 'Mexican Peso',
  MYR: 'Malaysian Ringgit',
  NZD: 'New Zealand Dollar',
  PHP: 'Philippine Peso',
  SGD: 'Singapore Dollar',
  THB: 'Thai Baht',
  ZAR: 'South African Rand',
};

const fetchExpenditures = async (journeyId: number) => {
  try {
    const response = await api.getAllExpenditures(journeyId);
    expendituresList.value = response.data;
  } catch (error) {
    console.error('Error fetching expenditures:', error);
  }
};

const confirmAndDeleteJourney = async (journeyId: number | null) => {
  if (journeyId === null) return;

  const confirmDelete = confirm('Are you sure you want to delete this journey?');
  if (!confirmDelete) return;

  try {
    await api.deleteJourney(journeyId);
    alert('Journey deleted successfully');
    await fetchJourneys();
    selectedJourneyId.value = null;
    localStorage.removeItem('selectedJourney');
    eventBus.emit('journeyIdChanged', null);
  } catch (error) {
    console.error('Error deleting journey:', error);
  }
};

watch(selectedJourneyId, async (newVal) => {
  if (newVal !== null) {
    localStorage.setItem('selectedJourney', newVal.toString());
    console.log('selectedJourneyIdnewVal:', newVal);
    console.log('selectedJourneyId:', localStorage.getItem('selectedJourney'));
    expendituresList.value = [];
    await fetchJourneyDetails(newVal);
    await fetchExpenditures(newVal);
    eventBus.emit('journeyIdChanged', newVal);
  } else {
    localStorage.removeItem('selectedJourney');
    eventBus.emit('journeyIdChanged', null);
  }
});

const fetchJourneyDetails = async (journeyId: number) => {
  try {
    const journeyResponse = await api.getJourneyById(journeyId);
    const journey = journeyResponse.data;
    homeCurrency.value = await api.getHomeCurrency(journeyId).then(response => response.data);
    vacCurrency.value = await api.getVacCurrency(journeyId).then(response => response.data);
    budget.value = await api.getBudget(journeyId).then(response => response.data);
    startDate.value = new Date(journey.startDate);
    endDate.value = new Date(journey.endDate);
    exchangeRate.value = await currencyapi.latest({
      base_currency: homeCurrency.value,
      currencies: vacCurrency.value
    }).then((response: any) => response.data[vacCurrency.value]);
    eventBus.emit('exchangeRateUpdated', exchangeRate.value);
  } catch (error) {
    console.error('Error fetching journey details:', error);
  }
};

const fetchJourneys = async () => {
  try {
    if (!uuid) {
      console.error('UUID is missing');
      return;
    }
    const response = await api.getAllJourneys(uuid);
    if (response.data && Array.isArray(response.data)) {
      journeys.value = response.data;
    } else {
      console.error('Unexpected API response:', response);
    }
  } catch (error) {
    console.error('Error fetching journeys:', error);
  }
};

const totalExpenditures = computed(() => {
  return expendituresList.value.reduce((sum, expenditure) => sum + expenditure.amount, 0);
});

const getCurrencyName = (code: string) => {
  return code;
};

const formatExchangeRate = (rate: number | null) => {
  return rate !== null ? rate.toFixed(2) : 'N/A';
};

const totalExpensesInHomeCurrency = computed(() => {
  if (typeof exchangeRate.value === 'number' && typeof totalExpenditures.value === 'number') {
    return parseFloat((totalExpenditures.value / exchangeRate.value).toFixed(2));
  } else {
    return 0;
  }
});

const budgetInVacationCurrency = computed(() => {
  if (typeof exchangeRate.value === 'number' && typeof budget.value === 'number') {
    return parseFloat((budget.value * exchangeRate.value).toFixed(2));
  } else {
    return 0;
  }
});

const budgetLeftInVacationCurrency = computed(() => {
  if (typeof exchangeRate.value === 'number' && typeof totalExpenditures.value === 'number') {
    const budgetInVacCurr = parseFloat((budget.value * exchangeRate.value).toFixed(2));
    return parseFloat((budgetInVacCurr - totalExpenditures.value).toFixed(2));
  } else {
    return 0;
  }
});

const budgetLeftInHomeCurrency = computed(() => {
  if (typeof budget.value === 'number' && typeof totalExpensesInHomeCurrency.value === 'number') {
    return parseFloat((budget.value - totalExpensesInHomeCurrency.value).toFixed(2));
  } else {
    return 0;
  }
});

const travelDurationInDays = computed(() => {
  if (startDate.value && endDate.value) {
    const start = new Date(startDate.value).getTime();
    const end = new Date(endDate.value).getTime();
    const diff = end - start;
    return diff > 0 ? diff / (1000 * 3600 * 24) : 0;
  }
  return 0;
});

const averageExpenditurePerDayInVacationCurrency = computed(() => {
  const days = travelDurationInDays.value;
  return days > 0 ? (totalExpenditures.value / days).toFixed(2) : 'N/A';
});

const averageExpenditurePerDayInHomeCurrency = computed(() => {
  const days = travelDurationInDays.value;
  const totalExpenditureInHomeCurrency = totalExpensesInHomeCurrency.value;
  return days > 0 ? (totalExpenditureInHomeCurrency / days).toFixed(2) : 'N/A';
});

onMounted(async () => {
  await fetchJourneys();
  const storedJourneyId = Number(localStorage.getItem('selectedJourney'));
  if (storedJourneyId && journeys.value.some(j => j.id === storedJourneyId)) {
    selectedJourneyId.value = storedJourneyId;
    await fetchJourneyDetails(storedJourneyId);
    await fetchExpenditures(storedJourneyId);
  } else {
    localStorage.removeItem('selectedJourney');
    selectedJourneyId.value = null;
    eventBus.emit('journeyIdChanged', null);
  }

  eventBus.on('expenditureAdded', async () => {
    if (selectedJourneyId.value !== null) {
      await fetchJourneyDetails(selectedJourneyId.value);
      await fetchExpenditures(selectedJourneyId.value);
    }
  });

  eventBus.on('expenditureDeleted', async () => {
    if (selectedJourneyId.value !== null) {
      await fetchJourneyDetails(selectedJourneyId.value);
      await fetchExpenditures(selectedJourneyId.value);
    }
  });

  eventBus.on('expenditureUpdated', async () => {
    if (selectedJourneyId.value !== null) {
      await fetchJourneyDetails(selectedJourneyId.value);
      await fetchExpenditures(selectedJourneyId.value);
    }
  });
});
</script>

<template>
  <div class="card shadow mb-3">
    <div class="card-body">
      <div class="d-flex justify-content-between align-items-center">
        <h3>Journey</h3>
        <div class="d-flex align-items-center">
          <select class="form-select me-2" v-model="selectedJourneyId">
            <option disabled value="">Please select one</option>
            <option v-for="journey in journeys" :key="journey.id" :value="journey.id">{{ journey.name }}</option>
          </select>
          <button class="btn bi bi-trash fs-5" @click="confirmAndDeleteJourney(selectedJourneyId)"></button>
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col text-start">
          <h4>Home Currency</h4>
        </div>
        <div class="col text-end">
          <h4>{{ getCurrencyName(homeCurrency) }}</h4>
        </div>
      </div>
      <div class="row">
        <div class="col text-start">
          <h4>Vacation Currency</h4>
        </div>
        <div class="col text-end">
          <h4>{{ getCurrencyName(vacCurrency) }}</h4>
        </div>
      </div>
      <div class="row">
        <div class="col text-start">
          <h4>Budget</h4>
        </div>
        <div class="col text-end">
          <h4>{{ budget }} {{ getCurrencyName(homeCurrency) }} ({{ budgetInVacationCurrency }}
            {{ getCurrencyName(vacCurrency) }})</h4>
        </div>
      </div>
      <div class="row">
        <div class="col text-start">
          <h4>Exchange Rate</h4>
        </div>
        <div class="col text-end">
          <h4>{{ formatExchangeRate(exchangeRate) }}</h4>
        </div>
      </div>
      <div class="row">
        <div class="col text-start">
          <h4>Travel Duration</h4>
        </div>
        <div class="col text-end">
          <h4>{{ (travelDurationInDays) }} Days</h4>
        </div>
      </div>
      <hr>
      <h3>Total Expenses</h3>
      <div class="row">
        <div class="col text-start">
          <h4>{{ getCurrencyName(homeCurrency) }}</h4>
        </div>
        <div class="col text-end">
          <h4>{{ totalExpensesInHomeCurrency }} {{ getCurrencyName(homeCurrency) }}</h4>
        </div>
      </div>
      <div class="row">
        <div class="col text-start">
          <h4>{{ getCurrencyName(vacCurrency) }}</h4>
        </div>
        <div class="col text-end">
          <h4>{{ totalExpenditures }} {{ getCurrencyName(vacCurrency) }}</h4>
        </div>
      </div>
      <div class="row">
        <div class="col text-start">
          <h4>Budget Left</h4>
        </div>
        <div class="col text-end">
          <h4>{{ budgetLeftInHomeCurrency }} {{ getCurrencyName(homeCurrency) }} ({{ budgetLeftInVacationCurrency }}
            {{ getCurrencyName(vacCurrency) }})</h4>
        </div>
      </div>
      <div class="row">
        <div class="col text-start">
          <h4>Average Expenditures Per Day</h4>
        </div>
        <div class="col text-end">
          <h4>{{ averageExpenditurePerDayInHomeCurrency }} {{ getCurrencyName(homeCurrency) }}
            ({{ averageExpenditurePerDayInVacationCurrency }} {{ getCurrencyName(vacCurrency) }})</h4>
        </div>
      </div>
    </div>
  </div>
</template>
