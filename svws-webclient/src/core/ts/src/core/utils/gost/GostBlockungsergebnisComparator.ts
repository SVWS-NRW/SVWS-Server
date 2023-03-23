import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisListeneintrag } from '../../../core/data/gost/GostBlockungsergebnisListeneintrag';
import { GostBlockungsergebnisBewertung } from '../../../core/data/gost/GostBlockungsergebnisBewertung';
import { Comparator } from '../../../java/util/Comparator';

export class GostBlockungsergebnisComparator extends JavaObject implements Comparator<GostBlockungsergebnisListeneintrag> {


	/**
	 *Erzeugt einen Comparator für zwei Objekte des Typs {@link GostBlockungsergebnisListeneintrag}. Zwei
	 * Elemente werden nach folgender Priorität sortiert: <br>
	 * (1) Array an Regelverletzungen (weniger besser) + Anzahl nicht gesetzter Kurse<br>
	 * (2) Summe nicht zugeordneter Fachwahlen + Summe an Kollisionen (weniger besser) <br>
	 * (3) Array an Kursdifferenzen-Histogramm (kleinste größte Kursdifferenz besser) <br>
	 * (4) Summe gleicher Facharten in der selben Schiene (weniger besser) <br>
	 * (5) ID von GostBlockungsergebnisListeneintrag (weniger besser) <br>
	 */
	public constructor() {
		super();
	}

	public compare(o1 : GostBlockungsergebnisListeneintrag, o2 : GostBlockungsergebnisListeneintrag) : number {
		const b1 : GostBlockungsergebnisBewertung = o1.bewertung;
		const b2 : GostBlockungsergebnisBewertung = o2.bewertung;
		const o1Wert1 : number = b1.regelVerletzungen.size() + b1.anzahlKurseNichtZugeordnet;
		const o2Wert1 : number = b2.regelVerletzungen.size() + b2.anzahlKurseNichtZugeordnet;
		if (o1Wert1 < o2Wert1)
			return -1;
		if (o1Wert1 > o2Wert1)
			return +1;
		const o1Wert2 : number = b1.anzahlSchuelerNichtZugeordnet + b1.anzahlSchuelerKollisionen;
		const o2Wert2 : number = b2.anzahlSchuelerNichtZugeordnet + b2.anzahlSchuelerKollisionen;
		if (o1Wert2 < o2Wert2)
			return -1;
		if (o1Wert2 > o2Wert2)
			return +1;
		const kdMax1 : number = b1.kursdifferenzMax;
		const kdMax2 : number = b2.kursdifferenzMax;
		if (kdMax1 < kdMax2)
			return -1;
		if (kdMax1 > kdMax2)
			return +1;
		const o1Kursdifferenzen : Array<number> | null = b1.kursdifferenzHistogramm;
		const o2Kursdifferenzen : Array<number> | null = b2.kursdifferenzHistogramm;
		for (let i : number = kdMax1; i >= 0; i--) {
			if (o1Kursdifferenzen[i] < o2Kursdifferenzen[i])
				return -1;
			if (o1Kursdifferenzen[i] > o2Kursdifferenzen[i])
				return +1;
		}
		const o1Wert4 : number = b1.anzahlKurseMitGleicherFachartProSchiene;
		const o2Wert4 : number = b2.anzahlKurseMitGleicherFachartProSchiene;
		if (o1Wert4 < o2Wert4)
			return -1;
		if (o1Wert4 > o2Wert4)
			return +1;
		if (o1.id < o2.id)
			return -1;
		if (o1.id > o2.id)
			return +1;
		return 0;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.gost.GostBlockungsergebnisComparator', 'java.util.Comparator'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_gost_GostBlockungsergebnisComparator(obj : unknown) : GostBlockungsergebnisComparator {
	return obj as GostBlockungsergebnisComparator;
}
