<template>
	<div class="page--content">
		<s-card-foerderschwerpunkt-daten :data="data" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";
	import type { FoerderschwerpunktDatenProps } from "./SFoerderschwerpunktDatenProps";

	const props = defineProps<FoerderschwerpunktDatenProps>();

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
