<template>
	<table class="svws-ui-table h-full max-w-fit overflow-hidden" aria-label="Tabelle">
		<thead class="svws-ui-thead" aria-label="Tabellenkopf">
			<tr class="svws-ui-tr grid-cols-[24rem_24rem_8rem_16rem_4rem_8rem]">
				<th class="svws-ui-td text-left svws-divider"> <div class="w-full ml-4 text-ui-50">{{ schueler.vorname }} {{ schueler.nachname }}</div> </th>
				<th class="svws-ui-td text-center svws-divider"> <div class="w-full">Zulassung</div> </th>
				<th class="svws-ui-td text-center"> <div class="w-full">Prüfung</div> </th>
				<th class="svws-ui-td text-center"> <div class="w-full">mündliche Prüfung</div> </th>
				<th class="svws-ui-td text-center svws-divider" />
				<th class="svws-ui-td text-center"> <div class="w-full">Abitur</div> </th>
			</tr>
			<tr class="svws-ui-tr grid-cols-[4rem_4rem_16rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_8rem]">
				<th class="svws-ui-td text-center"> <div class="w-full">Abi</div> </th>
				<th class="svws-ui-td text-center"> <div class="w-full">Kürzel</div> </th>
				<th class="svws-ui-td svws-divider"> Fach </th>
				<th class="svws-ui-td text-center"> <div class="w-full">Q1.1</div> </th>
				<th class="svws-ui-td text-center"> <div class="w-full">Q1.2</div> </th>
				<th class="svws-ui-td text-center"> <div class="w-full">Q2.1</div> </th>
				<th class="svws-ui-td text-center"> <div class="w-full">Q2.2</div> </th>
				<th class="svws-ui-td text-center"> <div class="w-full">Summe</div> </th>
				<th class="svws-ui-td text-center svws-divider"> <div class="w-full">⌀</div> </th>
				<th class="svws-ui-td text-center"> <div class="w-full">Punkte</div> </th>
				<th class="svws-ui-td text-center svws-divider"> <div class="w-full">Summe</div> </th>
				<th class="svws-ui-td text-center"> <div class="w-full">Pflicht</div> </th>
				<th class="svws-ui-td text-center"> <div class="w-full">Freiw.</div> </th>
				<th class="svws-ui-td text-center"> <div class="w-full">RF</div> </th>
				<th class="svws-ui-td text-center svws-divider"> <div class="w-full">Punkte</div> </th>
				<th class="svws-ui-td text-center svws-divider"> <div class="w-full">Summe</div> </th>
				<th class="svws-ui-td text-center" :class="{
					'text-ui-onsuccess bg-ui-success': istBestanden === true,
					'text-ui-ondanger bg-ui-danger': istBestanden === false,
				}">
					<template v-if="istBestanden === true">
						<div class="w-full">Bestanden: Ja</div>
					</template>
					<template v-else-if="istBestanden === false">
						<div class="w-full">Bestanden: Nein</div>
					</template>
					<template v-else>
						<div class="w-full">???</div>
					</template>
				</th>
			</tr>
		</thead>
		<tbody class="svws-ui-tbody h-full overflow-y-auto" aria-label="Tabelleninhalt">
			<template v-for="belegung in abiBelegungen.values()" :key="`${manager().daten().schuelerID}_${belegung.abiturFach}`">
				<template v-if="belegung.abiturFach !== null">
					<tr class="svws-ui-tr grid-cols-[4rem_4rem_16rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_8rem] text-ui-static">
						<td class="svws-ui-td text-center">
							<div class="w-full">{{ belegung.abiturFach }}.</div>
						</td>
						<td class="svws-ui-td text-center" :style="{ 'background-color': getFachfarbe(belegung) }">
							<div class="w-full">{{ manager().faecher().get(belegung.fachID)?.kuerzelAnzeige ?? "???" }}</div>
						</td>
						<td class="svws-ui-td" :style="{ 'background-color': getFachfarbe(belegung) }">
							{{ manager().faecher().get(belegung.fachID)?.bezeichnung ?? "???" }}
						</td>
						<template v-for="hj in GostHalbjahr.getQualifikationsphase()" :key="hj.id">
							<td class="svws-ui-td text-center" :class="{ 'svws-divider': (hj === GostHalbjahr.Q22) }">
								<div class="w-full">
									{{ getNotenpunkteString(belegung, hj) }}
								</div>
							</td>
						</template>
						<td class="svws-ui-td svws-divider text-center">
							<div class="w-full">{{ belegung.block1PunktSumme ?? 0 }}</div>
						</td>
						<td class="svws-ui-td svws-divider text-center">
							<div class="w-full">{{ formatNotenpunkteDurchschnitt(belegung.block1NotenpunkteDurchschnitt) }}</div>
						</td>
						<td class="svws-ui-td text-center">
							<div :ref="inputPruefungsnote(belegung)" class="w-full h-full focus:ring-2" :class="{
								'font-bold text-ui-danger': istDefizit(belegung.block2NotenKuerzelPruefung)
							}" />
						</td>
						<td class="svws-ui-td svws-divider text-center">
							<div class="w-full" :class="{
								'font-bold text-ui-danger': istWertungDefizit(belegung.block2PunkteZwischenstand)
							}">
								{{ belegung.block2PunkteZwischenstand ?? '' }}
							</div>
						</td>
						<td class="svws-ui-td text-center" :class="{ 'bg-ui-75': belegung.abiturFach >= 4 }">
							<div v-if="belegung.abiturFach < 4" class="w-full">
								<span v-if="belegung.block2MuendlichePruefungBestehen === true" class="icon-sm i-ri-check-line" />
							</div>
						</td>
						<td class="svws-ui-td text-center" :class="{ 'bg-ui-75': belegung.abiturFach >= 4 }">
							<div v-if="belegung.abiturFach < 4" :ref="inputFreiwilligePruefung(belegung)" class="w-full h-full">
								<span class="icon-sm" :class="{
									'i-ri-checkbox-line': belegung.block2MuendlichePruefungFreiwillig === true,
									'i-ri-checkbox-blank-line': !(belegung.block2MuendlichePruefungFreiwillig === true)
								}" />
							</div>
						</td>
						<td class="svws-ui-td text-center" :class="{ 'bg-ui-75': belegung.abiturFach >= 4 }">
							<div v-if="belegung.abiturFach < 4" :ref="inputPruefungsreihenfolge(belegung)" class="w-full h-full focus:ring-2" />
						</td>
						<td class="svws-ui-td svws-divider text-center" :class="{ 'bg-ui-75': belegung.abiturFach >= 4 }">
							<div v-if="belegung.abiturFach < 4" :ref="inputPruefungsnoteMdl(belegung)" class="w-full h-full focus:ring-2" :class="{
								'font-bold text-ui-danger': istDefizit(belegung.block2MuendlichePruefungNotenKuerzel)
							}" />
						</td>
						<td class="svws-ui-td svws-divider text-center">
							<div class="w-full" :class="{
								'font-bold text-ui-danger': istWertungDefizit(belegung.block2Punkte)
							}">
								{{ belegung.block2Punkte ?? '' }}
							</div>
						</td>
						<td class="svws-ui-td text-center" />
					</tr>
				</template>
			</template>
		</tbody>
		<tfoot>
			<tr class="svws-ui-tr grid-cols-[24rem_16rem_4rem_4rem_4rem_4rem_16rem_4rem_4rem_4rem] text-ui-static">
				<td class="svws-ui-td svws-divider" />
				<td class="svws-ui-td text-right">
					<div class="w-full"> Gesamt (normiert): </div>
				</td>
				<td class="svws-ui-td text-center">
					<div class="w-full font-bold"> {{ manager().daten().block1PunktSummeNormiert }} </div>
				</td>
				<td class="svws-ui-td svws-divider text-center" />
				<td class="svws-ui-td text-center" />
				<td class="svws-ui-td svws-divider text-center">
					<div class="w-full">
						{{ getPunktSummePruefungen() }}
					</div>
				</td>
				<td class="svws-ui-td svws-divider text-center" />
				<td class="svws-ui-td svws-divider text-center">
					<div class="w-full font-bold">{{ manager().daten().block2PunktSumme }}</div>
				</td>
				<td class="svws-ui-td text-center">
					<div class="w-full font-bold">{{ manager().daten().gesamtPunkte }}</div>
				</td>
				<td class="svws-ui-td text-center bg-ui-brand-secondary">
					<div class="w-full font-bold">{{ manager().daten().note }}</div>
				</td>
			</tr>
		</tfoot>
	</table>
