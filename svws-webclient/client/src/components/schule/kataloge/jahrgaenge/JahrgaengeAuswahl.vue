<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Jahrgänge</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="jahrgaenge"
				v-model:clicked="selectedJahrgaenge"
				:items="props.manager().filtered()" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisibleJahrgaenge">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions v-if="!readonly">
					<svws-ui-tooltip v-if="ServerMode.DEV.checkServerMode(serverMode)" position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries" :disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neuen Jahrgang anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { JahrgaengeAuswahlProps } from "./JahrgaengeAuswahlProps";
	import { computed } from "vue";
	import { useRegionSwitch, ViewType } from "@ui";
	import { BenutzerKompetenz, ServerMode } from "@core";
	import type { JahrgangsDaten } from "@core";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const props = defineProps<JahrgaengeAuswahlProps>();
	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
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

	const jahrgaenge = computed<JahrgangsDaten[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: JahrgangsDaten[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const showOnlyVisibleJahrgaenge = computed<boolean>({
		get: () => props.manager().filterNurSichtbar(),
		set: (value: boolean) => {
			props.manager().setFilterNurSichtbar(value);
			void props.setFilter();
		},
	});

	const selectedJahrgaenge = computed<JahrgangsDaten | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: JahrgangsDaten | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 },
	];

	function setAuswahl(jahrgangsdaten: JahrgangsDaten[]): void {
		props.manager().liste.auswahlClear();
		for (const data of jahrgangsdaten) {
			if (props.manager().liste.hasValue(data)) {
				props.manager().liste.auswahlAdd(data);
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
