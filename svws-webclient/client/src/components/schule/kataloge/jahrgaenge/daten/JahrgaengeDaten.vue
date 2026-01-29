<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						:model-value="manager().daten().kuerzel"
						@change="patchKuerzel"
						:valid="kuerzelIsValid" :min-len="1" :max-len="20" :readonly="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Bezeichnung"
						:model-value="manager().daten().bezeichnung"
						@change="patchBezeichnung"
						:valid="bezeichnungIsValid" :min-len="1" :max-len="100" :readonly="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Kurzbezeichnung"
						:model-value="manager().daten().kurzbezeichnung"
						@change="patchKurzbezeichnung"
						:valid="kurzbezeichnungIsValid" :max-len="2" :readonly="!hatKompetenzUpdate" />
					<ui-select label="Folgejahrgang"
						v-model="selectedFolgejahrgang"
						:manager="folgeJahrgangManager"
						:readonly="!hatKompetenzUpdate" />
					<ui-select label="Schulgliederung ASD-Kürzel"
						v-model="selectedSchulgliederung"
						:manager="schulgliederungKuerzelSelectManager"
						searchable statistics :readonly="!hatKompetenzUpdate" />
					<ui-select label="Schulgliederung ASD-Text"
						v-model="selectedSchulgliederung"
						:manager="schulgliederungTextSelectManager"
						searchable statistics :readonly="!hatKompetenzUpdate" />
					<ui-select label="Jahrgang ASD-Kürzel"
						:manager="jahrgangKuerzelSelectManager"
						v-model="selectedStatistikJahrgang"
						searchable statistics :readonly="!hatKompetenzUpdate" required :removable="false" />
					<ui-select label="Jahrgang ASD-Text"
						:manager="jahrgangTextSelectManager"
						v-model="selectedStatistikJahrgang"
						searchable statistics :readonly="!hatKompetenzUpdate" required :removable="false" />
					<svws-ui-input-number placeholder="Anzahl der Restabschnitte"
						:model-value="manager().daten().anzahlRestabschnitte"
						@change="patchAnzahlRestabschnitte"
						:valid="anzahlRestabschnitteIsValid" :min="0" :max="40" :readonly="!hatKompetenzUpdate" />
					<ui-select label="Bildungsstufe"
						:manager="bildungsstufeSelectManager"
						v-model="selectedBildungsstufe"
						:readonly="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						:model-value="manager().daten().sortierung"
						@change="patchSortierung"
						:valid="sortierungIsValid" :min="0" :max="32000" :readonly="!hatKompetenzUpdate" :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox :model-value="manager().daten().istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { Schulgliederung, Jahrgaenge, BenutzerKompetenz, Bildungsstufe } from "@core";
	import type { BildungsstufeKatalogEintrag, SchulgliederungKatalogEintrag, JahrgangsDaten, JahrgaengeKatalogEintrag } from "@core";
	import type { JahrgaengeDatenProps } from "./JahrgaengeDatenProps";
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid, optionalInputIsValid } from "~/util/validation/Validation";
	import { CoreTypeSelectManager, SelectManager } from "@ui";

	const props = defineProps<JahrgaengeDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const schulgliederungTextSelectManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const schulgliederungKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const jahrgangTextSelectManager = new CoreTypeSelectManager({
		clazz: Jahrgaenge.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const jahrgangKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Jahrgaenge.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const bildungsstufeSelectManager = new CoreTypeSelectManager({
		clazz: Bildungsstufe.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const availableFolgejahrgaenge = computed<JahrgangsDaten[]>(() => [...props.manager().liste.list()].filter(j => j.id !== props.manager().daten().id));
	const folgeJahrgangManager = new SelectManager({
		options: availableFolgejahrgaenge,
		optionDisplayText: v => v.bezeichnung ?? "",
		selectionDisplayText: v => v.bezeichnung ?? "",
	});

	const selectedFolgejahrgang = computed<JahrgangsDaten | null>({
		get: () => {
			const idFolgejahrgang = props.manager().daten().idFolgejahrgang;
			if (idFolgejahrgang === null) {
				return null;
			}

			const jahrgangsDaten = props.manager().liste.get(idFolgejahrgang);
			return jahrgangsDaten ?? null;
		},
		set: (value: JahrgangsDaten | null) => void props.patch({ idFolgejahrgang: value?.id ?? null }),
	});

	const selectedSchulgliederung = computed<SchulgliederungKatalogEintrag | null>({
		get: () => Schulgliederung.data().getEintragBySchuljahrUndSchluessel(props.schuljahr, props.manager().daten().kuerzelSchulgliederung ?? ""),
		set: (value: SchulgliederungKatalogEintrag | null) => void props.patch({ kuerzelSchulgliederung: value?.schluessel ?? null }),
	});

	const selectedStatistikJahrgang = computed<JahrgaengeKatalogEintrag | null>({
		get: () => Jahrgaenge.data().getEintragBySchuljahrUndSchluessel(props.schuljahr, props.manager().daten().kuerzelStatistik ?? ""),
		set: (value: JahrgaengeKatalogEintrag | null) => void props.patch({ kuerzelStatistik: value?.schluessel ?? null }),
	});

	const selectedBildungsstufe = computed<BildungsstufeKatalogEintrag | null>({
		get: () => Bildungsstufe.data().getEintragByID(props.manager().daten().idBildungsstufe ?? -1),
		set: (value: BildungsstufeKatalogEintrag | null) => void props.patch({ idBildungsstufe: value?.id ?? null }),
	});

	// patch

	async function patchKuerzel(kuerzel: string | null) {
		if (kuerzelIsValid(kuerzel)) {
			await props.patch({ kuerzel: kuerzel?.trim() ?? null });
		}
	}

	async function patchBezeichnung(bezeichnung: string | null) {
		if (bezeichnungIsValid(bezeichnung)) {
			await props.patch({ bezeichnung: bezeichnung?.trim() ?? null });
		}
	}

	async function patchKurzbezeichnung(kurzbezeichnung: string | null) {
		if (kurzbezeichnungIsValid(kurzbezeichnung)) {
			await props.patch({ kurzbezeichnung: kurzbezeichnung?.trim() ?? null });
		}
	}

	async function patchAnzahlRestabschnitte(anzahlRestabschnitte: number | null) {
		if (anzahlRestabschnitteIsValid(anzahlRestabschnitte)) {
			await props.patch({ anzahlRestabschnitte });
		}
	}
	async function patchSortierung(sortierung: number | null) {
		if (sortierungIsValid(sortierung)) {
			await props.patch({ sortierung: sortierung ?? undefined });
		}
	}

	// Validierung

	function kuerzelIsValid(kuerzel: string | null): boolean {
		return mandatoryInputIsValid(kuerzel, 20)
			&& isUniqueInList(kuerzel, props.manager().liste.list(), "kuerzel", "id", props.manager().auswahlID() ?? undefined);
	}

	function bezeichnungIsValid(bezeichnung: string | null): boolean {
		return mandatoryInputIsValid(bezeichnung, 100)
			&& isUniqueInList(bezeichnung, props.manager().liste.list(), "bezeichnung", "id", props.manager().auswahlID() ?? undefined);
	}

	function kurzbezeichnungIsValid(kurzbezeichnung: string | null): boolean {
		return optionalInputIsValid(kurzbezeichnung, 2);
	}

	function anzahlRestabschnitteIsValid(anzahlRestabschnitte: number | null): boolean {
		return !numberHasDecimals(anzahlRestabschnitte) && numberIsValid(anzahlRestabschnitte, false, 0, 40);
	}

	function sortierungIsValid(sortierung: number | null): boolean {
		return !numberHasDecimals(sortierung) && numberIsValid(sortierung, true, 0, 32000);
	}

</script>
