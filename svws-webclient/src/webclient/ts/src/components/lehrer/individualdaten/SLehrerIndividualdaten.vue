<template>
	<div class="app-container">
		<s-card-lehrer-basisdaten :stammdaten="stammdaten" @patch="doPatch" />
		<s-card-lehrer-kontaktdaten :stammdaten="stammdaten" :orte="orte" :ortsteile="ortsteile" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { LehrerStammdaten, List, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@svws-nrw/svws-core-ts";
	import { computed } from "vue";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";

	const props = defineProps<{
		patch: (data : Partial<LehrerStammdaten>) => Promise<void>;
		stammdaten: LehrerStammdaten;
		orte: List<OrtKatalogEintrag>;
		ortsteile: List<OrtsteilKatalogEintrag>;
	}>();

	const { doPatch } = useDebouncedPatch(computed(() => props.stammdaten), props.patch)
</script>
