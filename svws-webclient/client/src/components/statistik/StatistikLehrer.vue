<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-statistik-lehrer /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page flex gap-8">
		<ui-table-grid :manager="() => gridManager" class="w-fit">
			<template #default="{ row: [lehrer, fehler, anzahl] }">
				<template v-if="(fehler === null) && (lehrer === null)">
					<td class="text-left bg-ui-50">
						—
					</td>
					<td class="flex flex-row justify-between text-left bg-ui-50">
						<div>Allgemein</div>
						<div>{{ anzahl }} Fehler</div>
					</td>
				</template>
				<template v-else-if="(fehler === null) && (lehrer !== null)">
					<td class="text-left bg-ui-50">
						{{ lehrer.kuerzel }}
					</td>
					<td class="flex flex-row justify-between text-left bg-ui-50">
						<div>{{ lehrer.nachname }}, {{ lehrer.vorname }}</div>
						<div>{{ anzahl }} Fehler</div>
					</td>
				</template>
				<template v-else-if="fehler !== null">
					<td class="text-left p-1">
						<div class="flex flex-row gap-2 justify-between items-center">
							<span class="max-w-fit px-1 bg-ui-selected border-ui-selected text-ui-onselected border font-mono text-xs rounded-sm">{{ fehler.getFehlercode() }}</span>
							<span class="icon" :class="getIconClass(fehler.getFehlerart())" />
						</div>
					</td>
					<td class="text-left">
						{{ fehler.getFehlermeldung() }}
					</td>
				</template>
			</template>
		</ui-table-grid>
		<div v-if="statistikState.lehrerListeManager.auswahlID() !== null">
			<div class="font-bold ml-8 mb-4 flex flex-row items-center" @click="gotoLehrer(statistikState.lehrerListeManager.auswahl())"><span class="icon cursor-pointer i-ri-link" /> {{ statistikState.lehrerListeManager.auswahl().nachname }}, {{ statistikState.lehrerListeManager.auswahl().vorname }}</div>
			<svws-ui-tab-bar :tab-manager="() => tabManager">
				<lehrer-individualdaten v-if="tabManager.tab.name === 'LS'" :zeige-alles="false" :add-leitungsfunktion :delete-leitungsfunktionen
					:get-list-leitungsfunktionen :map-leitungsfunktionen :patch-leitungsfunktion :lehrer-liste-manager="() => statistikState.lehrerListeManager" :patch />
				<lehrer-personaldaten v-if="tabManager.tab.name === 'LP'" :add-anrechnung :add-fachrichtung :add-lehramt :add-lehrbefaehigung :add-lehrer-unterrichtsfach
					:add-mehrleistung :add-minderleistung :lehrer-liste-manager="() => statistikState.lehrerListeManager" :lehrer-unterrichtsfaecher :map-faecher
					:map-schulen :patch-abschnittsdaten :patch-anrechnungen :patch-fachrichtung :patch-lehramt :patch-lehrbefaehigung :patch-lehrer-unterrichtsfach :patch-mehrleistung :patch-minderleistung :patch-personaldaten
					:remove-anrechnung :remove-fachrichtungen :remove-lehraemter :remove-lehrbefaehigungen :remove-lehrer-unterrichtsfach :remove-mehrleistung :remove-minderleistung />
			</svws-ui-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, watch } from 'vue';

	import type { LehrerStatistikGesamt } from '@core/asd/data/statistik/LehrerStatistikGesamt';
	import type { Validator } from '@core/asd/validate/Validator';
	import type { ValidatorFehler } from '@core/asd/validate/ValidatorFehler';
	import { ValidatorFehlerart } from '@core/asd/validate/ValidatorFehlerart';
	import { ArrayList } from '@core/java/util/ArrayList';
	import type { List } from '@core/java/util/List';
	import { useStatistikState } from '@ui/states/statistik/StatistikState';
	import { useRegionSwitch } from '@ui/ui/composables/useRegionSwitch';
	import { GridManager } from '@ui/ui/controls/tablegrid/GridManager';
	import { TabManager } from '@ui/ui/nav/TabManager';

	import type { StatistikLehrerProps } from './StatistikLehrerProps';

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const props = defineProps<StatistikLehrerProps>();
	const statistikState = useStatistikState();

	function getIconClass(fehler: ValidatorFehlerart) {
		return {
			'i-ri-alert-fill': (fehler.ordinal() === ValidatorFehlerart.MUSS.ordinal()),
			'icon-ui-danger': (fehler.ordinal() === ValidatorFehlerart.MUSS.ordinal()),
			'i-ri-error-warning-fill': (fehler.ordinal() === ValidatorFehlerart.KANN.ordinal()),
			'icon-ui-caution': (fehler.ordinal() === ValidatorFehlerart.KANN.ordinal()),
			'i-ri-question-fill': (fehler.ordinal() === ValidatorFehlerart.HINWEIS.ordinal()),
			'icon-ui-warning': (fehler.ordinal() === ValidatorFehlerart.HINWEIS.ordinal()),
		};
	}

	const gridManager = new GridManager<string, readonly [LehrerStatistikGesamt | null, ValidatorFehler | null, number], List<readonly [LehrerStatistikGesamt | null, ValidatorFehler | null, number]>>({
		daten: computed(() => {
			const liste = new ArrayList<readonly [LehrerStatistikGesamt | null, ValidatorFehler | null, number]>();
			const allgFehler = statistikState.validatorGesamt.getFehlerByLehrerID(-1);
			if (!allgFehler.isEmpty()) {
				liste.add([null, null, allgFehler.size()]);
				for (let i = 0; i < allgFehler.size(); i++) {
					liste.add([null, allgFehler.get(i), i]);
				}
			}
			for (const lehrer of statistikState.statistikGesamt.lehrer) {
				const fehlerListe = statistikState.validatorGesamt.getFehlerByLehrerID(lehrer.id);
				if (!fehlerListe.isEmpty()) {
					liste.add([lehrer, null, fehlerListe.size()]);
					for (let i = 0; i < fehlerListe.size(); i++) {
						liste.add([lehrer, fehlerListe.get(i), i]);
					}
				}
			}
			return liste;
		}),
		getRowKey: row => `ID_${row[0] === null ? 'allgemein' : row[0].id}_${row[2]}`,
		allowEmptyRowSelection: true,
		columns: [
			// { kuerzel: "Auswahl", name: "Auswahl", width: "3rem", hideable: false },
			{ kuerzel: "Name", name: "Name", width: '6rem' },
			// { kuerzel: "Validator", name: "Validator", width: '1fr' },
			{ kuerzel: "Fehlermeldung", name: "Fehlermeldung", width: '32rem' },
		],
	});

	watch(() => gridManager.focusRow, async () => {
		if (gridManager.focusRow === null) {
			return null;
		}
		const [lehrer, fehler] = gridManager.daten.get(gridManager.focusRow);
		if (lehrer === null) {
			return null;
		}
		await statistikState.setLehrer(lehrer.id);
		let tab = tabManager.getTab("LS");
		if (fehler !== null) {
			const validator = fehler.getValidator() as Validator;
			const key = validator.getFehlercodePraefix().slice(0, 2);
			if (tabManager.existsTab(key)) {
				tab = tabManager.getTab(key);
			}
		}
		await tabManager.setTab(tab);
	});

	const tabs = [
		{ name: "LS", text: "Stammdaten" },
		{ name: "LP", text: "Personaldaten" },
	];

	const tabManager = new TabManager(tabs, tabs[0], async () => void 0);

</script>
