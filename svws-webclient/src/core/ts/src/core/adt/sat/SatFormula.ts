import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';

export class SatFormula extends JavaObject {

	/**
	 * Die aktuelle Anzahl an Variablen.
	 */
	private _nVars : number = 0;

	/**
	 * Die aktuelle Anzahl an Variablen.
	 */
	private readonly _clauses : ArrayList<Array<number> | null>;


	/**
	 * Erzeugt eine neues Objekt. Anschließend lassen sich Variablen erzeugen und Klauseln hinzufügen.
	 * Möchte man die Formel f = (x1 OR x2 OR NOT x3) AND (NOT x2 OR x3) AND (x5) kodieren, so funktioniert das so:  <br>
	 * <pre>
	 *     SatFormula f = new SatFormula();
	 *     int x1 = f.createNewVar();
	 *     int x2 = f.createNewVar();
	 *     int x3 = f.createNewVar();
	 *     f.createNewVar(); // not used
	 *     int x5 = f.createNewVar();
	 *
	 *     f.addClause(new int[] {x1, x2, -x3}); // adds {1, 2, -3}
	 *     f.addClause(new int[] {-x2, x3});     // adds {-2, 3}
	 *     f.addClause(new int[] {x5});          // adds {5}
	 * </pre>
	 */
	public constructor() {
		super();
		this._nVars = 0;
		this._clauses = new ArrayList();
	}

	public toString() : string | null {
		return this.getDimacsHeader();
	}

	/**
	 * Erzeugte eine neue Variable. Den zurückgegebenen Integer-Wert darf man nun in Klauseln (auch negiert)
	 * benutzen. Eine Variable hat niemals den Wert 0, da dieser Wert nicht negiert werden kann. Ebenso darf
	 * eine Variable nicht 0 sein, da im DIMACS CNF FORMAT das Symbol 0 zum Kodieren eines Zeilenendes benutzt wird.
	 *
	 * @return Die Nummer der neuen Variablen.
	 */
	public createNewVar() : number {
		this._nVars++;
		return this._nVars;
	}

	/**
	 * Hinzufügen einer Klausel. Eine Klausel ist eine Menge von Variablen, die mit einem logischen ODER verknüpft
	 * sind. Die Variablen dürfen negiert sein. <br>
	 * <pre>
	 * Das Array [-3, 8, 2]
	 * wird als  (NOT x3) OR x8 OR x2 interpretiert.
	 * </pre>
	 * Die Menge aller Klauseln sind mit einem AND verknüpft.
	 *
	 * @param pVars Die Variablen (auch negiert) der Klausel mit den zuvor generierten Variablen. Ist das Array
	 *              leer, wird es ignoriert.
	 * @throws DeveloperNotificationException falls die angegebenen Variablen ungültig sind.
	 */
	public addClause(pVars : Array<number>) : void {
		for (const v of pVars) {
			DeveloperNotificationException.check("Variable 0 ist nicht erlaubt!", v === 0);
			const absV : number = Math.abs(v);
			DeveloperNotificationException.check("Variable " + absV + " wurde vorher nicht erzeugt!", absV > this._nVars);
		}
		this._clauses.add(pVars);
	}

	/**
	 * Liefert die interne Anzahl an erzeugten Variablen.
	 *
	 * @return Die interne Anzahl an erzeugten Variablen.
	 */
	public getVarCount() : number {
		return this._nVars;
	}

	/**
	 * Liefert die interne Anzahl an erzeugten Klauseln.
	 *
	 * @return Die interne Anzahl an erzeugten Klauseln.
	 */
	public getClauseCount() : number {
		return this._clauses.size();
	}

	private getDimacsHeader() : string | null {
		return "p cnf " + this._nVars + " " + this._clauses.size();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.sat.SatFormula'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_sat_SatFormula(obj : unknown) : SatFormula {
	return obj as SatFormula;
}
