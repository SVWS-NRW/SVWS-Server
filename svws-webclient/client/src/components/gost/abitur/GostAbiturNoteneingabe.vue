<template>
	<div class="page page-flex-col pt-0">
		<div class="flex flex-row">
			<div class="min-w-64">
				<ui-select label="Kurs" v-model="auswahlKurs" :manager="kursSelectManager" removable />
			</div>
			<div class="min-w-64">
				<ui-select label="Prüfer" v-model="auswahlPruefer" :manager="prueferSelectManager" removable />
			</div>
		</div>
		<ui-table-grid name="Übersicht über die Prüfungsergebnisse" :header-count="2" :footer-count="0" :data="auswahlBelegungen"
			:cell-format="cellFormat" :get-key="(sb: SchuelerAbiturbelegung) => '`${sb.schueler.id}_${sb.belegung.abiturFach}`'">
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
					<th class="ui-divider">Summe</th>
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
						<td>
							<span v-if="(row.belegung.block2MuendlichePruefungBestehen === true) || istAbweichungspruefung(row)" class="icon-sm align-middle i-ri-check-line" />
						</td>
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

	import { computed, ref, shallowRef, watchEffect, type ComponentPublicInstance } from "vue";
	import type { List, AbiturFachbelegung, Comparator, Fachgruppe, NoteKatalogEintrag, SchuelerListeEintrag, AbiturdatenManager , KursDaten, LehrerListeEintrag, JavaMap } from "@core";
	import { GostHalbjahr, ArrayList, Fach, GostBesondereLernleistung, Note, RGBFarbe, DeveloperNotificationException, HashMap } from "@core";
	import { GridManager, ObjectSelectManager } from "@ui";

	import type { GostAbiturNoteneingabeProps } from "./GostAbiturNoteneingabeProps";

	const props = defineProps<GostAbiturNoteneingabeProps>();

	const gridManager = new GridManager<string>();
	const cellFormat = {
		widths: ['4rem', '4rem','16rem','16rem','6rem','6rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem'],
	};

	interface SchuelerAbiturbelegung {
		index: number,
		manager: AbiturdatenManager,
		abiturfach: number;
		schueler: SchuelerListeEintrag;
		belegung: AbiturFachbelegung;
		hatAbiFach5: boolean;
	};

	const auswahlBelegungen = computed<List<SchuelerAbiturbelegung>>(() => {
		const auswahl = new ArrayList<SchuelerAbiturbelegung>();
		const kurs = auswahlKurs.value;
		const kursBelegungen = alleKurse.value.get(kurs);
		if (kursBelegungen !== null) {
			auswahl.addAll(kursBelegungen);
			return auswahl;
		}
		const pruefer = auswahlPruefer.value;
		const prueferBelegungen = allePruefer.value.get(pruefer);
		if (prueferBelegungen !== null) {
			auswahl.addAll(prueferBelegungen);
			return auswahl;
		}
		auswahl.addAll(schuelerInPruefung.value);
		return auswahl;
	});

	const auswahlKurs = shallowRef<KursDaten | null>(null);
	const kursSelectManager = computed<ObjectSelectManager>(() => {
		const manager = new ObjectSelectManager(false, alleKurse.value.keySet(), k => k.kuerzel, k => k.kuerzel);
		manager.removable = true;
		return manager;
	});

	const auswahlPruefer = shallowRef<LehrerListeEintrag | null>(null);
	const prueferSelectManager = computed<ObjectSelectManager>(() => {
		const manager = new ObjectSelectManager(false, allePruefer.value.keySet(), l => l.kuerzel, l => l.kuerzel);
		manager.removable = true;
		return manager;
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
		let counter = 1;
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
		// TODO ggf. für Abi29ff zusätzlich check...
		// if ((isAbi29ff) && ((f === Fach.IN) || (f === Fach.VO)))
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
		void props.updateAbiturpruefungsdaten(() => row.manager, { fachID: row.belegung.fachID, block2NotenKuerzelPruefung: value });
	}

	function updateFreiwilligePruefung(row: SchuelerAbiturbelegung, value: boolean) : void {
		void props.updateAbiturpruefungsdaten(() => row.manager, { fachID: row.belegung.fachID, block2MuendlichePruefungFreiwillig: value });
	}

	function updatePruefungsreihenfolge(row: SchuelerAbiturbelegung, value: number | null) : void {
		void props.updateAbiturpruefungsdaten(() => row.manager, { fachID: row.belegung.fachID, block2MuendlichePruefungReihenfolge: value });
	}

	function updateNotenpunkteMdl(row: SchuelerAbiturbelegung, value: string | null) : void {
		void props.updateAbiturpruefungsdaten(() => row.manager, { fachID: row.belegung.fachID, block2MuendlichePruefungNotenKuerzel: value });
	}

	function inputPruefungsnote(row: SchuelerAbiturbelegung) {
		const key = 'PrüfungsnoteAbiFach_' + row.schueler.id + '_' + row.abiturfach;
		const setter = (value : string | null) => updateNotenpunkte(row, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			gridManager.applyInputAbiturNotenpunkte(key, 1, row.index, element, setter, row.manager.daten().schuljahrAbitur);
			if (element !== null)
				watchEffect(() => gridManager.update(key, row.belegung.block2NotenKuerzelPruefung));
		};
	}

	function inputFreiwilligePruefung(row: SchuelerAbiturbelegung) {
		const key = 'FreiwilligePrüfungAbiFach_' + row.schueler.id + '_' + row.abiturfach;
		const setter = (value : boolean) => updateFreiwilligePruefung(row, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			gridManager.applyInputToggle(key, 2, row.index, element, setter);
			if (element !== null)
				watchEffect(() => gridManager.update(key, row.belegung.block2MuendlichePruefungFreiwillig ?? false));
		};
	}

	function inputPruefungsreihenfolge(row: SchuelerAbiturbelegung) {
		const key = 'PrüfungsreihenfolgeAbiFach_' + row.schueler.id + '_' + row.abiturfach;
		const setter = (value : number | null) => updatePruefungsreihenfolge(row, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			gridManager.applyInputAbiturPruefungsreihenfolge(key, 3, row.index, element, setter);
			if (element !== null)
				watchEffect(() => gridManager.update(key, row.belegung.block2MuendlichePruefungReihenfolge));
		};
	}

	function inputPruefungsnoteMdl(row: SchuelerAbiturbelegung) {
		const key = 'PrüfungsnoteMdlAbiFach_' + row.schueler.id + '_' + row.abiturfach;
		const setter = (value : string | null) => updateNotenpunkteMdl(row, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			gridManager.applyInputAbiturNotenpunkte(key, 4, row.index, element, setter, row.manager.daten().schuljahrAbitur);
			if (element !== null)
				watchEffect(() => gridManager.update(key, row.belegung.block2MuendlichePruefungNotenKuerzel));
		};
	}

</script>
