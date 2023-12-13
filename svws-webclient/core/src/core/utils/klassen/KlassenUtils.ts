import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { KlassenListeEintrag } from '../../../core/data/klassen/KlassenListeEintrag';
import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaString } from '../../../java/lang/JavaString';
import type { Comparator } from '../../../java/util/Comparator';

export class KlassenUtils extends JavaObject {

	/**
	 * Ein Default-Comparator für den Vergleich von Klassen in Klassenlisten.
	 */
	public static readonly comparator : Comparator<KlassenListeEintrag> = { compare : (a: KlassenListeEintrag, b: KlassenListeEintrag) => {
		let cmp : number = a.sortierung - b.sortierung;
		if (cmp !== 0)
			return cmp;
		if ((a.kuerzel === null) || (b.kuerzel === null))
			return JavaLong.compare(a.id, b.id);
		cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.klassen.KlassenUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klassen.KlassenUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klassen_KlassenUtils(obj : unknown) : KlassenUtils {
	return obj as KlassenUtils;
}
