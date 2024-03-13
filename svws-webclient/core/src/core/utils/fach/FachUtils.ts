import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { FachDaten } from '../../../core/data/fach/FachDaten';
import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaString } from '../../../java/lang/JavaString';
import type { Comparator } from '../../../java/util/Comparator';

export class FachUtils extends JavaObject {

	/**
	 * Ein Default-Comparator für den Vergleich von Fächern in Fächerlisten.
	 */
	public static readonly comparator : Comparator<FachDaten> = { compare : (a: FachDaten, b: FachDaten) => {
		let cmp : number = a.sortierung - b.sortierung;
		if (cmp !== 0)
			return cmp;
		cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.fach.FachUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.fach.FachUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_fach_FachUtils(obj : unknown) : FachUtils {
	return obj as FachUtils;
}
