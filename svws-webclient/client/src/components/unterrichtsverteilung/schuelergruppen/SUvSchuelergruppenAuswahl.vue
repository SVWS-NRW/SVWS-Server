<template>
	<s-uv-auswahl>
		<template #title>
			<h1 class="select-none">Schülergruppen</h1>
		</template>
		<template #header>
			<p v-if="unselectable.size > 0 && hatKompetenzAendern" class="text-sm text-ui-secondary">Verwendete Schülergruppen können nicht gelöscht werden.</p>
		</template>
		<svws-ui-table :items="filteredItems" :columns clickable :clicked="clickedItem" @update:clicked="onClicked" :selectable="hatKompetenzAendern" :unselectable v-model="selectedSchuelergruppen" count scroll
			scroll-into-view
			allow-arrow-key-selection :focus-switching-enabled :focus-help-visible
			v-model:sort-by-and-order="sortByAndOrder" :filter-open="true" :filtered="hasActiveFilter" :filter-reset="resetFilter">
			<template #search>
				<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Schülergruppe" removable />
			</template>
			<template #noData>
				{{ search.trim().length > 0 ? 'Keine Treffer für diese Suche.' : 'Noch keine Schülergruppen vorhanden.' }}
			</template>
			<template #cell(bezeichnung)="{ value }">
				{{ value }}
			</template>
			<template #actions v-if="hatKompetenzAendern">
				<s-uv-auswahl-loeschen :items="selectedSchuelergruppen" art="Schülergruppen" :bezeichnung="item => item.bezeichnung" :loeschen="deleteItems" @deleted="onDeleted" />
				<template v-if="state.planungsabschnitt !== null && state.planungsabschnitt.id !== -1">
					<s-uv-schuelergruppen-neu-modal v-slot="{ openModal }">
						<svws-ui-button @click="openModal" type="icon" title="Schülergruppe erstellen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
					</s-uv-schuelergruppen-neu-modal>
				</template>
			</template>
		</svws-ui-table>
	</s-uv-auswahl>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";

	import type { UvSchuelergruppe } from "@core/core/data/uv/UvSchuelergruppe";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useUvState } from "@ui/states/UvState";
	import type { SortByAndOrder } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	import SUvAuswahl from "../SUvAuswahl.vue";
	import SUvAuswahlLoeschen from "../SUvAuswahlLoeschen.vue";

	const props = defineProps<{
		auswahl: UvSchuelergruppe | undefined;
		gotoSchuelergruppe: (schuelergruppe: UvSchuelergruppe | undefined) => void;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref('');
	const hasActiveFilter = computed(() => search.value.trim().length > 0);
	const sortByAndOrder = ref<SortByAndOrder>({ key: 'bezeichnung', order: true });
	function resetFilter() {
		search.value = '';
	}

	const listSchuelergruppen = computed(() =>
		((state.planungsabschnitt !== null) && (state.planungsabschnitt.id !== -1))
			? state.uvManager.schuelergruppeGetMengeByPlanungsabschnitt(state.planungsabschnitt)
			: new ArrayList<UvSchuelergruppe>()
	);

	const unselectable = computed(() => new Set(state.uvManager.schuelergruppeGetMengeVerwendetBySchuelergruppeMenge(listSchuelergruppen.value))
	);

	const selectedIds = ref<number[]>([]);
	const selectedSchuelergruppen = computed({
		get: () => tableItems.value.filter(item => selectedIds.value.includes(item.id)),
		set: (items: UvSchuelergruppe[]) => {
			selectedIds.value = items.map(item => item.id);
		},
	});

	function onClicked(schuelergruppe: UvSchuelergruppe | null) {
		props.gotoSchuelergruppe([...listSchuelergruppen.value].find(item => item.id === schuelergruppe?.id));
	}

	watch(() => state.planungsabschnitt?.id, () => {
		resetFilter();
		selectedSchuelergruppen.value = [];
	});

	const tableItems = computed(() => [...listSchuelergruppen.value]);

	const clickedItem = computed(() => tableItems.value.find(item => item.id === props.auswahl?.id) ?? null);
	const filteredItems = computed(() => tableItems.value.filter(item => item.bezeichnung.toLocaleLowerCase().includes(search.value.trim().toLocaleLowerCase())));
	async function deleteItems(items: UvSchuelergruppe[]) {
		const ids = new Set(items.map(item => item.id));
		await state.delSchuelergruppe([...listSchuelergruppen.value].filter(item => ids.has(item.id)));
	}

	function onDeleted(items: UvSchuelergruppe[]) {
		selectedSchuelergruppen.value = [];
		if (items.some(item => item.id === props.auswahl?.id)) {
			props.gotoSchuelergruppe(undefined);
		}
	}

	const columns = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: 'asc' },
	];

</script>
