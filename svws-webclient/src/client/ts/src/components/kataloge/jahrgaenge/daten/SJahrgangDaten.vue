<template>
	<div class="app-container">
		<s-card-jahrgang-basisdaten :data="data" :map-jahrgaenge="mapKatalogeintraege" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";
	import { JahrgangDatenProps } from "./SJahrgangDatenProps";

	const props = defineProps<JahrgangDatenProps>();

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
