<template>
	<tr
		v-if="visible"
		class="cursor-pointer px-2 text-left"
		:class="{ 'bg-red-400': (belegung === undefined) }"
	>
		<drag-data
			:key="kursid"
			tag="td"
			:data="{ id: kursid, fachID: fach.fachID, kursart: kursartid }"
			class="select-none px-2"
			:class="{ 'bg-slate-100' : (belegung !== undefined), 'cursor-move' : (belegung === undefined) }"
			:draggable="belegung === undefined && !blockung_aktiv"
			:style="{
				'background-color': belegung === undefined ? bgColor : false
			}"
			@drag-start="drag_started"
			@drag-end="drag_ended"
		>
			{{ get_kurs_name() }}
		</drag-data>
	</tr>
</template>

<script setup lang="ts">
	import {
		GostBlockungKurs,
		GostBlockungsergebnisKurs,
		GostBlockungsergebnisManager,
		GostFach,
		GostFachwahl,
		GostKursart,
		ZulaessigesFach
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps({
		fach: {
			type: GostFachwahl,
			required: true
		},
		kurse: {
			type: Map<
				GostBlockungKurs,
				GostBlockungsergebnisKurs[]
			>,
			required: true
		},
		schuelerId: { type: Number, required: true }
	});

	const main: Main = injectMainApp();
	const app = main.apps.gost

	const visible: ComputedRef<boolean> =
		computed(()=> true)

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> =
		computed(() => app.dataKursblockung.ergebnismanager);

	const kursart: ComputedRef<string | undefined> =
		computed(() =>
		manager.value?.getOfSchuelerOfFachKursart(props.schuelerId, props.fach.fachID).kuerzel.toString());

	const kursartid: ComputedRef<number | undefined> =
		computed(() => GostKursart.fromKuerzel(!kursart.value ? null : kursart.value)?.id)

	const kursid: ComputedRef<number | undefined> =
		computed(() => belegung.value?.id);

	const belegung: ComputedRef<GostBlockungsergebnisKurs | undefined> =
		computed(() => manager.value?.getOfSchuelerOfFachZugeordneterKurs(props.schuelerId, props.fach.fachID) || undefined);

	const gostfach: ComputedRef<GostFach | undefined> =
		computed(() => manager.value?.getFach(props.fach.fachID));

	const bgColor: ComputedRef<string> =
		computed(() => {
			if (belegung.value)
				return "gray"
			const zulfach = ZulaessigesFach.getByKuerzelASD(gostfach.value?.kuerzel || null);
			if (!zulfach)
				return "#ffffff";
			return zulfach.getHMTLFarbeRGB().valueOf()
		});

	const drag_data: WritableComputedRef<{ id: number, fachID: number, kursart: number } | undefined> =
		computed({
			get(): { id: number, fachID: number, kursart: number } {
				return main.config.drag_and_drop_data;
			},
			set(value: { id: number, fachID: number, kursart: number } | undefined) {
				main.config.drag_and_drop_data = value
			}
		});

	const blockung_aktiv: ComputedRef<boolean> =
		computed(()=> app.blockungsauswahl.ausgewaehlt?.istAktiv || false)

	const blockungsergebnis_aktiv: ComputedRef<boolean> =
		computed(()=> app.blockungsergebnisauswahl.ausgewaehlt?.istVorlage || false)

	function get_kurs_name(): String {
		if (kursid.value === undefined)
			return manager.value?.getFach(props.fach.fachID).kuerzelAnzeige + "-" + kursart.value || "?";
		return manager.value?.getOfKursName(kursid.value) || "";
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
