<template>
	<div class="app-container">
		<s-card-pausenzeit-daten :data="data" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import type { PausenzeitDatenProps } from "./SPausenzeitDatenProps";
	import { computed } from "vue";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";

	const props = defineProps<PausenzeitDatenProps>();

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
