<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden" :auto-close="!loading" :close-in-title="!loading">
		<template #modalTitle>Schülergruppe erstellen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input :disabled="loading" v-model="item.bezeichnung" required placeholder="Bezeichnung" class="col-span-full" />
				<svws-ui-multi-select :disabled="loading" v-model="selectedJahrgaenge" :items="jahrgangItems" :item-text="item => item.kuerzel ?? ''" class="col-span-full" title="Erlaubte Jahrgänge" />
			</svws-ui-input-wrapper>
			<svws-ui-notification v-if="error" type="error" class="mt-4">{{ error }}</svws-ui-notification>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false" :disabled="loading"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="create()" :disabled="!isValid || loading" :is-loading="loading"> Schülergruppe erstellen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import { UvSchuelergruppe } from "@core/core/data/uv/UvSchuelergruppe";
	import { useUvState } from "@ui/states/UvState";


	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const error = ref('');
	const isValid = computed(() => hatKompetenzAendern.value && item.value.bezeichnung.trim().length > 0);
	const show = ref<boolean>(false);
	const item = ref<UvSchuelergruppe>(new UvSchuelergruppe());
	const selectedJahrgaenge = ref<JahrgangsDaten[]>([]);

	const jahrgangItems = computed(() => [...state.uvManager.jahrgangsdatenGetMenge()]);

	watch(() => state.planungsabschnitt?.id, () => {
		show.value = false;
	});

	const openModal = () => {
		if (!hatKompetenzAendern.value || loading.value) {
			return;
		}
		error.value = '';
		item.value = new UvSchuelergruppe();
		selectedJahrgaenge.value = [];
		show.value = true;
	};

	async function create() {
		if (!isValid.value || loading.value || state.planungsabschnitt === null || state.planungsabschnitt.id === -1) {
			return;
		}
		item.value.idPlanungsabschnitt = state.planungsabschnitt.id;
		item.value.idsJahrgaengeErlaubt.clear();
		for (const j of selectedJahrgaenge.value) {
			item.value.idsJahrgaengeErlaubt.add(j.id);
		}
		item.value.bezeichnung = item.value.bezeichnung.trim();
		loading.value = true;
		error.value = '';
		try {
			await state.createSchuelergruppe(item.value);
			show.value = false;
		} catch {
			error.value = 'Die Schülergruppe konnte nicht erstellt werden. Die Eingaben bleiben erhalten. Bitte erneut versuchen.';
		} finally {
			loading.value = false;
		}
	}

</script>
