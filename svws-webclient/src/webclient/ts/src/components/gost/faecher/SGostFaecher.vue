<template>
	<div v-if="visible" class="s-gost-faecher--wrapper">
		<div class="w-128">
			<s-card-gost-faecher :data-faecher="dataFaecher" />
		</div>
		<div>
			<s-card-gost-zusatzkurse :jahrgangsdaten="jahrgangsdaten" />
			<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.VERBOTEN" :data-faecher="dataFaecher"
				:data-fachkombinationen="dataFachkombinationen" />
			<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH" :data-faecher="dataFaecher"
				:data-fachkombinationen="dataFachkombinationen" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, ShallowRef } from "vue";
	import { GostJahrgang, GostLaufbahnplanungFachkombinationTyp } from "@svws-nrw/svws-core-ts";
	import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeGostFaecher } from "~/router/apps/gost/RouteGostFaecher";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostFachkombinationen } from "~/apps/gost/DataGostFachkombinationen";

	const { jahrgangsdaten, dataFaecher } = defineProps<{ 
		item: ShallowRef<GostJahrgang | undefined>;
		schule: DataSchuleStammdaten;
		jahrgangsdaten: DataGostJahrgang;
		dataFaecher: DataGostFaecher;
		dataFachkombinationen: DataGostFachkombinationen;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeGostFaecher.hidden);
	});

</script>

<style>
	.s-gost-faecher--wrapper {
		@apply grid grid-cols-1 gap-8 gap-x-8 gap-y-4 2xl:grid-cols-2;
	}
</style>