import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class LernplattformV1Login extends JavaObject {

	/**
	 * Der Benutzername für die Lernplattform.
	 */
	public benutzername : string | null = null;

	/**
	 * Das Initialpasswort für die Lernplattform.
	 */
	public initialpasswort : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Login';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Login'].includes(name);
	}

	public static class = new Class<LernplattformV1Login>('de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Login');

	public static transpilerFromJSON(json : string): LernplattformV1Login {
		const obj = JSON.parse(json) as Partial<LernplattformV1Login>;
		const result = new LernplattformV1Login();
		result.benutzername = (obj.benutzername === undefined) ? null : obj.benutzername === null ? null : obj.benutzername;
		result.initialpasswort = (obj.initialpasswort === undefined) ? null : obj.initialpasswort === null ? null : obj.initialpasswort;
		return result;
	}

	public static transpilerToJSON(obj : LernplattformV1Login) : string {
		let result = '{';
		result += '"benutzername" : ' + ((obj.benutzername === null) ? 'null' : JSON.stringify(obj.benutzername)) + ',';
		result += '"initialpasswort" : ' + ((obj.initialpasswort === null) ? 'null' : JSON.stringify(obj.initialpasswort)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LernplattformV1Login>) : string {
		let result = '{';
		if (obj.benutzername !== undefined) {
			result += '"benutzername" : ' + ((obj.benutzername === null) ? 'null' : JSON.stringify(obj.benutzername)) + ',';
		}
		if (obj.initialpasswort !== undefined) {
			result += '"initialpasswort" : ' + ((obj.initialpasswort === null) ? 'null' : JSON.stringify(obj.initialpasswort)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lernplattform_v1_LernplattformV1Login(obj : unknown) : LernplattformV1Login {
	return obj as LernplattformV1Login;
}
