<template>
	<svws-ui-checkbox class="mb-2" v-if="(selectedItems !== undefined) && !schuelerklausuren.isEmpty()" :model-value="selectedItems.containsAll(schuelerklausuren)" @update:model-value="selectedItems.containsAll(schuelerklausuren) ? selectedItems.clear() : selectedItems.addAll(schuelerklausuren)">Alle auswählen</svws-ui-checkbox>
	<svws-ui-table :columns="nachschreiberAnsicht ? colsNachschreiber : cols" :disable-header="!slots.tableTitle">
		<template #noData>
			<slot name="noData">
				&nbsp;
			</slot>
		</template>
		<template #body>
			<div v-for="schuelertermin in schuelerklausuren"
				:key="schuelertermin.id"
				:data="schuelertermin"
				:draggable="isDraggable(schuelertermin)"
				@dragstart="onDragStart($event, schuelertermin)"
				@dragend="onDragEnd($event)"
				class="svws-ui-tr"
				:style="nachschreiberAnsicht ? nachschreiberRowStyle : rowStyle"
				:class="[klausurCssClasses === undefined ? '' : klausurCssClasses(schuelertermin, termin)]">
				<div class="svws-ui-td" :class="{'!px-0 flex justify-center overflow-visible': nachschreiberAnsicht}">
					<span v-if="isDraggable(schuelertermin)" class="icon i-ri-draggable" />
					<svws-ui-checkbox v-if="selectedItems !== undefined" :model-value="selectedItems.contains(schuelertermin)" @update:model-value="selectedItems.contains(schuelertermin) ? selectedItems.remove(schuelertermin) : selectedItems.add(schuelertermin)">
						{{ presenter.schuelerklausurterminNachname(schuelertermin) }}
					</svws-ui-checkbox>
					<template v-else-if="!nachschreiberAnsicht">
						{{ presenter.schuelerklausurterminNachname(schuelertermin) }}
					</template>
				</div>
				<div v-if="nachschreiberAnsicht" class="svws-ui-td min-w-0">
					<span class="truncate">{{ presenter.schuelerNameBySchuelerklausurtermin(schuelertermin) }}</span>
				</div>
				<div v-else class="svws-ui-td">{{ presenter.schuelerklausurterminVorname(schuelertermin) }}</div>
				<div class="svws-ui-td svws-align-center whitespace-nowrap">
					{{ presenter.schuelerklausurterminJahrgangText(schuelertermin) }}
				</div>
				<div class="svws-ui-td svws-align-left whitespace-nowrap">
					<s-gost-klausurplanung-kurs-badge :schuelerklausurtermin="schuelertermin" :tooltip="false" />
				</div>
				<div class="svws-ui-td svws-align-left whitespace-nowrap">
					<span>{{ nachschreiberAnsicht ? presenter.schuelerklausurterminVorgaengerDatumKurzJahrText(schuelertermin) : presenter.schuelerklausurterminVorgaengerDatumText(schuelertermin) }}</span>
					<svws-ui-tooltip v-if="presenter.schuelerklausurterminVorgaengerBemerkung(schuelertermin) !== null">
						<template #content>
							{{ presenter.schuelerklausurterminVorgaengerBemerkung(schuelertermin) }}
						</template>
						<span class="icon i-ri-eye-line" />
					</svws-ui-tooltip>
				</div>
				<div class="svws-ui-td svws-align-left whitespace-nowrap">
					{{ presenter.schuelerklausurterminLehrerKuerzel(schuelertermin) }}
				</div>
				<div class="svws-ui-td svws-align-right whitespace-nowrap">
					{{ presenter.schuelerklausurterminDauerText(schuelertermin) }}
				</div>
				<div v-if="nachschreiberAnsicht" class="svws-ui-td !px-0 flex justify-center">
					<svws-ui-button type="trash" size="small" title="Aus Termin entfernen" :disabled="!hatKompetenzUpdate" @click.stop="entplaneSchuelerklausurtermin(schuelertermin)" />
				</div>
			</div>
		</template>
	</svws-ui-table>
</template>


<script setup lang="ts">

	import type { GostSchuelerklausurtermin, List, JavaSet, GostKlausurtermin } from "@core";
	import { BenutzerKompetenz, ListUtils } from "@core";
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import { useBenutzerState, useGostKlausurplanungState, type DataTableColumn } from "@ui";
	import { computed, type HTMLAttributes } from "vue";
	import { useKlausurplanungPresenter } from "./SGostKlausurplanungPresenter";

	const props = defineProps<{
		termin?: GostKlausurtermin;
		schuelerklausuren: List<GostSchuelerklausurtermin>;
		onDrag?: (event: DragEvent, data: GostKlausurplanungDragData) => void;
		draggable?: (data: GostKlausurplanungDragData, termin: GostKlausurtermin) => boolean;
		onDrop?: (zone: GostKlausurplanungDropZone) => void;
		selectedItems?: JavaSet<GostSchuelerklausurtermin>;
		klausurCssClasses?: (klausur: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined) => HTMLAttributes["class"];
		nachschreiberAnsicht?: boolean;
	}>();

	const slots = defineSlots();
	const benutzerState = useBenutzerState();
	const state = useGostKlausurplanungState();
	const presenter = useKlausurplanungPresenter(state);
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const rowStyle = "grid-template-columns: minmax(4rem, 15fr) minmax(4rem, 8fr) minmax(4rem, 2fr) minmax(4rem, 8fr) minmax(4rem, 11fr) minmax(4rem, 4fr) minmax(4rem, 2fr);";
	const nachschreiberRowStyle = "grid-template-columns: 1.25rem minmax(7rem, 1fr) 2.5rem 5.75rem 7.25rem 3.75rem 3.5rem 2rem;";

	async function entplaneSchuelerklausurtermin(schuelertermin: GostSchuelerklausurtermin): Promise<void> {
		await state.patchSchuelerklausurtermine(ListUtils.create1(schuelertermin), { idTermin: null });
	}

	function isDraggable(schuelertermin: GostSchuelerklausurtermin): boolean {
		return (props.onDrag !== undefined) && (props.termin !== undefined) && (props.draggable?.(schuelertermin, props.termin) === true);
	}

	function onDragStart(event: DragEvent, schuelertermin: GostSchuelerklausurtermin): void {
		if (!isDraggable(schuelertermin)) {
			return;
		}
		props.onDrag?.(event, schuelertermin);
		event.stopPropagation();
	}

	function onDragEnd(event: DragEvent): void {
		props.onDrag?.(event, undefined);
		event.stopPropagation();
	}

	const cols: DataTableColumn[] = [
		{ key: "nachname", label: "Nachname", span: 15 },
		{ key: "vorname", label: "Vorname", span: 8 },
		{ key: "stufe", label: "Jg.", span: 2 },
		{ key: "kurs", label: "Kurs", span: 8 },
		{ key: "datum", label: "Datum", span: 11 },
		{ key: "kuerzel", label: "Lehrkraft", span: 4 },
		{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", span: 2, align: "right" },
	];

	const colsNachschreiber: DataTableColumn[] = [
		{ key: "drag", label: " ", fixedWidth: 1.25 },
		{ key: "name", label: "Name" },
		{ key: "stufe", label: "Jg.", fixedWidth: 2.5, align: "center" },
		{ key: "kurs", label: "Kurs" },
		{ key: "datum", label: "Datum" },
		{ key: "kuerzel", label: "Lehrkraft" },
		{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", align: "right" },
		{ key: "entplanen", label: " ", fixedWidth: 2 },
	];

</script>
