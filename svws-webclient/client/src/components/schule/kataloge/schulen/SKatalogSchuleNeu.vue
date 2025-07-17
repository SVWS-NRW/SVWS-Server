<template>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper :grid="1">
			<div class="pb-4">
				<svws-ui-radio-option v-model="isInternal" :value="true" label=" Schule aus NRW erstellen " :disabled />
				<svws-ui-radio-option class="pb-4" v-model="isInternal" :value="false" label=" Externe Schule erstellen " :disabled />
			</div>
			<svws-ui-tooltip v-if="!isInternal" color="primary" :show-arrow="false" :indicator="false">
				<template #content>
					Schulen außerhalb NRW und sonstige Herkünfte z.B. auch nicht staatl. anerkannte Schulen.
				</template>
				<svws-ui-select class="pb-4 w-full" title="Schulen außerhalb von NRW und Privatschulen" :items="Herkunftsschulnummern.all_values_by_name.values()"
					:model-value="externalSchulnummer" @update:model-value="v => data.schulnummerStatistik = v?.daten.schulnummer.toString() ?? ''"
					:item-text=" v => v.daten.bezeichnung" :disabled />
			</svws-ui-tooltip>
			<svws-ui-select v-if="isInternal" class="pb-4" title="Schulen innerhalb NRW" removable :items="schulenKatalogEintraege" autocomplete :disabled="isLoading || !hatKompetenzAdd"
				:model-value="selectedSchule" :item-filter="filterSchulenKatalogEintraege" @update:model-value="updateData" :item-text="schulenKatalogEintragText" />
			<div v-if="!schuleAlreadyCreated">
				<svws-ui-content-card title="Schulangaben" />
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-checkbox v-model="data.istSichtbar" :disabled="schuleAlreadyCreated || !hatKompetenzAdd">Ist Sichtbar</svws-ui-checkbox>
					<svws-ui-input-number placeholder="Sortierung" v-model="data.sortierung" :disabled="schuleAlreadyCreated || hatKompetenzAdd" />
					<svws-ui-select title="Schulform" :items="Schulform.values()" :item-text="i => i.daten(schuljahr)?.text?? '_'" removable
						v-model="selectedSchulform" />
					<svws-ui-text-input placeholder="Statistik-Schulnummer" required :valid="fieldIsValid('schulnummerStatistik')" readonly
						:model-value="data.schulnummerStatistik" :disabled />
					<svws-ui-text-input placeholder="Kürzel" :max-len="10" :valid="fieldIsValid('kuerzel')" v-model="data.kuerzel" :disabled />
					<svws-ui-text-input placeholder="Schulname" required :max-len="120" :valid="fieldIsValid('name')" v-model="data.name" :disabled />
					<svws-ui-text-input placeholder="Kurzbezeichnung" required :max-len="40" :valid="fieldIsValid('kurzbezeichnung')" :disabled
						v-model="data.kurzbezeichnung" />
					<svws-ui-text-input placeholder="Schulleitung" :max-len="40" :valid="fieldIsValid('schulleiter')" v-model="data.schulleiter" :disabled />
					<svws-ui-text-input placeholder="Straße" :max-len="55" :valid="fieldIsValid('strassenname')" v-model="adresse" :disabled />
					<svws-ui-text-input placeholder="PLZ" :max-len="10" :valid="fieldIsValid('plz')" v-model="data.plz" :disabled />
					<svws-ui-text-input placeholder="Ort" :max-len="50" :valid="fieldIsValid('ort')" v-model="data.ort" :disabled />
					<svws-ui-text-input placeholder="Telefon" :max-len="20" :valid="fieldIsValid('telefon')" v-model="data.telefon" type="tel" :disabled />
					<svws-ui-text-input placeholder="Fax" :max-len="20" :valid="fieldIsValid('fax')" type="tel" v-model="data.fax" :disabled />
					<svws-ui-text-input placeholder="E-Mail-Adresse" :max-len="40" :valid="fieldIsValid('email')" type="email" v-model="data.email" :disabled />
				</svws-ui-input-wrapper>
				<div class="mt-7 flex flex-row gap-4 justify end">
					<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
					<svws-ui-button @click="addSchule" :disabled="!formIsValid || !hatKompetenzAdd">Speichern</svws-ui-button>
				</div>
			</div>
			<div v-else-if="schuleAlreadyCreated">
				<p class="pb-4">Diese Schule wurde bereits angelegt:</p>
				<svws-ui-button @click="navigateToSelectedSchule"> Zur Schule </svws-ui-button>
			</div>
			<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
		</svws-ui-input-wrapper>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import { JavaObject, JavaString, SchulEintrag, Schulform, AdressenUtils, Herkunftsschulnummern, BenutzerKompetenz } from "@core";
	import type { SchulenKatalogEintrag, List } from "@core"
	import type { KatalogSchuleNeuProps } from "./SKatalogSchuleNeuProps";
	import { filterSchulenKatalogEintraege } from "~/utils/helfer";

	const props = defineProps<KatalogSchuleNeuProps>();
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);
	const isInternal = ref<boolean>(true);
	const data = ref<SchulEintrag>(Object.assign(new SchulEintrag(), {istSichtbar: true}));
	const schulenKatalogEintraege= computed<List<SchulenKatalogEintrag>>(() => props.schuleListeManager().getSchulenKatalogEintraege());
	const selectedSchule = ref<SchulenKatalogEintrag>();
	const externalSchulnummer = ref<Herkunftsschulnummern>();
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

	// befüllt das Formular mit den Werten der vorausgewählten Schule
	function updateData(schule : SchulenKatalogEintrag | undefined | null) {
		// Felder clearen
		if (schule === undefined || schule === null) {
			resetForm()
			return;
		}
		selectedSchule.value = schule;
		// Felder füllen
		data.value.kurzbezeichnung = schule.KurzBez?? "";
		data.value.schulnummerStatistik = schule.SchulNr;
		data.value.name = (schule.ABez1?? "") + (schule.ABez2?? "") + (schule.ABez3?? "");
		selectedSchulform.value = Schulform.data().getWertBySchluessel(schule.SF?? "");
		adresse.value = schule.Strasse;
		data.value.plz = schule.PLZ;
		data.value.ort = schule.Ort;
		data.value.telefon = schule.Telefon;
		data.value.fax = schule.Fax;
		data.value.email = schule.Email;
	}

	// ---Bezeichnungen---

	function schulenKatalogEintragText(i: SchulenKatalogEintrag) {
		return (i.KurzBez !== null) ? `${i.SchulNr}: ${i.KurzBez}` : `${i.SchulNr}: Schule ohne Name`;
	}

	// ---buttons---

	async function addSchule() {
		if (isLoading.value)
			return;

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	function cancel() {
		props.checkpoint.active = false;
		void props.gotoDefaultView(null);
	}

	// ---util---

	const schuleAlreadyCreated = computed(() => findSchuleByPredicate(
		(schuleintrag : SchulEintrag) => JavaObject.equalsTranspiler(schuleintrag.schulnummerStatistik, selectedSchule.value?.SchulNr)) !== null
	)

	function resetForm() {
		data.value = Object.assign(new SchulEintrag(), {istSichtbar: true})
		selectedSchule.value = undefined;
	}

	function navigateToSelectedSchule() {
		props.checkpoint.active = false;
		const schuleintrag = findSchuleByPredicate((schuleintrag : SchulEintrag) =>
			JavaObject.equalsTranspiler(schuleintrag.schulnummerStatistik, selectedSchule.value?.SchulNr));
		if (schuleintrag)
			void props.gotoDefaultView(schuleintrag.id);
	}

	function findSchuleByPredicate(predicate: (schuleintrag: any) => boolean) {
		for (const schuleintrag of props.schuleListeManager().liste.list()) {
			if (predicate(schuleintrag))
				return schuleintrag;
		}
		return null;
	}

	const isLoading = ref<boolean>(false);

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;
		props.checkpoint.active = true;
	}, {immediate: false, deep: true});

	watch(() => isInternal.value, () => {
		// intern / extern toggle setzt die Felder zurück
		resetForm();
	})

	// ---validation logic---

	function fieldIsValid(field: keyof SchulEintrag | null):(v:string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'kuerzel':
					return kuerzelIsValid(data.value.kuerzel);
				case 'kurzbezeichnung':
					return mandatoryInputIsValid(data.value.kurzbezeichnung, 40);
				case 'schulnummerStatistik':
					return mandatoryInputIsValid(data.value.schulnummerStatistik, 6);
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
		// alle Felder auf validity prüfen
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

	function mandatoryInputIsValid(name: string | null, maxLength : number | null) {
		return (
			name !== null &&
			!JavaString.isBlank(name) &&
			(maxLength === null || name.length <= maxLength)
		);
	}

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
		return findSchuleByPredicate(schuleintrag => JavaObject.equalsTranspiler(schuleintrag.kuerzel, kuerzel)) === null;
	}

</script>
