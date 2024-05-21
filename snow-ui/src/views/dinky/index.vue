<template>
  <div>
    <i-frame v-model:src="url"></i-frame>
  </div>
</template>

<script setup>
import iFrame from '@/components/iFrame'

import { ref,computed } from 'vue';
const route = useRoute();
const url = computed(() => {
  return import.meta.env.VITE_APP_DINKY_ADMIN+"/#"+route.path;
});
</script>
