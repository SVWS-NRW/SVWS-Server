<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-schueler-erziehungsberechtigte /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Erziehungsberechtigte" class="col-span-full">
			<svws-ui-table clickable
				@update:clicked="v => patchModal(v)" :items="data()" :columns :selectable="true" v-model="selected">
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
			<svws-ui-modal :show="showModal" @update:show="closeModal">
				<template #modalTitle>Erziehungsberechtigten hinzufügen</template>
				<template #modalContent>
					<svws-ui-input-wrapper :grid="2" class="text-left">
						<svws-ui-select title="Erzieherart" :items="mapErzieherarten.values()" v-model="selectedErzieherart" :item-text="i => i.bezeichnung"
							:item-sort="erzieherArtSort" class="col-span-full" />
						<svws-ui-spacing />
						<svws-ui-text-input v-model="newEntry.anrede" type="text" placeholder="Anrede" />
						<svws-ui-text-input v-model="newEntry.titel" type="text" placeholder="Titel" />
						<svws-ui-text-input v-model="newEntry.vorname" type="text" placeholder="Vorname" required />
						<svws-ui-text-input v-model="newEntry.nachname" type="text" placeholder="Nachname" required />
						<svws-ui-text-input v-model="newEntry.eMail" type="email" placeholder="E-Mail Adresse" verify-email />
						<svws-ui-select title="Staatsangehörigkeit" v-model="staatsangehoerigkeit" :items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit"
							:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" autocomplete />
						<svws-ui-spacing />
						<svws-ui-text-input placeholder="Straße und Hausnummer" v-model="adresse" type="text" />
						<svws-ui-select title="Wohnort" v-model="wohnort" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort" :item-text="i => `${i.plz} ${i.ortsname}`" autocomplete />
						<svws-ui-select title="Ortsteil" v-model="ortsteil" :items="ortsteile" :item-text="i => i.ortsteil ?? ''" :item-sort="ortsteilSort" :item-filter="ortsteilFilter" removable />
						<svws-ui-spacing />
						<svws-ui-tooltip class="col-span-full">
							<svws-ui-text-input v-model="newEntry.bemerkungen" type="text" placeholder="Bemerkungen" />
							<template #content>
								{{ newEntry.bemerkungen ?? 'Bemerkungen' }}
							</template>
						</svws-ui-tooltip>
						<svws-ui-checkbox :model-value="newEntry.erhaeltAnschreiben ?? false" @update:model-value="value => newEntry.erhaeltAnschreiben = value"
							type="checkbox" title="Erhält Anschreiben" class="col-span-full">
							Erhält Anschreiben
						</svws-ui-checkbox>
					</svws-ui-input-wrapper>
					<svws-ui-notification type="warning" v-if="mapErzieherarten.size === 0">
						Die Liste der Erzieherarten ist leer, es sollte mindestens eine Erzieherart unter Schule/Kataloge angelegt werden, damit zusätzliche Erzieher eine gültige Zuordnung haben.
					</svws-ui-notification>
					<div class="mt-7 flex flex-row gap-4 justify-end">
						<svws-ui-button type="secondary" @click="closeModal">Abbrechen</svws-ui-button>
						<svws-ui-button @click="sendRequest"
							:disabled="(selectedErzieherart === null) || (mapErzieherarten.size === 0) || (!formIsValid)">
							Speichern
						</svws-ui-button>
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
	import type {Erzieherart, OrtKatalogEintrag, OrtsteilKatalogEintrag} from "@core";
	import { AdressenUtils, Nationalitaeten, ErzieherStammdaten, JavaString } from "@core";
	import { staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort, orte_filter, orte_sort, ortsteilFilter, ortsteilSort, erzieherArtSort } from "~/utils/helfer";

	const props = defineProps<SchuelerErziehungsberechtigteProps>();

	const erzieher = ref<ErzieherStammdaten | undefined>();

	const selected = ref<ErzieherStammdaten[]>([]);
	const newEntry = ref<ErzieherStammdaten>(new ErzieherStammdaten())
	enum Mode { ADD, PATCH, PATCH_POS2, DEFAULT }
	const currentMode = ref<Mode>(Mode.DEFAULT);
	const showModal = ref<boolean>(false);

	const selectedErzieherart = computed<Erzieherart | null>({
		get: () => props.mapErzieherarten.get(newEntry.value.idErzieherArt ?? -1) ?? null,
		set: (erzieherart) => newEntry.value.idErzieherArt = (erzieherart !== null) ? erzieherart.id : 0,
	})

	const idErzieherArt = computed<Erzieherart | undefined>({
		get: () => ((erzieher.value === undefined) || (erzieher.value.idErzieherArt === null)) ? undefined : props.mapErzieherarten.get(erzieher.value.idErzieherArt),
		set: (value) => (erzieher.value !== undefined) && void props.patch({ idErzieherArt: value === undefined ? null : value.id }, erzieher.value.id),
	});

	const wohnort = computed<OrtKatalogEintrag | undefined>({
		get: () => ((newEntry.value.wohnortID === null)) ? undefined : props.mapOrte.get(newEntry.value.wohnortID),
		set: (value) => newEntry.value.wohnortID = value === undefined ? null : value.id,
	});

	const staatsangehoerigkeit = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(newEntry.value.staatsangehoerigkeitID) || Nationalitaeten.getDEU(),
		set: (value) => newEntry.value.staatsangehoerigkeitID = value.historie().getLast().iso3,
	});

	const ortsteil = computed<OrtsteilKatalogEintrag | undefined>({
		get: () => {
			const id = newEntry.value.ortsteilID;
			return id === null ? undefined : props.mapOrtsteile.get(id)
		},
		set: (value) => newEntry.value.ortsteilID = value === undefined ? null : value.id,
	});

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

	function addModal() {
		resetForm();
		setMode(Mode.ADD);
		openModal()
		newEntry.value.id = 0;
	}

	function patchModal(eintrag: ErzieherStammdaten) {
		resetForm();
		setMode(Mode.PATCH);
		newEntry.value.id = eintrag.id;
		newEntry.value.idErzieherArt = eintrag.idErzieherArt;
		newEntry.value.anrede = eintrag.anrede;
		newEntry.value.titel = eintrag.titel;
		newEntry.value.vorname = eintrag.vorname;
		newEntry.value.nachname = eintrag.nachname;
		newEntry.value.eMail = eintrag.eMail;
		newEntry.value.staatsangehoerigkeitID = eintrag.staatsangehoerigkeitID;
		newEntry.value.strassenname = eintrag.strassenname;
		newEntry.value.hausnummer = eintrag.hausnummer;
		newEntry.value.hausnummerZusatz = eintrag.hausnummerZusatz;
		newEntry.value.wohnortID = eintrag.wohnortID;
		newEntry.value.ortsteilID = eintrag.ortsteilID;
		newEntry.value.bemerkungen = eintrag.bemerkungen;
		newEntry.value.erhaeltAnschreiben = eintrag.erhaeltAnschreiben;
		openModal();
	}

	function openModal() {
		showModal.value = true;
	}

	function closeModal() {
		resetForm();
		setMode(Mode.DEFAULT);
		showModal.value = false;
	}

	function setMode(newMode: Mode) {
		return currentMode.value = newMode;
	}

	function resetForm() {
		const defaultErzieherStammdaten = new ErzieherStammdaten();
		const ersteErzieherart = props.mapErzieherarten.values().next().value;
		defaultErzieherStammdaten.idErzieherArt = ersteErzieherart?.id ?? 0;
		newEntry.value = defaultErzieherStammdaten;
		newEntry.value.erhaeltAnschreiben = false;
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

	// Patch-Modal um an der zweiten Postion in einem Eintrag einen Erziehungsberechtigten anzulegen
	async function openModalForPos2(item: ErzieherStammdaten) {
		resetForm();
		setMode(Mode.PATCH_POS2);
		openModal();
		// die ID des Eintrags für den Patch an der zweiten Position
		newEntry.value.id = item.id;
	}

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
		{ key: 'actions', label: 'Weiteres Elternteil', fixedWidth: 10, align: 'center' },
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

	const adresse = computed({
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

</script>
