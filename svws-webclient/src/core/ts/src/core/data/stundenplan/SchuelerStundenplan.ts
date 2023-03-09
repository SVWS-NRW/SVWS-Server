import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { SchuelerStundenplanUnterricht, cast_de_nrw_schule_svws_core_data_stundenplan_SchuelerStundenplanUnterricht } from '../../../core/data/stundenplan/SchuelerStundenplanUnterricht';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { StundenplanZeitraster, cast_de_nrw_schule_svws_core_data_stundenplan_StundenplanZeitraster } from '../../../core/data/stundenplan/StundenplanZeitraster';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class SchuelerStundenplan extends JavaObject {

	/**
	 * Die ID des Stundenplans. 
	 */
	public idStundenplan : number = -1;

	/**
	 * Die textuelle Beschreibung des Stundenplans. 
	 */
	public bezeichnungStundenplan : string = "";

	/**
	 * Die ID des Schuljahresabschnitts des Stundenplans. 
	 */
	public idSchuljahresabschnitt : number = -1;

	/**
	 * Das Zeitraster des Stundenplans. 
	 */
	public zeitraster : List<StundenplanZeitraster> = new Vector();

	/**
	 * Das Datum, ab dem der Stundenpland gültig ist. 
	 */
	public gueltigAb : string = "";

	/**
	 * Das Datum, bis wann der Stundenplan gültig ist. 
	 */
	public gueltigBis : string = "";

	/**
	 * Die ID des Schülers. 
	 */
	public idSchueler : number = -1;

	/**
	 * Der Nachname des Schülers. 
	 */
	public nachname : string = "";

	/**
	 * Der Vorname des Schülers. 
	 */
	public vorname : string = "";

	/**
	 * Die ID der Klasse des Schülers. 
	 */
	public idKlasse : number = -1;

	/**
	 * Der Statistik-Jahrgang des Schülers. 
	 */
	public jahrgang : string = "";

	/**
	 * Der Unterricht des Schülers. 
	 */
	public unterricht : List<SchuelerStundenplanUnterricht> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.stundenplan.SchuelerStundenplan'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerStundenplan {
		const obj = JSON.parse(json);
		const result = new SchuelerStundenplan();
		if (typeof obj.idStundenplan === "undefined")
			 throw new Error('invalid json format, missing attribute idStundenplan');
		result.idStundenplan = obj.idStundenplan;
		if (typeof obj.bezeichnungStundenplan === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnungStundenplan');
		result.bezeichnungStundenplan = obj.bezeichnungStundenplan;
		if (typeof obj.idSchuljahresabschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (!!obj.zeitraster) {
			for (let elem of obj.zeitraster) {
				result.zeitraster?.add(StundenplanZeitraster.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (typeof obj.gueltigAb === "undefined")
			 throw new Error('invalid json format, missing attribute gueltigAb');
		result.gueltigAb = obj.gueltigAb;
		if (typeof obj.gueltigBis === "undefined")
			 throw new Error('invalid json format, missing attribute gueltigBis');
		result.gueltigBis = obj.gueltigBis;
		if (typeof obj.idSchueler === "undefined")
			 throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		if (typeof obj.nachname === "undefined")
			 throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (typeof obj.vorname === "undefined")
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (typeof obj.idKlasse === "undefined")
			 throw new Error('invalid json format, missing attribute idKlasse');
		result.idKlasse = obj.idKlasse;
		if (typeof obj.jahrgang === "undefined")
			 throw new Error('invalid json format, missing attribute jahrgang');
		result.jahrgang = obj.jahrgang;
		if (!!obj.unterricht) {
			for (let elem of obj.unterricht) {
				result.unterricht?.add(SchuelerStundenplanUnterricht.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuelerStundenplan) : string {
		let result = '{';
		result += '"idStundenplan" : ' + obj.idStundenplan + ',';
		result += '"bezeichnungStundenplan" : ' + '"' + obj.bezeichnungStundenplan! + '"' + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		if (!obj.zeitraster) {
			result += '"zeitraster" : []';
		} else {
			result += '"zeitraster" : [ ';
			for (let i : number = 0; i < obj.zeitraster.size(); i++) {
				let elem = obj.zeitraster.get(i);
				result += StundenplanZeitraster.transpilerToJSON(elem);
				if (i < obj.zeitraster.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"gueltigAb" : ' + '"' + obj.gueltigAb! + '"' + ',';
		result += '"gueltigBis" : ' + '"' + obj.gueltigBis! + '"' + ',';
		result += '"idSchueler" : ' + obj.idSchueler + ',';
		result += '"nachname" : ' + '"' + obj.nachname! + '"' + ',';
		result += '"vorname" : ' + '"' + obj.vorname! + '"' + ',';
		result += '"idKlasse" : ' + obj.idKlasse + ',';
		result += '"jahrgang" : ' + '"' + obj.jahrgang! + '"' + ',';
		if (!obj.unterricht) {
			result += '"unterricht" : []';
		} else {
			result += '"unterricht" : [ ';
			for (let i : number = 0; i < obj.unterricht.size(); i++) {
				let elem = obj.unterricht.get(i);
				result += SchuelerStundenplanUnterricht.transpilerToJSON(elem);
				if (i < obj.unterricht.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerStundenplan>) : string {
		let result = '{';
		if (typeof obj.idStundenplan !== "undefined") {
			result += '"idStundenplan" : ' + obj.idStundenplan + ',';
		}
		if (typeof obj.bezeichnungStundenplan !== "undefined") {
			result += '"bezeichnungStundenplan" : ' + '"' + obj.bezeichnungStundenplan + '"' + ',';
		}
		if (typeof obj.idSchuljahresabschnitt !== "undefined") {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		}
		if (typeof obj.zeitraster !== "undefined") {
			if (!obj.zeitraster) {
				result += '"zeitraster" : []';
			} else {
				result += '"zeitraster" : [ ';
				for (let i : number = 0; i < obj.zeitraster.size(); i++) {
					let elem = obj.zeitraster.get(i);
					result += StundenplanZeitraster.transpilerToJSON(elem);
					if (i < obj.zeitraster.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.gueltigAb !== "undefined") {
			result += '"gueltigAb" : ' + '"' + obj.gueltigAb + '"' + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + '"' + obj.gueltigBis + '"' + ',';
		}
		if (typeof obj.idSchueler !== "undefined") {
			result += '"idSchueler" : ' + obj.idSchueler + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + '"' + obj.nachname + '"' + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + '"' + obj.vorname + '"' + ',';
		}
		if (typeof obj.idKlasse !== "undefined") {
			result += '"idKlasse" : ' + obj.idKlasse + ',';
		}
		if (typeof obj.jahrgang !== "undefined") {
			result += '"jahrgang" : ' + '"' + obj.jahrgang + '"' + ',';
		}
		if (typeof obj.unterricht !== "undefined") {
			if (!obj.unterricht) {
				result += '"unterricht" : []';
			} else {
				result += '"unterricht" : [ ';
				for (let i : number = 0; i < obj.unterricht.size(); i++) {
					let elem = obj.unterricht.get(i);
					result += SchuelerStundenplanUnterricht.transpilerToJSON(elem);
					if (i < obj.unterricht.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_stundenplan_SchuelerStundenplan(obj : unknown) : SchuelerStundenplan {
	return obj as SchuelerStundenplan;
}
