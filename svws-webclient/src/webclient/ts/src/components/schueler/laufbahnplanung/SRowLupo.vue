<template>
	<td class="border border-[#7f7f7f]/20 px-2 text-left select-text" :style="{ 'background-color': bgColor }">
		{{ fach.kuerzelAnzeige }}
	</td>
	<td class="border border-[#7f7f7f]/20 text-left select-text" :style="{ 'background-color': bgColor }">
		{{ fach.bezeichnung }}
	</td>
	<td class="border border-[#7f7f7f]/20 text-center" :style="{ 'background-color': bgColor }">
		{{ fach.wochenstundenQualifikationsphase }}
	</td>
	<td :class="[ 'text-center', { 'border border-[#7f7f7f]/20': bgColorIfLanguage !== '#7f7f7f' } ]" :style="{ 'background-color': bgColorIfLanguage }">
		{{ bgColorIfLanguage === "gray" ? "" : sprachenfolgeNr || "" }}
	</td>
	<td :class="[ 'text-center', { 'border border-[#7f7f7f]/20': bgColorIfLanguage !== '#7f7f7f' } ]" :style="{ 'background-color': bgColorIfLanguage }">
		{{ sprachenfolgeJahrgang }}
	</td>
	<td :class="[ 'w-12 text-center', {
			'cursor-pointer border': ef1_moeglich && !bewertet[0],
			'border-[#7f7f7f]/20': ef1_moeglich,
			'opacity-80': bewertet[0],
			'cursor-not-allowed': !ef1_moeglich || bewertet[0] || fachkombi_verboten_ef1 } ]"
		:style="{ 'background-color': (ef1_moeglich && !fachkombi_verboten_ef1) ? bgColor : 'gray' }" @click.stop="() => ef1_set()">
		<span :class="{'rounded-full px-2 bg-red-400': fachkombi_erforderlich_ef1}">
			{{ wahlen[0] }}</span>
	</td>
	<td :class="[ 'w-12 text-center', {
			'cursor-pointer border': ef2_moeglich && !bewertet[1],
			'border-[#7f7f7f]/20': ef2_moeglich,
			'opacity-80': bewertet[1],
			'cursor-not-allowed': !ef2_moeglich || bewertet[1] || fachkombi_verboten_ef2 } ]"
		:style="{ 'background-color': (ef2_moeglich && !fachkombi_verboten_ef2) ? bgColor: 'gray' }" @click.stop="() => ef2_set()">
		<span :class="{'rounded-full px-2 bg-red-400': fachkombi_erforderlich_ef2}">
			{{ wahlen[1] }}</span>
	</td>
	<td :class="[ 'w-12 text-center', {
			'cursor-pointer border': q11_moeglich && !bewertet[2],
			'border-[#7f7f7f]/20': q11_moeglich,
			'opacity-80': bewertet[2],
			'cursor-not-allowed': !q11_moeglich || bewertet[2] || fachkombi_verboten_q11 } ]"
		:style="{ 'background-color': (q11_moeglich && !fachkombi_verboten_q11) ? bgColor: 'gray' }" @click.stop="() => q11_set()">
		<span :class="{'rounded-full px-2 bg-red-400': fachkombi_erforderlich_q11}">
			{{ wahlen[2] }}</span>
	</td>
	<td :class="[ 'w-12 text-center', {
			'cursor-pointer border': q12_moeglich && !bewertet[3],
			'border-[#7f7f7f]/20': q12_moeglich,
			'opacity-80': bewertet[3],
			'cursor-not-allowed': !q12_moeglich || bewertet[3] || fachkombi_verboten_q12 } ]"
		:style="{ 'background-color': (q12_moeglich && !fachkombi_verboten_q12) ? bgColor: 'gray' }" @click.stop="() => q12_set()">
		<span :class="{'rounded-full px-2 bg-red-400': fachkombi_erforderlich_q12}">
			{{ wahlen[3] }} </span>
	</td>
	<td :class="[ 'w-12 text-center', {
			'cursor-pointer border': q21_moeglich && !bewertet[4],
			'border-[#7f7f7f]/20': q21_moeglich,
			'opacity-80': bewertet[4],
			'cursor-not-allowed': !q21_moeglich || bewertet[4] || fachkombi_verboten_q21 } ]"
		:style="{ 'background-color': (q21_moeglich && !fachkombi_verboten_q21) ? bgColor: 'gray' }" @click.stop="() => q21_set()">
		<span :class="{'rounded-full px-2 bg-red-400': fachkombi_erforderlich_q21}">
			{{ wahlen[4] }}</span>
	</td>
	<td :class="[ 'w-12 text-center', {
			'cursor-pointer border': q22_moeglich && !bewertet[5],
			'border-[#7f7f7f]/20': q22_moeglich,
			'opacity-80': bewertet[5],
			'cursor-not-allowed': !q22_moeglich || bewertet[5] || fachkombi_verboten_q22 } ]"
		:style="{ 'background-color': (q22_moeglich && !fachkombi_verboten_q22) ? bgColor: 'gray' }" @click.stop="() => q22_set()">
		<span :class="{'rounded-full px-2 bg-red-400': fachkombi_erforderlich_q22}">
			{{ wahlen[5] }}</span>
	</td>
	<td :class="[ 'w-12 text-center', {
			'cursor-pointer border': abi_moeglich && !bewertet[5],
			'border-[#7f7f7f]/20': abi_moeglich,
			'opacity-80': bewertet[5],
			'cursor-not-allowed': !abi_moeglich } ]"
		:style="{ 'background-color': abi_moeglich ? bgColor : 'gray' }" @click.stop="() => abi_set()">
		{{ abi_wahl }}
	</td>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";

	import { AbiturdatenManager, GostFach, GostFaecherManager, GostHalbjahr, GostJahrgangFachkombination, GostKursart, GostLaufbahnplanungFachkombinationTyp, List, Vector } from "@svws-nrw/svws-core-ts";
	import { DataSchuelerLaufbahnplanung } from "~/apps/schueler/DataSchuelerLaufbahnplanung";
	import { DataGostFachkombinationen } from "~/apps/gost/DataGostFachkombinationen";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";

	const { fach, dataLaufbahn, dataFaecher, dataFachkombinationen } = defineProps<{
		fach: GostFach,
		dataLaufbahn: DataSchuelerLaufbahnplanung
		dataFaecher: DataGostFaecher,
		dataFachkombinationen: DataGostFachkombinationen
	}>();

	const faechermanager: ComputedRef<GostFaecherManager | undefined> = computed(() => dataFaecher.manager);
	const abiturmanager: ComputedRef<AbiturdatenManager | undefined> = computed(() => dataLaufbahn.manager);

	const bewertet: ComputedRef<Array<boolean>> = computed(() => dataLaufbahn.daten?.bewertetesHalbjahr || []);

	const unbelegbarSprache: ComputedRef<boolean> = computed(() => dataLaufbahn.getFallsSpracheMoeglich(fach));

	const bgColor: ComputedRef<string> = computed(() => dataLaufbahn.getBgColor(fach));

	const bgColorIfLanguage: ComputedRef<string> = computed(() => dataLaufbahn.getBgColorIfLanguage(fach));

	const sprachenfolgeNr: ComputedRef<number> = computed(() => dataLaufbahn.sprachenfolgeNr(fach));
	const sprachenfolgeJahrgang: ComputedRef<string> = computed(() => dataLaufbahn.sprachenfolgeJahrgang(fach));

	const ef1_moeglich: ComputedRef<boolean> = computed(() => dataLaufbahn.getEF1Moeglich(fach));
	const ef2_moeglich: ComputedRef<boolean> = computed(() => dataLaufbahn.getEF2Moeglich(fach));
	const q11_moeglich: ComputedRef<boolean> = computed(() => dataLaufbahn.getQ11Moeglich(fach));
	const q12_moeglich: ComputedRef<boolean> = computed(() => dataLaufbahn.getQ12Moeglich(fach));
	const q21_moeglich: ComputedRef<boolean> = computed(() => dataLaufbahn.getQ21Moeglich(fach));
	const q22_moeglich: ComputedRef<boolean> = computed(() => dataLaufbahn.getQ22Moeglich(fach));
	const abi_moeglich: ComputedRef<boolean> = computed(() => dataLaufbahn.getAbiMoeglich(fach));

	const wahlen: ComputedRef<string[]> = computed(() => dataLaufbahn.getWahlen(fach));

	const abi_wahl: ComputedRef<string> = computed(() => ( dataLaufbahn.gostFachbelegungen[ fach.id ]?.abiturFach?.toString() || ""));

	const fachkombis: ComputedRef<List<GostJahrgangFachkombination>> =
		computed(() => {
			const result = new Vector<GostJahrgangFachkombination>();
			if (dataFachkombinationen.daten)
				for (const kombi of dataFachkombinationen.daten)
					if (kombi.fachID2 === fach.id && kombi.abiturjahr === dataLaufbahn.abiturjahr)
						result.add(kombi)
			return result;
		});

	const fachkombi_erforderlich: ComputedRef<List<GostJahrgangFachkombination>> =
		computed(()=> {
			const result = new Vector<GostJahrgangFachkombination>()
			for (const kombi of fachkombis.value)
				if (GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH.getValue() === kombi.typ)
					result.add(kombi);
			return result;
		})

	const fachkombi_verboten: ComputedRef<List<GostJahrgangFachkombination>> =
		computed(()=> {
			const result = new Vector<GostJahrgangFachkombination>()
			for (const kombi of fachkombis.value)
				if (GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue() === kombi.typ)
					result.add(kombi);
			return result;
		})

	function pruefeFachkombiErforderlich(halbjahr: GostHalbjahr) : boolean {
		if (fachkombi_erforderlich.value.size() > 0) {
			for (const kombi of fachkombi_erforderlich.value) {
				if (kombi.gueltigInHalbjahr[halbjahr.id]) {
					const fach1 = faechermanager.value?.get(kombi.fachID1);
					if (!fach1 || !abiturmanager.value)
						return false;
					const f1 = abiturmanager.value.getFachbelegungByKuerzel(fach1?.kuerzel || null)
					const f2 = abiturmanager.value.getFachbelegungByKuerzel(fach.kuerzel)
					const belegung_1 = abiturmanager.value.pruefeBelegungMitKursart(f1, GostKursart.fromKuerzel(kombi.kursart1)!, halbjahr)
					const belegung_2 = abiturmanager.value.pruefeBelegungMitKursart(f2, GostKursart.fromKuerzel(kombi.kursart1)!, halbjahr);
					if (belegung_2)
						return false;
					return belegung_1 !== belegung_2;
				}
			}
		}
		return false;
	}

	const fachkombi_erforderlich_ef1: ComputedRef<boolean> = computed(() => pruefeFachkombiErforderlich(GostHalbjahr.EF1));
	const fachkombi_erforderlich_ef2: ComputedRef<boolean> = computed(() => pruefeFachkombiErforderlich(GostHalbjahr.EF2));
	const fachkombi_erforderlich_q11: ComputedRef<boolean> = computed(() => pruefeFachkombiErforderlich(GostHalbjahr.Q11));
	const fachkombi_erforderlich_q12: ComputedRef<boolean> = computed(() => pruefeFachkombiErforderlich(GostHalbjahr.Q12));
	const fachkombi_erforderlich_q21: ComputedRef<boolean> = computed(() => pruefeFachkombiErforderlich(GostHalbjahr.Q21));
	const fachkombi_erforderlich_q22: ComputedRef<boolean> = computed(() => pruefeFachkombiErforderlich(GostHalbjahr.Q22));

	function pruefeFachkombiVerboten(halbjahr: GostHalbjahr) : boolean {
		if ((fachkombi_verboten.value.size() > 0)) {
			for (const kombi of fachkombi_verboten.value) {
				if (kombi.gueltigInHalbjahr[halbjahr.id]) {
					const fach = faechermanager.value?.get(kombi.fachID1)
					if (!fach)
						return false;
					const belegung = dataLaufbahn.getWahlen(fach)[halbjahr.id]
					return belegung ? true : false;
				}
			}
		}
		return false;
	}

	const fachkombi_verboten_ef1: ComputedRef<boolean> = computed(() => pruefeFachkombiVerboten(GostHalbjahr.EF1));
	const fachkombi_verboten_ef2: ComputedRef<boolean> = computed(() => pruefeFachkombiVerboten(GostHalbjahr.EF2));
	const fachkombi_verboten_q11: ComputedRef<boolean> = computed(() => pruefeFachkombiVerboten(GostHalbjahr.Q11));
	const fachkombi_verboten_q12: ComputedRef<boolean> = computed(() => pruefeFachkombiVerboten(GostHalbjahr.Q12));
	const fachkombi_verboten_q21: ComputedRef<boolean> = computed(() => pruefeFachkombiVerboten(GostHalbjahr.Q21));
	const fachkombi_verboten_q22: ComputedRef<boolean> = computed(() => pruefeFachkombiVerboten(GostHalbjahr.Q22));

	function ef1_set(): void { dataLaufbahn.setEF1Wahl(fach); }
	function ef2_set(): void { dataLaufbahn.setEF2Wahl(fach); }
	function q11_set(): void { dataLaufbahn.setQ11Wahl(fach); }
	function q12_set(): void { dataLaufbahn.setQ12Wahl(fach); }
	function q21_set(): void { dataLaufbahn.setQ21Wahl(fach); }
	function q22_set(): void { dataLaufbahn.setQ22Wahl(fach); }
	function abi_set(): void { dataLaufbahn.setAbiturWahl(fach); }

</script>
