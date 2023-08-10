<template>
	<div class="svws-ui-zeitraster--tag">
		<div class="svws-ui-zeitraster--head">{{ Wochentag.fromIDorException(tag) }}</div>
		<slot />
	</div>
</template>

<script setup lang="ts">
	import { Wochentag } from '@core';

	const props = defineProps<{
		tag: number;
	}>()

</script>

<style lang="postcss">
.svws-ui-zeitraster--tag {
  @apply flex flex-col border-r last:border-r-0 border-black/25 w-full;
}

.svws-ui-zeitraster--head {
  @apply text-headline-md text-center h-[2.75rem] inline-flex items-center justify-center;
}
</style>
