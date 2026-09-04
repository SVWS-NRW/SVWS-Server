<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Einwilligungsarten</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="einwilligungsarten"
				v-model:clicked="selectedEinwilligungsarten"
				:items="rowsFiltered" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="sichtbareEinwilligungsarten">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions v-if="!readonly">
					<svws-ui-tooltip position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries" :disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neue Einwilligungsart anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { EinwilligungenAuswahlProps } from "./EinwilligungsartenAuswahlProps";
	import type { Einwilligungsart } from "@core/core/data/schule/Einwilligungsart";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import type { DataTableColumn } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";

	const props = defineProps<EinwilligungenAuswahlProps>();
	const benutzerState = useBenutzerState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isHinzufuegenView = computed<boolean>(() => props.activeViewType === ViewType.HINZUFUEGEN);
	const isGruppenprozesseOrHinzufuegenView = computed<boolean>(() => (props.activeViewType === ViewType.GRUPPENPROZESSE) || isHinzufuegenView.value);
	const noFilteredEntries = computed<boolean>(() => props.manager().filtered().size() === 0);
	const searchTerm = ref<string>("");

	const rowsFiltered = computed<Einwilligungsart[]>(() => {
		const term = searchTerm.value.trim();
		if (term === '') {
			return [...props.manager().filtered()];
		}

		const termLower = searchTerm.value.toLocaleLowerCase();

		const arr = [];
		for (const e of props.manager().filtered()) {
			if (e.bezeichnung.toLocaleLowerCase().includes(termLower)) {
				arr.push(e);
			}
		}
		return arr;
	});

	const einwilligungsarten = computed<Einwilligungsart[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: Einwilligungsart[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const selectedEinwilligungsarten = computed<Einwilligungsart | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: Einwilligungsart | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	const sichtbareEinwilligungsarten = computed<boolean>({
		get: () => props.manager().filterNurSichtbar(),
		set: (value) => {
			props.manager().setFilterNurSichtbar(value);
			void props.setFilter();
		},
	});

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc" },
	];

	function setAuswahl(einwilligungsarten: Einwilligungsart[]) {
		props.manager().liste.auswahlClear();
		for (const einwilligungsart of einwilligungsarten) {
			if (props.manager().liste.hasValue(einwilligungsart)) {
				props.manager().liste.auswahlAdd(einwilligungsart);
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
