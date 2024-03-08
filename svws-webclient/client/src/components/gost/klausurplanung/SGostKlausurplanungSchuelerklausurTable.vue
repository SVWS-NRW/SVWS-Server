<template>
	<svws-ui-checkbox class="mb-5" v-if="selectedItems !== undefined && !schuelerklausuren.isEmpty()" :model-value="selectedItems.containsAll(schuelerklausuren)" @update:model-value="selectedItems.containsAll(schuelerklausuren) ? selectedItems.clear() : selectedItems.addAll(schuelerklausuren)">Alle auswählen</svws-ui-checkbox>
	<svws-ui-table :columns="cols" :disable-header="!$slots.tableTitle">
		<template #noData>
			<slot name="noData">
				&nbsp;
			</slot>
		</template>

		<template #body>
			<div v-for="schuelertermin in schuelerklausuren"
				:key="schuelertermin.id"
				:data="schuelertermin"
				:draggable="onDrag && draggable(schuelertermin)"
				@dragstart="onDrag!(schuelertermin);$event.stopPropagation()"
				@dragend="onDrag!(undefined);$event.stopPropagation()"
				class="svws-ui-tr" role="row">
				<div class="svws-ui-td" role="cell">
					<i-ri-draggable class="i-ri-draggable -m-0.5 -ml-3" />
					<svws-ui-checkbox v-if="selectedItems !== undefined" :model-value="selectedItems.contains(schuelertermin)" @update:model-value="selectedItems.contains(schuelertermin) ? selectedItems.remove(schuelertermin) : selectedItems.add(schuelertermin)" />
					{{ kMan().getSchuelerMap().get(props.kMan().schuelerklausurGetByIdOrException(schuelertermin.idSchuelerklausur).idSchueler)?.nachname }}
				</div>
				<div class="svws-ui-td" role="cell">{{ kMan().getSchuelerMap().get(props.kMan().schuelerklausurGetByIdOrException(schuelertermin.idSchuelerklausur).idSchueler)?.vorname }}</div>
				<div class="svws-ui-td svws-align-left" role="cell"><span class="svws-ui-badge" :style="`--background-color: ${ kMan().fachBgColorByKursklausur(kMan().kursklausurBySchuelerklausurTermin(schuelertermin)) };`">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(schuelertermin)) }}</span></div>
				<div class="svws-ui-td svws-align-left" role="cell">
					{{ kMan().datumSchuelerklausurVorgaenger(schuelertermin) !== null ? DateUtils.gibDatumGermanFormat(kMan().datumSchuelerklausurVorgaenger(schuelertermin)!) : "N.N." }}

					<svws-ui-tooltip v-if="kMan().schuelerklausurterminVorgaengerBySchuelerklausurtermin(schuelertermin)!.bemerkung !== null && (kMan().schuelerklausurterminVorgaengerBySchuelerklausurtermin(schuelertermin)!.bemerkung as string).trim().length > 0">
						<template #content>
							{{ kMan().schuelerklausurterminVorgaengerBySchuelerklausurtermin(schuelertermin)!.bemerkung }}
						</template>
						<i-ri-eye-line />
					</svws-ui-tooltip>
				</div>
				<div class="svws-ui-td svws-align-left" role="cell">{{ kMan().kursLehrerKuerzelByKursklausur(kMan().kursklausurBySchuelerklausurTermin(schuelertermin)) }}</div>
				<div class="svws-ui-td svws-align-center" role="cell">{{ kMan().vorgabeBySchuelerklausurTermin(schuelertermin).dauer }}</div>
				<div class="svws-ui-td svws-align-center" role="cell">
					<slot name="actions">
						<svws-ui-button v-if="patchKlausur !== undefined" type="icon" size="small" class="-mr-1" @click="patchKlausur(schuelertermin, {idTermin: null});$event.stopPropagation()"><i-ri-delete-bin-line class="-mx-1.5" /></svws-ui-button>
					</slot>
				</div>
			</div>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">
	import { DateUtils } from "@core";
	import type { GostKursklausurManager, GostSchuelerklausurTermin, List, JavaSet, GostKursklausur, GostKlausurenCollectionSkrsKrs } from "@core";
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type {DataTableColumn} from "@ui";
	import { computed} from "vue";

	const props = withDefaults(defineProps<{
		kMan: () => GostKursklausurManager;
		schuelerklausuren: List<GostSchuelerklausurTermin>;
		onDrag?: (data: GostKlausurplanungDragData) => void;
		draggable?: (data: GostKlausurplanungDragData) => boolean;
		onDrop?: (zone: GostKlausurplanungDropZone) => void;
		selectedItems?: JavaSet<GostSchuelerklausurTermin>;
		patchKlausur?: (klausur: GostKursklausur | GostSchuelerklausurTermin, patch: Partial<GostKursklausur | GostSchuelerklausurTermin>) => Promise<GostKlausurenCollectionSkrsKrs>;
	}>(), {
		onDrag: undefined,
		draggable: () => false,
		onDrop: undefined,
		selectedItems: undefined,
		patchKlausur: undefined,
	});


	function calculateColumns() {
		const cols: DataTableColumn[] = [
			{ key: "nachname", label: "Nachame", minWidth: 15 },
			{ key: "vorname", label: "Vorname", minWidth: 10 },
			{ key: "kurs", label: "Kurs", fixedWidth: 6 },
			{ key: "datum", label: "Datum", fixedWidth: 8 },
			{ key: "kuerzel", label: "Lehrkraft", fixedWidth: 4},
			{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", span: 0.5, align: "right", fixedWidth: 3},
			{ key: "actions", label: "Aktionen", span: 0.5, align: "right", fixedWidth: 3},
		];

		return cols;
	}

	const cols = computed(() => calculateColumns());

</script>
