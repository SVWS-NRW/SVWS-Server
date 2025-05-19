<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Anmeldedaten">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-select title="Status" :items="SchuelerStatus.values()" :item-text="i => i.name().toLowerCase()?? '—'"
					:model-value="SchuelerStatus.data().getWertByKuerzel('' + data.status)"
					@update:model-value="v => data.status = v?.ordinal() ?? -1" />
				<svws-ui-text-input placeholder="Schuljahr" type="text" />
				<svws-ui-text-input placeholder="Halbjahr" type="text" />
				<svws-ui-text-input placeholder="Jahrgang" type="text" />
				<svws-ui-text-input placeholder="Klasse" type="text" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Einschulungsart" type="text" />
				<svws-ui-text-input placeholder="Anmeldedatum" type="date" />
				<svws-ui-text-input placeholder="Aufnahmedatum" type="date" />
				<svws-ui-text-input placeholder="Beginn Bildungsgang" type="date" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Persönliche Daten">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-text-input placeholder="Name" required v-model="data.nachname" />
				<svws-ui-text-input placeholder="Vorname" required v-model="data.vorname" />
				<svws-ui-text-input placeholder="Weitere Vornamen" v-model="data.alleVornamen" />
				<!--TODO gegen einen Select austauschen-->
				<svws-ui-select title="Geschlecht" required :items="Geschlecht.values()" :item-text="i => i.text"
					:model-value="Geschlecht.fromValue(data.geschlecht)" @update:model-value="v => data.geschlecht = v?.id ?? -1" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Straße" type="text" v-model="data.strassenname" />
				<!--TODO Wohnort implementieren-->
				<svws-ui-select title="Wohnort" :items="mapOrte" :model-value="mapOrte.get(data.wohnortID?? -1)" :item-text="i => `${i.plz} ${i.ortsname}`"
					:item-filter="orte_filter" :item-sort="orte_sort" />
				<svws-ui-spacing />
				<!--TODO gegen einen Select austauschen-->
				<svws-ui-text-input placeholder="Ortsteil" />
				<svws-ui-text-input placeholder="Geburtsdatum" required type="date" v-model="data.geburtsdatum" />
				<svws-ui-text-input placeholder="Geburtsort" v-model="data.geburtsort" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon" type="tel" v-model="data.telefon" :valid="fieldIsValid('telefon')" :max-len="20" />
				<svws-ui-text-input placeholder="Mobil/Fax" type="tel" v-model="data.telefonMobil" :valid="fieldIsValid('telefonMobil')" :max-len="20" />
				<svws-ui-text-input placeholder="E-Mail" type="email" v-model="data.emailPrivat" :valid="fieldIsValid('emailPrivat')" />
				<svws-ui-spacing />
				<svws-ui-select title="1. Staatsangehörigkeit" :items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit"
					:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter"
					:model-value="Nationalitaeten.getByISO3(data.staatsangehoerigkeitID)" :valid="fieldIsValid('staatsangehoerigkeitID')"
					@update:model-value="v => data.staatsangehoerigkeitID = v?.historie().getLast().iso3 ?? null" />
				<svws-ui-select title="2. Staatsangehörigkeit" :items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit"
					:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter"
					:model-value="Nationalitaeten.getByISO3(data.staatsangehoerigkeit2ID)" :valid="fieldIsValid('staatsangehoerigkeit2ID')"
					@update:model-value="v => data.staatsangehoerigkeit2ID = v?.historie().getLast().iso3 ?? null" />
				<svws-ui-select title="Konfession" type="text" :items="mapReligionen" :item-text="i => i.bezeichnungZeugnis ?? ''"
					:model-value="mapReligionen.get(data.religionID?? -1)" @update:model-value="v => data.religionID = v?.id ?? -1" />
				<svws-ui-spacing />
				<svws-ui-select title="Geburtsland" :items="Nationalitaeten.values()" :item-text="i => `${i.historie().getLast().bezeichnung} (${i.historie().getLast().iso3})`"
					:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter" :model-value="Nationalitaeten.getByISO3(data.geburtsland)"
					:valid="fieldIsValid('geburtsland')" @update:model-value="v => data.geburtsland = v?.historie().getLast().iso3 ?? null" />
				<svws-ui-select title="Geburtsland Mutter" :items="Nationalitaeten.values()" :item-text="i => `${i.historie().getLast().bezeichnung} (${i.historie().getLast().iso3})`"
					:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter" :model-value="Nationalitaeten.getByISO3(data.geburtslandMutter)"
					:valid="fieldIsValid('geburtslandMutter')" @update:model-value="v => data.geburtslandMutter = v?.historie().getLast().iso3 ?? null" />
				<svws-ui-select title="Geburtsland Vater" :items="Nationalitaeten.values()" :item-text="i => `${i.historie().getLast().bezeichnung} (${i.historie().getLast().iso3})`"
					:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter" :model-value="Nationalitaeten.getByISO3(data.geburtslandVater)"
					:valid="fieldIsValid('geburtslandVater')" @update:model-value="v => data.geburtslandVater = v?.historie().getLast().iso3 ?? null" />
				<svws-ui-spacing />
				<svws-ui-select title="Verkehrssprache" :items="Verkehrssprache.values()" :item-text="i => `${i.daten.bezeichnung} (${i.daten.kuerzel})`"
					:model-value="Verkehrssprache.getByKuerzelAuto(data.verkehrspracheFamilie) " :item-sort="verkehrsspracheKatalogEintragSort"
					:item-filter="verkehrsspracheKatalogEintragFilter" @update:model-value="v => data.verkehrspracheFamilie = v?.daten.kuerzel ?? null" />
				<svws-ui-input-number placeholder="Zuzugsjahr" v-model="data.zuzugsjahr" />
				<svws-ui-text-input placeholder="Abmeldung vom Religionsunterricht" v-model="data.religionabmeldung" type="date" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Ext. ID-Nr." type="text" v-model="data.externeSchulNr" />
				<!--TODO Ausweisnummer, Schwerbehindertenausweis, Bemerkumng zu SchuelerStammdaten hinzufügen-->
				<svws-ui-text-input placeholder="NR. Schülerausweis" />
				<svws-ui-text-input placeholder="Schwerbehindertenausweis" type="text" />
				<svws-ui-text-input placeholder="Zust. Foto" type="text" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Bemerkung" type="text" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Vorschulentwicklung">
			<svws-ui-input-wrapper :grid="2">
				<!--TODO Kindergarten und Sprachkurs zu SchuelerStammdaten hinzufügen-->
				<svws-ui-text-input placeholder="Dauer des Kindergartenbesuchs" type="text" />
				<svws-ui-text-input placeholder="Name des Kindergartens" type="text" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Verpflichtung f. Sprachförderkurs" type="text" />
				<svws-ui-text-input placeholder="Teilnahme an Sprachförderkurs" type="text" />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addSchuelerStammdaten">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerNeuProps } from "~/components/schueler/SSchuelerNeuProps";
	import { ref } from "vue";
	import { Geschlecht, JavaString, Nationalitaeten, SchuelerStammdaten, SchuelerStatus, Verkehrssprache } from "@core";
	import { nationalitaetenKatalogEintragFilter, nationalitaetenKatalogEintragSort, orte_filter, orte_sort, staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort, verkehrsspracheKatalogEintragFilter, verkehrsspracheKatalogEintragSort } from "~/utils/helfer";

	const props = defineProps<SchuelerNeuProps>();

	const data = ref(new SchuelerStammdaten());
	const isLoading = ref<boolean>(false);

	//validation logic
	function fieldIsValid(field: keyof SchuelerStammdaten | null):(v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'telefon':
					return phoneNumberIsValid(data.value.telefon, false, 20);
				case 'telefonMobil':
					return phoneNumberIsValid(data.value.telefon, false, 20);
				case 'emailPrivat':
					return stringIsValid(data.value.telefon, false, 20);
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
