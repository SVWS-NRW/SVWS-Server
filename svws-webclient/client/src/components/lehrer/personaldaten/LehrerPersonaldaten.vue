<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Identnummer"
					span="full" class="contentFocusField"
					v-model="personaldatenModelProxy.proxy.identNrTeil1"
					:validation="() => personaldatenModelProxy.getFehler('identNrTeil1')"
					@change="personaldatenModelProxy.patch"
					:readonly focus statistics :max-len="10" />
				<svws-ui-text-input placeholder="Seriennummer"
					v-model="personaldatenModelProxy.proxy.identNrTeil2SerNr"
					:validation="() => personaldatenModelProxy.getFehler('identNrTeil2SerNr')"
					@change="personaldatenModelProxy.patch"
					:readonly statistics :max-len="5" />
				<svws-ui-text-input placeholder="Vergütungsschlüssel"
					v-model="personaldatenModelProxy.proxy.lbvVerguetungsschluessel"
					:validation="() => personaldatenModelProxy.getFehler('lbvVerguetungsschluessel')"
					@change="personaldatenModelProxy.patch"
					:readonly :max-len="1" />
				<svws-ui-text-input placeholder="PA-Nummer"
					v-model="personaldatenModelProxy.proxy.personalaktennummer"
					:validation="() => personaldatenModelProxy.getFehler('personalaktennummer')"
					@change="personaldatenModelProxy.patch"
					:readonly :max-len="20" />
				<svws-ui-text-input placeholder="LBV-Personalnummer"
					v-model="personaldatenModelProxy.proxy.lbvPersonalnummer"
					:validation="() => personaldatenModelProxy.getFehler('lbvPersonalnummer')"
					@change="personaldatenModelProxy.patch"
					:readonly :max-len="15" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Zugangsdatum"
					type="date"
					v-model="personaldatenModelProxy.proxy.zugangsdatum"
					@change="personaldatenModelProxy.patch"
					:readonly />
				<svws-ui-text-input placeholder="Abgangsdatum"
					type="date"
					v-model="personaldatenModelProxy.proxy.abgangsdatum"
					@change="personaldatenModelProxy.patch"
					:readonly />
				<ui-select label="Zugangsgrund"
					v-model="personaldatenModelProxy.zugangsgrund.value"
					:manager="zugangsgrundManager"
					:readonly />
				<ui-select label="Abgangsgrund"
					v-model="personaldatenModelProxy.abgangsgrund.value"
					:manager="abgangsgrundManager"
					:readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Beschäftigungsdaten">
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Rechtsverhältnis"
					class="contentFocusField"
					v-model="personalabschnittsdatenModelProxy.rechtsverhaeltnis.value"
					:validation="() => personalabschnittsdatenModelProxy.getFehler('idRechtsverhaeltnis')"
					:manager="rechtsverhaeltnisSelectManager"
					:removable="false" :readonly required statistics />
				<ui-select label="Beschäftigungsart"
					v-model="personalabschnittsdatenModelProxy.beschaeftigungsart.value"
					:validation="() => personalabschnittsdatenModelProxy.getFehler('idBeschaeftigungsart')"
					:manager="beschaeftigungsartSelectManager"
					:removable="false" required :readonly statistics />
				<svws-ui-input-number placeholder="Pflichtstundensoll"
					v-model="personalabschnittsdatenModelProxy.proxy.pflichtstundensoll"
					@change="personalabschnittsdatenModelProxy.patch"
					:decimal-places="1" :steps="0.5" :readonly statistics />
				<ui-select label="Einsatzstatus"
					v-model="personalabschnittsdatenModelProxy.einsatzstatus.value"
					:validation="() => personalabschnittsdatenModelProxy.getFehler('idEinsatzstatus')"
					:manager="einsatzstatusSelectManager"
					:readonly statistics :removable="false" required />
				<ui-select label="Stammschule"
					v-model="personalabschnittsdatenModelProxy.proxy.stammschulnummer"
					:validation="() => personalabschnittsdatenModelProxy.getFehler('stammschulnummer')"
					:manager="stammschuleSelectManager"
					:removable="true" :readonly required statistics />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Lehrämter">
			<svws-ui-input-wrapper>
				<lehrer-personaldaten-lehraemter :personaldaten-model-proxy="() => personaldatenModelProxy" :hat-update-kompetenz="!readonly"
					:lehrer-liste-manager :patch-lehramt :add-lehramt :remove-lehraemter :patch-lehrbefaehigung :add-lehrbefaehigung :remove-lehrbefaehigungen
					:patch-fachrichtung :add-fachrichtung :remove-fachrichtungen />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card v-if="serverState.hasDev" title="Unterrichtsfächer">
			<svws-ui-input-wrapper>
				<lehrer-personaldaten-unterrichtsfaecher :hat-update-kompetenz="!readonly"
					:lehrer-unterrichtsfaecher :map-faecher :id-lehrer="() => personaldatenModelProxy.proxy.id"
					:add-lehrer-unterrichtsfach :patch-lehrer-unterrichtsfach :remove-lehrer-unterrichtsfach />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Mehr- und Minderleistung, Anrechnungsstunden">
			<svws-ui-input-wrapper>
				<lehrer-personaldaten-anrechnungen :personalabschnittsdaten-model-proxy="() => personalabschnittsdatenModelProxy"
					:hat-update-kompetenz="!readonly" :add-mehrleistung :patch-mehrleistung :remove-mehrleistung :add-minderleistung :patch-minderleistung
					:remove-minderleistung :add-anrechnung :patch-anrechnungen :remove-anrechnung />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { LehrerPersonaldatenProps } from './LehrerPersonaldatenProps';
	import type { JavaSet, LehrerPersonalabschnittsdaten } from "@core";
	import { LehrerZugangsgrund, LehrerAbgangsgrund, BenutzerKompetenz, HashSet, LehrerBeschaeftigungsart, LehrerEinsatzstatus, LehrerRechtsverhaeltnis } from "@core";
	import { CoreTypeSelectManager, SelectManager, useAbschnittState, useBenutzerState, useSchuleState, useServerState } from "@ui";
	import { LehrerPersonalabschnittsdatenModelProxy } from "~/components/lehrer/personaldaten/modelproxy/LehrerPersonalabschnittsdatenModelProxy";
	import { LehrerPersonaldatenModelProxy } from "./modelproxy/LehrerPersonaldatenModelProxy";

	const props = defineProps<LehrerPersonaldatenProps>();
	const benutzerState = useBenutzerState();
	const serverState = useServerState();
	const schuleState = useSchuleState();
	const abschnittState = useAbschnittState();

	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN));
	const eigeneSchulnummer = computed<string>(() => `${schuleState.validatorKontext.getSchulnummer()}`);

	async function patchMethodLehrerPersonalabschnittsdaten(data: Partial<LehrerPersonalabschnittsdaten>): Promise<boolean> {
		const id = props.lehrerListeManager().getAbschnittBySchuljahresabschnittsId(abschnittState.auswahl.id)?.id ?? null;
		if (id !== null) {
			await props.patchAbschnittsdaten(data, id);
		}
		return true;
	}
	const personalabschnittsdatenModelProxy = new LehrerPersonalabschnittsdatenModelProxy(() => props.lehrerListeManager().getAbschnittBySchuljahresabschnittsId(abschnittState.auswahl.id), () => schuleState.validatorKontext, () => props.lehrerListeManager(), patchMethodLehrerPersonalabschnittsdaten);
	const personaldatenModelProxy = computed(() =>
		new LehrerPersonaldatenModelProxy(
			() => props.lehrerListeManager().personalDaten(),
			() => schuleState.validatorKontext,
			() => props.lehrerListeManager(),
			props.patchPersonaldaten
		)
	);
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
		const daten = props.lehrerListeManager().getAbschnittBySchuljahresabschnittsId(abschnittState.auswahl.id);
		if ((daten === null) || (daten.stammschulnummer === null)) {
			return result;
		}
		result.add(daten.stammschulnummer);
		return result;
	});

	const zugangsgrundManager = new CoreTypeSelectManager({
		clazz: LehrerZugangsgrund.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const abgangsgrundManager = new CoreTypeSelectManager({
		clazz: LehrerAbgangsgrund.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const rechtsverhaeltnisSelectManager = new CoreTypeSelectManager({
		clazz: LehrerRechtsverhaeltnis.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const beschaeftigungsartSelectManager = new CoreTypeSelectManager({
		clazz: LehrerBeschaeftigungsart.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const einsatzstatusSelectManager = new CoreTypeSelectManager({
		clazz: LehrerEinsatzstatus.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		schulformen: schuleState.schulform,
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
