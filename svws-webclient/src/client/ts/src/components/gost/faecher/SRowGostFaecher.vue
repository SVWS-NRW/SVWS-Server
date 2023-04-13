<template>
	<td class="px-2 text-left" :style="{ 'background-color': bgColor }">
		{{ fach.kuerzelAnzeige }}
	</td>
	<td class="text-left" :style="{ 'background-color': bgColor }">
		{{ fach.bezeichnung }}
	</td>
	<td class="text-center" :style="{ 'background-color': bgColor }">
		{{ fach.istFremdSpracheNeuEinsetzend ? "&#x2713;" : "&#x2717;" }}
	</td>
	<td class="text-center" :class="{ 'cursor-pointer': istProjektkurs }" :style="{ 'background-color': bgColor }"
		@click="set_pjk_stunden">
		<template v-if="istProjektkurs">
			<span class="px-1" :class="{ 'bg-green-400': fach.wochenstundenQualifikationsphase === 2, 'bg-slate-50': fach.wochenstundenQualifikationsphase === 3}">2</span>
			<span class="px-1" :class="{ 'bg-green-400': fach.wochenstundenQualifikationsphase === 3, 'bg-slate-50': fach.wochenstundenQualifikationsphase === 2}">3</span>
		</template>
		<template v-else>{{ fach.wochenstundenQualifikationsphase }}</template>
	</td>
	<td class="text-left" :style="{ 'background-color': bgColor }">
		<div class="flex gap-1 p-0" v-if="istJahrgangAllgemein && hatLeitfach1">
			<svws-ui-multi-select headless v-model="leitfach1" :disabled="!leitfach1" :items="mapLeitfaecher" :item-text="(i: GostFach) => i.kuerzelAnzeige ?? ''" />
			<svws-ui-icon class="text-red-400 cursor-pointer" @click="leitfach1=undefined"><i-ri-delete-bin-2-line /></svws-ui-icon>
		</div>
		<span v-else>{{ fach.projektKursLeitfach1Kuerzel }}</span>
	</td>
	<td class="text-left" :style="{ 'background-color': bgColor }">
		<div class="flex gap-1 p-0" v-if="istJahrgangAllgemein && istProjektkurs">
			<svws-ui-multi-select headless v-model="leitfach2" :disabled="!leitfach1" :items="mapLeitfaecher" :item-text="(i: GostFach) => i.kuerzelAnzeige ?? ''" />
			<svws-ui-icon class="text-red-400 cursor-pointer" @click="leitfach2=undefined"><i-ri-delete-bin-2-line /></svws-ui-icon>
		</div>
		<span v-else>{{ fach.projektKursLeitfach2Kuerzel }}</span>
	</td>

	<td :class="[ 'w-12 text-center', { 'cursor-pointer': ef_moeglich } ]"
		:style="{ 'background-color': ef_moeglich ? bgColor : 'gray' }" @click="ef1_set">
		{{ ef_moeglich ? toggle(fach.istMoeglichEF1) : "" }}
	</td>
	<td :class="[ 'w-12 text-center', { 'cursor-pointer': ef_moeglich } ]"
		:style="{ 'background-color': ef_moeglich ? bgColor : 'gray' }" @click="ef2_set">
		{{ ef_moeglich ? toggle(fach.istMoeglichEF2) : "" }}
	</td>
	<td :class="[ 'w-12 text-center', 'cursor-pointer' ]"
		:style="{ 'background-color': bgColor }" @click="q11_set">
		{{ toggle(fach.istMoeglichQ11) }}
	</td>
	<td :class="[ 'w-12 text-center', 'cursor-pointer' ]"
		:style="{ 'background-color': bgColor }" @click="q12_set">
		{{ toggle(fach.istMoeglichQ12) }}
	</td>
	<td :class="[ 'w-12 text-center', 'cursor-pointer' ]"
		:style="{ 'background-color': bgColor }" @click="q21_set">
		{{ toggle(fach.istMoeglichQ21) }}
	</td>
	<td :class="[ 'w-12 text-center', 'cursor-pointer' ]"
		:style="{ 'background-color': bgColor }" @click="q22_set">
		{{ toggle(fach.istMoeglichQ22) }}
	</td>
	<td :class="[ 'w-12 text-center', { 'cursor-pointer': abi_gk_moeglich } ]"
		:style="{ 'background-color': abi_gk_moeglich ? bgColor : 'gray' }" @click="abi_gk_set">
		{{ abi_gk_moeglich ? toggle(fach.istMoeglichAbiGK) : "" }}
	</td>
	<td :class="[ 'w-12 text-center', { 'cursor-pointer': abi_lk_moeglich } ]"
		:style="{ 'background-color': abi_lk_moeglich ? bgColor : 'gray' }" @click="abi_lk_set">
		{{ abi_lk_moeglich ? toggle(fach.istMoeglichAbiLK) : "" }}
	</td>
