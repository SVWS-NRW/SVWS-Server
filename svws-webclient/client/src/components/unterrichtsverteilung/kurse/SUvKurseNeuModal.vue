<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden" :auto-close="!loading" :close-in-title="!loading">
		<template #modalTitle>Kurs erstellen</template>
		<template #modalContent>
			<p class="text-sm text-ui-secondary mb-4">Der Kurs wird im Planungsabschnitt „{{ state.planungsabschnitt?.beschreibung }}“ erstellt.</p>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select v-model="selectedSchuljahresabschnitt" :disabled="loading" required :items="schuljahresabschnittItems" :item-text="presenter.schuljahresabschnittText" class="col-span-full" title="Schuljahresabschnitt" />
				<svws-ui-select title="Fach" :model-value="selectedFach" :items="fachItems" :item-text="presenter.fachText"
					@update:model-value="fach => fach !== null && fach !== undefined && (item.idFach = fach.id)" :disabled="loading || !hatKompetenzAendern" autocomplete required />
				<svws-ui-select title="Kursart" :model-value="item.kursart" :items="[...kursarten.keys()]"
					:item-text="art => `${art} (${kursarten.get(art) ?? 'Nicht verfügbar'})`"
					@update:model-value="kursart => kursart !== null && kursart !== undefined && (item.kursart = kursart)" :disabled="loading || !hatKompetenzAendern" autocomplete required />
				<svws-ui-input-number placeholder="Kursnummer" :model-value="item.kursnummer" @change="value => item.kursnummer = value ?? 0"
					:valid="value => (value !== null) && Number.isInteger(value) && (value >= 1)" :min="1" :step="1" :disabled="loading || !hatKompetenzAendern" required />
				<svws-ui-select v-model="selectedSchuelergruppe" :disabled="loading" :items="schuelergruppenItems" :item-text="sg => sg.bezeichnung" class="col-span-full" title="Schülergruppe" />
				<svws-ui-multi-select v-if="selectedSchuelergruppe.id === null" v-model="selectedJahrgaenge" :disabled="loading" :items="jahrgangItems" :item-text="item => item.kuerzel ?? ''" class="col-span-full" title="Jahrgänge (für neue Schülergruppe)" />
			</svws-ui-input-wrapper>
			<ul v-if="validationErrors.length > 0" class="text-sm text-ui-secondary mt-4" aria-live="polite">
				<li v-for="message in validationErrors" :key="message">{{ message }}</li>
			</ul>
			<svws-ui-notification v-if="error" type="error" class="mt-4">{{ error }}</svws-ui-notification>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false" :disabled="loading"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="create" :disabled="!isValid || loading" :is-loading="loading">Kurs erstellen</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
	import { ZulaessigeKursart } from "@core/asd/types/kurse/ZulaessigeKursart";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import { UvKurs } from "@core/core/data/uv/UvKurs";
	import type { UvSchuelergruppe } from "@core/core/data/uv/UvSchuelergruppe";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { useUvState } from "@ui/states/UvState";

	import { useUvPresenter } from "../UvPresenter";

	const emit = defineEmits<{ created: [kurs: UvKurs] }>();
	const state = useUvState();
	const presenter = useUvPresenter(state.uvManager);
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const error = ref("");
	const show = ref<boolean>(false);
	const abschnittState = useAbschnittState();
	const schuleState = useSchuleState();
	const item = ref<UvKurs>(new UvKurs());
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
	const fachItems = computed(() => [...state.uvManager.fachGetMengeAsList()]);
	const selectedFach = computed(() => fachItems.value.find(fach => fach.id === item.value.idFach));
	const kursarten = computed(() => getKursarten(selectedSchuljahresabschnitt.value?.schuljahr ?? schuleState.abschnitt.schuljahr));

	const validationErrors = computed(() => {
		const errors: string[] = [];
		if (state.uvManager.fachGetByIdOrNull(item.value.idFach) === null) {
			errors.push('Bitte ein Fach auswählen.');
		}
		if (!kursarten.value.has(item.value.kursart)) {
			errors.push('Bitte eine für das Schuljahr gültige Kursart auswählen.');
		}
		if (!Number.isInteger(item.value.kursnummer) || (item.value.kursnummer < 1)) {
			errors.push('Die Kursnummer muss eine ganze Zahl ab 1 sein.');
		}
		if (selectedSchuljahresabschnitt.value === undefined) {
			errors.push('Bitte einen Schuljahresabschnitt auswählen.');
		}
		return errors;
	});
	const isValid = computed(() => hatKompetenzAendern.value && validationErrors.value.length === 0);

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

	watch(() => state.planungsabschnitt?.id, () => {
		show.value = false;
	});

	const openModal = () => {
		if (!hatKompetenzAendern.value || loading.value) {
			return;
		}
		error.value = "";
		item.value = new UvKurs();
		item.value.kursnummer = 1;
		selectedSchuelergruppe.value = schuelergruppenItems.value[0];
		selectedSchuljahresabschnitt.value = schuleState.abschnitt;
		selectedJahrgaenge.value = [];
		show.value = true;
	};

	async function create() {
		const planungsabschnitt = state.planungsabschnitt;
		const abschnitt = selectedSchuljahresabschnitt.value;
		if (!isValid.value || loading.value || planungsabschnitt === null || planungsabschnitt.id === -1 || abschnitt === undefined) {
			return;
		}
		loading.value = true;
		error.value = "";
		try {
			item.value.idPlanungsabschnitt = planungsabschnitt.id;
			item.value.idSchuljahresabschnitt = abschnitt.id;
			const kurs = await state.createKurs(item.value, selectedSchuelergruppe.value.id, selectedJahrgaenge.value.map(j => j.id));
			show.value = false;
			if (state.planungsabschnitt?.id === planungsabschnitt.id) {
				emit('created', kurs);
			}
		} catch {
			const gruppe = schuelergruppenItems.value.find(g => g.id === item.value.idSchuelergruppe);
			if (gruppe !== undefined) {
				selectedSchuelergruppe.value = gruppe;
			}
			error.value = "Der Kurs konnte nicht erstellt werden. Die Eingaben und eine bereits angelegte Schülergruppe bleiben erhalten. Bitte erneut versuchen.";
		} finally {
			loading.value = false;
		}
	}

</script>
