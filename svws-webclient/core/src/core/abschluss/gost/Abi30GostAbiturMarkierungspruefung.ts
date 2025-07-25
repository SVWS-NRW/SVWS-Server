import { JavaObject } from '../../../java/lang/JavaObject';
import { GostAbiturMarkierungspruefungErgebnis } from '../../../core/abschluss/gost/GostAbiturMarkierungspruefungErgebnis';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { AbiturdatenManager } from '../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegpruefung } from '../../../core/abschluss/gost/GostBelegpruefung';
import { UnsupportedOperationException } from '../../../java/lang/UnsupportedOperationException';

export class Abi30GostAbiturMarkierungspruefung extends JavaObject {

	/**
	 * Das Ergebnis der Prüfung
	 */
	private readonly ergebnis : GostAbiturMarkierungspruefungErgebnis = new GostAbiturMarkierungspruefungErgebnis();


	/**
	 * Erstellt eine neue Instanz des Markierungsalgorithmus unter Verwendung des übergebenen Abiturdaten-Manager und den zuvor
	 * durchgeführten Belegprüfungen.
	 *
	 * @param manager            der Abiturdaten-Manager
	 * @param belegpruefungen    die durchgeführten Belegprüfungen
	 */
	private constructor(manager : AbiturdatenManager, belegpruefungen : List<GostBelegpruefung>) {
		super();
		throw new UnsupportedOperationException()
	}

	/**
	 * Führt eine Prüfung der Markierung von Halbjahresbelegungen zur Verwendung in Block II
	 * von anrechenbaren Kursen für die Abiturberechnung durch. Vorraussetzung hierfür ist, dass
	 * alle anrechenbare Kurse ein gültige Note zugeordnet haben.
	 *
	 * @param manager           der Manager zur Verwaltung der Abiturdaten.
	 * @param belegpruefungen   die zuvor durchgeführten Belegprüfung der Laufbahnplanung
	 *
	 * @return das Ergebnis der Prüfung
	 */
	public static pruefe(manager : AbiturdatenManager, belegpruefungen : List<GostBelegpruefung>) : GostAbiturMarkierungspruefungErgebnis {
		const pruefung : Abi30GostAbiturMarkierungspruefung = new Abi30GostAbiturMarkierungspruefung(manager, belegpruefungen);
		return pruefung.ergebnis;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.Abi30GostAbiturMarkierungspruefung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.Abi30GostAbiturMarkierungspruefung'].includes(name);
	}

	public static class = new Class<Abi30GostAbiturMarkierungspruefung>('de.svws_nrw.core.abschluss.gost.Abi30GostAbiturMarkierungspruefung');

}

export function cast_de_svws_nrw_core_abschluss_gost_Abi30GostAbiturMarkierungspruefung(obj : unknown) : Abi30GostAbiturMarkierungspruefung {
	return obj as Abi30GostAbiturMarkierungspruefung;
}
