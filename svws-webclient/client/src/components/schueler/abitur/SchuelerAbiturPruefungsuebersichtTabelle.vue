<template>
	<table class="svws-ui-table h-full max-w-fit overflow-hidden" role="table" aria-label="Tabelle">
		<thead class="svws-ui-thead" role="rowgroup" aria-label="Tabellenkopf">
			<tr class="svws-ui-tr grid-cols-[24rem_24rem_8rem_16rem_4rem_8rem]" role="row">
				<td class="svws-ui-td text-center svws-divider" role="columnheader" />
				<td class="svws-ui-td text-center svws-divider" role="columnheader"> <div class="w-full">Zulassung</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Prüfung</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">mündliche Prüfung</div> </td>
				<td class="svws-ui-td text-center svws-divider" role="columnheader" />
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Abitur</div> </td>
			</tr>
			<tr class="svws-ui-tr grid-cols-[4rem_4rem_16rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_8rem]" role="row">
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Abi</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Kürzel</div> </td>
				<td class="svws-ui-td svws-divider" role="columnheader"> Fach </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Q1.1</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Q1.2</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Q2.1</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Q2.2</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Summe</div> </td>
				<td class="svws-ui-td text-center svws-divider" role="columnheader"> <div class="w-full">⌀</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Punkte</div> </td>
				<td class="svws-ui-td text-center svws-divider" role="columnheader"> <div class="w-full">Summe</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Pflicht</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Freiw.</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">RF</div> </td>
				<td class="svws-ui-td text-center svws-divider" role="columnheader"> <div class="w-full">Punkte</div> </td>
				<td class="svws-ui-td text-center svws-divider" role="columnheader"> <div class="w-full">Punkte</div> </td>
				<td class="svws-ui-td text-center" role="columnheader" :class="{
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
				</td>
			</tr>
		</thead>
		<tbody class="svws-ui-tbody h-full overflow-y-auto" role="rowgroup" aria-label="Tabelleninhalt">
			<template v-for="i in 5" :key="abiBelegungen[i]">
				<template v-if="(abiBelegungen[i] !== null)">
					<tr class="svws-ui-tr grid-cols-[4rem_4rem_16rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_8rem] text-ui-static" role="row">
						<td class="svws-ui-td text-center" role="cell">
							<div class="w-full">{{ abiBelegungen[i].abiturFach }}.</div>
						</td>
						<td class="svws-ui-td text-center" :style="{ 'background-color': getFachfarbe(abiBelegungen[i]) }" role="cell">
							<div class="w-full">{{ manager().faecher().get(abiBelegungen[i].fachID)?.kuerzelAnzeige ?? "???" }}</div>
						</td>
						<td class="svws-ui-td" :style="{ 'background-color': getFachfarbe(abiBelegungen[i]) }" role="cell">
							{{ manager().faecher().get(abiBelegungen[i].fachID)?.bezeichnung ?? "???" }}
						</td>
						<template v-for="hj in GostHalbjahr.getQualifikationsphase()" :key="hj.id">
							<td class="svws-ui-td text-center" :class="{ 'svws-divider': (hj === GostHalbjahr.Q22) }" role="cell">
								<div class="w-full">
									{{ getNotenpunkteString(abiBelegungen[i], hj) }}
								</div>
							</td>
						</template>
						<td class="svws-ui-td svws-divider text-center" role="cell">
							<div class="w-full">{{ abiBelegungen[i].block1PunktSumme ?? 0 }}</div>
						</td>
						<td class="svws-ui-td svws-divider text-center" role="cell">
							<div class="w-full">{{ formatNotenpunkteDurchschnitt(abiBelegungen[i].block1NotenpunkteDurchschnitt) }}</div>
						</td>
						<td class="svws-ui-td text-center" role="cell">
							<div class="w-full" :class="{
								'font-bold text-ui-danger': istDefizit(abiBelegungen[i].block2NotenKuerzelPruefung)
							}">
								{{ getNotenpunkteStringFromKuerzel(abiBelegungen[i].block2NotenKuerzelPruefung) }}
							</div>
						</td>
						<td class="svws-ui-td svws-divider text-center" role="cell">
							<div class="w-full" :class="{
								'font-bold text-ui-danger': istWertungDefizit(abiBelegungen[i].block2PunkteZwischenstand)
							}">
								{{ abiBelegungen[i].block2PunkteZwischenstand ?? '' }}
							</div>
						</td>
						<td class="svws-ui-td text-center" role="cell" :class="{ 'bg-ui-75': i >= 4 }">
							<div class="w-full">
								<span v-if="abiBelegungen[i].block2MuendlichePruefungBestehen === true" class="icon-sm i-ri-check-line" />
							</div>
						</td>
						<td class="svws-ui-td text-center" role="cell" :class="{ 'bg-ui-75': i >= 4 }">
							<div class="w-full">
								<span v-if="abiBelegungen[i].block2MuendlichePruefungFreiwillig === true" class="icon-sm i-ri-check-line" />
							</div>
						</td>
						<td class="svws-ui-td text-center" role="cell" :class="{ 'bg-ui-75': i >= 4 }">
							<div class="w-full">{{ abiBelegungen[i].block2MuendlichePruefungReihenfolge ?? '' }}</div>
						</td>
						<td class="svws-ui-td svws-divider text-center" role="cell" :class="{ 'bg-ui-75': i >= 4 }">
							<div class="w-full">{{ getNotenpunkteStringFromKuerzel(abiBelegungen[i].block2MuendlichePruefungNotenKuerzel) }}</div>
						</td>
						<td class="svws-ui-td svws-divider text-center" role="cell">
							<div class="w-full" :class="{
								'font-bold text-ui-danger': istWertungDefizit(abiBelegungen[i].block2Punkte)
							}">
								{{ abiBelegungen[i].block2Punkte ?? '' }}
							</div>
						</td>
						<td class="svws-ui-td text-center" role="cell" />
					</tr>
				</template>
			</template>
			<tr class="svws-ui-tr grid-cols-[24rem_16rem_4rem_4rem_4rem_4rem_16rem_4rem_4rem_4rem] text-ui-static" role="row">
				<td class="svws-ui-td svws-divider" role="cell" />
				<td class="svws-ui-td text-right" role="cell">
					<div class="w-full"> Gesamt (normiert): </div>
				</td>
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full font-bold"> {{ manager().daten().block1PunktSummeNormiert }} </div>
				</td>
				<td class="svws-ui-td svws-divider text-center" role="cell" />
				<td class="svws-ui-td text-center" role="cell" />
				<td class="svws-ui-td svws-divider text-center" role="cell">
					<div class="w-full">
						{{ getPunktSummePruefungen() }}
					</div>
				</td>
				<td class="svws-ui-td svws-divider text-center" role="cell" />
				<td class="svws-ui-td svws-divider text-center" role="cell">
					<div class="w-full font-bold">{{ manager().daten().block2PunktSumme }}</div>
				</td>
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full font-bold">{{ manager().daten().gesamtPunkte }}</div>
				</td>
				<td class="svws-ui-td text-center bg-ui-brand-secondary" role="cell">
					<div class="w-full font-bold">{{ manager().daten().note }}</div>
				</td>
			</tr>
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { AbiturFachbelegung, Fachgruppe, NoteKatalogEintrag } from "@core";
	import { Fach, GostBesondereLernleistung, Note, RGBFarbe } from "@core";
	import { DeveloperNotificationException, GostHalbjahr } from "@core";

	import type { SchuelerAbiturPruefungsuebersichtTabelleProps } from "./SchuelerAbiturPruefungsuebersichtTabelleProps";

	const props = defineProps<SchuelerAbiturPruefungsuebersichtTabelleProps>();

	const schuljahr = computed<number>(() => props.manager().getAbiturjahr() - 1);

	const abiBelegungen = computed<Array<AbiturFachbelegung | null>>(() => {
		const result = new Array<AbiturFachbelegung | null>();
		for (let i = 1; i < 6; i++)
			result[i] = null;
		for (const belegung of props.manager().daten().fachbelegungen) {
			if ((belegung.abiturFach === null) || (belegung.abiturFach < 1) || (belegung.abiturFach > 5))
				continue;
			if (result[belegung.abiturFach] !== null)
				throw new DeveloperNotificationException("Ein Abiturfach darf nur einmal gesetzt sein. Dies muss an dieser Stelle sichergestellt werden.");
			result[belegung.abiturFach] = belegung;
		}
		return result;
	});

	const hatBLL = computed<boolean>(() => GostBesondereLernleistung.fromKuerzel(props.manager().daten().besondereLernleistung) !== GostBesondereLernleistung.KEINE);
	const hatAbiFach5 = computed<boolean>(() => (abiBelegungen.value[5] !== null) || hatBLL.value);

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

	function getNotenpunkteStringFromKuerzel(notenkuerzel: string | null) : string {
		const np = getNotenpunkteFromKuerzel(notenkuerzel);
		if (np === null)
			return "";
		return ((np < 10) ? "0" : "") + np;
	}

	function getPunktSummePruefungen() {
		let summe = 0;
		for (let i = 1; i <= 5; i++) {
			const belegung = abiBelegungen.value[i];
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

</script>
