import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanSchueler extends JavaObject {

	/**
	 * Die ID des Lehrers.
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanSchueler'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanSchueler {
		const obj = JSON.parse(json);
		const result = new StundenplanSchueler();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.nachname === "undefined")
			 throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (typeof obj.vorname === "undefined")
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (typeof obj.idKlasse === "undefined")
			 throw new Error('invalid json format, missing attribute idKlasse');
		result.idKlasse = obj.idKlasse;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanSchueler) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"nachname" : ' + '"' + obj.nachname! + '"' + ',';
		result += '"vorname" : ' + '"' + obj.vorname! + '"' + ',';
		result += '"idKlasse" : ' + obj.idKlasse + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanSchueler>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + '"' + obj.nachname + '"' + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + '"' + obj.vorname + '"' + ',';
		}
		if (typeof obj.idKlasse !== "undefined") {
			result += '"idKlasse" : ' + obj.idKlasse + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanSchueler(obj : unknown) : StundenplanSchueler {
	return obj as StundenplanSchueler;
}
