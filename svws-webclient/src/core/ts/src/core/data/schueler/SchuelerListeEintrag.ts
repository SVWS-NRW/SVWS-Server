import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class SchuelerListeEintrag extends JavaObject {

	/**
	 * Die ID des Schülers.
	 */
	public id : number = 0;

	/**
	 * Der Nachname des Schülers.
	 */
	public nachname : string = "";

	/**
	 * Der Vorname des Schülers.
	 */
	public vorname : string = "";

	/**
	 * Die ID der aktuellen Klasse des Schülers.
	 */
	public idKlasse : number = -1;

	/**
	 * Der aktuelle Jahrgang des Schülers.
	 */
	public jahrgang : string = "";

	/**
	 * Der Abiturjahrgang, falls es sich um eine Schule mit Gymnasialer Oberstufe handelt.
	 */
	public abiturjahrgang : number | null = null;

	/**
	 * Das Kürzel der aktuellen Schulgliederung des Schülers
	 */
	public schulgliederung : string = "";

	/**
	 * Die Bezeichnung des Status des Schülers (Aktiv, Extern, etc.).
	 */
	public status : number = 0;

	/**
	 * Die ID des Schuljahresabschnittes des Schülers.
	 */
	public idSchuljahresabschnitt : number = -1;

	/**
	 * Die Liste der IDs der belegten Kurse im aktuellen Abschnit
	 */
	public readonly kurse : List<number> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerListeEintrag {
		const obj = JSON.parse(json);
		const result = new SchuelerListeEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
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
		result.abiturjahrgang = typeof obj.abiturjahrgang === "undefined" ? null : obj.abiturjahrgang === null ? null : obj.abiturjahrgang;
		if (typeof obj.schulgliederung === "undefined")
			 throw new Error('invalid json format, missing attribute schulgliederung');
		result.schulgliederung = obj.schulgliederung;
		if (typeof obj.status === "undefined")
			 throw new Error('invalid json format, missing attribute status');
		result.status = obj.status;
		if (typeof obj.idSchuljahresabschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if ((obj.kurse !== undefined) && (obj.kurse !== null)) {
			for (const elem of obj.kurse) {
				result.kurse?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuelerListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"nachname" : ' + '"' + obj.nachname! + '"' + ',';
		result += '"vorname" : ' + '"' + obj.vorname! + '"' + ',';
		result += '"idKlasse" : ' + obj.idKlasse! + ',';
		result += '"jahrgang" : ' + '"' + obj.jahrgang! + '"' + ',';
		result += '"abiturjahrgang" : ' + ((!obj.abiturjahrgang) ? 'null' : obj.abiturjahrgang) + ',';
		result += '"schulgliederung" : ' + '"' + obj.schulgliederung! + '"' + ',';
		result += '"status" : ' + obj.status + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt! + ',';
		if (!obj.kurse) {
			result += '"kurse" : []';
		} else {
			result += '"kurse" : [ ';
			for (let i = 0; i < obj.kurse.size(); i++) {
				const elem = obj.kurse.get(i);
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
		if (typeof obj.abiturjahrgang !== "undefined") {
			result += '"abiturjahrgang" : ' + ((!obj.abiturjahrgang) ? 'null' : obj.abiturjahrgang) + ',';
		}
		if (typeof obj.schulgliederung !== "undefined") {
			result += '"schulgliederung" : ' + '"' + obj.schulgliederung + '"' + ',';
		}
		if (typeof obj.status !== "undefined") {
			result += '"status" : ' + obj.status + ',';
		}
		if (typeof obj.idSchuljahresabschnitt !== "undefined") {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		}
		if (typeof obj.kurse !== "undefined") {
			if (!obj.kurse) {
				result += '"kurse" : []';
			} else {
				result += '"kurse" : [ ';
				for (let i = 0; i < obj.kurse.size(); i++) {
					const elem = obj.kurse.get(i);
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

export function cast_de_svws_nrw_core_data_schueler_SchuelerListeEintrag(obj : unknown) : SchuelerListeEintrag {
	return obj as SchuelerListeEintrag;
}
