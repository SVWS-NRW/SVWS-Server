<template>
	<div class="s-gost-faecher--wrapper">
		<div class="w-128">
			<s-card-gost-faecher :faecher-manager="faecherManager" :abiturjahr="item?.abiturjahr ?? -1" :patch-fach="patchFach" />
		</div>
		<div>
			<s-card-gost-zusatzkurse v-if="jahrgangsdaten.daten !== undefined" :jahrgangsdaten="jahrgangsdaten.daten" :patch-jahrgangsdaten="patchJahrgangsdaten" />
			<template v-if="dataFachkombinationen.daten !== undefined">
				<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.VERBOTEN" :faecher-manager="faecherManager"
					:fachkombinationen="dataFachkombinationen.daten" :patch-fachkombination="patchFachkombination"
					:add-fachkombination="addFachkombination" :remove-fachkombination="removeFachkombination" />
				<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH" :faecher-manager="faecherManager"
					:fachkombinationen="dataFachkombinationen.daten" :patch-fachkombination="patchFachkombination"
					:add-fachkombination="addFachkombination" :remove-fachkombination="removeFachkombination" />
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostFach, GostFaecherManager, GostJahrgang, GostJahrgangFachkombination, GostJahrgangsdaten, GostLaufbahnplanungFachkombinationTyp } from "@svws-nrw/svws-core-ts";
	import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { DataGostFachkombinationen } from "~/apps/gost/DataGostFachkombinationen";

	const props = defineProps<{
		patchFach: (data: Partial<GostFach>, fach_id: number) => Promise<boolean>;
		patchFachkombination: (data: Partial<GostJahrgangFachkombination>, id : number) => Promise<boolean>;
		addFachkombination: (typ: GostLaufbahnplanungFachkombinationTyp) => Promise<GostJahrgangFachkombination | undefined>;
		removeFachkombination: (id: number) => Promise<GostJahrgangFachkombination | undefined>;
		patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
		item: GostJahrgang | undefined;
		schule: DataSchuleStammdaten;
		jahrgangsdaten: DataGostJahrgang;
		faecherManager: GostFaecherManager;
		dataFachkombinationen: DataGostFachkombinationen;
	}>();

</script>

<style>
	.s-gost-faecher--wrapper {
		@apply grid grid-cols-1 gap-8 gap-x-8 gap-y-4 2xl:grid-cols-2;
	}
</style>