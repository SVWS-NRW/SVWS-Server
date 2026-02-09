<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Identnummer" span="full" class="contentFocusField"
					:model-value="personaldaten().identNrTeil1"
					@change="patchIdentnummer"
					:valid="(v) => optionalInputIsValid(v, 10)"
					:readonly focus statistics :max-len="10" />
				<svws-ui-text-input placeholder="Seriennummer"
					:model-value="personaldaten().identNrTeil2SerNr"
					@change="patchSeriennummer"
					:valid="(v) => optionalInputIsValid(v, 5)"
					:readonly statistics :max-len="5" />
				<svws-ui-text-input placeholder="Vergütungsschlüssel"
					:model-value="personaldaten().lbvVerguetungsschluessel"
					@change="patchVerguetungsschluessel"
					:valid="(v) => optionalInputIsValid(v, 1)"
					:readonly :max-len="1" />
				<svws-ui-text-input placeholder="PA-Nummer"
					:model-value="personaldaten().personalaktennummer"
					@change="patchPersonalaktennummer"
					:valid="(v) => optionalInputIsValid(v, 20)"
					:readonly :max-len="20" />
				<svws-ui-text-input placeholder="LBV-Personalnummer"
					:model-value="personaldaten().lbvPersonalnummer"
					@change="patchLbvPersonalnummer"
					:valid="(v) => optionalInputIsValid(v, 15)"
					:readonly :max-len="15" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Zugangsdatum" type="date"
					:model-value="personaldaten().zugangsdatum"
					@change="zugangsdatum => patch({zugangsdatum})"
					:readonly />
				<svws-ui-text-input placeholder="Abgangsdatum" type="date"
					:model-value="personaldaten().abgangsdatum"
					@change="abgangsdatum => patch({abgangsdatum})"
					:readonly />
				<ui-select label="Zugangsgrund"
					v-model="zugangsgrund"
					:manager="zugangsgrundManager"
					:readonly searchable />
				<ui-select label="Abgangsgrund"
					v-model="abgangsgrund"
					:manager="abgangsgrundManager"
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
					v-model="beschaeftigungsart"
					:manager="beschaeftigungsartSelectManager"
					:removable="false" required :readonly statistics />
				<svws-ui-input-number placeholder="Pflichtstundensoll"
					:model-value="getPersonalabschnittsdaten()?.pflichtstundensoll ?? 0.0"
					@change="patchPflichtstundenSoll"
					:valid="pflichtstundenSollHasAMaximumOf2DecimalPlaces"
					:readonly statistics />
				<ui-select label="Einsatzstatus"
					v-model="einsatzstatus"
					:manager="einsatzstatusSelectManager"
					:readonly statistics
					:removable="false" required />
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
					:personalabschnittsdaten="getPersonalabschnittsdaten" :schuljahr :schulform :add-mehrleistung
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
	import type { LehrerBeschaeftigungsartKatalogEintrag, LehrerEinsatzstatusKatalogEintrag, JavaSet,
		LehrerZugangsgrundKatalogEintrag, LehrerAbgangsgrundKatalogEintrag, LehrerPersonaldaten } from "@core";
	import { LehrerZugangsgrund, LehrerAbgangsgrund, BenutzerKompetenz, HashSet, LehrerBeschaeftigungsart, LehrerEinsatzstatus, LehrerRechtsverhaeltnis } from "@core";
	import { CoreTypeSelectManager, SelectManager } from "@ui";
	import { optionalInputIsValid } from "~/util/validation/Validation";
	import { LehrerPersonalabschnittsdatenModelProxy } from "./LehrerPersonalabschnittsdatenModelProxy";

	const props = defineProps<LehrerPersonaldatenProps>();
	const personaldaten = () => props.lehrerListeManager().personalDaten();
	const getPersonalabschnittsdaten = () => props.lehrerListeManager().getAbschnittBySchuljahresabschnittsId(props.aktAbschnitt.id);
	const idAbschnittsdaten = computed<number | undefined>(() => getPersonalabschnittsdaten()?.id);
	const schuljahr = computed<number>(() => props.aktAbschnitt.schuljahr);
	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN));
	const eigeneSchulnummer = computed<string>(() => `${props.validatorKontext().getSchulnummer()}`);

	const zugangsgrund = computed<LehrerZugangsgrundKatalogEintrag | null>({
		get: () => {
			const wert = LehrerZugangsgrund.data().getWertByKuerzel(personaldaten().zugangsgrund ?? '');
			if (wert === null) {
				return null;
			}
			return LehrerZugangsgrund.data().getEintragBySchuljahrUndWert(schuljahr.value, wert);
		},
		set: (value: LehrerZugangsgrundKatalogEintrag | null) => void patchZugangsgrund(value?.kuerzel ?? null),
	});

	const abgangsgrund = computed<LehrerAbgangsgrundKatalogEintrag | null>({
		get: () => {
			const wert = LehrerAbgangsgrund.data().getWertByKuerzel(personaldaten().abgangsgrund ?? '');
			if (wert === null) {
				return null;
			}
			return LehrerAbgangsgrund.data().getEintragBySchuljahrUndWert(schuljahr.value, wert);
		},
		set: (value: LehrerAbgangsgrundKatalogEintrag | null) => void patchAbgangsgrund(value?.kuerzel ?? null),
	});

	const personalabschnittsdaten = () => props.lehrerListeManager().getAbschnittBySchuljahresabschnittsId(props.aktAbschnitt.id);

	async function patchMethod(data: Partial<LehrerPersonaldaten>): Promise<boolean> {
		const id = personalabschnittsdaten()?.id ?? null;
		if (id !== null) {
			await props.patchAbschnittsdaten(data, id);
		}
		return true;
	}
	const personalabschnittsdatenProxy = new LehrerPersonalabschnittsdatenModelProxy(personalabschnittsdaten, () => props.validatorKontext(), () => props.lehrerListeManager(), patchMethod);

	const beschaeftigungsart = computed<LehrerBeschaeftigungsartKatalogEintrag | null>({
		get: () => LehrerBeschaeftigungsart.data().getEintragBySchuljahrUndSchluessel(schuljahr.value, getPersonalabschnittsdaten()?.beschaeftigungsart ?? ''),
		set: (v: LehrerBeschaeftigungsartKatalogEintrag | null) => void patchBeschaeftigungsart(v?.schluessel ?? null),
	});

	const einsatzstatus = computed<LehrerEinsatzstatusKatalogEintrag | null>({
		get: () => LehrerEinsatzstatus.data().getEintragBySchuljahrUndSchluessel(schuljahr.value, getPersonalabschnittsdaten()?.einsatzstatus ?? ''),
		set: (v: LehrerEinsatzstatusKatalogEintrag | null) => void patchEinsatzstatus(v?.schluessel ?? null),
	});

	const stammschulnummer = computed<string | null | undefined>({
		get(): string | null | undefined {
			return getPersonalabschnittsdaten()?.stammschulnummer ?? null;
		},
		set(val: string | null | undefined) {
			// Bugfix: Wenn dieser Check auf undefined nicht vorhanden ist, dann kommt es zu einem Fehler, wenn die Schulnummer nicht
			//         im Katalog enthalten ist und zu einem anderen Lehrer gewechselt wird
			if (val === undefined) {
				return;
			}
			const daten = getPersonalabschnittsdaten();
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
		const daten = getPersonalabschnittsdaten();
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

	// --- patch ---

	async function patchIdentnummer(identNrTeil1: string | null) {
		if (optionalInputIsValid(identNrTeil1, 10)) {
			void props.patch({ identNrTeil1 });
		}
	}

	async function patchSeriennummer(identNrTeil2SerNr: string | null) {
		if (optionalInputIsValid(identNrTeil2SerNr, 5)) {
			void props.patch({ identNrTeil2SerNr });
		}
	}

	async function patchVerguetungsschluessel(lbvVerguetungsschluessel: string | null) {
		if (optionalInputIsValid(lbvVerguetungsschluessel, 1)) {
			void props.patch({ lbvVerguetungsschluessel });
		}
	}

	async function patchPersonalaktennummer(personalaktennummer: string | null) {
		if (optionalInputIsValid(personalaktennummer, 20)) {
			void props.patch({ personalaktennummer });
		}
	}

	async function patchLbvPersonalnummer(lbvPersonalnummer: string | null) {
		if (optionalInputIsValid(lbvPersonalnummer, 15)) {
			void props.patch({ lbvPersonalnummer });
		}
	}

	async function patchZugangsgrund(zugangsgrund: string | null) {
		if (optionalInputIsValid(zugangsgrund, 10)) {
			void props.patch({ zugangsgrund });
		}
	}

	async function patchAbgangsgrund(abgangsgrund: string | null) {
		if (optionalInputIsValid(abgangsgrund, 10)) {
			void props.patch({ abgangsgrund });
		}
	}

	async function patchPflichtstundenSoll(pflichtstundensoll: number | null) {
		if (idAbschnittsdaten.value !== undefined
			&& pflichtstundenSollHasAMaximumOf2DecimalPlaces(pflichtstundensoll)) {
			await props.patchAbschnittsdaten({ pflichtstundensoll }, idAbschnittsdaten.value);
		}
	}

	async function patchRechtsverhaeltnis(schluessel: string | null) {
		if (idAbschnittsdaten.value !== undefined) {
			void props.patchAbschnittsdaten({ rechtsverhaeltnis: schluessel }, idAbschnittsdaten.value);
		}
	}

	async function patchBeschaeftigungsart(schluessel: string | null) {
		if (idAbschnittsdaten.value !== undefined) {
			void props.patchAbschnittsdaten({ beschaeftigungsart: schluessel }, idAbschnittsdaten.value);
		}
	}

	async function patchEinsatzstatus(schluessel: string | null) {
		if (idAbschnittsdaten.value !== undefined) {
			void props.patchAbschnittsdaten({ einsatzstatus: schluessel }, idAbschnittsdaten.value);
		}
	}

	// --- validate ---

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

	// --- util ---

	function getSchulnummerText(schulnummer: string): string {
		const eintrag = props.mapSchulen().get(schulnummer);

		const schulePrefix = (eigeneSchulnummer.value === schulnummer) ? 'Eigene Schule - ' : '';
		const kuerzel = eintrag ? eintrag.kuerzel + ' - ' : '';

		return `${schulePrefix}${kuerzel}${schulnummer}`;
	}

</script>
