import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { SatSolverA, cast_de_nrw_schule_svws_core_kursblockung_satsolver_SatSolverA } from '../../../core/kursblockung/satsolver/SatSolverA';
import { LinkedCollection, cast_de_nrw_schule_svws_core_adt_collection_LinkedCollection } from '../../../core/adt/collection/LinkedCollection';
import { JavaIterator, cast_java_util_Iterator } from '../../../java/util/JavaIterator';
import { System, cast_java_lang_System } from '../../../java/lang/System';

export class SatSolverWrapper extends SatSolverA {

	private readonly _solver : SatSolverA;

	private readonly varFALSE : number;

	private readonly varTRUE : number;


	/**
	 * Erstellt eine Ebene über dem {@link SatSolverA}, um verschiedene Bedingungen/Constraints als Klauseln zu
	 * codieren.
	 * 
	 * @param solver Der Solver, der intern verwendet wird.
	 */
	public constructor(solver : SatSolverA) {
		super();
		this._solver = solver;
		this.varTRUE = this._solver.createNewVar();
		this.varFALSE = -this.varTRUE;
		this.c_1(this.varTRUE);
	}

	public createNewVar() : number {
		return this._solver.createNewVar();
	}

	public addClause(pVars : Array<number>) : void {
		this._solver.addClause(pVars);
	}

	public isVarTrue(pVar : number) : boolean {
		return this._solver.isVarTrue(pVar);
	}

	public solve(maxTimeMillis : number) : number {
		return this._solver.solve(maxTimeMillis);
	}

	public getVarCount() : number {
		return this._solver.getVarCount();
	}

	public getClauseCount() : number {
		return this._solver.getClauseCount();
	}

	/**
	 * Liefert ein Array der Länge n mit neu erzeugten Variablennummern.
	 * 
	 * @param n Die Länge des Arrays.
	 * 
	 * @return Ein Array der Länge n mit neu erzeugten Variablennummern.
	 */
	public createNewVars(n : number) : Array<number> {
		let temp : Array<number> | null = Array(n).fill(0);
		for (let i : number = 0; i < temp.length; i++){
			temp[i] = this._solver.createNewVar();
		}
		return temp;
	}

	/**
	 * Liefert eine Variable, die zuvor auf FALSE forciert wurde.
	 * 
	 * @return Eine Variable, die zuvor auf FALSE forciert wurde.
	 */
	public getVarFALSE() : number {
		return this.varFALSE;
	}

	/**
	 * Liefert eine Variable, die zuvor auf TRUE forciert wurde.
	 * 
	 * @return Eine Variable, die zuvor auf TRUE forciert wurde.
	 */
	public getVarTRUE() : number {
		return this.varTRUE;
	}

	/**
	 * Fügt eine Klausel der Größe 1 hinzu. Forciert damit die übergebene Variable auf TRUE.
	 * 
	 * @param x Die Variable wird auf TRUE gesetzt.
	 */
	public c_1(x : number) : void {
		this._solver.addClause([x]);
	}

	/**
	 * Fügt eine Klausel der Größe 2 hinzu. Forciert damit, dass mindestens eine der beiden Variablen TRUE ist,
	 * letzlich @code{x + y >= 1}.
	 * 
	 * @param x Die Variable x der Klausel (x OR y).
	 * @param y Die Variable y der Klausel (x OR y).
	 */
	public c_2(x : number, y : number) : void {
		this._solver.addClause([x, y]);
	}

	/**
	 * Fügt eine Klausel der Größe 3 hinzu. Forciert damit, dass mindestens eine der drei Variablen TRUE ist,
	 * letzlich @code{x + y + z >= 1}.
	 * 
	 * @param x Die Variable x der Klausel (x OR y OR z).
	 * @param y Die Variable y der Klausel (x OR y OR z).
	 * @param z Die Variable z der Klausel (x OR y OR z).
	 */
	public c_3(x : number, y : number, z : number) : void {
		this._solver.addClause([x, y, z]);
	}

	/**
	 * Forciert, dass nicht beide Variablen TRUE sind, letzlich @code{x + y ≤ 1}.
	 * 
	 * @param x Die Variable x der Klausel (-x OR -y).
	 * @param y Die Variable y der Klausel (-x OR -y).
	 */
	public c_not_both(x : number, y : number) : void {
		this.c_2(-x, -y);
	}

