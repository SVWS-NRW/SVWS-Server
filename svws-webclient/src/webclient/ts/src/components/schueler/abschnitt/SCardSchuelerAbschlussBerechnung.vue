<template>
	<svws-ui-content-card title="Abschluss Berechnung">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-textarea-input placeholder="" v-model="abschlussBerechnung" resizeable="vertical" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { SchuelerLernabschnittsdaten } from '@svws-nrw/svws-core-ts';
	import { computed, WritableComputedRef } from 'vue';

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
