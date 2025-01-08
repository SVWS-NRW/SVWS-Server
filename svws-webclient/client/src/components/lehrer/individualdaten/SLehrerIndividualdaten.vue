<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-wrapper>
					<svws-ui-checkbox focus-class :disabled="!hatUpdateKompetenz" :model-value="data().istSichtbar" @update:model-value="istSichtbar => patch({istSichtbar: istSichtbar === true})"> Ist sichtbar </svws-ui-checkbox>
					<svws-ui-checkbox :disabled="!hatUpdateKompetenz" :model-value="data().istRelevantFuerStatistik" @update:model-value="istRelevantFuerStatistik => patch({istRelevantFuerStatistik: istRelevantFuerStatistik === true})"> Ist Relevant für Statistik </svws-ui-checkbox>
				</svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Kürzel" :disabled="!hatUpdateKompetenz" :model-value="data().kuerzel" @change="kuerzel => patch({kuerzel: kuerzel ?? undefined})" required focus />
				<svws-ui-select title="Personal-Typ" :disabled="!hatUpdateKompetenz" v-model="inputPersonalTyp" :items="PersonalTyp.values()" :item-text="i => i.bezeichnung" required />
				<svws-ui-text-input placeholder="Nachname" :disabled="!hatUpdateKompetenz" :model-value="data().nachname" @change="nachname => patch({nachname: nachname ?? undefined})"
					required statistics :validator="() => validatorNachname" :do-validate="validateNachname" />
				<svws-ui-text-input placeholder="Rufname" :disabled="!hatUpdateKompetenz" :model-value="data().vorname" @change="vorname => patch({vorname: vorname ?? undefined})"
					required statistics :validator="() => validatorVorname" :do-validate="validateVorname" />
				<svws-ui-spacing />
				<svws-ui-select title="Geschlecht" :disabled="!hatUpdateKompetenz" v-model="inputGeschlecht" :items="Geschlecht.values()" :item-text="i=>i.text" required />
				<svws-ui-text-input placeholder="Geburtsdatum" :disabled="!hatUpdateKompetenz" :model-value="data().geburtsdatum" @change="geburtsdatum => geburtsdatum && patch({geburtsdatum})" type="date" required statistics />
				<svws-ui-select title="Staatsangehörigkeit" :disabled="!hatUpdateKompetenz" v-model="inputStaatsangehoerigkeit" :items="Nationalitaeten.values()"
					:item-text="i => i.daten.staatsangehoerigkeit" :item-sort="staatsangehoerigkeitKatalogEintragSort"
					:item-filter="staatsangehoerigkeitKatalogEintragFilter" required autocomplete />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Akadademischer Grad" :disabled="!hatUpdateKompetenz" :model-value="data().titel" @change="titel => patch({titel})" type="text" />
				<svws-ui-text-input placeholder="Amtsbezeichnung" :disabled="!hatUpdateKompetenz" :model-value="data().amtsbezeichnung" @change="amtsbezeichnung => patch({amtsbezeichnung})" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Wohnort und Kontaktdaten">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Straße" :disabled="!hatUpdateKompetenz" :model-value="strasse" @change="patchStrasse" type="text" span="full" />
				<svws-ui-select v-model="wohnortID" title="Wohnort" :disabled="!hatUpdateKompetenz" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
				<svws-ui-select v-model="ortsteilID" title="Ortsteil" :disabled="!hatUpdateKompetenz" :items="ortsteile" :item-sort="ortsteilSort" :item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''" removable />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon" :disabled="!hatUpdateKompetenz" :model-value="data().telefon" @change="telefon => patch({telefon})" type="tel" />
				<svws-ui-text-input placeholder="Mobil oder Fax" :disabled="!hatUpdateKompetenz" :model-value="data().telefonMobil" @change="telefonMobil => patch({telefonMobil})" type="tel" />
				<svws-ui-text-input placeholder="Private E-Mail-Adresse" :disabled="!hatUpdateKompetenz" :model-value="data().emailPrivat" @change="emailPrivat => patch({emailPrivat})" type="email" verify-email />
				<svws-ui-text-input placeholder="Schulische E-Mail-Adresse" :disabled="!hatUpdateKompetenz" :model-value="data().emailDienstlich" @change="emailDienstlich => patch({emailDienstlich})" type="email" verify-email />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Leitungsfunktionen">
			<svws-ui-table :columns="colsLeitungsfunktionen" :items="lehrerListeManager().daten().leitungsfunktionen" count>
				<template #cell(idLeitungsfunktion)="{ value }">
					{{ LehrerLeitungsfunktion.data().getWertByID(value)?.daten(schuljahr)?.text ?? '—' }}
				</template>
				<template #cell(beginn)="{ value }">
					{{ (value === null) || (JavaString.isBlank(value)) ? '–' : DateUtils.gibDatumGermanFormat(value) }}
				</template>
				<template #cell(ende)="{ value }">
					{{ (value === null) || (JavaString.isBlank(value)) ? '–' : DateUtils.gibDatumGermanFormat(value) }}
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { LehrerIndividualdatenProps } from "./SLehrerIndividualdatenProps";
	import type { OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
	import { Geschlecht, Nationalitaeten, PersonalTyp, AdressenUtils, DateUtils, JavaString, LehrerLeitungsfunktion, BenutzerKompetenz,
		ValidatorLehrerStammdatenNachname, ValidatorLehrerStammdatenVorname, ValidatorLehrerStammdatenGeburtsdatum } from "@core";
	import { staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort, orte_filter, orte_sort, ortsteilSort } from "~/utils/helfer";

	const props = defineProps<LehrerIndividualdatenProps>();

	const validatorNachname = computed(() => new ValidatorLehrerStammdatenNachname(props.lehrerListeManager().daten(), props.validatorKontext()));
	function validateNachname(validator: ValidatorLehrerStammdatenNachname, value: string | null): boolean {
		const name = props.lehrerListeManager().daten().nachname;
		props.lehrerListeManager().daten().nachname = value ?? "";
		const res = validator.run();
		props.lehrerListeManager().daten().nachname = name;
		return res;
	};

	const validatorVorname = computed(() => new ValidatorLehrerStammdatenVorname(props.lehrerListeManager().daten(), props.validatorKontext()));
	function validateVorname(validator: ValidatorLehrerStammdatenVorname, value: string | null): boolean {
		const name = props.lehrerListeManager().daten().vorname;
		props.lehrerListeManager().daten().vorname = value ?? "";
		const res = validator.run();
		props.lehrerListeManager().daten().vorname = name;
		return res;
	};

	const validatorGeburtsdatum = computed(() => new ValidatorLehrerStammdatenGeburtsdatum(props.lehrerListeManager().daten(), props.validatorKontext()));
	function validateGeburtsdatum(validator: ValidatorLehrerStammdatenGeburtsdatum, value: string | null): boolean {
		const datum = props.lehrerListeManager().daten().geburtsdatum;
		props.lehrerListeManager().daten().geburtsdatum = value ?? "";
		const res = validator.run();
		props.lehrerListeManager().daten().geburtsdatum = datum;
		return res;
	};

	const schuljahr = computed<number>(() => props.lehrerListeManager().getSchuljahr());

	const hatUpdateKompetenz = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.LEHRERDATEN_AENDERN));

	const data = () => props.lehrerListeManager().daten();

	const inputGeschlecht = computed<Geschlecht>({
		get: () => Geschlecht.fromValue(data().geschlecht) || Geschlecht.X,
		set: (value) => void props.patch({ geschlecht: value.id }),
	});

	const inputPersonalTyp = computed<PersonalTyp>({
		get: () => PersonalTyp.fromKuerzel(data().personalTyp) || PersonalTyp.SONSTIGE,
		set: (value) => void props.patch({ personalTyp: value.kuerzel }),
	});

	const inputStaatsangehoerigkeit = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(data().staatsangehoerigkeitID) || Nationalitaeten.DEU,
		set: (value) => void props.patch({ staatsangehoerigkeitID: value.daten.iso3 }),
	});

	const strasse = computed(() => AdressenUtils.combineStrasse(data().strassenname ?? "", data().hausnummer ?? "", data().hausnummerZusatz ?? ""))

	const patchStrasse = (value: string | null) => {
		const vals = AdressenUtils.splitStrasse(value);
		void props.patch({ strassenname: vals[0], hausnummer: vals[1], hausnummerZusatz: vals[2] });
	}

	const wohnortID = computed<OrtKatalogEintrag | null>({
		get: () => {
			const idWohnort = data().wohnortID;
			return (idWohnort === null) ? null : props.mapOrte.get(idWohnort) ?? null;
		},
		set: (val) => void props.patch({ wohnortID: val?.id ?? null }),
	});

	const ortsteile = computed<Array<OrtsteilKatalogEintrag>>(() => {
		const result : Array<OrtsteilKatalogEintrag> = [];
		for (const ortsteil of props.mapOrtsteile.values())
			if ((ortsteil.ort_id === null) || (ortsteil.ort_id === data().wohnortID))
				result.push(ortsteil);
		return result;
	});

	const ortsteilID = computed<OrtsteilKatalogEintrag | null>({
		get: () => {
			const idOrtsteil = data().ortsteilID;
			return idOrtsteil === null ? null : props.mapOrtsteile.get(idOrtsteil) ?? null;
		},
		set: (val) => void props.patch({ ortsteilID: val?.id ?? null }),
	});

	const colsLeitungsfunktionen = [
		{key: 'idLeitungsfunktion', label: 'Funktion', span: 2 },
		{key: 'beginn', label: 'Von', span: 1 },
		{key: 'ende', label: 'Bis', span: 1 },
	]

</script>
