<template>
	<div role="cell" class="data-table__td data-table__td__align-center select-none font-medium" :class="
		{
			'cursor-pointer': moeglich && !bewertet, '': moeglich,
			'text-black/50 data-table__td__disabled': bewertet,
			'data-table__td__disabled cursor-not-allowed': cursorNotAllowed,
			'data-table__th__separate': halbjahr?.halbjahr === 2 && (halbjahr.jahrgang === 'EF' || halbjahr.jahrgang === 'Q2'),
		}
	"
		:style=" { 'background-color': bewertet ? bgColorTransparent : bgColor }"
		@click.stop="stepper"
		:title="bewertet ? 'Bewertet, keine Änderungen mehr möglich' : ''">
		<template v-if="halbjahr !== undefined">
			<svws-ui-tooltip color="danger" v-if="istFachkombiErforderlich || istFachkombiVerboten || !zkMoeglich" position="bottom">
				<div class="inline-flex items-center">
					<span>{{ wahl }}&#8203;</span>
					<i-ri-error-warning-line class="text-error" :class="{'ml-0.5': wahl}" />
				</div>
				<template #content v-if="istFachkombiErforderlich">
					Fachkombination erforderlich
				</template>
				<template #content v-else-if="istFachkombiVerboten">
					Fachkombination ist nicht zulässig
				</template>
				<template #content v-else>
					Ein Zusatzkurs {{ fach.kuerzel }} wird in diesem Halbjahr nicht angeboten
				</template>
			</svws-ui-tooltip>
			<div class="inline-flex items-center" v-else-if="!moeglich && wahl">
				<span>{{ wahl }}</span>
				<i-ri-close-line class="text-error ml-0.5 cursor-pointer" @click="deleteFachwahl" />
			</div>
			<span v-else>
				<template v-if="wahl && wahl !== '6'">
					{{ wahl }}
				</template>
				<template v-else-if="wahl && wahl === '6'">
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
		</template>
		<template v-else>
			<template v-if="wahl">
				{{ wahl }}
			</template>
		</template>
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import type { AbiturdatenManager, GostFach, GostFaecherManager, GostJahrgangFachkombination, GostJahrgangsdaten, GostSchuelerFachwahl, List } from "@core";
	import { Fachgruppe, GostAbiturFach, GostFachbereich, GostHalbjahr, GostKursart, Jahrgaenge, ArrayList, ZulaessigesFach } from "@core";
	import { computed } from "vue";

	const props = withDefaults(defineProps<{
		abiturdatenManager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		gostJahrgangsdaten: GostJahrgangsdaten;
		fach: GostFach;
		halbjahr?: GostHalbjahr;
		wahl: string;
		moeglich: boolean;
		bewertet: boolean;
		fachkombiErforderlich?: List<GostJahrgangFachkombination>;
		fachkombiVerboten?: List<GostJahrgangFachkombination>;
		manuellerModus: boolean;
	}>(), {
		halbjahr: undefined,
		fachkombiErforderlich: () => new ArrayList<GostJahrgangFachkombination>(),
		fachkombiVerboten: () => new ArrayList<GostJahrgangFachkombination>()
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
				const f1 = props.abiturdatenManager.getFachbelegungByKuerzel(fach1.kuerzel)
				const f2 = props.abiturdatenManager.getFachbelegungByKuerzel(props.fach.kuerzel)
				const kursart1 = GostKursart.fromKuerzel(kombi.kursart1);
				const kursart2 = GostKursart.fromKuerzel(kombi.kursart2);
				const bel1 = kursart1
					? props.abiturdatenManager.pruefeBelegungMitKursart(f1, kursart1, props.halbjahr)
					: props.abiturdatenManager.pruefeBelegung(f1, props.halbjahr);
				const bel2 = kursart2
					? props.abiturdatenManager.pruefeBelegungMitKursart(f2, kursart2, props.halbjahr)
					: props.abiturdatenManager.pruefeBelegung(f2, props.halbjahr);
				if (bel2)
					return false;
				return bel1 !== bel2;
			}
		}
		return false;
	});

	const istFachkombiVerboten: ComputedRef<boolean> = computed(() => {
		if (props.halbjahr === undefined)
			return false;
		for (const kombi of props.fachkombiVerboten) {
			if (kombi.gueltigInHalbjahr[props.halbjahr.id]) {
				const fach1 = props.faechermanager.get(kombi.fachID1)
				if (!fach1)
					return false;
				const f1 = props.abiturdatenManager.getFachbelegungByKuerzel(fach1.kuerzel)
				const f2 = props.abiturdatenManager.getFachbelegungByKuerzel(props.fach.kuerzel)
				const bel1 = f1?.belegungen[props.halbjahr.id];
				const bel2 = f2?.belegungen[props.halbjahr.id];
				if ((bel1 === null) || (bel2 === null))
					return false;
				if (((kombi.kursart1 === null) || (kombi.kursart1 === bel1?.kursartKuerzel))
					&& ((kombi.kursart2 === null) || (kombi.kursart2 === bel2?.kursartKuerzel)))
					return true;
				return false;
			}
		}
		return false;
	});

	const cursorNotAllowed: ComputedRef<boolean> = computed(() =>
		(props.halbjahr === undefined) ?
			!props.moeglich :
			!props.moeglich || props.bewertet || istFachkombiVerboten.value
	);

	const bgColorDisabled: ComputedRef<string> = computed(() => `color-mix(in srgb, ${ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getHMTLFarbeRGB()}, rgb(100,100,100)`);
	// const bgColorDisabled: ComputedRef<string> = computed(() => ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getHMTLFarbeRGB());

	const bgColor: ComputedRef<string> = computed(() => {
		if (!props.moeglich)
			return bgColorDisabled.value;
		return ((props.halbjahr === undefined) && (!props.moeglich)) || ((props.halbjahr !== undefined) && (!props.moeglich) && (!istFachkombiVerboten.value))
			? ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getHMTLFarbeRGBA(1) //'gray'
			: ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getHMTLFarbeRGB();
	});

	const bgColorTransparent: ComputedRef<string> = computed(() => {
		if (!props.moeglich)
			return bgColorDisabled.value;
		return `color-mix(in srgb, ${ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getHMTLFarbeRGB()}, rgb(170,170,170)`;
	});


	const ist_VTF: ComputedRef<boolean> = computed(() =>
		ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getFachgruppe() === Fachgruppe.FG_VX);

	const ist_PJK: ComputedRef<boolean> = computed(()=>
		ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getFachgruppe() === Fachgruppe.FG_PX);

	const getAndereFachwahl: ComputedRef<GostSchuelerFachwahl | undefined> = computed(()=> {
		const fach = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel);
		if (fach.getFachgruppe() === Fachgruppe.FG_VX)
			return;
		const fachbelegungen = props.abiturdatenManager.getFachbelegungByFachkuerzel(props.fach.kuerzel);
		if (fachbelegungen !== undefined)
			for (const fachbelegung of fachbelegungen)
				if (fachbelegung.fachID !== props.fach.id)
					return props.abiturdatenManager.getSchuelerFachwahl(fachbelegung.fachID)
		return
	})

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
		if (props.bewertet)
			return;
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
			case null: wahl.halbjahre[GostHalbjahr.EF1.id] = ist_VTF.value || ist_PJK.value ? "M" : "S"; break;
			case "S":  wahl.halbjahre[GostHalbjahr.EF1.id] = "M"; break;
			case "M":  wahl.halbjahre[GostHalbjahr.EF1.id] = null; break;
		}
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
			wahl.abiturFach = props.abiturdatenManager.pruefeExistiertAbiFach(props.abiturdatenManager.getFachbelegungen(), GostAbiturFach.AB4) ? null : 4;
		if (wahl.abiturFach === 4 && wahl.halbjahre[GostHalbjahr.Q22.id] === "S")
			wahl.abiturFach = props.abiturdatenManager.pruefeExistiertAbiFach(props.abiturdatenManager.getFachbelegungen(), GostAbiturFach.AB3) ? null : 3;
	}


	const getAbiGKMoeglich: ComputedRef<boolean> = computed(()=> {
		const fachgruppe = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel).getFachgruppe();
		if (fachgruppe === Fachgruppe.FG_ME || fachgruppe === Fachgruppe.FG_VX || fachgruppe === Fachgruppe.FG_PX)
			return false;
		return props.fach.istMoeglichAbiGK;
	})


	const getAbiLKMoeglich: ComputedRef<boolean> = computed(() => {
		const fach = ZulaessigesFach.getByKuerzelASD(props.fach.kuerzel);
		const fachgruppe = fach.getFachgruppe();
		if (fachgruppe === Fachgruppe.FG_ME || fachgruppe === Fachgruppe.FG_VX || fachgruppe === Fachgruppe.FG_PX
			|| fach.getJahrgangAb() === Jahrgaenge.JG_EF || (props.fach.biliSprache === null && props.fach.biliSprache === "D"))
			return false;
		return props.fach.istMoeglichAbiLK;
	})

	//**Gibt ein false zurück, falls ein Fach mit GE/SW an diesem HJ gesetzt ist */
	const zkMoeglich: ComputedRef<boolean> = computed(()=>{
		if (props.wahl !== 'ZK')
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
		if (!beginn || beginn === props.halbjahr || beginn.next() === props.halbjahr)
			return true;
		return false;
	})


	function setAbiturWahl(wahl: GostSchuelerFachwahl): void {
		const abiMoeglicheKursart : GostKursart | null = props.abiturdatenManager.getMoeglicheKursartAlsAbiturfach(props.fach.id);
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
					// TODO Prüfe zuvor, ob das Fach überhaupt als erster LK wählbar ist
					wahl.abiturFach = 1;
					break;
				default:
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
