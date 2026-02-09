<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Beschäftigungsarten</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="beschaeftigungsarten"
				v-model:clicked="selectedBeschaeftigungsarten"
				:items="props.manager().filtered()" :columns
				clickable :selectable="hatKompetenzAendern" count :focus-help-visible :focus-switching-enabled scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input type="search" placeholder="Suchen"
						v-model="searchTerm"
						removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle"
						v-model="visibleBeschaeftigungsarten">
						Nur Sichtbare
					</svws-ui-checkbox>
				</template>
				<template #actions v-if="hatKompetenzAendern">
					<svws-ui-tooltip v-if="ServerMode.DEV.checkServerMode(serverMode)" position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries" :disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neue Beschäftigungsart anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { BeschaeftigungsartenAuswahlProps } from "~/components/schule/kataloge/beschaeftigungsarten/BeschaeftigungsartenAuswahlProps";
	import type { DataTableColumn } from "@ui";
	import type { Beschaeftigungsart } from "@core";
	import { useRegionSwitch, ViewType } from "@ui";
	import { BenutzerKompetenz, ServerMode } from "@core";
	import { computed } from "vue";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const props = defineProps<BeschaeftigungsartenAuswahlProps>();
	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isHinzufuegenView = computed<boolean>(() => props.activeViewType === ViewType.HINZUFUEGEN);
	const isGruppenprozesseOrHinzufuegenView = computed<boolean>(() => (props.activeViewType === ViewType.GRUPPENPROZESSE) || isHinzufuegenView.value);
	const noFilteredEntries = computed<boolean>(() => props.manager().filtered().size() === 0);
	const searchTerm = computed<string>({
		get: () => props.manager().searchTerm,
		set: (v: string) => {
			props.manager().searchTerm = v;
			void props.setFilter();
		},
	});

	const beschaeftigungsarten = computed<Beschaeftigungsart[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: Beschaeftigungsart[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const visibleBeschaeftigungsarten = computed<boolean>({
		get: () => props.manager().filterNurSichtbar,
		set: (value: boolean) => {
			props.manager().filterNurSichtbar = value;
			void props.setFilter();
		},
	});

	const selectedBeschaeftigungsarten = computed<Beschaeftigungsart | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: Beschaeftigungsart | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2, defaultSort: 'asc' },
	];

	function setAuswahl(beschaeftigungsarten: Beschaeftigungsart[]): void {
		props.manager().liste.auswahlClear();
		for (const beschaeftigungsart of beschaeftigungsarten) {
			if (props.manager().liste.hasValue(beschaeftigungsart)) {
				props.manager().liste.auswahlAdd(beschaeftigungsart);
			}
		}
	}

	async function navigateToView(): Promise<void> {
		if (props.manager().liste.auswahlExists()) {
			await props.gotoGruppenprozessView(true);
		} else {
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
		}
	}

</script>
