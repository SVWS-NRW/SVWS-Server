<template>
	<div class="data-table__tr data-table__tbody__tr" role="row" :style="{ 'background-color': bgColor }">
		<div role="cell" class="data-table__td">
			<div class="data-table__td-content" :title="fach.kuerzelAnzeige">
				{{ fach.kuerzelAnzeige }}
			</div>
		</div>
		<div role="cell" class="data-table__td">
			<span class="hyphens-auto">{{ fach.bezeichnung }}</span>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center">
			<i-ri-check-line v-if="fach.istFremdSpracheNeuEinsetzend" />
			<i-ri-close-line v-else class="opacity-25" />
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate" :class="{ 'cursor-pointer': istProjektkurs }" @click="set_pjk_stunden">
			<div v-if="istProjektkurs" class="flex items-center border border-black/25 rounded group">
				<span class="px-1 py-0.5 rounded-l" :class="{ 'bg-black font-bold text-white': fach.wochenstundenQualifikationsphase === 2, 'text-black/50 bg-white group-hover:text-black': fach.wochenstundenQualifikationsphase === 3}">2</span>
				<span class="px-1 py-0.5 rounded-r" :class="{ 'bg-black font-bold text-white': fach.wochenstundenQualifikationsphase === 3, 'text-black/50 bg-white group-hover:text-black': fach.wochenstundenQualifikationsphase === 2}">3</span>
			</div>
			<template v-else>{{ fach.wochenstundenQualifikationsphase }}</template>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center">
			<div class="flex gap-1 p-0" v-if="istJahrgangAllgemein && hatLeitfach1">
				<svws-ui-multi-select headless v-model="leitfach1" :disabled="!leitfach1" :items="mapLeitfaecher" :item-text="(i: GostFach) => i.kuerzelAnzeige ?? ''" />
				<svws-ui-button type="trash" @click="leitfach1=undefined" />
			</div>
			<span v-else>{{ fach.projektKursLeitfach1Kuerzel }}</span>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate">
			<div class="flex gap-1 p-0" v-if="istJahrgangAllgemein && istProjektkurs">
				<svws-ui-multi-select headless v-model="leitfach2" :disabled="!leitfach1" :items="mapLeitfaecher" :item-text="(i: GostFach) => i.kuerzelAnzeige ?? ''" />
				<svws-ui-button type="trash" @click="leitfach2=undefined" />
			</div>
			<span v-else>{{ fach.projektKursLeitfach2Kuerzel }}</span>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center" :class="[ { 'cursor-pointer': ef_moeglich } ]"
			:style="{ 'background-color': ef_moeglich ? bgColor : 'rgb(var(--color-gray))' }" @click="ef1_set">
			<template v-if="ef_moeglich">
				<i-ri-checkbox-line v-if="fach.istMoeglichEF1" />
				<i-ri-checkbox-blank-line v-else class="opacity-25" />
			</template>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center" :class="[ { 'cursor-pointer': ef_moeglich } ]"
			:style="{ 'background-color': ef_moeglich ? bgColor : 'rgb(var(--color-gray))' }" @click="ef2_set">
			<template v-if="ef_moeglich">
				<i-ri-checkbox-line v-if="fach.istMoeglichEF2" />
				<i-ri-checkbox-blank-line v-else class="opacity-25" />
			</template>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center" :class="[ 'cursor-pointer' ]"
			@click="q11_set">
			<i-ri-checkbox-line v-if="fach.istMoeglichQ11" />
			<i-ri-checkbox-blank-line v-else class="opacity-25" />
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center" :class="[ 'cursor-pointer' ]"
			@click="q12_set">
			<i-ri-checkbox-line v-if="fach.istMoeglichQ12" />
			<i-ri-checkbox-blank-line v-else class="opacity-25" />
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center" :class="[ 'cursor-pointer' ]"
			@click="q21_set">
			<i-ri-checkbox-line v-if="fach.istMoeglichQ21" />
			<i-ri-checkbox-blank-line v-else class="opacity-25" />
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate" :class="[ 'cursor-pointer' ]"
			@click="q22_set">
			<i-ri-checkbox-line v-if="fach.istMoeglichQ22" />
			<i-ri-checkbox-blank-line v-else class="opacity-25" />
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center" :class="[ { 'cursor-pointer': abi_gk_moeglich } ]"
			:style="{ 'background-color': abi_gk_moeglich ? bgColor : 'rgb(var(--color-gray))' }" @click="abi_gk_set">
			<template v-if="abi_gk_moeglich">
				<i-ri-checkbox-line v-if="fach.istMoeglichAbiGK" />
				<i-ri-checkbox-blank-line v-else class="opacity-25" />
			</template>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center" :class="[ { 'cursor-pointer': abi_lk_moeglich } ]"
			:style="{ 'background-color': abi_lk_moeglich ? bgColor : 'rgb(var(--color-gray))' }" @click="abi_lk_set">
			<template v-if="abi_lk_moeglich">
				<i-ri-checkbox-line v-if="fach.istMoeglichAbiLK" />
				<i-ri-checkbox-blank-line v-else class="opacity-25" />
			</template>
		</div>
	</div>
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
