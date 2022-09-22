<template>
	<tr class="border border-[#7f7f7f]/20 px-2 text-left" :style="{
		'background-color': bgColor
	}">
		<td class="px-2">{{ fach.kuerzel }}</td>
		<td>{{ fach.bezeichnung }}</td>
		<td class="text-center">{{ lk }}</td>
		<td class="flex text-center">
			<div class="cursor-pointer rounded-lg bg-slate-100 px-2">
				<span @click="del_kurs('lk')">-</span>
				<span class="px-2">{{ lk_kurszahl }}</span>
				<span @click="add_kurs('lk')">+</span>
			</div>
		</td>
		<td class="text-center">{{ gk }}</td>
		<td class="flex text-center" :style="{ 'background-color': bgColor + '80' }">
			<div class="cursor-pointer rounded-lg bg-slate-100 px-2">
				<span @click="del_kurs('gk')">-</span>
				<span class="px-2">{{ gk_kurszahl }}</span>
				<span @click="add_kurs('gk')">+</span>
			</div>
		</td>
		<td class="text-center">{{ zk }}</td>
		<td class="flex px-2 text-center">
			<div class="cursor-pointer rounded-lg bg-slate-100 px-2">
				<span @click="del_kurs('zk')">-</span>
				<span class="px-2">{{ zk_kurszahl }}</span>
				<span @click="add_kurs('zk')">+</span>
			</div>
		</td>
	</tr>
</template>

<script setup lang="ts">
import {
	GostBlockungsdaten,
	GostStatistikFachwahl,
	GostStatistikFachwahlHalbjahr
} from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef } from "vue";

import { injectMainApp, Main } from "~/apps/Main";

const props = defineProps({
	fach: {
		type: GostStatistikFachwahl,
		required: true
	},
	halbjahr: {
		type: Number,
		required: true
	}
});

const main: Main = injectMainApp();
const app = main.apps.gost;

const kursezaehler: ComputedRef<{ lk: number; gk: number; zk: number }> =
	computed(() => {
		const zaehler = { lk: 0, gk: 0, zk: 0 };
		for (const k of daten.value.kurse) {
			if (k.fach_id === props.fach.id)
				switch (k.kursart) {
					case 1:
						zaehler.lk++;
						break;
					case 2:
						zaehler.gk++;
						break;
					case 3:
						zaehler.zk++;
						break;
				}
		}
		return zaehler;
	});

async function add_kurs(art: "lk" | "gk" | "zk") {
	const nr = art === "lk" ? 1 : art === "gk" ? 2 : 3;
	await app.dataKursblockung.add_blockung_kurse(props.fach.id, nr)
}
async function del_kurs(art: "lk" | "gk" | "zk") {
	const nr = art === "lk" ? 1 : art === "gk" ? 2 : 3;
	await app.dataKursblockung.del_blockung_kurse(props.fach.id, nr)
}
const daten: ComputedRef<GostBlockungsdaten> = computed(() => {
	return app.dataKursblockung.daten || new GostBlockungsdaten();
});

const fach_halbjahr: ComputedRef<GostStatistikFachwahlHalbjahr> = computed(
	() =>
		props.fach.fachwahlen[props.halbjahr] ||
		new GostStatistikFachwahlHalbjahr()
);

const gk: ComputedRef<number> = computed(() => {
	return fach_halbjahr.value.wahlenGK;
});
const lk: ComputedRef<number> = computed(() => {
	return fach_halbjahr.value.wahlenLK;
});
const zk: ComputedRef<number> = computed(() => {
	return fach_halbjahr.value.wahlenZK;
});

const gk_kurszahl: ComputedRef<number> = computed(() => kursezaehler.value.gk);
const lk_kurszahl: ComputedRef<number> = computed(() => kursezaehler.value.lk);
const zk_kurszahl: ComputedRef<number> = computed(() => kursezaehler.value.zk);

const bgColor: ComputedRef<string | undefined> = computed(() => {
	return app.dataFachwahlen.getBgColor(props.fach);
});
</script>
