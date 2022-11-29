<template>
	<td class="border border-[#7f7f7f]/20 px-2 text-left" :style="{ 'background-color': bgColor }" >
		{{ fach.kuerzelAnzeige }}</td>
	<td class="border border-[#7f7f7f]/20 text-left" :style="{ 'background-color': bgColor }" >
		{{ fach.bezeichnung }} </td>
	<td class="border border-[#7f7f7f]/20 text-center" :style="{ 'background-color': bgColor }" >
		{{ fach.wochenstundenQualifikationsphase }} </td>
	<td :class="[ 'text-center', { 'border border-[#7f7f7f]/20': bgColorIfLanguage !== '#7f7f7f' } ]" :style="{ 'background-color': bgColorIfLanguage }" >
		{{ bgColorIfLanguage === "gray" ? "" : sprachenfolgeNr || "" }} </td>
	<td :class="[ 'text-center', { 'border border-[#7f7f7f]/20': bgColorIfLanguage !== '#7f7f7f' } ]" :style="{ 'background-color': bgColorIfLanguage }" >
		{{ sprachenfolgeJahrgang }} </td>
	<td :class="[ 'w-12 text-center', {
				'cursor-pointer border': ef1_moeglich && !bewertet[0],
				'border-[#7f7f7f]/20': ef1_moeglich,
				'cursor-not-allowed': !ef1_moeglich || bewertet[0] || fachkombi_verboten_ef1 } ]"
			:style="{ 'background-color': ef1_moeglich && !fachkombi_verboten_ef1 ? bgColor + (bewertet[0] ? '80' : '') : 'gray' }" @click="() => ef1_set()" >
		<span :class="{'rounded-full px-2 bg-red-400': fachkombi_erforderlich_ef1, 'rounded-full px-2 bg-green-400': fachkombi_erforderlich_ef1}">
			{{ wahlen[0] }}</span> </td>
	<td :class="[ 'w-12 text-center', {
				'cursor-pointer border': ef2_moeglich && !bewertet[1],
				'border-[#7f7f7f]/20': ef2_moeglich,
				'cursor-not-allowed': !ef2_moeglich || bewertet[1] || fachkombi_verboten_ef2 } ]"
			:style="{ 'background-color': ef2_moeglich && !fachkombi_verboten_ef2 ? bgColor + (bewertet[1] ? '80' : '') : 'gray' }" @click="() => ef2_set()" >
		<span :class="{'rounded-full px-2 bg-red-400': fachkombi_erforderlich_ef2, 'rounded-full px-2 bg-green-400': fachkombi_erforderlich_ef2}">
			{{ wahlen[1] }}</span> </td>
	<td :class="[ 'w-12 text-center', {
				'cursor-pointer border': q11_moeglich && !bewertet[2],
				'border-[#7f7f7f]/20': q11_moeglich,
				'cursor-not-allowed': !q11_moeglich || bewertet[2] || fachkombi_verboten_q11 } ]"
			:style="{ 'background-color': q11_moeglich && !fachkombi_verboten_q11 ? bgColor + (bewertet[2] ? '80' : '') : 'gray' }" @click="() => q11_set()" >
		<span :class="{'rounded-full px-2 bg-red-400': fachkombi_erforderlich_q11, 'rounded-full px-2 bg-green-400': fachkombi_erforderlich_q11}">
			{{ wahlen[2] }}</span> </td>
	<td :class="[ 'w-12 text-center', {
				'cursor-pointer border': q12_moeglich && !bewertet[3],
				'border-[#7f7f7f]/20': q12_moeglich,
				'cursor-not-allowed': !q12_moeglich || bewertet[3] || fachkombi_verboten_q12 } ]"
			:style="{ 'background-color': q12_moeglich && !fachkombi_verboten_q12 ? bgColor + (bewertet[3] ? '80' : '') : 'gray' }" @click="() => q12_set()" >
		<span :class="{'rounded-full px-2 bg-red-400': fachkombi_erforderlich_q12, 'rounded-full px-2 bg-green-400': fachkombi_erforderlich_q12}">
			{{ wahlen[3] }} </span> </td>
	<td :class="[ 'w-12 text-center', {
				'cursor-pointer border': q21_moeglich && !bewertet[4],
				'border-[#7f7f7f]/20': q21_moeglich,
				'cursor-not-allowed': !q21_moeglich || bewertet[4] || fachkombi_verboten_q21 } ]"
			:style="{ 'background-color': q21_moeglich && !fachkombi_verboten_q21 ? bgColor + (bewertet[4] ? '80' : '') : 'gray' }" @click="() => q21_set()" >
		<span :class="{'rounded-full px-2 bg-red-400': fachkombi_erforderlich_q21, 'rounded-full px-2 bg-green-400': fachkombi_erforderlich_q21}">
			{{ wahlen[4] }}</span> </td>
	<td :class="[ 'w-12 text-center', {
				'cursor-pointer border': q22_moeglich && !bewertet[5],
				'border-[#7f7f7f]/20': q22_moeglich,
				'cursor-not-allowed': !q22_moeglich || bewertet[5] || fachkombi_verboten_q22 } ]"
			:style="{ 'background-color': q22_moeglich && !fachkombi_verboten_q22 ? bgColor + (bewertet[5] ? '80' : '') : 'gray' }" @click="() => q22_set()" >
		<span :class="{'rounded-full px-2 bg-red-400': fachkombi_erforderlich_q22, 'rounded-full px-2 bg-green-400': fachkombi_erforderlich_q22}">
			{{ wahlen[5] }}</span> </td>
	<td :class="[ 'w-12 text-center', {
				'cursor-pointer border': abi_moeglich && !bewertet[5],
				'border-[#7f7f7f]/20': abi_moeglich,
				'cursor-not-allowed': !abi_moeglich } ]"
			:style="{ 'background-color': abi_moeglich ? bgColor + (bewertet[5] ? '80' : '') : 'gray' }" @click="() => abi_set()" >
		{{ abi_wahl }} </td>
