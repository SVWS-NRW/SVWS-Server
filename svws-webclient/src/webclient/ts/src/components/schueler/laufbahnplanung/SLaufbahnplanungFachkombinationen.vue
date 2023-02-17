<template>
	<template v-if="mapFachkombinationen.size">
		<h4 class="flex font-bold mt-5"> Informationen zu Fachkombinationsregeln </h4>
		<ul class="mt-1 flex flex-col gap-1.5">
			<li v-for="regel in fachkombi_erforderlich()" :key="regel.id" class="flex gap-1 leading-tight">
				<i-ri-checkbox-circle-line v-if="regel_umgesetzt(regel)" class="flex-shrink-0" style="color: rgb(var(--color-success))"/>
				<i-ri-error-warning-line v-else class="flex-shrink-0 text-error"/>
				<span :class="{'': regel_umgesetzt(regel)}">{{ regel.hinweistext }}</span>
			</li>
			<li v-for="regel in fachkombi_verboten()" :key="regel.id" class="flex gap-1 leading-tight">
				<i-ri-checkbox-circle-line v-if="regel_umgesetzt(regel)" class="flex-shrink-0" style="color: rgb(var(--color-success))"/>
				<i-ri-error-warning-line v-else class="flex-shrink-0 text-error"/>
				<span :class="{'': regel_umgesetzt(regel)}">{{ regel.hinweistext }}</span>
			</li>
		</ul>
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
