<template>
	<ui-table-grid name="Übersicht zu Anrechnungsstunden, Mehr- und Minderleistungsgründe" :cell-format="cellFormat" :manager="() => gridManager">
		<template #header v-if="gridManager.daten.size() !== 0">
			<th class="text-left">Typ</th>
			<th class="text-left">Grund</th>
			<th class="">Stunden</th>
			<th />
		</template>
		<template #default="{ row, index }">
			<td class="capitalize font-bold text-ui-brand text-left">
				{{ getTypText(row) }}
			</td>
			<td class="ui-table-grid-input ">
				<ui-select :key="`${row.id}-select`" :manager="getSelectManagerForRow(row)" headless :removable="false"
					:model-value="getGrund(row)" @update:model-value="setGrund(row, $event)" />
			</td>
			<td class="ui-table-grid-input" :ref="inputAnzahl(row, index)">
				{{ row.anzahl }}
			</td>
			<td>
				<div v-if="hatUpdateKompetenz" class="inline-flex gap-4">
					<svws-ui-button v-if="hatUpdateKompetenz" @click="removeDaten(row)" type="trash" />
				</div>
			</td>
		</template>
		<template #footer>
			<td class="col-span-4">
				<div class="w-fit flex flex-row items-center">
					<ui-select label="+ Mehrleistung hinzufügen" @update:model-value="addGrund($event)" :manager="selectManagerAddMehrleistung" style="width: 50.99rem" v-model="mehrleistung" :removable="false" />
				</div>
			</td>
			<td class="col-span-4">
				<div class="w-fit flex flex-row items-center">
					<ui-select label="+ Minderleistung hinzufügen" @update:model-value="addGrund($event)" :manager="selectManagerAddMinderleistung" style="width: 50.99rem" v-model="minderleistung" :removable="false" />
				</div>
			</td>
			<td class="col-span-4">
				<div class="w-fit flex flex-row items-center">
					<ui-select label="+ Anrechnung hinzufügen" @update:model-value="addGrund($event)" :manager="selectManagerAddAnrechnung" style="width: 50.99rem" :removable="false" />
				</div>
			</td>
		</template>
	</ui-table-grid>
</template>

