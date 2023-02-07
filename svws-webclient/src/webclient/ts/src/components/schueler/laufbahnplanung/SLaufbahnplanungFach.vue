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
			:fach="fach" :halbjahr="halbjahr" :wahl="wahlen[halbjahr.id]" :moeglich="istMoeglich(halbjahr)" :bewertet="istBewertet(halbjahr)"
			:fachkombi-erforderlich="fachkombi_erforderlich" :fachkombi-verboten="fachkombi_verboten" @update:wahl="onUpdateWahl" />
	</template>
	<s-laufbahnplanung-fach-halbjahr :abiturmanager="abiturmanager" :faechermanager="faechermanager" :jahrgangsdaten="jahrgangsdaten" :manueller-modus="manuellerModus"
		:fach="fach" :wahl="abi_wahl" :moeglich="istMoeglich()" :bewertet="istBewertet(GostHalbjahr.Q22)" @update:wahl="onUpdateWahl" />
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { AbiturdatenManager, AbiturFachbelegung, AbiturFachbelegungHalbjahr, Fachgruppe, GostFach, GostFaecherManager, GostHalbjahr,
		GostJahrgangFachkombination, GostJahrgangsdaten, GostKursart, GostLaufbahnplanungFachkombinationTyp, GostSchriftlichkeit,
		GostSchuelerFachwahl, Jahrgaenge, List, Sprachbelegung, SprachendatenUtils, Vector, ZulaessigesFach } from "@svws-nrw/svws-core-ts";

	const props = defineProps<{
		abiturmanager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		jahrgangsdaten: GostJahrgangsdaten;
		fach: GostFach;
		fachkombinationen: List<GostJahrgangFachkombination>;
		manuellerModus: boolean;
	}>();

	const emit = defineEmits<{
		(e: 'update:wahl', fachID: number, wahl: GostSchuelerFachwahl): void,
	}>();


	const bgColor: ComputedRef<string> = computed(() => ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getHMTLFarbeRGB());

	const bgColorIfLanguage: ComputedRef<string> = computed(() => ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).daten.istFremdsprache ? bgColor.value : "gray");

	const fachbelegung: ComputedRef<AbiturFachbelegung | null> = computed(() => props.abiturmanager.getFachbelegungByID(props.fach.id));

	function sprachbelegung(): Sprachbelegung | null {
		const sprach_kuerzel = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).daten.kuerzel;
		for (const sprache of props.abiturmanager.getSprachendaten().belegungen) {
			if (sprache.sprache === sprach_kuerzel)
				return sprache;
		}
		return null;
	}

	// Prüft, ob eine Sprache bisher schon unterrichtet wurde oder neu einsetzend ist
	function getFallsSpracheMoeglich(): boolean {
		const ist_fortfuehrbar = SprachendatenUtils.istFortfuehrbareSpracheInGOSt(
			props.abiturmanager.getSprachendaten(), ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).daten.kuerzel
		);
		sprachbelegung(); // TODO warum muss diese Zeile hier rein? Sonst Fehler mit Sprachenfolge in Laufbahnplanung  <--- ENTFERNEN ?!
		return ((ist_fortfuehrbar && !props.fach.istFremdSpracheNeuEinsetzend) || (!ist_fortfuehrbar && props.fach.istFremdSpracheNeuEinsetzend));
	}

	const sprachenfolgeNr: ComputedRef<number> = computed(() =>
		getFallsSpracheMoeglich() ? sprachbelegung()?.reihenfolge ?? 0 : 0
	);

	const sprachenfolgeJahrgang: ComputedRef<string> = computed(() =>
		getFallsSpracheMoeglich() ? (sprachbelegung()?.belegungVonJahrgang ?? "") : ""
	);

	const unbelegbarSprache: ComputedRef<boolean> = computed(() => getFallsSpracheMoeglich());

	function istBewertet(halbjahr: GostHalbjahr): boolean {
		return props.abiturmanager.istBewertet(halbjahr);
	}

	const fachkombis: ComputedRef<List<GostJahrgangFachkombination>> = computed(() => {
		const result = new Vector<GostJahrgangFachkombination>();
		for (const kombi of props.fachkombinationen)
			if (kombi.fachID2 === props.fach.id && kombi.abiturjahr === props.jahrgangsdaten.abiturjahr)
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


	function onUpdateWahl(wahl: GostSchuelerFachwahl, fachID?: number) {
		emit('update:wahl', fachID || props.fach.id, wahl);
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


	/**
	 * Prüft, ob ein Fach bereits belegt ist durch ein gleichnamiges Fach, z.B. bei Bili-Fächern
	 *
	 * @param {GostHalbjahr} hj Das GostHalbjahr
	 *
	 * @returns {boolean} ob doppel belegt wurde, z.B. bei Bili-Fächern
	 */
	function checkDoppelbelegung(hj: GostHalbjahr): boolean {
		// TODO Prüfe, ob der AbiturdatenManager nicht schon eine solche Methode hat - wenn nicht, dann ergänze eine
		const fach = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel);
		if (fach.getFachgruppe() === Fachgruppe.FG_VX)
			return false;
		const fachbelegungen = props.abiturmanager.getFachbelegungByFachkuerzel(props.fach.kuerzel);
		if (fachbelegungen !== undefined) {
			for (const fachbelegung of fachbelegungen) {
				if (props.abiturmanager.pruefeBelegung(fachbelegung, hj)) {
					if (fachbelegung.fachID !== props.fach.id)
						return true;
				}
			}
		}
		return false;
	}


	function istMoeglich(halbjahr?: GostHalbjahr) {
		if (props.fach.istFremdsprache && !getFallsSpracheMoeglich())
			return false;
		if (halbjahr === undefined)
			return getAbiMoeglich();
		if (checkDoppelbelegung(halbjahr))
			return false;
		if (halbjahr === GostHalbjahr.EF1) {
			const fach = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel);
			if (fach.getFachgruppe() === Fachgruppe.FG_ME || fach.getFachgruppe() === Fachgruppe.FG_PX)
				return false;
			return props.fach.istMoeglichEF1;
		}
		if (halbjahr === GostHalbjahr.EF2) {
			const fach = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel);
			if (fach.getFachgruppe() === Fachgruppe.FG_ME || fach.getFachgruppe() === Fachgruppe.FG_PX)
				return false;
			return props.fach.istMoeglichEF2;
		}
		if (halbjahr === GostHalbjahr.Q11)
			return props.fach.istMoeglichQ11;
		if (halbjahr === GostHalbjahr.Q12)
			return props.fach.istMoeglichQ12;
		if (halbjahr === GostHalbjahr.Q21)
			return props.fach.istMoeglichQ21;
		if (halbjahr === GostHalbjahr.Q22)
			return props.fach.istMoeglichQ22;
		return false;
	}


	const abi_wahl: ComputedRef<string> = computed(() => ( fachbelegung.value?.abiturFach?.toString() || ""));

	const wahlen: ComputedRef<string[]> = computed(() => {
		if (fachbelegung.value === null)
			return ["", "", "", "", "", ""];
		return fachbelegung.value.belegungen.map((b: AbiturFachbelegungHalbjahr | null) => {
			b = b ? b : new AbiturFachbelegungHalbjahr();
			if (b.halbjahrKuerzel === undefined)
				return "";
			const kursart = GostKursart.fromKuerzel(b.kursartKuerzel);
			if (!kursart)
				return b.kursartKuerzel;
			switch (kursart) {
				case GostKursart.ZK:
				case GostKursart.LK:
					return kursart.kuerzel;
			}
			return b.schriftlich ? "S" : "M";
		});
	});

</script>
