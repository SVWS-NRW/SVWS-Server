<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Kurse</h1>
			<div><abschnitt-auswahl :daten="schuljahresabschnittsauswahl" /></div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :clickable="!manager().liste.auswahlExists()" :clicked="clickedEintrag" @update:clicked="kursDaten => gotoDefaultView(kursDaten.id)"
				:items="rowsFiltered" :model-value="[...props.manager().liste.auswahl()]" @update:model-value="items => setAuswahl(items)"
				:columns selectable count :filter-open="true" :filtered="filterChanged()" :filterReset scroll-into-view scroll
				v-model:sort-by-and-order="sortByAndOrder" :sort-by-multi allow-arrow-key-selection :focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Kurs" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="filterNurSichtbar">Nur Sichtbare</svws-ui-checkbox>
					<svws-ui-multi-select v-model="filterSchueler" title="Schüler" :items="manager().schueler.list()" :item-text="textSchueler" :item-filter="findSchueler" autocomplete />
					<svws-ui-multi-select v-model="filterFaecher" title="Fach" :items="manager().faecher.list()" :item-text="text" :item-filter="find" autocomplete />
					<svws-ui-multi-select v-model="filterLehrer" title="Fachlehrer" :items="manager().lehrer.list()" :item-text="text" :item-filter="find" autocomplete />
					<svws-ui-multi-select v-model="filterJahrgaenge" title="Jahrgang" :items="manager().jahrgaenge.list()" :item-text="text" :item-filter="find" autocomplete />
					<svws-ui-multi-select v-model="filterSchulgliederung" title="Schulgliederung" :items="manager().schulgliederungen.list()" :item-text="text_schulgliederung" autocomplete />
				</template>
				<template #cell(lehrer)="{ value }"> {{ getLehrerKuerzel(value) }} </template>
				<template #cell(idJahrgaenge)="{ value }"> {{ getJahrgangsKuerzel(value) }} </template>
				<template #cell(schueler)="{ value }">{{ value.size() }}</template>
				<template #actions>
					<svws-ui-tooltip position="bottom" v-if="ServerMode.DEV.checkServerMode(serverMode) && hatKompetenzAendern">
						<svws-ui-button :disabled="activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="props.gotoHinzufuegenView(true)" :has-focus="rowsFiltered.length === 0">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neuen Kurs anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { KurseAuswahlProps } from "./SKurseAuswahlProps";
	import { ViewType, type DataTableColumn, type SortByAndOrder } from "@ui";
	import type { FachDaten, FaecherListeEintrag, JahrgangsDaten, KursDaten, LehrerListeEintrag, List, SchuelerListeEintrag, Schulgliederung } from "@core";
	import { ServerMode, BenutzerKompetenz } from "@core";
	import { useRegionSwitch } from "~/components/useRegionSwitch";

	const props = defineProps<KurseAuswahlProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const schuljahr = computed<number>(() => props.schuljahresabschnittsauswahl().aktuell.schuljahr);

	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc"},
		{ key: "lehrer", label: "Fachlehrer", sortable: true },
		{ key: "idJahrgaenge", label: "JG", tooltip: "Jahrgang", sortable: true, span: 0.5 },
		{ key: "schueler", label: "Schüler", span: 0.5, align: "right" },
	];

	const sortByMulti = computed<Map<string, boolean>>(() => {
		const map = new Map<string, boolean>();
		for (const pair of props.manager().orderGet())
			if (pair.b !== null)
				map.set(pair.a === "kuerzel" ? "kurse" : pair.a, pair.b);
		return map;
	})

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = props.manager().orderGet();
			if (list.isEmpty())
				return undefined;
			else {
				const { a: key, b: order} = list.get(0);
				return { key, order };
			}
		},
		set: (value) => {
			if ((value === undefined) || (value.key === null))
				return;
			props.manager().orderUpdate(value.key, value.order);
			void props.setFilter();
		},
	})

	function text(eintrag: LehrerListeEintrag | JahrgangsDaten | FachDaten): string {
		return eintrag.kuerzel ?? "";
	}

	function find(items: Iterable<LehrerListeEintrag | JahrgangsDaten | FachDaten>, search: string) {
		const list = [];
		for (const i of items)
			if ((i.kuerzel !== null) && i.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
				list.push(i);
		return list;
	}

	function textSchueler(eintrag: SchuelerListeEintrag): string {
		return eintrag.nachname + ", " + eintrag.vorname;
	}

	const findSchueler = (items: Iterable<SchuelerListeEintrag>, search: string) => {
		const list = [];
		for (const i of items)
			if ((i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase())) || (i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase())))
				list.push(i);
		return list;
	}

	function text_schulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten(schuljahr.value)?.kuerzel ?? '—';
	}

	const filterNurSichtbar = computed<boolean>({
		get: () => props.manager().filterNurSichtbar(),
		set: (value) => {
			props.manager().setFilterNurSichtbar(value);
			void props.setFilter();
			void props.setFilterNurSichtbar(value);
		},
	});

	const filterSchulgliederung = computed<Schulgliederung[]>({
		get: () => [...props.manager().schulgliederungen.auswahl()],
		set: (value) => {
			props.manager().schulgliederungen.auswahlClear();
			for (const v of value)
				props.manager().schulgliederungen.auswahlAdd(v);
			void props.setFilter();
		},
	});

	const filterJahrgaenge = computed<JahrgangsDaten[]>({
		get: () => [...props.manager().jahrgaenge.auswahl()],
		set: (value) => {
			props.manager().jahrgaenge.auswahlClear();
			for (const v of value)
				props.manager().jahrgaenge.auswahlAdd(v);
			void props.setFilter();
		},
	});

	const filterFaecher = computed<FaecherListeEintrag[]>({
		get: () => [...props.manager().faecher.auswahl()],
		set: (value) => {
			props.manager().faecher.auswahlClear();
			for (const v of value)
				props.manager().faecher.auswahlAdd(v);
			void props.setFilter();
		},
	});

	const filterLehrer = computed<LehrerListeEintrag[]>({
		get: () => [...props.manager().lehrer.auswahl()],
		set: (value) => {
			props.manager().lehrer.auswahlClear();
			for (const v of value)
				props.manager().lehrer.auswahlAdd(v);
			void props.setFilter();
		},
	});

	const filterSchueler = computed<SchuelerListeEintrag[]>({
		get: () => [...props.manager().schueler.auswahl()],
		set: (value) => {
			props.manager().schueler.auswahlClear();
			for (const v of value)
				props.manager().schueler.auswahlAdd(v);
			void props.setFilter();
		},
	});


	const search = ref("");

	const rowsFiltered = computed<KursDaten[]>(() => {
		const arr = [];
		for (const e of props.manager().filtered())
			if (e.kuerzel.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				arr.push(e);
		return arr;
	});


	async function filterReset() {
		props.manager().schulgliederungen.auswahlClear();
		props.manager().lehrer.auswahlClear();
		props.manager().schueler.auswahlClear();
		props.manager().jahrgaenge.auswahlClear();
		props.manager().setFilterNurSichtbar(true);
		await props.setFilter();
	}

	function filterChanged(): boolean {
		return (props.manager().schulgliederungen.auswahlExists()
			|| props.manager().lehrer.auswahlExists()
			|| props.manager().schueler.auswahlExists()
			|| props.manager().jahrgaenge.auswahlExists());
	}

	const clickedEintrag = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN))
			return null;
		return props.manager().hasDaten() ? props.manager().auswahl() : null;
	});

	async function setAuswahl(items : KursDaten[]) {
		props.manager().liste.auswahlClear();
		for (const item of items)
			if (props.manager().liste.hasValue(item))
				props.manager().liste.auswahlAdd(item);
		if (props.manager().liste.auswahlExists())
			await props.gotoGruppenprozessView(true);
		else
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
	}


	// TODO komma-separierte Liste mit Zusatzkräften
	function getLehrerKuerzel(idLehrer: number) {
		const lehrer = props.manager().lehrer.get(idLehrer);
		if (lehrer === null)
			return "---";
		return lehrer.kuerzel;
	}


	/**
	 * Ermittel eine komma-separierte Liste der Kürzel der Jahrgänge mit den übergebenen IDs.
	 *
	 * @param jahrgaengeIds   die Liste von Jahrgangs-IDs
	 */
	function getJahrgangsKuerzel(jahrgaengeIds: List<number>): string {
		return [...jahrgaengeIds].map(jgId => props.manager().jahrgaenge.get(jgId)?.kuerzel)
			.filter(jgKuerzel => (jgKuerzel !== undefined) && (jgKuerzel !== ''))
			.join(',');
	}

</script>
