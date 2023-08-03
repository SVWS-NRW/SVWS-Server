<template>
	<div class="page--content">
		<s-card-lehrer-basisdaten :stammdaten="stammdaten" @patch="patch" />
		<s-card-lehrer-kontaktdaten :stammdaten="stammdaten" :map-orte="mapOrte" :map-ortsteile="mapOrtsteile" @patch="patch" />
	</div>
</template>

<script setup lang="ts">
	import type { LehrerIndividualdatenProps } from "./SLehrerIndividualdatenProps";

	const props = defineProps<LehrerIndividualdatenProps>();
</script>
