<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-lehrer-individualdaten /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-wrapper>
					<svws-ui-checkbox :readonly v-model="modelProxy.proxy.istSichtbar" focus-class-content>
						Ist sichtbar
					</svws-ui-checkbox>
					<svws-ui-checkbox :readonly v-model="modelProxy.proxy.istRelevantFuerStatistik" statistics>
						Ist relevant für Statistik
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Kürzel" :readonly v-model="modelProxy.proxy.kuerzel" @commit="modelProxy.patch" statistics required focus />
				<svws-ui-select title="Personal-Typ" :readonly v-model="inputPersonalTyp" :items="PersonalTyp.values()"
					:item-text="i => i.bezeichnung" required />
				<svws-ui-text-input placeholder="Nachname" :readonly v-model="modelProxy.proxy.nachname" @commit="modelProxy.patch" required statistics
					:validation="() => modelProxy.getFehler('nachname')" />
				<svws-ui-text-input placeholder="Rufname" :readonly v-model="modelProxy.proxy.vorname" @commit="modelProxy.patch" required statistics
					:validation="() => modelProxy.getFehler('vorname')" />
				<svws-ui-spacing />
				<svws-ui-select title="Geschlecht" :readonly v-model="inputGeschlecht" :items="Geschlecht.values()" :item-text="i=>i.text"
					required />
				<svws-ui-text-input placeholder="Geburtsdatum" :readonly v-model="modelProxy.proxy.geburtsdatum" @commit="modelProxy.patch" type="date" required statistics
					:validation="() => modelProxy.getFehler('geburtsdatum')" />
				<svws-ui-select title="Staatsangehörigkeit" :readonly v-model="inputStaatsangehoerigkeit" :items="Nationalitaeten.values()"
					:item-text="i => i.historie().getLast().staatsangehoerigkeit" :item-sort="staatsangehoerigkeitKatalogEintragSort"
					:item-filter="staatsangehoerigkeitKatalogEintragFilter" required autocomplete statistics />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Akademischer Grad" :readonly v-model="modelProxy.proxy.titel" @commit="modelProxy.patch" />
				<svws-ui-text-input placeholder="Amtsbezeichnung" :readonly v-model="modelProxy.proxy.amtsbezeichnung" @commit="modelProxy.patch" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Wohnort und Kontaktdaten">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Straße" :readonly v-model="inputStrasse" @commit="modelProxy.patch" span="full" />
				<svws-ui-select v-model="wohnortID" title="Wohnort" :readonly :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort"
					:item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
				<svws-ui-select v-model="ortsteilID" title="Ortsteil" :readonly :items="ortsteile" :item-sort="ortsteilSort"
					:item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''" removable />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon" :readonly v-model="modelProxy.proxy.telefon" @commit="modelProxy.patch" type="tel" :max-len="20" />
				<svws-ui-text-input placeholder="Mobil oder Fax" :readonly v-model="modelProxy.proxy.telefonMobil" @commit="modelProxy.patch" type="tel" :max-len="20" />
				<svws-ui-text-input placeholder="Private E-Mail-Adresse" :readonly v-model="modelProxy.proxy.emailPrivat" @commit="modelProxy.patch" type="email" verify-email />
				<svws-ui-text-input placeholder="Schulische E-Mail-Adresse" :readonly v-model="modelProxy.proxy.emailDienstlich" @commit="modelProxy.patch" type="email" verify-email />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Leitungsfunktionen">
			<svws-ui-table :columns="colsLeitungsfunktionen" :items="dataNotPatched().leitungsfunktionen" count>
				<template #cell(idLeitungsfunktion)="{ value }">
					{{ LehrerLeitungsfunktion.data().getWertByID(value)?.daten(schuljahr)?.text ?? '—' }}
				</template>
				<template #cell(beginn)="{ value }">
					{{ (value === null) || (JavaString.isBlank(value)) ? '—' : DateUtils.gibDatumGermanFormat(value) }}
				</template>
				<template #cell(ende)="{ value }">
					{{ (value === null) || (JavaString.isBlank(value)) ? '—' : DateUtils.gibDatumGermanFormat(value) }}
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { LehrerIndividualdatenProps } from "./SLehrerIndividualdatenProps";
	import type { LehrerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
	import { Geschlecht, Nationalitaeten, PersonalTyp, AdressenUtils, DateUtils, JavaString, LehrerLeitungsfunktion, BenutzerKompetenz } from "@core";
	import { staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort, orte_filter, orte_sort, ortsteilSort } from "~/utils/helfer";
	import { LehrerIndividualdatenModelProxy } from "./LehrerIndividualdatenModelProxy";

	const props = defineProps<LehrerIndividualdatenProps>();

	const dataNotPatched = () => props.lehrerListeManager().daten();
	async function patchMethod(data: Partial<LehrerStammdaten>): Promise<boolean> {
		await props.patch(data);
		return true;
	}
	const modelProxy = new LehrerIndividualdatenModelProxy(dataNotPatched, () => props.validatorKontext(), patchMethod);

	const schuljahr = computed<number>(() => props.lehrerListeManager().getSchuljahr());

	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.LEHRERDATEN_AENDERN));

	const inputGeschlecht = computed<Geschlecht>({
		get: () => Geschlecht.fromValue(modelProxy.proxy.geschlecht) || Geschlecht.X,
		set: (value) => modelProxy.proxy.geschlecht = value.id,
	});

	const inputPersonalTyp = computed<PersonalTyp>({
		get: () => PersonalTyp.fromKuerzel(modelProxy.proxy.personalTyp) || PersonalTyp.SONSTIGE,
		set: (value) => modelProxy.proxy.personalTyp = value.kuerzel,
	});

	const inputStaatsangehoerigkeit = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(modelProxy.proxy.staatsangehoerigkeitID) ?? Nationalitaeten.getDEU(),
		set: (value) => modelProxy.proxy.staatsangehoerigkeitID = value.historie().getLast().iso3,
	});

	const inputStrasse = computed<string | null>({
		get: () => AdressenUtils.combineStrasse(modelProxy.proxy.strassenname ?? "", modelProxy.proxy.hausnummer ?? "", modelProxy.proxy.hausnummerZusatz ?? ""),
		set: (value) => {
			const vals = AdressenUtils.splitStrasse(value);
			modelProxy.proxy.strassenname = vals[0];
			modelProxy.proxy.hausnummer = vals[1];
			modelProxy.proxy.hausnummerZusatz = vals[2];
		},
	});

	const wohnortID = computed<OrtKatalogEintrag | null>({
		get: () => {
			const idWohnort = modelProxy.proxy.wohnortID;
			return (idWohnort === null) ? null : props.mapOrte.get(idWohnort) ?? null;
		},
		set: (val) => modelProxy.proxy.wohnortID = val?.id ?? null,
	});

	const ortsteile = computed<Array<OrtsteilKatalogEintrag>>(() => {
		const result: Array<OrtsteilKatalogEintrag> = [];
		for (const ortsteil of props.mapOrtsteile.values()) {
			if (ortsteil.ort_id === modelProxy.proxy.wohnortID) {
				result.push(ortsteil);
			}
		}
		return result;
	});

	const ortsteilID = computed<OrtsteilKatalogEintrag | null>({
		get: () => {
			const idOrtsteil = modelProxy.proxy.ortsteilID;
			return idOrtsteil === null ? null : props.mapOrtsteile.get(idOrtsteil) ?? null;
		},
		set: (val) => modelProxy.proxy.ortsteilID = val?.id ?? null,
	});

	const colsLeitungsfunktionen = [
		{ key: 'idLeitungsfunktion', label: 'Funktion', span: 2 },
		{ key: 'beginn', label: 'Von', span: 1 },
		{ key: 'ende', label: 'Bis', span: 1 },
	];

</script>
