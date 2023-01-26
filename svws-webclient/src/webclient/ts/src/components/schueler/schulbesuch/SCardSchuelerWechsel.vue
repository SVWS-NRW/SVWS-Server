<template>
	<svws-ui-content-card title="Wechsel zu aufnehmender Schule">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Name der Schule" :model-value="data.aufnehmdendSchulnummer?.valueOf()"
				@update:model-value="doPatch({ aufnehmdendSchulnummer: String($event) })" type="text" />
			<svws-ui-text-input placeholder="Wechseldatum" :model-value="data.aufnehmdendWechseldatum?.valueOf()"
				@update:model-value="doPatch({ aufnehmdendWechseldatum: String($event) })" type="date" />
			<svws-ui-checkbox text="Aufnahme bestätigt" :model-value="data.aufnehmdendBestaetigt?.valueOf()"
				@update:model-value="doPatch({ aufnehmdendBestaetigt: Boolean($event) })" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { SchuelerSchulbesuchsdaten } from "@svws-nrw/svws-core-ts";

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
