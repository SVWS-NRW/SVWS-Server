import { ArrayList } from '../../../java/util/ArrayList';
import { List } from '../../../java/util/List';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { SatInput } from '../../../core/adt/sat/SatInput';
import { SatOutput } from '../../../core/adt/sat/SatOutput';
import { System } from '../../../java/lang/System';
import { SatSolver, cast_de_svws_nrw_core_adt_sat_SatSolver } from '../../../core/adt/sat/SatSolver';

export class SatSolverSimple extends SatSolver {

	private _backtrackingState : Array<boolean> = Array(0).fill(false);

	private _backtrackingLiteral : Array<number> = Array(0).fill(0);

	private _solution : Array<number> = Array(0).fill(0);

	private _backtrackingIndex : number = 0;

	private _backtrackingForward : boolean = true;

	private readonly _clauses : List<Array<number>> = new ArrayList();


	/**
	 * Ein simpler SAT-Solver, der via Backtracking eine Lösung sucht. <br>
	 * Das Backtracking ist ohne Rekursion implementiert.
	 */
	public constructor() {
		super();
	}

	public apply(t : SatInput) : SatOutput {
		const timeEnd : number = System.currentTimeMillis() + this.maxTimeMillis;
		this.initialize(t);
		while (this._backtrackingIndex < this._backtrackingLiteral.length) {
			if (System.currentTimeMillis() > timeEnd)
				return new SatOutput();
			if (this._backtrackingForward) {
				if (this.isContradiction()) {
					this._backtrackingForward = false;
					this._backtrackingIndex--;
				} else {
					const lit : number = this.choose_literal();
					this.propagate(lit);
					this._backtrackingState[this._backtrackingIndex] = true;
					this._backtrackingForward = true;
					this._backtrackingIndex++;
				}
			} else {
				if (this._backtrackingState[this._backtrackingIndex]) {
					const lit : number = this._backtrackingLiteral[this._backtrackingIndex];
					this.propagate_undo(lit);
					this.propagate(-lit);
					this._backtrackingState[this._backtrackingIndex] = false;
					this._backtrackingForward = true;
				} else {
					this._backtrackingForward = false;
					const lit : number = this._backtrackingLiteral[this._backtrackingIndex];
					this._backtrackingState[this._backtrackingIndex] = false;
					this.propagate_undo(lit);
				}
			}
		}
		return new SatOutput(this._solution);
	}

	private propagate(lit : number) : void {
		if (lit >= 0) {
			this._solution[lit] = lit;
		} else {
			this._solution[-lit] = lit;
		}
	}

	private propagate_undo(lit : number) : void {
		if (lit >= 0) {
			this._solution[lit] = 0;
		} else {
			this._solution[-lit] = 0;
		}
	}

	private isContradiction() : boolean {
		for (const clause of this._clauses) {
			if (this.isEmpty(clause))
				return true;
		}
		return false;
	}

	private isEmpty(clause : Array<number>) : boolean {
		for (const literal of clause) {
			const abs : number = Math.abs(literal);
			const assignment : number = Math.abs(this._solution[abs]);
			if (assignment === literal)
				return false;
		}
		return true;
	}

	private choose_literal() : number {
		for (let literal : number = 1; literal < this._solution.length; literal++)
			if (literal === 0)
				return literal;
		throw new DeveloperNotificationException("Solver kann keine undefinierte Variable mehr finden!")
	}

	private initialize(t : SatInput) : void {
		const nVars : number = t.getVarCount();
		this._backtrackingState = Array(nVars).fill(false);
		this._backtrackingLiteral = Array(nVars).fill(0);
		this._solution = Array(nVars + 1).fill(0);
		this._backtrackingIndex = 0;
		this._backtrackingForward = true;
		this._clauses.clear();
		this._clauses.addAll(t.getClauses());
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.sat.SatSolverSimple', 'java.util.function.Function', 'de.svws_nrw.core.adt.sat.SatSolver'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_sat_SatSolverSimple(obj : unknown) : SatSolverSimple {
	return obj as SatSolverSimple;
}
