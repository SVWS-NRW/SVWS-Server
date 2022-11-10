import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { AdressbuchEintrag, cast_de_nrw_schule_svws_core_data_adressbuch_AdressbuchEintrag } from '../../../core/data/adressbuch/AdressbuchEintrag';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class Adressbuch extends JavaObject {

	public id : String = "";

	public displayname : String | null = null;

	public beschreibung : String | null = null;

	public synctoken : number = 0;

	public adressbuchTyp : String = "";

	public adressbuchEintraege : List<AdressbuchEintrag> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.adressbuch.Adressbuch'].includes(name);
	}

	public static transpilerFromJSON(json : string): Adressbuch {
		const obj = JSON.parse(json);
		const result = new Adressbuch();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = String(obj.id);
		result.displayname = typeof obj.displayname === "undefined" ? null : obj.displayname === null ? null : String(obj.displayname);
		result.beschreibung = typeof obj.beschreibung === "undefined" ? null : obj.beschreibung === null ? null : String(obj.beschreibung);
		if (typeof obj.synctoken === "undefined")
			 throw new Error('invalid json format, missing attribute synctoken');
		result.synctoken = obj.synctoken;
		if (typeof obj.adressbuchTyp === "undefined")
			 throw new Error('invalid json format, missing attribute adressbuchTyp');
		result.adressbuchTyp = String(obj.adressbuchTyp);
		if (!!obj.adressbuchEintraege) {
			for (let elem of obj.adressbuchEintraege) {
				result.adressbuchEintraege?.add(AdressbuchEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : Adressbuch) : string {
		let result = '{';
		result += '"id" : ' + '"' + obj.id.valueOf() + '"' + ',';
		result += '"displayname" : ' + ((!obj.displayname) ? 'null' : '"' + obj.displayname.valueOf() + '"') + ',';
		result += '"beschreibung" : ' + ((!obj.beschreibung) ? 'null' : '"' + obj.beschreibung.valueOf() + '"') + ',';
		result += '"synctoken" : ' + obj.synctoken + ',';
		result += '"adressbuchTyp" : ' + '"' + obj.adressbuchTyp.valueOf() + '"' + ',';
		if (!obj.adressbuchEintraege) {
			result += '"adressbuchEintraege" : []';
		} else {
			result += '"adressbuchEintraege" : [ ';
			for (let i : number = 0; i < obj.adressbuchEintraege.size(); i++) {
				let elem = obj.adressbuchEintraege.get(i);
				result += AdressbuchEintrag.transpilerToJSON(elem);
				if (i < obj.adressbuchEintraege.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Adressbuch>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + '"' + obj.id.valueOf() + '"' + ',';
		}
		if (typeof obj.displayname !== "undefined") {
			result += '"displayname" : ' + ((!obj.displayname) ? 'null' : '"' + obj.displayname.valueOf() + '"') + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + ((!obj.beschreibung) ? 'null' : '"' + obj.beschreibung.valueOf() + '"') + ',';
		}
		if (typeof obj.synctoken !== "undefined") {
			result += '"synctoken" : ' + obj.synctoken + ',';
		}
		if (typeof obj.adressbuchTyp !== "undefined") {
			result += '"adressbuchTyp" : ' + '"' + obj.adressbuchTyp.valueOf() + '"' + ',';
		}
		if (typeof obj.adressbuchEintraege !== "undefined") {
			if (!obj.adressbuchEintraege) {
				result += '"adressbuchEintraege" : []';
			} else {
				result += '"adressbuchEintraege" : [ ';
				for (let i : number = 0; i < obj.adressbuchEintraege.size(); i++) {
					let elem = obj.adressbuchEintraege.get(i);
					result += AdressbuchEintrag.transpilerToJSON(elem);
					if (i < obj.adressbuchEintraege.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_adressbuch_Adressbuch(obj : unknown) : Adressbuch {
	return obj as Adressbuch;
}
