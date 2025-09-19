<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Bezeichnung" :max-len="30" :min-len="1" v-model="data.bezeichnung" :disabled
					:valid="fieldIsValid('bezeichnung')" required />
				<div v-if="!isUniqueInList(data.bezeichnung, props.manager().liste.list(), 'bezeichnung')" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung wird bereits verwendet. </p>
				</div>
				<div v-if="data.bezeichnung.length > 30" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung verwendet zu viele Zeichen. </p>
				</div>
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
					<svws-ui-button @click="addErzieherart" :disabled="!formIsValid || !hatKompetenzUpdate">Speichern  </svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { SErzieherartenNeuProps } from "~/components/schule/allgemein/erzieherarten/SErzieherartenNeuProps";
	import { computed, ref, watch } from "vue";
	import { BenutzerKompetenz, Erzieherart } from "@core";
	import { isUniqueInList, mandatoryInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<SErzieherartenNeuProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzUpdate.value);
	const data = ref(new Erzieherart());
	const isLoading = ref<boolean>(false);

	function fieldIsValid(field: keyof Erzieherart | null) : (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'bezeichnung':
					return bezeichnungIsValid(data.value.bezeichnung);
				default:
					return true;
			}
		}
	}

	function bezeichnungIsValid(value: string | null) {
		if (!mandatoryInputIsValid(value, 30))
			return false;

		return isUniqueInList(value, props.manager().liste.list(), 'bezeichnung');
	}

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof Erzieherart);
			const fieldValue = data.value[field as keyof Erzieherart] as string | null;
			return validateField(fieldValue);
		})
	})

	async function addErzieherart() {
		if (isLoading.value === true)
			return;

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, exportBez, anzahlErziehungsberechtigte, ...partialData } = data.value;
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