</template>

<script setup lang="ts">

	import { computed, type ComponentPublicInstance } from "vue";
	import type { AbiturFachbelegung, Comparator, Fachgruppe, JavaMap, NoteKatalogEintrag } from "@core";
	import { ArrayList, Fach, GostBesondereLernleistung, HashMap, Note, RGBFarbe } from "@core";
	import { DeveloperNotificationException, GostHalbjahr } from "@core";

	import type { SchuelerAbiturPruefungsuebersichtTabelleProps } from "./SchuelerAbiturPruefungsuebersichtTabelleProps";
	import { GridManager } from "./GridManager";

	const props = defineProps<SchuelerAbiturPruefungsuebersichtTabelleProps>();

	const gridInputManager = new GridManager<string>();

	const schuljahr = computed<number>(() => props.manager().getAbiturjahr() - 1);

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
		// TODO ggf. für Abi29ff zusätzlich check...
		// if ((isAbi29ff) && ((f === Fach.IN) || (f === Fach.VO)))
		// 	return null;
		return f.getFachgruppe(schuljahr.value) ?? null;
	}

	function getFachfarbe(belegung: AbiturFachbelegung): string {
		const gruppe = getFachgruppe(belegung);
		const farbe : RGBFarbe = (gruppe === null) ? new RGBFarbe() : gruppe.getFarbe(schuljahr.value);
		return "rgb(" + farbe.red + "," + farbe.green + "," + farbe.blue + ")";
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
		void props.updateAbiturpruefungsdaten(props.manager, { fachID: belegung.fachID, block2NotenKuerzelPruefung: value });
	}

	function updateFreiwilligePruefung(belegung: AbiturFachbelegung, value: boolean) : void {
		void props.updateAbiturpruefungsdaten(props.manager, { fachID: belegung.fachID, block2MuendlichePruefungFreiwillig: value });
	}

	function updatePruefungsreihenfolge(belegung: AbiturFachbelegung, value: number | null) : void {
		void props.updateAbiturpruefungsdaten(props.manager, { fachID: belegung.fachID, block2MuendlichePruefungReihenfolge: value });
	}

	function updateNotenpunkteMdl(belegung: AbiturFachbelegung, value: string | null) : void {
		void props.updateAbiturpruefungsdaten(props.manager, { fachID: belegung.fachID, block2MuendlichePruefungNotenKuerzel: value });
	}

	function inputPruefungsnote(belegung: AbiturFachbelegung) {
		const key = 'PrüfungsnoteAbiFach' + belegung.abiturFach;
		const getter = () => belegung.block2NotenKuerzelPruefung;
		const setter = (value : string | null) => updateNotenpunkte(belegung, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			return gridInputManager.applyInputAbiturNotenpunkte(key, 1, belegung.abiturFach!, element, getter, setter,
				props.manager().daten().schuljahrAbitur);
		};
	}

	function inputFreiwilligePruefung(belegung: AbiturFachbelegung) {
		const key = 'FreiwilligePrüfungAbiFach' + belegung.abiturFach;
		const getter = () => belegung.block2MuendlichePruefungFreiwillig ?? false;
		const setter = (value : boolean) => updateFreiwilligePruefung(belegung, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			gridInputManager.applyInputToggle(key, 2, belegung.abiturFach!, element, getter, setter);
		};
	}

	function inputPruefungsreihenfolge(belegung: AbiturFachbelegung) {
		const key = 'PrüfungsreihenfolgeAbiFach' + belegung.abiturFach;
		const getter = () => belegung.block2MuendlichePruefungReihenfolge;
		const setter = (value : number | null) => updatePruefungsreihenfolge(belegung, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			gridInputManager.applyInputAbiturPruefungsreihenfolge(key, 3, belegung.abiturFach!, element, getter, setter);
		};
	}

	function inputPruefungsnoteMdl(belegung: AbiturFachbelegung) {
		const key = 'PrüfungsnoteMdlAbiFach' + belegung.abiturFach;
		const getter = () => belegung.block2MuendlichePruefungNotenKuerzel;
		const setter = (value : string | null) => updateNotenpunkteMdl(belegung, value);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			gridInputManager.applyInputAbiturNotenpunkte(key, 4, belegung.abiturFach!, element, getter, setter,
				props.manager().daten().schuljahrAbitur);
		};
	}

</script>
