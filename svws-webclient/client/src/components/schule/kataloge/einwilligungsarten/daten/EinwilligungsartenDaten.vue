<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="2"
						:model-value="manager().auswahl().bezeichnung"
						@change="patchBezeichnung"
						:valid="bezeichnungIsValid" :min-len="1" :max-len="250" required :readonly="!hatKompetenzUpdate" />
					<ui-select label="Einwilligungsschlüssel" class="col-span-full"
						v-model="selectedEinwilligungsschluessel"
						:manager="einwilligungsschluesselCoreTypeManager"
						searchable :readonly="!hatKompetenzUpdate" />
					<svws-ui-textarea-input placeholder="Beschreibung" span="full"
						:model-value="manager().auswahl().beschreibung"
						@change="patchBeschreibung"
						:readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Personenart" span="2"
						:model-value="textPersonTyp(manager().auswahl().idPersonTyp)"
						readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						:model-value="manager().daten().sortierung"
						@change="patchSortierung"
						:valid="sortierungIsValid" :min="0" :max="32000" :readonly="!hatKompetenzUpdate" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="istSichtbar" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { EinwilligungsartenDatenProps } from "./EinwilligungsartenDatenProps";
	import type { EinwilligungsschluesselKatalogEintrag, List } from "@core";
	import { BenutzerKompetenz, Einwilligungsschluessel, PersonTyp, ArrayList } from "@core";
	import { computed, watch } from "vue";
	import { mandatoryInputIsValid, numberIsValid } from "~/util/validation/Validation";
	import { CoreTypeSelectManager } from "@ui";

	const props = defineProps<EinwilligungsartenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const selectedEinwilligungsschluessel = computed<EinwilligungsschluesselKatalogEintrag | null>({
		get: () => Einwilligungsschluessel.data().getEintragBySchuljahrUndSchluessel(props.schuljahr, props.manager().daten().schluessel ?? ''),
		set: (v: EinwilligungsschluesselKatalogEintrag | null) => void patchEinwilligungsschluessel(v?.schluessel ?? null),
	});

	const istSichtbar = computed<boolean>({
		get: () => props.manager().daten().istSichtbar,
		set: (v: boolean) => void patchSichtbar(v),
	});

	async function patchEinwilligungsschluessel(value: string | null): Promise<void> {
		await props.patch({ schluessel: value });
	}

	async function patchBezeichnung(bezeichnung: string | null) {
		if (bezeichnungIsValid(bezeichnung)) {
			await props.patch({ bezeichnung: bezeichnung ?? '' });
		}
	}

	async function patchSichtbar(value: boolean): Promise<void> {
		await props.patch({ istSichtbar: value });
	}

	async function patchSortierung(value: number | null): Promise<void> {
		if (sortierungIsValid(value)) {
			await props.patch({ sortierung: value === null ? 32000 : value });
		}
	}

	async function patchBeschreibung(beschreibung: string | null) {
		await props.patch({ beschreibung: beschreibung ?? undefined });
	}

	function sortierungIsValid(value: number | null): boolean {
		return numberIsValid(value, true, 0, 32000);
	}

	function bezeichnungIsValid(value: string | null) {
		if (!mandatoryInputIsValid(value, 250)) {
			return false;
		}

		if (props.manager().daten().idPersonTyp === -1) {
			return true;
		}

		for (const einwilligungsart of props.manager().liste.list()) {
			if ((einwilligungsart.id !== props.manager().daten().id)
				&& (einwilligungsart.idPersonTyp === props.manager().daten().idPersonTyp)
				&& (einwilligungsart.bezeichnung.toLowerCase() === value.toLowerCase())) {
				return false;
			}
		}
		return true;
	}

	function textPersonTyp(idPpersonTyp: number): string {
		const personTyp = PersonTyp.getByID(idPpersonTyp) ?? null;
		return personTyp ? personTyp.bezeichnung : "";
	};

	const einwilligungsschluesselFilter = {
		key: "isNotUsed",
		apply: (options: List<EinwilligungsschluesselKatalogEintrag>) => {
			const filtered = new ArrayList<EinwilligungsschluesselKatalogEintrag>();
			for (const option of options) {
				if (!einwilligungsschluesselIsUsed(option)) {
					filtered.add(option);
				}
			}
			return filtered;
		},
	};

	function einwilligungsschluesselIsUsed(einwilligungsschluessel: EinwilligungsschluesselKatalogEintrag) {
		for (const einwilligungsart of props.manager().liste.list()) {
			if ((einwilligungsart.id !== props.manager().auswahl().id)
				&& (einwilligungsart.idPersonTyp === props.manager().auswahl().idPersonTyp)
				&& (einwilligungsart.schluessel === einwilligungsschluessel.schluessel)) {
				return true;
			}
		}
		return false;
	}

	const einwilligungsschluesselCoreTypeManager = new CoreTypeSelectManager({
		filters: [einwilligungsschluesselFilter],
		clazz: Einwilligungsschluessel.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	watch(() => props.manager().auswahl(), async () => {
		einwilligungsschluesselCoreTypeManager.updateFilteredOptions();
	}, { immediate: true });

	watch(() => props.manager().auswahl().idPersonTyp, async () => {
		einwilligungsschluesselCoreTypeManager.updateFilteredOptions();
	}, { immediate: true });

</script>
