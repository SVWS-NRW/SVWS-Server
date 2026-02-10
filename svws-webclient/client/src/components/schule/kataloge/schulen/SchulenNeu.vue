<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-input-wrapper :grid="1">
				<div class="pb-4 flex flex-row gap-6 items-center">
					<svws-ui-radio-option label=" Schule aus NRW erstellen "
						v-model="isInternal"
						:value="true" :disabled="!hatKompetenzAdd" />
					<svws-ui-radio-option label=" Externe Schule erstellen "
						v-model="isInternal"
						:value="false" :disabled="!hatKompetenzAdd" />
				</div>
				<svws-ui-tooltip v-if="!isInternal" color="primary" :show-arrow="false" :indicator="false">
					<template #content>
						Schulen außerhalb NRW und sonstige Herkünfte z.B. auch nicht staatl. anerkannte Schulen.
					</template>
					<ui-select label="Schulen außerhalb von NRW und Privatschulen" class="pb-4 w-full"
						:manager="externeSchulenSelectManager"
						v-model="selectedExterneSchulen"
						:disabled="!hatKompetenzAdd" />
				</svws-ui-tooltip>
				<ui-select v-if="isInternal"
					label="Schulen innerhalb NRW" class="pb-4 w-full"
					:manager="schulenNRWSelectManager"
					v-model="selectedSchule"
					:disabled="isLoading || !hatKompetenzAdd" searchable />
				<div v-if="!schuleAlreadyCreated">
					<svws-ui-content-card title="Schulangaben" />
					<svws-ui-input-wrapper :grid="2">
						<ui-select label="Schulform"
							:manager="schulformenSelectManager"
							v-model="selectedSchulformen"
							:disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="Statistik-Schulnummer"
							:model-value="data.schulnummerStatistik"
							:valid="() => fieldIsValid('schulnummerStatistik')" :disabled="!hatKompetenzAdd" readonly required />
						<svws-ui-text-input placeholder="Kürzel"
							v-model="data.kuerzel"
							:valid="() => fieldIsValid('kuerzel')" :max-len="10" :disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="Schulname"
							v-model="data.name"
							:valid="() => fieldIsValid('name')" :min-len="1" :max-len="120" :disabled="!hatKompetenzAdd" required />
						<svws-ui-text-input placeholder="Kurzbezeichnung"
							v-model="data.kurzbezeichnung"
							:valid="() => fieldIsValid('kurzbezeichnung')" :min-len="1" :max-len="40" :disabled="!hatKompetenzAdd" required />
						<svws-ui-text-input placeholder="Schulleitung"
							v-model="data.schulleiter"
							:valid="() => fieldIsValid('schulleiter')" :max-len="40" :disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="Straße"
							v-model="strasse"
							:valid="() => fieldIsValid('strassenname')" :max-len="55" :disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="PLZ"
							v-model="data.plz"
							:valid="() => fieldIsValid('plz')" :max-len="10" :disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="Ort"
							v-model="data.ort"
							:valid="() => fieldIsValid('ort')" :max-len="50" :disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="Telefon" type="tel"
							v-model="data.telefon"
							:valid="() => fieldIsValid('telefon')" :max-len="20" :disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="Fax" type="tel"
							v-model="data.fax"
							:valid="() => fieldIsValid('fax')" :max-len="20" :disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
							v-model="data.email"
							:valid="() => fieldIsValid('email')" :max-len="40" :disabled="!hatKompetenzAdd" />
					</svws-ui-input-wrapper>
					<svws-ui-spacing :size="2" />
					<svws-ui-content-card title="Ansicht & Sortierung">
						<svws-ui-input-wrapper :grid="2">
							<svws-ui-input-number placeholder="Sortierung"
								v-model="data.sortierung"
								:valid="() => fieldIsValid('sortierung')" :min="0" :max="32000" :disabled="schuleAlreadyCreated || !hatKompetenzAdd" :removable="false" />
							<svws-ui-spacing />
							<svws-ui-checkbox v-model="data.istSichtbar" :disabled="schuleAlreadyCreated || !hatKompetenzAdd">
								Sichtbar
							</svws-ui-checkbox>
						</svws-ui-input-wrapper>
					</svws-ui-content-card>
				</div>
				<div v-else-if="schuleAlreadyCreated">
					<p class="pb-4">Diese Schule wurde bereits angelegt:</p>
					<svws-ui-button @click="navigateToSelectedSchule"> Zur Schule </svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
			<div v-if="!schuleAlreadyCreated" class="mt-7 flex flex-row gap-4 justify-end w-full">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addSchule" :disabled="!formIsValid || !hatKompetenzAdd">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import { JavaObject, SchulEintrag, Schulform, AdressenUtils, Herkunftsschulnummer, BenutzerKompetenz } from "@core";
	import type { SchulenKatalogEintrag, HerkunftsschulnummerKatalogEintrag, SchulformKatalogEintrag } from "@core";
	import type { SchulenNeuProps } from "./SchulenNeuProps";
	import { emailIsValid, isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid, optionalInputIsValid, phoneNumberIsValid } from "~/util/validation/Validation";
	import { CoreTypeSelectManager, SelectManager } from "@ui";

	const props = defineProps<SchulenNeuProps>();
	const data = ref<SchulEintrag>(Object.assign(new SchulEintrag(), { sortierung: 32000, istSichtbar: true }));
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isInternal = ref<boolean>(true);
	const selectedSchulenKatalogEintrag = ref<SchulenKatalogEintrag>();
	const schuljahr = computed<number>(() => props.manager().getSchuljahr());
	const selectedSchulform = computed({
		get: () => Schulform.data().getWertByID(data.value.idSchulform ?? -1),
		set: (value: Schulform | null) => {
			if (value === null) {
				return;
			}
			const eintrag = Schulform.data().getEintragBySchuljahrUndWert(schuljahr.value, value);
			if (eintrag !== null) {
				data.value.idSchulform = eintrag.id;
			}
		},
	});

	const externeSchulenSelectManager = new CoreTypeSelectManager({
		clazz: Herkunftsschulnummer.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const schulenNRWSelectManager = new SelectManager<SchulenKatalogEintrag>({
		options: computed(() => props.manager().schulenKatalogEintraege),
		optionDisplayText: (s) => schulenKatalogEintragText(s),
		selectionDisplayText: (s) => schulenKatalogEintragText(s),
	});

	const schulformenSelectManager = new CoreTypeSelectManager({
		clazz: Schulform.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const selectedExterneSchulen = computed<HerkunftsschulnummerKatalogEintrag | null>({
		get: () => Herkunftsschulnummer.data().getEintragBySchuljahrUndSchluessel(props.schuljahr, data.value.schulnummerStatistik ?? ""),
		set: (value: HerkunftsschulnummerKatalogEintrag | null) => data.value.schulnummerStatistik = value?.schluessel ?? null,
	});

	const selectedSchule = computed<SchulenKatalogEintrag | null>({
		get: () => selectedSchulenKatalogEintrag.value ?? null,
		set: (value) => {
			selectedSchulenKatalogEintrag.value = value ?? undefined;
			updateData(value);
		},
	});

	const selectedSchulformen = computed<SchulformKatalogEintrag | null>({
		get: () => Schulform.data().getEintragByID(data.value.idSchulform ?? -1),
		set: (value: SchulformKatalogEintrag | null) => data.value.idSchulform = value?.id ?? null,
	});

	const strasse = computed({
		get: () => AdressenUtils.combineStrasse(data.value.strassenname, data.value.hausnummer, data.value.zusatzHausnummer),
		set: (strasse: string | null) => {
			const vals = AdressenUtils.splitStrasse(strasse);
			data.value.strassenname = vals[0];
			data.value.hausnummer = vals[1];
			data.value.zusatzHausnummer = vals[2];
		},
	});

	// befüllt das Formular mit den Werten der vorausgewählten Schule
	function updateData(schule: SchulenKatalogEintrag | undefined | null) {
		// Felder clearen
		if (schule === undefined || schule === null) {
			resetForm();
			return;
		}
		selectedSchulenKatalogEintrag.value = schule;
		// Felder füllen
		data.value.kurzbezeichnung = schule.KurzBez;
		data.value.schulnummerStatistik = schule.SchulNr;
		data.value.name = (schule.ABez1 ?? "") + (schule.ABez2 ?? "") + (schule.ABez3 ?? "");
		selectedSchulform.value = Schulform.data().getWertBySchluessel(schule.SF ?? "");
		strasse.value = schule.Strasse;
		data.value.plz = schule.PLZ;
		data.value.ort = schule.Ort;
		data.value.telefon = schule.Telefon;
		data.value.fax = schule.Fax;
		data.value.email = schule.Email;
	}

	// ---Bezeichnungen---

	function schulenKatalogEintragText(i: SchulenKatalogEintrag) {
		if (i.KurzBez === null) {
			return `${i.SchulNr}: Schule ohne Name`;
		}
		return `${i.SchulNr}: ${i.KurzBez}`;
	}

	// ---buttons---

	async function addSchule() {
		if (isLoading.value) {
			return;
		}

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, referenziertInAnderenTabellen, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	// ---util---

	const schuleAlreadyCreated = computed(() => findSchuleByPredicate(
		(schuleintrag: SchulEintrag) => JavaObject.equalsTranspiler(schuleintrag.schulnummerStatistik, selectedSchule.value?.SchulNr)) !== null
	);

	function resetForm() {
		data.value = Object.assign(new SchulEintrag(), { istSichtbar: true });
		selectedSchulenKatalogEintrag.value = undefined;
	}

	async function navigateToSelectedSchule() {
		props.checkpoint.active = false;
		const schuleintrag = findSchuleByPredicate((schuleintrag: SchulEintrag) =>
			JavaObject.equalsTranspiler(schuleintrag.schulnummerStatistik, selectedSchule.value?.SchulNr));
		if (schuleintrag) {
			await props.gotoDefaultView(schuleintrag.id);
		}
	}

	function findSchuleByPredicate(predicate: (schuleintrag: any) => boolean) {
		for (const schuleintrag of props.manager().liste.list()) {
			if (predicate(schuleintrag)) {
				return schuleintrag;
			}
		}
		return null;
	}

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

	watch(() => isInternal.value, () => {
		// intern / extern toggle setzt die Felder zurück
		resetForm();
	});

	// ---validate---
	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof SchulEintrag));
	});

	const fieldIsValid = (field: keyof SchulEintrag | null): boolean => {
		switch (field) {
			case 'kuerzel':
				return kuerzelIsValid(data.value.kuerzel);
			case 'name':
				return schulnameIsValid(data.value.name);
			case 'kurzbezeichnung':
				return kurzbezeichnungIsValid(data.value.kurzbezeichnung);
			case 'schulleiter':
				return schulleiterIsValid(data.value.schulleiter);
			case 'schulnummerStatistik':
				return schulnummerStatistikIsValid(data.value.schulnummerStatistik);
			case 'strassenname':
				return strasseIsValid();
			case 'plz':
				return plzIsValid(data.value.plz);
			case 'ort':
				return ortIsValid(data.value.ort);
			case 'telefon':
				return phoneNumberIsValid(data.value.telefon, 20);
			case "fax":
				return phoneNumberIsValid(data.value.fax, 20);
			case "email":
				return emailIsValid(data.value.email, 40);
			case 'sortierung':
				return sortierungIsValid(data.value.sortierung);
			default:
				return true;
		}
	};

	function kuerzelIsValid(kuerzel: string | null): boolean {
		return optionalInputIsValid(kuerzel, 10)
			&& isUniqueInList(kuerzel, props.manager().liste.list(), "kuerzel", "id", data.value.id);
	}

	function schulnameIsValid(schulname: string | null): schulname is string {
		return schulname !== null
			&& mandatoryInputIsValid(schulname, 120)
			&& isUniqueInList(schulname, props.manager().liste.list(), "name", "id", data.value.id);
	}

	function kurzbezeichnungIsValid(kurzbezeichnung: string | null): kurzbezeichnung is string {
		return kurzbezeichnung !== null
			&& mandatoryInputIsValid(kurzbezeichnung, 40)
			&& isUniqueInList(kurzbezeichnung, props.manager().liste.list(), "kurzbezeichnung", "id", data.value.id);
	}

	function schulleiterIsValid(schulleiter: string | null): boolean {
		return optionalInputIsValid(schulleiter, 40);
	}

	function schulnummerStatistikIsValid(schulnummer: string | null): schulnummer is string {
		return (schulnummer !== null)
			&& mandatoryInputIsValid(schulnummer, 6);
	}

	function strasseIsValid() {
		return optionalInputIsValid(data.value.strassenname, 55) &&
			optionalInputIsValid(data.value.hausnummer, 10) &&
			optionalInputIsValid(data.value.zusatzHausnummer, 30);
	}

	function plzIsValid(plz: string | null): boolean {
		return optionalInputIsValid(plz, 10);
	}

	function ortIsValid(plz: string | null): boolean {
		return optionalInputIsValid(plz, 50);
	}

	function sortierungIsValid(sortierung: number | null): sortierung is number {
		return sortierung !== null
			&& !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

</script>
