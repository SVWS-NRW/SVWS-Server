<template>
	<td class="select-text">
		<div class="text-left whitespace-nowrap min-w-fit">
			{{ fach.kuerzelAnzeige }}
		</div>
	</td>
	<td class="select-all">
		<div class="text-left break-all line-clamp-1">
			{{ fach.bezeichnung || '' }}
			<template v-if="manager.getFachgruppe(fach) === Fachgruppe.FG_PX">
				<svws-ui-tooltip>
					<span class="icon-sm i-ri-information-line icon-uistatic" />
					<template #content>
						<pre>{{ manager.getLeitfaecherTooltipText(fach) }}</pre>
					</template>
				</svws-ui-tooltip>
			</template>
		</div>
	</td>
	<td class="text-center ui-divider">
		{{ fach.wochenstundenQualifikationsphase }}
	</td>
	<template v-if="manager.istFremdsprache(fach)">
		<td class="text-center font-medium" :class="{ 'cell-disabled': !manager.istFremdsprache(fach) }">
			<template v-if="manager.istFremdsprache(fach)">
				<span v-if="manager.ignoriereSprachenfolge"> ? </span>
				<span v-else-if="!manager.hatSprachbelegung(fach)"> — </span>
				<span v-else> {{ manager.getSprachenfolgeNr(fach) }} </span>
			</template>
		</td>
		<td class="text-center font-medium ui-divider" :class="{ 'cell-disabled': !manager.istFremdsprache(fach)}">
			<template v-if="manager.istFremdsprache(fach)">
				<span v-if="manager.ignoriereSprachenfolge"> ? </span>
				<span v-else-if="!manager.hatSprachbelegung(fach)"> — </span>
				<span v-else> {{ manager.getSprachenfolgeJahrgang(fach) }} </span>
			</template>
		</td>
	</template>
	<template v-else>
		<td class="ui-table-grid-button text-center font-medium col-span-2 ui-divider"
			:class="hatUpdateKompetenz ? {
				'cursor-pointer': manager.zeigeReferenzfachWahl(fach),
				'cursor-not-allowed': !manager.zeigeReferenzfachWahl(fach),
				'cell-disabled': !manager.zeigeReferenzfachWahl(fach),
			} : {}"
			:ref="inputProjektkursReferenzfach(fach, rowIndex)">
			<template v-if="manager.zeigeReferenzfachWahl(fach)">
				<span class="text-left break-all line-clamp-1">{{ referenzfach }}</span>
			</template>
		</td>
	</template>
	<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
		<td class="ui-table-grid-button text-center ui-divider select-none font-medium relative flex items-center justify-center"
			:class="hatUpdateKompetenz ? {
				'cursor-pointer': manager.istMoeglich(fach, halbjahr) && !istBewertet(halbjahr),
				'cursor-not-allowed': (!manager.istMoeglich(fach, halbjahr) || istBewertet(halbjahr) || istFachkombiVerboten[halbjahr.id]),
				'cell-disabled': !manager.istMoeglich(fach, halbjahr),
				'cell-locked': (istBewertet(halbjahr) && manager.istMoeglich(fach, halbjahr)) || (manager.istAktuellOderVergangen(halbjahr) && manager.istMoeglich(fach, halbjahr)),
			} : {}"
			:title="getTooltipHalbjahr(halbjahr)"
			:ref="inputFachwahl(fach, rowIndex, halbjahr)">
			<span class="absolute left-1 flex items-center">
				<template v-if="istFachkombiErforderlich[halbjahr.id] || istFachkombiVerboten[halbjahr.id] || !zkMoeglich(halbjahr)">
					<svws-ui-tooltip :color="istBewertet(halbjahr) ? 'light' : 'danger'" position="bottom">
						<span class="icon mr-12 i-ri-error-warning-line " :class="istBewertet(halbjahr) ? 'icon-ui-75' : 'icon-ui-danger'" />
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
				</template>
			</span>
			<span class="relative">
				<template v-if="wahlen[halbjahr.id] !== '' && wahlen[halbjahr.id] === '6'">0</template>
				<template v-else>{{ wahlen[halbjahr.id] }}</template>
				<template v-if="manager.zeigeGKLWahlen() && gostLaufbahnplanungState.istGKLGewaehlt(fach.id, halbjahr)">+</template>
			</span>
			<span class="absolute flex items-center">
				<template v-if="!manager.istMoeglich(fach, halbjahr) && (wahlen[halbjahr.id] !== '') && hatUpdateKompetenz">
					<svws-ui-tooltip :color="istBewertet(halbjahr) && manager.hatNote(fach, halbjahr) ? 'light' : 'danger'">
						<svws-ui-button type="icon" size="small" :disabled="istBewertet(halbjahr) && manager.hatNote(fach, halbjahr)" @click="manager.deleteFachwahl(fach, halbjahr)"
							@keydown.enter.prevent="manager.deleteFachwahl(fach, halbjahr)" @keydown.space.prevent="manager.deleteFachwahl(fach, halbjahr)" class="left-5">
							<span class="icon i-ri-close-line" />
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
				</template>
				<template v-else-if="(wahlen[halbjahr.id] !== '') && (istBewertet(halbjahr) || manager.istAktuellOderVergangen(halbjahr)) && (!manager.hatNote(fach, halbjahr) && !manager.belegungHatImmerNoten) && hatUpdateKompetenz">
					<svws-ui-tooltip :color="'danger'">
						<svws-ui-button type="icon" size="small" @click="manager.deleteFachwahl(fach, halbjahr)" class="left-5"
							@keydown.enter.prevent="manager.deleteFachwahl(fach, halbjahr)" @keydown.space.prevent="manager.deleteFachwahl(fach, halbjahr)">
							<span class="icon i-ri-close-line" />
						</svws-ui-button>
						<template #content>
							Kurs ist bei den Fachwahlen eingetragen, es liegen aber keine Einträge in den Leistungsdaten vor. <br>
							Entfernen Sie entweder die Fachwahl durch Klicken oder korrigieren sie dies in den Leistungsdaten.
						</template>
					</svws-ui-tooltip>
				</template>
				<template v-else-if="wahlen[halbjahr.id] && wahlen[halbjahr.id] === '6'">
					<svws-ui-tooltip color="danger" position="bottom">
						<div class="inline-flex items-center">
							<span class="icon mr-12 i-ri-error-warning-line icon-ui-danger" />
						</div>
						<template #content>
							Dieser Kurs gilt aufgrund von 0 Punkten als nicht belegt.
						</template>
					</svws-ui-tooltip>
				</template>
			</span>
		</td>
	</template>
	<td v-if="hatUpdateKompetenz" class="ui-table-grid-button text-center select-none font-medium relative"
		:class="{
			'cursor-pointer': manager.istMoeglichAbi(fach) && !istBewertet(GostHalbjahr.Q22), '': manager.istMoeglichAbi(fach),
			'cursor-not-allowed': !manager.istMoeglichAbi(fach),
			'cell-disabled': !manager.istMoeglichAbi(fach),
			'cell-locked': istBewertet(GostHalbjahr.Q22) && manager.istMoeglichAbi(fach),
		}"
		:ref="inputAbitur(fach, rowIndex)">
		<template v-if="abi_wahl !== ''">
			<span class="relative">{{ abi_wahl }}</span>
			<span v-if="(abi_wahl !== '') && (!abi_wahl_pjk) && !manager.istMoeglichAbi(fach) && hatUpdateKompetenz" class="absolute right-1">
				<svws-ui-tooltip :color="'danger'">
					<svws-ui-button type="icon" size="small" @click="manager.deleteFachwahlAbitur(fach)"
						@keydown.enter.prevent="manager.deleteFachwahlAbitur(fach)" @keydown.space.prevent="manager.deleteFachwahlAbitur(fach)">
						<span class="icon i-ri-close-line" />
					</svws-ui-button>
					<template #content>
						Löschen (Nicht als Abiturfach wählbar)
					</template>
				</svws-ui-tooltip>
			</span>
		</template>
	</td>
	<td v-else class="text-center select-none font-medium"
		:class="{
			'cursor-pointer': manager.istMoeglichAbi(fach) && !istBewertet(GostHalbjahr.Q22), '': manager.istMoeglichAbi(fach),
			'cursor-not-allowed': !manager.istMoeglichAbi(fach),
			'cell-disabled': !manager.istMoeglichAbi(fach),
			'cell-locked': istBewertet(GostHalbjahr.Q22) && manager.istMoeglichAbi(fach),
		}">
		<span v-if="abi_wahl !== ''" class="relative">{{ abi_wahl }}</span>
	</td>
