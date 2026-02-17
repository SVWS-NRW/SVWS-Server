<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-schueler-adressen /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<div class="col-span-full">
			<s-card-schueler-beschaeftigung :list-schuelerbetriebe :beschaeftigungsarten-by-id
				:map-lehrer :betriebe-by-id :map-ansprechpartner :patch-schueler-betriebsdaten :set-schueler-betrieb />
			<s-card-schueler-add-adresse-modal v-if="!readonly" :id-schueler :beschaeftigungsarten-by-id :map-lehrer :betriebe-by-id :map-ansprechpartner
				:create-schueler-betriebsdaten v-slot="{ openModal }">
				<svws-ui-button class="contentFocusField mt-4" @click="openModal()" autofocus>Betrieb hinzufügen</svws-ui-button>
			</s-card-schueler-add-adresse-modal>
			<template v-if="(betriebsStammdaten !== undefined) && (betrieb !== undefined)">
				<s-card-schueler-adresse :benutzer-kompetenzen :betriebs-stammdaten :betrieb :orte-by-id
					:map-lehrer="mapLehrer" :map-ansprechpartner :create-ansprechpartner :patch-schueler-betriebsdaten :patch-betrieb :patch-ansprechpartner />
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { SchuelerAdressenProps } from "./SSChuelerAdressenProps";
	import { BenutzerKompetenz } from "@core";

	const props = defineProps<SchuelerAdressenProps>();
	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

</script>
