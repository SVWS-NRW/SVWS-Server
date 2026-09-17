<template>
	<div class="page page-flex-col min-w-196 max-w-354">
		<schueler-sprachbelegung :sprachbelegungen :patch-sprachbelegung :add-sprachbelegung :remove-sprachbelegung :readonly />
		<schueler-sprachpruefung-herkunftssprachlich :sprachpruefungen :patch-sprachpruefung :add-sprachpruefung :remove-sprachpruefung :readonly />
		<schueler-sprachpruefung-feststellungspruefung :sprachpruefungen :patch-sprachpruefung :add-sprachpruefung :remove-sprachpruefung :readonly />
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';

	import { BenutzerKompetenz } from '@core/core/types/benutzer/BenutzerKompetenz';
	import { useBenutzerState } from '@ui/states/BenutzerState';

	import { useSchuelerAuswahlState } from '~/states/schueler/SchuelerAuswahlState';

	import type { SchuelerSprachenProps } from './SchuelerSprachenProps';

	const props = defineProps<SchuelerSprachenProps>();
	const benutzerState = useBenutzerState();
	const schuelerAuswahlState = useSchuelerAuswahlState();

	const readonly = computed<boolean>(() => !(benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN)
		|| (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN)
			&& benutzerState.kompetenzenKlasse.has(schuelerAuswahlState.manager.auswahl().idKlasse))
	));

</script>
