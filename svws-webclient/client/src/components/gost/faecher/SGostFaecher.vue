<template>
	<div class="page--content page--content--full">
		<s-card-gost-faecher :faecher-manager :abiturjahr="jahrgangsdaten()?.abiturjahr ?? -1" :patch-fach :hat-update-kompetenz />
		<div class="flex flex-col gap-y-16 lg:gap-y-20 sticky top-8 h-fit">
			<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.VERBOTEN" :faecher-manager
				:map-fachkombinationen :patch-fachkombination :hat-update-kompetenz :add-fachkombination :remove-fachkombination />
			<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH" :faecher-manager
				:map-fachkombinationen :patch-fachkombination :hat-update-kompetenz :add-fachkombination :remove-fachkombination />
			<s-card-gost-zusatzkurse :jahrgangsdaten :patch-jahrgangsdaten :hat-update-kompetenz />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { GostFaecherProps } from "./SGostFaecherProps";
	import { BenutzerKompetenz, GostLaufbahnplanungFachkombinationTyp } from "@core";

	const props = defineProps<GostFaecherProps>();

	const hatUpdateKompetenz = computed<boolean>(() => {
		return props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)
			|| (props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)
				&& props.benutzerKompetenzenAbiturjahrgaenge.has(props.jahrgangsdaten().abiturjahr))
	});
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
