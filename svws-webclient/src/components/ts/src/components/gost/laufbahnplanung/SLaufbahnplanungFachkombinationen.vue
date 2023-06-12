<template>
	<template v-if="mapFachkombinationen.size">
		<h4 class="gap-1 flex items-center font-bold mt-5 cursor-pointer" @click="show=!show">
			<i-ri-arrow-down-s-line v-if="show" />
			<i-ri-arrow-right-s-line v-else />
			<span>Informationen zu Fachkombinationsregeln</span>
			<svws-ui-badge v-if="fehler.size" type="error">
				{{ fehler.size }}
			</svws-ui-badge>
			<i-ri-checkbox-circle-line v-else class="flex-shrink-0" style="color: rgb(var(--color-success))" />
		</h4>
		<ul class="mt-1 flex flex-col gap-1.5" v-show="show">
			<li v-for="regel in fachkombi_erforderlich" :key="regel.id" class="flex gap-1 leading-tight">
				<i-ri-checkbox-circle-line v-if="regel_umgesetzt(regel)" class="flex-shrink-0" style="color: rgb(var(--color-success))" />
				<i-ri-error-warning-line v-else class="flex-shrink-0 text-error" />
				<span>{{ regel.hinweistext }}</span>
			</li>
			<li v-for="regel in fachkombi_verboten" :key="regel.id" class="flex gap-1 leading-tight">
				<i-ri-checkbox-circle-line v-if="regel_umgesetzt(regel)" class="flex-shrink-0" style="color: rgb(var(--color-success))" />
				<i-ri-error-warning-line v-else class="flex-shrink-0 text-error" />
				<span>{{ regel.hinweistext }}</span>
			</li>
		</ul>
	</template>
</template>

<script setup lang="ts">

	import type { GostJahrgangFachkombination, GostFaecherManager, AbiturdatenManager} from "@core";
	import { GostLaufbahnplanungFachkombinationTyp, GostHalbjahr, GostKursart } from "@core";
	import { computed, ref } from "vue";

	const props = defineProps<{
		abiturdatenManager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
	}>();

	const fehler = ref(new Set());
	const show = ref(false);

	const fachkombi_erforderlich = computed((): Set<GostJahrgangFachkombination> => {
		const result = new Set<GostJahrgangFachkombination>()
		for (const kombi of props.mapFachkombinationen.values())
			if (GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH.getValue() === kombi.typ) {
				if (kombi.hinweistext === "") {
					const fach1 = props.faechermanager.get(kombi.fachID1);
					const fach2 = props.faechermanager.get(kombi.fachID2);
					const kursart1 = kombi.kursart1 ? `als ${kombi.kursart1}`: '';
					const kursart2 = kombi.kursart2 ? `als ${kombi.kursart2}` : '';
					kombi.hinweistext = `${fach1?.kuerzel} ${kursart1} erfordert ${fach2?.kuerzel} ${kursart2}`;
				}
				result.add(kombi);
			}
		return result;
	})

	const fachkombi_verboten = computed((): Set<GostJahrgangFachkombination> => {
		const result = new Set<GostJahrgangFachkombination>()
		for (const kombi of props.mapFachkombinationen.values())
			if (GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue() === kombi.typ) {
				if (kombi.hinweistext === "") {
					const fach1 = props.faechermanager.get(kombi.fachID1);
					const fach2 = props.faechermanager.get(kombi.fachID2);
					const kursart1 = kombi.kursart1 ? `als ${kombi.kursart1}`: '';
					const kursart2 = kombi.kursart2 ? `als ${kombi.kursart2}` : '';
					kombi.hinweistext = `${fach1?.kuerzel} ${kursart1} erlaubt kein ${fach2?.kuerzel} ${kursart2}`;
				}
				result.add(kombi);
			}
		return result;
	})

	function regel_umgesetzt(kombi: GostJahrgangFachkombination): boolean {
		const fach1 = props.faechermanager.get(kombi.fachID1);
		const fach2 = props.faechermanager.get(kombi.fachID2);
		const f1 = props.abiturdatenManager.getFachbelegungByKuerzel(fach1?.kuerzel || null)
		const f2 = props.abiturdatenManager.getFachbelegungByKuerzel(fach2?.kuerzel || null)
		const kursart1 = GostKursart.fromKuerzel(kombi.kursart1);
		const kursart2 = GostKursart.fromKuerzel(kombi.kursart2);
		if (kombi.typ === GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue() && (f1 === null || f2 === null) || f1 === null)
			return true;
		for (const hj of GostHalbjahr.values()) {
			if (kombi.gueltigInHalbjahr[hj.id]) {
				const bel1 = kursart1
					? props.abiturdatenManager.pruefeBelegungMitKursart(f1, kursart1, hj)
					: props.abiturdatenManager.pruefeBelegung(f1, hj);
				const bel2 = kursart2
					? props.abiturdatenManager.pruefeBelegungMitKursart(f2, kursart2, hj)
					: props.abiturdatenManager.pruefeBelegung(f2, hj);
				if (bel1 && bel2 && kombi.typ === GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue()) {
					fehler.value.add(f1.fachID)
					return false;
				}
				if (kombi.typ === GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH.getValue() && bel1 !== bel2) {
					fehler.value.add(f1.fachID)
					return false;
				}
			}
		}
		fehler.value.delete(f1.fachID)
		return true;
	}

</script>
