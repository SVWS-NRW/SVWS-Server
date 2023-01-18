import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';

export abstract class SatSolverA extends JavaObject {

	/**
	 *  Ergebnis der Methode {@link #solve}, wenn die Formel unlösbar ist.
	 */
	public static readonly RESULT_UNSATISFIABLE : number = -1;

	/**
	 *  Ergebnis der Methode {@link #solve}, wenn die Lösbarkeit der Formel in der vorgegebenen Zeit nicht bestimmt
	 *  werden konnte.
	 */
	public static readonly RESULT_UNKNOWN : number = 0;

	/**
	 *  Ergebnis der Methode {@link #solve}, wenn die Formel gelöst wurde ist.
	 */
	public static readonly RESULT_SATISFIABLE : number = 1;


	public constructor() {
		super();
	}

	/**
	 * Erzeugte eine neue Variable. Den zurückgegebenen Integer-Wert darf man nun in Klauseln (auch negiert)
	 * benutzen. Eine Variable hat niemals den Wert 0, da dieser Wert nicht negiert werden kann.
	 * 
	 * @return Die Nummer der neuen Variablen.
	 */
	public abstract createNewVar() : number;

	/**
	 * Hinzufügen einer Klausel. Eine Klausel ist eine Menge von Variablen, die mit einem logischen ODER verknüpft
	 * sind. Die Variablen dürfen negiert sein.<br>
	 * {@code Beispiel: [-3, 8, 2] bedeutet (NOT x3) OR x8 OR x2}<br>
	 * Die Menge aller Klauseln sind mit einem AND verknüpft.
	 * 
	 * @param pVars Die Variablen (auch negiert) der Klausel mit den zuvor generierten Variablen. Ist das Array
	 *              leer, wird es ignoriert.
	 */
	public abstract addClause(pVars : Array<number>) : void;

	/**
	 * Gibt den Wert TRUE oder FALSE der angefragten Variable zurück. Das Verhalten dieser Methode ergibt nur dann
	 * Sinn, wenn der Solver mit SATISFIABLE nach Aufruf der Methode {@link #solve } geantwortet hat.
	 * 
	 * @param pVar Die angefragte Variable (kann auch negativ sein, aber nicht 0).
	 * @return TRUE, falls die angefragte Variable nach der Berechnnung TRUE ist.
	 */
	public abstract isVarTrue(pVar : number) : boolean;

	/**
	 * Startet die Berechnung für maximal {@code maxTimeMillis} Millisekunden und liefert einen der drei möglichen
	 * Werte {@link #RESULT_SATISFIABLE}, {@link #RESULT_UNKNOWN} oder {@link #RESULT_UNSATISFIABLE}.
	 * 
	 * @param maxTimeMillis Die maximale Berechnungszeit (in Millisekunden).
	 * 
	 * @return Liefert einen der drei möglichen Werte {@link #RESULT_SATISFIABLE}, {@link #RESULT_UNKNOWN} oder
	 *         {@link #RESULT_UNSATISFIABLE}.
	 */
	public abstract solve(maxTimeMillis : number) : number;

	/**
	 * Liefert die interne Anzahl an erzeugten Variablen.
	 * 
	 * @return Die interne Anzahl an erzeugten Variablen.
	 */
	public abstract getVarCount() : number;

	/**
	 * Liefert die interne Anzahl an erzeugten Klauseln.
	 * 
	 * @return Die interne Anzahl an erzeugten Klauseln.
	 */
	public abstract getClauseCount() : number;

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.satsolver.SatSolverA'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_satsolver_SatSolverA(obj : unknown) : SatSolverA {
	return obj as SatSolverA;
}
