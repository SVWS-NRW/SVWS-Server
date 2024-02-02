<template>
	<div v-if="logs != null" class="mt-4 h-84 w-full overflow-x-auto">
		<div :class="{ 'text-error': status === false, 'text-success': status === true }">
			<span class="flex mb-4 text-headline-md">
				<i-ri-checkbox-circle-fill v-if="(status === true)" class="mr-1" />
				<i-ri-alert-line v-else-if="(status === false)" class="mr-1" />
				Log
			</span>
		</div>
		<pre v-if="(status !== undefined)">{{ log }}</pre>
	</div>
</template>

<script setup lang="ts">

	import { type List } from '@core';
	import { computed } from "vue";

	const props = defineProps<{
		logs?: List<string | null>;
		status?: boolean;
	}>();

	const log = computed(()=> {
		if (props.logs === undefined)
			return;
		let result = "";
		for (const s of props.logs)
			if (s !== null)
				result += s + "\n";
		return result;
	});

</script>