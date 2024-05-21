import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { ReligionEintrag } from '../../../core/data/schule/ReligionEintrag';
import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaString } from '../../../java/lang/JavaString';
import type { Comparator } from '../../../java/util/Comparator';

export class ReligionUtils extends JavaObject {

	/**
	 * Ein Default-Comparator für den Vergleich von Religion-Einträgen.
	 */
	public static readonly comparator : Comparator<ReligionEintrag> = { compare : (a: ReligionEintrag, b: ReligionEintrag) => {
		if ((a.kuerzel === null) || (b.kuerzel === null)) {
			if ((a.kuerzel === null) && (b.kuerzel === null))
				return 0;
			return (a.kuerzel === null) ? -1 : 1;
		}
		const cmp : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	/**
	 * Ein Comparator für den Vergleich von Religion-Einträgen anhand ihres Textes.
	 */
	public static readonly comparatorText : Comparator<ReligionEintrag> = { compare : (a: ReligionEintrag, b: ReligionEintrag) => {
		if ((a.text === null) || (b.text === null)) {
			if ((a.text === null) && (b.text === null))
				return 0;
			return (a.text === null) ? -1 : 1;
		}
		const cmp : number = JavaString.compareTo(a.text, b.text);
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
