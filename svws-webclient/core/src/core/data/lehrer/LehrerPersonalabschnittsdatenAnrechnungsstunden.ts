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
	public idGrund : number | null = null;

	/**
	 * Die Anzahl der Anrechnungsstunden, welche dem Grund zugeordnet sind.
	 */
	public idAnerkennungsgrund : number = 0.0;


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
		result.idGrund = typeof obj.idGrund === "undefined" ? null : obj.idGrund === null ? null : obj.idGrund;
		if (typeof obj.idAnerkennungsgrund === "undefined")
			 throw new Error('invalid json format, missing attribute idAnerkennungsgrund');
		result.idAnerkennungsgrund = obj.idAnerkennungsgrund;
		return result;
	}

	public static transpilerToJSON(obj : LehrerPersonalabschnittsdatenAnrechnungsstunden) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idAbschnittsdaten" : ' + obj.idAbschnittsdaten + ',';
		result += '"idGrund" : ' + ((!obj.idGrund) ? 'null' : obj.idGrund) + ',';
		result += '"idAnerkennungsgrund" : ' + obj.idAnerkennungsgrund + ',';
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
			result += '"idGrund" : ' + ((!obj.idGrund) ? 'null' : obj.idGrund) + ',';
		}
		if (typeof obj.idAnerkennungsgrund !== "undefined") {
			result += '"idAnerkennungsgrund" : ' + obj.idAnerkennungsgrund + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lehrer_LehrerPersonalabschnittsdatenAnrechnungsstunden(obj : unknown) : LehrerPersonalabschnittsdatenAnrechnungsstunden {
	return obj as LehrerPersonalabschnittsdatenAnrechnungsstunden;
}
