<template>
	<s-uv-auswahl>
		<template #title>
			<h1 class="select-none">UV-Fächer</h1>
		</template>
		<svws-ui-table :items="filteredItems" :columns clickable @update:clicked="gotoFach" :selectable="hatKompetenzAendern" v-model="selectedFaecher" count :clicked="auswahl" :unselectable
			scroll scroll-into-view allow-arrow-key-selection :focus-switching-enabled :focus-help-visible :filter-open="true" :filtered="hasActiveFilter" :filter-reset="resetFilter">
			<template #search><svws-ui-text-input v-model="search" type="search" placeholder="Fach suchen" removable /></template>
			<template #cell(idFach)="{ rowData }">
				<span><svws-ui-badge size="tiny" type="highlight" v-if="konfliktMitFach?.value !== null && konfliktMitFach.value.id === rowData.id"><span class="icon i-ri-alert-line icon-ui-onwarning" /></svws-ui-badge> {{ state.uvManager.fachdatenGetByFach(rowData).bezeichnung }}</span>
			</template>
			<template #cell(gueltigVon)="{ value }">
				{{ DateUtils.gibDatumGermanFormat(value) }}
			</template>
			<template #cell(gueltigBis)="{ value }">
				<span v-if="(value !== null && value !== '')">{{ DateUtils.gibDatumGermanFormat(value) }}</span>
				<span v-else class="text-ui-disabled">unbegrenzt</span>
			</template>
			<template #actions v-if="hatKompetenzAendern">
				<s-uv-grunddaten-faecher-import-modal v-slot="{ openModal }">
					<svws-ui-button @click="openModal" type="icon" title="Fächer importieren"> <span class="icon i-ri-upload-2-line" /> </svws-ui-button>
				</s-uv-grunddaten-faecher-import-modal>
				<svws-ui-button @click="state.delFaecher(selectedFaecher)" type="trash" :disabled="!selectedFaecher.length" />
			</template>
		</svws-ui-table>
	</s-uv-auswahl>
</template>

<script setup lang="ts">
	import type { WritableComputedRef } from "vue";
	import { computed, ref, watch } from "vue";

	import type { UvFach } from "@core/core/data/uv/UvFach";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { useUvState } from "@ui/states/UvState";
	import type { DataTableColumn } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	import SUvAuswahl from "../../SUvAuswahl.vue";
	import SUvGrunddatenFaecherImportModal from "~/components/unterrichtsverteilung/grunddaten/faecher/SUvGrunddatenFaecherImportModal.vue";

	const props = defineProps<{
		auswahl: UvFach | undefined;
		konfliktMitFach: WritableComputedRef<UvFach | null>;
		gotoFach: (fach: UvFach | undefined) => void;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref('');
	const hasActiveFilter = computed(() => search.value.trim().length > 0);
	function resetFilter() {
		search.value = '';
	}
	const filteredItems = computed(() => [...state.uvManager.fachGetMengeAsList()].filter(item => `${state.uvManager.fachdatenGetByFach(item).kuerzel} ${state.uvManager.fachdatenGetByFach(item).bezeichnung}`.toLocaleLowerCase().includes(search.value.trim().toLocaleLowerCase())));
	watch(() => state.planungsabschnitt?.id, resetFilter);


	const selectedFaecher = ref<UvFach[]>([]);
	const unselectable = computed<Set<UvFach>>(() => new Set(state.uvManager.fachGetMengeVerwendetInStundentafel()));

	const columns = [
		{ key: "idFach", label: "Fach", sortable: true, defaultSort: 'asc' },
		{ key: "gueltigVon", label: "Gültig von", sortable: true, span: 0.5, align: 'center' },
		{ key: "gueltigBis", label: "Gültig bis", sortable: true, span: 0.5, align: 'center' },
	] as DataTableColumn[];

</script>
