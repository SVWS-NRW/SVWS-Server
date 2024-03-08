<template>
	<div v-if="logs != null" class="w-full overflow-x-auto" :class="hfull ? ['h-full','overflow-y-hidden'] : ['mt-4', 'max-h-[24rem]']">
		<div>
			<span class="flex mb-2 text-headline-md gap-1 items-center">
				<i-ri-checkbox-circle-fill v-if="(status === true)" class="mr-3 text-success" />
				<i-ri-alert-fill v-else-if="(status === false)" class="mr-3 text-error" />
				<svws-ui-button v-if="log !== undefined" type="transparent" @click="copyToClipboard">
					<template v-if="copied === null">
						<i-ri-clipboard-line />
						<span>Log kopieren</span>
					</template>
					<template v-else-if="copied === false">
						<span>Kopieren fehlgeschlagen</span>
						<i-ri-error-warning-fill />
					</template>
					<template v-else>
						<span>Log kopiert</span>
						<i-ri-check-line class="text-success" />
					</template>
				</svws-ui-button>
				<slot name="button" />
			</span>
		</div>
		<div class="bg-black text-white rounded-xl overflow-hidden" :class="hfull ? ['h-full'] : []">
			<pre class="overflow-auto py-2 px-3" v-if="(status !== undefined)">{{ log }}</pre>
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