<template>
	<slot :open-modal />
	<svws-ui-modal :auto-close="!loading" :close-in-title="!loading" v-model:show="show" class="hidden" size="medium">
		<template #modalTitle>Fächer hinzufügen</template>
		<template #modalContent>
			<svws-ui-notification v-if="actionError" type="error" class="mb-3">{{ actionError }}</svws-ui-notification>
			<svws-ui-table :items="listFaecher" :columns selectable scroll v-model="selectedFaecher" count :unselectable="unselectableFaecher">
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Kürzel oder Bezeichnung suchen" removable />
				</template>
				<template #cell(importiert)="{ rowData }">
					<span v-if="unselectableFaecher.has(rowData)" class="text-sm text-ui-secondary">Bereits importiert</span>
				</template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button :disabled="loading || !hatKompetenzAendern" type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary"
				@click="importer()"
				:disabled="loading || !hatKompetenzAendern || selectedFaecher.length === 0">
				Fächer hinzufügen
			</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import type { FachDaten } from "@core/core/data/fach/FachDaten";
	import { useUvState } from "@ui/states/UvState";
	import type { DataTableColumn } from "@ui/types";
	import { FaecherListeManager } from "@ui/ui/manager/kataloge/FaecherListeManager";

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
		search.value = '';
		show.value = true;
	};

	const selectedFaecher = ref<FachDaten[]>([]);
	const search = ref('');
	const sichtbareFaecher = computed(() => [...state.uvManager.fachdatenGetMenge()]
		.filter(fach => fach.istSichtbar)
		.sort((a, b) => FaecherListeManager.comparator.compare(a, b)));
	const listFaecher = computed(() => {
		const suchtext = search.value.trim().toLocaleLowerCase();
		return sichtbareFaecher.value.filter(fach => `${fach.kuerzel} ${fach.bezeichnung}`.toLocaleLowerCase().includes(suchtext));
	});

	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
		{ key: "importiert", label: "Status", sortable: false },
	];

	const unselectableFaecher = computed(() => {
		const set = new Set<FachDaten>();
		for (const fach of sichtbareFaecher.value) {
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
			await state.grunddaten.addFaecher(selectedFaecher.value);
			selectedFaecher.value = [];
			show.value = false;
		} catch (error) {
			actionError.value = error instanceof Error ? error.message : 'Die Aktion konnte nicht abgeschlossen werden. Bitte den aktuellen Bestand prüfen und erneut versuchen.';
		} finally {
			loading.value = false;
		}
	}

</script>
