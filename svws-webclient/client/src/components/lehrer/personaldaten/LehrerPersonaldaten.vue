<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Identnummer" span="full" class="contentFocusField"
					v-model="personaldatenProxy.proxy.identNrTeil1" @change="personaldatenProxy.patch"
					:readonly focus statistics :max-len="10" />
				<svws-ui-text-input placeholder="Seriennummer"
					v-model="personaldatenProxy.proxy.identNrTeil2SerNr" @change="personaldatenProxy.patch"
					:readonly statistics :max-len="5" />
				<svws-ui-text-input placeholder="Vergütungsschlüssel"
					v-model="personaldatenProxy.proxy.lbvVerguetungsschluessel" @change="personaldatenProxy.patch"
					:readonly :max-len="1" />
				<svws-ui-text-input placeholder="PA-Nummer"
					v-model="personaldatenProxy.proxy.personalaktennummer" @change="personaldatenProxy.patch"
					:readonly :max-len="20" />
				<svws-ui-text-input placeholder="LBV-Personalnummer"
					v-model="personaldatenProxy.proxy.lbvPersonalnummer" @change="personaldatenProxy.patch"
					:readonly :max-len="15" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Zugangsdatum" type="date"
					v-model="personaldatenProxy.proxy.zugangsdatum" @change="personaldatenProxy.patch"
					:readonly />
				<svws-ui-text-input placeholder="Abgangsdatum" type="date"
					v-model="personaldatenProxy.proxy.abgangsdatum" @change="personaldatenProxy.patch"
					:readonly />
				<ui-select label="Zugangsgrund" v-model="personaldatenProxy.zugangsgrund.value" :manager="zugangsgrundManager"
					:readonly searchable />
				<ui-select label="Abgangsgrund" v-model="personaldatenProxy.abgangsgrund.value" :manager="abgangsgrundManager"
					:readonly searchable />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Beschäftigungsdaten">
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Rechtsverhältnis" class="contentFocusField"
					v-model="personalabschnittsdatenProxy.rechtsverhaeltnis.value"
					:manager="rechtsverhaeltnisSelectManager"
					:validation="() => personalabschnittsdatenProxy.getFehler('rechtsverhaeltnis')"
					:removable="false" :readonly required statistics />
				<ui-select label="Beschäftigungsart"
					v-model="personalabschnittsdatenProxy.beschaeftigungsart.value"
					:manager="beschaeftigungsartSelectManager"
					:validation="() => personalabschnittsdatenProxy.getFehler('beschaeftigungsart')"
					:removable="false" required :readonly statistics />
				<svws-ui-input-number placeholder="Pflichtstundensoll"
					v-model="personalabschnittsdatenProxy.proxy.pflichtstundensoll" @change="personaldatenProxy.patch"
					:readonly statistics />
				<ui-select label="Einsatzstatus"
					v-model="personalabschnittsdatenProxy.einsatzstatus.value"
					:manager="einsatzstatusSelectManager"
					:validation="() => personalabschnittsdatenProxy.getFehler('einsatzstatus')"
					:readonly statistics :removable="false" required />
				<ui-select label="Stammschule"
					v-model="stammschulnummer"
					:manager="stammschuleSelectManager"
					:removable="true" :readonly required statistics />
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
				<s-lehrer-personaldaten-anrechnungen :hat-update-kompetenz="!readonly"
					:personalabschnittsdaten :schuljahr :schulform :add-mehrleistung
					:patch-mehrleistung :remove-mehrleistung
					:add-minderleistung :patch-minderleistung :remove-minderleistung :add-anrechnung :patch-anrechnung
					:remove-anrechnung />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { LehrerPersonaldatenProps } from './LehrerPersonaldatenProps';
	import type { JavaSet, LehrerPersonalabschnittsdaten, LehrerPersonaldaten } from "@core";
	import { LehrerZugangsgrund, LehrerAbgangsgrund, BenutzerKompetenz, HashSet, LehrerBeschaeftigungsart, LehrerEinsatzstatus, LehrerRechtsverhaeltnis } from "@core";
	import { CoreTypeSelectManager, SelectManager } from "@ui";
	import { LehrerPersonalabschnittsdatenModelProxy } from "./LehrerPersonalabschnittsdatenModelProxy";
	import { LehrerPersonaldatenModelProxy } from "./LehrerPersonaldatenModelProxy";

	const props = defineProps<LehrerPersonaldatenProps>();
	const personaldaten = () => props.lehrerListeManager().personalDaten();
	const schuljahr = computed<number>(() => props.aktAbschnitt.schuljahr);
	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN));
	const eigeneSchulnummer = computed<string>(() => `${props.validatorKontext().getSchulnummer()}`);
	const personalabschnittsdaten = () => props.lehrerListeManager().getAbschnittBySchuljahresabschnittsId(props.aktAbschnitt.id);

	async function patchMethodLehrerPersonalabschnittsdaten(data: Partial<LehrerPersonalabschnittsdaten>): Promise<boolean> {
		const id = personalabschnittsdaten()?.id ?? null;
		if (id !== null) {
			await props.patchAbschnittsdaten(data, id);
		}
		return true;
	}
	async function patchMethodLehrerPersonaldaten(data: Partial<LehrerPersonaldaten>): Promise<boolean> {
		await props.patch(data);
		return true;
	}
	const personalabschnittsdatenProxy = new LehrerPersonalabschnittsdatenModelProxy(personalabschnittsdaten, () => props.validatorKontext(), () => props.lehrerListeManager(), patchMethodLehrerPersonalabschnittsdaten);
	const personaldatenProxy = new LehrerPersonaldatenModelProxy(personaldaten, () => props.validatorKontext(), () => props.lehrerListeManager(), patchMethodLehrerPersonaldaten);

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

	const zugangsgrundManager = new CoreTypeSelectManager({
		clazz: LehrerZugangsgrund.class,
		schuljahr: schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const abgangsgrundManager = new CoreTypeSelectManager({
		clazz: LehrerAbgangsgrund.class,
		schuljahr: schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const rechtsverhaeltnisSelectManager = new CoreTypeSelectManager({
		clazz: LehrerRechtsverhaeltnis.class,
		schuljahr: schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const beschaeftigungsartSelectManager = new CoreTypeSelectManager({
		clazz: LehrerBeschaeftigungsart.class,
		schuljahr: schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const einsatzstatusSelectManager = new CoreTypeSelectManager({
		clazz: LehrerEinsatzstatus.class,
		schuljahr: schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const stammschuleSelectManager = new SelectManager({
		options: moeglicheStammschulnummern,
		selectionDisplayText: getSchulnummerText,
		optionDisplayText: getSchulnummerText,
	});
	// --- util ---

	function getSchulnummerText(schulnummer: string): string {
		const eintrag = props.mapSchulen().get(schulnummer);

		const schulePrefix = (eigeneSchulnummer.value === schulnummer) ? 'Eigene Schule - ' : '';
		const kuerzel = eintrag ? eintrag.kuerzel + ' - ' : '';

		return `${schulePrefix}${kuerzel}${schulnummer}`;
	}

</script>
