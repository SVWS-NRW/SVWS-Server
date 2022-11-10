import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { ENMTeilleistung, cast_de_nrw_schule_svws_core_data_enm_ENMTeilleistung } from '../../../core/data/enm/ENMTeilleistung';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class ENMLeistung extends JavaObject {

	public id : number = 0;

	public lerngruppenID : number = 0;

	public note : String | null = null;

	public istSchriftlich : Boolean | null = null;

	public abiturfach : Number | null = null;

	public fehlstundenGesamt : Number | null = null;

	public fehlstundenUnentschuldigt : Number | null = null;

	public fachbezogeneBemerkungen : String | null = null;

	public neueZuweisungKursart : String | null = null;

	public istGemahnt : Boolean | null = null;

	public mahndatum : String | null = null;

	public teilleistungen : Vector<ENMTeilleistung> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMLeistung'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMLeistung {
		const obj = JSON.parse(json);
		const result = new ENMLeistung();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.lerngruppenID === "undefined")
			 throw new Error('invalid json format, missing attribute lerngruppenID');
		result.lerngruppenID = obj.lerngruppenID;
		result.note = typeof obj.note === "undefined" ? null : obj.note === null ? null : String(obj.note);
		result.istSchriftlich = typeof obj.istSchriftlich === "undefined" ? null : obj.istSchriftlich === null ? null : Boolean(obj.istSchriftlich);
		result.abiturfach = typeof obj.abiturfach === "undefined" ? null : obj.abiturfach === null ? null : Number(obj.abiturfach);
		result.fehlstundenGesamt = typeof obj.fehlstundenGesamt === "undefined" ? null : obj.fehlstundenGesamt === null ? null : Number(obj.fehlstundenGesamt);
		result.fehlstundenUnentschuldigt = typeof obj.fehlstundenUnentschuldigt === "undefined" ? null : obj.fehlstundenUnentschuldigt === null ? null : Number(obj.fehlstundenUnentschuldigt);
		result.fachbezogeneBemerkungen = typeof obj.fachbezogeneBemerkungen === "undefined" ? null : obj.fachbezogeneBemerkungen === null ? null : String(obj.fachbezogeneBemerkungen);
		result.neueZuweisungKursart = typeof obj.neueZuweisungKursart === "undefined" ? null : obj.neueZuweisungKursart === null ? null : String(obj.neueZuweisungKursart);
		result.istGemahnt = typeof obj.istGemahnt === "undefined" ? null : obj.istGemahnt === null ? null : Boolean(obj.istGemahnt);
		result.mahndatum = typeof obj.mahndatum === "undefined" ? null : obj.mahndatum === null ? null : String(obj.mahndatum);
		if (!!obj.teilleistungen) {
			for (let elem of obj.teilleistungen) {
				result.teilleistungen?.add(ENMTeilleistung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : ENMLeistung) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"lerngruppenID" : ' + obj.lerngruppenID + ',';
		result += '"note" : ' + ((!obj.note) ? 'null' : '"' + obj.note.valueOf() + '"') + ',';
		result += '"istSchriftlich" : ' + ((!obj.istSchriftlich) ? 'null' : obj.istSchriftlich.valueOf()) + ',';
		result += '"abiturfach" : ' + ((!obj.abiturfach) ? 'null' : obj.abiturfach.valueOf()) + ',';
		result += '"fehlstundenGesamt" : ' + ((!obj.fehlstundenGesamt) ? 'null' : obj.fehlstundenGesamt.valueOf()) + ',';
		result += '"fehlstundenUnentschuldigt" : ' + ((!obj.fehlstundenUnentschuldigt) ? 'null' : obj.fehlstundenUnentschuldigt.valueOf()) + ',';
		result += '"fachbezogeneBemerkungen" : ' + ((!obj.fachbezogeneBemerkungen) ? 'null' : '"' + obj.fachbezogeneBemerkungen.valueOf() + '"') + ',';
		result += '"neueZuweisungKursart" : ' + ((!obj.neueZuweisungKursart) ? 'null' : '"' + obj.neueZuweisungKursart.valueOf() + '"') + ',';
		result += '"istGemahnt" : ' + ((!obj.istGemahnt) ? 'null' : obj.istGemahnt.valueOf()) + ',';
		result += '"mahndatum" : ' + ((!obj.mahndatum) ? 'null' : '"' + obj.mahndatum.valueOf() + '"') + ',';
		if (!obj.teilleistungen) {
			result += '"teilleistungen" : []';
		} else {
			result += '"teilleistungen" : [ ';
			for (let i : number = 0; i < obj.teilleistungen.size(); i++) {
				let elem = obj.teilleistungen.get(i);
				result += ENMTeilleistung.transpilerToJSON(elem);
				if (i < obj.teilleistungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMLeistung>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.lerngruppenID !== "undefined") {
			result += '"lerngruppenID" : ' + obj.lerngruppenID + ',';
		}
		if (typeof obj.note !== "undefined") {
			result += '"note" : ' + ((!obj.note) ? 'null' : '"' + obj.note.valueOf() + '"') + ',';
		}
		if (typeof obj.istSchriftlich !== "undefined") {
			result += '"istSchriftlich" : ' + ((!obj.istSchriftlich) ? 'null' : obj.istSchriftlich.valueOf()) + ',';
		}
		if (typeof obj.abiturfach !== "undefined") {
			result += '"abiturfach" : ' + ((!obj.abiturfach) ? 'null' : obj.abiturfach.valueOf()) + ',';
		}
		if (typeof obj.fehlstundenGesamt !== "undefined") {
			result += '"fehlstundenGesamt" : ' + ((!obj.fehlstundenGesamt) ? 'null' : obj.fehlstundenGesamt.valueOf()) + ',';
		}
		if (typeof obj.fehlstundenUnentschuldigt !== "undefined") {
			result += '"fehlstundenUnentschuldigt" : ' + ((!obj.fehlstundenUnentschuldigt) ? 'null' : obj.fehlstundenUnentschuldigt.valueOf()) + ',';
		}
		if (typeof obj.fachbezogeneBemerkungen !== "undefined") {
			result += '"fachbezogeneBemerkungen" : ' + ((!obj.fachbezogeneBemerkungen) ? 'null' : '"' + obj.fachbezogeneBemerkungen.valueOf() + '"') + ',';
		}
		if (typeof obj.neueZuweisungKursart !== "undefined") {
			result += '"neueZuweisungKursart" : ' + ((!obj.neueZuweisungKursart) ? 'null' : '"' + obj.neueZuweisungKursart.valueOf() + '"') + ',';
		}
		if (typeof obj.istGemahnt !== "undefined") {
			result += '"istGemahnt" : ' + ((!obj.istGemahnt) ? 'null' : obj.istGemahnt.valueOf()) + ',';
		}
		if (typeof obj.mahndatum !== "undefined") {
			result += '"mahndatum" : ' + ((!obj.mahndatum) ? 'null' : '"' + obj.mahndatum.valueOf() + '"') + ',';
		}
		if (typeof obj.teilleistungen !== "undefined") {
			if (!obj.teilleistungen) {
				result += '"teilleistungen" : []';
			} else {
				result += '"teilleistungen" : [ ';
				for (let i : number = 0; i < obj.teilleistungen.size(); i++) {
					let elem = obj.teilleistungen.get(i);
					result += ENMTeilleistung.transpilerToJSON(elem);
					if (i < obj.teilleistungen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMLeistung(obj : unknown) : ENMLeistung {
	return obj as ENMLeistung;
}
