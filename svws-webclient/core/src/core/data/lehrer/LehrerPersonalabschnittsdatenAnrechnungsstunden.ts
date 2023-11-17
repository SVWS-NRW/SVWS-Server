import { JavaObject } from '../../../java/lang/JavaObject';

export class LehrerPersonalabschnittsdatenAnrechnungsstunden extends JavaObject {

	/**
	 * Die ID für den Eintrag von Anrechnungsstunden.
	 */
	public id : number = -1;

	/**
	 * Die ID der Lehrerabschnittsdaten.
	 */
	public idAbschnittsdaten : number = -1;

	/**
	 * Die ID des Anrechnungsgrundes.
	 */
	public idGrund : number = -1;

	/**
	 * Die Anzahl der Anrechnungsstunden, welche dem Grund zugeordnet sind.
	 */
	public anzahl : number = 0.0;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden'].includes(name);
	}

	public static transpilerFromJSON(json : string): LehrerPersonalabschnittsdatenAnrechnungsstunden {
		const obj = JSON.parse(json);
		const result = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idAbschnittsdaten === "undefined")
			 throw new Error('invalid json format, missing attribute idAbschnittsdaten');
		result.idAbschnittsdaten = obj.idAbschnittsdaten;
		if (typeof obj.idGrund === "undefined")
			 throw new Error('invalid json format, missing attribute idGrund');
		result.idGrund = obj.idGrund;
		if (typeof obj.anzahl === "undefined")
			 throw new Error('invalid json format, missing attribute anzahl');
		result.anzahl = obj.anzahl;
		return result;
	}

	public static transpilerToJSON(obj : LehrerPersonalabschnittsdatenAnrechnungsstunden) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idAbschnittsdaten" : ' + obj.idAbschnittsdaten + ',';
		result += '"idGrund" : ' + obj.idGrund + ',';
		result += '"anzahl" : ' + obj.anzahl + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idAbschnittsdaten !== "undefined") {
			result += '"idAbschnittsdaten" : ' + obj.idAbschnittsdaten + ',';
		}
		if (typeof obj.idGrund !== "undefined") {
			result += '"idGrund" : ' + obj.idGrund + ',';
		}
		if (typeof obj.anzahl !== "undefined") {
			result += '"anzahl" : ' + obj.anzahl + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lehrer_LehrerPersonalabschnittsdatenAnrechnungsstunden(obj : unknown) : LehrerPersonalabschnittsdatenAnrechnungsstunden {
	return obj as LehrerPersonalabschnittsdatenAnrechnungsstunden;
}
