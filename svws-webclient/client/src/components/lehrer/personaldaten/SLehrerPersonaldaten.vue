<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Identnummer" :model-value="personaldaten.identNrTeil1" @change="identNrTeil1 => patch({identNrTeil1})" type="text" span="full" />
				<svws-ui-text-input placeholder="Seriennummer" :model-value="personaldaten.identNrTeil2SerNr" @change="identNrTeil2SerNr => patch({identNrTeil2SerNr})" type="text" />
				<svws-ui-text-input placeholder="Vergütungsschlüssel" :model-value="personaldaten.lbvVerguetungsschluessel" @change="lbvVerguetungsschluessel => patch({lbvVerguetungsschluessel})" type="text" />
				<svws-ui-text-input placeholder="PA-Nummer" :model-value="personaldaten.personalaktennummer" @change="personalaktennummer => patch({personalaktennummer})" type="text" />
				<svws-ui-text-input placeholder="LBV-Pers.Nummer" :model-value="personaldaten.lbvPersonalnummer" @change="lbvPersonalnummer => patch({lbvPersonalnummer})" type="text" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Zugangsdatum" :model-value="personaldaten.zugangsdatum" @change="zugangsdatum => patch({zugangsdatum})" type="date" />
				<svws-ui-text-input placeholder="Abgangsdatum" :model-value="personaldaten.abgangsdatum" @change="abgangsdatum => patch({abgangsdatum})" type="date" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Beschäftigungsdaten">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Rechtsverhältnis" v-model="rechtsverhaeltnis" :items="LehrerRechtsverhaeltnis.values()"
					:item-text="(i: LehrerRechtsverhaeltnis) =>i.daten.text" />
				<svws-ui-select title="Beschäftigungsart" v-model="beschaeftigungsart" :items="LehrerBeschaeftigungsart.values()"
					:item-text="(i: LehrerBeschaeftigungsart) =>i.daten.text" />
				<svws-ui-input-number placeholder="Pflichtstundensoll" :model-value="personalabschnittsdaten?.pflichtstundensoll ?? 0.0" @change="pflichtstundensoll => patchAbschnittsdaten({ pflichtstundensoll: pflichtstundensoll }, personalabschnittsdaten?.id ?? -1)" />
				<svws-ui-select title="Einsatzstatus" v-model="einsatzstatus" :items="LehrerEinsatzstatus.values()"
					:item-text="(i: LehrerEinsatzstatus) =>i.daten.text" />
				<svws-ui-text-input placeholder="Stammschule" :model-value="personaldaten.stammschulnummer" @change="stammschulnummer => patch({stammschulnummer})" type="text" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Lehrämter">
			<svws-ui-input-wrapper>
				<s-lehrer-personaldaten-lehraemter :lehrer-liste-manager="lehrerListeManager" :patch-lehramt-anerkennung="patchLehramtAnerkennung" :add-lehramt="addLehramt" :remove-lehraemter="removeLehraemter" />
				<s-lehrer-personaldaten-lehrbefaehigungen :lehrer-liste-manager="lehrerListeManager" :patch-lehrbefaehigung-anerkennung="patchLehrbefaehigungAnerkennung" :add-lehrbefaehigung="addLehrbefaehigung" :remove-lehrbefaehigungen="removeLehrbefaehigungen" />
				<s-lehrer-personaldaten-fachrichtungen :lehrer-liste-manager="lehrerListeManager" :patch-fachrichtung-anerkennung="patchFachrichtungAnerkennung" :add-fachrichtung="addFachrichtung" :remove-fachrichtungen="removeFachrichtungen" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Mehr- und Minderleistung, Anrechnungsstunden">
			<svws-ui-input-wrapper>
				<svws-ui-select title="Mehrleistung" v-model="mehrleistungsgrund" :items="LehrerMehrleistungArt.values()"
					:item-text="(i: LehrerMehrleistungArt) =>i.daten.text" />
				<svws-ui-select title="Minderleistung" v-model="minderleistungsgrund" :items="LehrerMinderleistungArt.values()"
					:item-text="(i: LehrerMinderleistungArt) =>i.daten.text" />
				<svws-ui-input-number placeholder="Stundensumme" :model-value="personalabschnittsdaten?.pflichtstundensoll ?? 0.0" @change="pflichtstundensoll => patchAbschnittsdaten({ pflichtstundensoll: pflichtstundensoll }, personalabschnittsdaten?.id ?? -1)" />
				<svws-ui-select title="Nicht unterichtliche Tätigkeiten" v-model="anrechnungsgrund" :items="LehrerAnrechnungsgrund.values()"
					:item-text="(i: LehrerAnrechnungsgrund) =>i.daten.text" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Stammschulnummer" :model-value="personaldaten.stammschulnummer" @change="stammschulnummer => patch({stammschulnummer})" type="text" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { type LehrerPersonaldaten, type LehrerPersonalabschnittsdaten,
		LehrerBeschaeftigungsart, LehrerEinsatzstatus, LehrerRechtsverhaeltnis, LehrerAnrechnungsgrund, LehrerMehrleistungArt, LehrerMinderleistungArt } from "@core";
	import type { LehrerPersonaldatenProps } from './SLehrerPersonaldatenProps';

	const props = defineProps<LehrerPersonaldatenProps>();

	const personaldaten = computed<LehrerPersonaldaten>(() => props.lehrerListeManager().personalDaten());
	const personalabschnittsdaten = computed<LehrerPersonalabschnittsdaten | null>(() => props.lehrerListeManager().getAbschnittBySchuljahresabschnittsId(props.aktAbschnitt.id));

	const rechtsverhaeltnis = computed<LehrerRechtsverhaeltnis | undefined>({
		get(): LehrerRechtsverhaeltnis | undefined {
			return LehrerRechtsverhaeltnis.values().find(r => r.daten.kuerzel === personalabschnittsdaten.value?.rechtsverhaeltnis);
		},
		set(val: LehrerRechtsverhaeltnis | undefined) {
			if (personalabschnittsdaten.value != null)
				void props.patchAbschnittsdaten({ rechtsverhaeltnis: val?.daten.kuerzel }, personalabschnittsdaten.value.id);
		}
	});

	const beschaeftigungsart = computed<LehrerBeschaeftigungsart | undefined>({
		get(): LehrerBeschaeftigungsart | undefined {
			return LehrerBeschaeftigungsart.values().find(r => r.daten.kuerzel === personalabschnittsdaten.value?.beschaeftigungsart);
		},
		set(val: LehrerBeschaeftigungsart | undefined) {
			if (personalabschnittsdaten.value != null)
				void props.patchAbschnittsdaten({ beschaeftigungsart: val?.daten.kuerzel }, personalabschnittsdaten.value.id);
		}
	});

	const einsatzstatus = computed<LehrerEinsatzstatus | undefined>({
		get(): LehrerEinsatzstatus | undefined {
			return LehrerEinsatzstatus.values().find(r => r.daten.kuerzel === personalabschnittsdaten.value?.einsatzstatus);
		},
		set(val: LehrerEinsatzstatus | undefined) {
			if (personalabschnittsdaten.value != null)
				void props.patchAbschnittsdaten({ einsatzstatus: val?.daten.kuerzel }, personalabschnittsdaten.value.id);
		}
	});


	const mehrleistungsgrund = computed<LehrerMehrleistungArt | undefined>({
		get(): LehrerMehrleistungArt | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerMehrleistungArt.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerMehrleistungArt | undefined) {
			// TODO props.personaldaten.patch({ mehrleistungsgrund: val?.kuerzel });
		}
	});

	const minderleistungsgrund = computed<LehrerMinderleistungArt | undefined>({
		get(): LehrerMinderleistungArt | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerMinderleistungArt.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerMinderleistungArt | undefined) {
			// TODO props.personaldaten.patch({ minderleistungsgrund: val?.kuerzel });
		}
	});

	const anrechnungsgrund = computed<LehrerAnrechnungsgrund | undefined>({
		get(): LehrerAnrechnungsgrund | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerAnrechnungsgrund.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerAnrechnungsgrund | undefined) {
			// TODO props.personaldaten.patch({ anrechnungsgrund: val?.kuerzel });
		}
	});

</script>
