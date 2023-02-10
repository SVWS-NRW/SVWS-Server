<template>
	<div v-if="visible" class="s-gost-faecher--wrapper">
		<div class="w-128">
			<s-card-gost-faecher :data-faecher="dataFaecher" />
		</div>
		<div>
			<s-card-gost-zusatzkurse v-if="jahrgangsdaten.daten !== undefined" :jahrgangsdaten="jahrgangsdaten.daten" :patch-jahrgangsdaten="patchJahrgangsdaten" />
			<template v-if="dataFaecher.manager !== undefined && dataFachkombinationen.daten !== undefined">
				<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.VERBOTEN" :faecher-manager="dataFaecher.manager"
					:fachkombinationen="dataFachkombinationen.daten" :patch-fachkombination="patchFachkombination"
					:add-fachkombination="addFachkombination" :remove-fachkombination="removeFachkombination" />
				<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH" :faecher-manager="dataFaecher.manager"
					:fachkombinationen="dataFachkombinationen.daten" :patch-fachkombination="patchFachkombination"
					:add-fachkombination="addFachkombination" :remove-fachkombination="removeFachkombination" />
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, ShallowRef } from "vue";
	import { GostJahrgang, GostJahrgangFachkombination, GostJahrgangsdaten, GostLaufbahnplanungFachkombinationTyp } from "@svws-nrw/svws-core-ts";
	import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeGostFaecher } from "~/router/apps/gost/RouteGostFaecher";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostFachkombinationen } from "~/apps/gost/DataGostFachkombinationen";

	const props = defineProps<{
		patchFachkombination: (data: Partial<GostJahrgangFachkombination>, id : number) => Promise<boolean>;
		addFachkombination: (typ: GostLaufbahnplanungFachkombinationTyp) => Promise<GostJahrgangFachkombination | undefined>;
		removeFachkombination: (id: number) => Promise<GostJahrgangFachkombination | undefined>;
		patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
		item: ShallowRef<GostJahrgang | undefined>;
		schule: DataSchuleStammdaten;
		jahrgangsdaten: DataGostJahrgang;
		dataFaecher: DataGostFaecher;
		dataFachkombinationen: DataGostFachkombinationen;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeGostFaecher.hidden());
	});

</script>

<style>
	.s-gost-faecher--wrapper {
		@apply grid grid-cols-1 gap-8 gap-x-8 gap-y-4 2xl:grid-cols-2;
	}
</style>