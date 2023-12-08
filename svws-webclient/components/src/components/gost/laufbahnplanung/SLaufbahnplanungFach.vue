<template>
	<div role="row" class="svws-ui-tr" :style="{ '--background-color': bgColor }">
		<div role="cell" class="svws-ui-td select-text">
			<div class="whitespace-nowrap min-w-fit">
				{{ fach.kuerzelAnzeige }}
			</div>
		</div>
		<div role="cell" class="svws-ui-td select-all" :title="fach.bezeichnung || ''">
			<div class="break-all line-clamp-1 leading-tight -my-0.5">
				{{ fach.bezeichnung || '' }}
			</div>
		</div>
		<div role="cell" class="svws-ui-td svws-align-center svws-divider">
			{{ fach.wochenstundenQualifikationsphase }}
		</div>
		<div role="cell" class="svws-ui-td svws-align-center font-medium" :class="{ 'svws-disabled': !istFremdsprache }">
			<template v-if="istFremdsprache">
				<span v-if="ignoriereSprachenfolge" class="text-black/25"> ? </span>
				<span v-else-if="sprachenfolgeNr === 0 && !sprachenfolgeJahrgang" class="text-black/25"> — </span>
				<span v-else> {{ sprachenfolgeNr }} </span>
			</template>
		</div>
		<div role="cell" class="svws-ui-td svws-align-center font-medium svws-divider" :class="{ 'svws-disabled': !istFremdsprache}">
			<template v-if="istFremdsprache">
				<span v-if="ignoriereSprachenfolge" class="text-black/25"> ? </span>
				<span v-else-if="sprachenfolgeNr === 0 && !sprachenfolgeJahrgang" class="text-black/25"> — </span>
				<span v-else> {{ sprachenfolgeJahrgang }} </span>
			</template>
		</div>
		<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
			<div role="cell" class="svws-ui-td svws-align-center svws-divider select-none font-medium" :class="{
				'cursor-pointer': istMoeglich[halbjahr.id] && !istBewertet(halbjahr), '': istMoeglich[halbjahr.id],
				'cursor-not-allowed': !istMoeglich[halbjahr.id] || istBewertet(halbjahr) || istFachkombiVerboten[halbjahr.id],
				'svws-disabled': !istMoeglich[halbjahr.id],
				'svws-disabled-soft': istBewertet(halbjahr) && istMoeglich[halbjahr.id],
			}" @click.stop="stepper(halbjahr)" :title="istBewertet(halbjahr) ? 'Bewertet, keine Änderungen mehr möglich' : (!istMoeglich[halbjahr.id] ? 'Wahl nicht möglich' : '')">
				<template v-if="istFachkombiErforderlich[halbjahr.id] || istFachkombiVerboten[halbjahr.id] || !zkMoeglich(halbjahr)">
					<div class="inline-flex items-center relative w-full">
						<span class="w-full text-center">{{ wahlen[halbjahr.id] }}&#8203;</span>
						<span class="absolute -right-1">
							<svws-ui-tooltip :color="istBewertet(halbjahr) ? 'light' : 'danger'" position="bottom">
								<i-ri-error-warning-line :class="istBewertet(halbjahr) ? 'text-black/50' : 'text-error'" />
								<template #content v-if="istFachkombiErforderlich[halbjahr.id]">
									Fachkombination erforderlich
								</template>
								<template #content v-else-if="istFachkombiVerboten[halbjahr.id]">
									Fachkombination ist nicht zulässig
								</template>
								<template #content v-else>
									Ein Zusatzkurs {{ fach.kuerzel }} wird in diesem Halbjahr nicht angeboten
								</template>
							</svws-ui-tooltip>
						</span>
					</div>
				</template>
				<div class="inline-flex items-center gap-1 relative w-full" v-else-if="!istMoeglich[halbjahr.id] && wahlen[halbjahr.id]">
					<span class="text-center w-full">{{ wahlen[halbjahr.id] }}</span>
					<span class="absolute -right-1">
						<svws-ui-tooltip :color="istBewertet(halbjahr) ? 'light' : 'danger'">
							<svws-ui-button type="icon" size="small" :disabled="istBewertet(halbjahr)">
								<i-ri-close-line @click="deleteFachwahl(halbjahr)" />
							</svws-ui-button>
							<template #content>
								<template v-if="istBewertet(halbjahr)">
									Kurs nicht wählbar
								</template>
								<template v-else>
									Löschen (Kurs nicht wählbar)
								</template>
							</template>
						</svws-ui-tooltip>
					</span>
				</div>
				<span v-else>
					<template v-if="wahlen[halbjahr.id] && wahlen[halbjahr.id] !== '6'">
						{{ wahlen[halbjahr.id] }}
					</template>
					<template v-else-if="wahlen[halbjahr.id] && wahlen[halbjahr.id] === '6'">
						<svws-ui-tooltip color="danger" position="bottom">
							<div class="inline-flex items-center">
								<span>0</span>
								<i-ri-error-warning-line class="text-error ml-0.5" />
							</div>
							<template #content>
								Dieser Kurs gilt aufgrund von 0 Punkten als nicht belegt.
							</template>
						</svws-ui-tooltip>
					</template>
				</span>
			</div>
		</template>
		<div role="cell" class="svws-ui-td svws-align-center select-none font-medium" :class="{
			'cursor-pointer': istMoeglichAbi && !istBewertet(GostHalbjahr.Q22), '': istMoeglichAbi,
			'cursor-not-allowed': !istMoeglichAbi,
			'svws-disabled': !istMoeglichAbi,
			'svws-disabled-soft': istBewertet(GostHalbjahr.Q22) && istMoeglichAbi,
		}" @click.stop="stepperAbi()">
			<template v-if="abi_wahl"> {{ abi_wahl }} </template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import type { AbiturFachbelegung, GostFach, GostJahrgangFachkombination, GostJahrgangsdaten, GostSchuelerFachwahl, Sprachbelegung} from "@core";
	import { AbiturdatenManager, AbiturFachbelegungHalbjahr, Fachgruppe, GostAbiturFach, GostFachbereich, GostFachUtils, GostHalbjahr,
		GostKursart, SprachendatenUtils, ZulaessigesFach } from "@core";
	import { computed } from "vue";

	const props = withDefaults(defineProps<{
		abiturdatenManager: () => AbiturdatenManager;
		gostJahrgangsdaten: GostJahrgangsdaten;
		fach: GostFach;
		modus?: 'normal' | 'manuell' | 'hochschreiben';
		ignoriereSprachenfolge? : boolean;
	}>(), {
		modus: 'normal',
		ignoriereSprachenfolge: false,
	});

	const emit = defineEmits<{
		(e: 'update:wahl', fachID: number, wahl: GostSchuelerFachwahl): void,
	}>();


	const istFremdsprache: ComputedRef<boolean> = computed(() => ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).daten.istFremdsprache);

	const bgColor: ComputedRef<string> = computed(() => ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getHMTLFarbeRGB());

	const fachbelegung: ComputedRef<AbiturFachbelegung | null> = computed(() => props.abiturdatenManager().getFachbelegungByID(props.fach.id));

	const sprachbelegung: ComputedRef<Sprachbelegung | null> = computed(()=> {
		const sprach_kuerzel = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).daten.kuerzel;
		for (const sprache of props.abiturdatenManager().getSprachendaten().belegungen) {
			if (sprache.sprache === sprach_kuerzel)
				return sprache;
		}
		return null;
	})

	// Prüft, ob eine Sprache bisher schon unterrichtet wurde oder neu einsetzend ist
	const getFallsSpracheMoeglich: ComputedRef<boolean> = computed(()=> {
		const ist_fortfuehrbar = SprachendatenUtils.istFortfuehrbareSpracheInGOSt(
			props.abiturdatenManager().getSprachendaten(), ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).daten.kuerzel
		);
		sprachbelegung.value; // TODO warum muss diese Zeile hier rein? Sonst Fehler mit Sprachenfolge in Laufbahnplanung  <--- ENTFERNEN ?!
		return ((ist_fortfuehrbar && !props.fach.istFremdSpracheNeuEinsetzend) || (!ist_fortfuehrbar && props.fach.istFremdSpracheNeuEinsetzend));
	})

	const sprachenfolgeNr: ComputedRef<number> = computed(() =>
		getFallsSpracheMoeglich.value ? sprachbelegung.value?.reihenfolge ?? 0 : 0
	);

	const sprachenfolgeJahrgang: ComputedRef<string> = computed(() =>
		getFallsSpracheMoeglich.value ? (sprachbelegung.value?.belegungVonJahrgang ?? "") : ""
	);

	function istBewertet(halbjahr: GostHalbjahr): boolean {
		return props.abiturdatenManager().istBewertet(halbjahr);
	}

	function onUpdateWahl(wahl: GostSchuelerFachwahl, fachID?: number) {
		emit('update:wahl', fachID || props.fach.id, wahl);
	}


	const istMoeglichAbi: ComputedRef<boolean> = computed(() => props.abiturdatenManager().getMoeglicheKursartAlsAbiturfach(props.fach.id) !== null);


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
		const fachbelegungen = props.abiturdatenManager().getFachbelegungByFachkuerzel(props.fach.kuerzel);
		if (fachbelegungen !== undefined) {
			for (const fachbelegung of fachbelegungen) {
				if (props.abiturdatenManager().pruefeBelegung(fachbelegung, hj)) {
					if (fachbelegung.fachID !== props.fach.id)
						return true;
				}
			}
		}
		return false;
	}


	const istMoeglich: ComputedRef<boolean[]> = computed(() => {
		if (props.fach.istFremdsprache && ((!props.ignoriereSprachenfolge) && !getFallsSpracheMoeglich.value))
			return [ false, false, false, false, false, false ];
		const fach = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel);
		const result = [ false, false, false, false, false, false ];
		return [
			(props.fach.istMoeglichEF1 && !checkDoppelbelegung(GostHalbjahr.EF1) && (fach.getFachgruppe() !== Fachgruppe.FG_ME) && (fach.getFachgruppe() !== Fachgruppe.FG_PX)),
			(props.fach.istMoeglichEF2 && !checkDoppelbelegung(GostHalbjahr.EF2) && (fach.getFachgruppe() !== Fachgruppe.FG_ME) && (fach.getFachgruppe() !== Fachgruppe.FG_PX)),
			(props.fach.istMoeglichQ11 && !checkDoppelbelegung(GostHalbjahr.Q11)),
			(props.fach.istMoeglichQ12 && !checkDoppelbelegung(GostHalbjahr.Q12)),
			(props.fach.istMoeglichQ21 && !checkDoppelbelegung(GostHalbjahr.Q21)),
			(props.fach.istMoeglichQ22 && !checkDoppelbelegung(GostHalbjahr.Q22)),
		];
	});


	const abi_wahl: ComputedRef<string> = computed(() =>
		fachbelegung.value?.abiturFach?.toString() || "");

	const wahlen: ComputedRef<string[]> = computed(() => {
		if (fachbelegung.value === null)
			return ["", "", "", "", "", ""];
		return fachbelegung.value.belegungen.map((b: AbiturFachbelegungHalbjahr | null) => {
			b = b ? b : new AbiturFachbelegungHalbjahr();
			if (b.halbjahrKuerzel === undefined)
				return "";
			if (AbiturdatenManager.istNullPunkteBelegungInQPhase(b))
				return "6";
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


	function pruefeKombinationErforderlich(fachid: number, kombi: GostJahrgangFachkombination, hj: GostHalbjahr) {
		if (fachid !== kombi.fachID2)
			return false;
		if (!kombi.gueltigInHalbjahr[hj.id])
			return false;
		const fach1 = props.abiturdatenManager().faecher().get(kombi.fachID1);
		if (fach1 === null)
			return false;
		const f1 = props.abiturdatenManager().getFachbelegungByID(fach1.id)
		const f2 = props.abiturdatenManager().getFachbelegungByID(fachid)
		const kursart1 = GostKursart.fromKuerzel(kombi.kursart1);
		const kursart2 = GostKursart.fromKuerzel(kombi.kursart2);
		const bel1 = kursart1
			? props.abiturdatenManager().pruefeBelegungMitKursart(f1, kursart1, hj)
			: props.abiturdatenManager().pruefeBelegung(f1, hj);
		const bel2 = kursart2
			? props.abiturdatenManager().pruefeBelegungMitKursart(f2, kursart2, hj)
			: props.abiturdatenManager().pruefeBelegung(f2, hj);
		if (bel2)
			return false;
		return bel1 !== bel2;
	}

	function istFachkombiErforderlichHalbjahr(hj: GostHalbjahr) : boolean {
		for (const kombi of props.abiturdatenManager().faecher().getFachkombinationenErforderlich())
			if (pruefeKombinationErforderlich(props.fach.id, kombi, hj))
				return true;
		return false;
	}

	const istFachkombiErforderlich: ComputedRef<boolean[]> = computed(() => {
		const result = [];
		for (const halbjahr of GostHalbjahr.values())
			result.push(istFachkombiErforderlichHalbjahr(halbjahr));
		return result;
	});

	function pruefeKombinationVerboten(fachid: number, kombi: GostJahrgangFachkombination, hj: GostHalbjahr) {
		if (fachid !== kombi.fachID2)
			return false;
		if (!kombi.gueltigInHalbjahr[hj.id])
			return false;
		const fach1 = props.abiturdatenManager().faecher().get(kombi.fachID1)
		if (fach1 === null)
			return false;
		const f1 = props.abiturdatenManager().getFachbelegungByID(fach1.id)
		const f2 = props.abiturdatenManager().getFachbelegungByID(fachid)
		const kursart1 = GostKursart.fromKuerzel(kombi.kursart1);
		const kursart2 = GostKursart.fromKuerzel(kombi.kursart2);
		const bel1 = kursart1
			? props.abiturdatenManager().pruefeBelegungMitKursart(f1, kursart1, hj)
			: props.abiturdatenManager().pruefeBelegung(f1, hj);
		const bel2 = kursart2
			? props.abiturdatenManager().pruefeBelegungMitKursart(f2, kursart2, hj)
			: props.abiturdatenManager().pruefeBelegung(f2, hj);
		return bel1 && bel2;
	}

	function istFachkombiVerbotenHalbjahr(hj: GostHalbjahr) : boolean {
		const fachkombis = props.abiturdatenManager().faecher().getFachkombinationenVerboten();
		for (const kombi of fachkombis)
			if (pruefeKombinationVerboten(props.fach.id, kombi, hj))
				return true;
		return false;
	}

	const istFachkombiVerboten: ComputedRef<boolean[]> = computed(() => {
		const result = [];
		for (const halbjahr of GostHalbjahr.values())
			result.push(istFachkombiVerbotenHalbjahr(halbjahr));
		return result;
	});

	const ist_VTF: ComputedRef<boolean> = computed(() =>
		ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getFachgruppe() === Fachgruppe.FG_VX);

	const ist_PJK: ComputedRef<boolean> = computed(()=>
		ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getFachgruppe() === Fachgruppe.FG_PX);

	const getAndereFachwahl: ComputedRef<GostSchuelerFachwahl | undefined> = computed(()=> {
		const fach = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel);
		if (fach.getFachgruppe() === Fachgruppe.FG_VX)
			return;
		const fachbelegungen = props.abiturdatenManager().getFachbelegungByFachkuerzel(props.fach.kuerzel);
		if (fachbelegungen !== undefined)
			for (const fachbelegung of fachbelegungen)
				if (fachbelegung.fachID !== props.fach.id)
					return props.abiturdatenManager().getSchuelerFachwahl(fachbelegung.fachID)
		return
	})

	function stepperAbi() {
		if (props.modus === 'manuell') {
			stepper_manuellAbi();
			return;
		}
		if (!istMoeglichAbi.value)
			return;
		const wahl = props.abiturdatenManager().getSchuelerFachwahl(props.fach.id);
		setAbiturWahl(wahl);
		onUpdateWahl(wahl);
	}

	function stepper(halbjahr: GostHalbjahr) {
		if (props.modus === 'manuell') {
			stepper_manuell(halbjahr);
			return;
		}
		if ((!istMoeglich.value[halbjahr.id]) || istBewertet(halbjahr == undefined ? GostHalbjahr.Q22 : halbjahr))
			return;
		const wahl = props.abiturdatenManager().getSchuelerFachwahl(props.fach.id);
		if (halbjahr === undefined)
			setAbiturWahl(wahl);
		else if (halbjahr === GostHalbjahr.EF1)
			props.modus === 'normal' ? setEF1Wahl(wahl) : setEF1WahlHochschreiben(wahl);
		else if (halbjahr === GostHalbjahr.EF2)
			props.modus === 'normal' ? setEF2Wahl(wahl) : setEF2WahlHochschreiben(wahl);
		else if (halbjahr === GostHalbjahr.Q11)
			setQ11Wahl(wahl);
		else if (halbjahr === GostHalbjahr.Q12)
			setQ12Wahl(wahl);
		else if (halbjahr === GostHalbjahr.Q21)
			setQ21Wahl(wahl);
		else if (halbjahr === GostHalbjahr.Q22)
			setQ22Wahl(wahl);
		onUpdateWahl(wahl);
	}

	function deleteFachwahl(halbjahr: GostHalbjahr | undefined) {
		if (halbjahr === undefined || wahlen.value[halbjahr.id] === null || istMoeglich.value[halbjahr.id])
			return;
		const wahl = props.abiturdatenManager().getSchuelerFachwahl(props.fach.id);
		wahl.halbjahre[halbjahr.id] = null;
		onUpdateWahl(wahl, props.fach.id);
	}

	function stepper_manuellAbi() : void {
		if (istBewertet(GostHalbjahr.Q22))
			return;
		const wahl = props.abiturdatenManager().getSchuelerFachwahl(props.fach.id);
		if (!wahl.halbjahre[GostHalbjahr.Q22.id])
			return
		switch (wahl.abiturFach) {
			case null:
				wahl.abiturFach = wahl.halbjahre[GostHalbjahr.Q22.id] === "LK" ? 1 : 3;
				break;
			case 1:
				wahl.abiturFach = wahl.halbjahre[GostHalbjahr.Q22.id] === "LK" ? 2 : 3;
				break;
			case 2:
				wahl.abiturFach = wahl.halbjahre[GostHalbjahr.Q22.id] === "LK" ? null : 3;
				break;
			case 3:
				wahl.abiturFach = wahl.halbjahre[GostHalbjahr.Q22.id] === "LK" ? null : 4;
				break;
			case 4:
				wahl.abiturFach = null;
				break;
			default:
				wahl.abiturFach = null;
				break;
		}
		onUpdateWahl(wahl);
	}


	function stepper_manuell(halbjahr: GostHalbjahr) : void {
		if (istBewertet(halbjahr))
			return;
		const wahl = props.abiturdatenManager().getSchuelerFachwahl(props.fach.id);
		const hj = halbjahr.id;
		switch (wahl.halbjahre[hj]) {
			case "AT":
				wahl.halbjahre[hj] = null;
				break;
			case "ZK":
				wahl.halbjahre[hj] = null;
				break;
			case null:
				wahl.halbjahre[hj] = "M";
				break;
			case "M":
				wahl.halbjahre[hj] = "S";
				break;
			case "S":
				if ((hj <= 1) || (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(props.fach))) {
					if (GostFachbereich.SPORT.hat(props.fach))
						wahl.halbjahre[hj] = "AT";
					else
						wahl.halbjahre[hj] = null;
				} else { // in der Q-Phase als LK möglich, allerdings nicht im Fachbereich des literarisch-künstlerischen Bereichs
					wahl.halbjahre[hj] = "LK";
				}
				break;
			case "LK": {
				wahl.halbjahre[hj] = null
				if (GostFachbereich.SPORT.hat(props.fach))
					wahl.halbjahre[hj] = "AT";
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) || GostFachbereich.GESCHICHTE.hat(props.fach))
					wahl.halbjahre[hj] = "ZK";
				break;
			}
			default:
				wahl.halbjahre[hj] = null;
				break;
		}
		onUpdateWahl(wahl);
	}


	function setEF1Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.EF1.id]) {
			case null: wahl.halbjahre[GostHalbjahr.EF1.id] = ist_VTF.value || ist_PJK.value ? "M" : "S"; break;
			case "S":  wahl.halbjahre[GostHalbjahr.EF1.id] = "M"; break;
			case "M":  wahl.halbjahre[GostHalbjahr.EF1.id] = null; break;
		}
		// TODO AT für Sport !!!
	}


	function setEF2Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.EF2.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.EF2.id] = ist_VTF.value || ist_PJK.value ? "M" : "S";
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.EF2.id] = "M";
				break;
			case "M":
				wahl.halbjahre[GostHalbjahr.EF2.id] = null;
				if (GostFachbereich.SPORT.hat(props.fach)) wahl.halbjahre[GostHalbjahr.EF2.id] = "AT";
				break;
			case "AT":
				wahl.halbjahre[GostHalbjahr.EF2.id] = null;
		}
	}

	function identicalArrayIgnoreFirstAndSecond<T>(a1: Array<T>, a2: Array<T>) {
		let i = a1.length-2;
		while (i--)
			if (a1[i+2] !== a2[i])
				return false;
		return true;
	}

	function identicalArray<T>(a1: Array<T>, a2: Array<T>) {
		let i = a1.length;
		while (i--)
			if (a1[i] !== a2[i])
				return false;
		return true;
	}

	function setEF1WahlHochschreiben(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.EF1.id]) {
			case null: {
				if (wahl.abiturFach !== null)
					wahl.halbjahre[GostHalbjahr.EF1.id] = 'S';
				// Prüfe, ob die Folgehalbjahre auch leer sind, dann setze auch diese
				else if (identicalArray(wahl.halbjahre, [null, null, null, null, null, null]) && !(ist_VTF.value || ist_PJK.value))
					wahl.halbjahre = (ist_VTF.value || ist_PJK.value) ? ['M', 'M', 'M', 'M', 'M', 'M'] : ['S', 'S', 'S', 'S', 'S', 'M'];
				else
					wahl.halbjahre[GostHalbjahr.EF1.id] = ist_VTF.value || ist_PJK.value ? "M" : "S";
				break;
			}
			case "S":  {
				if (wahl.abiturFach !== null)
					wahl.halbjahre[GostHalbjahr.EF1.id] = 'M';
				// Prüfe, ob die Folgehalbjahre S,S,S,S,M sind und Abi-Fach nicht gesetzt (Spezialfälle berücksichtigen KU+MU+RE)
				else if (identicalArray(wahl.halbjahre, ['S', 'S', 'S', 'S', 'S', 'M']) && !(ist_VTF.value || ist_PJK.value))
					if (GostFachbereich.KUNST_MUSIK.hat(props.fach) || GostFachbereich.RELIGION.hat(props.fach))
						wahl.halbjahre = ['M', 'M', 'M', 'M', null, null];
					else
						wahl.halbjahre = ['M', 'M', 'M', 'M', 'M', 'M'];
				else
					wahl.halbjahre[GostHalbjahr.EF1.id] = "M";
				break;
			}
			case "M":  {
				if (wahl.abiturFach !== null)
					wahl.halbjahre[GostHalbjahr.EF1.id] = 'S';
				// Prüfe, ob die Folgehalbjahre M,M,M,M?,M? sind und passe diese an (Spezialfälle berücksichtigen KU+MU+RE)
				else if ((identicalArray(wahl.halbjahre, ['M', 'M', 'M', 'M', 'M', 'M']) || identicalArray(wahl.halbjahre, ['M', 'M', 'M', 'M', null, null])) && !(ist_VTF.value || ist_PJK.value))
					wahl.halbjahre = [null, null, null, null, null, null];
				else
					wahl.halbjahre[GostHalbjahr.EF1.id] = null;
				break;
			}
			// TODO AT für Sport !!!
		}
	}


	function setEF2WahlHochschreiben(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.EF2.id]) {
			case null: {
				if (wahl.abiturFach !== null)
					wahl.halbjahre[GostHalbjahr.EF2.id] = 'S';
				else if (identicalArrayIgnoreFirstAndSecond(wahl.halbjahre, [null, null, null, null]) && !(ist_VTF.value || ist_PJK.value))
					wahl.halbjahre = [wahl.halbjahre[0], 'S', 'S', 'S', 'S', 'M'];
				else
					wahl.halbjahre[GostHalbjahr.EF2.id] = ist_VTF.value || ist_PJK.value ? "M" : "S";
				break;
			}
			case "S": {
				if (wahl.abiturFach !== null)
					wahl.halbjahre[GostHalbjahr.EF2.id] = 'M';
				else if ((identicalArrayIgnoreFirstAndSecond(wahl.halbjahre, [null, null, null, null])
					|| identicalArrayIgnoreFirstAndSecond(wahl.halbjahre, ['S', 'S', 'S', 'M'])) && !(ist_VTF.value || ist_PJK.value))
					if (GostFachbereich.KUNST_MUSIK.hat(props.fach) || GostFachbereich.RELIGION.hat(props.fach))
						wahl.halbjahre = [wahl.halbjahre[0], 'M', 'M', 'M', null, null];
					else
						wahl.halbjahre = [wahl.halbjahre[0], 'M', 'M', 'M', 'M', 'M'];
				else
					wahl.halbjahre[GostHalbjahr.EF2.id] = "M";
				break;
			}
			case "M": {
				if (wahl.abiturFach !== null)
					wahl.halbjahre[GostHalbjahr.EF2.id] = 'S';
				else if ((identicalArrayIgnoreFirstAndSecond(wahl.halbjahre, [null, null, null, null])
					|| identicalArrayIgnoreFirstAndSecond(wahl.halbjahre, ['M', 'M', 'M', 'M'])
					|| identicalArrayIgnoreFirstAndSecond(wahl.halbjahre, ['M', 'M', null, null])) && !(ist_VTF.value || ist_PJK.value))
					wahl.halbjahre = [wahl.halbjahre[0], null, null, null, null, null];
				wahl.halbjahre[GostHalbjahr.EF2.id] = null;
				if (GostFachbereich.SPORT.hat(props.fach))
					wahl.halbjahre[GostHalbjahr.EF2.id] = "AT";
				break;
			}
			case "AT": {
				wahl.halbjahre[GostHalbjahr.EF2.id] = null;
			}
		}
	}


	function setQ11Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.Q11.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q11.id] = (GostFachbereich.DEUTSCH.hat(props.fach) || GostFachbereich.MATHEMATIK.hat(props.fach)) ? "S" : "M";
				break;
			case "M":
				wahl.halbjahre[GostHalbjahr.Q11.id] = ist_VTF.value || ist_PJK.value ? null : "S";
				break;
			case "S":
				//S->S ist richtig, weil DE und MA muss belegt sein, entweder S oder LK, anders geht es nicht.
				wahl.halbjahre[GostHalbjahr.Q11.id] = (props.fach.istMoeglichAbiLK) ? "LK" : (GostFachbereich.DEUTSCH.hat(props.fach) || GostFachbereich.MATHEMATIK.hat(props.fach)) ? "S" : null;
				break;
			case "LK":
				wahl.halbjahre[GostHalbjahr.Q11.id] = (GostFachbereich.DEUTSCH.hat(props.fach) || GostFachbereich.MATHEMATIK.hat(props.fach)) ? "S" : null;
				wahl.abiturFach = null;
				break;
		}
		// Sonderfall Sport - darf AT haben
		if (!wahl.halbjahre[GostHalbjahr.Q11.id] && GostFachbereich.SPORT.hat(props.fach))
			wahl.halbjahre[GostHalbjahr.Q11.id] = "AT";
		else if (wahl.halbjahre[GostHalbjahr.Q11.id] === "AT" && GostFachbereich.SPORT.hat(props.fach))
			wahl.halbjahre[GostHalbjahr.Q11.id] = null;
		// Q11 wählt bis Q22
		switch (wahl.halbjahre[GostHalbjahr.Q11.id]) {
			case null:
				if (!ist_VTF.value) {
					wahl.halbjahre[GostHalbjahr.Q12.id] = null;
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) && (props.gostJahrgangsdaten.hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW);
					if (beginn !== null) {
						const andereWahl = getAndereFachwahl.value;
						if (beginn === GostHalbjahr.Q11) {
							if (wahl.halbjahre[GostHalbjahr.EF2.id] === null && andereWahl?.halbjahre[GostHalbjahr.EF2.id] == undefined) {
								wahl.halbjahre[GostHalbjahr.Q11.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
							}
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(props.fach) && props.gostJahrgangsdaten.hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE);
					if (beginn !== null) {
						const andereWahl = getAndereFachwahl.value;
						if (beginn === GostHalbjahr.Q11) {
							if (wahl.halbjahre[GostHalbjahr.EF2.id] === null && andereWahl?.halbjahre[GostHalbjahr.EF2.id] == undefined) {
								wahl.halbjahre[GostHalbjahr.Q11.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
							}
						}
					}
					if ((beginn !== null) && (beginn === GostHalbjahr.Q11) && (wahl.halbjahre[GostHalbjahr.EF2.id] === null)) {
						wahl.halbjahre[GostHalbjahr.Q11.id] = "ZK";
						wahl.halbjahre[GostHalbjahr.Q12.id] = "ZK";
					}
				}
				break;
			case "M":
				if (props.fach.istMoeglichQ12 && !ist_VTF.value)
					wahl.halbjahre[GostHalbjahr.Q12.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
				if (!(ist_VTF.value || ist_PJK.value) && !GostFachbereich.KUNST_MUSIK.hat(props.fach) && !GostFachbereich.RELIGION.hat(props.fach)) {
					if (props.fach.istMoeglichQ21) wahl.halbjahre[GostHalbjahr.Q21.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
					if (props.fach.istMoeglichQ22) wahl.halbjahre[GostHalbjahr.Q22.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
				}
				break;
			case "S":
				if (props.fach.istMoeglichQ12) wahl.halbjahre[GostHalbjahr.Q12.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
				if (!(ist_VTF.value || ist_PJK.value)) {
					if (props.fach.istMoeglichQ21)
						wahl.halbjahre[GostHalbjahr.Q21.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
					// "S" kann nur für drittes Abifach gewählt werden, Vorauswahl daher "M"
					if (props.fach.istMoeglichQ22)
						wahl.halbjahre[GostHalbjahr.Q22.id] = "M";
				}
				break;
			case "ZK":
				wahl.halbjahre[GostHalbjahr.Q11.id] = null;
				wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				break;
			case "LK": {
				wahl.halbjahre[GostHalbjahr.Q12.id] = props.fach.istMoeglichQ12 ? wahl.halbjahre[GostHalbjahr.Q11.id] : null;
				wahl.halbjahre[GostHalbjahr.Q21.id] = props.fach.istMoeglichQ21 ? wahl.halbjahre[GostHalbjahr.Q11.id] : null;
				wahl.halbjahre[GostHalbjahr.Q22.id] = props.fach.istMoeglichQ22 ? wahl.halbjahre[GostHalbjahr.Q11.id] : null;
				// Bedingungen für LK1
				const alle_fachbelegungen = props.abiturdatenManager().getFachbelegungen();
				const lk1_belegt = props.abiturdatenManager().pruefeExistiertAbiFach(alle_fachbelegungen, GostAbiturFach.LK1);
				const lk2_belegt = props.abiturdatenManager().pruefeExistiertAbiFach(alle_fachbelegungen, GostAbiturFach.LK2);
				if (GostFachbereich.DEUTSCH.hat(props.fach) || GostFachbereich.MATHEMATIK.hat(props.fach)
					|| GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH.hat(props.fach)
					|| (GostFachbereich.FREMDSPRACHE.hat(props.fach) && !props.fach.istFremdSpracheNeuEinsetzend)) {
					wahl.abiturFach = !lk1_belegt ? 1 : lk2_belegt ? null : 2;
				} else {
					wahl.abiturFach = lk2_belegt ? null : 2;
				}
				break;
			}
		}
		if (wahl.halbjahre[GostHalbjahr.Q11.id] === null || wahl.halbjahre[GostHalbjahr.Q11.id] === "M")
			wahl.abiturFach = null;
	}


	function setQ12Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.Q12.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q12.id] = "M";
				if (ist_PJK.value && wahl.halbjahre[GostHalbjahr.Q11.id] === null && props.fach.istMoeglichQ21) {
					wahl.halbjahre[GostHalbjahr.Q21.id] = "M";
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) && (props.gostJahrgangsdaten.hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW);
					if (beginn !== null) {
						const andereWahl = getAndereFachwahl.value;
						if (beginn === GostHalbjahr.Q11) {
							if (wahl.halbjahre[GostHalbjahr.EF2.id] === null && andereWahl?.halbjahre[GostHalbjahr.EF2.id] == undefined) {
								wahl.halbjahre[GostHalbjahr.Q11.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
							}
						}
						if (beginn === GostHalbjahr.Q12) {
							if (wahl.halbjahre[GostHalbjahr.Q11.id] === null && andereWahl?.halbjahre[GostHalbjahr.Q11.id] == undefined) {
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
							}
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(props.fach) && props.gostJahrgangsdaten.hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE);
					if (beginn !== null) {
						const andereWahl = getAndereFachwahl.value;
						if (beginn === GostHalbjahr.Q11) {
							if (wahl.halbjahre[GostHalbjahr.EF2.id] === null && andereWahl?.halbjahre[GostHalbjahr.EF2.id] == undefined) {
								wahl.halbjahre[GostHalbjahr.Q11.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
							}
						}
						if (beginn === GostHalbjahr.Q12) {
							if (wahl.halbjahre[GostHalbjahr.Q11.id] === null && andereWahl?.halbjahre[GostHalbjahr.Q11.id] == undefined) {
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
							}
						}
					}
					if ((beginn !== null) && (((beginn === GostHalbjahr.Q11) && (wahl.halbjahre[GostHalbjahr.EF2.id] === null)) || ((beginn === GostHalbjahr.Q12) && (wahl.halbjahre[GostHalbjahr.Q11.id] === null)))) {
						if (beginn === GostHalbjahr.Q11)
							wahl.halbjahre[GostHalbjahr.Q11.id] = "ZK";
						wahl.halbjahre[GostHalbjahr.Q12.id] = "ZK";
						if (beginn === GostHalbjahr.Q12)
							wahl.halbjahre[GostHalbjahr.Q21.id] = "ZK";
					}
				}
				break;
			case "M":
				wahl.halbjahre[GostHalbjahr.Q12.id] = ist_VTF.value || ist_PJK.value ? null : "S";
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.Q12.id] = wahl.halbjahre[GostHalbjahr.Q11.id] === "LK" ? "LK" : null;
				break;
			case "ZK": {
				const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach))
					? GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW || "")
					: GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE || "");
				if ((beginn !== null) && (beginn === GostHalbjahr.Q11))
					wahl.halbjahre[GostHalbjahr.Q11.id] = null;
				wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				if ((beginn !== null) && (beginn === GostHalbjahr.Q12))
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				break;
				// TODO: Warum ist das so? Bis Q22. Was ist erlaubt: M, S, null?
			}
			case "LK":
				wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				wahl.abiturFach = null;
		}
		// Sonderfall Sport - darf AT haben
		if (!wahl.halbjahre[GostHalbjahr.Q12.id] && GostFachbereich.SPORT.hat(props.fach))
			wahl.halbjahre[GostHalbjahr.Q12.id] = "AT";
		else if (wahl.halbjahre[GostHalbjahr.Q12.id] === "AT" && GostFachbereich.SPORT.hat(props.fach))
			wahl.halbjahre[GostHalbjahr.Q12.id] = null;
		// Nachfolgende HJ ebenfalls setzen
		if (wahl.halbjahre[GostHalbjahr.Q12.id] === null && !ist_VTF.value) {
			wahl.halbjahre[GostHalbjahr.Q21.id] = null;
			wahl.halbjahre[GostHalbjahr.Q22.id] = null;
		}
		if (wahl.halbjahre[GostHalbjahr.Q12.id] === null || wahl.halbjahre[GostHalbjahr.Q12.id] === "M")
			wahl.abiturFach = null;
	}


	function setQ21Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.Q21.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q21.id] = "M";
				if (ist_PJK.value && wahl.halbjahre[GostHalbjahr.Q12.id] === null && props.fach.istMoeglichQ22) {
					wahl.halbjahre[GostHalbjahr.Q22.id] = "M";
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) && (props.gostJahrgangsdaten.hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW);
					if (beginn !== null) {
						const andereWahl = getAndereFachwahl.value;
						if (beginn === GostHalbjahr.Q12) {
							if (wahl.halbjahre[GostHalbjahr.Q11.id] === null && andereWahl?.halbjahre[GostHalbjahr.Q11.id] == undefined) {
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
							}
						}
						if (beginn === GostHalbjahr.Q21) {
							if (wahl.halbjahre[GostHalbjahr.Q12.id] === null && andereWahl?.halbjahre[GostHalbjahr.Q12.id] == undefined) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK'
							}
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(props.fach) && props.gostJahrgangsdaten.hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE);
					if (beginn !== null) {
						const andereWahl = getAndereFachwahl.value;
						if (beginn === GostHalbjahr.Q12) {
							if (wahl.halbjahre[GostHalbjahr.Q11.id] === null && andereWahl?.halbjahre[GostHalbjahr.Q11.id] == undefined) {
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
							}
						}
						if (beginn === GostHalbjahr.Q21) {
							if (wahl.halbjahre[GostHalbjahr.Q12.id] === null && andereWahl?.halbjahre[GostHalbjahr.Q12.id] == undefined) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK'
							}
						}
					}
				}
				break;
			case "M":
				wahl.halbjahre[GostHalbjahr.Q21.id] = ist_VTF.value || ist_PJK.value ? null : "S";
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.Q21.id] = wahl.halbjahre[GostHalbjahr.Q12.id] === "LK" ? "LK" : null;
				break;
			case "ZK": {
				const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach))
					? GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW || "")
					: GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE || "");
				if ((beginn !== null) && (beginn === GostHalbjahr.Q12))
					wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				if ((beginn !== null) && (beginn === GostHalbjahr.Q21))
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				break;
			}
			case "LK":
				wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				wahl.abiturFach = null;
				break;
		}
		// Sonderfall Sport - darf AT haben
		if (!wahl.halbjahre[GostHalbjahr.Q21.id] && GostFachbereich.SPORT.hat(props.fach))
			wahl.halbjahre[GostHalbjahr.Q21.id] = "AT";
		else if (wahl.halbjahre[GostHalbjahr.Q21.id] === "AT" && GostFachbereich.SPORT.hat(props.fach))
			wahl.halbjahre[GostHalbjahr.Q21.id] = null;
		// Nachfolgende HJ ebenfalls setzen
		if (wahl.halbjahre[GostHalbjahr.Q21.id] === null && !ist_VTF.value)
			wahl.halbjahre[GostHalbjahr.Q22.id] = null;
		if (wahl.halbjahre[GostHalbjahr.Q21.id] === null || wahl.halbjahre[GostHalbjahr.Q21.id] === "ZK")
			wahl.abiturFach = null;
	}


	function setQ22Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.Q22.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q22.id] = "M";
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) && (props.gostJahrgangsdaten.hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW);
					if (beginn !== null) {
						const andereWahl = getAndereFachwahl.value;
						if (beginn === GostHalbjahr.Q21) {
							if (wahl.halbjahre[GostHalbjahr.Q12.id] === null && andereWahl?.halbjahre[GostHalbjahr.Q12.id] == undefined) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK'
							}
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(props.fach) && props.gostJahrgangsdaten.hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE);
					if (beginn !== null) {
						const andereWahl = getAndereFachwahl.value;
						if (beginn === GostHalbjahr.Q21) {
							if (wahl.halbjahre[GostHalbjahr.Q12.id] === null && andereWahl?.halbjahre[GostHalbjahr.Q12.id] == undefined) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK'
							}
						}
					}
				}
				break;
			case "M":
				wahl.halbjahre[GostHalbjahr.Q22.id] = ist_VTF.value || ist_PJK.value ? null : "S";
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.Q22.id] = wahl.halbjahre[GostHalbjahr.Q21.id] === "LK" ? "LK" : null;
				break;
			case "ZK": {
				const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach))
					? GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW || "")
					: GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE || "");
				if ((beginn !== null) && (beginn === GostHalbjahr.Q21)) {
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				}
				wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				break;
			}
			case "LK":
				wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				wahl.abiturFach = null;
		}
		// Sonderfall Sport - darf AT haben
		if (!wahl.halbjahre[GostHalbjahr.Q22.id] && GostFachbereich.SPORT.hat(props.fach))
			wahl.halbjahre[GostHalbjahr.Q22.id] = "AT";
		else if (wahl.halbjahre[GostHalbjahr.Q22.id] === "AT" && GostFachbereich.SPORT.hat(props.fach))
			wahl.halbjahre[GostHalbjahr.Q22.id] = null;
		// Nachfolgende HJ ebenfalls setzen
		if (wahl.halbjahre[GostHalbjahr.Q22.id] === null || wahl.halbjahre[GostHalbjahr.Q22.id] === "ZK")
			wahl.abiturFach = null;
		if (wahl.abiturFach === 3 && wahl.halbjahre[GostHalbjahr.Q22.id] === "M")
			wahl.abiturFach = props.abiturdatenManager().pruefeExistiertAbiFach(props.abiturdatenManager().getFachbelegungen(), GostAbiturFach.AB4) ? null : 4;
		if (wahl.abiturFach === 4 && wahl.halbjahre[GostHalbjahr.Q22.id] === "S")
			wahl.abiturFach = props.abiturdatenManager().pruefeExistiertAbiFach(props.abiturdatenManager().getFachbelegungen(), GostAbiturFach.AB3) ? null : 3;
	}


	// Gibt ein false zurück, falls ein Fach mit GE/SW an diesem HJ gesetzt ist
	function zkMoeglich(halbjahr: GostHalbjahr): boolean {
		if (wahlen.value[halbjahr.id] !== 'ZK')
			return true;
		const sw = GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach);
		const ge = GostFachbereich.GESCHICHTE.hat(props.fach);
		if (!sw && !ge)
			return true;
		let beginn;
		if (sw)
			beginn = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW || "");
		if (ge)
			beginn = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE || "");
		if (!beginn || beginn === halbjahr || beginn.next() === halbjahr)
			return true;
		return false;
	}


	function setAbiturWahl(wahl: GostSchuelerFachwahl): void {
		const abiMoeglicheKursart : GostKursart | null = props.abiturdatenManager().getMoeglicheKursartAlsAbiturfach(props.fach.id);
		if (abiMoeglicheKursart === null) {
			wahl.abiturFach = null;
			return;
		}
		// LK...
		if (abiMoeglicheKursart === GostKursart.LK) {
			switch (wahl.abiturFach) {
				case 1:
					wahl.abiturFach = 2;
					break;
				case 2:
					if (GostFachUtils.istWaehlbarLeistungskurs1(props.fach))
						wahl.abiturFach = 1;
					break;
				default:
					if (GostFachUtils.istWaehlbarLeistungskurs1(props.fach) && !props.abiturdatenManager().hatAbiFach(GostAbiturFach.LK1))
						wahl.abiturFach = 1;
					wahl.abiturFach = 2;
					break;
			}
			return;
		}
		// GK...
		switch (wahl.abiturFach) {
			case null:
				wahl.abiturFach = wahl.halbjahre[GostHalbjahr.Q22.id] === "M" ? 4 : 3;
				break;
			case 4:
				wahl.abiturFach = wahl.halbjahre[GostHalbjahr.Q22.id] === "S" ? 3 : null;
				break;
			case 3:
				wahl.abiturFach = wahl.halbjahre[GostHalbjahr.Q22.id] === "M" ? 4 : null;
				break;
			default:
				wahl.abiturFach = null;
				break;
		}
	}

</script>

<style lang="postcss" scoped>

	.data-table__tr {
		--background-color: #ffffff;

		.data-table__td {
			background-color: var(--background-color);
		}

		&.svws-background-on-hover {
			.data-table__td {
				@apply bg-transparent;
			}

			&:hover {
				.data-table__td {
					background-color: var(--background-color);
				}
			}
		}
	}

</style>
