import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { SchuelerKAoADaten } from '../../../core/data/schueler/SchuelerKAoADaten';
import { JavaLong } from '../../../java/lang/JavaLong';
import { Class } from '../../../java/lang/Class';
import type { Comparator } from '../../../java/util/Comparator';

export class SchuelerKAoAUtils extends JavaObject {

	/**
	 * Ein Default-Comparator für den Vergleich von KAoA in KAoA-Listen.
	 */
	public static readonly comparator : Comparator<SchuelerKAoADaten> = { compare : (a: SchuelerKAoADaten, b: SchuelerKAoADaten) => {
		let cmp : number = JavaLong.compare(a.abschnitt, b.abschnitt);
		if (cmp !== 0)
			return cmp;
		cmp = JavaLong.compare(a.kategorie, b.kategorie);
		if (cmp !== 0)
			return cmp;
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.schueler.SchuelerKAoAUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.schueler.SchuelerKAoAUtils'].includes(name);
	}

	public static class = new Class<SchuelerKAoAUtils>('de.svws_nrw.core.utils.schueler.SchuelerKAoAUtils');

}

export function cast_de_svws_nrw_core_utils_schueler_SchuelerKAoAUtils(obj : unknown) : SchuelerKAoAUtils {
	return obj as SchuelerKAoAUtils;
}
