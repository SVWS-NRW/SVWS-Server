<template>
	<s-uv-auswahl>
		<template #title>
			<h1 class="select-none">Zeitraster</h1>
		</template>
		<svws-ui-table :items="filteredItems" :columns clickable :clicked="auswahl" @update:clicked="gotoZeitraster" :selectable="hatKompetenzAendern" v-model="selectedZeitraster" count
			scroll scroll-into-view allow-arrow-key-selection :focus-switching-enabled :focus-help-visible :filter-open="true" :filtered="hasActiveFilter" :filter-reset="resetFilter">
			<template #search><svws-ui-text-input v-model="search" type="search" placeholder="Zeitraster suchen" removable /></template>
			<template #cell(bezeichnung)="{ value }">
				{{ value }}
			</template>
			<template #cell(gueltigVon)="{ value }">
				{{ DateUtils.gibDatumGermanFormat(value) }}
			</template>
			<template #cell(gueltigBis)="{ value }">
				<span v-if="value">{{ DateUtils.gibDatumGermanFormat(value) }}</span>
				<span v-else class="text-ui-disabled">unbegrenzt</span>
			</template>
			<template #actions v-if="hatKompetenzAendern">
				<svws-ui-button type="icon" @click="openAddModal" title="Neues Zeitraster hinzufügen">
					<span class="icon i-ri-add-line" />
				</svws-ui-button>
				<svws-ui-button type="trash" @click="deleteSelected" :disabled="selectedZeitraster.length === 0" title="Ausgewählte Zeitraster löschen">
					<span class="icon i-ri-delete-bin-line" />
				</svws-ui-button>
			</template>
		</svws-ui-table>

		<svws-ui-modal v-model:show="showAddModal" size="small">
			<template #modalTitle>Neues Zeitraster</template>
			<template #modalContent>
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" v-model="newZeitraster.bezeichnung" span="full" required />
					<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
						<svws-ui-text-input placeholder="Gültig von" v-model="newZeitraster.gueltigVon" type="date" required />
						<svws-ui-text-input placeholder="Gültig bis (optional)" v-model="newZeitraster.gueltigBis" type="date" />
					</div>
				</svws-ui-input-wrapper>
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="showAddModal = false">Abbrechen</svws-ui-button>
				<svws-ui-button type="primary" @click="addNewZeitraster" :disabled="!newZeitraster.bezeichnung || !newZeitraster.gueltigVon">Hinzufügen</svws-ui-button>
			</template>
		</svws-ui-modal>
	</s-uv-auswahl>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";

	import type { UvZeitraster } from "@core/core/data/uv/UvZeitraster";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { useUvState } from "@ui/states/UvState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	import SUvAuswahl from "../../SUvAuswahl.vue";

	const props = defineProps<{
		auswahl: UvZeitraster | undefined;
		gotoZeitraster: (zeitraster: UvZeitraster | undefined) => Promise<void>;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref('');
	const hasActiveFilter = computed(() => search.value.trim().length > 0);
	function resetFilter() {
		search.value = '';
	}
	const filteredItems = computed(() => [...listZeitraster.value].filter(item => (item.bezeichnung ?? '')
		.toLocaleLowerCase().includes(search.value.trim().toLocaleLowerCase())));
	watch(() => state.planungsabschnitt?.id, resetFilter);


	const listZeitraster = computed<UvZeitraster[]>(() => [...state.uvManager.zeitrasterGetMengeAsList()]);
	const selectedZeitraster = ref<UvZeitraster[]>([]);
	const showAddModal = ref(false);

	const newZeitraster = ref<Partial<UvZeitraster>>({
		bezeichnung: "",
		gueltigVon: new Date().toISOString().slice(0, 10),
		gueltigBis: null,
	});


	const columns = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: 'asc' },
		{ key: "gueltigVon", label: "Gültig von", sortable: true },
		{ key: "gueltigBis", label: "Gültig bis", sortable: true },
	];

	function openAddModal() {
		newZeitraster.value = {
			bezeichnung: "",
			gueltigVon: new Date().toISOString().slice(0, 10),
			gueltigBis: null,
		};
		showAddModal.value = true;
	}

	async function addNewZeitraster() {
		if ((newZeitraster.value.bezeichnung === null)) {
			return;
		}
		const neu = await state.addZeitraster(newZeitraster.value);
		showAddModal.value = false;
		selectedZeitraster.value = [neu];
	}

	async function deleteSelected() {
		if (selectedZeitraster.value.length === 0) {
			return;
		}
		const ids = selectedZeitraster.value.map(z => z.id);
		await state.deleteZeitraster(ids);
		selectedZeitraster.value = [];
	}

</script>
