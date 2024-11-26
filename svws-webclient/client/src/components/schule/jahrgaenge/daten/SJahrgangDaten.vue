<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kürzel" :model-value="jahrgangListeManager().daten().kuerzel" @change="patchKuerzel" type="text" :valid="validateKuerzel" :max-len="20" :min-len="1" />
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="jahrgangListeManager().daten().bezeichnung" @change="bezeichnung => patch({ bezeichnung: bezeichnung ?? '' })" type="text" />
				<svws-ui-select title="Schulgliederung" v-model="schulgliederung" :items="Schulgliederung.getBySchuljahrAndSchulform(schuljahr, schulform)" :item-text="textSchulgliederung" />
				<svws-ui-select title="Statistik-Jahrgang" v-model="statistikjahrgang" :items="Jahrgaenge.getListBySchuljahrAndSchulform(schuljahr, schulform)" :item-text="textStatistikJahrgang" :empty-text="() => textStatistikJahrgang(null)" removable statistics />
				<svws-ui-select title="Folgejahrgang" v-model="idFolgejahrgang" :items="folgejahrgaenge" :item-text="textFolgejahrgang" />
				<svws-ui-input-number placeholder="Anzahl der Restabschnitte" :model-value="jahrgangListeManager().daten().anzahlRestabschnitte" @change="patchRestabschnitte" :valid="validateRestabschnitte" :min="0" :max="40" :required="true" />
				<svws-ui-spacing />
				<svws-ui-input-number placeholder="Sortierung" :required="true" :min="0" :model-value="jahrgangListeManager().daten().sortierung"
					@change="sortierung => patch({ sortierung: sortierung ?? undefined })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { type JahrgangsDaten, Schulgliederung, Jahrgaenge } from "@core";
	import type { JahrgangDatenProps } from "./SJahrgangDatenProps";

	const props = defineProps<JahrgangDatenProps>();

	const folgejahrgaenge = computed<JahrgangsDaten[]>(() =>
		[...props.jahrgangListeManager().liste.list()].filter(j => j.id !== props.jahrgangListeManager().daten().id)
	);

	const idFolgejahrgang = computed<JahrgangsDaten | undefined>({
		get: () => {
			const idFolgejahrgang = props.jahrgangListeManager().daten().idFolgejahrgang;
			if (idFolgejahrgang !== null) {
				const jahrgangsDaten = props.jahrgangListeManager().liste.get(idFolgejahrgang)
				return jahrgangsDaten ?? undefined;
			}
			return undefined;
		},
		set: (value) => void props.patch({ idFolgejahrgang: value?.id }),
	});

	const textFolgejahrgang = (jahrgang: JahrgangsDaten) => {
		return (jahrgang.kuerzel !== '') ? jahrgang.kuerzel + ' : ' + jahrgang.bezeichnung : jahrgang.bezeichnung;
	}

	const schulgliederung = computed<Schulgliederung | null>({
		get: () => {
			const kuerzel = props.jahrgangListeManager().daten().kuerzelSchulgliederung;
			if (kuerzel === null)
				return null;
			return Schulgliederung.data().getWertByKuerzel(kuerzel);
		},
		set: (value) => {
			const kuerzel = value?.daten(props.schuljahr)?.kuerzel;
			void props.patch({ kuerzelSchulgliederung : kuerzel ?? null });
		},
	});

	function textSchulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten(props.schuljahr)?.kuerzel ?? '—';
	}

	const statistikjahrgang = computed<Jahrgaenge | null>({
		get: () => {
			const kuerzel = props.jahrgangListeManager().daten().kuerzelStatistik;
			return (kuerzel === null) ? null : Jahrgaenge.data().getWertByKuerzel(kuerzel);
		},
		set: (value) => {
			const kuerzel = value?.daten(props.schuljahr)?.kuerzel ?? null;
			void props.patch({ kuerzelStatistik : kuerzel });
		},
	});

	function textStatistikJahrgang(jahrgang: Jahrgaenge | null) {
		const jahrgangEintrag = jahrgang === null ? null : jahrgang.daten(props.schuljahr);
		if (jahrgangEintrag === null)
			return 'JU - Jahrgangsübergreifend';
		return jahrgangEintrag.kuerzel + " - " + jahrgangEintrag.text;
	}

	async function patchKuerzel(kuerzel: string | null) {
		if (validateKuerzel(kuerzel))
			await props.patch({ kuerzel });
	}

	async function patchRestabschnitte(value: number | null) {
		if (validateRestabschnitte(value))
			await props.patch({ anzahlRestabschnitte: value });
	}

	/**
	 * Wenn die übergebene Anzahl der Restabschnitte zwischen 0 und 40 ist wird <code>true</code> ansonsten <code>false</code> zurückgegeben.
	 *
	 * @param restabschnitte zu validierende Anzahl der Restabschnitte
	 */
	function validateRestabschnitte(restabschnitte: number | null): boolean {
		if ((restabschnitte === null) || (restabschnitte < 0) || (restabschnitte > 40))
			return false;
		return true;
	}

	/**
	 * Wenn das neue Kürzel zwischen 1 und 20 Zeichen lang ist und nicht bereits vergeben ist wird <code>true</code> ansonsten <code>false</code> zurückgegeben.
	 *
	 * @param kuerzel zu validierendes Kürzel
	 */
	function validateKuerzel (kuerzel: string | null): boolean {
		if ((kuerzel === null) || (kuerzel.length < 1) || (kuerzel.length > 20))
			return false;

		// Prüfen, ob das Kürzel bereits vergeben ist
		for (const jg of props.jahrgangListeManager().liste.list())
			if ((jg.id !== props.jahrgangListeManager().auswahlID()) && (jg.kuerzel === kuerzel))
				return false;

		return true;
	}

</script>
