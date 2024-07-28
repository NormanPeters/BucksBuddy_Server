<!--src/views/Main.vue-->

<script setup lang="ts">
import {ref} from 'vue';
import TotalExpenditures from "@/components/expenses/JourneyCard.vue";
import History from "@/components/expenses/History.vue";
import InputForm from "@/components/expenses/InputForm.vue";

const historyComponent = ref();

const handleRefreshExpenditures = () => {
  if (historyComponent.value) {
    historyComponent.value.fetchExpenditures();
  }
};
</script>

<template>
  <div class="container">
    <div class="row">
      <div class="col-lg-6 col-md-9 col-sm-12">
        <total-expenditures/>
        <input-form @refreshExpenditures="handleRefreshExpenditures"/>
      </div>
      <div class="col-lg-6 col-md-9 col-sm-12">
        <history ref="historyComponent"/>
      </div>
    </div>
  </div>
</template>

