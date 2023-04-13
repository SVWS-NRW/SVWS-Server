<template>
	<div class="app-container">
		<s-card-schueler-basisdaten :data="data" @patch="doPatch" />
		<s-card-schueler-kontaktdaten :data="data" @patch="doPatch" :map-orte="mapOrte" :map-ortsteile="mapOrtsteile" />
		<s-card-schueler-staat-religion :data="data" @patch="doPatch" :map-religionen="mapReligionen" />
		<s-card-schueler-migrationshintergrund :data="data" @patch="doPatch" />
		<s-card-schueler-foerderbedarf :data="data" @patch="doPatch" :map-foerderschwerpunkte="mapFoerderschwerpunkte" />
		<s-card-schueler-statusdaten :data="data" @patch="doPatch" :map-fahrschuelerarten="mapFahrschuelerarten" :map-haltestellen="mapHaltestellen" />
		<s-card-schueler-bemerkungen :data="data" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";
	import type { SchuelerIndividualdatenProps } from "./SSchuelerIndividualdatenProps";

	const props = defineProps<SchuelerIndividualdatenProps>();
	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
