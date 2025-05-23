import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { LehrerListeEintrag } from '../../../core/data/lehrer/LehrerListeEintrag';
import { JavaLong } from '../../../java/lang/JavaLong';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import type { Comparator } from '../../../java/util/Comparator';

export class LehrerUtils extends JavaObject {

	/**
	 * Ein Default-Comparator für den Vergleich von Lehrern in Lehrerlisten.
	 */
	public static readonly comparator : Comparator<LehrerListeEintrag> = { compare : (a: LehrerListeEintrag, b: LehrerListeEintrag) => {
		let cmp : number = JavaString.compareTo(a.nachname, b.nachname);
		if (cmp !== 0)
			return cmp;
		cmp = JavaString.compareTo(a.vorname, b.vorname);
		if (cmp !== 0)
			return cmp;
		cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	/**
	 * Ein Kuerzel-Comparator für den Vergleich von Lehrern in Lehrerlisten.
	 */
	public static readonly comparatorKuerzel : Comparator<LehrerListeEintrag> = { compare : (a: LehrerListeEintrag, b: LehrerListeEintrag) => {
		const cmp : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.lehrer.LehrerUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.lehrer.LehrerUtils'].includes(name);
	}

	public static class = new Class<LehrerUtils>('de.svws_nrw.core.utils.lehrer.LehrerUtils');

}

export function cast_de_svws_nrw_core_utils_lehrer_LehrerUtils(obj : unknown) : LehrerUtils {
	return obj as LehrerUtils;
}
