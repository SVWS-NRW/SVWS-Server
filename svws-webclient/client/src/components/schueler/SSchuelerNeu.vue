<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Anmeldedaten">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-select title="Status" :items="SchuelerStatus.values()" :item-text="i => i.name().toLowerCase()?? '—'"
					:model-value="SchuelerStatus.data().getWertByKuerzel('' + data.status)"
					@update:model-value="v => data.status = v?.ordinal() ?? -1" :disabled="true" />
				<svws-ui-text-input placeholder="Schuljahr" type="text" :disabled="true" />
				<svws-ui-text-input placeholder="Halbjahr" type="text" :disabled="true" />
				<svws-ui-text-input placeholder="Jahrgang" type="text" :disabled="true" />
				<svws-ui-text-input placeholder="Klasse" type="text" :disabled="true" />
				<svws-ui-spacing />
				<!--TODO Map für Einschullungsart erstellen-->
				<svws-ui-text-input placeholder="Einschulungsart" type="text" :disabled="true" />
				<!--TODO Anmeldedatum darf nicht in der Zukunft liegen-->
				<svws-ui-text-input placeholder="Anmeldedatum" type="date" v-model="data.anmeldedatum" />
				<!--TODO Aufnahmedatum darf nicht vor dem Anmeldedatum liegen-->
				<svws-ui-text-input placeholder="Aufnahmedatum" type="date" v-model="data.aufnahmedatum" />
				<!--TODO Customize Toolbar… darf nicht vor dem Aufnahmedatum liegen-->
				<svws-ui-text-input placeholder="Beginn Bildungsgang" type="date" v-model="data.beginnBildungsgang" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Persönliche Daten">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-text-input placeholder="Name" required v-model="data.nachname" :valid="fieldIsValid('nachname')" />
				<svws-ui-text-input placeholder="Vorname" required v-model="data.vorname" :valid="fieldIsValid('vorname')" />
				<svws-ui-text-input placeholder="Weitere Vornamen" v-model="data.alleVornamen" :valid="fieldIsValid('alleVornamen')" />
				<svws-ui-select title="Geschlecht" required :items="Geschlecht.values()" :item-text="i => i.text"
					:model-value="Geschlecht.fromValue(data.geschlecht)" @update:model-value="v => data.geschlecht = v?.id ?? -1" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Straße" type="text" v-model="adresse" :valid="fieldIsValid('strassenname')" />
				<svws-ui-select title="Wohnort" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort" :item-text="i => `${i.plz} ${i.ortsname}`"
					:model-value="mapOrte.get(data.wohnortID?? -1)" @update:model-value="v => data.wohnortID = v?.id ?? null"
					:valid="fieldIsValid('wohnortID')" removable />
				<svws-ui-spacing />
				<svws-ui-select title="Ortsteil" :items="ortsteile" :item-sort="ortsteilSort" :item-text="i => i.ortsteil ?? ''"
					:model-value="mapOrtsteile.get(data.ortsteilID?? -1)" @update:model-value="v => data.ortsteilID = v?.id ?? null"
					:disabled="data.wohnortID === null" :valid="fieldIsValid('ortsteilID')" removable />
				<svws-ui-text-input placeholder="Geburtsdatum" required type="date" v-model="data.geburtsdatum" :valid="fieldIsValid('geburtsdatum')" />
				<svws-ui-text-input placeholder="Geburtsort" v-model="data.geburtsort" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon" type="tel" v-model="data.telefon" :valid="fieldIsValid('telefon')" :max-len="20" />
				<svws-ui-text-input placeholder="Mobil/Fax" type="tel" v-model="data.telefonMobil" :valid="fieldIsValid('telefonMobil')" :max-len="20" />
				<svws-ui-text-input placeholder="E-Mail" type="email" v-model="data.emailPrivat" :valid="fieldIsValid('emailPrivat')" />
				<svws-ui-spacing />
				<svws-ui-select title="1. Staatsangehörigkeit" :items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit"
					:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter"
					:model-value="Nationalitaeten.getByISO3(data.staatsangehoerigkeitID)" :valid="fieldIsValid('staatsangehoerigkeitID')"
					@update:model-value="v => data.staatsangehoerigkeitID = v?.historie().getLast().iso3 ?? null" removable />
				<svws-ui-select title="2. Staatsangehörigkeit" :items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit"
					:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter"
					:model-value="Nationalitaeten.getByISO3(data.staatsangehoerigkeit2ID)" :valid="fieldIsValid('staatsangehoerigkeit2ID')"
					@update:model-value="v => data.staatsangehoerigkeit2ID = v?.historie().getLast().iso3 ?? null" removable />
				<svws-ui-select title="Konfession" type="text" :items="mapReligionen" :item-text="i => i.bezeichnungZeugnis ?? ''"
					:model-value="mapReligionen.get(data.religionID?? -1)" @update:model-value="v => data.religionID = v?.id ?? -1" removable />
				<svws-ui-spacing />
				<svws-ui-select title="Geburtsland" :items="Nationalitaeten.values()" :item-text="i => `${i.historie().getLast().bezeichnung} (${i.historie().getLast().iso3})`"
					:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter" :model-value="Nationalitaeten.getByISO3(data.geburtsland)"
					:valid="fieldIsValid('geburtsland')" @update:model-value="v => data.geburtsland = v?.historie().getLast().iso3 ?? null" removable />
				<svws-ui-select title="Geburtsland Mutter" :items="Nationalitaeten.values()" :item-text="i => `${i.historie().getLast().bezeichnung} (${i.historie().getLast().iso3})`"
					:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter" :model-value="Nationalitaeten.getByISO3(data.geburtslandMutter)"
					:valid="fieldIsValid('geburtslandMutter')" @update:model-value="v => data.geburtslandMutter = v?.historie().getLast().iso3 ?? null" removable />
				<svws-ui-select title="Geburtsland Vater" :items="Nationalitaeten.values()" :item-text="i => `${i.historie().getLast().bezeichnung} (${i.historie().getLast().iso3})`"
					:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter" :model-value="Nationalitaeten.getByISO3(data.geburtslandVater)"
					:valid="fieldIsValid('geburtslandVater')" @update:model-value="v => data.geburtslandVater = v?.historie().getLast().iso3 ?? null" removable />
				<svws-ui-spacing />
				<svws-ui-select title="Verkehrssprache" :items="Verkehrssprache.values()" :item-text="i => `${i.daten.bezeichnung} (${i.daten.kuerzel})`"
					:model-value="Verkehrssprache.getByKuerzelAuto(data.verkehrspracheFamilie) " :item-sort="verkehrsspracheKatalogEintragSort"
					:item-filter="verkehrsspracheKatalogEintragFilter" @update:model-value="v => data.verkehrspracheFamilie = v?.daten.kuerzel ?? null" removable />
				<svws-ui-input-number placeholder="Zuzugsjahr" v-model="data.zuzugsjahr" />
				<svws-ui-text-input placeholder="Migrationshintergrund" :disabled="true" />
				<svws-ui-select title="Fahrschüler" :items="mapFahrschuelerarten" :item-text="i => i.text ?? ''" :model-value="mapFahrschuelerarten.get(data.fahrschuelerArtID?? -1)"
					@update:model-value="v => data.fahrschuelerArtID = v?.id ?? -1" removable />
				<svws-ui-select title="Haltestelle" :items="mapHaltestellen" :item-text="i => i.text ?? ''" :model-value="mapHaltestellen.get(data.haltestelleID?? -1)"
					@update:model-value="v => data.haltestelleID = v?.id ?? -1" removable />
				<svws-ui-text-input placeholder="Abmeldung vom Religionsunterricht" v-model="data.religionabmeldung" type="date" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Ext. ID-Nr." type="text" v-model="data.externeSchulNr" />
				<!--TODO Ausweisnummer, Schwerbehindertenausweis, Bemerkumng zu SchuelerStammdaten hinzufügen-->
				<svws-ui-text-input placeholder="NR. Schülerausweis" :disabled="true" />
				<svws-ui-text-input placeholder="Schwerbehindertenausweis" type="text" :disabled="true" />
				<svws-ui-text-input placeholder="Zust. Foto" type="text" :disabled="true" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Bemerkung" type="text" :disabled="true" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Vorschulentwicklung">
			<svws-ui-input-wrapper :grid="2">
				<!--TODO Validierungslogik für Kindergartendauer implementieren-->
				<svws-ui-text-input placeholder="Dauer des Kindergartenbesuchs" type="text" v-model="data.dauerKindergartenbesuch" :disabled="true" />
				<!--TODO Map für die Kindergarten erstellen-->
				<svws-ui-text-input placeholder="Name des Kindergartens" type="text" :disabled="true" />
				<svws-ui-spacing />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addSchuelerStammdaten" :disabled="!formIsValid">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerNeuProps } from "~/components/schueler/SSchuelerNeuProps";
	import { computed, ref } from "vue";
	import { AdressenUtils, Geschlecht, JavaString, Nationalitaeten, type OrtsteilKatalogEintrag, SchuelerStammdaten, SchuelerStatus, Verkehrssprache } from "@core";
	import { nationalitaetenKatalogEintragFilter, nationalitaetenKatalogEintragSort, orte_filter, orte_sort, ortsteilSort, staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort, verkehrsspracheKatalogEintragFilter, verkehrsspracheKatalogEintragSort } from "~/utils/helfer";

	const props = defineProps<SchuelerNeuProps>();

	const data = ref(new SchuelerStammdaten());
	const isLoading = ref<boolean>(false);

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
	});

	//validation logic
	function fieldIsValid(field: keyof SchuelerStammdaten | null):(v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'nachname':
					return stringIsValid(data.value.nachname, true, 120);
				case 'vorname':
					return stringIsValid(data.value.vorname, true, 120);
				case 'alleVornamen':
					return stringIsValid(data.value.alleVornamen, false, 120);
				case 'geburtsdatum':
					return data.value.geburtsdatum !== null;
				case 'geschlecht':
					return Geschlecht.fromValue(data.value.geschlecht) !== null;
				case 'strassenname':
					return adresseIsValid();
				case 'wohnortID':
					return (data.value.wohnortID === null) || (props.mapOrte.get(data.value.wohnortID) !== undefined);
				case 'ortsteilID':
					return (data.value.ortsteilID === null) || (props.mapOrtsteile.get(data.value.ortsteilID) !== undefined);
				case 'telefon':
					return phoneNumberIsValid(data.value.telefon, false, 20);
				case 'telefonMobil':
					return phoneNumberIsValid(data.value.telefon, false, 20);
				case 'emailPrivat':
					return stringIsValid(data.value.emailPrivat, false, 20);
				case 'staatsangehoerigkeitID':
					return (data.value.staatsangehoerigkeitID === null) || (Nationalitaeten.getByISO3(data.value.staatsangehoerigkeitID) !== null);
				case 'staatsangehoerigkeit2ID':
					return (data.value.staatsangehoerigkeit2ID === null) || (Nationalitaeten.getByISO3(data.value.staatsangehoerigkeit2ID) !== null);
				case 'geburtsland':
					return (data.value.geburtsland === null) || (Nationalitaeten.getByISO3(data.value.geburtsland) !== null);
				case 'geburtslandMutter':
					return (data.value.geburtslandMutter === null) || (Nationalitaeten.getByISO3(data.value.geburtslandMutter) !== null);
				case 'geburtslandVater':
					return (data.value.geburtslandVater === null) || (Nationalitaeten.getByISO3(data.value.geburtslandVater) !== null);
				default:
					return true;
			}
		}
	}

	const formIsValid = computed(() => {
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof SchuelerStammdaten);
			const fieldValue = data.value[field as keyof SchuelerStammdaten] as string | null;
			return validateField(fieldValue);
		});
	});

	function adresseIsValid() {
		return stringIsValid(data.value.strassenname, false, 55) &&
			stringIsValid(data.value.hausnummer, false, 10) &&
			stringIsValid(data.value.hausnummerZusatz, false, 30);
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

	async function addSchuelerStammdaten() {
		if (isLoading.value)
			return;

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, ...partialData } = data.value;
		await props.addSchueler(partialData);
		isLoading.value = false;
	}

	function cancel() {
		props.checkpoint.active = false;
		void props.gotoDefaultView(null);
	}

</script>
