<template>
	<svws-ui-content-card title="Erziehungsberechtigte" class="col-span-full">
		<svws-ui-table class="contentFocusField"
			v-model="selectedData"
			:items="sortedData"
			v-model:clicked="data"
			:no-data="sortedData.length === 0" clickable
			:columns :selectable="true" focus-first-element>
			<template #header(erhaeltAnschreiben)>
				<svws-ui-tooltip>
					<span class="icon i-ri-mail-send-line" />
					<template #content>
						Erhält Anschreiben
					</template>
				</svws-ui-tooltip>
			</template>
			<template #cell(idErzieherArt)="{ value }">
				{{ getBezeichnungErzieherart(value) }}
			</template>
			<template #cell(name)="{ rowData }">
				{{ rowData.vorname }} {{ rowData.nachname }}
			</template>
			<template #cell(email)="{ value: eMail }">
				{{ eMail ? eMail : '—' }}
			</template>
			<template #cell(adresse)="{ rowData }">
				{{
					getBezeichnungAdresse(rowData)
				}}
			</template>
			<template #cell(erhaeltAnschreiben)="{ value: erhaeltAnschreiben }">
				{{ erhaeltAnschreiben ? '&check;' : '&times;' }}
			</template>
			<template #cell(actions)="{ rowData }">
				<!-- Button zum Hinzufügen eines Erziehers an der zweiten Position, wird nur angezeigt wenn noch keine zweite Position in einem Eintrag existiert -->
				<svws-ui-button v-if="isSuffix1(rowData.id) && !hasSuffix2(rowData.id)"
					@click.stop="openModalForPos2(rowData)">
					+
				</svws-ui-button>
			</template>
			<template #actions>
				<svws-ui-button type="trash"
					@click="deleteEntry"
					:disabled="(selectedData.length === 0) || (readonly)" />
				<svws-ui-button title="Erziehungsberechtigten hinzufügen" type="icon"
					@click="add"
					:disabled="readonly">
					<span class="icon i-ri-add-line" />
				</svws-ui-button>
			</template>
		</svws-ui-table>
		<schueler-schnelleingabe-erzieher-form v-if="showPatchForm"
			:manager
			:data
			:patch-erzieher
			:readonly
			:schuljahr />
		<!-- Modal zum Hinzufügen eines zweiten Erziehungsberechtigten (Position 2) über den "+"-Button -->
		<svws-ui-modal :show="patchPosModalErzIsShown" @update:show="closeModal">
			<template #modalTitle>Einen zweiten Erziehungsberechtigten hinzufügen</template>
			<template #modalContent>
				<svws-ui-input-wrapper :grid="2" class="text-left">
					<svws-ui-text-input placeholder="Anrede"
						v-model="zweiterErz.anrede" />
					<svws-ui-text-input placeholder="Titel"
						v-model="zweiterErz.titel" />
					<svws-ui-text-input placeholder="Vorname"
						v-model="zweiterErz.vorname"
						required />
					<svws-ui-text-input placeholder="Nachname"
						v-model="zweiterErz.nachname"
						required />
					<svws-ui-text-input placeholder="E-Mail Adresse" type="email"
						v-model="zweiterErz.eMail"
						verify-email />
					<ui-select label="Staatsangehörigkeit"
						v-model="zweiteErzStaatsangehoerigkeit"
						:manager="staatsangehoerigkeitenManager"
						:removable="false" searchable />
				</svws-ui-input-wrapper>
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary"
						@click="patchPosModalErzIsShown = false">
						Abbrechen
					</svws-ui-button>
					<svws-ui-button @click="addSecondErzieher"
						:disabled="(!mandatoryInputIsValid(zweiterErz.vorname, 120))
							|| (!mandatoryInputIsValid(zweiterErz.nachname, 120))
							|| (!updateKompetenz)">
						Zweiten Erzieher speichern
					</svws-ui-button>
				</div>
			</template>
		</svws-ui-modal>
		<SSchuelerErziehungsberechtigteModal v-model:erster-erz="ersterErz"
			v-model:zweiter-erz="zweiterErz"
			:show-modal="modalIsShown"
			:erzieherarten-by-id="manager().erzieherartenById"
			:hat-kompetenz-update="updateKompetenz"
			:ist-erster-erz-gespeichert
			:orte-by-id="manager().orteById"
			:ortsteile-by-id="manager().ortsteileById"
			:schuljahr
			@close-modal="closeModal"
			@send-request="sendRequest"
			@save-and-show-second="saveAndShowSecondForm"
			@save-second-erzieher="addSecondErzieher" />
	</svws-ui-content-card>
