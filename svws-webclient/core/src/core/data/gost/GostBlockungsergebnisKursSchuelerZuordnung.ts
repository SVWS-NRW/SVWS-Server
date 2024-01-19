import { JavaObject } from '../../../java/lang/JavaObject';

export class GostBlockungsergebnisKursSchuelerZuordnung extends JavaObject {

	/**
	 * Die ID des Kurses
	 */
	public idKurs : number = -1;

	/**
	 * Die ID des Schülers
	 */
	public idSchueler : number = -1;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnung'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsergebnisKursSchuelerZuordnung {
		const obj = JSON.parse(json);
		const result = new GostBlockungsergebnisKursSchuelerZuordnung();
		if (typeof obj.idKurs === "undefined")
			 throw new Error('invalid json format, missing attribute idKurs');
		result.idKurs = obj.idKurs;
		if (typeof obj.idSchueler === "undefined")
			 throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnisKursSchuelerZuordnung) : string {
		let result = '{';
		result += '"idKurs" : ' + obj.idKurs + ',';
		result += '"idSchueler" : ' + obj.idSchueler + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnisKursSchuelerZuordnung>) : string {
		let result = '{';
		if (typeof obj.idKurs !== "undefined") {
			result += '"idKurs" : ' + obj.idKurs + ',';
		}
		if (typeof obj.idSchueler !== "undefined") {
			result += '"idSchueler" : ' + obj.idSchueler + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisKursSchuelerZuordnung(obj : unknown) : GostBlockungsergebnisKursSchuelerZuordnung {
	return obj as GostBlockungsergebnisKursSchuelerZuordnung;
}
