<template>
	<div v-if="jahrgangsdaten !== undefined" class="page--content">
		<s-card-gost-beratungslehrer v-if="istAbiturjahrgang" :jahrgangsdaten="jahrgangsdaten" />
		<div class="flex flex-col gap-3">
			<s-card-gost-text-beratungsbogen :jahrgangsdaten="jahrgangsdaten" :patch-jahrgangsdaten="patchJahrgangsdaten" />
			<s-card-gost-text-mailversand :jahrgangsdaten="jahrgangsdaten" :patch-jahrgangsdaten="patchJahrgangsdaten" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { GostBeratungProps } from "./SGostBeratungProps";

	const props = defineProps<GostBeratungProps>();

	const istAbiturjahrgang: ComputedRef<boolean> = computed(() => (props.jahrgangsdaten().abiturjahr > 0));

</script>
