<template>
	<svws-ui-content-card :title="`Details zu Fach ${fachBezeichnung} in dem Halbjahr ${halbjahrBezeichnung}`">
		<!-- -->
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { GostFachwahlenFachHalbjahrProps } from "./SGostFachwahlenFachHalbjahrProps";

	const props = defineProps<GostFachwahlenFachHalbjahrProps>();

	const fachBezeichnung = computed<string>(() => "TODO");

	const halbjahrBezeichnung = computed<string>(() => "TODO");

</script>
