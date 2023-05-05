import { JavaObject } from '../../../java/lang/JavaObject';
import { LinkedCollection } from '../../../core/adt/collection/LinkedCollection';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaIterator } from '../../../java/util/JavaIterator';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';

export class SatFormula extends JavaObject {

	/**
	 * Die aktuelle Anzahl an Variablen.
	 */
	private _nVars : number = 0;

	/**
	 * Eine Variable, die mit Hilfe einer Klausel auf TRUE forciert wird und somit eine Konstante ist.
	 */
	private _varTRUE : number = 0;

	/**
	 * Eine Variable, die mit Hilfe einer Klausel auf FALSE forciert wird und somit eine Konstante ist.
	 */
	private _varFALSE : number = 0;

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
		this._varTRUE = 0;
		this._varFALSE = 0;
	}

	public toString() : string | null {
		return this.getDimacsHeader();
	}

	/**
	 * Liefert eine Variable, die zuvor auf TRUE forciert wurde.
	 *
	 * @return Eine Variable, die zuvor auf TRUE forciert wurde.
	 */
	public getVarTRUE() : number {
		if (this._varTRUE === 0) {
			this._varTRUE = this.create_var();
			this.add_clause_1(+this._varTRUE);
		}
		return this._varTRUE;
	}

	/**
	 * Liefert eine Variable, die zuvor auf FALSE forciert wurde.
	 *
	 * @return Eine Variable, die zuvor auf FALSE forciert wurde.
	 */
	public getVarFALSE() : number {
		if (this._varFALSE === 0) {
			this._varFALSE = this.create_var();
			this.add_clause_1(-this._varFALSE);
		}
		return this._varFALSE;
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

	/**
	 * Erzeugte eine neue Variable. Den zurückgegebenen Integer-Wert darf man nun in Klauseln (auch negiert)
	 * benutzen. Eine Variable hat niemals den Wert 0, da dieser Wert nicht negiert werden kann. Ebenso darf
	 * eine Variable nicht 0 sein, da im DIMACS CNF FORMAT das Symbol 0 zum Kodieren eines Zeilenendes benutzt wird.
	 *
	 * @return Die Nummer der neuen Variablen.
	 */
	public create_var() : number {
		this._nVars++;
		return this._nVars;
	}

	/**
	 * Erzeugt mehrere Variablen auf einmal und liefert ein Array mit diesen zurück. <br>
	 * Siehe auch: {@link SatFormula#create_var()}
	 *
	 * @param n die Anzahl an zu erzeugenden Variablen.
	 *
	 * @return ein Array mit den neuen Variablen.
	 */
	public create_vars(n : number) : Array<number> {
		const temp : Array<number> | null = Array(n).fill(0);
		for (let i : number = 0; i < temp.length; i++)
			temp[i] = this.create_var();
		return temp;
	}

	/**
	 * Liefert die neu erzeugte Variable z für die 'z = x AND y' gilt.
	 *
	 * @param x Variable der obigen Gleichung.
	 * @param y Variable der obigen Gleichung.
	 *
	 * @return die neu erzeugte Variable z für die 'z = x AND y' gilt.
	 */
	public create_var_AND(x : number, y : number) : number {
		const c : number = this.create_var();
		this.add_clause_2(x, -c);
		this.add_clause_2(y, -c);
		this.add_clause_3(-x, -y, c);
		return c;
	}

	/**
	 * Liefert die neu erzeugte Variable z für die 'z = x OR y' gilt.
	 *
	 * @param x Variable der obigen Gleichung.
	 * @param y Variable der obigen Gleichung.
	 *
	 * @return die neu erzeugte Variable z für die 'z = x OR y' gilt.
	 */
	public create_var_OR(x : number, y : number) : number {
		const z : number = this.create_var();
		this.add_clause_2(-x, z);
		this.add_clause_2(-y, z);
		this.add_clause_3(x, y, -z);
		return z;
	}

	/**
	 * Forciert, dass in der Liste maximal eine Variable TRUE ist.
	 * Die Ergebnisvariable ist eine OR-Verknüpfung aller Variablen der Liste.
	 *
	 * @param pList Forciert, dass maximal eine Variable der Liste TRUE ist.
	 *
	 * @return Die Ergebnisvariable ist eine OR-Verknüpfung aller Variablen der Liste.
	 */
	public create_var_at_most_one_tree(pList : LinkedCollection<number>) : number {
		if (pList.isEmpty())
			return this.getVarFALSE();
		const list : LinkedCollection<number> = new LinkedCollection(pList);
		while (list.size() >= 2) {
			const a : number = list.removeFirst().valueOf();
			const b : number = list.removeFirst().valueOf();
			this.add_clause_not_both(a, b);
			const c : number = this.create_var_OR(a, b);
			list.addLast(c);
		}
		return list.removeFirst()!;
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
	 * @param pVars Die Variablen (auch negiert) der Klausel mit den zuvor generierten Variablen.
	 *              Ist das Array leer, passiert nichts.
	 *
	 * @throws DeveloperNotificationException falls die angegebenen Variablen ungültig sind.
	 */
	public add_clause(pVars : Array<number>) : void {
		if (pVars.length === 0)
			return;
		for (const v of pVars) {
			DeveloperNotificationException.ifTrue("Variable 0 ist nicht erlaubt!", v === 0);
			const absV : number = Math.abs(v);
			DeveloperNotificationException.ifTrue("Variable " + absV + " wurde vorher nicht erzeugt!", absV > this._nVars);
		}
		this._clauses.add(pVars);
	}

	/**
	 * Fügt eine Klausel der Größe 1 hinzu. Forciert damit die übergebene Variable auf TRUE.
	 *
	 * @param x Die Variable wird auf TRUE gesetzt.
	 */
	public add_clause_1(x : number) : void {
		this.add_clause([x]);
	}

	/**
	 * Fügt eine Klausel der Größe 2 hinzu. Forciert damit, dass mindestens eine der beiden Variablen TRUE ist.
	 *
	 * @param x Die Variable x der Klausel (x OR y).
	 * @param y Die Variable y der Klausel (x OR y).
	 */
	public add_clause_2(x : number, y : number) : void {
		this.add_clause([x, y]);
	}

	/**
	 * Fügt eine Klausel der Größe 3 hinzu. Forciert damit, dass mindestens eine der drei Variablen TRUE ist.
	 *
	 * @param x Die Variable x der Klausel (x OR y OR z).
	 * @param y Die Variable y der Klausel (x OR y OR z).
	 * @param z Die Variable z der Klausel (x OR y OR z).
	 */
	public add_clause_3(x : number, y : number, z : number) : void {
		this.add_clause([x, y, z]);
	}

	/**
	 * Forciert, dass nicht beide Variablen TRUE sind.
	 *
	 * @param x Die Variable x der Klausel (-x OR -y).
	 * @param y Die Variable y der Klausel (-x OR -y).
	 */
	public add_clause_not_both(x : number, y : number) : void {
		this.add_clause_2(-x, -y);
	}

	/**
	 * Forciert, dass beide Variablen gleich sind.
	 *
	 * @param x Die Variable x der Bedingung (x = y).
	 * @param y Die Variable y der Bedingung (x = y).
	 */
	public add_clause_equal(x : number, y : number) : void {
		this.add_clause_2(-x, +y);
		this.add_clause_2(+x, -y);
	}

	/**
	 * Forciert, dass beide Variablen ungleich sind.
	 *
	 * @param x Die Variable x der Bedingung (x != y).
	 * @param y Die Variable y der Bedingung (x != y).
	 */
	public add_clause_unequal(x : number, y : number) : void {
		this.add_clause_equal(x, -y);
	}

	/**
	 * Forciert, dass genau {@code amount} Variablen der Variablenliste den Wert TRUE haben.
	 *
	 * @param pList   Die Variablenliste.
	 * @param pAmount Die Anzahl an TRUEs in der Variablenliste.
	 */
	public add_clause_exactly(pList : LinkedCollection<number>, pAmount : number) : void {
		const list : LinkedCollection<number> = new LinkedCollection(pList);
		const size : number = list.size();
		DeveloperNotificationException.ifTrue("add_clause_exactly: " + pAmount + " > " + size, pAmount > size);
		if (pAmount === 0) {
			for (const x of list)
				this.add_clause_1(-x);
			return;
		}
		if (pAmount === size) {
			for (const x of list)
				this.add_clause_1(+x);
			return;
		}
		if (pAmount === 1) {
			this.add_clause_exactly_one(list);
			return;
		}
		this._bitonic_exactly(list, pAmount);
	}

	/**
	 * Forciert, dass genau eine Variable der Liste TRUE ist. <br>
	 * Falls die Liste leer ist, führt das zur direkten Unlösbarkeit der Formel.
	 *
	 * @param pList Menge an Variablen von denen genau eine TRUE sein soll.
	 */
	private add_clause_exactly_one(pList : LinkedCollection<number>) : void {
		const size : number = pList.size();
		if (size === 0) {
			this.add_clause_1(this.getVarFALSE());
			return;
		}
		if (size === 1) {
			this.add_clause_1(pList.getFirst()!);
			return;
		}
		if (size === 2) {
			this.add_clause_unequal(pList.getFirst()!, pList.getLast()!);
			return;
		}
		const x : number = this.create_var_at_most_one_tree(pList);
		this.add_clause_1(x);
	}

	private _bitonic_exactly(list : LinkedCollection<number>, amount : number) : void {
		this._bitonic_sort(list);
		let i : number = 0;
		const iter : JavaIterator<number> = list.iterator();
		while (iter.hasNext()) {
			const value : number = iter.next();
			if (i < amount) {
				this.add_clause_1(+value!);
			} else {
				this.add_clause_1(-value!);
			}
			i++;
		}
	}

	private _bitonic_sort(list : LinkedCollection<number>) : void {
		this._bitonic_fill_FALSE_until_power_two(list);
		this._bitonic_sort_power_two(list);
	}

	private _bitonic_fill_FALSE_until_power_two(list : LinkedCollection<number>) : void {
		let size : number = 1;
		while (size < list.size())
			size *= 2;
		while (list.size() < size)
			list.addLast(this.getVarFALSE());
	}

	private _bitonic_sort_power_two(list : LinkedCollection<number>) : void {
		for (let window : number = 2; window <= list.size(); window *= 2) {
			this._bitonic_sort_spiral(list, window);
			for (let difference : number = Math.trunc(window / 2); difference >= 2; difference /= 2)
				this._bitonic_sort_difference(list, difference);
		}
	}

	private _bitonic_sort_spiral(list : LinkedCollection<number>, size : number) : void {
		for (let i : number = 0; i < list.size(); i += size)
			for (let i1 : number = i, i2 : number = i + size - 1; i1 < i2; i1++, i2--)
				this._bitonic_comparator(list, i1, i2);
	}

	private _bitonic_sort_difference(list : LinkedCollection<number>, size : number) : void {
		const half : number = Math.trunc(size / 2);
		for (let i : number = 0; i < list.size(); i += size)
			for (let j : number = 0; j < half; j++)
				this._bitonic_comparator(list, i + j, i + j + half);
	}

	private _bitonic_comparator(result : LinkedCollection<number>, i1 : number, i2 : number) : void {
		DeveloperNotificationException.ifTrue("c_bitonic_comparator: i1=" + i1 + " nicht kleiner als i2=" + i2 + "!", i1 >= i2);
		const a : number = result.get(i1).valueOf();
		const b : number = result.get(i2).valueOf();
		result.set(i1, this.create_var_OR(a, b));
		result.set(i2, this.create_var_AND(a, b));
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.sat.SatFormula'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_sat_SatFormula(obj : unknown) : SatFormula {
	return obj as SatFormula;
}
