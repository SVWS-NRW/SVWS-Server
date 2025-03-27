import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuldateiVersion extends JavaObject {

	/**
	 * Die Versionsnummer der Schuldatei.
	 */
	public version : string = "";

	/**
	 * Der Name des API-Endpunktes der Schuldatei-Anwendung.
	 */
	public name : string = "";

	/**
	 * Die URL des API-Endpunktes.
	 */
	public url : string = "";

	/**
	 * Das Ablaufdatum der Version.
	 */
	public gueltigbis : string = "";

	/**
	 * Eine Bemerkung zu der Datei.
	 */
	public bemerkung : string = "";


	/**
	 * Erstellt eine Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.data.SchuldateiVersion';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.data.SchuldateiVersion'].includes(name);
	}

	public static class = new Class<SchuldateiVersion>('de.svws_nrw.schulen.v1.data.SchuldateiVersion');

	public static transpilerFromJSON(json : string): SchuldateiVersion {
		const obj = JSON.parse(json) as Partial<SchuldateiVersion>;
		const result = new SchuldateiVersion();
		if (obj.version === undefined)
			throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		if (obj.name === undefined)
			throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (obj.url === undefined)
			throw new Error('invalid json format, missing attribute url');
		result.url = obj.url;
		if (obj.gueltigbis === undefined)
			throw new Error('invalid json format, missing attribute gueltigbis');
		result.gueltigbis = obj.gueltigbis;
		if (obj.bemerkung === undefined)
			throw new Error('invalid json format, missing attribute bemerkung');
		result.bemerkung = obj.bemerkung;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiVersion) : string {
		let result = '{';
		result += '"version" : ' + JSON.stringify(obj.version) + ',';
		result += '"name" : ' + JSON.stringify(obj.name) + ',';
		result += '"url" : ' + JSON.stringify(obj.url) + ',';
		result += '"gueltigbis" : ' + JSON.stringify(obj.gueltigbis) + ',';
		result += '"bemerkung" : ' + JSON.stringify(obj.bemerkung) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiVersion>) : string {
		let result = '{';
		if (obj.version !== undefined) {
			result += '"version" : ' + JSON.stringify(obj.version) + ',';
		}
		if (obj.name !== undefined) {
			result += '"name" : ' + JSON.stringify(obj.name) + ',';
		}
		if (obj.url !== undefined) {
			result += '"url" : ' + JSON.stringify(obj.url) + ',';
		}
		if (obj.gueltigbis !== undefined) {
			result += '"gueltigbis" : ' + JSON.stringify(obj.gueltigbis) + ',';
		}
		if (obj.bemerkung !== undefined) {
			result += '"bemerkung" : ' + JSON.stringify(obj.bemerkung) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schulen_v1_data_SchuldateiVersion(obj : unknown) : SchuldateiVersion {
	return obj as SchuldateiVersion;
}
