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
				<ui-select v-if="istPrimarSchulform" label="EP-Jahre"
					v-model="model.epJahre.value"
					:manager="epJahrManager"
					:validation="() => model.getFehler('idEpJahre')"
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
						:validation="() => model.getFehler('idSchulgliederung')"
						:disabled="!hatUpdateKompetenz" statistics required :removable="false" />
					<svws-ui-text-input placeholder="Prüfungsordnung"
						v-model="model.proxy.pruefungsOrdnung"
						:validation="() => model.getFehler('pruefungsOrdnung')"
						:disabled="!hatUpdateKompetenz" required :max-len="20" />
					<ui-select label="Organisationsform"
						v-model="model.organisationsform.value"
						:manager="organisationsformManager"
						:validation="() => model.getFehler('idOrganisationsform')"
						:disabled="!hatUpdateKompetenz" statistics required :removable="false" />
					<ui-select label="Klassenart"
						v-model="model.klassenart.value"
						:manager="klassenartManager"
						:validation="() => model.getFehler('idKlassenart')"
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
	import type { SchuelerLernabschnittAllgemeinProps } from "./SchuelerLernabschnittAllgemeinProps";
	import { SchuelerLernabschnittAllgemeinModelProxy } from "./modelproxy/SchuelerLernabschnittAllgemeinModelProxy";
	import type { KlassenDaten } from '@core/asd/data/klassen/KlassenDaten';
	import type { SchuelerLernabschnittsdaten } from '@core/asd/data/schueler/SchuelerLernabschnittsdaten';
	import type { OrganisationsformKatalogEintrag } from '@core/asd/data/schule/OrganisationsformKatalogEintrag';
	import { BilingualeSprache } from '@core/asd/types/fach/BilingualeSprache';
	import { PrimarstufeSchuleingangsphaseBesuchsjahre } from '@core/asd/types/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahre';
	import { Klassenart } from '@core/asd/types/klassen/Klassenart';
	import { Foerderschwerpunkt } from '@core/asd/types/schule/Foerderschwerpunkt';
	import { Schulform } from '@core/asd/types/schule/Schulform';
	import { Schulgliederung } from '@core/asd/types/schule/Schulgliederung';
	import type { JahrgangsDaten } from '@core/core/data/jahrgang/JahrgangsDaten';
	import type { LehrerListeEintrag } from '@core/core/data/lehrer/LehrerListeEintrag';
	import type { FoerderschwerpunktEintrag } from '@core/core/data/schule/FoerderschwerpunktEintrag';
	import { BenutzerKompetenz } from '@core/core/types/benutzer/BenutzerKompetenz';
	import { useBenutzerState } from '@ui/states/BenutzerState';
	import { useSchuleState } from '@ui/states/SchuleState';
	import { CoreTypeSelectManager } from '@ui/ui/controls/select/manager/CoreTypeSelectManager';
	import { SelectManager } from '@ui/ui/controls/select/manager/SelectManager';

	const props = defineProps<SchuelerLernabschnittAllgemeinProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const primarschulformen = new Set<Schulform>([
		Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.PS, Schulform.S, Schulform.KS, Schulform.V,
	]);

	const hatUpdateKompetenz = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN)
		|| ((benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN))
			&& benutzerState.kompetenzenKlasse.has(props.manager().schuelerGet().idKlasse))
	);
	const schulform = computed<Schulform>(() => schuleState.schulform);
	const istPrimarSchulform = computed<boolean>(() => primarschulformen.has(schulform.value));
	const schuljahr = computed<number>(() => schuleState.abschnitt.schuljahr);
	const klassen = computed<Iterable<KlassenDaten>>(() => props.manager().klasseGetMenge());
	const jahrgaenge = computed<Iterable<JahrgangsDaten>>(() => props.manager().jahrgangGetMenge());
	const lehrer = computed<Iterable<LehrerListeEintrag>>(() => props.manager().lehrerGetMenge());
	const foerderschwerpunkte = computed<Iterable<FoerderschwerpunktEintrag>>(() => props.manager().foerderschwerpunktGetMenge());

	const klassenlehrer = computed<LehrerListeEintrag[]>(() => {
		if (model.klasse.value === null) {
			return [];
		}
		const klassenleitungen: LehrerListeEintrag[] = [];
		for (const idLehrer of model.klasse.value.klassenLeitungen) {
			klassenleitungen.push(props.manager().lehrerGetByIdOrException(idLehrer));
		}
		return klassenleitungen;
	});

	const model = new SchuelerLernabschnittAllgemeinModelProxy(
		() => props.manager().lernabschnittGet(),
		props.manager,
		() => schulform.value,
		() => schuljahr.value,
		async (data: Partial<SchuelerLernabschnittsdaten>) => {
			await props.patch(data);
			return true;
		}
	);

	const klasseManager = new SelectManager<KlassenDaten>({
		options: klassen,
		optionDisplayText: i => i.kuerzel ?? '—',
		selectionDisplayText: i => i.kuerzel ?? '—',
	});

	const jahrgangManager = new SelectManager<JahrgangsDaten>({
		options: jahrgaenge,
		optionDisplayText: i => i.kuerzel ?? '—',
		selectionDisplayText: i => i.kuerzel ?? '—',
	});

	const epJahrManager = new CoreTypeSelectManager({
		clazz: PrimarstufeSchuleingangsphaseBesuchsjahre.class,
		schuljahr,
		schulformen: schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const tutorManager = new SelectManager<LehrerListeEintrag>({
		options: lehrer,
		optionDisplayText: getLehrerText,
		selectionDisplayText: getLehrerText,
	});

	const sonderpaedagogeManager = new SelectManager<LehrerListeEintrag>({
		options: lehrer,
		optionDisplayText: getLehrerText,
		selectionDisplayText: getLehrerText,
	});

	const organisationsformManager = new SelectManager<OrganisationsformKatalogEintrag>({
		options: model.organisationsformen,
		optionDisplayText: i => i.text,
		selectionDisplayText: i => i.text,
	});

	const foerderschwerpunktManager = new SelectManager<FoerderschwerpunktEintrag>({
		options: foerderschwerpunkte,
		optionDisplayText: textFoerderschwerpunkt,
		selectionDisplayText: textFoerderschwerpunkt,
	});

	const gliederungManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr,
		schulformen: schulform,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const klassenartManager = new CoreTypeSelectManager({
		clazz: Klassenart.class,
		schuljahr,
		schulformen: schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const bilingualerZweigManager = new CoreTypeSelectManager({
		clazz: BilingualeSprache.class,
		schuljahr,
		schulformen: schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	function getLehrerText(lehrer: LehrerListeEintrag): string {
		return `${lehrer.nachname}, ${lehrer.vorname} (${lehrer.kuerzel})`;
	}

	function textFoerderschwerpunkt(value: FoerderschwerpunktEintrag | null): string {
		if (value === null) {
			return "";
		}
		return Foerderschwerpunkt.data().getEintragBySchuljahrUndSchluessel(schuljahr.value, value.kuerzelStatistik)?.text ?? "";
	}

</script>