</template>
<script setup lang="ts">

	import type { List, NationalitaetenKatalogEintrag } from "@core";
	import { AdressenUtils, ArrayList, ErzieherStammdaten, Nationalitaeten } from "@core";
	import type { DataTableColumn, SchuelerSchnelleingabeManager } from "@ui";
	import { CoreTypeSelectManager } from "@ui";
	import { computed, ref, watch } from "vue";
	import { mandatoryInputIsValid } from "~/util/validation/Validation";
	import SchuelerSchnelleingabeErzieherForm from "~/components/schueler/neuanlage/SchuelerSchnelleingabeErzieherForm.vue";

	const props = defineProps<{
		manager: () => SchuelerSchnelleingabeManager;
		getErzieher: () => List<ErzieherStammdaten>;
		addErzieher: (data: Partial<ErzieherStammdaten>, idEintrag: number, pos: number) => Promise<ErzieherStammdaten>;
		patchErzieher: (data: Partial<ErzieherStammdaten>, idEintrag: number) => Promise<void>;
		patchErzieherAnPosition: (data: Partial<ErzieherStammdaten>, idEintrag: number, idSchueler: number, pos: number) => Promise<void>;
		deleteErzieher: (idsEintraege: List<number>) => Promise<void>;
		schuljahr: number;
		readonly: boolean;
		updateKompetenz: boolean;
	}>();

	const manager = () => props.manager();
	const data = ref<ErzieherStammdaten | undefined>();
	const selectedData = ref<ErzieherStammdaten[]>([]);
	const ersterErz = ref<ErzieherStammdaten>(new ErzieherStammdaten());
	const zweiterErz = ref<ErzieherStammdaten>(new ErzieherStammdaten());
	const istErsterErzGespeichert = ref(false);
	const showPatchForm = computed<boolean>(() => (sortedData.value.length > 0) && (data.value !== undefined));

	const sortedData = computed(() => {
		const list = Array.from(props.getErzieher());
		return list.sort((a, b) => {
			const ersteErzId = Math.floor(a.id / 10);
			const zweiteErzId = Math.floor(b.id / 10);
			if (ersteErzId !== zweiteErzId) {
				return ersteErzId - zweiteErzId;
			}
			return a.id - b.id;
		});
	});

	const zweiteErzStaatsangehoerigkeit = computed<NationalitaetenKatalogEintrag | null>({
		get: () => Nationalitaeten.getByISO3(zweiterErz.value.staatsangehoerigkeitID ?? null)?.daten(props.schuljahr) ?? null,
		set: (value: NationalitaetenKatalogEintrag | null) => {
			zweiterErz.value.staatsangehoerigkeitID = value?.iso3 ?? null;
		},
	});

	const staatsangehoerigkeitenManager = new CoreTypeSelectManager({
		clazz: Nationalitaeten.class,
		schuljahr: props.schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	// --- requests ---

	async function sendRequest() {
		const { id, idSchueler, ...partialDataWithoutId } = ersterErz.value;
		const schuelerId = manager().stammdaten.id;
		if (currentMode.value === Mode.ADD) {
			await props.addErzieher(partialDataWithoutId, schuelerId, 1);
		}
		// Normale Patch für beide Positionen
		if (currentMode.value === Mode.PATCH) {
			await props.patchErzieher(partialDataWithoutId, ersterErz.value.id);
		}
		// Zweite Position zum bestehenden Eintrag hinzufügen
		if (currentMode.value === Mode.PATCH_POS2) {
			await props.patchErzieherAnPosition(partialDataWithoutId, ersterErz.value.id, schuelerId, 2);
		}
		enterDefaultMode();
	}

	function resetErzieher() {
		const defaultErzieherStammdaten = new ErzieherStammdaten();
		const ersteErzieherart = manager().erzieherartenById.values().next().value;
		defaultErzieherStammdaten.idErzieherArt = ersteErzieherart?.id ?? 0;
		ersterErz.value = defaultErzieherStammdaten;
		ersterErz.value.erhaeltAnschreiben = false;

		istErsterErzGespeichert.value = false;
		zweiterErz.value = new ErzieherStammdaten();
	}

	function add() {
		resetErzieher();
		setMode(Mode.ADD);
		openModal();
		ersterErz.value.id = 0;
	}

	async function deleteEntry() {
		if (selectedData.value.length === 0) {
			return;
		}
		const ids = new ArrayList<number>();
		for (const s of selectedData.value) {
			ids.add(s.id);
		}
		await props.deleteErzieher(ids);
		selectedData.value = [];
		data.value = undefined;
	}

	// Speichert den zweiten Erziehungsberechtigten (Position 2) und beendet anschließend den Bearbeitungsmodus.
	async function addSecondErzieher() {
		const { id, idSchueler, erhaeltAnschreiben, ...partialDataWithoutId } = zweiterErz.value;
		const schuelerId = manager().stammdaten.id;
		await props.patchErzieherAnPosition(partialDataWithoutId, ersterErz.value.id, schuelerId, 2);
		enterDefaultMode();
	}

	// Speichert den ersten Erziehungsberechtigten (Position 1) und bereitet das Formular für den zweiten Erziehungsberechtigten vor.
	async function saveAndShowSecondForm() {
		const { id, idSchueler, ...partialDataWithoutId } = ersterErz.value;
		const schuelerId = manager().stammdaten.id;
		const savedEntry = await props.addErzieher(partialDataWithoutId, schuelerId, 1);
		ersterErz.value.id = savedEntry.id;
		zweiterErz.value.idErzieherArt = ersterErz.value.idErzieherArt;
		zweiterErz.value.wohnortID = ersterErz.value.wohnortID;
		zweiterErz.value.ortsteilID = ersterErz.value.ortsteilID;
		zweiterErz.value.bemerkungen = ersterErz.value.bemerkungen;
		zweiterErz.value.strassenname = ersterErz.value.strassenname;
		zweiterErz.value.hausnummer = ersterErz.value.hausnummer;
		zweiterErz.value.hausnummerZusatz = ersterErz.value.hausnummerZusatz;
		istErsterErzGespeichert.value = true;
	}

	// --- modal ---

	const modalIsShown = ref<boolean>(false);
	const patchPosModalErzIsShown = ref<boolean>(false);

	function closeModal() {
		resetErzieher();
		setMode(Mode.DEFAULT);
		modalIsShown.value = false;
		patchPosModalErzIsShown.value = false;
	}

	async function openModalForPos2(item: ErzieherStammdaten) {
		resetErzieher();
		setMode(Mode.PATCH_POS2);
		openPatchPosModalErz();
		// die ID des Eintrags für den Patch an der zweiten Position
		ersterErz.value.id = item.id;
		zweiterErz.value.idErzieherArt = item.idErzieherArt ?? 0;
		zweiterErz.value.wohnortID = item.wohnortID;
		zweiterErz.value.ortsteilID = item.ortsteilID;
		zweiterErz.value.bemerkungen = item.bemerkungen;
		zweiterErz.value.erhaeltAnschreiben = item.erhaeltAnschreiben;
		zweiterErz.value.strassenname = item.strassenname;
		zweiterErz.value.hausnummer = item.hausnummer;
		zweiterErz.value.hausnummerZusatz = item.hausnummerZusatz;
	}

	function openModal() {
		modalIsShown.value = true;
	}

	function openPatchPosModalErz() {
		patchPosModalErzIsShown.value = true;
	}

	// --- mode ---

	enum Mode { ADD, PATCH, PATCH_POS2, DEFAULT }
	const currentMode = ref<Mode>(Mode.DEFAULT);


	function setMode(newMode: Mode) {
		currentMode.value = newMode;
	}

	function enterDefaultMode() {
		setMode(Mode.DEFAULT);
		resetErzieher();
		closeModal();
	}

	// --- util ---

	function getBezeichnungAdresse(rowData: ErzieherStammdaten): string {
		let result = strasseErzieher(rowData) ?? '';
		if (rowData.wohnortID === null) {
			return result;
		}
		const ort = manager().orteById.get(rowData.wohnortID);
		if (ort === undefined) {
			return result;
		}
		return result + ', ' + ort.plz + ' ' + ort.ortsname;
	}

	function getBezeichnungErzieherart(idErzieherart: number): string {
		return manager().erzieherartenById.get(idErzieherart)?.bezeichnung ?? "";
	}

	function strasseErzieher(erzieher: ErzieherStammdaten) {
		return AdressenUtils.combineStrasse(erzieher.strassenname ?? "", erzieher.hausnummer ?? "", erzieher.hausnummerZusatz ?? "");
	}

	/** Prüft, ob eine ID auf die erste Position (Suffix 1) endet. */
	function isSuffix1(id: number): boolean {
		return id % 10 === 1;
	}

	/** Prüft, ob bereits eine zweite Position (Suffix 2) für einen Eintrag existiert. */
	function hasSuffix2(id: number): boolean {
		return Array.from(props.getErzieher()).some(e => e.id === id + 1);
	}

	const columns: DataTableColumn[] = [
		{ key: "idErzieherArt", label: "Art" },
		{ key: "name", label: "Name" },
		{ key: "eMail", label: "E-Mail" },
		{ key: "adresse", label: "Adresse" },
		{ key: "erhaeltAnschreiben", label: "Anschreiben", tooltip: "Erhält Anschreiben", fixedWidth: 3, align: "center" },
		{ key: "actions", label: "2. Person", tooltip: "Weiteres Elternteil hinzufügen", fixedWidth: 10, align: "center" },
	];

	watch(() => props.getErzieher(), (neu) => {
		if (!neu.isEmpty()) {
			data.value = neu.getFirst();
		}
	}, { immediate: true });

</script>
