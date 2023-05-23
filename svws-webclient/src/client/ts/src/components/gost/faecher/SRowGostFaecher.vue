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
			:style="{ 'background-color': ef_moeglich ? bgColor : bgColorNichtMoeglich }">
			<span class="faecher-toggle--checkbox" v-if="ef_moeglich">
				<svws-ui-checkbox v-model="ef1" circle bw />
			</span>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate"
			:style="{ 'background-color': ef_moeglich ? bgColor : bgColorNichtMoeglich }">
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
			:style="{ 'background-color': abi_gk_moeglich ? bgColor : bgColorNichtMoeglich }">
			<span class="faecher-toggle--checkbox" v-if="abi_gk_moeglich">
				<svws-ui-checkbox v-model="abiGK" circle bw />
			</span>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center"
			:style="{ 'background-color': abi_lk_moeglich ? bgColor : bgColorNichtMoeglich }">
			<span class="faecher-toggle--checkbox" v-if="abi_lk_moeglich">
				<svws-ui-checkbox v-model="abiLK" circle bw />
			</span>
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";

	import type { GostFach, GostFaecherManager} from "@svws-nrw/svws-core";
	import { Fachgruppe, Jahrgaenge, ZulaessigesFach } from "@svws-nrw/svws-core";

	const props = defineProps<{
		patchFach: (data: Partial<GostFach>, fach_id: number) => Promise<boolean>;
		abiturjahr: number;
		fachId: number;
		mapLeitfaecher: Map<number, GostFach>;
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

	const leitfach1: WritableComputedRef<GostFach | undefined> = computed({
		get: () => fach.value.projektKursLeitfach1ID === null ? undefined : props.mapLeitfaecher.get(fach.value.projektKursLeitfach1ID),
		set: (value) => void doPatch({ projektKursLeitfach1ID: value?.id || null })
	});

	const leitfach2: WritableComputedRef<GostFach | undefined> = computed({
		get: () => fach.value.projektKursLeitfach2ID === null ? undefined : props.mapLeitfaecher.get(fach.value.projektKursLeitfach2ID),
		set: (value) => void doPatch({ projektKursLeitfach2ID: value?.id || null })
	});

	const istJahrgangAllgemein: ComputedRef<boolean> = computed(() => props.abiturjahr < 0);

	const istProjektkurs: ComputedRef<boolean> = computed(() => istPJK(fach.value));

	const hatLeitfach1: ComputedRef<boolean> = computed(() => {
		const fg = ZulaessigesFach.getByKuerzelASD(fach.value.kuerzel).getFachgruppe();
		return (fg === Fachgruppe.FG_VX) || (fg === Fachgruppe.FG_PX);
	});

	const bgColor: ComputedRef<string> = computed(() => ZulaessigesFach.getByKuerzelASD(fach.value.kuerzel).getHMTLFarbeRGB());

	const bgColorNichtMoeglich: ComputedRef<string> = computed(() => `color-mix(in srgb, ${ZulaessigesFach.getByKuerzelASD(fach.value.kuerzel).getHMTLFarbeRGB()}, rgb(170,170,170)`);

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

	function toggle(bool: boolean): string {
		return bool ? "\u2705" : "\u274C";
	}

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
