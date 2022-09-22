import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { KlausurblockungSchienenInputSchueler, cast_de_nrw_schule_svws_core_data_klausurblockung_KlausurblockungSchienenInputSchueler } from '../../../core/data/klausurblockung/KlausurblockungSchienenInputSchueler';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class KlausurblockungSchienenInput extends JavaObject {

	public datenbankID : number = -1;

	public maxTimeMillis : number = 1000;

	public schueler : Vector<KlausurblockungSchienenInputSchueler> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.klausurblockung.KlausurblockungSchienenInput'].includes(name);
	}

	public static transpilerFromJSON(json : string): KlausurblockungSchienenInput {
		const obj = JSON.parse(json);
		const result = new KlausurblockungSchienenInput();
		if (typeof obj.datenbankID === "undefined")
			 throw new Error('invalid json format, missing attribute datenbankID');
		result.datenbankID = obj.datenbankID;
		if (typeof obj.maxTimeMillis === "undefined")
			 throw new Error('invalid json format, missing attribute maxTimeMillis');
		result.maxTimeMillis = obj.maxTimeMillis;
		if (!!obj.schueler) {
			for (let elem of obj.schueler) {
				result.schueler?.add(KlausurblockungSchienenInputSchueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : KlausurblockungSchienenInput) : string {
		let result = '{';
		result += '"datenbankID" : ' + obj.datenbankID + ',';
		result += '"maxTimeMillis" : ' + obj.maxTimeMillis + ',';
		if (!obj.schueler) {
			result += '"schueler" : []';
		} else {
			result += '"schueler" : [ ';
			for (let i : number = 0; i < obj.schueler.size(); i++) {
				let elem = obj.schueler.get(i);
				result += KlausurblockungSchienenInputSchueler.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KlausurblockungSchienenInput>) : string {
		let result = '{';
		if (typeof obj.datenbankID !== "undefined") {
			result += '"datenbankID" : ' + obj.datenbankID + ',';
		}
		if (typeof obj.maxTimeMillis !== "undefined") {
			result += '"maxTimeMillis" : ' + obj.maxTimeMillis + ',';
		}
		if (typeof obj.schueler !== "undefined") {
			if (!obj.schueler) {
				result += '"schueler" : []';
			} else {
				result += '"schueler" : [ ';
				for (let i : number = 0; i < obj.schueler.size(); i++) {
					let elem = obj.schueler.get(i);
					result += KlausurblockungSchienenInputSchueler.transpilerToJSON(elem);
					if (i < obj.schueler.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_klausurblockung_KlausurblockungSchienenInput(obj : unknown) : KlausurblockungSchienenInput {
	return obj as KlausurblockungSchienenInput;
}
