<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input class="contentFocusField" placeholder="Kürzel" :model-value="manager().daten().kuerzel"
					:readonly :min-len="1" :max-len="10" @change="patchKuerzel" :valid="kuerzelIsValid" />
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="manager().daten().bezeichnung"
					:readonly :min-len="1" :max-len="50" @change="patchBezeichnung" :valid="v => (mandatoryInputIsValid(v, 50))" />
				<ui-select label="Floskelgruppenart" v-model="selectedFloskelgruppenart" :manager="floskelgruppenartManager" :removable="true" searchable />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { FloskelgruppenartKatalogEintrag } from "@core";
	import { BenutzerKompetenz, Floskelgruppenart } from "@core";
	import { computed } from "vue";
	import { isUniqueInList, mandatoryInputIsValid } from "~/util/validation/Validation";
	import { CoreTypeSelectManager } from "@ui";
	import type { FloskelgruppenDatenProps } from "./FloskelgruppenDatenProps";

	const props = defineProps<FloskelgruppenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzUpdate.value);
	const floskelgruppenartManager = new CoreTypeSelectManager({
		clazz: Floskelgruppenart.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const selectedFloskelgruppenart = computed<FloskelgruppenartKatalogEintrag | null>({
		get: (): FloskelgruppenartKatalogEintrag | null => {
			return Floskelgruppenart.data()
				.getWertByIDOrNull(props.manager().auswahl().idFloskelgruppenart ?? -1)
				?.daten(props.schuljahr) ?? null;
		},
		set: (value: FloskelgruppenartKatalogEintrag | null) => {
			void props.patch({ idFloskelgruppenart: value?.id ?? null });
		},
	});

	function kuerzelIsValid(value: string | null): boolean {
		return (mandatoryInputIsValid(value, 10) &&
			isUniqueInList(value, props.manager().liste.list(), "kuerzel", "id", props.manager().auswahlID()));
	}

	async function patchKuerzel(value: string | null) {
		if (kuerzelIsValid(value))
			await props.patch({ kuerzel: value?.trim() });

	}

	async function patchBezeichnung(value: string | null) {
		if (mandatoryInputIsValid(value, 50))
			await props.patch({ bezeichnung: value?.trim() });
	}


</script>
