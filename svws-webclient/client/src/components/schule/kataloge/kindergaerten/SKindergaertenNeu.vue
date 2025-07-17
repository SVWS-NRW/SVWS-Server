<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Bezeichnung" :min-len="1" :max-len="100" v-model="data.bezeichnung" required :disabled
					:valid="fieldIsValid('bezeichnung')" />
				<svws-ui-input-number placeholder="Sortierung" v-model="data.sortierung" :disabled />
				<svws-ui-text-input placeholder="Bemerkung" span="full" :max-len="50" v-model="data.bemerkung" :disabled :valid="fieldIsValid('bemerkung')" />
				<svws-ui-text-input placeholder="Telefon" :max-len="20" :valid="fieldIsValid('tel')" v-model="data.tel" type="tel" :disabled />
				<svws-ui-text-input placeholder="E-Mail-Adresse" :max-len="40" :valid="fieldIsValid('email')" type="email" v-model="data.email" :disabled />
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

	import { AdressenUtils, BenutzerKompetenz, JavaString, Kindergarten } from "@core";
	import {computed, ref, watch} from "vue";
	import type { KindergaertenNeuProps } from "~/components/schule/kataloge/kindergaerten/SKindergaertenNeuProps";

	const props = defineProps<KindergaertenNeuProps>();
	const data = ref<Kindergarten>(new Kindergarten());
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);
	const isLoading = ref<boolean>(false);

	function fieldIsValid(field: keyof Kindergarten | null): (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'bezeichnung':
					return stringIsValid(data.value.bezeichnung, true, 100);
				case 'strassenname':
					return adresseIsValid();
				case 'ort':
					return stringIsValid(data.value.ort, false, 30);
				case 'plz':
					return plzIsValid(data.value.plz, 10);
				case 'tel':
					return phoneNumberIsValid(data.value.tel, 20);
				case 'email':
					return emailIsValid(data.value.email);
				case 'bemerkung':
					return stringIsValid(data.value.bemerkung, false, 50);
				default:
					return true;
			}
		}
	}

	function emailIsValid(value: string | null) {
		if ((value === null) || JavaString.isBlank(value))
			return true;
		if (value.length > 40)
			return false;
		return /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(value) ||
			/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value);
	}

	function phoneNumberIsValid(input: string | null, maxLength: number) {
		if ((input === null) || (JavaString.isBlank(input)))
			return true;
		// folgende Formate sind erlaubt: 0151123456, 0151/123456, 0151-123456, +49/176-456456 -> Buchstaben sind nicht erlaubt
		return /^\+?\d+([-/]?\d+)*$/.test(input) && input.length <= maxLength;
	}

	function plzIsValid(input: string | null, maxLength : number) {
		if (input === null || JavaString.isBlank(input))
			return true;
		if (input.length > maxLength)
			return false;
		return /^\d+$/.test(input)
	}

	function stringIsValid(input: string | null, mandatory: boolean, maxLength: number) {
		if (mandatory)
			return (input !== null) && (!JavaString.isBlank(input)) && (input.length <= maxLength);
		return (input === null) || (input.length <= maxLength);
	}

	function adresseIsValid() {
		return stringIsValid(data.value.strassenname, false, 55) &&
			stringIsValid(data.value.hausNr, false, 10) &&
			stringIsValid(data.value.hausNrZusatz, false, 30);
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
	}, {immediate: false, deep: true});

</script>
