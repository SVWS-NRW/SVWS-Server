<template>
	<template v-if="!abiturdatenManager().faecher().getFachkombinationen().isEmpty()">
		<h4 class="gap-1 flex items-center font-bold mt-5 cursor-pointer" @click="show=!show">
			<span class="inline-flex gap-1 items-center">
				<template v-if="fehler.size">
					Fehler bei
				</template>
				<template v-else>
					<i-ri-checkbox-circle-fill class="flex-shrink-0 text-success" />
				</template>
				Fachkombinationsregeln
			</span>
			<svws-ui-badge v-if="fehler.size" type="error" size="big">
				{{ fehler.size }}
			</svws-ui-badge>
			<svws-ui-button type="transparent" size="small" class="ml-3 -mt-0.5">
				<template v-if="show">
					Details
					<i-ri-arrow-down-s-line />
				</template>
				<template v-else>
					Details
					<i-ri-arrow-right-s-line />
				</template>
			</svws-ui-button>
		</h4>
		<ul class="mt-1 flex flex-col gap-1.5" v-show="show">
			<li v-for="regel in abiturdatenManager().faecher().getFachkombinationenErforderlich()" :key="regel.id" class="flex gap-1 leading-tight">
				<i-ri-checkbox-circle-line v-if="regel_umgesetzt(regel)" class="flex-shrink-0 text-success" />
				<i-ri-error-warning-line v-else class="flex-shrink-0 text-error" />
				<span>{{ regel.hinweistext }}</span>
			</li>
			<li v-for="regel in abiturdatenManager().faecher().getFachkombinationenVerboten()" :key="regel.id" class="flex gap-1 leading-tight">
				<i-ri-checkbox-circle-line v-if="regel_umgesetzt(regel)" class="flex-shrink-0 text-success" />
				<i-ri-error-warning-line v-else class="flex-shrink-0 text-error" />
				<span>{{ regel.hinweistext }}</span>
			</li>
		</ul>
	</template>
</template>

<script setup lang="ts">

	import type { GostJahrgangFachkombination, AbiturdatenManager} from "@core";
	import { GostLaufbahnplanungFachkombinationTyp, GostHalbjahr, GostKursart } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		abiturdatenManager: () => AbiturdatenManager;
	}>();

	const fehler = ref(new Set());
	const show = ref(false);

	function regel_umgesetzt(kombi: GostJahrgangFachkombination): boolean {
		const fach1 = props.abiturdatenManager().faecher().get(kombi.fachID1);
		const fach2 = props.abiturdatenManager().faecher().get(kombi.fachID2);
		const f1 = (fach1 === null) ? null : props.abiturdatenManager().getFachbelegungByID(fach1.id);
		const f2 = (fach2 === null) ? null : props.abiturdatenManager().getFachbelegungByID(fach2.id);
		const kursart1 = GostKursart.fromKuerzel(kombi.kursart1);
		const kursart2 = GostKursart.fromKuerzel(kombi.kursart2);
		if (kombi.typ === GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue() && (f1 === null || f2 === null) || f1 === null)
			return true;
		for (const hj of GostHalbjahr.values()) {
			if (kombi.gueltigInHalbjahr[hj.id]) {
				const bel1 = kursart1 === null
					? props.abiturdatenManager().pruefeBelegung(f1, hj)
					: props.abiturdatenManager().pruefeBelegungMitKursart(f1, kursart1, hj);
				const bel2 = kursart2 === null
					? props.abiturdatenManager().pruefeBelegung(f2, hj)
					: props.abiturdatenManager().pruefeBelegungMitKursart(f2, kursart2, hj);
				if (bel1 && bel2 && kombi.typ === GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue()) {
					fehler.value.add(f1.fachID)
					return false;
				}
				if (kombi.typ === GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH.getValue() && bel1 && !bel2) {
					fehler.value.add(f1.fachID)
					return false;
				}
			}
		}
		fehler.value.delete(f1.fachID)
		return true;
	}

</script>
