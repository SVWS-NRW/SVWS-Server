<template>
	<div v-if="logs != null" class="w-full overflow-x-auto overflow-y-hidden mt-4">
		<div>
			<span class="flex mb-2 text-headline-md gap-1 items-center">
				<span class="icon i-ri-checkbox-circle-fill mr-3 icon-success" v-if="(status === true)" />
				<span class="icon i-ri-alert-fill mr-3 icon-error" v-else-if="(status === false)" />
				<svws-ui-button v-if="log !== undefined" type="transparent" @click="copyToClipboard">
					<template v-if="copied === null">
						<span class="icon i-ri-file-copy-line" />
						<span>Log kopieren</span>
					</template>
					<template v-else-if="copied === false">
						<span>Kopieren fehlgeschlagen</span>
						<span class="icon i-ri-error-warning-fill" />
					</template>
					<template v-else>
						<span>Log kopiert</span>
						<span class="icon i-ri-check-line icon-success" />
					</template>
				</svws-ui-button>
				<slot name="button" />
			</span>
		</div>
		<div class="bg-black text-white rounded-xl overflow-hidden">
			<div class="overflow-auto max-h-[24rem] w-full">
				<pre class="py-2 px-3 w-px" v-if="(status !== undefined)">{{ log }}</pre>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { List } from "../../../../core/src/java/util/List";

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

	const log = computed(() => {
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