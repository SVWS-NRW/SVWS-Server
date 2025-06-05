<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-wrapper>
					<svws-ui-checkbox v-model="data.istSichtbar" :disabled>Ist Sichtbar</svws-ui-checkbox>
					<svws-ui-checkbox v-model="data.istRelevantFuerStatistik" :disabled>Ist Relevant für Statistik</svws-ui-checkbox>
				</svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Kürzel" required :max-len="10" :valid="fieldIsValid('kuerzel')" v-model="data.kuerzel" statistics :disabled />
				<svws-ui-select title="Personal-Typ" required :items="PersonalTyp.values()" :item-text="i => i.bezeichnung" :disabled
					:model-value="PersonalTyp.fromKuerzel(data.kuerzel) ?? PersonalTyp.LEHRKRAFT" @update:model-value="v => data.personalTyp = v?.kuerzel ?? '' " />
				<svws-ui-text-input placeholder="Nachname" required :max-len="120" :valid="fieldIsValid('nachname')" :disabled
					v-model="data.nachname" statistics />
				<svws-ui-text-input placeholder="Rufname" required :max-len="80" v-model="data.vorname" :valid="fieldIsValid('vorname')" statistics :disabled />
				<svws-ui-spacing />
				<svws-ui-select title="Geschlecht" required :items="Geschlecht.values()" :item-text="i => i.text" statistics :disabled
					:model-value="Geschlecht.fromValue(data.geschlecht)" @update:model-value="v => data.geschlecht = v?.id ?? -1" />
				<svws-ui-text-input placeholder="Geburtsdatum" type="date" v-model="data.geburtsdatum" statistics :disabled />
				<svws-ui-select title="Staatsangehörigkeit" :items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit"
					:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" autocomplete statistics
					:model-value="Nationalitaeten.getByISO3(data.staatsangehoerigkeitID)" :valid="fieldIsValid('staatsangehoerigkeitID')"
					@update:model-value="v => data.staatsangehoerigkeitID = v?.historie().getLast().iso3 ?? null" :disabled />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Akademischer Grad" v-model="data.titel" :valid="fieldIsValid('titel')" :max-len="20" :disabled />
				<svws-ui-text-input placeholder="Amtsbezeichnung" v-model="data.amtsbezeichnung" :valid="fieldIsValid('amtsbezeichnung')" :max-len="15"
					:disabled />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addLehrerStammdaten" :disabled="!formIsValid || !hatKompetenzAdd">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card title="Wohnort und Kontaktdaten">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Straße" v-model="adresse" :valid="fieldIsValid('strassenname')" span="full" :max-len="55" :disabled />
				<svws-ui-select title="Wohnort" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort" :item-text="i => `${i.plz} ${i.ortsname}`"
					:model-value="mapOrte.get(data.wohnortID?? -1)" @update:model-value="v => data.wohnortID = v?.id ?? null" autocomplete removable
					:valid="fieldIsValid('wohnortID')" :disabled />
				<svws-ui-select title="Ortsteil" :items="ortsteile" :item-sort="ortsteilSort" :item-text="i => i.ortsteil ?? ''"
					:model-value="mapOrtsteile.get(data.ortsteilID?? -1)" @update:model-value="v => data.ortsteilID = v?.id ?? null" removable
					:disabled="(data.wohnortID === null) || !hatKompetenzAdd" :valid="fieldIsValid('ortsteilID')" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon" type="tel" v-model="data.telefon" :valid="fieldIsValid('telefon')" :max-len="20" :disabled />
				<svws-ui-text-input placeholder="Mobil oder Fax" type="tel" v-model="data.telefonMobil" :valid="fieldIsValid('telefonMobil')" :max-len="20"
					:disabled />
				<svws-ui-text-input placeholder="Private E-Mail-Adresse" type="email" v-model="data.emailPrivat" :valid="fieldIsValid('emailPrivat')"
					:max-len="100" :disabled />
				<svws-ui-text-input placeholder="Schulische E-Mail-Adresse" type="email" v-model="data.emailDienstlich" :valid="fieldIsValid('emailDienstlich')"
					:max-len="100" :disabled />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";
	import type { LehrerNeuProps } from './SLehrerNeuProps';
	import { BenutzerKompetenz, type OrtsteilKatalogEintrag } from "@core";
	import { Geschlecht, JavaObject, JavaString, LehrerStammdaten, Nationalitaeten, PersonalTyp, AdressenUtils } from "@core";
	import { orte_filter, orte_sort, ortsteilSort, staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort } from "~/utils/helfer";

	const props = defineProps<LehrerNeuProps>();
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.LEHRERDATEN_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);
	const data = ref<LehrerStammdaten>(
		Object.assign(new LehrerStammdaten(), {personalTyp: "LEHRKRAFT"})
	);
	const ortsteile = computed<Array<OrtsteilKatalogEintrag>>(() => {
		const result : Array<OrtsteilKatalogEintrag> = [];
		for (const ortsteil of props.mapOrtsteile.values())
			if (ortsteil.ort_id === data.value.wohnortID)
				result.push(ortsteil);
		return result;
	});
	const adresse = computed({
		get: () => AdressenUtils.combineStrasse(data.value.strassenname, data.value.hausnummer, data.value.hausnummerZusatz),
		set: (adresse : string) => {
			const vals = AdressenUtils.splitStrasse(adresse);
			data.value.strassenname = vals[0];
			data.value.hausnummer = vals[1];
			data.value.hausnummerZusatz = vals[2];
		},
	})

	//validation logic
	function fieldIsValid(field: keyof LehrerStammdaten | null):(v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'kuerzel':
					return kuerzelIsValid(data.value.kuerzel);
				case 'personalTyp':
					return !JavaString.isBlank(data.value.personalTyp);
				case 'nachname':
					return stringIsValid(data.value.nachname, true, 120);
				case 'geschlecht':
					return Geschlecht.fromValue(data.value.geschlecht) !== null;
				case 'vorname':
					return stringIsValid(data.value.vorname, true, 80);
				case 'staatsangehoerigkeitID':
					return (data.value.staatsangehoerigkeitID === null) || (Nationalitaeten.getByISO3(data.value.staatsangehoerigkeitID) !== null);
				case 'titel':
					return stringIsValid(data.value.titel, false, 20);
				case 'amtsbezeichnung':
					return stringIsValid(data.value.amtsbezeichnung, false, 15);
				case 'strassenname':
					return adresseIsValid();
				case 'wohnortID':
					return (data.value.wohnortID === null) || (props.mapOrte.get(data.value.wohnortID) !== undefined);
				case 'ortsteilID':
					return (data.value.ortsteilID === null) || (props.mapOrtsteile.get(data.value.ortsteilID) !== undefined);
				case 'telefon':
					return phoneNumberIsValid(data.value.telefon, false, 20);
				case 'telefonMobil':
					return phoneNumberIsValid(data.value.telefonMobil, false, 20);
				case 'emailPrivat':
					return stringIsValid(data.value.emailPrivat, false, 100);
				case 'emailDienstlich':
					return stringIsValid(data.value.emailDienstlich, false, 100);
				default:
					return true;
			}
		}
	}

	const formIsValid = computed(() => {
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof LehrerStammdaten);
			const fieldValue = data.value[field as keyof LehrerStammdaten] as string | null;
			return validateField(fieldValue);
		});
	});

	function adresseIsValid() {
		return stringIsValid(data.value.strassenname, false, 55) &&
			stringIsValid(data.value.hausnummer, false, 10) &&
			stringIsValid(data.value.hausnummerZusatz, false, 30);
	}

	function kuerzelIsValid(kuerzel : string | null) {
		if ((kuerzel === null) || JavaString.isBlank(kuerzel) || (kuerzel.length > 10))
			return false;
		for (const lehrerStammdaten of props.lehrerListeManager().liste.list()) {
			if (JavaObject.equalsTranspiler(lehrerStammdaten.kuerzel, kuerzel))
				return false;
		}
		return true;
	}

	function phoneNumberIsValid(input: string | null, mandatory: boolean, maxLength: number) {
		if ((input === null) || (JavaString.isBlank(input)))
			return !mandatory;
		// folgende Formate sind erlaubt: 0151123456, 0151/123456, 0151-123456, +49/176-456456 -> Buchstaben sind nicht erlaubt
		return /^\+?\d+([-/]?\d+)*$/.test(input) && input.length <= maxLength;
	}

	function stringIsValid(input: string | null, mandatory: boolean, maxLength: number) {
		if (mandatory)
			return (input !== null) && (!JavaString.isBlank(input)) && (input.length <= maxLength);
		return (input === null) || (input.length <= maxLength);
	}

	const isLoading = ref<boolean>(false);

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;

		props.checkpoint.active = true;
	}, {immediate: false, deep: true})


	//api call
	async function addLehrerStammdaten() {
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

</script>
