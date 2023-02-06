<template>
	<svws-ui-content-card title="Abschluss Berechnung">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-textarea-input placeholder="" v-model="inputAbschlussBerechnung" resizeable="vertical" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { SchuelerLernabschnittsdaten } from '@svws-nrw/svws-core-ts';

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
