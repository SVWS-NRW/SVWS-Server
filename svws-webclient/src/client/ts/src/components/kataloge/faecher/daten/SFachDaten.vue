<template>
	<div class="page--content">
		<s-card-fach-basisdaten :data="data" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";
	import type { FachDatenProps } from "./SFachDatenProps";

	const props = defineProps<FachDatenProps>();

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
