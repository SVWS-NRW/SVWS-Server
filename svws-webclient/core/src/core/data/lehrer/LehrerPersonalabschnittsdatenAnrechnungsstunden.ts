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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden'].includes(name);
	}

	public static transpilerFromJSON(json : string): LehrerPersonalabschnittsdatenAnrechnungsstunden {
		const obj = JSON.parse(json) as Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>;
		const result = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idAbschnittsdaten === undefined)
			throw new Error('invalid json format, missing attribute idAbschnittsdaten');
		result.idAbschnittsdaten = obj.idAbschnittsdaten;
		if (obj.idGrund === undefined)
			throw new Error('invalid json format, missing attribute idGrund');
		result.idGrund = obj.idGrund;
		if (obj.anzahl === undefined)
			throw new Error('invalid json format, missing attribute anzahl');
		result.anzahl = obj.anzahl;
		return result;
	}

	public static transpilerToJSON(obj : LehrerPersonalabschnittsdatenAnrechnungsstunden) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idAbschnittsdaten" : ' + obj.idAbschnittsdaten.toString() + ',';
		result += '"idGrund" : ' + obj.idGrund.toString() + ',';
		result += '"anzahl" : ' + obj.anzahl.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idAbschnittsdaten !== undefined) {
			result += '"idAbschnittsdaten" : ' + obj.idAbschnittsdaten.toString() + ',';
		}
		if (obj.idGrund !== undefined) {
			result += '"idGrund" : ' + obj.idGrund.toString() + ',';
		}
		if (obj.anzahl !== undefined) {
			result += '"anzahl" : ' + obj.anzahl.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lehrer_LehrerPersonalabschnittsdatenAnrechnungsstunden(obj : unknown) : LehrerPersonalabschnittsdatenAnrechnungsstunden {
	return obj as LehrerPersonalabschnittsdatenAnrechnungsstunden;
}
