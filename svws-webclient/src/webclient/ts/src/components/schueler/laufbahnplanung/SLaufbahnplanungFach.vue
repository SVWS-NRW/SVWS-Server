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
		<s-laufbahnplanung-fach-halbjahr :abiturmanager="abiturmanager" :faechermanager="faechermanager" :jahrgangsdaten="jahrgangsdaten" :manueller-modus="manuellerModus"
			:fach="fach" :halbjahr="halbjahr" :wahl="wahlen[halbjahr.id]" :moeglich="istMoeglich(halbjahr)" :bewertet="bewertet[halbjahr.id]"
			:fachkombi-erforderlich="fachkombi_erforderlich" :fachkombi-verboten="fachkombi_verboten" @update:wahl="onUpdateWahl" />
	</template>
	<s-laufbahnplanung-fach-halbjahr :abiturmanager="abiturmanager" :faechermanager="faechermanager" :jahrgangsdaten="jahrgangsdaten" :manueller-modus="manuellerModus"
		:fach="fach" :wahl="abi_wahl" :moeglich="istMoeglich()" :bewertet="bewertet[GostHalbjahr.Q22.id]" @update:wahl="onUpdateWahl" />
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";

	import { AbiturdatenManager, AbiturFachbelegungHalbjahr, Fachgruppe, GostFach, GostFaecherManager, GostHalbjahr,
		GostJahrgangFachkombination, GostJahrgangsdaten, GostKursart, GostLaufbahnplanungFachkombinationTyp, GostSchriftlichkeit,
		GostSchuelerFachwahl, Jahrgaenge, List, Vector, ZulaessigesFach } from "@svws-nrw/svws-core-ts";
	import { DataSchuelerLaufbahnplanung } from "~/apps/schueler/DataSchuelerLaufbahnplanung";

	const props = defineProps<{
		abiturmanager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		jahrgangsdaten: GostJahrgangsdaten;
		fach: GostFach,
		fachkombinationen: List<GostJahrgangFachkombination>;
		manuellerModus: boolean;
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


	function onUpdateWahl(wahl: GostSchuelerFachwahl) {
		props.dataLaufbahn.setWahl(props.fach, wahl);
	}


	function getAbiGKMoeglich(): boolean {
		const fachgruppe = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getFachgruppe();
		if (fachgruppe === Fachgruppe.FG_ME || fachgruppe === Fachgruppe.FG_VX || fachgruppe === Fachgruppe.FG_PX)
			return false;
		return props.fach.istMoeglichAbiGK;
	}


	function getAbiLKMoeglich(): boolean {
		const fach = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel);
		const fachgruppe = fach.getFachgruppe();
		if (fachgruppe === Fachgruppe.FG_ME || fachgruppe === Fachgruppe.FG_VX || fachgruppe === Fachgruppe.FG_PX
			|| fach.getJahrgangAb() === Jahrgaenge.JG_EF || (props.fach.biliSprache === null && props.fach.biliSprache === "D"))
			return false;
		return props.fach.istMoeglichAbiLK;
	}


	function getAbiMoeglich(): boolean {
		const fachbelegung = props.abiturmanager.getFachbelegungByID(props.fach.id);
		if (!fachbelegung?.letzteKursart)
			return false;
		switch (GostKursart.fromKuerzel(fachbelegung.letzteKursart)) {
			case GostKursart.LK:
				return getAbiLKMoeglich();
			case GostKursart.GK: {
				if (!props.abiturmanager.pruefeBelegungMitKursart(fachbelegung, GostKursart.GK, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
					return false;
				if (!props.abiturmanager.pruefeBelegungMitSchriftlichkeit(fachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21))
					return false;
				return getAbiGKMoeglich();
			}
		}
		return false;
	}


	function istMoeglich(halbjahr?: GostHalbjahr) {
		if (halbjahr === undefined) {
			return getAbiMoeglich();
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
