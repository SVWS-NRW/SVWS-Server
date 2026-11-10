<template>
	<ui-table-grid name="Eingabe der Notenpunkte in der Qualifikationsphase" :manager="() => gridManager" hide-selection>
		<template #header>
			<th>Kürzel</th>
			<th class="text-left"> Fach </th>
			<th>Kursart</th>
			<th v-for="hj in GostHalbjahr.getQualifikationsphase()" :key="hj.id" :class="{ 'ui-divider': (hj === GostHalbjahr.Q22) }">{{ hj.kuerzel }}</th>
		</template>
		<template #default="{ row: fach, index }">
			<td class="text-uistatic" :style="{ 'background-color': getFachfarbe(fach) }">{{ fach.kuerzelAnzeige }}</td>
			<td class="text-uistatic text-left" :style="{ 'background-color': getFachfarbe(fach) }">{{ fach.bezeichnung }}</td>
			<td class="text-uistatic ui-divider" :style="{ 'background-color': getFachfarbe(fach) }">{{ getFachbelegung(fach)?.letzteKursart ?? "" }}</td>
			<template v-for="hj in GostHalbjahr.getQualifikationsphase()" :key="hj.id">
				<td v-if="!istEditierbar(fach, hj)" :class="getCSSClasses(fach, hj)">{{ getNotenpunkteString(fach, hj) }}</td>
				<td v-else :ref="inputNotenpunkte(fach, index, hj)" class="bg-ui-neutral ui-table-grid-input" :class="getCSSClasses(fach, hj)" />
			</template>
		</template>
	</ui-table-grid>
</template>

