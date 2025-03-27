import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerLernplattform extends JavaObject {

	/**
	 * Die ID des zugehörigen Schülers.
	 */
	public idSchueler : number = 0;

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


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerLernplattform';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerLernplattform'].includes(name);
	}

	public static class = new Class<SchuelerLernplattform>('de.svws_nrw.core.data.schueler.SchuelerLernplattform');

	public static transpilerFromJSON(json : string): SchuelerLernplattform {
		const obj = JSON.parse(json) as Partial<SchuelerLernplattform>;
		const result = new SchuelerLernplattform();
		if (obj.idSchueler === undefined)
			throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
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
		return result;
	}

	public static transpilerToJSON(obj : SchuelerLernplattform) : string {
		let result = '{';
		result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		result += '"idLernplattform" : ' + obj.idLernplattform.toString() + ',';
		result += '"idCredential" : ' + obj.idCredential.toString() + ',';
		result += '"einwilligungAbgefragt" : ' + obj.einwilligungAbgefragt.toString() + ',';
		result += '"einwilligungNutzung" : ' + obj.einwilligungNutzung.toString() + ',';
		result += '"einwilligungAudiokonferenz" : ' + obj.einwilligungAudiokonferenz.toString() + ',';
		result += '"einwilligungVideokonferenz" : ' + obj.einwilligungVideokonferenz.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerLernplattform>) : string {
		let result = '{';
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerLernplattform(obj : unknown) : SchuelerLernplattform {
	return obj as SchuelerLernplattform;
}
