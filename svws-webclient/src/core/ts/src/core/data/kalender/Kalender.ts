import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { KalenderEintrag, cast_de_nrw_schule_svws_core_data_kalender_KalenderEintrag } from './KalenderEintrag';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class Kalender extends JavaObject {

	/**
	 * ID des Kalenders 
	 */
	public id : string = "";

	/**
	 * Anzeigename des Kalenders 
	 */
	public displayname : string | null = null;

	/**
	 * Beschreibung des Kalenders 
	 */
	public beschreibung : string | null = null;

	/**
	 * Versionskennzeichen des Kalenders 
	 */
	public synctoken : number = 0;

	/**
	 *  der Typ des Kalenders
	 */
	public kalenderTyp : string = "";

	/**
	 *  eine Liste der Einträge des Kalenders
	 */
	public kalenderEintraege : List<KalenderEintrag> = new Vector();

	/**
	 * ob der angemeldete Nutzer Schreibrecht auf dem Kalender hat 
	 */
	public darfSchreiben : boolean = false;

	/**
	 * ob der angemeldete Nutzer Leserecht auf dem Kalender hat 
	 */
	public darfLesen : boolean = false;

	/**
	 * BenutzerId des Besitzers dieses Kalenders 
	 */
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
		result.id = obj.id;
		result.displayname = typeof obj.displayname === "undefined" ? null : obj.displayname === null ? null : obj.displayname;
		result.beschreibung = typeof obj.beschreibung === "undefined" ? null : obj.beschreibung === null ? null : obj.beschreibung;
		if (typeof obj.synctoken === "undefined")
			 throw new Error('invalid json format, missing attribute synctoken');
		result.synctoken = obj.synctoken;
		if (typeof obj.kalenderTyp === "undefined")
			 throw new Error('invalid json format, missing attribute kalenderTyp');
		result.kalenderTyp = obj.kalenderTyp;
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
		result += '"id" : ' + '"' + obj.id! + '"' + ',';
		result += '"displayname" : ' + ((!obj.displayname) ? 'null' : '"' + obj.displayname + '"') + ',';
		result += '"beschreibung" : ' + ((!obj.beschreibung) ? 'null' : '"' + obj.beschreibung + '"') + ',';
		result += '"synctoken" : ' + obj.synctoken + ',';
		result += '"kalenderTyp" : ' + '"' + obj.kalenderTyp! + '"' + ',';
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
			result += '"id" : ' + '"' + obj.id + '"' + ',';
		}
		if (typeof obj.displayname !== "undefined") {
			result += '"displayname" : ' + ((!obj.displayname) ? 'null' : '"' + obj.displayname + '"') + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + ((!obj.beschreibung) ? 'null' : '"' + obj.beschreibung + '"') + ',';
		}
		if (typeof obj.synctoken !== "undefined") {
			result += '"synctoken" : ' + obj.synctoken + ',';
		}
		if (typeof obj.kalenderTyp !== "undefined") {
			result += '"kalenderTyp" : ' + '"' + obj.kalenderTyp + '"' + ',';
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
