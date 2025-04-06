<template>
	<div role="row" class="svws-ui-tr text-ui-static" :style="{ 'background-color': bgColor }">
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
				<span v-if="ignoriereSprachenfolge"> ? </span>
				<span v-else-if="sprachenfolgeNr === 0 && !sprachenfolgeJahrgang"> — </span>
				<span v-else> {{ sprachenfolgeNr }} </span>
			</template>
		</div>
		<div role="cell" class="svws-ui-td svws-align-center font-medium svws-divider" :class="{ 'svws-disabled': !manager.istFremdsprache(fach)}">
			<template v-if="manager.istFremdsprache(fach)">
				<span v-if="ignoriereSprachenfolge"> ? </span>
				<span v-else-if="sprachenfolgeNr === 0 && !sprachenfolgeJahrgang"> — </span>
				<span v-else> {{ sprachenfolgeJahrgang }} </span>
			</template>
		</div>
		<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
			<div role="cell" class="laufbahn-cell svws-ui-td svws-align-center svws-divider select-none font-medium"
				:class="hatUpdateKompetenz ? {
					'cursor-pointer': istMoeglich[halbjahr.id] && !istBewertet(halbjahr),
					'cursor-not-allowed': (!istMoeglich[halbjahr.id] || istBewertet(halbjahr) || istFachkombiVerboten[halbjahr.id]),
					'svws-disabled': !istMoeglich[halbjahr.id],
					'svws-disabled-soft': (istBewertet(halbjahr) && istMoeglich[halbjahr.id]) || (((gostHalbjahr !== null) && (gostHalbjahr.id >= halbjahr.id)) && istMoeglich[halbjahr.id]),
				} : {}"
				@click.stop="stepper(halbjahr)" :title="getTooltipHalbjahr(halbjahr)"
				:tabindex="istMoeglich[halbjahr.id] ? 0 : -1" @keydown.enter.prevent="handleKeyboardStep($event, halbjahr)" @keydown.space.prevent="handleKeyboardStep($event, halbjahr)"
				@keydown.delete.prevent="deleteFachwahlPlaceholder(halbjahr)" :ref="el => halbjahrRefs.set(halbjahr.id, el as HTMLElement)" @focus="() => emit('update:focus', fach.id, halbjahr.id)">
				<div class="inline-flex items-center gap-1 relative w-full">
					<span class="w-full text-center">
						<template v-if="wahlen[halbjahr.id] !== '' && wahlen[halbjahr.id] === '6'">0</template>
						<template v-else>{{ wahlen[halbjahr.id] }}&#8203;</template>
					</span>
					<span class="absolute -right-0 top-0">
						<template v-if="istFachkombiErforderlich[halbjahr.id] || istFachkombiVerboten[halbjahr.id] || !zkMoeglich(halbjahr)">
							<svws-ui-tooltip :color="istBewertet(halbjahr) ? 'light' : 'danger'" position="bottom">
								<span class="icon i-ri-error-warning-line" :class="istBewertet(halbjahr) ? 'icon-ui-contrast-75' : 'icon-ui-danger'" />
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
						<template v-else-if="!istMoeglich[halbjahr.id] && (wahlen[halbjahr.id] !== '') && hatUpdateKompetenz">
							<svws-ui-tooltip :color="istBewertet(halbjahr) && (noten[halbjahr.id] !== null) ? 'light' : 'danger'">
								<svws-ui-button type="icon" size="small" :disabled="istBewertet(halbjahr) && (noten[halbjahr.id] !== null)" @click="deleteFachwahl(halbjahr)"
									@keydown.enter.prevent="deleteFachwahl(halbjahr)" @keydown.space.prevent="deleteFachwahl(halbjahr)">
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
						<template v-else-if="(wahlen[halbjahr.id] !== '') && istBewertet(halbjahr) && ((noten[halbjahr.id] === null) && !belegungHatImmerNoten) && hatUpdateKompetenz">
							<svws-ui-tooltip :color="'danger'">
								<svws-ui-button type="icon" size="small" @click="deleteFachwahl(halbjahr)"
									@keydown.enter.prevent="deleteFachwahl(halbjahr)" @keydown.space.prevent="deleteFachwahl(halbjahr)">
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
				'cursor-pointer': istMoeglichAbi && !istBewertet(GostHalbjahr.Q22), '': istMoeglichAbi,
				'cursor-not-allowed': !istMoeglichAbi,
				'svws-disabled': !istMoeglichAbi,
				'svws-disabled-soft': istBewertet(GostHalbjahr.Q22) && istMoeglichAbi,
			} : {}"
			@click.stop="stepperAbi()" :tabindex="istMoeglichAbi ? 0 : -1" @keydown.enter.prevent="stepperAbi()" @keydown.space.prevent="stepperAbi()" @keydown.delete.prevent="deleteFachwahlAbiturPlaceholder()"
			:ref="el => halbjahrRefs.set(GostHalbjahr.values().length, el as HTMLElement)" @focus="() => emit('update:focus', fach.id, GostHalbjahr.values().length)">
			<template v-if="abi_wahl"> {{ abi_wahl }} </template>
			<span v-if="abi_wahl && !istMoeglichAbi && hatUpdateKompetenz" class="absolute -right-0">
				<svws-ui-tooltip :color="'danger'">
					<svws-ui-button type="icon" size="small" @click="deleteFachwahlAbitur()"
						@keydown.enter.prevent="deleteFachwahlAbitur()" @keydown.space.prevent="deleteFachwahlAbitur()">
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
	import type { GostSchuelerFachwahl } from "../../../../../core/src/core/data/gost/GostSchuelerFachwahl";
	import { Fachgruppe } from "../../../../../core/src/asd/types/fach/Fachgruppe";
	import { Fach } from "../../../../../core/src/asd/types/fach/Fach";
	import type { AbiturFachbelegung } from "../../../../../core/src/core/data/gost/AbiturFachbelegung";
	import type { Sprachbelegung } from "../../../../../core/src/asd/data/schueler/Sprachbelegung";
	import { SprachendatenUtils } from "../../../../../core/src/core/utils/schueler/SprachendatenUtils";
	import { GostHalbjahr } from "../../../../../core/src/core/types/gost/GostHalbjahr";
	import { AbiturFachbelegungHalbjahr } from "../../../../../core/src/core/data/gost/AbiturFachbelegungHalbjahr";
	import { GostKursart } from "../../../../../core/src/core/types/gost/GostKursart";
	import { Note } from "../../../../../core/src/asd/types/Note";
	import type { GostJahrgangFachkombination } from "../../../../../core/src/core/data/gost/GostJahrgangFachkombination";
	import { GostFachbereich } from "../../../../../core/src/core/types/gost/GostFachbereich";
	import { GostAbiturFach } from "../../../../../core/src/core/types/gost/GostAbiturFach";
	import { GostFachUtils } from "../../../../../core/src/core/utils/gost/GostFachUtils";
	import type { LaufbahnplanungUiManager } from "./LaufbahnplanungUiManager";

	const props = withDefaults(defineProps<{
		manager: LaufbahnplanungUiManager;
		abiturdatenManager: () => AbiturdatenManager;
		gostJahrgangsdaten: GostJahrgangsdaten;
		fach: GostFach;
		ignoriereSprachenfolge? : boolean;
		belegungHatImmerNoten?: boolean;
		hatUpdateKompetenz: boolean;
		setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
		activeFocus?: boolean;
		activeHalbjahrId?: number;
	}>(), {
		ignoriereSprachenfolge: false,
		belegungHatImmerNoten: false,
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
		if(props.activeFocus)
			doFocusOnHalbjahr();
	});

	const halbjahrRefs = ref(new Map<number, HTMLElement>());
	const schuljahr = computed<number>(() => props.abiturdatenManager().getSchuljahr());
	const gostHalbjahr = computed<GostHalbjahr | null>(() => GostHalbjahr.fromJahrgangUndHalbjahr(props.gostJahrgangsdaten.jahrgang, props.gostJahrgangsdaten.halbjahr));
	const bgColor = computed<string>(() => Fach.getBySchluesselOrDefault(props.fach.kuerzel).getHMTLFarbeRGB(schuljahr.value));
	const fachbelegung = computed<AbiturFachbelegung | null>(() => props.abiturdatenManager().getFachbelegungByID(props.fach.id));

	// Nächste Halbjahr-Zelle fokussieren, wenn möglich. Sonst "update:focus:impossible" emitten, sodass Parent-Komponente einen Schritt weiter gehen kann
	function doFocusOnHalbjahr() {
		const focusCell = halbjahrRefs.value.get(props.activeHalbjahrId);
		if (istMoeglich.value[props.activeHalbjahrId]
			|| ((props.activeHalbjahrId !== 6) && (wahlen.value[props.activeHalbjahrId] !== ""))
			|| istMoeglichAbi.value || (abi_wahl.value !== ""))
			focusCell?.focus();
		else
			emit("update:focus:impossible", props.fach.id, props.activeHalbjahrId);
	}

	const sprachbelegung = computed<Sprachbelegung | null>(() => {
		const sprach_kuerzel = Fach.getBySchluesselOrDefault(props.fach.kuerzel).daten(schuljahr.value)?.kuerzel ?? null;
		if (sprach_kuerzel === null)
			return null;
		for (const sprache of props.abiturdatenManager().getSprachendaten().belegungen)
			if (sprache.sprache === sprach_kuerzel)
				return sprache;
		return null;
	})

	const sprachenfolgeNr = computed<number>(() =>
		props.manager.istFremdspracheMoeglich(props.fach) ? sprachbelegung.value?.reihenfolge ?? 0 : 0
	);

	const sprachenfolgeJahrgang = computed<string>(() =>
		props.manager.istFremdspracheMoeglich(props.fach) ? (sprachbelegung.value?.belegungVonJahrgang ?? "") : ""
	);

	function istBewertet(halbjahr: GostHalbjahr): boolean {
		return props.abiturdatenManager().istBewertet(halbjahr);
	}

	const istMoeglichAbi = computed<boolean>(() => props.abiturdatenManager().getMoeglicheKursartAlsAbiturfach(props.fach.id) !== null);

	const istMoeglich = computed<boolean[]>(() => {
		if (props.manager.istFremdsprache(props.fach) && ((!props.ignoriereSprachenfolge) && !props.manager.istFremdspracheMoeglich(props.fach)))
			return [ false, false, false, false, false, false ];
		const istNichtErsatzOderPjk = (props.manager.getFachgruppe(props.fach) !== Fachgruppe.FG_ME)
			&& (props.manager.getFachgruppe(props.fach) !== Fachgruppe.FG_PX);
		return [
			(props.fach.istMoeglichEF1 && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.EF1) && istNichtErsatzOderPjk),
			(props.fach.istMoeglichEF2 && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.EF2) && istNichtErsatzOderPjk),
			(props.fach.istMoeglichQ11 && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.Q11)),
			(props.fach.istMoeglichQ12 && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.Q12)),
			(props.fach.istMoeglichQ21 && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.Q21)),
			(props.fach.istMoeglichQ22 && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.Q22)),
		];
	});


	function getTooltipHalbjahr(halbjahr: GostHalbjahr) : string {
		if (istBewertet(halbjahr)) {
			const note = noten.value[halbjahr.id];
			if (note === null)
				return 'Es liegen keine Leistungsdaten vor!';
			return `Note ${note.daten(schuljahr.value)?.kuerzel ?? '-'} (keine Änderungen mehr möglich)`;
		}
		return (!istMoeglich.value[halbjahr.id]) ? 'Wahl nicht möglich' : '';
	}


	const abi_wahl = computed<string>(() => {
		if ((fachbelegung.value === null) || (fachbelegung.value.abiturFach === null))
			return "";
		return fachbelegung.value.abiturFach.toString();
	})

	const wahlen = computed<string[]>(() => {
		if (fachbelegung.value === null)
			return ["", "", "", "", "", ""];
		return fachbelegung.value.belegungen.map((b: AbiturFachbelegungHalbjahr | null) => {
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


	const noten = computed<Array<Note | null>>(() => {
		if (fachbelegung.value === null)
			return [null, null, null, null, null, null];
		return fachbelegung.value.belegungen.map((b: AbiturFachbelegungHalbjahr | null) => {
			if ((b === null) || (b.notenkuerzel === null))
				return null; // gebe explizit null zurück, da dann keine Leistungsdaten für die Belegung vorliegen
			return Note.fromKuerzel(b.notenkuerzel);
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

	const ist_VTF = computed<boolean>(() => props.manager.getFachgruppe(props.fach) === Fachgruppe.FG_VX);
	const ist_PJK = computed<boolean>(() => props.manager.getFachgruppe(props.fach) === Fachgruppe.FG_PX);

	async function stepperAbi() {
		if (!props.hatUpdateKompetenz)
			return;
		if (props.manager.modus === 'manuell') {
			await stepper_manuellAbi();
			return;
		}
		if (!istMoeglichAbi.value)
			return;
		const wahl = props.abiturdatenManager().getSchuelerFachwahl(props.fach.id);
		setAbiturWahl(wahl);
		await props.setWahl(props.fach.id, wahl);
	}

	async function stepper(halbjahr: GostHalbjahr) {
		if (!props.hatUpdateKompetenz)
			return;
		if (props.manager.modus === 'manuell') {
			await stepper_manuell(halbjahr);
			return;
		}
		if ((!istMoeglich.value[halbjahr.id]) || istBewertet(halbjahr))
			return;
		const wahl = props.abiturdatenManager().getSchuelerFachwahl(props.fach.id);
		if (halbjahr === GostHalbjahr.EF1)
			if (props.manager.modus === 'normal')
				setEF1Wahl(wahl);
			else
				setEF1WahlHochschreiben(wahl);
		else if (halbjahr === GostHalbjahr.EF2)
			if (props.manager.modus === 'normal')
				setEF2Wahl(wahl);
			else
				setEF2WahlHochschreiben(wahl);
		else if (halbjahr === GostHalbjahr.Q11)
			setQ11Wahl(wahl);
		else if (halbjahr === GostHalbjahr.Q12)
			setQ12Wahl(wahl);
		else if (halbjahr === GostHalbjahr.Q21)
			setQ21Wahl(wahl);
		else if (halbjahr === GostHalbjahr.Q22)
			setQ22Wahl(wahl);
		await props.setWahl(props.fach.id, wahl);
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


	/**
	 * Lösch-Methode für Spezialfall "Löschen einer nicht erlaubten Fachwahl"
	 *
	 * @param halbjahr das Halbjahr, für das die Fachwahl gelöscht werden soll
	 */
	async function deleteFachwahl(halbjahr: GostHalbjahr | undefined) {
		if (halbjahr === undefined)
			return;
		if (istMoeglich.value[halbjahr.id] && (!istBewertet(halbjahr) || noten.value[halbjahr.id] !== null))
			return;
		const wahl = props.abiturdatenManager().getSchuelerFachwahl(props.fach.id);
		wahl.halbjahre[halbjahr.id] = null;
		await props.setWahl(props.fach.id, wahl);
	}


	/**
	 * Lösch-Methode für Spezialfall "Löschen einer nicht erlaubten Fachwahl" aus Abitur-Spalte
	 *
	 */
	async function deleteFachwahlAbitur() {
		const wahl = props.abiturdatenManager().getSchuelerFachwahl(props.fach.id);
		wahl.abiturFach = null;
		await props.setWahl(props.fach.id, wahl);
	}


	async function stepper_manuellAbi() {
		if (istBewertet(GostHalbjahr.Q22))
			return;
		const wahl = props.abiturdatenManager().getSchuelerFachwahl(props.fach.id);
		if (wahl.halbjahre[GostHalbjahr.Q22.id] === null)
			return
		switch (wahl.abiturFach) {
			case null:
				wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "LK") ? 1 : 3;
				break;
			case 1:
				wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "LK") ? 2 : 3;
				break;
			case 2:
				wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "LK") ? null : 3;
				break;
			case 3:
				wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "LK") ? null : 4;
				break;
			case 4:
				wahl.abiturFach = null;
				break;
			default:
				wahl.abiturFach = null;
				break;
		}
		await props.setWahl(props.fach.id, wahl);
	}


	async function stepper_manuell(halbjahr: GostHalbjahr) {
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
				if (!props.fach.istPruefungsordnungsRelevant || ist_VTF.value || ist_PJK.value || (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(props.fach)))
					wahl.halbjahre[hj] = null;
				else
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
		await props.setWahl(props.fach.id, wahl);
	}


	function setEF1Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.EF1.id]) {
			case null:
				if ((ist_VTF.value || ist_PJK.value) || (GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && !props.fach.istMoeglichAbiLK))
					wahl.halbjahre[GostHalbjahr.EF1.id] = "M";
				else
					wahl.halbjahre[GostHalbjahr.EF1.id] = "S";
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.EF1.id] = "M";
				break;
			case "M":
				if (GostFachbereich.SPORT.hat(props.fach))
					wahl.halbjahre[GostHalbjahr.EF1.id] = "AT";
				else
					wahl.halbjahre[GostHalbjahr.EF1.id] = null;
				break;
			case "AT":
				wahl.halbjahre[GostHalbjahr.EF1.id] = null;
				break;
		}
	}


	function setEF2Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.EF2.id]) {
			case null:
				if ((ist_VTF.value || ist_PJK.value) || (GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && !props.fach.istMoeglichAbiLK))
					wahl.halbjahre[GostHalbjahr.EF2.id] = "M";
				else
					wahl.halbjahre[GostHalbjahr.EF2.id] = "S";
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.EF2.id] = "M";
				break;
			case "M":
				if (GostFachbereich.SPORT.hat(props.fach))
					wahl.halbjahre[GostHalbjahr.EF2.id] = "AT";
				else
					wahl.halbjahre[GostHalbjahr.EF2.id] = null;
				break;
			case "AT":
				wahl.halbjahre[GostHalbjahr.EF2.id] = null;
				break;
		}
	}

	function istWaehlbar(fach: GostFach, i : number) : boolean {
		if ((i < GostHalbjahr.EF1.id) || (i > GostHalbjahr.Q22.id))
			return false;
		switch (GostHalbjahr.fromID(i)) {
			case GostHalbjahr.EF1: return fach.istMoeglichEF1;
			case GostHalbjahr.EF2: return fach.istMoeglichEF2;
			case GostHalbjahr.Q11: return fach.istMoeglichQ11;
			case GostHalbjahr.Q12: return fach.istMoeglichQ12;
			case GostHalbjahr.Q21: return fach.istMoeglichQ21;
			case GostHalbjahr.Q22: return fach.istMoeglichQ22;
		}
		return false;
	}

	function hochschreibenIstWahlIgnoreFirstAndSecond<T>(fach: GostFach, a1: Array<T>, a2: Array<T>) {
		let i = a1.length - 2;
		while (i-- !== 0)
			if ((a1[i+2] !== a2[i]) && (istWaehlbar(fach, i + 2)))
				return false;
		return true;
	}

	function hochschreibenIstWahl<T>(fach: GostFach, a1: Array<T>, a2: Array<T>) {
		let i = a1.length;
		while (i-- !== 0)
			if ((a1[i] !== a2[i]) && (istWaehlbar(fach, i)))
				return false;
		return true;
	}

	function setEF1WahlHochschreiben(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.EF1.id]) {
			case null: {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF1.id] = 'S';
				// Prüfe, ob die Folgehalbjahre auch leer sind, dann setze auch diese
				} else if (hochschreibenIstWahl(props.fach, wahl.halbjahre, [null, null, null, null, null, null]) && !(ist_VTF.value || ist_PJK.value)) {
					if ((GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && !props.fach.istMoeglichAbiLK))
						wahl.halbjahre = ['M', 'M', 'M', 'M', 'M', 'M'];
					else if ((GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && props.fach.istMoeglichAbiLK))
						wahl.halbjahre = ['S', 'S', 'LK', 'LK', 'LK', 'LK'];
					else
						wahl.halbjahre = [
							props.fach.istMoeglichEF1 ? 'S' : null,
							props.fach.istMoeglichEF2 ? 'S' : null,
							props.fach.istMoeglichQ11 ? 'S' : null,
							props.fach.istMoeglichQ12 ? 'S' : null,
							props.fach.istMoeglichQ21 ? 'S' : null,
							props.fach.istMoeglichQ22 ? 'M' : null,
						];
				} else {
					if (ist_VTF.value || ist_PJK.value || (GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && !props.fach.istMoeglichAbiLK))
						wahl.halbjahre[GostHalbjahr.EF1.id] = "M";
					else
						wahl.halbjahre[GostHalbjahr.EF1.id] = "S";
				}
				break;
			}
			case "S": {
				if (wahl.abiturFach !== null)
					wahl.halbjahre[GostHalbjahr.EF1.id] = 'M';
				// Prüfe, ob die Folgehalbjahre S,S,S,S,M sind und Abi-Fach nicht gesetzt (Spezialfälle berücksichtigen KU+MU+RE)
				else if (hochschreibenIstWahl(props.fach, wahl.halbjahre, ['S', 'S', 'S', 'S', 'S', 'M']) && !(ist_VTF.value || ist_PJK.value))
					if (GostFachbereich.KUNST_MUSIK.hat(props.fach) || GostFachbereich.RELIGION.hat(props.fach))
						wahl.halbjahre = ['M', 'M', 'M', 'M', null, null];
					else
						wahl.halbjahre = [
							props.fach.istMoeglichEF1 ? 'M' : null,
							props.fach.istMoeglichEF2 ? 'M' : null,
							props.fach.istMoeglichQ11 ? 'M' : null,
							props.fach.istMoeglichQ12 ? 'M' : null,
							props.fach.istMoeglichQ21 ? 'M' : null,
							props.fach.istMoeglichQ22 ? 'M' : null,
						];
				else if (hochschreibenIstWahl(props.fach, wahl.halbjahre, ['S', 'S', 'LK', 'LK', 'LK', 'LK']) && (GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && props.fach.istMoeglichAbiLK))
					wahl.halbjahre = ['M', 'M', 'M', 'M', 'M', 'M'];
				else
					wahl.halbjahre[GostHalbjahr.EF1.id] = "M";
				break;
			}
			case "M": {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF1.id] = 'S';
				// Prüfe, ob die Folgehalbjahre M,M,M,M?,M? sind und passe diese an (Spezialfälle berücksichtigen KU+MU+RE)
				} else if ((hochschreibenIstWahl(props.fach, wahl.halbjahre, ['M', 'M', 'M', 'M', 'M', 'M']) || hochschreibenIstWahl(props.fach, wahl.halbjahre, ['M', 'M', 'M', 'M', null, null])) && !(ist_VTF.value || ist_PJK.value)) {
					if (GostFachbereich.SPORT.hat(props.fach))
						wahl.halbjahre = ["AT", "AT", "AT", "AT", "AT", "AT"];
					else
						wahl.halbjahre = [null, null, null, null, null, null];
				} else {
					if (GostFachbereich.SPORT.hat(props.fach))
						wahl.halbjahre[GostHalbjahr.EF1.id] = "AT";
					else
						wahl.halbjahre[GostHalbjahr.EF1.id] = null;
				}
				break;
			}
			case "AT": {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF1.id] = 'S';
				} else if (hochschreibenIstWahl(props.fach, wahl.halbjahre, ["AT", "AT", "AT", "AT", "AT", "AT"])) {
					wahl.halbjahre = [null, null, null, null, null, null];
				} else {
					wahl.halbjahre[GostHalbjahr.EF1.id] = null;
				}
				break;
			}
		}
	}


	function setEF2WahlHochschreiben(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.EF2.id]) {
			case null: {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF2.id] = 'S';
				} else if (hochschreibenIstWahlIgnoreFirstAndSecond(props.fach, wahl.halbjahre, [null, null, null, null]) && !(ist_VTF.value || ist_PJK.value)) {
					if ((GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && !props.fach.istMoeglichAbiLK))
						wahl.halbjahre = [wahl.halbjahre[0], 'M', 'M', 'M', 'M', 'M'];
					else if ((GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && props.fach.istMoeglichAbiLK))
						wahl.halbjahre = [wahl.halbjahre[0], 'S', 'LK', 'LK', 'LK', 'LK'];
					else
						wahl.halbjahre = [
							wahl.halbjahre[0],
							props.fach.istMoeglichEF2 ? 'S' : null,
							props.fach.istMoeglichQ11 ? 'S' : null,
							props.fach.istMoeglichQ12 ? 'S' : null,
							props.fach.istMoeglichQ21 ? 'S' : null,
							props.fach.istMoeglichQ22 ? 'M' : null,
						];
				} else {
					if (ist_VTF.value || ist_PJK.value || (GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && !props.fach.istMoeglichAbiLK))
						wahl.halbjahre[GostHalbjahr.EF2.id] = "M";
					else
						wahl.halbjahre[GostHalbjahr.EF2.id] = "S";
				}
				break;
			}
			case "S": {
				if (wahl.abiturFach !== null)
					wahl.halbjahre[GostHalbjahr.EF2.id] = 'M';
				else if ((hochschreibenIstWahlIgnoreFirstAndSecond(props.fach, wahl.halbjahre, [null, null, null, null])
					|| hochschreibenIstWahlIgnoreFirstAndSecond(props.fach, wahl.halbjahre, ['S', 'S', 'S', 'M'])) && !(ist_VTF.value || ist_PJK.value))
					if (GostFachbereich.KUNST_MUSIK.hat(props.fach) || GostFachbereich.RELIGION.hat(props.fach))
						wahl.halbjahre = [wahl.halbjahre[0], 'M', 'M', 'M', null, null];
					else
						wahl.halbjahre = [
							wahl.halbjahre[0],
							props.fach.istMoeglichEF2 ? 'M' : null,
							props.fach.istMoeglichQ11 ? 'M' : null,
							props.fach.istMoeglichQ12 ? 'M' : null,
							props.fach.istMoeglichQ21 ? 'M' : null,
							props.fach.istMoeglichQ22 ? 'M' : null,
						];
				else if (hochschreibenIstWahlIgnoreFirstAndSecond(props.fach, wahl.halbjahre, ['LK', 'LK', 'LK', 'LK']) && (GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && props.fach.istMoeglichAbiLK))
					wahl.halbjahre = [wahl.halbjahre[0], 'M', 'M', 'M', 'M', 'M'];
				else
					wahl.halbjahre[GostHalbjahr.EF2.id] = "M";
				break;
			}
			case "M": {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF2.id] = 'S';
				} else if ((hochschreibenIstWahlIgnoreFirstAndSecond(props.fach, wahl.halbjahre, [null, null, null, null])
					|| hochschreibenIstWahlIgnoreFirstAndSecond(props.fach, wahl.halbjahre, ['M', 'M', 'M', 'M'])
					|| hochschreibenIstWahlIgnoreFirstAndSecond(props.fach, wahl.halbjahre, ['M', 'M', null, null])) && !(ist_VTF.value || ist_PJK.value)) {
					if (GostFachbereich.SPORT.hat(props.fach))
						wahl.halbjahre = [wahl.halbjahre[0], "AT", "AT", "AT", "AT", "AT"];
					else
						wahl.halbjahre = [wahl.halbjahre[0], null, null, null, null, null];
				} else {
					if (GostFachbereich.SPORT.hat(props.fach))
						wahl.halbjahre[GostHalbjahr.EF2.id] = "AT";
					else
						wahl.halbjahre[GostHalbjahr.EF2.id] = null;
				}
				break;
			}
			case "AT": {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF2.id] = 'S';
				} else if (hochschreibenIstWahl(props.fach, wahl.halbjahre, [wahl.halbjahre[0], "AT", "AT", "AT", "AT", "AT"])) {
					wahl.halbjahre = [wahl.halbjahre[0], null, null, null, null, null];
				} else {
					wahl.halbjahre[GostHalbjahr.EF2.id] = null;
				}
			}
		}
	}


	function hatSchuelerFachwahl(fachwahl : GostSchuelerFachwahl | null, halbjahr: GostHalbjahr) : boolean {
		if (fachwahl === null)
			return false;
		return fachwahl.halbjahre[halbjahr.id] !== null;
	}


	function setQ11Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.Q11.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q11.id] = (GostFachbereich.DEUTSCH.hat(props.fach) || GostFachbereich.MATHEMATIK.hat(props.fach)) ? "S" : "M";
				break;
			case "M":
				if (ist_VTF.value || ist_PJK.value || GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(props.fach))
					wahl.halbjahre[GostHalbjahr.Q11.id] = null;
				else if (GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && !props.fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q11.id] = null;
				else if (GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && props.fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q11.id] = "LK";
				else
					wahl.halbjahre[GostHalbjahr.Q11.id] = "S";
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
		if ((wahl.halbjahre[GostHalbjahr.Q11.id] === null) && GostFachbereich.SPORT.hat(props.fach))
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
						if (beginn === GostHalbjahr.Q11) {
							if (!hatSchuelerFachwahl(wahl, GostHalbjahr.EF2) && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.EF2)) {
								wahl.halbjahre[GostHalbjahr.Q11.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
							}
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(props.fach) && props.gostJahrgangsdaten.hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q11) {
							if (!hatSchuelerFachwahl(wahl, GostHalbjahr.EF2) && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.EF2)) {
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
		if ((wahl.halbjahre[GostHalbjahr.Q11.id] === null) || (wahl.halbjahre[GostHalbjahr.Q11.id] === "M"))
			wahl.abiturFach = null;
	}


	function setQ12Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.Q12.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q12.id] = "M";
				if (ist_PJK.value && (wahl.halbjahre[GostHalbjahr.Q11.id] === null) && props.fach.istMoeglichQ21) {
					wahl.halbjahre[GostHalbjahr.Q21.id] = "M";
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) && (props.gostJahrgangsdaten.hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q11) {
							if (!hatSchuelerFachwahl(wahl, GostHalbjahr.EF2) && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.EF2)) {
								wahl.halbjahre[GostHalbjahr.Q11.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
							}
						}
						if (beginn === GostHalbjahr.Q12) {
							if (!hatSchuelerFachwahl(wahl, GostHalbjahr.Q11) && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.Q11)) {
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
							}
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(props.fach) && props.gostJahrgangsdaten.hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q11) {
							if (!hatSchuelerFachwahl(wahl, GostHalbjahr.EF2) && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.EF2)) {
								wahl.halbjahre[GostHalbjahr.Q11.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
							}
						}
						if (beginn === GostHalbjahr.Q12) {
							if (!hatSchuelerFachwahl(wahl, GostHalbjahr.Q11) && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.Q11)) {
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
				if (ist_VTF.value || ist_PJK.value || GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(props.fach))
					wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				else if (GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && !props.fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				else if (GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && props.fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q12.id] = "LK";
				else
					wahl.halbjahre[GostHalbjahr.Q12.id] = "S";
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.Q12.id] = (wahl.halbjahre[GostHalbjahr.Q11.id] === "LK") ? "LK" : null;
				break;
			case "ZK": {
				const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach))
					? GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW ?? "")
					: GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE ?? "");
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
		if ((wahl.halbjahre[GostHalbjahr.Q12.id] === null) && GostFachbereich.SPORT.hat(props.fach))
			wahl.halbjahre[GostHalbjahr.Q12.id] = "AT";
		else if ((wahl.halbjahre[GostHalbjahr.Q12.id] === "AT") && GostFachbereich.SPORT.hat(props.fach))
			wahl.halbjahre[GostHalbjahr.Q12.id] = null;
		// Nachfolgende HJ ebenfalls setzen
		if ((wahl.halbjahre[GostHalbjahr.Q12.id] === null) && !ist_VTF.value) {
			wahl.halbjahre[GostHalbjahr.Q21.id] = null;
			wahl.halbjahre[GostHalbjahr.Q22.id] = null;
		}
		if ((wahl.halbjahre[GostHalbjahr.Q12.id] === null) || (wahl.halbjahre[GostHalbjahr.Q12.id] === "M"))
			wahl.abiturFach = null;
	}


	function setQ21Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.Q21.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q21.id] = "M";
				if (ist_PJK.value && (wahl.halbjahre[GostHalbjahr.Q12.id] === null) && props.fach.istMoeglichQ22) {
					wahl.halbjahre[GostHalbjahr.Q22.id] = "M";
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) && (props.gostJahrgangsdaten.hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q12) {
							if (!hatSchuelerFachwahl(wahl, GostHalbjahr.Q11) && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.Q11)) {
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
							}
						}
						if (beginn === GostHalbjahr.Q21) {
							if (!hatSchuelerFachwahl(wahl, GostHalbjahr.Q12) && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.Q12)) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK'
							}
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(props.fach) && props.gostJahrgangsdaten.hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q12) {
							if (!hatSchuelerFachwahl(wahl, GostHalbjahr.Q11) && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.Q11)) {
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
							}
						}
						if (beginn === GostHalbjahr.Q21) {
							if (!hatSchuelerFachwahl(wahl, GostHalbjahr.Q12) && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.Q12)) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK'
							}
						}
					}
				}
				break;
			case "M":
				if (ist_VTF.value || ist_PJK.value || GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(props.fach))
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				else if (GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && !props.fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				else if (GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && props.fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q21.id] = "LK";
				else
					wahl.halbjahre[GostHalbjahr.Q21.id] = "S";
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.Q21.id] = (wahl.halbjahre[GostHalbjahr.Q12.id] === "LK") ? "LK" : null;
				break;
			case "ZK": {
				const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach))
					? GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW ?? "")
					: GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE ?? "");
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
		if ((wahl.halbjahre[GostHalbjahr.Q21.id] === null) && GostFachbereich.SPORT.hat(props.fach))
			wahl.halbjahre[GostHalbjahr.Q21.id] = "AT";
		else if ((wahl.halbjahre[GostHalbjahr.Q21.id] === "AT") && GostFachbereich.SPORT.hat(props.fach))
			wahl.halbjahre[GostHalbjahr.Q21.id] = null;
		// Nachfolgende HJ ebenfalls setzen
		if ((wahl.halbjahre[GostHalbjahr.Q21.id] === null) && !ist_VTF.value)
			wahl.halbjahre[GostHalbjahr.Q22.id] = null;
		if ((wahl.halbjahre[GostHalbjahr.Q21.id] === null) || (wahl.halbjahre[GostHalbjahr.Q21.id] === "ZK"))
			wahl.abiturFach = null;
	}


	function setQ22Wahl(wahl: GostSchuelerFachwahl): void {
		switch (wahl.halbjahre[GostHalbjahr.Q22.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q22.id] = "M";
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach) && (props.gostJahrgangsdaten.hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q21) {
							if (!hatSchuelerFachwahl(wahl, GostHalbjahr.Q12) && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.Q12)) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK'
							}
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(props.fach) && props.gostJahrgangsdaten.hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q21) {
							if (!hatSchuelerFachwahl(wahl, GostHalbjahr.Q12) && !props.manager.hatDoppelbelegung(props.fach, GostHalbjahr.Q12)) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK'
							}
						}
					}
				}
				break;
			case "M":
				if (ist_VTF.value || ist_PJK.value || GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(props.fach))
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				else if (GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && !props.fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				else if (GostFachbereich.SPORT.hat(props.fach) && !props.fach.istMoeglichAbiGK && props.fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q22.id] = "LK";
				else
					wahl.halbjahre[GostHalbjahr.Q22.id] = "S";
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.Q22.id] = (wahl.halbjahre[GostHalbjahr.Q21.id] === "LK") ? "LK" : null;
				break;
			case "ZK": {
				const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(props.fach))
					? GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW ?? "")
					: GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE ?? "");
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
		if ((wahl.halbjahre[GostHalbjahr.Q22.id] === null) && GostFachbereich.SPORT.hat(props.fach))
			wahl.halbjahre[GostHalbjahr.Q22.id] = "AT";
		else if ((wahl.halbjahre[GostHalbjahr.Q22.id] === "AT") && GostFachbereich.SPORT.hat(props.fach))
			wahl.halbjahre[GostHalbjahr.Q22.id] = null;
		// Nachfolgende HJ ebenfalls setzen
		if ((wahl.halbjahre[GostHalbjahr.Q22.id] === null) || (wahl.halbjahre[GostHalbjahr.Q22.id] === "ZK"))
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
			beginn = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursSW ?? "");
		if (ge)
			beginn = GostHalbjahr.fromKuerzel(props.gostJahrgangsdaten.beginnZusatzkursGE ?? "");
		if (!beginn || (beginn === halbjahr) || (beginn.next() === halbjahr))
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
				wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "M") ? 4 : 3;
				break;
			case 4:
				wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "S") ? 3 : null;
				break;
			case 3:
				wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "M") ? 4 : null;
				break;
			default:
				wahl.abiturFach = null;
				break;
		}
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
		if ((!istMoeglich.value[halbjahr.id]) || istBewertet(halbjahr))
			return;
		const wahl = props.abiturdatenManager().getSchuelerFachwahl(props.fach.id);
		if (halbjahr === GostHalbjahr.EF1)
			setEF1WahlHochschreiben(wahl);
		else if (halbjahr === GostHalbjahr.EF2)
			setEF2WahlHochschreiben(wahl);
		else if (halbjahr === GostHalbjahr.Q11)
			setQ11Wahl(wahl);
		else if (halbjahr === GostHalbjahr.Q12)
			setQ12Wahl(wahl);
		else if (halbjahr === GostHalbjahr.Q21)
			setQ21Wahl(wahl);
		else if (halbjahr === GostHalbjahr.Q22)
			setQ22Wahl(wahl);
		await props.setWahl(props.fach.id, wahl);
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
