<template>
	<div class="s-gost-faecher--wrapper">
		<div class="w-128">
			<s-card-gost-faecher :get-faecher-manager="getFaecherManager" :abiturjahr="jahrgangsdaten?.abiturjahr ?? -1" :patch-fach="patchFach" />
		</div>
		<div>
			<s-card-gost-zusatzkurse v-if="jahrgangsdaten !== undefined" :jahrgangsdaten="jahrgangsdaten" :patch-jahrgangsdaten="patchJahrgangsdaten" />
			<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.VERBOTEN" :get-faecher-manager="getFaecherManager"
				:map-fachkombinationen="mapFachkombinationen" :patch-fachkombination="patchFachkombination"
				:add-fachkombination="addFachkombination" :remove-fachkombination="removeFachkombination" />
			<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH" :get-faecher-manager="getFaecherManager"
				:map-fachkombinationen="mapFachkombinationen" :patch-fachkombination="patchFachkombination"
				:add-fachkombination="addFachkombination" :remove-fachkombination="removeFachkombination" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostFach, GostFaecherManager, GostJahrgangFachkombination, GostJahrgangsdaten, GostLaufbahnplanungFachkombinationTyp } from "@svws-nrw/svws-core-ts";

	defineProps<{
		getFaecherManager: () => GostFaecherManager;
		patchFach: (data: Partial<GostFach>, fach_id: number) => Promise<boolean>;
		patchFachkombination: (data: Partial<GostJahrgangFachkombination>, id : number) => Promise<boolean>;
		addFachkombination: (typ: GostLaufbahnplanungFachkombinationTyp) => Promise<GostJahrgangFachkombination | undefined>;
		removeFachkombination: (id: number) => Promise<GostJahrgangFachkombination | undefined>;
		patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
		jahrgangsdaten: GostJahrgangsdaten | undefined;
		mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
	}>();

</script>

<style>
	.s-gost-faecher--wrapper {
		@apply grid grid-cols-1 gap-8 gap-x-8 gap-y-4 2xl:grid-cols-2;
	}
</style>