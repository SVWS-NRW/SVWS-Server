<template>
	<svws-ui-content-card title="Abschluss Berechnung">
		<svws-ui-input-wrapper>
			<svws-ui-textarea-input placeholder="Text" :model-value="data.textErgebnisPruefungsalgorithmus" @blur="textErgebnisPruefungsalgorithmus=>doPatch({textErgebnisPruefungsalgorithmus})" resizeable="vertical" :autoresize="true" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerLernabschnittsdaten } from "@core";

	const props = defineProps<{
		data: SchuelerLernabschnittsdaten;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerLernabschnittsdaten>) : void;
	}>()

	function doPatch(data: Partial<SchuelerLernabschnittsdaten>) {
		emit('patch', data);
	}

</script>
