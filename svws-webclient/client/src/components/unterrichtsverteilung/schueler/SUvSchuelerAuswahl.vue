<template>
	<s-uv-auswahl>
		<Teleport to=".svws-sub-nav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<s-uv-schueler-import-modal v-slot="{ openModal }">
					<svws-ui-button type="transparent" @click.stop="openModal" title="Importiere Schüler aus Schuljahresabschnitt" class="text-ui-100 subNavigationFocusField">
						<span class="icon-sm i-ri-sparkling-line" /> Importieren
					</svws-ui-button>
				</s-uv-schueler-import-modal>
			</svws-ui-sub-nav>
		</Teleport>
		<template #title>
			<h1 class="select-none">Schüler</h1>
		</template>
		<svws-ui-table :items="filteredItems" :columns clickable :clicked="auswahl" @update:clicked="onClicked" :selectable="hatKompetenzAendern" v-model="selectedSchueler" count
			scroll scroll-into-view allow-arrow-key-selection :focus-switching-enabled :focus-help-visible :filter-open="true" :filtered="hasActiveFilter" :filter-reset="resetFilter">
			<template #search><svws-ui-text-input v-model="search" type="search" placeholder="Schüler suchen" removable /></template>

			<template #cell(nachname)="{ rowData }">
				<div class="py-1 min-w-0">
					<div class="font-medium break-words">{{ rowData.daten.nachname }}</div>
					<div class="text-sm text-ui-secondary break-words">{{ rowData.daten.vorname }}</div>
				</div>
			</template>
			<template #cell(jahrgang)="{ rowData }">
				{{ presenter.jahrgangKuerzel(rowData.idJahrgang) }}
			</template>
			<template #cell(klasse)="{ rowData }">
				{{ presenter.klasseKuerzel(rowData.idKlasse) }}
			</template>
			<template #actions v-if="hatKompetenzAendern">
				<svws-ui-button @click="state.delSchueler(selectedSchueler)" type="trash" :disabled="!selectedSchueler.length" />
			</template>
		</svws-ui-table>
	</s-uv-auswahl>
</template>

<script setup lang="ts">
	import { computed, onMounted, ref, watch } from "vue";

	import type { UvPlanungsabschnittSchueler } from "@core/core/data/uv/UvPlanungsabschnittSchueler";
	import { useUvState } from "@ui/states/UvState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	import SUvAuswahl from "../SUvAuswahl.vue";
	import { useUvPresenter } from "../UvPresenter";

	const props = defineProps<{
		auswahl: UvPlanungsabschnittSchueler | undefined;
		gotoUvSchueler: (schueler: UvPlanungsabschnittSchueler | undefined) => void;
	}>();
	const state = useUvState();
	const presenter = useUvPresenter(state.uvManager);
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref('');
	const hasActiveFilter = computed(() => search.value.trim().length > 0);
	function resetFilter() {
		search.value = '';
	}
	const filteredItems = computed(() => [...listSchueler.value].filter(item => `${item.daten.nachname} ${item.daten.vorname} ${state.uvManager.jahrgangsdatenGetById(item.idJahrgang).kuerzel ?? ""}`.toLocaleLowerCase().includes(search.value.trim().toLocaleLowerCase())));
	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const listSchueler = computed(() => {
		if (state.planungsabschnitt === null || state.planungsabschnitt.id === -1) {
			return [];
		}
		return [...state.uvManager.planungsabschnittSchuelerGetMengeByPlanungsabschnitt(state.planungsabschnitt)];
	});

	const selectedSchueler = ref<UvPlanungsabschnittSchueler[]>([]);

	function onClicked(schueler: UvPlanungsabschnittSchueler | null) {
		props.gotoUvSchueler(schueler ?? undefined);
	}

	watch(() => state.planungsabschnitt?.id, () => {
		resetFilter();
		selectedSchueler.value = [];
	});

	const columns = [
		{ key: "nachname", label: "Name", sortable: true, defaultSort: "asc" as const },
		{ key: "jahrgang", label: "JG", tooltip: "Jahrgang", sortable: true, span: 0.4 },
		{ key: "klasse", label: "Klasse", sortable: true, span: 0.5 },
	];

</script>
