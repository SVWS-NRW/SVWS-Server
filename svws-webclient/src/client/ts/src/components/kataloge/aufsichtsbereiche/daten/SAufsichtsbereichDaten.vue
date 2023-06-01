<template>
	<div class="app-container">
		<s-card-aufsichtsbereich-daten :data="data" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import type { AufsichtsbereichDatenProps } from "./SAufsichtsbereichDatenProps";
	import { computed } from "vue";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";

	const props = defineProps<AufsichtsbereichDatenProps>();

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
