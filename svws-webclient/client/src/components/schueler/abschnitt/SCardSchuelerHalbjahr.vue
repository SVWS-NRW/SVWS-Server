<template>
	<svws-ui-content-card title="Allgemeine Angaben">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-multi-select title="Klasse" v-model="klasse" :items="mapKlassen" :item-text="i => `${i.kuerzel}`" autocomplete />
			<svws-ui-multi-select title="Jahrgang" v-model="jahrgang" :items="mapJahrgaenge" :item-text="i => `${i.kuerzel}`" autocomplete />
			<svws-ui-text-input placeholder="Datum von" :model-value="data.datumAnfang || undefined" @change="datumAnfang=>doPatch({datumAnfang})" type="date" />
			<svws-ui-text-input placeholder="Datum bis" :model-value="data.datumEnde || undefined" @change="datumEnde=>doPatch({datumEnde})" type="date" />
			<svws-ui-spacing />
			<div class="col-span-full flex flex-wrap gap-3" :class="{'opacity-50': !klassenlehrer.length}">
				<span class="font-bold">Klassenlehrer:</span>
				<span v-if="!klassenlehrer.length">Keine Daten vorhanden.</span>
				<div v-else class="separate-items--custom">
					<span v-for="kl in klassenlehrer" :key="kl.id">
						<svws-ui-tooltip>
							{{ getLehrerKuerzel(kl) }}
							<template #content>
								{{ getLehrerText(kl) }}
							</template>
						</svws-ui-tooltip>
					</span>
				</div>
			</div>
			<svws-ui-multi-select title="Tutor" v-model="tutor" :items="mapLehrer" :item-text="getLehrerText" autocomplete />
			<svws-ui-multi-select title="Sonderpädagoge" v-model="sonderpaedagoge" :items="mapLehrer" :item-text="getLehrerText" autocomplete />
			<svws-ui-spacing :size="2" />
			<svws-ui-input-wrapper class="col-span-full items-center" :grid="4">
				<span class="font-bold col-span-full">Fehlstunden</span>
				<svws-ui-text-input placeholder="Maximal" :model-value="data.fehlstundenGrenzwert || undefined" @change="doPatch({ fehlstundenGrenzwert: Number($event) })" type="number" />
				<svws-ui-text-input placeholder="Gesamt" :model-value="data.fehlstundenGesamt || undefined" @change="doPatch({ fehlstundenGesamt: Number($event) })" type="number" />
				<svws-ui-text-input placeholder="Unendschuldigt" :model-value="data.fehlstundenUnentschuldigt || undefined" @change="doPatch({ fehlstundenUnentschuldigt: Number($event) })" type="number" />
			</svws-ui-input-wrapper>
			<svws-ui-spacing :size="2" />
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-multi-select title="Schulgliederung" v-model="gliederung" :items="gliederungen" :item-text="i => `${i.daten.kuerzel} - ${i.daten.beschreibung}`" autocomplete />
				<svws-ui-text-input placeholder="Prüfungsordnung" :model-value="data.pruefungsOrdnung || undefined" />
				<svws-ui-multi-select title="Organisationsform" v-model="organisationsform" :items="organisationsformen" :item-text="i => i.beschreibung" autocomplete />
				<svws-ui-multi-select title="Klassenart" v-model="klassenart" :items="klassenarten" :item-text="i => i.daten.bezeichnung" autocomplete />
			</svws-ui-input-wrapper>
			<svws-ui-spacing />
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-multi-select title="Förderschwerpunkt" v-model="foerderschwerpunkt" :items="mapFoerderschwerpunkte" :item-text="i => i.text" autocomplete />
				<svws-ui-multi-select title="Weiterer Förderschwerpunkt" v-model="foerderschwerpunkt2" :items="mapFoerderschwerpunkte" :item-text="i => i.text" autocomplete />
				<svws-ui-checkbox v-model="schwerbehinderung" span="full"> Schwerstbehinderung </svws-ui-checkbox>
			</svws-ui-input-wrapper>
			<svws-ui-spacing :size="2" />
			<svws-ui-input-wrapper :grid="2">
				<span class="font-bold col-span-full">Lernbereichsnoten</span>
				<svws-ui-multi-select title="Gesellschaftswissenschaft" v-model="lernbereichsnoteGSbzwAL" :items="getLernbereichsnoten()"
					:item-text="i => `${i.kuerzel}`" autocomplete />
				<svws-ui-multi-select title="Naturwissenschaft" v-model="lernbereichsnoteNW" :items="getLernbereichsnoten()"
					:item-text="i => `${i.kuerzel}`" autocomplete />
			</svws-ui-input-wrapper>
			<svws-ui-spacing :size="2" />
			<svws-ui-input-wrapper>
				<div class="font-bold opacity-50"> TODO: Nachprüfungen </div>
				<div v-if="data.nachpruefungen !== null">
					<div>mögliche Nachprüfungsfächer</div>
					<div v-for="fach in data.nachpruefungen.moegliche" :key="fach">
						{{ fach }}
					</div>
					<div>Nachprüfungen</div>
					<div v-for="pruefung in data.nachpruefungen.pruefungen" :key="pruefung.fachID">
						{{ `${pruefung.grund} ${pruefung.datum} ${pruefung.fachID} ${pruefung.note}` }}
					</div>
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { FoerderschwerpunktEintrag, JahrgangsListeEintrag, KlassenListeEintrag, LehrerListeEintrag, List, OrganisationsformKatalogEintrag, SchuelerLernabschnittsdaten, SchuleStammdaten} from "@core";
	import type { ComputedRef, WritableComputedRef } from 'vue';
	import { AllgemeinbildendOrganisationsformen, BerufskollegOrganisationsformen, Klassenart, Note, Schulform, Schulgliederung, ArrayList, WeiterbildungskollegOrganisationsformen } from "@core";
	import { computed } from 'vue';

	const props = defineProps<{
		schule: SchuleStammdaten;
		data: SchuelerLernabschnittsdaten;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
		mapKlassen: Map<number, KlassenListeEintrag>;
		mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerLernabschnittsdaten>) : void;
	}>()

	function doPatch(data: Partial<SchuelerLernabschnittsdaten>) {
		emit('patch', data);
	}

	function getLehrerText(lehrer: LehrerListeEintrag) : string {
		return `${lehrer.nachname}, ${lehrer.vorname} (${lehrer.kuerzel})`;
	}

	function getLehrerKuerzel(lehrer: LehrerListeEintrag) : string {
		return lehrer.kuerzel;
	}

	const sonderpaedagoge: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get: () => props.data.sonderpaedagogeID === null ? undefined : props.mapLehrer.get(props.data.sonderpaedagogeID),
		set: (value) => emit('patch', { sonderpaedagogeID: value === undefined ? null : value.id })
	});

	const tutor: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get: () => props.data.tutorID === null ? undefined : props.mapLehrer.get(props.data.tutorID),
		set: (value) => emit('patch', { tutorID: value === undefined ? null : value.id })
	});

	const jahrgang: WritableComputedRef<JahrgangsListeEintrag | undefined> = computed({
		get: () => props.mapJahrgaenge.get(props.data.jahrgangID),
		set: (value) => {
			if (value !== undefined)
				emit('patch', { jahrgangID: value.id });
		}
	});

	const klasse: WritableComputedRef<KlassenListeEintrag | undefined> = computed({
		get: () => props.mapKlassen.get(props.data.klassenID),
		set: (value) => {
			if (value !== undefined)
				emit('patch', { klassenID: value.id });
		}
	});

	const klassenlehrer: ComputedRef<LehrerListeEintrag[]> = computed(() => {
		const k = klasse.value;
		if ((k === undefined) || (k.klassenLehrer === null))
			return [];
		const result: LehrerListeEintrag[] = [];
		for (const lid of k.klassenLehrer) {
			const l = props.mapLehrer.get(lid);
			if (l !== undefined)
				result.push(l);
		}
		return result;
	});

	const foerderschwerpunkt: WritableComputedRef<FoerderschwerpunktEintrag | undefined> = computed({
		get: () => props.data.foerderschwerpunkt1ID === null ? undefined : props.mapFoerderschwerpunkte.get(props.data.foerderschwerpunkt1ID),
		set: (value) => emit('patch', { foerderschwerpunkt1ID: value === undefined ? null : value.id })
	});

	const foerderschwerpunkt2: WritableComputedRef<FoerderschwerpunktEintrag | undefined> = computed({
		get: () => props.data.foerderschwerpunkt2ID === null ? undefined : props.mapFoerderschwerpunkte.get(props.data.foerderschwerpunkt2ID),
		set: (value) => emit('patch', { foerderschwerpunkt2ID: value === undefined ? null : value.id })
	});

	const schwerbehinderung: WritableComputedRef<boolean> = computed({
		get: () => props.data.hatSchwerbehinderungsNachweis,
		set: (value) => doPatch({ hatSchwerbehinderungsNachweis: value })
	});

	function getLernbereichsnoten() : Note[] {
		return [ Note.KEINE, Note.SEHR_GUT, Note.GUT, Note.BEFRIEDIGEND, Note.AUSREICHEND, Note.MANGELHAFT, Note.UNGENUEGEND ];
	}

	const lernbereichsnoteGSbzwAL: WritableComputedRef<Note | undefined> = computed({
		get: () => {
			const note = Note.fromNoteSekI(props.data.noteLernbereichGSbzwAL);
			return note === null ? undefined : note;
		},
		set: (value) => emit('patch', { noteLernbereichGSbzwAL: value === undefined || value === Note.KEINE ? null : value.getNoteSekI() })
	});

	const lernbereichsnoteNW: WritableComputedRef<Note | undefined> = computed({
		get: () => {
			const note = Note.fromNoteSekI(props.data.noteLernbereichNW);
			return note === null ? undefined : note;
		},
		set: (value) => emit('patch', { noteLernbereichNW: value === undefined || value === Note.KEINE ? null : value.getNoteSekI() })
	});

	const klassenarten: ComputedRef<List<Klassenart>> = computed(() => {
		const schulform = Schulform.getByKuerzel(props.schule.schulform);
		if (schulform === null)
			throw new Error("Keine gültige Schulform festgelegt");
		return Klassenart.get(schulform);
	});

	const klassenart: WritableComputedRef<Klassenart | undefined> = computed({
		get: () => {
			const art = Klassenart.getByASDKursart(props.data.Klassenart);
			return (art === null) ? undefined : art;
		},
		set: (value) => emit('patch', { Klassenart: value === undefined ? null : value.daten.kuerzel })
	});

	const gliederungen: ComputedRef<List<Schulgliederung>> = computed(() => {
		const schulform = Schulform.getByKuerzel(props.schule.schulform);
		if (schulform === null)
			throw new Error("Keine gültige Schulform festgelegt");
		return Schulgliederung.get(schulform);
	});

	const gliederung: WritableComputedRef<Schulgliederung | undefined> = computed({
		get: () => {
			if (props.data.schulgliederung === null)
				return undefined;
			const gliederung = Schulgliederung.getByKuerzel(props.data.schulgliederung);
			return gliederung === null ? undefined : gliederung;
		},
		set: (value) => emit('patch', { schulgliederung: value === undefined || value === null ? null : value.daten.kuerzel })
	});

	const organisationsformen: ComputedRef<List<OrganisationsformKatalogEintrag>> = computed(() => {
		const schulform = Schulform.getByKuerzel(props.schule.schulform);
		if (schulform === null)
			throw new Error("Keine gültige Schulform festgelegt");
		const result = new ArrayList<OrganisationsformKatalogEintrag>();
		if (schulform === Schulform.WB) {
			for (const orgform of WeiterbildungskollegOrganisationsformen.values())
				result.add(orgform.daten);
		} else if ((schulform === Schulform.BK) || (schulform === Schulform.SB)) {
			for (const orgform of BerufskollegOrganisationsformen.values())
				result.add(orgform.daten);
		} else {
			for (const orgform of AllgemeinbildendOrganisationsformen.values())
				result.add(orgform.daten);
		}
		return result;
	});

	const organisationsform: WritableComputedRef<OrganisationsformKatalogEintrag | undefined> = computed({
		get: () => {
			if (props.data.organisationsform === null)
				return undefined;
			const schulform = Schulform.getByKuerzel(props.schule.schulform);
			if (schulform === null)
				throw new Error("Keine gültige Schulform festgelegt");
			if (schulform === Schulform.WB) {
				const orgform = WeiterbildungskollegOrganisationsformen.getByKuerzel(props.data.organisationsform);
				return orgform === null ? undefined : orgform.daten;
			}
			if ((schulform === Schulform.BK) || (schulform === Schulform.SB)) {
				const orgform = BerufskollegOrganisationsformen.getByKuerzel(props.data.organisationsform);
				return orgform === null ? undefined : orgform.daten;
			}
			const orgform = AllgemeinbildendOrganisationsformen.getByKuerzel(props.data.organisationsform);
			return orgform === null ? undefined : orgform.daten;
		},
		set: (value) => {
			emit('patch', { organisationsform: value === undefined ? null : value.kuerzel });
		}
	});

</script>
