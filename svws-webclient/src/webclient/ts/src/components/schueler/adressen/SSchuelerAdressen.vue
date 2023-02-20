<template>
	<div class="app-container relative">
		<div class="svws-ui-bg-white sticky top-0 z-50 col-span-3 flex justify-end py-4">
			<s-card-schueler-add-adresse :id-schueler="idSchueler" :map-beschaeftigungsarten="mapBeschaeftigungsarten"
				:map-lehrer="mapLehrer" :map-betriebe="mapBetriebe" :map-ansprechpartner="mapAnsprechpartner"
				:create-schueler-betriebsdaten="createSchuelerBetriebsdaten" />
		</div>
		<div v-if="(betriebsStammdaten !== undefined) && (betrieb !== undefined)" class="col-span-3">
			<s-card-schueler-beschaeftigung :list-schuelerbetriebe="listSchuelerbetriebe" :map-beschaeftigungsarten="mapBeschaeftigungsarten"
				:map-lehrer="mapLehrer" :map-betriebe="mapBetriebe" :map-ansprechpartner="mapAnsprechpartner"
				:patch-schueler-betriebsdaten="patchSchuelerBetriebsdaten" :set-schueler-betrieb="setSchuelerBetrieb" />
			<s-card-schueler-adresse :list-schuelerbetriebe="listSchuelerbetriebe" :betriebs-stammdaten="betriebsStammdaten" :betrieb="betrieb" :map-orte="mapOrte"
				:map-lehrer="mapLehrer" :map-ansprechpartner="mapAnsprechpartner" :create-ansprechpartner="createAnsprechpartner"
				:patch-schueler-betriebsdaten="patchSchuelerBetriebsdaten" :patch-betrieb="patchBetrieb" :patch-ansprechpartner="patchAnsprechpartner" />
		</div>
		<div v-else>
			<h1>
				<strong> <div style="text-align: center;"> Noch kein Schülerbetrieb vorhanden </div> </strong>
			</h1>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { SchuelerAdressenProps } from "./SSChuelerAdressenProps";

	defineProps<SchuelerAdressenProps>();

</script>
