<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-schueler-erziehungsberechtigte /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Erziehungsberechtigte" class="col-span-full">
			<svws-ui-table class="contentFocusField" :items="sortedData" :columns :no-data="data().size() === 0" clickable :clicked="erzieher"
				@update:clicked="value => erzieher = value" v-model="selectedErz" :selectable="true" focus-first-element>
				<template #header(erhaeltAnschreiben)>
					<svws-ui-tooltip>
						<span class="icon i-ri-mail-send-line" />
						<template #content>
							Erhält Anschreiben
						</template>
					</svws-ui-tooltip>
				</template>
				<template #cell(idErzieherArt)="{ value }">
					{{ erzieherart ? mapErzieherarten.get(value)?.bezeichnung : '' }}
				</template>
				<template #cell(name)="{ rowData }">
					{{ rowData.vorname }} {{ rowData.nachname }}
				</template>
				<template #cell(email)="{ value: eMail }">
					{{ eMail ? eMail : '—' }}
				</template>
				<template #cell(adresse)="{ rowData }">
					{{ strasse(rowData) }}{{ rowData.wohnortID && mapOrte?.get(rowData.wohnortID) ? `, ${mapOrte.get(rowData.wohnortID)?.plz} ${mapOrte?.get(rowData.wohnortID)?.ortsname}` : '' }}
				</template>
				<template #cell(erhaeltAnschreiben)="{ value: erhaeltAnschreiben }">
					{{ erhaeltAnschreiben ? '&check;' : '&times;' }}
				</template>
				<template #cell(actions)="{ rowData }">
					<!-- Button zum Hinzufügen eines Erziehers an der zweiten Position, wird nur angezeigt wenn noch keine zweite Position in einem Eintrag existiert -->
					<svws-ui-button v-if="isSuffix1(rowData.id) && !hasSuffix2(rowData.id)"
						@click.stop="openModalForPos2(rowData)" :disabled="!hatKompetenzUpdate">
						+
					</svws-ui-button>
				</template>
				<template #actions>
					<svws-ui-button @click="deleteErzieherRequest" type="trash" :disabled="(selectedErz.length === 0) || (!hatKompetenzUpdate)" />
					<svws-ui-button @click="addModal" type="icon" title="Erziehungsberechtigten hinzufügen" :disabled="!hatKompetenzUpdate">
						<span class="icon i-ri-add-line" />
					</svws-ui-button>
				</template>
			</svws-ui-table>
			<svws-ui-content-card v-if="erzieher !== undefined" :title="(erzieher.vorname !== null) || (erzieher.nachname !== null) ?
				`Daten zu ${erzieher.vorname ? erzieher.vorname + ' ' : '' }${erzieher.nachname}` : 'Daten zur Person'" class="col-span-full mt-16 lg:mt-20">
				<template #actions>
					<svws-ui-checkbox class="mr-2" :model-value="erzieher.erhaeltAnschreiben === true" @update:model-value="erhaeltAnschreiben => (erzieher !== undefined) &&
						patchErzieher({ erhaeltAnschreiben }, erzieher.id)" :readonly>
						Erhält Anschreiben
					</svws-ui-checkbox>
				</template>
				<!-- Felder zum Patchen der Erzieherdaten -->
				<svws-ui-input-wrapper :grid="4">
					<ui-select label="Erzieherart" v-model="erzieherart" :manager="erzieherartenManager" :removable="false" :readonly searchable />
					<svws-ui-text-input placeholder="Anrede" :model-value="erzieher?.anrede" @change="anrede=>(erzieher !== undefined) &&
						patchErzieher({ anrede }, erzieher.id)" type="text" :readonly />
					<svws-ui-text-input placeholder="Titel" :model-value="erzieher?.titel" @change="titel=>(erzieher !== undefined) &&
						patchErzieher({ titel }, erzieher.id)" type="text" :readonly />
					<svws-ui-spacing />
					<svws-ui-text-input placeholder="Name" :model-value="erzieher?.nachname" @change="nachname=>(erzieher !== undefined) &&
						patchErzieher({ nachname }, erzieher.id)" type="text" :readonly />
					<svws-ui-text-input placeholder="Vorname" :model-value="erzieher?.vorname" @change="vorname=>(erzieher !== undefined) &&
						patchErzieher({ vorname }, erzieher.id)" type="text" :readonly />
					<svws-ui-text-input placeholder="E-Mail Adresse" :model-value="erzieher?.eMail" @change="eMail=>(erzieher !== undefined) &&
						patchErzieher({ eMail }, erzieher.id)" type="email" verify-email :readonly />
					<svws-ui-spacing />
					<ui-select label="Staatsangehörigkeit" v-model="ersterErzStaatsangehoerigkeit" :manager="staatsangehoerigkeitenManager" :readonly searchable />
					<svws-ui-text-input placeholder="Straße und Hausnummer" :model-value="strasse(erzieher)" @change="patchStrasse" type="text" :readonly />
					<ui-select label="Wohnort" v-model="erzWohnort" :manager="wohnortManager" :readonly searchable />
					<ui-select label="Ortsteil" v-model="erzOrtsteil" :manager="erzOrtsteilManager" :readonly="!erzieher.wohnortID" searchable />
					<svws-ui-spacing />
					<svws-ui-textarea-input placeholder="Bemerkungen" :model-value="erzieher?.bemerkungen" span="full" autoresize
						@change="bemerkungen => (erzieher !== undefined) && patchErzieher({ bemerkungen: bemerkungen === null ? '' : bemerkungen }, erzieher.id)"
						:readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<!-- Modal zum Hinzufügen eines zweiten Erziehungsberechtigten (Position 2) über den "+"-Button -->
			<svws-ui-modal :show="showPatchPosModal" @update:show="closeModal">
				<template #modalTitle>Einen zweiten Erziehungsberechtigten hinzufügen</template>
				<template #modalContent>
					<svws-ui-input-wrapper :grid="2" class="text-left">
						<svws-ui-text-input placeholder="Anrede" v-model="zweiterErz.anrede" type="text" :readonly />
						<svws-ui-text-input placeholder="Titel" v-model="zweiterErz.titel" type="text" :readonly />
						<svws-ui-text-input placeholder="Vorname" v-model="zweiterErz.vorname" type="text" required :readonly />
						<svws-ui-text-input placeholder="Nachname" v-model="zweiterErz.nachname" type="text" required :readonly />
						<svws-ui-text-input placeholder="E-Mail Adresse" v-model="zweiterErz.eMail" type="email" verify-email :readonly />
						<ui-select label="Staatsangehörigkeit" v-model="zweiteErzStaatsangehoerigkeit" :manager="staatsangehoerigkeitenManager" :readonly searchable />
					</svws-ui-input-wrapper>
					<div class="mt-7 flex flex-row gap-4 justify-end">
						<svws-ui-button type="secondary" @click="showPatchPosModal = false">Abbrechen</svws-ui-button>
						<svws-ui-button @click="saveSecondErzieher" :disabled="(!stringIsValid(zweiterErz.vorname, true, 120))
							|| (!stringIsValid(zweiterErz.nachname, true, 120)) || (!hatKompetenzUpdate)">
							Zweiten Erzieher speichern
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-modal>
			<SSchuelerErziehungsberechtigteModal v-model:erster-erz="ersterErz"
				v-model:zweiter-erz="zweiterErz"
				:show-modal="showModal"
				:map-erzieherarten="mapErzieherarten"
				:hat-kompetenz-update="hatKompetenzUpdate"
				:ist-erster-erz-gespeichert="istErsterErzGespeichert"
				:map-orte="mapOrte"
				:map-ortsteile="mapOrtsteile"
				:schuljahr="schuljahr"
				@close-modal="closeModal"
				@send-request="sendRequest"
				@save-and-show-second="saveAndShowSecondForm"
				@save-second-erzieher="saveSecondErzieher" />
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";
	import type { DataTableColumn } from "@ui";
	import { CoreTypeSelectManager, SelectManager } from "@ui";
	import type { SchuelerErziehungsberechtigteProps } from "./SSchuelerErziehungsberechtigteProps";
	import type { NationalitaetenKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
	import { AdressenUtils, Nationalitaeten, ErzieherStammdaten, JavaString, ArrayList, BenutzerKompetenz } from "@core";
	import { orte_sort, erzieherArtSort, ortsteilSort } from "~/utils/helfer";

	const props = defineProps<SchuelerErziehungsberechtigteProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

	const schuljahr = computed<number>(() => props.aktAbschnitt.schuljahr);

	const readonly = computed(() => !hatKompetenzUpdate.value);

	const erzieher = ref<ErzieherStammdaten | undefined>();

	const selectedErz = ref<ErzieherStammdaten[]>([]);

	const ersterErz = ref<ErzieherStammdaten>(new ErzieherStammdaten())
	const zweiterErz = ref<ErzieherStammdaten>(new ErzieherStammdaten());

	enum Mode { ADD, PATCH, PATCH_POS2, DEFAULT }
	const currentMode = ref<Mode>(Mode.DEFAULT);
	const showModal = ref<boolean>(false);
	const showPatchPosModal = ref<boolean>(false);
	const istErsterErzGespeichert = ref(false);

	const sortedData = computed(() => {
		const list = Array.from(props.data());
		return list.sort((a, b) => {
			const ersteErzId = Math.floor(a.id / 10);
			const zweiteErzId = Math.floor(b.id / 10);
			if (ersteErzId !== zweiteErzId)
				return ersteErzId - zweiteErzId;
			return a.id - b.id;
		});
	});

	const erzieherarten = computed(() => props.mapErzieherarten.values());

	const erzieherartenManager = new SelectManager({ options: erzieherarten.value, sort: erzieherArtSort, optionDisplayText: i => i.bezeichnung, selectionDisplayText: i => i.bezeichnung });

	const erzieherart = computed({
		get: () => props.mapErzieherarten.get(erzieher.value?.idErzieherArt ?? -1),
		set: (value) => {
			const id = value?.id ?? undefined;
			if (erzieher.value === undefined)
				return;
			erzieher.value.idErzieherArt = id ?? null;
			void props.patchErzieher({ idErzieherArt: id ?? null }, erzieher.value.id);
		},
	});

	const orte = computed(() => props.mapOrte.values());

	const wohnortManager = new SelectManager({ options: orte.value, optionDisplayText: i => `${i.plz} ${i.ortsname}`, sort: orte_sort, selectionDisplayText: i => `${i.plz} ${i.ortsname}` });

	const erzWohnort = computed({
		get: () => props.mapOrte.get(erzieher.value?.wohnortID ?? -1) ?? null,
		set: (value) => {
			if (erzieher.value === undefined)
				return;
			erzieher.value.wohnortID = value?.id ?? -1;
			void props.patchErzieher({ wohnortID: value?.id ?? null }, erzieher.value.id);
		},
	});

	const ersterErzStaatsangehoerigkeit = computed<NationalitaetenKatalogEintrag | null>({
		get: (): NationalitaetenKatalogEintrag | null => {
			const iso3 = erzieher.value?.staatsangehoerigkeitID ?? null;
			return Nationalitaeten.getByISO3(iso3)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			if (erzieher.value === undefined)
				return;
			const iso3 = value?.iso3 ?? null;
			erzieher.value.staatsangehoerigkeitID = iso3;
			void props.patchErzieher({ staatsangehoerigkeitID: iso3 }, erzieher.value.id);
		},
	});

	const staatsangehoerigkeitenManager = new CoreTypeSelectManager({ clazz: Nationalitaeten.class, schuljahr: schuljahr, optionDisplayText: "text", selectionDisplayText: "text" });

	const zweiteErzStaatsangehoerigkeit = computed<NationalitaetenKatalogEintrag | null>({
		get: (): NationalitaetenKatalogEintrag | null => {
			const iso3 = zweiterErz.value.staatsangehoerigkeitID ?? null;
			return Nationalitaeten.getByISO3(iso3)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			if (erzieher.value === undefined)
				return;
			zweiterErz.value.staatsangehoerigkeitID = value?.iso3 ?? null;
		},
	});

	const ortsteile = computed(() => Array.from(props.mapOrtsteile.values()));

	const erzOrtsteileFiltered = computed(() => {
		const wohnortID = erzieher.value?.wohnortID;
		if (wohnortID === null)
			return ortsteile.value;
		return ortsteile.value.filter(o => o.ort_id === wohnortID);
	});

	const erzItems = computed(() => erzOrtsteileFiltered.value);

	const erzOrtsteilManager = new SelectManager({ options: erzItems, sort: ortsteilSort, optionDisplayText: i => i.ortsteil ?? '',
		selectionDisplayText: i => i.ortsteil ?? '' });

	const erzOrtsteil = computed<OrtsteilKatalogEintrag | null>({
		get: () => {
			const id = erzieher.value?.ortsteilID ?? -1;
			return props.mapOrtsteile.get(id) ?? null;
		},
		set: (value: OrtsteilKatalogEintrag | null) => {
			if (erzieher.value === undefined)
				return;
			erzieher.value.ortsteilID = value?.id ?? null;
			void props.patchErzieher({ ortsteilID: value?.id ?? null }, erzieher.value.id);
		},
	});

	watch(() => props.data(), (neu) => {
		if (neu.isEmpty())
			erzieher.value = undefined;
		else
			erzieher.value = neu.getFirst();
	}, { immediate: true });

	const columns: DataTableColumn[] = [
		{ key: "idErzieherArt", label: "Art"},
		{ key: "name", label: "Name"},
		{ key: "eMail", label: "E-Mail"},
		{ key: "adresse", label: "Adresse"},
		{ key: "erhaeltAnschreiben", label: "Anschreiben", tooltip: "Erhält Anschreiben", fixedWidth: 3, align: "center"},
		{ key: "actions", label: "2. Person", tooltip: "Weiteres Elternteil hinzufügen", fixedWidth: 10, align: "center" },
	];

	function stringIsValid(input: string | null, mandatory: boolean, maxLength: number) {
		if (mandatory)
			return (input !== null) && (!JavaString.isBlank(input)) && (input.length <= maxLength);
		return (input === null) || (input.length <= maxLength);
	}

	function strasse(erzieher: ErzieherStammdaten) {
		return AdressenUtils.combineStrasse(erzieher.strassenname ?? "", erzieher.hausnummer ?? "", erzieher.hausnummerZusatz ?? "");
	}

	function patchStrasse(value: string | null) {
		if ((value !== null) && (erzieher.value !== undefined)) {
			const vals = AdressenUtils.splitStrasse(value);
			void props.patchErzieher({ strassenname: vals[0], hausnummer: vals[1], hausnummerZusatz: vals[2] }, erzieher.value.id);
		}
	}

	function setMode(newMode: Mode) {
		return currentMode.value = newMode;
	}

	function addModal() {
		resetForm();
		setMode(Mode.ADD);
		openModal()
		ersterErz.value.id = 0;
	}

	// Patch-Modal um an der zweiten Postion in einem Eintrag einen Erziehungsberechtigten anzulegen
	async function openModalForPos2(item: ErzieherStammdaten) {
		resetForm();
		setMode(Mode.PATCH_POS2);
		openPatchPosModal();
		// die ID des Eintrags für den Patch an der zweiten Position
		ersterErz.value.id = item.id;
		zweiterErz.value.idErzieherArt = item.idErzieherArt ?? 0;
		zweiterErz.value.erhaeltAnschreiben = item.erhaeltAnschreiben;
		zweiterErz.value.wohnortID = item.wohnortID;
		zweiterErz.value.ortsteilID = item.ortsteilID;
		zweiterErz.value.bemerkungen = item.bemerkungen;
	}

	function openModal() {
		showModal.value = true;
	}

	function openPatchPosModal() {
		showPatchPosModal.value = true;
	}

	function closeModal() {
		resetForm();
		setMode(Mode.DEFAULT);
		showModal.value = false;
		showPatchPosModal.value = false;
	}

	function resetForm() {
		const defaultErzieherStammdaten = new ErzieherStammdaten();
		const ersteErzieherart = props.mapErzieherarten.values().next().value;
		defaultErzieherStammdaten.idErzieherArt = ersteErzieherart?.id ?? 0;
		ersterErz.value = defaultErzieherStammdaten;
		ersterErz.value.erhaeltAnschreiben = false;

		istErsterErzGespeichert.value = false;
		zweiterErz.value = new ErzieherStammdaten();
	}

	function enterDefaultMode() {
		setMode(Mode.DEFAULT);
		resetForm();
		closeModal();
	}

	/**
	 * Prüft, ob eine ID auf die erste Position (Suffix 1) endet.
	 * @param id Die künstliche ID mit Suffix.
	 * @returns true, wenn Suffix === 1.
	 */
	function isSuffix1(id: number): boolean {
		return id % 10 === 1;
	}

	/**
	 * Prüft, ob bereits eine zweite Position (Suffix 2) für einen Eintrag existiert.
	 * @param id Die künstliche ID mit Suffix.
	 * @returns true, wenn ein Eintrag mit id+1 existiert.
	 */
	function hasSuffix2(id: number): boolean {
		return Array.from(props.data()).some(e => e.id === id + 1);
	}

	async function sendRequest() {
		const { id, idSchueler, ...partialDataWithoutId } = ersterErz.value;
		if (currentMode.value === Mode.ADD)
			await props.addErzieher(partialDataWithoutId, 1);
		// Normale Patch für beide Positionen
		if (currentMode.value === Mode.PATCH)
			await props.patchErzieher(partialDataWithoutId, ersterErz.value.id);
		// Zweite Position zum bestehenden Eintrag hinzufügen
		if (currentMode.value === Mode.PATCH_POS2)
			await props.patchErzieherAnPosition(partialDataWithoutId, ersterErz.value.id, 2);
		enterDefaultMode();
	}

	// Speichert den ersten Erziehungsberechtigten (Position 1) und bereitet das Formular für den zweiten Erziehungsberechtigten vor.
	async function saveAndShowSecondForm() {
		const { id, idSchueler, ...partialDataWithoutId } = ersterErz.value;
		const savedEntry = await props.addErzieher(partialDataWithoutId, 1);
		ersterErz.value.id = savedEntry.id;
		zweiterErz.value.idErzieherArt = ersterErz.value.idErzieherArt;
		zweiterErz.value.wohnortID = ersterErz.value.wohnortID;
		zweiterErz.value.ortsteilID = ersterErz.value.ortsteilID;
		zweiterErz.value.bemerkungen = ersterErz.value.bemerkungen;
		istErsterErzGespeichert.value = true;
	}

	// Speichert den zweiten Erziehungsberechtigten (Position 2) und beendet anschließend den Bearbeitungsmodus.
	async function saveSecondErzieher() {
		const { id, idSchueler, erhaeltAnschreiben, ...partialDataWithoutId } = zweiterErz.value;
		await props.patchErzieherAnPosition(partialDataWithoutId, ersterErz.value.id, 2);
		enterDefaultMode();
	}

	async function deleteErzieherRequest() {
		if (selectedErz.value.length === 0)
			return;
		const ids = new ArrayList<number>();
		for (const s of selectedErz.value)
			ids.add(s.id);
		await props.deleteErzieher(ids);
		selectedErz.value = [];
	}

</script>
