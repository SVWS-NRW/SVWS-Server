<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Kindergärten</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="kindergaerten"
				v-model:clicked="selectedKindergaerten"
				:items="rowsFiltered" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="sichtbareKindergaerten">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions v-if="!readonly">
					<svws-ui-tooltip position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries" :disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neuen Kindergarten anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { KindergaertenAuswahlProps } from "~/components/schule/kataloge/kindergaerten/KindergaertenAuswahlProps";
	import type { DataTableColumn } from "@ui";
	import type { Kindergarten } from "@core";
	import { BenutzerKompetenz } from "@core";
	import { useRegionSwitch, ViewType } from "@ui";
	import { computed, ref } from "vue";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const props = defineProps<KindergaertenAuswahlProps>();
	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isHinzufuegenView = computed<boolean>(() => props.activeViewType === ViewType.HINZUFUEGEN);
	const isGruppenprozesseOrHinzufuegenView = computed<boolean>(() => (props.activeViewType === ViewType.GRUPPENPROZESSE) || isHinzufuegenView.value);
	const noFilteredEntries = computed<boolean>(() => props.manager().filtered().size() === 0);
	const searchTerm = ref<string>("");

	const rowsFiltered = computed<Kindergarten[]>(() => {
		const term = searchTerm.value.trim();
		if (term === '') {
			return [...props.manager().filtered()];
		}

		const termLower = searchTerm.value.toLocaleLowerCase();

		const result = [];
		for (const e of props.manager().filtered()) {
			if (e.bezeichnung.toLocaleLowerCase().includes(termLower)) {
				result.push(e);
			}
		}
		return result;
	});

	const kindergaerten = computed<Kindergarten[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: Kindergarten[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const selectedKindergaerten = computed<Kindergarten | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: Kindergarten | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	function setAuswahl(kindergaerten: Kindergarten[]) {
		props.manager().liste.auswahlClear();
		for (const kindergarten of kindergaerten) {
			if (props.manager().liste.hasValue(kindergarten)) {
				props.manager().liste.auswahlAdd(kindergarten);
			}
		}
	}

	const sichtbareKindergaerten = computed<boolean>({
		get: () => props.manager().filterNurSichtbar,
		set: (value) => {
			props.manager().filterNurSichtbar = value;
			void props.setFilter();
		},
	});

	async function navigateToView(): Promise<void> {
		if (props.manager().liste.auswahlExists()) {
			await props.gotoGruppenprozessView(true);
		} else {
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
		}
	}

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc" },
	];

</script>
