import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class LehrerCredentialsOhneBenutzerEintrag extends JavaObject {

	/**
	 * Die ID des Lehrers
	 */
	public idLehrer: number = -1;

	/**
	 * Die ID der vorhandenen Credentials
	 */
	public idCredentials: number = -1;

	/**
	 * Der Benutzername der vorhandenen Credentials
	 */
	public benutzername: string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.benutzer.LehrerCredentialsOhneBenutzerEintrag';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.benutzer.LehrerCredentialsOhneBenutzerEintrag'].includes(name);
	}

	public static readonly class = new Class<LehrerCredentialsOhneBenutzerEintrag>('de.svws_nrw.core.data.benutzer.LehrerCredentialsOhneBenutzerEintrag');

	public static transpilerFromJSON(json: string): LehrerCredentialsOhneBenutzerEintrag {
		const obj = JSON.parse(json) as Partial<LehrerCredentialsOhneBenutzerEintrag>;
		const result = new LehrerCredentialsOhneBenutzerEintrag();
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (obj.idCredentials === undefined)
			throw new Error('invalid json format, missing attribute idCredentials');
		result.idCredentials = obj.idCredentials;
		if (obj.benutzername === undefined)
			throw new Error('invalid json format, missing attribute benutzername');
		result.benutzername = obj.benutzername;
		return result;
	}

	public static transpilerToJSON(obj: LehrerCredentialsOhneBenutzerEintrag): string {
		let result = '{';
		result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		result += '"idCredentials" : ' + obj.idCredentials.toString() + ',';
		result += '"benutzername" : ' + JSON.stringify(obj.benutzername) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<LehrerCredentialsOhneBenutzerEintrag>): string {
		let result = '{';
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		}
		if (obj.idCredentials !== undefined) {
			result += '"idCredentials" : ' + obj.idCredentials.toString() + ',';
		}
		if (obj.benutzername !== undefined) {
			result += '"benutzername" : ' + JSON.stringify(obj.benutzername) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_benutzer_LehrerCredentialsOhneBenutzerEintrag(obj: unknown): LehrerCredentialsOhneBenutzerEintrag {
	return obj as LehrerCredentialsOhneBenutzerEintrag;
}
