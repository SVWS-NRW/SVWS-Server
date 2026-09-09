<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-statistik-schueler /> </svws-ui-modal-hilfe>
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
						{{ SchuelerStatus.data().getWertByID(getEintrag(eintrag.id).status).name() }}
					</td>
					<td class="flex flex-row justify-between text-left bg-ui-50">
						<div>{{ getEintrag(eintrag.id).nachname }}, {{ getEintrag(eintrag.id).vorname }}</div>
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
		<div v-if="statistikState.schuelerListeManager.auswahlID() !== null" class="flex-1">
			<div class="font-bold ml-8 mb-4 flex flex-row items-center" @click="gotoSchueler(statistikState.schuelerListeManager.auswahl())"><span class="icon cursor-pointer i-ri-link" /> {{ statistikState.schuelerListeManager.auswahl().nachname }}, {{ statistikState.schuelerListeManager.auswahl().vorname }}</div>
			<svws-ui-tab-bar :tab-manager="() => tabManager">
				<schueler-individualdaten v-if="tabManager.tab.name === 'SS'" :zeige-alles="false" :add-schueler-telefoneintrag
					:delete-schueler-telefoneintrage :fahrschuelerarten-by-id
					:foerderschwerpunkte-by-id :get-list-schueler-telefoneintraege :haltestellen-by-id :map-schulen :map-telefon-arten
					:patch-schueler-telefoneintrag :religionen-by-id :schueler-liste-manager="() => statistikState.schuelerListeManager" autofocus :patch />
			</svws-ui-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, watch } from "vue";

	import type { SchuelerStatistikGesamt } from "@core/asd/data/statistik/SchuelerStatistikGesamt";
	import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
	import type { Validator } from "@core/asd/validate/Validator";
	import type { ValidatorFehler } from "@core/asd/validate/ValidatorFehler";
	import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
	import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useStatistikState } from "@ui/states/statistik/StatistikState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";
	import { TabManager } from "@ui/ui/nav/TabManager";

	import type { StatistikSchuelerProps } from "./StatistikSchuelerProps";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const props = defineProps<StatistikSchuelerProps>();


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

	const statistikState = useStatistikState();

	function getEintrag(id: number) {
		const eintrag = statistikState.mapSchueler.get(id);
		if (eintrag === undefined) {
			throw new DeveloperNotificationException("Es wurde ein Schüler mit ungültiger ID gesucht");
		}
		return eintrag;
	}

	const gridManager = new GridManager<string, readonly [SchuelerStatistikGesamt | null, ValidatorFehler | null, number], List<readonly [SchuelerStatistikGesamt | null, ValidatorFehler | null, number]>>({
		daten: computed(() => {
			const liste = new ArrayList<readonly [SchuelerStatistikGesamt | null, ValidatorFehler | null, number]>();
			const allgFehler = statistikState.validatorGesamt.getFehlerBySchuelerID(-1);
			if (!allgFehler.isEmpty()) {
				liste.add([null, null, allgFehler.size()]);
				for (let i = 0; i < allgFehler.size(); i++) {
					liste.add([null, allgFehler.get(i), i]);
				}
			}
			for (const eintrag of statistikState.statistikGesamt.schueler) {
				const fehlerListe = statistikState.validatorGesamt.getFehlerBySchuelerID(eintrag.id);
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
			{ kuerzel: "Name", name: "Name", width: '6rem' },
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
		await statistikState.setSchueler(eintrag.id);
		let tab = tabManager.getTab("SS");
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
		{ name: "SS", text: "Stammdaten" },
	];

	const tabManager = new TabManager(tabs, tabs[0], async () => void 0);

</script>
