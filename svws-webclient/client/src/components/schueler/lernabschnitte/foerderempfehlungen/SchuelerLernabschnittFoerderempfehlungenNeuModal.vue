<template>
	<svws-ui-modal v-model:show="modalIsOpen" size="big" class="hidden" :auto-close="false">
		<template #modalTitle>Neue Förderempfehlung</template>
		<template #modalContent>
			<h3 class="text-base font-semibold mb-2 flex justify-start">Allgemeine Angaben</h3>
			<div class="grid grid-cols-1 md:grid-cols-3 gap-4">
				<svws-ui-text-input placeholder="Angelegt am" type="date"
					v-model="data.datumAngelegt"
					:valid="() => fieldIsValid('datumAngelegt')" required />
				<svws-ui-text-input placeholder="Betroffene Fächer"
					v-model="data.faecher"
					:valid="() => fieldIsValid('faecher')" required :max-len="255" />
			</div>
			<h3 class="text-base font-semibold flex justify-start">Diagnose</h3>
			<div class="grid grid-cols-1 md:grid-cols-3 gap-4">
				<svws-ui-textarea-input placeholder="Inhaltliche prozessbezogene Kompetenzen" class="h-36 max-h-36 overflow-y-auto"
					:model-value="data.diagnoseKompetenzenInhaltlichProzessbezogen"
					@input="value => data.diagnoseKompetenzenInhaltlichProzessbezogen = value"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Methodische Kompetenzen" class="h-36 max-h-36 overflow-y-auto"
					:model-value="data.diagnoseKompetenzenMethodisch"
					@input="value => data.diagnoseKompetenzenMethodisch = value"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Lern- und Arbeitsverhalten" class="h-36 max-h-36 overflow-y-auto"
					:model-value="data.diagnoseLernUndArbeitsverhalten"
					@input="value => data.diagnoseLernUndArbeitsverhalten = value"
					:rows="6" resizeable="none" />
			</div>
			<h3 class="text-base font-semibold flex justify-start">Maßnahme</h3>
			<div class="grid grid-cols-1 md:grid-cols-3 gap-4">
				<svws-ui-textarea-input placeholder="Inhaltliche prozessbezogene Kompetenzen" class="h-36 max-h-36 overflow-y-auto"
					:model-value="data.massnahmeKompetenzenInhaltlichProzessbezogen"
					@input="value => data.massnahmeKompetenzenInhaltlichProzessbezogen = value"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Methodische Kompetenzen" class="h-36 max-h-36 overflow-y-auto"
					:model-value="data.massnahmeKompetenzenMethodische"
					@input="value => data.massnahmeKompetenzenMethodische = value"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Lern- und Arbeitsverhalten" class="h-36 max-h-36 overflow-y-auto"
					:model-value="data.massnahmeLernArbeitsverhalten"
					@input="value => data.massnahmeLernArbeitsverhalten = value"
					:rows="6" resizeable="none" />
			</div>
			<h3 class="text-base font-semibold flex justify-start">Verantwortlichkeit</h3>
			<div class="grid grid-cols-1 md:grid-cols-2 gap-4">
				<svws-ui-textarea-input placeholder="Schüler" class="h-36 max-h-36 overflow-y-auto"
					:model-value="data.verantwortlichkeitSchueler"
					@input="value => data.verantwortlichkeitSchueler = value"
					:rows="6" resizeable="none" />
				<svws-ui-textarea-input placeholder="Erziehungsberechtigte" class="h-36 max-h-36 overflow-y-auto"
					:model-value="data.verantwortlichkeitEltern"
					@input="value => data.verantwortlichkeitEltern = value"
					:rows="6" resizeable="none" />
			</div>
			<h3 class="text-base font-semibold flex justify-start">Weiteres Vorgehen</h3>
			<svws-ui-input-wrapper :grid="4" class="gap-4">
				<svws-ui-text-input type="date" placeholder="Umsetzung von" v-model="data.datumUmsetzungVon" />
				<svws-ui-text-input type="date" placeholder="Umsetzung bis" v-model="data.datumUmsetzungBis" />
				<svws-ui-text-input type="date" placeholder="Überprüfung bis" v-model="data.datumUeberpruefung" />
				<svws-ui-text-input type="date" placeholder="Nächstes Beratungsgespräch" v-model="data.datumNaechstesBeratungsgespraech" />
			</svws-ui-input-wrapper>
			<svws-ui-input-wrapper :grid="4" class="gap-4">
				<div class="col-span-2" />
				<svws-ui-checkbox class="mt-1 justify-end col-span-1 justify-self-start"
					v-model="data.eingabeFertig">
					Texteingabe abgeschlossen
				</svws-ui-checkbox>
				<svws-ui-checkbox class="mt-1 justify-end col-span-1 justify-self-start"
					v-model="data.abgeschlossen">
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

	import { computed, ref } from 'vue';
	import { SchuelerFoerderempfehlung, JavaString } from '@core';
	import { mandatoryInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<{
		add: (data: Partial<SchuelerFoerderempfehlung>) => Promise<void>,
		isOpen: boolean
	}>();

	const emit = defineEmits<{
		(e: 'update:isOpen', v: boolean): void;
	}>();

	const data = ref<SchuelerFoerderempfehlung>(createModel());
	const modalIsOpen = computed<boolean>({
		get: () => props.isOpen,
		set: (v: boolean) => {
			data.value = createModel();
			emit("update:isOpen", v);
		},
	});

	function closeModal() {
		data.value = createModel();
		emit("update:isOpen", false);
	}

	function createModel(): SchuelerFoerderempfehlung {
		return Object.assign(new SchuelerFoerderempfehlung(), { datumAngelegt: new Date().toISOString().slice(0, 10) });
	}

	async function addFoerderempfehlung(): Promise<void> {
		const { guid, idKlasse, idLehrer, ...partialData } = data.value;
		await props.add(partialData);
		closeModal();
	}

	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof SchuelerFoerderempfehlung));
	});

	const fieldIsValid = (field: keyof SchuelerFoerderempfehlung): boolean => {
		switch (field) {
			case 'datumAngelegt':
				return datumAngelegtIsValid();
			case 'faecher':
				return faecherIsValid();
			default:
				return true;
		}
	};

	function datumAngelegtIsValid(): boolean {
		return (data.value.datumAngelegt !== null) && !JavaString.isBlank(data.value.datumAngelegt);
	}

	function faecherIsValid(): boolean {
		return mandatoryInputIsValid(data.value.faecher, 255);
	}

</script>
