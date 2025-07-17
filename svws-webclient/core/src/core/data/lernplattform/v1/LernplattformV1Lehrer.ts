import { JavaObject } from '../../../../java/lang/JavaObject';
import { LernplattformV1Login } from '../../../../core/data/lernplattform/v1/LernplattformV1Login';
import { Class } from '../../../../java/lang/Class';

export class LernplattformV1Lehrer extends JavaObject {

	/**
	 * Die ID des Lehrers aus der SVWS-DB (z.B. 42)
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Lehrers (z.B. Mus)
	 */
	public kuerzel : string | null = null;

	/**
	 * Der Nachname des Lehrers (z.B. Mustermann)
	 */
	public nachname : string | null = null;

	/**
	 * Der Vorname des Lehrers (z.B. Max)
	 */
	public vorname : string | null = null;

	/**
	 * Das Geschlecht des Lehrers (m,w,d,x)
	 */
	public geschlecht : string | null = null;

	/**
	 * Die Dienst-EMail-Adresse des Lehrers
	 */
	public emailDienstlich : string | null = null;

	/**
	 * Die Login-Daten des Lehrers bestehend aus EMail und Initialpasswort für die Lernplattform.
	 */
	public lernplattformlogin : LernplattformV1Login = new LernplattformV1Login();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Lehrer';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Lehrer'].includes(name);
	}

	public static class = new Class<LernplattformV1Lehrer>('de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Lehrer');

	public static transpilerFromJSON(json : string): LernplattformV1Lehrer {
		const obj = JSON.parse(json) as Partial<LernplattformV1Lehrer>;
		const result = new LernplattformV1Lehrer();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.nachname = (obj.nachname === undefined) ? null : obj.nachname === null ? null : obj.nachname;
		result.vorname = (obj.vorname === undefined) ? null : obj.vorname === null ? null : obj.vorname;
		result.geschlecht = (obj.geschlecht === undefined) ? null : obj.geschlecht === null ? null : obj.geschlecht;
		result.emailDienstlich = (obj.emailDienstlich === undefined) ? null : obj.emailDienstlich === null ? null : obj.emailDienstlich;
		if (obj.lernplattformlogin === undefined)
			throw new Error('invalid json format, missing attribute lernplattformlogin');
		result.lernplattformlogin = LernplattformV1Login.transpilerFromJSON(JSON.stringify(obj.lernplattformlogin));
		return result;
	}

	public static transpilerToJSON(obj : LernplattformV1Lehrer) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"nachname" : ' + ((obj.nachname === null) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		result += '"geschlecht" : ' + ((obj.geschlecht === null) ? 'null' : JSON.stringify(obj.geschlecht)) + ',';
		result += '"emailDienstlich" : ' + ((obj.emailDienstlich === null) ? 'null' : JSON.stringify(obj.emailDienstlich)) + ',';
		result += '"lernplattformlogin" : ' + LernplattformV1Login.transpilerToJSON(obj.lernplattformlogin) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LernplattformV1Lehrer>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
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
		if (obj.emailDienstlich !== undefined) {
			result += '"emailDienstlich" : ' + ((obj.emailDienstlich === null) ? 'null' : JSON.stringify(obj.emailDienstlich)) + ',';
		}
		if (obj.lernplattformlogin !== undefined) {
			result += '"lernplattformlogin" : ' + LernplattformV1Login.transpilerToJSON(obj.lernplattformlogin) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lernplattform_v1_LernplattformV1Lehrer(obj : unknown) : LernplattformV1Lehrer {
	return obj as LernplattformV1Lehrer;
}
