<template>
	<td :class="[ 'min-w-[3rem] text-center', { 'cursor-pointer': moeglich && !bewertet, '': moeglich, 'text-sm text-black/50': bewertet, 'cursor-not-allowed': cursorNotAllowed } ]"
		:style=" { 'background-color': bewertet ? bgColorTransparent : bgColor }"
		@click.stop="stepper"
		:title="bewertet ? 'Bewertet, keine Änderungen mehr möglich' : ''">
		<template v-if="halbjahr !== undefined">
			<svws-ui-popover class="popper--danger" v-if="istFachkombiErforderlich" placement="bottom">
				<template #trigger>
					<div class="inline-flex items-center">
						<span>{{ wahl }}&#8203;</span>
						<i-ri-error-warning-line class="text-error" :class="{'ml-0.5': wahl}" />
					</div>
				</template>
				<template #content>
					Fachkombination erforderlich
				</template>
			</svws-ui-popover>
			<div class="inline-flex items-center" v-else-if="!moeglich && wahl">
				<span>{{ wahl }}</span>
				<i-ri-close-line class="text-error ml-0.5 cursor-pointer" @click="deleteFachwahl" />
			</div>
			<span v-else>{{ wahl }}</span>
		</template>
		<template v-else>
			{{ wahl }}
		</template>
	</td>
</template>

