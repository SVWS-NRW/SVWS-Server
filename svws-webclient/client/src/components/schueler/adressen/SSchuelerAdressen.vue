<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-schueler-adressen /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<div class="col-span-full">
			<s-card-schueler-beschaeftigung :list-schuelerbetriebe="listSchuelerbetriebe" :map-beschaeftigungsarten="mapBeschaeftigungsarten"
				:map-lehrer="mapLehrer" :map-betriebe="mapBetriebe" :map-ansprechpartner="mapAnsprechpartner"
				:patch-schueler-betriebsdaten="patchSchuelerBetriebsdaten" :set-schueler-betrieb="setSchuelerBetrieb" />
			<s-card-schueler-add-adresse-modal :id-schueler="idSchueler" :map-beschaeftigungsarten="mapBeschaeftigungsarten"
				:map-lehrer="mapLehrer" :map-betriebe="mapBetriebe" :map-ansprechpartner="mapAnsprechpartner"
				:create-schueler-betriebsdaten="createSchuelerBetriebsdaten" v-slot="{ openModal }">
				<svws-ui-button class="contentFocusField mt-4" @click="openModal()" autofocus>Betrieb hinzufügen</svws-ui-button>
			</s-card-schueler-add-adresse-modal>
			<template v-if="(betriebsStammdaten !== undefined) && (betrieb !== undefined)">
				<s-card-schueler-adresse :betriebs-stammdaten="betriebsStammdaten" :betrieb="betrieb" :map-orte="mapOrte"
					:map-lehrer="mapLehrer" :map-ansprechpartner="mapAnsprechpartner" :create-ansprechpartner="createAnsprechpartner"
					:patch-schueler-betriebsdaten="patchSchuelerBetriebsdaten" :patch-betrieb="patchBetrieb" :patch-ansprechpartner="patchAnsprechpartner" />
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerAdressenProps } from "./SSChuelerAdressenProps";

	defineProps<SchuelerAdressenProps>();

</script>
