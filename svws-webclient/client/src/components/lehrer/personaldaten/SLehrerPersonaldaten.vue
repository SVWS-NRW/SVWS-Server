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
				<svws-ui-input-number placeholder="Pflichtstundensoll" :readonly statistics :valid="pflichtstundenSollHasAMaximumOf2DecimalPlaces"
					:model-value="personalabschnittsdaten()?.pflichtstundensoll ?? 0.0" @change="patchPflichtstundenSoll" />
				<ui-select label="Einsatzstatus" :readonly v-model="einsatzstatus" statistics :manager="einsatzstatusSelectManager"
					class="contentFocusField" :removable="false" required />
				<ui-select label="Stammschule" :readonly v-model="stammschulnummer" statistics :manager="stammschuleSelectManager"
					class="contentFocusField" :removable="true" required />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Lehrämter">
			<svws-ui-input-wrapper>
				<s-lehrer-personaldaten-lehraemter :hat-update-kompetenz="!readonly" :schuljahr :lehrer-liste-manager
					:patch-lehramt :add-lehramt :remove-lehraemter
					:patch-lehrbefaehigung :add-lehrbefaehigung :remove-lehrbefaehigungen
					:patch-fachrichtung :add-fachrichtung :remove-fachrichtungen />
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

	import { computed } from "vue";
	import type { LehrerPersonaldatenProps } from './SLehrerPersonaldatenProps';
	import type { LehrerBeschaeftigungsartKatalogEintrag, LehrerEinsatzstatusKatalogEintrag, LehrerRechtsverhaeltnisKatalogEintrag,
		JavaSet, Validator } from "@core";
	import { BenutzerKompetenz, HashSet, ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten,
		LehrerBeschaeftigungsart, LehrerEinsatzstatus, LehrerRechtsverhaeltnis, LehrerPersonalabschnittsdaten } from "@core";
	import { CoreTypeSelectManager, SelectManager } from "@ui";

	const props = defineProps<LehrerPersonaldatenProps>();

	const schuljahr = computed<number>(() => props.aktAbschnitt.schuljahr);

	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN));

	const personaldaten = () => props.lehrerListeManager().personalDaten();
	const personalabschnittsdaten = () => props.lehrerListeManager().getAbschnittBySchuljahresabschnittsId(props.aktAbschnitt.id);


	const rechtsverhaeltnisSelectManager = new CoreTypeSelectManager({ clazz: LehrerRechtsverhaeltnis.class, schuljahr: schuljahr,
		schulformen: props.schulform, optionDisplayText: "text", selectionDisplayText: "text",
	});

	const rechtsverhaeltnis = computed<LehrerRechtsverhaeltnisKatalogEintrag | undefined>({
		get(): LehrerRechtsverhaeltnisKatalogEintrag | undefined {
			return LehrerRechtsverhaeltnis.values().map(r => r.daten(schuljahr.value) ?? undefined)
				.find(d => d?.schluessel === personalabschnittsdaten()?.rechtsverhaeltnis);
		},
		set(val: LehrerRechtsverhaeltnisKatalogEintrag | undefined) {
			const daten = personalabschnittsdaten();
			if (daten !== null) {
				void props.patchAbschnittsdaten({ rechtsverhaeltnis: val?.schluessel }, daten.id);
			}
		},
	});


	const beschaeftigungsartSelectManager = new CoreTypeSelectManager({ clazz: LehrerBeschaeftigungsart.class, schuljahr: schuljahr,
		schulformen: props.schulform, optionDisplayText: "text", selectionDisplayText: "text",
	});

	const beschaeftigungsart = computed<LehrerBeschaeftigungsartKatalogEintrag | undefined>({
		get(): LehrerBeschaeftigungsartKatalogEintrag | undefined {
			return LehrerBeschaeftigungsart.values().map(r => r.daten(schuljahr.value) || undefined)
				.find(d => d?.schluessel === personalabschnittsdaten()?.beschaeftigungsart);
		},
		set(val: LehrerBeschaeftigungsartKatalogEintrag | undefined) {
			const daten = personalabschnittsdaten();
			if (daten !== null) {
				void props.patchAbschnittsdaten({ beschaeftigungsart: val?.schluessel }, daten.id);
			}
		},
	});


	const einsatzstatusSelectManager = new CoreTypeSelectManager({ clazz: LehrerEinsatzstatus.class, schuljahr: schuljahr, schulformen: props.schulform,
		optionDisplayText: "text", selectionDisplayText: "text",
	});

	const einsatzstatus = computed<LehrerEinsatzstatusKatalogEintrag | undefined>({
		get(): LehrerEinsatzstatusKatalogEintrag | undefined {
			return LehrerEinsatzstatus.values().map(r => r.daten(schuljahr.value) || undefined)
				.find(d => d?.schluessel === personalabschnittsdaten()?.einsatzstatus);
		},
		set(val: LehrerEinsatzstatusKatalogEintrag | undefined) {
			const daten = personalabschnittsdaten();
			if (daten !== null) {
				void props.patchAbschnittsdaten({ einsatzstatus: val?.schluessel }, daten.id);
			}
		},
	});


	const eigeneSchulnummer = computed<string>(() => `${props.validatorKontext().getSchulnummer()}`);

	const moeglicheStammschulnummern = computed<JavaSet<string>>(() => {
		// Füge zunächst alle Schulnummern mit eingetragenen Kürzeln im Schul-Katalog hinzu
		const result = new HashSet<string>();
		for (const schule of props.mapSchulen().values()) {
			if (schule.schulnummerStatistik !== null) {
				result.add(schule.schulnummerStatistik);
			}
		}
		// Ergänze die eigene Schule, sofern diese nicht bereits im Katalog enthalten ist
		result.add(eigeneSchulnummer.value);
		// Ergänze ggf. noch den Eintrag aus der Datenbank
		const daten = personalabschnittsdaten();
		if ((daten === null) || (daten.stammschulnummer === null)) {
			return result;
		}
		result.add(daten.stammschulnummer);
		return result;
	});

	function getSchulnummerText(schulnummer: string): string {
		const eintrag = props.mapSchulen().get(schulnummer);
		const istEigene = (eigeneSchulnummer.value === schulnummer);
		if (istEigene && (eintrag !== undefined)) {
			return `Eigene Schule - ${eintrag.kuerzel} - ${schulnummer}`;
		}
		if (istEigene) {
			return `Eigene Schule - ${schulnummer}`;
		}
		if (eintrag !== undefined) {
			return `${eintrag.kuerzel} - ${schulnummer}`;
		}
		return schulnummer;
	}

	const stammschuleSelectManager = new SelectManager({ options: moeglicheStammschulnummern.value, selectionDisplayText: getSchulnummerText,
		optionDisplayText: getSchulnummerText });

	const stammschulnummer = computed<string | null | undefined>({
		get(): string | null | undefined {
			return personalabschnittsdaten()?.stammschulnummer ?? null;
		},
		set(val: string | null | undefined) {
			// Bugfix: Wenn dieser Check auf undefined nicht vorhanden ist, dann kommt es zu einem Fehler, wenn die Schulnummer nicht
			//         im Katalog enthalten ist und zu einem anderen Lehrer gewechselt wird
			if (val === undefined) {
				return;
			}
			const daten = personalabschnittsdaten();
			if (daten !== null) {
				void props.patchAbschnittsdaten({ stammschulnummer: val }, daten.id);
			}
		},
	});


	const validatorPersonalabschnittsDaten = computed<ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten>(() => {
		let daten = personalabschnittsdaten();
		if (daten === null) {
			// Erstelle Pseudo-Daten, die für die Validierung genutzt werden
			daten = new LehrerPersonalabschnittsdaten();
			daten.id = -1;
			daten.idLehrer = props.lehrerListeManager().auswahl().id;
			daten.idSchuljahresabschnitt = props.aktAbschnitt.id;
		}
		return new ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten(daten, props.lehrerListeManager().daten(), props.validatorKontext());
	});

	function validatePersonalabschnittDaten(validator: Validator): boolean {
		return validator.run();
	};

	async function patchPflichtstundenSoll(input: number | null) {
		if (pflichtstundenSollHasAMaximumOf2DecimalPlaces(input)) {
			await props.patchAbschnittsdaten({ pflichtstundensoll: input }, personalabschnittsdaten()?.id ?? -1);
		}
	}

	function pflichtstundenSollHasAMaximumOf2DecimalPlaces(pflichtstundenSoll: number | null) {
		if (pflichtstundenSoll === null) {
			return true;
		}
		const pflichtstundenSollParts = String(pflichtstundenSoll).split('.');
		if ((pflichtstundenSollParts.length === 2) && (pflichtstundenSollParts[1].length > 2)) {
			return false;
		}
		return true;
	}

</script>
