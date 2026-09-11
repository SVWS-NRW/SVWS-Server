<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden" size="medium" :auto-close="!loading" :close-in-title="!loading">
		<template #modalTitle>{{ importManager === undefined ? 'Kurse importieren – Quelle wählen' : 'Kurse importieren – Vorschau prüfen' }}</template>
		<template #modalContent>
			<div class="grid grid-cols-1 gap-4">
				<p class="text-sm text-ui-secondary">Ziel: {{ state.planungsabschnitt?.beschreibung }}</p>
				<p v-if="importManager !== undefined" class="text-sm text-ui-secondary">Quelle: {{ quelleText }}</p>
				<p v-if="importManager !== undefined && importQuelle === 'blockung' && selectedAbschnitt !== undefined" class="text-sm text-ui-secondary">Schuljahresabschnitt der Kurse: {{ presenter.schuljahresabschnittText(selectedAbschnitt) }}</p>
				<svws-ui-notification v-if="error" type="error">{{ error }}</svws-ui-notification>
				<template v-if="importManager === undefined">
					<svws-ui-radio-group :row="true">
						<svws-ui-radio-option v-model="importQuelle" value="blockung" name="importQuelle" label="GOSt-Blockung" :disabled="loading" />
						<svws-ui-radio-option v-model="importQuelle" value="schuljahresabschnitt" name="importQuelle"
							label="Kurse aus Schuljahresabschnitt" :disabled="loading" />
					</svws-ui-radio-group>
					<template v-if="importQuelle === 'blockung'">
						<output v-if="loadingImportquelle" class="text-sm text-ui-secondary">Importquellen werden geladen …</output>
						<svws-ui-notification v-if="quellenError" type="error">{{ quellenError }}</svws-ui-notification>
						<svws-ui-select v-model="selectedAbiturjahrgang"
							:items="abiturjahrgangItems"
							:item-text="jg => `${jg.bezeichnung} (Abi ${jg.abiturjahr})`"
							title="Abiturjahrgang"
							required :disabled="loading" />
						<svws-ui-select v-model="selectedHalbjahr"
							:items="halbjahrItems"
							:item-text="hj => hj.kuerzel"
							title="Halbjahr"
							required :disabled="loading" />
						<svws-ui-select v-model="selectedBlockung"
							:items="blockungItems"
							:item-text="b => b.name"
							title="Blockung"
							required
							:disabled="loading || loadingBlockungen || blockungItems.length === 0" />
						<svws-ui-select v-model="selectedErgebnis"
							:items="ergebnisItems"
							:item-text="e => `Ergebnis ${e.id}${e.istAktiv ? ' (aktiv)' : ''}`"
							title="Ergebnis"
							required
							:disabled="loading || loadingErgebnisse || ergebnisItems.length === 0" />
						<svws-ui-select v-model="selectedAbschnitt"
							:items="schuljahresabschnittItems"
							:item-text="presenter.schuljahresabschnittText"
							title="Schuljahresabschnitt der anzulegenden Kurse"
							required :disabled="loading" />
					</template>
					<svws-ui-select v-else v-model="selectedQuellabschnitt"
						:items="schuljahresabschnittItems"
						:item-text="presenter.schuljahresabschnittText"
						title="Quell-Schuljahresabschnitt"
						required :disabled="loading" />
				</template>
				<template v-else>
					<p>{{ importDaten?.kurse.size() ?? 0 }} Kurse können importiert werden.</p>
					<svws-ui-table :items="vorschauKurse" :columns="vorschauColumns" scroll count no-data-text="Aus dieser Quelle können keine Kurse übernommen werden." />
					<p class="text-sm text-ui-secondary">Die Vorschau prüft die Verfügbarkeit von Fächern, Lehrkräften und Schülern. Die Daten werden erst mit „Kurse importieren“ übernommen.</p>
					<details v-if="fehlendeFaecher.size() > 0" class="rounded-md border border-ui-caution/40 bg-ui-caution/5">
						<summary class="cursor-pointer px-3 py-2 text-sm text-ui-caution">
							{{ fehlendeFaecher.size() }} Fächer gefunden, die in der UV nicht existieren. Kurse mit diesen Fächern werden übersprungen.
						</summary>
						<div class="border-t border-ui-caution/30 px-3 py-2">
							<div class="flex flex-wrap gap-1.5">
								<span v-for="fach in fehlendeFaecher" :key="fach" class="rounded bg-ui-caution/15 px-2 py-0.5 text-sm text-ui-caution">{{ fach }}</span>
							</div>
						</div>
					</details>
					<details v-if="fehlendeLehrer.size() > 0" class="rounded-md border border-ui-caution/40 bg-ui-caution/5">
						<summary class="cursor-pointer px-3 py-2 text-sm text-ui-caution">
							{{ fehlendeLehrer.size() }} Lehrkräfte sind dem UV-Planungsabschnitt nicht zugeordnet. Diese Lehrkräfte werden nicht zugeordnet.
						</summary>
						<div class="border-t border-ui-caution/30 px-3 py-2">
							<div class="flex flex-wrap gap-1.5">
								<span v-for="lehrer in fehlendeLehrer" :key="lehrer" class="rounded bg-ui-caution/15 px-2 py-0.5 text-sm text-ui-caution">{{ lehrer }}</span>
							</div>
						</div>
					</details>
					<details v-if="fehlendeSchueler.size() > 0" class="rounded-md border border-ui-caution/40 bg-ui-caution/5">
						<summary class="cursor-pointer px-3 py-2 text-sm text-ui-caution">
							{{ fehlendeSchueler.size() }} Schüler existieren nicht im UV-Planungsabschnitt. Diese Schüler werden nicht zugeordnet.
						</summary>
						<div class="border-t border-ui-caution/30 px-3 py-2">
							<div class="flex flex-wrap gap-1.5">
								<span v-for="schueler in fehlendeSchueler" :key="schueler" class="rounded bg-ui-caution/15 px-2 py-0.5 text-sm text-ui-caution">{{ schueler }}</span>
							</div>
						</div>
					</details>
					<p v-if="(fehlendeFaecher.size() === 0) && (fehlendeLehrer.size() === 0) && (fehlendeSchueler.size() === 0)" class="text-sm text-ui-secondary">
						Alle benötigten Fächer, Lehrkräfte und Schüler sind vorhanden.
					</p>
				</template>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false" :disabled="loading">Abbrechen</svws-ui-button>
			<svws-ui-button v-if="importManager !== undefined" type="secondary" @click="zurueckZurQuelle" :disabled="loading">Zurück zur Quelle</svws-ui-button>
			<svws-ui-button v-if="importManager === undefined" type="primary" @click="createVorschau" :disabled="!canImport || loading" :is-loading="loading">Vorschau prüfen</svws-ui-button>
			<svws-ui-button v-else type="primary" @click="doImport" :disabled="loading || !hatKompetenzAendern || importFehlgeschlagen || !importDaten?.kurse.size()" :is-loading="loading">Kurse importieren</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, watch } from "vue";

	import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
	import type { GostBlockungListeneintrag } from "@core/core/data/gost/GostBlockungListeneintrag";
	import type { GostBlockungsergebnis } from "@core/core/data/gost/GostBlockungsergebnis";
	import type { GostJahrgang } from "@core/core/data/gost/GostJahrgang";
	import type { UvKursImportDaten } from "@core/core/data/uv/UvKursImportDaten";
	import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
	import { UvKursImportManager } from "@core/core/utils/uv/UvKursImportManager";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useUvState } from "@ui/states/UvState";
	import type { DataTableColumn } from "@ui/types";

	import { useUvPresenter } from "../UvPresenter";

	const emit = defineEmits<{ imported: [anzahl: number] }>();
	const importFehlgeschlagen = ref(false);
	const show = ref(false);
	const loading = ref(false);
	const error = ref('');
	const quellenError = ref('');
	const loadingQuellen = ref(false);
	const loadingBlockungen = ref(false);
	const loadingErgebnisse = ref(false);
	const loadingImportquelle = computed(() => loadingQuellen.value || loadingBlockungen.value || loadingErgebnisse.value);
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const importDaten = shallowRef<UvKursImportDaten>();
	const previewPlanungsabschnittId = ref<number>();
	const vorschauColumns: DataTableColumn[] = [
		{ key: 'bezeichnung', label: 'Kurs', sortable: true },
		{ key: 'abschnitt', label: 'Schuljahresabschnitt', sortable: true },
	];
	const vorschauKurse = computed(() => [...(importDaten.value?.kurse ?? [])].map(kurs => ({
		bezeichnung: presenter.kursBezeichnung(kurs, [...(importDaten.value?.schuelergruppen ?? [])].find(gruppe => gruppe.id === kurs.idSchuelergruppe)),
		abschnitt: schuljahresabschnittItems.value.find(abschnitt => abschnitt.id === kurs.idSchuljahresabschnitt),
	})).map(row => ({ ...row, abschnitt: row.abschnitt === undefined ? 'Nicht verfügbar' : presenter.schuljahresabschnittText(row.abschnitt) })));
	const abschnittState = useAbschnittState();
	const state = useUvState();
	const presenter = useUvPresenter(state.uvManager);
	type ImportQuelle = 'blockung' | 'schuljahresabschnitt';
	const importQuelle = ref<ImportQuelle>('blockung');

	// Gost-Blockung
	const abiturjahrgangItems = ref<GostJahrgang[]>([]);
	const selectedAbiturjahrgang = ref<GostJahrgang | undefined>(undefined);
	const selectedHalbjahr = ref<GostHalbjahr | undefined>(undefined);
	const selectedBlockung = ref<GostBlockungListeneintrag | undefined>(undefined);
	const selectedErgebnis = ref<GostBlockungsergebnis | undefined>(undefined);

	const blockungItems = ref<GostBlockungListeneintrag[]>([]);
	const ergebnisItems = ref<GostBlockungsergebnis[]>([]);

	// Gemeinsam
	const selectedAbschnitt = ref<Schuljahresabschnitt | undefined>(undefined);
	const selectedQuellabschnitt = ref<Schuljahresabschnitt | undefined>(undefined);
	const importManager = ref<UvKursImportManager | undefined>(undefined);
	const fehlendeFaecher = computed(() => importManager.value?.getFehlendeFaecher() ?? new ArrayList<string>());
	const fehlendeLehrer = computed(() => importManager.value?.getFehlendeLehrer() ?? new ArrayList<string>());
	const fehlendeSchueler = computed(() => importManager.value?.getFehlendeSchueler() ?? new ArrayList<string>());

	const schuljahresabschnittItems = computed(() => [...abschnittState.alle]);

	const halbjahrItems = GostHalbjahr.values();

	const quelleText = computed(() => {
		if (importQuelle.value === 'schuljahresabschnitt') {
			return selectedQuellabschnitt.value === undefined ? '—' : presenter.schuljahresabschnittText(selectedQuellabschnitt.value);
		}
		return `${selectedAbiturjahrgang.value?.bezeichnung ?? ''} · ${selectedHalbjahr.value?.kuerzel ?? ''} · ${selectedBlockung.value?.name ?? ''} · Ergebnis ${selectedErgebnis.value?.id ?? ''}`;
	});

	const canImport = computed(() => {
		if (!hatKompetenzAendern.value || state.planungsabschnitt === null || state.planungsabschnitt.id === -1) {
			return false;
		}
		if (importQuelle.value === 'schuljahresabschnitt') {
			return selectedQuellabschnitt.value !== undefined;
		}
		return !loadingImportquelle.value
			&& selectedBlockung.value !== undefined && selectedErgebnis.value !== undefined && selectedAbschnitt.value !== undefined;
	});

	const openModal = () => {
		if (!hatKompetenzAendern.value || loading.value) {
			return;
		}
		error.value = '';
		importFehlgeschlagen.value = false;
		quellenError.value = '';
		importDaten.value = undefined;
		importQuelle.value = 'blockung';
		abiturjahrgangItems.value = [];
		selectedAbiturjahrgang.value = undefined;
		selectedHalbjahr.value = undefined;
		selectedBlockung.value = undefined;
		selectedErgebnis.value = undefined;
		selectedAbschnitt.value = abschnittState.auswahl;
		selectedQuellabschnitt.value = abschnittState.auswahl;
		importManager.value = undefined;
		blockungItems.value = [];
		ergebnisItems.value = [];
		show.value = true;
	};

	watch(show, async (isOpen, _oldValue, onCleanup) => {
		const cancelled = ref(false);
		onCleanup(() => {
			cancelled.value = true;
		});
		if (!isOpen) {
			return;
		}
		loadingQuellen.value = true;
		try {
			const items = await state.getGostAbiturjahrgaenge();
			if (!cancelled.value) {
				abiturjahrgangItems.value = items;
			}
		} catch {
			if (!cancelled.value) {
				abiturjahrgangItems.value = [];
				quellenError.value = 'Die Abiturjahrgänge konnten nicht geladen werden. Den Import erneut öffnen oder Kurse aus einem Schuljahresabschnitt wählen.';
			}
		} finally {
			if (!cancelled.value) {
				loadingQuellen.value = false;
			}
		}
	});

	watch(() => state.planungsabschnitt?.id, () => {
		show.value = false;
		importManager.value = undefined;
		importDaten.value = undefined;
	});

	// Wenn Abiturjahrgang sich ändert: Halbjahr zurücksetzen
	watch(selectedAbiturjahrgang, () => {
		selectedHalbjahr.value = undefined;
		selectedBlockung.value = undefined;
		blockungItems.value = [];
		selectedErgebnis.value = undefined;
		ergebnisItems.value = [];
	});

	// Veraltete Antworten dürfen neuere Auswahlen nicht überschreiben.
	watch(selectedHalbjahr, async (halbjahr, _oldValue, onCleanup) => {
		const cancelled = ref(false);
		onCleanup(() => {
			cancelled.value = true;
		});
		blockungItems.value = [];
		selectedBlockung.value = undefined;
		ergebnisItems.value = [];
		selectedErgebnis.value = undefined;
		loadingBlockungen.value = false;
		const jahrgang = selectedAbiturjahrgang.value;
		if (jahrgang === undefined || halbjahr === undefined) {
			return;
		}
		loadingBlockungen.value = true;
		quellenError.value = '';
		try {
			const items = await state.getGostBlockungen(jahrgang.abiturjahr, halbjahr.id);
			if (!cancelled.value) {
				blockungItems.value = items;
			}
		} catch {
			if (!cancelled.value) {
				quellenError.value = 'Die Blockungen konnten nicht geladen werden. Bitte das Halbjahr erneut auswählen.';
			}
		} finally {
			if (!cancelled.value) {
				loadingBlockungen.value = false;
			}
		}
	});

	watch(selectedBlockung, async (blockung, _oldValue, onCleanup) => {
		const cancelled = ref(false);
		onCleanup(() => {
			cancelled.value = true;
		});
		ergebnisItems.value = [];
		selectedErgebnis.value = undefined;
		loadingErgebnisse.value = false;
		if (blockung === undefined) {
			return;
		}
		loadingErgebnisse.value = true;
		quellenError.value = '';
		try {
			const items = await state.getGostBlockungsergebnisse(blockung.id);
			if (!cancelled.value) {
				ergebnisItems.value = items;
			}
		} catch {
			if (!cancelled.value) {
				quellenError.value = 'Die Blockungsergebnisse konnten nicht geladen werden. Bitte die Blockung erneut auswählen.';
			}
		} finally {
			if (!cancelled.value) {
				loadingErgebnisse.value = false;
			}
		}
	});

	function zurueckZurQuelle() {
		importFehlgeschlagen.value = false;
		importManager.value = undefined;
		importDaten.value = undefined;
		error.value = '';
	}

	function istVorschauAktuell(id: number): boolean {
		return show.value && state.planungsabschnitt?.id === id;
	}

	async function createVorschau() {
		if (!show.value || !canImport.value || loading.value) {
			return;
		}
		loading.value = true;
		error.value = '';
		try {
			const planungsabschnitt = state.planungsabschnitt;
			if (planungsabschnitt === null || planungsabschnitt.id === -1) {
				return;
			}
			if (importQuelle.value === 'schuljahresabschnitt') {
				const quellabschnitt = selectedQuellabschnitt.value;
				if (quellabschnitt === undefined) {
					return;
				}
				const kurse = await state.getKurseFuerSchuljahresabschnitt(quellabschnitt.id);
				if (!istVorschauAktuell(planungsabschnitt.id)) {
					return;
				}
				importManager.value = new UvKursImportManager(kurse, planungsabschnitt, state.uvManager);
				importDaten.value = importManager.value.createImportDaten();
				previewPlanungsabschnittId.value = planungsabschnitt.id;
				return;
			}
			const blockungEintrag = selectedBlockung.value;
			const ergebnis = selectedErgebnis.value;
			const abschnitt = selectedAbschnitt.value;
			if (blockungEintrag === undefined || ergebnis === undefined || abschnitt === undefined) {
				return;
			}
			const blockung = await state.getGostBlockung(blockungEintrag.id);
			if (!istVorschauAktuell(planungsabschnitt.id)) {
				return;
			}
			importManager.value = new UvKursImportManager(blockung, ergebnis, planungsabschnitt, state.uvManager, abschnitt.id);
			importDaten.value = importManager.value.createImportDaten();
			previewPlanungsabschnittId.value = planungsabschnitt.id;
		} catch {
			importManager.value = undefined;
			importDaten.value = undefined;
			error.value = 'Die Vorschau konnte nicht erstellt werden. Bitte die Quelle prüfen und erneut versuchen.';
		} finally {
			loading.value = false;
		}
	}

	async function doImport() {
		if (!show.value || loading.value || importFehlgeschlagen.value || !hatKompetenzAendern.value || importDaten.value === undefined || importDaten.value.kurse.isEmpty()
			|| state.planungsabschnitt?.id !== previewPlanungsabschnittId.value) {
			return;
		}
		loading.value = true;
		error.value = '';
		try {
			const anzahl = importDaten.value.kurse.size();
			const planungsabschnittId = previewPlanungsabschnittId.value;
			await state.importKursdaten(importDaten.value);
			show.value = false;
			importDaten.value = undefined;
			importManager.value = undefined;
			if (state.planungsabschnitt?.id === planungsabschnittId) {
				emit('imported', anzahl);
			}
		} catch {
			importFehlgeschlagen.value = true;
			error.value = 'Der Import konnte nicht abgeschlossen werden. Den Kursbestand prüfen, bevor der Import erneut gestartet wird.';
		} finally {
			loading.value = false;
		}
	}

</script>
