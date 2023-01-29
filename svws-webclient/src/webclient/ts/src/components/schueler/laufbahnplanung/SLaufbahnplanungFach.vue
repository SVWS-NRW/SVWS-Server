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
	<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
		<s-laufbahnplanung-fach-halbjahr :abiturmanager="abiturmanager" :faechermanager="faechermanager"
			:fach="fach" :halbjahr="halbjahr" :wahl="wahlen[halbjahr.id]" :moeglich="istMoeglich(halbjahr)" :bewertet="bewertet[halbjahr.id]"
			:fachkombi-erforderlich="fachkombi_erforderlich" :fachkombi-verboten="fachkombi_verboten" @click="onClick(halbjahr)" />
	</template>
	<s-laufbahnplanung-fach-halbjahr :abiturmanager="abiturmanager" :faechermanager="faechermanager"
		:fach="fach" :wahl="abi_wahl" :moeglich="istMoeglich()" :bewertet="bewertet[GostHalbjahr.Q22.id]" @click="onClick()" />
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";

	import { AbiturdatenManager, AbiturFachbelegungHalbjahr, GostFach, GostFaecherManager, GostHalbjahr, GostJahrgangFachkombination, GostKursart, GostLaufbahnplanungFachkombinationTyp, List, Vector } from "@svws-nrw/svws-core-ts";
	import { DataSchuelerLaufbahnplanung } from "~/apps/schueler/DataSchuelerLaufbahnplanung";

	const props = defineProps<{
		abiturmanager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		fach: GostFach,
		fachkombinationen: List<GostJahrgangFachkombination>;
		dataLaufbahn: DataSchuelerLaufbahnplanung
	}>();

	const bewertet: ComputedRef<Array<boolean>> = computed(() => props.dataLaufbahn.daten?.bewertetesHalbjahr || []);

	const unbelegbarSprache: ComputedRef<boolean> = computed(() => props.dataLaufbahn.getFallsSpracheMoeglich(props.fach));

	const bgColor: ComputedRef<string> = computed(() => props.dataLaufbahn.getBgColor(props.fach));

	const bgColorIfLanguage: ComputedRef<string> = computed(() => props.dataLaufbahn.getBgColorIfLanguage(props.fach));

	const sprachenfolgeNr: ComputedRef<number> = computed(() => props.dataLaufbahn.sprachenfolgeNr(props.fach));
	const sprachenfolgeJahrgang: ComputedRef<string> = computed(() => props.dataLaufbahn.sprachenfolgeJahrgang(props.fach));

	const abi_wahl: ComputedRef<string> = computed(() => ( props.dataLaufbahn.gostFachbelegungen[ props.fach.id ]?.abiturFach?.toString() || ""));

	const fachkombis: ComputedRef<List<GostJahrgangFachkombination>> = computed(() => {
		const result = new Vector<GostJahrgangFachkombination>();
		for (const kombi of props.fachkombinationen)
			if (kombi.fachID2 === props.fach.id && kombi.abiturjahr === props.dataLaufbahn.abiturjahr)
				result.add(kombi)
		return result;
	});

	const fachkombi_erforderlich: ComputedRef<List<GostJahrgangFachkombination>> = computed(()=> {
		const result = new Vector<GostJahrgangFachkombination>()
		for (const kombi of fachkombis.value)
			if (GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH.getValue() === kombi.typ)
				result.add(kombi);
		return result;
	})

	const fachkombi_verboten: ComputedRef<List<GostJahrgangFachkombination>> = computed(()=> {
		const result = new Vector<GostJahrgangFachkombination>()
		for (const kombi of fachkombis.value)
			if (GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue() === kombi.typ)
				result.add(kombi);
		return result;
	})

	function onClick(halbjahr?: GostHalbjahr) {
		if (halbjahr === undefined) {
			props.dataLaufbahn.setAbiturWahl(props.fach);
		} else if (halbjahr === GostHalbjahr.EF1) {
			props.dataLaufbahn.setEF1Wahl(props.fach);
		} else if (halbjahr === GostHalbjahr.EF2) {
			props.dataLaufbahn.setEF2Wahl(props.fach);
		} else if (halbjahr === GostHalbjahr.Q11) {
			props.dataLaufbahn.setQ11Wahl(props.fach);
		} else if (halbjahr === GostHalbjahr.Q12) {
			props.dataLaufbahn.setQ12Wahl(props.fach);
		} else if (halbjahr === GostHalbjahr.Q21) {
			props.dataLaufbahn.setQ21Wahl(props.fach);
		} else if (halbjahr === GostHalbjahr.Q22) {
			props.dataLaufbahn.setQ22Wahl(props.fach);
		}
	}

	function istMoeglich(halbjahr?: GostHalbjahr) {
		if (halbjahr === undefined) {
			return props.dataLaufbahn.getAbiMoeglich(props.fach);
		} else if (halbjahr === GostHalbjahr.EF1) {
			return props.dataLaufbahn.getEF1Moeglich(props.fach);
		} else if (halbjahr === GostHalbjahr.EF2) {
			return props.dataLaufbahn.getEF2Moeglich(props.fach);
		} else if (halbjahr === GostHalbjahr.Q11) {
			return props.dataLaufbahn.getQ11Moeglich(props.fach);
		} else if (halbjahr === GostHalbjahr.Q12) {
			return props.dataLaufbahn.getQ12Moeglich(props.fach);
		} else if (halbjahr === GostHalbjahr.Q21) {
			return props.dataLaufbahn.getQ21Moeglich(props.fach);
		} else if (halbjahr === GostHalbjahr.Q22) {
			return props.dataLaufbahn.getQ22Moeglich(props.fach);
		}
		return false;
	}

	const wahlen: ComputedRef<string[]> = computed(() => {
		const belegung = props.dataLaufbahn.gostFachbelegungen[props.fach.id];
		if (belegung === undefined)
			return ["", "", "", "", "", ""];
		return belegung.belegungen.map(
			(b: AbiturFachbelegungHalbjahr | null) => {
				b = b ? b : new AbiturFachbelegungHalbjahr();
				if (!b.halbjahrKuerzel)
					return "";
				const kursart = GostKursart.fromKuerzel(b.kursartKuerzel);
				if (!kursart)
					return b.kursartKuerzel.toString() || "";
				switch (kursart) {
					case GostKursart.ZK:
					case GostKursart.LK:
						return kursart.kuerzel;
				}
				return b.schriftlich ? "S" : "M";
			}
		)
	});

</script>
