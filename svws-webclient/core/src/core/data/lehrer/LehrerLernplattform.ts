import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class LehrerLernplattform extends JavaObject {

	/**
	 * Die ID des zugehörigen Lehrers.
	 */
	public idLehrer : number = 0;

	/**
	 * Die ID der Lernplattform.
	 */
	public idLernplattform : number = 0;

	/**
	 * Die ID der Credential für den Lernplattform-Datensatz.
	 */
	public idCredential : number = 0;

	/**
	 * Die Abfrage der Einwilligung zu einer Lernplattform.
	 */
	public einwilligungAbgefragt : boolean = false;

	/**
	 * Die Einwilligung zur Nutzung einer Lernplattform.
	 */
	public einwilligungNutzung : boolean = false;

	/**
	 * Die Einwilligung zur Audiokonferenz einer Lernplattform.
	 */
	public einwilligungAudiokonferenz : boolean = false;

	/**
	 * Die Einwilligung zur VideoKonferenz einer Lernplattform.
	 */
	public einwilligungVideokonferenz : boolean = false;

	/**
	 * Benutzername für die Lernplattform eines Lehrers
	 */
	public benutzername : string | null = null;

	/**
	 * Initialkennwort für die Lernplattform eines Lehrers
	 */
	public initialKennwort : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.lehrer.LehrerLernplattform';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lehrer.LehrerLernplattform'].includes(name);
	}

	public static class = new Class<LehrerLernplattform>('de.svws_nrw.core.data.lehrer.LehrerLernplattform');

	public static transpilerFromJSON(json : string): LehrerLernplattform {
		const obj = JSON.parse(json) as Partial<LehrerLernplattform>;
		const result = new LehrerLernplattform();
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (obj.idLernplattform === undefined)
			throw new Error('invalid json format, missing attribute idLernplattform');
		result.idLernplattform = obj.idLernplattform;
		if (obj.idCredential === undefined)
			throw new Error('invalid json format, missing attribute idCredential');
		result.idCredential = obj.idCredential;
		if (obj.einwilligungAbgefragt === undefined)
			throw new Error('invalid json format, missing attribute einwilligungAbgefragt');
		result.einwilligungAbgefragt = obj.einwilligungAbgefragt;
		if (obj.einwilligungNutzung === undefined)
			throw new Error('invalid json format, missing attribute einwilligungNutzung');
		result.einwilligungNutzung = obj.einwilligungNutzung;
		if (obj.einwilligungAudiokonferenz === undefined)
			throw new Error('invalid json format, missing attribute einwilligungAudiokonferenz');
		result.einwilligungAudiokonferenz = obj.einwilligungAudiokonferenz;
		if (obj.einwilligungVideokonferenz === undefined)
			throw new Error('invalid json format, missing attribute einwilligungVideokonferenz');
		result.einwilligungVideokonferenz = obj.einwilligungVideokonferenz;
		result.benutzername = (obj.benutzername === undefined) ? null : obj.benutzername === null ? null : obj.benutzername;
		result.initialKennwort = (obj.initialKennwort === undefined) ? null : obj.initialKennwort === null ? null : obj.initialKennwort;
		return result;
	}

	public static transpilerToJSON(obj : LehrerLernplattform) : string {
		let result = '{';
		result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		result += '"idLernplattform" : ' + obj.idLernplattform.toString() + ',';
		result += '"idCredential" : ' + obj.idCredential.toString() + ',';
		result += '"einwilligungAbgefragt" : ' + obj.einwilligungAbgefragt.toString() + ',';
		result += '"einwilligungNutzung" : ' + obj.einwilligungNutzung.toString() + ',';
		result += '"einwilligungAudiokonferenz" : ' + obj.einwilligungAudiokonferenz.toString() + ',';
		result += '"einwilligungVideokonferenz" : ' + obj.einwilligungVideokonferenz.toString() + ',';
		result += '"benutzername" : ' + ((obj.benutzername === null) ? 'null' : JSON.stringify(obj.benutzername)) + ',';
		result += '"initialKennwort" : ' + ((obj.initialKennwort === null) ? 'null' : JSON.stringify(obj.initialKennwort)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerLernplattform>) : string {
		let result = '{';
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		}
		if (obj.idLernplattform !== undefined) {
			result += '"idLernplattform" : ' + obj.idLernplattform.toString() + ',';
		}
		if (obj.idCredential !== undefined) {
			result += '"idCredential" : ' + obj.idCredential.toString() + ',';
		}
		if (obj.einwilligungAbgefragt !== undefined) {
			result += '"einwilligungAbgefragt" : ' + obj.einwilligungAbgefragt.toString() + ',';
		}
		if (obj.einwilligungNutzung !== undefined) {
			result += '"einwilligungNutzung" : ' + obj.einwilligungNutzung.toString() + ',';
		}
		if (obj.einwilligungAudiokonferenz !== undefined) {
			result += '"einwilligungAudiokonferenz" : ' + obj.einwilligungAudiokonferenz.toString() + ',';
		}
		if (obj.einwilligungVideokonferenz !== undefined) {
			result += '"einwilligungVideokonferenz" : ' + obj.einwilligungVideokonferenz.toString() + ',';
		}
		if (obj.benutzername !== undefined) {
			result += '"benutzername" : ' + ((obj.benutzername === null) ? 'null' : JSON.stringify(obj.benutzername)) + ',';
		}
		if (obj.initialKennwort !== undefined) {
			result += '"initialKennwort" : ' + ((obj.initialKennwort === null) ? 'null' : JSON.stringify(obj.initialKennwort)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lehrer_LehrerLernplattform(obj : unknown) : LehrerLernplattform {
	return obj as LehrerLernplattform;
}
