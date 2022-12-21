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
  	@apply mt-0 flex-grow overflow-y-auto px-6 pb-7 outline-none;
	-webkit-overflow-scrolling: touch;
}
</style>
