<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Floskelgruppe</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="floskelgruppen"
				v-model:clicked="selectedFloskelgruppe"
				:items="manager().filtered()" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input placeholder="Suchen" v-model="searchTerm" type="search" />
				</template>
				<template #actions>
					<svws-ui-tooltip position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries"
							:disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neue Floskelgruppe anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { FloskelgruppenAuswahlProps } from "./FloskelgruppenAuswahlProps";
	import type { Floskelgruppe } from "@core/core/data/schule/Floskelgruppe";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import type { DataTableColumn } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";

	const props = defineProps<FloskelgruppenAuswahlProps>();
	const benutzerState = useBenutzerState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isHinzufuegenView = computed<boolean>(() => props.activeViewType === ViewType.HINZUFUEGEN);
	const isGruppenprozesseorHinhzufuegenView = computed<boolean>(() => (props.activeViewType === ViewType.GRUPPENPROZESSE) || isHinzufuegenView.value);
	const noFilteredEntries = computed<boolean>(() => props.manager().filtered().size() === 0);

	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2, defaultSort: 'asc' },
	];
	const searchTerm = computed<string>({
		get: () => props.manager().searchTerm,
		set: (v: string) => {
			props.manager().searchTerm = v;
			void props.setFilter();
		},
	});

	const floskelgruppen = computed<Floskelgruppe[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: Floskelgruppe[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const selectedFloskelgruppe = computed<Floskelgruppe | null>({
		get: () => (!isGruppenprozesseorHinhzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: Floskelgruppe | null) => void props.gotoDefaultView(v?.id ?? null),
	});


	function setAuswahl(items: Floskelgruppe[]) {
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
