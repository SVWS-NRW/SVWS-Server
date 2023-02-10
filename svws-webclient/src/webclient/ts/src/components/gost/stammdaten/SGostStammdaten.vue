<template>
	<div v-if="jahrgangsdaten !== undefined" class="app-container">
		<s-card-gost-beratungslehrer v-if="istAbiturjahrgang" :jahrgangsdaten="jahrgangsdaten" />
		<div>
			<s-card-gost-text-beratungsbogen :jahrgangsdaten="jahrgangsdaten" :patch-jahrgangsdaten="patchJahrgangsdaten" />
			<s-card-gost-text-mailversand :jahrgangsdaten="jahrgangsdaten" :patch-jahrgangsdaten="patchJahrgangsdaten" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostJahrgangsdaten } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{
		patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
		jahrgangsdaten: GostJahrgangsdaten;
	}>();

	const istAbiturjahrgang: ComputedRef<boolean> = computed(() => (props.jahrgangsdaten.abiturjahr > 0));

</script>
