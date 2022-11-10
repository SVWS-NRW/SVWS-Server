import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class GostBlockungsergebnisBewertung extends JavaObject {

	public regelVerletzungen : Vector<Number> = new Vector();

	public anzahlKurseNichtZugeordnet : number = 0;

	public anzahlSchuelerNichtZugeordnet : number = 0;

	public anzahlSchuelerKollisionen : number = 0;

	public kursdifferenzMax : number = 0;

	public kursdifferenzHistogramm : Array<number> = Array(0).fill(0);

	public anzahlKurseMitGleicherFachartProSchiene : number = 0;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisBewertung'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsergebnisBewertung {
		const obj = JSON.parse(json);
		const result = new GostBlockungsergebnisBewertung();
		if (!!obj.regelVerletzungen) {
			for (let elem of obj.regelVerletzungen) {
				result.regelVerletzungen?.add(Number(elem));
			}
		}
		if (typeof obj.anzahlKurseNichtZugeordnet === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlKurseNichtZugeordnet');
		result.anzahlKurseNichtZugeordnet = obj.anzahlKurseNichtZugeordnet;
		if (typeof obj.anzahlSchuelerNichtZugeordnet === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlSchuelerNichtZugeordnet');
		result.anzahlSchuelerNichtZugeordnet = obj.anzahlSchuelerNichtZugeordnet;
		if (typeof obj.anzahlSchuelerKollisionen === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlSchuelerKollisionen');
		result.anzahlSchuelerKollisionen = obj.anzahlSchuelerKollisionen;
		if (typeof obj.kursdifferenzMax === "undefined")
			 throw new Error('invalid json format, missing attribute kursdifferenzMax');
		result.kursdifferenzMax = obj.kursdifferenzMax;
		for (let i : number = 0; i < obj.kursdifferenzHistogramm.length; i++) {
			result.kursdifferenzHistogramm[i] = obj.kursdifferenzHistogramm[i];
		}
		if (typeof obj.anzahlKurseMitGleicherFachartProSchiene === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlKurseMitGleicherFachartProSchiene');
		result.anzahlKurseMitGleicherFachartProSchiene = obj.anzahlKurseMitGleicherFachartProSchiene;
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnisBewertung) : string {
		let result = '{';
		if (!obj.regelVerletzungen) {
			result += '"regelVerletzungen" : []';
		} else {
			result += '"regelVerletzungen" : [ ';
			for (let i : number = 0; i < obj.regelVerletzungen.size(); i++) {
				let elem = obj.regelVerletzungen.get(i);
				result += elem;
				if (i < obj.regelVerletzungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"anzahlKurseNichtZugeordnet" : ' + obj.anzahlKurseNichtZugeordnet + ',';
		result += '"anzahlSchuelerNichtZugeordnet" : ' + obj.anzahlSchuelerNichtZugeordnet + ',';
		result += '"anzahlSchuelerKollisionen" : ' + obj.anzahlSchuelerKollisionen + ',';
		result += '"kursdifferenzMax" : ' + obj.kursdifferenzMax + ',';
		if (!obj.kursdifferenzHistogramm) {
			result += '"kursdifferenzHistogramm" : []';
		} else {
			result += '"kursdifferenzHistogramm" : [ ';
			for (let i : number = 0; i < obj.kursdifferenzHistogramm.length; i++) {
				let elem = obj.kursdifferenzHistogramm[i];
				result += JSON.stringify(elem);
				if (i < obj.kursdifferenzHistogramm.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"anzahlKurseMitGleicherFachartProSchiene" : ' + obj.anzahlKurseMitGleicherFachartProSchiene + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnisBewertung>) : string {
		let result = '{';
		if (typeof obj.regelVerletzungen !== "undefined") {
			if (!obj.regelVerletzungen) {
				result += '"regelVerletzungen" : []';
			} else {
				result += '"regelVerletzungen" : [ ';
				for (let i : number = 0; i < obj.regelVerletzungen.size(); i++) {
					let elem = obj.regelVerletzungen.get(i);
					result += elem;
					if (i < obj.regelVerletzungen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.anzahlKurseNichtZugeordnet !== "undefined") {
			result += '"anzahlKurseNichtZugeordnet" : ' + obj.anzahlKurseNichtZugeordnet + ',';
		}
		if (typeof obj.anzahlSchuelerNichtZugeordnet !== "undefined") {
			result += '"anzahlSchuelerNichtZugeordnet" : ' + obj.anzahlSchuelerNichtZugeordnet + ',';
		}
		if (typeof obj.anzahlSchuelerKollisionen !== "undefined") {
			result += '"anzahlSchuelerKollisionen" : ' + obj.anzahlSchuelerKollisionen + ',';
		}
		if (typeof obj.kursdifferenzMax !== "undefined") {
			result += '"kursdifferenzMax" : ' + obj.kursdifferenzMax + ',';
		}
		if (typeof obj.kursdifferenzHistogramm !== "undefined") {
			let a = obj.kursdifferenzHistogramm;
			if (!a) {
				result += '"kursdifferenzHistogramm" : []';
			} else {
				result += '"kursdifferenzHistogramm" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += JSON.stringify(elem);
					if (i < a.length - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.anzahlKurseMitGleicherFachartProSchiene !== "undefined") {
			result += '"anzahlKurseMitGleicherFachartProSchiene" : ' + obj.anzahlKurseMitGleicherFachartProSchiene + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisBewertung(obj : unknown) : GostBlockungsergebnisBewertung {
	return obj as GostBlockungsergebnisBewertung;
}
