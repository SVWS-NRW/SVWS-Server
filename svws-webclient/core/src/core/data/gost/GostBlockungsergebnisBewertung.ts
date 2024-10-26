import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class GostBlockungsergebnisBewertung extends JavaObject {

	/**
	 * Bewertungskriterium 1a: Array mit den Regel-IDs der {@link GostBlockungRegel} die nicht erfüllt werden konnten.
	 */
	public regelVerletzungen : List<number> = new ArrayList<number>();

	/**
	 * Bewertungskriterium 1b: Anzahl aller Kurse, die nicht auf Schienen verteilt wurden.
	 */
	public anzahlKurseNichtZugeordnet : number = 0;

	/**
	 * Bewertungskriterium 2a: Anzahl aller Fachwahlen der SuS, die nicht zugeordnet wurden.
	 */
	public anzahlSchuelerNichtZugeordnet : number = 0;

	/**
	 * Bewertungskriterium 2b: Anzahl der Kollisionen bei der Zuordnung von Schülern zu den Kurses in den Schienen.
	 */
	public anzahlSchuelerKollisionen : number = 0;

	/**
	 * Bewertungskriterium 3a: Die größte Kursdifferenz in der Blockung.
	 */
	public kursdifferenzMax : number = 0;

	/**
	 * Bewertungskriterium 3b: Array mit dem Histogramm der Kursdifferenzen. <br>
	 *  Beispiel: [7, 5, 2, 1, 0, 0, ...] bedeutet: <br>
	 *  Die Kursdifferenz 0 gibt es 7 Mal <br>
	 *  Die Kursdifferenz 1 gibt es 5 Mal <br>
	 *  Die Kursdifferenz 2 gibt es 2 Mal <br>
	 *  Die Kursdifferenz 3 gibt es 1 Mal <br>
	 */
	public kursdifferenzHistogramm : Array<number> = Array(0).fill(0);

	/**
	 * Bewertungskriterium 4: Anzahl aller Kurse mit gleicher Fachart in einer Schiene.
	 */
	public anzahlKurseMitGleicherFachartProSchiene : number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungsergebnisBewertung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungsergebnisBewertung'].includes(name);
	}

	public static class = new Class<GostBlockungsergebnisBewertung>('de.svws_nrw.core.data.gost.GostBlockungsergebnisBewertung');

	public static transpilerFromJSON(json : string): GostBlockungsergebnisBewertung {
		const obj = JSON.parse(json) as Partial<GostBlockungsergebnisBewertung>;
		const result = new GostBlockungsergebnisBewertung();
		if (obj.regelVerletzungen !== undefined) {
			for (const elem of obj.regelVerletzungen) {
				result.regelVerletzungen.add(elem);
			}
		}
		if (obj.anzahlKurseNichtZugeordnet === undefined)
			throw new Error('invalid json format, missing attribute anzahlKurseNichtZugeordnet');
		result.anzahlKurseNichtZugeordnet = obj.anzahlKurseNichtZugeordnet;
		if (obj.anzahlSchuelerNichtZugeordnet === undefined)
			throw new Error('invalid json format, missing attribute anzahlSchuelerNichtZugeordnet');
		result.anzahlSchuelerNichtZugeordnet = obj.anzahlSchuelerNichtZugeordnet;
		if (obj.anzahlSchuelerKollisionen === undefined)
			throw new Error('invalid json format, missing attribute anzahlSchuelerKollisionen');
		result.anzahlSchuelerKollisionen = obj.anzahlSchuelerKollisionen;
		if (obj.kursdifferenzMax === undefined)
			throw new Error('invalid json format, missing attribute kursdifferenzMax');
		result.kursdifferenzMax = obj.kursdifferenzMax;
		if (obj.kursdifferenzHistogramm !== undefined) {
			for (let i = 0; i < obj.kursdifferenzHistogramm.length; i++) {
				result.kursdifferenzHistogramm[i] = obj.kursdifferenzHistogramm[i];
			}
		}
		if (obj.anzahlKurseMitGleicherFachartProSchiene === undefined)
			throw new Error('invalid json format, missing attribute anzahlKurseMitGleicherFachartProSchiene');
		result.anzahlKurseMitGleicherFachartProSchiene = obj.anzahlKurseMitGleicherFachartProSchiene;
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnisBewertung) : string {
		let result = '{';
		result += '"regelVerletzungen" : [ ';
		for (let i = 0; i < obj.regelVerletzungen.size(); i++) {
			const elem = obj.regelVerletzungen.get(i);
			result += elem.toString();
			if (i < obj.regelVerletzungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"anzahlKurseNichtZugeordnet" : ' + obj.anzahlKurseNichtZugeordnet.toString() + ',';
		result += '"anzahlSchuelerNichtZugeordnet" : ' + obj.anzahlSchuelerNichtZugeordnet.toString() + ',';
		result += '"anzahlSchuelerKollisionen" : ' + obj.anzahlSchuelerKollisionen.toString() + ',';
		result += '"kursdifferenzMax" : ' + obj.kursdifferenzMax.toString() + ',';
		result += '"kursdifferenzHistogramm" : [ ';
		for (let i = 0; i < obj.kursdifferenzHistogramm.length; i++) {
			const elem = obj.kursdifferenzHistogramm[i];
			result += JSON.stringify(elem);
			if (i < obj.kursdifferenzHistogramm.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"anzahlKurseMitGleicherFachartProSchiene" : ' + obj.anzahlKurseMitGleicherFachartProSchiene.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnisBewertung>) : string {
		let result = '{';
		if (obj.regelVerletzungen !== undefined) {
			result += '"regelVerletzungen" : [ ';
			for (let i = 0; i < obj.regelVerletzungen.size(); i++) {
				const elem = obj.regelVerletzungen.get(i);
				result += elem.toString();
				if (i < obj.regelVerletzungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.anzahlKurseNichtZugeordnet !== undefined) {
			result += '"anzahlKurseNichtZugeordnet" : ' + obj.anzahlKurseNichtZugeordnet.toString() + ',';
		}
		if (obj.anzahlSchuelerNichtZugeordnet !== undefined) {
			result += '"anzahlSchuelerNichtZugeordnet" : ' + obj.anzahlSchuelerNichtZugeordnet.toString() + ',';
		}
		if (obj.anzahlSchuelerKollisionen !== undefined) {
			result += '"anzahlSchuelerKollisionen" : ' + obj.anzahlSchuelerKollisionen.toString() + ',';
		}
		if (obj.kursdifferenzMax !== undefined) {
			result += '"kursdifferenzMax" : ' + obj.kursdifferenzMax.toString() + ',';
		}
		if (obj.kursdifferenzHistogramm !== undefined) {
			const a = obj.kursdifferenzHistogramm;
			result += '"kursdifferenzHistogramm" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += JSON.stringify(elem);
				if (i < a.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.anzahlKurseMitGleicherFachartProSchiene !== undefined) {
			result += '"anzahlKurseMitGleicherFachartProSchiene" : ' + obj.anzahlKurseMitGleicherFachartProSchiene.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisBewertung(obj : unknown) : GostBlockungsergebnisBewertung {
	return obj as GostBlockungsergebnisBewertung;
}
