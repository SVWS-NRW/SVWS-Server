<template>
	<div class="page page-grid-cards">
		<schueler-schnelleingabe-anmeldedaten :manager
			:readonly
			:patch-schueler
			:patch-schulbesuchsdaten
			:patch-lernabschnittsdaten
			:schulen-mit-primaerstufe />
		<schueler-schnelleingabe-schuelerdaten :manager
			:readonly
			:patch-schueler
			:patch-lernabschnittsdaten />
		<schueler-schnelleingabe-erzieher :manager
			:get-erzieher
			:add-erzieher
			:patch-erzieher
			:patch-erzieher-an-position
			:delete-erzieher
			:readonly
			:update-kompetenz="hatKompetenzUpdate" />
		<schueler-schnelleingabe-telefonnummern :manager
			:get-telefone
			:add-telefon
			:patch-telefon
			:delete-telefone
			:update-kompetenz="hatKompetenzUpdate" />
		<schueler-schnelleingabe-vorschulentwicklung v-if="schulenMitPrimaerstufe"
			:manager
			:patch-schulbesuchsdaten />
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

	import { BenutzerKompetenz, Schulform } from "@core";
	import { useSchuleState } from "@ui";
	import { computed } from "vue";
	import type { SchuelerSchnelleingabeProps } from "~/components/schueler/neuanlage/SchuelerSchnelleingabeProps";

	const props = defineProps<SchuelerSchnelleingabeProps>();
	const schuleState = useSchuleState();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

	const schulenMitPrimaerstufe = computed(() => {
		const erlaubteSchulformen = [Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V];
		return erlaubteSchulformen.includes(schuleState.schulform);
	});

	async function cancel() {
		await props.gotoDefaultView(props.manager().stammdaten.id);
	}

</script>
