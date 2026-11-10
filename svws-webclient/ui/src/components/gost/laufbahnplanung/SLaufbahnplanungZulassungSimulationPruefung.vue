<template>
	<div class="flex flex-col gap-4">
		<div class="flex flex-row gap-4">
			<div class="w-64">
				<ui-select label="Besondere Lernleistung" :manager="managerBLL" :model-value="artBLL" :removable="false" :nullable="false" :searchable="false" @update:model-value="setArtBLL" />
			</div>
			<div v-if="hatEingabeNotenpunkteBLL" class="w-40">
				<ui-select label="Notenpunkte" :manager="managerNotenpunkteBLL" :model-value="notenpunkteBLL" :searchable="false" @update:model-value="setNotenpunkteBLL" />
			</div>
		</div>
		<ui-table-grid name="Eingabe der Prüfungsergebnisse" :manager="() => gridManager" hide-selection>
			<template #header>
				<th>Abi</th>
				<th>Kürzel</th>
				<th class="ui-divider text-left"> Fach </th>
				<th>Punkte</th>
				<th class="ui-divider">Wert</th>
				<th>Pflicht</th>
				<th>Freiw.</th>
				<th>RF</th>
				<th class="ui-divider">Punkte</th>
				<th>Summe</th>
			</template>
			<template #default="{ row: belegung, index }">
				<td>{{ belegung.abiturFach }}.</td>
				<td class="text-uistatic" :style="{ 'background-color': getFachfarbe(belegung) }">{{ faecher().get(belegung.fachID)?.kuerzelAnzeige ?? "???" }}</td>
				<td class="text-uistatic text-left ui-divider" :style="{ 'background-color': getFachfarbe(belegung) }">{{ faecher().get(belegung.fachID)?.bezeichnung ?? "???" }}</td>
				<td :ref="inputPruefung(belegung, index)" class="bg-ui-neutral ui-table-grid-input" :class="{ 'font-bold text-ui-danger': istDefizit(belegung.block2NotenKuerzelPruefung) }" />
				<td class="ui-divider">{{ getErgebnis(belegung)?.block2PunkteZwischenstand ?? '' }}</td>
				<template v-if="(belegung.abiturFach ?? 0) < 4">
					<td :ref="inputPflicht(belegung, index)" class="bg-ui-neutral ui-table-grid-button">
						<span class="icon-sm align-middle" :class="(belegung.block2MuendlichePruefungBestehen === true) ? 'i-ri-checkbox-line' : 'i-ri-checkbox-blank-line'" />
					</td>
					<td :ref="inputFreiwillig(belegung, index)" class="bg-ui-neutral ui-table-grid-button">
						<span class="icon-sm align-middle" :class="(belegung.block2MuendlichePruefungFreiwillig === true) ? 'i-ri-checkbox-line' : 'i-ri-checkbox-blank-line'" />
					</td>
					<td :ref="inputReihenfolge(belegung, index)" class="bg-ui-neutral ui-table-grid-input" />
					<td :ref="inputMuendlich(belegung, index)" class="bg-ui-neutral ui-table-grid-input ui-divider" :class="{ 'font-bold text-ui-danger': istDefizit(belegung.block2MuendlichePruefungNotenKuerzel) }" />
				</template>
				<td v-else class="col-span-4 ui-divider" />
				<td>{{ getErgebnis(belegung)?.block2Punkte ?? '' }}</td>
			</template>
			<template #footer>
				<td class="col-span-3 ui-divider text-left font-bold">Summe Block II / Gesamt / Note</td>
				<td class="col-span-2 ui-divider font-bold">{{ ergebnis().block2PunktSumme }}</td>
				<td class="col-span-3 font-bold">{{ ergebnis().gesamtPunkte }}</td>
				<td class="ui-divider bg-ui-brand-secondary font-bold">{{ ergebnis().note }}</td>
				<td :class="{ 'text-ui-onsuccess bg-ui-success': ergebnis().pruefungBestanden === true, 'text-ui-ondanger bg-ui-danger': ergebnis().pruefungBestanden === false }">
					{{ (ergebnis().pruefungBestanden === true) ? 'Ja' : ((ergebnis().pruefungBestanden === false) ? 'Nein' : '???') }}
				</td>
			</template>
		</ui-table-grid>
	</div>
