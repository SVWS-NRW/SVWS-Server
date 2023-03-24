import { JavaObject } from '../../../java/lang/JavaObject';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { AVLSet } from '../../../core/adt/set/AVLSet';
import { Comparable } from '../../../java/lang/Comparable';
import { Variable, cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable } from '../../../core/kursblockung/satsolver/Variable';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { JavaIterator } from '../../../java/util/JavaIterator';

export class Clause extends JavaObject implements Comparable<Clause> {

	/**
	 *  Alle Variablen, die zu dieser Klausel gehören.
	 */
	readonly variables : Array<Variable>;

	/**
	 *  Die Anzahl an noch nicht definierten Literalen.
	 */
	free : number = 0;

	/**
	 *  Die Anzahl an erfüllten Literalen.
	 */
	sat : number = 0;


	/**
	 * Konstruktor für eine 1-CNF-Klausel.
	 *
	 * @param pX Die 1. Variable in dieser Klausel.
	 */
	public constructor(pX : Variable);

	/**
	 * Konstruktor für eine 2-CNF-Klausel.
	 *
	 * @param pX Die 1. Variable in dieser Klausel.
	 * @param pY Die 2. Variable in dieser Klausel.
	 */
	public constructor(pX : Variable, pY : Variable);

	/**
	 * Konstruktor für eine 3-CNF-Klausel.
	 *
	 * @param pX Die 1. Variable in dieser Klausel.
	 * @param pY Die 2. Variable in dieser Klausel.
	 * @param pZ Die 3. Variable in dieser Klausel.
	 */
	public constructor(pX : Variable, pY : Variable, pZ : Variable);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : Variable, __param1? : Variable, __param2? : Variable) {
		super();
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.kursblockung.satsolver.Variable')))) && (typeof __param1 === "undefined") && (typeof __param2 === "undefined")) {
			const pX : Variable = cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable(__param0);
			this.variables = [pX];
			this.free = 1;
			this.sat = 0;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.kursblockung.satsolver.Variable')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.nrw.schule.svws.core.kursblockung.satsolver.Variable')))) && (typeof __param2 === "undefined")) {
			const pX : Variable = cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable(__param0);
			const pY : Variable = cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable(__param1);
			this.variables = [pX, pY];
			this.free = 2;
			this.sat = 0;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.kursblockung.satsolver.Variable')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.nrw.schule.svws.core.kursblockung.satsolver.Variable')))) && ((typeof __param2 !== "undefined") && ((__param2 instanceof JavaObject) && (__param2.isTranspiledInstanceOf('de.nrw.schule.svws.core.kursblockung.satsolver.Variable'))))) {
			const pX : Variable = cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable(__param0);
			const pY : Variable = cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable(__param1);
			const pZ : Variable = cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable(__param2);
			this.variables = [pX, pY, pZ];
			this.free = 3;
			this.sat = 0;
		} else throw new Error('invalid method overload');
	}

	public toString() : string {
		let s : string = "";
		for (const v of this.variables) {
			if (v.index === -1) {
				return "[SAT]";
			}
			if (v.index >= 0) {
				s = s! + " " + v.nr;
			}
		}
		return "[" + s! + "]";
	}

	private getSet() : AVLSet<number> {
		const set : AVLSet<number> = new AVLSet();
		for (const v of this.variables) {
			if (v.index >= 0) {
				set.add(v.nr);
			}
		}
		return set;
	}

	public compareTo(o : Clause) : number {
		const set1 : AVLSet<number> = this.getSet();
		const set2 : AVLSet<number> = o.getSet();
		if (set1.size() < set2.size())
			return -1;
		if (set1.size() > set2.size())
			return +1;
		const i1 : JavaIterator<number> | null = set1.iterator();
		const i2 : JavaIterator<number> | null = set2.iterator();
		if ((i1 === null) || (i2 === null))
			throw new NullPointerException()
		while (i1.hasNext()) {
			const cmp : number = JavaInteger.compare(i1.next()!, i2.next()!);
			if (cmp !== 0)
				return cmp;
		}
		return 0;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.lang.Comparable', 'de.nrw.schule.svws.core.kursblockung.satsolver.Clause'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_satsolver_Clause(obj : unknown) : Clause {
	return obj as Clause;
}
