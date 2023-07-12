<template>
	<svws-ui-content-card :title="`Informationen zu den Abiturfachwahlen in dem Fach ${fachBezeichnung}`">
		<!-- -->
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { GostFachwahlenAbiturFachProps } from "./SGostFachwahlenAbiturFachProps";

	const props = defineProps<GostFachwahlenAbiturFachProps>();

	const fachBezeichnung = computed<string>(() => "TODO");

</script>
