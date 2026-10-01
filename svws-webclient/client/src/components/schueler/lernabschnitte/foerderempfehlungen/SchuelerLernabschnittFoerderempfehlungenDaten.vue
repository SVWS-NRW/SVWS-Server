<template>
	<svws-ui-content-card title="" class="col-span-full mt-8">
		<h3 class="text-lg font-semibold mb-3">Allgemeine Angaben</h3>
		<div class="grid grid-cols-1 md:grid-cols-3 gap-4">
			<svws-ui-text-input placeholder="Angelegt am" type="date"
				v-model="model.proxy.datumAngelegt"
				:validation="() => model.getFehler('datumAngelegt')"
				required readonly />
			<svws-ui-text-input placeholder="Betroffene Fächer"
				v-model="model.proxy.faecher"
				@change="model.patch"
				:validation="() => model.getFehler('faecher')"
				required :max-len="255" />
			<ui-select label="Lehrkraft"
				v-model="model.lehrkraft.value"
				:deep-search-attributes="['kuerzel', 'nachname', 'vorname']"
				:manager="lehrerManager" />
		</div>
		<div class="mt-6">
			<div class="flex items-center gap-2 cursor-pointer mb-3" @click="diagnoseCollapsed = !diagnoseCollapsed">
				<span class="icon" :class="diagnoseCollapsed ? 'i-ri-arrow-down-s-line' : 'i-ri-arrow-right-s-line'" />
				<h3 class="text-lg font-semibold">Diagnose</h3>
			</div>
			<div v-if="diagnoseCollapsed" class="grid grid-cols-1 md:grid-cols-3 gap-4">
				<svws-ui-textarea-input placeholder="Inhaltliche prozessbezogene Kompetenzen" class="h-26 max-h-26 overflow-y-auto"
					v-model="model.proxy.diagnoseKompetenzenInhaltlichProzessbezogen"
					@change="model.patch"
					:validation="() => model.getFehler('diagnoseKompetenzenInhaltlichProzessbezogen')"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Methodische Kompetenzen" class="h-26 max-h-26 overflow-y-auto"
					v-model="model.proxy.diagnoseKompetenzenMethodisch"
					@change="model.patch"
					:validation="() => model.getFehler('diagnoseKompetenzenMethodisch')"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Lern- und Arbeitsverhalten" class="h-26 max-h-26 overflow-y-auto"
					v-model="model.proxy.diagnoseLernUndArbeitsverhalten"
					@change="model.patch"
					:validation="() => model.getFehler('diagnoseLernUndArbeitsverhalten')"
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
					v-model="model.proxy.massnahmeKompetenzenInhaltlichProzessbezogen"
					@change="model.patch"
					:validation="() => model.getFehler('massnahmeKompetenzenInhaltlichProzessbezogen')"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Methodische Kompetenzen" class="h-26 max-h-26 overflow-y-auto"
					v-model="model.proxy.massnahmeKompetenzenMethodische"
					@change="model.patch"
					:validation="() => model.getFehler('massnahmeKompetenzenMethodische')"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Lern- und Arbeitsverhalten" class="h-26 max-h-26 overflow-y-auto"
					v-model="model.proxy.massnahmeLernArbeitsverhalten"
					@change="model.patch"
					:validation="() => model.getFehler('massnahmeLernArbeitsverhalten')"
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
					v-model="model.proxy.verantwortlichkeitSchueler"
					@change="model.patch"
					:validation="() => model.getFehler('verantwortlichkeitSchueler')"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Erziehungsberechtigte" class="h-26 max-h-26 overflow-y-auto"
					v-model="model.proxy.verantwortlichkeitEltern"
					@change="model.patch"
					:validation="() => model.getFehler('verantwortlichkeitEltern')"
					:rows="6" resizeable="none" />
			</div>
		</div>
		<h3 class="text-lg font-semibold mt-6 mb-3">Weiteres Vorgehen</h3>
		<svws-ui-input-wrapper :grid="4" class="gap-4">
			<svws-ui-text-input placeholder="Umsetzung von" type="date"
				v-model="model.proxy.datumUmsetzungVon" />
			<svws-ui-text-input placeholder="Umsetzung bis" type="date"
				v-model="model.proxy.datumUmsetzungBis" />
			<svws-ui-text-input placeholder="Überprüfung bis" type="date"
				v-model="model.proxy.datumUeberpruefung" />
			<svws-ui-text-input placeholder="Nächstes Beratungsgespräch" type="date"
				v-model="model.proxy.datumNaechstesBeratungsgespraech" />
		</svws-ui-input-wrapper>
		<svws-ui-input-wrapper :grid="4" class="gap-4">
			<div class="col-span-2" />
			<svws-ui-checkbox v-model="model.proxy.eingabeFertig">
				Texteingabe abgeschlossen
			</svws-ui-checkbox>
			<svws-ui-checkbox v-model="model.proxy.abgeschlossen">
				Empfehlung abgeschlossen
			</svws-ui-checkbox>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';

	import type { SchuelerFoerderempfehlung } from '@core/asd/data/schueler/SchuelerFoerderempfehlung';
	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import type { List } from "@core/java/util/List";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	import { SchuelerFoerderempfehlungModelProxy } from "~/components/schueler/lernabschnitte/foerderempfehlungen/modelproxy/SchuelerFoerderempfehlungModelProxy";

	const props = defineProps<{
		selectedFoerderempfehlung: SchuelerFoerderempfehlung;
		lehrer: () => List<LehrerListeEintrag>,
		patch: (data: Partial<SchuelerFoerderempfehlung>, guid: string) => Promise<boolean>;
	}>();

	const diagnoseCollapsed = ref(true);
	const massnahmeCollapsed = ref(true);
	const verantwortlichkeitCollapsed = ref(true);

	const model = new SchuelerFoerderempfehlungModelProxy(() => props.selectedFoerderempfehlung,
		props.lehrer, (data) => props.patch(data, props.selectedFoerderempfehlung.guid ?? ''));

	const lehrerManager = new SelectManager({
		options: computed(() => props.lehrer()),
		optionDisplayText: v => `${v.nachname}, ${v.vorname}`,
		selectionDisplayText: v => `${v.nachname}, ${v.vorname}`,
	});

</script>
