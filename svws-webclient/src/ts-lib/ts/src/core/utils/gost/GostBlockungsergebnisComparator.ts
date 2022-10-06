import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisListeneintrag, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisListeneintrag } from '../../../core/data/gost/GostBlockungsergebnisListeneintrag';
import { Comparator, cast_java_util_Comparator } from '../../../java/util/Comparator';

export class GostBlockungsergebnisComparator extends JavaObject implements Comparator<GostBlockungsergebnisListeneintrag> {


	/**
	 *Erzeugt einen Comparator für zwei Objekte des Typs {@link GostBlockungsergebnisListeneintrag}. Zwei Elemente
	 * werden nach folgender Priorität sortiert: <br>
	 * (1) Array an Regelverletzungen (weniger besser)<br>
	 * (2) Summe nicht zugeordneter Fachwahlen (weniger besser) <br>
	 * (3) Array an Kursdifferenzen (kleinste größte Kursdifferenz besser) <br>
	 * (4) Summe gleicher Facharten in der selben Schiene (weniger besser) <br>
	 */
	public constructor() {
		super();
	}

	public compare(o1 : GostBlockungsergebnisListeneintrag, o2 : GostBlockungsergebnisListeneintrag) : number {
		if (o1.bewertung.regelVerletzungen.length < o2.bewertung.regelVerletzungen.length) 
			return -1;
		if (o1.bewertung.regelVerletzungen.length > o2.bewertung.regelVerletzungen.length) 
			return +1;
		if (o1.bewertung.anzahlNichtZugeordnet < o2.bewertung.anzahlNichtZugeordnet) 
			return -1;
		if (o1.bewertung.anzahlNichtZugeordnet > o2.bewertung.anzahlNichtZugeordnet) 
			return +1;
		if (o1.bewertung.anzahlKollisionen < o2.bewertung.anzahlKollisionen) 
			return -1;
		if (o1.bewertung.anzahlKollisionen > o2.bewertung.anzahlKollisionen) 
			return +1;
		if (o1.bewertung.anzahlSchienenMitKollisionen < o2.bewertung.anzahlSchienenMitKollisionen) 
			return -1;
		if (o1.bewertung.anzahlSchienenMitKollisionen > o2.bewertung.anzahlSchienenMitKollisionen) 
			return +1;
		let length1 : number = o1.bewertung.kursdifferenzen.length;
		let length2 : number = o2.bewertung.kursdifferenzen.length;
		if (length1 < length2) 
			return -1;
		if (length1 > length2) 
			return +1;
		for (let i : number = length1 - 1; i >= 0; i--){
			if (o1.bewertung.kursdifferenzen[i] < o2.bewertung.kursdifferenzen[i]) 
				return -1;
			if (o1.bewertung.kursdifferenzen[i] > o2.bewertung.kursdifferenzen[i]) 
				return -1;
		}
		if (o1.bewertung.anzahlKurseMitGleicherFachartProSchiene < o2.bewertung.anzahlKurseMitGleicherFachartProSchiene) 
			return -1;
		if (o1.bewertung.anzahlKurseMitGleicherFachartProSchiene > o2.bewertung.anzahlKurseMitGleicherFachartProSchiene) 
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
