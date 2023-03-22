<template>
	<div class="app-container">
		<div>
			<s-card-schueler-add-adresse-modal :id-schueler="idSchueler" :map-beschaeftigungsarten="mapBeschaeftigungsarten"
				:map-lehrer="mapLehrer" :map-betriebe="mapBetriebe" :map-ansprechpartner="mapAnsprechpartner"
				:create-schueler-betriebsdaten="createSchuelerBetriebsdaten" v-slot="{ openModal }">
				<svws-ui-button @click="openModal()">Adresse hinzufügen</svws-ui-button>
			</s-card-schueler-add-adresse-modal>
		</div>
		<div v-if="(betriebsStammdaten !== undefined) && (betrieb !== undefined)" class="col-span-full">
			<s-card-schueler-beschaeftigung :list-schuelerbetriebe="listSchuelerbetriebe" :map-beschaeftigungsarten="mapBeschaeftigungsarten"
				:map-lehrer="mapLehrer" :map-betriebe="mapBetriebe" :map-ansprechpartner="mapAnsprechpartner"
				:patch-schueler-betriebsdaten="patchSchuelerBetriebsdaten" :set-schueler-betrieb="setSchuelerBetrieb" />
			<s-card-schueler-adresse :list-schuelerbetriebe="listSchuelerbetriebe" :betriebs-stammdaten="betriebsStammdaten" :betrieb="betrieb" :map-orte="mapOrte"
				:map-lehrer="mapLehrer" :map-ansprechpartner="mapAnsprechpartner" :create-ansprechpartner="createAnsprechpartner"
				:patch-schueler-betriebsdaten="patchSchuelerBetriebsdaten" :patch-betrieb="patchBetrieb" :patch-ansprechpartner="patchAnsprechpartner" />
		</div>
		<div v-else class="col-span-full">
			<span>Noch kein Schülerbetrieb vorhanden.</span>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { SchuelerAdressenProps } from "./SSChuelerAdressenProps";

	defineProps<SchuelerAdressenProps>();
</script>
