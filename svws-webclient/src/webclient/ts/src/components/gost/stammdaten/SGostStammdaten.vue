<template>
	<div v-if="props.item !== undefined" class="app-container">
		<s-card-gost-beratungslehrer v-if="istAbiturjahrgang" :jahrgangsdaten="jahrgangsdaten" />
		<div>
			<s-card-gost-text-beratungsbogen :jahrgangsdaten="jahrgangsdaten" />
			<s-card-gost-text-mailversand :jahrgangsdaten="jahrgangsdaten" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostJahrgang } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";

	import { routeGost } from "~/router/apps/RouteGost";

	const props = defineProps<{ id?: number; item?: GostJahrgang, routename: string }>();

	const jahrgangsdaten: ComputedRef<DataGostJahrgang> = computed(() => {
		return routeGost.data.jahrgangsdaten;
	});

	const istAbiturjahrgang: ComputedRef<boolean> = computed(() => (props.id !== undefined) && (props.id > 0));

</script>
