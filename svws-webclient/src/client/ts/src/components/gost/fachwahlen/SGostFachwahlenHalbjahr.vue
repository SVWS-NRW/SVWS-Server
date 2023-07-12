<template>
	<svws-ui-content-card :title="`Details zu dem Halbjahr ${halbjahrBezeichnung}`">
		<!-- -->
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { GostFachwahlenHalbjahrProps } from "./SGostFachwahlenHalbjahrProps";

	const props = defineProps<GostFachwahlenHalbjahrProps>();

	const halbjahrBezeichnung = computed<string>(() => "TODO");

</script>
