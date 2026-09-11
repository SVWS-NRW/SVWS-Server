<template>
	<slot :open-modal />
	<svws-ui-modal :auto-close="!loading" :close-in-title="!loading" v-model:show="show" class="hidden">
		<template #modalTitle>Schiene hinzufügen</template>
		<template #modalContent>
			<svws-ui-notification v-if="actionError" type="error" class="mb-3">{{ actionError }}</svws-ui-notification>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number :disabled="loading || !hatKompetenzAendern" v-model="item.nummer" required placeholder="Nummer" :min="1" />
				<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" placeholder="Bezeichnung" v-model="item.bezeichnung" />
				<svws-ui-multi-select :disabled="loading || !hatKompetenzAendern" v-model="selectedJahrgaenge" :items="jahrgangItems" :item-text="item => item.kuerzel ?? ''" class="col-span-full" title="Jahrgänge" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button :disabled="loading || !hatKompetenzAendern" type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="create()" :disabled="loading || !hatKompetenzAendern || !isValid"> Schiene hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import { UvSchiene } from "@core/core/data/uv/UvSchiene";
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

	const item = ref<UvSchiene>(new UvSchiene());
	const selectedJahrgaenge = ref<JahrgangsDaten[]>([]);

	const jahrgangItems = computed(() => [...state.uvManager.jahrgangsdatenGetMenge()]);

	const isValid = computed(() => {
		return state.planungsabschnitt !== null && state.uvManager.schieneGetByPlanungsabschnittAndNummer(state.planungsabschnitt, item.value.nummer) === null;
	});

	const openModal = () => {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		actionError.value = '';
		item.value = new UvSchiene();
		item.value.nummer = 1;
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
			if (state.planungsabschnitt === null) {
				return;
			}
			item.value.idPlanungsabschnitt = state.planungsabschnitt.id;
			item.value.idsJahrgaengeErlaubt.clear();
			for (const j of selectedJahrgaenge.value) {
				item.value.idsJahrgaengeErlaubt.add(j.id);
			}
			await state.createSchiene(item.value);
			item.value = new UvSchiene();
			selectedJahrgaenge.value = [];
			show.value = false;
		} catch {
			actionError.value = 'Die Aktion konnte nicht abgeschlossen werden. Die Eingaben bleiben erhalten. Bitte den aktuellen Bestand prüfen und erneut versuchen.';
		} finally {
			loading.value = false;
		}
	}

</script>
