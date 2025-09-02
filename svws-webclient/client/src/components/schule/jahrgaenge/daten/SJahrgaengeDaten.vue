<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Kürzel" :model-value="manager().daten().kuerzel" @change="patchKuerzel"
					:valid="kuerzelIsValid" :max-len="20" :min-len="1" :readonly required />
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="manager().daten().bezeichnung" :max-len="100" @change="patchBezeichnung"
					:valid="bezeichnungIsValid" :readonly />
				<svws-ui-text-input placeholder="Kurzbezeichnung" :model-value="manager().daten().kurzbezeichnung" :max-len="2"
					@change="patchKurzbezeichnung" :valid="kurzbezeichnungIsValid" :readonly />
				<svws-ui-select title="Schulgliederung" v-model="schulgliederung" :items="Schulgliederung.getBySchuljahrAndSchulform(schuljahr, schulform)"
					:item-text="textSchulgliederung" statistics :readonly />
				<svws-ui-select title="Jahrgang" v-model="statistikJahrgang" :items="manager().getAvailableJahrgaenge(statistikJahrgang)"
					:item-text="textStatistikJahrgang" removable statistics :readonly required />
				<svws-ui-select title="Folgejahrgang" v-model="folgejahrgang" :items="folgejahrgaenge" :item-text="textFolgejahrgang" :readonly />
				<svws-ui-input-number placeholder="Anzahl der Restabschnitte" :model-value="manager().daten().anzahlRestabschnitte"
					@change="patchAnzahlRestabschnitte" :valid="anzahlRestabschnitteIsValid" :min="0" :max="40" :readonly />
				<svws-ui-input-number placeholder="Sortierung" :min="0" :model-value="manager().daten().sortierung"
					@change="sortierung => patch({ sortierung: sortierung ?? undefined })" :readonly />
				<svws-ui-checkbox :model-value="manager().daten().istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })" :readonly>
					Sichtbar
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { Schulgliederung, Jahrgaenge, JavaString, BenutzerKompetenz } from "@core";
	import type { JahrgangsDaten } from "@core";
	import type { JahrgangDatenProps } from "./SJahrgaengeDatenProps";

	const props = defineProps<JahrgangDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzUpdate.value);
	const folgejahrgaenge = computed<JahrgangsDaten[]>(() => [...props.manager().liste.list()].filter(j => j.id !== props.manager().daten().id));

	const folgejahrgang = computed<JahrgangsDaten | undefined>({
		get: () => {
			const idFolgejahrgang = props.manager().daten().idFolgejahrgang;
			if (idFolgejahrgang === null)
				return undefined;

			const jahrgangsDaten = props.manager().liste.get(idFolgejahrgang)
			return jahrgangsDaten ?? undefined;
		},
		set: (value) => void props.patch({ idFolgejahrgang: value?.id }),
	});

	const schulgliederung = computed<Schulgliederung | null>({
		get: () => {
			const kuerzel = props.manager().daten().kuerzelSchulgliederung;
			return (kuerzel === null) ? null : Schulgliederung.data().getWertByKuerzel(kuerzel);
		},
		set: (value) => {
			const kuerzel = value?.daten(props.schuljahr)?.kuerzel;
			void props.patch({ kuerzelSchulgliederung : kuerzel ?? null });
		},
	});

	const statistikJahrgang = computed<Jahrgaenge | null>({
		get: () => {
			const kuerzel = props.manager().daten().kuerzelStatistik;
			return (kuerzel === null) ? null : Jahrgaenge.data().getWertByKuerzel(kuerzel);
		},
		set: (value) => {
			const kuerzelStatistik = value?.daten(props.schuljahr)?.kuerzel ?? null;
			void props.patch({ kuerzelStatistik });
		},
	});

	// --- Bezeichnungen ---
	function textSchulgliederung(schulgliederung: Schulgliederung): string {
		const eintrag = schulgliederung.daten(props.schuljahr);
		if (eintrag === null)
			return '_';
		return eintrag.kuerzel + ' - ' + eintrag.text;
	}

	function textStatistikJahrgang(jahrgang: Jahrgaenge | null) {
		const jahrgangEintrag = (jahrgang === null) ? null : jahrgang.daten(props.schuljahr);
		if (jahrgangEintrag === null)
			return 'JU - Jahrgangsübergreifend';

		return jahrgangEintrag.kuerzel + " - " + jahrgangEintrag.text;
	}

	const textFolgejahrgang = (jahrgang: JahrgangsDaten) => {
		return (JavaString.isBlank(jahrgang.kuerzel)) ? jahrgang.bezeichnung : jahrgang.kuerzel + ' : ' + jahrgang.bezeichnung;
	}


	// --- Patch ---
	async function patchBezeichnung(bezeichnung: string | null) {
		if (bezeichnungIsValid(bezeichnung))
			await props.patch({ bezeichnung: bezeichnung ?? '' });
	}

	async function patchKuerzel(kuerzel: string | null) {
		if (kuerzelIsValid(kuerzel))
			await props.patch({ kuerzel });
	}

	async function patchAnzahlRestabschnitte(anzahlRestabschnitte: number | null) {
		if (anzahlRestabschnitteIsValid(anzahlRestabschnitte))
			await props.patch({ anzahlRestabschnitte });
	}

	async function patchKurzbezeichnung(kurzbezeichnung: string | null) {
		if (kurzbezeichnungIsValid(kurzbezeichnung))
			await props.patch({ kurzbezeichnung });
	}


	// --- Validierung ---
	function bezeichnungIsValid(bezeichnung: string | null): boolean {
		if ((bezeichnung !== null) && (bezeichnung.length > 100))
			return false;

		for (const jahrgang of props.manager().liste.list())
			if ((jahrgang.id !== props.manager().auswahlID()) && JavaString.equalsIgnoreCase(jahrgang.bezeichnung, bezeichnung))
				return false;

		return true;
	}

	function kurzbezeichnungIsValid(kurzbezeichnung: string | null): boolean {
		return (kurzbezeichnung === null) || (kurzbezeichnung.length <= 2);
	}

	/**
	 * Wenn die übergebene Anzahl der Restabschnitte zwischen 0 und 40 ist, wird true ansonsten false zurückgegeben.
	 *
	 * @param anzahlRestabschnitte zu validierende Anzahl der Restabschnitte
	 */
	function anzahlRestabschnitteIsValid(anzahlRestabschnitte: number | null): boolean {
		return ((anzahlRestabschnitte !== null) && (anzahlRestabschnitte >= 0) && (anzahlRestabschnitte <= 40));
	}

	/**
	 * Wenn das neue Kürzel zwischen 1 und 20 Zeichen lang ist und nicht bereits vergeben ist, wird true ansonsten false zurückgegeben.
	 *
	 * @param kuerzel zu validierendes Kürzel
	 */
	function kuerzelIsValid (kuerzel: string | null): boolean {
		if ((kuerzel === null) || JavaString.isBlank(kuerzel) || (kuerzel.length > 20))
			return false;

		// Prüfen, ob das Kürzel bereits vergeben ist
		for (const jahrgang of props.manager().liste.list())
			if ((jahrgang.id !== props.manager().auswahlID()) && (jahrgang.kuerzel !== null) && JavaString.equalsIgnoreCase(jahrgang.kuerzel, kuerzel))
				return false;

		return true;
	}

</script>
