import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnis } from '../../../core/data/gost/GostBlockungsergebnis';
import { GostBlockungsergebnisBewertungComparator } from '../../../core/utils/gost/GostBlockungsergebnisBewertungComparator';
import type { Comparator } from '../../../java/util/Comparator';

export class GostBlockungsergebnisComparator extends JavaObject implements Comparator<GostBlockungsergebnis> {


	/**
	 * Erzeugt einen Comparator für zwei Objekte des Typs {@link GostBlockungsergebnis}.
	 * Zwei Elemente werden nach folgender Priorität sortiert:
	 * <br>(1) Array an Regelverletzungen (weniger besser) + Anzahl nicht gesetzter Kurse
	 * <br>(2) Summe nicht zugeordneter Fachwahlen + Summe an Kollisionen (weniger besser)
	 * <br>(3) Array an Kursdifferenzen-Histogramm (kleinste größte Kursdifferenz besser)
	 * <br>(4) Summe gleicher Facharten in der selben Schiene (weniger besser)
	 * <br>(5) ID von GostBlockungsergebnis (weniger besser)
	 */
	public constructor() {
		super();
	}

	public compare(o1 : GostBlockungsergebnis, o2 : GostBlockungsergebnis) : number {
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
		return 'de.svws_nrw.core.utils.gost.GostBlockungsergebnisComparator';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostBlockungsergebnisComparator', 'java.util.Comparator'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_GostBlockungsergebnisComparator(obj : unknown) : GostBlockungsergebnisComparator {
	return obj as GostBlockungsergebnisComparator;
}
