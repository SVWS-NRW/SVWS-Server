<template>
	<s-uv-auswahl>
		<Teleport to=".svws-sub-nav-target" v-if="isMounted && hatKompetenzAendern">
			<svws-ui-sub-nav>
				<s-uv-kurse-import-blockung-modal v-slot="{ openModal }" @imported="onImported">
					<svws-ui-button type="transparent" @click.stop="openModal" title="Kurse importieren" class="text-ui-100 subNavigationFocusField">
						<span class="icon-sm i-ri-upload-2-line" /> Kurse importieren
					</svws-ui-button>
				</s-uv-kurse-import-blockung-modal>
			</svws-ui-sub-nav>
		</Teleport>
		<template #title>
			<h1 class="select-none">Kurse</h1>
		</template>
		<template #header>
			<output v-if="successMessage" class="text-sm text-ui-secondary">{{ successMessage }}</output>
		</template>
		<svws-ui-table :items="filteredItems" :columns clickable :clicked="clickedKurs" @update:clicked="onClicked"
			:selectable="hatKompetenzAendern" v-model="selectedRows" count scroll scroll-into-view
			v-model:sort-by-and-order="sortByAndOrder" :filter-open="true" :filtered="hasActiveFilter" :filter-reset="resetFilter"
			allow-arrow-key-selection :focus-switching-enabled :focus-help-visible>
			<template #search>
				<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Kurs oder Fach" removable />
			</template>
			<template #filterAdvanced>
				<svws-ui-multi-select v-model="filterJahrgaenge" :items="jahrgaenge" :item-text="jahrgang => presenter.jahrgangKuerzel(jahrgang.id)" title="Jahrgang" autocomplete />
				<svws-ui-multi-select v-model="filterFaecher" :items="faecher" :item-text="item => item" title="Fach" autocomplete />
				<svws-ui-multi-select v-model="filterKursarten" :items="kursarten" :item-text="item => item" title="Kursart" autocomplete />
			</template>
			<template #noData>
				<span v-if="rows.length === 0">Noch keine Kurse vorhanden.{{ hatKompetenzAendern ? ' Einen Kurs erstellen oder Kurse importieren.' : '' }}</span>
				<span v-else>Keine Kurse für diese Suche und Filter gefunden.</span>
			</template>
			<template #actions v-if="hatKompetenzAendern">
				<svws-ui-button @click="openDeleteModal" type="trash" :disabled="!selectedRows.length || deleting" title="Ausgewählte Kurse löschen" />
				<template v-if="state.planungsabschnitt !== null && state.planungsabschnitt.id !== -1">
					<s-uv-kurse-neu-modal v-slot="{ openModal }" @created="onCreated">
						<svws-ui-button @click="openModal" type="icon" title="Kurs erstellen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
					</s-uv-kurse-neu-modal>
				</template>
			</template>
		</svws-ui-table>
		<svws-ui-modal v-model:show="showDeleteModal" size="small" :auto-close="!deleting" :close-in-title="!deleting">
			<template #modalTitle>Kurse löschen</template>
			<template #modalContent>
				<p>{{ deleteRows.length }} ausgewählte Kurse aus dem Planungsabschnitt „{{ state.planungsabschnitt?.beschreibung }}“ löschen?</p>
				<ul class="mt-3 list-disc pl-5"><li v-for="row in deleteRows" :key="row.id">{{ row.bezeichnung }}</li></ul>
				<svws-ui-notification v-if="deleteError" type="error" class="mt-4">{{ deleteError }}</svws-ui-notification>
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="showDeleteModal = false" :disabled="deleting">Abbrechen</svws-ui-button>
				<svws-ui-button type="danger" @click="deleteKurse" :disabled="deleting || !hatKompetenzAendern || !deleteRows.length" :is-loading="deleting">Kurse löschen</svws-ui-button>
			</template>
		</svws-ui-modal>
	</s-uv-auswahl>
</template>

