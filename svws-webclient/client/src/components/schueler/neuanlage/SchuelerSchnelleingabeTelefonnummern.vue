<template>
	<svws-ui-content-card title="Weitere Telefonnummern" class="col-span-full">
		<svws-ui-table v-model="selectedTelefonart"
			:items="getTelefone()"
			@update:clicked="v => patchTelefonnummer(v)"
			:no-data="getTelefone().size() === 0"
			clickable :columns :selectable="true">
			<template #cell(idTelefonArt)="{ value }">
				{{ bezeichnung(value) }}
			</template>
			<template #cell(telefonnummer)="{ value }">
				{{ value }}
			</template>
			<template #cell(bemerkung)="{ value }">
				{{ value }}
			</template>
			<template #cell(istGesperrt)="{ value }">
				{{ value ? 'Gesperrt' : 'Nicht gesperrt' }}
			</template>
			<template #actions>
				<div class="inline-flex gap-4">
					<svws-ui-button type="trash"
						@click="deleteTelefonnummern"
						:disabled="(selectedTelefonart.length === 0) || (!updateKompetenz)" />
					<svws-ui-button title="Telefonnummer hinzufügen" type="icon"
						@click="addTelefonnummer"
						:disabled="!updateKompetenz">
						<span class="icon i-ri-add-line" />
					</svws-ui-button>
				</div>
			</template>
		</svws-ui-table>
		<svws-ui-modal :show="modalIsShown"
			@update:show="closeModal">
			<template #modalTitle>Telefonnummer hinzufügen</template>
			<template #modalContent>
				<svws-ui-input-wrapper :grid="2" class="text-left">
					<ui-select label="Telefonart"
						v-model="telefonart"
						:manager="telefonartenManager"
						:removable="false" />
					<svws-ui-text-input placeholder="Telefonnummer" type="text"
						v-model="data.telefonnummer"
						:max-len="20" />
					<svws-ui-tooltip class="col-span-full">
						<svws-ui-text-input placeholder="Bemerkung" type="text"
							v-model="data.bemerkung" />
						<template #content>
							{{ data.bemerkung ?? 'Bemerkung' }}
						</template>
					</svws-ui-tooltip>
					<svws-ui-spacing />
					<svws-ui-checkbox class="col-span-full" v-model="data.istGesperrt">
						Für Weitergabe gesperrt
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
				<svws-ui-notification v-if="manager().telefonartenById.size === 0" type="warning">
					Die Liste der Telefonarten ist leer, es sollte mindestens eine Telefonart unter Schule/Kataloge angelegt werden,
					damit zusätzliche Telefonnummern eine gültige Zuordnung haben.
				</svws-ui-notification>
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary"
						@click="closeModal">
						Abbrechen
					</svws-ui-button>
					<svws-ui-button @click="sendRequest"
						:disabled="cannotSendRequest">
						Speichern
					</svws-ui-button>
				</div>
			</template>
		</svws-ui-modal>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { SchuelerTelefon } from "@core/core/data/schueler/SchuelerTelefon";
	import type { Telefonart } from "@core/core/data/schule/Telefonart";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import type { DataTableColumn } from "@ui/types";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import type { SchuelerSchnelleingabeManager } from "@ui/ui/manager/schueler/SchuelerSchnelleingabeManager";
	import { computed, ref } from "vue";

	const props = defineProps<{
		manager: () => SchuelerSchnelleingabeManager;
		getTelefone: () => List<SchuelerTelefon>;
		addTelefon: (data: Partial<SchuelerTelefon>, idSchueler: number) => Promise<void>;
		patchTelefon: (data: Partial<SchuelerTelefon>, idEintrag: number) => Promise<void>;
		deleteTelefone: (idsEintraege: List<number>) => Promise<void>;
		updateKompetenz: boolean;
	}>();

	const selectedTelefonart = ref<SchuelerTelefon[]>([]);
	const data = ref<SchuelerTelefon>(new SchuelerTelefon());
	const telefonarten = computed(() => props.manager().telefonartenById.values());

	const cannotSendRequest = computed(() => {
		return (
			telefonart.value === null
			|| props.manager().telefonartenById.size === 0
			|| data.value.telefonnummer === null
			|| data.value.telefonnummer.length === 0
			|| !props.updateKompetenz
		);
	});

	const telefonart = computed<Telefonart | null>({
		get: () => props.manager().telefonartenById.get(data.value.idTelefonArt) ?? null,
		set: (value: Telefonart | null) => data.value.idTelefonArt = value === null ? -1 : value.id,
	});

	const telefonartenManager = new SelectManager({
		options: telefonarten,
		optionDisplayText: i => i.bezeichnung,
		selectionDisplayText: i => i.bezeichnung,
	});

	// --- requests ---

	function addTelefonnummer() {
		resetData();
		setMode(Mode.ADD);
		openModal();
	}

	async function sendRequest() {
		const { id, idSchueler, ...partialDataWithoutId } = data.value;
		const schuelerId = props.manager().stammdaten.id;
		if (currentMode.value === Mode.ADD) {
			await props.addTelefon(partialDataWithoutId, schuelerId);
		}
		if (currentMode.value === Mode.PATCH) {
			await props.patchTelefon(partialDataWithoutId, data.value.id);
		}
		enterDefaultMode();
	}

	function patchTelefonnummer(telefonEintrag: SchuelerTelefon) {
		resetData();
		setMode(Mode.PATCH);
		data.value.id = telefonEintrag.id;
		data.value.idTelefonArt = telefonEintrag.idTelefonArt;
		data.value.telefonnummer = telefonEintrag.telefonnummer;
		data.value.bemerkung = telefonEintrag.bemerkung;
		data.value.istGesperrt = telefonEintrag.istGesperrt;
		openModal();
	}

	async function deleteTelefonnummern() {
		if (selectedTelefonart.value.length === 0) {
			return;
		}
		const ids = new ArrayList<number>();
		for (const s of selectedTelefonart.value) {
			ids.add(s.id);
		}
		await props.deleteTelefone(ids);
		selectedTelefonart.value = [];
	}

	// --- modal ---

	const modalIsShown = ref<boolean>(false);

	function openModal() {
		modalIsShown.value = true;
	}

	function closeModal() {
		resetData();
		setMode(Mode.DEFAULT);
		modalIsShown.value = false;
	}

	// --- mode ---

	enum Mode { ADD, PATCH, DEFAULT }
	const currentMode = ref<Mode>(Mode.DEFAULT);

	function setMode(newMode: Mode) {
		currentMode.value = newMode;
	}

	function enterDefaultMode() {
		setMode(Mode.DEFAULT);
		resetData();
		closeModal();
	}

	// --- util ---

	function bezeichnung(idTelefonArt: number): string {
		return props.manager().telefonartenById.get(idTelefonArt)?.bezeichnung ?? "";
	}

	function resetData() {
		const defaultTelefon = new SchuelerTelefon();
		defaultTelefon.telefonnummer = '+49';
		const ersteTelefonArt = props.manager().telefonartenById.values().next().value;
		defaultTelefon.idTelefonArt = ersteTelefonArt?.id ?? 0;
		data.value = defaultTelefon;
	}

	const columns: DataTableColumn[] = [
		{ key: "idTelefonArt", label: "Ansprechpartner" },
		{ key: "telefonnummer", label: "Telefonnummern" },
		{ key: "bemerkung", label: "Bemerkung", span: 2 },
		{ key: "istGesperrt", label: "Gesperrt", span: 1, align: "right" },
	];

</script>
