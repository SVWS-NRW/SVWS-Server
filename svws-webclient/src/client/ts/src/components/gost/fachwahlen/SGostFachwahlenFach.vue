<template>
	<svws-ui-content-card :title="`Details zu dem Fach ${fachBezeichnung}`">
		<!-- -->
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { GostFachwahlenFachProps } from "./SGostFachwahlenFachProps";

	const props = defineProps<GostFachwahlenFachProps>();

	const fachBezeichnung = computed<string>(() => "TODO");

</script>
