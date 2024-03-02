<template>
	<div class="page--content">
		<svws-ui-content-card title="Stammdaten">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Bezeichnung 1" :model-value="schule().bezeichnung1" @change="bezeichnung1 => patch({ bezeichnung1 })" type="text" :disabled="!editSchuldaten" />
				<svws-ui-text-input placeholder="Schulnummer" :model-value="schule().schulNr" type="text" disabled />
				<svws-ui-text-input placeholder="Bezeichnung 2" :model-value="schule().bezeichnung2" @change="bezeichnung2 => patch({ bezeichnung2 })" type="text" :disabled="!editSchuldaten" />
				<svws-ui-text-input placeholder="Schulform" :model-value="textSchulform" type="text" disabled />
				<svws-ui-text-input placeholder="Bezeichnung 3" :model-value="schule().bezeichnung3" @change="bezeichnung3 => patch({ bezeichnung3 })" type="text" :disabled="!editSchuldaten" />
				<svws-ui-spacing />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Kontaktinformationen">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Straße" :model-value="strasse" @change="patchStrasse" type="text" span="full" :disabled="!editSchuldaten" />
				<!-- TODO PLZ Ort sollte durch den Orstkatalog gehandhabt werden - siehe auch Schüler-Kontaktdaten -->
				<!-- <svws-ui-select title="Wohnort" v-model="wohnortID" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete :disabled="!editSchuldaten" /> -->
				<svws-ui-text-input placeholder="Telefon" :model-value="schule().telefon" @change="telefon => patch({ telefon })" type="tel" :disabled="!editSchuldaten" />
				<svws-ui-text-input placeholder="Fax" :model-value="schule().fax" @change="fax => patch({ fax })" type="tel" :disabled="!editSchuldaten" />
				<svws-ui-text-input placeholder="Homepage" :model-value="schule().webAdresse" @change="webAdresse => patch({ webAdresse })" verify-email :disabled="!editSchuldaten" />
				<svws-ui-text-input placeholder="E-Mail-Adresse" :model-value="schule().email" @change="email => patch({ email })" type="email" verify-email :disabled="!editSchuldaten" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<!-- TODO Aktueller Schuljahresabschnitt und Optionen zum Setzen der Schule in den nächsten Schuljahresabschnitt -->
		<!-- TODO Dauer der Unterrichtseinheiten -->
		<svws-ui-content-card v-if="showSchuldaten" title="E-Mail-Server">
			<svws-ui-input-wrapper :grid="2">
				Hier werden zukünftig die Informationen für den SMTP-Server der Schule verwaltet
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { AdressenUtils, BenutzerKompetenz, Schulform } from "@core";
	import type { SchuleAppProps } from "./SSchuleAppProps";

	const props = defineProps<SchuleAppProps>();

	const showSchuldaten = computed<boolean>(() => props.benutzerIstAdmin || props.benutzerKompetenzen.has(BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN));
	const editSchuldaten = computed<boolean>(() => props.benutzerIstAdmin || props.benutzerKompetenzen.has(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));

	const strasse = computed(() => AdressenUtils.combineStrasse(props.schule().strassenname || "", props.schule().hausnummer || "", props.schule().hausnummerZusatz || ""))

	const patchStrasse = (value: string ) => {
		if (value) {
			const vals = AdressenUtils.splitStrasse(value);
			void props.patch({ strassenname: vals?.[0] || value, hausnummer: vals?.[1] || "", hausnummerZusatz: vals?.[2] || "" });
		}
	}

	const textSchulform = computed<string>(() => {
		const schulform = Schulform.getByKuerzel(props.schule().schulform);
		return schulform?.daten.bezeichnung ?? "---";
	});

</script>
