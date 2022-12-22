<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Schüler</span>
		</template>
		<template #abschnitt>
			<!--TODO: Abschnitt Auswahl
			<div v-if="schule_abschnitte" class="mt-2">
				<svws-ui-multi-select v-model="akt_abschnitt" :items="schule_abschnitte" :item-sort="item_sort" :item-text="item_text"></svws-ui-multi-select>
			</div>
			-->
		</template>
		<template #header>
			<div>
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
	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";

	import { SchuelerListeEintrag, SchuelerStatus, JahrgangsListeEintrag,
		KlassenListeEintrag, KursListeEintrag, Schulgliederung } from "@svws-nrw/svws-core-ts";

	import { injectMainApp, Main } from "~/apps/Main";
	import { RouteSchueler } from "~/router/apps/RouteSchueler";
	import { routeAppAuswahl } from "~/router/RouteUtils";

	export interface SchuelerProps {
		selectedItems: Array<SchuelerListeEintrag>;
		schulgliederung: Schulgliederung | undefined;
		filtered: boolean;
		search: string;
	}

	const props = defineProps<{ id: Number | undefined; item: SchuelerListeEintrag | undefined; routename: string }>();

	// TODO Speichere in einem speziellen Filter-Objekt
	const filtered: Ref<boolean> = ref(false);
	const search: Ref<string> = ref("");
	const cols = [
		{ key: "klasse", label: "Klasse", sortable: true, span: '1' },
		{ key: "nachname", label: "Nachname", sortable: true, span: '2' },
		{ key: "vorname", label: "Vorname", sortable: true, span: '2' },
	]
	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	const appKlassen = main.apps.klassen;
	const appSchule = main.apps.schule;
	const appJahrgaenge = main.apps.jahrgaenge;
	const appKurse = main.apps.kurse;

	// rows(): Array<SchuelerListeEintrag & {klasse: string}> {
	const rows: ComputedRef<Array<any>> = computed(() => {
		const array = app.auswahl.gefiltert.map(e => ({
			...e,
			klasse: appKlassen.auswahl.liste.find(k => k.id === e.idKlasse)?.kuerzel?.toString() || ""
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

	const selected = routeAppAuswahl(RouteSchueler);

	const inputKatalogSchuelerStatus: ComputedRef<Array<SchuelerStatus>> =
		computed(() => {
			return SchuelerStatus.values();
		});

	const filterStatus: WritableComputedRef<Array<SchuelerStatus> | undefined> =
		computed({
			get(): Array<SchuelerStatus> | undefined {
				return app.auswahl.filter.status;
			},
			set(value: Array<SchuelerStatus> | undefined) {
				const filter = app.auswahl.filter;
				if (filter) {
					filter.status = value || [];
					app.auswahl.filter = filter;
					filtered.value = true;
				}
			}
		});

	const inputSchulgliederungen: ComputedRef<Array<Schulgliederung> | undefined> = computed(() => {
		return appSchule.schulgliederungen;
	});

	const filterSchulgliederung: WritableComputedRef<Schulgliederung | undefined> = computed({
		get(): Schulgliederung | undefined {
			return app.auswahl.filter.schulgliederung;
		},
		set(value: Schulgliederung | undefined) {
			const filter = app.auswahl.filter;
			if (filter) {
				filter.schulgliederung = value;
				app.auswahl.filter = filter;
				filtered.value = true;
			}
		}
	});

	const inputJahrgaenge: ComputedRef<Array<JahrgangsListeEintrag> | undefined> = computed(() => {
		return appJahrgaenge.auswahl.liste;
	});

	const filterJahrgaenge: WritableComputedRef<JahrgangsListeEintrag | undefined> = computed({
		get(): JahrgangsListeEintrag | undefined {
			return app.auswahl.filter.jahrgang;
		},
		set(value: JahrgangsListeEintrag | undefined) {
			const filter = app.auswahl.filter;
			if (filter && app) {
				filter.jahrgang = value;
				filter.klasse = undefined;
				filter.kurs = undefined;
				app.auswahl.filter = filter;
				filtered.value = true;
			}
		}
	});

	const inputKlassen: ComputedRef<Array<KlassenListeEintrag> | undefined> = computed(() => {
		if (appKlassen === undefined)
			return undefined;
		const liste = [...appKlassen.auswahl.liste];
		const jahrgang = app.auswahl.filter.jahrgang;
		return (jahrgang === undefined)
			? liste
			: liste.filter(k => k.idJahrgang === jahrgang.id);
	});

	const filterKlassen: WritableComputedRef<KlassenListeEintrag | undefined> = computed({
		get(): KlassenListeEintrag | undefined {
			return app.auswahl.filter.klasse;
		},
		set(value: KlassenListeEintrag | undefined) {
			if (app) {
				const filter = app.auswahl.filter;
				filter.klasse = value;
				app.auswahl.filter = filter;
				filtered.value = true;
			}
		}
	});

	const inputKurse: ComputedRef<Array<KursListeEintrag> | undefined> = computed(() => {
		if (appKurse === undefined)
			return undefined;
		const liste = [...appKurse.auswahl.liste];
		const jahrgang = app.auswahl.filter.jahrgang;
		return (jahrgang === undefined)
			? liste
			: liste.filter(k => k.idJahrgaenge.contains(jahrgang.id));
	});

	const filterKurse: WritableComputedRef<KursListeEintrag | undefined> = computed({
		get(): KursListeEintrag | undefined {
			return app.auswahl.filter.kurs;
		},
		set(value: KursListeEintrag | undefined) {
			if (app) {
				const filter = app.auswahl.filter;
				filter.kurs = value;
				app.auswahl.filter = filter;
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
		get: () => app.auswahl.ausgewaehlt_gruppe,
		set: (items: SchuelerListeEintrag[]) => {
			app.auswahl.ausgewaehlt_gruppe = items;
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
		if (app) {
			search.value = "";
			const filter = app.auswahl.filter;
			filter.jahrgang = undefined;
			filter.klasse = undefined;
			filter.kurs = undefined;
			filter.schulgliederung = undefined;
			filter.status = [SchuelerStatus.AKTIV];
			app.auswahl.filter = filter;
			filtered.value = false;
		}
	}

</script>
