import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenLehrerfunktion';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenLehrerfunktion'].includes(name);
	}

	public static class = new Class<LehrerPersonalabschnittsdatenLehrerfunktion>('de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenLehrerfunktion');

	public static transpilerFromJSON(json : string): LehrerPersonalabschnittsdatenLehrerfunktion {
		const obj = JSON.parse(json) as Partial<LehrerPersonalabschnittsdatenLehrerfunktion>;
		const result = new LehrerPersonalabschnittsdatenLehrerfunktion();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idAbschnittsdaten === undefined)
			throw new Error('invalid json format, missing attribute idAbschnittsdaten');
		result.idAbschnittsdaten = obj.idAbschnittsdaten;
		if (obj.idFunktion === undefined)
			throw new Error('invalid json format, missing attribute idFunktion');
		result.idFunktion = obj.idFunktion;
		return result;
	}

	public static transpilerToJSON(obj : LehrerPersonalabschnittsdatenLehrerfunktion) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idAbschnittsdaten" : ' + obj.idAbschnittsdaten.toString() + ',';
		result += '"idFunktion" : ' + obj.idFunktion.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerPersonalabschnittsdatenLehrerfunktion>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idAbschnittsdaten !== undefined) {
			result += '"idAbschnittsdaten" : ' + obj.idAbschnittsdaten.toString() + ',';
		}
		if (obj.idFunktion !== undefined) {
			result += '"idFunktion" : ' + obj.idFunktion.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_lehrer_LehrerPersonalabschnittsdatenLehrerfunktion(obj : unknown) : LehrerPersonalabschnittsdatenLehrerfunktion {
	return obj as LehrerPersonalabschnittsdatenLehrerfunktion;
}
