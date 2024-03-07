<template>
	<div v-if="logs != null" class="w-full overflow-x-auto" :class="hfull ? ['h-full','overflow-y-hidden'] : ['mt-4', 'h-84']">
		<div :class="{ 'text-error': status === false, 'text-success': status === true }">
			<span class="flex mb-4 text-headline-md gap-4">
				<i-ri-checkbox-circle-fill v-if="(status === true)" class="mr-1" />
				<i-ri-alert-line v-else-if="(status === false)" class="mr-1" />
				<svws-ui-button v-if="log !== undefined" type="transparent" @click="copyToClipboard" class="pb-2">
					<span>Log Kopieren</span>
					<i-ri-clipboard-line v-if="copied === null" />
					<i-ri-error-warning-fill v-else-if="copied === false" />
					<i-ri-check-line v-else class="text-success" />
				</svws-ui-button>
			</span>
		</div>
		<div :class="hfull ? ['h-full', 'overflow-y-auto'] : []">
			<pre v-if="(status !== undefined)">{{ log }}</pre>
			<div class="pt-4" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { List } from '@core';

	const props = withDefaults(defineProps<{
		logs?: List<string | null>;
		status?: boolean;
		hfull?: boolean;
	}>(), {
		logs: undefined,
		status: undefined,
		hfull: false,
	});

	const copied = ref<boolean|null>(null);

	const log = computed(()=> {
		if (props.logs === undefined)
			return;
		let result = "";
		for (const s of props.logs)
			if (s !== null)
				result += s + "\n";
		return result;
	});

	async function copyToClipboard() {
		if (log.value === undefined)
			return;
		try {
			await navigator.clipboard.writeText("```\n"+log.value+"\n```");
		} catch(e) {
			copied.value = false;
		}
		copied.value = true;
	}
</script>