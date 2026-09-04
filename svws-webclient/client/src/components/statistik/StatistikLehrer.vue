<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-statistik-lehrer /> </svws-ui-modal-hilfe>
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
			<template #default="{ row: [key, lehrerEintrag, _lehrerStatistik, list] }">
				<template v-if="key !== null">
					<td class="col-span-2 text-left bg-ui-50">{{ key }}</td>
				</template>
				<template v-else>
					<!-- <td class="flex items-center justify-center">
							<svws-ui-checkbox :model-value="auswahl.includes(lehrer)" @update:model-value="toggleSelection(lehrer)" />
						</td> -->
					<td>
						<div class="text-left">
							{{ lehrerEintrag.nachname }}, {{ lehrerEintrag.vorname }}
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
		<div v-if="lehrerListeManager().auswahlID() !== null" class="flex-1">
			<div class="font-bold ml-8 mb-4 flex flex-row items-center" @click="gotoLehrer(lehrerListeManager().auswahl())"><span class="icon cursor-pointer i-ri-link" /> {{ lehrerListeManager().auswahl().nachname }}, {{ lehrerListeManager().auswahl().vorname }}</div>
			<svws-ui-tab-bar :tab-manager="() => tabManager">
				<lehrer-individualdaten v-if="tabManager.tab.name === 'Stammdaten'" :zeige-alles="false" :add-leitungsfunktion :delete-leitungsfunktionen
					:get-list-leitungsfunktionen :map-leitungsfunktionen :patch-leitungsfunktion :lehrer-liste-manager :patch />
				<lehrer-personaldaten v-if="tabManager.tab.name === 'Personaldaten'" :add-anrechnung :add-fachrichtung :add-lehramt :add-lehrbefaehigung :add-lehrer-unterrichtsfach
					:add-mehrleistung :add-minderleistung :lehrer-liste-manager :lehrer-unterrichtsfaecher :map-faecher
					:map-schulen :patch-abschnittsdaten :patch-anrechnungen :patch-fachrichtung :patch-lehramt :patch-lehrbefaehigung :patch-lehrer-unterrichtsfach :patch-mehrleistung :patch-minderleistung :patch-personaldaten
					:remove-anrechnung :remove-fachrichtungen :remove-lehraemter :remove-lehrbefaehigungen :remove-lehrer-unterrichtsfach :remove-mehrleistung :remove-minderleistung />
			</svws-ui-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { LehrerStatistikGesamt } from "@core/asd/data/statistik/LehrerStatistikGesamt";
	import type { BasicValidator } from "@core/asd/validate/BasicValidator";
	import { ValidatorLpLehrerPersonaldaten } from "@core/asd/validate/lehrer/ValidatorLpLehrerPersonaldaten";
	import { ValidatorLsLehrerStammdaten } from "@core/asd/validate/lehrer/ValidatorLsLehrerStammdaten";
	import type { ValidatorFehler } from "@core/asd/validate/ValidatorFehler";
	import { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import { ListUtils } from "@core/core/utils/ListUtils";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";
	import { TabManager } from "@ui/ui/nav/TabManager";
	import type { StatistikLehrerProps } from "./StatistikLehrerProps";
	import { computed, watch } from "vue";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const props = defineProps<StatistikLehrerProps>();

	const schuleState = useSchuleState();

	class MappedFehlerGenerator<Eintrag, Stat> {

		private _mapFehler = new Map<string, List<readonly [Eintrag, Stat, List<ValidatorFehler>]>>();

		get mapFehler() {
			return this._mapFehler;
		}

		addFehlerByKey(key: string, eintrag: Eintrag, stat: Stat, validator: BasicValidator) {
			validator.run();
			const list = validator.getFehler();
			if (list.isEmpty()) {
				return;
			}
			let listByKey = this._mapFehler.get(key);
			const arr = [eintrag, stat, list] as const;
			if (listByKey === undefined) {
				listByKey = ListUtils.create1(arr);
			} else {
				listByKey.add(arr);
			}
			this._mapFehler.set(key, listByKey);
		}
	}


	const tuples = computed(() => {
		const gesamt = props.statistikGesamt;
		const genValidatorFehler = new MappedFehlerGenerator<LehrerListeEintrag, LehrerStatistikGesamt>();
		for (const lehrer of props.statistikGesamt.lehrer) {
			const l = props.mapLehrer.get(lehrer.id);
			if (l === undefined) {
				continue;
			}
			genValidatorFehler.addFehlerByKey("Stammdaten", l, lehrer, new ValidatorLsLehrerStammdaten({ get: () => lehrer.nachname }, { get: () => lehrer.vorname }, { get: () => lehrer.geburtsdatum }, { get: () => lehrer.geschlecht }, { get: () => lehrer.kuerzel }, { get: () => lehrer.idStaatsangehoerigkeit }, schuleState.validatorKontext));
			genValidatorFehler.addFehlerByKey("Personaldaten", l, lehrer, new ValidatorLpLehrerPersonaldaten({ get: () => lehrer.id }, { get: () => gesamt.schule.idSchuljahresabschnitt }, { get: () => lehrer.idStaatsangehoerigkeit }, { get: () => lehrer.idRechtsverhaeltnis }, { get: () => lehrer.pflichtstundensoll }, { get: () => lehrer.anrechnungen }, { get: () => lehrer.idEinsatzstatus }, { get: () => lehrer.idBeschaeftigungsart }, { get: () => lehrer.geburtsdatum }, { get: () => lehrer.lehraemter }, { get: () => lehrer.mehrleistung }, { get: () => lehrer.minderleistung }, schuleState.validatorKontext));
		}
		return genValidatorFehler.mapFehler;
	});

	const gridManager = new GridManager<string, readonly [string | null, LehrerListeEintrag, LehrerStatistikGesamt, List<ValidatorFehler>], List<readonly [string | null, LehrerListeEintrag, LehrerStatistikGesamt, List<ValidatorFehler>]>>({
		daten: computed(() => {
			const liste = new ArrayList<readonly [string | null, LehrerListeEintrag, LehrerStatistikGesamt, List<ValidatorFehler>]>();
			for (const [key, values] of tuples.value.entries()) {
				liste.add([key, new LehrerListeEintrag(), new LehrerStatistikGesamt(), new ArrayList()]);
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
			{ kuerzel: "Fehlermeldung", name: "Fehlermeldung", width: '4fr' },
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
		{ name: "Personaldaten", text: "Personaldaten" },
	];

	const tabManager = new TabManager(tabs, tabs[0], async () => void 0);

</script>
