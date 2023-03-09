<template>
	<svws-ui-content-card title="Fehlzeiten"> Inhalte </svws-ui-content-card>
</template>

<script setup lang="ts">

	import { SchuelerLernabschnittsdaten } from "@svws-nrw/svws-core";

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
