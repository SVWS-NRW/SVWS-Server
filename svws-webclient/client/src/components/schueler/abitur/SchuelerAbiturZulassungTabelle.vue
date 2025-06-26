<template>
	<ui-table-grid name="Übersicht über die Prüfungsergebnisse" :footer-count="5" :data="faecherBelegtInQPhase" :cell-format="cellFormat" :get-key="getKey">
		<template #header>
			<th>Kürzel</th>
			<th class="text-left"> Fach </th>
			<th>Kursart</th>
			<th>Q1.1</th>
			<th>Q1.2</th>
			<th>Q2.1</th>
			<th>Q2.2</th>
			<th>Summe</th>
			<th>Abi</th>
			<th>⌀</th>
		</template>
		<template #default="{ row: fach, index }">
			<td :style="{ 'background-color': getFachfarbe(fach) }">{{ fach.kuerzelAnzeige }}</td>
			<td class="text-left" :style="{ 'background-color': getFachfarbe(fach) }">{{ fach.bezeichnung }}</td>
			<td class="ui-divider" :style="{ 'background-color': getFachfarbe(fach) }">{{ getKursart(fach) }}</td>
			<template v-for="hj in GostHalbjahr.getQualifikationsphase()" :key="hj.id">
				<td :ref="(updateAbiturpruefungsdaten === null) || !hatBelegung(fach, hj) ? undefined : inputMarkierungToggle(fach, index, hj)" :class="{
					'ui-table-grid-button': hatBelegung(fach, hj) && (updateAbiturpruefungsdaten !== null),
					'ui-divider': (hj === GostHalbjahr.Q22),
					'bg-ui-brand-secondary font-bold': istGewertet(fach, hj),
					'text-ui-danger': istDefizit(fach, hj),
					'underline': istSchriftlich(fach, hj)
				}">
					{{ getNotenpunkteString(fach, hj) }}
				</td>
			</template>
			<td class="ui-divider">{{ getFachSummeBlockI(fach) }}</td>
			<td>{{ getAbiFach(fach) }}</td>
			<td>{{ getDurchschnittAbiFach(fach) }}</td>
		</template>
		<template #footer="params">
			<template v-if="params.i === 1">
				<td class="col-span-3 ui-divider text-left">Punktsumme der gewerteten Kurse</td>
				<td>{{ kursinfoQ11[0] }}</td>
				<td>{{ kursinfoQ12[0] }}</td>
				<td>{{ kursinfoQ21[0] }}</td>
				<td class="ui-divider">{{ kursinfoQ22[0] }}</td>
				<td class="ui-divider font-bold">{{ (manager().daten().block1PunktSummeGK ?? 0) + (manager().daten().block1PunktSummeLK ?? 0) }}</td>
				<td class="col-span-2" />
			</template>
			<template v-else-if="params.i === 2">
				<td class="col-span-3 ui-divider text-left">Anzahl der gewerteten Kurse</td>
				<td>{{ kursinfoQ11[1] }}</td>
				<td>{{ kursinfoQ12[1] }}</td>
				<td>{{ kursinfoQ21[1] }}</td>
				<td class="ui-divider">{{ kursinfoQ22[1] }}</td>
				<td class="ui-divider font-bold">{{ anzahlKurse }}</td>
				<td class="col-span-2" />
			</template>
			<template v-else-if="params.i === 3">
				<td class="col-span-3 ui-divider text-left">Anzahl der gewerteten Defizite (GK/LK)</td>
				<td>
					<span :class="{ 'font-bold': kursinfoQ11[4] > 0 }">{{ kursinfoQ11[4] === 0 ? '-' : kursinfoQ11[4] }}</span>
					/
					<span :class="{ 'font-bold': kursinfoQ11[3] > 0 }">{{ kursinfoQ11[3] === 0 ? '-' : kursinfoQ11[3] }}</span>
				</td>
				<td>
					<span :class="{ 'font-bold': kursinfoQ12[4] > 0 }">{{ kursinfoQ12[4] === 0 ? '-' : kursinfoQ12[4] }}</span>
					/
					<span :class="{ 'font-bold': kursinfoQ12[3] > 0 }">{{ kursinfoQ12[3] === 0 ? '-' : kursinfoQ12[3] }}</span>
				</td>
				<td>
					<span :class="{ 'font-bold': kursinfoQ21[4] > 0 }">{{ kursinfoQ21[4] === 0 ? '-' : kursinfoQ21[4] }}</span>
					/
					<span :class="{ 'font-bold': kursinfoQ21[3] > 0 }">{{ kursinfoQ21[3] === 0 ? '-' : kursinfoQ21[3] }}</span>
				</td>
				<td class="ui-divider">
					<span :class="{ 'font-bold': kursinfoQ22[4] > 0 }">{{ kursinfoQ22[4] === 0 ? '-' : kursinfoQ22[4] }}</span>
					/
					<span :class="{ 'font-bold': kursinfoQ22[3] > 0 }">{{ kursinfoQ22[3] === 0 ? '-' : kursinfoQ22[3] }}</span>
				</td>
				<td class="ui-divider font-bold" :class="{ 'text-ui-danger': defiziteGesZuViele }">
					<span :class="{ 'font-bold': defiziteGK > 0 }">{{ defiziteGK === 0 ? '-' : defiziteGK }}</span>
					/
					<span :class="{ 'font-bold': defiziteLK > 0 }">{{ defiziteLK === 0 ? '-' : defiziteLK }}</span>
				</td>
				<td class="col-span-2" />
			</template>
			<template v-else-if="params.i === 4">
				<td class="col-span-3 ui-divider text-left">Durchschnitt der Notenpunkte</td>
				<td>{{ getNotenpunkteDurchschnittOfHalbjahr(GostHalbjahr.Q11) }}</td>
				<td>{{ getNotenpunkteDurchschnittOfHalbjahr(GostHalbjahr.Q12) }}</td>
				<td>{{ getNotenpunkteDurchschnittOfHalbjahr(GostHalbjahr.Q21) }}</td>
				<td class="ui-divider">{{ getNotenpunkteDurchschnittOfHalbjahr(GostHalbjahr.Q22) }}</td>
				<td class="ui-divider font-bold">{{ formatNotenpunkteDurchschnitt(manager().daten().block1NotenpunkteDurchschnitt) }}</td>
				<td class="col-span-2" />
			</template>
			<template v-else-if="params.i === 5">
				<td class="ui-divider col-span-3 text-left font-bold" :class="{
					'bg-ui-success text-ui-onsuccess' : hatZulassung,
					'bg-ui-danger text-ui-ondanger' : !hatZulassung
				}">
					Zulassung: {{ hatZulassung ? "Ja" : "Nein" }}
				</td>
				<td class="ui-divider col-span-4 text-right font-bold"> normierte Punktsumme: </td>
				<td class="ui-divider font-bold"> {{ manager().daten().block1PunktSummeNormiert }} </td>
				<td class="col-span-2" />
			</template>
		</template>
	</ui-table-grid>
