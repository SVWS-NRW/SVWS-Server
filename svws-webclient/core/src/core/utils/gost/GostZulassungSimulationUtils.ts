import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { HashMap } from '../../../java/util/HashMap';
import { GostFaecherManager } from '../../../core/utils/gost/GostFaecherManager';
import { AbiturFachbelegung } from '../../../core/data/gost/AbiturFachbelegung';
import { AbiturFachbelegungHalbjahr } from '../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { GostBesondereLernleistung } from '../../../core/types/gost/GostBesondereLernleistung';
import { GostBelegpruefungsArt } from '../../../core/abschluss/gost/GostBelegpruefungsArt';
import { AbiturdatenManager } from '../../../core/abschluss/gost/AbiturdatenManager';
import { Abiturdaten } from '../../../core/data/gost/Abiturdaten';
import { GostJahrgangsdaten } from '../../../core/data/gost/GostJahrgangsdaten';
import { GostZulassungSimulation } from '../../../core/data/gost/GostZulassungSimulation';
import { Note } from '../../../asd/types/Note';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { Class } from '../../../java/lang/Class';
import type { JavaMap } from '../../../java/util/JavaMap';

export class GostZulassungSimulationUtils extends JavaObject {


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed");
	}

	private static getMapFaecher(daten: Abiturdaten): JavaMap<number, AbiturFachbelegung> {
		const mapFaecher: JavaMap<number, AbiturFachbelegung> = new HashMap<number, AbiturFachbelegung>();
		for (const fach of daten.fachbelegungen) {
			mapFaecher.put(fach.fachID, fach);
		}
		return mapFaecher;
	}

	private static getKuerzel(notenpunkte: number | null, schuljahr: number): string | null {
		const note: Note = Note.fromNotenpunkte(notenpunkte);
		return (note as unknown === Note.KEINE as unknown) ? null : note.getNoteKuerzel(schuljahr);
	}

	/**
	 * Entfernt die leeren Belegungen, die der AbiturdatenManager beim Lesen der Fachwahlen für Halbjahre ohne Belegung anlegt.
	 * Sie würden sonst als Belegung gewertet.
	 *
	 * @param daten   die Abiturdaten
	 */
	private static entferneLeereBelegungen(daten: Abiturdaten): void {
		for (const fach of daten.fachbelegungen) {
			for (let i: number = 0; i < fach.belegungen.length; i++) {
				const belegung: AbiturFachbelegungHalbjahr | null = fach.belegungen[i];
				if ((belegung !== null) && JavaObject.equalsTranspiler("", (belegung.kursartKuerzel))) {
					fach.belegungen[i] = null;
				}
			}
		}
	}

	private static setzeBlock1Zurueck(daten: Abiturdaten): void {
		for (const fach of daten.fachbelegungen) {
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				const belegung: AbiturFachbelegungHalbjahr | null = fach.belegungen[halbjahr.id];
				if (belegung !== null) {
					belegung.block1gewertet = false;
				}
			}
		}
		daten.block1AnzahlKurse = null;
		daten.block1DefiziteGesamt = null;
		daten.block1DefiziteLK = null;
		daten.block1PunktSummeGK = null;
		daten.block1PunktSummeLK = null;
		daten.block1PunktSummeNormiert = null;
		daten.block1NotenpunkteDurchschnitt = null;
		daten.block1Zulassung = null;
	}

	/**
	 * Gibt zurück, ob die Note einer Halbjahresbelegung nicht durch eine Eingabe der Simulation verändert werden darf. Dies gilt immer
	 * für Vertiefungskurse, da sie keine Notenpunkte haben, und für Pseudonoten, damit diese erhalten bleiben. Andere vorhandene Noten
	 * sind nur gesperrt, wenn sie fixiert sind.
	 *
	 * @param faecher        der Manager für die Fächer des Abiturjahrgangs
	 * @param idFach         die ID des Faches
	 * @param belegung       die Halbjahresbelegung
	 * @param schuljahr      das Schuljahr, in welchem die Abiturprüfung stattfindet
	 * @param fixiereNoten   gibt an, ob vorhandene Noten Vorrang vor den Eingaben haben
	 *
	 * @return true, wenn die Note nicht verändert werden darf, und ansonsten false
	 */
	public static istEingabeGesperrt(faecher: GostFaecherManager, idFach: number, belegung: AbiturFachbelegungHalbjahr, schuljahr: number, fixiereNoten: boolean): boolean {
		if (faecher.fachIstVertiefungskurs(idFach)) {
			return true;
		}
		const note: Note = Note.fromKuerzel(belegung.notenkuerzel);
		return (note as unknown !== Note.KEINE as unknown) && (fixiereNoten || !note.istNote(schuljahr));
	}

	/**
	 * Wendet die Eingaben der Simulation auf die Abiturdaten an. Alle Halbjahre der Qualifikationsphase gelten danach als bewertet,
	 * damit die Zulassung berechnet wird. Einträge ohne passende Belegung, auf gesperrten Noten und Noten außerhalb des Katalogs bleiben
	 * unberücksichtigt.
	 * Vorhandene Prüfungsdaten werden zurückgesetzt: Die besondere Lernleistung wird immer durch die Eingabe ersetzt, ebenso alle
	 * Prüfungswerte eines Faches, zu dem ein Eintrag vorliegt.
	 *
	 * @param daten          die Abiturdaten, welche verändert werden
	 * @param simulation     die Eingaben der Simulation
	 * @param faecher        der Manager für die Fächer des Abiturjahrgangs
	 * @param fixiereNoten   gibt an, ob vorhandene Noten Vorrang vor den Eingaben haben
	 */
	public static combine(daten: Abiturdaten, simulation: GostZulassungSimulation, faecher: GostFaecherManager, fixiereNoten: boolean): void {
		GostZulassungSimulationUtils.entferneLeereBelegungen(daten);
		const mapFaecher: JavaMap<number, AbiturFachbelegung> = GostZulassungSimulationUtils.getMapFaecher(daten);
		for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
			daten.bewertetesHalbjahr[halbjahr.id] = true;
		}
		for (const eintrag of simulation.noten) {
			const halbjahr: GostHalbjahr | null = GostHalbjahr.fromID(eintrag.halbjahr);
			const fach: AbiturFachbelegung | null = mapFaecher.get(eintrag.idFach);
			if ((halbjahr === null) || !halbjahr.istQualifikationsphase() || (fach === null)) {
				continue;
			}
			const belegung: AbiturFachbelegungHalbjahr | null = fach.belegungen[halbjahr.id];
			if ((belegung === null) || GostZulassungSimulationUtils.istEingabeGesperrt(faecher, fach.fachID, belegung, daten.schuljahrAbitur, fixiereNoten)) {
				continue;
			}
			const kuerzel: string | null = GostZulassungSimulationUtils.getKuerzel(eintrag.notenpunkte, daten.schuljahrAbitur);
			if ((kuerzel !== null) || (eintrag.notenpunkte === null)) {
				belegung.notenkuerzel = kuerzel;
			}
		}
		for (const eintrag of simulation.abiturfaecher) {
			const fach: AbiturFachbelegung | null = mapFaecher.get(eintrag.idFach);
			if ((fach === null) || (fach.abiturFach === null)) {
				continue;
			}
			fach.block2NotenKuerzelPruefung = GostZulassungSimulationUtils.getKuerzel(eintrag.pruefungNotenpunkte, daten.schuljahrAbitur);
			fach.block2MuendlichePruefungBestehen = eintrag.muendlichBestehen;
			fach.block2MuendlichePruefungFreiwillig = eintrag.muendlichFreiwillig;
			fach.block2MuendlichePruefungReihenfolge = eintrag.muendlichReihenfolge;
			fach.block2MuendlichePruefungNotenKuerzel = GostZulassungSimulationUtils.getKuerzel(eintrag.muendlichNotenpunkte, daten.schuljahrAbitur);
		}
		daten.besondereLernleistung = GostBesondereLernleistung.fromKuerzel(simulation.besondereLernleistung).kuerzel;
		daten.besondereLernleistungNotenKuerzel = GostZulassungSimulationUtils.getKuerzel(simulation.besondereLernleistungNotenpunkte, daten.schuljahrAbitur);
	}

	/**
	 * Berechnet Zulassung und Prüfungsergebnis für Abiturdaten, auf welche die Eingaben bereits angewendet wurden. Markierungen aus
	 * der Planung werden verworfen und neu bestimmt. Bilinguale Belegungen werden dabei zusammengefasst, daher werden die Abiturdaten
	 * vom Manager verändert.
	 *
	 * @param daten                      die Abiturdaten, welche verändert werden
	 * @param jahrgangsdaten             die Daten des Abiturjahrgangs
	 * @param faecher                    der Manager für die Fächer des Abiturjahrgangs
	 * @param refreshPflichtpruefungen   gibt an, ob die mündlichen Pflichtprüfungen neu bestimmt werden. Dies überschreibt die Eingaben dazu.
	 *
	 * @return der Manager mit dem Ergebnis
	 */
	public static berechne(daten: Abiturdaten, jahrgangsdaten: GostJahrgangsdaten | null, faecher: GostFaecherManager, refreshPflichtpruefungen: boolean): AbiturdatenManager {
		GostZulassungSimulationUtils.setzeBlock1Zurueck(daten);
		const manager: AbiturdatenManager = new AbiturdatenManager(daten, jahrgangsdaten, faecher, GostBelegpruefungsArt.GESAMT);
		manager.kombiniereFachbelegungenEinesFaches();
		manager.applyErgebnisMarkierungsalgorithmus();
		AbiturdatenManager.berechnePruefungsergebnis(daten, refreshPflichtpruefungen);
		return manager;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.GostZulassungSimulationUtils';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostZulassungSimulationUtils'].includes(name);
	}

	public static readonly class = new Class<GostZulassungSimulationUtils>('de.svws_nrw.core.utils.gost.GostZulassungSimulationUtils');

}

export function cast_de_svws_nrw_core_utils_gost_GostZulassungSimulationUtils(obj: unknown): GostZulassungSimulationUtils {
	return obj as GostZulassungSimulationUtils;
}
