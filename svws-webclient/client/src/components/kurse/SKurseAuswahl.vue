<template>
	<svws-ui-secondary-menu>
		<template #headline>Kurse</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #content>
			<svws-ui-table clickable :clicked="kursListeManager().hasDaten() ? kursListeManager().auswahl() : null" @update:clicked="gotoEintrag"
				:items="rowsFiltered" :model-value="selectedItems" @update:model-value="items => setAuswahl(items)"
				:columns="cols" selectable count :filter-open="true" :filtered="filterChanged()" :filterReset="filterReset" scroll-into-view scroll>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Kurs" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="filterNurSichtbare">Nur Sichtbare</svws-ui-checkbox>
					<div/>
					<svws-ui-multi-select v-model="filterJahrgaenge" title="Jahrgang" :items="kursListeManager().jahrgaenge.list()" :item-text="text" :item-filter="find" />
					<svws-ui-multi-select v-model="filterLehrer" title="Fachlehrer" :items="kursListeManager().lehrer.list()" :item-text="text" :item-filter="find" />
					<svws-ui-multi-select v-model="filterSchueler" title="Schüler" :items="kursListeManager().schueler.list()" :item-text="textSchueler" :item-filter="findSchueler" />
					<svws-ui-multi-select v-model="filterSchulgliederung" title="Schulgliederung" :items="kursListeManager().schulgliederungen.list()" :item-text="text_schulgliederung" />
				</template>
				<template #cell(lehrer)="{ value }"> {{ getLehrerKuerzel(value) }} </template>
				<template #cell(idJahrgaenge)="{ value }"> {{ getJahrgangsKuerzel(value) }} </template>
				<template #cell(schueler)="{ value }">{{ value.size() }}</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { ArrayList, JahrgangsDaten, KursDaten, LehrerListeEintrag, SchuelerListeEintrag, Schulgliederung } from "@core";
	import type { Ref} from "vue";
	import type { KurseAuswahlProps } from "./SKurseAuswahlProps";
	import { ref, computed, shallowRef } from "vue";
	import type { DataTableColumn } from "@ui";

	const props = defineProps<KurseAuswahlProps>();

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc"},
		{ key: "lehrer", label: "Fachlehrer", sortable: true },
		{ key: "idJahrgaenge", label: "JG", tooltip: "Jahrgang", sortable: true, span: 0.5 },
		{ key: "schueler", label: "Schüler", span: 0.5, align: "right" },
	];

	function text(eintrag: LehrerListeEintrag | JahrgangsDaten): string {
		return eintrag.kuerzel ?? "";
	}

	const find = (items: Iterable<LehrerListeEintrag | JahrgangsDaten>, search: string) => {
		const list = [];
		for (const i of items)
			if (i.kuerzel?.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
				list.push(i);
		return list;
	}

	function textSchueler(eintrag: SchuelerListeEintrag): string {
		return eintrag.nachname + ", " + eintrag.vorname;
	}

	const findSchueler = (items: Iterable<SchuelerListeEintrag>, search: string) => {
		const list = [];
		for (const i of items)
			if ((i.nachname?.toLocaleLowerCase().includes(search.toLocaleLowerCase())) || (i.vorname?.toLocaleLowerCase().includes(search.toLocaleLowerCase())))
				list.push(i);
		return list;
	}

	function text_schulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten.kuerzel;
	}

	const filterNurSichtbare = computed<boolean>({
		get: () => props.kursListeManager().filterNurSichtbar(),
		set: (value) => {
			props.kursListeManager().setFilterNurSichtbar(value);
			void props.setFilter();
		}
	});

	const filterSchulgliederung = computed<Schulgliederung[]>({
		get: () => [...props.kursListeManager().schulgliederungen.auswahl()],
		set: (value) => {
			props.kursListeManager().schulgliederungen.auswahlClear();
			for (const v of value)
				props.kursListeManager().schulgliederungen.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterJahrgaenge = computed<JahrgangsDaten[]>({
		get: () => [...props.kursListeManager().jahrgaenge.auswahl()],
		set: (value) => {
			props.kursListeManager().jahrgaenge.auswahlClear();
			for (const v of value)
				props.kursListeManager().jahrgaenge.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterLehrer = computed<LehrerListeEintrag[]>({
		get: () => [...props.kursListeManager().lehrer.auswahl()],
		set: (value) => {
			props.kursListeManager().lehrer.auswahlClear();
			for (const v of value)
				props.kursListeManager().lehrer.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterSchueler = computed<SchuelerListeEintrag[]>({
		get: () => [...props.kursListeManager().schueler.auswahl()],
		set: (value) => {
			props.kursListeManager().schueler.auswahlClear();
			for (const v of value)
				props.kursListeManager().schueler.auswahlAdd(v);
			void props.setFilter();
		}
	});


	const search: Ref<string> = ref("");

	const rowsFiltered = computed<KursDaten[]>(() => {
		const arr = [];
		for (const e of props.kursListeManager().filtered())
			if (e.kuerzel?.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				arr.push(e);
		arr.sort((a, b) => a.sortierung - b.sortierung);
		return arr;
	});


	async function filterReset() {
		props.kursListeManager().schulgliederungen.auswahlClear();
		props.kursListeManager().lehrer.auswahlClear();
		props.kursListeManager().schueler.auswahlClear();
		props.kursListeManager().jahrgaenge.auswahlClear();
		props.kursListeManager().setFilterNurSichtbar(true);
		await props.setFilter();
	}

	function filterChanged(): boolean {
		return (props.kursListeManager().schulgliederungen.auswahlExists()
			|| props.kursListeManager().lehrer.auswahlExists()
			|| props.kursListeManager().schueler.auswahlExists()
			|| props.kursListeManager().jahrgaenge.auswahlExists());
	}

	const selectedItems = shallowRef<KursDaten[]>([]);

	function setAuswahl(items : KursDaten[]) {
		const auswahl = props.kursListeManager().liste;
		for (const vorhanden of [ ... auswahl.auswahl() ])
			if (!items.includes(vorhanden))
				auswahl.auswahlRemove(vorhanden);
		for (const item of items)
			auswahl.auswahlAdd(item);
		selectedItems.value = [ ... auswahl.auswahl() ];
	}


	// TODO komma-separierte Liste mit Zusatzkräften
	function getLehrerKuerzel(list: number[]) {
		if (!props.kursListeManager().hasDaten())
			return "---";
		const idLehrer = props.kursListeManager().daten().lehrer;
		if (idLehrer === null)
			return "---";
		const lehrer = props.kursListeManager().lehrer.get(idLehrer);
		if (lehrer === null)
			return "---";
		return lehrer.kuerzel;
	}


	/**
	 * Ermittel eine komma-separierte Liste der Kürzel der Jahrgänge mit den übergebenen IDs.
	 *
	 * @param jahrgaenge   die Liste von Jahrgangs-IDs
	 */
	function getJahrgangsKuerzel(jahrgaenge: ArrayList<number>) : string {
		// Prüfe zunächst, ob die Liste der Jahrgänge von dem Kurs einen Jahrgang der Map beinhaltet.
		let found = false;
		let result = "";
		for (const jg of jahrgaenge) {
			const jahrgang = props.kursListeManager().jahrgaenge.get(jg);
			if ((jahrgang !== null) && (jahrgang.kuerzel !== null)) {
				if (found)
					result += ",";
				result += jahrgang.kuerzel;
				found = true;
			}
		}
		return result;
	}

</script>
