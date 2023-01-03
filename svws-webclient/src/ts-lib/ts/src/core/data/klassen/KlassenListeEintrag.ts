import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class KlassenListeEintrag extends JavaObject {

	public id : number = 0;

	public kuerzel : String | null = null;

	public idJahrgang : Number | null = null;

	public parallelitaet : String | null = null;

	public sortierung : number = 0;

	public istSichtbar : boolean = false;

	public klassenLehrer : Vector<Number> | null = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.klassen.KlassenListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): KlassenListeEintrag {
		const obj = JSON.parse(json);
		const result = new KlassenListeEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : String(obj.kuerzel);
		result.idJahrgang = typeof obj.idJahrgang === "undefined" ? null : obj.idJahrgang === null ? null : Number(obj.idJahrgang);
		result.parallelitaet = typeof obj.parallelitaet === "undefined" ? null : obj.parallelitaet === null ? null : String(obj.parallelitaet);
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (!!obj.klassenLehrer) {
			for (let elem of obj.klassenLehrer) {
				result.klassenLehrer?.add(Number(elem));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : KlassenListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		result += '"idJahrgang" : ' + ((!obj.idJahrgang) ? 'null' : obj.idJahrgang.valueOf()) + ',';
		result += '"parallelitaet" : ' + ((!obj.parallelitaet) ? 'null' : '"' + obj.parallelitaet.valueOf() + '"') + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		if (!obj.klassenLehrer) {
			result += '"klassenLehrer" : []';
		} else {
			result += '"klassenLehrer" : [ ';
			for (let i : number = 0; i < obj.klassenLehrer.size(); i++) {
				let elem = obj.klassenLehrer.get(i);
				result += elem;
				if (i < obj.klassenLehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KlassenListeEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.idJahrgang !== "undefined") {
			result += '"idJahrgang" : ' + ((!obj.idJahrgang) ? 'null' : obj.idJahrgang.valueOf()) + ',';
		}
		if (typeof obj.parallelitaet !== "undefined") {
			result += '"parallelitaet" : ' + ((!obj.parallelitaet) ? 'null' : '"' + obj.parallelitaet.valueOf() + '"') + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.istSichtbar !== "undefined") {
			result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		}
		if (typeof obj.klassenLehrer !== "undefined") {
			if (!obj.klassenLehrer) {
				result += '"klassenLehrer" : []';
			} else {
				result += '"klassenLehrer" : [ ';
				for (let i : number = 0; i < obj.klassenLehrer.size(); i++) {
					let elem = obj.klassenLehrer.get(i);
					result += elem;
					if (i < obj.klassenLehrer.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_klassen_KlassenListeEintrag(obj : unknown) : KlassenListeEintrag {
	return obj as KlassenListeEintrag;
}
