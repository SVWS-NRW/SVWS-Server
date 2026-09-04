<template>
	<svws-ui-modal v-model:show="modalIsOpen" size="big" class="hidden" :auto-close="false">
		<template #modalTitle>Neue Förderempfehlung</template>
		<template #modalContent>
			<h3 class="text-base font-semibold mb-2 flex justify-start">Allgemeine Angaben</h3>
			<div class="grid grid-cols-1 md:grid-cols-3 gap-4">
				<svws-ui-text-input placeholder="Angelegt am" type="date"
					v-model="model.proxy.datumAngelegt"
					:validation="() => model.getFehler('datumAngelegt')"
					required />
				<svws-ui-text-input placeholder="Betroffene Fächer"
					v-model="model.proxy.faecher"
					:validation="() => model.getFehler('faecher')"
					required :max-len="255" />
			</div>
			<h3 class="text-base font-semibold flex justify-start">Diagnose</h3>
			<div class="grid grid-cols-1 md:grid-cols-3 gap-4">
				<svws-ui-textarea-input placeholder="Inhaltliche prozessbezogene Kompetenzen" class="h-36 max-h-36 overflow-y-auto"
					v-model="model.proxy.diagnoseKompetenzenInhaltlichProzessbezogen"
					:validation="() => model.getFehler('diagnoseKompetenzenInhaltlichProzessbezogen')"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Methodische Kompetenzen" class="h-36 max-h-36 overflow-y-auto"
					v-model="model.proxy.diagnoseKompetenzenMethodisch"
					:validation="() => model.getFehler('diagnoseKompetenzenMethodisch')"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Lern- und Arbeitsverhalten" class="h-36 max-h-36 overflow-y-auto"
					v-model="model.proxy.diagnoseLernUndArbeitsverhalten"
					:validation="() => model.getFehler('diagnoseLernUndArbeitsverhalten')"
					:rows="6" resizeable="none" />
			</div>
			<h3 class="text-base font-semibold flex justify-start">Maßnahme</h3>
			<div class="grid grid-cols-1 md:grid-cols-3 gap-4">
				<svws-ui-textarea-input placeholder="Inhaltliche prozessbezogene Kompetenzen" class="h-36 max-h-36 overflow-y-auto"
					v-model="model.proxy.massnahmeKompetenzenInhaltlichProzessbezogen"
					:validation="() => model.getFehler('massnahmeKompetenzenInhaltlichProzessbezogen')"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Methodische Kompetenzen" class="h-36 max-h-36 overflow-y-auto"
					v-model="model.proxy.massnahmeKompetenzenMethodische"
					:validation="() => model.getFehler('massnahmeKompetenzenMethodische')"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Lern- und Arbeitsverhalten" class="h-36 max-h-36 overflow-y-auto"
					v-model="model.proxy.massnahmeLernArbeitsverhalten"
					:validation="() => model.getFehler('massnahmeLernArbeitsverhalten')"
					:rows="6" resizeable="none" />
			</div>
			<h3 class="text-base font-semibold flex justify-start">Verantwortlichkeit</h3>
			<div class="grid grid-cols-1 md:grid-cols-2 gap-4">
				<svws-ui-textarea-input placeholder="Schüler" class="h-36 max-h-36 overflow-y-auto"
					v-model="model.proxy.verantwortlichkeitSchueler"
					:validation="() => model.getFehler('verantwortlichkeitSchueler')"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Erziehungsberechtigte" class="h-36 max-h-36 overflow-y-auto"
					v-model="model.proxy.verantwortlichkeitEltern"
					:validation="() => model.getFehler('verantwortlichkeitEltern')"
					:rows="6" resizeable="none" />
			</div>
			<h3 class="text-base font-semibold flex justify-start">Weiteres Vorgehen</h3>
			<svws-ui-input-wrapper :grid="4" class="gap-4">
				<svws-ui-text-input type="date" placeholder="Umsetzung von" v-model="model.proxy.datumUmsetzungVon" />
				<svws-ui-text-input type="date" placeholder="Umsetzung bis" v-model="model.proxy.datumUmsetzungBis" />
				<svws-ui-text-input type="date" placeholder="Überprüfung bis" v-model="model.proxy.datumUeberpruefung" />
				<svws-ui-text-input type="date" placeholder="Nächstes Beratungsgespräch" v-model="model.proxy.datumNaechstesBeratungsgespraech" />
			</svws-ui-input-wrapper>
			<svws-ui-input-wrapper :grid="4" class="gap-4">
				<div class="col-span-2" />
				<svws-ui-checkbox class="mt-1 justify-end col-span-1 justify-self-start"
					v-model="model.proxy.eingabeFertig">
					Texteingabe abgeschlossen
				</svws-ui-checkbox>
				<svws-ui-checkbox class="mt-1 justify-end col-span-1 justify-self-start"
					v-model="model.proxy.abgeschlossen">
					Empfehlung abgeschlossen
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
			<div class="mt-7 flex gap-4 justify-end">
				<svws-ui-button type="secondary" @click="closeModal">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addFoerderempfehlung" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { SchuelerFoerderempfehlung } from '@core/asd/data/schueler/SchuelerFoerderempfehlung';
	import { computed, ref } from 'vue';
	import { SchuelerFoerderempfehlungModelProxy } from "~/components/schueler/lernabschnitte/foerderempfehlungen/modelproxy/SchuelerFoerderempfehlungModelProxy";

	const props = defineProps<{
		add: (data: Partial<SchuelerFoerderempfehlung>) => Promise<void>,
		isOpen: boolean
	}>();

	const emit = defineEmits<{
		(e: 'update:isOpen', v: boolean): void;
	}>();

	const initialData = ref<SchuelerFoerderempfehlung>(createModel());
	const model = new SchuelerFoerderempfehlungModelProxy(() => initialData.value);
	const formIsValid = computed(() => model.getAlleFehler().isEmpty());
	const modalIsOpen = computed<boolean>({
		get: () => props.isOpen,
		set: (v: boolean) => {
			initialData.value = createModel();
			emit("update:isOpen", v);
		},
	});

	function closeModal() {
		initialData.value = createModel();
		emit("update:isOpen", false);
	}

	function createModel(): SchuelerFoerderempfehlung {
		return Object.assign(new SchuelerFoerderempfehlung(), { datumAngelegt: new Date().toISOString().slice(0, 10) });
	}

	async function addFoerderempfehlung(): Promise<void> {
		const { guid, idKlasse, idLehrer, ...partialData } = model.proxy;
		await props.add(partialData);
		closeModal();
	}

</script>
