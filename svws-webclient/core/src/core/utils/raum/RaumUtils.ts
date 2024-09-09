import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { Raum } from '../../../core/data/schule/Raum';
import { JavaLong } from '../../../java/lang/JavaLong';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import type { Comparator } from '../../../java/util/Comparator';

export class RaumUtils extends JavaObject {

	/**
	 * Ein Default-Comparator für den Vergleich von Räumen in Raumlisten.
	 */
	public static readonly comparator : Comparator<Raum> = { compare : (a: Raum, b: Raum) => {
		let cmp : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.raum.RaumUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.raum.RaumUtils'].includes(name);
	}

	public static class = new Class<RaumUtils>('de.svws_nrw.core.utils.raum.RaumUtils');

}

export function cast_de_svws_nrw_core_utils_raum_RaumUtils(obj : unknown) : RaumUtils {
	return obj as RaumUtils;
}
