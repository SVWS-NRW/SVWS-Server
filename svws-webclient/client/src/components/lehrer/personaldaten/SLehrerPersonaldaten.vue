<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Identnummer" :disabled="!hatUpdateKompetenz" :model-value="personaldaten().identNrTeil1"
					@change="identNrTeil1 => patch({identNrTeil1})" span="full" focus statistics />
				<svws-ui-text-input placeholder="Seriennummer" :disabled="!hatUpdateKompetenz" :model-value="personaldaten().identNrTeil2SerNr"
					@change="identNrTeil2SerNr => patch({identNrTeil2SerNr})" statistics />
				<svws-ui-text-input placeholder="Vergütungsschlüssel" :disabled="!hatUpdateKompetenz" :model-value="personaldaten().lbvVerguetungsschluessel"
					@change="lbvVerguetungsschluessel => patch({lbvVerguetungsschluessel})" />
				<svws-ui-text-input placeholder="PA-Nummer" :disabled="!hatUpdateKompetenz" :model-value="personaldaten().personalaktennummer"
					@change="personalaktennummer => patch({personalaktennummer})" />
				<svws-ui-text-input placeholder="LBV-Personalnummer" :disabled="!hatUpdateKompetenz" :model-value="personaldaten().lbvPersonalnummer"
					@change="lbvPersonalnummer => patch({lbvPersonalnummer})" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Zugangsdatum" :disabled="!hatUpdateKompetenz" :model-value="personaldaten().zugangsdatum"
					@change="zugangsdatum => patch({zugangsdatum})" type="date" />
				<svws-ui-text-input placeholder="Abgangsdatum" :disabled="!hatUpdateKompetenz" :model-value="personaldaten().abgangsdatum"
					@change="abgangsdatum => patch({abgangsdatum})" type="date" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Beschäftigungsdaten">
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Rechtsverhältnis" :disabled="!hatUpdateKompetenz" v-model="rechtsverhaeltnis" :manager="rechtsverhaeltnisSelectManager" statistics
					:validator="() => validatorPersonalabschnittsDaten" :do-validate="validatePersonalabschnittDaten" required focus-class-content />
				<ui-select label="Beschäftigungsart" :disabled="!hatUpdateKompetenz" v-model="beschaeftigungsart" :manager="beschaeftigungsartSelectManager" statistics
					required focus-class-content />
				<svws-ui-input-number placeholder="Pflichtstundensoll" :disabled="!hatUpdateKompetenz" statistics
					:model-value="personalabschnittsdaten()?.pflichtstundensoll ?? 0.0"
					@change="pflichtstundensoll => patchAbschnittsdaten({ pflichtstundensoll: pflichtstundensoll }, personalabschnittsdaten()?.id ?? -1)" />
				<ui-select label="Einsatzstatus" :disabled="!hatUpdateKompetenz" v-model="einsatzstatus" statistics
					:manager="einsatzstatusSelectManager" required focus-class-content />
				<svws-ui-text-input placeholder="Stammschule" :disabled="!hatUpdateKompetenz" :model-value="personalabschnittsdaten()?.stammschulnummer"
					@change="stammschulnummer => patchAbschnittsdaten({ stammschulnummer }, personalabschnittsdaten()?.id ?? -1)" statistics />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Lehrämter">
			<svws-ui-input-wrapper>
				<s-lehrer-personaldaten-lehraemter :hat-update-kompetenz :lehrer-liste-manager :patch-lehramt
					:add-lehramt :remove-lehraemter :schuljahr />
				<s-lehrer-personaldaten-lehrbefaehigungen :hat-update-kompetenz :lehrer-liste-manager
					:patch-lehrbefaehigung :add-lehrbefaehigung :remove-lehrbefaehigungen :schuljahr />
				<s-lehrer-personaldaten-fachrichtungen :hat-update-kompetenz :lehrer-liste-manager
					:patch-fachrichtung :add-fachrichtung :remove-fachrichtungen="removeFachrichtungen"
					:schuljahr />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Mehr- und Minderleistung, Anrechnungsstunden">
			<svws-ui-input-wrapper>
				<svws-ui-select title="Mehrleistung" :disabled="!hatUpdateKompetenz" v-model="mehrleistungsgrund" :items="LehrerMehrleistungsarten.values()"
					:item-text="i => i.daten(schuljahr)?.text ?? '—'" focus-class-content statistics />
				<svws-ui-select title="Minderleistung" :disabled="!hatUpdateKompetenz" v-model="minderleistungsgrund" :items="LehrerMinderleistungsarten.values()"
					:item-text="i => i.daten(schuljahr)?.text ?? '—'" statistics />
				<svws-ui-input-number placeholder="Stundensumme" :disabled="!hatUpdateKompetenz" :model-value="personalabschnittsdaten()?.pflichtstundensoll ?? 0.0"
					@change="pflichtstundensoll => patchAbschnittsdaten({ pflichtstundensoll: pflichtstundensoll }, personalabschnittsdaten()?.id ?? -1)"
					statistics />
				<svws-ui-select title="Nicht unterrichtliche Tätigkeiten" :disabled="!hatUpdateKompetenz" v-model="anrechnungsgrund"
					:items="LehrerAnrechnungsgrund.values()"
					:item-text="i => i.daten(schuljahr)?.text ?? '—'" statistics />
				<svws-ui-spacing />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, watch } from "vue";
	import type { LehrerPersonaldatenProps } from './SLehrerPersonaldatenProps';
	import type { Validator} from "@core";
	import { DeveloperNotificationException, ValidatorLehrerPersonalabschnittsdaten} from "@core";
	import { LehrerBeschaeftigungsart, LehrerEinsatzstatus, LehrerRechtsverhaeltnis, LehrerAnrechnungsgrund, LehrerMehrleistungsarten, LehrerMinderleistungsarten,
		BenutzerKompetenz} from "@core";
	import { CoreTypeSelectManager } from "../../../../../ui/src/ui/controls/select/selectManager/CoreTypeSelectManager";

	const props = defineProps<LehrerPersonaldatenProps>();

	const schuljahr = computed<number>(() => props.aktAbschnitt.schuljahr);

	watch(
		() => schuljahr.value,
		(newValue) => {
			rechtsverhaeltnisSelectManager.schuljahr = newValue;
			beschaeftigungsartSelectManager.schuljahr = newValue;
			einsatzstatusSelectManager.schuljahr = newValue;
		}
	);

	watch(
		() => props.schulform,
		(newValue) => {
			rechtsverhaeltnisSelectManager.schulformen = newValue;
			beschaeftigungsartSelectManager.schulformen = newValue;
			einsatzstatusSelectManager.schulformen = newValue;
		}
	);

	const hatUpdateKompetenz = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN));

	const personaldaten = () => props.lehrerListeManager().personalDaten();
	const personalabschnittsdaten = () => props.lehrerListeManager().getAbschnittBySchuljahresabschnittsId(props.aktAbschnitt.id);

	const rechtsverhaeltnisSelectManager = new CoreTypeSelectManager({ clazz: LehrerRechtsverhaeltnis.class, schuljahr: schuljahr.value, removable: false,
		schulformen: props.schulform, optionDisplayText: "text", selectionDisplayText: "text",
	});

	const beschaeftigungsartSelectManager = new CoreTypeSelectManager({ clazz: LehrerBeschaeftigungsart.class, schuljahr: schuljahr.value,
		schulformen: props.schulform, removable: false, optionDisplayText: "text", selectionDisplayText: "text",
	});

	const einsatzstatusSelectManager = new CoreTypeSelectManager({ clazz: LehrerEinsatzstatus.class, schuljahr: schuljahr.value, schulformen: props.schulform,
		removable: false, optionDisplayText: "text", selectionDisplayText: "text",
	});

	const rechtsverhaeltnis = computed<LehrerRechtsverhaeltnis | undefined>({
		get(): LehrerRechtsverhaeltnis | undefined {
			return LehrerRechtsverhaeltnis.values().find(r => r.daten(schuljahr.value)?.schluessel === personalabschnittsdaten()?.rechtsverhaeltnis);
		},
		set(val: LehrerRechtsverhaeltnis | undefined) {
			const daten = personalabschnittsdaten();
			if (daten !== null)
				void props.patchAbschnittsdaten({ rechtsverhaeltnis: val?.daten(schuljahr.value)?.schluessel }, daten.id);
		},
	});

	const beschaeftigungsart = computed<LehrerBeschaeftigungsart | undefined>({
		get(): LehrerBeschaeftigungsart | undefined {
			return LehrerBeschaeftigungsart.values().find(r => r.daten(schuljahr.value)?.schluessel === personalabschnittsdaten()?.beschaeftigungsart);
		},
		set(val: LehrerBeschaeftigungsart | undefined) {
			const daten = personalabschnittsdaten();
			if (daten !== null)
				void props.patchAbschnittsdaten({ beschaeftigungsart: val?.daten(schuljahr.value)?.schluessel }, daten.id);
		},
	});

	const einsatzstatus = computed<LehrerEinsatzstatus | undefined>({
		get(): LehrerEinsatzstatus | undefined {
			return LehrerEinsatzstatus.values().find(r => r.daten(schuljahr.value)?.schluessel === personalabschnittsdaten()?.einsatzstatus);
		},
		set(val: LehrerEinsatzstatus | undefined) {
			const daten = personalabschnittsdaten();
			if (daten !== null)
				void props.patchAbschnittsdaten({ einsatzstatus: val?.daten(schuljahr.value)?.schluessel }, daten.id);
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

	function validatePersonalabschnittDaten(validator: Validator, value: string | null): boolean {
		const geburtsdatum = props.lehrerListeManager().daten().geburtsdatum;
		props.lehrerListeManager().daten().geburtsdatum = value ?? "";
		const res = validator.run();
		props.lehrerListeManager().daten().geburtsdatum = geburtsdatum;
		return res;
	};

</script>
