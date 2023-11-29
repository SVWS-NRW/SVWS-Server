<template>
	<div class="page--content">
		<s-card-schueler-basisdaten :schueler-liste-manager="schuelerListeManager" :patch="patch" />
		<s-card-schueler-kontaktdaten :schueler-liste-manager="schuelerListeManager" :patch="patch" :map-orte="mapOrte" :map-ortsteile="mapOrtsteile" />
		<s-card-schueler-staat-religion :schueler-liste-manager="schuelerListeManager" :patch="patch" :map-religionen="mapReligionen" />
		<s-card-schueler-migrationshintergrund :schueler-liste-manager="schuelerListeManager" :patch="patch" />
		<s-card-schueler-statusdaten :schueler-liste-manager="schuelerListeManager" :patch="patch" :map-fahrschuelerarten="mapFahrschuelerarten"
			:map-haltestellen="mapHaltestellen" :map-schulen="mapSchulen" />
		<s-card-schueler-bemerkungen :schueler-liste-manager="schuelerListeManager" :patch="patch" />
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerIndividualdatenProps } from "./SSchuelerIndividualdatenProps";

	const props = defineProps<SchuelerIndividualdatenProps>();

</script>
