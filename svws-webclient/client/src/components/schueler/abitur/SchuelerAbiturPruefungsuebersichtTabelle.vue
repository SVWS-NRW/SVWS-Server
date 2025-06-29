<template>
	<ui-table-grid name="Übersicht über die Prüfungsergebnisse" :header-count="2" :manager="() => gridManager" :cell-format="cellFormat">
		<template #header="params">
			<template v-if="params.i === 1">
				<th class="ui-divider text-ui-50 text-left col-span-3">{{ schueler.nachname }}, {{ schueler.vorname }}</th>
				<th class="ui-divider col-span-6">Zulassung</th>
				<th class="col-span-2">Prüfung</th>
				<th class="col-span-4">mündliche Prüfung</th>
				<th class="ui-divider" />
				<th class="col-span-2">Abitur</th>
			</template>
			<template v-else>
				<th>Abi</th>
				<th>Kürzel</th>
				<th class="ui-divider text-left"> Fach </th>
				<th>Q1.1</th>
				<th>Q1.2</th>
				<th>Q2.1</th>
				<th>Q2.2</th>
				<th>Summe</th>
				<th class="ui-divider">⌀</th>
				<th>Punkte</th>
				<th class="ui-divider">Wert</th>
				<th>Pflicht</th>
				<th>Freiw.</th>
				<th>RF</th>
				<th class="ui-divider">Punkte</th>
				<th class="ui-divider">Summe</th>
				<th class="col-span-2" :class="{
					'text-ui-onsuccess bg-ui-success': istBestanden === true,
					'text-ui-ondanger bg-ui-danger': istBestanden === false,
				}">
					Bestanden: {{ (istBestanden === true) ? 'Ja' : ((istBestanden === false) ? 'Nein' : '???') }}
				</th>
			</template>
		</template>
		<template #default="{ row: belegung, index: rowIndex }">
			<template v-if="belegung.abiturFach !== null">
				<td>{{ belegung.abiturFach }}.</td>
				<td :style="{ 'background-color': getFachfarbe(belegung) }">
					{{ manager().faecher().get(belegung.fachID)?.kuerzelAnzeige ?? "???" }}
				</td>
				<td class="text-left ui-divider" :style="{ 'background-color': getFachfarbe(belegung) }">
					{{ manager().faecher().get(belegung.fachID)?.bezeichnung ?? "???" }}
				</td>
				<template v-for="hj in GostHalbjahr.getQualifikationsphase()" :key="hj.id">
					<td :class="{ 'ui-divider': (hj === GostHalbjahr.Q22) }">
						{{ getNotenpunkteString(belegung, hj) }}
					</td>
				</template>
				<td class="ui-divider">
					{{ belegung.block1PunktSumme ?? 0 }}
				</td>
				<td class="ui-divider" :class="{ 'font-bold text-ui-danger': istAbweichungspruefung(belegung) }">
					{{ formatNotenpunkteDurchschnitt(belegung.block1NotenpunkteDurchschnitt) }}
				</td>
				<td :ref="inputPruefungsnote(belegung, rowIndex)" class="ui-table-grid-input" :class="{
					'font-bold text-ui-danger': istDefizit(belegung.block2NotenKuerzelPruefung)
				}" />
				<td class="ui-divider" :class="{ 'font-bold text-ui-danger': istWertungDefizit(belegung.block2PunkteZwischenstand) }">
					{{ belegung.block2PunkteZwischenstand ?? '' }}
				</td>
				<template v-if="belegung.abiturFach < 4">
					<td v-if="!istAbweichungspruefung(belegung)" :ref="inputPflichtPruefung(belegung, rowIndex)" class="ui-table-grid-button">
						<span class="icon-sm align-middle" :class="{
							'i-ri-checkbox-line': (belegung.block2MuendlichePruefungBestehen === true) || (belegung.block2MuendlichePruefungAbweichung === true),
							'i-ri-checkbox-blank-line': !((belegung.block2MuendlichePruefungBestehen === true) || (belegung.block2MuendlichePruefungAbweichung === true))
						}" />
					</td>
					<td v-else><span class="icon-sm align-middle i-ri-check-line" /></td>
					<td :ref="inputFreiwilligePruefung(belegung, rowIndex)" class="ui-table-grid-button">
						<span class="icon-sm align-middle" :class="{
							'i-ri-checkbox-line': belegung.block2MuendlichePruefungFreiwillig === true,
							'i-ri-checkbox-blank-line': !(belegung.block2MuendlichePruefungFreiwillig === true)
						}" />
					</td>
					<td :ref="inputPruefungsreihenfolge(belegung, rowIndex)" class="ui-table-grid-input" />
					<td :ref="inputPruefungsnoteMdl(belegung, rowIndex)" class="ui-table-grid-input ui-divider" :class="{
						'font-bold text-ui-danger': istDefizit(belegung.block2MuendlichePruefungNotenKuerzel)
					}" />
				</template>
				<td v-else class="col-span-4 ui-divider bg-ui-75" />
				<td class="ui-divider" :class="{ 'font-bold text-ui-danger': istWertungDefizit(belegung.block2Punkte) }">
					{{ belegung.block2Punkte ?? '' }}
				</td>
				<td class="col-span-2" />
			</template>
		</template>
		<template #footer>
			<td class="col-span-3 ui-divider" />
			<td class="col-span-4 text-right">Gesamt (normiert):</td>
			<td class="font-bold">{{ manager().daten().block1PunktSummeNormiert }}</td>
			<td class="ui-divider" />
			<td />
			<td class="ui-divider">{{ getPunktSummePruefungen() }}</td>
			<td class="col-span-4 ui-divider" />
			<td class="font-bold ui-divider">{{ manager().daten().block2PunktSumme }}</td>
			<td class="font-bold">{{ manager().daten().gesamtPunkte }}</td>
			<td class="bg-ui-brand-secondary font-bold">{{ manager().daten().note }}</td>
		</template>
	</ui-table-grid>