<script setup lang="ts">

	import { GridManager, CoreTypeSelectManager } from "@ui";
	import type { ComponentPublicInstance } from "vue";
	import { computed, ref, watch, watchEffect } from "vue";
	import type { LehrerPersonalabschnittsdaten, LehrerPersonalabschnittsdatenAnrechnungsstunden, Schulform, List} from "@core";
	import { LehrerAnrechnungsgrundKatalogEintrag, LehrerMehrleistungsartKatalogEintrag, LehrerMinderleistungsartKatalogEintrag,LehrerMehrleistungsarten, LehrerMinderleistungsarten, LehrerAnrechnungsgrund, ArrayList, DeveloperNotificationException } from "@core";

	const props = defineProps<{
		hatUpdateKompetenz: boolean;
		personalabschnittsdaten: () => LehrerPersonalabschnittsdaten | null,
		schuljahr: number,
		schulform: Schulform;
		addMehrleistung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => Promise<void>;
		patchMehrleistung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number) => Promise<void>;
		removeMehrleistung: (data: LehrerPersonalabschnittsdatenAnrechnungsstunden) => Promise<void>;
		addMinderleistung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => Promise<void>;
		patchMinderleistung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number) => Promise<void>;
		removeMinderleistung: (data: LehrerPersonalabschnittsdatenAnrechnungsstunden) => Promise<void>;
		addAnrechnung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => Promise<void>;
		patchAnrechnung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number) => Promise<void>;
		removeAnrechnung: (data: LehrerPersonalabschnittsdatenAnrechnungsstunden) => Promise<void>;
	}>();

	const schuljahr = computed(() => props.schuljahr);

	const gridManager = new GridManager<string, LehrerPersonalabschnittsdatenAnrechnungsstunden, List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>({
		daten: computed<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>(() => {
			// Konvertiere in eine ArrayList für die GridManager-Kompatibilität
			const result = new ArrayList<LehrerPersonalabschnittsdatenAnrechnungsstunden>();

			const abschnittsdaten = props.personalabschnittsdaten();
			if (abschnittsdaten === null)
				return result;

			// Füge Mehrleistungen, Minderleistung und Anrechngen hinzu
			result.addAll(abschnittsdaten.mehrleistung);
			result.addAll(abschnittsdaten.minderleistung);
			result.addAll(abschnittsdaten.anrechnungen);

			// Sortiere nach Typ, dann idGrund, dann id
			result.sort({
				compare: (a: LehrerPersonalabschnittsdatenAnrechnungsstunden, b: LehrerPersonalabschnittsdatenAnrechnungsstunden): number => {
					if (a.idGrund < b.idGrund) return -1;
					if (a.idGrund > b.idGrund) return 1;
					const typA = getTypText(a);
					const typB = getTypText(b);
					if (typA > typB) return -1;
					if (typA < typB) return 1;
					if (a.id < b.id) return -1;
					if (a.id > b.id) return 1;
					return 0;
				},
			});
			return result;
		}),
		getRowKey: row => "-"+row.idGrund+"-"+row.id+"-"+getTypText(row),
		columns: [
			{ kuerzel: "typ", name: "Typ", width: "8rem", hideable: false },
			{ kuerzel: "grund", name: "Grund", width: "34rem", hideable: false },
			{ kuerzel: "anzahl", name: "Anzahl Stunden", width: "5rem", hideable: true },
			{ kuerzel: "Buttons", name: "Buttons", width: "4rem", hideable:false},
		],
	});

	const cellFormat = {
		widths: ['4rem', '8rem', '12rem', '6rem'],
	};
	const useIds = computed(() => {
		const set = new Set<number>();
		for (const daten of gridManager.daten)
			set.add(daten.idGrund);
		return set;
	});


	const verfuegbareMehrleistungsarten = computed(() => {
		const set = new Set<number>();
		for (const art of LehrerMehrleistungsarten.values()) {
			const daten = art.daten(props.schuljahr);
			if ((daten !== null) && (!useIds.value.has(daten.id)))
				set.add(daten.id);
		}
		return set;
	});


	const verfuegbareMinderleistungsarten = computed(() => {
		const set = new Set<number>();
		for (const art of LehrerMinderleistungsarten.values()) {
			const daten = art.daten(props.schuljahr);
			if ((daten !== null) && (!useIds.value.has(daten.id)))
				set.add(daten.id);
		}
		return set;
	});

	const verfuegbareAnrechnungen = computed(() => {
		const set = new Set<number>();
		for (const art of LehrerAnrechnungsgrund.values()) {
			const daten = art.daten(props.schuljahr);
			if ((daten !== null) && (!useIds.value.has(daten.id)))
				set.add(daten.id);
		}
		return set;
	});

	const mehrleistung = ref<LehrerMehrleistungsartKatalogEintrag | null>(null);
	const minderleistung = ref<LehrerMinderleistungsartKatalogEintrag | null>(null);

	const selectManagerAddMehrleistung = new CoreTypeSelectManager<LehrerMehrleistungsartKatalogEintrag, LehrerMehrleistungsarten>({
		clazz: LehrerMehrleistungsarten.class,
		schuljahr: schuljahr.value,
		schulformen: props.schulform,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
		filters: [{
			key: "verfuegbar",
			apply: (options) => {
				const result = new ArrayList<LehrerMehrleistungsartKatalogEintrag>();
				// Prüfe, ob die Items in den verfügbaren Mehrleistungsarten enthalten sind.
				for (const item of options)
					if (verfuegbareMehrleistungsarten.value.has(item.id))
						result.add(item);
				return result;
			},
		}],
	});

	const selectManagerAddMinderleistung = new CoreTypeSelectManager<LehrerMinderleistungsartKatalogEintrag, LehrerMinderleistungsarten>({
		clazz: LehrerMinderleistungsarten.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
		filters: [{
			key: "verfuegbar",
			apply: (options) => {
				const result = new ArrayList<LehrerMinderleistungsartKatalogEintrag>();
				// Prüfe, ob die Items in den verfügbaren Mehrleistungsarten enthalten sind.
				for (const item of options)
					if (verfuegbareMinderleistungsarten.value.has(item.id))
						result.add(item);
				return result;
			},
		}],
	});

	const selectManagerAddAnrechnung = new CoreTypeSelectManager<LehrerAnrechnungsgrundKatalogEintrag, LehrerAnrechnungsgrund>({
		clazz: LehrerAnrechnungsgrund.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
		filters: [{
			key: "verfuegbar",
			apply: (options) => {
				const result = new ArrayList<LehrerAnrechnungsgrundKatalogEintrag>();
				// Prüfe, ob die Items in den verfügbaren  Anrechnungen enthalten sing
				for (const item of options)
					if (verfuegbareAnrechnungen.value.has(item.id))
						result.add(item);
				return result;
			},
		}],
	});

	// Manuelle Herbeiführung der Reaktivität zwischen gridManager-Daten und Selectfelder
	watch(verfuegbareMehrleistungsarten, () => {
		selectManagerAddMehrleistung.updateFilteredOptions();
		selectManagerAddMinderleistung.updateFilteredOptions();
		selectManagerAddAnrechnung.updateFilteredOptions();
		const datenArray = gridManager.daten.toArray() as LehrerPersonalabschnittsdatenAnrechnungsstunden[];
		datenArray.forEach(daten => getSelectManagerForRow(daten).updateFilteredOptions());
	});

	// Map zur Speicherung der individuellen Select Manager für jede Zeile
	const selectManagerMap = new Map<string, CoreTypeSelectManager<any, any>>();

	// Funktion zum Erstellen oder Abrufen eines individuellen Select-Managers für jede Zeile
	function getSelectManagerForRow(row: LehrerPersonalabschnittsdatenAnrechnungsstunden) {

		const key = `${row.id}-${getTypText(row)}`;
		if (!selectManagerMap.has(key)) {
			let manager: CoreTypeSelectManager<any, any>;

			switch (getTypText(row)) {
				case 'mehrleistung':
					manager = new CoreTypeSelectManager<LehrerMehrleistungsartKatalogEintrag, LehrerMehrleistungsarten>({
						clazz: LehrerMehrleistungsarten.class,
						schuljahr: props.schuljahr,
						schulformen: props.schulform,
						optionDisplayText: "kuerzelText",
						selectionDisplayText: "kuerzelText",
						filters: [{
							key: "verfuegbar",
							apply: (options) => {
								const result = new ArrayList<LehrerMehrleistungsartKatalogEintrag>();
								// Prüfe, ob die Items in den verfügbaren Mehrleistungsarten enthalten ist
								for (const item of options)
									if (verfuegbareMehrleistungsarten.value.has(item.id))
										result.add(item);
								return result;
							},

						}],
					});
					break;
				case 'minderleistung':
					manager = new CoreTypeSelectManager<LehrerMinderleistungsartKatalogEintrag, LehrerMinderleistungsarten>({
						clazz: LehrerMinderleistungsarten.class,
						schuljahr: props.schuljahr,
						schulformen: props.schulform,
						optionDisplayText: "kuerzelText",
						selectionDisplayText: "kuerzelText",
						filters: [{
							key: "verfuegbar",
							apply: (options) => {
								const result = new ArrayList<LehrerMehrleistungsartKatalogEintrag>();
								// Prüfe, ob die Items in den verfügbaren Minderleistungen enthalten sind.
								for (const item of options)
									if (verfuegbareMinderleistungsarten.value.has(item.id))
										result.add(item);
								return result;
							},
						}],
					});
					break;
				case 'anrechnung':
					manager = new CoreTypeSelectManager<LehrerAnrechnungsgrundKatalogEintrag, LehrerAnrechnungsgrund>({
						clazz: LehrerAnrechnungsgrund.class,
						schuljahr: props.schuljahr,
						schulformen: props.schulform,
						optionDisplayText: "kuerzelText",
						selectionDisplayText: "kuerzelText",
						filters: [{
							key: "verfuegbar",
							apply: (options) => {
								const result = new ArrayList<LehrerMehrleistungsartKatalogEintrag>();
								// Prüfe, ob die Items in den verfügbaren Anrechnungen enthalten sind.
								for (const item of options)
									if (verfuegbareAnrechnungen.value.has(item.id))
										result.add(item);
								return result;
							},
						}],
					});
					break;
				default:
					throw new DeveloperNotificationException(`Unbekannter Typ: ${getTypText(row)}`);
			}
			selectManagerMap.set(key, manager);
			return manager;
		}
		return selectManagerMap.get(key)!;
	}

	// Funktionen für das v-model der Grund-Auswahl
	function getGrund(rowdata: LehrerPersonalabschnittsdatenAnrechnungsstunden): LehrerMehrleistungsartKatalogEintrag | LehrerMinderleistungsartKatalogEintrag | LehrerAnrechnungsgrundKatalogEintrag | null {
		switch (getTypText(rowdata)) {
			case 'mehrleistung': {
				const mehrleistung = LehrerMehrleistungsarten.values().find(e => e.daten(props.schuljahr)?.id === rowdata.idGrund);
				return mehrleistung?.daten(props.schuljahr) ?? null;
			}
			case 'minderleistung': {
				const minderleistung = LehrerMinderleistungsarten.values().find(e => e.daten(props.schuljahr)?.id === rowdata.idGrund);
				return minderleistung?.daten(props.schuljahr) ?? null;
			}
			case 'anrechnung': {
				const anrechnung = LehrerAnrechnungsgrund.values().find(e => e.daten(props.schuljahr)?.id === rowdata.idGrund);
				return anrechnung?.daten(props.schuljahr) ?? null;
			}
			default:
				return null;
		}
	}

	async function setGrund(rowdata: LehrerPersonalabschnittsdatenAnrechnungsstunden, value: LehrerMehrleistungsartKatalogEintrag | LehrerMinderleistungsartKatalogEintrag | LehrerAnrechnungsgrundKatalogEintrag | null | undefined): Promise<void> {
		if ((value !== undefined) && (value !== null)) {
			switch (getTypText(rowdata)) {
				case 'mehrleistung':
					await props.patchMehrleistung({idGrund: value.id}, rowdata.id);
					break;
				case 'minderleistung':
					await props.patchMinderleistung({idGrund: value.id}, rowdata.id);
					break;
				case 'anrechnung':
					await props.patchAnrechnung({idGrund: value.id}, rowdata.id);
					break;
			}
		}
	}

	// Funktionen zum Aktualisieren der Daten
	function updateAnzahl(datenId: number, neueStunden: number | null, typ: 'mehrleistung' | 'minderleistung' | 'anrechnung'): void {
		if (neueStunden === null)
			return;
		switch (typ) {
			case 'mehrleistung':
				void props.patchMehrleistung({ anzahl: neueStunden }, datenId);
				break;
			case 'minderleistung':
				void props.patchMinderleistung({ anzahl: neueStunden }, datenId);
				break;
			case 'anrechnung':
				void props.patchAnrechnung({ anzahl: neueStunden }, datenId);
				break;
		}
	}

	// Input-Handler für Stunden
	function inputAnzahl(row: LehrerPersonalabschnittsdatenAnrechnungsstunden, index: number) {
		const key = getTypText(row)+"-"+row.idGrund+"-"+row.id;
		const setter = (value: number | null) => updateAnzahl(row.id, value, getTypText(row));
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			if (element instanceof HTMLElement) {
				const input = gridManager.applyInputIntegerDiv(key, 4, index, element, 100, setter);
				if (input !== null) {
					watchEffect(() => gridManager.update(key, row.anzahl));
				}
			}
		};
	}

	// Funktionen zum Entfernen der Daten
	function removeDaten(row: LehrerPersonalabschnittsdatenAnrechnungsstunden): void {
		switch (getTypText(row)) {
			case 'mehrleistung': {
				void props.removeMehrleistung(row);
				break;
			}
			case 'minderleistung':
				void props.removeMinderleistung(row);
				break;
			case 'anrechnung':
				void props.removeAnrechnung(row);
				break;
		}
	}

	// Hilfsfunktion zum Ermitteln des Typ-Texts
	function getTypText(data: LehrerPersonalabschnittsdatenAnrechnungsstunden): 'anrechnung' | 'minderleistung' | 'mehrleistung' {
		if( LehrerMehrleistungsarten.values().find(e => e.daten(props.schuljahr)?.id === data.idGrund))
			return 'mehrleistung';
		else if (LehrerMinderleistungsarten.values().find(e => e.daten(props.schuljahr)?.id === data.idGrund))
			return 'minderleistung';
		else (LehrerAnrechnungsgrund.values().find(e => e.daten(props.schuljahr)?.id === data.idGrund))
		return 'anrechnung';
	}

	async function addGrund(value: LehrerMehrleistungsartKatalogEintrag | LehrerMinderleistungsartKatalogEintrag | LehrerAnrechnungsgrundKatalogEintrag | null | undefined) {
		if ((value !== undefined) && (value !== null)) {
			const data = {
				idAbschnittsdaten: props.personalabschnittsdaten()?.id ?? -1,
				idGrund: value.id,
				anzahl: 1, // oder ein anderer Defaultwert
			}
			if (value instanceof LehrerMehrleistungsartKatalogEintrag)
				await props.addMehrleistung(data);
			else if (value instanceof LehrerMinderleistungsartKatalogEintrag)
				await props.addMinderleistung(data);
			else if (value instanceof LehrerAnrechnungsgrundKatalogEintrag)
				await props.addAnrechnung(data);
			else
				throw new DeveloperNotificationException("Unbekannter Typ");
		}
		mehrleistung.value = null;
		minderleistung.value = null;
	}
</script>
