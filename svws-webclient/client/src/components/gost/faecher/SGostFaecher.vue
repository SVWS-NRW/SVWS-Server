<template>
	<div class="page page-flex-row" style="grid-template-columns: minmax(min-content, 1.5fr) minmax(min-content, 1fr)">
		<s-card-gost-faecher class="min-w-fit max-w-fit" :faecher-manager :abiturjahr="jahrgangsdaten()?.abiturjahr ?? -1" :patch-fach :hat-update-kompetenz />
		<div class="min-w-180 max-w-180 flex flex-col gap-y-16 lg:gap-y-20 top-8 h-full overflow-y-auto overflow-x-hidden pr-4 scrollbar-thin">
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
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { GostLaufbahnplanungFachkombinationTyp } from "@core/core/types/gost/GostLaufbahnplanungFachkombinationTyp";

	const props = defineProps<GostFaecherProps>();
	const benutzerState = useBenutzerState();

	const hatUpdateKompetenz = computed<boolean>(() => {
		return benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)
			|| (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)
				&& benutzerState.kompetenzenAbiturjahrgaenge.has(props.jahrgangsdaten().abiturjahr));
	});

</script>

<style scoped>

	.scrollbar-thin {
		scrollbar-gutter: stable;
		scrollbar-width: thin;
		scrollbar-color: rgba(0,0,0,0.2) transparent;
	}

</style>
