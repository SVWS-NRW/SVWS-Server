<template>
	<s-uv-auswahl>
		<svws-ui-modal v-model:show="showBlockenModal" size="small" :auto-close="!isActionPending" :close-in-title="!isActionPending">
			<template #modalTitle>Überschreiben bestätigen</template>
			<template #modalContent>
				<div class="flex flex-col gap-3">
					<p>Beim Blocken werden alle bestehenden Zuweisungen von Lehrkräften zu Lerngruppen überschrieben.</p>
					<p>Fortfahren?</p>
					<svws-ui-notification v-if="actionError" type="error">{{ actionError }}</svws-ui-notification>
				</div>
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="showBlockenModal = false" :disabled="isActionPending">Abbrechen</svws-ui-button>
				<svws-ui-button @click="bestaetigeLerngruppenBlocken" :disabled="isActionPending">Blocken</svws-ui-button>
			</template>
		</svws-ui-modal>
		<Teleport to=".svws-sub-nav-target" v-if="isMounted && hatKompetenzAendern">
			<svws-ui-sub-nav>
				<svws-ui-button type="transparent" :disabled="isActionPending" @click.stop="createKlassenLerngruppen" title="Lerngruppen für Klassen erzeugen" class="text-ui-100 subNavigationFocusField">
					<span class="icon-sm i-ri-sparkling-line" /> Klassen-Lerngruppen erzeugen
				</svws-ui-button>
				<svws-ui-button :disabled="isActionPending || listLerngruppen.isEmpty()" type="transparent" @click.stop="showBlockenModal = true" title="Lerngruppen blocken" class="text-ui-100 subNavigationFocusField">
					<span class="icon-sm i-ri-sparkling-line" /> Blocken
				</svws-ui-button>
				<svws-ui-button :disabled="isActionPending || selectedLerngruppen.length === 0" type="transparent" @click.stop="erzeugeAusLerngruppen" title="Unterrichte aus den ausgewählten Lerngruppen erzeugen" class="text-ui-100 subNavigationFocusField">
					<span class="icon-sm i-ri-sparkling-line" /> Unterrichte erzeugen
				</svws-ui-button>
			</svws-ui-sub-nav>
		</Teleport>
		<template #title>
			<h1 class="select-none">Lerngruppen</h1>
		</template>
		<template #header>
			<svws-ui-notification v-if="actionError && !showBlockenModal" type="error">{{ actionError }}</svws-ui-notification>
			<output v-if="actionMessage" class="text-sm text-ui-secondary">{{ actionMessage }}</output>
		</template>
		<svws-ui-table :items="filteredItems" :columns clickable :clicked="clickedItem" @update:clicked="onClicked" :selectable="hatKompetenzAendern" v-model="selectedLerngruppen" count scroll scroll-into-view
			allow-arrow-key-selection :focus-switching-enabled :focus-help-visible
			v-model:sort-by-and-order="sortByAndOrder" :filter-open="true" :filtered="hasActiveFilter" :filter-reset="resetFilter">
			<template #search>
				<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Lerngruppe, Fach oder Lehrkraft" removable />
			</template>
			<template #noData>
				{{ search.trim().length > 0 ? 'Keine Treffer für diese Suche.' : 'Noch keine Lerngruppen vorhanden.' }}
			</template>

			<template #actions v-if="hatKompetenzAendern">
				<s-uv-auswahl-loeschen :disabled="isActionPending" :items="selectedLerngruppen" art="Lerngruppen" :bezeichnung="presenter.lerngruppeBezeichnung" :loeschen="deleteItems" @deleted="onDeleted" />
			</template>
		</svws-ui-table>
	</s-uv-auswahl>
</template>

