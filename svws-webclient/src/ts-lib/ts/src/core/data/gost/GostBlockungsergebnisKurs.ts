import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class GostBlockungsergebnisKurs extends JavaObject {

	/**
	 * Die ID des Kurses 
	 */
	public id : number = -1;

	/**
	 * Die ID des Kurs-Faches 
	 */
	public fachID : number = -1;

	/**
	 * Die Kursart des Kurses 
	 */
	public kursart : number = -1;

	/**
	 * Die Anzahl an Schienen die der Kurs belegen soll. Falls > 1 handelt es sich um einen Multikurs. 
	 */
	public anzahlSchienen : number = -1;

	/**
	 * Eine Liste Schüler-IDs, welche diesem Kurs zugeordnet sind. 
	 */
	public readonly schueler : Vector<Number> = new Vector();

	/**
	 * Die Schienen-IDs, denen der Kurs zugeordnet ist. 
	 */
	public readonly schienen : Vector<Number> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisKurs'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsergebnisKurs {
		const obj = JSON.parse(json);
		const result = new GostBlockungsergebnisKurs();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.fachID === "undefined")
			 throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		if (typeof obj.kursart === "undefined")
			 throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (typeof obj.anzahlSchienen === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlSchienen');
		result.anzahlSchienen = obj.anzahlSchienen;
		if (!!obj.schueler) {
			for (let elem of obj.schueler) {
				result.schueler?.add(Number(elem));
			}
		}
		if (!!obj.schienen) {
			for (let elem of obj.schienen) {
				result.schienen?.add(Number(elem));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnisKurs) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"kursart" : ' + obj.kursart + ',';
		result += '"anzahlSchienen" : ' + obj.anzahlSchienen + ',';
		if (!obj.schueler) {
			result += '"schueler" : []';
		} else {
			result += '"schueler" : [ ';
			for (let i : number = 0; i < obj.schueler.size(); i++) {
				let elem = obj.schueler.get(i);
				result += elem;
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.schienen) {
			result += '"schienen" : []';
		} else {
			result += '"schienen" : [ ';
			for (let i : number = 0; i < obj.schienen.size(); i++) {
				let elem = obj.schienen.get(i);
				result += elem;
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnisKurs>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + obj.fachID + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + obj.kursart + ',';
		}
		if (typeof obj.anzahlSchienen !== "undefined") {
			result += '"anzahlSchienen" : ' + obj.anzahlSchienen + ',';
		}
		if (typeof obj.schueler !== "undefined") {
			if (!obj.schueler) {
				result += '"schueler" : []';
			} else {
				result += '"schueler" : [ ';
				for (let i : number = 0; i < obj.schueler.size(); i++) {
					let elem = obj.schueler.get(i);
					result += elem;
					if (i < obj.schueler.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.schienen !== "undefined") {
			if (!obj.schienen) {
				result += '"schienen" : []';
			} else {
				result += '"schienen" : [ ';
				for (let i : number = 0; i < obj.schienen.size(); i++) {
					let elem = obj.schienen.get(i);
					result += elem;
					if (i < obj.schienen.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisKurs(obj : unknown) : GostBlockungsergebnisKurs {
	return obj as GostBlockungsergebnisKurs;
}
