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
				v-model:sort-by-and-order="sortByAndOrder"
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
				<template #filter>
					<div class="col-span-full flex flex-wrap gap-x-5">
						<svws-ui-checkbox type="toggle" v-model="filterNurAktiv">Nur Aktive</svws-ui-checkbox>
					</div>
				</template>
				<template #cell(bezeichnung)="{ value }">
					{{ value }}
				</template>
				<template #cell(gueltigAb)="{ value }">
					{{ (DateUtils.isValidDate(value) ? DateUtils.gibDatumGermanFormat(value) : '') }}
				</template>
				<template #cell(gueltigBis)="{ value }">
					{{ (DateUtils.isValidDate(value) ? DateUtils.gibDatumGermanFormat(value) : '') }}
				</template>
				<template #cell(aktiv)="{ value }">
					<span v-if="value" class="icon icon-ui-brand i-ri-checkbox-circle-fill ml-2 hover:opacity-50" title="Dieser Stundenplan ist aktiv" />
				</template>
				<template #actions>
					<svws-ui-tooltip position="bottom" v-if="hatKompetenzAendern">
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

	import { computed } from "vue";
	import type { StundenplanAuswahlProps } from "./SStundenplanAuswahlProps";
	import type { DataTableColumn, SortByAndOrder } from "@ui";
	import { ViewType } from "@ui";
	import { BenutzerKompetenz, DateUtils } from "@core";
	import type { StundenplanListeEintrag } from "@core";
	import {useRegionSwitch} from "~/components/useRegionSwitch";

	const props = defineProps<StundenplanAuswahlProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.STUNDENPLAN_AENDERN));

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: 'asc' },
		{ key: "gueltigAb", label: "von", fixedWidth: 7, sortable: true, defaultSort: 'asc' },
		{ key: "gueltigBis", label: "bis", fixedWidth: 7 },
		{ key: "aktiv", label: "", fixedWidth: 2 },
	];

	const unselectable = computed<Set<StundenplanListeEintrag>>(() => new Set([props.manager().getStundenplanVorlage()]));

	const rowsFiltered = computed<StundenplanListeEintrag[]>(() => {
		const arr = [];
		for (const e of props.manager().filtered())
			arr.push(e);
		return arr;
	});

	const filterNurAktiv = computed<boolean>({
		get: () => props.manager().filterNurAktiv(),
		set: (value) => {
			props.manager().setFilterNurAktiv(value);
			void props.setFilter();
			// void props.setFilterNurAktiv(value);
		},
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

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = props.manager().orderGet();
			if (list.isEmpty())
				return undefined;
			else {
				const { a: key, b: order} = list.get(0);
				return { key, order };
			}
		},
		set: (value) => {
			if ((value === undefined) || (value.key === null))
				return;
			props.manager().orderUpdate(value.key, value.order);
			void props.setFilter();
		},
	})

</script>
