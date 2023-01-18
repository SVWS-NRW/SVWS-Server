import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { SchuelerblockungInputKurs, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungInputKurs } from '../../../core/data/kursblockung/SchuelerblockungInputKurs';
import { SchuelerblockungInputFachwahl, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungInputFachwahl } from '../../../core/data/kursblockung/SchuelerblockungInputFachwahl';
import { GostFachwahl, cast_de_nrw_schule_svws_core_data_gost_GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class SchuelerblockungInput extends JavaObject {

	/**
	 * Die Anzahl an vorhandenen Schienen. 
	 */
	public schienen : number = 0;

	/**
	 * Alle Kurse, die zu den Fachwahlen des Schülers passen. 
	 */
	public kurse : Vector<SchuelerblockungInputKurs> = new Vector();

	/**
	 * Alle Fachwahlen des Schülers. 
	 */
	public fachwahlen : Vector<GostFachwahl> = new Vector();

	/**
	 * Zu jeder Fachwahl eine textuelle Darstellung. 
	 */
	public fachwahlenText : Vector<String> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kursblockung.SchuelerblockungInput'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerblockungInput {
		const obj = JSON.parse(json);
		const result = new SchuelerblockungInput();
		if (typeof obj.schienen === "undefined")
			 throw new Error('invalid json format, missing attribute schienen');
		result.schienen = obj.schienen;
		if (!!obj.kurse) {
			for (let elem of obj.kurse) {
				result.kurse?.add(SchuelerblockungInputKurs.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.fachwahlen) {
			for (let elem of obj.fachwahlen) {
				result.fachwahlen?.add(GostFachwahl.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.fachwahlenText) {
			for (let elem of obj.fachwahlenText) {
				result.fachwahlenText?.add(String(elem));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuelerblockungInput) : string {
		let result = '{';
		result += '"schienen" : ' + obj.schienen + ',';
		if (!obj.kurse) {
			result += '"kurse" : []';
		} else {
			result += '"kurse" : [ ';
			for (let i : number = 0; i < obj.kurse.size(); i++) {
				let elem = obj.kurse.get(i);
				result += SchuelerblockungInputKurs.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.fachwahlen) {
			result += '"fachwahlen" : []';
		} else {
			result += '"fachwahlen" : [ ';
			for (let i : number = 0; i < obj.fachwahlen.size(); i++) {
				let elem = obj.fachwahlen.get(i);
				result += GostFachwahl.transpilerToJSON(elem);
				if (i < obj.fachwahlen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.fachwahlenText) {
			result += '"fachwahlenText" : []';
		} else {
			result += '"fachwahlenText" : [ ';
			for (let i : number = 0; i < obj.fachwahlenText.size(); i++) {
				let elem = obj.fachwahlenText.get(i);
				result += '"' + elem + '"';
				if (i < obj.fachwahlenText.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerblockungInput>) : string {
		let result = '{';
		if (typeof obj.schienen !== "undefined") {
			result += '"schienen" : ' + obj.schienen + ',';
		}
		if (typeof obj.kurse !== "undefined") {
			if (!obj.kurse) {
				result += '"kurse" : []';
			} else {
				result += '"kurse" : [ ';
				for (let i : number = 0; i < obj.kurse.size(); i++) {
					let elem = obj.kurse.get(i);
					result += SchuelerblockungInputKurs.transpilerToJSON(elem);
					if (i < obj.kurse.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.fachwahlen !== "undefined") {
			if (!obj.fachwahlen) {
				result += '"fachwahlen" : []';
			} else {
				result += '"fachwahlen" : [ ';
				for (let i : number = 0; i < obj.fachwahlen.size(); i++) {
					let elem = obj.fachwahlen.get(i);
					result += GostFachwahl.transpilerToJSON(elem);
					if (i < obj.fachwahlen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.fachwahlenText !== "undefined") {
			if (!obj.fachwahlenText) {
				result += '"fachwahlenText" : []';
			} else {
				result += '"fachwahlenText" : [ ';
				for (let i : number = 0; i < obj.fachwahlenText.size(); i++) {
					let elem = obj.fachwahlenText.get(i);
					result += '"' + elem + '"';
					if (i < obj.fachwahlenText.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungInput(obj : unknown) : SchuelerblockungInput {
	return obj as SchuelerblockungInput;
}
