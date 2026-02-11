<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Förderschwerpunkt ASD-Kürzel"
						v-model="selectedFoerderschwerpunkt"
						:manager="foerderschwerpunktKuerzelManager"
						:valid="kuerzelIsValid" statistics
						searchable :disabled="!hatKompetenzUpdate" required :removable="false" />
					<ui-select label="Förderschwerpunkt ASD-Text" class="contentFocusField"
						v-model="selectedFoerderschwerpunkt"
						:manager="foerderschwerpunktTextManager"
						searchable :removable="false" statistics required :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Interne Bezeichnung" span="2"
						:model-value="manager().daten().kuerzel"
						@change="patchKuerzel"
						:valid="kuerzelIsValid" :min-len="1" :max-len="50" required :readonly="!hatKompetenzUpdate" />
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

	import type { FoerderschwerpunktKatalogEintrag } from "@core";
	import { BenutzerKompetenz, Foerderschwerpunkt } from "@core";
	import { computed } from "vue";
	import type { FoerderschwerpunkteDatenProps } from "~/components/schule/kataloge/foerderschwerpunkte/daten/FoerderschwerpunkteDatenProps";
	import { isUniqueInList, mandatoryInputIsValid, numberIsValid } from "~/util/validation/Validation";
	import { CoreTypeSelectManager } from "@ui";

	const props = defineProps<FoerderschwerpunkteDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const selectedFoerderschwerpunkt = computed<FoerderschwerpunktKatalogEintrag | null>({
		get: () => Foerderschwerpunkt.data().getEintragBySchuljahrUndSchluessel(props.schuljahr, props.manager().daten().kuerzelStatistik),
		set: (v: FoerderschwerpunktKatalogEintrag | null) => void patchKuerzelStatistik(v?.kuerzel ?? null),
	});

	const istSichtbar = computed<boolean>({
		get: () => props.manager().daten().istSichtbar,
		set: (v: boolean) => void patchSichtbar(v),
	});

	const foerderschwerpunktKuerzelManager = new CoreTypeSelectManager({
		clazz: Foerderschwerpunkt.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const foerderschwerpunktTextManager = new CoreTypeSelectManager({
		clazz: Foerderschwerpunkt.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	async function patchKuerzelStatistik(value: string | null): Promise<void> {
		await props.patch({ kuerzelStatistik: value ?? '' });
	}

	async function patchSortierung(value: number | null): Promise<void> {
		if (sortierungIsValid(value)) {
			await props.patch({ sortierung: value === null ? 32000 : value });
		}
	}

	async function patchKuerzel(value: string | null): Promise<void> {
		if (kuerzelIsValid(value)) {
			await props.patch({ kuerzel: value ?? '' });
		}
	}

	async function patchSichtbar(value: boolean): Promise<void> {
		await props.patch({ istSichtbar: value });
	}

	function kuerzelIsValid(value: string | null): boolean {
		if (!mandatoryInputIsValid(value, 50)) {
			return false;
		}

		return isUniqueInList(value, props.manager().liste.list(), "kuerzel", "id", props.manager().auswahlID() ?? undefined);
	}

	function sortierungIsValid(value: number | null): boolean {
		return numberIsValid(value, true, 0, 32000);
	}

</script>
