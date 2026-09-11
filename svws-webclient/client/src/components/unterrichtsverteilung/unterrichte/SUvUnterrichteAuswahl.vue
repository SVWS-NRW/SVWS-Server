<template>
	<s-uv-auswahl>
		<template #title>
			<h1 class="select-none">Unterrichte</h1>
		</template>
		<svws-ui-table :items="filteredItems" :columns clickable :clicked="clickedItem" @update:clicked="onClicked" :selectable="hatKompetenzAendern" v-model="selectedUnterrichte" count scroll scroll-into-view
			allow-arrow-key-selection :focus-switching-enabled :focus-help-visible
			v-model:sort-by-and-order="sortByAndOrder" :filter-open="true" :filtered="hasActiveFilter" :filter-reset="resetFilter">
			<template #search>
				<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Lerngruppe, Fach oder Zeitraster" removable />
			</template>
			<template #noData>
				{{ search.trim().length > 0 ? 'Keine Treffer für diese Suche.' : 'Noch keine Unterrichte vorhanden.' }}
			</template>
			<template #actions v-if="hatKompetenzAendern">
				<s-uv-auswahl-loeschen :items="selectedUnterrichte" art="Unterrichte" :bezeichnung="getLerngruppeBezeichnung" :loeschen="deleteItems" @deleted="onDeleted" />
			</template>
		</svws-ui-table>
	</s-uv-auswahl>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import { UvUnterricht } from "@core/core/data/uv/UvUnterricht";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useUvState } from "@ui/states/UvState";
	import type { SortByAndOrder } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	import SUvAuswahl from "../SUvAuswahl.vue";
	import SUvAuswahlLoeschen from "../SUvAuswahlLoeschen.vue";
	import { useUvPresenter } from "../UvPresenter";

	const props = defineProps<{
		auswahl: UvUnterricht | undefined;
		gotoUnterricht: (unterricht: UvUnterricht | undefined) => void;
	}>();
	const state = useUvState();
	const presenter = useUvPresenter(state.uvManager);
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref('');
	const hasActiveFilter = computed(() => search.value.trim().length > 0);
	const sortByAndOrder = ref<SortByAndOrder>({ key: 'lerngruppe', order: true });
	function resetFilter() {
		search.value = '';
	}

	const selectedIds = ref<number[]>([]);
	const selectedUnterrichte = computed({
		get: () => tableItems.value.filter(item => selectedIds.value.includes(item.id)),
		set: (items: UvUnterricht[]) => {
			selectedIds.value = items.map(item => item.id);
		},
	});

	const wochentage = ["", "Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"];

	const listUnterrichte = computed(() =>
		((state.planungsabschnitt !== null) && (state.planungsabschnitt.id !== -1))
			? state.uvManager.unterrichtGetMengeByPlanungsabschnitt(state.planungsabschnitt)
			: new ArrayList<UvUnterricht>()
	);

	function onClicked(unterricht: UvUnterricht | null) {
		props.gotoUnterricht([...listUnterrichte.value].find(item => item.id === unterricht?.id));
	}

	watch(() => state.planungsabschnitt?.id, () => {
		resetFilter();
		selectedUnterrichte.value = [];
	});

	function getLerngruppeBezeichnung(u: UvUnterricht): string {
		const lerngruppe = state.uvManager.lerngruppeGetByIdOrNull(u.idLerngruppe);
		return lerngruppe === null ? `LG-ID: ${u.idLerngruppe}` : presenter.lerngruppeBezeichnung(lerngruppe);
	}

	function getFach(u: UvUnterricht): string {
		const lerngruppe = state.uvManager.lerngruppeGetByIdOrNull(u.idLerngruppe);
		return lerngruppe === null ? '—' : presenter.lerngruppeFach(lerngruppe);
	}

	function getZeitrasterText(u: UvUnterricht): string {
		if (u.idZeitrasterEintrag === null) {
			return "—";
		}
		if (state.planungsabschnitt === null || state.planungsabschnitt.id === -1) {
			return `ID: ${u.idZeitrasterEintrag}`;
		}
		for (const zr of state.uvManager.zeitrasterGetMengeByPlanungsabschnitt(state.planungsabschnitt)) {
			try {
				const eintrag = state.uvManager.zeitrasterEintragGetByIdOrException(zr.id, u.idZeitrasterEintrag);
				return `${wochentage[eintrag.wochentag] ?? "?"}, ${eintrag.stunde}.`;
			} catch { /* naechstes Zeitraster versuchen */ }
		}
		return `ID: ${u.idZeitrasterEintrag}`;
	}

	const tableItems = computed(() => [...listUnterrichte.value].map(item => Object.assign(new UvUnterricht(), item, { lerngruppe: getLerngruppeBezeichnung(item), fach: getFach(item), zeitraster: getZeitrasterText(item) })));

	const clickedItem = computed(() => tableItems.value.find(item => item.id === props.auswahl?.id) ?? null);
	const filteredItems = computed(() => tableItems.value.filter(item => `${item.lerngruppe} ${item.fach} ${item.zeitraster}`.toLocaleLowerCase().includes(search.value.trim().toLocaleLowerCase())));
	async function deleteItems(items: UvUnterricht[]) {
		const ids = new Set(items.map(item => item.id));
		await state.delUnterricht([...listUnterrichte.value].filter(item => ids.has(item.id)));
	}

	function onDeleted(items: UvUnterricht[]) {
		selectedUnterrichte.value = [];
		if (items.some(item => item.id === props.auswahl?.id)) {
			props.gotoUnterricht(undefined);
		}
	}

	const columns = [
		{ key: "lerngruppe", label: "Lerngruppe", sortable: true },
		{ key: "fach", label: "Fach", sortable: true, span: 0.4 },
		{ key: "zeitraster", label: "Zeitraster", sortable: true, span: 0.5 },
	];

</script>
