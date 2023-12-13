import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { JavaLong } from '../../../java/lang/JavaLong';
import { Schuljahresabschnitt } from '../../../core/data/schule/Schuljahresabschnitt';
import type { Comparator } from '../../../java/util/Comparator';

export class SchuljahresabschnittsUtils extends JavaObject {

	/**
	 * Ein Default-Comparator für den Vergleich von Schuljahresabschnitten in Schuljahresabschnittslisten.
	 */
	public static readonly comparator : Comparator<Schuljahresabschnitt> = { compare : (a: Schuljahresabschnitt, b: Schuljahresabschnitt) => {
		let cmp : number = a.schuljahr - b.schuljahr;
		if (cmp !== 0)
			return cmp;
		cmp = a.abschnitt - b.abschnitt;
		if (cmp !== 0)
			return cmp;
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.schule.SchuljahresabschnittsUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.schule.SchuljahresabschnittsUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_schule_SchuljahresabschnittsUtils(obj : unknown) : SchuljahresabschnittsUtils {
	return obj as SchuljahresabschnittsUtils;
}
