<template>
	<section class="flex min-h-0 flex-1 flex-col bg-ui">
		<div class="flex shrink-0 items-center justify-between gap-2 border-b border-ui-10 p-3">
			<h3 class="font-semibold">Lerngruppe erstellen</h3>
			<svws-ui-button type="secondary" :disabled="loading" @click="props.abbrechen()">Abbrechen</svws-ui-button>
		</div>
		<div class="flex min-h-0 flex-1 flex-col gap-3 overflow-auto p-3">
			<svws-ui-content-card title="Daten der Lerngruppe">
				<p class="mb-3 text-sm text-ui-secondary">Planungsabschnitt „{{ state.planungsabschnitt?.beschreibung }}“</p>
				<svws-ui-radio-group :row="true" class="mb-3">
					<svws-ui-radio-option v-model="unterrichtsart"
						value="klasse"
						name="unterrichtsart"
						label="Klassenunterricht"
						:disabled="loading || schuelerBearbeitung" />
					<svws-ui-radio-option v-model="unterrichtsart"
						value="kurs"
						name="unterrichtsart"
						label="Kursunterricht"
						:disabled="loading || schuelerBearbeitung" />
				</svws-ui-radio-group>
				<p class="mb-3 text-sm text-ui-secondary">Die Unterrichtsart kann nach dem Erstellen nicht geändert werden.</p>
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-select v-if="unterrichtsart === 'klasse'"
						v-model="selectedKlasse"
						:disabled="loading"
						required
						:items="klassenItems"
						:item-text="item => item.kuerzel"
						title="Klasse"
						autocomplete />
					<svws-ui-select title="Fach"
						:model-value="selectedFach"
						:items="fachItems"
						:item-text="presenter.fachText"
						@update:model-value="fach => fach !== null && fach !== undefined && (item.idFach = fach.id)"
						:disabled="loading || !hatKompetenzAendern"
						autocomplete
						required />
					<svws-ui-select v-if="unterrichtsart === 'kurs'"
						v-model="selectedSchuljahresabschnitt"
						:disabled="loading"
						required
						:items="schuljahresabschnittItems"
						:item-text="presenter.schuljahresabschnittText"
						class="col-span-full"
						title="Schuljahresabschnitt" />
					<svws-ui-select v-if="unterrichtsart === 'kurs'"
						title="Kursart"
						:model-value="item.kursart"
						:items="[...kursarten.keys()]"
						:item-text="art => `${art} (${kursarten.get(art) ?? 'Nicht verfügbar'})`"
						@update:model-value="kursart => kursart !== null && kursart !== undefined && (item.kursart = kursart)"
						:disabled="loading || !hatKompetenzAendern"
						autocomplete
						required />
					<svws-ui-input-number v-if="unterrichtsart === 'kurs'"
						placeholder="Kursnummer"
						:model-value="item.kursnummer"
						@change="value => item.kursnummer = value ?? 0"
						:valid="UvPlanungsManager.kursnummerGueltig"
						:min="1"
						:step="1"
						:disabled="loading || !hatKompetenzAendern"
						required />
					<svws-ui-select v-if="unterrichtsart === 'kurs'"
						v-model="selectedSchuelergruppe"
						:disabled="loading || schuelerBearbeitung"
						:items="schuelergruppenItems"
						:item-text="sg => sg.bezeichnung"
						class="col-span-full"
						title="Schülergruppe" />
					<svws-ui-multi-select v-if="unterrichtsart === 'kurs' && selectedSchuelergruppe.id === null"
						v-model="selectedJahrgaenge"
						:disabled="loading"
						:items="jahrgangItems"
						:item-text="jahrgang => jahrgang.kuerzel ?? ''"
						class="col-span-full"
						title="Jahrgänge (für neue Schülergruppe)" />
					<svws-ui-input-number placeholder="Wochenstunden"
						:model-value="wochenstunden"
						@change="value => wochenstunden = value ?? -1"
						:min="0"
						:steps="0.5"
						:decimal-places="2"
						:disabled="loading"
						required />
					<svws-ui-input-number placeholder="Wochenstunden unterrichtet"
						:model-value="wochenstundenUnterrichtet"
						@change="value => wochenstundenUnterrichtet = value ?? -1"
						:min="0"
						:steps="0.5"
						:decimal-places="2"
						:disabled="loading"
						required />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card v-if="unterrichtsart === 'kurs'" title="Schüler der Lerngruppe">
				<template #actions v-if="hatKompetenzAendern"><svws-ui-button v-if="!schuelerBearbeitung" type="secondary" :disabled="loading" @click="props.schuelerBearbeiten()">Schüler bearbeiten</svws-ui-button></template>
				<p v-if="selectedSchuelergruppe.id !== null" class="mb-2 text-xs text-ui-secondary">Vorhandene Schüler der gewählten Schülergruppe bleiben erhalten. Neu hinzugefügte Schüler können bis zum Speichern wieder entfernt werden.</p>
				<s-uv-planungs-schueler-tabelle :selected="[]"
					:items="schuelerMitglieder"
					:selectable="false"
					:navigation-disabled="true"
					:removable="schuelerBearbeitung"
					:draggable="schuelerBearbeitung"
					:removable-ids="hinzugefuegteIds"
					:disabled="loading"
					:entfernen="props.schuelerEntfernen"
					@drag-start="dragStartEntfernen">
					<template #no-data>Noch keine Schüler zugeordnet.</template>
				</s-uv-planungs-schueler-tabelle>
			</svws-ui-content-card>
			<ul v-if="validationErrors.length > 0" class="text-sm text-ui-secondary" aria-live="polite">
				<li v-for="message in validationErrors" :key="message">{{ message }}</li>
			</ul>
			<svws-ui-notification v-if="error" type="error">{{ error }}</svws-ui-notification>
		</div>
		<div class="flex shrink-0 justify-end border-t border-ui-10 p-3">
			<svws-ui-button type="primary"
				:disabled="!isValid || loading || schuelerBearbeitung"
				:is-loading="loading"
				@click="create">
				Lerngruppe erstellen
			</svws-ui-button>
		</div>
	</section>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { UvKlasse } from '@core/core/data/uv/UvKlasse';
	import { UvKurs } from "@core/core/data/uv/UvKurs";
	import type { UvPlanungsabschnittSchueler } from '@core/core/data/uv/UvPlanungsabschnittSchueler';
	import type { UvSchuelergruppe } from "@core/core/data/uv/UvSchuelergruppe";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { useUvState } from "@ui/states/UvState";

	import { createUvPresenter } from "../UvPresenter";
	import { getKursarten } from "~/utils/helfer";

	import SUvPlanungsSchuelerTabelle from './SUvPlanungsSchuelerTabelle.vue';
	import { uvDragDatenSchreiben } from './UvDragDaten';
	import { UvPlanungsManager } from "./UvPlanungsManager";

	const props = defineProps<{
		selectedSchueler: UvPlanungsabschnittSchueler[];
		schuelerMitglieder: UvPlanungsabschnittSchueler[];
		schuelerBearbeitung: boolean;
		abbrechen: () => void;
		schuelerBearbeiten: () => void;
		schuelerEntfernen: (id: number) => void;
	}>();
	const emit = defineEmits<{
		created: [id: number];
		'schuelergruppe-change': [id: number | null];
	}>();
	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const error = ref("");
	const abschnittState = useAbschnittState();
	const schuleState = useSchuleState();
	const item = ref<UvKurs>(new UvKurs());
	const unterrichtsart = ref<'klasse' | 'kurs'>('klasse');
	const selectedKlasse = ref<UvKlasse | undefined>();
	const wochenstunden = ref(2);
	const wochenstundenUnterrichtet = ref(2);
	const selectedSchuelergruppe = ref<UvSchuelergruppe | { id: null; bezeichnung: string }>({ id: null, bezeichnung: "Neue Schülergruppe anlegen" });
	const selectedSchuljahresabschnitt = ref<Schuljahresabschnitt | undefined>(undefined);
	const selectedJahrgaenge = ref<JahrgangsDaten[]>([]);

	const schuelergruppenItems = computed(() => {
		const list: Array<UvSchuelergruppe | { id: null; bezeichnung: string }> = [{ id: null, bezeichnung: "Neue Schülergruppe anlegen" }];
		if (state.planungsabschnitt !== null) {
			list.push(...state.uvManager.schuelergruppeGetMengeByPlanungsabschnitt(state.planungsabschnitt));
		}
		return list;
	});

	const schuljahresabschnittItems = computed(() => [...abschnittState.alle]);

	const jahrgangItems = computed(() => [...state.uvManager.jahrgangsdatenGetMenge()]);
	const klassenItems = computed(() => state.planungsabschnitt === null ? [] : [...state.uvManager.klasseGetMengeByPlanungsabschnitt(state.planungsabschnitt)]);
	const fachItems = computed(() => [...state.uvManager.fachGetMengeAsList()]);
	const selectedFach = computed(() => fachItems.value.find(fach => fach.id === item.value.idFach));
	const kursarten = computed(() => getKursarten(selectedSchuljahresabschnitt.value?.schuljahr ?? schuleState.abschnitt.schuljahr));

	const validationErrors = computed(() => UvPlanungsManager.lerngruppeNeuFehler(state.uvManager, {
		unterrichtsart: unterrichtsart.value,
		kurs: item.value,
		schuljahr: selectedSchuljahresabschnitt.value?.schuljahr ?? schuleState.abschnitt.schuljahr,
		hatKlasse: selectedKlasse.value !== undefined,
		hatSchuljahresabschnitt: selectedSchuljahresabschnitt.value !== undefined,
		wochenstunden: wochenstunden.value,
		wochenstundenUnterrichtet: wochenstundenUnterrichtet.value,
	}));
	const isValid = computed(() => hatKompetenzAendern.value && validationErrors.value.length === 0);
	const hinzugefuegteIds = computed(() => new Set(props.selectedSchueler.map(schueler => schueler.idSchueler)));

	item.value.kursnummer = 1;
	selectedSchuelergruppe.value = schuelergruppenItems.value[0];
	selectedSchuljahresabschnitt.value = schuleState.abschnitt;
	watch([unterrichtsart, () => selectedSchuelergruppe.value.id], () => {
		emit('schuelergruppe-change', unterrichtsart.value === 'kurs' ? selectedSchuelergruppe.value.id : null);
	});

	function dragStartEntfernen(event: DragEvent, idSchueler: number): void {
		if (event.dataTransfer === null) {
			return;
		}
		uvDragDatenSchreiben(event.dataTransfer, { typ: 'lerngruppe-entwurf', aktion: 'entfernen', id: idSchueler });
	}

	/** Legt Klassenunterricht oder Kursunterricht mit den Daten des lokalen Entwurfs an. */
	async function create(): Promise<void> {
		const planungsabschnitt = state.planungsabschnitt;
		const art = unterrichtsart.value;
		const auswahl = art === 'klasse' ? selectedKlasse.value : selectedSchuljahresabschnitt.value;
		if (!isValid.value || loading.value || planungsabschnitt === null || planungsabschnitt.id === -1 || auswahl === undefined) {
			return;
		}
		loading.value = true;
		error.value = "";
		try {
			let neueLerngruppe;
			if (art === 'klasse') {
				neueLerngruppe = await state.planung.createLerngruppe({ idKlasse: auswahl.id, idFach: item.value.idFach, wochenstunden: wochenstunden.value, wochenstundenUnterrichtet: wochenstundenUnterrichtet.value });
			} else {
				neueLerngruppe = await state.planung.createKursLerngruppe({ ...item.value, idSchuljahresabschnitt: auswahl.id }, selectedSchuelergruppe.value.id, selectedJahrgaenge.value.map(j => j.id),
					props.selectedSchueler.map(schueler => schueler.idSchueler), wochenstunden.value, wochenstundenUnterrichtet.value);
			}
			if (state.planungsabschnitt?.id === planungsabschnitt.id) {
				emit('created', neueLerngruppe.id);
			}
		} catch (error_) {
			error.value = error_ instanceof Error ? error_.message : 'Die Lerngruppe konnte nicht erstellt werden. Bitte die Eingaben prüfen und erneut versuchen.';
		} finally {
			loading.value = false;
		}
	}

</script>
