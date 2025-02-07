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
	import { BenutzerKompetenz, GostLaufbahnplanungFachkombinationTyp } from "@core";

	const props = defineProps<GostFaecherProps>();

	const hatUpdateKompetenz = computed<boolean>(() => {
		return props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)
			|| (props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)
				&& props.benutzerKompetenzenAbiturjahrgaenge.has(props.jahrgangsdaten().abiturjahr))
	});

</script>

<style lang="postcss" scoped>

	@reference "../../../../../ui/src/assets/styles/index.css"

	.scrollbar-thin {
		scrollbar-gutter: stable;
		scrollbar-width: thin;
		scrollbar-color: rgba(0,0,0,0.2) transparent;
	}

</style>
