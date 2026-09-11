<template>
	<s-uv-auswahl>
		<template #title>
			<h1 class="select-none">Stundentafeln</h1>
		</template>
		<svws-ui-table :items="filteredItems" :columns clickable :clicked="auswahl" @update:clicked="gotoStundentafel" :selectable="hatKompetenzAendern" v-model="selectedStundentafeln" count
			scroll scroll-into-view allow-arrow-key-selection :focus-switching-enabled :focus-help-visible :filter-open="true" :filtered="hasActiveFilter" :filter-reset="resetFilter">
			<template #search><svws-ui-text-input v-model="search" type="search" placeholder="Stundentafel suchen" removable /></template>
			<template #cell(jahrgangId)="{ rowData }">
				{{ state.uvManager.jahrgangsdatenGetByStundentafel(rowData).kuerzel }}
			</template>
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
				<s-uv-auswahl-loeschen :items="selectedStundentafeln" art="Stundentafeln" :bezeichnung="item => item.bezeichnung" :loeschen="state.delStundentafel" @deleted="onDeleted" />
				<s-uv-grunddaten-stundentafeln-neu-modal v-slot="{ openModal }">
					<svws-ui-button @click="openModal" type="icon" title="Stundentafel neu erstellen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
				</s-uv-grunddaten-stundentafeln-neu-modal>
			</template>
		</svws-ui-table>
	</s-uv-auswahl>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";

	import type { UvStundentafel } from "@core/core/data/uv/UvStundentafel";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { useUvState } from "@ui/states/UvState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	import SUvAuswahl from "../../SUvAuswahl.vue";
	import SUvAuswahlLoeschen from "../../SUvAuswahlLoeschen.vue";

	import SUvGrunddatenStundentafelnNeuModal from "./SUvGrunddatenStundentafelnNeuModal.vue";

	const props = defineProps<{
		auswahl: UvStundentafel | undefined;
		gotoStundentafel: (stundentafel: UvStundentafel | undefined) => void;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref('');
	const hasActiveFilter = computed(() => search.value.trim().length > 0);
	function resetFilter() {
		search.value = '';
	}
	const filteredItems = computed(() => [...listStundentafeln.value].filter(item => `${state.uvManager.jahrgangsdatenGetByStundentafel(item).kuerzel ?? ''} ${item.bezeichnung}`
		.toLocaleLowerCase().includes(search.value.trim().toLocaleLowerCase())));
	watch(() => state.planungsabschnitt?.id, resetFilter);


	const listStundentafeln = computed(() => [...state.uvManager.stundentafelGetMengeAsList()]);

	const selectedStundentafeln = ref<UvStundentafel[]>([]);


	function onDeleted(items: UvStundentafel[]) {
		selectedStundentafeln.value = [];
		if (items.some(item => item.id === props.auswahl?.id)) {
			props.gotoStundentafel(undefined);
		}
	}

	const columns = [
		{ key: "jahrgangId", label: "JG", tooltip: "Jahrgang", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
		{ key: "gueltigVon", label: "Von", tooltip: "Gültig von", sortable: true },
		{ key: "gueltigBis", label: "Bis", tooltip: "Gültig bis", sortable: true },
	];

</script>
