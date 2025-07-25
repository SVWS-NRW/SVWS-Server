<template>
	<svws-ui-checkbox class="mb-2" v-if="selectedItems !== undefined && !schuelerklausuren.isEmpty()" :model-value="selectedItems.containsAll(schuelerklausuren)" @update:model-value="selectedItems.containsAll(schuelerklausuren) ? selectedItems.clear() : selectedItems.addAll(schuelerklausuren)">Alle auswählen</svws-ui-checkbox>
	<svws-ui-table :columns="cols" :disable-header="!slots.tableTitle">
		<template #noData>
			<slot name="noData">
				&nbsp;
			</slot>
		</template>
		<template #body>
			<div v-for="schuelertermin in schuelerklausuren"
				:key="schuelertermin.id"
				:data="schuelertermin"
				:draggable="props.onDrag && draggable(schuelertermin, termin!)"
				@dragstart="onDrag!($event, schuelertermin);$event.stopPropagation()"
				@dragend="onDrag!($event, undefined);$event.stopPropagation()"
				class="svws-ui-tr" role="row" style="grid-template-columns: minmax(4rem, 15fr) minmax(4rem, 8fr) minmax(4rem, 2fr) minmax(4rem, 8fr) minmax(4rem, 11fr) minmax(4rem, 4fr) minmax(4rem, 2fr);"
				:class="[klausurCssClasses === undefined ? '' : klausurCssClasses(schuelertermin, termin)]">
				<div class="svws-ui-td" role="cell">
					<span v-if="draggable(schuelertermin, termin!)" class="icon i-ri-draggable" />
					<svws-ui-checkbox v-if="selectedItems !== undefined" :model-value="selectedItems.contains(schuelertermin)" @update:model-value="selectedItems.contains(schuelertermin) ? selectedItems.remove(schuelertermin) : selectedItems.add(schuelertermin)" />
					{{ kMan().schuelerGetByIdOrException(kMan().schuelerklausurGetByIdOrException(schuelertermin.idSchuelerklausur).idSchueler)?.nachname }}
				</div>
				<div class="svws-ui-td" role="cell">{{ kMan().schuelerGetByIdOrException(kMan().schuelerklausurGetByIdOrException(schuelertermin.idSchuelerklausur).idSchueler)?.vorname }}</div>
				<div class="svws-ui-td" role="cell">
					{{ GostHalbjahr.fromIDorException(kMan().vorgabeBySchuelerklausurTermin(schuelertermin).halbjahr).jahrgang }}
				</div>
				<div class="svws-ui-td svws-align-left" role="cell"><span class="svws-ui-badge" :style="`color: var(--color-text-uistatic); background-color: ${ kMan().fachHTMLFarbeRgbaByKursklausur(kMan().kursklausurBySchuelerklausurTermin(schuelertermin)) };`">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(schuelertermin)) }}</span></div>
				<div class="svws-ui-td svws-align-left" role="cell">
					{{ kMan().datumSchuelerklausurVorgaenger(schuelertermin) !== null ? DateUtils.gibDatumGermanFormat(kMan().datumSchuelerklausurVorgaenger(schuelertermin)!) : "N.N." }}

					<svws-ui-tooltip v-if="kMan().schuelerklausurterminVorgaengerBySchuelerklausurtermin(schuelertermin)!.bemerkung !== null && (kMan().schuelerklausurterminVorgaengerBySchuelerklausurtermin(schuelertermin)!.bemerkung as string).trim().length > 0">
						<template #content>
							{{ kMan().schuelerklausurterminVorgaengerBySchuelerklausurtermin(schuelertermin)!.bemerkung }}
						</template>
						<span class="icon i-ri-eye-line" />
					</svws-ui-tooltip>
				</div>
				<div class="svws-ui-td svws-align-left" role="cell">{{ kMan().kursLehrerKuerzelByKursklausur(kMan().kursklausurBySchuelerklausurTermin(schuelertermin)) }}</div>
				<div class="svws-ui-td svws-align-center" role="cell">{{ kMan().vorgabeBySchuelerklausurTermin(schuelertermin).dauer }}</div>
				<!-- <div class="svws-ui-td svws-align-center" role="cell">
					<slot name="actions">
						<svws-ui-button v-if="patchKlausur !== undefined" type="icon" size="small" class="-mr-1" @click="patchKlausur(schuelertermin, {idTermin: null});$event.stopPropagation()"><span class="icon i-ri-delete-bin-line -mx-1.5" /></svws-ui-button>
					</slot>
				</div> -->
			</div>
		</template>
	</svws-ui-table>
</template>


<script setup lang="ts">

	import { DateUtils, GostHalbjahr } from "@core";
	import type { GostKlausurplanManager, GostSchuelerklausurTermin, List, JavaSet, GostKursklausur, GostKlausurenCollectionSkrsKrsData, GostKlausurtermin } from "@core";
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type {DataTableColumn} from "@ui";

	const props = withDefaults(defineProps<{
		kMan: () => GostKlausurplanManager;
		termin?: GostKlausurtermin | undefined;
		schuelerklausuren: List<GostSchuelerklausurTermin>;
		onDrag?: (event: DragEvent, data: GostKlausurplanungDragData) => void;
		draggable?: (data: GostKlausurplanungDragData, termin: GostKlausurtermin) => boolean;
		onDrop?: (zone: GostKlausurplanungDropZone) => void;
		selectedItems?: JavaSet<GostSchuelerklausurTermin>;
		patchKlausur?: (klausur: GostKursklausur | GostSchuelerklausurTermin, patch: Partial<GostKursklausur | GostSchuelerklausurTermin>) => Promise<void>;
		klausurCssClasses?: (klausur: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined) => void;
	}>(), {
		termin: undefined,
		onDrag: undefined,
		draggable: () => false,
		onDrop: undefined,
		selectedItems: undefined,
		patchKlausur: undefined,
		klausurCssClasses: undefined,
	});

	const slots = defineSlots();

	const cols: DataTableColumn[] = [
		{ key: "nachname", label: "Nachame", span: 15 },
		{ key: "vorname", label: "Vorname", span: 8 },
		{ key: "stufe", label: "Jg.", span: 2 },
		{ key: "kurs", label: "Kurs", span: 8 },
		{ key: "datum", label: "Datum", span: 11 },
		{ key: "kuerzel", label: "Lehrkraft", span: 4},
		{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", span: 2, align: "right"},
	];

</script>
