import { JavaObject } from '../../../java/lang/JavaObject';

export class SatOutput extends JavaObject {

	/**
	 * Es existiert eine Lösung.
	 */
	public static readonly TYPE_UNKNOWN : number = 0;

	/**
	 * Es existiert (beweisbar) keine Lösung existiert.
	 */
	public static readonly TYPE_SATISFIABLE : number = 1;

	/**
	 * Unbekannt, ob eine Lösung existiert (z. B. bei einem TimeOut).
	 */
	public static readonly TYPE_UNSATISFIABLE : number = 2;

	/**
	 *  Eine Lösung einer Formel {@link SatInput}. Das Format muss wie folgt aussehen:
	 *  <br><br>
	 *  Das Array muss um Eins größer sein, als die Anzahl der verwendeten Variablen.
	 *  An Position i steht der Wert i. Positiv, falls die Variable i TRUE ist, oder negativ,
	 *  falls die Variable i FALSE ist. Der 0-Index wird ignoriert.
	 *  <br><br>
	 *  Beispiel solution = {0, -1, 2, -3, -4} bedeutet x1=FALSE, x2=TRUE, x3=FALSE, x4=FALSE
	 */
	private readonly solution : Array<number>;

	private readonly type : number;


	/**
	 * Erzeugt eine Lösung anhand der übergebenen Parameter.
	 *
	 * @param pSolution Das Array der Variablen.
	 * @param pType     Einer der drei möglichen Typen.
	 */
	private constructor(pSolution : Array<number>, pType : number) {
		super();
		this.solution = pSolution;
		this.type = pType;
	}

	/**
	 * Getter für data;
	 *
	 * @return data
	 */
	public getSolution() : Array<number> {
		return this.solution;
	}

	/**
	 * Liefert TRUE, falls eine Lösung existiert.
	 *
	 * @return TRUE, falls eine Lösung existiert.
	 */
	public isSatisfiable() : boolean {
		return this.type === SatOutput.TYPE_SATISFIABLE;
	}

	/**
	 * Liefert TRUE, falls (beweisbar) keine Lösung existiert.
	 *
	 * @return TRUE, falls (beweisbar) keine Lösung existiert.
	 */
	public isUnsatisfiable() : boolean {
		return this.type === SatOutput.TYPE_UNSATISFIABLE;
	}

	/**
	 * Liefert TRUE, falls unbekannt ist, ob eine Lösung existiert (z. B. bei einem TimeOut).
	 *
	 * @return TRUE, falls unbekannt ist, ob eine Lösung existiert (z. B. bei einem TimeOut).
	 */
	public isUnknown() : boolean {
		return this.type === SatOutput.TYPE_UNKNOWN;
	}

	/**
	 * Liefert ein Objekt dieser Klasse mit dem Typ TYPE_UNKNOWN  (z. B. bei einem TimeOut).
	 *
	 * @return ein Objekt dieser Klasse mit dem Typ TYPE_UNKNOWN  (z. B. bei einem TimeOut).
	 */
	public static createUNKNOWN() : SatOutput {
		return new SatOutput(Array(0).fill(0), SatOutput.TYPE_UNKNOWN);
	}

	/**
	 * Liefert ein Objekt dieser Klasse mit dem Typ TYPE_UNSATISFIABLE.
	 *
	 * @return ein Objekt dieser Klasse mit dem Typ TYPE_UNSATISFIABLE
	 */
	public static createUNSATISFIABLE() : SatOutput {
		return new SatOutput(Array(0).fill(0), SatOutput.TYPE_UNSATISFIABLE);
	}

	/**
	 * Liefert ein Objekt dieser Klasse mit dem Typ TYPE_SATISFIABLE.
	 *
	 * @param pSolution Die Lösung der Variablenbelegungen.
	 * @return ein Objekt dieser Klasse mit dem Typ TYPE_SATISFIABLE.
	 */
	public static createSATISFIABLE(pSolution : Array<number>) : SatOutput {
		return new SatOutput(pSolution, SatOutput.TYPE_SATISFIABLE);
	}

	/**
	 * Liefert eine Kopie, welche aber potentiell eine andere Lösung besitzt.
	 *
	 * @param pOutput   Das zu kopierende Objekt.
	 * @param pSolution Die Lösung der Variablenbelegungen.
	 * @return eine Kopie, welche aber potentiell eine andere Lösung besitzt.
	 */
	public static createCopy(pOutput : SatOutput, pSolution : Array<number>) : SatOutput {
		return new SatOutput(pSolution, pOutput.type);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.sat.SatOutput'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_sat_SatOutput(obj : unknown) : SatOutput {
	return obj as SatOutput;
}
