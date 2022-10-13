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
			:data="{ id: kursid, fachID: fachid, kursart: kursart }"
			class="select-none px-2"
			:draggable="true"
			:style="{
				'background-color': bgColor
			}"
		>
			{{ kursbezeichnung }}
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
		ZulaessigesFach
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

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
	// data() {
	// 	return {};
	// },

	const main: Main = injectMainApp();
	const app = main.apps.gost

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> = computed(() => {
		return app.dataKursblockungsergebnis.manager;
	});

	const fachid: ComputedRef<number> = computed(() => {
		return props.fach.fachID;
	});

	const kursart: ComputedRef<string | undefined> = computed(() => {
		const halbjahr = app.dataKursblockung.daten?.gostHalbjahr || 0;
		const belegung = props.fach.belegungen[halbjahr];
		if (!belegung)
			return undefined;
		return belegung.kursartKuerzel.valueOf();
	});

	const kursid: ComputedRef<number | undefined> = computed(() => {
		return belegung.value?.id;
	});

	const kursbezeichnung: ComputedRef<string> = computed(() => {
		const belegungValue = belegung.value;
		if (!belegungValue) {
			const fachname: string = gostfach.value?.kuerzelAnzeige?.valueOf() || "??";
			const kursartString: string = (!kursart.value) ? "??" : kursart.value;
			return fachname + "-" + kursartString;
		}
		return belegung.value?.name?.valueOf() || "???";
	});

	const belegung: ComputedRef<GostBlockungsergebnisKurs | undefined> = computed(() => {
		return manager.value?.getKursSchuelerZuordnungFuerSchuelerUndFach(props.schuelerId, fachid.value) || undefined;
	});

	const gostfach: ComputedRef<GostFach | undefined> = computed(() => {
		return manager.value?.getFach(fachid.value);
	});

	const bgColor: ComputedRef<string> = computed(() => {
		const zulfach = ZulaessigesFach.getByKuerzelASD(gostfach.value?.kuerzel || null);
		const fachgruppe = zulfach?.getFachgruppe();
		if (!fachgruppe)
			return "#ffffff";
		const rgb =
			(fachgruppe.farbe.getRed() << 16) |
			(fachgruppe.farbe.getGreen() << 8) |
			(fachgruppe.farbe.getBlue() << 0);
		return "#" + (0x1000000 + rgb).toString(16).slice(1);
	});
</script>
