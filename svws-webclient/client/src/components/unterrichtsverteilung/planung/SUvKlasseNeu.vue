<template>
	<section class="flex min-h-0 flex-1 flex-col bg-ui">
		<div class="flex shrink-0 items-center justify-between gap-2 border-b border-ui-10 p-3">
			<h3 class="font-semibold">Klasse erstellen</h3>
			<svws-ui-button type="secondary" :disabled="loading" @click="props.abbrechen()">Abbrechen</svws-ui-button>
		</div>
		<div class="min-h-0 flex-1 overflow-auto p-3">
			<svws-ui-content-card title="Daten der Klasse">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input v-model="item.kuerzel" :disabled="loading" :max-len="15" required placeholder="Kürzel" />
					<svws-ui-text-input v-model="item.bezeichnung" :disabled="loading" :max-len="150" placeholder="Bezeichnung" />
					<svws-ui-select title="Parallelität" :disabled="loading" :model-value="item.parallelitaet"
						@update:model-value="value => item.parallelitaet = value ?? ''"
						:items="parallelitaetItems" :item-text="wert => wert === '' ? 'Keine' : wert" />
					<svws-ui-select v-model="selectedSchuljahresabschnitt"
						:disabled="loading"
						:items="schuljahresabschnittItems"
						:item-text="schuljahresabschnittText"
						title="Schuljahresabschnitt"
						required />
					<svws-ui-select v-model="selectedStundentafel"
						:disabled="loading"
						:items="stundentafelItems"
						:item-text="st => st.bezeichnung"
						title="Stundentafel" />
					<svws-ui-select v-model="selectedSchuelergruppe"
						:disabled="loading"
						:items="schuelergruppenItems"
						:item-text="sg => sg.bezeichnung"
						title="Schülergruppe" />
					<svws-ui-multi-select v-if="selectedSchuelergruppe.id === null"
						v-model="selectedJahrgaenge"
						:disabled="loading"
						:items="jahrgangItems"
						:item-text="jahrgang => jahrgang.kuerzel ?? ''"
						class="col-span-full"
						title="Jahrgänge (für neue Schülergruppe)" />
				</svws-ui-input-wrapper>
				<svws-ui-notification v-if="error" type="error" class="mt-3">{{ error }}</svws-ui-notification>
			</svws-ui-content-card>
		</div>
		<div class="flex shrink-0 justify-end border-t border-ui-10 p-3">
			<svws-ui-button type="primary" :disabled="loading || !isValid" :is-loading="loading" @click="create">Klasse erstellen</svws-ui-button>
		</div>
	</section>
</template>

<script setup lang="ts">
	import { computed, ref } from 'vue';

	import type { Schuljahresabschnitt } from '@core/asd/data/schule/Schuljahresabschnitt';
	import type { JahrgangsDaten } from '@core/core/data/jahrgang/JahrgangsDaten';
	import { UvKlasse } from '@core/core/data/uv/UvKlasse';
	import type { UvSchuelergruppe } from '@core/core/data/uv/UvSchuelergruppe';
	import type { UvStundentafel } from '@core/core/data/uv/UvStundentafel';
	import { useAbschnittState } from '@ui/states/AbschnittState';
	import { useSchuleState } from '@ui/states/SchuleState';
	import { useUvState } from '@ui/states/UvState';

	import { parallelitaetItems, schuljahresabschnittText } from '../UvPresenter';

	const props = defineProps<{
		abbrechen: () => void;
	}>();
	const emit = defineEmits<{
		created: [id: number];
	}>();
	const state = useUvState();
	const abschnittState = useAbschnittState();
	const schuleState = useSchuleState();
	const item = ref(new UvKlasse());
	const loading = ref(false);
	const error = ref('');
	const selectedSchuelergruppe = ref<UvSchuelergruppe | { id: null; bezeichnung: string }>({ id: null, bezeichnung: 'Neue Schülergruppe anlegen' });
	const selectedStundentafel = ref<UvStundentafel | { id: null; bezeichnung: string }>({ id: null, bezeichnung: 'Keine Stundentafel' });
	const selectedSchuljahresabschnitt = ref<Schuljahresabschnitt | undefined>(schuleState.abschnitt);
	const selectedJahrgaenge = ref<JahrgangsDaten[]>([]);
	const schuelergruppenItems = computed(() => state.planungsabschnitt === null ? [] : [
		{ id: null, bezeichnung: 'Neue Schülergruppe anlegen' },
		...state.uvManager.schuelergruppeGetMengeByPlanungsabschnitt(state.planungsabschnitt),
	]);
	const stundentafelItems = computed(() => [{ id: null, bezeichnung: 'Keine Stundentafel' }, ...state.uvManager.stundentafelGetMengeAsList()]);
	const schuljahresabschnittItems = computed(() => [...abschnittState.alle]);
	const jahrgangItems = computed(() => [...state.uvManager.jahrgangsdatenGetMenge()]);
	const isValid = computed(() => state.hatKompetenzAendern && state.planungsabschnitt !== null && state.planungsabschnitt.id !== -1
		&& item.value.kuerzel.trim().length > 0 && item.value.kuerzel.length <= 15
		&& (item.value.bezeichnung?.length ?? 0) <= 150 && item.value.parallelitaet.length <= 2 && selectedSchuljahresabschnitt.value !== undefined);

	async function create(): Promise<void> {
		const abschnitt = selectedSchuljahresabschnitt.value;
		const plan = state.planungsabschnitt;
		if (!isValid.value || loading.value || abschnitt === undefined || plan === null) {
			return;
		}
		loading.value = true;
		error.value = '';
		try {
			const neu = await state.planung.createKlasseMitSchuelergruppe({ ...item.value, idPlanungsabschnitt: plan.id,
				idStundentafel: selectedStundentafel.value.id, idSchuljahresabschnitt: abschnitt.id,
				idSchuelergruppe: selectedSchuelergruppe.value.id ?? 0 },
				selectedSchuelergruppe.value.id === null ? selectedJahrgaenge.value.map(jahrgang => jahrgang.id) : null);
			emit('created', neu.id);
		} catch (e) {
			error.value = e instanceof Error ? e.message : 'Die Klasse konnte nicht erstellt werden. Bitte die Eingaben prüfen.';
		} finally {
			loading.value = false;
		}
	}
</script>
