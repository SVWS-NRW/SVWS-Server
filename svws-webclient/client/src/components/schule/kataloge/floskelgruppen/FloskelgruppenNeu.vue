<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input class="contentFocusField" placeholder="Kürzel" :min-len="1" :max-len="10" v-model="data.kuerzel" :disabled required
					:valid="fieldIsValid('kuerzel')" />
				<svws-ui-text-input placeholder="Bezeichnung" :min-len="1" :max-len="50" v-model="data.bezeichnung" :disabled required
					:valid="fieldIsValid('bezeichnung')" />
				<ui-select label="Floskelgruppenart" v-model="selectedFloskelgruppenart" :manager="floskelgruppenartManager" :removable="true" searchable />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addFloskelgruppe" :disabled="!formIsValid">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { FloskelgruppenNeuProps } from "./FloskelgruppenNeuProps";
	import { computed, ref, watch } from "vue";
	import { Floskelgruppenart, type FloskelgruppenartKatalogEintrag } from "@core";
	import { BenutzerKompetenz, Floskelgruppe } from "@core";
	import { isUniqueInList, mandatoryInputIsValid } from "~/util/validation/Validation";
	import { CoreTypeSelectManager } from "@ui";

	const props = defineProps<FloskelgruppenNeuProps>();
	const data = ref<Floskelgruppe>(new Floskelgruppe());
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);
	const floskelgruppenartManager = new CoreTypeSelectManager({
		clazz: Floskelgruppenart.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const selectedFloskelgruppenart = computed<FloskelgruppenartKatalogEintrag | null>({
		get: (): FloskelgruppenartKatalogEintrag | null => {
			return Floskelgruppenart.data()
				.getWertByIDOrNull(data.value.idFloskelgruppenart ?? -1)
				?.daten(props.schuljahr) ?? null;
		},
		set: (value: FloskelgruppenartKatalogEintrag | null) => {
			data.value.idFloskelgruppenart = value?.id ?? null;
		},
	});

	function kuerzelIsValid(value: string | null): boolean {
		return (mandatoryInputIsValid(value, 10) &&
			isUniqueInList(value, props.manager().liste.list(), "kuerzel"));
	}

	function bezeichnungIsValid(value: string | null): boolean {
		return (mandatoryInputIsValid(value, 50));
	}

	function fieldIsValid(field: keyof Floskelgruppe | null): (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'bezeichnung':
					return bezeichnungIsValid(data.value.bezeichnung);
				case 'kuerzel':
					return kuerzelIsValid(data.value.kuerzel);
				default:
					return true;
			}
		};
	}

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof Floskelgruppe);
			const fieldValue = data.value[field as keyof Floskelgruppe] as string | null;
			return validateField(fieldValue);
		});
	});

	async function addFloskelgruppe() {
		if (isLoading.value)
			return;

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
		if (isLoading.value)
			return;

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
