<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input class="contentFocusField" placeholder="Bezeichnung" :model-value="manager().auswahl().bezeichnung"
					@change="bezeichnung => patch({ bezeichnung: bezeichnung ?? undefined })" :readonly required :min-len="1" :max-len="250" />
				<svws-ui-text-input placeholder="Schlüssel" :model-value="manager().auswahl().schluessel"
					@change="schluessel => patch({ schluessel: schluessel ?? undefined })" :readonly :max-len="20" />
				<svws-ui-textarea-input placeholder="Beschreibung" :model-value="manager().auswahl().beschreibung"
					@change="beschreibung => patch({ beschreibung: beschreibung ?? undefined })" :readonly />
				<svws-ui-text-input placeholder="Personenart" :model-value="getPersonTypName(manager().auswahl().personTyp)" disabled />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { EinwilligungsartenDatenProps } from "./EinwilligungsartenDatenProps";
	import { BenutzerKompetenz, PersonTyp } from "@core";
	import { computed } from "vue";

	const props = defineProps<EinwilligungsartenDatenProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const getPersonTypName = (personTypID: number | undefined): string => {
		const personTyp = personTypID !== undefined ? PersonTyp.getByID(personTypID) : undefined;
		return personTyp ? personTyp.bezeichnung : PersonTyp.SCHUELER.toString();
	};

</script>
