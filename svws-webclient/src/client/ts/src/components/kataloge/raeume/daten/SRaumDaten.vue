<template>
	<div class="app-container">
		<s-card-raum-daten :data="data" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import type { RaumDatenProps } from "./SRaumDatenProps";
	import { computed } from "vue";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";

	const props = defineProps<RaumDatenProps>();

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
