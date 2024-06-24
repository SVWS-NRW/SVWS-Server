import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class SchuelerListeEintrag extends JavaObject {

	/**
	 * Die ID des Schülers.
	 */
	public id : number = -1;

	/**
	 * Der Nachname des Schülers.
	 */
	public nachname : string = "";

	/**
	 * Der Vorname des Schülers.
	 */
	public vorname : string = "";

	/**
	 * Das Geschlecht des Schülers (m, w, d, x - siehe {@link Geschlecht}).
	 */
	public geschlecht : string = "";

	/**
	 * Die ID der aktuellen Klasse des Schülers.
	 */
	public idKlasse : number = -1;

	/**
	 * Die ID des aktuellen Jahrgangs des Schülers.
	 */
	public idJahrgang : number = -1;

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
	 * Gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht.
	 */
	public istDuplikat : boolean = false;

	/**
	 * Das Schulnummer bei einem externen Schüler oder null, wenn der Schüler kein externer Schüler ist.
	 */
	public externeSchulNr : string | null = null;

	/**
	 * Die ID des Schuljahresabschnittes des Schülers.
	 */
	public idSchuljahresabschnitt : number = -1;

	/**
	 * Die Liste der IDs der belegten Kurse im aktuellen Abschnit
	 */
	public readonly kurse : List<number> = new ArrayList<number>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerListeEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerListeEintrag {
		const obj = JSON.parse(json);
		const result = new SchuelerListeEintrag();
		if (obj.id === undefined)
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.nachname === undefined)
			 throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (obj.vorname === undefined)
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (obj.geschlecht === undefined)
			 throw new Error('invalid json format, missing attribute geschlecht');
		result.geschlecht = obj.geschlecht;
		if (obj.idKlasse === undefined)
			 throw new Error('invalid json format, missing attribute idKlasse');
		result.idKlasse = obj.idKlasse;
		if (obj.idJahrgang === undefined)
			 throw new Error('invalid json format, missing attribute idJahrgang');
		result.idJahrgang = obj.idJahrgang;
		if (obj.jahrgang === undefined)
			 throw new Error('invalid json format, missing attribute jahrgang');
		result.jahrgang = obj.jahrgang;
		result.abiturjahrgang = (obj.abiturjahrgang === undefined) ? null : obj.abiturjahrgang === null ? null : obj.abiturjahrgang;
		if (obj.schulgliederung === undefined)
			 throw new Error('invalid json format, missing attribute schulgliederung');
		result.schulgliederung = obj.schulgliederung;
		if (obj.status === undefined)
			 throw new Error('invalid json format, missing attribute status');
		result.status = obj.status;
		if (obj.istDuplikat === undefined)
			 throw new Error('invalid json format, missing attribute istDuplikat');
		result.istDuplikat = obj.istDuplikat;
		result.externeSchulNr = (obj.externeSchulNr === undefined) ? null : obj.externeSchulNr === null ? null : obj.externeSchulNr;
		if (obj.idSchuljahresabschnitt === undefined)
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
		result += '"nachname" : ' + JSON.stringify(obj.nachname!) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname!) + ',';
		result += '"geschlecht" : ' + JSON.stringify(obj.geschlecht!) + ',';
		result += '"idKlasse" : ' + obj.idKlasse! + ',';
		result += '"idJahrgang" : ' + obj.idJahrgang! + ',';
		result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang!) + ',';
		result += '"abiturjahrgang" : ' + ((!obj.abiturjahrgang) ? 'null' : obj.abiturjahrgang) + ',';
		result += '"schulgliederung" : ' + JSON.stringify(obj.schulgliederung!) + ',';
		result += '"status" : ' + obj.status + ',';
		result += '"istDuplikat" : ' + obj.istDuplikat + ',';
		result += '"externeSchulNr" : ' + ((!obj.externeSchulNr) ? 'null' : JSON.stringify(obj.externeSchulNr)) + ',';
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
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + JSON.stringify(obj.nachname!) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + JSON.stringify(obj.vorname!) + ',';
		}
		if (obj.geschlecht !== undefined) {
			result += '"geschlecht" : ' + JSON.stringify(obj.geschlecht!) + ',';
		}
		if (obj.idKlasse !== undefined) {
			result += '"idKlasse" : ' + obj.idKlasse + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + obj.idJahrgang + ',';
		}
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang!) + ',';
		}
		if (obj.abiturjahrgang !== undefined) {
			result += '"abiturjahrgang" : ' + ((!obj.abiturjahrgang) ? 'null' : obj.abiturjahrgang) + ',';
		}
		if (obj.schulgliederung !== undefined) {
			result += '"schulgliederung" : ' + JSON.stringify(obj.schulgliederung!) + ',';
		}
		if (obj.status !== undefined) {
			result += '"status" : ' + obj.status + ',';
		}
		if (obj.istDuplikat !== undefined) {
			result += '"istDuplikat" : ' + obj.istDuplikat + ',';
		}
		if (obj.externeSchulNr !== undefined) {
			result += '"externeSchulNr" : ' + ((!obj.externeSchulNr) ? 'null' : JSON.stringify(obj.externeSchulNr)) + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		}
		if (obj.kurse !== undefined) {
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
