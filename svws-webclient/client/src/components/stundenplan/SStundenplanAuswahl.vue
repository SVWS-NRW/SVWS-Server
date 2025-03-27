<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1 class="select-none">Stundenplan</h1>
			<div><abschnitt-auswahl :daten="schuljahresabschnittsauswahl" /></div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :clickable="!manager().liste.auswahlExists()"
				:clicked="clickedEintrag"
				@update:clicked="stundenplan => gotoDefaultView(stundenplan.id)"
				:items="rowsFiltered"
				:model-value="[...props.manager().liste.auswahl()]"
				@update:model-value="items => setAuswahl(items)"
				:columns
				:unselectable
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
				<template #cell(bezeichnung)="{ value, rowData }">
					<span v-if="manager().hatUeberschneidungMitAnderemStundenplan(rowData)" class="icon-lg bg-ui-danger i-ri-alert-fill" />
					{{ value }}
				</template>
				<template #actions>
					<svws-ui-tooltip position="bottom" v-if="ServerMode.DEV.checkServerMode(serverMode)/* && hatKompetenzAendern*/">
						<svws-ui-button :disabled="activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="startCreationMode" :has-focus="rowsFiltered.length === 0">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neuen Stundenplan anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { StundenplanAuswahlProps } from "./SStundenplanAuswahlProps";
	import type { DataTableColumn } from "@ui";
	import { ViewType } from "@ui";
	import { ServerMode } from "@core";
	import type { StundenplanListeEintrag } from "@core";
	import {useRegionSwitch} from "~/components/useRegionSwitch";

	const props = defineProps<StundenplanAuswahlProps>();
	const selected = ref<StundenplanListeEintrag[]>([]);

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", span: 2, sortable: false },
		{ key: "gueltigAb", label: "von", span: 1, sortable: true, defaultSort: 'asc', type: 'date' },
		{ key: "gueltigBis", label: "bis", span: 1, sortable: false, type: 'date' }
	];

	const unselectable = computed<Set<StundenplanListeEintrag>>(() => {
		const vorlage = props.manager().getStundenplanVorlage();
		return vorlage !== null ? new Set([vorlage]) : new Set();
	});

	const rowsFiltered = computed<StundenplanListeEintrag[]>(() => {
		const arr = [];
		for (const e of props.manager().filtered())
			arr.push(e);
		return arr;
	});

	async function startCreationMode(): Promise<void> {
		await props.gotoHinzufuegenView(true)
	}

	async function filterReset() {
		await props.setFilter();
	}

	function filterChanged(): boolean {
		return false;
	}

	const clickedEintrag = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN))
			return null;
		return props.manager().hasDaten() ? props.manager().auswahl() : null;
	});

	async function setAuswahl(items : StundenplanListeEintrag[]) {
		props.manager().liste.auswahlClear();
		for (const item of items)
			if (props.manager().liste.hasValue(item))
				props.manager().liste.auswahlAdd(item);
		if (props.manager().liste.auswahlExists())
			await props.gotoGruppenprozessView(true);
		else
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.stundenplanGetID());
	}

</script>
