<template>
	<table class="svws-ui-table h-full max-w-fit overflow-hidden" role="table" aria-label="Tabelle">
		<thead class="svws-ui-thead" role="rowgroup" aria-label="Tabellenkopf">
			<tr class="svws-ui-tr grid-cols-[24rem_20rem_8rem] text-ui-static" role="row" :class="{
				'bg-ui-success text-ui-onsuccess' : hatZulassung,
				'bg-ui-danger text-ui-ondanger' : !hatZulassung
			}">
				<td class="svws-ui-td text-center svws-divider" role="cell">
					<div class="font-bold text-base leading-0">Zulassung:</div>
					<div class="font-bold text-base leading-0"> {{ hatZulassung ? "Ja" : "Nein" }} </div>
				</td>
				<td class="svws-ui-td text-left" role="cell">
					<div class="font-bold text-base">normierte Punktsumme:</div>
					<div class="w-16 font-bold text-base">{{ manager().daten().block1PunktSummeNormiert }}</div>
				</td>
				<td class="svws-ui-td text-center" role="cell" />
			</tr>
			<tr class="svws-ui-tr grid-cols-[4rem_16rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem]" role="row">
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Kürzel</div> </td>
				<td class="svws-ui-td" role="columnheader"> Fach </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Kursart</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Q1.1</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Q1.2</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Q2.1</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Q2.2</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Summe</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Abi</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">⌀</div> </td>
			</tr>
		</thead>
		<tbody class="svws-ui-tbody h-full overflow-y-auto" role="rowgroup" aria-label="Tabelleninhalt">
			<template v-for="fach in faecherBelegtInQPhase" :key="fach.id">
				<tr class="svws-ui-tr grid-cols-[4rem_16rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem] text-ui-static" role="row">
					<td class="svws-ui-td text-center" :style="{ 'background-color': getFachfarbe(fach) }" role="cell">
						<div class="w-full">{{ fach.kuerzelAnzeige }}</div>
					</td>
					<td class="svws-ui-td" :style="{ 'background-color': getFachfarbe(fach) }" role="cell">
						{{ fach.bezeichnung }}
					</td>
					<td class="svws-ui-td svws-divider text-center" :style="{ 'background-color': getFachfarbe(fach) }" role="cell">
						<div class="w-full">{{ getKursart(fach) }}</div>
					</td>
					<template v-for="hj in GostHalbjahr.getQualifikationsphase()" :key="hj.id">
						<td class="svws-ui-td text-center" :class="{
							'svws-disabled': !hatBelegung(fach, hj),
							'svws-divider': (hj === GostHalbjahr.Q22),
							'bg-ui-brand-secondary font-bold': istGewertet(fach, hj),
							'text-ui-danger': istDefizit(fach, hj),
							'underline': istSchriftlich(fach, hj)
						}" role="cell">
							<div class="w-full">
								{{ getNotenpunkteString(fach, hj) }}
							</div>
						</td>
					</template>
					<td class="svws-ui-td svws-divider text-center" role="cell">
						<div class="w-full">{{ getFachSummeBlockI(fach) }}</div>
					</td>
					<td class="svws-ui-td text-center" role="cell">
						<div class="w-full">{{ getAbiFach(fach) }}</div>
					</td>
					<td class="svws-ui-td text-center" role="cell">
						<div class="w-full">{{ getDurchschnittAbiFach(fach) }}</div>
					</td>
				</tr>
			</template>
			<tr class="svws-ui-tr grid-cols-[24rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem] text-ui-static" role="row">
				<td class="svws-ui-td svws-divider" role="cell">
					<div class="w-full">Punktsumme der gewerteten Kurse</div>
				</td>
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full">{{ kursinfoQ11[0] }}</div>
				</td>
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full">{{ kursinfoQ12[0] }}</div>
				</td>
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full">{{ kursinfoQ21[0] }}</div>
				</td>
				<td class="svws-ui-td text-center svws-divider" role="cell">
					<div class="w-full">{{ kursinfoQ22[0] }}</div>
				</td>
				<td class="svws-ui-td text-center svws-divider" role="cell">
					<div class="w-full font-bold">{{ (manager().daten().block1PunktSummeGK ?? 0) + (manager().daten().block1PunktSummeLK ?? 0) }}</div>
				</td>
				<td class="svws-ui-td text-center" role="cell" />
				<td class="svws-ui-td text-center" role="cell" />
			</tr>
			<tr class="svws-ui-tr grid-cols-[24rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem] text-ui-static" role="row">
				<td class="svws-ui-td svws-divider" role="cell">
					<div class="w-full">Anzahl der gewerteten Kurse</div>
				</td>
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full">{{ kursinfoQ11[1] }}</div>
				</td>
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full">{{ kursinfoQ12[1] }}</div>
				</td>
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full">{{ kursinfoQ21[1] }}</div>
				</td>
				<td class="svws-ui-td text-center svws-divider" role="cell">
					<div class="w-full">{{ kursinfoQ22[1] }}</div>
				</td>
				<td class="svws-ui-td text-center svws-divider" role="cell">
					<div class="w-full font-bold">{{ anzahlKurse }}</div>
				</td>
				<td class="svws-ui-td text-center" role="cell" />
				<td class="svws-ui-td text-center" role="cell" />
			</tr>
			<tr class="svws-ui-tr grid-cols-[24rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem] text-ui-static" role="row">
				<td class="svws-ui-td svws-divider" role="cell">
					<div class="w-full">Anzahl der gewerteten Defizite (GK/LK) </div>
				</td>
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full">
						<span :class="{ 'font-bold': kursinfoQ11[4] > 0 }">{{ kursinfoQ11[4] === 0 ? '-' : kursinfoQ11[4] }}</span>
						/
						<span :class="{ 'font-bold': kursinfoQ11[3] > 0 }">{{ kursinfoQ11[3] === 0 ? '-' : kursinfoQ11[3] }}</span>
					</div>
				</td>
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full">
						<span :class="{ 'font-bold': kursinfoQ12[4] > 0 }">{{ kursinfoQ12[4] === 0 ? '-' : kursinfoQ12[4] }}</span>
						/
						<span :class="{ 'font-bold': kursinfoQ12[3] > 0 }">{{ kursinfoQ12[3] === 0 ? '-' : kursinfoQ12[3] }}</span>
					</div>
				</td>
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full">
						<span :class="{ 'font-bold': kursinfoQ21[4] > 0 }">{{ kursinfoQ21[4] === 0 ? '-' : kursinfoQ21[4] }}</span>
						/
						<span :class="{ 'font-bold': kursinfoQ21[3] > 0 }">{{ kursinfoQ21[3] === 0 ? '-' : kursinfoQ21[3] }}</span>
					</div>
				</td>
				<td class="svws-ui-td text-center svws-divider" role="cell">
					<div class="w-full">
						<span :class="{ 'font-bold': kursinfoQ22[4] > 0 }">{{ kursinfoQ22[4] === 0 ? '-' : kursinfoQ22[4] }}</span>
						/
						<span :class="{ 'font-bold': kursinfoQ22[3] > 0 }">{{ kursinfoQ22[3] === 0 ? '-' : kursinfoQ22[3] }}</span>
					</div>
				</td>
				<td class="svws-ui-td text-center svws-divider" role="cell">
					<div class="w-full font-bold" :class="{ 'text-ui-danger': defiziteGesZuViele }">
						<span :class="{ 'font-bold': defiziteGK > 0 }">{{ defiziteGK === 0 ? '-' : defiziteGK }}</span>
						/
						<span :class="{ 'font-bold': defiziteLK > 0 }">{{ defiziteLK === 0 ? '-' : defiziteLK }}</span>
					</div>
				</td>
				<td class="svws-ui-td text-center" role="cell" />
				<td class="svws-ui-td text-center" role="cell" />
			</tr>
			<tr class="svws-ui-tr grid-cols-[24rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem] text-ui-static" role="row">
				<td class="svws-ui-td svws-divider" role="cell">
					<div class="w-full">Durchschnitt der Notenpunkte</div>
				</td>
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full">{{ getNotenpunkteDurchschnittOfHalbjahr(GostHalbjahr.Q11) }}</div>
				</td>
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full">{{ getNotenpunkteDurchschnittOfHalbjahr(GostHalbjahr.Q12) }}</div>
				</td>
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full">{{ getNotenpunkteDurchschnittOfHalbjahr(GostHalbjahr.Q21) }}</div>
				</td>
				<td class="svws-ui-td text-center svws-divider" role="cell">
					<div class="w-full">{{ getNotenpunkteDurchschnittOfHalbjahr(GostHalbjahr.Q22) }}</div>
				</td>
				<td class="svws-ui-td text-center svws-divider" role="cell">
					<div class="w-full font-bold">{{ formatNotenpunkteDurchschnitt(manager().daten().block1NotenpunkteDurchschnitt) }}</div>
				</td>
				<td class="svws-ui-td text-center" role="cell" />
				<td class="svws-ui-td text-center" role="cell" />
			</tr>
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { AbiturFachbelegungHalbjahr, Fachgruppe, GostFach, List} from "@core";
	import { Fach, RGBFarbe } from "@core";
	import { ArrayList, GostHalbjahr } from "@core";

	import type { SchuelerAbiturZulassungTabelleProps } from "./SchuelerAbiturZulassungTabelleProps";

	const props = defineProps<SchuelerAbiturZulassungTabelleProps>();

	const schuljahr = computed<number>(() => props.manager().getAbiturjahr() - 1);

	const faecherBelegtInQPhase = computed<List<GostFach>>(() => {
		const result = new ArrayList<GostFach>();
		for (const fach of props.manager().faecher().faecher()) {
			const fb = props.manager().getFachbelegungByID(fach.id);
			if (fb === null)
				continue;
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				const fbh : AbiturFachbelegungHalbjahr | null = fb.belegungen[halbjahr.id];
				if ((fbh !== null)) {
					result.add(fach);
					break;
				}
			}
		}
		return result;
	});

	const hatZulassung = computed<boolean>(() => props.manager().daten().block1Zulassung ?? false);

	function getAbiFach(fach: GostFach): string {
		const belegung = props.manager().getFachbelegungByID(fach.id);
		if (belegung === null)
			return "???";
		return (belegung.abiturFach === null) ? "" : "" + belegung.abiturFach;
	}

	function getKursart(fach: GostFach): string {
		const belegung = props.manager().getFachbelegungByID(fach.id);
		if ((belegung === null) || (belegung.letzteKursart === null))
			return "???";
		return belegung.letzteKursart;
	}

	function hatBelegung(fach: GostFach, hj: GostHalbjahr) : boolean {
		const belegung = props.manager().getFachbelegungByID(fach.id);
		if (belegung === null)
			return false;
		return (belegung.belegungen[hj.id] !== null)
	}

	function istSchriftlich(fach: GostFach, hj: GostHalbjahr) : boolean {
		const belegung = props.manager().getFachbelegungByID(fach.id);
		if (belegung === null)
			return false;
		const belegungHalbjahr = belegung.belegungen[hj.id];
		return (belegungHalbjahr !== null) && (belegungHalbjahr.schriftlich);
	}

	function getNotenpunkteString(fach: GostFach, hj: GostHalbjahr) : string {
		const np = props.manager().getNotenpunkteByFachIDAndHalbjahr(fach.id, hj);
		if (np === null)
			return "";
		return ((np < 10) ? "0" : "") + np;
	}

	function getFachgruppe(fach: GostFach): Fachgruppe | null {
		const f = Fach.getBySchluesselOrDefault(fach.kuerzel);
		// TODO ggf. für Abi29ff zusätzlich check...
		// if ((isAbi29ff) && ((f === Fach.IN) || (f === Fach.VO)))
		// 	return null;
		return f.getFachgruppe(schuljahr.value) ?? null;
	}

	function getFachfarbe(fach: GostFach): string {
		const gruppe = getFachgruppe(fach);
		const farbe : RGBFarbe = (gruppe === null) ? new RGBFarbe() : gruppe.getFarbe(schuljahr.value);
		return "rgb(" + farbe.red + "," + farbe.green + "," + farbe.blue + ")";
	}

	function istGewertet(fach: GostFach, hj: GostHalbjahr): boolean {
		const belegung = props.manager().getFachbelegungByID(fach.id);
		if (belegung === null)
			return false;
		const hjbelegung = belegung.belegungen[hj.id];
		return ((hjbelegung !== null) && (hjbelegung.block1gewertet === true));
	}

	function getFachSummeBlockI(fach: GostFach): number {
		const belegung = props.manager().getFachbelegungByID(fach.id);
		if (belegung === null)
			return 0;
		return belegung.block1PunktSumme ?? 0;
	}

	const anzahlKurse = computed<number>(() => props.manager().daten().block1AnzahlKurse ?? 0);

	const defiziteGes = computed<number>(() => props.manager().daten().block1DefiziteGesamt ?? 0);
	const defiziteLK = computed<number>(() => props.manager().daten().block1DefiziteLK ?? 0);
	const defiziteGK = computed<number>(() => defiziteGes.value - defiziteLK.value);
	const defiziteGesZuViele = computed<boolean>(() => (defiziteGes.value > 8) || ((anzahlKurse.value < 38) && (defiziteGes.value > 7)));

	const kursinfoQ11 = computed<Array<number>>(() => props.manager().getKursinformationenOfMarkierteKurseByHalbjahr(GostHalbjahr.Q11));
	const kursinfoQ12 = computed<Array<number>>(() => props.manager().getKursinformationenOfMarkierteKurseByHalbjahr(GostHalbjahr.Q12));
	const kursinfoQ21 = computed<Array<number>>(() => props.manager().getKursinformationenOfMarkierteKurseByHalbjahr(GostHalbjahr.Q21));
	const kursinfoQ22 = computed<Array<number>>(() => props.manager().getKursinformationenOfMarkierteKurseByHalbjahr(GostHalbjahr.Q22));

	function getKursInfo(halbjahr: GostHalbjahr): Array<number> {
		switch (halbjahr) {
			case GostHalbjahr.Q11: return kursinfoQ11.value;
			case GostHalbjahr.Q12: return kursinfoQ12.value;
			case GostHalbjahr.Q21: return kursinfoQ21.value;
			case GostHalbjahr.Q22: return kursinfoQ22.value;
		}
		return new Array<number>();
	}

	function istDefizit(fach: GostFach, hj: GostHalbjahr) : boolean {
		const np = props.manager().getNotenpunkteByFachIDAndHalbjahr(fach.id, hj);
		return (np !== null) && (np < 5);
	}

	function getDurchschnittAbiFach(fach: GostFach): string {
		const belegung = props.manager().getFachbelegungByID(fach.id);
		const avg = (belegung === null) || (belegung.abiturFach === null) ? null : belegung.block1NotenpunkteDurchschnitt;
		return formatNotenpunkteDurchschnitt(avg);
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

	function getNotenpunkteDurchschnittOfHalbjahr(halbjahr: GostHalbjahr): string {
		const kursinfo = getKursInfo(halbjahr);
		const avg = (kursinfo[2] === 0) ? null : Math.round((kursinfo[0] / kursinfo[2]) * 100.0) / 100.0;
		return formatNotenpunkteDurchschnitt(avg);
	}

</script>
