<template>
	<div class="border rounded-md m-1 ">
		<StundenplanStunde :stunde="entry" />
	</div>
</template>

<script setup lang="ts">
	import { type StundenplanZeitraster } from '@core';

	const props = defineProps<{
		entry: StundenplanZeitraster;
	}>()

</script>