<template>
	<div class="svws-ui-tr svws-custom-color" role="row" :style="{ '--background-color': bgColor }">
		<div role="cell" class="svws-ui-td">
			<span :title="fach.kuerzelAnzeige || undefined">
				{{ fach.kuerzelAnzeige }}
			</span>
		</div>
		<div role="cell" class="svws-ui-td" :title="fach?.bezeichnung || undefined">
			<span class="line-clamp-1 break-all leading-tight -my-0.5">{{ fach.bezeichnung }}</span>
		</div>
		<div role="cell" class="svws-ui-td svws-align-center">
			<input type="checkbox" class="svws-ui-checkbox svws-headless" disabled v-model="fach.istFremdSpracheNeuEinsetzend">
		</div>
		<div role="cell" class="svws-ui-td svws-align-center svws-divider svws-no-padding" :class="{ 'cursor-pointer': istProjektkurs }" @click="set_pjk_stunden">
			<div v-if="istProjektkurs" class="flex items-center gap-0.5 border border-black/25 border-dashed hover:border-black/50 hover:border-solid hover:bg-white my-auto p-[0.1rem] rounded">
				<span :class="{ 'opacity-100 font-bold': fach.wochenstundenQualifikationsphase === 2, 'opacity-25 hover:opacity-100 font-medium': fach.wochenstundenQualifikationsphase === 3}">2</span>
				<span class="opacity-50">/</span>
				<span :class="{ 'opacity-100 font-bold': fach.wochenstundenQualifikationsphase === 3, 'opacity-25 hover:opacity-100 font-medium': fach.wochenstundenQualifikationsphase === 2}">3</span>
			</div>
			<span v-else class="my-auto">{{ fach.wochenstundenQualifikationsphase }}</span>
		</div>
		<div role="cell" class="svws-ui-td">
			<svws-ui-select v-if="istJahrgangAllgemein && hatLeitfach1" removable headless v-model="leitfach1" :items="leitfaecher1" :item-text="(i: GostFach) => i.kuerzelAnzeige ?? '–'" />
			<span v-else class="px-2 text-center w-full" :class="{'opacity-25': !fach.projektKursLeitfach1Kuerzel}">{{ fach.projektKursLeitfach1Kuerzel || '—' }}</span>
		</div>
		<div role="cell" class="svws-ui-td svws-divider">
			<svws-ui-select v-if="istJahrgangAllgemein && istProjektkurs" removable headless v-model="leitfach2" :items="leitfaecher2" :item-text="(i: GostFach) => i.kuerzelAnzeige ?? '–'" />
			<span v-else class="px-2 text-center w-full" :class="{'opacity-25': !fach.projektKursLeitfach2Kuerzel}">{{ fach.projektKursLeitfach2Kuerzel || '—' }}</span>
		</div>
		<div role="cell" class="svws-ui-td svws-align-center" :class="{'svws-disabled': !ef_moeglich}">
			<input v-if="ef_moeglich" type="checkbox" class="svws-ui-checkbox svws-headless" v-model="ef1">
		</div>
		<div role="cell" class="svws-ui-td svws-align-center svws-divider" :class="{'svws-disabled': !ef_moeglich}">
			<input v-if="ef_moeglich" type="checkbox" class="svws-ui-checkbox svws-headless" v-model="ef2">
		</div>
		<div role="cell" class="svws-ui-td svws-align-center">
			<input type="checkbox" class="svws-ui-checkbox svws-headless" v-model="q11">
		</div>
		<div role="cell" class="svws-ui-td svws-align-center svws-divider">
			<input type="checkbox" class="svws-ui-checkbox svws-headless" v-model="q12">
		</div>
		<div role="cell" class="svws-ui-td svws-align-center">
			<input type="checkbox" class="svws-ui-checkbox svws-headless" v-model="q21">
		</div>
		<div role="cell" class="svws-ui-td svws-align-center svws-divider">
			<input type="checkbox" class="svws-ui-checkbox svws-headless" v-model="q22">
		</div>
		<div role="cell" class="svws-ui-td svws-align-center" :class="{'svws-disabled': !abi_gk_moeglich}">
			<input v-if="abi_gk_moeglich" type="checkbox" class="svws-ui-checkbox svws-headless" v-model="abiGK">
		</div>
		<div role="cell" class="svws-ui-td svws-align-center" :class="{'svws-disabled': !abi_lk_moeglich}">
			<input v-if="abi_lk_moeglich" type="checkbox" class="svws-ui-checkbox svws-headless" v-model="abiLK">
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { List, GostFach, GostFaecherManager} from "@core";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";
	import { ArrayList, Fachgruppe, Jahrgaenge, ZulaessigesFach } from "@core";

	const props = defineProps<{
		patchFach: (data: Partial<GostFach>, fach_id: number) => Promise<boolean>;
		abiturjahr: number;
		fachId: number;
		faecherManager: () => GostFaecherManager;
	}>();

	async function doPatch(data: Partial<GostFach>) {
		await props.patchFach(data, props.fachId);
	}

	const fach = computed(()=> {
		const fach = props.faecherManager().get(props.fachId);
		if (fach === null)
			throw new Error("Fehler, es gibt kein gültiges Fach.");
		return fach;
	})

	function istPJK(fach: GostFach) : boolean {
		return ZulaessigesFach.getByKuerzelASD(fach.kuerzel).getFachgruppe() === Fachgruppe.FG_PX;
	}

	const leitfaecher1: ComputedRef<List<GostFach>> = computed(() => {
		const leitfaecher = props.faecherManager().getLeitfaecher();
		if (leitfach2.value === undefined)
			return leitfaecher;
		const result = new ArrayList<GostFach>(leitfaecher);
		result.removeElementAt(result.indexOf(leitfach2.value));
		return result;
	});

	const leitfaecher2: ComputedRef<List<GostFach>> = computed(() => {
		const leitfaecher = props.faecherManager().getLeitfaecher();
		if (leitfach1.value === undefined)
			return leitfaecher;
		const result = new ArrayList<GostFach>(leitfaecher);
		result.removeElementAt(result.indexOf(leitfach1.value));
		return result;
	});

	const leitfach1: WritableComputedRef<GostFach | undefined> = computed({
		get: () => {
			if (fach.value.projektKursLeitfach1ID === null)
				return undefined;
			let result = props.faecherManager().get(fach.value.projektKursLeitfach1ID);
			return result === null ? undefined : result;
		},
		set: (value) => void doPatch({ projektKursLeitfach1ID: value?.id || null })
	});

	const leitfach2: WritableComputedRef<GostFach | undefined> = computed({
		get: () => {
			if (fach.value.projektKursLeitfach2ID === null)
				return undefined;
			let result = props.faecherManager().get(fach.value.projektKursLeitfach2ID);
			return result === null ? undefined : result;
		},
		set: (value) => void doPatch({ projektKursLeitfach2ID: value?.id || null })
	});

	const istJahrgangAllgemein: ComputedRef<boolean> = computed(() => props.abiturjahr < 0);

	const istProjektkurs: ComputedRef<boolean> = computed(() => istPJK(fach.value));

	const hatLeitfach1: ComputedRef<boolean> = computed(() => {
		const fg = ZulaessigesFach.getByKuerzelASD(fach.value.kuerzel).getFachgruppe();
		return (fg === Fachgruppe.FG_VX) || (fg === Fachgruppe.FG_PX);
	});

	const bgColor: ComputedRef<string> = computed(() => ZulaessigesFach.getByKuerzelASD(fach.value.kuerzel).getHMTLFarbeRGB());
	/*const bgColorNichtMoeglich: ComputedRef<string> = computed(() => `color-mix(in srgb, ${ZulaessigesFach.getByKuerzelASD(fach.value.kuerzel).getHMTLFarbeRGB()}, rgb(170,170,170)`);*/

	const ef_moeglich: ComputedRef<boolean> = computed(() => {
		const fg = ZulaessigesFach.getByKuerzelASD(fach.value.kuerzel).getFachgruppe();
		return !((fg === Fachgruppe.FG_ME) || (fg === Fachgruppe.FG_PX));
	});

	const abi_gk_moeglich: ComputedRef<boolean> = computed(() => {
		const fg = ZulaessigesFach.getByKuerzelASD(fach.value.kuerzel).getFachgruppe();
		return (fg !== Fachgruppe.FG_ME) && (fg !== Fachgruppe.FG_VX) && (fg !== Fachgruppe.FG_PX);
	});

	const abi_lk_moeglich: ComputedRef<boolean> = computed(() => {
		const f = ZulaessigesFach.getByKuerzelASD(fach.value.kuerzel);
		if ((f.getJahrgangAb() === Jahrgaenge.JG_EF) ||
			((fach.value.biliSprache !== null) && (fach.value.biliSprache !== "D")))
			return false;
		const fg = f.getFachgruppe();
		return (fg !== Fachgruppe.FG_ME) && (fg !== Fachgruppe.FG_VX) && (fg !== Fachgruppe.FG_PX);
	});

	const ef1 = computed({
		get: () => fach.value.istMoeglichEF1,
		set: (value) => void doPatch({ istMoeglichEF1: !fach.value.istMoeglichEF1 })
	})

	const ef2 = computed({
		get: () => fach.value.istMoeglichEF2,
		set: (value) => void doPatch({ istMoeglichEF2: !fach.value.istMoeglichEF2 })
	})

	const q11 = computed({
		get: () => fach.value.istMoeglichQ11,
		set: (value) => void doPatch({ istMoeglichQ11: !fach.value.istMoeglichQ11 })
	})

	const q12 = computed({
		get: () => fach.value.istMoeglichQ12,
		set: (value) => void doPatch({ istMoeglichQ12: !fach.value.istMoeglichQ12 })
	})

	const q21 = computed({
		get: () => fach.value.istMoeglichQ21,
		set: (value) => void doPatch({ istMoeglichQ21: !fach.value.istMoeglichQ21 })
	})

	const q22 = computed({
		get: () => fach.value.istMoeglichQ22,
		set: (value) => void doPatch({ istMoeglichQ22: !fach.value.istMoeglichQ22 })
	})

	const abiGK = computed({
		get: () => fach.value.istMoeglichAbiGK,
		set: (value) => void doPatch({ istMoeglichAbiGK: !fach.value.istMoeglichAbiGK })
	})

	const abiLK = computed({
		get: () => fach.value.istMoeglichAbiLK,
		set: (value) => void doPatch({ istMoeglichAbiLK: !fach.value.istMoeglichAbiLK })
	})

	async function set_pjk_stunden() {
		if (!istPJK(fach.value))
			return;
		await doPatch({ wochenstundenQualifikationsphase: fach.value.wochenstundenQualifikationsphase === 2 ? 3 : 2 });
	}

</script>
