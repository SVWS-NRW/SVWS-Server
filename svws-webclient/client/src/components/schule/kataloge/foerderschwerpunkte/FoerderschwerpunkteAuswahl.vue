<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Förderschwerpunkte</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="foerderschwerpunkte"
				v-model:clicked="selectedFoerderschwerpunkt"
				:items="rowsFiltered" :columns
				clickable :selectable="!readonly" count :focus-help-visible :focus-switching-enabled scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="sichtbareFoerderschwerpunkte">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions v-if="!readonly">
					<svws-ui-tooltip position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries" :disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neuen Förderschwerpunkt anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { FoerderschwerpunkteAuswahlProps } from "~/components/schule/kataloge/foerderschwerpunkte/FoerderschwerpunkteAuswahlProps";
	import { computed, ref } from "vue";
	import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import type { DataTableColumn } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";

	const props = defineProps<FoerderschwerpunkteAuswahlProps>();
	const benutzerState = useBenutzerState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isHinzufuegenView = computed<boolean>(() => props.activeViewType === ViewType.HINZUFUEGEN);
	const isGruppenprozesseOrHinzufuegenView = computed<boolean>(() => (props.activeViewType === ViewType.GRUPPENPROZESSE) || isHinzufuegenView.value);
	const noFilteredEntries = computed<boolean>(() => props.manager().filtered().size() === 0);
	const searchTerm = ref<string>("");

	const rowsFiltered = computed<FoerderschwerpunktEintrag[]>(() => {
		const term = searchTerm.value.trim();
		if (term === '') {
			return [...props.manager().filtered()];
		}

		const isNumber = /^\d+$/.test(searchTerm.value.trim());
		const termLower = searchTerm.value.toLocaleLowerCase();

		const arr = [];
		for (const e of props.manager().filtered()) {
			if ((isNumber && (e.id.toString().includes(term)))
				|| e.kuerzel.toLocaleLowerCase().includes(termLower)
				|| e.kuerzelStatistik.toLocaleLowerCase().includes(term)) {
				arr.push(e);
			}
		}
		return arr;
	});

	const foerderschwerpunkte = computed<FoerderschwerpunktEintrag[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: FoerderschwerpunktEintrag[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const sichtbareFoerderschwerpunkte = computed<boolean>({
		get: () => props.manager().filterNurSichtbar(),
		set: (value: boolean) => {
			props.manager().setFilterNurSichtbar(value);
			void props.setFilter();
		},
	});

	const selectedFoerderschwerpunkt = computed<FoerderschwerpunktEintrag | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: FoerderschwerpunktEintrag | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	const columns: DataTableColumn[] = [
		{ key: "kuerzelStatistik", label: "Statistik-Kürzel", defaultSort: 'asc', sortable: true },
		{ key: "kuerzel", label: "interne Bezeichnung", sortable: true, span: 2 },
	];

	function setAuswahl(foerderschwerpunkte: FoerderschwerpunktEintrag[]): void {
		props.manager().liste.auswahlClear();
		for (const foerderschwerpunkt of foerderschwerpunkte) {
			if (props.manager().liste.hasValue(foerderschwerpunkt)) {
				props.manager().liste.auswahlAdd(foerderschwerpunkt);
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
