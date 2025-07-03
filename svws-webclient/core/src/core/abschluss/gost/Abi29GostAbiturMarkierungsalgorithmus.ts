import { JavaObject } from '../../../java/lang/JavaObject';
import { GostAbiturMarkierungsalgorithmusErgebnis } from '../../../core/abschluss/gost/GostAbiturMarkierungsalgorithmusErgebnis';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { AbiturdatenManager } from '../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegpruefung } from '../../../core/abschluss/gost/GostBelegpruefung';
import { UnsupportedOperationException } from '../../../java/lang/UnsupportedOperationException';

export class Abi29GostAbiturMarkierungsalgorithmus extends JavaObject {

	/**
	 * Das Ergebnis des Algorithmus
	 */
	private readonly ergebnis : GostAbiturMarkierungsalgorithmusErgebnis = new GostAbiturMarkierungsalgorithmusErgebnis();


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
	 * Führt eine automatische Markierung von Halbjahresbelegungen zur Verwendung in Block II
	 * von anrechenbaren Kursen für die Abiturberechnung durch. Vorraussetzung hierfür ist, dass
	 * alle anrechenbare Kurse ein gültige Note zugeordnet haben.
	 *
	 * @param manager           der Manager zur Verwaltung der Abiturdaten.
	 * @param belegpruefungen   die zuvor durchgeführten Belegprüfung der Laufbahnplanung
	 *
	 * @return das Ergebnis der Berechnung
	 */
	public static berechne(manager : AbiturdatenManager, belegpruefungen : List<GostBelegpruefung>) : GostAbiturMarkierungsalgorithmusErgebnis {
		const initialState : Abi29GostAbiturMarkierungsalgorithmus = new Abi29GostAbiturMarkierungsalgorithmus(manager, belegpruefungen);
		return initialState.ergebnis;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.Abi29GostAbiturMarkierungsalgorithmus';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.Abi29GostAbiturMarkierungsalgorithmus'].includes(name);
	}

	public static class = new Class<Abi29GostAbiturMarkierungsalgorithmus>('de.svws_nrw.core.abschluss.gost.Abi29GostAbiturMarkierungsalgorithmus');

}

export function cast_de_svws_nrw_core_abschluss_gost_Abi29GostAbiturMarkierungsalgorithmus(obj : unknown) : Abi29GostAbiturMarkierungsalgorithmus {
	return obj as Abi29GostAbiturMarkierungsalgorithmus;
}
