<template>
	<div class="page page-flex-col pt-0 overflow-hidden">
		<div class="flex flex-row">
			<div class="min-w-64">
				<ui-select label="Kurs" v-model="auswahlKurs" :manager="kursSelectManager" />
			</div>
			<div class="min-w-64">
				<ui-select label="Prüfer" v-model="auswahlPruefer" :manager="prueferSelectManager" />
			</div>
			<div class="min-w-64">
				<ui-select label="Abiturfach" v-model="auswahlAbiturfach" :manager="abiturfachSelectManager" />
			</div>
		</div>
		<ui-table-grid name="Übersicht über die Prüfungsergebnisse" :header-count="2" :footer-count="0" :manager="() => gridManager" :cell-format="cellFormat">
			<template #header="params">
				<template v-if="params.i === 1">
					<th class="ui-divider text-ui-50 text-left col-span-6" />
					<th class="ui-divider">Vornote</th>
					<th class="col-span-2">Prüfung</th>
					<th class="col-span-4">mündliche Prüfung</th>
					<th class="ui-divider" />
					<th class="col-span-5">Gesamt</th>
				</template>
				<template v-else>
					<th>Abi</th>
					<th>Kürzel</th>
					<th class="ui-divider text-left"> Fach </th>
					<th class="ui-divider text-left"> Schüler </th>
					<th class="ui-divider text-left"> Kurs </th>
					<th class="ui-divider text-left"> Prüfer </th>
					<th class="ui-divider">⌀</th>
					<th>Punkte</th>
					<th class="ui-divider">Wert</th>
					<th>Pflicht</th>
					<th>Freiw.</th>
					<th>RF</th>
					<th class="ui-divider">Punkte</th>
					<th class="ui-divider">Summe</th>
					<th>Block I</th>
					<th>Block II</th>
					<th>Gesamt</th>
					<th>Best.</th>
					<th>Note</th>
				</template>
			</template>
			<template #default="{ row }">
				<template v-if="row.belegung.abiturFach !== null">
					<td>{{ row.belegung.abiturFach }}.</td>
					<td :style="{ 'background-color': getFachfarbe(row) }">
						{{ row.manager.faecher().get(row.belegung.fachID)?.kuerzelAnzeige ?? "???" }}
					</td>
					<td class="text-left ui-divider" :style="{ 'background-color': getFachfarbe(row) }">
						{{ row.manager.faecher().get(row.belegung.fachID)?.bezeichnung ?? "???" }}
					</td>
					<td class="text-left ui-divider">
						{{ row.schueler.nachname }}, {{ row.schueler.vorname }}
					</td>
					<td class="text-left ui-divider">
						{{ getQ22Kurs(row)?.kuerzel ?? "-" }}
					</td>
					<td class="text-left ui-divider">
						{{ getPruefer(row)?.kuerzel }}
					</td>
					<td class="ui-divider" :class="{ 'font-bold text-ui-danger': istAbweichungspruefung(row) }">
						{{ formatNotenpunkteDurchschnitt(row.belegung.block1NotenpunkteDurchschnitt) }}
					</td>
					<td :ref="inputPruefungsnote(row)" class="ui-table-grid-input" :class="{
						'font-bold text-ui-danger': istDefizit(row)
					}" />
					<td class="ui-divider" :class="{ 'font-bold text-ui-danger': istWertungDefizit(row) }">
						{{ row.belegung.block2PunkteZwischenstand ?? '' }}
					</td>
					<template v-if="row.belegung.abiturFach < 4">
						<td v-if="!istAbweichungspruefung(row)" :ref="inputPflichtPruefung(row)" class="ui-table-grid-button">
							<span class="icon-sm align-middle" :class="{
								'i-ri-checkbox-line': (row.belegung.block2MuendlichePruefungBestehen === true) || (row.belegung.block2MuendlichePruefungAbweichung === true),
								'i-ri-checkbox-blank-line': !((row.belegung.block2MuendlichePruefungBestehen === true) || (row.belegung.block2MuendlichePruefungAbweichung === true))
							}" />
						</td>
						<td v-else><span class="icon-sm align-middle i-ri-check-line" /></td>
						<td :ref="inputFreiwilligePruefung(row)" class="ui-table-grid-button">
							<span class="icon-sm align-middle" :class="{
								'i-ri-checkbox-line': row.belegung.block2MuendlichePruefungFreiwillig === true,
								'i-ri-checkbox-blank-line': !(row.belegung.block2MuendlichePruefungFreiwillig === true)
							}" />
						</td>
						<td :ref="inputPruefungsreihenfolge(row)" class="ui-table-grid-input" />
						<td :ref="inputPruefungsnoteMdl(row)" class="ui-table-grid-input ui-divider" :class="{
							'font-bold text-ui-danger': istDefizit(row)
						}" />
					</template>
					<td v-else class="col-span-4 ui-divider bg-ui-75" />
					<td class="ui-divider" :class="{ 'font-bold text-ui-danger': istWertungDefizit(row) }">
						{{ row.belegung.block2Punkte ?? '' }}
					</td>
					<td>{{ row.manager.daten().block1PunktSummeNormiert }}</td>
					<td>{{ row.manager.daten().block2PunktSumme }}</td>
					<td>{{ row.manager.daten().gesamtPunkte }}</td>
					<td :class="{
						'text-ui-onsuccess bg-ui-success': row.manager.daten().pruefungBestanden === true,
						'text-ui-ondanger bg-ui-danger': row.manager.daten().pruefungBestanden === false,
					}">
						{{ (row.manager.daten().pruefungBestanden === true) ? 'Ja' : ((row.manager.daten().pruefungBestanden === false) ? 'Nein' : '???') }}
					</td>
					<td>{{ row.manager.daten().note }}</td>
				</template>
			</template>
		</ui-table-grid>
	</div>
