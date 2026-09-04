<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<div class="page page-grid-cards">
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-content-card title="Stammdaten">
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-text-input placeholder="Bezeichnung 1" class="contentFocusField"
							:model-value="schuleState.stammdaten.bezeichnung1"
							@change="bezeichnung1 => bezeichnung1 && patch({ bezeichnung1 })"
							:readonly="!hatKompetenzUpdate" />
						<svws-ui-text-input placeholder="Schulnummer"
							:model-value="schuleState.stammdaten.schulNr.toString()"
							readonly statistics />
						<svws-ui-text-input placeholder="Bezeichnung 2"
							:model-value="schuleState.stammdaten.bezeichnung2"
							@change="bezeichnung2 => patch({ bezeichnung2 })"
							:readonly="!hatKompetenzUpdate" />
						<svws-ui-text-input placeholder="Schulform"
							:model-value="textSchulform"
							readonly />
						<svws-ui-text-input placeholder="Bezeichnung 3"
							:model-value="schuleState.stammdaten.bezeichnung3"
							@change="bezeichnung3 => patch({ bezeichnung3 })"
							:readonly="!hatKompetenzUpdate" />
						<svws-ui-spacing />
					</svws-ui-input-wrapper>
				</svws-ui-content-card>
				<svws-ui-spacing :size="2" />
				<svws-ui-content-card title="Kontaktinformationen">
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-text-input placeholder="Straße" class="contentFocusField" span="full"
							:model-value="strasse"
							@change="patchStrasse"
							:readonly="!hatKompetenzUpdate" />
						<svws-ui-text-input placeholder="PLZ"
							:model-value="schuleState.stammdaten.plz"
							@change="patchPlz"
							:valid="v => optionalInputIsValid(v, 10)"
							:readonly="!hatKompetenzUpdate" :max-len="10" />
						<svws-ui-text-input placeholder="Ort"
							:model-value="schuleState.stammdaten.ort"
							@change="patchOrt"
							:valid="o => optionalInputIsValid(o, 50)"
							:readonly="!hatKompetenzUpdate" :max-len="50" />
						<svws-ui-text-input placeholder="Telefon" type="tel"
							:model-value="schuleState.stammdaten.telefon"
							@change="telefon => patch({ telefon })"
							:readonly="!hatKompetenzUpdate" :max-len="20" />
						<svws-ui-text-input placeholder="Fax"
							:model-value="schuleState.stammdaten.fax"
							@change="fax => patch({ fax })" type="tel"
							:readonly="!hatKompetenzUpdate" :max-len="20" />
						<svws-ui-text-input placeholder="Homepage"
							:model-value="schuleState.stammdaten.webAdresse"
							@change="webAdresse => patch({ webAdresse })"
							verify-email :readonly="!hatKompetenzUpdate" />
						<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
							:model-value="schuleState.stammdaten.email"
							@change="email => patch({ email })"
							verify-email :readonly="!hatKompetenzUpdate" />
					</svws-ui-input-wrapper>
				</svws-ui-content-card>
				<svws-ui-spacing :size="2" />
				<svws-ui-content-card v-if="serverState.hasDev" title="Teilstandorte">
					<svws-ui-table class="max-h-72!"
						v-model="selectedTeilstandorte"
						:items="getListTeilstandorte()"
						:clicked="clickedTeilstandort"
						@update:clicked="teil => patchTeilstandort(teil)"
						:columns="teilstandorteTableColumns"
						:clickable="hatKompetenzUpdate"
						:selectable="hatKompetenzUpdate"
						scroll scroll-into-view count>
						<template #cell(referenziertInAnderenTabellen)="{ value, rowData }">
							<div v-if="(value && rowData.adrMerkmal !== null) && adrMerkmaleSelectedEntries.has(rowData.adrMerkmal)">
								<svws-ui-tooltip>
									<span class="icon icon-ui-danger i-ri-alert-line" />
									<template #content>
										Dieser Ansprechpartner ist an anderer Stelle referenziert und kann daher nicht gelöscht werden.
									</template>
								</svws-ui-tooltip>
							</div>
						</template>
						<template #actions v-if="hatKompetenzUpdate">
							<div class="inline-flex gap-4">
								<svws-ui-button type="trash"
									@click="deleteSelectedTeilstandorte"
									:disabled="((selectedTeilstandorte.length === 0) || !allEntriesDeletable)" />
								<svws-ui-button title="Teilstandort hinzufügen" type="icon"
									@click="addTeilstandort">
									<span class="icon i-ri-add-line" />
								</svws-ui-button>
							</div>
						</template>
					</svws-ui-table>
					<svws-ui-modal :show="showModalTeilstandort" @update:show="closeModalTeilstandort">
						<template #modalTitle>Teilstandort hinzufügen</template>
						<template #modalContent>
							<svws-ui-input-wrapper :grid="2" class="text-left">
								<ui-select label="Merkmal"
									v-model="model.proxy.adrMerkmal"
									:manager="adrMerkmaleSelectManager"
									:validation="() => currentTeilstandortMode === Mode.ADD ? model.getFehler('adrMerkmal') : new ArrayList()"
									:readonly="!hatKompetenzUpdate || (currentTeilstandortMode === Mode.PATCH)"
									:removable="false"
									required />
								<svws-ui-text-input placeholder="strassenname"
									v-model="model.proxy.strassenname"
									:validation="() => model.getFehler('strassenname')"
									:max-len="55" :readonly="!hatKompetenzUpdate" />
								<svws-ui-text-input placeholder="Hausnummer"
									v-model="model.proxy.hausNr"
									:validation="() => model.getFehler('hausNr')"
									:max-len="10" :readonly="!hatKompetenzUpdate" />
								<svws-ui-text-input placeholder="Hausnummer zusatz"
									v-model="model.proxy.hausNrZusatz"
									:validation="() => model.getFehler('hausNrZusatz')"
									:max-len="30" :readonly="!hatKompetenzUpdate" />
								<svws-ui-text-input placeholder="PLZ"
									v-model="model.proxy.plz"
									:validation="() => model.getFehler('plz')"
									:max-len="10" :readonly="!hatKompetenzUpdate" />
								<svws-ui-text-input placeholder="Ort"
									v-model="model.proxy.ort"
									:validation="() => model.getFehler('ort')"
									:max-len="50" :readonly="!hatKompetenzUpdate" />
								<svws-ui-text-input placeholder="Kürzel"
									v-model="model.proxy.kuerzel"
									:validation="() => model.getFehler('kuerzel')"
									:max-len="30" :readonly="!hatKompetenzUpdate" />
								<div class="col-span-full">
									<svws-ui-text-input placeholder="Bemerkung"
										v-model="model.proxy.bemerkung"
										:validation="() => model.getFehler('bemerkung')"
										:max-len="50" :readonly="!hatKompetenzUpdate" />
								</div>
								<svws-ui-spacing />
							</svws-ui-input-wrapper>
							<div class="mt-7 flex flex-row gap-4 justify end">
								<svws-ui-button type="secondary" @click="closeModalTeilstandort">Abbrechen</svws-ui-button>
								<svws-ui-button @click="saveTeilstandort" :disabled="saveTeilstandortDisabled">
									Speichern
								</svws-ui-button>
							</div>
						</template>
					</svws-ui-modal>
				</svws-ui-content-card>
				<svws-ui-spacing :size="2" />
			</svws-ui-input-wrapper>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { SchuleAdressdatenProps } from "~/components/schule/stammdaten/adressdaten/SchuleAdressdatenProps";
	import { optionalInputIsValid } from "~/util/validation/Validation";
	import { TeilstandortModelProxy } from "~/components/schule/stammdaten/adressdaten/modelproxy/TeilstandortModelProxy";
	import { Teilstandort } from "@core/core/data/schule/Teilstandort";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { AdressenUtils } from "@core/core/utils/AdressenUtils";
	import { JavaString } from "@core/java/lang/JavaString";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { useServerState } from "@ui/states/ServerState";
	import type { DataTableColumn } from "@ui/types";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	const props = defineProps<SchuleAdressdatenProps>();
	const benutzerState = useBenutzerState();
	const serverState = useServerState();
	const schuleState = useSchuleState();

	const selectedTeilstandorte = ref<Teilstandort[]>([]);
	const clickedTeilstandort = ref<Teilstandort | null>(null);
	const teilstandortEntry = ref<Teilstandort>(new Teilstandort());
	enum Mode { ADD, PATCH, DEFAULT }
	const currentTeilstandortMode = ref<Mode>(Mode.DEFAULT);
	const showModalTeilstandort = ref<boolean>(false);

	const model = new TeilstandortModelProxy(() => teilstandortEntry.value, props.getListTeilstandorte);
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const strasse = computed(() => AdressenUtils.combineStrasse(schuleState.stammdaten.strassenname ?? "", schuleState.stammdaten.hausnummer ?? "", schuleState.stammdaten.hausnummerZusatz ?? ""));
	const adrMerkmaleSelectedEntries = computed<Set<string>>(() =>
		new Set<string>(selectedTeilstandorte.value.map(e => e.adrMerkmal).filter(m => m !== null))
	);
	const allEntriesDeletable = computed<boolean>(() =>
		selectedTeilstandorte.value.every(entry => !entry.referenziertInAnderenTabellen)
	);
	const saveTeilstandortDisabled = computed<boolean>(() => {
		if ((model.proxy.adrMerkmal === null) || JavaString.isBlank(model.proxy.adrMerkmal)) {
			return true;
		}

		if (currentTeilstandortMode.value === Mode.ADD) {
			return !model.getFehler('adrMerkmal').isEmpty() || !model.getAlleFehler().isEmpty();
		}

		// adrMerkmal ignorieren, nur restliche Felder prüfen
		const fieldsToValidate: (keyof Teilstandort)[] = ['kuerzel', 'strassenname', 'hausNr', 'hausNrZusatz', 'plz', 'ort', 'bemerkung'];
		return fieldsToValidate.some(field => !model.getFehler(field).isEmpty());
	});

	const adrMerkmale = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'];

	const availableAdrMerkmale = computed(() => {
		const usedAdrMerkmale = new Set<string | null>();

		for (const teilstandort of props.getListTeilstandorte()) {
			usedAdrMerkmale.add(teilstandort.adrMerkmal);
		}

		return adrMerkmale.filter(merkmal => !usedAdrMerkmale.has(merkmal) || (merkmal === model.proxy.adrMerkmal));
	});

	const adrMerkmaleSelectManager = new SelectManager({
		options: availableAdrMerkmale,
		optionDisplayText: adrMerkmal => adrMerkmal,
		selectionDisplayText: adrMerkmal => adrMerkmal,
	});

	function enterDefaultMode() {
		setTeilstandortMode(Mode.DEFAULT);
		closeModalTeilstandort();
	}

	const teilstandorteTableColumns: DataTableColumn[] = [
		{ key: "referenziertInAnderenTabellen", label: "", fixedWidth: 2 },
		{ key: "adrMerkmal", label: "Merkmal", span: 0.75 },
		{ key: "strassenname", label: "Straße" },
		{ key: "hausNr", label: "Hausnr.", span: 0.5 },
		{ key: "hausNrZusatz", label: "Hausnr-Zusatz", span: 1 },
		{ key: "plz", label: "PLZ", span: 0.65 },
		{ key: "ort", label: "Ort" },
		{ key: "kuerzel", label: "Kürzel", span: 0.75 },
		{ key: "bemerkung", label: "Bemerkung", span: 2 },
	];

	function addTeilstandort() {
		resetTeilstandort();
		setTeilstandortMode(Mode.ADD);
		openModalTeilstandort();
	}

	async function saveTeilstandort() {
		const { referenziertInAnderenTabellen, ...addData } = model.proxy;
		if (currentTeilstandortMode.value === Mode.ADD) {
			if (!props.getListTeilstandorte().isEmpty()) {
				clickedTeilstandort.value = props.getListTeilstandorte().getFirst();
			}

			await props.addTeilstandorteintrag(addData);
			clickedTeilstandort.value = props.getListTeilstandorte().getLast();

		} else if (currentTeilstandortMode.value === Mode.PATCH) {
			const { adrMerkmal, ...patchData } = addData;
			await props.patchTeilstandorteintrag(patchData, teilstandortEntry.value.adrMerkmal ?? "-");
		}

		enterDefaultMode();
	}

	function patchTeilstandort(teilstandort: Teilstandort) {
		resetTeilstandort();
		setTeilstandortMode(Mode.PATCH);
		teilstandortEntry.value.adrMerkmal = teilstandort.adrMerkmal;
		teilstandortEntry.value.kuerzel = teilstandort.kuerzel;
		teilstandortEntry.value.strassenname = teilstandort.strassenname;
		teilstandortEntry.value.hausNr = teilstandort.hausNr;
		teilstandortEntry.value.hausNrZusatz = teilstandort.hausNrZusatz;
		teilstandortEntry.value.plz = teilstandort.plz;
		teilstandortEntry.value.ort = teilstandort.ort;
		teilstandortEntry.value.bemerkung = teilstandort.bemerkung;
		clickedTeilstandort.value = teilstandort;
		openModalTeilstandort();
	}

	async function deleteSelectedTeilstandorte() {
		if (selectedTeilstandorte.value.length === 0) {
			return;
		}

		const adrMerkmale = new ArrayList<string>();
		for (const adrMerkmal of selectedTeilstandorte.value) {
			adrMerkmale.add(adrMerkmal.adrMerkmal);
		}

		await props.deleteTeilstandorteintraege(adrMerkmale);
		selectedTeilstandorte.value = [];
	}

	function openModalTeilstandort() {
		showModalTeilstandort.value = true;
	}

	function closeModalTeilstandort() {
		resetTeilstandort();
		setTeilstandortMode(Mode.DEFAULT);
		showModalTeilstandort.value = false;
	}

	function setTeilstandortMode(newMode: Mode) {
		currentTeilstandortMode.value = newMode;
	}

	function resetTeilstandort() {
		teilstandortEntry.value = new Teilstandort();
	}
	const patchStrasse = (value: string | null) => {
		if (value !== null) {
			const [strassenname, hausnummer, hausnummerZusatz] = AdressenUtils.splitAndTrimStrasse(value);
			void props.patch({ strassenname, hausnummer, hausnummerZusatz });
		}
	};

	const textSchulform = computed<string>(() => {
		return schuleState.schulform.daten(schuleState.abschnitt.schuljahr)?.text ?? "—";
	});

	function patchOrt(ort: string | null) {
		if (optionalInputIsValid(ort, 50)) {
			void props.patch({ ort });
		}
	}

	function patchPlz(plz: string | null) {
		if (optionalInputIsValid(plz, 10)) {
			void props.patch({ plz });
		}
	}

</script>
