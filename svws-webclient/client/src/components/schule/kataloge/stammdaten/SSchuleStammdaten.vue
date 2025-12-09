<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>{{ schule().bezeichnung1 }}</span>
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							Schulnummer: {{ schule().schulNr }}
						</svws-ui-badge>
					</h2>
					<span class="svws-subline">{{ `${schule().bezeichnung2 ?? ''} ${schule().bezeichnung3 ?? ''}` }}</span>
				</div>
			</div>
			<div class="svws-ui-header--actions">
				<svws-ui-modal-hilfe> <hilfe-schule-stammdaten /> </svws-ui-modal-hilfe>
			</div>
		</header>
		<div class="page page-grid-cards">
			<svws-ui-content-card title="Stammdaten">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input class="contentFocusField" placeholder="Bezeichnung 1" :model-value="schule().bezeichnung1" @change="bezeichnung1 => bezeichnung1 && patch({ bezeichnung1 })" type="text" :readonly />
					<svws-ui-text-input placeholder="Schulnummer" :model-value="schule().schulNr.toString()" :readonly statistics />
					<svws-ui-text-input placeholder="Bezeichnung 2" :model-value="schule().bezeichnung2" @change="bezeichnung2 => patch({ bezeichnung2 })" :readonly />
					<svws-ui-text-input placeholder="Schulform" :model-value="textSchulform" readonly />
					<svws-ui-text-input placeholder="Bezeichnung 3" :model-value="schule().bezeichnung3" @change="bezeichnung3 => patch({ bezeichnung3 })" :readonly />
					<svws-ui-spacing />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card title="Kontaktinformationen">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input class="contentFocusField" placeholder="Straße" :model-value="strasse" @change="patchStrasse" span="full" :readonly />
					<!-- TODO PLZ Ort sollte durch den Orstkatalog gehandhabt werden - siehe auch Schüler-Kontaktdaten -->
					<!-- <svws-ui-select title="Wohnort" v-model="wohnortID" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete :disabled="!editSchuldaten" /> -->
					<svws-ui-text-input placeholder="Telefon" :model-value="schule().telefon" @change="telefon => patch({ telefon })" type="tel" :readonly :max-len="20" />
					<svws-ui-text-input placeholder="Fax" :model-value="schule().fax" @change="fax => patch({ fax })" type="tel" :readonly :max-len="20" />
					<svws-ui-text-input placeholder="Homepage" :model-value="schule().webAdresse" @change="webAdresse => patch({ webAdresse })" verify-email :readonly />
					<svws-ui-text-input placeholder="E-Mail-Adresse" :model-value="schule().email" @change="email => patch({ email })" type="email" verify-email :readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<!-- TODO Aktueller Schuljahresabschnitt und Optionen zum Setzen der Schule in den nächsten Schuljahresabschnitt -->
			<!-- TODO Dauer der Unterrichtseinheiten -->
			<svws-ui-content-card v-if="!readonly" title="E-Mail-Server">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input class="contentFocusField" placeholder="SMTP-Host" :model-value="smptServerKonfiguration().host" @change="host => host && patchSMTPServerKonfiguration({ host })" />
					<svws-ui-input-number placeholder="Port" :model-value="smptServerKonfiguration().port" @change="port => (port !== null) && (port !== undefined) && patchSMTPServerKonfiguration({ port })" />
					<svws-ui-checkbox type="toggle" :model-value="smptServerKonfiguration().useStartTLS" @update:model-value="value => patchSMTPServerKonfiguration({ useStartTLS : (value === true) ? true : false })">Nutze StartTLS</svws-ui-checkbox>
					<svws-ui-checkbox type="toggle" :model-value="smptServerKonfiguration().useTLS" @update:model-value="value => patchSMTPServerKonfiguration({ useTLS : (value === true) ? true : false })">Nutze TLS</svws-ui-checkbox>
					<svws-ui-text-input placeholder="Trust TLS Host" :model-value="smptServerKonfiguration().trustTLSHost" @change="trustTLSHost => patchSMTPServerKonfiguration({ trustTLSHost: trustTLSHost || null })" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { AdressenUtils, BenutzerKompetenz, Schulform } from "@core";
	import type { SchuleAppProps } from "./SSchuleAppProps";

	const props = defineProps<SchuleAppProps>();

	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));

	const strasse = computed(() => AdressenUtils.combineStrasse(props.schule().strassenname ?? "", props.schule().hausnummer ?? "", props.schule().hausnummerZusatz ?? ""));

	const patchStrasse = (value: string | null) => {
		if (value !== null) {
			const [strassenname, hausnummer, hausnummerZusatz] = AdressenUtils.splitStrasse(value);
			void props.patch({ strassenname, hausnummer, hausnummerZusatz });
		}
	};

	const textSchulform = computed<string>(() => {
		let schuljahr = -1;
		const id = props.schule().idSchuljahresabschnitt;
		for (const abschnitt of props.schule().abschnitte)
			if (abschnitt.id === id) {
				schuljahr = abschnitt.schuljahr;
				break;
			}
		const schulform = Schulform.data().getWertByKuerzel(props.schule().schulform);
		return schulform?.daten(schuljahr)?.text ?? "—";
	});

</script>
