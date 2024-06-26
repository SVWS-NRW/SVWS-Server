<template>
	<div class="content">
		<svws-ui-content-card>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Klasse" :items="manager().klasseGetMenge()" :item-text="i => `${i.kuerzel}`" autocomplete statistics
					:model-value="klasse" @update:model-value="value => patch({ klassenID: ((value === undefined) || (value === null)) ? null : value.id })" />
				<svws-ui-select title="Jahrgang" :items="manager().jahrgangGetMenge()" :item-text="i => `${i.kuerzel}`" autocomplete statistics
					:model-value="jahrgang" @update:model-value="value => patch({ jahrgangID: ((value === undefined) || (value === null)) ? null : value.id })" />
				<svws-ui-text-input placeholder="Datum von" type="date" statistics
					:model-value="manager().lernabschnittGet().datumAnfang || undefined"
					@change="datumAnfang => patch({datumAnfang})" />
				<svws-ui-text-input placeholder="Datum bis" type="date" statistics
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
					<svws-ui-select title="Tutor" :items="manager().lehrerGetMenge()" :item-text="getLehrerText" autocomplete
						:model-value="tutor" @update:model-value="value => patch({ tutorID: ((value === undefined) || (value === null)) ? null : value.id })" />
					<svws-ui-select title="Sonderpädagoge" :items="manager().lehrerGetMenge()" :item-text="getLehrerText" autocomplete
						:model-value="sonderpaedagoge" @update:model-value="value => patch({ sonderpaedagogeID: ((value === undefined) || (value === null)) ? null : value.id })" />
				</div>
				<svws-ui-spacing :size="2" />
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-select title="Schulgliederung" :items="gliederungen" :item-text="i => `${i.daten.kuerzel} - ${i.daten.beschreibung}`" autocomplete statistics
						v-model="gliederung" />
					<svws-ui-text-input placeholder="Prüfungsordnung" :model-value="manager().lernabschnittGet().pruefungsOrdnung || undefined" />
					<svws-ui-select title="Organisationsform" :items="organisationsformen" :item-text="i => i.beschreibung" autocomplete statistics
						v-model="organisationsform" />
					<svws-ui-select title="Klassenart" :items="klassenarten" :item-text="i => i.daten.bezeichnung" autocomplete statistics
						v-model="klassenart" />
				</svws-ui-input-wrapper>
				<svws-ui-spacing />
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-select title="Förderschwerpunkt" :items="manager().foerderschwerpunktGetMenge()" :item-text="i => ((i === undefined) || (i.text === undefined)) ? '—' : i.text " autocomplete statistics
						v-model="foerderschwerpunkt" />
					<svws-ui-select title="Weiterer Förderschwerpunkt" :items="manager().foerderschwerpunktGetMenge()" :item-text="i => ((i === undefined) || (i.text === undefined)) ? '—' : i.text" autocomplete statistics
						v-model="foerderschwerpunkt2" />
					<svws-ui-checkbox v-model="schwerbehinderung" statistics span="full"> Schwerstbehinderung </svws-ui-checkbox>
					<svws-ui-checkbox v-model="autismus" span="full"> Autismus </svws-ui-checkbox>
					<svws-ui-checkbox v-model="aosf" span="full"> AOSF </svws-ui-checkbox>
					<svws-ui-checkbox v-model="zieldifferentesLernen" span="full"> zieldifferentes Lernen </svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { FoerderschwerpunktEintrag, JahrgangsDaten, KlassenDaten, LehrerListeEintrag, List, OrganisationsformKatalogEintrag } from "@core";
	import { AllgemeinbildendOrganisationsformen, BerufskollegOrganisationsformen, Klassenart, Schulform, Schulgliederung, ArrayList, WeiterbildungskollegOrganisationsformen, DeveloperNotificationException } from "@core";

	import type { SchuelerLernabschnittAllgemeinProps } from "./SSchuelerLernabschnittAllgemeinProps";

	const props = defineProps<SchuelerLernabschnittAllgemeinProps>();

	function getLehrerText(lehrer: LehrerListeEintrag) : string {
		return `${lehrer.nachname}, ${lehrer.vorname} (${lehrer.kuerzel})`;
	}

	const klasse = computed<KlassenDaten | undefined>(() => {
		const id = props.manager().lernabschnittGet().klassenID;
		if (id === null)
			return undefined;
		return props.manager().klasseGetByIdOrException(id);
	});

	const jahrgang = computed<JahrgangsDaten | undefined>(() => {
		const id = props.manager().lernabschnittGet().jahrgangID;
		if (id === null)
			return undefined;
		return props.manager().jahrgangGetByIdOrException(id);
	});

	const sonderpaedagoge = computed<LehrerListeEintrag | undefined>(() => {
		const id = props.manager().lernabschnittGet().sonderpaedagogeID;
		if (id === null)
			return undefined;
		return props.manager().lehrerGetByIdOrException(id);
	});

	const tutor = computed<LehrerListeEintrag | undefined>(() => {
		const id = props.manager().lernabschnittGet().tutorID;
		if (id === null)
			return undefined;
		return props.manager().lehrerGetByIdOrException(id);
	});

	const klassenlehrer = computed<LehrerListeEintrag[]>(() => {
		const k = klasse.value;
		if ((k === undefined) || (k.klassenLeitungen === null))
			return [];
		const result: LehrerListeEintrag[] = [];
		for (const lid of k.klassenLeitungen)
			result.push(props.manager().lehrerGetByIdOrException(lid));
		return result;
	});

	const foerderschwerpunkt = computed<FoerderschwerpunktEintrag | undefined>({
		get: () => {
			const id = props.manager().lernabschnittGet().foerderschwerpunkt1ID;
			if (id === null)
				return undefined;
			return props.manager().foerderschwerpunktGetByIdOrException(id);
		},
		set: (value) => void props.patch({ foerderschwerpunkt1ID: value === undefined ? null : value.id })
	});

	const foerderschwerpunkt2 = computed<FoerderschwerpunktEintrag | undefined>({
		get: () => {
			const id = props.manager().lernabschnittGet().foerderschwerpunkt2ID;
			if (id === null)
				return undefined;
			return props.manager().foerderschwerpunktGetByIdOrException(id);
		},
		set: (value) => void props.patch({ foerderschwerpunkt2ID: value === undefined ? null : value.id })
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
		const schulform = Schulform.getByKuerzel(props.schule.schulform);
		if (schulform === null)
			throw new DeveloperNotificationException("Keine gültige Schulform festgelegt");
		return Klassenart.get(schulform);
	});

	const klassenart = computed<Klassenart | undefined>({
		get: () => {
			const art = Klassenart.getByKuerzel(props.manager().lernabschnittGet().Klassenart);
			return (art === null) ? undefined : art;
		},
		set: (value) => void props.patch({ Klassenart: value === undefined ? null : value.daten.kuerzel })
	});

	const gliederungen = computed<List<Schulgliederung>>(() => {
		const schulform = Schulform.getByKuerzel(props.schule.schulform);
		if (schulform === null)
			throw new DeveloperNotificationException("Keine gültige Schulform festgelegt");
		return Schulgliederung.get(schulform);
	});

	const gliederung = computed<Schulgliederung | undefined>({
		get: () => {
			if (props.manager().lernabschnittGet().schulgliederung === null)
				return undefined;
			const gliederung = Schulgliederung.getByKuerzel(props.manager().lernabschnittGet().schulgliederung);
			return gliederung === null ? undefined : gliederung;
		},
		set: (value) => void props.patch({ schulgliederung: value === undefined || value === null ? null : value.daten.kuerzel })
	});

	const organisationsformen = computed<List<OrganisationsformKatalogEintrag>>(() => {
		const schulform = Schulform.getByKuerzel(props.schule.schulform);
		if (schulform === null)
			throw new DeveloperNotificationException("Keine gültige Schulform festgelegt");
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

	const organisationsform = computed<OrganisationsformKatalogEintrag | undefined>({
		get: () => {
			if (props.manager().lernabschnittGet().organisationsform === null)
				return undefined;
			const schulform = Schulform.getByKuerzel(props.schule.schulform);
			if (schulform === null)
				throw new DeveloperNotificationException("Keine gültige Schulform festgelegt");
			if (schulform === Schulform.WB) {
				const orgform = WeiterbildungskollegOrganisationsformen.getByKuerzel(props.manager().lernabschnittGet().organisationsform);
				return orgform === null ? undefined : orgform.daten;
			}
			if ((schulform === Schulform.BK) || (schulform === Schulform.SB)) {
				const orgform = BerufskollegOrganisationsformen.getByKuerzel(props.manager().lernabschnittGet().organisationsform);
				return orgform === null ? undefined : orgform.daten;
			}
			const orgform = AllgemeinbildendOrganisationsformen.getByKuerzel(props.manager().lernabschnittGet().organisationsform);
			return orgform === null ? undefined : orgform.daten;
		},
		set: (value) => {
			void props.patch({ organisationsform: value === undefined ? null : value.kuerzel });
		}
	});

</script>


<style lang="postcss" scoped>

	.content {
		@apply w-full h-full grid grid-cols-2;
	}

</style>