</template>

<script setup lang="ts">

	import type { ComponentPublicInstance} from "vue";
	import { computed, watchEffect } from "vue";
	import type { AbiturFachbelegung, AbiturFachbelegungHalbjahr, Fachgruppe, GostFach, List} from "@core";
	import { Fach, RGBFarbe } from "@core";
	import { ArrayList, GostHalbjahr } from "@core";
	import { GridManager } from "@ui";

	import type { SchuelerAbiturZulassungTabelleProps } from "./SchuelerAbiturZulassungTabelleProps";

	const props = defineProps<SchuelerAbiturZulassungTabelleProps>();

	const gridManager = new GridManager<string>();
	const cellFormat = {
		widths: ['4rem','16rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','4rem','1.25rem'],
	};
	function getKey(fach: GostFach): string {
		let result = props.manager().daten().schuelerID + "_" + fach.id;
		const fachbelegung = props.manager().getFachbelegungByID(fach.id);
		if (fachbelegung === null)
			return result;
		for (const halbjahr of GostHalbjahr.getQualifikationsphase())
			result += "_" + (fachbelegung.belegungen[halbjahr.id]?.block1gewertet ?? "");
		return result;
	}

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

	function updateMarkierung(fach: GostFach, hj: GostHalbjahr, value: boolean) : void {
		const fachbelegung = props.manager().getFachbelegungByID(fach.id);
		if (fachbelegung === null)
			return;
		const partial = <Partial<AbiturFachbelegung>>{ fachID: fach.id, belegungen: fachbelegung.belegungen };
		if (partial.belegungen === undefined)
			return;
		const belegungHalbjahr = partial.belegungen[hj.id];
		if (belegungHalbjahr === null)
			return;
		belegungHalbjahr.block1gewertet = value;
		if (props.updateAbiturpruefungsdaten !== null)
			void props.updateAbiturpruefungsdaten(props.manager, partial);
	}

	function inputMarkierungToggle(fach: GostFach, index: number, hj: GostHalbjahr) {
		const key = 'Markierung_' + fach.id + '_' + index + '_' + hj.id;
		const setter = (value : boolean) => updateMarkierung(fach, hj, value);
		const belegungHalbjahr = props.manager().getFachbelegungByID(fach.id)?.belegungen[hj.id] ?? null;
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, hj.id, index, element, setter);
			if (input !== null)
				watchEffect(() => gridManager.update(key, belegungHalbjahr?.block1gewertet ?? false));
		};
	}

</script>
