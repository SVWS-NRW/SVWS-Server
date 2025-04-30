<template>
	<table class="svws-ui-table h-full max-w-fit overflow-hidden" role="table" aria-label="Tabelle">
		<thead class="svws-ui-thead" role="rowgroup" aria-label="Tabellenkopf">
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
					<template v-for="hj in GostHalbjahr.getQualifikationsphase()">
						<td class="svws-ui-td text-center" :class="{ 
							'svws-disabled': !hatBelegung(fach, hj), 
							'svws-divider': (hj === GostHalbjahr.Q22), 
							'bg-ui-brand-secondary font-bold': istGewertet(fach, hj)
						}" role="cell">
							<div class="w-full">{{ getNotenpunkte(fach, hj) }}</div>
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
			<tr class="svws-ui-tr grid-cols-[40rem_4rem_4rem_4rem] text-ui-static" role="row">
				<td class="svws-ui-td" role="cell">
					<div class="w-full font-bold">Gesamt</div>
				</td>
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full">{{ getSummeBlockI() }}</div>
				</td>
				<td class="svws-ui-td text-center" role="cell" />
				<td class="svws-ui-td text-center" role="cell">
					<div class="w-full">{{ getDurchschnittBlockI() }}</div>
				</td>
			</tr>
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import { AbiturFachbelegungHalbjahr, Fach, Fachgruppe, GostFach, GostKursart, List, Note, RGBFarbe } from "@core";
	import { ArrayList, GostHalbjahr } from "@core";

	import type { SchuelerAbiturZulassungProps } from "./SchuelerAbiturZulassungProps";

	const props = withDefaults(defineProps<SchuelerAbiturZulassungProps>(), {
		berechnen: false,
	});

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

	function istGewertet(fach: GostFach, hj: GostHalbjahr): boolean {
		const belegung = props.manager().getFachbelegungByID(fach.id);
		if (belegung === null)
			return false;
		const hjbelegung = belegung.belegungen[hj.id];
		return ((hjbelegung !== null) && (hjbelegung.block1gewertet === true));
	}

	function getFachSummeBlockI(fach: GostFach): number {
		let summe = 0;
		const belegung = props.manager().getFachbelegungByID(fach.id);
		if (belegung === null)
			return summe;
		if (!props.berechnen)
			return belegung.block1PunktSumme || 0;
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

	function getSummeBlockI(): number {
		return (props.manager().daten().block1PunktSummeGK ?? 0) + (props.manager().daten().block1PunktSummeLK ?? 0);
	}

	function average(): number {
		return 0.0;
	}

	function getDurchschnittAbiFach(fach: GostFach): number | null {
		const belegung = props.manager().getFachbelegungByID(fach.id);
		if ((belegung === null) || (belegung.abiturFach === null))
			return null;
		if (!props.berechnen)
			return belegung.block1NotenpunkteDurchschnitt;
		// wenn noch keine Bewertung da ist, dann kann auch kein Durchschnitt bestimmt werden.
		if (!props.manager().istBewertet(GostHalbjahr.Q11) && !props.manager().istBewertet(GostHalbjahr.Q12)
			&& !props.manager().istBewertet(GostHalbjahr.Q21) && !props.manager().istBewertet(GostHalbjahr.Q22))
			return null;
		let summe = 0;
		let count = 0;
		for (const hj of GostHalbjahr.getQualifikationsphase()) {
			const hjbelegung = belegung.belegungen[hj.id];
			if (hjbelegung === null)
				continue;
			const np = getNotenpunkte(fach, hj);
			if (typeof np === "string")
				continue;
			count ++;
			summe += np;
		}
		return summe / count;
	}

	function getDurchschnittBlockI(): string {
		const ds = props.manager().daten().block1NotenpunkteDurchschnitt;
		return (ds === null) ? "" : ds.toString();
	}

</script>
