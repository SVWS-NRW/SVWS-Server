<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Telefonarten</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="telefonarten"
				v-model:clicked="selectedTelefonart"
				:items="manager().filtered()" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisibleTelefonarten">Nur Sichtbare</svws-ui-checkbox>
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
							Neue Telefonart anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { Telefonart } from "@core/core/data/schule/Telefonart";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import type { DataTableColumn } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";
	import { computed } from "vue";
	import type { TelefonartenAuswahlProps } from "~/components/schule/kataloge/telefonarten/TelefonartenAuswahlProps";

	const props = defineProps<TelefonartenAuswahlProps>();
	const benutzerState = useBenutzerState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
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

	const telefonarten = computed<Telefonart[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: Telefonart[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const showOnlyVisibleTelefonarten = computed<boolean>({
		get: () => props.manager().filterNurSichtbar,
		set: (value) => {
			props.manager().filterNurSichtbar = value;
			void props.setFilter();
		},
	});

	const selectedTelefonart = computed<Telefonart | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: Telefonart | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	function setAuswahl(items: Telefonart[]) {
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
