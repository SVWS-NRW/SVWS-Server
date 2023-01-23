<template>
	<td class="border border-[#7f7f7f]/20 px-2 text-left"
		:style="{
			'background-color': bgColor
		}">
		{{ fach.kuerzelAnzeige }}
	</td>
	<td class="border border-[#7f7f7f]/20 text-left"
		:style="{
			'background-color': bgColor
		}">
		{{ fach.bezeichnung }}
	</td>
	<td class="border border-[#7f7f7f]/20 text-center"
		:style="{
			'background-color': bgColor
		}">
		{{ fach.istFremdSpracheNeuEinsetzend ? "&#x2713;" : "&#x2717;" }}
	</td>
	<td class="border border-[#7f7f7f]/20 text-center"
		:class="{ 'cursor-pointer': istProjektkurs }"
		:style="{
			'background-color': bgColor
		}"
		@click="set_pjk_stunden">
		<template v-if="istProjektkurs">
			<span class="px-1"
				:class="{
					'bg-green-400': fach.wochenstundenQualifikationsphase === 2,
					'bg-slate-50': fach.wochenstundenQualifikationsphase === 3
				}">2</span>
			<span class="px-1"
				:class="{
					'bg-green-400': fach.wochenstundenQualifikationsphase === 3,
					'bg-slate-50': fach.wochenstundenQualifikationsphase === 2
				}">3
			</span>
		</template>
		<template v-else>{{ fach.wochenstundenQualifikationsphase }}</template>
	</td>
	<td class="border border-[#7f7f7f]/20 text-left"
		:style="{
			'background-color': bgColor
		}">
		<div class="flex gap-1 p-0" v-if="istJahrgangAllgemein && hatLeitfach1">
			<svws-ui-multi-select headless
				:disabled="!leitfach1"
				:items="katalogLeitfaecher"
				:item-text="(i: GostFach) => i.kuerzelAnzeige?.toString() || ''"
				v-model="leitfach1" />
			<svws-ui-icon class="text-red-400 cursor-pointer" @click="leitfach1=undefined"><i-ri-delete-bin-2-line /></svws-ui-icon>
		</div>
		<span v-else>{{ fach.projektKursLeitfach1Kuerzel }}</span>
	</td>
	<td class="border border-[#7f7f7f]/20 text-left"
		:style="{
			'background-color': bgColor
		}">
		<div class="flex gap-1 p-0" v-if="istJahrgangAllgemein && istProjektkurs">
			<svws-ui-multi-select headless
				:disabled="!leitfach1"
				:items="katalogLeitfaecher"
				:item-text="(i: GostFach) => i.kuerzelAnzeige?.toString() || ''"
				v-model="leitfach2" />
			<svws-ui-icon class="text-red-400 cursor-pointer" @click="leitfach2=undefined"><i-ri-delete-bin-2-line /></svws-ui-icon>
		</div>
		<span v-else>{{ fach.projektKursLeitfach2Kuerzel }}</span>
	</td>

	<td :class="[
			'w-12 text-center',
			{
				'cursor-pointer border border-[#7f7f7f]/20': ef1_moeglich
			}
		]"
		:style="{
			'background-color': ef1_moeglich ? bgColor : 'gray'
		}"
		@click="ef1_set">
		{{ ef1_moeglich ? toggle(fach.istMoeglichEF1) : "" }}
	</td>
	<td :class="[
			'w-12 text-center',
			{
				'cursor-pointer border border-[#7f7f7f]/20': ef2_moeglich
			}
		]"
		:style="{
			'background-color': ef2_moeglich ? bgColor : 'gray'
		}"
		@click="ef2_set">
		{{ ef2_moeglich ? toggle(fach.istMoeglichEF2) : "" }}
	</td>
	<td :class="[
			'w-12 text-center',
			{
				'cursor-pointer border border-[#7f7f7f]/20': q11_moeglich
			}
		]"
		:style="{
			'background-color': q11_moeglich ? bgColor : 'gray'
		}"
		@click="q11_set">
		{{ q11_moeglich ? toggle(fach.istMoeglichQ11) : "" }}
	</td>
	<td :class="[
			'w-12 text-center',
			{
				'cursor-pointer border border-[#7f7f7f]/20': q12_moeglich
			}
		]"
		:style="{
			'background-color': q12_moeglich ? bgColor : 'gray'
		}"
		@click="q12_set">
		{{ q12_moeglich ? toggle(fach.istMoeglichQ12) : "" }}
	</td>
	<td :class="[
			'w-12 text-center',
			{
				'cursor-pointer border border-[#7f7f7f]/20': q21_moeglich
			}
		]"
		:style="{
			'background-color': q21_moeglich ? bgColor : 'gray'
		}"
		@click="q21_set">
		{{ q21_moeglich ? toggle(fach.istMoeglichQ21) : "" }}
	</td>
	<td :class="[
			'w-12 text-center',
			{
				'cursor-pointer border border-[#7f7f7f]/20': q22_moeglich
			}
		]"
		:style="{
			'background-color': q22_moeglich ? bgColor : 'gray'
		}"
		@click="q22_set">
		{{ q22_moeglich ? toggle(fach.istMoeglichQ22) : "" }}
	</td>
	<td :class="[
			'w-12 text-center',
			{
				'cursor-pointer border border-[#7f7f7f]/20': abi_gk_moeglich
			}
		]"
		:style="{
			'background-color': abi_gk_moeglich ? bgColor : 'gray'
		}"
		@click="abi_gk_set">
		{{ abi_gk_moeglich ? toggle(fach.istMoeglichAbiGK) : "" }}
	</td>
	<td :class="[
			'w-12 text-center',
			{
				'cursor-pointer border border-[#7f7f7f]/20': abi_lk_moeglich
			}
		]"
		:style="{
			'background-color': abi_lk_moeglich ? bgColor : 'gray'
		}"
		@click="abi_lk_set">
		{{ abi_lk_moeglich ? toggle(fach.istMoeglichAbiLK) : "" }}
	</td>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { GostFach } from "@svws-nrw/svws-core-ts";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { routeGost } from "~/router/apps/RouteGost";

	const props = defineProps<{
		fach: GostFach;
		dataFaecher: DataGostFaecher;
	}>();

	const katalogLeitfaecher: ComputedRef<(GostFach)[]> = computed(() => props.dataFaecher.faecherOhnePJKundVTF);

	const leitfach1: WritableComputedRef<GostFach | undefined> = computed({
		get: () => props.dataFaecher.faecherOhnePJKundVTF.find(item => props.fach.projektKursLeitfach1ID == item.id),
		set: (value) => {
			if (!routeGost.liste.ausgewaehlt?.abiturjahr)
				return;
			void props.dataFaecher.patch({ projektKursLeitfach1ID: value?.id || null }, props.fach, routeGost.liste.ausgewaehlt.abiturjahr.valueOf());
		}
	});

	const leitfach2: WritableComputedRef<GostFach | undefined> = computed({
		get: () => props.dataFaecher.faecherOhnePJKundVTF.find(item => props.fach.projektKursLeitfach2ID == item.id),
		set: (value) => {
			if (!routeGost.liste.ausgewaehlt?.abiturjahr || value === leitfach1.value)
				return;
			void props.dataFaecher.patch({ projektKursLeitfach2ID: value?.id || null }, props.fach, routeGost.liste.ausgewaehlt.abiturjahr.valueOf());
		}
	});

	const istJahrgangAllgemein: ComputedRef<boolean> = computed(() =>
		!routeGost.liste.ausgewaehlt || !routeGost.liste.ausgewaehlt.abiturjahr || routeGost.liste.ausgewaehlt.abiturjahr < 0
	);

	const istProjektkurs: ComputedRef<boolean> = computed(() => props.dataFaecher.ist_PJK(props.fach));

	const hatLeitfach1: ComputedRef<boolean> = computed(() => props.dataFaecher.hatLeitfach1(props.fach));

	const bgColor: ComputedRef<string> = computed(() => props.dataFaecher.getBgColor(props.fach));

	const ef1_moeglich: ComputedRef<boolean> = computed(() => props.dataFaecher.getEF1Moeglich(props.fach));
	const ef2_moeglich: ComputedRef<boolean> = computed(() => props.dataFaecher.getEF2Moeglich(props.fach));
	const q11_moeglich: ComputedRef<boolean> = computed(() => props.dataFaecher.getQ11Moeglich(props.fach));
	const q12_moeglich: ComputedRef<boolean> = computed(() => props.dataFaecher.getQ12Moeglich(props.fach));
	const q21_moeglich: ComputedRef<boolean> = computed(() => props.dataFaecher.getQ21Moeglich(props.fach));
	const q22_moeglich: ComputedRef<boolean> = computed(() => props.dataFaecher.getQ22Moeglich(props.fach));
	const abi_gk_moeglich: ComputedRef<boolean> = computed(() => props.dataFaecher.getAbiGKMoeglich(props.fach));
	const abi_lk_moeglich: ComputedRef<boolean> = computed(() => props.dataFaecher.getAbiLKMoeglich(props.fach));

	function toggle(bool: boolean): string {
		return bool ? "\u2705" : "\u274C";
	}

	function set(data: Partial<GostFach>) {
		void props.dataFaecher.patch(data, props.fach, routeGost.liste.ausgewaehlt?.abiturjahr?.valueOf());
	}

	function ef1_set(): void {
		if (!ef1_moeglich.value) return;
		set({ istMoeglichEF1: !props.fach.istMoeglichEF1 });
	}

	function ef2_set(): void {
		if (!ef2_moeglich.value) return;
		set({ istMoeglichEF2: !props.fach.istMoeglichEF2 });
	}

	function q11_set(): void {
		if (!q11_moeglich.value) return;
		set({ istMoeglichQ11: !props.fach.istMoeglichQ11 });
	}

	function q12_set(): void {
		if (!q12_moeglich.value) return;
		set({ istMoeglichQ12: !props.fach.istMoeglichQ12 });
	}

	function q21_set(): void {
		if (!q21_moeglich.value) return;
		set({ istMoeglichQ21: !props.fach.istMoeglichQ21 });
	}

	function q22_set(): void {
		if (!q22_moeglich.value) return;
		set({ istMoeglichQ22: !props.fach.istMoeglichQ22 });
	}

	function abi_gk_set(): void {
		if (!abi_gk_moeglich.value) return;
		set({ istMoeglichAbiGK: !props.fach.istMoeglichAbiGK });
	}

	function abi_lk_set(): void {
		if (!abi_gk_moeglich.value) return;
		set({ istMoeglichAbiLK: !props.fach.istMoeglichAbiLK });
	}

	function set_pjk_stunden(): void {
		if (!props.dataFaecher.ist_PJK(props.fach)) return;
		set({ wochenstundenQualifikationsphase: props.fach.wochenstundenQualifikationsphase == 2 ? 3 : 2 });
	}

</script>
