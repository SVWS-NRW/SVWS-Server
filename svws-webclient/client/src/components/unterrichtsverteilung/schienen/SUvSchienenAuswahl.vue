<template>
	<s-uv-auswahl>
		<template #title>
			<h1 class="select-none">Schienen</h1>
		</template>
		<svws-ui-table :items="filteredItems" :columns clickable :clicked="auswahl" @update:clicked="onClicked" :selectable="hatKompetenzAendern" :unselectable v-model="selectedSchienen" count
			scroll scroll-into-view allow-arrow-key-selection :focus-switching-enabled :focus-help-visible :filter-open="true" :filtered="hasActiveFilter" :filter-reset="resetFilter">
			<template #search><svws-ui-text-input v-model="search" type="search" placeholder="Schiene suchen" removable /></template>

			<template #cell(nummer)="{ value }">
				{{ value }}
			</template>
			<template #cell(bezeichnung)="{ value }">
				{{ value ?? '-' }}
			</template>
			<template #actions v-if="hatKompetenzAendern">
				<s-uv-auswahl-loeschen :items="selectedSchienen" art="Schienen" :bezeichnung="item => String(item.nummer) + ' ' + (item.bezeichnung ?? '')" :loeschen="state.delSchiene" @deleted="onDeleted" />
				<template v-if="state.planungsabschnitt !== null && state.planungsabschnitt.id !== -1">
					<s-uv-schienen-neu-modal v-slot="{ openModal }">
						<svws-ui-button @click="openModal" type="icon" title="Schiene erstellen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
					</s-uv-schienen-neu-modal>
				</template>
			</template>
		</svws-ui-table>
	</s-uv-auswahl>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";

	import type { UvSchiene } from "@core/core/data/uv/UvSchiene";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useUvState } from "@ui/states/UvState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	import SUvAuswahl from "../SUvAuswahl.vue";
	import SUvAuswahlLoeschen from "../SUvAuswahlLoeschen.vue";

	const props = defineProps<{
		auswahl: UvSchiene | undefined;
		gotoSchiene: (schiene: UvSchiene | undefined) => void;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref('');
	const hasActiveFilter = computed(() => search.value.trim().length > 0);
	function resetFilter() {
		search.value = '';
	}
	const filteredItems = computed(() => [...listSchienen.value].filter(item => `${item.nummer} ${item.bezeichnung ?? ''}`
		.toLocaleLowerCase().includes(search.value.trim().toLocaleLowerCase())));
	const listSchienen = computed(() =>
		((state.planungsabschnitt !== null) && (state.planungsabschnitt.id !== -1))
			? state.uvManager.schieneGetMengeByPlanungsabschnitt(state.planungsabschnitt)
			: new ArrayList<UvSchiene>()
	);

	const unselectable = computed(
		() => new Set(state.uvManager.schieneGetMengeVerwendetBySchieneMenge(listSchienen.value))
	);

	const selectedSchienen = ref<UvSchiene[]>([]);

	function onClicked(schiene: UvSchiene | null) {
		props.gotoSchiene(schiene ?? undefined);
	}

	watch(() => state.planungsabschnitt?.id, () => {
		resetFilter();
		selectedSchienen.value = [];
	});

	function onDeleted(items: UvSchiene[]) {
		selectedSchienen.value = [];
		if (items.some(item => item.id === props.auswahl?.id)) {
			props.gotoSchiene(undefined);
		}
	}

	const columns = [
		{ key: "nummer", label: "Nummer", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
	];

</script>
