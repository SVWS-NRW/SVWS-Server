<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Bezeichnung" :max-len="30" :min-len="1" v-model="data.bezeichnung" :disabled required
					:valid="fieldIsValid('bezeichnung')" />
				<div v-if="!isUniqueInList(data.bezeichnung, props.manager().liste.list(), 'bezeichnung')" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung wird bereits verwendet. </p>
				</div>
				<div v-if="bezeichnungIsTooLong" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung verwendet zu viele Zeichen. </p>
				</div>
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
					<svws-ui-button @click="addTelefonArt" :disabled="!formIsValid || !hatKompetenzUpdate">Speichern</svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { STelefonArtenNeuProps } from "~/components/schule/allgemein/telefonarten/STelefonArtenNeuProps";
	import { BenutzerKompetenz, TelefonArt } from "@core";
	import { computed, ref, watch } from "vue";
	import { isUniqueInList, mandatoryInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<STelefonArtenNeuProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzUpdate.value);
	const data = ref(new TelefonArt());
	const isLoading = ref<boolean>(false);
	const bezeichnungIsTooLong = computed(() => data.value.bezeichnung.length > 30);

	function fieldIsValid(field: keyof TelefonArt | null) : (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'bezeichnung':
					return bezeichnungIsValid(data.value.bezeichnung);
				default:
					return true;
			}
		}
	}

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof TelefonArt);
			const fieldValue = data.value[field as keyof TelefonArt] as string | null;
			return validateField(fieldValue);
		})
	})

	function bezeichnungIsValid(value: string | null) {
		if (!mandatoryInputIsValid(value, 30))
			return false;

		return isUniqueInList(value, props.manager().liste.list(), 'bezeichnung');
	}

	async function addTelefonArt() {
		if (isLoading.value)
			return;

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, anzahlTelefonnummern, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