<script setup lang="ts">

	import { AbiturdatenManager, Fachgruppe, GostAbiturFach, GostFach, GostFachbereich, GostFaecherManager, GostHalbjahr,
		GostJahrgangFachkombination, GostJahrgangsdaten, GostKursart, GostSchuelerFachwahl, Jahrgaenge, List, Vector, ZulaessigesFach } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	const props = withDefaults(defineProps<{
		abiturdatenManager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		gostJahrgangsdaten: GostJahrgangsdaten;
		fach: GostFach;
		halbjahr?: GostHalbjahr | undefined;
		wahl: string;
		moeglich: boolean;
		bewertet: boolean;
		fachkombiErforderlich?: List<GostJahrgangFachkombination>;
		fachkombiVerboten?: List<GostJahrgangFachkombination>;
		manuellerModus: boolean;
	}>(), {
		halbjahr: undefined,
		fachkombiErforderlich: () => new Vector<GostJahrgangFachkombination>(),
		fachkombiVerboten: () => new Vector<GostJahrgangFachkombination>()
	});

	const emit = defineEmits<{
		(e: 'update:wahl', wahl: GostSchuelerFachwahl, fachID?: number): void,
	}>();

	const istFachkombiErforderlich: ComputedRef<boolean> = computed(() => {
		if (props.halbjahr === undefined)
			return false;
		for (const kombi of props.fachkombiErforderlich) {
			if (kombi.gueltigInHalbjahr[props.halbjahr.id]) {
				const fach1 = props.faechermanager.get(kombi.fachID1);
				if (!fach1)
					return false;
				const f1 = props.abiturdatenManager.getFachbelegungByKuerzel(fach1?.kuerzel || null)
				const f2 = props.abiturdatenManager.getFachbelegungByKuerzel(props.fach.kuerzel)
				const belegung_1 = props.abiturdatenManager.pruefeBelegungMitKursart(f1, GostKursart.fromKuerzel(kombi.kursart1)!, props.halbjahr)
				const belegung_2 = props.abiturdatenManager.pruefeBelegungMitKursart(f2, GostKursart.fromKuerzel(kombi.kursart1)!, props.halbjahr);
				if (belegung_2)
					return false;
				return belegung_1 !== belegung_2;
			}
		}
		return false;
	});

	const istFachkombiVerboten: ComputedRef<boolean> = computed(() => {
		if (props.halbjahr === undefined)
			return false;
		for (const kombi of props.fachkombiVerboten) {
			if (kombi.gueltigInHalbjahr[props.halbjahr.id]) {
				const fach = props.faechermanager.get(kombi.fachID1)
				if (!fach)
					return false;
				return props.wahl ? true : false;
			}
		}
		return false;
	});

	const cursorNotAllowed: ComputedRef<boolean> = computed(() =>
		(props.halbjahr === undefined) ?
			!props.moeglich :
			!props.moeglich || props.bewertet || istFachkombiVerboten.value
	);

	const bgColor: ComputedRef<string> = computed(() => {
		if (!props.moeglich)
			return 'rgba(160,160,160,1)';
		return ((props.halbjahr === undefined) && (!props.moeglich)) || ((props.halbjahr !== undefined) && (!props.moeglich) && (!istFachkombiVerboten.value))
			? ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getHMTLFarbeRGBA(1) //'gray'
			: ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getHMTLFarbeRGB();
	});

	const bgColorTransparent: ComputedRef<string> = computed(() => {
		if (!props.moeglich)
			return 'rgba(160,160,160,0.8)';
		return ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getHMTLFarbeRGBA(0.6)
	});


	function ist_VTF(): boolean {
		return ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getFachgruppe() === Fachgruppe.FG_VX;
	}

	function ist_PJK(): boolean {
		return ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getFachgruppe() === Fachgruppe.FG_PX;
	}

	function getAndereFachwahl(): GostSchuelerFachwahl | undefined {
		const fach = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel);
		if (fach.getFachgruppe() === Fachgruppe.FG_VX)
			return;
		const fachbelegungen = props.abiturdatenManager.getFachbelegungByFachkuerzel(props.fach.kuerzel);
		if (fachbelegungen !== undefined) {
			for (const fachbelegung of fachbelegungen) {
				if (fachbelegung.fachID !== props.fach.id)
					return props.abiturdatenManager.getSchuelerFachwahl(fachbelegung.fachID)
			}
		}
	}

	function stepper() {
		if (props.manuellerModus) {
			stepper_manuell();
			return;
		}
		if ((!props.moeglich) || props.bewertet)
			return;
		const wahl = props.abiturdatenManager.getSchuelerFachwahl(props.fach.id);
		if (props.halbjahr === undefined)
			setAbiturWahl(wahl);
		else if (props.halbjahr === GostHalbjahr.EF1)
			setEF1Wahl(wahl);
		else if (props.halbjahr === GostHalbjahr.EF2)
			setEF2Wahl(wahl);
		else if (props.halbjahr === GostHalbjahr.Q11)
			setQ11Wahl(wahl);
		else if (props.halbjahr === GostHalbjahr.Q12)
			setQ12Wahl(wahl);
		else if (props.halbjahr === GostHalbjahr.Q21)
			setQ21Wahl(wahl);
		else if (props.halbjahr === GostHalbjahr.Q22)
			setQ22Wahl(wahl);
		emit('update:wahl', wahl);
	}

	function deleteFachwahl() {
		if (!props.wahl || props.moeglich === true || props.halbjahr === undefined)
			return;
		const wahl = props.abiturdatenManager.getSchuelerFachwahl(props.fach.id);
		wahl[props.halbjahr.toString() as ('EF1' | 'EF2' | 'Q11' | 'Q12' | 'Q21' | 'Q22')] = null;
		emit('update:wahl', wahl, props.fach.id);
	}

	function stepper_manuell() : void {
		const wahl = props.abiturdatenManager.getSchuelerFachwahl(props.fach.id);
		if (props.halbjahr === undefined) {
			if (!wahl.Q22)
				return
			switch (wahl.abiturFach) {
				case null:
					wahl.abiturFach = wahl.Q22 === "LK" ? 1 : 3;
					break;
				case 1:
					wahl.abiturFach = wahl.Q22 === "LK" ? 2 : 3;
					break;
				case 2:
					wahl.abiturFach = wahl.Q22 === "LK" ? null : 3;
					break;
				case 3:
					wahl.abiturFach = wahl.Q22 === "LK" ? null : 4;
					break;
				case 4:
					wahl.abiturFach = null;
					break;
				default:
					wahl.abiturFach = null;
					break;
			}
		} else {
			const hj = props.halbjahr.toString() as 'EF1' | 'EF2' | 'Q11' | 'Q12' | 'Q21' | 'Q22';
			switch (wahl[hj]) {
				case "AT":
					wahl[hj] = null;
					break;
				case "ZK":
					wahl[hj] = null;
					break;
				case null:
					wahl[hj] = "M";
					break;
				case "M":
					wahl[hj] = "S";
					break;
				case "S":
					wahl[hj] = "LK";
					break;
				case "LK": {
					wahl[hj] = null
					if (GostFachbereich.SPORT.hat(props.fach))
						wahl[hj] = "AT";
					if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) || GostFachbereich.GESCHICHTE.hat(props.fach))
						wahl[hj] = "ZK";
					break;
				}
				default:
					wahl[hj] = null;
					break;
			}
		}
		emit('update:wahl', wahl);
	}


	function setEF1Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.EF1) {
			case null: wahl.EF1 = ist_VTF() || ist_PJK() ? "M" : "S"; break;
			case "S":  wahl.EF1 = "M"; break;
			case "M":  wahl.EF1 = null; break;
		}
	}


	function setEF2Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.EF2) {
			case null:
				wahl.EF2 = ist_VTF() || ist_PJK() ? "M" : "S";
				break;
			case "S":
				wahl.EF2 = "M";
				break;
			case "M":
				wahl.EF2 = null;
				if (GostFachbereich.SPORT.hat(props.fach)) wahl.EF2 = "AT";
				break;
			case "AT":
				wahl.EF2 = null;
		}
	}


	function setQ11Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.Q11) {
			case null:
				wahl.Q11 = (GostFachbereich.DEUTSCH.hat(props.fach) || GostFachbereich.MATHEMATIK.hat(props.fach)) ? "S" : "M";
				break;
			case "M":
				wahl.Q11 = ist_VTF() || ist_PJK() ? null : "S";
				break;
			case "S":
				//S->S ist richtig, weil DE und MA muss belegt sein, entweder S oder LK, anders geht es nicht.
				wahl.Q11 = (props.fach.istMoeglichAbiLK) ? "LK" : (GostFachbereich.DEUTSCH.hat(props.fach) || GostFachbereich.MATHEMATIK.hat(props.fach)) ? "S" : null;
				break;
			case "LK":
				wahl.Q11 = (GostFachbereich.DEUTSCH.hat(props.fach) || GostFachbereich.MATHEMATIK.hat(props.fach)) ? "S" : null;
				wahl.abiturFach = null;
				break;
		}
		// Sonderfall Sport - darf AT haben
		if (!wahl.Q11 && GostFachbereich.SPORT.hat(props.fach))
			wahl.Q11 = "AT";
		else if (wahl.Q11 === "AT" && GostFachbereich.SPORT.hat(props.fach))
			wahl.Q11 = null;
		// Q11 wählt bis Q22
		switch (wahl.Q11) {
			case null:
				if (!ist_VTF()) {
					wahl.Q12 = null;
					wahl.Q21 = null;
					wahl.Q22 = null;
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) && (props.gostJahrgangsdaten.hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW);
					if ((beginn !== null) && (beginn === GostHalbjahr.Q11) && (wahl.EF2 === null)) {
						wahl.Q11 = "ZK";
						wahl.Q12 = "ZK";
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(props.fach) && props.gostJahrgangsdaten.hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE);
					if ((beginn !== null) && (beginn === GostHalbjahr.Q11) && (wahl.EF2 === null)) {
						wahl.Q11 = "ZK";
						wahl.Q12 = "ZK";
					}
				}
				break;
			case "M":
				if (props.fach.istMoeglichQ12 && !ist_VTF())
					wahl.Q12 = wahl.Q11;
				if (!(ist_VTF() || ist_PJK()) && !GostFachbereich.KUNST_MUSIK.hat(props.fach) && !GostFachbereich.RELIGION.hat(props.fach)) {
					if (props.fach.istMoeglichQ21) wahl.Q21 = wahl.Q11;
					if (props.fach.istMoeglichQ22) wahl.Q22 = wahl.Q11;
				}
				break;
			case "S":
				if (props.fach.istMoeglichQ12) wahl.Q12 = wahl.Q11;
				if (!(ist_VTF() || ist_PJK())) {
					if (props.fach.istMoeglichQ21)
						wahl.Q21 = wahl.Q11;
					// "S" kann nur für drittes Abifach gewählt werden, Vorauswahl daher "M"
					if (props.fach.istMoeglichQ22)
						wahl.Q22 = "M";
				}
				break;
			case "ZK":
				wahl.Q11 = null;
				wahl.Q12 = null;
				break;
			case "LK": {
				wahl.Q12 = props.fach.istMoeglichQ12 ? wahl.Q11 : null;
				wahl.Q21 = props.fach.istMoeglichQ21 ? wahl.Q11 : null;
				wahl.Q22 = props.fach.istMoeglichQ22 ? wahl.Q11 : null;
				// Bedingungen für LK1
				const alle_fachbelegungen = props.abiturdatenManager.getFachbelegungen();
				const lk1_belegt = props.abiturdatenManager.pruefeExistiertAbiFach(alle_fachbelegungen, GostAbiturFach.LK1);
				const lk2_belegt = props.abiturdatenManager.pruefeExistiertAbiFach(alle_fachbelegungen, GostAbiturFach.LK2);
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
		if (wahl.Q11 === null || wahl.Q11 === "M")
			wahl.abiturFach = null;
	}


	function setQ12Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.Q12) {
			case null:
				wahl.Q12 = "M";
				if (ist_PJK() && wahl.Q11 === null && props.fach.istMoeglichQ21) {
					wahl.Q21 = "M";
					wahl.Q22 = null;
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) && (props.gostJahrgangsdaten.hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW);
					if ((beginn !== null) && (((beginn === GostHalbjahr.Q11) && (wahl.EF2 === null)) || ((beginn === GostHalbjahr.Q12) && (wahl.Q11 === null)))) {
						if (beginn === GostHalbjahr.Q11)
							wahl.Q11 = "ZK";
						wahl.Q12 = "ZK";
						if (beginn === GostHalbjahr.Q12)
							wahl.Q21 = "ZK";
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(props.fach) && props.gostJahrgangsdaten.hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE);
					if ((beginn !== null) && (((beginn === GostHalbjahr.Q11) && (wahl.EF2 === null)) || ((beginn === GostHalbjahr.Q12) && (wahl.Q11 === null)))) {
						if (beginn === GostHalbjahr.Q11)
							wahl.Q11 = "ZK";
						wahl.Q12 = "ZK";
						if (beginn === GostHalbjahr.Q12)
							wahl.Q21 = "ZK";
					}
				}
				break;
			case "M":
				wahl.Q12 = ist_VTF() || ist_PJK() ? null : "S";
				break;
			case "S":
				wahl.Q12 = wahl.Q11 === "LK" ? "LK" : null;
				break;
			case "ZK": {
				const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach))
					? GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW || "")
					: GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE || "");
				if ((beginn !== null) && (beginn === GostHalbjahr.Q11))
					wahl.Q11 = null;
				wahl.Q12 = null;
				if ((beginn !== null) && (beginn === GostHalbjahr.Q12))
					wahl.Q21 = null;
				break;
				// TODO: Warum ist das so? Bis Q22. Was ist erlaubt: M, S, null?
			}
			case "LK":
				wahl.Q12 = null;
				wahl.abiturFach = null;
		}
		// Sonderfall Sport - darf AT haben
		if (!wahl.Q12 && GostFachbereich.SPORT.hat(props.fach))
			wahl.Q12 = "AT";
		else if (wahl.Q12 === "AT" && GostFachbereich.SPORT.hat(props.fach))
			wahl.Q12 = null;
		// Nachfolgende HJ ebenfalls setzen
		if (wahl.Q12 === null && !ist_VTF()) {
			wahl.Q21 = null;
			wahl.Q22 = null;
		}
		if (wahl.Q12 === null || wahl.Q12 === "M")
			wahl.abiturFach = null;
	}


	function setQ21Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.Q21) {
			case null:
				wahl.Q21 = "M";
				if (ist_PJK() && wahl.Q12 === null && props.fach.istMoeglichQ22) {
					wahl.Q22 = "M";
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) && (props.gostJahrgangsdaten.hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW);
					if ((beginn !== null) && (((beginn === GostHalbjahr.Q12) && (wahl.Q11 === null)) || ((beginn === GostHalbjahr.Q21) && (wahl.Q12 === null)))) {
						if (beginn === GostHalbjahr.Q12)
							wahl.Q12 = "ZK";
						wahl.Q21 = "ZK";
						if (beginn === GostHalbjahr.Q21)
							wahl.Q22 = "ZK";
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(props.fach) && props.gostJahrgangsdaten.hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE);
					if ((beginn !== null) && (((beginn === GostHalbjahr.Q12) && (wahl.Q11 === null)) || ((beginn === GostHalbjahr.Q21) && (wahl.Q12 === null)))) {
						if (beginn === GostHalbjahr.Q12)
							wahl.Q12 = "ZK";
						wahl.Q21 = "ZK";
						if (beginn === GostHalbjahr.Q21)
							wahl.Q22 = "ZK";
					}
				}
				break;
			case "M":
				wahl.Q21 = ist_VTF() || ist_PJK() ? null : "S";
				break;
			case "S":
				wahl.Q21 = wahl.Q12 === "LK" ? "LK" : null;
				break;
			case "ZK": {
				const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach))
					? GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW || "")
					: GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE || "");
				if ((beginn !== null) && (beginn === GostHalbjahr.Q12))
					wahl.Q12 = null;
				wahl.Q21 = null;
				if ((beginn !== null) && (beginn === GostHalbjahr.Q21))
					wahl.Q22 = null;
				break;
			}
			case "LK":
				wahl.Q21 = null;
				wahl.abiturFach = null;
				break;
		}
		// Sonderfall Sport - darf AT haben
		if (!wahl.Q21 && GostFachbereich.SPORT.hat(props.fach))
			wahl.Q21 = "AT";
		else if (wahl.Q21 === "AT" && GostFachbereich.SPORT.hat(props.fach))
			wahl.Q21 = null;
		// Nachfolgende HJ ebenfalls setzen
		if (wahl.Q21 === null && !ist_VTF())
			wahl.Q22 = null;
		if (wahl.Q21 === null || wahl.Q21 === "ZK")
			wahl.abiturFach = null;
	}


	function setQ22Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.Q22) {
			case null:
				wahl.Q22 = "M";
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) && (props.gostJahrgangsdaten.hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW);
					if ((beginn !== null) && (beginn === GostHalbjahr.Q21) && (wahl.Q12 === null)) {
						wahl.Q21 = "ZK";
						wahl.Q22 = "ZK";
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(props.fach) && props.gostJahrgangsdaten.hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE);
					if ((beginn !== null) && (beginn === GostHalbjahr.Q21) && (wahl.Q12 === null)) {
						wahl.Q21 = "ZK";
						wahl.Q22 = "ZK";
					}
				}
				break;
			case "M":
				wahl.Q22 = ist_VTF() || ist_PJK() ? null : "S";
				break;
			case "S":
				wahl.Q22 = wahl.Q21 === "LK" ? "LK" : null;
				break;
			case "ZK": {
				const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach))
					? GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW || "")
					: GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE || "");
				if ((beginn !== null) && (beginn === GostHalbjahr.Q21)) {
					wahl.Q21 = null;
				}
				wahl.Q22 = null;
				break;
			}
			case "LK":
				wahl.Q22 = null;
				wahl.abiturFach = null;
		}
		// Sonderfall Sport - darf AT haben
		if (!wahl.Q22 && GostFachbereich.SPORT.hat(props.fach))
			wahl.Q22 = "AT";
		else if (wahl.Q22 === "AT" && GostFachbereich.SPORT.hat(props.fach))
			wahl.Q22 = null;
		// Nachfolgende HJ ebenfalls setzen
		if (wahl.Q22 === null || wahl.Q22 === "ZK")
			wahl.abiturFach = null;
		if (wahl.abiturFach === 3 && wahl.Q22 === "M")
			wahl.abiturFach = props.abiturdatenManager.pruefeExistiertAbiFach(props.abiturdatenManager.getFachbelegungen(), GostAbiturFach.AB4) ? null : 4;
		if (wahl.abiturFach === 4 && wahl.Q22 === "S")
			wahl.abiturFach = props.abiturdatenManager.pruefeExistiertAbiFach(props.abiturdatenManager.getFachbelegungen(), GostAbiturFach.AB3) ? null : 3;
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


	function setAbiturWahl(wahl: GostSchuelerFachwahl): void {
		if (wahl.Q11 === "LK" && wahl.Q12 === "LK" && wahl.Q21 === "LK" && wahl.Q22 === "LK") {
			if (!getAbiLKMoeglich()) {
				wahl.abiturFach = null;
			} else {
				switch (wahl.abiturFach) {
					case 1:
						wahl.abiturFach = 2;
						break;
					case 2:
						// TODO Prüfe zuvor, ob das Fach überhaupt als erster LK wählbar ist
						wahl.abiturFach = 1;
						break;
					default:
						wahl.abiturFach = 2;
						break;
				}
			}
		} else if ((wahl.Q11 === "S" || wahl.Q11 === "LK") && (wahl.Q12 === "S" || wahl.Q12 === "LK")
			&& (wahl.Q21 === "S" || wahl.Q21 === "LK") && (wahl.Q22 === "S" || wahl.Q22 === "LK" || wahl.Q22 === "M")) {
			if (!getAbiGKMoeglich()) {
				wahl.abiturFach = null;
			} else {
				switch (wahl.abiturFach) {
					case null:
						wahl.abiturFach = wahl.Q22 === "M" ? 4 : 3;
						break;
					case 4:
						wahl.abiturFach = wahl.Q22 === "S" ? 3 : null;
						break;
					case 3:
						wahl.abiturFach = wahl.Q22 === "M" ? 4 : null;
						break;
					default:
						wahl.abiturFach = null;
						break;
				}
			}
		} else {
			wahl.abiturFach = null;
		}
	}

</script>
