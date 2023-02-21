<template>
	<div class="app-container">
		<s-card-kurs-basisdaten :data="data" :map-jahrgaenge="mapJahrgaenge" :map-lehrer="mapLehrer" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";
	import { KursDatenProps } from "./SKursDatenProps";

	const props = defineProps<KursDatenProps>();

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)
</script>
