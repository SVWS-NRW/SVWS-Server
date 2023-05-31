<template>
	<div class="page--content">
		<s-card-schueler-basisdaten :data="data" @patch="patch" />
		<s-card-schueler-kontaktdaten :data="data" @patch="patch" :map-orte="mapOrte" :map-ortsteile="mapOrtsteile" />
		<s-card-schueler-staat-religion :data="data" @patch="patch" :map-religionen="mapReligionen" />
		<s-card-schueler-migrationshintergrund :data="data" @patch="patch" />
		<s-card-schueler-foerderbedarf :data="data" @patch="patch" :map-foerderschwerpunkte="mapFoerderschwerpunkte" />
		<s-card-schueler-statusdaten :data="data" @patch="patch" :map-fahrschuelerarten="mapFahrschuelerarten" :map-haltestellen="mapHaltestellen" />
		<s-card-schueler-bemerkungen :data="data" @patch="patch" />
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerIndividualdatenProps } from "./SSchuelerIndividualdatenProps";

	const props = defineProps<SchuelerIndividualdatenProps>();

</script>
