import { AVLSet } from '../../../core/adt/set/AVLSet';
import { Variable, cast_de_svws_nrw_core_kursblockung_satsolver_Variable } from '../../../core/kursblockung/satsolver/Variable';
import { StringBuilder } from '../../../java/lang/StringBuilder';
import { LinkedCollection } from '../../../core/adt/collection/LinkedCollection';
import { Heap } from '../../../core/kursblockung/satsolver/Heap';
import { System } from '../../../java/lang/System';
import { Random } from '../../../java/util/Random';
import { SatSolverA } from '../../../core/kursblockung/satsolver/SatSolverA';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { Clause } from '../../../core/kursblockung/satsolver/Clause';
import { Arrays } from '../../../java/util/Arrays';

export class SatSolver3 extends SatSolverA {

	private static readonly MAX_LEARNED_CLAUSE_SIZE : number = 3;

	/**
	 *  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	protected readonly _random : Random;

	/**
	 *  Im Heap werden Variablen gespeichert. Die oberste Variable wird als nächstes gewählt.
	 */
	private readonly heap : Heap;

	/**
	 *  Die Anzahl an Variablen in den Arrays {@link #vArrayPos und #vArrayNeg}.
	 */
	private vSize : number = 0;

	/**
	 *  Alle positiven Variablen (bzw. Literale).
	 */
	private vArrayPos : Array<Variable>;

	/**
	 *  Alle negativen Variablen (bzw. Literale).
	 */
	private vArrayNeg : Array<Variable>;

	/**
	 *  Die Anzahl an eingefügten Klauseln.
	 */
	private cSize : number = 0;

	private learnClauseMin : number = 0;


	/**
	 * Konstruktor.
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	public constructor(pRandom : Random) {
		super();
		this._random = pRandom;
		this.heap = new Heap(pRandom);
		this.vSize = 0;
		this.cSize = 0;
		this.vArrayPos = Array(1).fill(null);
		this.vArrayNeg = Array(1).fill(null);
	}

	public isVarTrue(pVar : number) : boolean {
		const v : Variable = this.getVarOf(pVar);
		return v.index === -1;
	}

	public createNewVar() : number {
		this.vSize++;
		if (this.vSize >= this.vArrayPos.length) {
			const newSize : number = this.vSize * 2;
			this.vArrayPos = Arrays.copyOf(this.vArrayPos, newSize);
			this.vArrayNeg = Arrays.copyOf(this.vArrayNeg, newSize);
		}
		const vPos : Variable = new Variable(this._random, +this.vSize);
		const vNeg : Variable = new Variable(this._random, -this.vSize);
		vPos.negation = vNeg;
		vNeg.negation = vPos;
		this.vArrayPos[this.vSize] = vPos;
		this.vArrayNeg[this.vSize] = vNeg;
		this.heap.insert(vPos);
		this.heap.insert(vNeg);
		return this.vSize;
	}

	public addClause(pVars : Array<number>) : void {
		if (pVars.length === 0) {
			console.log(JSON.stringify("WARNUNG: Leere Klausel bei SatSolver.addClause(int[] pVars)!"));
			return;
		}
		const set : AVLSet<number> = new AVLSet();
		for (const v of pVars) {
			if (set.contains(-v)) {
				return;
			}
			set.add(v);
		}
		const list : LinkedCollection<number> = new LinkedCollection();
		while (!set.isEmpty()) {
			list.addLast(set.pollFirst());
		}
		while (list.size() > 3) {
			const x : number = list.removeFirst().valueOf();
			const y : number = list.removeFirst().valueOf();
			const z : number = this.createNewVar();
			list.addLast(z);
			this.addClause2(-x, z);
			this.addClause2(-y, z);
			this.addClause3(x, y, -z);
		}
		if (list.size() === 3) {
			this.addClause3(list.removeFirst()!, list.removeFirst()!, list.removeFirst()!);
		}
		if (list.size() === 2) {
			this.addClause2(list.removeFirst()!, list.removeFirst()!);
		}
		if (list.size() === 1) {
			this.addClause1(list.removeFirst()!);
		}
	}

	/**
	 * Fügt der Datenstruktur eine 1-CNF-Klausel hinzu.
	 *
	 * @param x Das 1. Literal (Variablennummer) der Klausel.
	 */
	private addClause1(x : number) : void {
		const varX : Variable = this.getVarOf(x);
		if (varX.negation === null)
			throw new NullPointerException()
		this.heap.remove(varX);
		this.heap.remove(varX.negation);
		const c : Clause = new Clause(varX);
		this.cSize++;
		varX.clauses.addLast(c);
		varX.statSatFree[c.sat][c.free]++;
		this.heap.insert(varX);
		this.heap.insert(varX.negation);
	}

