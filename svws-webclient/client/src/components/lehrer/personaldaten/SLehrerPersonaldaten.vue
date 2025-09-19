<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Identnummer" :readonly :model-value="personaldaten().identNrTeil1"
					@change="identNrTeil1 => patch({identNrTeil1})" span="full" focus statistics />
				<svws-ui-text-input placeholder="Seriennummer" :readonly :model-value="personaldaten().identNrTeil2SerNr"
					@change="identNrTeil2SerNr => patch({identNrTeil2SerNr})" statistics />
				<svws-ui-text-input placeholder="Vergütungsschlüssel" :readonly :model-value="personaldaten().lbvVerguetungsschluessel"
					@change="lbvVerguetungsschluessel => patch({lbvVerguetungsschluessel})" />
				<svws-ui-text-input placeholder="PA-Nummer" :readonly :model-value="personaldaten().personalaktennummer"
					@change="personalaktennummer => patch({personalaktennummer})" />
				<svws-ui-text-input placeholder="LBV-Personalnummer" :readonly :model-value="personaldaten().lbvPersonalnummer"
					@change="lbvPersonalnummer => patch({lbvPersonalnummer})" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Zugangsdatum" :readonly :model-value="personaldaten().zugangsdatum"
					@change="zugangsdatum => patch({zugangsdatum})" type="date" />
				<svws-ui-text-input placeholder="Abgangsdatum" :readonly :model-value="personaldaten().abgangsdatum"
					@change="abgangsdatum => patch({abgangsdatum})" type="date" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Beschäftigungsdaten">
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Rechtsverhältnis" :readonly v-model="rechtsverhaeltnis" :manager="rechtsverhaeltnisSelectManager" statistics
					:validator="() => validatorPersonalabschnittsDaten" :do-validate="validatePersonalabschnittDaten" class="contentFocusField"
					:removable="false" required />
				<ui-select label="Beschäftigungsart" :readonly v-model="beschaeftigungsart" :manager="beschaeftigungsartSelectManager" statistics
					class="contentFocusField" :removable="false" required />
				<svws-ui-input-number placeholder="Pflichtstundensoll" :readonly statistics
					:model-value="personalabschnittsdaten()?.pflichtstundensoll ?? 0.0"
					@change="pflichtstundensoll => patchAbschnittsdaten({ pflichtstundensoll: pflichtstundensoll }, personalabschnittsdaten()?.id ?? -1)" />
				<ui-select label="Einsatzstatus" :readonly v-model="einsatzstatus" statistics :manager="einsatzstatusSelectManager"
					class="contentFocusField" :removable="false" required />
				<svws-ui-text-input placeholder="Stammschule" :readonly :model-value="personalabschnittsdaten()?.stammschulnummer"
					@change="stammschulnummer => patchAbschnittsdaten({ stammschulnummer }, personalabschnittsdaten()?.id ?? -1)" statistics />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Lehrämter">
			<svws-ui-input-wrapper>
				<s-lehrer-personaldaten-lehraemter :hat-update-kompetenz="!readonly" :schuljahr :lehrer-liste-manager
					:patch-lehramt :add-lehramt :remove-lehraemter
					:patch-lehrbefaehigung :add-lehrbefaehigung :remove-lehrbefaehigungen
					:patch-fachrichtung :add-fachrichtung :remove-fachrichtungen="removeFachrichtungen" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Mehr- und Minderleistung, Anrechnungsstunden">
			<svws-ui-input-wrapper>
				<s-lehrer-personaldaten-anrechnungen :hat-update-kompetenz="!readonly" :personalabschnittsdaten :schuljahr :schulform :add-mehrleistung :patch-mehrleistung :remove-mehrleistung
					:add-minderleistung :patch-minderleistung :remove-minderleistung :add-anrechnung :patch-anrechnung :remove-anrechnung />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, watch } from "vue";
	import type { LehrerPersonaldatenProps } from './SLehrerPersonaldatenProps';
	import type { LehrerBeschaeftigungsartKatalogEintrag, LehrerEinsatzstatusKatalogEintrag, LehrerRechtsverhaeltnisKatalogEintrag, Validator} from "@core";
	import { DeveloperNotificationException, ValidatorLehrerPersonalabschnittsdaten} from "@core";
	import { LehrerBeschaeftigungsart, LehrerEinsatzstatus, LehrerRechtsverhaeltnis, LehrerAnrechnungsgrund, LehrerMehrleistungsarten, LehrerMinderleistungsarten,
		BenutzerKompetenz} from "@core";
	import { CoreTypeSelectManager } from "@ui";

	const props = defineProps<LehrerPersonaldatenProps>();

	const schuljahr = computed<number>(() => props.aktAbschnitt.schuljahr);

	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN));

	const personaldaten = () => props.lehrerListeManager().personalDaten();
	const personalabschnittsdaten = () => props.lehrerListeManager().getAbschnittBySchuljahresabschnittsId(props.aktAbschnitt.id);

	const rechtsverhaeltnisSelectManager = new CoreTypeSelectManager({ clazz: LehrerRechtsverhaeltnis.class, schuljahr: schuljahr,
		schulformen: props.schulform, optionDisplayText: "text", selectionDisplayText: "text",
	});

	const beschaeftigungsartSelectManager = new CoreTypeSelectManager({ clazz: LehrerBeschaeftigungsart.class, schuljahr: schuljahr,
		schulformen: props.schulform, optionDisplayText: "text", selectionDisplayText: "text",
	});

	const einsatzstatusSelectManager = new CoreTypeSelectManager({ clazz: LehrerEinsatzstatus.class, schuljahr: schuljahr, schulformen: props.schulform,
		optionDisplayText: "text", selectionDisplayText: "text",
	});

	const rechtsverhaeltnis = computed<LehrerRechtsverhaeltnisKatalogEintrag | undefined>({
		get(): LehrerRechtsverhaeltnisKatalogEintrag | undefined {
			return LehrerRechtsverhaeltnis.values().map(r => r.daten(schuljahr.value) ?? undefined)
				.find(d => d?.schluessel === personalabschnittsdaten()?.rechtsverhaeltnis);
		},
		set(val: LehrerRechtsverhaeltnisKatalogEintrag | undefined) {
			const daten = personalabschnittsdaten();
			if (daten !== null)
				void props.patchAbschnittsdaten({ rechtsverhaeltnis: val?.schluessel }, daten.id);
		},
	});

	const beschaeftigungsart = computed<LehrerBeschaeftigungsartKatalogEintrag | undefined>({
		get(): LehrerBeschaeftigungsartKatalogEintrag | undefined {
			return LehrerBeschaeftigungsart.values().map(r => r.daten(schuljahr.value) || undefined)
				.find(d => d?.schluessel === personalabschnittsdaten()?.beschaeftigungsart);
		},
		set(val: LehrerBeschaeftigungsartKatalogEintrag | undefined) {
			const daten = personalabschnittsdaten();
			if (daten !== null)
				void props.patchAbschnittsdaten({ beschaeftigungsart: val?.schluessel }, daten.id);
		},
	});


	const einsatzstatus = computed<LehrerEinsatzstatusKatalogEintrag | undefined>({
		get(): LehrerEinsatzstatusKatalogEintrag | undefined {
			return LehrerEinsatzstatus.values().map(r => r.daten(schuljahr.value) || undefined)
				.find(d => d?.schluessel === personalabschnittsdaten()?.einsatzstatus);
		},
		set(val: LehrerEinsatzstatusKatalogEintrag | undefined) {
			const daten = personalabschnittsdaten();
			if (daten !== null)
				void props.patchAbschnittsdaten({ einsatzstatus: val?.schluessel }, daten.id);
		},
	});

	const mehrleistungsgrund = computed<LehrerMehrleistungsarten | undefined>({
		get(): LehrerMehrleistungsarten | undefined {
			// TODO aus Personaldaten bestimmten
			const schluessel = undefined;
			return LehrerMehrleistungsarten.values().find(e => e.daten(schuljahr.value)?.schluessel === schluessel);
		},
		set(val: LehrerMehrleistungsarten | undefined) {
			// TODO props.patch({ mehrleistungsgrund: val?.schluessel });
		},
	});

	const minderleistungsgrund = computed<LehrerMinderleistungsarten | undefined>({
		get(): LehrerMinderleistungsarten | undefined {
			// TODO aus Personaldaten bestimmen
			const schluessel = undefined;
			return LehrerMinderleistungsarten.values().find(e => e.daten(schuljahr.value)?.schluessel === schluessel);
		},
		set(val: LehrerMinderleistungsarten | undefined) {
			// TODO props.patch({ minderleistungsgrund: val?.schluessel });
		},
	});

	const anrechnungsgrund = computed<LehrerAnrechnungsgrund | undefined>({
		get(): LehrerAnrechnungsgrund | undefined {
			// TODO aus Personaldaten bestimmten
			const schluessel = undefined;
			return LehrerAnrechnungsgrund.values().find(e => e.daten(schuljahr.value)?.schluessel === schluessel);
		},
		set(val: LehrerAnrechnungsgrund | undefined) {
			// TODO props.patch({ anrechnungsgrund: val?.schluessel });
		},
	});


	const validatorPersonalabschnittsDaten = computed<ValidatorLehrerPersonalabschnittsdaten>(() => {
		const daten = personalabschnittsdaten();
		if (daten !== null)
			return new ValidatorLehrerPersonalabschnittsdaten(daten, props.lehrerListeManager().daten(), props.validatorKontext());
		else throw new DeveloperNotificationException("Keine Personalabschnittsdaten gefunden.");
	});

	function validatePersonalabschnittDaten(validator: Validator): boolean {
		return validator.run();
	};

</script>
