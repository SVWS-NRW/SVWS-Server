<template>
	<div class="page page-grid-cards" v-if="data.id === 0">
		<svws-ui-content-card title="Persönliche Daten">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-text-input placeholder="Name" required v-model="data.nachname" :valid="fieldIsValid('nachname')" />
				<svws-ui-text-input placeholder="Vorname" required v-model="data.vorname" :valid="fieldIsValid('vorname')" />
				<svws-ui-text-input placeholder="Weitere Vornamen" v-model="data.alleVornamen" :valid="fieldIsValid('alleVornamen')" />
				<svws-ui-select title="Geschlecht" required :items="Geschlecht.values()" :item-text="i => i.text"
					:model-value="Geschlecht.fromValue(data.geschlecht)" @update:model-value="v => data.geschlecht = v?.id ?? -1" />
				<svws-ui-text-input placeholder="Geburtsdatum" required type="date" v-model="data.geburtsdatum" :valid="fieldIsValid('geburtsdatum')" />
				<svws-ui-select title="Status" :items="SchuelerStatus.values()" :item-text="i => i.name().toLowerCase()?? '—'"
					:model-value="SchuelerStatus.data().getWertByKuerzel('' + data.status)"
					@update:model-value="v => data.status = v?.ordinal() ?? -1" :disabled="true" />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addSchuelerStammdaten" :disabled="(!formIsValid)">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>
	<div class="page page-grid-cards" v-if="data.id !== 0">
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
				<svws-ui-select title="Einschulungsart" :items="mapEinschulungsarten" :item-text="i => i.text ?? ''" :model-value="mapEinschulungsarten.get(data.einschulungsartID ?? -1)"
					@update:model-value="v => data.einschulungsartID = v?.id ?? null" removable v-if="schulenMitPrimaerstufe" />
				<!--TODO Anmeldedatum darf nicht in der Zukunft liegen-->
				<svws-ui-text-input placeholder="Anmeldedatum" type="date" v-model="data.anmeldedatum" />
				<!--TODO Aufnahmedatum darf nicht vor dem Anmeldedatum liegen-->
				<svws-ui-text-input placeholder="Aufnahmedatum" type="date" v-model="data.aufnahmedatum" />
				<!--TODO Beginn Bildungsgang darf nicht vor dem Aufnahmedatum liegen-->
				<svws-ui-text-input placeholder="Beginn Bildungsgang" type="date" v-model="data.beginnBildungsgang" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Persönliche Daten">
			<svws-ui-input-wrapper :grid="4">
				<!--TODO Leere Inputfelder unterbinden-->
				<svws-ui-text-input placeholder="Name" required :model-value="data.nachname"
					@change="nachname => patch({ nachname: nachname ?? undefined }, data.id)" />
				<svws-ui-text-input placeholder="Vorname" required :model-value="data.vorname"
					@change="vorname => patch({ vorname: vorname ?? undefined }, data.id)" :valid="fieldIsValid('vorname')" />
				<svws-ui-text-input placeholder="Weitere Vornamen" :model-value="data.alleVornamen"
					@change="alleVornamen => patch({ alleVornamen: alleVornamen ?? undefined }, data.id)" :valid="fieldIsValid('alleVornamen')" />
				<svws-ui-select title="Geschlecht" required :items="Geschlecht.values()" :item-text="i => i.text" v-model="geschlecht" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Straße" type="text" :model-value="strasse" @change="patchStrasse" :valid="fieldIsValid('strassenname')" />
				<svws-ui-select title="Wohnort" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort" :item-text="i => `${i.plz} ${i.ortsname}`"
					v-model="wohnortID" :valid="fieldIsValid('wohnortID')" removable />
				<svws-ui-spacing />
				<svws-ui-select title="Ortsteil" :items="ortsteile" :item-sort="ortsteilSort" :item-text="i => i.ortsteil ?? ''"
					v-model="ortsteilID" :valid="fieldIsValid('ortsteilID')" removable />
				<svws-ui-text-input placeholder="Geburtsdatum" required type="date" :model-value="data.geburtsdatum"
					@change="geburtsdatum => geburtsdatum && patch({geburtsdatum}, data.id)" :valid="fieldIsValid('geburtsdatum')" />
				<svws-ui-text-input placeholder="Geburtsort" :model-value="data.geburtsort" @change="geburtsort => patch({ geburtsort }, data.id)" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon" type="tel" :model-value="data.telefon" @change="telefon => patch({ telefon }, data.id)"
					:valid="fieldIsValid('telefon')" :max-len="20" />
				<svws-ui-text-input placeholder="Mobil/Fax" type="tel" :model-value="data.telefonMobil"
					@change="telefonMobil => patch({ telefonMobil }, data.id)" :valid="fieldIsValid('telefonMobil')" :max-len="20" />
				<svws-ui-text-input placeholder="E-Mail" type="email" :model-value="data.emailPrivat"
					@change="emailPrivat => patch({ emailPrivat }, data.id)" :valid="fieldIsValid('emailPrivat')" />
				<svws-ui-spacing />
				<svws-ui-select title="1. Staatsangehörigkeit" :items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit"
					:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" v-model="staatsangehoerigkeit"
					:valid="fieldIsValid('staatsangehoerigkeitID')" removable />
				<svws-ui-select title="2. Staatsangehörigkeit" :items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit"
					:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" v-model="staatsangehoerigkeit2"
					:valid="fieldIsValid('staatsangehoerigkeit2ID')" removable />
				<svws-ui-select title="Konfession" type="text" :items="mapReligionen" :item-text="i => i.bezeichnungZeugnis ?? ''" v-model="religion" removable />
				<svws-ui-spacing />
				<svws-ui-checkbox :model-value="data.hatMigrationshintergrund" @update:model-value="hatMigrationshintergrund => {
					data.hatMigrationshintergrund = hatMigrationshintergrund; patch({ hatMigrationshintergrund }, data.id) }"
					type="checkbox" title="Migrationshintergrund">
					Migrationshintergrund vorhanden
				</svws-ui-checkbox>
				<svws-ui-input-number placeholder="Zuzugsjahr" :model-value="data.zuzugsjahr" @change="zuzugsjahr => patch({ zuzugsjahr }, data.id)"
					:disabled="!data.hatMigrationshintergrund" />
				<svws-ui-select title="Geburtsland" :items="Nationalitaeten.values()" :item-text="i => `${i.historie().getLast().bezeichnung} (${i.historie().getLast().iso3})`"
					:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter" v-model="geburtsland"
					:valid="fieldIsValid('geburtsland')" :disabled="!data.hatMigrationshintergrund" removable />
				<svws-ui-spacing />
				<svws-ui-select title="Geburtsland Mutter" :items="Nationalitaeten.values()" :item-text="i => `${i.historie().getLast().bezeichnung} (${i.historie().getLast().iso3})`"
					:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter" v-model="geburtslandMutter"
					:valid="fieldIsValid('geburtslandMutter')" :disabled="!data.hatMigrationshintergrund" removable />
				<svws-ui-select title="Geburtsland Vater" :items="Nationalitaeten.values()" :item-text="i => `${i.historie().getLast().bezeichnung} (${i.historie().getLast().iso3})`"
					:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter" v-model="geburtslandVater"
					:valid="fieldIsValid('geburtslandVater')" :disabled="!data.hatMigrationshintergrund" removable />
				<svws-ui-select title="Verkehrssprache" :items="Verkehrssprache.values()" :item-text="i => `${i.historie().getLast().text} (${i.historie().getLast().iso3})`"
					v-model="verkehrsprache" :item-sort="verkehrsspracheKatalogEintragSort" :item-filter="verkehrsspracheKatalogEintragFilter"
					:disabled="!data.hatMigrationshintergrund" removable />
				<svws-ui-spacing />
				<svws-ui-select title="Fahrschüler" :items="mapFahrschuelerarten" :item-text="i => i.text ?? ''" v-model="fahrschuelerart" removable />
				<svws-ui-select title="Haltestelle" :items="mapHaltestellen" :item-text="i => i.text ?? ''" v-model="haltestelle" removable />
				<svws-ui-text-input placeholder="Abmeldung vom Religionsunterricht" :model-value="data.religionabmeldung"
					@change="religionabmeldung => patch({ religionabmeldung }, data.id)" type="date" />
				<svws-ui-spacing />
				<svws-ui-select title="Ext. ID-Nr." v-model="externeSchulNr" :items="mapSchulen.values()"
					:item-text="i => i.kuerzel ?? i.schulnummerStatistik ?? i.kurzbezeichnung ?? i.name" removable />
				<!--TODO Ausweisnummer, Schwerbehindertenausweis, Bemerkumng zu SchuelerStammdaten hinzufügen-->
				<svws-ui-text-input placeholder="NR. Schülerausweis" :disabled="true" />
				<svws-ui-text-input placeholder="Schwerbehindertenausweis" type="text" :disabled="true" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Bemerkung" type="text" :disabled="true" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<!-- TODO Aus SchuelerSchulbesuchsdaten patchen-->
		<svws-ui-content-card title="Vorschulentwicklung" v-if="schulenMitPrimaerstufe">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Dauer des Kindergartenbesuchs" :items="Kindergartenbesuch.values()" :item-text="i => i.daten(schuljahr)?.schluessel ?? '-'"
					v-model="dauerKindergarten" removable />
				<svws-ui-select title="Name des Kindergartens" :items="mapKindergaerten" :item-text="i => i.bezeichnung" :model-value="mapKindergaerten.get(data.kindergartenID?? -1)"
					@update:model-value="v => data.kindergartenID = v?.id ?? null" removable />
				<svws-ui-spacing />
				<svws-ui-checkbox v-model="data.verpflichtungSprachfoerderkurs" type="checkbox" title="Verpflichtung f. Sprachförderkurs">
					Verpflichtung f. Sprachförderkurs
				</svws-ui-checkbox>
				<svws-ui-checkbox v-model="data.teilnahmeSprachfoerderkurs" type="checkbox" title="Teilnahme an Sprachförderkurs">
					Teilnahme an Sprachförderkurs
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addSchuelerStammdaten" :disabled="((!formIsValid) || (data.id !== 0))">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>
	<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
