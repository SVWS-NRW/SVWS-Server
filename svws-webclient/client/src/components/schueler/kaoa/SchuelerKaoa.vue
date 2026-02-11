<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe>
			<hilfe-schueler-kaoa />
		</svws-ui-modal-hilfe>
	</Teleport>


	<div class="w-full overflow-y-auto">
		<div class="page page-grid-cards">
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-content-card class="col-span-full">
					<!-- Anlegen Button !-->
					<div class="grid justify-items-end mr-2 pb-4">
						<svws-ui-button title="AddButton" class="contentFocusField min-h-8"
							@click="enterAddMode"
							:disabled="!hatKompetenzUpdate || isAddMode">
							<span class="icon i-ri-chat-new-line" />
							<span class="ml-2">Neuen Eintrag anlegen</span>
						</svws-ui-button>
					</div>

					<!-- UI Card zum Erstellen eines neuen Eintrags  !-->
					<div v-if="isAddMode" class="pb-4">
						<ui-card :title="cardTitle(null)"
							:is-open="true" :collapsible="false">
							<template #info v-if="(data.idSchuljahresabschnitt !== -1) && (schuljahresabschnitt)">
								{{ schuljahresabschnittText(schuljahresabschnitt) }}
							</template>
							<kaoa-form :data :manager :auswahl :readonly="!hatKompetenzUpdate" />
							<template #buttonFooterRight>
								<div class="mt-7 flex flex-row gap-4 justify-end">
									<svws-ui-button type="secondary" @click="enterDefaultMode">
										Abbrechen
									</svws-ui-button>
									<svws-ui-button @click="addEntry"
										:disabled="!validateRequiredFieldsFilled() || !hatKompetenzUpdate">
										Speichern
									</svws-ui-button>
								</div>
							</template>
						</ui-card>
					</div>

					<!-- UI Cards zum Anzeigen und Patchen !-->
					<div class="space-y-2!">
						<ui-card v-for="kaoaDaten of props.manager().kAoADatenById.values()" :key="kaoaDaten.id"
							:title="cardTitle(kaoaDaten)"
							:info="schuljahresabschnittTextFromKaoaDaten(kaoaDaten)"
							:collapsible="idPatchObject !== kaoaDaten.id">
							<kaoa-form :data="idPatchObject === kaoaDaten.id ? data : kaoaDaten"
								:manager
								:auswahl
								:readonly="isEntryReadonly(kaoaDaten)" />
							<template #buttonFooterRight>
								<div class="mt-7 flex flex-row gap-4 justify-end">
									<svws-ui-button v-if="idPatchObject === kaoaDaten.id"
										@click="patchEntry"
										:disabled="!validateRequiredFieldsFilled() || !hatKompetenzUpdate">
										Änderung speichern
									</svws-ui-button>
									<svws-ui-button v-if="idPatchObject === kaoaDaten.id" type="secondary" @click="enterDefaultMode">
										Abbrechen
									</svws-ui-button>
									<svws-ui-button v-if="idPatchObject !== kaoaDaten.id" @click="enterPatchMode(kaoaDaten)">
										Bearbeiten
									</svws-ui-button>
									<svws-ui-button type="danger" @click="deleteEntry(kaoaDaten.id)" :disabled="!hatKompetenzUpdate">
										Löschen
									</svws-ui-button>
								</div>
							</template>
						</ui-card>
					</div>
				</svws-ui-content-card>
			</svws-ui-input-wrapper>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerKAoAProps } from './SchuelerKaoaProps';
	import type { Schuljahresabschnitt } from "@core";
	import { BenutzerKompetenz, Jahrgaenge, KAOAKategorie, KAOAMerkmal, KAOAZusatzmerkmal, SchuelerKAoADaten } from "@core";
	import { computed, ref, watch } from 'vue';
	import { optionalInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<SchuelerKAoAProps>();

	const data = ref<SchuelerKAoADaten>(new SchuelerKAoADaten());
	const idPatchObject = ref<number>(-1);

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN));
	const schuljahr = computed<number>(() => (schuljahresabschnitt.value?.schuljahr === undefined) ? -1 : schuljahresabschnitt.value.schuljahr);
	const isAddMode = computed<boolean>(() => currentMode.value === Mode.ADD);
	const schuljahresabschnitt = computed<Schuljahresabschnitt | null>(() => {
		const idSchuljahresabschnitt = (data.value.idSchuljahresabschnitt === -1) ? props.auswahl().idSchuljahresabschnitt : data.value.idSchuljahresabschnitt;
		return props.manager().schuljahresabschnitteById.get(idSchuljahresabschnitt) ?? null;
	});

	function isEntryReadonly(kaoaDaten: SchuelerKAoADaten): boolean {
		return idPatchObject.value !== kaoaDaten.id || !hatKompetenzUpdate.value;
	}

	// --- validate ---

	function validateRequiredFieldsFilled() {
		if ((data.value.idKategorie === -1) || (data.value.idMerkmal === -1) || (data.value.idZusatzmerkmal === -1)) {
			return false;
		}
		const optionsart = KAOAZusatzmerkmal.data().getEintragByID(data.value.idZusatzmerkmal)?.optionsart ?? null;
		switch (optionsart) {
			case 'SBO_EBENE_4':
				return data.value.idEbene4 !== null;
			case 'ANSCHLUSSOPTION':
				return data.value.idAnschlussoption !== null;
			case 'BERUFSFELD':
				return data.value.idBerufsfeld !== null;
			case 'FREITEXT':
			case 'FREITEXT_BERUF':
				return optionalInputIsValid(data.value.bemerkung, 255);
			case 'KEINE':
				return true;
			default:
				return false;
		}
	}

	// --- header ---

	function schuljahresabschnittText(value: Schuljahresabschnitt) {
		return value.schuljahr > 0 ? `${value.schuljahr}/${(value.schuljahr + 1) % 100}.${value.abschnitt}` : "Abschnitt";
	}

	function schuljahresabschnittTextFromKaoaDaten(item: SchuelerKAoADaten) {
		const abschnitt = props.manager().schuljahresabschnitteById.get(item.idSchuljahresabschnitt);
		return abschnitt === undefined ? "-" : schuljahresabschnittText(abschnitt);
	}

	function cardTitle(kaoaDaten: SchuelerKAoADaten | null) {
		return getKuerzel(kaoaDaten) + " " + getBezeichnung(kaoaDaten);
	}

	function getKuerzel(kaoaDaten: SchuelerKAoADaten | null) {
		let eintrag;
		if ((kaoaDaten === null) || (kaoaDaten.id === idPatchObject.value)) {
			if (data.value.idZusatzmerkmal !== -1) {
				eintrag = KAOAZusatzmerkmal.data().getEintragByID(data.value.idZusatzmerkmal);
			} else if (data.value.idMerkmal !== -1) {
				eintrag = KAOAMerkmal.data().getEintragByID(data.value.idMerkmal);
			} else if (data.value.idKategorie !== -1) {
				eintrag = KAOAKategorie.data().getEintragByID(data.value.idKategorie);
			}
		} else {
			eintrag = KAOAZusatzmerkmal.data().getWertByID(kaoaDaten.idZusatzmerkmal).daten(schuljahr.value);
		}

		return eintrag?.kuerzel ?? "SBO";
	}

	function getBezeichnung(kaoaDaten: SchuelerKAoADaten | null) {
		const idKategorie = (kaoaDaten !== null) ? kaoaDaten.idKategorie : data.value.idKategorie;
		return (idKategorie !== -1) ? KAOAKategorie.data().getWertByID(idKategorie).daten(schuljahr.value)?.text : "";
	}

	// --- api ---

	async function deleteEntry(id: number) {
		await props.delete(props.auswahl().id, id);
		enterDefaultMode();
	}
	async function patchEntry() {
		const { id, ...partialData } = data.value;
		if (validateRequiredFieldsFilled()) {
			await props.patch(partialData, idPatchObject.value);
		}
		enterDefaultMode();
	}
	async function addEntry() {
		const { id, ...partialData } = data.value;
		if (validateRequiredFieldsFilled()) {
			await props.add(partialData, props.auswahl().id);
		}
		enterDefaultMode();
	}

	// --- Mode ---

	enum Mode { ADD, PATCH, DEFAULT }
	const currentMode = ref<Mode>(Mode.DEFAULT);

	function deselectEntry() {
		idPatchObject.value = -1;
	}

	function setMode(newMode: Mode) {
		currentMode.value = newMode;
	}

	function resetDataModel() {
		data.value = new SchuelerKAoADaten();
	}

	function enterPatchMode(kaoaDaten: SchuelerKAoADaten) {
		resetDataModel();
		transferPatchValues(kaoaDaten);
		setMode(Mode.PATCH);
		idPatchObject.value = kaoaDaten.id;
	}

	function enterAddMode() {
		setMode(Mode.ADD);
		deselectEntry();
		resetDataModel();
		const kuerzelJahrgang = props.manager().lernabschnitteBySchuljahr.get(schuljahr.value)?.jahrgang ?? '';
		const jahrgang = Jahrgaenge.data().getWertByKuerzel(kuerzelJahrgang)?.daten(schuljahr.value) ?? null;
		data.value.idJahrgang = jahrgang?.id ?? -1;
		data.value.idSchuljahresabschnitt = schuljahresabschnitt.value?.id ?? -1;
	}

	function enterDefaultMode() {
		setMode(Mode.DEFAULT);
		deselectEntry();
		resetDataModel();
	}

	// die Werte des aktuell ausgewählten zu bearbeitenden Objekts übertragen
	function transferPatchValues(kaoaDaten: SchuelerKAoADaten) {
		Object.assign(data.value, {
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

	// Auswahl eines anderen Schuelers
	watch(props, () => {
		enterDefaultMode();
	});

</script>
