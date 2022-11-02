<template>
	<template v-if="(lk_kurszahl === 0 && lk) || (gk_kurszahl === 0 && gk) || (zk_kurszahl === 0 && zk)">
		<tr class="text-left" :style="{
			'background-color': bgColor
		}">
			<td class="px-2 border-y border-[#7f7f7f]/20 border-r" :colspan="schienen.size()+4">{{ fach.bezeichnung }} </td>
			<td class="bg-white"></td>
		</tr>
		<tr v-if="lk_kurszahl === 0 && lk" class="text-left" :style="{
			'background-color': bgColor
		}">
			<td class="px-4 border-y border-[#7f7f7f]/20" :colspan="schienen.size()">
				{{ fach.kuerzel }}-LK: {{lk}} Kurswahlen
			</td>
			<td class="px-2 border-y border-[#7f7f7f]/20 border-r" colspan="4">
				<svws-ui-button class="" size="small" @click="add_kurs('lk')">Kurs hinzufügen</svws-ui-button>
			</td>
			<td class="bg-white"></td>
		</tr>
		<tr v-if="gk_kurszahl === 0 && gk" class="text-left" :style="{
			'background-color': bgColor
		}">
			<td class="px-4 border-y border-[#7f7f7f]/20" :colspan="schienen.size()">
				{{ fach.kuerzel }}-GK: {{gk}} Kurswahlen
			</td>
			<td class="px-2 border-y border-[#7f7f7f]/20 border-r" colspan="4">
				<svws-ui-button class="" size="small" @click="add_kurs('gk')">Kurs hinzufügen</svws-ui-button>
			</td>
			<td class="bg-white"></td>
		</tr>
		<tr v-if="zk_kurszahl === 0 && zk" class="text-left" :style="{
			'background-color': bgColor
		}">
			<td class="px-4 border-y border-[#7f7f7f]/20" :colspan="schienen.size()">
				{{ fach.kuerzel }}-ZK: {{zk}} Kurswahlen
			</td>
			<td class="px-2 border-y border-[#7f7f7f]/20 border-r" colspan="4">
				<svws-ui-button class="" size="small" @click="add_kurs('zk')">Kurs hinzufügen</svws-ui-button>
			</td>
			<td class="bg-white"></td>
		</tr>
	</template>
</template>

<script setup lang="ts">
import {
	GostBlockungKurs,
	GostBlockungSchiene,
	GostStatistikFachwahl,
	GostStatistikFachwahlHalbjahr,
	List,
	Vector
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
		for (const k of kurse.value) {
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
//TODO M
const kurse: ComputedRef<Vector<GostBlockungKurs>> = computed(()=>
	app.dataKursblockung.manager?.getKursmengeSortiertNachKursartFachNummer() || new Vector<GostBlockungKurs>()
)
const schienen: ComputedRef<List<GostBlockungSchiene>> = computed(()=>
	app.dataKursblockung.daten?.schienen || new Vector<GostBlockungSchiene>()
)

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
