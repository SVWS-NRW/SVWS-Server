<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Ortsteile</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="ortsteile"
				v-model:clicked="selectedOrtsteile"
				:items="props.manager().filtered()" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisibleOrtsteile">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions v-if="!readonly">
					<svws-ui-tooltip position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries" :disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neuen Ortsteil anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { useRegionSwitch, ViewType } from "@ui";
	import type { OrtsteilKatalogEintrag } from "@core";
	import { BenutzerKompetenz } from "@core";
	import type { OrtsteileAuswahlProps } from "~/components/schule/kataloge/ortsteile/OrtsteileAuswahlProps";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const props = defineProps<OrtsteileAuswahlProps>();
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

	const ortsteile = computed<OrtsteilKatalogEintrag[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: OrtsteilKatalogEintrag[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const showOnlyVisibleOrtsteile = computed<boolean>({
		get: () => props.manager().filterNurSichtbar,
		set: (value: boolean) => {
			props.manager().filterNurSichtbar = value;
			void props.setFilter();
		},
	});

	const selectedOrtsteile = computed<OrtsteilKatalogEintrag | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: OrtsteilKatalogEintrag | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	const columns = [
		{ key: "plzOrt", label: "PLZ", sortable: true, defaultSort: "asc", span: 1 },
		{ key: "bezeichnungOrt", label: "Ort", sortable: true, defaultSort: "asc", span: 2 },
		{ key: "ortsteil", label: "Ortsteil", sortable: true, defaultSort: "asc", span: 2 },
	];

	function setAuswahl(ortsteile: OrtsteilKatalogEintrag[]): void {
		props.manager().liste.auswahlClear();
		for (const data of ortsteile) {
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
