<template>
	<div class="s-gost-faecher--wrapper">
		<div class="w-128">
			<s-card-gost-faecher :faecher-manager="faecherManager" :abiturjahr="jahrgangsdaten?.abiturjahr ?? -1" :patch-fach="patchFach" />
		</div>
		<div>
			<s-card-gost-zusatzkurse v-if="jahrgangsdaten !== undefined" :jahrgangsdaten="jahrgangsdaten" :patch-jahrgangsdaten="patchJahrgangsdaten" />
			<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.VERBOTEN" :faecher-manager="faecherManager"
				:map-fachkombinationen="mapFachkombinationen" :patch-fachkombination="patchFachkombination"
				:add-fachkombination="addFachkombination" :remove-fachkombination="removeFachkombination" />
			<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH" :faecher-manager="faecherManager"
				:map-fachkombinationen="mapFachkombinationen" :patch-fachkombination="patchFachkombination"
				:add-fachkombination="addFachkombination" :remove-fachkombination="removeFachkombination" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostLaufbahnplanungFachkombinationTyp } from "@svws-nrw/svws-core";
	import type { GostFaecherProps } from "./SGostFaecherProps";

	defineProps<GostFaecherProps>();

</script>

<style>
	.s-gost-faecher--wrapper {
		@apply grid grid-cols-1 gap-8 gap-x-8 gap-y-4 2xl:grid-cols-2;
	}
</style>