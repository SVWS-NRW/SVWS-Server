<template>
	<div class="app-container">
		<s-card-religion-daten :data="data" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";
	import { ReligionDatenProps } from "./SReligionDatenProps";

	const props = defineProps<ReligionDatenProps>();

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)
</script>