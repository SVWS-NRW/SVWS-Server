<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input class="contentFocusField" placeholder="Kürzel" :model-value="manager().daten().kuerzel"
					:readonly :min-len="1" :max-len="10" @change="patchKuerzel" :valid="kuerzelIsValid" required />
				<svws-ui-text-input class="contentFocusField" placeholder="Text" :model-value="manager().daten().text"
					:readonly @change="patchText" required :valid="textIsValid" />
				<ui-select label="Floskelgruppe" v-model="selectedFloskelgruppe" :manager="floskelgruppenManager" :removable="false" searchable required />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { Floskelgruppe, List } from "@core";
	import { BenutzerKompetenz } from "@core";
	import type { FloskelnDatenProps } from "./FloskelnDatenProps";
	import { isUniqueInList, mandatoryInputIsValid } from "~/util/validation/Validation";
	import { SelectManager } from "@ui";

	const props = defineProps<FloskelnDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzUpdate.value);
	const floskelgruppen = computed<List<Floskelgruppe>>(() => props.manager().getFloskelgruppen());

	const floskelgruppenManager = new SelectManager({	options: floskelgruppen, optionDisplayText: v => v.bezeichnung ?? "",
		selectionDisplayText: v => v.bezeichnung ?? "",
	});

	const selectedFloskelgruppe = computed({
		get: (): Floskelgruppe | null => props.manager().getFloskelgruppenById().get(props.manager().auswahl().idFloskelgruppe ?? -1) ?? null,
		set: (value: Floskelgruppe | null) => {
			void props.patch({ idFloskelgruppe: value?.id ?? null });
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

	function textIsValid(value: string | null): boolean {
		return (value !== null) && (value !== "");
	}

	async function patchText(value: string | null) {
		if (textIsValid(value))
			await props.patch({ text: value?.trim() });
	}

</script>
