<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline gap-1">
			<h1>Abteilungen</h1>
			<div class="opacity-50 font-bold text-base">{{ getTextFolgeAbschnitt() }}</div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="abteilungen"
				v-model:clicked="selectedAbteilungen"
				:items="props.manager().filtered()" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisibleAbteilungen">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions v-if="!readonly">
					<svws-ui-tooltip position="top">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries" :disabled="isHinzufuegenDisabled">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neue Abteilung anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { AbteilungenAuswahlProps } from "~/components/schule/kataloge/abteilungen/AbteilungenAuswahlProps";
	import { computed } from "vue";
	import type { Abteilung } from "@core/core/data/schule/Abteilung";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import type { DataTableColumn } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";

	const props = defineProps<AbteilungenAuswahlProps>();
	const benutzerState = useBenutzerState();
	const abschnittState = useAbschnittState();
	const schuleState = useSchuleState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const isHinzufuegenView = computed<boolean>(() => props.activeViewType === ViewType.HINZUFUEGEN);
	const isGruppenprozesseOrHinzufuegenView = computed<boolean>(() => (props.activeViewType === ViewType.GRUPPENPROZESSE) || isHinzufuegenView.value);
	const noFilteredEntries = computed<boolean>(() => props.manager().filtered().size() === 0);
	const isHinzufuegenDisabled = computed(() => isHinzufuegenView.value
		|| ((abschnittState.auswahl.id !== schuleState.abschnitt.id)
			&& (abschnittState.auswahl.id !== schuleState.abschnitt.idFolgeAbschnitt)));
	const searchTerm = computed<string>({
		get: () => props.manager().searchTerm,
		set: (v: string) => {
			props.manager().searchTerm = v;
			void props.setFilter();
		},
	});

	const abteilungen = computed<Abteilung[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: Abteilung[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const showOnlyVisibleAbteilungen = computed<boolean>({
		get: () => props.manager().filterNurSichtbar,
		set: (value: boolean) => {
			props.manager().filterNurSichtbar = value;
			void props.setFilter();
		},
	});

	const selectedAbteilungen = computed<Abteilung | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: Abteilung | null) => void props.gotoDefaultView(v?.id ?? null),
	});
	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc" },
	];

	function setAuswahl(abteilungen: Abteilung[]): void {
		props.manager().liste.auswahlClear();
		for (const data of abteilungen) {
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

	function getTextFolgeAbschnitt() {
		const aktAbschnitt = abschnittState.auswahl;
		return aktAbschnitt.schuljahr > 0 ? `${aktAbschnitt.schuljahr}/${(aktAbschnitt.schuljahr + 1) % 100}.${aktAbschnitt.abschnitt}` : "Abschnitt";
	}
</script>