	/**
	 * Fügt der Datenstruktur eine 2-CNF-Klausel hinzu.
	 *
	 * @param x Das 1. Literal (Variablennummer) der Klausel.
	 * @param y Das 2. Literal (Variablennummer) der Klausel.
	 */
	private addClause2(x : number, y : number) : void {
		const varX : Variable = this.getVarOf(x);
		const varY : Variable = this.getVarOf(y);
		if ((varX.negation === null) || (varY.negation === null))
			throw new NullPointerException()
		this.heap.remove(varX);
		this.heap.remove(varY);
		this.heap.remove(varX.negation);
		this.heap.remove(varY.negation);
		const c : Clause = new Clause(varX, varY);
		this.cSize++;
		varX.clauses.addLast(c);
		varX.statSatFree[c.sat][c.free]++;
		varX.neighbours.addLast(varY);
		varY.clauses.addLast(c);
		varY.statSatFree[c.sat][c.free]++;
		varY.neighbours.addLast(varX);
		this.heap.insert(varX);
		this.heap.insert(varY);
		this.heap.insert(varX.negation);
		this.heap.insert(varY.negation);
	}

	/**
	 * Fügt der Datenstruktur eine 3-CNF-Klausel hinzu.
	 *
	 * @param x Das 1. Literal (Variablennummer) der Klausel.
	 * @param y Das 2. Literal (Variablennummer) der Klausel.
	 * @param z Das 3. Literal (Variablennummer) der Klausel.
	 */
	private addClause3(x : number, y : number, z : number) : void {
		const varX : Variable = this.getVarOf(x);
		const varY : Variable = this.getVarOf(y);
		const varZ : Variable = this.getVarOf(z);
		if ((varX.negation === null) || (varY.negation === null) || (varZ.negation === null))
			throw new NullPointerException()
		this.heap.remove(varX);
		this.heap.remove(varY);
		this.heap.remove(varZ);
		this.heap.remove(varX.negation);
		this.heap.remove(varY.negation);
		this.heap.remove(varZ.negation);
		const c : Clause = new Clause(varX, varY, varZ);
		this.cSize++;
		varX.clauses.addLast(c);
		varX.statSatFree[c.sat][c.free]++;
		varX.neighbours.addLast(varY);
		varX.neighbours.addLast(varZ);
		varY.clauses.addLast(c);
		varY.statSatFree[c.sat][c.free]++;
		varY.neighbours.addLast(varX);
		varY.neighbours.addLast(varZ);
		varZ.clauses.addLast(c);
		varZ.statSatFree[c.sat][c.free]++;
		varZ.neighbours.addLast(varX);
		varZ.neighbours.addLast(varY);
		this.heap.insert(varX);
		this.heap.insert(varY);
		this.heap.insert(varZ);
		this.heap.insert(varX.negation);
		this.heap.insert(varY.negation);
		this.heap.insert(varZ.negation);
	}

	public getVarCount() : number {
		return this.vSize;
	}

	public getClauseCount() : number {
		return this.cSize;
	}

