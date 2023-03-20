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
		GostJahrgangFachkombination, GostJahrgangsdaten, GostKursart, GostSchuelerFachwahl, Jahrgaenge, List, Vector, ZulaessigesFach } from "@svws-nrw/svws-core";
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
		wahl.halbjahre[props.halbjahr.id] = null;
		emit('update:wahl', wahl, props.fach.id);
	}

	function stepper_manuell() : void {
		const wahl = props.abiturdatenManager.getSchuelerFachwahl(props.fach.id);
		if (props.halbjahr === undefined) {
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
		} else {
			const hj = props.halbjahr.id;
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
					wahl.halbjahre[hj] = "LK";
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
		}
		emit('update:wahl', wahl);
	}


	function setEF1Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.EF1.id]) {
			case null: wahl.halbjahre[GostHalbjahr.EF1.id] = ist_VTF() || ist_PJK() ? "M" : "S"; break;
			case "S":  wahl.halbjahre[GostHalbjahr.EF1.id] = "M"; break;
			case "M":  wahl.halbjahre[GostHalbjahr.EF1.id] = null; break;
		}
	}


	function setEF2Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.EF2.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.EF2.id] = ist_VTF() || ist_PJK() ? "M" : "S";
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


	function setQ11Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.Q11.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q11.id] = (GostFachbereich.DEUTSCH.hat(props.fach) || GostFachbereich.MATHEMATIK.hat(props.fach)) ? "S" : "M";
				break;
			case "M":
				wahl.halbjahre[GostHalbjahr.Q11.id] = ist_VTF() || ist_PJK() ? null : "S";
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
				if (!ist_VTF()) {
					wahl.halbjahre[GostHalbjahr.Q12.id] = null;
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) && (props.gostJahrgangsdaten.hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW);
					if (beginn !== null) {
						const andereWahl = getAndereFachwahl();
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
						const andereWahl = getAndereFachwahl();
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
				if (props.fach.istMoeglichQ12 && !ist_VTF())
					wahl.halbjahre[GostHalbjahr.Q12.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
				if (!(ist_VTF() || ist_PJK()) && !GostFachbereich.KUNST_MUSIK.hat(props.fach) && !GostFachbereich.RELIGION.hat(props.fach)) {
					if (props.fach.istMoeglichQ21) wahl.halbjahre[GostHalbjahr.Q21.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
					if (props.fach.istMoeglichQ22) wahl.halbjahre[GostHalbjahr.Q22.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
				}
				break;
			case "S":
				if (props.fach.istMoeglichQ12) wahl.halbjahre[GostHalbjahr.Q12.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
				if (!(ist_VTF() || ist_PJK())) {
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
		if (wahl.halbjahre[GostHalbjahr.Q11.id] === null || wahl.halbjahre[GostHalbjahr.Q11.id] === "M")
			wahl.abiturFach = null;
	}


	function setQ12Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.Q12.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q12.id] = "M";
				if (ist_PJK() && wahl.halbjahre[GostHalbjahr.Q11.id] === null && props.fach.istMoeglichQ21) {
					wahl.halbjahre[GostHalbjahr.Q21.id] = "M";
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) && (props.gostJahrgangsdaten.hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW);
					if (beginn !== null) {
						const andereWahl = getAndereFachwahl();
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
						const andereWahl = getAndereFachwahl();
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
				wahl.halbjahre[GostHalbjahr.Q12.id] = ist_VTF() || ist_PJK() ? null : "S";
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
		if (wahl.halbjahre[GostHalbjahr.Q12.id] === null && !ist_VTF()) {
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
				if (ist_PJK() && wahl.halbjahre[GostHalbjahr.Q12.id] === null && props.fach.istMoeglichQ22) {
					wahl.halbjahre[GostHalbjahr.Q22.id] = "M";
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) && (props.gostJahrgangsdaten.hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW);
					if (beginn !== null) {
						const andereWahl = getAndereFachwahl();
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
						const andereWahl = getAndereFachwahl();
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
				wahl.halbjahre[GostHalbjahr.Q21.id] = ist_VTF() || ist_PJK() ? null : "S";
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
		if (wahl.halbjahre[GostHalbjahr.Q21.id] === null && !ist_VTF())
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
						const andereWahl = getAndereFachwahl();
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
						const andereWahl = getAndereFachwahl();
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
				wahl.halbjahre[GostHalbjahr.Q22.id] = ist_VTF() || ist_PJK() ? null : "S";
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
			wahl.abiturFach = props.abiturdatenManager.pruefeExistiertAbiFach(props.abiturdatenManager.getFachbelegungen(), GostAbiturFach.AB4) ? null : 4;
		if (wahl.abiturFach === 4 && wahl.halbjahre[GostHalbjahr.Q22.id] === "S")
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
		if (wahl.halbjahre[GostHalbjahr.Q11.id] === "LK" && wahl.halbjahre[GostHalbjahr.Q12.id] === "LK" && wahl.halbjahre[GostHalbjahr.Q21.id] === "LK" && wahl.halbjahre[GostHalbjahr.Q22.id] === "LK") {
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
		} else if ((wahl.halbjahre[GostHalbjahr.Q11.id] === "S" || wahl.halbjahre[GostHalbjahr.Q11.id] === "LK") && (wahl.halbjahre[GostHalbjahr.Q12.id] === "S" || wahl.halbjahre[GostHalbjahr.Q12.id] === "LK")
			&& (wahl.halbjahre[GostHalbjahr.Q21.id] === "S" || wahl.halbjahre[GostHalbjahr.Q21.id] === "LK") && (wahl.halbjahre[GostHalbjahr.Q22.id] === "S" || wahl.halbjahre[GostHalbjahr.Q22.id] === "LK" || wahl.halbjahre[GostHalbjahr.Q22.id] === "M")) {
			if (!getAbiGKMoeglich()) {
				wahl.abiturFach = null;
			} else {
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
		} else {
			wahl.abiturFach = null;
		}
	}

</script>