</template>

<script setup lang="ts">

	import { computed, shallowRef, watchEffect, type ComponentPublicInstance } from "vue";
	import type { List, AbiturFachbelegung, Comparator, Fachgruppe, NoteKatalogEintrag, SchuelerListeEintrag, KursDaten, LehrerListeEintrag, JavaMap } from "@core";
	import { AbiturdatenManager } from "@core";
	import { GostHalbjahr, ArrayList, Fach, GostBesondereLernleistung, Note, RGBFarbe, DeveloperNotificationException, HashMap, GostAbiturFach } from "@core";
	import { GridManager, BaseSelectManager } from "@ui";

	import type { GostAbiturNoteneingabeProps } from "./GostAbiturNoteneingabeProps";

	const props = defineProps<GostAbiturNoteneingabeProps>();

	const auswahlBelegungen = computed<List<SchuelerAbiturbelegung>>(() => {
		const auswahl = new ArrayList<SchuelerAbiturbelegung>();
		// Wenn ein Kurs-Filter gesetzt ist, dann filtere die Abiturfachbelegungen nach dem Kurs
		const kurs = auswahlKurs.value;
		const kursBelegungen = alleKurse.value.get(kurs);
		if (kursBelegungen !== null) {
			auswahl.addAll(kursBelegungen);
			return auswahl;
		}
		// Wenn ein Prüfer gesetzt ist, dann filtere die Abiturfachbelegungen anhand des Prüfers
		const pruefer = auswahlPruefer.value;
		const prueferBelegungen = allePruefer.value.get(pruefer);
		if (prueferBelegungen !== null) {
			auswahl.addAll(prueferBelegungen);
			return auswahl;
		}
		// Wenn ein Abiturfach gesetzt ist, dann filtere die Abiturfachbelegungen anhand des Abiturfaches
		const abiturfach = auswahlAbiturfach.value;
		if (abiturfach !== null) {
			for (const belegung of schuelerInPruefung.value)
				if (GostAbiturFach.fromID(belegung.abiturfach) === abiturfach)
					auswahl.add(belegung);
			return auswahl;
		}
		// Ansonsten nehme alle Abiturfachbelegungen
		auswahl.addAll(schuelerInPruefung.value);
		return auswahl;
	});

	const gridManager = new GridManager<string, SchuelerAbiturbelegung, List<SchuelerAbiturbelegung>>({
		daten: auswahlBelegungen,
		getRowKey: row => `${row.schueler.id}_${row.belegung.abiturFach}`,
	});
	const cellFormat = {
		widths: ['4rem', '4rem','16rem','16rem','6rem','6rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem', '1.25rem'],
	};

	interface SchuelerAbiturbelegung {
		index: number,
		manager: AbiturdatenManager,
		abiturfach: number;
		schueler: SchuelerListeEintrag;
		belegung: AbiturFachbelegung;
		hatAbiFach5: boolean;
	};

	const auswahlKurs = shallowRef<KursDaten | null>(null);
	const kursSelectManager = computed<BaseSelectManager<KursDaten>>(() => new BaseSelectManager({ options: alleKurse.value.keySet(),
		optionDisplayText: k => k.kuerzel, selectionDisplayText: k => k.kuerzel	}));

	const auswahlPruefer = shallowRef<LehrerListeEintrag | null>(null);
	const prueferSelectManager = computed<BaseSelectManager<LehrerListeEintrag>>(() => new BaseSelectManager({ options: allePruefer.value.keySet(),
		optionDisplayText: l => l.kuerzel, selectionDisplayText: l => l.kuerzel	}));

	const auswahlAbiturfach = shallowRef<GostAbiturFach | null>(null);
	const abiturfachSelectManager = computed<BaseSelectManager<GostAbiturFach>>(() => {
		const abiManager = props.managerMap().isEmpty() ? null : props.managerMap().values().iterator().next();
		const values = [ GostAbiturFach.LK1, GostAbiturFach.LK2, GostAbiturFach.AB3, GostAbiturFach.AB4 ];
		if ((abiManager !== null) && AbiturdatenManager.nutzeExperimentellenCode(props.serverMode, abiManager.daten().abiturjahr))
			values.push(GostAbiturFach.AB5);
		return new BaseSelectManager({ options: values, optionDisplayText: f => f.kuerzel, selectionDisplayText: f => f.kuerzel	});
	});

	const alleKurse = computed<JavaMap<KursDaten, List<SchuelerAbiturbelegung>>>(() => {
		const result = new HashMap<KursDaten, List<SchuelerAbiturbelegung>>();
		for (const row of schuelerInPruefung.value) {
			const idKurs = row.belegung.belegungen[GostHalbjahr.Q22.id]?.idKurs ?? null;
			const kurs = (idKurs === null) ? null : props.mapKurse.get(idKurs);
			if (kurs === null)
				continue;
			const list = result.computeIfAbsent(kurs, { apply : (k: KursDaten) => new ArrayList<SchuelerAbiturbelegung>() });
			if (list === null)
				continue;
			list.add(row);
		}
		return result;
	});

	const allePruefer = computed<JavaMap<LehrerListeEintrag, List<SchuelerAbiturbelegung>>>(() => {
		const result = new HashMap<LehrerListeEintrag, List<SchuelerAbiturbelegung>>();
		for (const row of schuelerInPruefung.value) {
			const idLehrer = row.belegung.block2Pruefer;
			const lehrer = (idLehrer === null) ? null : props.mapLehrer.get(idLehrer);
			if (lehrer === null)
				continue;
			const list = result.computeIfAbsent(lehrer, { apply : (l: LehrerListeEintrag) => new ArrayList<SchuelerAbiturbelegung>() });
			if (list === null)
				continue;
			list.add(row);
		}
		return result;
	});

	const schuelerInPruefung = computed<List<SchuelerAbiturbelegung>>(() => {
		const result = new ArrayList<SchuelerAbiturbelegung>();
		let counter = 0;
		for (const schueler of props.schuelerListe) {
			const manager = props.managerMap().get(schueler.id);
			if ((manager !== null) && (manager.daten().block1Zulassung === true)) {
				const tmp = new ArrayList<AbiturFachbelegung>();
				const abiFaecher = new Set<number>();
				for (const belegung of manager.daten().fachbelegungen) {
					if ((belegung.abiturFach === null) || (belegung.abiturFach < 1) || (belegung.abiturFach > 5))
						continue;
					if (abiFaecher.has(belegung.abiturFach))
						throw new DeveloperNotificationException("Ein Abiturfach darf nur einmal gesetzt sein. Dies muss an dieser Stelle sichergestellt werden.");
					abiFaecher.add(belegung.abiturFach);
					tmp.add(belegung);
				}
				tmp.sort(<Comparator<AbiturFachbelegung>>{ compare(a, b) { return a.abiturFach! - b.abiturFach!; } });
				for (const belegung of tmp) {
					if (belegung.abiturFach === null)
						continue;
					result.add({
						index: counter++,
						manager,
						abiturfach: belegung.abiturFach,
						schueler,
						belegung,
						hatAbiFach5: abiFaecher.has(5) || (GostBesondereLernleistung.fromKuerzel(manager.daten().besondereLernleistung) !== GostBesondereLernleistung.KEINE),
					});
				}
			}
		}
		return result;
	});

	function getPruefer(row: SchuelerAbiturbelegung): LehrerListeEintrag | null {
		if (row.belegung.block2Pruefer === null)
			return null;
		return props.mapLehrer.get(row.belegung.block2Pruefer);
	}

	function getQ22Kurs(row: SchuelerAbiturbelegung): KursDaten | null {
		const belegungQ22 = row.belegung.belegungen[GostHalbjahr.Q22.id];
		if (belegungQ22 === null)
			return null;
		return props.mapKurse.get(belegungQ22.idKurs);
	}

	function istWertungDefizit(row: SchuelerAbiturbelegung): boolean {
		if (row.belegung.block2PunkteZwischenstand === null)
			return false;
		return row.belegung.block2PunkteZwischenstand < (row.hatAbiFach5 ? 20 : 25);
	}

	function getNotenpunkteFromKuerzel(notenkuerzel: string | null, schuljahr: number) : number | null {
		if (notenkuerzel === null)
			return null;
		const nke : NoteKatalogEintrag | null = Note.fromKuerzel(notenkuerzel).daten(schuljahr);
		if ((nke === null) || (nke.notenpunkte === null))
			return null;
		return nke.notenpunkte;
	}

	function istDefizit(row: SchuelerAbiturbelegung) : boolean {
		const np = getNotenpunkteFromKuerzel(row.belegung.block2NotenKuerzelPruefung, row.manager.getSchuljahr());
		return (np !== null) && (np < 5);
	}

	function getFachgruppe(row: SchuelerAbiturbelegung): Fachgruppe | null {
		const fach = row.manager.faecher().get(row.belegung.fachID);
		if (fach === null)
			return null;
		const f = Fach.getBySchluesselOrDefault(fach.kuerzel);
		// TODO ggf. für Abi30ff zusätzlich check...
		// if ((isAbi30ff) && ((f === Fach.IN) || (f === Fach.VO)))
		// 	return null;
		return f.getFachgruppe(row.manager.getSchuljahr()) ?? null;
	}

	function getFachfarbe(row: SchuelerAbiturbelegung): string {
		const gruppe = getFachgruppe(row);
		const farbe : RGBFarbe = (gruppe === null) ? new RGBFarbe() : gruppe.getFarbe(row.manager.getSchuljahr());
		return "rgb(" + farbe.red + "," + farbe.green + "," + farbe.blue + ")";
	}

	function istAbweichungspruefung(row: SchuelerAbiturbelegung): boolean {
		return (row.manager.daten().abiturjahr <= 2019) && (row.belegung.block2MuendlichePruefungAbweichung === true);
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

	function updateNotenpunkte(row: SchuelerAbiturbelegung, value: string | null) : void {
		void props.updateAbiturpruefungsdaten(() => row.manager, { fachID: row.belegung.fachID, block2NotenKuerzelPruefung: value }, true);
	}

	function updatePflichtPruefung(row: SchuelerAbiturbelegung, value: boolean) : void {
		void props.updateAbiturpruefungsdaten(() => row.manager, { fachID: row.belegung.fachID, block2MuendlichePruefungBestehen: value }, false);
	}

	function updateFreiwilligePruefung(row: SchuelerAbiturbelegung, value: boolean) : void {
		void props.updateAbiturpruefungsdaten(() => row.manager, { fachID: row.belegung.fachID, block2MuendlichePruefungFreiwillig: value }, false);
	}

	function updatePruefungsreihenfolge(row: SchuelerAbiturbelegung, value: number | null) : void {
		void props.updateAbiturpruefungsdaten(() => row.manager, { fachID: row.belegung.fachID, block2MuendlichePruefungReihenfolge: value }, false);
	}

	function updateNotenpunkteMdl(row: SchuelerAbiturbelegung, value: string | null) : void {
		void props.updateAbiturpruefungsdaten(() => row.manager, { fachID: row.belegung.fachID, block2MuendlichePruefungNotenKuerzel: value }, false);
	}

	function inputPruefungsnote(row: SchuelerAbiturbelegung) {
		const key = 'PrüfungsnoteAbiFach_' + row.schueler.id + '_' + row.abiturfach;
		const setter = (value : string | null) => updateNotenpunkte(row, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputAbiturNotenpunkte(key, 1, row.index, element, setter, row.manager.daten().schuljahrAbitur);
			if (input !== null)
				watchEffect(() => gridManager.update(key, row.belegung.block2NotenKuerzelPruefung));
		};
	}

	function inputPflichtPruefung(row: SchuelerAbiturbelegung) {
		const key = 'PflichtPrüfungAbiFach_' + row.schueler.id + '_' + row.abiturfach;
		const setter = (value : boolean) => updatePflichtPruefung(row, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, 2, row.index, element, setter);
			if (input !== null)
				watchEffect(() => gridManager.update(key, (row.belegung.block2MuendlichePruefungBestehen === true) || (row.belegung.block2MuendlichePruefungAbweichung === true)));
		};
	}

	function inputFreiwilligePruefung(row: SchuelerAbiturbelegung) {
		const key = 'FreiwilligePrüfungAbiFach_' + row.schueler.id + '_' + row.abiturfach;
		const setter = (value : boolean) => updateFreiwilligePruefung(row, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, 3, row.index, element, setter);
			if (input !== null)
				watchEffect(() => gridManager.update(key, row.belegung.block2MuendlichePruefungFreiwillig ?? false));
		};
	}

	function inputPruefungsreihenfolge(row: SchuelerAbiturbelegung) {
		const key = 'PrüfungsreihenfolgeAbiFach_' + row.schueler.id + '_' + row.abiturfach;
		const setter = (value : number | null) => updatePruefungsreihenfolge(row, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputAbiturPruefungsreihenfolge(key, 4, row.index, element, setter);
			if (input !== null)
				watchEffect(() => gridManager.update(key, row.belegung.block2MuendlichePruefungReihenfolge));
		};
	}

	function inputPruefungsnoteMdl(row: SchuelerAbiturbelegung) {
		const key = 'PrüfungsnoteMdlAbiFach_' + row.schueler.id + '_' + row.abiturfach;
		const setter = (value : string | null) => updateNotenpunkteMdl(row, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputAbiturNotenpunkte(key, 5, row.index, element, setter, row.manager.daten().schuljahrAbitur);
			if (input !== null)
				watchEffect(() => gridManager.update(key, row.belegung.block2MuendlichePruefungNotenKuerzel));
		};
	}

</script>
