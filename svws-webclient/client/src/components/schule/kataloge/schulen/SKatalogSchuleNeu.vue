<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Optional eine Schule aus NRW vorauswählen:" />
			<svws-ui-select class="pb-4" title="Schulen aus NRW" removable :item-text="schulenKatalogEintragText" autocomplete :items="schulenKatalogEintraege"
				:model-value="selectedSchule" :item-filter="filterSchulenKatalogEintraege" @update:model-value="(v) => updateData(v)" :disabled="isLoading" />
			<svws-ui-content-card title="Schulangaben" />
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-checkbox v-model="data.istSichtbar" :disabled="schuleAlreadyCreated">Ist Sichtbar</svws-ui-checkbox>
				<svws-ui-input-number placeholder="Sortierung" v-model="data.sortierung" :disabled="schuleAlreadyCreated" />
				<svws-ui-text-input placeholder="Kürzel" :max-len="10" :valid="fieldIsValid('kuerzel')" v-model="data.kuerzel"
					:disabled="schuleAlreadyCreated" />
				<svws-ui-text-input placeholder="Kurzbezeichnung" required :max-len="40" :valid="fieldIsValid('kurzbezeichnung')"
					v-model="data.kurzbezeichnung" :disabled="schuleAlreadyCreated" />
				<svws-ui-text-input placeholder="Schulnummer" required :min-len="6" :max-len="6" :valid="fieldIsValid('schulnummer')" v-model="data.schulnummer"
					:disabled="schuleAlreadyCreated" />
				<svws-ui-select title="Schulform" :items="Schulform.values()" :item-text="i => i.daten(schuljahr)?.text?? '_'" removable
					v-model="selectedSchulform" :disabled="schuleAlreadyCreated" />
				<svws-ui-text-input placeholder="Schulname" required :max-len="120" :valid="fieldIsValid('name')" v-model="data.name"
					:disabled="schuleAlreadyCreated" />
				<svws-ui-text-input placeholder="Schulleitung" :max-len="40" :valid="fieldIsValid('schulleiter')" v-model="data.schulleiter"
					:disabled="schuleAlreadyCreated" />
				<svws-ui-text-input placeholder="Straße" :max-len="55" :valid="fieldIsValid('strassenname')" v-model="adresse"
					:disabled="schuleAlreadyCreated" />
				<svws-ui-text-input placeholder="PLZ" :max-len="10" :valid="fieldIsValid('plz')" v-model="data.plz" :disabled="schuleAlreadyCreated" />
				<svws-ui-text-input placeholder="Ort" :max-len="50" :valid="fieldIsValid('ort')" v-model="data.ort" :disabled="schuleAlreadyCreated" />
				<svws-ui-text-input placeholder="Telefon" :max-len="20" :valid="fieldIsValid('telefon')" v-model="data.telefon" type="tel"
					:disabled="schuleAlreadyCreated" />
				<svws-ui-text-input placeholder="Fax" :max-len="20" :valid="fieldIsValid('fax')" type="tel" v-model="data.fax"
					:disabled="schuleAlreadyCreated" />
				<svws-ui-text-input placeholder="E-Mail-Adresse" :max-len="40" :valid="fieldIsValid('email')" type="email" v-model="data.email"
					:disabled="schuleAlreadyCreated" />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addSchule" :disabled="!formIsValid || schuleAlreadyCreated">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import {JavaObject, JavaString, SchulEintrag, Schulform, AdressenUtils, LehrerStammdaten} from "@core";
	import type { SchulenKatalogEintrag, List } from "@core"
	import type { KatalogSchuleNeuProps } from "./SKatalogSchuleNeuProps";
	import { filterSchulenKatalogEintraege } from "~/utils/helfer";

	const props = defineProps<KatalogSchuleNeuProps>();
	const data = ref<SchulEintrag>(
		Object.assign(new SchulEintrag(), {istSichtbar: true})
	);
	const schuljahr = computed<number>(() => props.schuleListeManager().getSchuljahr());
	const selectedSchulform = computed({
		get: () => data.value.idSchulform !== null ? Schulform.data().getWertByID(data.value.idSchulform) : null,
		set: (val: Schulform | null) => {
			if (val === null)
				return;
			const eintrag = Schulform.data().getEintragBySchuljahrUndWert(schuljahr.value, val);
			if (eintrag !== null)
				data.value.idSchulform = eintrag.id;
		},
	})
	const adresse = computed({
		get: () => AdressenUtils.combineStrasse(data.value.strassenname, data.value.hausnummer, data.value.zusatzHausnummer),
		set: (adresse : string | null) => {
			const vals = AdressenUtils.splitStrasse(adresse);
			data.value.strassenname = vals[0];
			data.value.hausnummer = vals[1];
			data.value.zusatzHausnummer = vals[2];
		},
	})
	const schulenKatalogEintraege= computed<List<SchulenKatalogEintrag>>(() => props.schuleListeManager().getSchulenKatalogEintraege());
	const selectedSchule = ref<SchulenKatalogEintrag>();

	function updateData(schule : SchulenKatalogEintrag | undefined | null) {
		// Felder clearen
		if (schule === undefined || schule === null) {
			data.value = new SchulEintrag();
			selectedSchule.value = undefined;
			return;
		}
		selectedSchule.value = schule;
		// Felder füllen
		data.value.kurzbezeichnung = schule.KurzBez?? "";
		data.value.schulnummer = schule.SchulNr;
		data.value.name = (schule.ABez1?? "") + (schule.ABez2?? "") + (schule.ABez3?? "");
		selectedSchulform.value = Schulform.data().getWertBySchluessel(schule.SF?? "");
		adresse.value = schule.Strasse;
		data.value.plz = schule.PLZ;
		data.value.ort = schule.Ort;
		data.value.telefon = schule.Telefon;
		data.value.fax = schule.Fax;
		data.value.email = schule.Email;
	}

	// Bezeichnungen
	function schulenKatalogEintragText(i: SchulenKatalogEintrag) {
		return (i.KurzBez !== null) ? `${i.SchulNr}: ${i.KurzBez}` : `${i.SchulNr}: Schule ohne Name`;
	}

	//validation logic
	function fieldIsValid(field: keyof SchulEintrag | null):(v:string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'kuerzel':
					return kuerzelIsValid(data.value.kuerzel);
				case 'kurzbezeichnung':
					return mandatoryInputIsValid(data.value.kurzbezeichnung, 40);
				case 'schulnummer':
					return schulnummerIsValid(data.value.schulnummer);
				case 'name':
					return mandatoryInputIsValid(data.value.name, 120);
				case 'schulleiter':
					return optionalInputIsValid(data.value.schulleiter, 40);
				case 'strassenname':
					return adresseIsValid();
				case 'plz':
					return optionalNumberIsValid(data.value.plz, 10);
				case 'ort':
					return optionalInputIsValid(data.value.ort, 50);
				case 'telefon':
					return phoneNumberIsValid(data.value.telefon, 20);
				case "fax":
					return phoneNumberIsValid(data.value.fax, 20);
				case "email" :
					return emailIsValid(data.value.email);
				default:
					return true;
			}
		}
	}

	const formIsValid = computed(() => {
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof SchulEintrag);
			const fieldValue = data.value[field as keyof SchulEintrag] as string | null;
			return validateField(fieldValue);
		})
	})

	function adresseIsValid() {
		return optionalInputIsValid(data.value.strassenname, 55) &&
			optionalInputIsValid(data.value.hausnummer, 10) &&
			optionalInputIsValid(data.value.zusatzHausnummer, 30);
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

	function optionalNumberIsValid(input: string | null, maxLength : number) {
		if (input === null || JavaString.isBlank(input))
			return true;
		if (input.length > maxLength)
			return false;
		return /^\d+$/.test(input)
	}

	function mandatoryInputIsValid(name: string | null, maxLength : number) {
		return !((name === null) || JavaString.isBlank(name) || (name.length > maxLength));
	}

	function schulnummerIsValid(schulnummer : string | null) {
		if ((schulnummer === null) || (JavaString.isBlank(schulnummer)) || (!/^\d{6}$/.test(schulnummer)))
			return false;
		for (const schuleintrag of props.schuleListeManager().liste.list()) {
			if (JavaObject.equalsTranspiler(schuleintrag.schulnummer, schulnummer))
				return false;
		}
		return true;
	}

	const schuleAlreadyCreated = computed(() => {
		if (selectedSchule.value === undefined)
			return false;
		for (const schuleintrag of props.schuleListeManager().liste.list()) {
			if (JavaObject.equalsTranspiler(schuleintrag.schulnummer, selectedSchule.value.SchulNr))
				return true;
		}
		return false;
	})

	function optionalInputIsValid(input : string | null, maxLength : number) {
		if ((input === null) || (JavaString.isBlank(input)))
			return true;
		return input.length <= maxLength;
	}

	function kuerzelIsValid(kuerzel : string | null) {
		if ((kuerzel === null) || (JavaString.isBlank(kuerzel)))
			return true;
		if (kuerzel.length > 10)
			return false;
		for (const schuleintrag of props.schuleListeManager().liste.list()) {
			if (JavaObject.equalsTranspiler(schuleintrag.kuerzel, kuerzel))
				return false;
		}
		return true;
	}

	//api call
	async function addSchule() {
		if (isLoading.value)
			return;

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	//other
	function cancel() {
		props.checkpoint.active = false;
		void props.gotoDefaultView(null);
	}

	const isLoading = ref<boolean>(false);

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;
		props.checkpoint.active = true;
	}, {immediate: false, deep: true});

</script>
