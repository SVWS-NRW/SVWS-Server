import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { KursListeEintrag } from '../../../core/data/kurse/KursListeEintrag';
import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaString } from '../../../java/lang/JavaString';
import type { Comparator } from '../../../java/util/Comparator';

export class KursUtils extends JavaObject {

	/**
	 * Ein Default-Comparator für den Vergleich von Klassen in Klassenlisten.
	 */
	public static readonly comparator : Comparator<KursListeEintrag> = { compare : (a: KursListeEintrag, b: KursListeEintrag) => {
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.kurse.KursUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_kurse_KursUtils(obj : unknown) : KursUtils {
	return obj as KursUtils;
}
