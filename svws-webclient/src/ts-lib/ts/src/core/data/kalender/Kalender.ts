import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { KalenderEintrag, cast_de_nrw_schule_svws_core_data_kalender_KalenderEintrag } from '../../../core/data/kalender/KalenderEintrag';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class Kalender extends JavaObject {

	public id : String = "";

	public displayname : String | null = null;

	public beschreibung : String | null = null;

	public synctoken : number = 0;

	public kalenderTyp : String = "";

	public kalenderEintraege : List<KalenderEintrag> = new Vector();

	public darfSchreiben : boolean = false;

	public darfLesen : boolean = false;

	public besitzer : number = 0;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kalender.Kalender'].includes(name);
	}

	public static transpilerFromJSON(json : string): Kalender {
		const obj = JSON.parse(json);
		const result = new Kalender();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = String(obj.id);
		result.displayname = typeof obj.displayname === "undefined" ? null : obj.displayname === null ? null : String(obj.displayname);
		result.beschreibung = typeof obj.beschreibung === "undefined" ? null : obj.beschreibung === null ? null : String(obj.beschreibung);
		if (typeof obj.synctoken === "undefined")
			 throw new Error('invalid json format, missing attribute synctoken');
		result.synctoken = obj.synctoken;
		if (typeof obj.kalenderTyp === "undefined")
			 throw new Error('invalid json format, missing attribute kalenderTyp');
		result.kalenderTyp = String(obj.kalenderTyp);
		if (!!obj.kalenderEintraege) {
			for (let elem of obj.kalenderEintraege) {
				result.kalenderEintraege?.add(KalenderEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (typeof obj.darfSchreiben === "undefined")
			 throw new Error('invalid json format, missing attribute darfSchreiben');
		result.darfSchreiben = obj.darfSchreiben;
		if (typeof obj.darfLesen === "undefined")
			 throw new Error('invalid json format, missing attribute darfLesen');
		result.darfLesen = obj.darfLesen;
		if (typeof obj.besitzer === "undefined")
			 throw new Error('invalid json format, missing attribute besitzer');
		result.besitzer = obj.besitzer;
		return result;
	}

	public static transpilerToJSON(obj : Kalender) : string {
		let result = '{';
		result += '"id" : ' + '"' + obj.id.valueOf() + '"' + ',';
		result += '"displayname" : ' + ((!obj.displayname) ? 'null' : '"' + obj.displayname.valueOf() + '"') + ',';
		result += '"beschreibung" : ' + ((!obj.beschreibung) ? 'null' : '"' + obj.beschreibung.valueOf() + '"') + ',';
		result += '"synctoken" : ' + obj.synctoken + ',';
		result += '"kalenderTyp" : ' + '"' + obj.kalenderTyp.valueOf() + '"' + ',';
		if (!obj.kalenderEintraege) {
			result += '"kalenderEintraege" : []';
		} else {
			result += '"kalenderEintraege" : [ ';
			for (let i : number = 0; i < obj.kalenderEintraege.size(); i++) {
				let elem = obj.kalenderEintraege.get(i);
				result += KalenderEintrag.transpilerToJSON(elem);
				if (i < obj.kalenderEintraege.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"darfSchreiben" : ' + obj.darfSchreiben + ',';
		result += '"darfLesen" : ' + obj.darfLesen + ',';
		result += '"besitzer" : ' + obj.besitzer + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Kalender>) : string {
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
		if (typeof obj.kalenderTyp !== "undefined") {
			result += '"kalenderTyp" : ' + '"' + obj.kalenderTyp.valueOf() + '"' + ',';
		}
		if (typeof obj.kalenderEintraege !== "undefined") {
			if (!obj.kalenderEintraege) {
				result += '"kalenderEintraege" : []';
			} else {
				result += '"kalenderEintraege" : [ ';
				for (let i : number = 0; i < obj.kalenderEintraege.size(); i++) {
					let elem = obj.kalenderEintraege.get(i);
					result += KalenderEintrag.transpilerToJSON(elem);
					if (i < obj.kalenderEintraege.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.darfSchreiben !== "undefined") {
			result += '"darfSchreiben" : ' + obj.darfSchreiben + ',';
		}
		if (typeof obj.darfLesen !== "undefined") {
			result += '"darfLesen" : ' + obj.darfLesen + ',';
		}
		if (typeof obj.besitzer !== "undefined") {
			result += '"besitzer" : ' + obj.besitzer + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_kalender_Kalender(obj : unknown) : Kalender {
	return obj as Kalender;
}
