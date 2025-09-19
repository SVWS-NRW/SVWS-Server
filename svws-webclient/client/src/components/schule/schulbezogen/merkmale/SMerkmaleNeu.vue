<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Bezeichnung" :max-len="100" :min-len="1" v-model="data.bezeichnung" :disabled
					:valid="fieldIsValid('bezeichnung')" required />
				<div v-if="!isUniqueInList(data.bezeichnung, props.manager().liste.list(), 'bezeichnung')" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung wird bereits verwendet. </p>
				</div>
				<div v-if="bezeichnungIsTooLong" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung verwendet zu viele Zeichen. </p>
				</div>
				<svws-ui-text-input placeholder="Kürzel" :max-len="10" :min-len="1" v-model="data.kuerzel" :disabled
					:valid="fieldIsValid('kuerzel')" required />
				<div v-if="!isUniqueInList(data.kuerzel, props.manager().liste.list(), 'kuerzel')" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Dieses Kürzel wird bereits verwendet. </p>
				</div>
				<div v-if="kuerzelIsTooLong" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Dieses Kürzel verwendet zu viele Zeichen. </p>
				</div>
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

	import type { MerkmaleNeuProps } from "~/components/schule/schulbezogen/merkmale/SMerkmaleNeuProps";
	import { BenutzerKompetenz, Merkmal } from "@core";
	import { ref, computed, watch } from "vue";
	import { isUniqueInList, mandatoryInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<MerkmaleNeuProps>();
	const data = ref<Merkmal>(new Merkmal());
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);
	const bezeichnungIsTooLong = computed(() => {
		if (data.value.bezeichnung === null)
			return false;
		return data.value.bezeichnung.length > 100;
	});
	const kuerzelIsTooLong = computed(() => {
		if (data.value.kuerzel === null)
			return false;
		return data.value.kuerzel.length > 10
	});

	function fieldIsValid(field: keyof Merkmal | null) : (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'bezeichnung':
					return bezeichnungIsValid(data.value.bezeichnung);
				case 'kuerzel':
					return kuerzelIsValid(data.value.kuerzel);
				default:
					return true;
			}
		}
	}

	function bezeichnungIsValid(value: string | null) {
		if (!mandatoryInputIsValid(value, 100))
			return false;

		return isUniqueInList(value, props.manager().liste.list(), 'bezeichnung');
	}

	function kuerzelIsValid(value: string | null) {
		if (!mandatoryInputIsValid(value, 10))
			return false;

		return isUniqueInList(value, props.manager().liste.list(), 'kuerzel');
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

	async function cancel() {
		props.checkpoint.active = false;
		await props.goToDefaultView(null);
	}

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
