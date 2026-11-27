<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden" size="medium" :auto-close="!loading" :close-in-title="!loading">
		<template #modalTitle>{{ importManager === undefined ? 'Kursunterricht importieren – Quelle wählen' : 'Kursunterricht importieren – Vorschau prüfen' }}</template>
		<template #modalContent>
			<div class="grid grid-cols-1 gap-4">
				<p class="text-sm text-ui-secondary">Ziel: {{ state.planungsabschnitt?.beschreibung }}</p>
				<p v-if="importManager !== undefined" class="text-sm text-ui-secondary">Quelle: {{ quelleText }}</p>
				<p v-if="importManager !== undefined && importQuelle === 'blockung' && gostQuelle !== undefined" class="text-sm text-ui-secondary">Schuljahresabschnitt: {{ presenter.schuljahresabschnittText(gostQuelle.abschnitt) }}</p>
				<svws-ui-notification v-if="error" type="error">{{ error }}</svws-ui-notification>
				<template v-if="importManager === undefined">
					<svws-ui-radio-group :row="true">
						<svws-ui-radio-option v-model="importQuelle" value="blockung" name="importQuelle" label="GOSt-Blockung" :disabled="loading" />
						<svws-ui-radio-option v-model="importQuelle" value="schuljahresabschnitt" name="importQuelle"
							label="Kursunterricht aus Schuljahresabschnitt" :disabled="loading" />
					</svws-ui-radio-group>
					<s-uv-gost-blockung-import-auswahl v-if="show"
						v-show="importQuelle === 'blockung'"
						v-model="gostQuelle"
						:disabled="loading" />
					<svws-ui-select v-if="importQuelle === 'schuljahresabschnitt'" v-model="selectedQuellabschnitt"
						:items="schuljahresabschnittItems"
						:item-text="presenter.schuljahresabschnittText"
						title="Quell-Schuljahresabschnitt"
						required :disabled="loading" />
				</template>
				<template v-else>
					<p>{{ importDaten?.kurse.size() ?? 0 }} Lerngruppen mit Kursunterricht können importiert werden.</p>
					<div class="max-h-[50vh] overflow-y-auto">
						<svws-ui-table :items="vorschauKurse"
							:columns="vorschauColumns"
							scroll
							count
							no-data-text="Aus dieser Quelle können keine Lerngruppen übernommen werden." />
					</div>
					<p class="text-sm text-ui-secondary">Die Vorschau prüft die Verfügbarkeit von Fächern, Lehrkräften und Schülern. Die Daten werden erst mit „Lerngruppen importieren“ übernommen.</p>
					<details v-if="fehlendeFaecher.size() > 0" class="rounded-md border border-ui-caution/40 bg-ui-caution/5">
						<summary class="cursor-pointer px-3 py-2 text-sm text-ui-caution">
							{{ fehlendeFaecher.size() }} Fächer gefunden, die in der UV nicht existieren. Lerngruppen mit diesen Fächern werden übersprungen.
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
								<span v-for="schueler in fehlendeSchueler"
									:key="schueler"
									class="rounded bg-ui-caution/15 px-2 py-0.5 text-sm text-ui-caution">{{ schueler }}</span>
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
			<svws-ui-button v-if="importManager === undefined"
				type="primary"
				@click="createVorschau"
				:disabled="!canImport || loading"
				:is-loading="loading">
				Vorschau prüfen
			</svws-ui-button>
			<svws-ui-button v-else
				type="primary"
				@click="doImport"
				:disabled="loading || !hatKompetenzAendern || importFehlgeschlagen || !importDaten?.kurse.size()"
				:is-loading="loading">
				Lerngruppen importieren
			</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, watch } from "vue";

	import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
	import type { UvKursImportDaten } from "@core/core/data/uv/UvKursImportDaten";
	import { UvKursImportManager } from "@core/core/utils/uv/UvKursImportManager";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useUvState } from "@ui/states/UvState";
	import type { DataTableColumn } from "@ui/types";

	import { createUvPresenter } from "../UvPresenter";

	import SUvGostBlockungImportAuswahl, { type UvGostBlockungImportQuelle } from "./SUvGostBlockungImportAuswahl.vue";
	import { UvPlanungsManager } from "./UvPlanungsManager";

	const emit = defineEmits<{ imported: [anzahl: number] }>();
	const importFehlgeschlagen = ref(false);
	const show = ref(false);
	const loading = ref(false);
	const error = ref('');
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const importDaten = shallowRef<UvKursImportDaten>();
	const previewPlanungsabschnittId = ref<number>();
	const vorschauColumns: DataTableColumn[] = [
		{ key: 'bezeichnung', label: 'Kurs', sortable: true },
		{ key: 'abschnitt', label: 'Schuljahresabschnitt', sortable: true },
	];
	const vorschauKurse = computed(() => (importDaten.value === undefined ? [] : UvPlanungsManager.kursImportVorschau(state.uvManager, importDaten.value)).map(({ kurs, bezeichnung }) => ({
		bezeichnung,
		abschnitt: schuljahresabschnittItems.value.find(abschnitt => abschnitt.id === kurs.idSchuljahresabschnitt),
	})).map(row => ({ ...row, abschnitt: row.abschnitt === undefined ? 'Nicht verfügbar' : presenter.value.schuljahresabschnittText(row.abschnitt) })));
	const abschnittState = useAbschnittState();
	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));
	type ImportQuelle = 'blockung' | 'schuljahresabschnitt';
	const importQuelle = ref<ImportQuelle>('blockung');

	const gostQuelle = shallowRef<UvGostBlockungImportQuelle>();
	const selectedQuellabschnitt = ref<Schuljahresabschnitt | undefined>(undefined);
	const importManager = ref<UvKursImportManager | undefined>(undefined);
	const fehlendeFaecher = computed(() => importManager.value?.getFehlendeFaecher() ?? new ArrayList<string>());
	const fehlendeLehrer = computed(() => importManager.value?.getFehlendeLehrer() ?? new ArrayList<string>());
	const fehlendeSchueler = computed(() => importManager.value?.getFehlendeSchueler() ?? new ArrayList<string>());

	const schuljahresabschnittItems = computed(() => [...abschnittState.alle]);

	const quelleText = computed(() => {
		if (importQuelle.value === 'schuljahresabschnitt') {
			return selectedQuellabschnitt.value === undefined ? '—' : presenter.value.schuljahresabschnittText(selectedQuellabschnitt.value);
		}
		return gostQuelle.value?.text ?? '—';
	});

	const canImport = computed(() => {
		if (!hatKompetenzAendern.value || state.planungsabschnitt === null || state.planungsabschnitt.id === -1) {
			return false;
		}
		if (importQuelle.value === 'schuljahresabschnitt') {
			return selectedQuellabschnitt.value !== undefined;
		}
		return gostQuelle.value !== undefined;
	});

	const openModal = () => {
		if (!hatKompetenzAendern.value || loading.value) {
			return;
		}
		error.value = '';
		importFehlgeschlagen.value = false;
		importDaten.value = undefined;
		importQuelle.value = 'blockung';
		gostQuelle.value = undefined;
		selectedQuellabschnitt.value = abschnittState.auswahl;
		importManager.value = undefined;
		show.value = true;
	};

	watch(() => state.planungsabschnitt?.id, () => {
		show.value = false;
		importManager.value = undefined;
		importDaten.value = undefined;
	});

	function istVorschauAktuell(id: number): boolean {
		return show.value && state.planungsabschnitt?.id === id;
	}

	/** Bereitet beide Importquellen mit demselben Importmanager für die Vorschau auf. */
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
				const kurse = await state.planung.getKurseFuerSchuljahresabschnitt(quellabschnitt.id);
				if (!istVorschauAktuell(planungsabschnitt.id)) {
					return;
				}
				importManager.value = new UvKursImportManager(kurse, planungsabschnitt, state.uvManager);
				importDaten.value = importManager.value.createImportDaten();
				previewPlanungsabschnittId.value = planungsabschnitt.id;
				return;
			}
			const quelle = gostQuelle.value;
			if (quelle === undefined) {
				return;
			}
			const blockung = await state.planung.getGostBlockung(quelle.idBlockung);
			if (!istVorschauAktuell(planungsabschnitt.id)) {
				return;
			}
			importManager.value = new UvKursImportManager(blockung, quelle.ergebnis, planungsabschnitt, state.uvManager, quelle.abschnitt.id);
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

	/** Übernimmt die bestätigte Vorschau, solange ihr Zielabschnitt noch ausgewählt ist. */
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
			await state.planung.importKursdaten(importDaten.value);
			show.value = false;
			importDaten.value = undefined;
			importManager.value = undefined;
			if (state.planungsabschnitt?.id === planungsabschnittId) {
				emit('imported', anzahl);
			}
		} catch {
			importFehlgeschlagen.value = true;
			error.value = 'Der Import konnte nicht abgeschlossen werden. Die Lerngruppen prüfen, bevor der Import erneut gestartet wird.';
		} finally {
			loading.value = false;
		}
	}

</script>
