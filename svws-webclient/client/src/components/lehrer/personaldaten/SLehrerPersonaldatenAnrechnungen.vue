<template>
	<ui-table-grid name="Übersicht zu Anrechnungsstunden, Mehr- und Minderleistungsgründe" :manager="() => gridManager">
		<template #header v-if="gridManager.daten.size() !== 0">
			<th class="text-left">Typ</th>
			<th class="text-left">Grund</th>
			<th class="">Stunden</th>
			<th />
		</template>
		<template #default="{ row, index }">
			<td class="capitalize font-bold text-ui-brand text-left">
				{{ row.typ }}
			</td>
			<td class="text-left">
				{{ getGrundText(row) }}
			</td>
			<td class="ui-table-grid-input" :ref="inputAnzahl(row, index)">
				{{ row.data.anzahl }}
			</td>
			<td>
				<div v-if="hatUpdateKompetenz" class="inline-flex gap-4">
					<svws-ui-button v-if="hatUpdateKompetenz" @click="removeDaten(row)" type="trash" />
				</div>
			</td>
		</template>
		<template #footer>
			<template v-if="hatUpdateKompetenz">
				<td class="col-span-4 text-right">
					<svws-ui-tooltip>
						<svws-ui-button type="icon" @click="openHinzufuegen">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Anrechnungs-, Mehr- oder Minderleistungsgründe hinzufügen
						</template>
					</svws-ui-tooltip>
				</td>
			</template>
			<template v-else>
				<td class="col-span-4" />
			</template>
		</template>
	</ui-table-grid>
	<svws-ui-modal v-model:show="showHinzufuegen" size="medium" class="hidden">
		<template #modalTitle> Anrechnungs-, Mehr- oder Minderleistungsgründe hinzufügen </template>
		<template #modalContent>
			<div class="flex flex-row">
				<div class="basis-3/4">
					<ui-select-multi label="Mehrleistungsgründe" v-model="auswahlMehrleistungenNeu" :manager="mehrleistungenSelectManager" statistics required />
					<ui-select-multi label="Minderleistungsgründe" v-model="auswahlMinderleistungenNeu" :manager="minderleistungenSelectManager" statistics required />
					<ui-select-multi label="Anrechnungsgründe" v-model="auswahlAnrechnungenNeu" :manager="anrechnungenSelectManager" statistics required />
				</div>
				<div class="basis-1/4 flex flex-row justify-evenly items-end">
					<svws-ui-button type="secondary" @click="showHinzufuegen = false"> Abbrechen </svws-ui-button>
					<svws-ui-button @click="create"> Anlegen </svws-ui-button>
				</div>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { GridManager, CoreTypeSelectManager } from "@ui";
	import type { ComponentPublicInstance } from "vue";
	import { computed, ref, shallowRef } from "vue";
	import type { LehrerPersonalabschnittsdaten, LehrerPersonalabschnittsdatenAnrechnungsstunden, Schulform, List, JavaSet, LehrerAnrechnungsgrundKatalogEintrag,
		LehrerMehrleistungsartKatalogEintrag, LehrerMinderleistungsartKatalogEintrag,
		Comparator } from "@core";
	import { LehrerMehrleistungsarten, LehrerMinderleistungsarten, LehrerAnrechnungsgrund, ArrayList, HashSet } from "@core";

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

	type Eintrag = { typ: 'mehrleistung' | 'minderleistung' | 'anrechnung', data: LehrerPersonalabschnittsdatenAnrechnungsstunden };

	const comparatorEintrag: Comparator<Eintrag> = {
		compare: (a: Eintrag, b: Eintrag): number => {
			if ((a.data.idGrund === null) && (b.data.idGrund !== null)) {
				return -1;
			}
			if ((a.data.idGrund !== null) && (b.data.idGrund === null)) {
				return 1;
			}
			if ((a.data.idGrund !== null) && (b.data.idGrund !== null)) {
				if (a.data.idGrund < b.data.idGrund) {
					return -1;
				}
				if (a.data.idGrund > b.data.idGrund) {
					return 1;
				}
			}
			if (a.typ > b.typ) {
				return -1;
			}
			if (a.typ < b.typ) {
				return 1;
			}
			if (a.data.id < b.data.id) {
				return -1;
			}
			if (a.data.id > b.data.id) {
				return 1;
			}
			return 0;
		},
	};

	const gridManager = new GridManager<string, Eintrag, List<Eintrag>>({
		daten: computed<List<Eintrag>>(() => {
			const result = new ArrayList<Eintrag>();
			const abschnittsdaten = props.personalabschnittsdaten();
			if (abschnittsdaten === null) {
				return result;
			}
			// Füge Mehrleistungen, Minderleistung und Anrechngen hinzu
			for (const data of abschnittsdaten.mehrleistung) {
				result.add({ typ: 'mehrleistung', data });
			}
			for (const data of abschnittsdaten.minderleistung) {
				result.add({ typ: 'minderleistung', data });
			}
			for (const data of abschnittsdaten.anrechnungen) {
				result.add({ typ: 'anrechnung', data });
			}
			result.sort(comparatorEintrag);
			return result;
		}),
		getRowKey: row => "-" + row.data.idGrund + "-" + row.data.id + "-" + row.typ,
		columns: [
			{ kuerzel: "typ", name: "Typ", width: "8rem", hideable: false },
			{ kuerzel: "grund", name: "Grund", width: "minmax(40%,100%)", hideable: false },
			{ kuerzel: "anzahl", name: "Anzahl Stunden", width: "5rem", hideable: true },
			{ kuerzel: "Buttons", name: "Buttons", width: "4rem", hideable: false },
		],
	});

	function updateAnzahl(row: Eintrag, anzahl: number | null): void {
		if (anzahl === null) {
			return;
		}
		if (row.typ === 'mehrleistung') {
			void props.patchMehrleistung({ anzahl }, row.data.id);
		} else if (row.typ === 'minderleistung') {
			void props.patchMinderleistung({ anzahl }, row.data.id);
		} else {
			void props.patchAnrechnung({ anzahl }, row.data.id);
		}
	}

	function inputAnzahl(row: Eintrag, index: number) {
		const key = row.typ + "-" + row.data.idGrund + "-" + row.data.id;
		const setter = (value: number | null) => updateAnzahl(row, value);
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputNumberFixed(key, 4, index, element, 100, 2, setter);
			if (input !== null) {
				gridManager.update(key, row.data.anzahl);
			}
		};
	}

	function removeDaten(row: Eintrag): void {
		if (row.typ === 'mehrleistung') {
			void props.removeMehrleistung(row.data);
		} else if (row.typ === 'minderleistung') {
			void props.removeMinderleistung(row.data);
		} else {
			void props.removeAnrechnung(row.data);
		}
	}

	function getGrundText(row: Eintrag): string {
		let data = null;
		if (row.data.idGrund !== null) {
			if (row.typ === 'mehrleistung') {
				data = LehrerMehrleistungsarten.data().getEintragByID(row.data.idGrund);
			} else if (row.typ === 'minderleistung') {
				data = LehrerMinderleistungsarten.data().getEintragByID(row.data.idGrund);
			} else {
				data = LehrerAnrechnungsgrund.data().getEintragByID(row.data.idGrund);
			}
		}
		return (data === null) ? "???" : data.kuerzel + " - " + data.text;
	}

	const showHinzufuegen = ref<boolean>(false);

	const auswahlMehrleistungenNeu = shallowRef<Array<LehrerMehrleistungsartKatalogEintrag>>([]);
	const auswahlMinderleistungenNeu = shallowRef<Array<LehrerMinderleistungsartKatalogEintrag>>([]);
	const auswahlAnrechnungenNeu = shallowRef<Array<LehrerAnrechnungsgrundKatalogEintrag>>([]);

	const mehrleistungenSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerMehrleistungsarten.class, schuljahr: props.schuljahr, schulformen: props.schulform,
		filters: [{ key: 'vorhandene', apply: filterMehrleistungen }],
		selectionDisplayText: 'text', optionDisplayText: 'kuerzelText',
	}));

	const minderleistungenSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerMinderleistungsarten.class, schuljahr: props.schuljahr, schulformen: props.schulform,
		filters: [{ key: 'vorhandene', apply: filterMinderleistungen }],
		selectionDisplayText: 'text', optionDisplayText: 'kuerzelText',
	}));

	const anrechnungenSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerAnrechnungsgrund.class, schuljahr: props.schuljahr, schulformen: props.schulform,
		filters: [{ key: 'vorhandene', apply: filterAnrechnungen }],
		selectionDisplayText: 'text', optionDisplayText: 'kuerzelText',
	}));

	function openHinzufuegen() {
		auswahlMehrleistungenNeu.value = [];
		auswahlMinderleistungenNeu.value = [];
		auswahlAnrechnungenNeu.value = [];
		showHinzufuegen.value = true;
	}

	const mehrleistungenVorhanden = computed<JavaSet<number>>(() => {
		const vorhanden = new HashSet<number>();
		const abschnittsdaten = props.personalabschnittsdaten();
		if (abschnittsdaten === null) {
			return vorhanden;
		}
		for (const mehrleistung of abschnittsdaten.mehrleistung) {
			if (mehrleistung.idGrund !== null) {
				vorhanden.add(mehrleistung.idGrund);
			}
		}
		return vorhanden;
	});

	function filterMehrleistungen(options: List<LehrerMehrleistungsartKatalogEintrag>): List<LehrerMehrleistungsartKatalogEintrag> {
		const result = new ArrayList<LehrerMehrleistungsartKatalogEintrag>();
		for (const e of options) {
			if (!mehrleistungenVorhanden.value.contains(e.id)) {
				result.add(e);
			}
		}
		return result;
	}

	const minderleistungenVorhanden = computed<JavaSet<number>>(() => {
		const vorhanden = new HashSet<number>();
		const abschnittsdaten = props.personalabschnittsdaten();
		if (abschnittsdaten === null) {
			return vorhanden;
		}
		for (const minderleistung of abschnittsdaten.minderleistung) {
			if (minderleistung.idGrund !== null) {
				vorhanden.add(minderleistung.idGrund);
			}
		}
		return vorhanden;
	});

	function filterMinderleistungen(options: List<LehrerMinderleistungsartKatalogEintrag>): List<LehrerMinderleistungsartKatalogEintrag> {
		const result = new ArrayList<LehrerMinderleistungsartKatalogEintrag>();
		for (const e of options) {
			if (!minderleistungenVorhanden.value.contains(e.id)) {
				result.add(e);
			}
		}
		return result;
	}

	const anrechnungenVorhanden = computed<JavaSet<number>>(() => {
		const vorhanden = new HashSet<number>();
		const abschnittsdaten = props.personalabschnittsdaten();
		if (abschnittsdaten === null) {
			return vorhanden;
		}
		for (const anrechnung of abschnittsdaten.anrechnungen) {
			if (anrechnung.idGrund !== null) {
				vorhanden.add(anrechnung.idGrund);
			}
		}
		return vorhanden;
	});

	function filterAnrechnungen(options: List<LehrerAnrechnungsgrundKatalogEintrag>): List<LehrerAnrechnungsgrundKatalogEintrag> {
		const result = new ArrayList<LehrerAnrechnungsgrundKatalogEintrag>();
		for (const e of options) {
			if (!anrechnungenVorhanden.value.contains(e.id)) {
				result.add(e);
			}
		}
		return result;
	}

	async function create() {
		for (const eintrag of auswahlMehrleistungenNeu.value) {
			if (!mehrleistungenVorhanden.value.contains(eintrag.id)) {
				await props.addMehrleistung({ idAbschnittsdaten: props.personalabschnittsdaten()?.id ?? -1, idGrund: eintrag.id, anzahl: 1 });
			}
		}
		for (const eintrag of auswahlMinderleistungenNeu.value) {
			if (!minderleistungenVorhanden.value.contains(eintrag.id)) {
				await props.addMinderleistung({ idAbschnittsdaten: props.personalabschnittsdaten()?.id ?? -1, idGrund: eintrag.id, anzahl: 1 });
			}
		}
		for (const eintrag of auswahlAnrechnungenNeu.value) {
			if (!anrechnungenVorhanden.value.contains(eintrag.id)) {
				await props.addAnrechnung({ idAbschnittsdaten: props.personalabschnittsdaten()?.id ?? -1, idGrund: eintrag.id, anzahl: 1 });
			}
		}
		showHinzufuegen.value = false;
	}

</script>
