<template>
	<slot :open-modal />
	<svws-ui-modal :auto-close="!loading" :close-in-title="!loading" v-model:show="show" class="hidden">
		<template #modalTitle>Klasse hinzufügen</template>
		<template #modalContent>
			<svws-ui-notification v-if="actionError" type="error" class="mb-3">{{ actionError }}</svws-ui-notification>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select :disabled="loading || !hatKompetenzAendern" v-model="selectedSchuelergruppe" :items="schuelergruppenItems" :item-text="sg => sg.bezeichnung" class="col-span-full" title="Schülergruppe" />
				<svws-ui-multi-select :disabled="loading || !hatKompetenzAendern" v-if="selectedSchuelergruppe.id === null" v-model="selectedJahrgaenge" :items="jahrgangItems" :item-text="item => item.kuerzel ?? ''" class="col-span-full" title="Jahrgaenge (fuer neue Schuelergruppe)" />
				<svws-ui-select :disabled="loading || !hatKompetenzAendern" v-model="selectedStundentafel" :items="stundentafelItems" :item-text="st => st.bezeichnung" class="col-span-full" title="Stundentafel" />
				<svws-ui-select :disabled="loading || !hatKompetenzAendern" v-model="selectedSchuljahresabschnitt" :items="schuljahresabschnittItems" :item-text="sja => `${sja.schuljahr}/${sja.schuljahr + 1} - ${sja.abschnitt}. Abschnitt`" class="col-span-full" title="Schuljahresabschnitt" />
				<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" v-model="item.kuerzel" required placeholder="Kürzel" />
				<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" v-model="item.bezeichnung" placeholder="Bezeichnung" />
				<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" v-model="item.parallelitaet" placeholder="Parallelität" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button :disabled="loading || !hatKompetenzAendern" type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="create()" :disabled="loading || !hatKompetenzAendern || item.kuerzel.length === 0 || selectedSchuljahresabschnitt === undefined"> Klasse hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import { UvKlasse } from "@core/core/data/uv/UvKlasse";
	import type { UvSchuelergruppe } from "@core/core/data/uv/UvSchuelergruppe";
	import type { UvStundentafel } from "@core/core/data/uv/UvStundentafel";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { useUvState } from "@ui/states/UvState";

	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const actionError = ref('');
	watch(() => state.planungsabschnitt?.id, () => {
		show.value = false;
		actionError.value = '';
	});
	const show = ref<boolean>(false);
	const abschnittState = useAbschnittState();
	const schuleState = useSchuleState();
	const item = ref<UvKlasse>(new UvKlasse());
	const selectedSchuelergruppe = ref<UvSchuelergruppe | { id: null; bezeichnung: string }>({ id: null, bezeichnung: "Neue Schuelergruppe anlegen" });
	const selectedStundentafel = ref<UvStundentafel | { id: null; bezeichnung: string }>({ id: null, bezeichnung: "Keine Stundentafel" });
	const selectedSchuljahresabschnitt = ref<Schuljahresabschnitt | undefined>(undefined);
	const selectedJahrgaenge = ref<JahrgangsDaten[]>([]);

	const schuelergruppenItems = computed(() => {
		const list: Array<UvSchuelergruppe | { id: null; bezeichnung: string }> = [{ id: null, bezeichnung: "Neue Schuelergruppe anlegen" }];
		if (state.planungsabschnitt !== null) {
			list.push(...state.uvManager.schuelergruppeGetMengeByPlanungsabschnitt(state.planungsabschnitt));
		}
		return list;
	});

	const stundentafelItems = computed(() => {
		const list: Array<UvStundentafel | { id: null; bezeichnung: string }> = [{ id: null, bezeichnung: "Keine Stundentafel" }];
		list.push(...state.uvManager.stundentafelGetMengeAsList());
		return list;
	});

	const schuljahresabschnittItems = computed(() => [...abschnittState.alle]);

	const jahrgangItems = computed(() => [...state.uvManager.jahrgangsdatenGetMenge()]);

	const openModal = () => {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		actionError.value = '';
		item.value = new UvKlasse();
		selectedSchuelergruppe.value = schuelergruppenItems.value[0];
		selectedStundentafel.value = stundentafelItems.value[0];
		selectedSchuljahresabschnitt.value = schuleState.abschnitt;
		selectedJahrgaenge.value = [];
		show.value = true;
	};

	async function create() {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		actionError.value = '';
		try {
			const schuljahresabschnitt = selectedSchuljahresabschnitt.value;
			if (state.planungsabschnitt === null || state.planungsabschnitt.id === -1 || item.value.kuerzel.trim().length === 0 || schuljahresabschnitt === undefined) {
				return;
			}
			item.value.idPlanungsabschnitt = state.planungsabschnitt.id;
			item.value.idStundentafel = selectedStundentafel.value.id;
			item.value.idSchuljahresabschnitt = schuljahresabschnitt.id;
			const idSchuelergruppe = selectedSchuelergruppe.value.id;
			const idsJahrgaenge = selectedJahrgaenge.value.map(j => j.id);
			await state.createKlasse(item.value, idSchuelergruppe, idsJahrgaenge);
			item.value = new UvKlasse();
			selectedSchuelergruppe.value = schuelergruppenItems.value[0];
			selectedStundentafel.value = stundentafelItems.value[0];
			selectedSchuljahresabschnitt.value = schuleState.abschnitt;
			selectedJahrgaenge.value = [];
			show.value = false;
		} catch {
			const gruppe = schuelergruppenItems.value.find(g => g.id === item.value.idSchuelergruppe);
			if (gruppe !== undefined) {
				selectedSchuelergruppe.value = gruppe;
			}
			actionError.value = 'Die Aktion konnte nicht abgeschlossen werden. Die Eingaben bleiben erhalten. Bitte den aktuellen Bestand prüfen und erneut versuchen.';
		} finally {
			loading.value = false;
		}
	}

</script>
