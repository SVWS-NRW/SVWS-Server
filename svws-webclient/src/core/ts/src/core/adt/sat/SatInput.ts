import { JavaObject } from '../../../java/lang/JavaObject';
import { LinkedCollection, cast_de_svws_nrw_core_adt_collection_LinkedCollection } from '../../../core/adt/collection/LinkedCollection';
import { ArrayList } from '../../../java/util/ArrayList';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import type { List } from '../../../java/util/List';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';

export class SatInput extends JavaObject {

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
	private readonly _clauses : List<Array<number>>;


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

	public toString() : string {
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
	 * Liefert die Menge aller Klauseln.
	 *
	 * @return die Menge aller Klauseln.
	 */
	public getClauses() : List<Array<number>> {
		return this._clauses;
	}

	/**
	 * Liefert den Header des DIMACs Formats. Diese zeigt die Variablen- und Klauselanzahl.
	 *
	 * @return den Header des DIMACs Formats. Diese zeigt die Variablen- und Klauselanzahl.
	 */
	public getDimacsHeader() : string {
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
	 * Siehe auch: {@link SatInput#create_var()}
	 *
	 * @param n die Anzahl an zu erzeugenden Variablen.
	 *
	 * @return ein Array mit den neuen Variablen.
	 */
	public create_vars1D(n : number) : Array<number> {
		const temp : Array<number> = Array(n).fill(0);
		for (let i : number = 0; i < temp.length; i++)
			temp[i] = this.create_var();
		return temp;
	}

	/**
	 * Erzeugt mehrere Variablen auf einmal und liefert ein Array mit diesen zurück. <br>
	 * Siehe auch: {@link SatInput#create_var()}
	 *
	 * @param rows die Anzahl an Zeilen eines 2D-Arrays.
	 * @param cols die Anzahl an Spalten eines 2D-Arrays.
	 *
	 * @return ein Array mit den neuen Variablen.
	 */
	public create_vars2D(rows : number, cols : number) : Array<Array<number>> {
		const temp : Array<Array<number>> = [...Array(rows)].map(e => Array(cols).fill(0));
		for (let r : number = 0; r < rows; r++)
			temp[r] = this.create_vars1D(cols);
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
	 * Hinzufügen einer Klausel. Eine Klausel ist eine nicht leere Menge von Variablen,
	 * die mit einem logischen ODER verknüpft sind. Die Variablen dürfen negiert sein. <br>
	 * <pre>
	 * Das Array [-3, 8, 2]
	 * wird als  (NOT x3) OR x8 OR x2 interpretiert.
	 * </pre>
	 * Die Menge aller Klauseln sind mit einem AND verknüpft.
	 *
	 * @param pVars Die Variablen (auch negiert) der Klausel mit den zuvor generierten Variablen.
	 *
	 * @throws DeveloperNotificationException falls die angegebenen Variablen ungültig sind.
	 */
	public add_clause(pVars : Array<number>) : void {
		DeveloperNotificationException.ifTrue("Die Klausel darf nicht leer sein!", pVars.length === 0);
		for (const literal of pVars) {
			DeveloperNotificationException.ifTrue("Variable 0 ist nicht erlaubt!", literal === 0);
			const absL : number = Math.abs(literal);
			DeveloperNotificationException.ifTrue("Variable " + absL + " wurde vorher nicht erzeugt!", absL > this._nVars);
		}
		this._clauses.add(pVars);
	}

	/**
	 * Fügt eine Klausel hinzu. Falls die Variablen noch nicht existieren, werden sie erzeugt.
	 *
	 * @param pVars Die Variablen (auch negiert) der Klausel.
	 *
	 * @throws DeveloperNotificationException falls die Klausel leer ist, oder eine Variable 0 ist.
	 */
	public add_clause_and_variables(pVars : Array<number>) : void {
		DeveloperNotificationException.ifTrue("Die Klausel darf nicht leer sein!", pVars.length === 0);
		for (const literal of pVars) {
			DeveloperNotificationException.ifTrue("Variable 0 ist nicht erlaubt!", literal === 0);
			const absL : number = Math.abs(literal);
			this._nVars = Math.max(this._nVars, absL);
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
	 * Forciert, dass genau {@code pAmount} Variablen des Arrays den Wert TRUE haben.
	 *
	 * @param pArray  Das Array der Variablen.
	 * @param pAmount Die Anzahl an TRUEs in der Variablenliste.
	 */
	public add_clause_exactly(pArray : Array<number>, pAmount : number) : void;

	/**
	 * Forciert, dass genau {@code pAmount} Variablen der Variablenliste den Wert TRUE haben.
	 *
	 * @param pList   Die Variablenliste.
	 * @param pAmount Die Anzahl an TRUEs in der Variablenliste.
	 */
	public add_clause_exactly(pList : LinkedCollection<number>, pAmount : number) : void;

	/**
	 * Implementation for method overloads of 'add_clause_exactly'
	 */
	public add_clause_exactly(__param0 : Array<number> | LinkedCollection<number>, __param1 : number) : void {
		if (((typeof __param0 !== "undefined") && Array.isArray(__param0)) && ((typeof __param1 !== "undefined") && typeof __param1 === "number")) {
			const pArray : Array<number> = __param0;
			const pAmount : number = __param1 as number;
			const list : LinkedCollection<number> = new LinkedCollection();
			for (const x of pArray)
				list.addLast(x);
			this.add_clause_exactly(list, pAmount);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.adt.collection.LinkedCollection'))) || (__param0 === null)) && ((typeof __param1 !== "undefined") && typeof __param1 === "number")) {
			const pList : LinkedCollection<number> = cast_de_svws_nrw_core_adt_collection_LinkedCollection(__param0);
			const pAmount : number = __param1 as number;
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
		} else throw new Error('invalid method overload');
	}

	/**
	 * Forciert, dass genau {@code pAmount} Variablen der Matrix {@code pData} in Zeile {@code pRow} den Wert TRUE haben.
	 *
	 * @param pData   Die Matrix.
	 * @param pRow    Die Zeile der Matrix.
	 * @param pAmount Die Anzahl an TRUEs.
	 */
	public add_clause_exactly_in_row(pData : Array<Array<number>>, pRow : number, pAmount : number) : void {
		const pList : LinkedCollection<number> = new LinkedCollection();
		for (let c : number = 0; c < pData[pRow].length; c++)
			pList.add(pData[pRow][c]);
		this.add_clause_exactly(pList, pAmount);
	}

	/**
	 * Forciert, dass genau {@code pAmount} Variablen der Matrix {@code pData} in Spalte {@code pCol} den Wert TRUE haben.
	 *
	 * @param pData   Die Matrix.
	 * @param pCol    Die Spalte der Matrix.
	 * @param pAmount Die Anzahl an TRUEs.
	 */
	public add_clause_exactly_in_column(pData : Array<Array<number>>, pCol : number, pAmount : number) : void {
		const pList : LinkedCollection<number> = new LinkedCollection();
		for (let r : number = 0; r < pData.length; r++)
			pList.add(pData[r][pCol]);
		this.add_clause_exactly(pList, pAmount);
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

	/**
	 * Überprüft, ob die übergebene Lösung valide ist.
	 *
	 * @param solution Die übergebene Lösung.
	 * @return TRUE, falls die Lösung alle Klauseln des Inputs erfüllt.
	 */
	public isValidSolution(solution : Array<number>) : boolean {
		DeveloperNotificationException.ifTrue("Arraygröße " + solution.length + " passt nicht zur Variablenanzahl " + this._nVars + "!", solution.length !== this._nVars + 1);
		for (const clause of this._clauses) {
			let countTRUE : number = 0;
			for (const literal of clause) {
				const abs : number = Math.abs(literal);
				const assignment : number = solution[abs];
				DeveloperNotificationException.ifTrue("x_" + abs + " == 0", assignment === 0);
				if (assignment === literal)
					countTRUE++;
			}
			if (countTRUE === 0)
				return false;
		}
		return true;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.sat.SatInput'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_sat_SatInput(obj : unknown) : SatInput {
	return obj as SatInput;
}
