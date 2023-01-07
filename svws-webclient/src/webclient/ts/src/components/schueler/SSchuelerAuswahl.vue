<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Schüler</span>
		</template>
		<template #abschnitt>
			<svws-ui-multi-select v-if="schule_abschnitte" v-model="akt_abschnitt" :items="schule_abschnitte" :item-sort="item_sort" :item-text="item_text"></svws-ui-multi-select>
		</template>
		<template #header>
			<div class="mt-1">
				<svws-ui-multi-select v-model="filterStatus" :items="inputKatalogSchuelerStatus" :item-text="text_status" tags title="Status" />
				<div class="input-wrapper mt-6">
					<svws-ui-multi-select v-model="filterKlassen" title="Klasse" :items="inputKlassen" :item-text="text_klasse" />
					<svws-ui-multi-select v-model="filterJahrgaenge" title="Jahrgang" :items="inputJahrgaenge" :item-text="text_jahrgang" />
					<svws-ui-multi-select v-model="filterKurse" title="Kurs" :items="inputKurse" :item-text="text_kurs" />
					<svws-ui-multi-select v-model="filterSchulgliederung" title="Schulgliederung" :items="inputSchulgliederungen" :item-text="text_schulgliederung" />
				</div>
			</div>
			<div>
				<div class="mt-2 flex flex-row items-center justify-between">
					<svws-ui-button type="transparent">
						<svws-ui-icon><i-ri-filter-3-line /></svws-ui-icon>
						<span class="ml-2">Erweiterte Filter</span>
					</svws-ui-button>
					<svws-ui-button v-show="filtered" type="transparent" @click="filterReset">
						<svws-ui-icon><i-ri-close-line /></svws-ui-icon>
						<span class="ml-2">Filter zurücksetzen</span>
					</svws-ui-button>
				</div>
			</div>
			<div>
				<div class="mt-6 mb-2">
					<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Namen oder Klasse"><i-ri-search-line/></svws-ui-text-input>
				</div>
			</div>
		</template>
		<template #content>
			<div class="container">
				<svws-ui-table v-model="selected" v-model:selection="selectedItems" :data="rowsFiltered" :columns="cols" is-multi-select :footer="true">
					<!-- Footer mit Button zum Hinzufügen einer Zeile -->
					<template #footer>
						<div class="text-sm-bold normal-case mr-auto">
							<span v-if="selectedItems.length">{{selectedItems.length}}/{{rowsFiltered.length}} ausgewählt</span>
							<span v-else>{{rowsFiltered.length}} Einträge</span>
						</div>
						<button class="button button--icon" @click="addLine()">
							<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
						</button>
						<button class="button button--icon">
							<svws-ui-icon><i-ri-file-copy-line /></svws-ui-icon>
						</button>
						<button class="button button--icon">
							<svws-ui-icon><i-ri-more-2-line /></svws-ui-icon>
						</button>
					</template>
				</svws-ui-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, Ref, ref, ShallowRef, WritableComputedRef } from "vue";

	import { SchuelerListeEintrag, SchuelerStatus, JahrgangsListeEintrag,
		KlassenListeEintrag, KursListeEintrag, Schulgliederung, Schuljahresabschnitt, LogConsumerConsole } from "@svws-nrw/svws-core-ts";

	import { injectMainApp, Main } from "~/apps/Main";
	import { routeSchueler } from "~/router/apps/RouteSchueler";
	import { ListSchueler } from "~/apps/schueler/ListSchueler";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { ListKlassen } from "~/apps/klassen/ListKlassen";
	import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";
	import { ListKurse } from "~/apps/kurse/ListKurse";
	import { DataTableColumn } from "@svws-nrw/svws-ui";

	export interface SchuelerProps {
		selectedItems: Array<SchuelerListeEintrag>;
		schulgliederung: Schulgliederung | undefined;
		filtered: boolean;
		search: string;
	}

	const { schule, listKlassen, mapKlassen, listJahrgaenge, listKurse } = defineProps<{ 
		item: ShallowRef<SchuelerListeEintrag | undefined>;
		stammdaten: DataSchuelerStammdaten;
		schule: DataSchuleStammdaten;
		listKlassen: ListKlassen;
		mapKlassen: Map<Number, KlassenListeEintrag>;
		listJahrgaenge: ListJahrgaenge;
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>;
		listKurse: ListKurse;
		mapKurs: Map<Number, KursListeEintrag>;
	}>();

	const selected = routeSchueler.auswahl;

	// TODO Speichere in einem speziellen Filter-Objekt
	const filtered: Ref<boolean> = ref(false);
	const search: Ref<string> = ref("");
	const cols: DataTableColumn[] = [
		{ key: "klasse", label: "Klasse", sortable: true, span: 1 },
		{ key: "nachname", label: "Nachname", sortable: true, span: 2 },
		{ key: "vorname", label: "Vorname", sortable: true, span: 2 },
	]
	const main: Main = injectMainApp();

	const listSchueler: ComputedRef<ListSchueler> = computed(() => routeSchueler.data.auswahl);

	// rows(): Array<SchuelerListeEintrag & {klasse: string}> {
	const rows: ComputedRef<Array<any>> = computed(() => {
		const array = listSchueler.value.gefiltert.map(e => ({
			...e,
			klasse: mapKlassen.get(e.idKlasse)?.kuerzel?.toString() || ""
		}));
		return array;
	});

	const rowsFiltered: ComputedRef<Array<any>> = computed(() => {
		const rowsConst = rows.value;
		if (search.value && rowsConst) {
			return rowsConst.filter(
				(e: any) =>
					e.nachname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()) ||
					e.vorname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
			);
		}
		return rowsConst;
	});

	const inputKatalogSchuelerStatus: ComputedRef<Array<SchuelerStatus>> =
		computed(() => {
			return SchuelerStatus.values();
		});

	const filterStatus: WritableComputedRef<Array<SchuelerStatus> | undefined> =
		computed({
			get(): Array<SchuelerStatus> | undefined {
				return listSchueler.value.filter.status;
			},
			set(value: Array<SchuelerStatus> | undefined) {
				const filter = listSchueler.value.filter;
				if (filter) {
					filter.status = value || [];
					listSchueler.value.filter = filter;
					filtered.value = true;
				}
			}
		});

	const inputSchulgliederungen = schule.schulgliederungen;

	const filterSchulgliederung: WritableComputedRef<Schulgliederung | undefined> = computed({
		get(): Schulgliederung | undefined {
			return listSchueler.value.filter.schulgliederung;
		},
		set(value: Schulgliederung | undefined) {
			const filter = listSchueler.value.filter;
			if (filter) {
				filter.schulgliederung = value;
				listSchueler.value.filter = filter;
				filtered.value = true;
			}
		}
	});

	const inputJahrgaenge: ComputedRef<Array<JahrgangsListeEintrag> | undefined> = computed(() => {
		return listJahrgaenge.liste;
	});

	const filterJahrgaenge: WritableComputedRef<JahrgangsListeEintrag | undefined> = computed({
		get(): JahrgangsListeEintrag | undefined {
			return listSchueler.value.filter.jahrgang;
		},
		set(value: JahrgangsListeEintrag | undefined) {
			const filter = listSchueler.value.filter;
			if (filter && listSchueler.value) {
				filter.jahrgang = value;
				filter.klasse = undefined;
				filter.kurs = undefined;
				listSchueler.value.filter = filter;
				filtered.value = true;
			}
		}
	});

	const inputKlassen: ComputedRef<Array<KlassenListeEintrag> | undefined> = computed(() => {
		const liste = [...listKlassen.liste];
		const jahrgang = listSchueler.value.filter.jahrgang;
		return (jahrgang === undefined) ? liste : liste.filter(k => k.idJahrgang === jahrgang.id);
	});

	const filterKlassen: WritableComputedRef<KlassenListeEintrag | undefined> = computed({
		get(): KlassenListeEintrag | undefined {
			return listSchueler.value.filter.klasse;
		},
		set(value: KlassenListeEintrag | undefined) {
			if (listSchueler.value) {
				const filter = listSchueler.value.filter;
				filter.klasse = value;
				listSchueler.value.filter = filter;
				filtered.value = true;
			}
		}
	});

	const inputKurse: ComputedRef<Array<KursListeEintrag> | undefined> = computed(() => {
		const liste = [...listKurse.liste];
		const jahrgang = listSchueler.value.filter.jahrgang;
		return (jahrgang === undefined)
			? liste
			: liste.filter(k => k.idJahrgaenge.contains(jahrgang.id));
	});

	const filterKurse: WritableComputedRef<KursListeEintrag | undefined> = computed({
		get(): KursListeEintrag | undefined {
			return listSchueler.value.filter.kurs;
		},
		set(value: KursListeEintrag | undefined) {
			if (listSchueler.value) {
				const filter = listSchueler.value.filter;
				filter.kurs = value;
				listSchueler.value.filter = filter;
				filtered.value = true;
			}
		}
	});

	function text_status(status: SchuelerStatus): string {
		if (status instanceof Array) return "";
		return status.bezeichnung.toString();
	}

	function text_klasse(klasse: KlassenListeEintrag): string {
		return klasse.kuerzel?.toString() ?? "";
	}

	function text_jahrgang(jahrgang: JahrgangsListeEintrag): string {
		return jahrgang.kuerzel?.toString() ?? "";
	}

	function text_kurs(kurs: KursListeEintrag): string {
		return kurs.kuerzel.toString();
	}

	function text_schulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten.kuerzel.toString();
	}

	const selectedItems: WritableComputedRef<SchuelerListeEintrag[]> = computed({
		get: () => listSchueler.value.ausgewaehlt_gruppe,
		set: (items: SchuelerListeEintrag[]) => {
			listSchueler.value.ausgewaehlt_gruppe = items;
		}
	});

	function onAction(action: string, item: SchuelerListeEintrag) {
		switch(action) {
			case 'delete':
				deleteSchueler(item);
				break;
			case 'copy':
				copySchuelerEintrag(item);
				break;
		}
	}

	function copySchuelerEintrag(item: SchuelerListeEintrag) {
		// TODO: Funktion implementieren
		console.log('copy geklickt', item);
	}

	function deleteSchueler(item: SchuelerListeEintrag) {
		// TODO delete Schueler
		console.log("delete", item);
	}

	function addLine() {
		// TODO: Funktion implementieren
		console.log('addLine geklickt');
	}

	function filterReset() {
		if (listSchueler.value) {
			search.value = "";
			const filter = listSchueler.value.filter;
			filter.jahrgang = undefined;
			filter.klasse = undefined;
			filter.kurs = undefined;
			filter.schulgliederung = undefined;
			filter.status = [ SchuelerStatus.AKTIV, SchuelerStatus.EXTERN ];
			listSchueler.value.filter = filter;
			filtered.value = false;
		}
	}
	const schule_abschnitte: ComputedRef<Array<Schuljahresabschnitt> | undefined> = computed(() => {
		return schule.daten?.abschnitte?.toArray(new Array<Schuljahresabschnitt>()) || [];
	});

	const akt_abschnitt: WritableComputedRef<Schuljahresabschnitt> = computed({
		get(): Schuljahresabschnitt {
			return main.config.akt_abschnitt;
		},
		set(abschnitt: Schuljahresabschnitt) {
			main.config.akt_abschnitt = abschnitt;
		}
	});

	function item_sort(a: Schuljahresabschnitt, b: Schuljahresabschnitt) {
		return b.schuljahr + b.abschnitt * 0.1 - (a.schuljahr + a.abschnitt * 0.1);
	}

	function item_text(item: Schuljahresabschnitt) {
		return item.schuljahr
			? `${item.schuljahr}, ${item.abschnitt}. HJ`
			: "Abschnitt";
	}

</script>
