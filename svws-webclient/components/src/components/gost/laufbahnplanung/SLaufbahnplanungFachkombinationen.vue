<template>
	<template v-if="!abiturdatenManager().faecher().getFachkombinationen().isEmpty()">
		<svws-ui-table :no-data="fehler.size === 0" :items="[]" :columns="[{key: 'icon', label: ' ', fixedWidth: 2.25},{key: 'beschreibung', label: (fehler.size ? 'Fehler bei Fachkombinationsregeln' : 'Alle Fachkombinationsregeln erfüllt')}]" class="overflow-visible">
			<template #header(icon)>
				<span v-if="fehler.size" class="rounded w-[1.75rem] inline-flex items-center justify-center bg-error text-white border-2 border-error -m-1">{{ fehler.size }}</span>
				<i-ri-checkbox-circle-fill v-else class="flex-shrink-0 text-success text-headline-md" />
			</template>
			<template #body>
				<div v-for="regel in abiturdatenManager().faecher().getFachkombinationenErforderlich()" :key="regel.id" class="svws-ui-tr" role="row">
					<div class="svws-ui-td" role="cell">
						<i-ri-checkbox-circle-line v-if="regel_umgesetzt(regel)" class="flex-shrink-0 text-success" />
						<i-ri-error-warning-line v-else class="flex-shrink-0 text-error" />
					</div>
					<div class="svws-ui-td leading-tight select-all" role="cell">
						{{ regel.hinweistext }}
					</div>
				</div>
				<div v-for="regel in abiturdatenManager().faecher().getFachkombinationenVerboten()" :key="regel.id" class="svws-ui-tr" role="row">
					<div class="svws-ui-td" role="cell">
						<i-ri-checkbox-circle-line v-if="regel_umgesetzt(regel)" class="flex-shrink-0 text-success" />
						<i-ri-error-warning-line v-else class="flex-shrink-0 text-error" />
					</div>
					<div class="svws-ui-td leading-tight select-all" role="cell">
						{{ regel.hinweistext }}
					</div>
				</div>
			</template>
		</svws-ui-table>
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
