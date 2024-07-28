<!--InputForm-->
<script setup lang="ts">
import {defineEmits, onMounted, ref} from 'vue';
import api from '@/services/api';
import {type Expenditure} from '@/types';
import eventBus from '@/services/eventBus';

const title = ref('');
const amount = ref<number | null>(null);
const date = ref<string>('');
const journeyId = ref<number | null>(Number(localStorage.getItem('selectedJourney')));
const emit = defineEmits(['refreshExpenditures']);
const exchangeRate = ref<number | null>(null);
const vacCurrency = ref<string>('');

const setDateToToday = () => {
  const now = new Date();
  const day = String(now.getDate()).padStart(2, '0');
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const year = now.getFullYear();
  date.value = `${year}-${month}-${day}`;
};

const addExpenditure = async () => {
  if (title.value && amount.value !== null && date.value) {
    const newExpenditure: Omit<Expenditure, 'id'> = {
      name: title.value,
      amount: amount.value,
      date: new Date(date.value),
      journeyId: journeyId.value!,
      isEditing: false
    };
    try {
      await api.createExpenditure(journeyId.value!, newExpenditure);
      title.value = '';
      amount.value = null;
      setDateToToday();
      emit('refreshExpenditures');
      eventBus.emit('expenditureAdded', null);
    } catch (error) {
      console.error(error);
    }
  }
};

onMounted(async () => {
  try {
    eventBus.on('journeyIdChanged', (newJourneyId: number | null) => {
      if (newJourneyId !== null) {
        journeyId.value = newJourneyId;
      }
    });
  } catch (error) {
    console.error('Error fetching journey details:', error);
  }
  eventBus.on('exchangeRateUpdated', (rate: number | null) => {
    exchangeRate.value = rate;
  });
  eventBus.on('vacCurrencyUpdated', (vacCurrencyName: string) => {
    vacCurrency.value = vacCurrencyName;
  });
  setDateToToday();
});
</script>

<template>
  <div class="card shadow mb-3">
    <div class="card-body">
      <h3 class="card-title">New Expense</h3>

      <div class="row mb-2">
        <div class="col">
          <label for="titleInput" class="form-label">Title</label>
          <input type="text" class="form-control" id="titleInput" v-model="title" @keyup.enter="addExpenditure">
        </div>
      </div>
      <div class="row mb-2">
        <div class="col">
          <label for="amountInput" class="form-label">Amount in {{ vacCurrency.valueOf() }}</label>
          <input type="number" class="form-control" id="amountInput" v-model.number="amount"
                 @keyup.enter="addExpenditure">
        </div>
        <div class="col">
          <label for="exchangeRateDisplay" class="form-label">Exchange Rate: </label>
          <br>
          <span id="exchangeRateDisplay">{{ exchangeRate }}</span>
        </div>
        <div class="col">
          <label for="dateInput" class="form-label">Date</label>
          <input type="date" class="form-control" id="dateInput" v-model="date" @keyup.enter="addExpenditure">
        </div>
      </div>
      <div class="row mb-2">
        <div class="col">
          <button type="button" class="btn btn-primary" @click="addExpenditure">Submit</button>
        </div>
      </div>
    </div>
  </div>
</template>