<script setup lang="ts">
	import { computed, onMounted, ref, watch } from "vue";

	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { UvKurs } from "@core/core/data/uv/UvKurs";
	import { useUvState } from "@ui/states/UvState";
	import type { DataTableColumn, SortByAndOrder } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	import SUvAuswahl from "../SUvAuswahl.vue";
	import { useUvPresenter } from "../UvPresenter";

	const props = defineProps<{
		auswahl: UvKurs | undefined;
		gotoKurs: (kurs: UvKurs | undefined) => void;
	}>();
	const state = useUvState();
	const presenter = useUvPresenter(state.uvManager);
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	type KursRow = { id: number; kurs: UvKurs; fach: string; fachText: string; kursart: string; kursnummer: number; bezeichnung: string;
		jahrgaenge: JahrgangsDaten[]; jahrgangText: string; schuelerzahl: number };
	const rows = computed<KursRow[]>(() => {
		if (state.planungsabschnitt === null || state.planungsabschnitt.id === -1) {
			return [];
		}
		const faecherById = new Map([...state.uvManager.fachGetMengeAsList()].map(fach => [fach.id, state.uvManager.fachdatenGetByFach(fach)]));
		return [...state.uvManager.kursGetMengeByPlanungsabschnitt(state.planungsabschnitt)].map(kurs => {
			const fachdaten = faecherById.get(kurs.idFach);
			const fach = fachdaten?.kuerzel ?? 'Fach nicht verfügbar';
			const gruppe = state.uvManager.schuelergruppeGetByKurs(kurs);
			const kursJahrgaenge = [...state.uvManager.jahrgangsdatenGetMengeBySchuelergruppe(gruppe)];
			return { id: kurs.id, kurs, fach, fachText: fachdaten?.bezeichnung ?? fach, kursart: kurs.kursart,
				jahrgaenge: kursJahrgaenge, jahrgangText: kursJahrgaenge.map(jahrgang => presenter.jahrgangKuerzel(jahrgang.id)).join(', '),
				schuelerzahl: state.uvManager.planungsabschnittSchuelerGetMengeBySchuelergruppe(gruppe).size(),
				kursnummer: kurs.kursnummer, bezeichnung: presenter.kursBezeichnung(kurs) };
		});
	});
	const successMessage = ref('');
	const search = ref('');
	const filterFaecher = ref<string[]>([]);
	const filterKursarten = ref<string[]>([]);
	const filterJahrgaenge = ref<JahrgangsDaten[]>([]);
	const jahrgaenge = computed(() => [...new Map(rows.value.flatMap(row => row.jahrgaenge).map(j => [j.id, j])).values()]
		.sort((a, b) => a.sortierung === b.sortierung ? a.id - b.id : a.sortierung - b.sortierung));
	const faecher = computed(() => [...new Set(rows.value.map(row => row.fach))].sort((a, b) => a.localeCompare(b)));
	const kursarten = computed(() => [...new Set(rows.value.map(row => row.kursart))].sort((a, b) => a.localeCompare(b)));
	const hasActiveFilter = computed(() => (search.value.trim().length > 0) || filterFaecher.value.length > 0 || filterKursarten.value.length > 0
		|| filterJahrgaenge.value.length > 0);
	const filteredItems = computed(() => rows.value.filter(row => {
		const matchesSearch = `${row.bezeichnung} ${row.fachText}`.toLocaleLowerCase().includes(search.value.trim().toLocaleLowerCase());
		return matchesSearch && (filterFaecher.value.length === 0 || filterFaecher.value.includes(row.fach))
			&& (filterKursarten.value.length === 0 || filterKursarten.value.includes(row.kursart))
			&& (filterJahrgaenge.value.length === 0 || row.jahrgaenge.some(j => filterJahrgaenge.value.some(filter => filter.id === j.id)));
	}));
	const sortByAndOrder = ref<SortByAndOrder>({ key: 'fach', order: true });
	const selectedIds = ref<number[]>([]);
	const selectedRows = computed({
		get: () => rows.value.filter(row => selectedIds.value.includes(row.id)),
		set: (value: KursRow[]) => {
			selectedIds.value = value.map(row => row.id);
		},
	});
	const clickedKurs = computed(() => rows.value.find(row => row.id === props.auswahl?.id) ?? null);

	function onClicked(row: KursRow | null) {
		props.gotoKurs(row?.kurs);
	}

	function onCreated(kurs: UvKurs) {
		successMessage.value = `Kurs ${presenter.kursBezeichnung(kurs)} wurde erstellt.`;
		resetFilter();
		selectedIds.value = [];
		props.gotoKurs(kurs);
	}

	function onImported(anzahl: number) {
		resetFilter();
		successMessage.value = `${anzahl} Kurse wurden importiert.`;
	}

	function resetFilter() {
		search.value = '';
		filterFaecher.value = [];
		filterKursarten.value = [];
		filterJahrgaenge.value = [];
	}

	watch(() => state.planungsabschnitt?.id, () => {
		successMessage.value = '';
		selectedIds.value = [];
		showDeleteModal.value = false;
		resetFilter();
	});

	const columns: DataTableColumn[] = [
		{ key: 'jahrgangText', label: 'JG', tooltip: 'Jahrgänge der Schüler', sortable: true },
		{ key: 'fach', label: 'Fach', sortable: true },
		{ key: 'kursart', label: 'Art', tooltip: 'Kursart', sortable: true, span: 0.6 },
		{ key: 'kursnummer', label: 'Nr.', tooltip: 'Kursnummer', sortable: true, align: 'right', span: 0.4 },
		{ key: 'schuelerzahl', label: 'Schüler', tooltip: 'Anzahl der Schüler', sortable: true, align: 'right', fixedWidth: 5 },
	];
	const showDeleteModal = ref(false);
	const deleting = ref(false);
	const deleteError = ref('');
	const deleteRows = ref<KursRow[]>([]);

	function openDeleteModal() {
		if (!hatKompetenzAendern.value || selectedRows.value.length === 0 || deleting.value) {
			return;
		}
		deleteRows.value = [...selectedRows.value];
		deleteError.value = '';
		showDeleteModal.value = true;
	}

	async function deleteKurse() {
		if (!hatKompetenzAendern.value || deleting.value || deleteRows.value.length === 0) {
			return;
		}
		deleting.value = true;
		deleteError.value = '';
		try {
			const ids = new Set(deleteRows.value.map(row => row.id));
			await state.delKurs(deleteRows.value.map(row => row.kurs));
			selectedIds.value = selectedIds.value.filter(id => !ids.has(id));
			if ((props.auswahl !== undefined) && ids.has(props.auswahl.id)) {
				props.gotoKurs(undefined);
			}
			showDeleteModal.value = false;
		} catch {
			deleteError.value = 'Die Kurse konnten nicht gelöscht werden. Bitte erneut versuchen.';
		} finally {
			deleting.value = false;
		}
	}
</script>
