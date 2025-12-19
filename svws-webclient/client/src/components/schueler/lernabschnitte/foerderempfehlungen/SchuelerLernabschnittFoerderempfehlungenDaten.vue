<template>
	<svws-ui-content-card title="" class="col-span-full mt-8">
		<h3 class="text-lg font-semibold mb-3">Allgemeine Angaben</h3>
		<div class="grid grid-cols-1 md:grid-cols-3 gap-4">
			<svws-ui-text-input placeholder="Angelegt am" type="date"
				:model-value="selectedFoerderempfehlung.datumAngelegt"
				readonly />
			<svws-ui-text-input placeholder="Betroffene Fächer"
				:model-value="selectedFoerderempfehlung.faecher"
				@change="patchFaecher"
				required :max-len="255" />
		</div>
		<div class="mt-6">
			<div class="flex items-center gap-2 cursor-pointer mb-3" @click="diagnoseCollapsed = !diagnoseCollapsed">
				<span class="icon" :class="diagnoseCollapsed ? 'i-ri-arrow-down-s-line' : 'i-ri-arrow-right-s-line'" />
				<h3 class="text-lg font-semibold">Diagnose</h3>
			</div>
			<div v-if="diagnoseCollapsed" class="grid grid-cols-1 md:grid-cols-3 gap-4">
				<svws-ui-textarea-input placeholder="Inhaltliche prozessbezogene Kompetenzen" class="h-26 max-h-26 overflow-y-auto"
					:model-value="selectedFoerderempfehlung.diagnoseKompetenzenInhaltlichProzessbezogen ?? ''"
					@change="v => patch({ diagnoseKompetenzenInhaltlichProzessbezogen: v }, selectedFoerderempfehlung.guid ?? '')"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Methodische Kompetenzen" class="h-26 max-h-26 overflow-y-auto"
					:model-value="selectedFoerderempfehlung.diagnoseKompetenzenMethodisch ?? ''"
					@change="v => patch({ diagnoseKompetenzenMethodisch: v }, selectedFoerderempfehlung.guid ?? '')"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Lern- und Arbeitsverhalten" class="h-26 max-h-26 overflow-y-auto"
					:model-value="selectedFoerderempfehlung.diagnoseLernUndArbeitsverhalten ?? ''"
					@change="v => patch({ diagnoseLernUndArbeitsverhalten: v }, selectedFoerderempfehlung.guid ?? '')"
					:rows="6" resizeable="none" />
			</div>
		</div>
		<div class="mt-6">
			<div class="flex items-center gap-2 cursor-pointer mb-3" @click="massnahmeCollapsed = !massnahmeCollapsed">
				<span class="icon" :class="massnahmeCollapsed ? 'i-ri-arrow-down-s-line' : 'i-ri-arrow-right-s-line'" />
				<h3 class="text-lg font-semibold">Empfehlung</h3>
			</div>
			<div v-if="massnahmeCollapsed" class="grid grid-cols-1 md:grid-cols-3 gap-4">
				<svws-ui-textarea-input placeholder="Inhaltliche prozessbezogene Kompetenzen" class="h-26 max-h-26 overflow-y-auto"
					:model-value="selectedFoerderempfehlung.massnahmeKompetenzenInhaltlichProzessbezogen ?? ''"
					@change="v => patch({ massnahmeKompetenzenInhaltlichProzessbezogen: v }, selectedFoerderempfehlung.guid ?? '')"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Methodische Kompetenzen" class="h-26 max-h-26 overflow-y-auto"
					:model-value="selectedFoerderempfehlung.massnahmeKompetenzenMethodische ?? ''"
					@change="v => patch({ massnahmeKompetenzenMethodische: v }, selectedFoerderempfehlung.guid ?? '')"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Lern- und Arbeitsverhalten" class="h-26 max-h-26 overflow-y-auto"
					:model-value="selectedFoerderempfehlung.massnahmeLernArbeitsverhalten ?? ''"
					@change="v => patch({ massnahmeLernArbeitsverhalten: v }, selectedFoerderempfehlung.guid ?? '')"
					:rows="6" resizeable="none" />
			</div>
		</div>
		<div class="mt-6">
			<div class="flex items-center gap-2 cursor-pointer mb-3" @click="verantwortlichkeitCollapsed = !verantwortlichkeitCollapsed">
				<span class="icon" :class="verantwortlichkeitCollapsed ? 'i-ri-arrow-down-s-line' : 'i-ri-arrow-right-s-line'" />
				<h3 class="text-lg font-semibold">Verantwortlichkeit</h3>
			</div>
			<div v-if="verantwortlichkeitCollapsed" class="grid grid-cols-1 md:grid-cols-2 gap-4">
				<svws-ui-textarea-input placeholder="Schüler" class="h-26 max-h-26 overflow-y-auto"
					:model-value="selectedFoerderempfehlung.verantwortlichkeitSchueler ?? ''"
					@change="v => patch({ verantwortlichkeitSchueler: v }, selectedFoerderempfehlung.guid ?? '')"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Erziehungsberechtigte" class="h-26 max-h-26 overflow-y-auto"
					:model-value="selectedFoerderempfehlung.verantwortlichkeitEltern ?? ''"
					@change="v => patch({ verantwortlichkeitEltern: v }, selectedFoerderempfehlung.guid ?? '')"
					:rows="6" resizeable="none" />
			</div>
		</div>
		<h3 class="text-lg font-semibold mt-6 mb-3">Weiteres Vorgehen</h3>
		<svws-ui-input-wrapper :grid="4" class="gap-4">
			<svws-ui-text-input placeholder="Umsetzung von" type="date"
				:model-value="selectedFoerderempfehlung.datumUmsetzungVon"
				@update:model-value="v => patch({ datumUmsetzungVon: v }, selectedFoerderempfehlung.guid ?? '')" />
			<svws-ui-text-input placeholder="Umsetzung bis" type="date"
				:model-value="selectedFoerderempfehlung.datumUmsetzungBis"
				@update:model-value="v => patch({ datumUmsetzungBis: v }, selectedFoerderempfehlung.guid ?? '')" />
			<svws-ui-text-input placeholder="Überprüfung bis" type="date"
				:model-value="selectedFoerderempfehlung.datumUeberpruefung"
				@update:model-value="v => patch({ datumUeberpruefung: v }, selectedFoerderempfehlung.guid ?? '')" />
			<svws-ui-text-input placeholder="Nächstes Beratungsgespräch" type="date"
				:model-value="selectedFoerderempfehlung.datumNaechstesBeratungsgespraech"
				@update:model-value="v => patch({ datumNaechstesBeratungsgespraech: v }, selectedFoerderempfehlung.guid ?? '')" />
		</svws-ui-input-wrapper>
		<svws-ui-input-wrapper :grid="4" class="gap-4">
			<div class="col-span-2" />
			<svws-ui-checkbox :model-value="selectedFoerderempfehlung.eingabeFertig"
				@update:model-value="v => patch({ eingabeFertig: v }, selectedFoerderempfehlung.guid ?? '')">
				Texteingabe abgeschlossen
			</svws-ui-checkbox>
			<svws-ui-checkbox :model-value="selectedFoerderempfehlung.abgeschlossen"
				@update:model-value="abgeschlossen => patch({ abgeschlossen }, selectedFoerderempfehlung.guid ?? '')">
				Empfehlung abgeschlossen
			</svws-ui-checkbox>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import type { SchuelerFoerderempfehlung } from '@core';
	import { mandatoryInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<{
		selectedFoerderempfehlung: SchuelerFoerderempfehlung;
		patch: (data: Partial<SchuelerFoerderempfehlung>, guid: string) => Promise<void>;
	}>();

	const diagnoseCollapsed = ref(true);
	const massnahmeCollapsed = ref(true);
	const verantwortlichkeitCollapsed = ref(true);

	function faecherIsValid(faecher: string | null): boolean {
		return mandatoryInputIsValid(faecher, 255);
	}

	async function patchFaecher(faecher: string | null): Promise<void> {
		if (faecherIsValid(faecher)) {
			await props.patch({ faecher }, props.selectedFoerderempfehlung.guid ?? '');
		}
	}

</script>
