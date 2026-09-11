<template>
	<s-uv-auswahl>
		<template #title>
			<h1 class="select-none">Räume</h1>
		</template>
		<svws-ui-table :items="filteredItems" :columns clickable @update:clicked="gotoRaum" :selectable="hatKompetenzAendern" v-model="selectedRaeume" count :clicked="auswahl"
			scroll scroll-into-view allow-arrow-key-selection :focus-switching-enabled :focus-help-visible :filter-open="true" :filtered="hasActiveFilter" :filter-reset="resetFilter">
			<template #search><svws-ui-text-input v-model="search" type="search" placeholder="Raum suchen" removable /></template>
			<template #cell(kuerzel)="{ rowData, value }">
				<span :class="{'font-bold': auswahl !== undefined && rowData?.id === auswahl.id}">{{ value }}</span>
			</template>
			<template #actions v-if="hatKompetenzAendern">
				<s-uv-auswahl-loeschen :items="selectedRaeume" art="Räume" :bezeichnung="item => item.kuerzel" :loeschen="state.delRaeume" @deleted="onDeleted" />
				<s-uv-grunddaten-raeume-import-modal v-slot="{ openModal }">
					<svws-ui-button type="icon" @click="openModal()">
						<span class="icon-sm i-ri-download-2-line" />
					</svws-ui-button>
				</s-uv-grunddaten-raeume-import-modal>
				<s-uv-grunddaten-raeume-neu-modal v-slot="{ openModal }">
					<svws-ui-button @click="openModal" type="icon" title="Raum neu erstellen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
				</s-uv-grunddaten-raeume-neu-modal>
			</template>
		</svws-ui-table>
	</s-uv-auswahl>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";

	import type { UvRaum } from "@core/core/data/uv/UvRaum";
	import { useUvState } from "@ui/states/UvState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	import SUvAuswahl from "../../SUvAuswahl.vue";
	import SUvAuswahlLoeschen from "../../SUvAuswahlLoeschen.vue";

	const props = defineProps<{
		auswahl: UvRaum | undefined;
		gotoRaum: (raum: UvRaum | undefined) => void;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref('');
	const hasActiveFilter = computed(() => search.value.trim().length > 0);
	function resetFilter() {
		search.value = '';
	}
	const filteredItems = computed(() => [...state.uvManager.raumGetMengeAsList()].filter(item => `${item.kuerzel} ${item.beschreibung ?? ''}`
		.toLocaleLowerCase().includes(search.value.trim().toLocaleLowerCase())));
	watch(() => state.planungsabschnitt?.id, resetFilter);


	const selectedRaeume = ref<UvRaum[]>([]);


	function onDeleted(items: UvRaum[]) {
		selectedRaeume.value = [];
		if (items.some(item => item.id === props.auswahl?.id)) {
			props.gotoRaum(undefined);
		}
	}

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc', span: 0.7 },
		{ key: "beschreibung", label: "Beschreibung", sortable: true, defaultSort: 'asc' },
	];

</script>
