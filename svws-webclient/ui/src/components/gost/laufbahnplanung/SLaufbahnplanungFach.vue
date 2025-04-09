<template>
	<div role="row" class="svws-ui-tr text-uistatic" :style="{ 'background-color': manager.getFachfarbe(fach) }">
		<div role="cell" class="svws-ui-td select-text">
			<div class="whitespace-nowrap min-w-fit">
				{{ fach.kuerzelAnzeige }}
			</div>
		</div>
		<div role="cell" class="svws-ui-td select-all">
			<div class="break-all line-clamp-1 leading-tight -my-0.5">
				{{ fach.bezeichnung || '' }}
			</div>
		</div>
		<div role="cell" class="svws-ui-td svws-align-center svws-divider">
			{{ fach.wochenstundenQualifikationsphase }}
		</div>
		<div role="cell" class="svws-ui-td svws-align-center font-medium" :class="{ 'svws-disabled': !manager.istFremdsprache(fach) }">
			<template v-if="manager.istFremdsprache(fach)">
				<span v-if="manager.ignoriereSprachenfolge"> ? </span>
				<span v-else-if="!manager.hatSprachbelegung(fach)"> — </span>
				<span v-else> {{ manager.getSprachenfolgeNr(fach) }} </span>
			</template>
		</div>
		<div role="cell" class="svws-ui-td svws-align-center font-medium svws-divider" :class="{ 'svws-disabled': !manager.istFremdsprache(fach)}">
			<template v-if="manager.istFremdsprache(fach)">
				<span v-if="manager.ignoriereSprachenfolge"> ? </span>
				<span v-else-if="!manager.hatSprachbelegung(fach)"> — </span>
				<span v-else> {{ manager.getSprachenfolgeJahrgang(fach) }} </span>
			</template>
		</div>
		<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
			<div role="cell" class="laufbahn-cell svws-ui-td svws-align-center svws-divider select-none font-medium"
				:class="hatUpdateKompetenz ? {
					'cursor-pointer': manager.istMoeglich(fach, halbjahr) && !istBewertet(halbjahr),
					'cursor-not-allowed': (!manager.istMoeglich(fach, halbjahr) || istBewertet(halbjahr) || istFachkombiVerboten[halbjahr.id]),
					'svws-disabled': !manager.istMoeglich(fach, halbjahr),
					'svws-disabled-soft': (istBewertet(halbjahr) && manager.istMoeglich(fach, halbjahr)) || (manager.istAktuellOderVergangen(halbjahr) && manager.istMoeglich(fach, halbjahr)),
				} : {}"
				@click.stop="stepper(halbjahr)" :title="getTooltipHalbjahr(halbjahr)"
				:tabindex="manager.istMoeglich(fach, halbjahr) ? 0 : -1" @keydown.enter.prevent="handleKeyboardStep($event, halbjahr)" @keydown.space.prevent="handleKeyboardStep($event, halbjahr)"
				@keydown.delete.prevent="deleteFachwahlPlaceholder(halbjahr)" :ref="el => halbjahrRefs.set(halbjahr.id, el as HTMLElement)" @focus="() => emit('update:focus', fach.id, halbjahr.id)">
				<div class="inline-flex items-center gap-1 relative w-full">
					<span class="w-full text-center">
						<template v-if="wahlen[halbjahr.id] !== '' && wahlen[halbjahr.id] === '6'">0</template>
						<template v-else>{{ wahlen[halbjahr.id] }}&#8203;</template>
					</span>
					<span class="absolute -right-0 top-0">
						<template v-if="istFachkombiErforderlich[halbjahr.id] || istFachkombiVerboten[halbjahr.id] || !zkMoeglich(halbjahr)">
							<svws-ui-tooltip :color="istBewertet(halbjahr) ? 'light' : 'danger'" position="bottom">
								<span class="icon i-ri-error-warning-line" :class="istBewertet(halbjahr) ? 'icon-ui-75' : 'icon-ui-danger'" />
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
						<template v-else-if="!manager.istMoeglich(fach, halbjahr) && (wahlen[halbjahr.id] !== '') && hatUpdateKompetenz">
							<svws-ui-tooltip :color="istBewertet(halbjahr) && manager.hatNote(fach, halbjahr) ? 'light' : 'danger'">
								<svws-ui-button type="icon" size="small" :disabled="istBewertet(halbjahr) && manager.hatNote(fach, halbjahr)" @click="manager.deleteFachwahl(fach, halbjahr)"
									@keydown.enter.prevent="manager.deleteFachwahl(fach, halbjahr)" @keydown.space.prevent="manager.deleteFachwahl(fach, halbjahr)">
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
						<template v-else-if="(wahlen[halbjahr.id] !== '') && istBewertet(halbjahr) && (!manager.hatNote(fach, halbjahr) && !manager.belegungHatImmerNoten) && hatUpdateKompetenz">
							<svws-ui-tooltip :color="'danger'">
								<svws-ui-button type="icon" size="small" @click="manager.deleteFachwahl(fach, halbjahr)"
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
									<span class="icon i-ri-error-warning-line icon-ui-danger ml-0.5" />
								</div>
								<template #content>
									Dieser Kurs gilt aufgrund von 0 Punkten als nicht belegt.
								</template>
							</svws-ui-tooltip>
						</template>
					</span>
				</div>
			</div>
		</template>
		<div role="cell" class="laufbahn-cell svws-ui-td svws-align-center select-none font-medium"
			:class="hatUpdateKompetenz ? {
				'cursor-pointer': manager.istMoeglichAbi(fach) && !istBewertet(GostHalbjahr.Q22), '': manager.istMoeglichAbi(fach),
				'cursor-not-allowed': !manager.istMoeglichAbi(fach),
				'svws-disabled': !manager.istMoeglichAbi(fach),
				'svws-disabled-soft': istBewertet(GostHalbjahr.Q22) && manager.istMoeglichAbi(fach),
			} : {}"
			@click.stop="stepperAbi()" :tabindex="manager.istMoeglichAbi(fach) ? 0 : -1" @keydown.enter.prevent="stepperAbi()" @keydown.space.prevent="stepperAbi()" @keydown.delete.prevent="deleteFachwahlAbiturPlaceholder()"
			:ref="el => halbjahrRefs.set(GostHalbjahr.values().length, el as HTMLElement)" @focus="() => emit('update:focus', fach.id, GostHalbjahr.values().length)">
			<template v-if="abi_wahl"> {{ abi_wahl }} </template>
			<span v-if="abi_wahl && !manager.istMoeglichAbi(fach) && hatUpdateKompetenz" class="absolute -right-0">
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
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, onUpdated, ref } from "vue";
	import { AbiturdatenManager } from "../../../../../core/src/core/abschluss/gost/AbiturdatenManager";
	import type { GostJahrgangsdaten } from "../../../../../core/src/core/data/gost/GostJahrgangsdaten";
	import type { GostFach } from "../../../../../core/src/core/data/gost/GostFach";
	import { Fach } from "../../../../../core/src/asd/types/fach/Fach";
	import { GostHalbjahr } from "../../../../../core/src/core/types/gost/GostHalbjahr";
	import { AbiturFachbelegungHalbjahr } from "../../../../../core/src/core/data/gost/AbiturFachbelegungHalbjahr";
	import { GostKursart } from "../../../../../core/src/core/types/gost/GostKursart";
	import type { GostJahrgangFachkombination } from "../../../../../core/src/core/data/gost/GostJahrgangFachkombination";
	import { GostFachbereich } from "../../../../../core/src/core/types/gost/GostFachbereich";
	import type { LaufbahnplanungUiManager } from "./LaufbahnplanungUiManager";

	const props = withDefaults(defineProps<{
		manager: LaufbahnplanungUiManager;
		abiturdatenManager: () => AbiturdatenManager;
		gostJahrgangsdaten: GostJahrgangsdaten;
		fach: GostFach;
		hatUpdateKompetenz: boolean;
		activeFocus?: boolean;
		activeHalbjahrId?: number;
	}>(), {
		activeHalbjahrId: 0,
	});


	const emit = defineEmits<{
		// Beim Fokus (egal ob Pfeiltasten oder Tab) wird aktuelle fachId und halbjahrId an Parent-Komponente emitted
		"update:focus": [fachId: number, halbjahrId: number];
		// Ist Fokus nicht möglich, wid das per emit mit fachId und halbjahrId an Parent-Komponente gemeldet
		"update:focus:impossible": [fachId: number, halbjahrId: number];
	}>();

	onUpdated(() => {
		// Prüft, ob die Fach-Komponente aktuell den Fokus hat
		if (props.activeFocus)
			doFocusOnHalbjahr();
	});

	const halbjahrRefs = ref(new Map<number, HTMLElement>());

	// Nächste Halbjahr-Zelle fokussieren, wenn möglich. Sonst "update:focus:impossible" emitten, sodass Parent-Komponente einen Schritt weiter gehen kann
	function doFocusOnHalbjahr() {
		const focusCell = halbjahrRefs.value.get(props.activeHalbjahrId);
		const halbjahr = (props.activeHalbjahrId < GostHalbjahr.values().length) ? GostHalbjahr.fromID(props.activeHalbjahrId) : null;
		if ((halbjahr !== null) && (props.manager.istMoeglich(props.fach, halbjahr) || (wahlen.value[halbjahr.id] !== ""))) {
			focusCell?.focus();
		} else if ((halbjahr === null) && (props.manager.istMoeglichAbi(props.fach) || (abi_wahl.value !== ""))) {
			focusCell?.focus();
		} else {
			emit("update:focus:impossible", props.fach.id, props.activeHalbjahrId);
		}
	}

	function istBewertet(halbjahr: GostHalbjahr): boolean {
		return props.abiturdatenManager().istBewertet(halbjahr);
	}

	function getTooltipHalbjahr(halbjahr: GostHalbjahr) : string {
		if (istBewertet(halbjahr)) {
			const note = props.manager.getNote(props.fach, halbjahr);
			if (note === null)
				return 'Es liegen keine Leistungsdaten vor!';
			const schuljahr = props.abiturdatenManager().getSchuljahr();
			return `Note ${note.daten(schuljahr)?.kuerzel ?? '-'} (keine Änderungen mehr möglich)`;
		}
		return (!props.manager.istMoeglich(props.fach, halbjahr)) ? 'Wahl nicht möglich' : '';
	}


	const abi_wahl = computed<string>(() => {
		const fachbelegung = props.abiturdatenManager().getFachbelegungByID(props.fach.id);
		if ((fachbelegung === null) || (fachbelegung.abiturFach === null))
			return "";
		return fachbelegung.abiturFach.toString();
	})

	const wahlen = computed<string[]>(() => {
		const fachbelegung = props.abiturdatenManager().getFachbelegungByID(props.fach.id);
		if (fachbelegung === null)
			return ["", "", "", "", "", ""];
		return fachbelegung.belegungen.map((b: AbiturFachbelegungHalbjahr | null) => {
			b = (b !== null) ? b : new AbiturFachbelegungHalbjahr();
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

	const istFachkombiErforderlich = computed<boolean[]>(() => {
		const result = [];
		for (const halbjahr of GostHalbjahr.values())
			result.push(istFachkombiErforderlichHalbjahr(halbjahr));
		return result;
	});

	function pruefeKombinationVerboten(fachid: number, kombi: GostJahrgangFachkombination, hj: GostHalbjahr) {
		if (((fachid !== kombi.fachID1) && (fachid !== kombi.fachID2)) || (!kombi.gueltigInHalbjahr[hj.id]))
			return false;
		const fachID1 = (fachid === kombi.fachID2) ? kombi.fachID1 : fachid;
		const fachID2 = (fachid === kombi.fachID2) ? fachid : kombi.fachID2;
		const kursart1 = (fachid === kombi.fachID2) ? GostKursart.fromKuerzel(kombi.kursart1) : GostKursart.fromKuerzel(kombi.kursart2);
		const kursart2 = (fachid === kombi.fachID2) ? GostKursart.fromKuerzel(kombi.kursart2) : GostKursart.fromKuerzel(kombi.kursart1);
		const fach1 = props.abiturdatenManager().faecher().get(fachID1)
		const fach2 = props.abiturdatenManager().faecher().get(fachID2)
		if ((fach1 === null) || (fach2 === null))
			return false;
		const f1 = props.abiturdatenManager().getFachbelegungByID(fach1.id);
		const f2 = props.abiturdatenManager().getFachbelegungByID(fach2.id);
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

	const istFachkombiVerboten = computed<boolean[]>(() => {
		const result = [];
		for (const halbjahr of GostHalbjahr.values())
			result.push(istFachkombiVerbotenHalbjahr(halbjahr));
		return result;
	});

	async function stepperAbi() {
		if (!props.hatUpdateKompetenz)
			return;
		await props.manager.stepperAbitur(props.fach);
	}

	async function stepper(halbjahr: GostHalbjahr) {
		if (!props.hatUpdateKompetenz)
			return;
		await props.manager.stepper(props.fach, halbjahr);
	}


	/**
	 * Lösch-Methode für gültige Fachwahlen
	 *
	 * @param halbjahr das Halbjahr, für das die Fachwahl gelöscht werden soll
	 */
	function deleteFachwahlPlaceholder(halbjahr: GostHalbjahr | undefined) {
		// TODO: Implementieren, umbenennen, Methode "deleteFachwahl" umbenennen, möglicherweise Methoden zusammenführen
		console.log("Methode noch nicht implementiert");
	}


	/**
	 * Lösch-Methode für gültige Fachwahlen aus Abitur-Spalte
	 *
	 */
	function deleteFachwahlAbiturPlaceholder() {
		// TODO: Implementieren, umbenennen, Methode "deleteFachwahlAbitur" umbenennen, möglicherweise Methoden zusammenführen
		console.log("Methode noch nicht implementiert");
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
			beginn = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW ?? "");
		if (ge)
			beginn = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE ?? "");
		if (!beginn || (beginn === halbjahr) || (beginn.next() === halbjahr))
			return true;
		return false;
	}


	// Bei gedrückter ALT-Taste + ENTER-Taste direkt hochschreiben (handleHochschreiben), sonst Schritt weiter gehen (stepper)
	async function handleKeyboardStep(event: KeyboardEvent, halbjahr: GostHalbjahr) {
		if (event.altKey)
			await handleHochschreiben(halbjahr);
		else
			await stepper(halbjahr);
	}

	// Unabhängig vom eingestellten Modus direkt "hochschreiben" ausführen
	async function handleHochschreiben(halbjahr: GostHalbjahr) {
		if (!props.hatUpdateKompetenz)
			return;
		await props.manager.stepper(props.fach, halbjahr, 'hochschreiben');
	}

</script>

<style scoped>

	.laufbahn-cell {
		&:focus {
			outline: none;
			box-shadow: inset 0 0 0 2px;
		}
	}

	.data-table__tr {
		background-color: #ffffff;

		.data-table__td {
			background-color: var(background-color);
		}

		&.svws-background-on-hover {
			.data-table__td {
				background-color: transparent;
			}

			&:hover {
				.data-table__td {
					background-color: var(background-color);
				}
			}
		}
	}

</style>
