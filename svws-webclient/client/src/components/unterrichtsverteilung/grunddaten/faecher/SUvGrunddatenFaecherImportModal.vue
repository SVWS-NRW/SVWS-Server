<template>
	<slot :open-modal />
	<svws-ui-modal :auto-close="!loading" :close-in-title="!loading" v-model:show="show" class="hidden" size="medium">
		<template #modalTitle>Fächer hinzufügen</template>
		<template #modalContent>
			<svws-ui-notification v-if="actionError" type="error" class="mb-3">{{ actionError }}</svws-ui-notification>
			<svws-ui-table :items="listFaecher" :columns selectable scroll v-model="selectedFaecher" count :unselectable="unselectableFaecher">
				<template #filter />
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button :disabled="loading || !hatKompetenzAendern" type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="loading || !hatKompetenzAendern || selectedFaecher.length === 0"> Fächer hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import type { FachDaten } from "@core/core/data/fach/FachDaten";
	import { useUvState } from "@ui/states/UvState";
	import type { DataTableColumn } from "@ui/types";

	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const actionError = ref('');
	watch(() => state.planungsabschnitt?.id, () => {
		show.value = false;
		actionError.value = '';
	});

	const show = ref<boolean>(false);

	const openModal = () => {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		actionError.value = '';
		show.value = true;
	};

	const selectedFaecher = ref<FachDaten[]>([]);
	const listFaecher = computed(() => state.uvManager.fachdatenGetMenge());

	const columns = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: 'asc' },
	] as DataTableColumn[];

	const unselectableFaecher = computed(() => {
		const set = new Set<FachDaten>();
		for (const fach of listFaecher.value) {
			const uvFaecher = state.uvManager.fachGetMengeByFachdaten(fach);
			for (const uvFach of uvFaecher) {
				if (uvFach.gueltigBis === null) {
					set.add(fach);
					break;
				}
			}
		}
		return set;
	});

	async function importer() {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		actionError.value = '';
		try {
			await state.addFaecher(selectedFaecher.value);
			selectedFaecher.value = [];
			show.value = false;
		} catch {
			actionError.value = 'Die Aktion konnte nicht abgeschlossen werden. Die Eingaben bleiben erhalten. Bitte den aktuellen Bestand prüfen und erneut versuchen.';
		} finally {
			loading.value = false;
		}
	}

</script>
