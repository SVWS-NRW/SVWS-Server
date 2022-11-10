import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class SchuelerListeEintrag extends JavaObject {

	public id : number = 0;

	public nachname : String = "";

	public vorname : String = "";

	public idKlasse : Number = -1;

	public jahrgang : String = "";

	public schulgliederung : String = "";

	public status : String = "";

	public idSchuljahresabschnitt : Number = -1;

	public readonly kurse : Vector<Number> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schueler.SchuelerListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerListeEintrag {
		const obj = JSON.parse(json);
		const result = new SchuelerListeEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.nachname === "undefined")
			 throw new Error('invalid json format, missing attribute nachname');
		result.nachname = String(obj.nachname);
		if (typeof obj.vorname === "undefined")
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = String(obj.vorname);
		if (typeof obj.idKlasse === "undefined")
			 throw new Error('invalid json format, missing attribute idKlasse');
		result.idKlasse = Number(obj.idKlasse);
		if (typeof obj.jahrgang === "undefined")
			 throw new Error('invalid json format, missing attribute jahrgang');
		result.jahrgang = String(obj.jahrgang);
		if (typeof obj.schulgliederung === "undefined")
			 throw new Error('invalid json format, missing attribute schulgliederung');
		result.schulgliederung = String(obj.schulgliederung);
		if (typeof obj.status === "undefined")
			 throw new Error('invalid json format, missing attribute status');
		result.status = String(obj.status);
		if (typeof obj.idSchuljahresabschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = Number(obj.idSchuljahresabschnitt);
		if (!!obj.kurse) {
			for (let elem of obj.kurse) {
				result.kurse?.add(Number(elem));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuelerListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"nachname" : ' + '"' + obj.nachname.valueOf() + '"' + ',';
		result += '"vorname" : ' + '"' + obj.vorname.valueOf() + '"' + ',';
		result += '"idKlasse" : ' + obj.idKlasse.valueOf() + ',';
		result += '"jahrgang" : ' + '"' + obj.jahrgang.valueOf() + '"' + ',';
		result += '"schulgliederung" : ' + '"' + obj.schulgliederung.valueOf() + '"' + ',';
		result += '"status" : ' + '"' + obj.status.valueOf() + '"' + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.valueOf() + ',';
		if (!obj.kurse) {
			result += '"kurse" : []';
		} else {
			result += '"kurse" : [ ';
			for (let i : number = 0; i < obj.kurse.size(); i++) {
				let elem = obj.kurse.get(i);
				result += elem;
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerListeEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + '"' + obj.nachname.valueOf() + '"' + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + '"' + obj.vorname.valueOf() + '"' + ',';
		}
		if (typeof obj.idKlasse !== "undefined") {
			result += '"idKlasse" : ' + obj.idKlasse.valueOf() + ',';
		}
		if (typeof obj.jahrgang !== "undefined") {
			result += '"jahrgang" : ' + '"' + obj.jahrgang.valueOf() + '"' + ',';
		}
		if (typeof obj.schulgliederung !== "undefined") {
			result += '"schulgliederung" : ' + '"' + obj.schulgliederung.valueOf() + '"' + ',';
		}
		if (typeof obj.status !== "undefined") {
			result += '"status" : ' + '"' + obj.status.valueOf() + '"' + ',';
		}
		if (typeof obj.idSchuljahresabschnitt !== "undefined") {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.valueOf() + ',';
		}
		if (typeof obj.kurse !== "undefined") {
			if (!obj.kurse) {
				result += '"kurse" : []';
			} else {
				result += '"kurse" : [ ';
				for (let i : number = 0; i < obj.kurse.size(); i++) {
					let elem = obj.kurse.get(i);
					result += elem;
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

export function cast_de_nrw_schule_svws_core_data_schueler_SchuelerListeEintrag(obj : unknown) : SchuelerListeEintrag {
	return obj as SchuelerListeEintrag;
}
