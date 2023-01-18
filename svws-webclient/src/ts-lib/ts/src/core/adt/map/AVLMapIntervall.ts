import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class AVLMapIntervall<K> extends JavaObject {

	/**
	 *  Ein Dummy-Element für den Schlüsselwert "-Unendlich".
	 */
	static readonly _INFINITY_MINUS : unknown | null = new Object();

	/**
	 *  Ein Dummy-Element für den Schlüsselwert "+Unendlich".
	 */
	static readonly _INFINITY_PLUS : unknown | null = new Object();

	/**
	 *  Der Anfang des Intervalls.
	 */
	readonly from : K;

	/**
	 *  Gibt an, ob der Intervall-Anfang inklusive ist.
	 */
	readonly fromInc : boolean;

	/**
	 *  Das Ende des Intervalls.
	 */
	readonly to : K;

	/**
	 *  Gibt an, ob das Intervall-Ende inklusive ist.
	 */
	readonly toInc : boolean;


	/**
	 * @param pFrom    Der Anfang des Intervalls.
	 * @param pFromInc Gibt an, ob der Intervall-Anfang inklusive ist.
	 * @param pTo      Das Ende des Intervalls.
	 * @param pToInc   Gibt an, ob das Intervall-Ende inklusive ist.
	 */
	constructor(pFrom : K, pFromInc : boolean, pTo : K, pToInc : boolean) {
		super();
		this.from = pFrom;
		this.fromInc = pFromInc;
		this.to = pTo;
		this.toInc = pToInc;
	}

	public toString() : String {
		let sFrom : String = (this.from === AVLMapIntervall._INFINITY_MINUS) ? "-INF" : "" + this.from;
		let sTo : String = (this.to === AVLMapIntervall._INFINITY_PLUS) ? "+INF" : "" + this.to;
		return "[" + sFrom.valueOf() + ", " + this.fromInc + ", " + sTo.valueOf() + ", " + this.toInc + "]";
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.adt.map.AVLMapIntervall'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_adt_map_AVLMapIntervall<K>(obj : unknown) : AVLMapIntervall<K> {
	return obj as AVLMapIntervall<K>;
}
