<template>
	<div class="page page-flex-col min-w-196 max-w-354">
		<schueler-sprachbelegung :sprachbelegungen :patch-sprachbelegung :add-sprachbelegung :remove-sprachbelegung
			:schueler-liste-manager :schulform :server-mode :readonly />
		<schueler-sprachpruefung-herkunftssprachlich :sprachpruefungen :patch-sprachpruefung :add-sprachpruefung :remove-sprachpruefung
			:schueler-liste-manager :schulform :server-mode :readonly />
		<schueler-sprachpruefung-feststellungspruefung :sprachpruefungen :patch-sprachpruefung :add-sprachpruefung :remove-sprachpruefung
			:schueler-liste-manager :schulform :server-mode :readonly />
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { SchuelerSprachenProps } from './SchuelerSprachenProps';
	import { BenutzerKompetenz } from '@core';

	const props = defineProps<SchuelerSprachenProps>();
	const readonly = computed<boolean>(() => !(props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN)
		|| (props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN)
			&& props.benutzerKompetenzenKlassen.has(props.schuelerListeManager().auswahl().idKlasse))
	));

</script>
