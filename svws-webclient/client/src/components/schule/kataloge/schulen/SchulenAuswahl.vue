<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Schulen</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="schulen"
				v-model:clicked="selectedSchulen"
				:items="manager().filtered()" :columns
				clickable :selectable="hatKompetenzAendern" count :focus-help-visible :focus-switching-enabled scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input type="search" placeholder="Suchen (Ort, Schulnr., Kürzel, Kurzbez.)"
						v-model="searchTerm"
						removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle"
						v-model="visibleSchulen">
						Nur Sichtbare
					</svws-ui-checkbox>
				</template>
				<template #actions v-if="hatKompetenzAendern">
					<svws-ui-tooltip v-if="ServerMode.DEV.checkServerMode(serverMode)" position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries" :disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neue Schule anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { SchulenAuswahlProps } from "~/components/schule/kataloge/schulen/SchulenAuswahlProps";
	import { computed } from 'vue';
	import { BenutzerKompetenz, type SchulEintrag, ServerMode } from "@core";
	import { useRegionSwitch, ViewType } from "@ui";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const props = defineProps<SchulenAuswahlProps>();
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

	const schulen = computed<SchulEintrag[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: SchulEintrag[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const visibleSchulen = computed<boolean>({
		get: () => props.manager().filterNurSichtbar,
		set: (value: boolean) => {
			props.manager().filterNurSichtbar = value;
			void props.setFilter();
		},
	});

	const selectedSchulen = computed<SchulEintrag | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: SchulEintrag | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "kurzbezeichnung", label: "Kurzbezeichnung", sortable: true, span: 4 },
	];

	function setAuswahl(schulen: SchulEintrag[]): void {
		props.manager().liste.auswahlClear();
		for (const schule of schulen) {
			if (props.manager().liste.hasValue(schule)) {
				props.manager().liste.auswahlAdd(schule);
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
