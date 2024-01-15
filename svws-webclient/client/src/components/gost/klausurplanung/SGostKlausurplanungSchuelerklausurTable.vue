<template>
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
				:draggable="draggable(schuelertermin)"
				@dragstart="onDrag(schuelertermin);$event.stopPropagation()"
				@dragend="onDrag(undefined);$event.stopPropagation()"
				class="svws-ui-tr" role="row" :title="cols.map(c => c.tooltip !== undefined ? c.tooltip : c.label).join(', ')">
				<div class="svws-ui-td" role="cell">
					<i-ri-draggable class="i-ri-draggable -m-0.5 -ml-3" />
					{{ mapSchueler.get(props.kursklausurmanager().schuelerklausurGetByIdOrException(schuelertermin.idSchuelerklausur).idSchueler)?.nachname }}
				</div>
				<div class="svws-ui-td" role="cell">{{ mapSchueler.get(props.kursklausurmanager().schuelerklausurGetByIdOrException(schuelertermin.idSchuelerklausur).idSchueler)?.vorname }}</div>
				<div class="svws-ui-td svws-align-right" role="cell"><span class="svws-ui-badge" :style="`--background-color: ${getBgColor(props.kursmanager.get(kursklausurmanager().kursklausurBySchuelerklausurTermin(schuelertermin).idKurs)!.kuerzel.split('-')[0])};`">{{ kursklausurmanager().kursklausurBySchuelerklausurTermin(schuelertermin).kursKurzbezeichnung }}</span></div>
				<div class="svws-ui-td svws-align-right" role="cell">{{ getLehrerKuerzel(kursklausurmanager().kursklausurBySchuelerklausurTermin(schuelertermin).idKurs) }}</div>
				<div class="svws-ui-td svws-align-right" role="cell">{{ kursklausurmanager().vorgabeBySchuelerklausurTermin(schuelertermin).dauer }}</div>
			</div>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import type { GostKursklausurManager, GostKursklausur, GostSchuelerklausurTermin, List, LehrerListeEintrag, KursManager, SchuelerListeEintrag} from "@core";
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type {DataTableColumn} from "@ui";
	import {computed} from "vue";
	import {ZulaessigesFach, DateUtils } from "@core";

	const props = withDefaults(defineProps<{
		kursklausurmanager: () => GostKursklausurManager;
		schuelerklausuren: List<GostSchuelerklausurTermin>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapSchueler: Map<number, SchuelerListeEintrag>;
		kursmanager: KursManager;
		onDrag?: (data: GostKlausurplanungDragData) => void;
		draggable?: (data: GostKlausurplanungDragData) => boolean;
		onDrop?: (zone: GostKlausurplanungDropZone) => void;
	}>(), {
		onDrag: undefined,
		draggable: () => false,
		onDrop: undefined,
	});


	function calculateColumns() {
		const cols: DataTableColumn[] = [
			{ key: "nachname", label: "Nachame", minWidth: 8.25 },
			{ key: "vorname", label: "Vorname", minWidth: 8 },
			{ key: "kurs", label: "Kurs", span: 1.25 },
			{ key: "kuerzel", label: "Lehrkraft" },
			{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", span: 0.5, align: "right", minWidth: 3.25 },
		];

		return cols;
	}

	const cols = computed(() => calculateColumns());

	function getLehrerKuerzel(kursid: number) {
		const kurs = props.kursmanager.get(kursid);
		const lehrerid = kurs?.lehrer;
		if (typeof lehrerid === 'number')
			return props.mapLehrer.get(lehrerid)?.kuerzel || ''
		return ''
	}

	const getBgColor = (kuerzel: string | null) => ZulaessigesFach.getByKuerzelASD(kuerzel).getHMTLFarbeRGBA(1.0); // TODO: Fachkuerzel für Kursklausur

</script>
