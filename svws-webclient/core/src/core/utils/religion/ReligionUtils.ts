import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { ReligionEintrag } from '../../../core/data/schule/ReligionEintrag';
import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaString } from '../../../java/lang/JavaString';
import type { Comparator } from '../../../java/util/Comparator';

export class ReligionUtils extends JavaObject {

	/**
	 * Ein Default-Comparator für den Vergleich von Religionen in Religionslisten.
	 */
	public static readonly comparator : Comparator<ReligionEintrag> = { compare : (a: ReligionEintrag, b: ReligionEintrag) => {
		if ((a.kuerzel === null) || (b.kuerzel === null)) {
			if ((a.kuerzel === null) && (b.kuerzel === null))
				return 0;
			return (a.kuerzel === null) ? -1 : 1;
		}
		let cmp : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.religion.ReligionUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.religion.ReligionUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_religion_ReligionUtils(obj : unknown) : ReligionUtils {
	return obj as ReligionUtils;
}
