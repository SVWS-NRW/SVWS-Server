import { JavaObject } from '../../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../../java/lang/IllegalStateException';
import { VermerkartEintrag } from '../../../../core/data/schule/VermerkartEintrag';
import { JavaLong } from '../../../../java/lang/JavaLong';
import { JavaString } from '../../../../java/lang/JavaString';
import type { Comparator } from '../../../../java/util/Comparator';

export class VermerkArtUtils extends JavaObject {

	/**
	 * Ein Default-Comparator für den Vergleich von Klassen in Klassenlisten.
	 */
	public static readonly comparator : Comparator<VermerkartEintrag> = { compare : (a: VermerkartEintrag, b: VermerkartEintrag) => {
		let cmp : number = a.sortierung - b.sortierung;
		if (cmp !== 0)
			return cmp;
		if ((a.bezeichnung === null) || (b.bezeichnung === null))
			return JavaLong.compare(a.id, b.id);
		cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.kataloge.vermerkart.VermerkArtUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.kataloge.vermerkart.VermerkArtUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_kataloge_vermerkart_VermerkArtUtils(obj : unknown) : VermerkArtUtils {
	return obj as VermerkArtUtils;
}
