<template>
	<div v-if="lerngruppe !== undefined" class="flex flex-col gap-3 p-3">
		<svws-ui-notification v-if="fehler" type="error">{{ fehler }}</svws-ui-notification>
		<svws-ui-notification v-if="stundenFehler || Object.keys(draft).length > 0" type="error">
			{{ stundenFehler }}
			<svws-ui-button v-if="stundenEntwurfGueltig"
				type="secondary"
				:disabled="loading || bearbeitung !== null"
				@click="patch({})">
				Stunden erneut speichern
			</svws-ui-button>
			<svws-ui-button type="secondary" :disabled="loading || bearbeitung !== null" @click="stundenVerwerfen">Stundenänderungen verwerfen</svws-ui-button>
		</svws-ui-notification>
		<svws-ui-content-card class="uv-deaktivierbar col-span-full" :inert="bearbeitung !== null">
			<template #title>
				<div class="content-card--header"><h3 class="content-card--headline" title="Daten der Lerngruppe">Daten der Lerngruppe</h3><s-uv-fach-badge :id-fach="presenter.lerngruppeFachId(lerngruppe)" />&nbsp;<svws-ui-badge type="light" title="ID" class="font-mono" size="small"> ID: {{ lerngruppe.id }} </svws-ui-badge></div>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Typ" :model-value="typ" readonly />
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="bezeichnung" readonly />
				<template v-if="lerngruppe.idKlasse !== null">
					<svws-ui-select title="Klasse"
						:model-value="selectedKlasse"
						:items="klassen"
						:item-text="item => item.kuerzel"
						:readonly="!hatKompetenzAendern"
						:disabled="loading || bearbeitung !== null"
						autocomplete
						required
						@update:model-value="item => item && patchStammdaten({ idKlasse: item.id })" />
					<svws-ui-select title="Fach"
						:model-value="selectedFach"
						:items="faecher"
						:item-text="presenter.fachText"
						:readonly="!hatKompetenzAendern"
						:disabled="loading || bearbeitung !== null"
						autocomplete
						required
						@update:model-value="item => item && patchStammdaten({ idFach: item.id })" />
				</template>
				<svws-ui-input-number placeholder="Wochenstunden"
					:model-value="form.wochenstunden"
					:readonly="!hatKompetenzAendern"
					:disabled="loading"
					:min="0"
					:steps="0.5"
					:decimal-places="2"
					@change="value => patch({ wochenstunden: value ?? -1 })" />
				<svws-ui-input-number placeholder="Wochenstunden unterrichtet"
					:model-value="form.wochenstundenUnterrichtet"
					:readonly="!hatKompetenzAendern"
					:disabled="loading"
					:min="0"
					:steps="0.5"
					:decimal-places="2"
					@change="value => patch({ wochenstundenUnterrichtet: value ?? -1 })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<div v-if="kurs !== null"
			:inert="bearbeitung !== null"
			class="uv-deaktivierbar">
			<s-uv-kurse :kurs :schuljahr :bearbeitung :schuelergruppe-erstellen />
		</div>
		<s-uv-schuelergruppe-daten :gruppe="schuelergruppe"
			:bearbeitung
			:inert="bearbeitung !== null && bearbeitung.art !== 'gruppe-jahrgang'"
			class="uv-deaktivierbar"
			:bearbeiten />
		<s-uv-lerngruppe-lehrkraftzuordnungen v-model:loading="loading"
			:lerngruppe
			:bearbeitung
			:bearbeiten
			:goto-lehrer />
		<svws-ui-content-card title="Schienen der Lerngruppe"
			class="uv-deaktivierbar col-span-full"
			:inert="bearbeitung !== null && bearbeitung.art !== 'lerngruppe-schiene'">
			<template #actions v-if="hatKompetenzAendern"><svws-ui-button v-if="bearbeitung === null" type="secondary" @click="bearbeiten('lerngruppe-schiene', lerngruppe.id)">Bearbeiten</svws-ui-button></template>
			<div v-if="lerngruppenschienen.size() > 0" class="flex flex-wrap gap-2">
				<div v-for="zuordnung in lerngruppenschienen"
					:key="zuordnung.idSchiene"
					class="flex items-center gap-2 rounded-lg border border-ui-25 bg-ui px-3 py-2 hover:border-ui-brand">
					<button type="button"
						class="min-w-0 text-left focus-visible:outline-2 focus-visible:outline-ui-brand"
						:disabled="bearbeitung !== null"
						@click="gotoSchiene(zuordnung.idSchiene)">
						<span class="font-semibold text-ui-brand">{{ state.uvManager.schieneGetByLerngruppenSchiene(zuordnung).nummer }}</span>
						<span class="ml-2 text-sm">{{ state.uvManager.schieneGetByLerngruppenSchiene(zuordnung).bezeichnung }}</span>
					</button>
					<svws-ui-button v-if="bearbeitung?.art === 'lerngruppe-schiene'"
						type="danger"
						:disabled="loading"
						@click="removeLerngruppenSchiene(zuordnung)">
						Entfernen
					</svws-ui-button>
				</div>
			</div>
			<p v-else class="text-sm text-ui-secondary">Keine Schienen zugeordnet.</p>
		</svws-ui-content-card>
		<s-uv-schuelergruppen-schuelerliste v-if="schuelergruppe !== null"
			:gruppe="schuelergruppe"
			title="Schüler der Lerngruppe"
			:goto-schueler
			:goto-klasse
			:bearbeitung
			:zuordnung-pending
			:schueler-entfernen
			:inert="bearbeitung !== null && bearbeitung.art !== 'gruppe-schueler'"
			class="uv-deaktivierbar"
			:bearbeiten="idGruppe => bearbeiten('gruppe-schueler', idGruppe)" />
	</div>
	<div v-else class="p-3">
		<span class="text-ui-secondary">Keine Lerngruppe ausgewählt</span>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, shallowRef, watch } from "vue";

	import { UvLerngruppe } from "@core/core/data/uv/UvLerngruppe";
	import type { UvLerngruppenSchiene } from "@core/core/data/uv/UvLerngruppenSchiene";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useUvState } from "@ui/states/UvState";

	import SUvFachBadge from "../../SUvFachBadge.vue";
	import { createUvPresenter } from "../../UvPresenter";
	import type { UvZuordnungArt, UvZuordnungBearbeitung } from "../UvPlanung";
	import { useUvPlanungsManager } from '../UvPlanungsKontext';
	import SUvSchuelergruppenSchuelerliste from "~/components/unterrichtsverteilung/schuelergruppen/SUvSchuelergruppenSchuelerliste.vue";

	import SUvKurse from './SUvKurse.vue';
	import SUvLerngruppeLehrkraftzuordnungen from './SUvLerngruppeLehrkraftzuordnungen.vue';
	import SUvSchuelergruppeDaten from './SUvSchuelergruppeDaten.vue';

	const { lerngruppe, bearbeitung, bearbeiten, schuelergruppeErstellen } = defineProps<{
		lerngruppe: UvLerngruppe | undefined;
		schuljahr: number;
		gotoKlasse: (id: number) => void;
		gotoLehrer: (id: number) => void;
		gotoSchiene: (id: number) => void;
		gotoSchueler: (id: number) => void;
		bearbeitung: UvZuordnungBearbeitung | null;
		zuordnungPending: boolean;
		schuelerEntfernen: (idGruppe: number, idSchueler: number) => Promise<void>;
		bearbeiten: (art: UvZuordnungArt, idZiel: number) => void;
		schuelergruppeErstellen: (art: 'kurse', id: number, bezeichnung: string) => void;
	}>();


	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));
	const planungsManager = useUvPlanungsManager();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const fehler = ref('');
	const stundenFehler = ref('');
	const draft = shallowRef<Partial<UvLerngruppe>>({});
	const form = computed(() => Object.assign(new UvLerngruppe(), lerngruppe, draft.value));
	const stundenEntwurfGueltig = computed(() => Object.keys(draft.value).length > 0
		&& planungsManager.value?.lerngruppeStundenAenderungGueltig(form.value) === true);

	// Anzeige der Stammdaten und Auswahlwerte für das Formular.

	const typ = computed(() => {
		if (lerngruppe === undefined) {
			return '';
		}
		return lerngruppe.idKurs === null ? 'Klassenunterricht' : 'Kursunterricht';
	});
	const bezeichnung = computed(() => lerngruppe === undefined ? '' : presenter.value.lerngruppeBezeichnung(lerngruppe));
	const kurs = computed(() => lerngruppe === undefined || lerngruppe.idKurs === null ? null : state.uvManager.kursGetByIdOrNull(lerngruppe.idKurs));
	const klassen = computed(() => state.planungsabschnitt === null ? [] : [...state.uvManager.klasseGetMengeByPlanungsabschnitt(state.planungsabschnitt)]);
	const faecher = computed(() => [...state.uvManager.fachGetMengeAsList()]);
	const selectedKlasse = computed(() => klassen.value.find(item => item.id === form.value.idKlasse));
	const selectedFach = computed(() => faecher.value.find(item => item.id === form.value.idFach));
	const schuelergruppe = computed(() => lerngruppe === undefined ? null : state.uvManager.schuelergruppeGetByLerngruppe(lerngruppe));

	watch(() => lerngruppe?.id, () => {
		fehler.value = '';
		stundenVerwerfen();
	});

	function stundenVerwerfen(): void {
		draft.value = {};
		stundenFehler.value = '';
	}

	// Stammdaten und Stundenentwurf speichern

	async function patchStammdaten(changes: Partial<UvLerngruppe>): Promise<void> {
		const id = lerngruppe?.id;
		if (id === undefined || !hatKompetenzAendern.value || loading.value || bearbeitung !== null) {
			return;
		}
		loading.value = true;
		fehler.value = '';
		try {
			await state.planung.patchLerngruppe(id, changes);
		} catch {
			if (lerngruppe?.id === id) {
				fehler.value = 'Die Änderung an Klasse oder Fach wurde nicht gespeichert. Bitte erneut auswählen.';
			}
		} finally {
			loading.value = false;
		}
	}

	/** Hält Stundenänderungen im Entwurf, bis ein gültiger Stand gespeichert werden kann. */
	async function patch(changes: Partial<UvLerngruppe>): Promise<void> {
		const id = lerngruppe?.id;
		if (id === undefined || !hatKompetenzAendern.value || loading.value || bearbeitung !== null) {
			return;
		}
		draft.value = { ...draft.value, ...changes };
		if (!stundenEntwurfGueltig.value) {
			stundenFehler.value = 'Nicht gespeichert: Die Stunden sind ungültig oder unterschreiten einen vorhandenen Lehrkrafteinsatz.';
			return;
		}
		loading.value = true;
		stundenFehler.value = '';
		try {
			await state.planung.patchLerngruppe(id, draft.value);
			if (lerngruppe?.id === id) {
				stundenVerwerfen();
			}
		} catch {
			if (lerngruppe?.id === id) {
				stundenFehler.value = 'Die Stunden wurden nicht gespeichert. Bitte erneut versuchen.';
			}
		} finally {
			loading.value = false;
		}
	}

	const lerngruppenschienen = computed(() => {
		if (lerngruppe === undefined) {
			return new ArrayList<UvLerngruppenSchiene>();
		}
		return state.uvManager.lerngruppenSchieneGetMengeByLerngruppe(lerngruppe);
	});

	async function removeLerngruppenSchiene(item: UvLerngruppenSchiene) {
		const idLerngruppe = lerngruppe?.id;
		if (idLerngruppe === undefined) {
			return;
		}
		if (loading.value || !hatKompetenzAendern.value || bearbeitung?.art !== 'lerngruppe-schiene') {
			return;
		}
		loading.value = true;
		fehler.value = '';
		try {
			await state.planung.delLerngruppenSchiene([item]);
		} catch {
			fehler.value = 'Die Änderung wurde nicht gespeichert. Bitte erneut versuchen.';
		} finally {
			loading.value = false;
		}
	}

</script>
