<template>
	<tr
		v-if="
			app.dataKursblockung.daten?.gostHalbjahr != null &&
			!!fach.belegungen[app.dataKursblockung.daten?.gostHalbjahr]
		"
		class="cursor-pointer border border-[#7f7f7f]/20 px-2 text-left"
		:class="{ 'bg-red-400': !belegung }"
	>
		<drag-data
			:key="kursid"
			tag="td"
			type="kurs"
			:data="{ id: kursid, fachID: fach.fachID, kursart: kursartid }"
			class="select-none px-2"
			:class="{'bg-slate-100': belegung, 'cursor-default':!!belegung}"
			:draggable="!belegung"
			:style="{
				'background-color': !belegung ? bgColor : false
			}"
		>
			{{ get_kurs_name() }}
		</drag-data>
	</tr>
</template>

<script setup lang="ts">
	import {
		AbiturFachbelegung,
		GostBlockungKurs,
		GostBlockungsergebnisKurs,
		GostBlockungsergebnisManager,
		GostFach,
		GostKursart,
		KursartKatalogEintrag,
		ZulaessigesFach
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";
import SCardGostKursblockungParameter from "./SCardGostKursblockungParameter.vue";

	const props = defineProps({
		fach: {
			type: AbiturFachbelegung,
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

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> = computed(() => {
		return app.dataKursblockungsergebnis.manager;
	});

	const kursart: ComputedRef<string | undefined> = computed(() => {
		const halbjahr = app.dataKursblockung.daten?.gostHalbjahr || 0;
		const belegung = props.fach.belegungen[halbjahr];
		if (!belegung)
			return undefined;
		return belegung.kursartKuerzel.toString()
	});

	const kursartid: ComputedRef<number | undefined> = computed(() => {
		return GostKursart.fromKuerzel(!kursart.value ? null : kursart.value)?.id;
	})

	const kursid: ComputedRef<number | undefined> = computed(() => {
		return belegung.value?.id;
	});

	const belegung: ComputedRef<GostBlockungsergebnisKurs | undefined> = computed(() => {
		try {
			return manager.value?.getOfSchuelerOfFachZugeordneterKurs(props.schuelerId, props.fach.fachID) || undefined;
		} catch (e) {return undefined}
	});

	const gostfach: ComputedRef<GostFach | undefined> = computed(() => {
		return manager.value?.getFach(props.fach.fachID);
	});

	const bgColor: ComputedRef<string> = computed(() => {
		if (belegung.value) return "gray"
		const zulfach = ZulaessigesFach.getByKuerzelASD(gostfach.value?.kuerzel || null);
		if (!zulfach)
			return "#ffffff";
		return zulfach.getHMTLFarbeRGB().valueOf();
	});

	function get_kurs_name(): String {
		if (!kursid.value) return manager.value?.getFach(props.fach.fachID).kuerzelAnzeige+"-"+kursart.value || "?"
		return manager.value?.getOfKursName(kursid.value) || ""
	}
</script>
