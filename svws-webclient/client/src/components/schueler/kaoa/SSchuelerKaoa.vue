<template>
	<div class="page">
		<svws-ui-content-card class="col-span-full">
			<div class="grid justify-items-end mr-2">
				<svws-ui-button class="contentFocusField" v-if="currentMode !== Mode.ADD" title="AddButton" @click="enterAddMode"> Neuer Eintrag </svws-ui-button>
			</div>
			<!-- UI Card zum Erstellen eines neuen Eintrags  !-->
			<ui-card v-if="currentMode === Mode.ADD" :is-open="true" :collapsible="false"
				:title="'Standardelement der Berufsorientierung ' + dynamicHeader">
				<template #info v-if="(schuelerKAoADaten.idSchuljahresabschnitt !== -1) && (schuljahresabschnitt)">
					{{ schuljahresabschnittText(schuljahresabschnitt) }}
				</template>
				<!-- CREATE  !-->
				<div class="w-full grid grid-cols-2 gap-10">
					<div class="overflow-hidden flex flex-col gap-2">
						<svws-ui-select class="mt-2" title="Schuljahresabschnitt" removable :item-text="schuljahresabschnittText"
							:items="props.schuelerKaoaManager()._schuljahresabschnitteFiltered" :model-value="schuljahresabschnitt"
							@update:model-value="(v) => updateModel(1, v?.id ?? -1)" />
						<svws-ui-select v-if="schuelerKAoADaten.idSchuljahresabschnitt !== -1" class="pl-8" title="Kategorie" :item-filter="coreTypeDataFilter"
							:items="KAOAKategorie.getEintraegeBySchuljahrAndIdJahrgang(schuljahr, schuelerKAoADaten.idJahrgang)" removable :item-text
							:model-value="KAOAKategorie.data().getEintragByID(schuelerKAoADaten.idKategorie)" autocomplete
							@update:model-value="(v) => updateModel(2, v?.id ?? -1)" />
						<svws-ui-select v-if="schuelerKAoADaten.idKategorie !== -1" class="pl-16" title="Merkmal" :item-filter="coreTypeDataFilter"
							:items="KAOAMerkmal.getEintraegeBySchuljahrAndIdKategorie(schuljahr, schuelerKAoADaten.idKategorie)" removable :item-text
							:model-value="KAOAMerkmal.data().getEintragByID(schuelerKAoADaten.idMerkmal)" autocomplete
							@update:model-value="(v) => updateModel(3, v?.id ?? -1)" />
						<svws-ui-select v-if="schuelerKAoADaten.idMerkmal !== -1" class="pl-24" title="Zusatzmerkmal" :item-filter="coreTypeDataFilter"
							:items="KAOAZusatzmerkmal.getEintraegeBySchuljahrAndIdMerkmal(schuljahr, schuelerKAoADaten.idMerkmal)" removable :item-text
							:model-value="KAOAZusatzmerkmal.data().getEintragByID(schuelerKAoADaten.idZusatzmerkmal)" autocomplete
							@update:model-value="(v) => updateModel(4, v?.id ?? -1)" />
					</div>
					<div class="overflow-hidden flex flex-col gap-2">
						<div class="flex flex-col gap-2 mt-2">
							<svws-ui-select v-if="showEbene4" title="KAoAEbene4" removable :item-text :item-filter="coreTypeDataFilter" autocomplete
								:items="KAOAEbene4.getEintraegeBySchuljahrAndIdZusatzmerkmal(schuljahr, zusatzmerkmal? zusatzmerkmal.id : -1)"
								:model-value="KAOAEbene4.data().getEintragByID(schuelerKAoADaten.idEbene4 ?? -1)"
								@update:model-value="(v) => schuelerKAoADaten.idEbene4 = v?.id ?? null" />
							<svws-ui-select v-if="zusatzmerkmal && showAnschlussoption" title="KAoAAnschlussoption" removable :item-text autocomplete
								:items="KAOAAnschlussoptionen.getEintraegeBySchuljahrAndIdZusatzmerkmal(schuljahr, zusatzmerkmal.id)"
								:model-value="KAOAAnschlussoptionen.data().getEintragByID(schuelerKAoADaten.idAnschlussoption ?? -1)"
								@update:model-value="(v) => schuelerKAoADaten.idAnschlussoption = v?.id ?? null"
								:item-filter="coreTypeDataFilter" />
							<svws-ui-select v-if="showBerufsfeld" title="KAoABerufsfeld" removable :item-text autocomplete
								:items="KAOABerufsfeld.getEintraegeBySchuljahr(schuljahr)" :item-filter="coreTypeDataFilter"
								:model-value="KAOABerufsfeld.data().getEintragByID(schuelerKAoADaten.idBerufsfeld ?? -1)"
								@update:model-value="(v) => schuelerKAoADaten.idBerufsfeld = v?.id ?? null" />
							<svws-ui-text-input v-if="showFreitext" placeholder="Bemerkung" :max-len="255" :valid="(v) => validateBemerkung(v)"
								v-model="schuelerKAoADaten.bemerkung" />
						</div>
						<div class="grow" />
						<div class="flex gap-2 mt-2 justify-end items-end">
							<svws-ui-button :disabled="!validateRequiredFieldsFilled()" @click="sendRequest(Mode.ADD)">
								Speichern
							</svws-ui-button>
							<svws-ui-button type="secondary" @click="enterDefaultMode">
								Abbrechen
							</svws-ui-button>
						</div>
					</div>
				</div>
			</ui-card>

			<!-- UI Card zum Anzeigen und Patchen !-->
			<ui-card v-for="kaoaDaten of props.schuelerKaoaManager().liste.list()" :key="kaoaDaten.id"
				:title="(kaoaDaten.id === idPatchObject) ? dynamicHeader : kaoaDatenHeader(kaoaDaten)"
				:info="schuljahresabschnittTextFromKaoaDaten(kaoaDaten)">
				<!-- PATCH  !-->
				<div v-if="(idPatchObject === kaoaDaten.id) && (currentMode === Mode.PATCH)" class="w-full grid grid-cols-2 gap-10">
					<div class="overflow-hidden flex flex-col gap-2">
						<svws-ui-select class="mt-2" title="Schuljahresabschnitt" removable :item-text="schuljahresabschnittText"
							:items="props.schuelerKaoaManager()._schuljahresabschnitteFiltered" :model-value="schuljahresabschnitt"
							@update:model-value="(v) => updateModel(1, v?.id ?? -1)" />
						<svws-ui-select v-if="schuelerKAoADaten.idSchuljahresabschnitt !== -1" class="pl-8" title="Kategorie" removable :item-text
							:items="KAOAKategorie.getEintraegeBySchuljahrAndIdJahrgang(schuljahr, schuelerKAoADaten.idJahrgang)"
							:model-value="KAOAKategorie.data().getEintragByID(schuelerKAoADaten.idKategorie)"
							@update:model-value="(v) => updateModel(2, v?.id ?? -1)"
							:item-filter="coreTypeDataFilter" autocomplete />
						<svws-ui-select v-if="schuelerKAoADaten.idKategorie !== -1" class="pl-16" title="Merkmal" removable :item-text
							:items="KAOAMerkmal.getEintraegeBySchuljahrAndIdKategorie(schuljahr, schuelerKAoADaten.idKategorie)"
							:model-value="KAOAMerkmal.data().getEintragByID(schuelerKAoADaten.idMerkmal)"
							@update:model-value="(v) => updateModel(3, v?.id ?? -1)"
							:item-filter="coreTypeDataFilter" autocomplete />
						<svws-ui-select v-if="schuelerKAoADaten.idMerkmal !== -1" class="pl-24" title="Zusatzmerkmal" removable :item-text
							:items="KAOAZusatzmerkmal.getEintraegeBySchuljahrAndIdMerkmal(schuljahr, schuelerKAoADaten.idMerkmal)"
							:model-value="KAOAZusatzmerkmal.data().getEintragByID(schuelerKAoADaten.idZusatzmerkmal)"
							@update:model-value="(v) => updateModel(4, v?.id ?? -1)"
							:item-filter="coreTypeDataFilter" autocomplete />
					</div>
					<div class="overflow-hidden flex flex-col gap-2">
						<div class="flex flex-col gap-2 mt-2">
							<svws-ui-select v-if="showEbene4" title="KAoAEbene4" removable :item-text :item-filter="coreTypeDataFilter" autocomplete
								:items="KAOAEbene4.getEintraegeBySchuljahrAndIdZusatzmerkmal(schuljahr, zusatzmerkmal? zusatzmerkmal.id : -1)"
								:model-value="KAOAEbene4.data().getEintragByID(schuelerKAoADaten.idEbene4 ?? -1)"
								@update:model-value="(v) => schuelerKAoADaten.idEbene4 = v?.id ?? null" />
							<svws-ui-select v-if="zusatzmerkmal && showAnschlussoption" title="KAoAAnschlussoption" removable :item-text
								:items="KAOAAnschlussoptionen.getEintraegeBySchuljahrAndIdZusatzmerkmal(schuljahr, zusatzmerkmal.id)"
								:model-value="KAOAAnschlussoptionen.data().getEintragByID(schuelerKAoADaten.idAnschlussoption ?? -1)"
								@update:model-value="(v) => schuelerKAoADaten.idAnschlussoption = v?.id ?? null"
								:item-filter="coreTypeDataFilter" autocomplete />
							<svws-ui-select v-if="showBerufsfeld" title="KAoABerufsfeld" removable :item-text
								:items="KAOABerufsfeld.getEintraegeBySchuljahr(schuljahr)"
								:model-value="KAOABerufsfeld.data().getEintragByID(schuelerKAoADaten.idBerufsfeld ?? -1)"
								@update:model-value="(v) => schuelerKAoADaten.idBerufsfeld = v?.id ?? null"
								:item-filter="coreTypeDataFilter" autocomplete />
							<svws-ui-text-input v-if="showFreitext" placeholder="Bemerkung" :max-len="255" :valid="(v) => validateBemerkung(v)"
								v-model="schuelerKAoADaten.bemerkung" />
						</div>
						<div class="grow" />
						<div class="flex gap-2 mt-2 justify-end items-end">
							<svws-ui-button :disabled="!validateRequiredFieldsFilled()" @click="sendRequest(Mode.PATCH)">
								Änderung speichern
							</svws-ui-button>
							<svws-ui-button type="secondary" @click="enterDefaultMode">
								Abbrechen
							</svws-ui-button>
							<svws-ui-button type="danger" @click="deleteEntry(kaoaDaten.id)">
								Löschen
							</svws-ui-button>
						</div>
					</div>
				</div>

				<!-- ANZEIGEN  !-->
				<div v-if="idPatchObject !== kaoaDaten.id" class="w-full grid grid-cols-2 gap-10">
					<div class="overflow-hidden flex flex-col gap-2">
						<svws-ui-select class="mt-2" title="Schuljahresabschnitt" :item-text="schuljahresabschnittText" readonly
							:items="props.schuelerKaoaManager()._schuljahresabschnitteFiltered"
							:model-value="props.schuelerKaoaManager().schuljahresabschnitte.get(kaoaDaten.idSchuljahresabschnitt)" />
						<svws-ui-select class="pl-8" title="Kategorie" :item-text readonly removable
							:items="KAOAKategorie.getEintraegeBySchuljahrAndIdJahrgang(schuljahr, kaoaDaten.idJahrgang)"
							:model-value="KAOAKategorie.data().getEintragByID(kaoaDaten.idKategorie)" />
						<svws-ui-select class="pl-16" title="Merkmal" :item-text readonly removable
							:items="KAOAMerkmal.getEintraegeBySchuljahrAndIdKategorie(schuljahr, kaoaDaten.idKategorie)"
							:model-value="KAOAMerkmal.data().getEintragByID(kaoaDaten.idMerkmal)" />
						<svws-ui-select class="pl-24" title="Zusatzmerkmal" :item-text readonly removable
							:items="KAOAZusatzmerkmal.getEintraegeBySchuljahrAndIdMerkmal(schuljahr, kaoaDaten.idMerkmal)"
							:model-value="KAOAZusatzmerkmal.data().getEintragByID(kaoaDaten.idZusatzmerkmal)" />
					</div>
					<div class="overflow-hidden flex flex-col gap-2">
						<div class="flex flex-col gap-2 mt-2">
							<svws-ui-select v-if="kaoaDaten.idEbene4 !== null" title="KAoAEbene4" :item-text readonly removable
								:items="KAOAEbene4.getEintraegeBySchuljahrAndIdZusatzmerkmal(schuljahr, kaoaDaten.idZusatzmerkmal)"
								:model-value="KAOAEbene4.data().getEintragByID(kaoaDaten.idEbene4)" />
							<svws-ui-select v-if="kaoaDaten.idAnschlussoption !== null" title="KAoAAnschlussoption" :item-text readonly removable
								:items="KAOAAnschlussoptionen.getEintraegeBySchuljahrAndIdZusatzmerkmal(schuljahr, kaoaDaten.idZusatzmerkmal)"
								:model-value="KAOAAnschlussoptionen.data().getEintragByID(kaoaDaten.idAnschlussoption)" />
							<svws-ui-select v-if="kaoaDaten.idBerufsfeld !== null" title="KAoABerufsfeld" :item-text readonly removable
								:items="KAOABerufsfeld.getEintraegeBySchuljahr(schuljahr)"
								:model-value="KAOABerufsfeld.data().getEintragByID(kaoaDaten.idBerufsfeld)" />
							<svws-ui-text-input v-if="((kaoaDaten.bemerkung !== null) && (!JavaString.isBlank(kaoaDaten.bemerkung)))" readonly
								placeholder="Bemerkung" :model-value="kaoaDaten.bemerkung" :max-len="255" :valid="(v) => validateBemerkung(v)" />
						</div>
						<div class="grow" />
						<div class="flex gap-2 mt-2  justify-end items-end">
							<svws-ui-button v-if="idPatchObject !== kaoaDaten.id" @click="enterPatchMode(kaoaDaten)">
								Bearbeiten
							</svws-ui-button>
							<svws-ui-button type="danger" @click="deleteEntry(kaoaDaten.id)">
								Löschen
							</svws-ui-button>
						</div>
					</div>
				</div>
			</ui-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed, watch } from 'vue';
	import type { SchuelerKAoAProps } from './SSchuelerKaoaProps';
	import type {Schuljahresabschnitt, CoreTypeData, JahrgaengeKatalogEintrag, KAOAZusatzmerkmalKatalogEintrag} from "@core";
	import { JavaString, KAOAAnschlussoptionen, KAOABerufsfeld, KAOAEbene4 , Jahrgaenge, KAOAKategorie, KAOAMerkmal, KAOAZusatzmerkmal, SchuelerKAoADaten } from "@core";
	import { coreTypeDataFilter } from "~/utils/helfer";

	const props = defineProps<SchuelerKAoAProps>();
	const schuelerKAoADaten = ref<SchuelerKAoADaten>(new SchuelerKAoADaten());
	const idPatchObject = ref<number>(-1);

	const schuljahr = computed<number>(() => (schuljahresabschnitt.value?.schuljahr !== undefined) ? schuljahresabschnitt.value.schuljahr : -1);
	const jahrgang = computed<JahrgaengeKatalogEintrag | null> (
		() => Jahrgaenge.data().getWertByKuerzel(props.schuelerKaoaManager().getKuerzelJahrgangBySchuljahr(schuljahr.value))?.daten(schuljahr.value) ?? null);
	const zusatzmerkmal = computed<KAOAZusatzmerkmalKatalogEintrag | null>(() => KAOAZusatzmerkmal.data().getEintragByID(schuelerKAoADaten.value.idZusatzmerkmal) ?? null);
	const schuljahresabschnitt = computed<Schuljahresabschnitt | null>(() => {
		if (schuelerKAoADaten.value.idSchuljahresabschnitt === -1)
			return props.schuelerKaoaManager().schuljahresabschnitte.get(props.auswahl().idSchuljahresabschnitt) ?? null;
		return props.schuelerKaoaManager().schuljahresabschnitte.get(schuelerKAoADaten.value.idSchuljahresabschnitt) ?? null;
	});

	const showEbene4 = computed<boolean>(() => zusatzmerkmal.value?.optionsart ==='SBO_EBENE_4');
	const showAnschlussoption = computed<boolean>(() => zusatzmerkmal.value?.optionsart ==='ANSCHLUSSOPTION');
	const showBerufsfeld = computed<boolean>(() => zusatzmerkmal.value?.optionsart ==='BERUFSFELD');
	const showFreitext = computed<boolean>(() => (zusatzmerkmal.value?.optionsart ==='FREITEXT') || (zusatzmerkmal.value?.optionsart ==='FREITEXT_BERUF'));

	// setzt die selektierten Felder abhängig vom Ziellevel zurück
	function updateModel(targetLevel: number, value: number) {
		if (targetLevel <= 1) {
			schuelerKAoADaten.value.idSchuljahresabschnitt = value;
			schuelerKAoADaten.value.idJahrgang = jahrgang.value? jahrgang.value.id : -1;
		}
		if (targetLevel <= 2)
			schuelerKAoADaten.value.idKategorie = targetLevel === 2 ? value : -1;
		if (targetLevel <= 3)
			schuelerKAoADaten.value.idMerkmal = targetLevel === 3 ? value : -1;
		if (targetLevel <= 4)
			schuelerKAoADaten.value.idZusatzmerkmal = targetLevel === 4 ? value : -1;
		if (targetLevel >= 1) {
			schuelerKAoADaten.value.idEbene4 = null;
			schuelerKAoADaten.value.idAnschlussoption = null;
			schuelerKAoADaten.value.idBerufsfeld = null;
			schuelerKAoADaten.value.bemerkung = null;
		}
	}

	// die Werte des aktuell ausgewählten zu bearbeitenden Objekts übertragen
	function transferPatchValues(kaoaDaten: SchuelerKAoADaten) {
		Object.assign(schuelerKAoADaten.value, {
			idJahrgang: kaoaDaten.idJahrgang,
			idSchuljahresabschnitt: kaoaDaten.idSchuljahresabschnitt,
			idKategorie: kaoaDaten.idKategorie,
			idMerkmal: kaoaDaten.idMerkmal,
			idZusatzmerkmal: kaoaDaten.idZusatzmerkmal,
			idEbene4: kaoaDaten.idEbene4,
			idAnschlussoption: kaoaDaten.idAnschlussoption,
			idBerufsfeld: kaoaDaten.idBerufsfeld,
			bemerkung: kaoaDaten.bemerkung,
		});
	}

	// api call für Delete
	async function deleteEntry(id: number) {
		await props.delete(props.auswahl().id, id);
		setMode(Mode.DEFAULT);
	}

	// api call für Create und Patch
	async function sendRequest(type : Mode) {
		if (!validateRequiredFieldsFilled())
			return;
		if (type === Mode.ADD)
			await props.add(createPartialWithoutId(), props.auswahl().id);
		if (type === Mode.PATCH)
			await props.patch(createPartialWithoutId(), idPatchObject.value);
		enterDefaultMode();
	}

	// das zu übertragene Objekt darf keine Id enthalten. Diese wird im Backend vergeben.
	function createPartialWithoutId() {
		const data: Partial<SchuelerKAoADaten> = schuelerKAoADaten.value;
		delete data.id;
		return data;
	}

	// Validierung
	const optionsart = computed<string | null> (() => zusatzmerkmal.value?.optionsart ?? null);

	function validateRequiredFieldsFilled() {
		if ((schuelerKAoADaten.value.idKategorie === -1) || (schuelerKAoADaten.value.idMerkmal === -1) || (schuelerKAoADaten.value.idZusatzmerkmal === -1))
			return false;
		switch(optionsart.value) {
			case 'SBO_EBENE_4':
				return schuelerKAoADaten.value.idEbene4 !== null;
			case 'ANSCHLUSSOPTION':
				return schuelerKAoADaten.value.idAnschlussoption !== null;
			case 'BERUFSFELD':
				return schuelerKAoADaten.value.idBerufsfeld !== null;
			case 'FREITEXT':
			case 'FREITEXT_BERUF':
				return validateBemerkung(schuelerKAoADaten.value.bemerkung);
			case 'KEINE':
				return true;
			default:
				return false;
		}
	}

	function validateBemerkung(bemerkung : string | null) {
		return ((bemerkung !== null) && (!JavaString.isBlank(bemerkung))) ? (bemerkung.trim().length <= 255) : true;
	}

	// Bezeichnungen
	function itemText(i: CoreTypeData) {
		return i.kuerzel + '- ' + i.text
	}

	function schuljahresabschnittText(value: Schuljahresabschnitt) {
		return value.schuljahr > 0 ? `${value.schuljahr}/${(value.schuljahr + 1) % 100}.${value.abschnitt}` : "Abschnitt";
	}

	function schuljahresabschnittTextFromKaoaDaten(item: SchuelerKAoADaten) {
		const abschnitt = props.schuelerKaoaManager().schuljahresabschnitte.get(item.idSchuljahresabschnitt);
		return abschnitt ? schuljahresabschnittText(abschnitt) : "-";
	}

	const dynamicHeader = computed<string> (() => {
		let eintrag;
		if (schuelerKAoADaten.value.idZusatzmerkmal !== -1)
			eintrag = KAOAZusatzmerkmal.data().getEintragByID(schuelerKAoADaten.value.idZusatzmerkmal);
		else if (schuelerKAoADaten.value.idMerkmal !== -1)
			eintrag = KAOAMerkmal.data().getEintragByID(schuelerKAoADaten.value.idMerkmal);
		else if (schuelerKAoADaten.value.idKategorie !== -1)
			eintrag = KAOAKategorie.data().getEintragByID(schuelerKAoADaten.value.idKategorie);
		else if (schuelerKAoADaten.value.idSchuljahresabschnitt !== -1)
			return "SBO";
		return eintrag?.kuerzel ?? "";
	})

	function kaoaDatenHeader(kaoaDaten : SchuelerKAoADaten) {
		const kuerzel = KAOAZusatzmerkmal.data().getWertByID(kaoaDaten.idZusatzmerkmal).daten(schuljahr.value)?.kuerzel
		const text = KAOAKategorie.data().getWertByID(kaoaDaten.idKategorie).daten(schuljahr.value)?.text;
		return kuerzel + " " + text;
	}

	// Modus
	enum Mode { ADD, PATCH , DEFAULT }
	const currentMode = ref<Mode>(Mode.DEFAULT);

	function deselectEntry() {
		idPatchObject.value = -1;
	}

	function setMode(newMode: Mode) {
		return currentMode.value = newMode;
	}

	function resetSchuelerKaoaFields() {
		schuelerKAoADaten.value = new SchuelerKAoADaten();
	}

	function enterPatchMode(kaoaDaten : SchuelerKAoADaten) {
		resetSchuelerKaoaFields();
		transferPatchValues(kaoaDaten);
		setMode(Mode.PATCH);
		idPatchObject.value = kaoaDaten.id;
	}

	function enterAddMode() {
		setMode(Mode.ADD);
		deselectEntry();
		resetSchuelerKaoaFields();
		updateModel(1, schuljahresabschnitt.value ? schuljahresabschnitt.value.id : -1);
	}

	function enterDefaultMode() {
		setMode(Mode.DEFAULT);
		deselectEntry();
		resetSchuelerKaoaFields();
	}

	// Auswahl eines anderen Schuelers
	watch(props, () => {
		enterDefaultMode();
	});
</script>
