<template>
	<div class="flex flex-col border w-full">
		<div class="font-bold text-headline-md">{{ Wochentag.fromIDorException(tag) }}</div>
		<slot />
	</div>
</template>

<script setup lang="ts">
	import { Wochentag } from '@core';

	const props = defineProps<{
		tag: number;
	}>()

</script>