</template>

<script setup lang="ts">

	import { computed, watchEffect, type ComponentPublicInstance } from "vue";
	import type { AbiturFachbelegung, Collection, Comparator, Fachgruppe, JavaMap, NoteKatalogEintrag } from "@core";
	import { ArrayList, Fach, GostBesondereLernleistung, HashMap, Note, RGBFarbe } from "@core";
	import { DeveloperNotificationException, GostHalbjahr } from "@core";
	import { GridManager } from "@ui";

	import type { SchuelerAbiturPruefungsuebersichtTabelleProps } from "./SchuelerAbiturPruefungsuebersichtTabelleProps";

	const props = defineProps<SchuelerAbiturPruefungsuebersichtTabelleProps>();

	const abiBelegungen = computed<JavaMap<number, AbiturFachbelegung>>(() => {
		const tmp = new ArrayList<AbiturFachbelegung>();
		for (const belegung of props.manager().daten().fachbelegungen) {
			if ((belegung.abiturFach === null) || (belegung.abiturFach < 1) || (belegung.abiturFach > 5))
				continue;
			tmp.add(belegung);
		}
		tmp.sort(<Comparator<AbiturFachbelegung>>{ compare(a, b) { return a.abiturFach! - b.abiturFach!; } });
		const result = new HashMap<number, AbiturFachbelegung>();
		for (const belegung of tmp) {
			if (belegung.abiturFach === null)
				continue;
			if (result.containsKey(belegung.abiturFach))
				throw new DeveloperNotificationException("Ein Abiturfach darf nur einmal gesetzt sein. Dies muss an dieser Stelle sichergestellt werden.");
			result.put(belegung.abiturFach, belegung);
		}
		return result;
	});

	const gridManager = new GridManager<string, AbiturFachbelegung, Collection<AbiturFachbelegung>>({
		daten: computed<Collection<AbiturFachbelegung>>(() => abiBelegungen.value.values()),
		getRowKey: row => `${props.manager().daten().schuelerID}_${row.abiturFach}`,
	});
	const cellFormat = {
		widths: ['4rem', '4rem','16rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem'],
	};

	const schuljahr = computed<number>(() => props.manager().getAbiturjahr() - 1);

	const hatBLL = computed<boolean>(() => GostBesondereLernleistung.fromKuerzel(props.manager().daten().besondereLernleistung) !== GostBesondereLernleistung.KEINE);
	const hatAbiFach5 = computed<boolean>(() => abiBelegungen.value.containsKey(5) || hatBLL.value);

	function istWertungDefizit(punkte: number | null): boolean {
		if (punkte === null)
			return false;
		return punkte < (hatAbiFach5.value ? 20 : 25);
	}

	function getNotenpunkteString(belegung: AbiturFachbelegung, hj: GostHalbjahr) : string {
		const np = props.manager().getNotenpunkteByFachIDAndHalbjahr(belegung.fachID, hj);
		if (np === null)
			return "";
		return ((np < 10) ? "0" : "") + np;
	}

	function getNotenpunkteFromKuerzel(notenkuerzel: string | null) : number | null {
		if (notenkuerzel === null)
			return null;
		const nke : NoteKatalogEintrag | null = Note.fromKuerzel(notenkuerzel).daten(props.manager().getSchuljahr());
		if ((nke === null) || (nke.notenpunkte === null))
			return null;
		return nke.notenpunkte;
	}

	const istBestanden = computed<boolean | null>(() => props.manager().daten().pruefungBestanden);

	function istDefizit(notenkuerzel: string | null) : boolean {
		const np = getNotenpunkteFromKuerzel(notenkuerzel);
		return (np !== null) && (np < 5);
	}

	function getPunktSummePruefungen() {
		let summe = 0;
		for (let i = 1; i <= 5; i++) {
			const belegung = abiBelegungen.value.get(i);
			if (belegung !== null)
				summe += (belegung.block2PunkteZwischenstand ?? 0);
		}
		return summe;
	}

	function getFachgruppe(belegung: AbiturFachbelegung): Fachgruppe | null {
		const fach = props.manager().faecher().get(belegung.fachID);
		if (fach === null)
			return null;
		const f = Fach.getBySchluesselOrDefault(fach.kuerzel);
		// TODO ggf. für Abi30ff zusätzlich check...
		// if ((isAbi30ff) && ((f === Fach.IN) || (f === Fach.VO)))
		// 	return null;
		return f.getFachgruppe(schuljahr.value) ?? null;
	}

	function getFachfarbe(belegung: AbiturFachbelegung): string {
		const gruppe = getFachgruppe(belegung);
		const farbe : RGBFarbe = (gruppe === null) ? new RGBFarbe() : gruppe.getFarbe(schuljahr.value);
		return "rgb(" + farbe.red + "," + farbe.green + "," + farbe.blue + ")";
	}

	function istAbweichungspruefung(belegung: AbiturFachbelegung): boolean {
		return (props.manager().daten().abiturjahr <= 2019) && (belegung.block2MuendlichePruefungAbweichung === true);
	}

	function formatNotenpunkteDurchschnitt(avg: number | null): string {
		if (avg === null)
			return "";
		let tmp = ((avg < 10) ? "0" : "") + avg;
		if (tmp.length === 2)
			tmp += ".";
		while (tmp.length < 5)
			tmp += "0";
		return tmp;
	}

	function updateNotenpunkte(belegung: AbiturFachbelegung, value: string | null) : void {
		void props.updateAbiturpruefungsdaten(props.manager, { fachID: belegung.fachID, block2NotenKuerzelPruefung: value }, true);
	}

	function updatePflichtPruefung(belegung: AbiturFachbelegung, value: boolean) : void {
		void props.updateAbiturpruefungsdaten(props.manager, { fachID: belegung.fachID, block2MuendlichePruefungBestehen: value }, false);
	}

	function updateFreiwilligePruefung(belegung: AbiturFachbelegung, value: boolean) : void {
		void props.updateAbiturpruefungsdaten(props.manager, { fachID: belegung.fachID, block2MuendlichePruefungFreiwillig: value }, false);
	}

	function updatePruefungsreihenfolge(belegung: AbiturFachbelegung, value: number | null) : void {
		void props.updateAbiturpruefungsdaten(props.manager, { fachID: belegung.fachID, block2MuendlichePruefungReihenfolge: value }, false);
	}

	function updateNotenpunkteMdl(belegung: AbiturFachbelegung, value: string | null) : void {
		void props.updateAbiturpruefungsdaten(props.manager, { fachID: belegung.fachID, block2MuendlichePruefungNotenKuerzel: value }, false);
	}

	function inputPruefungsnote(belegung: AbiturFachbelegung, rowIndex: number) {
		const key = 'PrüfungsnoteAbiFach' + belegung.abiturFach;
		const setter = (value : string | null) => updateNotenpunkte(belegung, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputAbiturNotenpunkte(key, 1, rowIndex, element, setter, props.manager().daten().schuljahrAbitur);
			if (input !== null)
				watchEffect(() => gridManager.update(key, belegung.block2NotenKuerzelPruefung));
		};
	}

	function inputPflichtPruefung(belegung: AbiturFachbelegung, rowIndex: number) {
		const key = 'PflichtPrüfungAbiFach' + belegung.abiturFach;
		const setter = (value : boolean) => updatePflichtPruefung(belegung, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, 2, rowIndex, element, setter);
			if (input !== null)
				watchEffect(() => gridManager.update(key, (belegung.block2MuendlichePruefungBestehen === true) || (belegung.block2MuendlichePruefungAbweichung === true)));
		};
	}

	function inputFreiwilligePruefung(belegung: AbiturFachbelegung, rowIndex: number) {
		const key = 'FreiwilligePrüfungAbiFach' + belegung.abiturFach;
		const setter = (value : boolean) => updateFreiwilligePruefung(belegung, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, 3, rowIndex, element, setter);
			if (input !== null)
				watchEffect(() => gridManager.update(key, belegung.block2MuendlichePruefungFreiwillig ?? false));
		};
	}

	function inputPruefungsreihenfolge(belegung: AbiturFachbelegung, rowIndex: number) {
		const key = 'PrüfungsreihenfolgeAbiFach' + belegung.abiturFach;
		const setter = (value : number | null) => updatePruefungsreihenfolge(belegung, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputAbiturPruefungsreihenfolge(key, 4, rowIndex, element, setter);
			if (input !== null)
				watchEffect(() => gridManager.update(key, belegung.block2MuendlichePruefungReihenfolge));
		};
	}

	function inputPruefungsnoteMdl(belegung: AbiturFachbelegung, rowIndex: number) {
		const key = 'PrüfungsnoteMdlAbiFach' + belegung.abiturFach;
		const setter = (value : string | null) => updateNotenpunkteMdl(belegung, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputAbiturNotenpunkte(key, 5, rowIndex, element, setter, props.manager().daten().schuljahrAbitur);
			if (input !== null)
				watchEffect(() => gridManager.update(key, belegung.block2MuendlichePruefungNotenKuerzel));
		};
	}

</script>
