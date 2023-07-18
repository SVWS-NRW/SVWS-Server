import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanPausenaufsicht } from '../../../core/data/stundenplan/StundenplanPausenaufsicht';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { StundenplanZeitraster } from '../../../core/data/stundenplan/StundenplanZeitraster';
import { StundenplanPausenzeit } from '../../../core/data/stundenplan/StundenplanPausenzeit';
import { LehrerStundenplanUnterricht } from '../../../core/data/stundenplan/LehrerStundenplanUnterricht';

export class LehrerStundenplan extends JavaObject {

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
	public zeitraster : List<StundenplanZeitraster> = new ArrayList();

	/**
	 * Die Pausenzeiten des Stundenplans
	 */
	public pausenzeiten : List<StundenplanPausenzeit> = new ArrayList();

	/**
	 * Das Datum, ab dem der Stundenpland gültig ist.
	 */
	public gueltigAb : string = "";

	/**
	 * Das Datum, bis wann der Stundenplan gültig ist.
	 */
	public gueltigBis : string = "";

	/**
	 * Die ID des Lehrers.
	 */
	public idLehrer : number = -1;

	/**
	 * Der Nachname des Lehrers.
	 */
	public nachname : string = "";

	/**
	 * Der Vorname des Lehrers.
	 */
	public vorname : string = "";

	/**
	 * Der Unterricht des Lehrers.
	 */
	public unterricht : List<LehrerStundenplanUnterricht> = new ArrayList();

	/**
	 * Die Pausenaufsichten des Lehrers.
	 */
	public pausenaufsichten : List<StundenplanPausenaufsicht> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.LehrerStundenplan'].includes(name);
	}

	public static transpilerFromJSON(json : string): LehrerStundenplan {
		const obj = JSON.parse(json);
		const result = new LehrerStundenplan();
		if (typeof obj.idStundenplan === "undefined")
			 throw new Error('invalid json format, missing attribute idStundenplan');
		result.idStundenplan = obj.idStundenplan;
		if (typeof obj.bezeichnungStundenplan === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnungStundenplan');
		result.bezeichnungStundenplan = obj.bezeichnungStundenplan;
		if (typeof obj.idSchuljahresabschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if ((obj.zeitraster !== undefined) && (obj.zeitraster !== null)) {
			for (const elem of obj.zeitraster) {
				result.zeitraster?.add(StundenplanZeitraster.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.pausenzeiten !== undefined) && (obj.pausenzeiten !== null)) {
			for (const elem of obj.pausenzeiten) {
				result.pausenzeiten?.add(StundenplanPausenzeit.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (typeof obj.gueltigAb === "undefined")
			 throw new Error('invalid json format, missing attribute gueltigAb');
		result.gueltigAb = obj.gueltigAb;
		if (typeof obj.gueltigBis === "undefined")
			 throw new Error('invalid json format, missing attribute gueltigBis');
		result.gueltigBis = obj.gueltigBis;
		if (typeof obj.idLehrer === "undefined")
			 throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (typeof obj.nachname === "undefined")
			 throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (typeof obj.vorname === "undefined")
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if ((obj.unterricht !== undefined) && (obj.unterricht !== null)) {
			for (const elem of obj.unterricht) {
				result.unterricht?.add(LehrerStundenplanUnterricht.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.pausenaufsichten !== undefined) && (obj.pausenaufsichten !== null)) {
			for (const elem of obj.pausenaufsichten) {
				result.pausenaufsichten?.add(StundenplanPausenaufsicht.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : LehrerStundenplan) : string {
		let result = '{';
		result += '"idStundenplan" : ' + obj.idStundenplan + ',';
		result += '"bezeichnungStundenplan" : ' + '"' + obj.bezeichnungStundenplan! + '"' + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		if (!obj.zeitraster) {
			result += '"zeitraster" : []';
		} else {
			result += '"zeitraster" : [ ';
			for (let i = 0; i < obj.zeitraster.size(); i++) {
				const elem = obj.zeitraster.get(i);
				result += StundenplanZeitraster.transpilerToJSON(elem);
				if (i < obj.zeitraster.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.pausenzeiten) {
			result += '"pausenzeiten" : []';
		} else {
			result += '"pausenzeiten" : [ ';
			for (let i = 0; i < obj.pausenzeiten.size(); i++) {
				const elem = obj.pausenzeiten.get(i);
				result += StundenplanPausenzeit.transpilerToJSON(elem);
				if (i < obj.pausenzeiten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"gueltigAb" : ' + '"' + obj.gueltigAb! + '"' + ',';
		result += '"gueltigBis" : ' + '"' + obj.gueltigBis! + '"' + ',';
		result += '"idLehrer" : ' + obj.idLehrer + ',';
		result += '"nachname" : ' + '"' + obj.nachname! + '"' + ',';
		result += '"vorname" : ' + '"' + obj.vorname! + '"' + ',';
		if (!obj.unterricht) {
			result += '"unterricht" : []';
		} else {
			result += '"unterricht" : [ ';
			for (let i = 0; i < obj.unterricht.size(); i++) {
				const elem = obj.unterricht.get(i);
				result += LehrerStundenplanUnterricht.transpilerToJSON(elem);
				if (i < obj.unterricht.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.pausenaufsichten) {
			result += '"pausenaufsichten" : []';
		} else {
			result += '"pausenaufsichten" : [ ';
			for (let i = 0; i < obj.pausenaufsichten.size(); i++) {
				const elem = obj.pausenaufsichten.get(i);
				result += StundenplanPausenaufsicht.transpilerToJSON(elem);
				if (i < obj.pausenaufsichten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerStundenplan>) : string {
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
				for (let i = 0; i < obj.zeitraster.size(); i++) {
					const elem = obj.zeitraster.get(i);
					result += StundenplanZeitraster.transpilerToJSON(elem);
					if (i < obj.zeitraster.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.pausenzeiten !== "undefined") {
			if (!obj.pausenzeiten) {
				result += '"pausenzeiten" : []';
			} else {
				result += '"pausenzeiten" : [ ';
				for (let i = 0; i < obj.pausenzeiten.size(); i++) {
					const elem = obj.pausenzeiten.get(i);
					result += StundenplanPausenzeit.transpilerToJSON(elem);
					if (i < obj.pausenzeiten.size() - 1)
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
		if (typeof obj.idLehrer !== "undefined") {
			result += '"idLehrer" : ' + obj.idLehrer + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + '"' + obj.nachname + '"' + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + '"' + obj.vorname + '"' + ',';
		}
		if (typeof obj.unterricht !== "undefined") {
			if (!obj.unterricht) {
				result += '"unterricht" : []';
			} else {
				result += '"unterricht" : [ ';
				for (let i = 0; i < obj.unterricht.size(); i++) {
					const elem = obj.unterricht.get(i);
					result += LehrerStundenplanUnterricht.transpilerToJSON(elem);
					if (i < obj.unterricht.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.pausenaufsichten !== "undefined") {
			if (!obj.pausenaufsichten) {
				result += '"pausenaufsichten" : []';
			} else {
				result += '"pausenaufsichten" : [ ';
				for (let i = 0; i < obj.pausenaufsichten.size(); i++) {
					const elem = obj.pausenaufsichten.get(i);
					result += StundenplanPausenaufsicht.transpilerToJSON(elem);
					if (i < obj.pausenaufsichten.size() - 1)
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

export function cast_de_svws_nrw_core_data_stundenplan_LehrerStundenplan(obj : unknown) : LehrerStundenplan {
	return obj as LehrerStundenplan;
}
