<template>
	<div class="page page-grid-cards">
		<schueler-schnelleingabe-anmeldedaten :manager
			:readonly
			:schuljahr
			:patch-schueler
			:patch-schulbesuchsdaten
			:patch-lernabschnittsdaten
			:schulen-mit-primaerstufe />
		<schueler-schnelleingabe-schuelerdaten :manager
			:readonly
			:patch-schueler
			:patch-lernabschnittsdaten
			:schuljahr />
		<schueler-schnelleingabe-erzieher :manager
			:get-erzieher
			:add-erzieher
			:patch-erzieher
			:patch-erzieher-an-position
			:delete-erzieher
			:schuljahr
			:update-kompetenz="hatKompetenzUpdate" />
		<schueler-schnelleingabe-telefonnummern :manager
			:get-telefone
			:add-telefon
			:patch-telefon
			:delete-telefone
			:update-kompetenz="hatKompetenzUpdate" />
		<schueler-schnelleingabe-vorschulentwicklung v-if="schulenMitPrimaerstufe"
			:manager
			:patch-schulbesuchsdaten
			:schuljahr />
		<schueler-schnelleingabe-vermerke :manager
			:get-vermerke
			:add-vermerk
			:patch-vermerk
			:delete-vermerke
			:update-kompetenz="hatKompetenzUpdate" />
		<svws-ui-content-card />
		<svws-ui-content-card class="col-span-full">
			<div class="-mt-16 flex flex-row gap-4 justify-end w-full">
				<svws-ui-button type="primary" @click="gotoSchuelerNeuView">Weiteren Schüler anlegen</svws-ui-button>
				<svws-ui-button type="secondary" @click="cancel">Neuaufnahme beenden</svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { Schulform } from "@core/asd/types/schule/Schulform";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { computed } from "vue";
	import type { SchuelerSchnelleingabeProps } from "~/components/schueler/neuanlage/SchuelerSchnelleingabeProps";

	const props = defineProps<SchuelerSchnelleingabeProps>();
	const benutzerState = useBenutzerState();
	const abschnittState = useAbschnittState();
	const schuleState = useSchuleState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const schuljahr = computed<number>(() => abschnittState.auswahl.schuljahr);

	const schulenMitPrimaerstufe = computed(() => {
		const erlaubteSchulformen = [Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V];
		return erlaubteSchulformen.includes(schuleState.schulform);
	});

	function cancel() {
		void props.gotoDefaultView(props.manager().stammdaten.id);
	}

</script>
