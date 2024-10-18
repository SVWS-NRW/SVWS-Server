import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class StundenplanSchueler extends JavaObject {

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
	 * Die ID der Klasse in der sich der Schüler befindet.
	 */
	public idKlasse : number = -1;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanSchueler';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanSchueler'].includes(name);
	}

	public static class = new Class<StundenplanSchueler>('de.svws_nrw.core.data.stundenplan.StundenplanSchueler');

	public static transpilerFromJSON(json : string): StundenplanSchueler {
		const obj = JSON.parse(json) as Partial<StundenplanSchueler>;
		const result = new StundenplanSchueler();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.nachname === undefined)
			throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (obj.vorname === undefined)
			throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (obj.idKlasse === undefined)
			throw new Error('invalid json format, missing attribute idKlasse');
		result.idKlasse = obj.idKlasse;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanSchueler) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		result += '"idKlasse" : ' + obj.idKlasse.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanSchueler>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		}
		if (obj.idKlasse !== undefined) {
			result += '"idKlasse" : ' + obj.idKlasse.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanSchueler(obj : unknown) : StundenplanSchueler {
	return obj as StundenplanSchueler;
}
