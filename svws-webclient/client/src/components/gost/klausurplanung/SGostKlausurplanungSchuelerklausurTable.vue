<template>
	<svws-ui-checkbox class="mb-5" v-if="selectedItems !== undefined" :model-value="selectedItems.containsAll(schuelerklausuren)" @update:model-value="selectedItems.containsAll(schuelerklausuren) ? selectedItems.clear() : selectedItems.addAll(schuelerklausuren)">Alle auswählen</svws-ui-checkbox>
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
				class="svws-ui-tr" role="row" :title="cols.map(c => c.tooltip !== undefined ? c.tooltip : c.label).join(', ')">
				<div class="svws-ui-td" role="cell">
					<i-ri-draggable class="i-ri-draggable -m-0.5 -ml-3" />
					<svws-ui-checkbox v-if="selectedItems !== undefined" :model-value="selectedItems.contains(schuelertermin)" @update:model-value="selectedItems.contains(schuelertermin) ? selectedItems.remove(schuelertermin) : selectedItems.add(schuelertermin)" />
					{{ mapSchueler.get(props.kMan().schuelerklausurGetByIdOrException(schuelertermin.idSchuelerklausur).idSchueler)?.nachname }}
				</div>
				<div class="svws-ui-td" role="cell">{{ mapSchueler.get(props.kMan().schuelerklausurGetByIdOrException(schuelertermin.idSchuelerklausur).idSchueler)?.vorname }}</div>
				<div class="svws-ui-td svws-align-right" role="cell"><span class="svws-ui-badge" :style="`--background-color: ${ kMan().fachBgColorByKursklausur(kMan().kursklausurBySchuelerklausurTermin(schuelertermin)) };`">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(schuelertermin)) }}</span></div>
				<div class="svws-ui-td svws-align-right" role="cell">{{ kMan().kursLehrerKuerzelByKursklausur(kMan().kursklausurBySchuelerklausurTermin(schuelertermin)) }}</div>
				<div class="svws-ui-td svws-align-right" role="cell">{{ kMan().vorgabeBySchuelerklausurTermin(schuelertermin).dauer }}</div>
			</div>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import type { GostKursklausurManager, GostSchuelerklausurTermin, List, JavaSet, SchuelerListeEintrag} from "@core";
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type {DataTableColumn} from "@ui";
	import { computed} from "vue";

	const props = withDefaults(defineProps<{
		kMan: () => GostKursklausurManager;
		schuelerklausuren: List<GostSchuelerklausurTermin>;
		mapSchueler: Map<number, SchuelerListeEintrag>;
		onDrag?: (data: GostKlausurplanungDragData) => void;
		draggable?: (data: GostKlausurplanungDragData) => boolean;
		onDrop?: (zone: GostKlausurplanungDropZone) => void;
		selectedItems?: JavaSet<GostSchuelerklausurTermin>;
	}>(), {
		onDrag: undefined,
		draggable: () => false,
		onDrop: undefined,
		selectedItems: undefined,
	});


	function calculateColumns() {
		const cols: DataTableColumn[] = [
			{ key: "nachname", label: "Nachame", minWidth: 10.25 },
			{ key: "vorname", label: "Vorname", minWidth: 8 },
			{ key: "kurs", label: "Kurs", span: 1.25 },
			{ key: "kuerzel", label: "Lehrkraft" },
			{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", span: 0.5, align: "right", minWidth: 3.25 },
		];

		return cols;
	}

	const cols = computed(() => calculateColumns());

</script>
