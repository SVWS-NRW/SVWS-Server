<template>
	<div class="svws-ui-tr"
		:style="rowStyle"
		:data="klausur"
		:draggable="draggable === true"
		:class="[
			rowClass,
			{
				'cursor-grab active:cursor-grabbing group': draggable === true,
			},
		]"
		@dragstart="onDragStart"
		@dragend="onDragEnd">
		<div class="svws-ui-td !px-0 flex justify-center overflow-visible">
			<span v-if="draggable === true" class="icon i-ri-draggable" />
		</div>
		<div class="svws-ui-td" :class="{'-ml-2': inTooltip}">
			{{ GostHalbjahr.fromIDorException(state.manager.vorgabeByKursklausur(klausur).halbjahr).jahrgang }}
		</div>
		<div class="svws-ui-td whitespace-nowrap overflow-visible">
			<s-gost-klausurplanung-kurs-badge :kursklausur="klausur" :termin="badgeTermin" />
		</div>
		<div class="svws-ui-td whitespace-nowrap">
			{{ state.manager.kursLehrerKuerzelByKursklausur(klausur) }}
		</div>
		<div class="svws-ui-td flex justify-center whitespace-nowrap">
			<slot name="schreiber">
				<div>
					<span v-if="anzahlSchreiber !== state.manager.kursAnzahlKlausurschreiberByKursklausur(klausur)" class="font-bold">{{ anzahlSchreiber }}/</span>
					<span :class="anzahlSchreiber !== state.manager.kursAnzahlKlausurschreiberByKursklausur(klausur) ? 'line-through' : ''">{{ state.manager.kursAnzahlKlausurschreiberByKursklausur(klausur) }}</span>/{{ state.manager.kursAnzahlSchuelerGesamtByKursklausur(klausur) }}
				</div>
				<svws-ui-tooltip :hover="true" :indicator="false">
					<template #content>
						Kurs enthält externe Schüler
					</template>
					<svws-ui-badge v-if="state.manager.kursklausurMitExternenS(klausur)" type="highlight" size="normal">E</svws-ui-badge>
				</svws-ui-tooltip>
			</slot>
		</div>
		<div class="svws-ui-td svws-align-right whitespace-nowrap" :class="{'pr-3': inTooltip}">
			{{ state.manager.vorgabeByKursklausur(klausur).dauer }}
		</div>
		<slot name="zusatz" />
		<div v-if="showKursschiene" class="svws-ui-td svws-align-right whitespace-nowrap">
			<span class="opacity-50">{{ state.manager.kursSchieneByKursklausur(klausur).isEmpty() ? "-" : state.manager.kursSchieneByKursklausur(klausur).get(0) }}</span>
		</div>
		<div v-if="showQuartal" class="svws-ui-td svws-align-right whitespace-nowrap">
			<span class="opacity-50">{{ state.manager.vorgabeByKursklausur(klausur).quartal }}.</span>
		</div>
		<div v-if="showLastKlausurtermin" class="svws-ui-td svws-align-right whitespace-nowrap">
			<span class="opacity-50">{{ datumVorklausur }}</span>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, type HTMLAttributes } from "vue";
	import type { GostKlausurplanungDragData } from "./SGostKlausurplanung";
	import type { GostKlausurtermin } from "@core/core/data/gost/klausuren/GostKlausurtermin";
	import type { GostKursklausur } from "@core/core/data/gost/klausuren/GostKursklausur";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { useGostKlausurplanungState } from "@ui/states/GostKlausurplanungState";
	import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";

	const props = defineProps<{
		klausur: GostKursklausur;
		termin?: GostKlausurtermin;
		badgeTermin?: GostKlausurtermin;
		rowStyle?: string;
		rowClass?: HTMLAttributes["class"];
		draggable?: boolean;
		onDrag?: (event: DragEvent, data: GostKlausurplanungDragData) => void;
		inTooltip?: boolean;
		showKursschiene?: boolean;
		showQuartal?: boolean;
		showLastKlausurtermin?: boolean;
	}>();

	const state = useGostKlausurplanungState();

	const anzahlSchreiber = computed<number>(() => props.termin === undefined
		? state.manager.kursAnzahlKlausurschreiberByKursklausur(props.klausur)
		: state.manager.schuelerklausurterminAktuellGetMengeByTerminAndKursklausur(props.termin, props.klausur).size());

	const datumVorklausur = computed<string>(() => {
		const vorklausur = state.manager.kursklausurVorterminByKursklausur(props.klausur);
		if (vorklausur === null) {
			return "-";
		}
		const termin = state.manager.terminOrNullByKursklausur(vorklausur);
		return ((termin === null) || (termin.datum === null)) ? "-" : DateUtils.gibDatumGermanFormat(termin.datum).substring(0, 6);
	});

	function onDragStart(event: DragEvent): void {
		if (props.draggable !== true) {
			return;
		}
		props.onDrag?.(event, props.klausur);
		event.stopPropagation();
	}

	function onDragEnd(event: DragEvent): void {
		if (props.draggable !== true) {
			return;
		}
		props.onDrag?.(event, undefined);
		event.stopPropagation();
	}
</script>
