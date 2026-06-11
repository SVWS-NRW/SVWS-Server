<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<div class="flex flex-row gap-4 mb-4">
				<ui-select label="Klasse"
					v-model="model.klasse.value"
					:manager="klasseManager"
					:validation="() => model.getFehler('klassenID')"
					:disabled="!hatUpdateKompetenz" statistics required :removable="false" />
				<ui-select label="Jahrgang"
					v-model="model.jahrgang.value"
					:manager="jahrgangManager"
					:validation="() => model.getFehler('jahrgangID')"
					:disabled="!hatUpdateKompetenz" statistics required :removable="false" />
				<ui-select label="EP-Jahr"
					v-model="model.epJahr.value"
					:manager="epJahrManager"
					:validation="() => model.getFehler('epJahre')"
					:disabled="!hatUpdateKompetenz" statistics required :removable="false" />
			</div>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Datum von" type="date"
					v-model="model.proxy.datumAnfang"
					:disabled="!hatUpdateKompetenz" />
				<svws-ui-text-input placeholder="Datum bis" type="date"
					v-model="model.proxy.datumEnde"
					:disabled="!hatUpdateKompetenz" />
				<svws-ui-spacing />
				<div>
					<span class="font-bold" :class="{'opacity-50': !klassenlehrer.length}"> Klassenlehrer </span>
					<span v-if="!klassenlehrer.length"> — Keine Daten vorhanden.</span>
					<div v-else class="flex flex-col leading-tight text-base">
						<span v-for="kl in klassenlehrer" :key="kl.id"> {{ getLehrerText(kl) }} </span>
					</div>
				</div>
				<div class="flex flex-col gap-3">
					<ui-select label="Tutor"
						v-model="model.tutor.value"
						:manager="tutorManager"
						:disabled="!hatUpdateKompetenz" />
					<ui-select label="Sonderpädagoge"
						v-model="model.sonderpaedagoge.value"
						:manager="sonderpaedagogeManager"
						:disabled="!hatUpdateKompetenz" />
				</div>
				<svws-ui-spacing :size="2" />
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Schulgliederung"
						v-model="model.gliederung.value"
						:manager="gliederungManager"
						:validation="() => model.getFehler('schulgliederung')"
						:disabled="!hatUpdateKompetenz" statistics required :removable="false" />
					<svws-ui-text-input placeholder="Prüfungsordnung"
						v-model="model.proxy.pruefungsOrdnung"
						:validation="() => model.getFehler('pruefungsOrdnung')"
						:disabled="!hatUpdateKompetenz" required :max-len="20" />
					<ui-select label="Organisationsform"
						v-model="model.organisationsform.value"
						:manager="organisationsformManager"
						:validation="() => model.getFehler('organisationsform')"
						:disabled="!hatUpdateKompetenz" statistics required :removable="false" />
					<ui-select label="Klassenart"
						v-model="model.klassenart.value"
						:manager="klassenartManager"
						:validation="() => model.getFehler('Klassenart')"
						:disabled="!hatUpdateKompetenz" statistics required :removable="false" />
				</svws-ui-input-wrapper>
				<svws-ui-spacing />
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Bilingualer Zweig"
						v-model="model.bilingualerZweig.value"
						:manager="bilingualerZweigManager"
						:disabled="!hatUpdateKompetenz" />
				</svws-ui-input-wrapper>
				<svws-ui-spacing />
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Förderschwerpunkt"
						v-model="model.foerderschwerpunkt.value"
						:manager="foerderschwerpunktManager"
						:disabled="!hatUpdateKompetenz" statistics />
					<ui-select label="Weiterer Förderschwerpunkt"
						v-model="model.foerderschwerpunkt2.value"
						:manager="foerderschwerpunktManager"
						:disabled="!hatUpdateKompetenz" statistics />
					<svws-ui-checkbox :disabled="!hatUpdateKompetenz" v-model="model.proxy.hatSchwerbehinderungsNachweis" statistics span="full">Schwerstbehinderung</svws-ui-checkbox>
					<svws-ui-checkbox :disabled="!hatUpdateKompetenz" v-model="model.proxy.hatAutismus" span="full"> Autismus </svws-ui-checkbox>
					<svws-ui-checkbox :disabled="!hatUpdateKompetenz" v-model="model.proxy.hatAOSF" span="full"> AOSF </svws-ui-checkbox>
					<svws-ui-checkbox :disabled="!hatUpdateKompetenz" v-model="model.proxy.hatZieldifferentenUnterricht" span="full"> Zieldifferentes Lernen </svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { FoerderschwerpunktEintrag, JahrgangsDaten, KlassenDaten, LehrerListeEintrag, OrganisationsformKatalogEintrag } from
	"@core";
	import { BilingualeSprache, Foerderschwerpunkt, Klassenart, Schulgliederung, BenutzerKompetenz, PrimarstufeSchuleingangsphaseBesuchsjahre } from "@core";
	import { CoreTypeSelectManager, SelectManager, useSchuleState } from '@ui';
	import type { SchuelerLernabschnittAllgemeinProps } from "./SchuelerLernabschnittAllgemeinProps";
	import { SchuelerLernabschnittAllgemeinModelProxy } from "./modelproxy/SchuelerLernabschnittAllgemeinModelProxy";

	const props = defineProps<SchuelerLernabschnittAllgemeinProps>();
	const schuleState = useSchuleState();

	const schuljahr = computed<number>(() => props.manager().schuljahrGet());

	const model = new SchuelerLernabschnittAllgemeinModelProxy(
		() => props.manager().lernabschnittGet(),
		props.manager,
		() => schuleState.schulform,
		async (data) => {
			await props.patch(data);
			return true;
		}
	);

	const klasseManager = new SelectManager<KlassenDaten>({
		options: computed(() => props.manager().klasseGetMenge()),
		optionDisplayText: i => i.kuerzel ?? '—',
		selectionDisplayText: i => i.kuerzel ?? '—',
	});

	const jahrgangManager = new SelectManager<JahrgangsDaten>({
		options: computed(() => props.manager().jahrgangGetMenge()),
		optionDisplayText: i => i.kuerzel ?? '—',
		selectionDisplayText: i => i.kuerzel ?? '—',
	});

	const epJahrManager = new CoreTypeSelectManager({
		clazz: PrimarstufeSchuleingangsphaseBesuchsjahre.class,
		schuljahr,
		schulformen: computed(() => schuleState.schulform),
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const tutorManager = new SelectManager<LehrerListeEintrag>({
		options: computed(() => props.manager().lehrerGetMenge()),
		optionDisplayText: getLehrerText,
		selectionDisplayText: getLehrerText,
	});

	const sonderpaedagogeManager = new SelectManager<LehrerListeEintrag>({
		options: computed(() => props.manager().lehrerGetMenge()),
		optionDisplayText: getLehrerText,
		selectionDisplayText: getLehrerText,
	});

	const organisationsformManager = new SelectManager<OrganisationsformKatalogEintrag>({
		options: computed(() => model.organisationsformen.value),
		optionDisplayText: i => i.text,
		selectionDisplayText: i => i.text,
	});

	const foerderschwerpunktManager = new SelectManager<FoerderschwerpunktEintrag>({
		options: computed(() => props.manager().foerderschwerpunktGetMenge()),
		optionDisplayText: textFoerderschwerpunkt,
		selectionDisplayText: textFoerderschwerpunkt,
	});

	const gliederungManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr,
		schulformen: computed(() => schuleState.schulform),
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const klassenartManager = new CoreTypeSelectManager({
		clazz: Klassenart.class,
		schuljahr,
		schulformen: computed(() => schuleState.schulform),
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const bilingualerZweigManager = new CoreTypeSelectManager({
		clazz: BilingualeSprache.class,
		schuljahr,
		schulformen: computed(() => schuleState.schulform),
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const hatUpdateKompetenz = computed<boolean>(() => {
		return (props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN))
			|| ((props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN))
				&& props.benutzerKompetenzenKlassen.has(props.schuelerListeManager().auswahl().idKlasse));
	});

	function getLehrerText(lehrer: LehrerListeEintrag): string {
		return `${lehrer.nachname}, ${lehrer.vorname} (${lehrer.kuerzel})`;
	}

	const klassenlehrer = computed<LehrerListeEintrag[]>(() => {
		const k = model.klasse.value;
		if (k === null) {
			return [];
		}
		const result: LehrerListeEintrag[] = [];
		for (const lid of k.klassenLeitungen) {
			result.push(props.manager().lehrerGetByIdOrException(lid));
		}
		return result;
	});

	function textFoerderschwerpunkt(value: FoerderschwerpunktEintrag | null): string {
		if (!value) {
			return "";
		}
		const wert = Foerderschwerpunkt.data().getWertBySchluessel(value.kuerzelStatistik);
		return wert?.daten(schuljahr.value)?.text ?? "";

	}

</script>
