import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class LehrerFunktion extends JavaObject {

	/**
	 * Die ID für diesen Eintrag.
	 */
	public id: number = 0;

	/**
	 * Die ID der Lehrerabschnittsdaten.
	 */
	public idAbschnittsdaten: number = 0;

	/**
	 * Die ID in dem Katalog der schulspezifischen Lehrerfunktionen.
	 */
	public idFunktion: number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.lehrer.LehrerFunktion';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.lehrer.LehrerFunktion'].includes(name);
	}

	public static readonly class = new Class<LehrerFunktion>('de.svws_nrw.asd.data.lehrer.LehrerFunktion');

	public static transpilerFromJSON(json: string): LehrerFunktion {
		const obj = JSON.parse(json) as Partial<LehrerFunktion>;
		const result = new LehrerFunktion();
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

	public static transpilerToJSON(obj: LehrerFunktion): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idAbschnittsdaten" : ' + obj.idAbschnittsdaten.toString() + ',';
		result += '"idFunktion" : ' + obj.idFunktion.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<LehrerFunktion>): string {
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

export function cast_de_svws_nrw_asd_data_lehrer_LehrerFunktion(obj: unknown): LehrerFunktion {
	return obj as LehrerFunktion;
}