</template>

<script setup lang="ts">
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";

	import type { GostFach} from "@svws-nrw/svws-core";
	import { Fachgruppe, Jahrgaenge, ZulaessigesFach } from "@svws-nrw/svws-core";

	const props = defineProps<{
		patchFach: (data: Partial<GostFach>, fach_id: number) => Promise<boolean>;
		abiturjahr: number;
		fach: GostFach;
		mapLeitfaecher: Map<number, GostFach>;
	}>();

	async function doPatch(data: Partial<GostFach>) {
		await props.patchFach(data, props.fach.id);
	}

	function istPJK(fach: GostFach) : boolean {
		return ZulaessigesFach.getByKuerzelASD(fach.kuerzel).getFachgruppe() === Fachgruppe.FG_PX;
	}

	const leitfach1: WritableComputedRef<GostFach | undefined> = computed({
		get: () => props.fach.projektKursLeitfach1ID === null ? undefined : props.mapLeitfaecher.get(props.fach.projektKursLeitfach1ID),
		set: (value) => void doPatch({ projektKursLeitfach1ID: value?.id || null })
	});

	const leitfach2: WritableComputedRef<GostFach | undefined> = computed({
		get: () => props.fach.projektKursLeitfach2ID === null ? undefined : props.mapLeitfaecher.get(props.fach.projektKursLeitfach2ID),
		set: (value) => void doPatch({ projektKursLeitfach2ID: value?.id || null })
	});

	const istJahrgangAllgemein: ComputedRef<boolean> = computed(() => props.abiturjahr < 0);

	const istProjektkurs: ComputedRef<boolean> = computed(() => istPJK(props.fach));

	const hatLeitfach1: ComputedRef<boolean> = computed(() => {
		const fg = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getFachgruppe();
		return (fg === Fachgruppe.FG_VX) || (fg === Fachgruppe.FG_PX);
	});

	const bgColor: ComputedRef<string> = computed(() => ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getHMTLFarbeRGB());

	const ef_moeglich: ComputedRef<boolean> = computed(() => {
		const fg = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getFachgruppe();
		return !((fg === Fachgruppe.FG_ME) || (fg === Fachgruppe.FG_PX));
	});

	const abi_gk_moeglich: ComputedRef<boolean> = computed(() => {
		const fg = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getFachgruppe();
		return (fg !== Fachgruppe.FG_ME) && (fg !== Fachgruppe.FG_VX) && (fg !== Fachgruppe.FG_PX);
	});

	const abi_lk_moeglich: ComputedRef<boolean> = computed(() => {
		const fach = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel);
		if ((fach.getJahrgangAb() === Jahrgaenge.JG_EF) ||
			((props.fach.biliSprache !== null) && (props.fach.biliSprache !== "D")))
			return false;
		const fg = fach.getFachgruppe();
		return (fg !== Fachgruppe.FG_ME) && (fg !== Fachgruppe.FG_VX) && (fg !== Fachgruppe.FG_PX);
	});

	function toggle(bool: boolean): string {
		return bool ? "\u2705" : "\u274C";
	}

	async function ef1_set() {
		if (!ef_moeglich.value)
			return;
		await doPatch({ istMoeglichEF1: !props.fach.istMoeglichEF1 });
	}

	async function ef2_set() {
		if (!ef_moeglich.value)
			return;
		await doPatch({ istMoeglichEF2: !props.fach.istMoeglichEF2 });
	}

	async function q11_set() {
		await doPatch({ istMoeglichQ11: !props.fach.istMoeglichQ11 });
	}

	async function q12_set() {
		await doPatch({ istMoeglichQ12: !props.fach.istMoeglichQ12 });
	}

	async function q21_set() {
		await doPatch({ istMoeglichQ21: !props.fach.istMoeglichQ21 });
	}

	async function q22_set() {
		await doPatch({ istMoeglichQ22: !props.fach.istMoeglichQ22 });
	}

	async function abi_gk_set() {
		if (!abi_gk_moeglich.value)
			return;
		await doPatch({ istMoeglichAbiGK: !props.fach.istMoeglichAbiGK });
	}

	async function abi_lk_set() {
		if (!abi_gk_moeglich.value)
			return;
		await doPatch({ istMoeglichAbiLK: !props.fach.istMoeglichAbiLK });
	}

	async function set_pjk_stunden() {
		if (!istPJK(props.fach))
			return;
		await doPatch({ wochenstundenQualifikationsphase: props.fach.wochenstundenQualifikationsphase == 2 ? 3 : 2 });
	}

</script>
