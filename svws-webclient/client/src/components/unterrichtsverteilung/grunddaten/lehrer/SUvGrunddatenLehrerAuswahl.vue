<template>
	<s-uv-auswahl>
		<svws-ui-modal v-model:show="showActionModal" size="small">
			<template #modalTitle>{{ actionModalTitle }}</template>
			<template #modalContent>
				<p>{{ actionModalText }}</p>
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="closeActionModal"> Abbrechen </svws-ui-button>
				<svws-ui-button type="primary" @click="confirmActionModal"> {{ actionModalConfirmText }} </svws-ui-button>
			</template>
		</svws-ui-modal>
		<Teleport to=".svws-sub-nav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<span class="text-ui-100 subNavigationFocusField" title="Pflichtstundensoll aus Personalabschnittsdaten importieren">
					<svws-ui-button-select type="transparent"
						:dropdown-actions="pflichtstundensollActions"
						:disabled="!selectedLehrer.length"
						:default-action="{ text: 'Pflichtstundensoll', action: async () => {}, default: true }">
						<template #icon><span class="icon-sm i-ri-upload-2-line" /></template>
					</svws-ui-button-select>
				</span>
				<span class="text-ui-100 subNavigationFocusField" title="Anrechnungsstunden aus Personalabschnittsdaten importieren">
					<svws-ui-button-select type="transparent"
						:dropdown-actions="anrechnungsstundenActions"
						:disabled="!selectedLehrer.length"
						:default-action="{ text: 'Anrechnungsstunden', action: async () => {}, default: true }">
						<template #icon><span class="icon-sm i-ri-upload-2-line" /></template>
					</svws-ui-button-select>
				</span>
			</svws-ui-sub-nav>
		</Teleport>
		<template #title>
			<h1 class="select-none">Lehrkräfte</h1>
		</template>
		<svws-ui-table :items="filteredItems" :columns clickable :clicked="auswahl" @update:clicked="gotoUvLehrer" :selectable="hatKompetenzAendern" v-model="selectedLehrer" count
			scroll scroll-into-view allow-arrow-key-selection :focus-switching-enabled :focus-help-visible :filter-open="true" :filtered="hasActiveFilter" :filter-reset="resetFilter">
			<template #search><svws-ui-text-input v-model="search" type="search" placeholder="Lehrkraft suchen" removable /></template>

			<template #cell(kuerzel)="{ rowData, value }">
				<span v-if="rowData.idKLehrer === null" class="inline-flex items-center gap-1">
					<span>{{ value }}</span>
					<span class="icon i-ri-alert-line icon-ui-warning" />
				</span>
				<span v-else>{{ value }}</span>
			</template>
			<template #cell(nachname)="{ rowData }">
				<div class="py-1 min-w-0">
					<div class="font-medium break-words">{{ rowData.nachname }}</div>
					<div class="text-ui-secondary text-sm break-words">{{ rowData.vorname }}</div>
				</div>
			</template>
			<template #cell(ps)="{ rowData }">
				{{ getPflichtstundensoll(rowData) }}
			</template>
			<template #cell(an)="{ rowData }">
				{{ getAnrechnungsstunden(rowData) }}
			</template>
			<template #actions v-if="hatKompetenzAendern">
				<span :title="deleteTooltip">
					<svws-ui-button @click="deleteSelectedLehrer()" type="trash" :disabled="deleteDisabled" :title="deleteDisabled ? undefined : 'Markierte Lehrer löschen'" />
				</span>
				<template v-if="(state.planungsabschnitt !== null) && (state.planungsabschnitt.id !== -1)">
					<s-uv-grunddaten-lehrer-liste-modal v-slot="{ openModal }" :add="state.addLehrerToPlanungsabschnitt" :list-lehrer="state.uvManager.lehrerGetMengeTaetigAberNichtInPlanungsabschnitt(state.planungsabschnitt)">
						<svws-ui-button @click="openModal" type="icon" title="Lehrer importieren"> <span class="icon i-ri-upload-2-line" /> </svws-ui-button>
					</s-uv-grunddaten-lehrer-liste-modal>
				</template>
				<template v-else>
					<s-uv-grunddaten-lehrer-import-modal v-slot="{ openModal }" :list-lehrer="schulLehrer">
						<svws-ui-button @click="openImportModal(openModal)" type="icon" title="Lehrer importieren"> <span class="icon i-ri-upload-2-line" /> </svws-ui-button>
					</s-uv-grunddaten-lehrer-import-modal>
				</template>
				<s-uv-grunddaten-lehrer-neu-modal v-slot="{ openModal }">
					<svws-ui-button @click="openModal" type="icon" title="Lehrer neu erstellen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
				</s-uv-grunddaten-lehrer-neu-modal>
			</template>
		</svws-ui-table>
	</s-uv-auswahl>
