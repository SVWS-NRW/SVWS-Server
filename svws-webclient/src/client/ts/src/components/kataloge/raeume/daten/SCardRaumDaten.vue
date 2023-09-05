<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="ID" v-model="data.id" type="text" readonly />
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel=>doPatch({kuerzel})" type="text" />
			<svws-ui-text-input placeholder="Größe" :model-value="data.groesse" @change="groesse=>doPatch({groesse: Number(groesse)})" type="number" />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Beschreibung" :model-value="data.beschreibung" @change="beschreibung=>doPatch({beschreibung})" type="text" span="full" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { Raum } from "@core";

	const props = defineProps<{
		data: Raum
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<Raum>): void;
	}>()

	function doPatch(data: Partial<Raum>) {
		emit('patch', data);
	}

</script>
