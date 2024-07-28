<!--History-->
<script setup lang="ts">
import {ref, onMounted, watch} from 'vue';
import api from '@/services/api';
import {type Expenditure} from '@/types';
import eventBus from '@/services/eventBus';

const expendituresList = ref<Expenditure[]>([]);
const sortedExpenditures = ref<Expenditure[]>([]);
const journeyId = ref<number | null>(Number(localStorage.getItem('selectedJourney')));
const vacCurrency = ref<string>('');
const homeCurrency = ref<string>('');
const exchangeRate = ref<number | null>(null);

const sortCriteria = ref<string>('name');
const sortOrder = ref<string>('asc');

const fetchExpenditures = async () => {
  if (journeyId.value === null) {
    console.error('Cannot fetch expenditures: journeyId is null');
    return;
  }

  try {
    const response = await api.getAllExpenditures(journeyId.value);
    expendituresList.value = response.data.map((expenditure: Expenditure) => ({
      ...expenditure,
      isEditing: false,
    }));
    sortExpenditures();
    const homeCurrencyResponse = await api.getHomeCurrency(journeyId.value)
    homeCurrency.value = homeCurrencyResponse.data;
    const vacCurrencyResponse = await api.getVacCurrency(journeyId.value)
    vacCurrency.value = vacCurrencyResponse.data;
  } catch (error) {
    console.error('Error fetching expenditures:', error);
  }
};

const clearExpenditures = () => {
  expendituresList.value = [];
  sortedExpenditures.value = [];
  vacCurrency.value = '';
  homeCurrency.value = '';
};

const deleteExpenditure = async (id: number) => {
  try {
    if (journeyId.value === null) {
      console.error('Cannot delete expenditure: journeyId is null');
      return;
    }
    await api.deleteExpenditure(journeyId.value, id);
    await fetchExpenditures();
    eventBus.emit('expenditureDeleted', journeyId.value);
  } catch (error) {
    console.error(error);
  }
};

const editExpenditure = (id: number) => {
  const index = expendituresList.value.findIndex(item => item.id === id);
  if (index !== -1) {
    expendituresList.value[index].isEditing = true;
  }
};

const saveExpenditure = async (id: number, updatedExpenditure: Expenditure) => {
  try {
    if (journeyId.value === null) {
      console.error('Cannot save expenditure: journeyId is null');
      return;
    }
    await api.updateExpenditure(journeyId.value, id, updatedExpenditure);
    await fetchExpenditures();
    eventBus.emit('expenditureUpdated', journeyId.value);
  } catch (error) {
    console.error(error);
  }
};

const amountInHomeCurrency = (amount: number): string => {
  return exchangeRate.value !== null ? (amount / exchangeRate.value).toFixed(2) : 'N/A';
};

const sortExpenditures = () => {
  sortedExpenditures.value = [...expendituresList.value].sort((a, b) => {
    let comparison = 0;
    if (sortCriteria.value === 'amount') {
      comparison = a.amount - b.amount;
    } else if (sortCriteria.value === 'date') {
      comparison = new Date(a.date).getTime() - new Date(b.date).getTime();
    } else {
      comparison = a.name.localeCompare(b.name);
    }

    return sortOrder.value === 'asc' ? comparison : -comparison;
  });
};

watch([expendituresList, sortCriteria, sortOrder], sortExpenditures);

onMounted(() => {
  eventBus.on('journeyIdChanged', (newJourneyId: number | null) => {
    if (newJourneyId !== null) {
      journeyId.value = newJourneyId;
      clearExpenditures();
      fetchExpenditures();
    } else {
      clearExpenditures();
    }
  });

  eventBus.on('exchangeRateUpdated', (rate: number) => {
    exchangeRate.value = rate;
  });

  eventBus.on('vacCurrencyUpdated', (vacCurrencyName: string) => {
    vacCurrency.value = vacCurrencyName;
  });

  eventBus.on('homeCurrencyUpdated', (homeCurrencyName: string) => {
    homeCurrency.value = homeCurrencyName;
  });

  if (journeyId.value !== null) {
    fetchExpenditures();
  }
});

defineExpose({
  fetchExpenditures
});

const formatDate = (dateString: Date): string => {
  const date = new Date(dateString);
  const day = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const year = date.getFullYear();
  return `${day}.${month}.${year}`;
};

const formatAmount = (amount: number): string => {
  return amount.toFixed(2);
};
</script>

<template>
  <div class="card shadow mb-3">
    <div class="card-body">
      <h3 class="card-title">History</h3>

      <!-- Sort Controls -->
      <div class="row mb-2">
        <div class="col">
          <label for="sortCriteria" class="form-label">Sort By</label>
          <select id="sortCriteria" v-model="sortCriteria" class="form-select">
            <option value="name">Name</option>
            <option value="amount">Amount</option>
            <option value="date">Date</option>
          </select>
        </div>
        <div class="col">
          <label for="sortOrder" class="form-label">Order</label>
          <select id="sortOrder" v-model="sortOrder" class="form-select">
            <option value="asc">Ascending</option>
            <option value="desc">Descending</option>
          </select>
        </div>
      </div>

      <div class="historycard mb-2" v-for="item in sortedExpenditures" :key="item.id">
        <div class="historycard-body d-flex align-items-center p-1 ms-2">
          <div class="col-3 fw-bold" v-if="!item.isEditing">
            {{ item.name }}
          </div>
          <div class="col-3" v-else>
            <input v-model="item.name" class="form-control"/>
          </div>

          <div class="col-2 text-center" v-if="!item.isEditing">
            {{ formatAmount(item.amount) }} {{ vacCurrency }}
          </div>
          <div class="col-3" v-else>
            <input v-model="item.amount" type="number" class="form-control ms-2"/>
          </div>

          <div class="col-2 text-center" v-if="!item.isEditing">
            {{ amountInHomeCurrency(item.amount) }} {{ homeCurrency }}
          </div>

          <div class="col-2 text-center" v-if="!item.isEditing">
            {{ formatDate(item.date) }}
          </div>
          <div class="col-3" v-else>
            <input v-model="item.date" type="date" class="form-control ms-3"/>
          </div>

          <div class="col-3 d-flex justify-content-end">
            <button
                class="btn bi bi-pencil-square fs-5"
                title="edit"
                v-if="!item.isEditing"
                @click="editExpenditure(item.id)">
            </button>
            <button
                class="btn bi bi-save fs-5"
                title="save"
                v-else
                @click="saveExpenditure(item.id, item)">
            </button>
            <button
                class="btn bi bi-trash fs-5"
                title="delete"
                @click="deleteExpenditure(item.id)">
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.historycard {
  background-color: var(--bs-body-bg);
  border: 1px solid; /* Fügt einen festen Rahmen hinzu */
  border-color: var(--bs-border-color); /* Setzt die Rahmenfarbe */
  border-radius: 5px;
}
</style>
