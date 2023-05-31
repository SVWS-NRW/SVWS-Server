<template>
	<svws-ui-content-card title="Abschluss Berechnung">
		<svws-ui-input-wrapper>
			<svws-ui-textarea-input placeholder="" v-model="abschlussBerechnung" resizeable="vertical" :autoresize="true" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerLernabschnittsdaten } from "@svws-nrw/svws-core";
	import type { WritableComputedRef } from 'vue';
	import { computed } from 'vue';

	const props = defineProps<{
		data: SchuelerLernabschnittsdaten;
	}>();

	const abschlussBerechnung: WritableComputedRef<string> = computed({
		get: () => props.data.textErgebnisPruefungsalgorithmus || "",
		set: (value) => {}
	});

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerLernabschnittsdaten>) : void;
	}>()

	function doPatch(data: Partial<SchuelerLernabschnittsdaten>) {
		emit('patch', data);
	}

</script>
