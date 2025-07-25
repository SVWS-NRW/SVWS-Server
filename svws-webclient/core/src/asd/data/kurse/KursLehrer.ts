import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class KursLehrer extends JavaObject {

	/**
	 * Die ID des Kurslehrers.
	 */
	public idLehrer : number = 0;

	/**
	 * Die ID des Kurses
	 */
	public idKurs : number = 0;

	/**
	 * Die Wochenstunden des Kurslehrers in dem Kurs.
	 */
	public wochenstundenLehrer : number = -1;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.kurse.KursLehrer';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.kurse.KursLehrer'].includes(name);
	}

	public static class = new Class<KursLehrer>('de.svws_nrw.asd.data.kurse.KursLehrer');

	public static transpilerFromJSON(json : string): KursLehrer {
		const obj = JSON.parse(json) as Partial<KursLehrer>;
		const result = new KursLehrer();
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (obj.idKurs === undefined)
			throw new Error('invalid json format, missing attribute idKurs');
		result.idKurs = obj.idKurs;
		if (obj.wochenstundenLehrer === undefined)
			throw new Error('invalid json format, missing attribute wochenstundenLehrer');
		result.wochenstundenLehrer = obj.wochenstundenLehrer;
		return result;
	}

	public static transpilerToJSON(obj : KursLehrer) : string {
		let result = '{';
		result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		result += '"idKurs" : ' + obj.idKurs.toString() + ',';
		result += '"wochenstundenLehrer" : ' + obj.wochenstundenLehrer.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KursLehrer>) : string {
		let result = '{';
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		}
		if (obj.idKurs !== undefined) {
			result += '"idKurs" : ' + obj.idKurs.toString() + ',';
		}
		if (obj.wochenstundenLehrer !== undefined) {
			result += '"wochenstundenLehrer" : ' + obj.wochenstundenLehrer.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_kurse_KursLehrer(obj : unknown) : KursLehrer {
	return obj as KursLehrer;
}