	public solve(pMaxTimeMillis : number) : number {
		const timeStart : number = System.currentTimeMillis();
		const backtrackV : Array<Variable | null> = Array(this.vSize).fill(null);
		const backtrackLearn : Array<Variable> = Array(SatSolver3.MAX_LEARNED_CLAUSE_SIZE).fill(null);
		const backtrackB : Array<boolean> = Array(this.vSize).fill(false);
		let index : number = 0;
		let max : number = 1;
		let countDown : number = 0;
		this.learnClauseMin = SatSolver3.MAX_LEARNED_CLAUSE_SIZE + 1;
		let maxIndex : number = 0;
		while ((index >= 0) && !this.heap.isEmpty()) {
			if (index > maxIndex) {
				maxIndex = index;
			}
			if (countDown === 0) {
				for (let i : number = index; i >= 0; i--) {
					if (backtrackV[i] !== null) {
						const bvi : Variable | null = cast_de_svws_nrw_core_kursblockung_satsolver_Variable(backtrackV[i]);
						this.unitpropagation_undo(bvi);
					}
					backtrackV[i] = null;
					backtrackB[i] = false;
				}
				index = 0;
				if ((this.learnClauseMin >= 1) && (this.learnClauseMin <= SatSolver3.MAX_LEARNED_CLAUSE_SIZE)) {
					const clause : Array<number> = Array(this.learnClauseMin).fill(0);
					for (let i : number = 0; i < clause.length; i++) {
						clause[i] = -backtrackLearn[i].nr;
					}
					this.addClause(clause);
				}
				const result : number = this.simplify();
				if (result !== SatSolverA.RESULT_UNKNOWN) {
					return result;
				}
				max = max + 1 + Math.trunc(max / 2);
				countDown = max;
				this.learnClauseMin = SatSolver3.MAX_LEARNED_CLAUSE_SIZE + 1;
				if (System.currentTimeMillis() - timeStart > pMaxTimeMillis) {
					return SatSolverA.RESULT_UNKNOWN;
				}
				continue;
			}
			countDown--;
			let varP : Variable | null = backtrackV[index];
			if ((varP !== null) && (backtrackB[index])) {
				this.unitpropagation_undo(varP);
				backtrackV[index] = null;
				backtrackB[index] = false;
				this.learnClause(backtrackV, backtrackLearn, index);
				index--;
				continue;
			}
			if (varP === null) {
				varP = this.heap.top();
				if (!varP.isUnsat()) {
					backtrackV[index] = varP;
					this.unitpropagation(varP);
					index++;
					continue;
				}
			} else {
				this.unitpropagation_undo(varP);
			}
			if (varP.negation === null)
				throw new NullPointerException()
			varP = varP.negation;
			if (varP.isUnsat()) {
				backtrackV[index] = null;
				this.learnClause(backtrackV, backtrackLearn, index);
				index--;
				continue;
			}
			backtrackV[index] = varP;
			backtrackB[index] = true;
			this.unitpropagation(varP);
			index++;
		}
		return this.heap.isEmpty() ? SatSolverA.RESULT_SATISFIABLE : SatSolverA.RESULT_UNSATISFIABLE;
	}

	private learnClause(backtrackV : Array<Variable | null>, backtrackLearn : Array<Variable | null>, size : number) : void {
		if (size < 1) {
			return;
		}
		if (size >= this.learnClauseMin) {
			return;
		}
		this.learnClauseMin = size;
		System.arraycopy(backtrackV, 0, backtrackLearn, 0, size);
	}

