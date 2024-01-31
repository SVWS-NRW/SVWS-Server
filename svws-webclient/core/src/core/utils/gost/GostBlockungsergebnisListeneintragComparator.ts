import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisBewertungComparator } from '../../../core/utils/gost/GostBlockungsergebnisBewertungComparator';
import { GostBlockungsergebnisListeneintrag } from '../../../core/data/gost/GostBlockungsergebnisListeneintrag';
import type { Comparator } from '../../../java/util/Comparator';

export class GostBlockungsergebnisListeneintragComparator extends JavaObject implements Comparator<GostBlockungsergebnisListeneintrag> {


	/**
	 * Erzeugt einen Comparator für zwei Objekte des Typs {@link GostBlockungsergebnisListeneintrag}.
	 * Zwei Elemente werden nach folgender Priorität sortiert:
	 * <br>(1) Array an Regelverletzungen (weniger besser) + Anzahl nicht gesetzter Kurse
	 * <br>(2) Summe nicht zugeordneter Fachwahlen + Summe an Kollisionen (weniger besser)
	 * <br>(3) Array an Kursdifferenzen-Histogramm (kleinste größte Kursdifferenz besser)
	 * <br>(4) Summe gleicher Facharten in der selben Schiene (weniger besser)
	 * <br>(5) ID von GostBlockungsergebnisListeneintrag (weniger besser)
	 */
	public constructor() {
		super();
	}

	public compare(o1 : GostBlockungsergebnisListeneintrag, o2 : GostBlockungsergebnisListeneintrag) : number {
		const cmp : number = GostBlockungsergebnisBewertungComparator.compareBewertungen(o1.bewertung, o2.bewertung);
		if (cmp !== 0)
			return cmp;
		if (o1.id < o2.id)
			return -1;
		if (o1.id > o2.id)
			return +1;
		return 0;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.GostBlockungsergebnisListeneintragComparator';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostBlockungsergebnisListeneintragComparator', 'java.util.Comparator'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_GostBlockungsergebnisListeneintragComparator(obj : unknown) : GostBlockungsergebnisListeneintragComparator {
	return obj as GostBlockungsergebnisListeneintragComparator;
}
