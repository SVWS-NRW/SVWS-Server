import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';

export class GostBlockungsergebnisBewertung extends JavaObject {

	public regelVerletzungen : Array<Number> = Array(0).fill(null);

	public anzahlNichtZugeordnet : number = 0;

	public anzahlKollisionen : number = 0;

	public anzahlSchienenMitKollisionen : number = 0;

	public kursdifferenzen : Array<Number> = Array(0).fill(null);

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
		for (let i : number = 0; i < obj.regelVerletzungen.length; i++) {
			result.regelVerletzungen[i] = obj.regelVerletzungen[i];
		}
		if (typeof obj.anzahlNichtZugeordnet === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlNichtZugeordnet');
		result.anzahlNichtZugeordnet = obj.anzahlNichtZugeordnet;
		if (typeof obj.anzahlKollisionen === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlKollisionen');
		result.anzahlKollisionen = obj.anzahlKollisionen;
		if (typeof obj.anzahlSchienenMitKollisionen === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlSchienenMitKollisionen');
		result.anzahlSchienenMitKollisionen = obj.anzahlSchienenMitKollisionen;
		for (let i : number = 0; i < obj.kursdifferenzen.length; i++) {
			result.kursdifferenzen[i] = obj.kursdifferenzen[i];
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
			for (let i : number = 0; i < obj.regelVerletzungen.length; i++) {
				let elem = obj.regelVerletzungen[i];
				result += elem;
				if (i < obj.regelVerletzungen.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"anzahlNichtZugeordnet" : ' + obj.anzahlNichtZugeordnet + ',';
		result += '"anzahlKollisionen" : ' + obj.anzahlKollisionen + ',';
		result += '"anzahlSchienenMitKollisionen" : ' + obj.anzahlSchienenMitKollisionen + ',';
		if (!obj.kursdifferenzen) {
			result += '"kursdifferenzen" : []';
		} else {
			result += '"kursdifferenzen" : [ ';
			for (let i : number = 0; i < obj.kursdifferenzen.length; i++) {
				let elem = obj.kursdifferenzen[i];
				result += elem;
				if (i < obj.kursdifferenzen.length - 1)
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
			let a = obj.regelVerletzungen;
			if (!a) {
				result += '"regelVerletzungen" : []';
			} else {
				result += '"regelVerletzungen" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += elem;
					if (i < a.length - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.anzahlNichtZugeordnet !== "undefined") {
			result += '"anzahlNichtZugeordnet" : ' + obj.anzahlNichtZugeordnet + ',';
		}
		if (typeof obj.anzahlKollisionen !== "undefined") {
			result += '"anzahlKollisionen" : ' + obj.anzahlKollisionen + ',';
		}
		if (typeof obj.anzahlSchienenMitKollisionen !== "undefined") {
			result += '"anzahlSchienenMitKollisionen" : ' + obj.anzahlSchienenMitKollisionen + ',';
		}
		if (typeof obj.kursdifferenzen !== "undefined") {
			let a = obj.kursdifferenzen;
			if (!a) {
				result += '"kursdifferenzen" : []';
			} else {
				result += '"kursdifferenzen" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += elem;
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
