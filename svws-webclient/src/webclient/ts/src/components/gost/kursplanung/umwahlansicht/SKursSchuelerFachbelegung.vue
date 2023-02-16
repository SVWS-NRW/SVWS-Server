<template>
	<tr class="cursor-pointer" :class="{ 'bg-red-400': (belegung === undefined) }">
		<svws-ui-drag-data :key="kursid" tag="td" :data="{ id: kursid, fachID: fach.fachID, kursart: kursartid }"
			:draggable="(belegung === undefined) && (!blockung_aktiv)" @drag-start="drag_started" @drag-end="drag_ended"
			class="select-none" :class="{ 'bg-white' : (belegung !== undefined), 'cursor-move' : (belegung === undefined) }"
			:style="style">
			<div class="flex justify-between">
				<span> {{ get_kurs_name() }} </span>
				<svws-ui-icon v-if="!belegung"> <i-ri-forbid-2-line /> </svws-ui-icon>
			</div>
		</svws-ui-drag-data>
	</tr>
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager,
		GostFach, GostFachwahl, GostKursart, ZulaessigesFach } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { routeApp } from "~/router/RouteApp";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		fach: GostFachwahl;
		kurse: Map<GostBlockungKurs, GostBlockungsergebnisKurs[]>;
		schuelerId: number;
	}>();

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

	const drag_data: WritableComputedRef<{ id: number, fachID: number, kursart: number } | undefined> = computed({
		get(): { id: number, fachID: number, kursart: number } {
			return routeApp.data.drag_and_drop_data.value;
		},
		set(value: { id: number, fachID: number, kursart: number } | undefined) {
			routeApp.data.drag_and_drop_data.value = value
		}
	});

	const blockung_aktiv: ComputedRef<boolean> = computed(() => props.getDatenmanager().daten().istAktiv);

	function get_kurs_name(): String {
		if (kursid.value === undefined)
			return props.getErgebnismanager().getFach(props.fach.fachID).kuerzelAnzeige + "-" + kursart.value || "?";
		return props.getErgebnismanager().getOfKursName(kursid.value);
	}

	function drag_started(e: DragEvent) {
		const transfer = e.dataTransfer;
		const data = JSON.parse(transfer?.getData('text/plain') || "") as { id: number, fachID: number, kursart: number } | undefined;
		if (data === undefined)
			return;
		drag_data.value = data
	}

	function drag_ended() {
		drag_data.value = undefined;
	}

</script>
