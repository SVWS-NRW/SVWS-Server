<template>
	<s-uv-auswahl>
		<template #title>
			<h1 class="select-none">Klassen</h1>
		</template>
		<svws-ui-table :items="filteredItems" :columns clickable :clicked="auswahl" @update:clicked="onClicked" :selectable="hatKompetenzAendern" :unselectable v-model="selectedKlassen" count
			scroll scroll-into-view allow-arrow-key-selection :focus-switching-enabled :focus-help-visible :filter-open="true" :filtered="hasActiveFilter" :filter-reset="resetFilter">
			<template #search><svws-ui-text-input v-model="search" type="search" placeholder="Klasse suchen" removable /></template>

			<template #cell(kuerzel)="{ value }">
				{{ value }}
			</template>
			<template #cell(bezeichnung)="{ value }">
				{{ value ?? '-' }}
			</template>
			<template #cell(parallelitaet)="{ value }">
				{{ value }}
			</template>
			<template #actions v-if="hatKompetenzAendern">
				<s-uv-auswahl-loeschen :items="selectedKlassen" art="Klassen" :bezeichnung="item => item.kuerzel" :loeschen="state.delKlasse" @deleted="onDeleted" />
				<template v-if="state.planungsabschnitt !== null && state.planungsabschnitt.id !== -1">
					<s-uv-klassen-neu-modal v-slot="{ openModal }">
						<svws-ui-button @click="openModal" type="icon" title="Klasse erstellen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
					</s-uv-klassen-neu-modal>
				</template>
			</template>
		</svws-ui-table>
	</s-uv-auswahl>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";

	import type { UvKlasse } from "@core/core/data/uv/UvKlasse";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useUvState } from "@ui/states/UvState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	import SUvAuswahl from "../SUvAuswahl.vue";
	import SUvAuswahlLoeschen from "../SUvAuswahlLoeschen.vue";

	const props = defineProps<{
		auswahl: UvKlasse | undefined;
		gotoKlasse: (klasse: UvKlasse | undefined) => void;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref('');
	const hasActiveFilter = computed(() => search.value.trim().length > 0);
	function resetFilter() {
		search.value = '';
	}
	const filteredItems = computed(() => [...listKlassen.value].filter(item => `${item.kuerzel} ${item.bezeichnung ?? ''} ${item.parallelitaet}`
		.toLocaleLowerCase().includes(search.value.trim().toLocaleLowerCase())));
	const listKlassen = computed(() =>
		((state.planungsabschnitt !== null) && (state.planungsabschnitt.id !== -1))
			? state.uvManager.klasseGetMengeByPlanungsabschnitt(state.planungsabschnitt)
			: new ArrayList<UvKlasse>()
	);

	const unselectable = computed(
		() => new Set(state.uvManager.klasseGetMengeVerwendetByKlasseMenge(listKlassen.value))
	);

	const selectedKlassen = ref<UvKlasse[]>([]);

	function onClicked(klasse: UvKlasse | null) {
		props.gotoKlasse(klasse ?? undefined);
	}

	watch(() => state.planungsabschnitt?.id, () => {
		resetFilter();
		selectedKlassen.value = [];
	});

	function onDeleted(items: UvKlasse[]) {
		selectedKlassen.value = [];
		if (items.some(item => item.id === props.auswahl?.id)) {
			props.gotoKlasse(undefined);
		}
	}

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
		{ key: "parallelitaet", label: "Parallelität", sortable: true },
	];

</script>
