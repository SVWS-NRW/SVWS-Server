import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class BenutzergruppeDaten extends JavaObject {

	/**
	 * Die ID der Benutzergruppe.
	 */
	public id : number = -1;

	/**
	 * Die Bezeichnung der Benutzergruppe.
	 */
	public bezeichnung : string = "";

	/**
	 * Gibt an, ob es sich um eine Administrative Benutzergruppe handelt oder nicht.
	 */
	public istAdmin : boolean = false;

	/**
	 * Die IDs der Kompetenzen, die speziell dieser Benutzergruppe zugeordnet sind.
	 */
	public kompetenzen : List<number> = new ArrayList<number>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.benutzer.BenutzergruppeDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.benutzer.BenutzergruppeDaten'].includes(name);
	}

	public static class = new Class<BenutzergruppeDaten>('de.svws_nrw.core.data.benutzer.BenutzergruppeDaten');

	public static transpilerFromJSON(json : string): BenutzergruppeDaten {
		const obj = JSON.parse(json) as Partial<BenutzergruppeDaten>;
		const result = new BenutzergruppeDaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.istAdmin === undefined)
			throw new Error('invalid json format, missing attribute istAdmin');
		result.istAdmin = obj.istAdmin;
		if (obj.kompetenzen !== undefined) {
			for (const elem of obj.kompetenzen) {
				result.kompetenzen.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BenutzergruppeDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"istAdmin" : ' + obj.istAdmin.toString() + ',';
		result += '"kompetenzen" : [ ';
		for (let i = 0; i < obj.kompetenzen.size(); i++) {
			const elem = obj.kompetenzen.get(i);
			result += elem.toString();
			if (i < obj.kompetenzen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzergruppeDaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.istAdmin !== undefined) {
			result += '"istAdmin" : ' + obj.istAdmin.toString() + ',';
		}
		if (obj.kompetenzen !== undefined) {
			result += '"kompetenzen" : [ ';
			for (let i = 0; i < obj.kompetenzen.size(); i++) {
				const elem = obj.kompetenzen.get(i);
				result += elem.toString();
				if (i < obj.kompetenzen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_benutzer_BenutzergruppeDaten(obj : unknown) : BenutzergruppeDaten {
	return obj as BenutzergruppeDaten;
}
