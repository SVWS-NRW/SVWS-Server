<template>
	<svws-ui-content-card title="Weitere Telefonnummern">
		<svws-ui-table class="max-h-72! w-full"
			v-model="selectedTelefonnummern"
			:items="getListSchuelerTelefoneintraege()"
			:clicked="clickedTelefonnummer"
			@update:clicked="tel => patchTelefonnummer(tel)"
			:columns="telefonnummernTableColumns"
			:clickable="!readonly"
			:selectable="!readonly"
			scroll scroll-into-view count>
			<template #cell(idTelefonArt)="{ value }">
				{{ getBezeichnungTelefonart(value) }}
			</template>
			<template #cell(istGesperrt)="{ value }">
				{{ value ? 'Gesperrt' : 'Nicht gesperrt' }}
			</template>
			<template #actions v-if="!readonly">
				<div class="inline-flex gap-4">
					<svws-ui-button @click="deleteSelectedTelefonnummern" type="trash" :disabled="selectedTelefonnummern.length === 0" />
					<svws-ui-button @click="addTelefonnummer" type="icon" title="Telefonnummer hinzufügen">
						<span class="icon i-ri-add-line" />
					</svws-ui-button>
				</div>
			</template>
		</svws-ui-table>
		<svws-ui-modal :show="showModalTelefonnummer" @update:show="closeModalTelefonnummer">
			<template #modalTitle>Telefonnummer hinzufügen</template>
			<template #modalContent>
				<svws-ui-input-wrapper :grid="2" class="text-left">
					<svws-ui-select title="Telefonart" :items="mapTelefonArten.values()" v-model="selectedTelefonArt" :item-text="i => i.bezeichnung" />
					<svws-ui-text-input v-model="telefonnummernEntry.telefonnummer" type="tel" placeholder="Telefonnummer" :valid="v => phoneNumberIsValid(v, 20)"
						:max-len="20" />
					<svws-ui-tooltip class="col-span-full">
						<svws-ui-text-input v-model="telefonnummernEntry.bemerkung" type="text" placeholder="Bemerkung" />
						<template #content>
							{{ telefonnummernEntry.bemerkung ?? 'Bemerkung' }}
						</template>
					</svws-ui-tooltip>
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="telefonnummernEntry.istGesperrt" class="col-span-full">
						Für Weitergabe gesperrt
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
				<svws-ui-notification type="warning" v-if="mapTelefonArten.size === 0">
					Die Liste der Telefonarten ist leer, es sollte mindestens eine Telefonart unter
					Schule/Kataloge angelegt werden, damit zusätzliche Telefonnummern eine gültige Zuordnung haben.
				</svws-ui-notification>
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary" @click="closeModalTelefonnummer">Abbrechen</svws-ui-button>
					<svws-ui-button @click="saveTelefonnummer" :disabled="saveTelefonnummernDisabled">
						Speichern
					</svws-ui-button>
				</div>
			</template>
		</svws-ui-modal>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { SchuelerTelefonnummernProps } from "./SchuelerTelefonnummernProps";
	import { phoneNumberIsValid } from "~/util/validation/Validation";
	import { SchuelerTelefon } from "@core/core/data/schueler/SchuelerTelefon";
	import type { Telefonart } from "@core/core/data/schule/Telefonart";
	import { JavaString } from "@core/java/lang/JavaString";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { DataTableColumn } from "@ui/types";

	const props = defineProps<SchuelerTelefonnummernProps>();

	// --- State ---

	const selectedTelefonnummern = ref<SchuelerTelefon[]>([]);
	const clickedTelefonnummer = ref<SchuelerTelefon | null>(null);
	const telefonnummernEntry = ref<SchuelerTelefon>(new SchuelerTelefon());

	enum Mode { ADD, PATCH, DEFAULT }

	const currentTelefonnummernMode = ref<Mode>(Mode.DEFAULT);
	const showModalTelefonnummer = ref<boolean>(false);

	// --- Computed ---

	const selectedTelefonArt = computed<Telefonart | null>({
		get: () => props.mapTelefonArten.get(telefonnummernEntry.value.idTelefonArt) ?? null,
		set: (selected: Telefonart | null) => telefonnummernEntry.value.idTelefonArt = (selected === null) ? -1 : selected.id,
	});

	const saveTelefonnummernDisabled = computed<boolean>(() =>
		(selectedTelefonArt.value === null)
		|| (props.mapTelefonArten.size === 0)
		|| JavaString.isBlank(telefonnummernEntry.value.telefonnummer)
		|| !phoneNumberIsValid(telefonnummernEntry.value.telefonnummer, 20));

	const telefonnummernTableColumns: DataTableColumn[] = [
		{ key: "idTelefonArt", label: "Telefonart" },
		{ key: "telefonnummer", label: "Telefonnummern" },
		{ key: "bemerkung", label: "Bemerkung", span: 2 },
		{ key: "istGesperrt", label: "Gesperrt", span: 1, align: "right" },
	];

	// --- Hilfsfunktionen ---

	function getBezeichnungTelefonart(idTelefonArt: number): string {
		return props.mapTelefonArten.get(idTelefonArt)?.bezeichnung ?? "";
	}

	function resetTelefonnummer() {
		const defaultTelefon = new SchuelerTelefon();
		defaultTelefon.telefonnummer = '+49';
		const ersteTelefonArt = props.mapTelefonArten.values().next().value;
		defaultTelefon.idTelefonArt = ersteTelefonArt?.id ?? -1;
		telefonnummernEntry.value = defaultTelefon;
	}

	function setTelefonnummernMode(newMode: Mode) {
		currentTelefonnummernMode.value = newMode;
	}

	function openModalTelefonnummer() {
		showModalTelefonnummer.value = true;
	}

	function closeModalTelefonnummer() {
		resetTelefonnummer();
		setTelefonnummernMode(Mode.DEFAULT);
		showModalTelefonnummer.value = false;
	}

	function enterDefaultMode() {
		setTelefonnummernMode(Mode.DEFAULT);
		closeModalTelefonnummer();
	}

	function addTelefonnummer() {
		resetTelefonnummer();
		setTelefonnummernMode(Mode.ADD);
		openModalTelefonnummer();
	}

	function patchTelefonnummer(telefonnummer: SchuelerTelefon) {
		resetTelefonnummer();
		setTelefonnummernMode(Mode.PATCH);
		telefonnummernEntry.value.id = telefonnummer.id;
		telefonnummernEntry.value.idTelefonArt = telefonnummer.idTelefonArt;
		telefonnummernEntry.value.telefonnummer = telefonnummer.telefonnummer;
		telefonnummernEntry.value.bemerkung = telefonnummer.bemerkung;
		telefonnummernEntry.value.istGesperrt = telefonnummer.istGesperrt;
		clickedTelefonnummer.value = telefonnummer;
		openModalTelefonnummer();
	}

	async function saveTelefonnummer() {
		const { id, idSchueler, ...partialDataWithoutId } = telefonnummernEntry.value;
		if (currentTelefonnummernMode.value === Mode.ADD) {
			// Workaround: Der erste Eintrag wird vor dem Anlegen eines neuen SchuelerTelefons ausgewählt,
			// damit anschließend das Scrollen zum letzten angelegten Element in der Tabelle funktioniert
			if (!props.getListSchuelerTelefoneintraege().isEmpty()) {
				clickedTelefonnummer.value = props.getListSchuelerTelefoneintraege().getFirst();
			}
			await props.addSchuelerTelefoneintrag(partialDataWithoutId, props.idSchueler);
			clickedTelefonnummer.value = props.getListSchuelerTelefoneintraege().getLast();
		} else if (currentTelefonnummernMode.value === Mode.PATCH) {
			await props.patchSchuelerTelefoneintrag(partialDataWithoutId, telefonnummernEntry.value.id);
		}
		enterDefaultMode();
	}

	async function deleteSelectedTelefonnummern() {
		if (selectedTelefonnummern.value.length === 0) {
			return;
		}
		const ids = new ArrayList<number>();
		for (const s of selectedTelefonnummern.value) {
			ids.add(s.id);
		}
		await props.deleteSchuelerTelefoneintrage(ids);
		selectedTelefonnummern.value = [];
	}

</script>
