<template>
	<div class="flex flex-col gap-1 h-14">
		<div class="font-bold text-headline-md">{{ stunde.unterrichtstunde }}</div>
	</div>
</template>

<script setup lang="ts">
	import { type StundenplanZeitraster } from '@core';

	const props = defineProps<{
		stunde: StundenplanZeitraster;
	}>()

</script>