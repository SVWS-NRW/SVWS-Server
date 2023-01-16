<template>
	<div v-if="visible" class="app-container">
		<s-card-gost-fachwahlen :item="item" :data-fachwahlen="dataFachwahlen" />
	</div>
</template>

<script setup lang="ts">

	import { GostJahrgang } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
	import { DataGostSchuelerFachwahlen } from "~/apps/gost/DataGostSchuelerFachwahlen";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeGostFachwahlen } from "~/router/apps/gost/RouteGostFachwahlen";

	const props = defineProps<{
		item: ShallowRef<GostJahrgang | undefined>;
		dataFachwahlen: DataGostSchuelerFachwahlen;
		schule: DataSchuleStammdaten;
		jahrgangsdaten: DataGostJahrgang;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeGostFachwahlen.hidden());
	});

</script>
