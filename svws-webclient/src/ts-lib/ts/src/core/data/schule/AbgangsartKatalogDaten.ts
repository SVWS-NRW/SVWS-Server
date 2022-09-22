import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { SchulformGliederungJahrgaenge, cast_de_nrw_schule_svws_core_data_schule_SchulformGliederungJahrgaenge } from '../../../core/data/schule/SchulformGliederungJahrgaenge';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class AbgangsartKatalogDaten extends JavaObject {

	public id : number = -1;

	public beschreibung : String = "";

	public zulaessig : List<SchulformGliederungJahrgaenge> = new Vector();

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.AbgangsartKatalogDaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): AbgangsartKatalogDaten {
		const obj = JSON.parse(json);
		const result = new AbgangsartKatalogDaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.beschreibung === "undefined")
			 throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		if (!!obj.zulaessig) {
			for (let elem of obj.zulaessig) {
				result.zulaessig?.add(SchulformGliederungJahrgaenge.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : AbgangsartKatalogDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"beschreibung" : ' + '"' + obj.beschreibung.valueOf() + '"' + ',';
		if (!obj.zulaessig) {
			result += '"zulaessig" : []';
		} else {
			result += '"zulaessig" : [ ';
			for (let i : number = 0; i < obj.zulaessig.size(); i++) {
				let elem = obj.zulaessig.get(i);
				result += SchulformGliederungJahrgaenge.transpilerToJSON(elem);
				if (i < obj.zulaessig.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AbgangsartKatalogDaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + '"' + obj.beschreibung.valueOf() + '"' + ',';
		}
		if (typeof obj.zulaessig !== "undefined") {
			if (!obj.zulaessig) {
				result += '"zulaessig" : []';
			} else {
				result += '"zulaessig" : [ ';
				for (let i : number = 0; i < obj.zulaessig.size(); i++) {
					let elem = obj.zulaessig.get(i);
					result += SchulformGliederungJahrgaenge.transpilerToJSON(elem);
					if (i < obj.zulaessig.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.gueltigVon !== "undefined") {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schule_AbgangsartKatalogDaten(obj : unknown) : AbgangsartKatalogDaten {
	return obj as AbgangsartKatalogDaten;
}
