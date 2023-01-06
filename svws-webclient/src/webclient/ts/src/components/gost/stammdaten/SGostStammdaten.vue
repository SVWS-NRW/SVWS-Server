<template>
	<div v-if="item.value !== undefined" class="app-container">
		<s-card-gost-beratungslehrer v-if="istAbiturjahrgang" :jahrgangsdaten="jahrgangsdaten" />
		<div>
			<s-card-gost-text-beratungsbogen :jahrgangsdaten="jahrgangsdaten" />
			<s-card-gost-text-mailversand :jahrgangsdaten="jahrgangsdaten" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostJahrgang } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";

	import { routeGost } from "~/router/apps/RouteGost";

	const { item } = defineProps<{ 
		item: ShallowRef<GostJahrgang | undefined>;
		schule: DataSchuleStammdaten;
		jahrgangsdaten: DataGostJahrgang;
	}>();

	const jahrgangsdaten: ComputedRef<DataGostJahrgang> = computed(() => {
		return routeGost.data.jahrgangsdaten;
	});

	const istAbiturjahrgang: ComputedRef<boolean> = computed(() => (item.value !== undefined) && (item.value?.abiturjahr > 0));

</script>
