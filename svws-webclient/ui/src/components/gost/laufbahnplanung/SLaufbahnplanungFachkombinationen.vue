<template>
	<template v-if="!abiturdatenManager().faecher().getFachkombinationen().isEmpty()">
		<svws-ui-table :no-data="false" :items="[]" :columns="[{key: 'icon', label: ' ', fixedWidth: 1.8},{key: 'beschreibung', label: 'Fachkombinationsregeln'}]" type="navigation" class="svws-no-mx">
			<template #header>
				<div class="svws-ui-tr" role="row">
					<div class="svws-ui-td col-span-full" role="columnheader">
						<span class="icon i-ri-checkbox-circle-fill shrink-0 icon-ui-success -my-1" v-if="regelnVerletzt.size === 0" />
						<template v-if="regelnVerletzt.size !== 0">
							{{ regelnVerletzt.size }} Fehler bei Fachkombinationsregeln
						</template>
						<template v-else>
							Alle Fachkombinationsregeln erfüllt
						</template>
					</div>
				</div>
			</template>
			<template #body>
				<div v-for="regel in abiturdatenManager().faecher().getFachkombinationenErforderlich()" :key="regel.id" class="svws-ui-tr" role="row">
					<div class="svws-ui-td" role="cell">
						<span class="icon i-ri-check-line shrink-0 icon-ui-success" v-if="!regelnVerletzt.has(regel)" />
						<span class="icon i-ri-error-warning-line shrink-0 icon-error text-button mt-0.5" />
					</div>
					<div class="svws-ui-td select-all" role="cell">
						{{ regel.hinweistext }}
					</div>
				</div>
				<div v-for="regel in abiturdatenManager().faecher().getFachkombinationenVerboten()" :key="regel.id" class="svws-ui-tr" role="row">
					<div class="svws-ui-td" role="cell">
						<span class="icon i-ri-check-line shrink-0 icon-ui-success" v-if="!regelnVerletzt.has(regel)" />
						<span class="icon i-ri-error-warning-line shrink-0 icon-error text-button mt-0.5" />
					</div>
					<div class="svws-ui-td select-all" role="cell">
						{{ regel.hinweistext }}
					</div>
				</div>
			</template>
		</svws-ui-table>
	</template>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { AbiturdatenManager } from "../../../../../core/src/core/abschluss/gost/AbiturdatenManager";
	import type { GostJahrgangFachkombination } from "../../../../../core/src/core/data/gost/GostJahrgangFachkombination";
	import { GostKursart } from "../../../../../core/src/core/types/gost/GostKursart";
	import { GostLaufbahnplanungFachkombinationTyp } from "../../../../../core/src/core/types/gost/GostLaufbahnplanungFachkombinationTyp";
	import { GostHalbjahr } from "../../../../../core/src/core/types/gost/GostHalbjahr";

	const props = defineProps<{
		abiturdatenManager: () => AbiturdatenManager;
	}>();

	const regelnVerletzt = computed<Set<GostJahrgangFachkombination>>(() => {
		const set = new Set<GostJahrgangFachkombination>();
		for (const kombi of props.abiturdatenManager().faecher().getFachkombinationen())
			if (!istRegelErfuellt(kombi))
				set.add(kombi);
		return set;
	});

	function istRegelErfuellt(kombi: GostJahrgangFachkombination) : boolean {
		const fach1 = props.abiturdatenManager().faecher().get(kombi.fachID1);
		const fach2 = props.abiturdatenManager().faecher().get(kombi.fachID2);
		const f1 = (fach1 === null) ? null : props.abiturdatenManager().getFachbelegungByID(fach1.id);
		const f2 = (fach2 === null) ? null : props.abiturdatenManager().getFachbelegungByID(fach2.id);
		const kursart1 = GostKursart.fromKuerzel(kombi.kursart1);
		const kursart2 = GostKursart.fromKuerzel(kombi.kursart2);
		if (((kombi.typ === GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue()) && (f1 === null || f2 === null)) || (f1 === null))
			return true;
		for (const hj of GostHalbjahr.values()) {
			if (kombi.gueltigInHalbjahr[hj.id]) {
				const bel1 = kursart1 === null
					? props.abiturdatenManager().pruefeBelegung(f1, hj)
					: props.abiturdatenManager().pruefeBelegungMitKursart(f1, kursart1, hj);
				const bel2 = kursart2 === null
					? props.abiturdatenManager().pruefeBelegung(f2, hj)
					: props.abiturdatenManager().pruefeBelegungMitKursart(f2, kursart2, hj);
				if (bel1 && bel2 && kombi.typ === GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue())
					return false;
				if (kombi.typ === GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH.getValue() && bel1 && !bel2)
					return false;
			}
		}
		return true;
	}

</script>

<style lang="postcss" scoped>

	@reference "../../../assets/styles/index.css";

	.svws-ui-td {
		@apply leading-5 align-middle;
	}
</style>