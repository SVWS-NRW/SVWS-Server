<template>
	<div class="page--content">
		<s-card-lehrer-basisdaten :lehrer-liste-manager="lehrerListeManager" :patch="patch" />
		<s-card-lehrer-kontaktdaten :lehrer-liste-manager="lehrerListeManager" :patch="patch" :map-orte="mapOrte" :map-ortsteile="mapOrtsteile" />
		<s-lehrer-individualdaten-leitungsfunktionen :lehrer-liste-manager="lehrerListeManager" />
	</div>
</template>

<script setup lang="ts">

	import type { LehrerIndividualdatenProps } from "./SLehrerIndividualdatenProps";

	const props = defineProps<LehrerIndividualdatenProps>();

</script>
