<template>
	<table class="svws-ui-table svws-has-background h-full max-w-fit overflow-hidden" role="table" aria-label="Tabelle">
		<thead class="svws-ui-thead mb-1" role="rowgroup" aria-label="Tabellenkopf">
			<tr class="svws-ui-tr grid-cols-[4rem_16rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem]" role="row">
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Kürzel</div> </td>
				<td class="svws-ui-td" role="columnheader"> Fach </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Kursart</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Q1.1</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Q1.2</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Q2.1</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Q2.2</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Abi</div> </td>
				<td class="svws-ui-td text-center" role="columnheader"> <div class="w-full">Summe</div> </td>
			</tr>
		</thead>
		<tbody class="svws-ui-tbody h-full overflow-y-auto" role="rowgroup" aria-label="Tabelleninhalt">
			<template v-for="fach in faecherBelegtInQPhase" :key="fach.id">
				<tr class="svws-ui-tr grid-cols-[4rem_16rem_4rem_4rem_4rem_4rem_4rem_4rem_4rem] text-ui-static" :style="{ 'background-color': getFachfarbe(fach) }" role="row">
					<td class="svws-ui-td text-center" role="cell">
						<div class="w-full">{{ fach.kuerzelAnzeige }}</div>
					</td>
					<td class="svws-ui-td" role="cell">
						{{ fach.bezeichnung }}
					</td>
					<td class="svws-ui-td text-center" role="cell">
						<div class="w-full">{{ getKursart(fach) }}</div>
					</td>
					<td class="svws-ui-td text-center" :class="{ 'svws-disabled': !hatBelegung(fach, GostHalbjahr.Q11) }" role="cell">
						<div class="w-full">{{ getNotenpunkte(fach, GostHalbjahr.Q11) }}</div>
					</td>
					<td class="svws-ui-td text-center" :class="{ 'svws-disabled': !hatBelegung(fach, GostHalbjahr.Q12) }" role="cell">
						<div class="w-full">{{ getNotenpunkte(fach, GostHalbjahr.Q12) }}</div>
					</td>
					<td class="svws-ui-td text-center" :class="{ 'svws-disabled': !hatBelegung(fach, GostHalbjahr.Q21) }" role="cell">
						<div class="w-full">{{ getNotenpunkte(fach, GostHalbjahr.Q21) }}</div>
					</td>
					<td class="svws-ui-td text-center" :class="{ 'svws-disabled': !hatBelegung(fach, GostHalbjahr.Q22) }" role="cell">
						<div class="w-full">{{ getNotenpunkte(fach, GostHalbjahr.Q22) }}</div>
					</td>
					<td class="svws-ui-td text-center" role="cell">
						<div class="w-full">{{ getAbiFach(fach) }}</div>
					</td>
					<td class="svws-ui-td text-center" role="cell">
						<div class="w-full">{{ getSummeBlockI(fach) }}</div>
					</td>
				</tr>
			</template>
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import { AbiturFachbelegungHalbjahr, Fach, Fachgruppe, GostFach, GostKursart, List, Note, RGBFarbe } from "@core";
	import { ArrayList, GostHalbjahr } from "@core";

	import type { SchuelerAbiturZulassungProps } from "./SchuelerAbiturZulassungProps";

	const props = defineProps<SchuelerAbiturZulassungProps>();

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

	function getNotenpunkte(fach: GostFach, hj: GostHalbjahr): string | number {
		const belegung = props.manager().getFachbelegungByID(fach.id);
		if (belegung === null)
			return "";
		const hjbelegung = belegung.belegungen[hj.id];
		if (hjbelegung === null)
			return "";
		if ((hjbelegung.notenkuerzel === null) || (hjbelegung.notenkuerzel === "")) {
			if (hjbelegung.kursartKuerzel !== GostKursart.PJK.kuerzel)
				return "";
			const hjNext = hj.next();
			if (hjNext === null)
				return "";
			return getNotenpunkte(fach, hjNext);
		}
		const note = Note.fromKuerzel(hjbelegung.notenkuerzel);
		if (note.istNote(schuljahr.value))
			return note.daten(schuljahr.value)!.notenpunkte ?? 0;
		return hjbelegung.notenkuerzel;
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

	function getSummeBlockI(fach: GostFach): number {
		let summe = 0;
		const belegung = props.manager().getFachbelegungByID(fach.id);
		if (belegung === null)
			return summe;
		const istLK = (belegung.letzteKursart === GostKursart.LK.kuerzel);
		for (const hj of GostHalbjahr.getQualifikationsphase()) {
			const hjbelegung = belegung.belegungen[hj.id];
			if ((hjbelegung === null) || (hjbelegung.block1gewertet !== true))
				continue;
			const np = getNotenpunkte(fach, hj);
			if (typeof np === "string")
				continue;
			summe += np * (istLK ? 2 : 1);
		}
		return summe;
	}

</script>
