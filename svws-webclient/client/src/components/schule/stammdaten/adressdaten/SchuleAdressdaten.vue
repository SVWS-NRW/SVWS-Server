<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<div class="page page-grid-cards">
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-content-card title="Stammdaten">
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-text-input placeholder="Bezeichnung 1" class="contentFocusField"
							:model-value="schule().bezeichnung1"
							@change="bezeichnung1 => bezeichnung1 && patch({ bezeichnung1 })"
							:readonly />
						<svws-ui-text-input placeholder="Schulnummer"
							:model-value="schule().schulNr.toString()"
							readonly statistics />
						<svws-ui-text-input placeholder="Bezeichnung 2"
							:model-value="schule().bezeichnung2"
							@change="bezeichnung2 => patch({ bezeichnung2 })"
							:readonly />
						<svws-ui-text-input placeholder="Schulform"
							:model-value="textSchulform"
							readonly />
						<svws-ui-text-input placeholder="Bezeichnung 3"
							:model-value="schule().bezeichnung3"
							@change="bezeichnung3 => patch({ bezeichnung3 })"
							:readonly />
						<svws-ui-spacing />
					</svws-ui-input-wrapper>
				</svws-ui-content-card>
				<svws-ui-spacing :size="2" />
				<svws-ui-content-card title="Kontaktinformationen">
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-text-input placeholder="Straße" class="contentFocusField" span="full"
							:model-value="strasse"
							@change="patchStrasse"
							:readonly />
						<svws-ui-text-input placeholder="Telefon" type="tel"
							:model-value="schule().telefon"
							@change="telefon => patch({ telefon })"
							:readonly :max-len="20" />
						<svws-ui-text-input placeholder="Fax"
							:model-value="schule().fax"
							@change="fax => patch({ fax })" type="tel"
							:readonly :max-len="20" />
						<svws-ui-text-input placeholder="Homepage"
							:model-value="schule().webAdresse"
							@change="webAdresse => patch({ webAdresse })"
							verify-email :readonly />
						<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
							:model-value="schule().email"
							@change="email => patch({ email })"
							verify-email :readonly />
						<svws-ui-text-input placeholder="Ort"
							:model-value="schule().ort"
							@change="patchOrt"
							:valid="o => optionalInputIsValid(o, 50)"
							:readonly :max-len="50" />
						<svws-ui-text-input placeholder="PLZ"
							:model-value="schule().plz"
							@change="patchPlz"
							:valid="v => optionalInputIsValid(v, 10)"
							:readonly :max-len="10" />
					</svws-ui-input-wrapper>
				</svws-ui-content-card>
				<svws-ui-spacing :size="2" />
				<svws-ui-content-card v-if="!readonly" title="E-Mail-Server">
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-text-input placeholder="SMTP-Host" class="contentFocusField"
							:model-value="smptServerKonfiguration().host"
							@change="host => host && patchSMTPServerKonfiguration({ host })" />
						<svws-ui-input-number placeholder="Port"
							:model-value="smptServerKonfiguration().port"
							@change="port => (port !== null) && (port !== undefined) && patchSMTPServerKonfiguration({ port })" />
						<svws-ui-checkbox type="toggle"
							:model-value="smptServerKonfiguration().useStartTLS"
							@update:model-value="value => patchSMTPServerKonfiguration({ useStartTLS : value })">
							Nutze StartTLS
						</svws-ui-checkbox>
						<svws-ui-checkbox type="toggle"
							:model-value="smptServerKonfiguration().useTLS"
							@update:model-value="value => patchSMTPServerKonfiguration({ useTLS : value })">
							Nutze TLS
						</svws-ui-checkbox>
						<svws-ui-text-input placeholder="Trust TLS Host"
							:model-value="smptServerKonfiguration().trustTLSHost"
							@change="trustTLSHost => patchSMTPServerKonfiguration({ trustTLSHost: trustTLSHost || null })" />
					</svws-ui-input-wrapper>
				</svws-ui-content-card>
			</svws-ui-input-wrapper>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { AdressenUtils, BenutzerKompetenz, Schulform } from "@core";
	import type { SchuleAdressdatenProps } from "~/components/schule/stammdaten/adressdaten/SchuleAdressdatenProps";
	import { optionalInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<SchuleAdressdatenProps>();

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
		for (const abschnitt of props.schule().abschnitte) {
			if (abschnitt.id === id) {
				schuljahr = abschnitt.schuljahr;
				break;
			}
		}
		const schulform = Schulform.data().getWertByKuerzel(props.schule().schulform);
		return schulform?.daten(schuljahr)?.text ?? "—";
	});

	function patchOrt(ort: string | null) {
		if (optionalInputIsValid(ort, 50)) {
			void props.patch({ ort });
		}
	}

	function patchPlz(plz: string | null) {
		if (optionalInputIsValid(plz, 10)) {
			void props.patch({ plz });
		}
	}

</script>
