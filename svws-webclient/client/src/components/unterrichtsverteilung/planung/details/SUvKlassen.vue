<template>
	<div v-if="klasse !== undefined" class="flex flex-col gap-3 p-3">
		<svws-ui-content-card title="Daten der Klasse" class="uv-deaktivierbar" :inert="bearbeitung !== null">
			<template #title>
				<div class="content-card--header gap-2"><h3 class="content-card--headline" title="Daten der Klasse">Daten der Klasse</h3><svws-ui-badge type="light" title="ID" class="font-mono" size="big"> ID: {{ klasse.id }} </svws-ui-badge></div>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Kürzel" :required="true" :max-len="15"
					:model-value="form.kuerzel"
					@change="onKuerzelChange" />
				<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Bezeichnung" :max-len="150"
					:model-value="form.bezeichnung"
					@change="bezeichnung => patch({ bezeichnung })" />
				<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading" title="Parallelität"
					:model-value="form.parallelitaet"
					@update:model-value="(value: string | null | undefined) => patch({ parallelitaet: value ?? '' })"
					:items="parallelitaetItems"
					:item-text="wert => wert === '' ? 'Keine' : wert" />
				<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading" title="Stundentafel"
					:model-value="selectedStundentafel"
					@update:model-value="onStundentafelChange"
					:items="stundentafelItems"
					:item-text="st => st.bezeichnung" />
				<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading" title="Schuljahresabschnitt"
					:model-value="selectedSchuljahresabschnitt"
					@update:model-value="onSchuljahresabschnittChange"
					:items="schuljahresabschnittItems"
					:item-text="schuljahresabschnittText" />
				<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading || bearbeitung !== null" title="Schülergruppe"
					:model-value="selectedSchuelergruppe"
					@update:model-value="onSchuelergruppeChange"
					:items="schuelergruppeItems"
					:item-text="sg => sg.bezeichnung" />
				<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading" title="Organisationsform"
					:model-value="selectedOrganisationsform"
					@update:model-value="onOrganisationsformChange"
					:items="organisationsformItems"
					:item-text="eintrag => eintrag.text" />
				<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading" title="Schulgliederung"
					:model-value="selectedSchulgliederung"
					@update:model-value="onSchulgliederungChange"
					:items="schulgliederungItems"
					:item-text="eintrag => eintrag.text" />
				<svws-ui-input-number :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Fachklasse-ID" :model-value="form.idFachklasse"
					@change="idFachklasse => patch({ idFachklasse })" />
			</svws-ui-input-wrapper>
			<div class="flex flex-wrap gap-2">
				<svws-ui-button v-if="hatKompetenzAendern"
					type="secondary"
					:disabled="loading || bearbeitung !== null"
					@click="props.schuelergruppeErstellen('klassen', klasse.id, klasse.bezeichnung?.trim() || klasse.kuerzel)">
					Neue Schülergruppe
				</svws-ui-button>
				<svws-ui-button v-if="klasse.idStundentafel !== null && bearbeitung === null"
					type="secondary"
					:disabled="loading"
					@click="gotoStundentafel(klasse.idStundentafel)">
					Stundentafel öffnen
				</svws-ui-button>
			</div>
			<svws-ui-notification v-if="formularFehler !== ''" type="error">{{ formularFehler }}</svws-ui-notification>
			<svws-ui-button v-if="formularFehler !== '' && hatKompetenzAendern"
				type="secondary"
				:disabled="loading"
				@click="patch({})">
				Erneut speichern
			</svws-ui-button>
		</svws-ui-content-card>
		<s-uv-schuelergruppe-daten :gruppe="persistierteSchuelergruppe ?? null"
			:bearbeitung
			:inert="loading || (bearbeitung !== null && bearbeitung.art !== 'gruppe-jahrgang')"
			class="uv-deaktivierbar"
			:bearbeiten="props.bearbeiten" />
		<svws-ui-content-card title="Klassenlehrer" class="uv-deaktivierbar" :inert="bearbeitung !== null && !klassenlehrerBearbeitung">
			<template #actions v-if="hatKompetenzAendern"><svws-ui-button v-if="bearbeitung === null" type="secondary" @click="props.bearbeiten('klasse-lehrer', klasse.id)" :disabled="loading">Bearbeiten</svws-ui-button></template>
			<div v-if="klassenlehrer.length > 0" class="grid grid-cols-[repeat(auto-fill,minmax(13rem,1fr))] gap-3">
				<s-uv-planungs-lehrer-zuordnung-kachel v-for="zuordnung in klassenlehrer"
					:key="zuordnung.id"
					:lehrer="state.uvManager.lehrerGetByKlassenLehrer(zuordnung)"
					:id-klasse="klasse.id"
					:reihenfolge="reihenfolgeEntwurf?.id === zuordnung.id ? reihenfolgeEntwurf.wert : zuordnung.reihenfolge"
					:gespeicherte-reihenfolge="zuordnung.reihenfolge"
					:readonly="!hatKompetenzAendern"
					:loading
					:editing="klassenlehrerBearbeitung"
					@fixierung-busy="fixierungLaeuft = $event"
					:oeffnen="() => gotoLehrer(zuordnung.idLehrer)"
					:reihenfolge-aendern="value => patchKlassenLehrer(zuordnung, value)"
					:entfernen="() => deleteKlassenlehrer(zuordnung)" />
			</div>
			<p v-else class="text-sm text-ui-secondary">Keine Klassenlehrer zugeordnet.</p>
			<svws-ui-notification v-if="zuordnungsFehler !== ''" type="error">{{ zuordnungsFehler }}</svws-ui-notification>
			<svws-ui-button v-if="erneutVersuchen !== null"
				type="secondary"
				:disabled="loading || (reihenfolgeEntwurf === null && !klassenlehrerBearbeitung)"
				@click="erneutVersuchen?.()">
				Erneut versuchen
			</svws-ui-button>
		</svws-ui-content-card>
		<s-uv-schuelergruppen-schuelerliste v-if="persistierteSchuelergruppe !== undefined"
			:gruppe="persistierteSchuelergruppe"
			:goto-schueler
			:goto-klasse
			:bearbeitung
			:zuordnung-pending
			:schueler-entfernen
			title="Schüler der Klasse"
			:inert="loading || (bearbeitung !== null && bearbeitung.art !== 'gruppe-schueler')"
			class="uv-deaktivierbar"
			:bearbeiten="idGruppe => props.bearbeiten('gruppe-schueler', idGruppe)" />
	</div>
	<div v-else class="p-3">
		<span class="text-ui-disabled">Keine Klasse ausgewählt</span>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, shallowRef, watch } from 'vue';

	import type { Schuljahresabschnitt } from '@core/asd/data/schule/Schuljahresabschnitt';
	import { UvKlasse } from '@core/core/data/uv/UvKlasse';
	import type { UvKlassenLehrer } from '@core/core/data/uv/UvKlassenLehrer';
	import type { UvSchuelergruppe } from '@core/core/data/uv/UvSchuelergruppe';
	import type { UvStundentafel } from '@core/core/data/uv/UvStundentafel';
	import { useAbschnittState } from '@ui/states/AbschnittState';
	import { useUvState } from '@ui/states/UvState';

	import SUvSchuelergruppenSchuelerliste from '../../schuelergruppen/SUvSchuelergruppenSchuelerliste.vue';
	import { klassenKatalogOptionen, parallelitaetItems, schuljahresabschnittText } from '../../UvPresenter';
	import SUvPlanungsLehrerZuordnungKachel from '../SUvPlanungsLehrerZuordnungKachel.vue';
	import type { UvZuordnungArt, UvZuordnungBearbeitung } from '../UvPlanung';

	import SUvSchuelergruppeDaten from './SUvSchuelergruppeDaten.vue';

	const props = defineProps<{
		klasse: UvKlasse | undefined;
		schuljahr: number;
		gotoSchueler: (idSchueler: number) => void;
		gotoKlasse: (idKlasse: number) => void;
		gotoLehrer: (idLehrer: number) => void;
		gotoStundentafel: (idStundentafel: number) => Promise<void>;
		bearbeitung: UvZuordnungBearbeitung | null;
		zuordnungPending: boolean;
		schuelerEntfernen: (idGruppe: number, idSchueler: number) => Promise<void>;
		bearbeiten: (art: UvZuordnungArt, idZiel: number) => void;
		schuelergruppeErstellen: (art: 'klassen', id: number, bezeichnung: string) => void;
	}>();

	const abschnittState = useAbschnittState();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const speichert = ref(false);
	const fixierungLaeuft = ref(false);
	const loading = computed(() => speichert.value || fixierungLaeuft.value);
	const entwurf = shallowRef<Partial<UvKlasse>>({});
	const formularFehler = ref('');
	const zuordnungsFehler = ref('');
	const reihenfolgeEntwurf = ref<{ id: number; wert: number } | null>(null);
	const erneutVersuchen = shallowRef<(() => Promise<void>) | null>(null);

	// Formular und Kataloge für die ausgewählte Klasse

	const form = computed(() => Object.assign(new UvKlasse(), props.klasse, entwurf.value));
	const schuljahresabschnittItems = computed(() => [...abschnittState.alle]);
	const selectedSchuljahresabschnitt = computed(() => abschnittState.getOrNull(form.value.idSchuljahresabschnitt) ?? undefined);
	const katalogSchuljahr = computed(() => selectedSchuljahresabschnitt.value?.schuljahr ?? props.schuljahr);
	const kataloge = computed(() => klassenKatalogOptionen(katalogSchuljahr.value, form.value.orgFormKrz, form.value.asdSchulformNr));
	const organisationsformItems = computed(() => kataloge.value.organisationsformen);
	const schulgliederungItems = computed(() => kataloge.value.schulgliederungen);
	const selectedOrganisationsform = computed(() => organisationsformItems.value.find(option => option.kuerzel === form.value.orgFormKrz));
	const selectedSchulgliederung = computed(() => schulgliederungItems.value.find(option => option.kuerzel === form.value.asdSchulformNr));

	const stundentafelItems = computed(() => [{ id: null, bezeichnung: 'Keine Stundentafel' }, ...state.uvManager.stundentafelGetMengeAsList()]);
	const selectedStundentafel = computed(() => stundentafelItems.value.find(tafel => tafel.id === form.value.idStundentafel) ?? stundentafelItems.value[0]);
	const schuelergruppeItems = computed(() => state.planungsabschnitt === null ? []
		: [...state.uvManager.schuelergruppeGetMengeByPlanungsabschnitt(state.planungsabschnitt)]);
	const selectedSchuelergruppe = computed(() => schuelergruppeItems.value.find(gruppe => gruppe.id === form.value.idSchuelergruppe));
	const persistierteSchuelergruppe = computed(() => schuelergruppeItems.value.find(gruppe => gruppe.id === props.klasse?.idSchuelergruppe));
	// Klassenleitung und Entwurf zur Reihenfolge

	const klassenlehrer = computed(() => props.klasse === undefined ? [] : [...state.uvManager.klassenLehrerGetMengeByKlasse(props.klasse)]);
	const klassenlehrerBearbeitung = computed(() => props.bearbeitung?.art === 'klasse-lehrer' && props.bearbeitung.idZiel === props.klasse?.id);

	watch(() => props.klasse?.id, () => {
		entwurf.value = {};
		formularFehler.value = '';
		zuordnungsFehler.value = '';
		reihenfolgeEntwurf.value = null;
		erneutVersuchen.value = null;
	});

	/** Speichert die Klassenänderung und behält den Entwurf bei einem Fehler. */
	async function patch(changes: Partial<UvKlasse>): Promise<void> {
		const klasse = props.klasse;
		if (klasse === undefined || loading.value || !hatKompetenzAendern.value || props.bearbeitung !== null) {
			return;
		}
		const daten = { ...entwurf.value, ...changes };
		entwurf.value = daten;
		formularFehler.value = '';
		if (form.value.kuerzel.trim() === '') {
			formularFehler.value = 'Bitte ein Kürzel eingeben.';
			return;
		}
		speichert.value = true;
		try {
			await state.planung.patchKlasse(klasse.id, daten);
			if (props.klasse?.id === klasse.id) {
				entwurf.value = {};
			}
		} catch (error) {
			if (props.klasse?.id === klasse.id) {
				formularFehler.value = error instanceof Error ? error.message : 'Die Klasse konnte nicht gespeichert werden.';
			}
		} finally {
			speichert.value = false;
		}
	}

	function onKuerzelChange(kuerzel: string | null | undefined): Promise<void> {
		return patch({ kuerzel: kuerzel ?? '' });
	}

	function onStundentafelChange(tafel: UvStundentafel | { id: null; bezeichnung: string } | null | undefined): Promise<void> {
		return patch({ idStundentafel: tafel?.id ?? null });
	}

	function onSchuljahresabschnittChange(abschnitt: Schuljahresabschnitt | null | undefined): void {
		if (abschnitt !== null && abschnitt !== undefined) {
			void patch({ idSchuljahresabschnitt: abschnitt.id });
		}
	}

	function onSchuelergruppeChange(gruppe: UvSchuelergruppe | null | undefined): void {
		if (gruppe !== null && gruppe !== undefined) {
			void patch({ idSchuelergruppe: gruppe.id });
		}
	}

	function onOrganisationsformChange(option: { kuerzel: string | null } | null | undefined): Promise<void> {
		return patch({ orgFormKrz: option?.kuerzel ?? null });
	}

	function onSchulgliederungChange(option: { kuerzel: string | null } | null | undefined): Promise<void> {
		return patch({ asdSchulformNr: option?.kuerzel ?? null });
	}

	function patchKlassenLehrer(zuordnung: UvKlassenLehrer, reihenfolge: number | null): Promise<void> {
		return aendereKlassenlehrer(zuordnung, reihenfolge ?? zuordnung.reihenfolge);
	}

	function deleteKlassenlehrer(zuordnung: UvKlassenLehrer): Promise<void> {
		return aendereKlassenlehrer(zuordnung, null);
	}

	function istAktuelleKlasse(id: number): boolean {
		return props.klasse?.id === id;
	}

	async function aendereKlassenlehrer(zuordnung: UvKlassenLehrer, reihenfolge: number | null): Promise<void> {
		if (loading.value || !hatKompetenzAendern.value || !istAktuelleKlasse(zuordnung.idKlasse)
			|| (reihenfolge === null ? !klassenlehrerBearbeitung.value : props.bearbeitung !== null && !klassenlehrerBearbeitung.value)) {
			return;
		}
		zuordnungsFehler.value = '';
		erneutVersuchen.value = null;
		reihenfolgeEntwurf.value = reihenfolge === null ? null : { id: zuordnung.id, wert: reihenfolge };
		speichert.value = true;
		try {
			if (reihenfolge === null) {
				await state.planung.delKlassenLehrer([zuordnung]);
			} else {
				await state.planung.patchKlassenLehrer(zuordnung.id, { reihenfolge });
			}
			reihenfolgeEntwurf.value = null;
		} catch (error) {
			if (istAktuelleKlasse(zuordnung.idKlasse)) {
				zuordnungsFehler.value = error instanceof Error ? error.message : 'Die Zuordnung konnte nicht gespeichert werden.';
				erneutVersuchen.value = () => aendereKlassenlehrer(zuordnung, reihenfolge);
			}
		} finally {
			speichert.value = false;
		}
	}
</script>
