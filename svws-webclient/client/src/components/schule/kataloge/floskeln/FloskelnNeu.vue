<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input class="contentFocusField" placeholder="Kürzel" :min-len="1" :max-len="10" v-model="data.kuerzel" :disabled required
					:valid="fieldIsValid('kuerzel')" />
				<svws-ui-text-input placeholder="Text" v-model="data.text" :disabled required :valid="fieldIsValid('text')" />
				<ui-select label="Floskelgruppe" v-model="selectedFloskelgruppe" :manager="floskelgruppenManager" :removable="false" searchable required />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addFloskel" :disabled="!formIsValid">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import type { Floskelgruppe, List } from "@core";
	import { BenutzerKompetenz, Floskel } from "@core";
	import { isUniqueInList, mandatoryInputIsValid } from "~/util/validation/Validation";
	import { SelectManager } from "@ui";
	import type { FloskelnNeuProps } from "./FloskelnNeuProps";

	const props = defineProps<FloskelnNeuProps>();
	const data = ref<Floskel>(new Floskel());
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);
	const floskelgruppen = computed<List<Floskelgruppe>>(() => props.manager().getFloskelgruppen());

	const floskelgruppenManager = new SelectManager({	options: floskelgruppen, optionDisplayText: v => v.bezeichnung,
		selectionDisplayText: v => v.bezeichnung,
	});

	const selectedFloskelgruppe = computed<Floskelgruppe | null>({
		get: (): Floskelgruppe | null => props.manager().getFloskelgruppenById().get(data.value.idFloskelgruppe ?? -1) ?? null,
		set: (value: Floskelgruppe | null) => data.value.idFloskelgruppe = value?.id ?? null,
	});

	function kuerzelIsValid(value: string | null): boolean {
		return (mandatoryInputIsValid(value, 10) &&
			isUniqueInList(value, props.manager().liste.list(), "kuerzel"));
	}

	function textIsValid(value: string | null): boolean {
		return (value !== null) && (value !== "");
	}

	function fieldIsValid(field: keyof Floskel | null): (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'kuerzel':
					return kuerzelIsValid(data.value.kuerzel);
				case 'text':
					return textIsValid(data.value.text);
				case 'idFloskelgruppe':
					return (data.value.idFloskelgruppe !== null);
				default:
					return true;
			}
		};
	}

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof Floskel);
			const fieldValue = data.value[field as keyof Floskel] as string | null;
			return validateField(fieldValue);
		});
	});

	async function addFloskel() {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.goToDefaultView(null);
	}

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
