<template>
	<svws-ui-content-card title="Wechsel zu aufnehmender Schule">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Name der Schule" :model-value="data.aufnehmdendSchulnummer"
				@update:model-value="doPatch({ aufnehmdendSchulnummer: String($event) })" type="text" span="full" />
			<svws-ui-text-input placeholder="Wechseldatum" :model-value="data.aufnehmdendWechseldatum"
				@update:model-value="doPatch({ aufnehmdendWechseldatum: String($event) })" type="date" />
			<svws-ui-checkbox text="Aufnahme bestätigt" :model-value="data.aufnehmdendBestaetigt || undefined"
				@update:model-value="doPatch({ aufnehmdendBestaetigt: Boolean($event) })" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerSchulbesuchsdaten } from "@svws-nrw/svws-core";

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
