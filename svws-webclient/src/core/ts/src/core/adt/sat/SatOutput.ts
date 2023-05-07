import { JavaObject } from '../../../java/lang/JavaObject';

export class SatOutput extends JavaObject {

	/**
	 *  Eine Lösung einer Formel {@link SatInput}. Das Format muss wie folgt aussehen:
	 *  <br><br>
	 *  Das Array muss um Eins größer sein, als die Anzahl der verwendeten Variablen.
	 *  An Position i steht der Wert i. Positiv, falls die Variable i TRUE ist, oder negativ,
	 *  falls die Variable i FALSE ist. Der 0-Index wird ignoriert.
	 *  <br><br>
	 *  Beispiel solution = {0, -1, 2, -3, -4} bedeutet x1=FALSE, x2=TRUE, x3=FALSE, x4=FALSE
	 */
	private solution : Array<number>;


	/**
	 * Konstruktor.
	 */
	public constructor();

	/**
	 * Konstruktor für den Fall, dass eine Lösung gefunden wurde.
	 *
	 *
	 *
	 * @param pSolution Die Lösung der Variablenbelegungen.
	 */
	public constructor(pSolution : Array<number>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : Array<number>) {
		super();
		if ((typeof __param0 === "undefined")) {
			this.solution = Array(0).fill(0);
		} else if (((typeof __param0 !== "undefined") && Array.isArray(__param0))) {
			const pSolution : Array<number> = __param0;
			this.solution = pSolution;
		} else throw new Error('invalid method overload');
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
	 * Setzt die Anzahl an Variablen.
	 * @param pSize Die Anzahl an Variablen.
	 */
	public setSolutionSize(pSize : number) : void {
		this.solution = Array(pSize + 1).fill(0);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.sat.SatOutput'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_sat_SatOutput(obj : unknown) : SatOutput {
	return obj as SatOutput;
}
