import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostBlockungSchiene, cast_de_nrw_schule_svws_core_data_gost_GostBlockungSchiene } from '../../../core/data/gost/GostBlockungSchiene';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { GostBlockungRegel, cast_de_nrw_schule_svws_core_data_gost_GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { GostBlockungKurs, cast_de_nrw_schule_svws_core_data_gost_GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';

export class GostBlockungsdaten extends JavaObject {

	public id : number = -1;

	public name : String = "Neue Blockung";

	public gostHalbjahr : number = 0;

	public ergebnisID : Number | null = null;

	public schienen : Vector<GostBlockungSchiene> = new Vector();

	public regeln : Vector<GostBlockungRegel> = new Vector();

	public kurse : Vector<GostBlockungKurs> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostBlockungsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsdaten {
		const obj = JSON.parse(json);
		const result = new GostBlockungsdaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (typeof obj.gostHalbjahr === "undefined")
			 throw new Error('invalid json format, missing attribute gostHalbjahr');
		result.gostHalbjahr = obj.gostHalbjahr;
		result.ergebnisID = typeof obj.ergebnisID === "undefined" ? null : obj.ergebnisID;
		if (!!obj.schienen) {
			for (let elem of obj.schienen) {
				result.schienen?.add(GostBlockungSchiene.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.regeln) {
			for (let elem of obj.regeln) {
				result.regeln?.add(GostBlockungRegel.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.kurse) {
			for (let elem of obj.kurse) {
				result.kurse?.add(GostBlockungKurs.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		result += '"ergebnisID" : ' + ((!obj.ergebnisID) ? 'null' : obj.ergebnisID.valueOf()) + ',';
		if (!obj.schienen) {
			result += '"schienen" : []';
		} else {
			result += '"schienen" : [ ';
			for (let i : number = 0; i < obj.schienen.size(); i++) {
				let elem = obj.schienen.get(i);
				result += GostBlockungSchiene.transpilerToJSON(elem);
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.regeln) {
			result += '"regeln" : []';
		} else {
			result += '"regeln" : [ ';
			for (let i : number = 0; i < obj.regeln.size(); i++) {
				let elem = obj.regeln.get(i);
				result += GostBlockungRegel.transpilerToJSON(elem);
				if (i < obj.regeln.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.kurse) {
			result += '"kurse" : []';
		} else {
			result += '"kurse" : [ ';
			for (let i : number = 0; i < obj.kurse.size(); i++) {
				let elem = obj.kurse.get(i);
				result += GostBlockungKurs.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsdaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		}
		if (typeof obj.gostHalbjahr !== "undefined") {
			result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		}
		if (typeof obj.ergebnisID !== "undefined") {
			result += '"ergebnisID" : ' + ((!obj.ergebnisID) ? 'null' : obj.ergebnisID.valueOf()) + ',';
		}
		if (typeof obj.schienen !== "undefined") {
			if (!obj.schienen) {
				result += '"schienen" : []';
			} else {
				result += '"schienen" : [ ';
				for (let i : number = 0; i < obj.schienen.size(); i++) {
					let elem = obj.schienen.get(i);
					result += GostBlockungSchiene.transpilerToJSON(elem);
					if (i < obj.schienen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.regeln !== "undefined") {
			if (!obj.regeln) {
				result += '"regeln" : []';
			} else {
				result += '"regeln" : [ ';
				for (let i : number = 0; i < obj.regeln.size(); i++) {
					let elem = obj.regeln.get(i);
					result += GostBlockungRegel.transpilerToJSON(elem);
					if (i < obj.regeln.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.kurse !== "undefined") {
			if (!obj.kurse) {
				result += '"kurse" : []';
			} else {
				result += '"kurse" : [ ';
				for (let i : number = 0; i < obj.kurse.size(); i++) {
					let elem = obj.kurse.get(i);
					result += GostBlockungKurs.transpilerToJSON(elem);
					if (i < obj.kurse.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_gost_GostBlockungsdaten(obj : unknown) : GostBlockungsdaten {
	return obj as GostBlockungsdaten;
}
