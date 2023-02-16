<template>
	<template v-if="(mapFachkombinationen.size)">
		<table class="border-collapse text-sm">
			<thead class="bg-slate-100">
				<tr>
					<td class="px-2"> Informationen zu Fachkombinationsregeln </td>
				</tr>
			</thead>
			<tbody>
				<tr v-for="regel in fachkombi_erforderlich()" :key="regel.id" class="border border-[#7f7f7f]/20 text-left">
					<td class="px-2">
						<span v-if="regel_umgesetzt(regel)" class="px-2 rounded-full bg-green-400 mr-1" /> {{ regel.hinweistext }}
					</td>
				</tr>
				<tr v-for="regel in fachkombi_verboten()" :key="regel.id" class="border border-[#7f7f7f]/20 text-left">
					<td class="px-2">
						<span v-if="regel_umgesetzt(regel)" class="px-2 rounded-full bg-green-400 mr-1" /> {{ regel.hinweistext }}
					</td>
				</tr>
			</tbody>
		</table>
	</template>
</template>

<script setup lang="ts">

	import { List, GostJahrgangFachkombination, Vector, GostLaufbahnplanungFachkombinationTyp, GostFaecherManager, AbiturdatenManager, GostHalbjahr, GostKursart } from '@svws-nrw/svws-core-ts';

	const props = defineProps<{
		abiturdatenManager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
	}>();

	const fachkombi_erforderlich = (): List<GostJahrgangFachkombination> => {
		const result = new Vector<GostJahrgangFachkombination>()
		for (const kombi of props.mapFachkombinationen.values())
			if (GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH.getValue() === kombi.typ) {
				if (kombi.hinweistext === "") {
					const fach1 = props.faechermanager.get(kombi.fachID1);
					const fach2 = props.faechermanager.get(kombi.fachID2);
					kombi.hinweistext = `${fach1?.kuerzel} als ${kombi.kursart1} erlaubt kein ${fach2} als ${kombi.kursart2}`;
				}
				result.add(kombi);
			}
		return result;
	}

	const fachkombi_verboten = (): List<GostJahrgangFachkombination> => {
		const result = new Vector<GostJahrgangFachkombination>()
		for (const kombi of props.mapFachkombinationen.values())
			if (GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue() === kombi.typ) {
				if (kombi.hinweistext === "") {
					const fach1 = props.faechermanager.get(kombi.fachID1);
					const fach2 = props.faechermanager.get(kombi.fachID2);
					kombi.hinweistext = `${fach1?.kuerzel} als ${kombi.kursart1} erfordert ${fach2} als ${kombi.kursart2}`;
				}
				result.add(kombi);
			}
		return result;
	}

	function regel_umgesetzt(kombi: GostJahrgangFachkombination): boolean {
		const fach1 = props.faechermanager.get(kombi.fachID1);
		const f1 = props.abiturdatenManager.getFachbelegungByKuerzel(fach1?.kuerzel || null)
		const fach2 = props.faechermanager.get(kombi.fachID2);
		const f2 = props.abiturdatenManager.getFachbelegungByKuerzel(fach2?.kuerzel || null)
		for (const hj of GostHalbjahr.values()) {
			if (kombi.gueltigInHalbjahr[hj.id]) {
				const belegung_1 = props.abiturdatenManager.pruefeBelegungMitKursart(f1, GostKursart.fromKuerzel(kombi.kursart1)!, hj)
				const belegung_2 = props.abiturdatenManager.pruefeBelegungMitKursart(f2, GostKursart.fromKuerzel(kombi.kursart1)!, hj);
				if (belegung_1 && belegung_2 && kombi.typ === GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue())
					return false;
				if (kombi.typ === GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH.getValue() && belegung_1 != belegung_2)
					return false;
			}
		}
		return true;
	}

</script>