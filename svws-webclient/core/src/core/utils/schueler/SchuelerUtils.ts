import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { SchuelerListeEintrag } from '../../../core/data/schueler/SchuelerListeEintrag';
import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaString } from '../../../java/lang/JavaString';
import { SchuelerVermerkartZusammenfassung } from '../../../core/data/schueler/SchuelerVermerkartZusammenfassung';
import type { Comparator } from '../../../java/util/Comparator';

export class SchuelerUtils extends JavaObject {

	/**
	 * Ein Default-Comparator für den Vergleich von Schülern in Schuelerlisten.
	 */
	public static readonly comparator : Comparator<SchuelerListeEintrag> = { compare : (a: SchuelerListeEintrag, b: SchuelerListeEintrag) => {
		let cmp : number = JavaString.compareTo(a.nachname, b.nachname);
		if (cmp !== 0)
			return cmp;
		cmp = JavaString.compareTo(a.vorname, b.vorname);
		if (cmp !== 0)
			return cmp;
		cmp = JavaString.compareTo(a.jahrgang, b.jahrgang);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	/**
	 * Ein Default-Comparator für den Vergleich von Schülern in Schuelerlisten.
	 */
	public static readonly comparatorSchuelerVermerkartZusammenfassung : Comparator<SchuelerVermerkartZusammenfassung> = { compare : (a: SchuelerVermerkartZusammenfassung, b: SchuelerVermerkartZusammenfassung) => {
		let cmp : number = JavaString.compareTo(a.nachname, b.nachname);
		if (cmp !== 0)
			return cmp;
		cmp = JavaString.compareTo(a.vorname, b.vorname);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.schueler.SchuelerUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.schueler.SchuelerUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_schueler_SchuelerUtils(obj : unknown) : SchuelerUtils {
	return obj as SchuelerUtils;
}
