<template>
	<div v-if="props.kurs !== undefined" class="page page-grid-cards">
		<svws-ui-content-card class="col-span-full">
			<template #title>
				<div class="content-card--header"><h3 class="content-card--headline" title="Daten des Kurses">{{ kursBezeichnung }}</h3>&nbsp;<svws-ui-badge type="light" title="ID" class="font-mono" size="small"> ID: {{ props.kurs?.id }} </svws-ui-badge></div>
			</template>
			<div class="mb-3 flex flex-wrap items-center gap-2">Schülergruppe <svws-ui-button v-if="kurs?.idSchuelergruppe !== null && kurs?.idSchuelergruppe !== undefined" type="icon" title="Schülergruppe öffnen" @click="gotoKursSchuelergruppe"><span class="icon i-ri-link" /></svws-ui-button></div>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Fach" :model-value="selectedFach" :items="fachItems" :item-text="presenter.fachText"
					@update:model-value="fach => fach !== null && fach !== undefined && patchKurs({ idFach: fach.id })" :readonly="!hatKompetenzAendern" :disabled="loading" autocomplete required />
				<svws-ui-select title="Kursart" :model-value="formKurs.kursart" :items="[...kursarten.keys()]"
					:item-text="art => `${art} (${kursarten.get(art) ?? 'Nicht verfügbar'})`"
					@update:model-value="kursart => kursart !== null && kursart !== undefined && patchKurs({ kursart })" :readonly="!hatKompetenzAendern" :disabled="loading" autocomplete required />
				<svws-ui-input-number placeholder="Kursnummer" :model-value="formKurs.kursnummer"
					@change="kursnummer => kursnummer !== null && patchKurs({ kursnummer })" :valid="value => (value !== null) && Number.isInteger(value) && (value >= 1)" :min="1" :step="1" :readonly="!hatKompetenzAendern" :disabled="loading" required />
				<svws-ui-select :model-value="selectedSchuljahresabschnitt"
					@update:model-value="onSchuljahresabschnittChange"
					:items="schuljahresabschnittItems"
					:item-text="presenter.schuljahresabschnittText"
					title="Schuljahresabschnitt" :readonly="!hatKompetenzAendern" :disabled="loading" required />
				<svws-ui-select title="Schülergruppe"
					:model-value="selectedSchuelergruppe"
					@update:model-value="onSchuelergruppeChange"
					:items="schuelergruppeItems"
					:item-text="sg => sg.bezeichnung" :readonly="!hatKompetenzAendern" :disabled="loading" autocomplete required />
			</svws-ui-input-wrapper>
			<div v-if="error" class="mt-4">
				<svws-ui-notification type="error">{{ error }}</svws-ui-notification>
				<svws-ui-button v-if="failedPatch !== undefined && hatKompetenzAendern && validationErrors.length === 0" type="secondary" :disabled="loading" @click="patchKurs(failedPatch)">Erneut versuchen</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<s-uv-schuelergruppen-schuelerliste v-if="persistierteSchuelergruppe !== undefined"
			:gruppe="persistierteSchuelergruppe"
			class="col-span-full"
			title="Schüler des Kurses"
			no-data-text="Keine Schüler in diesem Kurs"
			:goto-schueler
			:goto-klasse />
	</div>
	<div v-else class="page page-flex-row max-w-480">
		<span class="text-ui-disabled">Kein Kurs ausgewählt</span>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";

	import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
	import { ZulaessigeKursart } from "@core/asd/types/kurse/ZulaessigeKursart";
	import { UvKurs } from "@core/core/data/uv/UvKurs";
	import type { UvSchuelergruppe } from "@core/core/data/uv/UvSchuelergruppe";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useUvState } from "@ui/states/UvState";

	import { useUvPresenter } from "../UvPresenter";
	import SUvSchuelergruppenSchuelerliste from "~/components/unterrichtsverteilung/schuelergruppen/SUvSchuelergruppenSchuelerliste.vue";

	const props = defineProps<{
		kurs: UvKurs | undefined;
		schuljahr: number;
		gotoSchuelergruppe: (idSchuelergruppe: number) => Promise<void>;
		gotoSchueler: (idSchueler: number) => Promise<void>;
		gotoKlasse: (idKlasse: number) => Promise<void>;
	}>();
	const abschnittState = useAbschnittState();
	const state = useUvState();
	const presenter = useUvPresenter(state.uvManager);

	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const error = ref("");
	const failedPatch = ref<Partial<UvKurs> | undefined>();
	const formKurs = computed(() => Object.assign(new UvKurs(), props.kurs, failedPatch.value));
	const kursBezeichnung = computed(() => props.kurs === undefined ? 'Kurs' : presenter.kursBezeichnung(props.kurs));
	const fachItems = computed(() => [...state.uvManager.fachGetMengeAsList()]);
	const selectedFach = computed(() => fachItems.value.find(fach => fach.id === formKurs.value.idFach));
	const kursarten = computed(() => getKursarten(selectedSchuljahresabschnitt.value?.schuljahr ?? props.schuljahr));
	const validationErrors = computed(() => {
		const errors: string[] = [];
		if (state.uvManager.fachGetByIdOrNull(formKurs.value.idFach) === null) {
			errors.push('Bitte ein Fach auswählen.');
		}
		if (!kursarten.value.has(formKurs.value.kursart)) {
			errors.push('Bitte eine für das Schuljahr gültige Kursart auswählen.');
		}
		if (!Number.isInteger(formKurs.value.kursnummer) || (formKurs.value.kursnummer < 1)) {
			errors.push('Die Kursnummer muss eine ganze Zahl ab 1 sein.');
		}
		return errors;
	});

	async function gotoKursSchuelergruppe(): Promise<void> {
		const idSchuelergruppe = props.kurs?.idSchuelergruppe;
		if (idSchuelergruppe === undefined) {
			return;
		}
		await props.gotoSchuelergruppe(idSchuelergruppe);
	}

	function getKursarten(schuljahr: number): Map<string, string> {
		const result = new Map<string, string>();
		for (const art of ZulaessigeKursart.data().getWerteBySchuljahr(schuljahr)) {
			const daten = art.daten(schuljahr);
			if ((daten === null) || (daten.kuerzel === 'PUK')) {
				continue;
			}
			result.set(daten.kuerzelAllg ?? daten.kuerzel, daten.bezeichnungAllg ?? daten.text);
			if (daten.kuerzelAllg === 'DK') {
				result.set(daten.kuerzel, daten.text);
			}
		}
		return new Map([...result.entries()].sort());
	}

	watch(() => props.kurs?.id, () => {
		error.value = "";
		failedPatch.value = undefined;
	});

	async function patchKurs(patch: Partial<UvKurs>) {
		const kurs = props.kurs;
		if (kurs === undefined || !hatKompetenzAendern.value || loading.value) {
			return;
		}
		error.value = "";
		const changes = { ...failedPatch.value, ...patch };
		failedPatch.value = changes;
		if (validationErrors.value.length > 0) {
			error.value = validationErrors.value.join(' ') + ' Die Änderung wurde nicht gespeichert.';
			return;
		}
		loading.value = true;
		try {
			await state.patchKurs(kurs.idPlanungsabschnitt, kurs.id, changes);
			if (props.kurs?.id === kurs.id) {
				failedPatch.value = undefined;
			}
		} catch {
			if (props.kurs?.id === kurs.id) {
				failedPatch.value = changes;
				error.value = "Die Änderung konnte nicht gespeichert werden. Bitte erneut versuchen.";
			}
		} finally {
			loading.value = false;
		}
	}

	const schuljahresabschnittItems = computed(() => [...abschnittState.alle]);

	const selectedSchuljahresabschnitt = computed(() => {
		if (props.kurs === undefined) {
			return undefined;
		}
		return abschnittState.getOrNull(formKurs.value.idSchuljahresabschnitt) ?? undefined;
	});

	async function onSchuljahresabschnittChange(sja: Schuljahresabschnitt | null | undefined) {
		if (props.kurs === undefined || sja === null || sja === undefined) {
			return;
		}
		await patchKurs({ idSchuljahresabschnitt: sja.id });
	}

	const schuelergruppeItems = computed(() => state.planungsabschnitt === null ? []
		: [...state.uvManager.schuelergruppeGetMengeByPlanungsabschnitt(state.planungsabschnitt)]);

	const selectedSchuelergruppe = computed(() => {
		if (props.kurs === undefined) {
			return undefined;
		}
		return schuelergruppeItems.value.find(sg => sg.id === formKurs.value.idSchuelergruppe);
	});

	const persistierteSchuelergruppe = computed(() => schuelergruppeItems.value.find(sg => sg.id === props.kurs?.idSchuelergruppe));

	async function onSchuelergruppeChange(sg: UvSchuelergruppe | null | undefined) {
		if (props.kurs === undefined || sg === null || sg === undefined) {
			return;
		}
		await patchKurs({ idSchuelergruppe: sg.id });
	}

</script>
