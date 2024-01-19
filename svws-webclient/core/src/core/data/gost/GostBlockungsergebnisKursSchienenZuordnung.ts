import { JavaObject } from '../../../java/lang/JavaObject';

export class GostBlockungsergebnisKursSchienenZuordnung extends JavaObject {

	/**
	 * Die ID des Kurses
	 */
	public idKurs : number = -1;

	/**
	 * Die ID der Schiene
	 */
	public idSchiene : number = -1;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnung'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsergebnisKursSchienenZuordnung {
		const obj = JSON.parse(json);
		const result = new GostBlockungsergebnisKursSchienenZuordnung();
		if (typeof obj.idKurs === "undefined")
			 throw new Error('invalid json format, missing attribute idKurs');
		result.idKurs = obj.idKurs;
		if (typeof obj.idSchiene === "undefined")
			 throw new Error('invalid json format, missing attribute idSchiene');
		result.idSchiene = obj.idSchiene;
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnisKursSchienenZuordnung) : string {
		let result = '{';
		result += '"idKurs" : ' + obj.idKurs + ',';
		result += '"idSchiene" : ' + obj.idSchiene + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnisKursSchienenZuordnung>) : string {
		let result = '{';
		if (typeof obj.idKurs !== "undefined") {
			result += '"idKurs" : ' + obj.idKurs + ',';
		}
		if (typeof obj.idSchiene !== "undefined") {
			result += '"idSchiene" : ' + obj.idSchiene + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisKursSchienenZuordnung(obj : unknown) : GostBlockungsergebnisKursSchienenZuordnung {
	return obj as GostBlockungsergebnisKursSchienenZuordnung;
}
