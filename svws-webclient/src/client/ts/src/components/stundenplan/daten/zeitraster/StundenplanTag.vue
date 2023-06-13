<template>
	<div class="flex flex-col border w-full">
		<div class="font-bold text-headline-md">{{ Wochentag.fromIDorException(tag) }}</div>
		<StundenplanEntry v-for="stunde of listZeitraster" :key="stunde.id" :entry="stunde" class="hover:bg-slate-400 select-none cursor-pointer" :class="{'bg-slate-400': stunde.unterrichtstunde === selected}" />
	</div>
</template>

<script setup lang="ts">
	import { Wochentag, type StundenplanZeitraster } from '@core';

	const props = defineProps<{
		tag: number;
		listZeitraster: StundenplanZeitraster[];
		selected?: number;
	}>()

</script>