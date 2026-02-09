<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Erzieherarten</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="erzieherarten"
				v-model:clicked="selectedErzieherarten"
				:items="props.manager().filtered()" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisibleErzieherarten">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions v-if="!readonly">
					<svws-ui-tooltip position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries" :disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neue Erzieherart anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { Erzieherart } from "@core";
	import { BenutzerKompetenz } from "@core";
	import type { DataTableColumn } from "@ui";
	import { useRegionSwitch, ViewType } from "@ui";
	import type { ErzieherartenAuswahlProps } from "~/components/schule/kataloge/erzieherarten/ErzieherartenAuswahlProps";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const props = defineProps<ErzieherartenAuswahlProps>();
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

	const erzieherarten = computed<Erzieherart[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: Erzieherart[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const showOnlyVisibleErzieherarten = computed<boolean>({
		get: () => props.manager().filterNurSichtbar,
		set: (value: boolean) => {
			props.manager().filterNurSichtbar = value;
			void props.setFilter();
		},
	});

	const selectedErzieherarten = computed<Erzieherart | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: Erzieherart | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc", span: 2 },
	];

	function setAuswahl(erzieherarten: Erzieherart[]): void {
		props.manager().liste.auswahlClear();
		for (const data of erzieherarten) {
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
