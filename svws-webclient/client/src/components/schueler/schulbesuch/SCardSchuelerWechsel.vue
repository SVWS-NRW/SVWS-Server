<template>
	<svws-ui-content-card title="Wechsel zu aufnehmender Schule">
		<template #actions>
			<svws-ui-checkbox :model-value="data.aufnehmdendBestaetigt || undefined" @update:model-value="doPatch({ aufnehmdendBestaetigt: Boolean($event) })">
				Aufnahme bestätigt
			</svws-ui-checkbox>
		</template>
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Name der Schule" :model-value="data.aufnehmdendSchulnummer" @change="aufnehmdendSchulnummer=>doPatch({ aufnehmdendSchulnummer })" type="text" span="full" />
			<svws-ui-text-input placeholder="Wechseldatum" :model-value="data.aufnehmdendWechseldatum" @change="aufnehmdendWechseldatum=>doPatch({ aufnehmdendWechseldatum })" type="date" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerSchulbesuchsdaten } from "@core";

	defineProps<{
		data: SchuelerSchulbesuchsdaten;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerSchulbesuchsdaten>): void;
	}>()

	function doPatch(data: Partial<SchuelerSchulbesuchsdaten>) {
		emit('patch', data);
	}

</script>
