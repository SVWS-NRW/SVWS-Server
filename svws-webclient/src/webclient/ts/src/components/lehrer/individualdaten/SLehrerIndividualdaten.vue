<template>
	<div v-if="visible" class="app-container">
		<s-card-lehrer-basisdaten :stammdaten="stammdaten" />
		<s-card-lehrer-kontaktdaten :stammdaten="stammdaten" :orte="orte" :ortsteile="ortsteile" />
	</div>
</template>

<script setup lang="ts">

	import { List, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataLehrerStammdaten } from "~/apps/lehrer/DataLehrerStammdaten";

	const props = defineProps<{
		stammdaten: DataLehrerStammdaten;
		orte: List<OrtKatalogEintrag>;
		ortsteile: List<OrtsteilKatalogEintrag>;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return props.stammdaten.daten !== undefined;
	});

</script>
