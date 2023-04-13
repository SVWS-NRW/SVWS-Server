<template>
	<tr :class="{ 'bg-red-400': (belegung === undefined) }">
		<svws-ui-drag-data :key="kursid" tag="td" :data="{ id: kursid, fachID: fach.fachID, kursart: kursartid }"
			:draggable="(belegung === undefined) && (!blockung_aktiv)" @drag-start="drag_started" @drag-end="drag_ended"
			class="select-none" :class="{ 'bg-white' : (belegung !== undefined), 'cursor-grab' : (belegung === undefined) }"
			:style="style">
			<div class="flex justify-between">
				<span> {{ get_kurs_name() }} </span>
				<svws-ui-icon v-if="!belegung"> <i-ri-forbid-2-line /> </svws-ui-icon>
			</div>
		</svws-ui-drag-data>
	</tr>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager,
		GostFach, GostFachwahl} from "@svws-nrw/svws-core";
	import { GostKursart, ZulaessigesFach } from "@svws-nrw/svws-core";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";

	type DndData = { id: number, fachID: number, kursart: number };

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		fach: GostFachwahl;
		kurse: Map<GostBlockungKurs, GostBlockungsergebnisKurs[]>;
		schuelerId: number;
		dragAndDropData?: DndData;
	}>();

	const emit = defineEmits<{
		(e: 'dnd', data: DndData | undefined): void;
	}>()

	const kursart: ComputedRef<string | undefined> = computed(() =>
		props.getErgebnismanager().getOfSchuelerOfFachKursart(props.schuelerId, props.fach.fachID).kuerzel);

	const kursartid: ComputedRef<number | undefined> = computed(() => GostKursart.fromKuerzel(!kursart.value ? null : kursart.value)?.id)

	const kursid: ComputedRef<number | undefined> = computed(() => belegung.value?.id);

	const belegung: ComputedRef<GostBlockungsergebnisKurs | undefined> = computed(() =>
		props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(props.schuelerId, props.fach.fachID) || undefined
	);

	const gostfach: ComputedRef<GostFach> = computed(() => props.getErgebnismanager().getFach(props.fach.fachID));

	const bgColor: ComputedRef<string> = computed(() => {
		if (belegung.value !== undefined)
			return "gray"
		const zulfach = ZulaessigesFach.getByKuerzelASD(gostfach.value.kuerzel);
		if (!zulfach)
			return "#ffffff";
		return zulfach.getHMTLFarbeRGB();
	});

	const style: ComputedRef = computed(() => {
		if (belegung.value !== undefined)
			return {};
		return { 'background-color': bgColor };
	});

	const blockung_aktiv: ComputedRef<boolean> = computed(() => props.getDatenmanager().daten().istAktiv);

	function get_kurs_name(): string {
		if (kursid.value === undefined)
			return props.getErgebnismanager().getFach(props.fach.fachID).kuerzelAnzeige + "-" + kursart.value || "?";
		return props.getErgebnismanager().getOfKursName(kursid.value);
	}

	function drag_started(e: DragEvent) {
		const transfer = e.dataTransfer;
		const data = JSON.parse(transfer?.getData('text/plain') || "");
		if (data === undefined)
			return;
		emit('dnd', data);
	}

	function drag_ended() {
		emit('dnd', undefined);
	}

</script>
