<template>
	<ui-table-grid v-if="gridManager.daten.size() !== 0" name="Übersicht zu Anrechnungsstunden, Mehr- und Minderleistungsgründe" :manager="() => gridManager">
		<template #header>
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
				{{ row.data.proxy.anzahl }}
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
	<div v-else>
		<svws-ui-button v-if="hatUpdateKompetenz" @click="openHinzufuegen" type="secondary">Anrechnungs-, Mehr- oder Minderleistungsgründe hinzufügen</svws-ui-button>
		<div v-else>Keine Anrechnungs-, Mehr- oder Minderleistungsgründe zugeordnet.</div>
	</div>
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

	import type { ComponentPublicInstance } from "vue";
	import { computed, ref, shallowRef } from "vue";
	import { GridManager, CoreTypeSelectManager, useSchuleState, useAbschnittState } from "@ui";
	import type { LehrerPersonalabschnittsdatenAnrechnungsstunden, List, JavaSet, LehrerAnrechnungsgrundKatalogEintrag,
		LehrerMehrleistungsartKatalogEintrag, LehrerMinderleistungsartKatalogEintrag, Comparator } from "@core";
	import { LehrerMehrleistungsarten, LehrerMinderleistungsarten, LehrerAnrechnungsgrund, ArrayList, HashSet } from "@core";
	import { LehrerPersonalabschnittsdatenAnrechnungsstundenModelProxy } from "./modelproxy/LehrerPersonalabschnittsdatenAnrechnungsstundenModelProxy";
	import type { LehrerPersonalabschnittsdatenModelProxy } from "./modelproxy/LehrerPersonalabschnittsdatenModelProxy";

	const props = defineProps<{
		hatUpdateKompetenz: boolean;
		personalabschnittsdatenModelProxy: () => LehrerPersonalabschnittsdatenModelProxy,
		addMehrleistung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => Promise<void>;
		patchMehrleistung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number) => Promise<void>;
		removeMehrleistung: (data: LehrerPersonalabschnittsdatenAnrechnungsstunden) => Promise<void>;
		addMinderleistung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => Promise<void>;
		patchMinderleistung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number) => Promise<void>;
		removeMinderleistung: (data: LehrerPersonalabschnittsdatenAnrechnungsstunden) => Promise<void>;
		addAnrechnung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => Promise<void>;
		patchAnrechnungen: (data: List<Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>>) => Promise<void>;
		removeAnrechnung: (data: LehrerPersonalabschnittsdatenAnrechnungsstunden) => Promise<void>;
	}>();
	const schuleState = useSchuleState();
	const abschnittState = useAbschnittState();

	type Eintrag = { typ: 'mehrleistung' | 'minderleistung' | 'anrechnung', data: LehrerPersonalabschnittsdatenAnrechnungsstundenModelProxy };

	const comparatorEintrag: Comparator<Eintrag> = {
		compare: (a: Eintrag, b: Eintrag): number => {
			if ((a.data.proxy.idGrund === null) && (b.data.proxy.idGrund !== null)) {
				return -1;
			}
			if ((a.data.proxy.idGrund !== null) && (b.data.proxy.idGrund === null)) {
				return 1;
			}
			if ((a.data.proxy.idGrund !== null) && (b.data.proxy.idGrund !== null)) {
				if (a.data.proxy.idGrund < b.data.proxy.idGrund) {
					return -1;
				}
				if (a.data.proxy.idGrund > b.data.proxy.idGrund) {
					return 1;
				}
			}
			if (a.typ > b.typ) {
				return -1;
			}
			if (a.typ < b.typ) {
				return 1;
			}
			if (a.data.proxy.id < b.data.proxy.id) {
				return -1;
			}
			if (a.data.proxy.id > b.data.proxy.id) {
				return 1;
			}
			return 0;
		},
	};

	const gridManager = new GridManager<string, Eintrag, List<Eintrag>>({
		daten: computed<List<Eintrag>>(() => {
			const result = new ArrayList<Eintrag>();
			const abschnittsdaten = props.personalabschnittsdatenModelProxy().data;
			// Füge Mehrleistungen, Minderleistung und Anrechnungen hinzu
			for (const data of abschnittsdaten.mehrleistung) {
				const patchMethod = async (proxy: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => {
					await props.patchMehrleistung(proxy, data.id);
					return true;
				};
				const modelProxy = new LehrerPersonalabschnittsdatenAnrechnungsstundenModelProxy(() => data, patchMethod);
				result.add({ typ: 'mehrleistung', data: modelProxy });
			}
			for (const data of abschnittsdaten.minderleistung) {
				const patchMethod = async (proxy: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => {
					await props.patchMinderleistung(proxy, data.id);
					return true;
				};
				const modelProxy = new LehrerPersonalabschnittsdatenAnrechnungsstundenModelProxy(() => data, patchMethod);
				result.add({ typ: 'minderleistung', data: modelProxy });
			}
			for (const data of abschnittsdaten.anrechnungen) {
				const patchMethod = async (proxy: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => {
					await props.patchAnrechnungen(ArrayList.of({ ...proxy, id: data.id }));
					return true;
				};
				const modelProxy = new LehrerPersonalabschnittsdatenAnrechnungsstundenModelProxy(() => data, patchMethod);
				result.add({ typ: 'anrechnung', data: modelProxy });
			}
			result.sort(comparatorEintrag);
			return result;
		}),
		getRowKey: row => `${row.typ}-${row.data.proxy.idGrund}-${row.data.proxy.id}`,
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
		row.data.proxy.anzahl = anzahl;
		void row.data.patch();
	}

	function inputAnzahl(row: Eintrag, index: number) {
		const key = `${row.typ}-${row.data.proxy.idGrund}-${row.data.proxy.id}`;
		const setter = (value: number | null) => updateAnzahl(row, value);
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputNumberFixed(key, 4, index, element, 100, 2, setter);
			if (input !== null) {
				gridManager.update(key, row.data.proxy.anzahl);
			}
		};
	}

	async function removeDaten(row: Eintrag): Promise<void> {
		if (row.typ === 'mehrleistung') {
			await props.removeMehrleistung(row.data.data);
		} else if (row.typ === 'minderleistung') {
			await props.removeMinderleistung(row.data.data);
		} else {
			await props.removeAnrechnung(row.data.data);
		}
	}

	function getGrundText(row: Eintrag): string {
		let data = null;
		if (row.data.proxy.idGrund !== null) {
			if (row.typ === 'mehrleistung') {
				data = LehrerMehrleistungsarten.data().getEintragByID(row.data.proxy.idGrund);
			} else if (row.typ === 'minderleistung') {
				data = LehrerMinderleistungsarten.data().getEintragByID(row.data.proxy.idGrund);
			} else {
				data = LehrerAnrechnungsgrund.data().getEintragByID(row.data.proxy.idGrund);
			}
		}
		return (data === null) ? "???" : data.kuerzel + " - " + data.text;
	}

	const showHinzufuegen = ref<boolean>(false);

	const auswahlMehrleistungenNeu = shallowRef<Array<LehrerMehrleistungsartKatalogEintrag>>([]);
	const auswahlMinderleistungenNeu = shallowRef<Array<LehrerMinderleistungsartKatalogEintrag>>([]);
	const auswahlAnrechnungenNeu = shallowRef<Array<LehrerAnrechnungsgrundKatalogEintrag>>([]);

	const mehrleistungenSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerMehrleistungsarten.class, schuljahr: abschnittState.auswahl.schuljahr, schulformen: schuleState.schulform,
		filters: [{ key: 'vorhandene', apply: filterMehrleistungen }],
		selectionDisplayText: 'text', optionDisplayText: 'kuerzelText',
	}));

	const minderleistungenSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerMinderleistungsarten.class, schuljahr: abschnittState.auswahl.schuljahr, schulformen: schuleState.schulform,
		filters: [{ key: 'vorhandene', apply: filterMinderleistungen }],
		selectionDisplayText: 'text', optionDisplayText: 'kuerzelText',
	}));

	const anrechnungenSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerAnrechnungsgrund.class, schuljahr: abschnittState.auswahl.schuljahr, schulformen: schuleState.schulform,
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
		const abschnittsdaten = props.personalabschnittsdatenModelProxy().proxy;
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
		const abschnittsdaten = props.personalabschnittsdatenModelProxy().proxy;
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
		const abschnittsdaten = props.personalabschnittsdatenModelProxy().proxy;
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
		const data: Partial <LehrerPersonalabschnittsdatenAnrechnungsstunden> = {
			idAbschnittsdaten: props.personalabschnittsdatenModelProxy().proxy.id,
			anzahl: 1,
		};
		for (const eintrag of auswahlMehrleistungenNeu.value) {
			data.idGrund = eintrag.id;
			if (!mehrleistungenVorhanden.value.contains(eintrag.id)) {
				await props.addMehrleistung(data);
			}
		}
		for (const eintrag of auswahlMinderleistungenNeu.value) {
			data.idGrund = eintrag.id;
			if (!minderleistungenVorhanden.value.contains(eintrag.id)) {
				await props.addMinderleistung(data);
			}
		}
		for (const eintrag of auswahlAnrechnungenNeu.value) {
			data.idGrund = eintrag.id;
			if (!anrechnungenVorhanden.value.contains(eintrag.id)) {
				await props.addAnrechnung(data);
			}
		}
		showHinzufuegen.value = false;
		props.personalabschnittsdatenModelProxy().validate();
	}

</script>
