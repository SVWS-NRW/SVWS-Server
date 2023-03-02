<template>
	<div class="app-container">
		<s-card-lehrer-basisdaten :stammdaten="stammdaten" @patch="doPatch" />
		<s-card-lehrer-kontaktdaten :stammdaten="stammdaten" :map-orte="mapOrte" :map-ortsteile="mapOrtsteile" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";
	import { LehrerIndividualdatenProps } from "./SLehrerIndividualdatenProps";

	const props = defineProps<LehrerIndividualdatenProps>();

	const { doPatch } = useDebouncedPatch(computed(() => props.stammdaten), props.patch)
</script>