<script setup lang="ts">

	import type { ComponentPublicInstance } from "vue";
	import { computed, watchEffect } from "vue";

	import { RGBFarbe } from "@core/asd/data/RGBFarbe";
	import { Fach } from "@core/asd/types/fach/Fach";
	import { Note } from "@core/asd/types/Note";
	import type { Abiturdaten } from "@core/core/data/gost/Abiturdaten";
	import type { AbiturFachbelegung } from "@core/core/data/gost/AbiturFachbelegung";
	import type { AbiturFachbelegungHalbjahr } from "@core/core/data/gost/AbiturFachbelegungHalbjahr";
	import type { GostFach } from "@core/core/data/gost/GostFach";
	import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
	import type { GostFaecherManager } from "@core/core/utils/gost/GostFaecherManager";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";

	const props = defineProps<{
		eingabe: () => Abiturdaten;
		faecher: () => GostFaecherManager;
		istGesperrt: (idFach: number, halbjahr: GostHalbjahr) => boolean;
		setNote: (idFach: number, halbjahr: GostHalbjahr, notenpunkte: number | null) => void;
	}>();

	/**
	 * Bestimmt die Fachbelegung zu dem übergebenen Fach
	 *
	 * @param fach   das Fach
	 *
	 * @returns die Abiturfachbelegung oder null, wenn keine existiert
	 */
	function getFachbelegung(fach: GostFach): AbiturFachbelegung | null {
		for (const belegung of props.eingabe().fachbelegungen) {
			if (belegung.fachID === fach.id) {
				return belegung;
			}
		}
		return null;
	}

	/**
	 * Bestimmt die Halbjahresbelegung zu dem übergebenen Fach in dem übergebenen Halbjahr
	 *
	 * @param fach   das Fach
	 * @param hj     das Halbjahr
	 *
	 * @returns die Halbjahresbelegung oder null, wenn keine existiert
	 */
	function getBelegung(fach: GostFach, hj: GostHalbjahr): AbiturFachbelegungHalbjahr | null {
		return getFachbelegung(fach)?.belegungen[hj.id] ?? null;
	}


	// Der Grid-Manager für die Noteneingabe
	const gridManager = new GridManager<string, GostFach, List<GostFach>>({
		daten: computed<List<GostFach>>(() => {
			const result = new ArrayList<GostFach>();
			for (const fach of props.faecher().faecher()) {
				if (GostHalbjahr.getQualifikationsphase().some(hj => getBelegung(fach, hj) !== null)) {
					result.add(fach);
				}
			}
			return result;
		}),
		getRowKey: fach => "" + fach.id,
		columns: [
			{ kuerzel: "Kürzel", name: "Fachkürzel", width: "4rem", hideable: false },
			{ kuerzel: "Fach", name: "Fach", width: "16rem", hideable: false },
			{ kuerzel: "Kursart", name: "Kursart", width: "4rem", hideable: false },
			{ kuerzel: "Q1.1", name: "Notenpunkte in der Q1.1", width: "4rem", hideable: false },
			{ kuerzel: "Q1.2", name: "Notenpunkte in der Q1.2", width: "4rem", hideable: false },
			{ kuerzel: "Q2.1", name: "Notenpunkte in der Q2.1", width: "4rem", hideable: false },
			{ kuerzel: "Q2.2", name: "Notenpunkte in der Q2.2", width: "4rem", hideable: false },
			{ kuerzel: "", name: "", width: "1.25rem", hideable: false },
		],
	});

	// Das Schuljahr, in welchem die Abiturprüfung stattfindet
	const schuljahr = computed<number>(() => props.eingabe().schuljahrAbitur);

	/**
	 * Konvertiert das Noten-Kürzel in Notenpunkte, sofern es sich um "normale" Noten handelt.
	 *
	 * @param kuerzel   das Noten-Kürzel oder null
	 *
	 * @returns die Notenpunkte oder null
	 */
	function getNotenpunkteFromKuerzel(kuerzel: string | null): number | null {
		return Note.fromKuerzel(kuerzel).daten(schuljahr.value)?.notenpunkte ?? null;
	}

	/**
	 * Bestimmt die Notenpunkte für das übergebene Fach in dem übergebenen Halbjahr.
	 *
	 * @param fach   das Fach
	 * @param hj     das Halbjahr
	 *
	 * @returns die Notenpunkte oder null
	 */
	function getNotenpunkte(fach: GostFach, hj: GostHalbjahr): number | null {
		return getNotenpunkteFromKuerzel(getBelegung(fach, hj)?.notenkuerzel ?? null);
	}

	/**
	 * Bestimmt die Notenpunkte für das übergebene Fach in dem übergebenen Halbjahr als zweistelligen String
	 *
	 * @param fach   das Fach
	 * @param hj     das Halbjahr
	 *
	 * @returns die Notenpunkte, die Pseudonote oder einen leeren String
	 */
	function getNotenpunkteString(fach: GostFach, hj: GostHalbjahr): string {
		const belegung = getBelegung(fach, hj);
		if (belegung === null) {
			return "";
		}
		const np = getNotenpunkte(fach, hj);
		return (np === null) ? (belegung.notenkuerzel ?? "") : (((np < 10) ? "0" : "") + np);
	}

	/**
	 * Gibt zurück, ob die Zelle für das übergebene Fach in dem übergebenen Halbjahr editierbar ist oder nicht.
	 *
	 * @param fach   das Fach
	 * @param hj     das Halbjahr
	 *
	 * @returns true, wenn die Zelle editierbar ist, und ansonsten false
	 */
	function istEditierbar(fach: GostFach, hj: GostHalbjahr): boolean {
		return (getBelegung(fach, hj) !== null) && !props.istGesperrt(fach.id, hj);
	}

	/**
	 * Gibt die CSS-Klassen der Zelle für das übergebene Fach in dem übergebenen Halbjahr zurück
	 *
	 * @param fach   das Fach
	 * @param hj     das Halbjahr
	 *
	 * @returns die CSS-Klassen
	 */
	function getCSSClasses(fach: GostFach, hj: GostHalbjahr) {
		const np = getNotenpunkte(fach, hj);
		return {
			'ui-divider': (hj === GostHalbjahr.Q22),
			'font-bold text-ui-danger': (np !== null) && (np < 5),
			'underline': getBelegung(fach, hj)?.schriftlich === true,
		};
	}

	/**
	 * Bestimmt die Hintergrundfarbe für das übergebene Fach
	 *
	 * @param fach   das Fach
	 *
	 * @returns der String mit der RGB-Farbdefinition
	 */
	function getFachfarbe(fach: GostFach): string {
		const f = Fach.getBySchluesselOrDefault(fach.kuerzel);
		const gruppe = f.getFachgruppe(schuljahr.value);
		const farbe: RGBFarbe = (gruppe === null) ? new RGBFarbe() : gruppe.getFarbe(schuljahr.value);
		return "rgb(" + farbe.red + "," + farbe.green + "," + farbe.blue + ")";
	}


	// Der Input für die Notenpunkte
	function inputNotenpunkte(fach: GostFach, index: number, hj: GostHalbjahr) {
		const key = 'Note_' + fach.id + '_' + hj.id;
		const setter = (value: string | null) => {
			const np = getNotenpunkteFromKuerzel(value);
			if (np !== getNotenpunkte(fach, hj)) {
				props.setNote(fach.id, hj, np);
			}
		};
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputAbiturNotenpunkte(key, hj.id, index, element, setter, schuljahr.value);
			if (input !== null) {
				watchEffect(() => gridManager.update(key, getBelegung(fach, hj)?.notenkuerzel ?? null));
			}
		};
	}

</script>
