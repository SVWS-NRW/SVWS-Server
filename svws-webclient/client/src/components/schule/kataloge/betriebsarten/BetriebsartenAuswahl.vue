<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Betriebsarten</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="betriebsarten"
				v-model:clicked="selectedBetriebsart"
				:items="manager().filtered()" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisibleBetriebsarten">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions>
					<svws-ui-tooltip position="bottom" v-if="ServerMode.DEV.checkServerMode(serverMode)">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries"
							:disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neue Betriebsart anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { BetriebsartenAuswahlProps } from './BetriebsartenAuswahlProps';
	import type { DataTableColumn } from "@ui";
	import { useRegionSwitch, ViewType } from "@ui";
	import type { Betriebsart } from "@core";
	import { BenutzerKompetenz, ServerMode } from "@core";
	import { computed } from "vue";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const props = defineProps<BetriebsartenAuswahlProps>();
	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isHinzufuegenView = computed<boolean>(() => props.activeViewType === ViewType.HINZUFUEGEN);
	const isGruppenprozesseOrHinzufuegenView = computed<boolean>(() => (props.activeViewType === ViewType.GRUPPENPROZESSE) || isHinzufuegenView.value);
	const noFilteredEntries = computed<boolean>(() => props.manager().filtered().size() === 0);

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc" },
	];

	const searchTerm = computed<string>({
		get: () => props.manager().searchTerm,
		set: (v: string) => {
			props.manager().searchTerm = v;
			void props.setFilter();
		},
	});

	const betriebsarten = computed<Betriebsart[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: Betriebsart[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const showOnlyVisibleBetriebsarten = computed<boolean>({
		get: () => props.manager().filterNurSichtbar,
		set: (value) => {
			props.manager().filterNurSichtbar = value;
			void props.setFilter();
		},
	});

	const selectedBetriebsart = computed<Betriebsart | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: Betriebsart | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	function setAuswahl(items: Betriebsart[]) {
		props.manager().liste.auswahlClear();
		for (const item of items) {
			if (props.manager().liste.hasValue(item)) {
				props.manager().liste.auswahlAdd(item);
			}
		}
	}

	async function navigateToView() {
		if (props.manager().liste.auswahlExists()) {
			await props.gotoGruppenprozessView(true);
		} else {
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
		}
	}

</script>
