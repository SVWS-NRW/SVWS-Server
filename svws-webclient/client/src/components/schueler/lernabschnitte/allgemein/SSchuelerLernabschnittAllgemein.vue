<template>
	<div class="content">
		<svws-ui-content-card>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select :disabled="!hatUpdateKompetenz" title="Klasse" :items="manager().klasseGetMenge()" :item-text="i => `${i.kuerzel}`" autocomplete statistics
					:model-value="klasse" @update:model-value="value => patch({ klassenID: ((value === undefined) || (value === null)) ? null : value.id })" />
				<svws-ui-select :disabled="!hatUpdateKompetenz" title="Jahrgang" :items="manager().jahrgangGetMenge()" :item-text="i => `${i.kuerzel}`" autocomplete statistics
					:model-value="jahrgang" @update:model-value="value => patch({ jahrgangID: ((value === undefined) || (value === null)) ? null : value.id })" />
				<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Datum von" type="date" statistics
					:model-value="manager().lernabschnittGet().datumAnfang || undefined"
					@change="datumAnfang => patch({datumAnfang})" />
				<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Datum bis" type="date" statistics
					:model-value="manager().lernabschnittGet().datumEnde || undefined"
					@change="datumEnde => patch({datumEnde})" />
				<svws-ui-spacing />
				<div>
					<span class="font-bold" :class="{'opacity-50': !klassenlehrer.length}">Klassenlehrer </span>
					<span v-if="!klassenlehrer.length">– Keine Daten vorhanden.</span>
					<div v-else class="flex flex-col leading-tight text-base">
						<span v-for="kl in klassenlehrer" :key="kl.id">
							{{ getLehrerText(kl) }}
						</span>
					</div>
				</div>
				<div class="flex flex-col gap-3">
					<svws-ui-select :disabled="!hatUpdateKompetenz" title="Tutor" :items="manager().lehrerGetMenge()" :item-text="getLehrerText" autocomplete
						:model-value="tutor" @update:model-value="value => patch({ tutorID: ((value === undefined) || (value === null)) ? null : value.id })" />
					<svws-ui-select :disabled="!hatUpdateKompetenz" title="Sonderpädagoge" :items="manager().lehrerGetMenge()" :item-text="getLehrerText" autocomplete
						:model-value="sonderpaedagoge" @update:model-value="value => patch({ sonderpaedagogeID: ((value === undefined) || (value === null)) ? null : value.id })" />
				</div>
				<svws-ui-spacing :size="2" />
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-select :disabled="!hatUpdateKompetenz" title="Schulgliederung" :items="gliederungen" :item-text="i => `${i.daten(schuljahr)?.kuerzel ?? '—'} - ${i.daten(schuljahr)?.text ?? '—'}`" autocomplete statistics
						v-model="gliederung" />
					<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Prüfungsordnung" :model-value="manager().lernabschnittGet().pruefungsOrdnung || undefined" />
					<svws-ui-select :disabled="!hatUpdateKompetenz" title="Organisationsform" :items="organisationsformen" :item-text="i => i.text" autocomplete statistics
						v-model="organisationsform" />
					<svws-ui-select :disabled="!hatUpdateKompetenz" title="Klassenart" :items="klassenarten" :item-text="i => i.daten(schuljahr)?.text ?? '—'" autocomplete statistics
						v-model="klassenart" />
				</svws-ui-input-wrapper>
				<svws-ui-spacing />
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-select :disabled="!hatUpdateKompetenz" title="Förderschwerpunkt" :items="manager().foerderschwerpunktGetMenge()" :item-text="i => ((i === undefined) || (i.text === undefined)) ? '—' : i.text " autocomplete statistics
						v-model="foerderschwerpunkt" />
					<svws-ui-select :disabled="!hatUpdateKompetenz" title="Weiterer Förderschwerpunkt" :items="manager().foerderschwerpunktGetMenge()" :item-text="i => ((i === undefined) || (i.text === undefined)) ? '—' : i.text" autocomplete statistics
						v-model="foerderschwerpunkt2" />
					<svws-ui-checkbox :disabled="!hatUpdateKompetenz" v-model="schwerbehinderung" statistics span="full"> Schwerstbehinderung </svws-ui-checkbox>
					<svws-ui-checkbox :disabled="!hatUpdateKompetenz" v-model="autismus" span="full"> Autismus </svws-ui-checkbox>
					<svws-ui-checkbox :disabled="!hatUpdateKompetenz" v-model="aosf" span="full"> AOSF </svws-ui-checkbox>
					<svws-ui-checkbox :disabled="!hatUpdateKompetenz" v-model="zieldifferentesLernen" span="full"> zieldifferentes Lernen </svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { FoerderschwerpunktEintrag, JahrgangsDaten, KlassenDaten, LehrerListeEintrag, List, OrganisationsformKatalogEintrag } from "@core";
	import { AllgemeinbildendOrganisationsformen, BerufskollegOrganisationsformen, Klassenart, Schulform, Schulgliederung, ArrayList, WeiterbildungskollegOrganisationsformen, DeveloperNotificationException, BenutzerKompetenz } from "@core";

	import type { SchuelerLernabschnittAllgemeinProps } from "./SSchuelerLernabschnittAllgemeinProps";

	const props = defineProps<SchuelerLernabschnittAllgemeinProps>();

	const schuljahr = computed<number>(() => props.manager().schuljahrGet());

	const hatUpdateKompetenz = computed<boolean>(() => {
		return (props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN))
			|| ((props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN))
				&& props.benutzerKompetenzenKlassen.has(props.schuelerListeManager().auswahl().idKlasse));
	});

	function getLehrerText(lehrer: LehrerListeEintrag) : string {
		return `${lehrer.nachname}, ${lehrer.vorname} (${lehrer.kuerzel})`;
	}

	const klasse = computed<KlassenDaten | null>(() => {
		const id = props.manager().lernabschnittGet().klassenID;
		if (id === null)
			return null;
		return props.manager().klasseGetByIdOrException(id);
	});

	const jahrgang = computed<JahrgangsDaten | null>(() => {
		const id = props.manager().lernabschnittGet().jahrgangID;
		if (id === null)
			return null;
		return props.manager().jahrgangGetByIdOrException(id);
	});

	const sonderpaedagoge = computed<LehrerListeEintrag | null>(() => {
		const id = props.manager().lernabschnittGet().sonderpaedagogeID;
		if (id === null)
			return null;
		return props.manager().lehrerGetByIdOrException(id);
	});

	const tutor = computed<LehrerListeEintrag | null>(() => {
		const id = props.manager().lernabschnittGet().tutorID;
		if (id === null)
			return null;
		return props.manager().lehrerGetByIdOrException(id);
	});

	const klassenlehrer = computed<LehrerListeEintrag[]>(() => {
		const k = klasse.value;
		if (k === null)
			return [];
		const result: LehrerListeEintrag[] = [];
		for (const lid of k.klassenLeitungen)
			result.push(props.manager().lehrerGetByIdOrException(lid));
		return result;
	});

	const foerderschwerpunkt = computed<FoerderschwerpunktEintrag | null>({
		get: () => {
			const id = props.manager().lernabschnittGet().foerderschwerpunkt1ID;
			if (id === null)
				return null;
			return props.manager().foerderschwerpunktGetByIdOrException(id);
		},
		set: (value) => void props.patch({ foerderschwerpunkt1ID: value?.id ?? null })
	});

	const foerderschwerpunkt2 = computed<FoerderschwerpunktEintrag | null>({
		get: () => {
			const id = props.manager().lernabschnittGet().foerderschwerpunkt2ID;
			if (id === null)
				return null;
			return props.manager().foerderschwerpunktGetByIdOrException(id);
		},
		set: (value) => void props.patch({ foerderschwerpunkt2ID: value?.id ?? null })
	});

	const aosf = computed<boolean>({
		get: () => props.manager().lernabschnittGet().hatAOSF,
		set: (value) => void props.patch({ hatAOSF: value })
	});

	const autismus = computed<boolean>({
		get: () => props.manager().lernabschnittGet().hatAutismus,
		set: (value) => void props.patch({ hatAutismus: value })
	});

	const schwerbehinderung = computed<boolean>({
		get: () => props.manager().lernabschnittGet().hatSchwerbehinderungsNachweis,
		set: (value) => void props.patch({ hatSchwerbehinderungsNachweis: value })
	});

	const zieldifferentesLernen = computed<boolean>({
		get: () => props.manager().lernabschnittGet().hatZieldifferentenUnterricht,
		set: (value) => void props.patch({ hatZieldifferentenUnterricht: value })
	});

	const klassenarten = computed<List<Klassenart>>(() => {
		const schulform = Schulform.data().getWertByKuerzel(props.schule.schulform);
		if (schulform === null)
			throw new DeveloperNotificationException("Keine gültige Schulform festgelegt");
		return Klassenart.getBySchuljahrAndSchulform(schuljahr.value, schulform);
	});

	const klassenart = computed<Klassenart | null>({
		get: () => {
			const kuerzel = props.manager().lernabschnittGet().Klassenart;
			return ((kuerzel === null) ? null : Klassenart.data().getWertByKuerzel(kuerzel)) ?? null;
		},
		set: (value) => void props.patch({ Klassenart: value?.daten(schuljahr.value)?.kuerzel ?? null })
	});

	const gliederungen = computed<List<Schulgliederung>>(() => {
		const schulform = Schulform.data().getWertByKuerzel(props.schule.schulform);
		if (schulform === null)
			throw new DeveloperNotificationException("Keine gültige Schulform festgelegt");
		return Schulgliederung.getBySchuljahrAndSchulform(schuljahr.value, schulform);
	});

	const gliederung = computed<Schulgliederung | null>({
		get: () => {
			if (props.manager().lernabschnittGet().schulgliederung === null)
				return null;
			const kuerzel = props.manager().lernabschnittGet().schulgliederung;
			return ((kuerzel === null) ? null : Schulgliederung.data().getWertByKuerzel(kuerzel));
		},
		set: (value) => void props.patch({ schulgliederung: value?.daten(schuljahr.value)?.kuerzel ?? null })
	});

	const organisationsformen = computed<List<OrganisationsformKatalogEintrag>>(() => {
		const schulform = Schulform.data().getWertByKuerzel(props.schule.schulform);
		if (schulform === null)
			throw new DeveloperNotificationException("Keine gültige Schulform festgelegt");
		const result = new ArrayList<OrganisationsformKatalogEintrag>();
		if (schulform === Schulform.WB) {
			for (const orgform of WeiterbildungskollegOrganisationsformen.values())
				result.add(orgform.daten(schuljahr.value));
		} else if ((schulform === Schulform.BK) || (schulform === Schulform.SB)) {
			for (const orgform of BerufskollegOrganisationsformen.values())
				result.add(orgform.daten(schuljahr.value));
		} else {
			for (const orgform of AllgemeinbildendOrganisationsformen.values())
				result.add(orgform.daten(schuljahr.value));
		}
		return result;
	});

	const organisationsform = computed<OrganisationsformKatalogEintrag | null>({
		get: () => {
			if (props.manager().lernabschnittGet().organisationsform === null)
				return null;
			const schulform = Schulform.data().getWertByKuerzel(props.schule.schulform);
			if (schulform === null)
				throw new DeveloperNotificationException("Keine gültige Schulform festgelegt");
			const kuerzel = props.manager().lernabschnittGet().organisationsform;
			if (schulform === Schulform.WB) {
				return ((kuerzel === null) ? null : WeiterbildungskollegOrganisationsformen.data().getWertByKuerzel(kuerzel)?.daten(schuljahr.value) ?? null);
			}
			if ((schulform === Schulform.BK) || (schulform === Schulform.SB)) {
				return ((kuerzel === null) ? null : BerufskollegOrganisationsformen.data().getWertByKuerzel(kuerzel)?.daten(schuljahr.value) ?? null);
			}
			return ((kuerzel === null) ? null : AllgemeinbildendOrganisationsformen.data().getWertByKuerzel(kuerzel)?.daten(schuljahr.value) ?? null);
		},
		set: (value) => {
			void props.patch({ organisationsform: value?.kuerzel ?? null });
		}
	});

</script>


<style lang="postcss" scoped>

	.content {
		@apply w-full h-full grid grid-cols-2;
	}

</style>
