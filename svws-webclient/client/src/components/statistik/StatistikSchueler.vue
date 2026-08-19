<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-statistik-schueler /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page flex gap-8">
		<ui-table-grid :manager="() => gridManager" class="flex-1">
			<!-- <template #header>
					<template v-for="col of gridManager.cols.values()" :key="col.name">
						<th v-if="col.kuerzel === 'Auswahl'" class="flex items-center justify-center">
							<svws-ui-checkbox :model-value="(auswahl.length === gridManager.daten.size()) && (auswahl.length > 0)"
								:indeterminate="(auswahl.length > 0) && (auswahl.length < gridManager.daten.size())"
								@update:model-value="value => auswahl = value ? [...gridManager.daten] : []" />
						</th>
						<th v-else class="flex justify-center" :class="[col.kuerzel === '2FA' ? 'text-center' : 'text-left']">
							{{ col.kuerzel }}
						</th>
					</template>
				</template> -->
			<template #default="{ row: [key, schuelerEintrag, _lehrerStatistik, list] }">
				<template v-if="key !== null">
					<td class="col-span-2 text-left bg-ui-50">{{ key }}</td>
				</template>
				<template v-else>
					<!-- <td class="flex items-center justify-center">
							<svws-ui-checkbox :model-value="auswahl.includes(lehrer)" @update:model-value="toggleSelection(lehrer)" />
						</td> -->
					<td>
						<div class="text-left">
							{{ schuelerEintrag.nachname }}, {{ schuelerEintrag.vorname }}
							<!-- <svws-ui-tooltip>
									<span v-if="lehrer.art2FA > 0" class="icon-sm i-ri-verified-badge-fill icon-ui-success" />
									<span v-else class="icon-sm i-ri-alert-fill icon-ui-danger" />
									<template #content>
										<span v-if="lehrer.art2FA > 0">Es wurde eine Zwei-Faktor-Authentifizierung eingerichtet ({{ lehrer.art2FA === 1 ? 'TOTP' : 'EMail' }}).</span>
										<span v-else>Es wurde keine Zwei-Faktor-Authentifizierung eingerichtet.</span>
									</template>
								</svws-ui-tooltip> -->
						</div>
					</td>
					<td class="text-left flex flex-col">
						<div v-for="fehler of list" class="" :key="fehler.getFehlercode()">
							{{ fehler.getFehlermeldung() }}
							<!-- <svws-ui-tooltip>
									<span v-if="lehrer.art2FA > 0" class="icon-sm i-ri-verified-badge-fill icon-ui-success" />
									<span v-else class="icon-sm i-ri-alert-fill icon-ui-danger" />
									<template #content>
										<span v-if="lehrer.art2FA > 0">Es wurde eine Zwei-Faktor-Authentifizierung eingerichtet ({{ lehrer.art2FA === 1 ? 'TOTP' : 'EMail' }}).</span>
										<span v-else>Es wurde keine Zwei-Faktor-Authentifizierung eingerichtet.</span>
									</template>
								</svws-ui-tooltip> -->
						</div>
					</td>
				</template>
			</template>
		</ui-table-grid>
		<div v-if="schuelerListeManager().auswahlID() !== null" class="flex-1">
			<div class="font-bold ml-8 mb-4 flex flex-row items-center" @click="gotoSchueler(schuelerListeManager().auswahl())"><span class="icon cursor-pointer i-ri-link" /> {{ schuelerListeManager().auswahl().nachname }}, {{ schuelerListeManager().auswahl().vorname }}</div>
			<svws-ui-tab-bar :tab-manager="() => tabManager">
				<schueler-individualdaten v-if="tabManager.tab.name === 'Stammdaten'" :zeige-alles="false" :add-schueler-telefoneintrag
					:delete-schueler-telefoneintrage :fahrschuelerarten-by-id
					:foerderschwerpunkte-by-id :get-list-schueler-telefoneintraege :haltestellen-by-id :map-schulen :map-telefon-arten
					:patch-schueler-telefoneintrag :religionen-by-id :schueler-liste-manager autofocus :patch />
			</svws-ui-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GridManager, useRegionSwitch, TabManager, useSchuleState } from "@ui";
	import type { StatistikSchuelerProps } from "./StatistikSchuelerProps";
	import type { ValidatorFehler, List } from "@core";
	import { SchuelerListeEintrag, SchuelerStatistikGesamt, ValidatorSsSchuelerStammdaten, ArrayList } from "@core";
	import { computed, watch } from "vue";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const props = defineProps<StatistikSchuelerProps>();

	const schuleState = useSchuleState();

	const tuples = computed(() => {
		const mapValidators = new Map<string, List<readonly [SchuelerListeEintrag, SchuelerStatistikGesamt, List<ValidatorFehler>]>>([
			["Stammdaten", new ArrayList<readonly [SchuelerListeEintrag, SchuelerStatistikGesamt, List<ValidatorFehler>]>()],
		]);
		for (const schueler of props.statistikGesamt.schueler) {
			const validator = new ValidatorSsSchuelerStammdaten({ get: () => schueler.geschlecht }, { get: () => schueler.geburtsdatum }, { get: () => schueler.idGeburtsland }, { get: () => schueler.idGeburtslandMutter }, { get: () => schueler.idGeburtslandVater }, { get: () => schueler.hatMigrationshintergrund }, { get: () => schueler.idStaatsangehoerigkeit }, { get: () => schueler.idStaatsangehoerigkeit2 }, schuleState.validatorKontext);
			validator.run();
			const listFehler = validator.getFehler();
			const list = mapValidators.get("Stammdaten");
			if (!listFehler.isEmpty()) {
				const s = props.mapSchueler.get(schueler.id);
				if (s === undefined || list === undefined) {
					continue;
				}
				list.add([s, schueler, listFehler]);
			}
		}
		return mapValidators;
	});

	const gridManager = new GridManager<string, readonly [string | null, SchuelerListeEintrag, SchuelerStatistikGesamt, List<ValidatorFehler>], List<readonly [string | null, SchuelerListeEintrag, SchuelerStatistikGesamt, List<ValidatorFehler>]>>({
		daten: computed(() => {
			const liste = new ArrayList<readonly [string | null, SchuelerListeEintrag, SchuelerStatistikGesamt, List<ValidatorFehler>]>();
			for (const [key, values] of tuples.value.entries()) {
				liste.add([key, new SchuelerListeEintrag(), new SchuelerStatistikGesamt(), new ArrayList()]);
				for (const value of values) {
					liste.add([null, ...value]);
				}
			}
			return liste;
		}),
		getRowKey: row => `ID_${row[0] === null ? row[0] : row[1].id}`,
		allowEmptyRowSelection: true,
		columns: [
			// { kuerzel: "Auswahl", name: "Auswahl", width: "3rem", hideable: false },
			{ kuerzel: "Name", name: "Name", width: '1fr' },
			// { kuerzel: "Validator", name: "Validator", width: '1fr' },
			{ kuerzel: "Fehlermeldung", name: "Fehlermeldung", width: '3fr' },
		],
	});

	watch(() => gridManager.focusRow, async () => {
		if (gridManager.focusRow === null) {
			return null;
		}
		const eintrag = gridManager.daten.get(gridManager.focusRow)[1];
		await props.setAuswahl(eintrag.id);
		let tab: string | null = null;
		let index = 0;
		for (const eintrag of gridManager.daten) {
			if (eintrag[0] !== null) {
				tab = eintrag[0];
			}
			if ((index === gridManager.focusRow) && (tab !== null)) {
				await tabManager.setTab(tabManager.getTab(tab));
				break;
			}
			index++;
		}
	});

	const tabs = [
		{ name: "Stammdaten", text: "Stammdaten" },
	];

	const tabManager = new TabManager(tabs, tabs[0], async () => void 0);

</script>
