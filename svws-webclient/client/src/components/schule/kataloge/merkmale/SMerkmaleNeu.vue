<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Merkmal anlegen">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Bezeichnung" :max-len="100" v-model="data.bezeichnung" :disabled :valid="fieldIsValid('bezeichnung')" />
				<svws-ui-text-input placeholder="Kürzel" :max-len="10" v-model="data.kuerzel" :disabled :valid="fieldIsValid('kuerzel')" />
				<svws-ui-spacing />
				<svws-ui-checkbox v-model="data.istSchuelermerkmal" :disabled>Schülermerkmal</svws-ui-checkbox>
				<div />
				<svws-ui-checkbox v-model="data.istSchulmerkmal" :disabled>Schulmerkmal</svws-ui-checkbox>
				<div class="mt-7 flex flex-row gap-4 justify end">
					<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
					<svws-ui-button @click="add" :disabled="!formIsValid || !hatKompetenzAdd">Speichern</svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { MerkmaleNeuProps } from "~/components/schule/kataloge/merkmale/SMerkmaleNeuProps";
	import { BenutzerKompetenz, Merkmal } from "@core";
	import { ref, computed, watch } from "vue";

	const props = defineProps<MerkmaleNeuProps>();
	const data = ref<Merkmal>(new Merkmal());
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);

	function fieldIsValid(field: keyof Merkmal | null) : (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'bezeichnung':
					return inputIsValid(data.value.bezeichnung, 100);
				case 'sortierung':
					return inputIsValid(data.value.kuerzel, 10);
				default:
					return true;
			}
		}
	}

	function inputIsValid(input: string | null, maxLength: number) {
		if (input === null)
			return true;
		return input.length <= maxLength;
	}

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof Merkmal);
			const fieldValue = data.value[field as keyof Merkmal] as string | null;
			return validateField(fieldValue);
		})
	})

	async function add() {
		if (isLoading.value)
			return;

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, ...partialData } = data.value;
		await props.addMerkmal(partialData);
		isLoading.value = false;
	}

	function cancel() {
		props.checkpoint.active = false;
		void props.goToDefaultView(null);
	}

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;
		props.checkpoint.active = true;
	}, {immediate: false, deep: true});

</script>
