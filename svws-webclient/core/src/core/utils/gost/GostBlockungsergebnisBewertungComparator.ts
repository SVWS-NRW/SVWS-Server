import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisBewertung } from '../../../core/data/gost/GostBlockungsergebnisBewertung';
import type { Comparator } from '../../../java/util/Comparator';

export class GostBlockungsergebnisBewertungComparator extends JavaObject implements Comparator<GostBlockungsergebnisBewertung> {


	/**
	 * Erzeugt einen Comparator für zwei Objekte des Typs {@link GostBlockungsergebnisBewertung}.
	 * Zwei Elemente werden nach folgender Priorität sortiert:
	 * <br>(1) Array an Regelverletzungen (weniger besser) + Anzahl nicht gesetzter Kurse
	 * <br>(2) Summe nicht zugeordneter Fachwahlen + Summe an Kollisionen (weniger besser)
	 * <br>(3) Array an Kursdifferenzen-Histogramm (kleinste größte Kursdifferenz besser)
	 * <br>(4) Summe gleicher Facharten in der selben Schiene (weniger besser) <br>
	 */
	public constructor() {
		super();
	}

	/**
	 * Vergleicht zwei Objekte des Typs {@link GostBlockungsergebnisBewertung}.
	 *
	 * Zwei Elemente werden nach folgender Priorität sortiert:
	 * <br>(1) Array an Regelverletzungen (weniger besser) + Anzahl nicht gesetzter Kurse
	 * <br>(2) Summe nicht zugeordneter Fachwahlen + Summe an Kollisionen (weniger besser)
	 * <br>(3) Array an Kursdifferenzen-Histogramm (kleinste größte Kursdifferenz besser)
	 * <br>(4) Summe gleicher Facharten in der selben Schiene (weniger besser) <br>
	 *
	 * @param o1   die erste Bewertung
	 * @param o2   die zweite Bewertung
	 *
	 * @return negativ bei kleiner, 0 bei gleich und positiv bei größer
	 */
	public static compareBewertungen(o1 : GostBlockungsergebnisBewertung, o2 : GostBlockungsergebnisBewertung) : number {
		const o1Wert1 : number = o1.regelVerletzungen.size() + o1.anzahlKurseNichtZugeordnet;
		const o2Wert1 : number = o2.regelVerletzungen.size() + o2.anzahlKurseNichtZugeordnet;
		if (o1Wert1 < o2Wert1)
			return -1;
		if (o1Wert1 > o2Wert1)
			return +1;
		const o1Wert2 : number = o1.anzahlSchuelerNichtZugeordnet + o1.anzahlSchuelerKollisionen;
		const o2Wert2 : number = o2.anzahlSchuelerNichtZugeordnet + o2.anzahlSchuelerKollisionen;
		if (o1Wert2 < o2Wert2)
			return -1;
		if (o1Wert2 > o2Wert2)
			return +1;
		const kdMax1 : number = o1.kursdifferenzMax;
		const kdMax2 : number = o2.kursdifferenzMax;
		if (kdMax1 < kdMax2)
			return -1;
		if (kdMax1 > kdMax2)
			return +1;
		const o1Kursdifferenzen : Array<number> | null = o1.kursdifferenzHistogramm;
		const o2Kursdifferenzen : Array<number> | null = o2.kursdifferenzHistogramm;
		for (let i : number = kdMax1; i >= 0; i--) {
			if (o1Kursdifferenzen[i] < o2Kursdifferenzen[i])
				return -1;
			if (o1Kursdifferenzen[i] > o2Kursdifferenzen[i])
				return +1;
		}
		const o1Wert4 : number = o1.anzahlKurseMitGleicherFachartProSchiene;
		const o2Wert4 : number = o2.anzahlKurseMitGleicherFachartProSchiene;
		if (o1Wert4 < o2Wert4)
			return -1;
		if (o1Wert4 > o2Wert4)
			return +1;
		return 0;
	}

	public compare(o1 : GostBlockungsergebnisBewertung, o2 : GostBlockungsergebnisBewertung) : number {
		return GostBlockungsergebnisBewertungComparator.compareBewertungen(o1, o2);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.GostBlockungsergebnisBewertungComparator';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostBlockungsergebnisBewertungComparator', 'java.util.Comparator'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_GostBlockungsergebnisBewertungComparator(obj : unknown) : GostBlockungsergebnisBewertungComparator {
	return obj as GostBlockungsergebnisBewertungComparator;
}
