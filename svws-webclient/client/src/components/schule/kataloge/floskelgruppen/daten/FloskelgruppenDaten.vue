<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
					:model-value="manager().auswahl().kuerzel"
					@change="patchKuerzel"
					:valid="kuerzelIsValid"
					:min-len="1" :max-len="10" :readonly />
				<svws-ui-text-input placeholder="Bezeichnung"
					:model-value="manager().auswahl().bezeichnung"
					@change="patchBezeichnung"
					:valid="bezeichnungIsValid"
					:min-len="1" :max-len="50" :readonly />
				<ui-select label="Floskelgruppenart"
					v-model="selectedFloskelgruppenart"
					:manager="floskelgruppenartManager"
					:removable="false" searchable required />
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
			return Floskelgruppenart.data().getWertByIDOrNull(props.manager().auswahl().idFloskelgruppenart ?? -1)?.daten(props.schuljahr) ?? null;
		},
		set: (value: FloskelgruppenartKatalogEintrag | null) => void patchFloskelgruppenart(value?.id),
	});

	// patch
	async function patchKuerzel(value: string | null) {
		if (kuerzelIsValid(value)) {
			await props.patch({ kuerzel: value.trim() });
		}
	}

	async function patchBezeichnung(value: string | null) {
		if (bezeichnungIsValid(value)) {
			await props.patch({ bezeichnung: value.trim() });
		}
	}

	async function patchFloskelgruppenart(value: number | undefined): Promise<void> {
		if (value !== undefined) {
			await props.patch({ idFloskelgruppenart: value });
		}
	}

	// validate
	function kuerzelIsValid(value: string | null): value is string {
		return mandatoryInputIsValid(value, 10)
			&& isUniqueInList(value, props.manager().liste.list(), 'kuerzel', 'id', props.manager().auswahlID() ?? undefined);
	}

	function bezeichnungIsValid(value: string | null): value is string {
		return mandatoryInputIsValid(value, 50)
			&& isUniqueInList(value, props.manager().liste.list(), 'bezeichnung', 'id', props.manager().auswahlID() ?? undefined);
	}

</script>
