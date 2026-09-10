<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-statistik-klassen /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page flex gap-8">
		<ui-table-grid :manager="() => gridManager" class="w-fit">
			<template #default="{ row: [eintrag, fehler, anzahl] }">
				<template v-if="(fehler === null) && (eintrag === null)">
					<td class="text-left bg-ui-50">
						—
					</td>
					<td class="flex flex-row justify-between text-left bg-ui-50">
						<div>Allgemein</div>
						<div>{{ anzahl }} Fehler</div>
					</td>
				</template>
				<template v-else-if="(fehler === null) && (eintrag !== null)">
					<td class="text-left bg-ui-50">
						{{ eintrag.kuerzel }}
					</td>
					<td class="flex flex-row justify-between text-left bg-ui-50">
						<div>{{ Jahrgaenge.data().getEintragByID(eintrag.idJahrgang)?.text ?? 'Jahrgang fehlt' }}</div>
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
		<div v-if="statistikState.klassenListeManager.auswahlID() !== null">
			<div class="font-bold ml-8 mb-4 flex flex-row items-center" @click="gotoKlasse(statistikState.klassenListeManager.auswahl())"><span class="icon cursor-pointer i-ri-link" /> {{ statistikState.klassenListeManager.auswahl().kuerzel }}</div>
			<svws-ui-tab-bar :tab-manager="() => tabManager">
				<klassen-daten v-if="tabManager.tab.name === 'K'" />
			</svws-ui-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, watch } from 'vue';

	import type { KlassenStatistikGesamt } from '@core/asd/data/statistik/KlassenStatistikGesamt';
	import { Jahrgaenge } from '@core/asd/types/jahrgang/Jahrgaenge';
	import type { Validator } from '@core/asd/validate/Validator';
	import type { ValidatorFehler } from '@core/asd/validate/ValidatorFehler';
	import { ValidatorFehlerart } from '@core/asd/validate/ValidatorFehlerart';
	import { ArrayList } from '@core/java/util/ArrayList';
	import type { List } from '@core/java/util/List';
	import { useStatistikState } from '@ui/states/statistik/StatistikState';
	import { useRegionSwitch } from '@ui/ui/composables/useRegionSwitch';
	import { GridManager } from '@ui/ui/controls/tablegrid/GridManager';
	import { TabManager } from '@ui/ui/nav/TabManager';

	import type { StatistikKlassenProps } from './StatistikKlassenProps';

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const props = defineProps<StatistikKlassenProps>();
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

	const gridManager = new GridManager<string, readonly [KlassenStatistikGesamt | null, ValidatorFehler | null, number], List<readonly [KlassenStatistikGesamt | null, ValidatorFehler | null, number]>>({
		daten: computed(() => {
			const liste = new ArrayList<readonly [KlassenStatistikGesamt | null, ValidatorFehler | null, number]>();
			const allgFehler = statistikState.validatorGesamt.getFehlerByKlassenID(-1);
			if (!allgFehler.isEmpty()) {
				liste.add([null, null, allgFehler.size()]);
				for (let i = 0; i < allgFehler.size(); i++) {
					liste.add([null, allgFehler.get(i), i]);
				}
			}
			for (const eintrag of statistikState.statistikGesamt.klassen) {
				const fehlerListe = statistikState.validatorGesamt.getFehlerByKlassenID(eintrag.id);
				if (!fehlerListe.isEmpty()) {
					liste.add([eintrag, null, fehlerListe.size()]);
					for (let i = 0; i < fehlerListe.size(); i++) {
						liste.add([eintrag, fehlerListe.get(i), i]);
					}
				}
			}
			return liste;
		}),
		getRowKey: row => `ID_${row[0] === null ? 'allgemein' : row[0].id}_${row[2]}`,
		allowEmptyRowSelection: true,
		columns: [
			// { kuerzel: "Auswahl", name: "Auswahl", width: "3rem", hideable: false },
			{ kuerzel: "Name", name: "Name", width: '8rem' },
			// { kuerzel: "Validator", name: "Validator", width: '1fr' },
			{ kuerzel: "Fehlermeldung", name: "Fehlermeldung", width: '32rem' },
		],
	});

	watch(() => gridManager.focusRow, async () => {
		if (gridManager.focusRow === null) {
			return null;
		}
		const [eintrag, fehler] = gridManager.daten.get(gridManager.focusRow);
		if (eintrag === null) {
			return null;
		}
		await statistikState.setKlasse(eintrag.id);
		let tab = tabManager.getTab("K");
		if (fehler !== null) {
			const validator = fehler.getValidator() as Validator;
			const key = validator.getFehlercodePraefix().slice(0, 1);
			if (tabManager.existsTab(key)) {
				tab = tabManager.getTab(key);
			}
		}
		await tabManager.setTab(tab);
	});

	const tabs = [
		{ name: "K", text: "Klassendaten" },
	];

	const tabManager = new TabManager(tabs, tabs[0], async () => void 0);

</script>
