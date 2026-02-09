<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Entlassgründe</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="entlassgruende"
				v-model:clicked="selectedEntlassgrund"
				:items="rowsFiltered" :columns
				clickable :selectable="!readonly" count :focus-help-visible :focus-switching-enabled scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input type="search" placeholder="Suchen"
						v-model="searchTerm"
						removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle"
						v-model="visibleEntlassgruende">
						Nur Sichtbare
					</svws-ui-checkbox>
				</template>
				<template #actions>
					<svws-ui-tooltip position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries" :disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neuen Entlassgrund anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { EntlassgruendeAuswahlProps } from "~/components/schule/kataloge/entlassgruende/EntlassgruendeAuswahlProps";
	import type { DataTableColumn } from "@ui";
	import type { KatalogEntlassgrund } from "@core";
	import { BenutzerKompetenz } from "@core";
	import { useRegionSwitch, ViewType } from "@ui";
	import { computed, ref } from "vue";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const props = defineProps<EntlassgruendeAuswahlProps>();
	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isHinzufuegenView = computed<boolean>(() => props.activeViewType === ViewType.HINZUFUEGEN);
	const isGruppenprozesseOrHinzufuegenView = computed<boolean>(() => (props.activeViewType === ViewType.GRUPPENPROZESSE) || isHinzufuegenView.value);
	const noFilteredEntries = computed<boolean>(() => props.manager().filtered().size() === 0);
	const searchTerm = ref<string>("");

	const rowsFiltered = computed<KatalogEntlassgrund[]>(() => {
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

	const entlassgruende = computed<KatalogEntlassgrund[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: KatalogEntlassgrund[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const visibleEntlassgruende = computed<boolean>({
		get: () => props.manager().filterNurSichtbar(),
		set: (value: boolean) => {
			props.manager().setFilterNurSichtbar(value);
			void props.setFilter();
		},
	});

	const selectedEntlassgrund = computed<KatalogEntlassgrund | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: KatalogEntlassgrund | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc" },
	];

	function setAuswahl(entlassgruende: KatalogEntlassgrund[]): void {
		props.manager().liste.auswahlClear();
		for (const entlassgrund of entlassgruende) {
			if (props.manager().liste.hasValue(entlassgrund)) {
				props.manager().liste.auswahlAdd(entlassgrund);
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
