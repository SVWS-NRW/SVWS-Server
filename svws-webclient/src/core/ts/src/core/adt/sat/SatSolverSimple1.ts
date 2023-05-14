import { ArrayList } from '../../../java/util/ArrayList';
import { List } from '../../../java/util/List';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { SatInput } from '../../../core/adt/sat/SatInput';
import { SatOutput } from '../../../core/adt/sat/SatOutput';
import { System } from '../../../java/lang/System';
import { SatSolver, cast_de_svws_nrw_core_adt_sat_SatSolver } from '../../../core/adt/sat/SatSolver';

export class SatSolverSimple1 extends SatSolver {

	/**
	 * Eine Kopie aller Klauseln.
	 */
	private readonly _clauses : List<Array<number>> = new ArrayList();

	/**
	 * Das Array, welches jeder Variablen (1-indiziert) seine Lösung zuordnet.
	 */
	private _solution : Array<number> = Array(0).fill(0);


	/**
	 * Ein simpler SAT-Solver, der via Backtracking eine Lösung sucht. <br>
	 * Das Backtracking ist ohne Rekursion implementiert.
	 */
	public constructor() {
		super();
	}

	public apply(t : SatInput) : SatOutput {
		const timeEnd : number = System.currentTimeMillis() + this.maxTimeMillis;
		const nVars : number = t.getVarCount();
		DeveloperNotificationException.ifSmaller("nVars", nVars, 1);
		this._clauses.clear();
		this._clauses.addAll(t.getClauses());
		this._solution = Array(nVars + 1).fill(0);
		let i : number = 1;
		while (System.currentTimeMillis() <= timeEnd) {
			if (this.conflict()) {
				while (true) {
					i--;
					if (i < 1)
						return SatOutput.createUNSATISFIABLE();
					if (this._solution[i] !== -i)
						break;
					this._solution[i] = 0;
				}
			}
			if (i >= this._solution.length)
				return SatOutput.createSATISFIABLE(this._solution);
			if (this._solution[i] === 0) {
				this._solution[i] = i;
			} else {
				this._solution[i] = -i;
			}
			i++;
		}
		return SatOutput.createUNKNOWN();
	}

	private conflict() : boolean {
		for (const clause of this._clauses)
			if (this.isEmpty(clause))
				return true;
		return false;
	}

	private isEmpty(clause : Array<number>) : boolean {
		for (const literal of clause) {
			const abs : number = Math.abs(literal);
			const assignment : number = this._solution[abs];
			if ((assignment === literal) || (assignment === 0))
				return false;
		}
		return true;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.sat.SatSolverSimple1', 'java.util.function.Function', 'de.svws_nrw.core.adt.sat.SatSolver'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_sat_SatSolverSimple1(obj : unknown) : SatSolverSimple1 {
	return obj as SatSolverSimple1;
}
