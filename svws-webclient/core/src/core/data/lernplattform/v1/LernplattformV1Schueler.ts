import { JavaObject } from '../../../../java/lang/JavaObject';
import { LernplattformV1Login } from '../../../../core/data/lernplattform/v1/LernplattformV1Login';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class LernplattformV1Schueler extends JavaObject {

	/**
	 * Die ID des Schülers in der SVWS-DB
	 */
	public id : number = 0;

	/**
	 * Die ID des aktuellen Jahrgangs, in dem sich der Schüler befindet.
	 */
	public idJahrgang : number = 0;

	/**
	 * Die ID der aktuellen Klasse, in der sich der Schüler befindet.
	 */
	public idKlasse : number = 0;

	/**
	 * Der Nachname des Schülers (z.B. Mustermann)
	 */
	public nachname : string | null = null;

	/**
	 * Der Vorname des Schülers (z.B. Max)
	 */
	public vorname : string | null = null;

	/**
	 * Das Geschlecht des Schülers (m,w,d,x)
	 */
	public geschlecht : string | null = null;

	/**
	 * Das Geburtsdatum des Schülers (z.B. 21.01.2000)
	 */
	public geburtsdatum : string | null = null;

	/**
	 * Die Status-ID des Schülers.
	 */
	public status : number = 0;

	/**
	 * Logindaten des Schülers bestehend aus Benutzername und Initialpasswort.
	 */
	public lernplattformlogin : LernplattformV1Login = new LernplattformV1Login();

	/**
	 * Die IDs der Lerngruppen des Schülers in dem Lernabschnitt.
	 */
	public idsLerngruppen : List<number> = new ArrayList<number>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Schueler';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Schueler'].includes(name);
	}

	public static class = new Class<LernplattformV1Schueler>('de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Schueler');

	public static transpilerFromJSON(json : string): LernplattformV1Schueler {
		const obj = JSON.parse(json) as Partial<LernplattformV1Schueler>;
		const result = new LernplattformV1Schueler();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idJahrgang === undefined)
			throw new Error('invalid json format, missing attribute idJahrgang');
		result.idJahrgang = obj.idJahrgang;
		if (obj.idKlasse === undefined)
			throw new Error('invalid json format, missing attribute idKlasse');
		result.idKlasse = obj.idKlasse;
		result.nachname = (obj.nachname === undefined) ? null : obj.nachname === null ? null : obj.nachname;
		result.vorname = (obj.vorname === undefined) ? null : obj.vorname === null ? null : obj.vorname;
		result.geschlecht = (obj.geschlecht === undefined) ? null : obj.geschlecht === null ? null : obj.geschlecht;
		result.geburtsdatum = (obj.geburtsdatum === undefined) ? null : obj.geburtsdatum === null ? null : obj.geburtsdatum;
		if (obj.status === undefined)
			throw new Error('invalid json format, missing attribute status');
		result.status = obj.status;
		if (obj.lernplattformlogin === undefined)
			throw new Error('invalid json format, missing attribute lernplattformlogin');
		result.lernplattformlogin = LernplattformV1Login.transpilerFromJSON(JSON.stringify(obj.lernplattformlogin));
		if (obj.idsLerngruppen !== undefined) {
			for (const elem of obj.idsLerngruppen) {
				result.idsLerngruppen.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : LernplattformV1Schueler) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		result += '"idKlasse" : ' + obj.idKlasse.toString() + ',';
		result += '"nachname" : ' + ((obj.nachname === null) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		result += '"geschlecht" : ' + ((obj.geschlecht === null) ? 'null' : JSON.stringify(obj.geschlecht)) + ',';
		result += '"geburtsdatum" : ' + ((obj.geburtsdatum === null) ? 'null' : JSON.stringify(obj.geburtsdatum)) + ',';
		result += '"status" : ' + obj.status.toString() + ',';
		result += '"lernplattformlogin" : ' + LernplattformV1Login.transpilerToJSON(obj.lernplattformlogin) + ',';
		result += '"idsLerngruppen" : [ ';
		for (let i = 0; i < obj.idsLerngruppen.size(); i++) {
			const elem = obj.idsLerngruppen.get(i);
			result += elem.toString();
			if (i < obj.idsLerngruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LernplattformV1Schueler>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		}
		if (obj.idKlasse !== undefined) {
			result += '"idKlasse" : ' + obj.idKlasse.toString() + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + ((obj.nachname === null) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		}
		if (obj.geschlecht !== undefined) {
			result += '"geschlecht" : ' + ((obj.geschlecht === null) ? 'null' : JSON.stringify(obj.geschlecht)) + ',';
		}
		if (obj.geburtsdatum !== undefined) {
			result += '"geburtsdatum" : ' + ((obj.geburtsdatum === null) ? 'null' : JSON.stringify(obj.geburtsdatum)) + ',';
		}
		if (obj.status !== undefined) {
			result += '"status" : ' + obj.status.toString() + ',';
		}
		if (obj.lernplattformlogin !== undefined) {
			result += '"lernplattformlogin" : ' + LernplattformV1Login.transpilerToJSON(obj.lernplattformlogin) + ',';
		}
		if (obj.idsLerngruppen !== undefined) {
			result += '"idsLerngruppen" : [ ';
			for (let i = 0; i < obj.idsLerngruppen.size(); i++) {
				const elem = obj.idsLerngruppen.get(i);
				result += elem.toString();
				if (i < obj.idsLerngruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lernplattform_v1_LernplattformV1Schueler(obj : unknown) : LernplattformV1Schueler {
	return obj as LernplattformV1Schueler;
}
