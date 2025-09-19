<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input span="full" placeholder="Bezeichnung" :min-len="1" :max-len="100" v-model="data.bezeichnung" required :disabled
					:valid="fieldIsValid('bezeichnung')" />
				<div v-if="data.bezeichnung.length > 100" class="flex my-auto col-span-2">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung verwendet zu viele Zeichen. </p>
				</div>
				<svws-ui-text-input placeholder="Bemerkung" span="full" :max-len="50" v-model="data.bemerkung" :disabled :valid="fieldIsValid('bemerkung')" />
				<div v-if="data.bezeichnung.length > 50" class="flex my-auto col-span-2">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bemerkung verwendet zu viele Zeichen. </p>
				</div>
				<svws-ui-text-input placeholder="Telefon" :max-len="20" :valid="fieldIsValid('tel')" v-model="data.tel" type="tel" :disabled />
				<svws-ui-text-input placeholder="E-Mail-Adresse" :max-len="40" :valid="fieldIsValid('email')" type="email" v-model="data.email" :disabled />
				<svws-ui-input-number placeholder="Sortierung" v-model="data.sortierung" :disabled />
				<svws-ui-checkbox v-model="data.istSichtbar" :disabled>
					Sichtbar
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<div />
		<svws-ui-content-card title="Adresse">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Straße" v-model="adresse" :valid="fieldIsValid('strassenname')" span="full" :max-len="55" :disabled />
				<svws-ui-text-input placeholder="PLZ" :max-len="10" :valid="fieldIsValid('plz')" v-model="data.plz" :disabled />
				<svws-ui-text-input placeholder="Ort" v-model="data.ort" :valid="fieldIsValid('ort')" :max-len="30" :disabled />
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

	import { AdressenUtils, BenutzerKompetenz, Kindergarten } from "@core";
	import { computed, ref, watch } from "vue";
	import type { KindergaertenNeuProps } from "~/components/schule/allgemein/kindergaerten/SKindergaertenNeuProps";
	import { emailIsValid, mandatoryInputIsValid, optionalInputIsValid, phoneNumberIsValid } from "~/util/validation/Validation";

	const props = defineProps<KindergaertenNeuProps>();
	const data = ref<Kindergarten>(new Kindergarten());
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);
	const isLoading = ref<boolean>(false);

	function fieldIsValid(field: keyof Kindergarten | null): (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'bezeichnung':
					return mandatoryInputIsValid(data.value.bezeichnung, 100);
				case 'strassenname':
					return adresseIsValid();
				case 'ort':
					return optionalInputIsValid(data.value.ort, 30);
				case 'plz':
					return optionalInputIsValid(data.value.plz, 10);
				case 'tel':
					return phoneNumberIsValid(data.value.tel, 20);
				case 'email':
					return emailIsValid(data.value.email, 40);
				case 'bemerkung':
					return optionalInputIsValid(data.value.bemerkung, 50);
				default:
					return true;
			}
		}
	}

	function adresseIsValid() {
		return optionalInputIsValid(data.value.strassenname, 55) &&
			optionalInputIsValid(data.value.hausNr, 10) &&
			optionalInputIsValid(data.value.hausNrZusatz, 30);
	}

	const adresse = computed({
		get: () => AdressenUtils.combineStrasse(data.value.strassenname, data.value.hausNr, data.value.hausNrZusatz),
		set: (adresse : string) => {
			const vals = AdressenUtils.splitStrasse(adresse);
			data.value.strassenname = vals[0];
			data.value.hausNr = vals[1];
			data.value.hausNrZusatz = vals[2];
		},
	})

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof Kindergarten);
			const fieldValue = data.value[field as keyof Kindergarten] as string | null;
			return validateField(fieldValue);
		})
	})

	async function add() {
		if (isLoading.value)
			return;

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, ...partialData } = data.value;
		await props.addKindergarten(partialData);
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
	}, { immediate: false, deep: true });

</script>
