<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Kurse</h1>
			<div><abschnitt-auswahl /></div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :clickable="!kurseAuswahlState.manager.liste.auswahlExists()" :clicked="clickedEintrag" @update:clicked="kursDaten => kurseAuswahlState.gotoDefaultView(kursDaten.id)"
				:items="rowsFiltered" :model-value="[...kurseAuswahlState.manager.liste.auswahl()]" @update:model-value="items => setAuswahl(items)"
				:columns selectable count :filter-open="true" :filtered="filterChanged()" :filterReset scroll-into-view scroll
				v-model:sort-by-and-order="sortByAndOrder" :sort-by-multi allow-arrow-key-selection :focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Kurs" />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="filterNurSichtbar">Nur Sichtbare</svws-ui-checkbox>
					<svws-ui-multi-select v-model="filterSchueler" title="Schüler" :items="kurseAuswahlState.manager.schueler.list()" :item-text="textSchueler" :item-filter="findSchueler" autocomplete />
					<svws-ui-multi-select v-model="filterFaecher" title="Fach" :items="kurseAuswahlState.manager.faecher.list()" :item-text="text" :item-filter="find" autocomplete />
					<svws-ui-multi-select v-model="filterLehrer" title="Fachlehrer" :items="kurseAuswahlState.manager.lehrer.list()" :item-text="text" :item-filter="find" autocomplete />
					<svws-ui-multi-select v-model="filterJahrgaenge" title="Jahrgang" :items="kurseAuswahlState.manager.jahrgaenge.list()" :item-text="text" :item-filter="find" autocomplete />
					<svws-ui-multi-select v-model="filterSchulgliederung" title="Schulgliederung" :items="kurseAuswahlState.manager.schulgliederungen.list()" :item-text="text_schulgliederung" autocomplete />
				</template>
				<template #cell(lehrer)="{ value }"> {{ getLehrerKuerzel(value) }} </template>
				<template #cell(idJahrgaenge)="{ value }"> {{ getJahrgangsKuerzel(value) }} </template>
				<template #cell(schueler)="{ value }">{{ value.size() }}</template>
				<template #actions v-if="serverState.hasDev && hatKompetenzAendern">
					<svws-ui-tooltip position="bottom">
						<svws-ui-button :disabled="kurseAuswahlState.activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="kurseAuswahlState.gotoHinzufuegenView(true)" :has-focus="rowsFiltered.length === 0">
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

	import { computed, ref } from "vue";

	import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
	import type { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
	import type { FachDaten } from "@core/core/data/fach/FachDaten";
	import { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import type { List } from "@core/java/util/List";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useServerState } from "@ui/states/ServerState";
	import type { DataTableColumn, SortByAndOrder } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";

	import { useKurseAuswahlState } from "~/states/kurse/KurseAuswahlState";

	const benutzerState = useBenutzerState();
	const serverState = useServerState();
	const abschnittState = useAbschnittState();
	const kurseAuswahlState = useKurseAuswahlState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const hatKompetenzAendern = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "lehrer", label: "Fachlehrer", sortable: true },
		{ key: "idJahrgaenge", label: "JG", tooltip: "Jahrgang", sortable: true, span: 0.5 },
		{ key: "schueler", label: "Schüler", span: 0.5, align: "right" },
	];

	const sortByMulti = computed<Map<string, boolean>>(() => {
		const map = new Map<string, boolean>();
		for (const { field, ascending } of kurseAuswahlState.manager.orderGet()) {
			map.set(field === "kuerzel" ? "kurse" : field, ascending);
		}
		return map;
	});

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = kurseAuswahlState.manager.orderGet();
			if (list.length === 0) {
				return undefined;
			} else {
				const { field: key, ascending: order } = list[0];
				return { key, order };
			}
		},
		set: (value) => {
			if ((value === undefined) || (value.key === null)) {
				return;
			}
			kurseAuswahlState.manager.orderUpdate(value.key, value.order);
			void kurseAuswahlState.setFilter();
		},
	});

	function text(eintrag: LehrerListeEintrag | JahrgangsDaten | FachDaten): string {
		if (eintrag instanceof LehrerListeEintrag) {
			return `${eintrag.kuerzel} (${eintrag.nachname}, ${eintrag.vorname})`;
		} else if (eintrag instanceof JahrgangsDaten) {
			return eintrag.kuerzel ?? "";
		} else {
			return `${eintrag.kuerzel} (${eintrag.bezeichnung})`;
		}
	}

	function find(items: Iterable<LehrerListeEintrag | JahrgangsDaten | FachDaten>, search: string) {
		const list = [];
		for (const i of items) {
			if ((i.kuerzel !== null) && i.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase())) {
				list.push(i);
			}
		}
		return list;
	}

	function textSchueler(eintrag: SchuelerListeEintrag): string {
		return eintrag.nachname + ", " + eintrag.vorname;
	}

	const findSchueler = (items: Iterable<SchuelerListeEintrag>, search: string) => {
		const list = [];
		for (const i of items) {
			if ((i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase())) || (i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase()))) {
				list.push(i);
			}
		}
		return list;
	};

	function text_schulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten(abschnittState.auswahl.schuljahr)?.kuerzel ?? '—';
	}

	const filterNurSichtbar = computed<boolean>({
		get: () => kurseAuswahlState.manager.filterNurSichtbar(),
		set: (value) => {
			kurseAuswahlState.manager.setFilterNurSichtbar(value);
			void kurseAuswahlState.setFilter();
			void kurseAuswahlState.setFilterNurSichtbar(value);
		},
	});

	const filterSchulgliederung = computed<Schulgliederung[]>({
		get: () => [...kurseAuswahlState.manager.schulgliederungen.auswahl()],
		set: (value) => {
			kurseAuswahlState.manager.schulgliederungen.auswahlClear();
			for (const v of value) {
				kurseAuswahlState.manager.schulgliederungen.auswahlAdd(v);
			}
			void kurseAuswahlState.setFilter();
		},
	});

	const filterJahrgaenge = computed<JahrgangsDaten[]>({
		get: () => [...kurseAuswahlState.manager.jahrgaenge.auswahl()],
		set: (value) => {
			kurseAuswahlState.manager.jahrgaenge.auswahlClear();
			for (const v of value) {
				kurseAuswahlState.manager.jahrgaenge.auswahlAdd(v);
			}
			void kurseAuswahlState.setFilter();
		},
	});

	const filterFaecher = computed<FachDaten[]>({
		get: () => [...kurseAuswahlState.manager.faecher.auswahl()],
		set: (value) => {
			kurseAuswahlState.manager.faecher.auswahlClear();
			for (const v of value) {
				kurseAuswahlState.manager.faecher.auswahlAdd(v);
			}
			void kurseAuswahlState.setFilter();
		},
	});

	const filterLehrer = computed<LehrerListeEintrag[]>({
		get: () => [...kurseAuswahlState.manager.lehrer.auswahl()],
		set: (value) => {
			kurseAuswahlState.manager.lehrer.auswahlClear();
			for (const v of value) {
				kurseAuswahlState.manager.lehrer.auswahlAdd(v);
			}
			void kurseAuswahlState.setFilter();
		},
	});

	const filterSchueler = computed<SchuelerListeEintrag[]>({
		get: () => [...kurseAuswahlState.manager.schueler.auswahl()],
		set: (value) => {
			kurseAuswahlState.manager.schueler.auswahlClear();
			for (const v of value) {
				kurseAuswahlState.manager.schueler.auswahlAdd(v);
			}
			void kurseAuswahlState.setFilter();
		},
	});


	const search = ref("");

	const rowsFiltered = computed<KursDaten[]>(() => {
		const arr = [];
		for (const e of kurseAuswahlState.manager.filtered()) {
			if (e.kuerzel.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())) {
				arr.push(e);
			}
		}
		return arr;
	});


	async function filterReset() {
		kurseAuswahlState.manager.schulgliederungen.auswahlClear();
		kurseAuswahlState.manager.lehrer.auswahlClear();
		kurseAuswahlState.manager.schueler.auswahlClear();
		kurseAuswahlState.manager.jahrgaenge.auswahlClear();
		kurseAuswahlState.manager.setFilterNurSichtbar(true);
		await kurseAuswahlState.setFilter();
	}

	function filterChanged(): boolean {
		return (kurseAuswahlState.manager.schulgliederungen.auswahlExists()
			|| kurseAuswahlState.manager.lehrer.auswahlExists()
			|| kurseAuswahlState.manager.schueler.auswahlExists()
			|| kurseAuswahlState.manager.jahrgaenge.auswahlExists());
	}

	const clickedEintrag = computed(() => {
		if ((kurseAuswahlState.activeViewType === ViewType.GRUPPENPROZESSE) || (kurseAuswahlState.activeViewType === ViewType.HINZUFUEGEN)) {
			return null;
		}
		return kurseAuswahlState.manager.hasDaten() ? kurseAuswahlState.manager.auswahl() : null;
	});

	async function setAuswahl(items: KursDaten[]) {
		kurseAuswahlState.manager.liste.auswahlClear();
		for (const item of items) {
			if (kurseAuswahlState.manager.liste.hasValue(item)) {
				kurseAuswahlState.manager.liste.auswahlAdd(item);
			}
		}
		if (kurseAuswahlState.manager.liste.auswahlExists()) {
			await kurseAuswahlState.gotoGruppenprozessView(true);
		} else {
			await kurseAuswahlState.gotoDefaultView(kurseAuswahlState.manager.getVorherigeAuswahl()?.id);
		}
	}


	// TODO komma-separierte Liste mit Zusatzkräften
	function getLehrerKuerzel(idLehrer: number) {
		const lehrer = kurseAuswahlState.manager.lehrer.get(idLehrer);
		if (lehrer === null) {
			return "---";
		}
		return lehrer.kuerzel;
	}


	/**
	 * Ermittel eine komma-separierte Liste der Kürzel der Jahrgänge mit den übergebenen IDs.
	 *
	 * @param jahrgaengeIds   die Liste von Jahrgangs-IDs
	 */
	function getJahrgangsKuerzel(jahrgaengeIds: List<number>): string {
		return [...jahrgaengeIds].map(jgId => kurseAuswahlState.manager.jahrgaenge.get(jgId)?.kuerzel)
			.filter(jgKuerzel => (jgKuerzel !== undefined) && (jgKuerzel !== ''))
			.join(',');
	}

</script>
