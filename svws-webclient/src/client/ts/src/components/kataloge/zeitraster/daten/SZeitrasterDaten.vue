<template>
	<div class="app-container">
		<s-card-zeitraster-daten :data="data" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import type { ZeitrasterDatenProps } from "./SZeitrasterDatenProps";
	import { computed } from "vue";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";

	const props = defineProps<ZeitrasterDatenProps>();

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
