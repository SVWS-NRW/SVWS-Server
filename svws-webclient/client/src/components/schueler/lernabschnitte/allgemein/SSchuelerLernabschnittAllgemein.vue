<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<div class="flex flex-row gap-4 mb-4">
				<svws-ui-select :disabled="!hatUpdateKompetenz" title="Klasse" :items="manager().klasseGetMenge()" :item-text="i => i.kuerzel ?? '—'"
					:model-value="klasse" @update:model-value="value => patch({ klassenID: ((value === undefined) || (value === null)) ? null : value.id })"
					autocomplete statistics required autofocus focus-class-content />
				<svws-ui-select :disabled="!hatUpdateKompetenz" title="Jahrgang" :items="manager().jahrgangGetMenge()" :item-text="i => i.kuerzel ?? '—'"
					:model-value="jahrgang" @update:model-value="value => patch({ jahrgangID: ((value === undefined) || (value === null)) ? null : value.id })"
					autocomplete statistics required />
				<svws-ui-select :disabled="!hatUpdateKompetenz" title="EP-Jahr" :items="PrimarstufeSchuleingangsphaseBesuchsjahre.data().getEintraegeBySchuljahr(schuljahr)" :item-text="i => i.kuerzel"
					:model-value="epJahre" @update:model-value="value => patch({ epJahre: ((value === undefined) || (value === null)) ? null : value.id })"
					statistics required />
			</div>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Datum von" type="date" statistics required
					:model-value="manager().lernabschnittGet().datumAnfang || undefined" @change="datumAnfang => patch({datumAnfang})" />
				<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Datum bis" type="date" statistics required
					:model-value="manager().lernabschnittGet().datumEnde || undefined" @change="datumEnde => patch({datumEnde})" />
				<svws-ui-spacing />
				<div>
					<span class="font-bold" :class="{'opacity-50': !klassenlehrer.length}"> Klassenlehrer </span>
					<span v-if="!klassenlehrer.length"> — Keine Daten vorhanden.</span>
					<div v-else class="flex flex-col leading-tight text-base">
						<span v-for="kl in klassenlehrer" :key="kl.id"> {{ getLehrerText(kl) }} </span>
					</div>
				</div>
				<div class="flex flex-col gap-3">
					<svws-ui-select :disabled="!hatUpdateKompetenz" title="Tutor" :items="manager().lehrerGetMenge()" :item-text="getLehrerText" autocomplete
						:model-value="tutor" @update:model-value="value => patch({ tutorID: ((value === undefined) || (value === null)) ? null : value.id })" />
					<svws-ui-select :disabled="!hatUpdateKompetenz" title="Sonderpädagoge" :items="manager().lehrerGetMenge()" :model-value="sonderpaedagoge"
						@update:model-value="value => patch({ sonderpaedagogeID: ((value === undefined) || (value === null)) ? null : value.id })"
						:item-text="getLehrerText" autocomplete />
				</div>
				<svws-ui-spacing :size="2" />
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-select :disabled="!hatUpdateKompetenz" title="Schulgliederung" :items="gliederungen" autocomplete statistics required
						v-model="gliederung" :item-text="i => `${i.daten(schuljahr)?.kuerzel ?? '—'} - ${i.daten(schuljahr)?.text ?? '—'}`" />
					<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Prüfungsordnung" required
						:model-value="manager().lernabschnittGet().pruefungsOrdnung || undefined" />
					<svws-ui-select :disabled="!hatUpdateKompetenz" title="Organisationsform" :items="organisationsformen" autocomplete statistics required
						v-model="organisationsform" :item-text="i => i.text" />
					<svws-ui-select :disabled="!hatUpdateKompetenz" title="Klassenart" :items="klassenarten" autocomplete statistics required
						v-model="klassenart" :item-text="i => i.daten(schuljahr)?.text ?? '—'" />
				</svws-ui-input-wrapper>
				<svws-ui-spacing />
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-select :disabled="!hatUpdateKompetenz" title="Bilingualer Zweig" :items="bilingualeZweige" v-model="bilingualerZweig"
						:item-text="i => i.daten(schuljahr)?.text ?? '—'" autocomplete removable />
				</svws-ui-input-wrapper>
				<svws-ui-spacing />
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-select :disabled="!hatUpdateKompetenz" title="Förderschwerpunkt" :items="manager().foerderschwerpunktGetMenge()"
						:item-text="i => i.text" autocomplete removable statistics v-model="foerderschwerpunkt" />
					<svws-ui-select :disabled="!hatUpdateKompetenz" title="Weiterer Förderschwerpunkt" :items="manager().foerderschwerpunktGetMenge()"
						:item-text="i => i.text" autocomplete removable statistics v-model="foerderschwerpunkt2" />
					<svws-ui-checkbox :disabled="!hatUpdateKompetenz" v-model="schwerbehinderung" statistics span="full">Schwerstbehinderung</svws-ui-checkbox>
					<svws-ui-checkbox :disabled="!hatUpdateKompetenz" v-model="autismus" span="full"> Autismus </svws-ui-checkbox>
					<svws-ui-checkbox :disabled="!hatUpdateKompetenz" v-model="aosf" span="full"> AOSF </svws-ui-checkbox>
					<svws-ui-checkbox :disabled="!hatUpdateKompetenz" v-model="zieldifferentesLernen" span="full"> Zieldifferentes Lernen </svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { FoerderschwerpunktEintrag, JahrgangsDaten, KlassenDaten, LehrerListeEintrag, List, OrganisationsformKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag } from "@core";
	import { BilingualeSprache, AllgemeinbildendOrganisationsformen, BerufskollegOrganisationsformen, Klassenart, Schulform, Schulgliederung, ArrayList,
		WeiterbildungskollegOrganisationsformen, DeveloperNotificationException, BenutzerKompetenz,
		PrimarstufeSchuleingangsphaseBesuchsjahre} from "@core";

	import type { SchuelerLernabschnittAllgemeinProps } from "./SSchuelerLernabschnittAllgemeinProps";

	const props = defineProps<SchuelerLernabschnittAllgemeinProps>();

	const schuljahr = computed<number>(() => props.manager().schuljahrGet());
	// Die Schulform muss definiert sein, sonst würde diese Ansicht gar nicht erst aufgerufen werden...
	const schulform = computed<Schulform>(() => Schulform.data().getWertByKuerzel(props.schule.schulform) ?? Schulform.G);

	const primarschulformen = new Set([Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.PS, Schulform.S, Schulform.KS, Schulform.V]);

	const epJahre = computed<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag | null>(() => {
		if (!primarschulformen.has(schulform.value))
			return null;
		const ep = props.manager().schuelerGet().epJahre ?? null;
		if (ep === null)
			return null;
		const schuljahr = props.manager().schuljahrGet();
		return PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWertByIDOrNull(ep)?.daten(schuljahr) ?? null;
	});

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
		set: (value) => void props.patch({ foerderschwerpunkt1ID: value?.id ?? null }),
	});

	const foerderschwerpunkt2 = computed<FoerderschwerpunktEintrag | null>({
		get: () => {
			const id = props.manager().lernabschnittGet().foerderschwerpunkt2ID;
			if (id === null)
				return null;
			return props.manager().foerderschwerpunktGetByIdOrException(id);
		},
		set: (value) => void props.patch({ foerderschwerpunkt2ID: value?.id ?? null }),
	});

	const aosf = computed<boolean>({
		get: () => props.manager().lernabschnittGet().hatAOSF,
		set: (value) => void props.patch({ hatAOSF: value }),
	});

	const autismus = computed<boolean>({
		get: () => props.manager().lernabschnittGet().hatAutismus,
		set: (value) => void props.patch({ hatAutismus: value }),
	});

	const schwerbehinderung = computed<boolean>({
		get: () => props.manager().lernabschnittGet().hatSchwerbehinderungsNachweis,
		set: (value) => void props.patch({ hatSchwerbehinderungsNachweis: value }),
	});

	const zieldifferentesLernen = computed<boolean>({
		get: () => props.manager().lernabschnittGet().hatZieldifferentenUnterricht,
		set: (value) => void props.patch({ hatZieldifferentenUnterricht: value }),
	});

	const klassenarten = computed<List<Klassenart>>(() => {
		return Klassenart.getBySchuljahrAndSchulform(schuljahr.value, schulform.value);
	});

	const klassenart = computed<Klassenart | null>({
		get: () => {
			const kuerzel = props.manager().lernabschnittGet().Klassenart;
			return ((kuerzel === null) ? null : Klassenart.data().getWertByKuerzel(kuerzel)) ?? null;
		},
		set: (value) => void props.patch({ Klassenart: value?.daten(schuljahr.value)?.kuerzel ?? null }),
	});

	const gliederungen = computed<List<Schulgliederung>>(() => {
		return Schulgliederung.getBySchuljahrAndSchulform(schuljahr.value, schulform.value);
	});

	const gliederung = computed<Schulgliederung | null>({
		get: () => {
			if (props.manager().lernabschnittGet().schulgliederung === null)
				return null;
			const kuerzel = props.manager().lernabschnittGet().schulgliederung;
			return ((kuerzel === null) ? null : Schulgliederung.data().getWertByKuerzel(kuerzel));
		},
		set: (value) => void props.patch({ schulgliederung: value?.daten(schuljahr.value)?.kuerzel ?? null }),
	});

	const organisationsformen = computed<List<OrganisationsformKatalogEintrag>>(() => {
		const result = new ArrayList<OrganisationsformKatalogEintrag>();
		if (schulform.value === Schulform.WB) {
			for (const orgform of WeiterbildungskollegOrganisationsformen.values())
				result.add(orgform.daten(schuljahr.value));
		} else if ((schulform.value === Schulform.BK) || (schulform.value === Schulform.SB)) {
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
			const kuerzel = props.manager().lernabschnittGet().organisationsform;
			if (schulform.value === Schulform.WB)
				return ((kuerzel === null) ? null : WeiterbildungskollegOrganisationsformen.data().getWertByKuerzel(kuerzel)?.daten(schuljahr.value) ?? null);
			if ((schulform.value === Schulform.BK) || (schulform.value === Schulform.SB))
				return ((kuerzel === null) ? null : BerufskollegOrganisationsformen.data().getWertByKuerzel(kuerzel)?.daten(schuljahr.value) ?? null);
			return ((kuerzel === null) ? null : AllgemeinbildendOrganisationsformen.data().getWertByKuerzel(kuerzel)?.daten(schuljahr.value) ?? null);
		},
		set: (value) => void props.patch({ organisationsform: value?.kuerzel ?? null }),
	});

	const bilingualeZweige = computed<List<BilingualeSprache>>(() => BilingualeSprache.data().getListBySchuljahrAndSchulform(schuljahr.value, schulform.value));

	const bilingualerZweig = computed<BilingualeSprache | null>({
		get: () => {
			const bilingualerZweig = props.manager().lernabschnittGet().bilingualerZweig;
			if (bilingualerZweig === null)
				return null;
			const schulform = Schulform.data().getWertByKuerzel(props.schule.schulform);
			if (schulform === null)
				throw new DeveloperNotificationException("Keine gültige Schulform festgelegt");
			const bili = BilingualeSprache.data().getWertBySchluessel(bilingualerZweig);
			if ((bili !== null) && (bili.hatSchulform(schuljahr.value, schulform)))
				return bili;
			return null;
		},
		set: (value : BilingualeSprache | null) => void props.patch({ bilingualerZweig: value?.daten(schuljahr.value)?.kuerzel ?? null }),
	});

</script>
