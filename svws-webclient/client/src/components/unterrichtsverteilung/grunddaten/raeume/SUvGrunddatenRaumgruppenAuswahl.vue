<template>
	<s-uv-auswahl>
		<template #title>
			<h1 class="select-none">Raumgruppen</h1>
		</template>
		<svws-ui-table :items="filteredItems" :columns clickable @update:clicked="gotoRaumgruppe" :selectable="hatKompetenzAendern" v-model="selectedRaumgruppen" count :clicked="auswahl"
			scroll scroll-into-view allow-arrow-key-selection :focus-switching-enabled :focus-help-visible :filter-open="true" :filtered="hasActiveFilter" :filter-reset="resetFilter">
			<template #search><svws-ui-text-input v-model="search" type="search" placeholder="Raumgruppe suchen" removable /></template>
			<template #cell(bezeichnung)="{ rowData, value }">
				<span :class="{'font-bold': auswahl !== undefined && rowData?.id === auswahl.id}">{{ value }}</span>
			</template>
			<template #actions v-if="hatKompetenzAendern">
				<s-uv-auswahl-loeschen :items="selectedRaumgruppen" art="Raumgruppen" :bezeichnung="item => item.bezeichnung" :loeschen="state.delRaumgruppen" @deleted="onDeleted" />
				<s-uv-grunddaten-raumgruppen-neu-modal v-slot="{ openModal }">
					<svws-ui-button @click="openModal" type="icon" title="Raumgruppe neu erstellen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
				</s-uv-grunddaten-raumgruppen-neu-modal>
			</template>
		</svws-ui-table>
	</s-uv-auswahl>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";

	import type { UvRaumgruppe } from "@core/core/data/uv/UvRaumgruppe";
	import { useUvState } from "@ui/states/UvState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	import SUvAuswahl from "../../SUvAuswahl.vue";
	import SUvAuswahlLoeschen from "../../SUvAuswahlLoeschen.vue";

	import SUvGrunddatenRaumgruppenNeuModal from "./SUvGrunddatenRaumgruppenNeuModal.vue";

	const props = defineProps<{
		auswahl: UvRaumgruppe | undefined;
		gotoRaumgruppe: (raumgruppe: UvRaumgruppe | undefined) => void;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref('');
	const hasActiveFilter = computed(() => search.value.trim().length > 0);
	function resetFilter() {
		search.value = '';
	}
	const filteredItems = computed(() => [...state.uvManager.raumgruppeGetMengeAsList()].filter(item => `${item.bezeichnung} ${item.beschreibung ?? ''}`
		.toLocaleLowerCase().includes(search.value.trim().toLocaleLowerCase())));
	watch(() => state.planungsabschnitt?.id, resetFilter);


	const selectedRaumgruppen = ref<UvRaumgruppe[]>([]);


	function onDeleted(items: UvRaumgruppe[]) {
		selectedRaumgruppen.value = [];
		if (items.some(item => item.id === props.auswahl?.id)) {
			props.gotoRaumgruppe(undefined);
		}
	}

	const columns = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: 'asc', span: 1 },
		{ key: "beschreibung", label: "Beschreibung", sortable: true, defaultSort: 'asc' },
	];

</script>
