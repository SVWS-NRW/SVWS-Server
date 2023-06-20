<template>
	<div class="page--content page--content--full">
		<div class="s-gost-faecher--wrapper">
			<s-card-gost-faecher :faecher-manager="faecherManager" :abiturjahr="jahrgangsdaten()?.abiturjahr ?? -1" :patch-fach="patchFach" />
			<div class="flex flex-col gap-12">
				<s-card-gost-zusatzkurse :jahrgangsdaten="jahrgangsdaten" :patch-jahrgangsdaten="patchJahrgangsdaten" />
				<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.VERBOTEN" :faecher-manager="faecherManager"
					:map-fachkombinationen="mapFachkombinationen" :patch-fachkombination="patchFachkombination"
					:add-fachkombination="addFachkombination" :remove-fachkombination="removeFachkombination" />
				<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH" :faecher-manager="faecherManager"
					:map-fachkombinationen="mapFachkombinationen" :patch-fachkombination="patchFachkombination"
					:add-fachkombination="addFachkombination" :remove-fachkombination="removeFachkombination" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostLaufbahnplanungFachkombinationTyp } from "@core";
	import type { GostFaecherProps } from "./SGostFaecherProps";

	const props = defineProps<GostFaecherProps>();

</script>

<style>
	.s-gost-faecher--wrapper {
		@apply grid grid-cols-1 gap-16;

		@media (min-width: theme("screens.2xl")) {
			grid-template-columns: 1fr 1fr;
		}
	}
</style>
