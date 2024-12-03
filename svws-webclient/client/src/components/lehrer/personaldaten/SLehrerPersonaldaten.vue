<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Identnummer" :disabled="!hatUpdateKompetenz" :model-value="personaldaten().identNrTeil1"
					@change="identNrTeil1 => patch({identNrTeil1})" type="text" span="full" focus />
				<svws-ui-text-input placeholder="Seriennummer" :disabled="!hatUpdateKompetenz" :model-value="personaldaten().identNrTeil2SerNr"
					@change="identNrTeil2SerNr => patch({identNrTeil2SerNr})" type="text" />
				<svws-ui-text-input placeholder="Vergütungsschlüssel" :disabled="!hatUpdateKompetenz" :model-value="personaldaten().lbvVerguetungsschluessel"
					@change="lbvVerguetungsschluessel => patch({lbvVerguetungsschluessel})" type="text" />
				<svws-ui-text-input placeholder="PA-Nummer" :disabled="!hatUpdateKompetenz" :model-value="personaldaten().personalaktennummer"
					@change="personalaktennummer => patch({personalaktennummer})" type="text" />
				<svws-ui-text-input placeholder="LBV-Personalnummer" :disabled="!hatUpdateKompetenz" :model-value="personaldaten().lbvPersonalnummer"
					@change="lbvPersonalnummer => patch({lbvPersonalnummer})" type="text" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Zugangsdatum" :disabled="!hatUpdateKompetenz" :model-value="personaldaten().zugangsdatum"
					@change="zugangsdatum => patch({zugangsdatum})" type="date" />
				<svws-ui-text-input placeholder="Abgangsdatum" :disabled="!hatUpdateKompetenz" :model-value="personaldaten().abgangsdatum"
					@change="abgangsdatum => patch({abgangsdatum})" type="date" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Beschäftigungsdaten">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Rechtsverhältnis" :disabled="!hatUpdateKompetenz" v-model="rechtsverhaeltnis" :items="LehrerRechtsverhaeltnis.values()"
					:item-text="(i: LehrerRechtsverhaeltnis) => i.daten(schuljahr)?.text ?? '—'" />
				<svws-ui-select title="Beschäftigungsart" :disabled="!hatUpdateKompetenz" v-model="beschaeftigungsart" :items="LehrerBeschaeftigungsart.values()"
					:item-text="(i: LehrerBeschaeftigungsart) => i.daten(schuljahr)?.text ?? '—'" />
				<svws-ui-input-number placeholder="Pflichtstundensoll" :disabled="!hatUpdateKompetenz" :model-value="personalabschnittsdaten()?.pflichtstundensoll ?? 0.0"
					@change="pflichtstundensoll => patchAbschnittsdaten({ pflichtstundensoll: pflichtstundensoll }, personalabschnittsdaten()?.id ?? -1)" />
				<svws-ui-select title="Einsatzstatus" :disabled="!hatUpdateKompetenz" v-model="einsatzstatus" :items="LehrerEinsatzstatus.values()"
					:item-text="(i: LehrerEinsatzstatus) => i.daten(schuljahr)?.text ?? '—'" />
				<svws-ui-text-input placeholder="Stammschule" :disabled="!hatUpdateKompetenz" :model-value="personalabschnittsdaten()?.stammschulnummer"
					@change="stammschulnummer => patchAbschnittsdaten({ stammschulnummer }, personalabschnittsdaten()?.id ?? -1)" type="text" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Lehrämter">
			<svws-ui-input-wrapper>
				<s-lehrer-personaldaten-lehraemter :hat-update-kompetenz :lehrer-liste-manager :patch-lehramt-anerkennung :add-lehramt :remove-lehraemter :schuljahr />
				<s-lehrer-personaldaten-lehrbefaehigungen :hat-update-kompetenz :lehrer-liste-manager :patch-lehrbefaehigung-anerkennung :add-lehrbefaehigung :remove-lehrbefaehigungen :schuljahr />
				<s-lehrer-personaldaten-fachrichtungen :hat-update-kompetenz :lehrer-liste-manager :patch-fachrichtung-anerkennung :add-fachrichtung :remove-fachrichtungen="removeFachrichtungen" :schuljahr />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Mehr- und Minderleistung, Anrechnungsstunden">
			<svws-ui-input-wrapper>
				<svws-ui-select title="Mehrleistung" :disabled="!hatUpdateKompetenz" v-model="mehrleistungsgrund" :items="LehrerMehrleistungsarten.values()"
					:item-text="i => i.daten(schuljahr)?.text ?? '—'" />
				<svws-ui-select title="Minderleistung" :disabled="!hatUpdateKompetenz" v-model="minderleistungsgrund" :items="LehrerMinderleistungsarten.values()"
					:item-text="i => i.daten(schuljahr)?.text ?? '—'" />
				<svws-ui-input-number placeholder="Stundensumme" :disabled="!hatUpdateKompetenz" :model-value="personalabschnittsdaten()?.pflichtstundensoll ?? 0.0"
					@change="pflichtstundensoll => patchAbschnittsdaten({ pflichtstundensoll: pflichtstundensoll }, personalabschnittsdaten()?.id ?? -1)" />
				<svws-ui-select title="Nicht unterrichtliche Tätigkeiten" :disabled="!hatUpdateKompetenz" v-model="anrechnungsgrund" :items="LehrerAnrechnungsgrund.values()"
					:item-text="i => i.daten(schuljahr)?.text ?? '—'" />
				<svws-ui-spacing />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { LehrerPersonaldatenProps } from './SLehrerPersonaldatenProps';
	import { LehrerBeschaeftigungsart, LehrerEinsatzstatus, LehrerRechtsverhaeltnis, LehrerAnrechnungsgrund, LehrerMehrleistungsarten, LehrerMinderleistungsarten, BenutzerKompetenz} from "@core";

	const props = defineProps<LehrerPersonaldatenProps>();

	const schuljahr = computed<number>(() => props.aktAbschnitt.schuljahr);

	const hatUpdateKompetenz = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN));

	const personaldaten = () => props.lehrerListeManager().personalDaten();
	const personalabschnittsdaten = () => props.lehrerListeManager().getAbschnittBySchuljahresabschnittsId(props.aktAbschnitt.id);

	const rechtsverhaeltnis = computed<LehrerRechtsverhaeltnis | undefined>({
		get(): LehrerRechtsverhaeltnis | undefined {
			return LehrerRechtsverhaeltnis.values().find(r => r.daten(schuljahr.value)?.kuerzel === personalabschnittsdaten()?.rechtsverhaeltnis);
		},
		set(val: LehrerRechtsverhaeltnis | undefined) {
			const daten = personalabschnittsdaten();
			if (daten !== null)
				void props.patchAbschnittsdaten({ rechtsverhaeltnis: val?.daten(schuljahr.value)?.kuerzel }, daten.id);
		},
	});

	const beschaeftigungsart = computed<LehrerBeschaeftigungsart | undefined>({
		get(): LehrerBeschaeftigungsart | undefined {
			return LehrerBeschaeftigungsart.values().find(r => r.daten(schuljahr.value)?.kuerzel === personalabschnittsdaten()?.beschaeftigungsart);
		},
		set(val: LehrerBeschaeftigungsart | undefined) {
			const daten = personalabschnittsdaten();
			if (daten !== null)
				void props.patchAbschnittsdaten({ beschaeftigungsart: val?.daten(schuljahr.value)?.kuerzel }, daten.id);
		},
	});

	const einsatzstatus = computed<LehrerEinsatzstatus | undefined>({
		get(): LehrerEinsatzstatus | undefined {
			return LehrerEinsatzstatus.values().find(r => r.daten(schuljahr.value)?.kuerzel === personalabschnittsdaten()?.einsatzstatus);
		},
		set(val: LehrerEinsatzstatus | undefined) {
			const daten = personalabschnittsdaten();
			if (daten !== null)
				void props.patchAbschnittsdaten({ einsatzstatus: val?.daten(schuljahr.value)?.kuerzel }, daten.id);
		},
	});


	const mehrleistungsgrund = computed<LehrerMehrleistungsarten | undefined>({
		get(): LehrerMehrleistungsarten | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerMehrleistungsarten.values().find(e => e.daten(schuljahr.value)?.kuerzel === kuerzel);
		},
		set(val: LehrerMehrleistungsarten | undefined) {
			// TODO props.patch({ mehrleistungsgrund: val?.kuerzel });
		},
	});

	const minderleistungsgrund = computed<LehrerMinderleistungsarten | undefined>({
		get(): LehrerMinderleistungsarten | undefined {
			// TODO aus Personaldaten bestimmen
			const kuerzel = undefined;
			return LehrerMinderleistungsarten.values().find(e => e.daten(schuljahr.value)?.kuerzel === kuerzel);
		},
		set(val: LehrerMinderleistungsarten | undefined) {
			// TODO props.patch({ minderleistungsgrund: val?.kuerzel });
		},
	});

	const anrechnungsgrund = computed<LehrerAnrechnungsgrund | undefined>({
		get(): LehrerAnrechnungsgrund | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerAnrechnungsgrund.values().find(e => e.daten(schuljahr.value)?.kuerzel === kuerzel);
		},
		set(val: LehrerAnrechnungsgrund | undefined) {
			// TODO props.patch({ anrechnungsgrund: val?.kuerzel });
		},
	});

</script>