</template>

<script setup lang="ts">
	import { computed, onMounted, ref, watch } from "vue";

	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import type { UvLehrer } from "@core/core/data/uv/UvLehrer";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useUvState } from "@ui/states/UvState";
	import type { DataTableColumn } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	import SUvAuswahl from "../../SUvAuswahl.vue";

	const props = defineProps<{
		auswahl: UvLehrer | undefined;
		gotoUvLehrer: (lehrer: UvLehrer | undefined) => void;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref('');
	const hasActiveFilter = computed(() => search.value.trim().length > 0);
	function resetFilter() {
		search.value = '';
	}
	const filteredItems = computed(() => [...listLehrer.value].filter(item => `${item.kuerzel} ${item.nachname ?? ''} ${item.vorname ?? ''}`
		.toLocaleLowerCase().includes(search.value.trim().toLocaleLowerCase())));
	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);
	type ActionModalType = "deletePflichtstundensoll" | "importPflichtstundensoll" | "deleteAnrechnungsstunden" | "importAnrechnungsstunden" | null;

	const listLehrer = computed(() =>
		((state.planungsabschnitt !== null) && (state.planungsabschnitt.id !== -1))
			? state.uvManager.lehrerGetMengeByPlanungsabschnitt(state.planungsabschnitt)
			: state.uvManager.lehrerGetMengeAsList()
	);

	const selectedLehrer = ref<UvLehrer[]>([]);
	const selectedLehrerCollection = computed(() => {
		const result = new ArrayList<UvLehrer>();
		for (const lehrer of selectedLehrer.value) {
			result.add(lehrer);
		}
		return result;
	});
	const schulLehrer = ref<LehrerListeEintrag[]>([]);
	const actionModalType = ref<ActionModalType>(null);
	const showActionModal = computed({
		get: () => actionModalType.value !== null,
		set: (value: boolean) => {
			if (!value) {
				actionModalType.value = null;
			}
		},
	});

	type Item = {
		text: string;
		action: () => void | Promise<any>;
		default?: boolean;
		separator?: boolean;
	};

	const pflichtstundensollActions = computed<Iterable<Item>>(() => [
		{ text: "bei markierten löschen", action: () => openActionModal("deletePflichtstundensoll") },
		{ text: "bei markierten importieren", action: () => openActionModal("importPflichtstundensoll") },
	]);

	const anrechnungsstundenActions = computed<Iterable<Item>>(() => [
		{ text: "bei markierten löschen", action: () => openActionModal("deleteAnrechnungsstunden") },
		{ text: "bei markierten importieren", action: () => openActionModal("importAnrechnungsstunden") },
	]);

	const actionModalTitle = computed(() => {
		switch (actionModalType.value) {
			case "deletePflichtstundensoll":
				return "Pflichtstundensoll löschen";
			case "importPflichtstundensoll":
				return "Pflichtstundensoll importieren";
			case "deleteAnrechnungsstunden":
				return "Anrechnungsstunden löschen";
			case "importAnrechnungsstunden":
				return "Anrechnungsstunden importieren";
			default:
				return "";
		}
	});

	const actionModalText = computed(() => {
		switch (actionModalType.value) {
			case "deletePflichtstundensoll":
				return "Sollen die Pflichtstunden-Einträge der markierten Lehrkräfte wirklich gelöscht werden?";
			case "importPflichtstundensoll":
				return "Der Import aus den Personalabschnittsdaten erfolgt nur bei markierten Lehrkräften, denen noch keine Pflichtstunden-Einträge zugewiesen sind.";
			case "deleteAnrechnungsstunden":
				return "Sollen die Anrechnungsstunden der markierten Lehrkräfte wirklich gelöscht werden?";
			case "importAnrechnungsstunden":
				return "Der Import aus den Personalabschnittsdaten erfolgt nur bei markierten Lehrkräften, denen noch keine Anrechnungsstunden zugewiesen sind.";
			default:
				return "";
		}
	});

	const actionModalConfirmText = computed(() => {
		switch (actionModalType.value) {
			case "deletePflichtstundensoll":
			case "deleteAnrechnungsstunden":
				return "Löschen";
			case "importPflichtstundensoll":
			case "importAnrechnungsstunden":
				return "Importieren";
			default:
				return "";
		}
	});

	const hasSelectedVerwendeteLehrer = computed(() => ((state.planungsabschnitt !== null) && (state.planungsabschnitt.id !== -1))
		? !state.uvManager.lehrerGetMengeVerwendetInPlanungsabschnittByLehrerMenge(state.planungsabschnitt, selectedLehrerCollection.value).isEmpty()
		: !state.uvManager.lehrerGetMengeVerwendetByLehrerMenge(selectedLehrerCollection.value).isEmpty());
	const deleteDisabled = computed(() => (selectedLehrer.value.length === 0) || hasSelectedVerwendeteLehrer.value);
	const deleteTooltip = computed(() => {
		if (selectedLehrer.value.length === 0) {
			return "Keine Lehrkräfte ausgewählt";
		}
		if (hasSelectedVerwendeteLehrer.value) {
			return ((state.planungsabschnitt !== null) && (state.planungsabschnitt.id !== -1))
				? "Markierte Lehrkräfte werden im ausgewählten Planungsabschnitt verwendet und können deshalb nicht gelöscht werden."
				: "Markierte Lehrkräfte werden in der Unterrichtsverteilung verwendet und können deshalb nicht gelöscht werden.";
		}
		return "Markierte Lehrer löschen";
	});


	watch(() => state.planungsabschnitt?.id, () => {
		resetFilter();
		selectedLehrer.value = [];
	});

	const columns = computed(() => {
		const result: DataTableColumn[] = [
			{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc', fixedWidth: 5 },
			{ key: "nachname", label: "Name", sortable: true, span: 1.6 },
		];
		if ((state.planungsabschnitt !== null) && (state.planungsabschnitt.id !== -1)) {
			result.push(
				{ key: "ps", label: "PS", sortable: false, span: 0.45, align: 'right' },
				{ key: "an", label: "AN", sortable: false, span: 0.45, align: 'right' }
			);
		}
		return result;
	});

	async function deleteSelectedLehrer() {
		if (deleteDisabled.value) {
			return;
		}
		await state.delLehrer(selectedLehrer.value);
	}

	async function openImportModal(openModal: () => void) {
		schulLehrer.value = await state.getSchulLehrer();
		openModal();
	}

	function openActionModal(type: ActionModalType) {
		actionModalType.value = type;
	}

	function closeActionModal() {
		actionModalType.value = null;
	}

	async function confirmActionModal() {
		switch (actionModalType.value) {
			case "deletePflichtstundensoll":
				await state.delPflichtstundensollBeiLehrern(selectedLehrer.value);
				break;
			case "importPflichtstundensoll":
				await state.importPflichtstundensollBeiLehrern(selectedLehrer.value);
				break;
			case "deleteAnrechnungsstunden":
				await state.delAnrechnungsstundenBeiLehrern(selectedLehrer.value);
				break;
			case "importAnrechnungsstunden":
				await state.importAnrechnungsstundenBeiLehrern(selectedLehrer.value);
				break;
			default:
				return;
		}
		actionModalType.value = null;
	}

	function getPflichtstundensoll(lehrer: UvLehrer): string {
		if ((state.planungsabschnitt === null) || (state.planungsabschnitt.id === -1)) {
			return "—";
		}
		const wert = state.uvManager.lehrerPflichtstundensollGetDoubleByLehrerAndPlanungsabschnitt(lehrer, state.planungsabschnitt);
		return (wert === null) ? "—" : wert.toLocaleString('de-DE');
	}

	function getAnrechnungsstunden(lehrer: UvLehrer): string {
		if ((state.planungsabschnitt === null) || (state.planungsabschnitt.id === -1)) {
			return "—";
		}
		return state.uvManager.lehrerAnrechnungsstundenGetDoubleByLehrerAndPlanungsabschnitt(lehrer, state.planungsabschnitt).toLocaleString('de-DE');
	}

</script>
