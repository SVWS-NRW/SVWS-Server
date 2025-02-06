<template>
	<div class="page--content">
		<svws-ui-content-card title="Einwilligungsart" class="w-full">
			<svws-ui-input-wrapper>
				<svws-ui-text-input class="contentFocusField w-5/5" placeholder="Bezeichnung" :model-value="einwilligungsartenListeManager().auswahl().bezeichnung"
					@change="bezeichnung => patch({ bezeichnung: bezeichnung ?? undefined })" />
				<svws-ui-text-input class="contentFocusField w-5/5" placeholder="Schlüssel" :model-value="einwilligungsartenListeManager().auswahl().schluessel"
					@change="schluessel => patch({ schluessel: schluessel ?? undefined })" />
				<svws-ui-text-input class="contentFocusField w-5/5" placeholder="Beschreibung" :model-value="einwilligungsartenListeManager().auswahl().beschreibung"
					@change="beschreibung => patch({ beschreibung: beschreibung ?? undefined })" />
				<svws-ui-text-input class="contentFocusField w-5/5" placeholder="Personenart" :model-value="getPersonTypName(einwilligungsartenListeManager().auswahl().personTyp)" disabled />
				<svws-ui-checkbox :model-value="einwilligungsartenListeManager().daten().istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })">
					Sichtbar
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { EinwilligungsartenDatenProps } from "./SEinwilligungsartenDatenProps";
	import { PersonTyp } from "@core";

	defineProps<EinwilligungsartenDatenProps>();

	const getPersonTypName = (personTypID: number | undefined): string => {
		const personTyp = personTypID !== undefined ? PersonTyp.getByID(personTypID) : undefined;
		return personTyp ? personTyp.bezeichnung : PersonTyp.SCHUELER.toString();
	};

</script>
