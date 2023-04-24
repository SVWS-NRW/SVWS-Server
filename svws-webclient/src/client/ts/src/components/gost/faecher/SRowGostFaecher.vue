<template>
	<div class="data-table__tr data-table__tbody__tr" role="row" :style="{ 'background-color': bgColor }">
		<div role="cell" class="data-table__td">
			<div class="data-table__td-content" :title="fach.kuerzelAnzeige || undefined">
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
		<div role="cell" class="data-table__td data-table__td__align-center"
			:style="{ 'background-color': ef_moeglich ? bgColor : 'rgb(var(--color-gray))' }">
			<span class="faecher-toggle--checkbox" v-if="ef_moeglich">
				<svws-ui-checkbox v-model="ef1" circle bw />
			</span>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate"
			:style="{ 'background-color': ef_moeglich ? bgColor : 'rgb(var(--color-gray))' }">
			<span class="faecher-toggle--checkbox" v-if="ef_moeglich">
				<svws-ui-checkbox v-model="ef2" circle bw />
			</span>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center">
			<span class="faecher-toggle--checkbox">
				<svws-ui-checkbox v-model="q11" circle bw />
			</span>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate">
			<span class="faecher-toggle--checkbox">
				<svws-ui-checkbox v-model="q12" circle bw />
			</span>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center">
			<span class="faecher-toggle--checkbox">
				<svws-ui-checkbox v-model="q21" circle bw />
			</span>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate">
			<span class="faecher-toggle--checkbox">
				<svws-ui-checkbox v-model="q22" circle bw />
			</span>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center"
			:style="{ 'background-color': abi_gk_moeglich ? bgColor : 'rgb(var(--color-gray))' }">
			<span class="faecher-toggle--checkbox" v-if="abi_gk_moeglich">
				<svws-ui-checkbox v-model="abiGK" circle bw />
			</span>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center"
			:style="{ 'background-color': abi_lk_moeglich ? bgColor : 'rgb(var(--color-gray))' }">
			<span class="faecher-toggle--checkbox" v-if="abi_lk_moeglich">
				<svws-ui-checkbox v-model="abiLK" circle bw />
			</span>
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

	const ef1 = computed({
		get: () => props.fach.istMoeglichEF1,
		set: (value) => void doPatch({ istMoeglichEF1: !props.fach.istMoeglichEF1 })
	})

	const ef2 = computed({
		get: () => props.fach.istMoeglichEF2,
		set: (value) => void doPatch({ istMoeglichEF2: !props.fach.istMoeglichEF2 })
	})

	const q11 = computed({
		get: () => props.fach.istMoeglichQ11,
		set: (value) => void doPatch({ istMoeglichQ11: !props.fach.istMoeglichQ11 })
	})

	const q12 = computed({
		get: () => props.fach.istMoeglichQ12,
		set: (value) => void doPatch({ istMoeglichQ12: !props.fach.istMoeglichQ12 })
	})

	const q21 = computed({
		get: () => props.fach.istMoeglichQ21,
		set: (value) => void doPatch({ istMoeglichQ21: !props.fach.istMoeglichQ21 })
	})

	const q22 = computed({
		get: () => props.fach.istMoeglichQ22,
		set: (value) => void doPatch({ istMoeglichQ22: !props.fach.istMoeglichQ22 })
	})

	const abiGK = computed({
		get: () => props.fach.istMoeglichAbiGK,
		set: (value) => void doPatch({ istMoeglichAbiGK: !props.fach.istMoeglichAbiGK })
	})

	const abiLK = computed({
		get: () => props.fach.istMoeglichAbiLK,
		set: (value) => void doPatch({ istMoeglichAbiLK: !props.fach.istMoeglichAbiLK })
	})

	async function set_pjk_stunden() {
		if (!istPJK(props.fach))
			return;
		await doPatch({ wochenstundenQualifikationsphase: props.fach.wochenstundenQualifikationsphase == 2 ? 3 : 2 });
	}

</script>

<style lang="postcss" scoped>
	.faecher-toggle--checkbox {
		svg {
			@apply -my-0.5;
			width: 1.4em;
			height: 1.4em;

			&:hover,
			&:focus {
				@apply opacity-75;
			}
		}
	}
</style>