</template>

<script setup lang="ts">
	import { computed, ComputedRef } from "vue";

	import { GostFach, GostJahrgangFachkombination, GostLaufbahnplanungFachkombinationTyp, List, Vector } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import { DataSchuelerLaufbahnplanung } from "~/apps/schueler/DataSchuelerLaufbahnplanung";
	import { App } from "~/apps/BaseApp";

	const { fach } = defineProps({ fach: { type: Object as () => GostFach, required: true } });

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	const faechermanager = App.apps.gost.dataFaecher.manager;

	const daten: ComputedRef<DataSchuelerLaufbahnplanung> =
		computed(() => app.dataGostLaufbahndaten || new DataSchuelerLaufbahnplanung());

	const bewertet: ComputedRef<Array<boolean>> =
		computed(() => daten.value.daten?.bewertetesHalbjahr || []);

	const unbelegbarSprache: ComputedRef<boolean> =
		computed(() => daten.value.getFallsSpracheMoeglich(fach));

	const bgColor: ComputedRef<string> =
		computed(() => daten.value.getBgColor(fach));

	const bgColorIfLanguage: ComputedRef<string> =
		computed(() => daten.value.getBgColorIfLanguage(fach));

	const sprachenfolgeNr: ComputedRef<number> =
		computed(() => daten.value.sprachenfolgeNr(fach));

	const sprachenfolgeJahrgang: ComputedRef<string> =
		computed(() => daten.value.sprachenfolgeJahrgang(fach));

	const ef1_moeglich: ComputedRef<boolean> =
		computed(() => daten.value.getEF1Moeglich(fach));

	const ef2_moeglich: ComputedRef<boolean> =
		computed(() => daten.value.getEF2Moeglich(fach));

	const q11_moeglich: ComputedRef<boolean> =
		computed(() => daten.value.getQ11Moeglich(fach));

	const q12_moeglich: ComputedRef<boolean> =
		computed(() => daten.value.getQ12Moeglich(fach));

	const q21_moeglich: ComputedRef<boolean> =
		computed(() => daten.value.getQ21Moeglich(fach));

	const q22_moeglich: ComputedRef<boolean> =
		computed(() => daten.value.getQ22Moeglich(fach));

	const abi_moeglich: ComputedRef<boolean> =
		computed(() => daten.value.getAbiMoeglich(fach));
	
	const wahlen: ComputedRef<string[]> =
		computed(() => daten.value.getWahlen(fach));

	const abi_wahl: ComputedRef<string> =
		computed(() => ( daten.value.gostFachbelegungen[ fach.id ]?.abiturFach?.toString() || ""));

	const fachkombis: ComputedRef<List<GostJahrgangFachkombination>> =
		computed(() => {
			const result = new Vector<GostJahrgangFachkombination>();
			if (App.apps.gost.dataFachkombinationen.daten)
				for (let kombi of App.apps.gost.dataFachkombinationen.daten)
					if (kombi.fachID2 === fach.id)
						result.add(kombi)
			return result;
		});

	const fachkombi_erforderlich: ComputedRef<List<GostJahrgangFachkombination>> =
		computed(()=> {
			let result = new Vector<GostJahrgangFachkombination>()
			for (const kombi of fachkombis.value)
				if (GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH.getValue() === kombi.typ)
					result.add(kombi);
			return result;
		})

	const fachkombi_verboten: ComputedRef<List<GostJahrgangFachkombination>> =
		computed(()=> {
			let result = new Vector<GostJahrgangFachkombination>()
			for (const kombi of fachkombis.value)
				if (GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue() === kombi.typ)
					result.add(kombi);
			return result;
		})

	const fachkombi_erforderlich_ef1: ComputedRef<boolean> =
		computed(()=> {
			if (fachkombi_erforderlich.value.size() > 0 && !bewertet.value[0])
				for (const kombi of fachkombi_erforderlich.value)
					if (kombi.gueltigInHalbjahr[1]) {
						const fach = faechermanager?.get(kombi.fachID1 || 0)
						if (!fach) return false;
						const belegung_1 = daten.value.getWahlen(fach)[0]
						const belegung_2 = wahlen.value[0]
						return belegung_1 !== belegung_2 ? true : false;
					};
			return false;
		})
	const fachkombi_erforderlich_ef2: ComputedRef<boolean> =
		computed(()=> {
			if (fachkombi_erforderlich.value.size() > 0 && !bewertet.value[1])
				for (const kombi of fachkombi_erforderlich.value)
					if (kombi.gueltigInHalbjahr[2]) {
						const fach = faechermanager?.get(kombi.fachID1 || 0)
						if (!fach) return false;
						const belegung_1 = daten.value.getWahlen(fach)[1]
						const belegung_2 = wahlen.value[1]
						return belegung_1 !== belegung_2 ? true : false;
					};
			return false;
		})
	const fachkombi_erforderlich_q11: ComputedRef<boolean> =
		computed(()=> {
			if (fachkombi_erforderlich.value.size() > 0 && !bewertet.value[2])
				for (const kombi of fachkombi_erforderlich.value)
					if (kombi.gueltigInHalbjahr[3]) {
						const fach = faechermanager?.get(kombi.fachID1 || 0)
						if (!fach) return false;
						const belegung_1 = daten.value.getWahlen(fach)[2]
						const belegung_2 = wahlen.value[2]
						return belegung_1 !== belegung_2 ? true : false;
					};
			return false;
		})
	const fachkombi_erforderlich_q12: ComputedRef<boolean> =
		computed(()=> {
			if (fachkombi_erforderlich.value.size() > 0 && !bewertet.value[3])
				for (const kombi of fachkombi_erforderlich.value)
					if (kombi.gueltigInHalbjahr[4]) {
						const fach = faechermanager?.get(kombi.fachID1 || 0)
						if (!fach) return false;
						const belegung_1 = daten.value.getWahlen(fach)[3]
						const belegung_2 = wahlen.value[3]
						return belegung_1 !== belegung_2 ? true : false;
					};
			return false;
		})
	const fachkombi_erforderlich_q21: ComputedRef<boolean> =
		computed(()=> {
			if (fachkombi_erforderlich.value.size() > 0 && !bewertet.value[4])
				for (const kombi of fachkombi_erforderlich.value)
					if (kombi.gueltigInHalbjahr[5]) {
						const fach = faechermanager?.get(kombi.fachID1 || 0)
						if (!fach) return false;
						const belegung_1 = daten.value.getWahlen(fach)[4]
						const belegung_2 = wahlen.value[4]
						return belegung_1 !== belegung_2 ? true : false;
					};
			return false;
		})
	const fachkombi_erforderlich_q22: ComputedRef<boolean> =
		computed(()=> {
			if (fachkombi_erforderlich.value.size() > 0 && !bewertet.value[5])
				for (const kombi of fachkombi_erforderlich.value)
					if (kombi.gueltigInHalbjahr[6]) {
						const fach = faechermanager?.get(kombi.fachID1 || 0)
						if (!fach) return false;
						const belegung_1 = daten.value.getWahlen(fach)[5]
						const belegung_2 = wahlen.value[5]
						return belegung_1 !== belegung_2 ? true : false;
					};
			return false;
		})
	const fachkombi_verboten_ef1: ComputedRef<boolean> =
		computed(()=> {
			if (fachkombi_verboten.value.size() > 0 && !bewertet.value[0])
				for (const kombi of fachkombi_verboten.value)
					if (kombi.gueltigInHalbjahr[1]) {
						const fach = faechermanager?.get(kombi.fachID1)
						if (!fach) return false;
						const belegung = daten.value.getWahlen(fach)[0]
						return belegung ? true : false;
					};
			return false;
		})
	const fachkombi_verboten_ef2: ComputedRef<boolean> =
		computed(()=> {
			if (fachkombi_verboten.value.size() > 0 && !bewertet.value[1])
				for (const kombi of fachkombi_verboten.value)
					if (kombi.gueltigInHalbjahr[2]) {
						const fach = faechermanager?.get(kombi.fachID1 || 0)
						if (!fach) return false;
						const belegung = daten.value.getWahlen(fach)[1]
						return belegung ? true : false;
					};
			return false;
		})
	const fachkombi_verboten_q11: ComputedRef<boolean> =
		computed(()=> {
			if (fachkombi_verboten.value.size() > 0 && !bewertet.value[2])
				for (const kombi of fachkombi_verboten.value)
					if (kombi.gueltigInHalbjahr[3]) {
						const fach = faechermanager?.get(kombi.fachID1 || 0)
						if (!fach) return false;
						const belegung = daten.value.getWahlen(fach)[2]
						return belegung ? true : false;
					};
			return false;
		})
	const fachkombi_verboten_q12: ComputedRef<boolean> =
		computed(()=> {
			if (fachkombi_verboten.value.size() > 0 && !bewertet.value[3])
				for (const kombi of fachkombi_verboten.value)
					if (kombi.gueltigInHalbjahr[4]) {
						const fach = faechermanager?.get(kombi.fachID1 || 0)
						if (!fach) return false;
						const belegung = daten.value.getWahlen(fach)[3]
						return belegung ? true : false;
					};
			return false;
		})
	const fachkombi_verboten_q21: ComputedRef<boolean> =
		computed(()=> {
			if (fachkombi_verboten.value.size() > 0 && !bewertet.value[4])
				for (const kombi of fachkombi_verboten.value)
					if (kombi.gueltigInHalbjahr[5]) {
						const fach = faechermanager?.get(kombi.fachID1 || 0)
						if (!fach) return false;
						const belegung = daten.value.getWahlen(fach)[4]
						return belegung ? true : false;
					};
			return false;
		})
	const fachkombi_verboten_q22: ComputedRef<boolean> =
		computed(()=> {
			if (fachkombi_verboten.value.size() > 0 && !bewertet.value[5])
				for (const kombi of fachkombi_verboten.value)
					if (kombi.gueltigInHalbjahr[6]) {
						const fach = faechermanager?.get(kombi.fachID1 || 0)
						if (!fach) return false;
						const belegung = daten.value.getWahlen(fach)[5]
						return belegung ? true : false;
					};
			return false;
		})

	function ef1_set(): void { daten.value.setEF1Wahl(fach); }
	function ef2_set(): void { daten.value.setEF2Wahl(fach); }
	function q11_set(): void { daten.value.setQ11Wahl(fach); }
	function q12_set(): void { daten.value.setQ12Wahl(fach); }
	function q21_set(): void { daten.value.setQ21Wahl(fach); }
	function q22_set(): void { daten.value.setQ22Wahl(fach); }
	function abi_set(): void { daten.value.setAbiturWahl(fach); }
</script>