	private simplify() : number {
		let count1 : number = 0;
		let changed : boolean = true;
		while (changed) {
			changed = false;
			for (let nr : number = 1; nr <= this.vSize; nr++) {
				const varP : Variable | null = this.vArrayPos[nr];
				if (varP.index < 0) {
					continue;
				}
				if (varP.statSatFree[0][0] > 0) {
					return SatSolverA.RESULT_UNSATISFIABLE;
				}
				if (varP.negation === null)
					throw new NullPointerException()
				if (varP.negation.statSatFree[0][0] > 0) {
					return SatSolverA.RESULT_UNSATISFIABLE;
				}
				if ((varP.statSatFree[0][1] > 0) && (varP.negation.statSatFree[0][1] > 0)) {
					return SatSolverA.RESULT_UNSATISFIABLE;
				}
				if (varP.statSatFree[0][1] > 0) {
					this.unitpropagation(varP);
					count1++;
					changed = true;
					continue;
				}
				if (varP.negation.statSatFree[0][1] > 0) {
					this.unitpropagation(varP.negation);
					count1++;
					changed = true;
					continue;
				}
				if (varP.getClauseOccurences() === 0) {
					this.unitpropagation(varP.negation);
					count1++;
					changed = true;
					continue;
				}
				if (varP.negation.getClauseOccurences() === 0) {
					this.unitpropagation(varP);
					count1++;
					changed = true;
				}
			}
		}
		return SatSolverA.RESULT_UNKNOWN;
	}

	private static fill(index : number) : string | null {
		let s : StringBuilder = new StringBuilder();
		for (let i : number = 0; i < index; i++)
			s.append("    ");
		return s.toString();
	}

	/**
	 * Setzt die Variable {@code varP} auf TRUE und aktualisiert die gesamte Datenstruktur entsprechend.
	 *
	 * @param varP Die Variable, die auf TRUE gesetzt wird.
	 */
	private unitpropagation(varP : Variable) : void {
		const varN : Variable | null = varP.negation;
		if (varN === null)
			throw new NullPointerException()
		this.heap.remove(varP);
		this.heap.remove(varN);
		varP.index = -1;
		varN.index = -2;
		this.unitpropagationHelper(varP.clauses, +1, -1);
		this.unitpropagationHelper(varN.clauses, +0, -1);
	}

	/**
	 * Setzt die Variable {@code varP} von TRUE auf FREI und aktualisiert die gesamte Datenstruktur entsprechend.
	 *
	 * @param varP Die Variable, die von TRUE auf FREI gesetzt wird.
	 */
	private unitpropagation_undo(varP : Variable) : void {
		const varN : Variable | null = varP.negation;
		if (varN === null)
			throw new NullPointerException()
		this.unitpropagationHelper(varP.clauses, -1, +1);
		this.unitpropagationHelper(varN.clauses, +0, +1);
		this.heap.insert(varP);
		this.heap.insert(varN);
	}

	/**
	 * Eine Hilsmethode, die alle Klauseln der Liste aktualisiert.
	 *
	 * @param clauses    Alle zu informmierenden Klausel.
	 * @param pDeltaSat  Die Veränderung der erfüllten Klauseln, relativ zum 1. Index im 2D-Array
	 *                   {@link Variable#statSatFree}.
	 * @param pDeltaFree Die Veränderung der freien Variablen, relativ zum 2. Index im 2D-Array
	 *                   {@link Variable#statSatFree}.
	 */
	private unitpropagationHelper(clauses : LinkedCollection<Clause>, pDeltaSat : number, pDeltaFree : number) : void {
		for (const c of clauses) {
			const sat1 : number = c.sat;
			const free1 : number = c.free;
			const sat2 : number = sat1 + pDeltaSat;
			const free2 : number = free1 + pDeltaFree;
			for (const v of c.variables) {
				v.statSatFree[sat1][free1]--;
				v.statSatFree[sat2][free2]++;
				this.heap.update(v);
			}
			c.sat = sat2;
			c.free = free2;
		}
	}

	/**
	 * Liefert das zugehörige Variablenobjekt.
	 *
	 * @param pNr Der Variablennummer.
	 *
	 * @return Das zugehörige Variablenobjekt.
	 */
	private getVarOf(pNr : number) : Variable {
		return (pNr > 0) ? this.vArrayPos[pNr] : this.vArrayNeg[-pNr];
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.satsolver.SatSolverA', 'de.svws_nrw.core.kursblockung.satsolver.SatSolver3'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_satsolver_SatSolver3(obj : unknown) : SatSolver3 {
	return obj as SatSolver3;
}
