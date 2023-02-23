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

	import { computed, ComputedRef } from "vue";
	import { GostStammdatenProps } from "./SGostStammdatenProps";

	const props = defineProps<GostStammdatenProps>();

	const istAbiturjahrgang: ComputedRef<boolean> = computed(() => (props.jahrgangsdaten.abiturjahr > 0));

</script>