</template>

<script setup lang="ts">

	import type { SchuelerNeuProps } from "~/components/schueler/SSchuelerNeuProps";
	import {computed, ref, watch} from "vue";
	import { AdressenUtils, Geschlecht, JavaString, type KatalogEintrag, Kindergartenbesuch, Nationalitaeten, type OrtKatalogEintrag, type OrtsteilKatalogEintrag, type ReligionEintrag, SchuelerStammdaten, SchuelerStatus, type SchulEintrag, Schulform, Verkehrssprache } from "@core";
	import { nationalitaetenKatalogEintragFilter, nationalitaetenKatalogEintragSort, orte_filter, orte_sort, ortsteilSort, staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort, verkehrsspracheKatalogEintragFilter, verkehrsspracheKatalogEintragSort } from "~/utils/helfer";

	const props = defineProps<SchuelerNeuProps>();

	const schuljahr = computed<number>(() => props.aktAbschnitt.schuljahr);

	const data = ref(new SchuelerStammdaten());
	const isLoading = ref<boolean>(false);

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;
		props.checkpoint.active = true;
	}, {immediate: false, deep: true});

	//TODO Schulform.GY aus dem Array entfernen
	const schulenMitPrimaerstufe = computed(() => {
		const erlaubteSchulformen = [ Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V, Schulform.GY];
		return erlaubteSchulformen.includes(props.schulform);
	});

	const wohnortID = computed<OrtKatalogEintrag | undefined>({
		get: () => {
			const id = data.value.wohnortID;
			return id === null ? undefined : props.mapOrte.get(id)
		},
		set: (value) => void props.patch({ wohnortID: value === undefined ? null : value.id }, data.value.id),
	});

	const ortsteile = computed<Array<OrtsteilKatalogEintrag>>(() => {
		const result : Array<OrtsteilKatalogEintrag> = [];
		for (const ortsteil of props.mapOrtsteile.values())
			if (ortsteil.ort_id === data.value.wohnortID)
				result.push(ortsteil);
		return result;
	});

	const ortsteilID = computed<OrtsteilKatalogEintrag | undefined>({
		get: () => {
			const id = data.value.ortsteilID;
			return id === null ? undefined : props.mapOrtsteile.get(id)
		},
		set: (value) => void props.patch({ ortsteilID: value === undefined ? null : value.id }, data.value.id),
	});

	const geschlecht = computed<Geschlecht>({
		get: () => Geschlecht.fromValue(data.value.geschlecht) ?? Geschlecht.X,
		set: (value) => void props.patch({ geschlecht: value.id }, data.value.id),
	});

	const strasse = computed(() => AdressenUtils.combineStrasse(data.value.strassenname ?? "", data.value.hausnummer ?? "", data.value.hausnummerZusatz ?? ""))

	function patchStrasse(value: string | null) {
		if (value !== null) {
			const vals = AdressenUtils.splitStrasse(value);
			void props.patch({ strassenname: vals[0], hausnummer: vals[1], hausnummerZusatz: vals[2] }, data.value.id);
		}
	}

	const staatsangehoerigkeit = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(data.value.staatsangehoerigkeitID) || Nationalitaeten.getDEU(),
		set: (value) => void props.patch({ staatsangehoerigkeitID: value.historie().getLast().iso3 }, data.value.id),
	});

	const staatsangehoerigkeit2 = computed<Nationalitaeten | null>({
		get: () => Nationalitaeten.getByISO3(data.value.staatsangehoerigkeit2ID),
		set: (value) => void props.patch({ staatsangehoerigkeit2ID: value?.historie().getLast().iso3 ?? null }, data.value.id),
	});

	const religion = computed<ReligionEintrag | undefined>({
		get: () => {
			const id = data.value.religionID;
			return id === null ? undefined : props.mapReligionen.get(id)
		},
		set: (value) => void props.patch({ religionID: value === undefined ? null : value.id }, data.value.id),
	});

	const geburtsland = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(data.value.geburtsland) || Nationalitaeten.getDEU(),
		set: (value) => void props.patch({ geburtsland: value.historie().getLast().iso3 }, data.value.id),
	});

	const geburtslandMutter = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(data.value.geburtslandMutter) || Nationalitaeten.getDEU(),
		set: (value) => void props.patch({ geburtslandMutter: value.historie().getLast().iso3 }, data.value.id),
	});

	const geburtslandVater = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(data.value.geburtslandVater) || Nationalitaeten.getDEU(),
		set: (value) => void props.patch({ geburtslandVater: value.historie().getLast().iso3 }, data.value.id),
	});

	const verkehrsprache = computed<Verkehrssprache>({
		get: () => Verkehrssprache.getByIsoKuerzel(data.value.verkehrspracheFamilie) || Verkehrssprache.data().getWertBySchluesselOrException("de"),
		set: (value) => void props.patch({ verkehrspracheFamilie: value.historie().getLast().iso3 }, data.value.id),
	});

	const externeSchulNr = computed<SchulEintrag | undefined>({
		get: () => (data.value.externeSchulNr === null) ? undefined : (props.mapSchulen.get(data.value.externeSchulNr) || undefined),
		set: (value) => void props.patch({ externeSchulNr: value === undefined ? null : value.schulnummerStatistik }, data.value.id),
	});

	const fahrschuelerart = computed<KatalogEintrag | undefined>({
		get: () => {
			const id = data.value.fahrschuelerArtID;
			return id === null ? undefined : props.mapFahrschuelerarten.get(id)
		},
		set: (value) => void props.patch({ fahrschuelerArtID: value === undefined ? null : value.id }, data.value.id),
	});

	const haltestelle = computed<KatalogEintrag | undefined>({
		get: () => {
			const id = data.value.haltestelleID;
			return id === null ? undefined : props.mapHaltestellen.get(id)
		},
		set: (value) => void props.patch({ haltestelleID: value === undefined ? null : value.id }, data.value.id),
	});

	const dauerKindergarten = computed<Kindergartenbesuch | undefined>({
		get(): Kindergartenbesuch | undefined {
			return Kindergartenbesuch.values().find(i => i.daten(schuljahr.value)?.schluessel === data.value.dauerKindergartenbesuch);
		},
		set(v: Kindergartenbesuch | undefined) {
			data.value.dauerKindergartenbesuch = v?.daten(schuljahr.value)?.schluessel ?? null;
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

		const result = await props.addSchueler(partialData);
		isLoading.value = false;
		data.value.id = result.id;
	}

	function cancel() {
		props.checkpoint.active = false;
		void props.gotoDefaultView(null);
	}

</script>
