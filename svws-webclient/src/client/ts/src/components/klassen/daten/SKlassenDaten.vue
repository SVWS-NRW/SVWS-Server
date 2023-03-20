<template>
	<div class="app-container">
		<s-card-klasse-basisdaten :data="data" :map-jahrgaenge="mapJahrgaenge" @patch="doPatch" />
		<s-card-klasse-klassenleitungen :data="data" :map-lehrer="mapLehrer" @patch="doPatch" />
		<s-card-klasse-schueler :list-schueler="data.schueler" :goto-schueler="gotoSchueler" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";
	import { KlassenDatenProps } from "./SKlassenDatenProps";

	const props = defineProps<KlassenDatenProps>();

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