	/**
	 * Liefert die Variable z für die {@code z = x AND y} gilt.
	 * 
	 * @param x Variable der obigen Gleichung.
	 * @param y Variable der obigen Gleichung.
	 * 
	 * @return Die Variable z für die {@code z = x AND y} gilt.
	 */
	public c_new_var_AND(x : number, y : number) : number {
		let c : number = this._solver.createNewVar();
		this.c_2(x, -c);
		this.c_2(y, -c);
		this.c_3(-x, -y, c);
		return c;
	}

	/**
	 * Forciert, dass genau {@code amount} Variablen des Arrays den Wert TRUE haben.
	 * 
	 * @param pArray Das Variablenarray.
	 * @param amount Die Anzahl an TRUEs in der Variablenliste.
	 */
	public c_exactly_GENERIC(pArray : Array<number>, amount : number) : void;

	/**
	 * Forciert, dass genau {@code amount} Variablen der Variablenliste den Wert TRUE haben.
	 * 
	 * @param list   Die Variablenliste.
	 * @param amount Die Anzahl an TRUEs in der Variablenliste.
	 */
	public c_exactly_GENERIC(list : LinkedCollection<Number>, amount : number) : void;

	/**
	 * Implementation for method overloads of 'c_exactly_GENERIC'
	 */
	public c_exactly_GENERIC(__param0 : Array<number> | LinkedCollection<Number>, __param1 : number) : void {
		if (((typeof __param0 !== "undefined") && Array.isArray(__param0)) && ((typeof __param1 !== "undefined") && typeof __param1 === "number")) {
			let pArray : Array<number> = __param0;
			let amount : number = __param1 as number;
			this.c_exactly_GENERIC(SatSolverWrapper.toList(pArray), amount);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.adt.collection.LinkedCollection'))) || (__param0 === null)) && ((typeof __param1 !== "undefined") && typeof __param1 === "number")) {
			let list : LinkedCollection<Number> = cast_de_nrw_schule_svws_core_adt_collection_LinkedCollection(__param0);
			let amount : number = __param1 as number;
			list = new LinkedCollection(list);
			if (amount > list.size()) {
				console.log(JSON.stringify("FEHLER: c_exactly_GENERIC --> amount > list.size()"));
			}
			if (amount === 0) {
				for (let x of list) {
					this.c_1(-x);
				}
				return;
			}
			if (amount === list.size()) {
				for (let x of list) {
					this.c_1(+x);
				}
				return;
			}
			if (amount === 1) {
				if (list.size() === 1) {
					this.c_1(list.getFirst().valueOf());
					return;
				}
				if (list.size() === 2) {
					this.c_unequal(list.getFirst().valueOf(), list.getLast().valueOf());
					return;
				}
				this.c_exactly_one(list);
				return;
			}
			this.c_exactly_NETWORK(list, amount);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Forciert, dass höchstens {@code maximum} Variablen der Variablenliste den Wert TRUE haben.
	 * 
	 * @param list    Die Variablenliste.
	 * @param maximum Die maximale Anzahl an TRUEs in der Variablenliste.
	 */
	public c_at_most_GENERIC(list : LinkedCollection<Number>, maximum : number) : void {
		list = new LinkedCollection(list);
		if (maximum >= list.size()) {
			return;
		}
		if (maximum === 0) {
			for (let x of list) {
				this.c_1(-x);
			}
			return;
		}
		if (maximum === 1) {
			this.c_at_most_one_tree(list);
			return;
		}
		this.c_at_most_NETWORK(list, maximum);
	}

	/**
	 * Forciert, dass genau eine Variable der Liste TRUE ist. Falls die Liste leer ist, führt das zur direkten
	 * Unlösbarkeit der Formel.
	 * 
	 * @param list Genau eine der Variablen der Liste muss TRUE sein.
	 */
	private c_exactly_one(list : LinkedCollection<Number>) : void {
		this.c_1(this.c_at_most_one_tree(list));
	}

	/**
	 * Forciert, dass {@code z = x OR y} gilt.
	 * 
	 * @param x Variable der obigen Gleichung.
	 * @param y Variable der obigen Gleichung.
	 * @param z Variable der obigen Gleichung.
	 */
	private c_z_equals_x_or_y(x : number, y : number, z : number) : void {
		this.c_2(-x, z);
		this.c_2(-y, z);
		this.c_3(x, y, -z);
	}

	/**
	 * Liefert die Variable z für die {@code z = x OR y} gilt.
	 * 
	 * @param x Variable der obigen Gleichung.
	 * @param y Variable der obigen Gleichung.
	 * 
	 * @return Die Variable z für die {@code z = x OR y} gilt.
	 */
	private c_new_var_OR(x : number, y : number) : number {
		let z : number = this._solver.createNewVar();
		this.c_2(-x, z);
		this.c_2(-y, z);
		this.c_3(x, y, -z);
		return z;
	}

	/**
	 * Forciert, dass in der Liste maximal eine Variable TRUE ist. Die Ergebnisvariable ist eine OR-Verknüpfung aller
	 * Variablen der Liste.
	 * 
	 * @param list Forciert, dass maximal eine Variable der Liste TRUE ist.
	 * 
	 * @return Die Ergebnisvariable ist eine OR-Verknüpfung aller Variablen der Liste.
	 */
	private c_at_most_one_tree(list : LinkedCollection<Number>) : number {
		list = new LinkedCollection(list);
		if (list.isEmpty()) {
			list.add(this.varFALSE);
		}
		while (list.size() >= 2) {
			let a : number = list.removeFirst().valueOf();
			let b : number = list.removeFirst().valueOf();
			let c : number = this._solver.createNewVar();
			this.c_not_both(a, b);
			this.c_z_equals_x_or_y(a, b, c);
			list.add(c);
		}
		return list.removeFirst().valueOf();
	}

	private c_exactly_NETWORK(list : LinkedCollection<Number>, amount : number) : void {
		this.c_bitonic_sort(list);
		let i : number = 0;
		let iter : JavaIterator<Number> = list.iterator();
		while (iter.hasNext()) {
			let value : Number = iter.next();
			if (i < amount) {
				this.c_1(+value.valueOf());
			} else {
				this.c_1(-value.valueOf());
			}
			i++;
		}
	}

	private c_at_most_NETWORK(list : LinkedCollection<Number>, maximum : number) : void {
		this.c_bitonic_sort(list);
		let i : number = 0;
		let iter : JavaIterator<Number> = list.iterator();
		while (iter.hasNext()) {
			let value : Number = iter.next();
			if (i < maximum) 
				i++; else 
				this.c_1(-value.valueOf());
		}
	}

	private c_bitonic_sort(list : LinkedCollection<Number>) : void {
		this.c_fill_False_until_power_two(list);
		this.c_bitonic_sort_power_two(list);
	}

	private c_fill_False_until_power_two(list : LinkedCollection<Number>) : void {
		let size : number = 1;
		while (size < list.size()) {
			size *= 2;
		}
		while (list.size() < size) {
			list.addLast(this.varFALSE);
		}
	}

	private c_bitonic_sort_power_two(list : LinkedCollection<Number>) : void {
		for (let window : number = 2; window <= list.size(); window *= 2){
			this.c_bitonic_sort_spiral(list, window);
			for (let difference : number = Math.trunc(window / 2); difference >= 2; difference /= 2){
				this.c_bitonic_sort_difference(list, difference);
			}
		}
	}

	private c_bitonic_sort_spiral(list : LinkedCollection<Number>, size : number) : void {
		for (let i : number = 0; i < list.size(); i += size){
			for (let i1 : number = i, i2 : number = i + size - 1; i1 < i2; i1++, i2--){
				this.c_bitonic_comparator(list, i1, i2);
			}
		}
	}

	private c_bitonic_sort_difference(list : LinkedCollection<Number>, size : number) : void {
		let half : number = Math.trunc(size / 2);
		for (let i : number = 0; i < list.size(); i += size){
			for (let j : number = 0; j < half; j++){
				this.c_bitonic_comparator(list, i + j, i + j + half);
			}
		}
	}

	private c_bitonic_comparator(result : LinkedCollection<Number>, i1 : number, i2 : number) : void {
		if (i1 >= i2) {
			console.log(JSON.stringify("c_bitonic_comparator: " + i1 + "," + i2 + " <-- ERROR!!!"));
		}
		let a : number = result.get(i1).valueOf();
		let b : number = result.get(i2).valueOf();
		result.set(i1, this.c_new_var_OR(a, b));
		result.set(i2, this.c_new_var_AND(a, b));
	}

	private static toList(pArray : Array<number>) : LinkedCollection<Number> {
		let list : LinkedCollection<Number> = new LinkedCollection();
		for (let x of pArray) {
			list.addLast(x);
		}
		return list;
	}

	private c_unequal(x : number, y : number) : void {
		this.c_equal(x, -y);
	}

	private c_equal(x : number, y : number) : void {
		this.c_2(-x, +y);
		this.c_2(+x, -y);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.satsolver.SatSolverA', 'de.nrw.schule.svws.core.kursblockung.satsolver.SatSolverWrapper'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_satsolver_SatSolverWrapper(obj : unknown) : SatSolverWrapper {
	return obj as SatSolverWrapper;
}
