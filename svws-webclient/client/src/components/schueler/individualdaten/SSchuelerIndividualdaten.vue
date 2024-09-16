<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input v-autofocus placeholder="Nachname" :readonly="!hatKompetenzUpdate" :model-value="data.nachname"
					@change="nachname => patch({ nachname })" type="text" />
				<svws-ui-text-input placeholder="Rufname" :readonly="!hatKompetenzUpdate" :model-value="data.vorname"
					@change="vorname => patch({ vorname })" type="text" />
				<svws-ui-text-input placeholder="Alle Vornamen" :readonly="!hatKompetenzUpdate" :model-value="data.alleVornamen"
					@change="alleVornamen => patch({ alleVornamen })" type="text" />
				<svws-ui-spacing />
				<svws-ui-select title="Geschlecht" :disabled="!hatKompetenzUpdate" v-model="geschlecht" :items="Geschlecht.values()"
					statistics :item-text="(i: Geschlecht)=>i.text" />
				<svws-ui-text-input placeholder="Geburtsdatum" :readonly="!hatKompetenzUpdate" :model-value="data.geburtsdatum"
					@change="geburtsdatum => geburtsdatum && patch({geburtsdatum})" type="date" :valid="istGeburtsdatumGueltig" required statistics />
				<svws-ui-text-input placeholder="Geburtsort" :readonly="!hatKompetenzUpdate" :model-value="data.geburtsort"
					@change="geburtsort => patch({ geburtsort })" type="text" />
				<svws-ui-text-input placeholder="Geburtsname" :readonly="!hatKompetenzUpdate" :model-value="data.geburtsname"
					@change="geburtsname => patch({ geburtsname })" type="text" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Wohnort und Kontaktdaten" v-if="hatKompetenzAnsehen">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Straße" :readonly="!hatKompetenzUpdate" :model-value="strasse" @change="patchStrasse" type="text" span="full" />
				<svws-ui-select title="Wohnort" :disabled="!hatKompetenzUpdate" v-model="wohnortID" :items="mapOrte" :item-filter="orte_filter"
					:item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete statistics />
				<svws-ui-select title="Ortsteil" :disabled="!hatKompetenzUpdate" v-model="ortsteilID" :items="ortsteile"
					:item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''" :item-sort="ortsteilSort" :item-filter="ortsteilFilter" removable />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon" :readonly="!hatKompetenzUpdate" :model-value="data.telefon" @change="telefon => patch({ telefon })" type="tel" />
				<svws-ui-text-input placeholder="Mobil oder Fax" :readonly="!hatKompetenzUpdate" :model-value="data.telefonMobil"
					@change="telefonMobil => patch({ telefonMobil })" type="tel" />
				<svws-ui-text-input placeholder="Private E-Mail-Adresse" :readonly="!hatKompetenzUpdate" :model-value="data.emailPrivat"
					@change="emailPrivat => patch({ emailPrivat })" type="email" verify-email />
				<svws-ui-text-input placeholder="Schulische E-Mail-Adresse" :readonly="!hatKompetenzUpdate" :model-value="data.emailSchule"
					@change="emailSchule => patch({ emailSchule })" type="email" verify-email />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Staatsangehörigkeit und Konfession" v-if="hatKompetenzAnsehen">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="1. Staatsangehörigkeit" :disabled="!hatKompetenzUpdate" v-model="staatsangehoerigkeit" autocomplete
					:items="Nationalitaeten.values()" :item-text="i => i.daten.staatsangehoerigkeit"
					:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" required statistics />
				<svws-ui-select title="2. Staatsangehörigkeit" :disabled="!hatKompetenzUpdate" v-model="staatsangehoerigkeit2" autocomplete removable
					:items="Nationalitaeten.values()" :item-text="i => i.daten.staatsangehoerigkeit"
					:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" />
				<svws-ui-select title="Konfession" :disabled="!hatKompetenzUpdate" v-model="religion" :items="mapReligionen" :item-text="i=>i.text ?? ''" required statistics />
				<div class="flex items-center pl-2">
					<svws-ui-checkbox v-model="druckeKonfessionAufZeugnisse" :disabled="!hatKompetenzUpdate">Konfession aufs Zeugnis</svws-ui-checkbox>
				</div>
				<svws-ui-text-input placeholder="Abmeldung vom Religionsunterricht" :readonly="!hatKompetenzUpdate" :model-value="data.religionabmeldung"
					@change="religionabmeldung => patch({religionabmeldung})" type="date" />
				<svws-ui-text-input placeholder="Wiederanmeldung" :readonly="!hatKompetenzUpdate" :model-value="data.religionanmeldung"
					@change="religionanmeldung => patch({religionanmeldung})" type="date" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Migrationshintergrund" v-if="hatKompetenzAnsehen">
			<template #actions>
				<svws-ui-checkbox :disabled="!hatKompetenzUpdate" class="mt-3 xl:mt-0" :model-value="hatMigrationshintergrund"
					@update:model-value="hatMigrationshintergrund => patch({hatMigrationshintergrund})">
					Migrationshintergrund vorhanden
				</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number placeholder="Zuzugsjahr" :model-value="data.zuzugsjahr" @change="zuzugsjahr => patch({zuzugsjahr})"
					:disabled="!hatMigrationshintergrund || !hatKompetenzUpdate" statistics hide-stepper :min :max />
				<svws-ui-select title="Geburtsland" v-model="geburtsland" :items="Nationalitaeten.values()" :item-text="i => `${i.daten.bezeichnung} (${i.daten.iso3})`"
					:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter"
					:disabled="!hatMigrationshintergrund || !hatKompetenzUpdate" autocomplete statistics />
				<svws-ui-select title="Verkehrssprache" v-model="verkehrsprache" autocomplete :items="Verkehrssprache.values()"
					:item-text="i => `${i.daten.bezeichnung} (${i.daten.kuerzel})`" :item-sort="verkehrsspracheKatalogEintragSort"
					:item-filter="verkehrsspracheKatalogEintragFilter" :disabled="!hatMigrationshintergrund || !hatKompetenzUpdate" class="col-span-full" statistics />
				<svws-ui-select title="Geburtsland Mutter" v-model="geburtslandMutter" :items="Nationalitaeten.values()"
					:item-text="i => `${i.daten.bezeichnung} (${i.daten.iso3})`" :item-sort="nationalitaetenKatalogEintragSort"
					:item-filter="nationalitaetenKatalogEintragFilter" :disabled="!hatMigrationshintergrund || !hatKompetenzUpdate" autocomplete statistics />
				<svws-ui-select title="Geburtsland Vater" v-model="geburtslandVater" :items="Nationalitaeten.values()"
					:item-text="i => `${i.daten.bezeichnung} (${i.daten.iso3})`" :item-sort="nationalitaetenKatalogEintragSort"
					:item-filter="nationalitaetenKatalogEintragFilter" :disabled="!hatMigrationshintergrund || !hatKompetenzUpdate" autocomplete statistics />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Statusdaten" v-if="hatKompetenzAnsehen">
			<template #actions v-if="schulform === Schulform.BK || schulform === Schulform.SB">
				<svws-ui-checkbox :disabled="!hatKompetenzUpdate" :model-value="data.istDuplikat" @update:model-value="istDuplikat => patch({istDuplikat})">Ist Duplikat</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Status" :disabled="!hatKompetenzUpdate" :model-value="SchuelerStatus.data().getWertByKuerzel('' + data.status)"
					@update:model-value="status => (status?.daten(schuljahr)?.id !== undefined) && patch({ status: status?.daten(schuljahr)?.id })"
					:items="SchuelerStatus.values()" :item-text="i => i.daten(schuljahr)?.text ?? '—'" statistics />
				<svws-ui-select v-if="schuelerListeManager().daten().status === SchuelerStatus.EXTERN.daten(schuljahr)?.id" :disabled="!hatKompetenzUpdate"
					title="Stammschule" v-model="inputStammschule" :items="mapSchulen.values()" :item-text="i => i.kuerzel ?? i.schulnummer" removable />
				<div v-else />
				<svws-ui-select title="Fahrschüler" :disabled="!hatKompetenzUpdate" v-model="inputFahrschuelerArtID" :items="mapFahrschuelerarten"
					:item-text="i => i.text ?? ''" removable />
				<svws-ui-select title="Haltestelle" :disabled="!hatKompetenzUpdate" v-model="inputHaltestelleID" :items="mapHaltestellen"
					:item-text="i => i.text ?? ''" removable />
				<svws-ui-text-input placeholder="Anmeldedatum" :readonly="!hatKompetenzUpdate" :model-value="data.anmeldedatum"
					@change="d => patch({ anmeldedatum : d ?? null })" type="date" removable />
				<svws-ui-text-input placeholder="Aufnahmedatum" :readonly="!hatKompetenzUpdate" :model-value="data.aufnahmedatum"
					@change="aufnahmedatum => patch({aufnahmedatum})" type="date" statistics />
				<svws-ui-spacing />
				<svws-ui-input-wrapper :grid="2" class="input-wrapper--checkboxes">
					<svws-ui-checkbox :disabled="!hatKompetenzUpdate" :model-value="data.istVolljaehrig === true"
						@update:model-value="istVolljaehrig => patch({ istVolljaehrig })"> Volljährig </svws-ui-checkbox>
					<svws-ui-checkbox :disabled="!hatKompetenzUpdate" :model-value="data.keineAuskunftAnDritte"
						@update:model-value="keineAuskunftAnDritte => patch({ keineAuskunftAnDritte })"> Keine Auskunft an Dritte </svws-ui-checkbox>
					<svws-ui-checkbox :disabled="!hatKompetenzUpdate" :model-value="data.istSchulpflichtErfuellt === true" readonly>
						Schulpflicht erfüllt
					</svws-ui-checkbox>
					<svws-ui-checkbox :disabled="!hatKompetenzUpdate" :model-value="data.istBerufsschulpflichtErfuellt === true"
						@update:model-value="istBerufsschulpflichtErfuellt => patch({ istBerufsschulpflichtErfuellt })">
						Schulpflicht SII erfüllt
					</svws-ui-checkbox>
					<svws-ui-checkbox :disabled="!hatKompetenzUpdate" :model-value="data.hatMasernimpfnachweis"
						@update:model-value="hatMasernimpfnachweis => patch({ hatMasernimpfnachweis })">
						Masern Impfnachweis
					</svws-ui-checkbox>
					<svws-ui-checkbox :disabled="!hatKompetenzUpdate" :model-value="data.erhaeltSchuelerBAFOEG"
						@update:model-value="erhaeltSchuelerBAFOEG => patch({ erhaeltSchuelerBAFOEG })">
						BAFöG
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, type WritableComputedRef } from "vue";
	import type { InputDataType } from "@ui";
	import type { SchuelerIndividualdatenProps } from "./SSchuelerIndividualdatenProps";
	import type { SchuelerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag, ReligionEintrag, KatalogEintrag, SchulEintrag} from "@core";
	import { SchuelerStatus, Schulform, Nationalitaeten, Geschlecht, AdressenUtils, Verkehrssprache, BenutzerKompetenz, DateUtils } from "@core";
	import { verkehrsspracheKatalogEintragFilter, verkehrsspracheKatalogEintragSort, nationalitaetenKatalogEintragFilter, nationalitaetenKatalogEintragSort,
		staatsangehoerigkeitKatalogEintragSort, staatsangehoerigkeitKatalogEintragFilter, orte_sort, orte_filter, ortsteilSort, ortsteilFilter } from "~/utils/helfer";

	const props = defineProps<SchuelerIndividualdatenProps>();

	const schuljahr = computed<number>(() => props.schuelerListeManager().schuelerGetSchuljahrOrException());

	const hatKompetenzAnsehen = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN));
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

	const data = computed<SchuelerStammdaten>(() => props.schuelerListeManager().daten());

	const istGeburtsdatumGueltig = (strDate: InputDataType) => {
		if (strDate === null || typeof strDate === 'number')
			return true;
		try {
			const date = DateUtils.extractFromDateISO8601(strDate);
			const curDate = new Date();
			const diffYear = curDate.getFullYear() - date[0];
			return (diffYear > 3) && (diffYear < 51);
		} catch (e) {
			return false;
		}
	};

	const geschlecht: WritableComputedRef<Geschlecht> = computed({
		get: () => Geschlecht.fromValue(data.value.geschlecht) ?? Geschlecht.X,
		set: (value) => void props.patch({ geschlecht: value.id })
	});

	const strasse = computed(() => AdressenUtils.combineStrasse(data.value.strassenname || "", data.value.hausnummer || "", data.value.hausnummerZusatz || ""))

	const patchStrasse = (value: string ) => {
		if (value) {
			const vals = AdressenUtils.splitStrasse(value);
			void props.patch({ strassenname: vals[0] || value, hausnummer: vals[1] || "", hausnummerZusatz: vals[2] || "" });
		}
	}

	const wohnortID = computed<OrtKatalogEintrag | undefined>({
		get: () => {
			const id = data.value.wohnortID;
			return id === null ? undefined : props.mapOrte.get(id)
		},
		set: (value) => void props.patch({ wohnortID: value === undefined ? null : value.id })
	});

	const ortsteile = computed<Array<OrtsteilKatalogEintrag>>(() => {
		const result : Array<OrtsteilKatalogEintrag> = [];
		for (const ortsteil of props.mapOrtsteile.values())
			if ((ortsteil.ort_id === null) || (ortsteil.ort_id === data.value.wohnortID))
				result.push(ortsteil);
		return result;
	});

	const ortsteilID = computed<OrtsteilKatalogEintrag | undefined>({
		get: () => {
			const id = data.value.ortsteilID;
			return id === null ? undefined : props.mapOrtsteile.get(id)
		},
		set: (value) => void props.patch({ ortsteilID: value === undefined ? null : value.id })
	});


	const staatsangehoerigkeit: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(data.value.staatsangehoerigkeitID) || Nationalitaeten.DEU,
		set: (value) => void props.patch({ staatsangehoerigkeitID: value.daten.iso3 })
	});

	const staatsangehoerigkeit2 = computed<Nationalitaeten | null>({
		get: () => Nationalitaeten.getByISO3(data.value.staatsangehoerigkeit2ID),
		set: (value) => void props.patch({ staatsangehoerigkeit2ID: value?.daten.iso3 ?? null })
	});

	const religion: WritableComputedRef<ReligionEintrag | undefined> = computed({
		get: () => {
			const id = data.value.religionID;
			return id === null ? undefined : props.mapReligionen.get(id)
		},
		set: (value) => void props.patch({ religionID: value === undefined ? null : value.id })
	});

	const druckeKonfessionAufZeugnisse: WritableComputedRef<boolean> = computed({
		get: () => data.value.druckeKonfessionAufZeugnisse,
		set: (value) => void props.patch({ druckeKonfessionAufZeugnisse: value })
	});


	const hatMigrationshintergrund = computed<boolean>(() => props.schuelerListeManager().daten().hatMigrationshintergrund);
	const max = new Date().getFullYear() + 1;
	const min = max - 100;

	const geburtsland: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(data.value.geburtsland) || Nationalitaeten.DEU,
		set: (value) => void props.patch({ geburtsland: value.daten.iso3 })
	});

	const geburtslandMutter: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(data.value.geburtslandMutter) || Nationalitaeten.DEU,
		set: (value) => void props.patch({ geburtslandMutter: value.daten.iso3 })
	});

	const geburtslandVater: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(data.value.geburtslandVater) || Nationalitaeten.DEU,
		set: (value) => void props.patch({ geburtslandVater: value.daten.iso3 })
	});

	const verkehrsprache: WritableComputedRef<Verkehrssprache> = computed({
		get: () => Verkehrssprache.getByKuerzelAuto(data.value.verkehrspracheFamilie) || Verkehrssprache.DEU,
		set: (value) => void props.patch({ verkehrspracheFamilie: value.daten.kuerzel })
	});


	const inputStammschule: WritableComputedRef<SchulEintrag | undefined> = computed({
		get: () => (data.value.externeSchulNr === null) ? undefined : (props.mapSchulen.get(data.value.externeSchulNr) || undefined),
		set: (value) => void props.patch({ externeSchulNr: value === undefined ? null : value.schulnummer })
	});

	const inputFahrschuelerArtID: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => {
			const id = data.value.fahrschuelerArtID;
			return id === null ? undefined : props.mapFahrschuelerarten.get(id)
		},
		set: (value) => void props.patch({ fahrschuelerArtID: value === undefined ? null : value.id })
	});

	const inputHaltestelleID: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => {
			const id = data.value.haltestelleID;
			return id === null ? undefined : props.mapHaltestellen.get(id)
		},
		set: (value) => void props.patch({ haltestelleID: value === undefined ? null : value.id })
	});

</script>
