<template>
	<td
		class="border border-[#7f7f7f]/20 px-2 text-left"
		:style="{
			'background-color': bgColor
		}"
	>
		{{ fach.kuerzelAnzeige }}
	</td>
	<td
		class="border border-[#7f7f7f]/20 text-left"
		:style="{
			'background-color': bgColor
		}"
	>
		{{ fach.bezeichnung }}
	</td>
	<td
		class="border border-[#7f7f7f]/20 text-center"
		:style="{
			'background-color': bgColor
		}"
	>
		{{ fach.wochenstundenQualifikationsphase }}
	</td>
	<td
		:class="[
			'text-center',
			{ 'border border-[#7f7f7f]/20': bgColorIfLanguage !== '#7f7f7f' }
		]"
		:style="{
			'background-color': bgColorIfLanguage
		}"
	>
		{{ bgColorIfLanguage === "gray" ? "" : sprachenfolgeNr || "" }}
	</td>
	<td
		:class="[
			'text-center',
			{ 'border border-[#7f7f7f]/20': bgColorIfLanguage !== '#7f7f7f' }
		]"
		:style="{
			'background-color': bgColorIfLanguage
		}"
	>
		{{ sprachenfolgeJahrgang }}
	</td>
	<td
		:class="[
			'w-12 text-center',
			{
				'cursor-pointer border': ef1_moeglich && !bewertet[0],
				'border-[#7f7f7f]/20': ef1_moeglich,
				'cursor-not-allowed': !ef1_moeglich || bewertet[0]
			}
		]"
		:style="{
			'background-color': ef1_moeglich
				? bgColor + (bewertet[0] ? '80' : '')
				: 'gray'
		}"
		@click="() => ef1_set()"
	>
		{{ wahlen[0] }}
	</td>
	<td
		:class="[
			'w-12 text-center',
			{
				'cursor-pointer border': ef2_moeglich && !bewertet[1],
				'border-[#7f7f7f]/20': ef2_moeglich,
				'cursor-not-allowed': !ef2_moeglich || bewertet[1]
			}
		]"
		:style="{
			'background-color': ef2_moeglich
				? bgColor + (bewertet[1] ? '80' : '')
				: 'gray'
		}"
		@click="() => ef2_set()"
	>
		{{ wahlen[1] }}
	</td>
	<td
		:class="[
			'w-12 text-center',
			{
				'cursor-pointer border': q11_moeglich && !bewertet[2],
				'border-[#7f7f7f]/20': q11_moeglich,
				'cursor-not-allowed': !q11_moeglich || bewertet[2]
			}
		]"
		:style="{
			'background-color': q11_moeglich
				? bgColor + (bewertet[2] ? '80' : '')
				: 'gray'
		}"
		@click="() => q11_set()"
	>
		{{ wahlen[2] }}
	</td>
	<td
		:class="[
			'w-12 text-center',
			{
				'cursor-pointer border': q12_moeglich && !bewertet[3],
				'border-[#7f7f7f]/20': q12_moeglich,
				'cursor-not-allowed': !q12_moeglich || bewertet[3]
			}
		]"
		:style="{
			'background-color': q12_moeglich
				? bgColor + (bewertet[3] ? '80' : '')
				: 'gray'
		}"
		@click="() => q12_set()"
	>
		{{ wahlen[3] }}
	</td>
	<td
		:class="[
			'w-12 text-center',
			{
				'cursor-pointer border': q21_moeglich && !bewertet[4],
				'border-[#7f7f7f]/20': q21_moeglich,
				'cursor-not-allowed': !q21_moeglich || bewertet[4]
			}
		]"
		:style="{
			'background-color': q21_moeglich
				? bgColor + (bewertet[4] ? '80' : '')
				: 'gray'
		}"
		@click="() => q21_set()"
	>
		{{ wahlen[4] }}
	</td>
	<td
		:class="[
			'w-12 text-center',
			{
				'cursor-pointer border': q22_moeglich && !bewertet[5],
				'border-[#7f7f7f]/20': q22_moeglich,
				'cursor-not-allowed': !q22_moeglich || bewertet[5]
			}
		]"
		:style="{
			'background-color': q22_moeglich
				? bgColor + (bewertet[5] ? '80' : '')
				: 'gray'
		}"
		@click="() => q22_set()"
	>
		{{ wahlen[5] }}
	</td>
	<td
		:class="[
			'w-12 text-center',
			{
				'cursor-pointer border': abi_moeglich && !bewertet[5],
				'border-[#7f7f7f]/20': abi_moeglich,
				'cursor-not-allowed': !abi_moeglich
			}
		]"
		:style="{
			'background-color': abi_moeglich
				? bgColor + (bewertet[5] ? '80' : '')
				: 'gray'
		}"
		@click="() => abi_set()"
	>
		{{ abi_wahl || "" }}
	</td>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, Ref, ref } from "vue";

	import { GostFach } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import { DataSchuelerLaufbahnplanung } from "~/apps/schueler/DataSchuelerLaufbahnplanung";

	const props = defineProps({
		fach: {
			type: Object as () => GostFach,
			required: true
		}
	});

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const daten: ComputedRef<DataSchuelerLaufbahnplanung> = computed(() => {
		return app.dataGostLaufbahndaten || new DataSchuelerLaufbahnplanung();
	});

	const id: Ref<number> = ref(props.fach.id);

	const bewertet: ComputedRef<Array<boolean>> = computed(() => {
		return daten.value.daten?.bewertetesHalbjahr || [];
	});

	const unbelegbarSprache: ComputedRef<boolean> = computed(() => {
		return daten.value.getFallsSpracheMoeglich(props.fach);
	});

	const bgColor: ComputedRef<string> = computed<string>(() => {
		return daten.value.getBgColor(props.fach);
	});

	const bgColorIfLanguage: ComputedRef<string> = computed<string>(() => {
		return daten.value.getBgColorIfLanguage(props.fach);
	});

	const sprachenfolgeNr: ComputedRef<number> = computed<number>(() => {
		return daten.value.sprachenfolgeNr(props.fach);
	});

	const sprachenfolgeJahrgang: ComputedRef<string> = computed<string>(() => {
		return daten.value.sprachenfolgeJahrgang(props.fach);
	});

	const ef1_moeglich: ComputedRef<boolean> = computed<boolean>(() => {
		return daten.value.getEF1Moeglich(props.fach);
	});

	const ef2_moeglich: ComputedRef<boolean> = computed<boolean>(() => {
		return daten.value.getEF2Moeglich(props.fach);
	});

	const q11_moeglich: ComputedRef<boolean> = computed<boolean>(() => {
		return daten.value.getQ11Moeglich(props.fach);
	});

	const q12_moeglich: ComputedRef<boolean> = computed<boolean>(() => {
		return daten.value.getQ12Moeglich(props.fach);
	});

	const q21_moeglich: ComputedRef<boolean> = computed<boolean>(() => {
		return daten.value.getQ21Moeglich(props.fach);
	});

	const q22_moeglich: ComputedRef<boolean> = computed<boolean>(() => {
		return daten.value.getQ22Moeglich(props.fach);
	});

	const abi_moeglich: ComputedRef<boolean> = computed<boolean>(() => {
		return daten.value.getAbiMoeglich(props.fach);
	});

	const wahlen: ComputedRef<string[]> = computed<string[]>(() => {
		return daten.value.getWahlen(props.fach);
	});

	const abi_wahl: ComputedRef<string | null> = computed<string | null>(() => {
		return (
			daten.value.gostFachbelegungen[
				props.fach.id
			]?.abiturFach?.toString() || ""
		);
	});

	function ef1_set(): void {
		daten.value.setEF1Wahl(props.fach);
	}

	function ef2_set(): void {
		daten.value.setEF2Wahl(props.fach);
	}

	function q11_set(): void {
		daten.value.setQ11Wahl(props.fach);
	}

	function q12_set(): void {
		daten.value.setQ12Wahl(props.fach);
	}

	function q21_set(): void {
		daten.value.setQ21Wahl(props.fach);
	}

	function q22_set(): void {
		daten.value.setQ22Wahl(props.fach);
	}

	function abi_set(): void {
		daten.value.setAbiturWahl(props.fach);
	}
</script>
