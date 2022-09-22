import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { AVLSet, cast_de_nrw_schule_svws_core_adt_set_AVLSet } from '../../../core/adt/set/AVLSet';
import { Comparable, cast_java_lang_Comparable } from '../../../java/lang/Comparable';
import { Variable, cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable } from '../../../core/kursblockung/satsolver/Variable';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { JavaIterator, cast_java_util_Iterator } from '../../../java/util/JavaIterator';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Clause extends JavaObject implements Comparable<Clause> {

	readonly variables : Array<Variable>;

	free : number = 0;

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
			let pX : Variable = cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable(__param0);
			this.variables = [pX];
			this.free = 1;
			this.sat = 0;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.kursblockung.satsolver.Variable')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.nrw.schule.svws.core.kursblockung.satsolver.Variable')))) && (typeof __param2 === "undefined")) {
			let pX : Variable = cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable(__param0);
			let pY : Variable = cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable(__param1);
			this.variables = [pX, pY];
			this.free = 2;
			this.sat = 0;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.kursblockung.satsolver.Variable')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.nrw.schule.svws.core.kursblockung.satsolver.Variable')))) && ((typeof __param2 !== "undefined") && ((__param2 instanceof JavaObject) && (__param2.isTranspiledInstanceOf('de.nrw.schule.svws.core.kursblockung.satsolver.Variable'))))) {
			let pX : Variable = cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable(__param0);
			let pY : Variable = cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable(__param1);
			let pZ : Variable = cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable(__param2);
			this.variables = [pX, pY, pZ];
			this.free = 3;
			this.sat = 0;
		} else throw new Error('invalid method overload');
	}

	public toString() : String {
		let s : String = "";
		for (let v of this.variables) {
			if (v.index === -1) {
				return "[SAT]";
			}
			if (v.index >= 0) {
				s = s.valueOf() + " " + v.nr;
			}
		}
		return "[" + s.valueOf() + "]";
	}

	private getSet() : AVLSet<Number> {
		let set : AVLSet<Number> = new AVLSet();
		for (let v of this.variables) {
			if (v.index >= 0) {
				set.add(v.nr);
			}
		}
		return set;
	}

	public compareTo(o : Clause) : number {
		let set1 : AVLSet<Number> = this.getSet();
		let set2 : AVLSet<Number> = o.getSet();
		if (set1.size() < set2.size()) 
			return -1;
		if (set1.size() > set2.size()) 
			return +1;
		let i1 : JavaIterator<Number> | null = set1.iterator();
		let i2 : JavaIterator<Number> | null = set2.iterator();
		if ((i1 === null) || (i2 === null)) 
			throw new NullPointerException()
		while (i1.hasNext()) {
			let cmp : number = JavaInteger.compare(i1.next().valueOf(), i2.next().valueOf());
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
