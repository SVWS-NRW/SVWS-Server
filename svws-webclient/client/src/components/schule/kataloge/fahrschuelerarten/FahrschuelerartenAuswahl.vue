<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Fahrschülerarten</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="fahrschuelerarten"
				v-model:clicked="selectedFahrschuelerarten"
				:items="props.manager().filtered()" :columns
				clickable :selectable="hatKompetenzAendern" count :focus-help-visible :focus-switching-enabled scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input type="search" placeholder="Suchen"
						v-model="searchTerm"
						removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle"
						v-model="visibleFahrschuelerarten">
						Nur Sichtbare
					</svws-ui-checkbox>
				</template>
				<template #actions v-if="hatKompetenzAendern">
					<svws-ui-tooltip position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries" :disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neue Fahrschülerart anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { FahrschuelerartenAuswahlProps } from "~/components/schule/kataloge/fahrschuelerarten/FahrschuelerartenAuswahlProps";
	import { useRegionSwitch, ViewType } from "@ui";
	import type { Fahrschuelerart } from "@core";
	import { BenutzerKompetenz } from "@core";
	import { computed } from "vue";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const props = defineProps<FahrschuelerartenAuswahlProps>();
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

	const fahrschuelerarten = computed<Fahrschuelerart[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: Fahrschuelerart[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const visibleFahrschuelerarten = computed<boolean>({
		get: () => props.manager().filterNurSichtbar,
		set: (value: boolean) => {
			props.manager().filterNurSichtbar = value;
			void props.setFilter();
		},
	});

	const selectedFahrschuelerarten = computed<Fahrschuelerart | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: Fahrschuelerart | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc" },
	];

	function setAuswahl(fahrschuelerarten: Fahrschuelerart[]): void {
		props.manager().liste.auswahlClear();
		for (const fahrschuelerart of fahrschuelerarten) {
			if (props.manager().liste.hasValue(fahrschuelerart)) {
				props.manager().liste.auswahlAdd(fahrschuelerart);
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
