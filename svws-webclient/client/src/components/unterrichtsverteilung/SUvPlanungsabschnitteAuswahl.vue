<template>
	<s-uv-auswahl>
		<template #title>
			<h1 class="select-none">Unterrichtsverteilung</h1>
			<div><abschnitt-auswahl /></div>
		</template>
		<svws-ui-table :clickable="!manager().liste.auswahlExists()"
			:clicked="clickedEintrag"
			@update:clicked="pla => gotoDefaultView(pla.id)"
			:items="rowsFiltered"
			:model-value="[...manager().liste.auswahl()]"
			@update:model-value="items => setAuswahl(items)"
			v-model:sort-by-and-order="sortByAndOrder"
			:columns
			selectable
			count
			:filter-open="true"
			:filtered="filterChanged()"
			:filterReset
			scroll-into-view
			scroll
			allow-arrow-key-selection
			:focus-switching-enabled
			:focus-help-visible>
			<template #filter>
				<div class="col-span-full flex flex-wrap gap-x-5">
					<svws-ui-checkbox type="toggle" v-model="filterNurAktiv">Nur Aktive</svws-ui-checkbox>
				</div>
			</template>
			<template #cell(beschreibung)="{ rowData }">
				<div class="py-1 whitespace-normal break-words">
					<div class="font-medium">{{ rowData.beschreibung }}</div>
					<div v-if="rowData.id !== -1" class="text-sm text-ui-secondary mt-1">
						{{ DateUtils.gibDatumGermanFormat(rowData.gueltigVon) }} – {{ rowData.gueltigBis ? DateUtils.gibDatumGermanFormat(rowData.gueltigBis) : 'unbegrenzt' }}
					</div>
				</div>
			</template>
			<template #cell(aktiv)="{ value }">
				<span v-if="value" class="icon icon-ui-brand i-ri-checkbox-circle-fill" title="Dieser Planungsabschnitt ist aktiv" />
			</template>
			<template #actions>
				<svws-ui-tooltip position="bottom" v-if="hatKompetenzAendern">
					<svws-ui-button :disabled="activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="startCreationMode" :has-focus="rowsFiltered.length === 0">
						<span class="icon i-ri-add-line" />
					</svws-ui-button>
					<template #content>
						Neuen Planungsabschnitt anlegen
					</template>
				</svws-ui-tooltip>
			</template>
		</svws-ui-table>
	</s-uv-auswahl>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import type { UvPlanungsabschnitt } from "@core/core/data/uv/UvPlanungsabschnitt";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { useUvState } from "@ui/states/UvState";
	import type { DataTableColumn, SortByAndOrder } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import type { UvPlanungsabschnitteListeManager } from "@ui/ui/manager/unterrichtsverteilung/UvPlanungsabschnitteListeManager";
	import { ViewType } from "@ui/ui/nav/ViewType";

	import SUvAuswahl from "./SUvAuswahl.vue";

	const props = defineProps<{
		manager: () => UvPlanungsabschnitteListeManager;
		activeViewType: ViewType;
		gotoDefaultView: (eintragId?: number | null) => Promise<void>;
		gotoGruppenprozessView: (hatAuswahl?: boolean) => Promise<void>;
		gotoHinzufuegenView: (hatAuswahl?: boolean) => Promise<void>;
		setFilter: () => Promise<void>;
	}>();
	const state = useUvState();
	const manager = () => props.manager();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const hatKompetenzAendern = computed(() => state.hatKompetenzAllgemeinAendern);

	const columns: DataTableColumn[] = [
		{ key: "beschreibung", label: "Bezeichnung", sortable: true, defaultSort: 'asc' },
		{ key: "aktiv", label: "", fixedWidth: 2 },
	];

	const rowsFiltered = computed<UvPlanungsabschnitt[]>(() => {
		const arr = [];
		for (const e of manager().filtered()) {
			arr.push(e);
		}
		return arr;
	});

	const filterNurAktiv = computed<boolean>({
		get: () => manager().filterNurAktiv(),
		set: (value) => {
			manager().setFilterNurAktiv(value);
			props.setFilter().catch(() => {});
		},
	});


	async function startCreationMode(): Promise<void> {
		await props.gotoHinzufuegenView(true);
	}

	async function filterReset() {
		await props.setFilter();
	}

	function filterChanged(): boolean {
		return false;
	}

	const clickedEintrag = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN)) {
			return null;
		}
		return (manager().hasDaten() === true) ? manager().auswahl() : null;
	});

	async function setAuswahl(items: UvPlanungsabschnitt[]) {
		manager().liste.auswahlClear();
		for (const item of items) {
			if (manager().liste.hasValue(item) === true) {
				manager().liste.auswahlAdd(item);
			}
		}
		if (manager().liste.auswahlExists() === true) {
			await props.gotoGruppenprozessView(true);
		} else {
			await props.gotoDefaultView(manager().getVorherigeAuswahl()?.id);
		}
	}

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = manager().orderGet();
			if (list.length === 0) {
				return undefined;
			} else {
				const { field: key, ascending: order } = list[0];
				return { key, order };
			}
		},
		set: (value) => {
			if ((value === undefined) || (value.key === null)) {
				return;
			}
			manager().orderUpdate(value.key, value.order);
			props.setFilter().catch(() => {});
		},
	});
</script>