<script setup lang="ts">
	import { computed, onMounted, ref, watch } from "vue";

	import { UvLerngruppe } from "@core/core/data/uv/UvLerngruppe";
	import { UvRegelManager } from "@core/core/utils/uv/UvRegelManager";
	import { UvAlgorithmusImpl } from "@core/core/utils/uvblockung/UvAlgorithmusImpl";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useUvState } from "@ui/states/UvState";
	import type { SortByAndOrder } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	import SUvAuswahl from "../SUvAuswahl.vue";
	import SUvAuswahlLoeschen from "../SUvAuswahlLoeschen.vue";
	import { useUvPresenter } from "../UvPresenter";

	const props = defineProps<{
		auswahl: UvLerngruppe | undefined;
		gotoLerngruppe: (lerngruppe: UvLerngruppe | undefined) => void;
	}>();
	const state = useUvState();
	const presenter = useUvPresenter(state.uvManager);
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref('');
	const hasActiveFilter = computed(() => search.value.trim().length > 0);
	const sortByAndOrder = ref<SortByAndOrder>({ key: 'bezeichnung', order: true });
	function resetFilter() {
		search.value = '';
	}

	const isMounted = ref(false);
	const showBlockenModal = ref(false);
	const isActionPending = ref(false);
	const actionError = ref('');
	const actionMessage = ref('');
	onMounted(() => isMounted.value = true);

	const listLerngruppen = computed(() =>
		((state.planungsabschnitt !== null) && (state.planungsabschnitt.id !== -1))
			? state.uvManager.lerngruppeGetMengeByPlanungsabschnitt(state.planungsabschnitt)
			: new ArrayList<UvLerngruppe>()
	);

	const selectedIds = ref<number[]>([]);
	const selectedLerngruppen = computed({
		get: () => tableItems.value.filter(item => selectedIds.value.includes(item.id)),
		set: (items: UvLerngruppe[]) => {
			selectedIds.value = items.map(item => item.id);
		},
	});

	function onClicked(lerngruppe: UvLerngruppe | null) {
		props.gotoLerngruppe([...listLerngruppen.value].find(item => item.id === lerngruppe?.id));
	}

	watch(() => state.planungsabschnitt?.id, () => {
		resetFilter();
		showBlockenModal.value = false;
		actionError.value = '';
		actionMessage.value = '';
		selectedLerngruppen.value = [];
	});

	function getLehrer(lg: UvLerngruppe): string {
		const lehrer = [...state.uvManager.lerngruppenLehrerGetMengeByLerngruppe(lg)];
		return lehrer.map(lgl => state.uvManager.lehrerGetByLerngruppenLehrer(lgl).kuerzel).join(", ");
	}

	const tableItems = computed(() => [...listLerngruppen.value].map(item => Object.assign(new UvLerngruppe(), item, { typ: presenter.lerngruppeTyp(item), bezeichnung: presenter.lerngruppeBezeichnung(item), fach: presenter.lerngruppeFach(item), lehrer: getLehrer(item) })));

	const clickedItem = computed(() => tableItems.value.find(item => item.id === props.auswahl?.id) ?? null);
	const filteredItems = computed(() => tableItems.value.filter(item => `${item.typ} ${item.bezeichnung} ${item.fach} ${item.lehrer}`.toLocaleLowerCase().includes(search.value.trim().toLocaleLowerCase())));
	async function deleteItems(items: UvLerngruppe[]) {
		const ids = new Set(items.map(item => item.id));
		await state.delLerngruppe([...listLerngruppen.value].filter(item => ids.has(item.id)));
	}

	function onDeleted(items: UvLerngruppe[]) {
		selectedLerngruppen.value = [];
		if (items.some(item => item.id === props.auswahl?.id)) {
			props.gotoLerngruppe(undefined);
		}
	}

	const columns = [
		{ key: "typ", label: "Typ", sortable: true, span: 0.3 },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
		{ key: "fach", label: "Fach", sortable: true, span: 0.3 },
		{ key: "wochenstundenUnterrichtet", label: "Wstd.", tooltip: "Unterrichtete Wochenstunden", sortable: true, span: 0.2 },
		{ key: "lehrer", label: "Lehrer", sortable: true, span: 0.5 },
	];

	async function erzeugeAusLerngruppen() {
		if (state.planungsabschnitt === null || state.planungsabschnitt.id === -1) {
			return;
		}
		if (selectedLerngruppen.value.length === 0) {
			return;
		}
		if (!hatKompetenzAendern.value || isActionPending.value) {
			return;
		}
		const idPlanungsabschnitt = state.planungsabschnitt.id;
		const ids = new Set(selectedIds.value);
		isActionPending.value = true;
		actionError.value = '';
		actionMessage.value = '';
		try {
			await state.erzeugeUnterrichteByLerngruppen([...listLerngruppen.value].filter(item => ids.has(item.id)));
			if (istAktuellerPlanungsabschnitt(idPlanungsabschnitt)) {
				actionMessage.value = 'Unterrichte wurden erzeugt.';
			}
		} catch {
			if (istAktuellerPlanungsabschnitt(idPlanungsabschnitt)) {
				actionError.value = 'Die Aktion konnte nicht abgeschlossen werden. Bitte den aktuellen Bestand prüfen und erneut versuchen.';
			}
		} finally {
			isActionPending.value = false;
		}
	}

	async function createKlassenLerngruppen() {
		if (state.planungsabschnitt === null || state.planungsabschnitt.id === -1) {
			return;
		}
		if (!hatKompetenzAendern.value || isActionPending.value) {
			return;
		}
		const idPlanungsabschnitt = state.planungsabschnitt.id;
		isActionPending.value = true;
		actionError.value = '';
		actionMessage.value = '';
		try {
			await state.createLerngruppenFromKlassen();
			if (istAktuellerPlanungsabschnitt(idPlanungsabschnitt)) {
				actionMessage.value = 'Klassen-Lerngruppen wurden erzeugt.';
			}
		} catch {
			if (istAktuellerPlanungsabschnitt(idPlanungsabschnitt)) {
				actionError.value = 'Die Aktion konnte nicht abgeschlossen werden. Bitte den aktuellen Bestand prüfen und erneut versuchen.';
			}
		} finally {
			isActionPending.value = false;
		}
	}

	async function lerngruppenBlocken() {
		if (state.planungsabschnitt === null || state.planungsabschnitt.id === -1) {
			return;
		}
		const manRegeln = new UvRegelManager(state.uvManager, new ArrayList());
		const algo = new UvAlgorithmusImpl(state.uvManager, manRegeln, state.planungsabschnitt);
		algo.berechneInnerhalb(2000);
		const lehrer = algo.gibBestesAktuellesErgebnis();
		await state.createLerngruppenLehrerMultiple(lehrer);
	}

	function istAktuellerPlanungsabschnitt(id: number): boolean {
		return state.planungsabschnitt?.id === id;
	}

	async function bestaetigeLerngruppenBlocken() {
		const planungsabschnitt = state.planungsabschnitt;
		if (!hatKompetenzAendern.value || isActionPending.value || planungsabschnitt === null || planungsabschnitt.id === -1) {
			return;
		}
		const idPlanungsabschnitt = planungsabschnitt.id;
		isActionPending.value = true;
		actionError.value = '';
		actionMessage.value = '';
		try {
			await new Promise<void>(resolve => {
				requestAnimationFrame(() => requestAnimationFrame(() => resolve()));
			});
			if (!istAktuellerPlanungsabschnitt(idPlanungsabschnitt)) {
				return;
			}
			await lerngruppenBlocken();
			if (istAktuellerPlanungsabschnitt(idPlanungsabschnitt)) {
				actionMessage.value = 'Lehrkräfte wurden den Lerngruppen zugewiesen.';
				showBlockenModal.value = false;
			}
		} catch {
			if (istAktuellerPlanungsabschnitt(idPlanungsabschnitt)) {
				actionError.value = 'Die Aktion konnte nicht abgeschlossen werden. Bitte den aktuellen Bestand prüfen und erneut versuchen.';
			}
		} finally {
			isActionPending.value = false;
		}
	}

</script>
