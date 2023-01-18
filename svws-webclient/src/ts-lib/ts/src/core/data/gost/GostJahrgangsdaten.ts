import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostBeratungslehrer, cast_de_nrw_schule_svws_core_data_gost_GostBeratungslehrer } from '../../../core/data/gost/GostBeratungslehrer';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class GostJahrgangsdaten extends JavaObject {

	/**
	 * Das Jahr, in welchem der Jahrgang Abitur machen wird oder -1 für die Vorlage für einen neuen Abiturjahrgang. 
	 */
	public abiturjahr : number = 0;

	/**
	 * Die aktuelle Jahrgangsstufe, welche dem Abiturjahrgang zugeordnet ist. 
	 */
	public jahrgang : String | null = null;

	/**
	 * Die textuelle Bezeichnung für den Abiturjahrgang 
	 */
	public bezeichnung : String | null = null;

	/**
	 * Gibt an, ob das Abitur für diesen Jahrgang berets abgeschlossen ist, d.h. die Schule sich bereits in einem späteren Schuljahr befindet. 
	 */
	public istAbgeschlossen : boolean = false;

	/**
	 * Der derzeitige Beratungstext, welcher auf einem Ausdruck eines Schülerlaufbahnbogens für die 
	 *  gymnasiale Oberstufe gedruckt wird. 
	 */
	public textBeratungsbogen : String | null = null;

	/**
	 * Der derzeitige Text, der beim Versenden einer Beratungsdatei per Mail verwendet wird. 
	 */
	public textMailversand : String | null = null;

	/**
	 * Legt fest, ob ein Zusatzkurs in Geschichte angeboten wird. 
	 */
	public hatZusatzkursGE : boolean = true;

	/**
	 * Das eindeutige Kürzel des Halbjahrs, zu dem ein Zusatzkurs in Geschichte beginnt. 
	 */
	public beginnZusatzkursGE : String | null = null;

	/**
	 * Legt fest, ob ein Zusatzkurs in Sozialwissenschaften angeboten wird. 
	 */
	public hatZusatzkursSW : boolean = true;

	/**
	 * Das eindeutige Kürzel des Halbjahrs, zu dem ein Zusatzkurs in Sozialwissenschaften beginnt. 
	 */
	public beginnZusatzkursSW : String | null = null;

	/**
	 * Gibt an, ob für die jeweilige Halbjahre der Oberstufe bereits eine Blockung in den Leistungsdaten persistiert wurde (0 = EF.1, 1=EF.2, ...) 
	 */
	public istBlockungFestgelegt : Array<boolean> = Array(6).fill(false);

	/**
	 * Die Liste der Beratungslehrer für diesen Jahrgang der gymnasialen Oberstufe 
	 */
	public readonly beratungslehrer : Vector<GostBeratungslehrer> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostJahrgangsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostJahrgangsdaten {
		const obj = JSON.parse(json);
		const result = new GostJahrgangsdaten();
		if (typeof obj.abiturjahr === "undefined")
			 throw new Error('invalid json format, missing attribute abiturjahr');
		result.abiturjahr = obj.abiturjahr;
		result.jahrgang = typeof obj.jahrgang === "undefined" ? null : obj.jahrgang === null ? null : String(obj.jahrgang);
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : String(obj.bezeichnung);
		if (typeof obj.istAbgeschlossen === "undefined")
			 throw new Error('invalid json format, missing attribute istAbgeschlossen');
		result.istAbgeschlossen = obj.istAbgeschlossen;
		result.textBeratungsbogen = typeof obj.textBeratungsbogen === "undefined" ? null : obj.textBeratungsbogen === null ? null : String(obj.textBeratungsbogen);
		result.textMailversand = typeof obj.textMailversand === "undefined" ? null : obj.textMailversand === null ? null : String(obj.textMailversand);
		if (typeof obj.hatZusatzkursGE === "undefined")
			 throw new Error('invalid json format, missing attribute hatZusatzkursGE');
		result.hatZusatzkursGE = obj.hatZusatzkursGE;
		result.beginnZusatzkursGE = typeof obj.beginnZusatzkursGE === "undefined" ? null : obj.beginnZusatzkursGE === null ? null : String(obj.beginnZusatzkursGE);
		if (typeof obj.hatZusatzkursSW === "undefined")
			 throw new Error('invalid json format, missing attribute hatZusatzkursSW');
		result.hatZusatzkursSW = obj.hatZusatzkursSW;
		result.beginnZusatzkursSW = typeof obj.beginnZusatzkursSW === "undefined" ? null : obj.beginnZusatzkursSW === null ? null : String(obj.beginnZusatzkursSW);
		for (let i : number = 0; i < obj.istBlockungFestgelegt.length; i++) {
			result.istBlockungFestgelegt[i] = obj.istBlockungFestgelegt[i];
		}
		if (!!obj.beratungslehrer) {
			for (let elem of obj.beratungslehrer) {
				result.beratungslehrer?.add(GostBeratungslehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostJahrgangsdaten) : string {
		let result = '{';
		result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : '"' + obj.jahrgang.valueOf() + '"') + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		result += '"istAbgeschlossen" : ' + obj.istAbgeschlossen + ',';
		result += '"textBeratungsbogen" : ' + ((!obj.textBeratungsbogen) ? 'null' : '"' + obj.textBeratungsbogen.valueOf() + '"') + ',';
		result += '"textMailversand" : ' + ((!obj.textMailversand) ? 'null' : '"' + obj.textMailversand.valueOf() + '"') + ',';
		result += '"hatZusatzkursGE" : ' + obj.hatZusatzkursGE + ',';
		result += '"beginnZusatzkursGE" : ' + ((!obj.beginnZusatzkursGE) ? 'null' : '"' + obj.beginnZusatzkursGE.valueOf() + '"') + ',';
		result += '"hatZusatzkursSW" : ' + obj.hatZusatzkursSW + ',';
		result += '"beginnZusatzkursSW" : ' + ((!obj.beginnZusatzkursSW) ? 'null' : '"' + obj.beginnZusatzkursSW.valueOf() + '"') + ',';
		if (!obj.istBlockungFestgelegt) {
			result += '"istBlockungFestgelegt" : []';
		} else {
			result += '"istBlockungFestgelegt" : [ ';
			for (let i : number = 0; i < obj.istBlockungFestgelegt.length; i++) {
				let elem = obj.istBlockungFestgelegt[i];
				result += JSON.stringify(elem);
				if (i < obj.istBlockungFestgelegt.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.beratungslehrer) {
			result += '"beratungslehrer" : []';
		} else {
			result += '"beratungslehrer" : [ ';
			for (let i : number = 0; i < obj.beratungslehrer.size(); i++) {
				let elem = obj.beratungslehrer.get(i);
				result += GostBeratungslehrer.transpilerToJSON(elem);
				if (i < obj.beratungslehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostJahrgangsdaten>) : string {
		let result = '{';
		if (typeof obj.abiturjahr !== "undefined") {
			result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		}
		if (typeof obj.jahrgang !== "undefined") {
			result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : '"' + obj.jahrgang.valueOf() + '"') + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		}
		if (typeof obj.istAbgeschlossen !== "undefined") {
			result += '"istAbgeschlossen" : ' + obj.istAbgeschlossen + ',';
		}
		if (typeof obj.textBeratungsbogen !== "undefined") {
			result += '"textBeratungsbogen" : ' + ((!obj.textBeratungsbogen) ? 'null' : '"' + obj.textBeratungsbogen.valueOf() + '"') + ',';
		}
		if (typeof obj.textMailversand !== "undefined") {
			result += '"textMailversand" : ' + ((!obj.textMailversand) ? 'null' : '"' + obj.textMailversand.valueOf() + '"') + ',';
		}
		if (typeof obj.hatZusatzkursGE !== "undefined") {
			result += '"hatZusatzkursGE" : ' + obj.hatZusatzkursGE + ',';
		}
		if (typeof obj.beginnZusatzkursGE !== "undefined") {
			result += '"beginnZusatzkursGE" : ' + ((!obj.beginnZusatzkursGE) ? 'null' : '"' + obj.beginnZusatzkursGE.valueOf() + '"') + ',';
		}
		if (typeof obj.hatZusatzkursSW !== "undefined") {
			result += '"hatZusatzkursSW" : ' + obj.hatZusatzkursSW + ',';
		}
		if (typeof obj.beginnZusatzkursSW !== "undefined") {
			result += '"beginnZusatzkursSW" : ' + ((!obj.beginnZusatzkursSW) ? 'null' : '"' + obj.beginnZusatzkursSW.valueOf() + '"') + ',';
		}
		if (typeof obj.istBlockungFestgelegt !== "undefined") {
			let a = obj.istBlockungFestgelegt;
			if (!a) {
				result += '"istBlockungFestgelegt" : []';
			} else {
				result += '"istBlockungFestgelegt" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += JSON.stringify(elem);
					if (i < a.length - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.beratungslehrer !== "undefined") {
			if (!obj.beratungslehrer) {
				result += '"beratungslehrer" : []';
			} else {
				result += '"beratungslehrer" : [ ';
				for (let i : number = 0; i < obj.beratungslehrer.size(); i++) {
					let elem = obj.beratungslehrer.get(i);
					result += GostBeratungslehrer.transpilerToJSON(elem);
					if (i < obj.beratungslehrer.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_gost_GostJahrgangsdaten(obj : unknown) : GostJahrgangsdaten {
	return obj as GostJahrgangsdaten;
}
