import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { Sprachbelegung, cast_de_nrw_schule_svws_core_data_schueler_Sprachbelegung } from '../../../core/data/schueler/Sprachbelegung';
import { Sprachpruefung, cast_de_nrw_schule_svws_core_data_schueler_Sprachpruefung } from '../../../core/data/schueler/Sprachpruefung';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class Sprachendaten extends JavaObject {

	public schuelerID : number = 0;

	public belegungen : Vector<Sprachbelegung> = new Vector();

	public pruefungen : Vector<Sprachpruefung> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schueler.Sprachendaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): Sprachendaten {
		const obj = JSON.parse(json);
		const result = new Sprachendaten();
		if (typeof obj.schuelerID === "undefined")
			 throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (!!obj.belegungen) {
			for (let elem of obj.belegungen) {
				result.belegungen?.add(Sprachbelegung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.pruefungen) {
			for (let elem of obj.pruefungen) {
				result.pruefungen?.add(Sprachpruefung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : Sprachendaten) : string {
		let result = '{';
		result += '"schuelerID" : ' + obj.schuelerID + ',';
		if (!obj.belegungen) {
			result += '"belegungen" : []';
		} else {
			result += '"belegungen" : [ ';
			for (let i : number = 0; i < obj.belegungen.size(); i++) {
				let elem = obj.belegungen.get(i);
				result += Sprachbelegung.transpilerToJSON(elem);
				if (i < obj.belegungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.pruefungen) {
			result += '"pruefungen" : []';
		} else {
			result += '"pruefungen" : [ ';
			for (let i : number = 0; i < obj.pruefungen.size(); i++) {
				let elem = obj.pruefungen.get(i);
				result += Sprachpruefung.transpilerToJSON(elem);
				if (i < obj.pruefungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Sprachendaten>) : string {
		let result = '{';
		if (typeof obj.schuelerID !== "undefined") {
			result += '"schuelerID" : ' + obj.schuelerID + ',';
		}
		if (typeof obj.belegungen !== "undefined") {
			if (!obj.belegungen) {
				result += '"belegungen" : []';
			} else {
				result += '"belegungen" : [ ';
				for (let i : number = 0; i < obj.belegungen.size(); i++) {
					let elem = obj.belegungen.get(i);
					result += Sprachbelegung.transpilerToJSON(elem);
					if (i < obj.belegungen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.pruefungen !== "undefined") {
			if (!obj.pruefungen) {
				result += '"pruefungen" : []';
			} else {
				result += '"pruefungen" : [ ';
				for (let i : number = 0; i < obj.pruefungen.size(); i++) {
					let elem = obj.pruefungen.get(i);
					result += Sprachpruefung.transpilerToJSON(elem);
					if (i < obj.pruefungen.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_schueler_Sprachendaten(obj : unknown) : Sprachendaten {
	return obj as Sprachendaten;
}
