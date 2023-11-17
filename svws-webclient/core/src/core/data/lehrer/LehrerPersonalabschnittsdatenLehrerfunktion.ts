import { JavaObject } from '../../../java/lang/JavaObject';

export class LehrerPersonalabschnittsdatenLehrerfunktion extends JavaObject {

	/**
	 * Die ID für diesen Eintrag.
	 */
	public id : number = -1;

	/**
	 * Die ID der Lehrerabschnittsdaten.
	 */
	public idAbschnittsdaten : number = -1;

	/**
	 * Die ID in dem Katalog der schulspezifischen Lehrerfunktionen.
	 */
	public idFunktion : number = -1;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lehrer.LehrerPersonalabschnittsdatenLehrerfunktion'].includes(name);
	}

	public static transpilerFromJSON(json : string): LehrerPersonalabschnittsdatenLehrerfunktion {
		const obj = JSON.parse(json);
		const result = new LehrerPersonalabschnittsdatenLehrerfunktion();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idAbschnittsdaten === "undefined")
			 throw new Error('invalid json format, missing attribute idAbschnittsdaten');
		result.idAbschnittsdaten = obj.idAbschnittsdaten;
		if (typeof obj.idFunktion === "undefined")
			 throw new Error('invalid json format, missing attribute idFunktion');
		result.idFunktion = obj.idFunktion;
		return result;
	}

	public static transpilerToJSON(obj : LehrerPersonalabschnittsdatenLehrerfunktion) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idAbschnittsdaten" : ' + obj.idAbschnittsdaten + ',';
		result += '"idFunktion" : ' + obj.idFunktion + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerPersonalabschnittsdatenLehrerfunktion>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idAbschnittsdaten !== "undefined") {
			result += '"idAbschnittsdaten" : ' + obj.idAbschnittsdaten + ',';
		}
		if (typeof obj.idFunktion !== "undefined") {
			result += '"idFunktion" : ' + obj.idFunktion + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lehrer_LehrerPersonalabschnittsdatenLehrerfunktion(obj : unknown) : LehrerPersonalabschnittsdatenLehrerfunktion {
	return obj as LehrerPersonalabschnittsdatenLehrerfunktion;
}
