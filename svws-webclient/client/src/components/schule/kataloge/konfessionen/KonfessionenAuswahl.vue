<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Konfessionen</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="konfessionen"
				v-model:clicked="selectedKonfession"
				:items="manager().filtered()" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisibleKonfessionen">Nur Sichtbare</svws-ui-checkbox>
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
							Neue Konfession anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { BenutzerKompetenz, type ReligionEintrag } from "@core";
	import type { KonfessionenAuswahlProps } from "~/components/schule/kataloge/konfessionen/KonfessionenAuswahlPops";
	import { useRegionSwitch, ViewType } from "@ui";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const props = defineProps<KonfessionenAuswahlProps>();
	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isHinzufuegenView = computed<boolean>(() => props.activeViewType === ViewType.HINZUFUEGEN);
	const isGruppenprozesseOrHinzufuegenView = computed<boolean>(() => (props.activeViewType === ViewType.GRUPPENPROZESSE) || isHinzufuegenView.value);
	const noFilteredEntries = computed<boolean>(() => props.manager().filtered().size() === 0);

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 },
	];

	const searchTerm = computed<string>({
		get: () => props.manager().searchTerm,
		set: (v: string) => {
			props.manager().searchTerm = v;
			void props.setFilter();
		},
	});

	const konfessionen = computed<ReligionEintrag[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: ReligionEintrag[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const showOnlyVisibleKonfessionen = computed<boolean>({
		get: () => props.manager().filterNurSichtbar,
		set: (value) => {
			props.manager().filterNurSichtbar = value;
			void props.setFilter();
		},
	});

	const selectedKonfession = computed<ReligionEintrag | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: ReligionEintrag | null) => void props.gotoDefaultView(v?.id ?? null),
	});



	function setAuswahl(konfessionen: ReligionEintrag[]): void {
		props.manager().liste.auswahlClear();
		for (const item of konfessionen) {
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