</template>

<script setup lang="ts">

	import { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
	import { AbiturFachbelegungHalbjahr } from "@core/core/data/gost/AbiturFachbelegungHalbjahr";
	import type { GostFach } from "@core/core/data/gost/GostFach";
	import type { GostJahrgangFachkombination } from "@core/core/data/gost/GostJahrgangFachkombination";
	import { GostFachbereich } from "@core/core/types/gost/GostFachbereich";
	import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
	import { GostKursart } from "@core/core/types/gost/GostKursart";
	import type { Collection } from "@core/java/util/Collection";
	import { useGostLaufbahnplanungState } from "@ui/states/GostLaufbahnplanungState";
	import type { GridManager } from "@ui/ui/controls/tablegrid/GridManager";
	import type { ComponentPublicInstance } from "vue";
	import { computed, watchEffect } from "vue";
	import type { LaufbahnplanungUiManager } from "./LaufbahnplanungUiManager";
	import { Fachgruppe } from "@core/asd/types/fach/Fachgruppe";

	const props = defineProps<{
		gridManager: GridManager<string, GostFach, Collection<GostFach>>;
		rowIndex: number;
		manager: LaufbahnplanungUiManager;
		fach: GostFach;
		hatUpdateKompetenz: boolean;
	}>();

	const gostLaufbahnplanungState = useGostLaufbahnplanungState();

	function inputAbitur(fach: GostFach, rowIndex: number) {
		const key = `Fach_${fach.id}_Abitur`;
		const setter = (value: boolean) => {
			if (!props.hatUpdateKompetenz) {
				return;
			}
			void props.manager.stepperAbitur(props.fach);
		};
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			const input = props.gridManager.applyInputToggle(key, 8, rowIndex, element, setter);
			if (input !== null) {
				input.navigateOnEnter = null;
				watchEffect(() => {
					if (gostLaufbahnplanungState.valid) {
						const fachbelegung = gostLaufbahnplanungState.abiturdatenManager.getFachbelegungByID(props.fach.id);
						const neu = (fachbelegung === null) || (fachbelegung.abiturFach === null) ? "" : fachbelegung.abiturFach.toString();
						props.gridManager.update(key, neu);
					}
				});
			}
		};
	}

	function inputFachwahl(fach: GostFach, rowIndex: number, halbjahr: GostHalbjahr) {
		const key = `Fach_${fach.id}_Halbjahr_${halbjahr.id}`;
		const setter = (value: boolean) => {
			if (!props.hatUpdateKompetenz) {
				return;
			}
			void props.manager.stepper(props.fach, halbjahr);
		};
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			const input = props.gridManager.applyInputToggle(key, halbjahr.id + 2, rowIndex, element, setter);
			if (input !== null) {
				input.navigateOnEnter = null;
				watchEffect(() => props.gridManager.update(key, wahlen.value[halbjahr.id]));
			}
		};
	}

	function inputProjektkursReferenzfach(fach: GostFach, rowIndex: number) {
		const key = `Fach_${fach.id}_Referenzfach`;
		const setter = (value: boolean) => {
			if (!props.hatUpdateKompetenz) {
				return;
			}
			void props.manager.stepperReferenzfach(props.fach);
		};
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			const input = props.gridManager.applyInputToggle(key, 1, rowIndex, element, setter);
			if (input !== null) {
				input.navigateOnEnter = null;
				watchEffect(() => props.gridManager.update(key, referenzfach.value));
			}
		};
	}

	function istBewertet(halbjahr: GostHalbjahr): boolean {
		return gostLaufbahnplanungState.abiturdatenManager.istBewertet(halbjahr);
	}

	function getTooltipHalbjahr(halbjahr: GostHalbjahr): string {
		if (istBewertet(halbjahr)) {
			const note = props.manager.getNote(props.fach, halbjahr);
			if (note === null) {
				return 'Es liegen keine Leistungsdaten vor!';
			}
			const schuljahr = gostLaufbahnplanungState.abiturdatenManager.getSchuljahr();
			return `Note ${note.daten(schuljahr)?.kuerzel ?? '-'} (keine Änderungen mehr möglich)`;
		}
		return props.manager.istMoeglich(props.fach, halbjahr) ? '' : 'Wahl nicht möglich';
	}


	const abi_wahl = computed<string>(() => {
		if (!gostLaufbahnplanungState.valid) {
			return "";
		}
		const fachbelegung = gostLaufbahnplanungState.abiturdatenManager.getFachbelegungByID(props.fach.id);
		if (fachbelegung === null) {
			return "";
		}
		if (fachbelegung.abiturFach === null) {
			return (props.manager.istAbi30ProjektkursAbiturfach5(fachbelegung)) ? "(5)" : "";
		}
		return fachbelegung.abiturFach.toString();
	});

	const abi_wahl_pjk = computed<boolean>(() => {
		if (!gostLaufbahnplanungState.valid) {
			return false;
		}
		const fachbelegung = gostLaufbahnplanungState.abiturdatenManager.getFachbelegungByID(props.fach.id);
		if (fachbelegung === null) {
			return false;
		}
		return (fachbelegung.abiturFach === null) && (props.manager.istAbi30ProjektkursAbiturfach5(fachbelegung));
	});

	const wahlen = computed<string[]>(() => {
		if (!gostLaufbahnplanungState.valid) {
			return ["", "", "", "", "", ""];
		}
		const fachbelegung = gostLaufbahnplanungState.abiturdatenManager.getFachbelegungByID(props.fach.id);
		if (fachbelegung === null) {
			return ["", "", "", "", "", ""];
		}
		return fachbelegung.belegungen.map((b: AbiturFachbelegungHalbjahr | null) => {
			b = (b === null) ? new AbiturFachbelegungHalbjahr() : b;
			if (AbiturdatenManager.istNullPunkteBelegungInQPhase(b)) {
				return "6";
			}
			const kursart = GostKursart.fromKuerzel(b.kursartKuerzel);
			if (!kursart) {
				return b.kursartKuerzel;
			}
			switch (kursart) {
				case GostKursart.ZK:
				case GostKursart.LK:
					return kursart.kuerzel;
			}
			return b.schriftlich ? "S" : "M";
		});
	});


	const referenzfach = computed<string>(() => {
		if (!gostLaufbahnplanungState.valid) {
			return "---";
		}
		const fachbelegung = gostLaufbahnplanungState.abiturdatenManager.getFachbelegungByID(props.fach.id);
		if ((fachbelegung === null) || (fachbelegung.idReferenzfach === null)) {
			return "---";
		}
		const referenzfach = gostLaufbahnplanungState.abiturdatenManager.faecher().get(fachbelegung.idReferenzfach);
		if (referenzfach === null) {
			return "---";
		}
		return `${referenzfach.kuerzelAnzeige} - ${referenzfach.bezeichnung}`;
	});


	function pruefeKombinationErforderlich(fachid: number, kombi: GostJahrgangFachkombination, hj: GostHalbjahr) {
		if (fachid !== kombi.fachID2) {
			return false;
		}
		if (!kombi.gueltigInHalbjahr[hj.id]) {
			return false;
		}
		const fach1 = gostLaufbahnplanungState.abiturdatenManager.faecher().get(kombi.fachID1);
		if (fach1 === null) {
			return false;
		}
		const f1 = gostLaufbahnplanungState.abiturdatenManager.getFachbelegungByID(fach1.id);
		const f2 = gostLaufbahnplanungState.abiturdatenManager.getFachbelegungByID(fachid);
		const kursart1 = GostKursart.fromKuerzel(kombi.kursart1);
		const kursart2 = GostKursart.fromKuerzel(kombi.kursart2);
		const bel1 = kursart1
			? gostLaufbahnplanungState.abiturdatenManager.pruefeBelegungMitKursart(f1, kursart1, hj)
			: gostLaufbahnplanungState.abiturdatenManager.pruefeBelegung(f1, hj);
		const bel2 = kursart2
			? gostLaufbahnplanungState.abiturdatenManager.pruefeBelegungMitKursart(f2, kursart2, hj)
			: gostLaufbahnplanungState.abiturdatenManager.pruefeBelegung(f2, hj);
		if (bel2) {
			return false;
		}
		return bel1 !== bel2;
	}

	function istFachkombiErforderlichHalbjahr(hj: GostHalbjahr): boolean {
		if (!gostLaufbahnplanungState.valid) {
			return false;
		}
		for (const kombi of gostLaufbahnplanungState.abiturdatenManager.faecher().getFachkombinationenErforderlich()) {
			if (pruefeKombinationErforderlich(props.fach.id, kombi, hj)) {
				return true;
			}
		}
		return false;
	}

	const istFachkombiErforderlich = computed<boolean[]>(() => {
		const result = [];
		for (const halbjahr of GostHalbjahr.values()) {
			result.push(istFachkombiErforderlichHalbjahr(halbjahr));
		}
		return result;
	});

	function pruefeKombinationVerboten(fachid: number, kombi: GostJahrgangFachkombination, hj: GostHalbjahr) {
		if (((fachid !== kombi.fachID1) && (fachid !== kombi.fachID2)) || (!kombi.gueltigInHalbjahr[hj.id])) {
			return false;
		}
		const fachID1 = (fachid === kombi.fachID2) ? kombi.fachID1 : fachid;
		const fachID2 = (fachid === kombi.fachID2) ? fachid : kombi.fachID2;
		const kursart1 = (fachid === kombi.fachID2) ? GostKursart.fromKuerzel(kombi.kursart1) : GostKursart.fromKuerzel(kombi.kursart2);
		const kursart2 = (fachid === kombi.fachID2) ? GostKursart.fromKuerzel(kombi.kursart2) : GostKursart.fromKuerzel(kombi.kursart1);
		const fach1 = gostLaufbahnplanungState.abiturdatenManager.faecher().get(fachID1);
		const fach2 = gostLaufbahnplanungState.abiturdatenManager.faecher().get(fachID2);
		if ((fach1 === null) || (fach2 === null)) {
			return false;
		}
		const f1 = gostLaufbahnplanungState.abiturdatenManager.getFachbelegungByID(fach1.id);
		const f2 = gostLaufbahnplanungState.abiturdatenManager.getFachbelegungByID(fach2.id);
		const bel1 = kursart1
			? gostLaufbahnplanungState.abiturdatenManager.pruefeBelegungMitKursart(f1, kursart1, hj)
			: gostLaufbahnplanungState.abiturdatenManager.pruefeBelegung(f1, hj);
		const bel2 = kursart2
			? gostLaufbahnplanungState.abiturdatenManager.pruefeBelegungMitKursart(f2, kursart2, hj)
			: gostLaufbahnplanungState.abiturdatenManager.pruefeBelegung(f2, hj);
		return bel1 && bel2;
	}

	function istFachkombiVerbotenHalbjahr(hj: GostHalbjahr): boolean {
		if (!gostLaufbahnplanungState.valid) {
			return false;
		}
		const fachkombis = gostLaufbahnplanungState.abiturdatenManager.faecher().getFachkombinationenVerboten();
		for (const kombi of fachkombis) {
			if (pruefeKombinationVerboten(props.fach.id, kombi, hj)) {
				return true;
			}
		}
		return false;
	}

	const istFachkombiVerboten = computed<boolean[]>(() => {
		const result = [];
		for (const halbjahr of GostHalbjahr.values()) {
			result.push(istFachkombiVerbotenHalbjahr(halbjahr));
		}
		return result;
	});

	// Gibt ein false zurück, falls ein Fach mit GE/SW an diesem HJ gesetzt ist
	function zkMoeglich(halbjahr: GostHalbjahr): boolean {
		if (wahlen.value[halbjahr.id] !== 'ZK') {
			return true;
		}
		const sw = GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach);
		const ge = GostFachbereich.GESCHICHTE.hat(props.fach);
		if (!sw && !ge) {
			return true;
		}
		let beginn;
		if (sw) {
			beginn = GostHalbjahr.fromKuerzel(gostLaufbahnplanungState.gostJahrgangsdaten.beginnZusatzkursSW ?? "");
		}
		if (ge) {
			beginn = GostHalbjahr.fromKuerzel(gostLaufbahnplanungState.gostJahrgangsdaten.beginnZusatzkursGE ?? "");
		}
		if (!beginn || (beginn === halbjahr) || (beginn.next() === halbjahr)) {
			return true;
		}
		return false;
	}

	const bgColor = computed(() => props.manager.getFachfarbe(props.fach));

</script>

<style scoped>

	td {
		background-color: v-bind(bgColor);
		color: var(--color-text-uistatic);
	}

	.cell-disabled {
		background-image: linear-gradient(rgba(0, 0, 0, 0.2), rgba(0, 0, 0, 0.2));
		background-blend-mode: multiply;
	}

	.cell-locked {
		background-image: linear-gradient(rgba(0, 0, 0, 0.1), rgba(0, 0, 0, 0.1));
		background-blend-mode: multiply;
	}

	.ui-table-grid-button {
		&:focus {
			--tw-ring-color: var(--color-ring-neutral);
		}
	}
	table.ui-table-grid {
		& > tbody > tr > th.ui-divider,
		& > tbody > tr > td.ui-divider {
			border-right: 1px solid var(--color-border-uistatic-50);
		}
		& > tbody > tr > th,
		& > tbody > tr > td {
			border-color: var(--color-border-uistatic-50);
			border-bottom: 1px solid var(--color-border-uistatic-50);
		}
	}
</style>