</template>

<script setup lang="ts">

	import type { ComponentPublicInstance } from "vue";
	import { computed, watchEffect } from "vue";

	import { RGBFarbe } from "@core/asd/data/RGBFarbe";
	import { Fach } from "@core/asd/types/fach/Fach";
	import { Note } from "@core/asd/types/Note";
	import { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
	import type { Abiturdaten } from "@core/core/data/gost/Abiturdaten";
	import type { AbiturFachbelegung } from "@core/core/data/gost/AbiturFachbelegung";
	import type { GostZulassungSimulationAbiturfach } from "@core/core/data/gost/GostZulassungSimulationAbiturfach";
	import { GostBesondereLernleistung } from "@core/core/types/gost/GostBesondereLernleistung";
	import type { GostFaecherManager } from "@core/core/utils/gost/GostFaecherManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";

	const props = defineProps<{
		eingabe: () => Abiturdaten;
		ergebnis: () => Abiturdaten;
		faecher: () => GostFaecherManager;
		setPruefung: (idFach: number, patch: Partial<GostZulassungSimulationAbiturfach>, refreshPflichtpruefungen: boolean) => void;
		setBesondereLernleistung: (art: string) => void;
		setBesondereLernleistungNotenpunkte: (notenpunkte: number | null) => void;
	}>();

	const gridManager = new GridManager<string, AbiturFachbelegung, AbiturFachbelegung[]>({
		daten: computed<AbiturFachbelegung[]>(() => props.eingabe().fachbelegungen.toArray(new Array<AbiturFachbelegung>())
			.filter(f => f.abiturFach !== null)
			.sort((a, b) => (a.abiturFach ?? 0) - (b.abiturFach ?? 0))),
		getRowKey: belegung => "" + belegung.abiturFach,
		columns: [
			{ kuerzel: "Abi", name: "Abiturfach", width: "4rem", hideable: false },
			{ kuerzel: "Kürzel", name: "Fachkürzel", width: "4rem", hideable: false },
			{ kuerzel: "Fach", name: "Fach", width: "16rem", hideable: false },
			{ kuerzel: "Punkte", name: "Notenpunkte der Abiturprüfung", width: "4rem", hideable: false },
			{ kuerzel: "Wert", name: "Wertung der Abiturprüfung", width: "4rem", hideable: false },
			{ kuerzel: "Pflicht", name: "mündliche Pflichtprüfung im 1.-3. Abiturfach", width: "4rem", hideable: false },
			{ kuerzel: "Freiw.", name: "mündliche freiwillige Prüfung im 1.-3. Abiturfach", width: "4rem", hideable: false },
			{ kuerzel: "RF", name: "Reihenfolge bei den mündlichen Prüfungen im 1.-3. Abiturfach", width: "4rem", hideable: false },
			{ kuerzel: "Punkte (mdl.)", name: "Notenpunkte der mündlichen Prüfung im 1.-3. Abiturfach", width: "4rem", hideable: false },
			{ kuerzel: "Summe", name: "Summe aus der Abiturprüfung und der mündlichen Prüfung", width: "4rem", hideable: false },
			{ kuerzel: "", name: "", width: "1.25rem", hideable: false },
		],
	});

	const schuljahr = computed<number>(() => props.eingabe().schuljahrAbitur);

	function getNotenpunkteFromKuerzel(kuerzel: string | null): number | null {
		return Note.fromKuerzel(kuerzel).daten(schuljahr.value)?.notenpunkte ?? null;
	}

	// Die Daten für das UiSelect zu der Art der Besonderen Lernleistung
	const artenBLL = [
		{ kuerzel: GostBesondereLernleistung.KEINE.kuerzel, label: "Keine" },
		{ kuerzel: GostBesondereLernleistung.PROJEKTKURS.kuerzel, label: "Projektkurs" },
		{ kuerzel: GostBesondereLernleistung.EXTERNE.kuerzel, label: "Extern" },
	];
	const artBLL = computed(() => artenBLL.find(art => art.kuerzel === (props.eingabe().besondereLernleistung ?? GostBesondereLernleistung.KEINE.kuerzel)) ?? artenBLL[0]);
	const managerBLL = new SelectManager({ options: artenBLL, optionDisplayText: art => art.label, selectionDisplayText: art => art.label, sort: (a, b) => artenBLL.indexOf(a) - artenBLL.indexOf(b) });

	/**
	 * Setzt die Art der Besonderen Lernleistung
	 *
	 * @param art   die Art
	 */
	function setArtBLL(art: { kuerzel: string } | null | undefined) {
		if ((art !== null) && (art !== undefined)) {
			props.setBesondereLernleistung(art.kuerzel);
		}
	}

	// Die Daten für das UiSelect zu den Notenpunkten der Besonderen Lernleistung
	const notenpunkteBLL = computed<number | null>(() => getNotenpunkteFromKuerzel(props.eingabe().besondereLernleistungNotenKuerzel));
	const managerNotenpunkteBLL = new SelectManager({ options: Array.from({ length: 16 }, (_, i) => 15 - i), sort: (a, b) => b - a });

	// Ab dem Abitur 2030 erfolgt die Noteneingabe zur besonderen Lernleistung über das 5. Abiturfach ...
	const hatBLL = computed<boolean>(() => GostBesondereLernleistung.fromKuerzel(props.eingabe().besondereLernleistung) !== GostBesondereLernleistung.KEINE);
	const hatEingabeNotenpunkteBLL = computed<boolean>(() => hatBLL.value && !AbiturdatenManager.istAbitur2030(props.eingabe().abiturjahr));

	/**
	 * Setzt die Notenpunkte für die Besondere Lernleistung.
	 *
	 * @param notenpunkte   die Notenpunkte
	 */
	function setNotenpunkteBLL(notenpunkte: number | null | undefined) {
		props.setBesondereLernleistungNotenpunkte(notenpunkte ?? null);
	}


	/**
	 * Bestimmt die Fachbelegung zu dem übergebenen Abiturfach aus dem ggf. neu erzeugten Klon der Abiturdaten.
	 *
	 * @param abiturFach   das abiturFach
	 *
	 * @returns die Abiturfachbelegung
	 */
	function getEingabe(abiturFach: number | null): AbiturFachbelegung | null {
		for (const belegung of props.eingabe().fachbelegungen) {
			if (belegung.abiturFach === abiturFach) {
				return belegung;
			}
		}
		return null;
	}


	/**
	 * Bestimmt die passende Abiturfachbelegung nach der Berechnung des Simulations-Ergebnisses
	 * zu der Fachbelegung vor der Berechnung.
	 *
	 * @param belegung   die Fachbelegung aus der Eingabe
	 *
	 * @returns die Fachbelegung vom Ergebnis
	 */
	function getErgebnis(belegung: AbiturFachbelegung): AbiturFachbelegung | null {
		for (const fach of props.ergebnis().fachbelegungen) {
			if (fach.abiturFach === belegung.abiturFach) {
				return fach;
			}
		}
		return null;
	}

	/**
	 * Prüft, ob es sich um ein Defizit handelt oder nicht.
	 *
	 * @param kuerzel   das Noten-Kürzel
	 *
	 * @returns true, wenn es sich um ein Defizit handelt.
	 */
	function istDefizit(kuerzel: string | null): boolean {
		const np = getNotenpunkteFromKuerzel(kuerzel);
		return (np !== null) && (np < 5);
	}


	/**
	 * Bestimmt die Hintergrundfarbe für das Fach der übergebenen Fachbelegung
	 *
	 * @param belegung   die Fachbelegung
	 *
	 * @returns der String mit der RGB-Farbdefinition
	 */
	function getFachfarbe(belegung: AbiturFachbelegung): string {
		const fach = props.faecher().get(belegung.fachID);
		const gruppe = (fach === null) ? null : Fach.getBySchluesselOrDefault(fach.kuerzel).getFachgruppe(schuljahr.value);
		const farbe: RGBFarbe = (gruppe === null) ? new RGBFarbe() : gruppe.getFarbe(schuljahr.value);
		return "rgb(" + farbe.red + "," + farbe.green + "," + farbe.blue + ")";
	}



	// Input für die Notenpunkte der Abiturprüfung
	function inputPruefung(belegung: AbiturFachbelegung, index: number) {
		const key = 'Pruefung_' + belegung.abiturFach;
		const abiturFach = belegung.abiturFach;
		const idFach = belegung.fachID;
		const setter = (value: string | null) => {
			const np = getNotenpunkteFromKuerzel(value);
			if (np !== getNotenpunkteFromKuerzel(getEingabe(abiturFach)?.block2NotenKuerzelPruefung ?? null)) {
				props.setPruefung(idFach, { pruefungNotenpunkte: np }, true);
			}
		};
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputAbiturNotenpunkte(key, 1, index, element, setter, schuljahr.value);
			if (input !== null) {
				watchEffect(() => gridManager.update(key, getEingabe(abiturFach)?.block2NotenKuerzelPruefung ?? null));
			}
		};
	}

	// Input für die Checkbox, ob es sich um eine Pflichtprüfung handelt
	function inputPflicht(belegung: AbiturFachbelegung, index: number) {
		const key = 'Pflicht_' + belegung.abiturFach;
		const abiturFach = belegung.abiturFach;
		const idFach = belegung.fachID;
		const setter = (value: boolean) => props.setPruefung(idFach, { muendlichBestehen: value }, false);
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, 2, index, element, setter);
			if (input !== null) {
				watchEffect(() => gridManager.update(key, getEingabe(abiturFach)?.block2MuendlichePruefungBestehen === true));
			}
		};
	}

	// Input für die Checkbox, ob es sich um eine freiwillige mündliche Prüfung handelt
	function inputFreiwillig(belegung: AbiturFachbelegung, index: number) {
		const key = 'Freiwillig_' + belegung.abiturFach;
		const abiturFach = belegung.abiturFach;
		const idFach = belegung.fachID;
		const setter = (value: boolean) => props.setPruefung(idFach, { muendlichFreiwillig: value }, false);
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, 3, index, element, setter);
			if (input !== null) {
				watchEffect(() => gridManager.update(key, getEingabe(abiturFach)?.block2MuendlichePruefungFreiwillig === true));
			}
		};
	}

	// Input für die Nummer der Reihenfolge der mündlichen Abiturprüfungen
	function inputReihenfolge(belegung: AbiturFachbelegung, index: number) {
		const key = 'Reihenfolge_' + belegung.abiturFach;
		const abiturFach = belegung.abiturFach;
		const idFach = belegung.fachID;
		const setter = (value: number | null) => {
			if (value !== (getEingabe(abiturFach)?.block2MuendlichePruefungReihenfolge ?? null)) {
				props.setPruefung(idFach, { muendlichReihenfolge: value }, false);
			}
		};
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputAbiturPruefungsreihenfolge(key, 4, index, element, setter);
			if (input !== null) {
				watchEffect(() => gridManager.update(key, getEingabe(abiturFach)?.block2MuendlichePruefungReihenfolge ?? null));
			}
		};
	}

	// Input für die Notenpunkte der mündlichen Abiturprüfung im 1.-3. Fach
	function inputMuendlich(belegung: AbiturFachbelegung, index: number) {
		const key = 'Muendlich_' + belegung.abiturFach;
		const abiturFach = belegung.abiturFach;
		const idFach = belegung.fachID;
		const setter = (value: string | null) => {
			const np = getNotenpunkteFromKuerzel(value);
			if (np !== getNotenpunkteFromKuerzel(getEingabe(abiturFach)?.block2MuendlichePruefungNotenKuerzel ?? null)) {
				props.setPruefung(idFach, { muendlichNotenpunkte: np }, false);
			}
		};
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputAbiturNotenpunkte(key, 5, index, element, setter, schuljahr.value);
			if (input !== null) {
				watchEffect(() => gridManager.update(key, getEingabe(abiturFach)?.block2MuendlichePruefungNotenKuerzel ?? null));
			}
		};
	}

</script>
