<script lang="ts" setup>
	import { Tab } from '@headlessui/vue';

	const {
		disabled = false,
		hidden = false
	} = defineProps<{
		disabled?: boolean;
		hidden?: boolean;
	}>();
</script>

<template>
	<Tab v-slot="{ selected }" as="template" :disabled="disabled || hidden">
		<button :class="[selected ? 'tab--active' : '', hidden ? 'hidden' : 'tab']">
			<slot />
		</button>
	</Tab>
</template>

<style>
.tab {
  @apply bg-light;
  @apply inline-flex items-center justify-center;
  @apply py-2 px-4;
  @apply rounded-full;
  @apply select-none;
  @apply text-sm font-bold text-dark;
  @apply whitespace-nowrap;
}

.tab:focus {
  @apply outline-none ring ring-primary ring-opacity-75;
}

.tab--active {
  @apply bg-dark;
  @apply text-light;
}

.tab:disabled {
  @apply cursor-not-allowed;
  @apply text-disabled-dark;
}
</style>
