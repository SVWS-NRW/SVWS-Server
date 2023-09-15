<template>
	<div class="page--content page--content--full">
		<s-card-gost-faecher :faecher-manager="faecherManager" :abiturjahr="jahrgangsdaten()?.abiturjahr ?? -1" :patch-fach="patchFach" />
		<div class="flex flex-col gap-y-16 lg:gap-y-20 sticky top-8 h-fit">
			<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.VERBOTEN" :faecher-manager="faecherManager"
				:map-fachkombinationen="mapFachkombinationen" :patch-fachkombination="patchFachkombination"
				:add-fachkombination="addFachkombination" :remove-fachkombination="removeFachkombination" />
			<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH" :faecher-manager="faecherManager"
				:map-fachkombinationen="mapFachkombinationen" :patch-fachkombination="patchFachkombination"
				:add-fachkombination="addFachkombination" :remove-fachkombination="removeFachkombination" />
			<s-card-gost-zusatzkurse :jahrgangsdaten="jahrgangsdaten" :patch-jahrgangsdaten="patchJahrgangsdaten" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostLaufbahnplanungFachkombinationTyp } from "@core";
	import type { GostFaecherProps } from "./SGostFaecherProps";

	const props = defineProps<GostFaecherProps>();

</script>

<style lang="postcss" scoped>
	.page--content {
    @apply grid grid-cols-1;

    @media (min-width: theme("screens.2xl")) {
      grid-template-columns: minmax(min-content, 1fr) minmax(42rem, 0.75fr);
      grid-auto-columns: max-content;
    }
  }
</style>
