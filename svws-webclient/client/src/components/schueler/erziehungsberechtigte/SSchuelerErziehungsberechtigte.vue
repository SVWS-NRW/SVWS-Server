<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-schueler-erziehungsberechtigte /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Erziehungsberechtigte" class="col-span-full">
			<svws-ui-table class="contentFocusField" :items="sortedData" :columns :no-data="data().size() === 0" clickable :clicked="erzieher"
				@update:clicked="value => erzieher = value" focus-first-element>
				<template #header(erhaeltAnschreiben)>
					<svws-ui-tooltip>
						<span class="icon i-ri-mail-send-line" />
						<template #content>
							Erhält Anschreiben
						</template>
					</svws-ui-tooltip>
				</template>
				<template #cell(idErzieherArt)="{ value }">
					{{ idErzieherArt ? mapErzieherarten.get(value)?.bezeichnung : '' }}
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
						@click.stop="openModalForPos2(rowData)">
						+
					</svws-ui-button>
				</template>
				<template #actions>
					<svws-ui-button @click="addModal" type="icon" title="Erziehungsberechtigten hinzufügen">
						<span class="icon i-ri-add-line" />
					</svws-ui-button>
				</template>
			</svws-ui-table>
			<svws-ui-content-card v-if="erzieher !== undefined" :title="(erzieher.vorname !== null) || (erzieher.nachname !== null) ?
				`Daten zu ${erzieher.vorname ? erzieher.vorname + ' ' : '' }${erzieher.nachname}` : 'Daten zur Person'" class="col-span-full mt-16 lg:mt-20">
				<template #actions>
					<svws-ui-checkbox :model-value="erzieher.erhaeltAnschreiben === true" @update:model-value="erhaeltAnschreiben => erzieher !== undefined &&
						patch({ erhaeltAnschreiben }, erzieher.id)" class="mr-2">
						Erhält Anschreiben
					</svws-ui-checkbox>
				</template>
				<!-- Felder zum Patchen der Erzieherdaten -->
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-select title="Erzieherart" v-model="idErzieherArt" :items="mapErzieherarten" :item-sort="erzieherArtSort" :item-text="i => i.bezeichnung ?? ''" />
					<svws-ui-text-input placeholder="Anrede" :model-value="erzieher?.anrede" @change="anrede=>erzieher !== undefined && patch({ anrede }, erzieher.id)" type="text" />
					<svws-ui-text-input placeholder="Titel" :model-value="erzieher?.titel" @change="titel=>erzieher !== undefined && patch({ titel }, erzieher.id)" type="text" />
					<svws-ui-spacing />
					<svws-ui-text-input placeholder="Name" :model-value="erzieher?.nachname" @change="nachname=>erzieher !== undefined && patch({ nachname }, erzieher.id)" type="text" />
					<svws-ui-text-input placeholder="Vorname" :model-value="erzieher?.vorname" @change="vorname=>erzieher !== undefined && patch({ vorname }, erzieher.id)" type="text" />
					<svws-ui-text-input placeholder="E-Mail Adresse" :model-value="erzieher?.eMail" @change="eMail=>erzieher !== undefined && patch({ eMail }, erzieher.id)" type="email" verify-email />
					<svws-ui-spacing />
					<svws-ui-select title="Staatsangehörigkeit" v-model="patchStaatsangehoerigkeit" :items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit"
						:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" autocomplete />
					<svws-ui-text-input placeholder="Straße und Hausnummer" :model-value="strasse(erzieher)" @change="patchStrasse" type="text" />
					<svws-ui-select title="Wohnort" v-model="wohnort" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort"
						:item-text="i => `${i.plz} ${i.ortsname}`" autocomplete />
					<svws-ui-select title="Ortsteil" v-model="ortsteil" :items="ortsteile" :item-text="i => i.ortsteil ?? ''" :item-sort="ortsteilSort"
						:item-filter="ortsteilFilter" removable />
					<svws-ui-spacing />
					<svws-ui-textarea-input placeholder="Bemerkungen" :model-value="erzieher?.bemerkungen" span="full" autoresize
						@change="bemerkungen => erzieher !== undefined && patch({ bemerkungen: bemerkungen === null ? '' : bemerkungen }, erzieher.id)" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<!-- Modal zum Hinzufügen eines zweiten Erziehungsberechtigten (Position 2) über den "+"-Button -->
			<svws-ui-modal :show="showPatchPosModal" @update:show="closeModal">
				<template #modalTitle>Zweiten Erziehungsberechtigten hinzufügen</template>
				<template #modalContent>
					<svws-ui-input-wrapper :grid="2" class="text-left">
						<svws-ui-text-input placeholder="Anrede" v-model="secondEntry.anrede" type="text" />
						<svws-ui-text-input placeholder="Titel" v-model="secondEntry.titel" type="text" />
						<svws-ui-text-input placeholder="Vorname" v-model="secondEntry.vorname" type="text" required />
						<svws-ui-text-input placeholder="Nachname" v-model="secondEntry.nachname" type="text" required />
						<svws-ui-text-input placeholder="E-Mail Adresse" v-model="secondEntry.eMail" type="email" verify-email />
						<svws-ui-select title="Staatsangehörigkeit" v-model="zweiteErzStaatsangehoerigkeit" :items="Nationalitaeten.values()"
							:item-text="i => i.historie().getLast().staatsangehoerigkeit" :item-sort="staatsangehoerigkeitKatalogEintragSort"
							:item-filter="staatsangehoerigkeitKatalogEintragFilter" autocomplete />
					</svws-ui-input-wrapper>
					<div class="mt-7 flex flex-row gap-4 justify-end">
						<svws-ui-button type="secondary" @click="showPatchPosModal = false">Abbrechen</svws-ui-button>
						<svws-ui-button @click="saveSecondErzieher" :disabled="(!secondEntry.vorname) || (!secondEntry.nachname)">
							Zweiten Erzieher speichern
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-modal>
			<!-- Modal zum Hinzufügen des ersten Erziehungsberechtigten (Position 1) -->
			<svws-ui-modal :show="showModal" @update:show="closeModal">
				<template #modalTitle>Erziehungsberechtigten hinzufügen</template>
				<template #modalContent>
					<svws-ui-input-wrapper :grid="2" class="text-left">
						<svws-ui-select title="Erzieherart" :items="mapErzieherarten.values()" v-model="selectedErzieherart" :item-text="i => i.bezeichnung"
							:item-sort="erzieherArtSort" class="col-span-full" :readonly="isFirstErzieherSaved" />
						<svws-ui-spacing />
						<svws-ui-text-input placeholder="Anrede" v-model="newEntry.anrede" type="text" :readonly="isFirstErzieherSaved" />
						<svws-ui-text-input placeholder="Titel" v-model="newEntry.titel" type="text" :readonly="isFirstErzieherSaved" />
						<svws-ui-text-input placeholder="Vorname" v-model="newEntry.vorname" type="text" :readonly="isFirstErzieherSaved" required />
						<svws-ui-text-input placeholder="Nachname" v-model="newEntry.nachname" type="text" :readonly="isFirstErzieherSaved" required />
						<svws-ui-text-input placeholder="E-Mail Adresse" v-model="newEntry.eMail" type="email" :readonly="isFirstErzieherSaved" verify-email />
						<svws-ui-select title="Staatsangehörigkeit" v-model="ersteErzStaatsangehoerigkeit" :items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit"
							:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" :readonly="isFirstErzieherSaved" autocomplete />
						<svws-ui-spacing />
						<svws-ui-text-input placeholder="Straße und Hausnummer" v-model="adresseModal" type="text" :readonly="isFirstErzieherSaved" />
						<svws-ui-select title="Wohnort" v-model="wohnortModal" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort"
							:item-text="i => `${i.plz} ${i.ortsname}`" :readonly="isFirstErzieherSaved" autocomplete />
						<svws-ui-select title="Ortsteil" v-model="ortsteil" :items="ortsteile" :item-text="i => i.ortsteil ?? ''" :item-sort="ortsteilSort"
							:item-filter="ortsteilFilter" :readonly="isFirstErzieherSaved" removable />
						<svws-ui-spacing />
						<svws-ui-tooltip class="col-span-full">
							<svws-ui-text-input v-model="newEntry.bemerkungen" type="text" placeholder="Bemerkungen" :readonly="isFirstErzieherSaved" />
							<template #content>
								{{ newEntry.bemerkungen ?? 'Bemerkungen' }}
							</template>
						</svws-ui-tooltip>
						<svws-ui-checkbox :model-value="newEntry.erhaeltAnschreiben ?? false" @update:model-value="value => newEntry.erhaeltAnschreiben = value"
							type="checkbox" title="Erhält Anschreiben" class="col-span-full" :readonly="isFirstErzieherSaved">
							Erhält Anschreiben
						</svws-ui-checkbox>
					</svws-ui-input-wrapper>
					<!-- Modal zum Hinzufügen des ersten Erziehungsberechtigten (Position 2) -->
					<div v-if="isFirstErzieherSaved" class="mt-10 border-t pt-6">
						<h4 class="text-xl mb-3">Zweiter Erziehungsberechtigter</h4>
						<svws-ui-input-wrapper :grid="2" class="text-left">
							<svws-ui-text-input placeholder="Anrede" v-model="secondEntry.anrede" type="text" />
							<svws-ui-text-input placeholder="Titel" v-model="secondEntry.titel" type="text" />
							<svws-ui-text-input placeholder="Vorname" v-model="secondEntry.vorname" type="text" required />
							<svws-ui-text-input placeholder="Nachname" v-model="secondEntry.nachname" type="text" required />
							<svws-ui-text-input placeholder="E-Mail Adresse" v-model="secondEntry.eMail" type="email" verify-email />
							<svws-ui-select title="Staatsangehörigkeit" v-model="zweiteErzStaatsangehoerigkeit" :items="Nationalitaeten.values()"
								:item-text="i => i.historie().getLast().staatsangehoerigkeit" :item-sort="staatsangehoerigkeitKatalogEintragSort"
								:item-filter="staatsangehoerigkeitKatalogEintragFilter" autocomplete />
						</svws-ui-input-wrapper>
					</div>
					<svws-ui-notification type="warning" v-if="mapErzieherarten.size === 0">
						Die Liste der Erzieherarten ist leer, es sollte mindestens eine Erzieherart unter Schule/Kataloge angelegt werden, damit zusätzliche Erzieher eine gültige Zuordnung haben.
					</svws-ui-notification>
					<div class="mt-7 flex flex-row gap-4 justify-between">
						<!-- Erstellt den ersten Erziehungsberechtigten und erweitert das Modal, um einen zweiten Erzieher hinzuzufügen -->
						<svws-ui-tooltip class="col-span-full" v-if="!isFirstErzieherSaved">
							<svws-ui-button
								@click="saveAndShowSecondForm" :disabled="(selectedErzieherart === null) || (mapErzieherarten.size === 0) || (!formIsValid)">
								+ 2. Person
							</svws-ui-button>
							<template #content>
								Einen zweiten Erzieher hinzufügen
							</template>
						</svws-ui-tooltip>

						<div class="flex flex-row gap-4 ml-auto">
							<svws-ui-button type="secondary" @click="closeModal">Abbrechen</svws-ui-button>
							<!-- Erstellt den ersten Erziehungsberechtigten -->
							<svws-ui-button v-if="!isFirstErzieherSaved" @click="sendRequest" :disabled="(selectedErzieherart === null)
								|| (mapErzieherarten.size === 0) || (!formIsValid)">
								Speichern
							</svws-ui-button>
							<!-- Erstellt den zweiten Erzieher -->
							<svws-ui-button v-if="isFirstErzieherSaved" @click="saveSecondErzieher" :disabled="(!secondEntry.vorname) || (!secondEntry.nachname)">
								2. Person speichern
							</svws-ui-button>
						</div>
					</div>
				</template>
			</svws-ui-modal>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { SchuelerErziehungsberechtigteProps } from "./SSchuelerErziehungsberechtigteProps";
	import type { Erzieherart, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
	import { AdressenUtils, Nationalitaeten, ErzieherStammdaten, JavaString } from "@core";
	import { staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort, orte_filter, orte_sort, ortsteilFilter, ortsteilSort, erzieherArtSort } from "~/utils/helfer";

	const props = defineProps<SchuelerErziehungsberechtigteProps>();

	const erzieher = ref<ErzieherStammdaten | undefined>();

	const newEntry = ref<ErzieherStammdaten>(new ErzieherStammdaten())
	enum Mode { ADD, PATCH, PATCH_POS2, DEFAULT }
	const currentMode = ref<Mode>(Mode.DEFAULT);
	const showModal = ref<boolean>(false);
	const showPatchPosModal = ref<boolean>(false);

	const isFirstErzieherSaved = ref(false);
	const secondEntry = ref<ErzieherStammdaten>(new ErzieherStammdaten());

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

	const selectedErzieherart = computed<Erzieherart | null>({
		get: () => props.mapErzieherarten.get(newEntry.value.idErzieherArt ?? -1) ?? null,
		set: (erzieherart) => newEntry.value.idErzieherArt = (erzieherart !== null) ? erzieherart.id : 0,
	})

	const idErzieherArt = computed<Erzieherart | undefined>({
		get: () => ((erzieher.value === undefined) || (erzieher.value.idErzieherArt === null)) ? undefined : props.mapErzieherarten.get(erzieher.value.idErzieherArt),
		set: (value) => (erzieher.value !== undefined) && void props.patch({ idErzieherArt: value === undefined ? null : value.id }, erzieher.value.id),
	});

	const wohnort = computed<OrtKatalogEintrag | undefined>({
		get: () => ((erzieher.value === undefined) || (erzieher.value.wohnortID === null)) ? undefined : props.mapOrte.get(erzieher.value.wohnortID),
		set: (value) => (erzieher.value !== undefined) && void props.patch({ wohnortID: value === undefined ? null : value.id }, erzieher.value.id),
	});

	const wohnortModal = computed<OrtKatalogEintrag | undefined>({
		get: () => ((newEntry.value.wohnortID === null)) ? undefined : props.mapOrte.get(newEntry.value.wohnortID),
		set: (value) => newEntry.value.wohnortID = value === undefined ? null : value.id,
	});

	const patchStaatsangehoerigkeit = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(erzieher.value?.staatsangehoerigkeitID ?? null) || Nationalitaeten.getDEU(),
		set: (value) => void props.patch({ staatsangehoerigkeitID: value.historie().getLast().iso3 }, erzieher.value?.id ?? 0),
	});

	const ersteErzStaatsangehoerigkeit = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(newEntry.value.staatsangehoerigkeitID) || Nationalitaeten.getDEU(),
		set: (value) => newEntry.value.staatsangehoerigkeitID = value.historie().getLast().iso3,
	});

	const zweiteErzStaatsangehoerigkeit = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(secondEntry.value.staatsangehoerigkeitID) || Nationalitaeten.getDEU(),
		set: (value) => secondEntry.value.staatsangehoerigkeitID = value.historie().getLast().iso3,
	});

	const ortsteil = computed<OrtsteilKatalogEintrag | undefined>({
		get: () => {
			const id = newEntry.value.ortsteilID;
			return id === null ? undefined : props.mapOrtsteile.get(id)
		},
		set: (value) => newEntry.value.ortsteilID = value === undefined ? null : value.id,
	});

	const adresseModal = computed({
		get: () => AdressenUtils.combineStrasse(newEntry.value.strassenname, newEntry.value.hausnummer, newEntry.value.hausnummerZusatz),
		set: (adresse : string) => {
			const vals = AdressenUtils.splitStrasse(adresse);
			newEntry.value.strassenname = vals[0];
			newEntry.value.hausnummer = vals[1];
			newEntry.value.hausnummerZusatz = vals[2];
		},
	})

	const ortsteile = computed<Array<OrtsteilKatalogEintrag>>(() => {
		const result : Array<OrtsteilKatalogEintrag> = [];
		for (const ortsteil of props.mapOrtsteile.values())
			if ((ortsteil.ort_id === null) || (ortsteil.ort_id === newEntry.value.wohnortID))
				result.push(ortsteil);
		return result;
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
		{ key: "actions", label: "Weiteres Elternteil", tooltip: "Weiteres Elternteil hinzufügen", fixedWidth: 10, align: "center" },
	];

	function fieldIsValid(field: keyof ErzieherStammdaten | null):(v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'nachname':
					return stringIsValid(newEntry.value.nachname, true, 120);
				case 'vorname':
					return stringIsValid(newEntry.value.vorname, true, 120);
				case 'strassenname':
					return adresseIsValid();
				case 'wohnortID':
					return (newEntry.value.wohnortID === null) || (props.mapOrte.get(newEntry.value.wohnortID) !== undefined);
				case 'ortsteilID':
					return (newEntry.value.ortsteilID === null) || (props.mapOrtsteile.get(newEntry.value.ortsteilID) !== undefined);
				case 'eMail':
					return stringIsValid(newEntry.value.eMail, false, 20);
				case 'staatsangehoerigkeitID':
					return (newEntry.value.staatsangehoerigkeitID === null) || (Nationalitaeten.getByISO3(newEntry.value.staatsangehoerigkeitID) !== null);
				default:
					return true;
			}
		}
	}

	const formIsValid = computed(() => {
		return Object.keys(newEntry.value).every((field) => {
			const validateField = fieldIsValid(field as keyof ErzieherStammdaten);
			const fieldValue = newEntry.value[field as keyof ErzieherStammdaten] as string | null;
			return validateField(fieldValue);
		})
	})

	function stringIsValid(input: string | null, mandatory: boolean, maxLength: number) {
		if (mandatory)
			return (input !== null) && (!JavaString.isBlank(input)) && (input.length <= maxLength);
		return (input === null) || (input.length <= maxLength);
	}

	function adresseIsValid() {
		return stringIsValid(newEntry.value.strassenname, false, 55) &&
			stringIsValid(newEntry.value.hausnummer, false, 10) &&
			stringIsValid(newEntry.value.hausnummerZusatz, false, 30);
	}

	function strasse(erzieher: ErzieherStammdaten) {
		return AdressenUtils.combineStrasse(erzieher.strassenname ?? "", erzieher.hausnummer ?? "", erzieher.hausnummerZusatz ?? "");
	}

	function patchStrasse(value: string | null) {
		if ((value !== null) && (erzieher.value !== undefined)) {
			const vals = AdressenUtils.splitStrasse(value);
			void props.patch({ strassenname: vals[0], hausnummer: vals[1], hausnummerZusatz: vals[2] }, erzieher.value.id);
		}
	}

	function setMode(newMode: Mode) {
		return currentMode.value = newMode;
	}

	function addModal() {
		resetForm();
		setMode(Mode.ADD);
		openModal()
		newEntry.value.id = 0;
	}

	// Patch-Modal um an der zweiten Postion in einem Eintrag einen Erziehungsberechtigten anzulegen
	async function openModalForPos2(item: ErzieherStammdaten) {
		resetForm();
		setMode(Mode.PATCH_POS2);
		openPatchPosModal();
		// die ID des Eintrags für den Patch an der zweiten Position
		newEntry.value.id = item.id;
		secondEntry.value.idErzieherArt = item.idErzieherArt ?? 0;
		secondEntry.value.erhaeltAnschreiben = item.erhaeltAnschreiben;
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
		newEntry.value = defaultErzieherStammdaten;
		newEntry.value.erhaeltAnschreiben = false;

		isFirstErzieherSaved.value = false;
		secondEntry.value = new ErzieherStammdaten();
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
		const { id, idSchueler, ...partialDataWithoutId } = newEntry.value;
		if (currentMode.value === Mode.ADD)
			await props.add(partialDataWithoutId, 1);
		// Normale Patch für beide Positionen
		if (currentMode.value === Mode.PATCH)
			await props.patch(partialDataWithoutId, newEntry.value.id);
		// Zweite Position zum bestehenden Eintrag hinzufügen
		if (currentMode.value === Mode.PATCH_POS2)
			await props.patchPosition(partialDataWithoutId, newEntry.value.id, 2);
		enterDefaultMode();
	}

	// Speichert den ersten Erziehungsberechtigten (Position 1) und bereitet das Formular für den zweiten Erziehungsberechtigten vor.
	async function saveAndShowSecondForm() {
		const { id, idSchueler, ...partialDataWithoutId } = newEntry.value;
		const savedEntry = await props.add(partialDataWithoutId, 1);
		newEntry.value.id = savedEntry.id;
		secondEntry.value.idErzieherArt = newEntry.value.idErzieherArt;
		isFirstErzieherSaved.value = true;
	}

	// Speichert den zweiten Erziehungsberechtigten (Position 2) und beendet anschließend den Bearbeitungsmodus.
	async function saveSecondErzieher() {
		const { id, idSchueler, ...partialDataWithoutId } = secondEntry.value;
		await props.patchPosition(partialDataWithoutId, newEntry.value.id, 2);
		enterDefaultMode();
	}

</script>
