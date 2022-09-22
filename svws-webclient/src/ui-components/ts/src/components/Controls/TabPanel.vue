<script lang="ts" setup>
import { TabPanel } from '@headlessui/vue'
</script>

<template>
  <TabPanel as="div" class="tab-container">
    <slot />
  </TabPanel>
</template>

<style>
.tab-container {
  @apply mt-0 mb-8 flex-grow overflow-y-auto px-4;
}
</style>