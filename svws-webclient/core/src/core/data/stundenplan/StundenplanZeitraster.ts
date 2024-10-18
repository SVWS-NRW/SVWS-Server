import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class StundenplanZeitraster extends JavaObject {

	/**
	 * Die ID des Zeitraster-Eintrages.
	 */
	public id : number = -1;

	/**
	 * Der {@link Wochentag} an dem der Unterricht stattfindet (1=Montag, 2=Dienstag, ..., 7=Sonntag)
	 */
	public wochentag : number = 1;

	/**
	 * Die Nummer der Unterrichtsstunde an dem Wochentag
	 */
	public unterrichtstunde : number = 1;

	/**
	 * Die Uhrzeit in Minuten seit 0 Uhr, wann die Unterrichtsstunde beginnt. NULL bedeutet "noch nicht definiert".
	 */
	public stundenbeginn : number | null = null;

	/**
	 * Die Uhrzeit in Minuten seit 0 Uhr, wann die Unterrichtsstunde endet. NULL bedeutet "noch nicht definiert".
	 */
	public stundenende : number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanZeitraster';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanZeitraster'].includes(name);
	}

	public static class = new Class<StundenplanZeitraster>('de.svws_nrw.core.data.stundenplan.StundenplanZeitraster');

	public static transpilerFromJSON(json : string): StundenplanZeitraster {
		const obj = JSON.parse(json) as Partial<StundenplanZeitraster>;
		const result = new StundenplanZeitraster();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.wochentag === undefined)
			throw new Error('invalid json format, missing attribute wochentag');
		result.wochentag = obj.wochentag;
		if (obj.unterrichtstunde === undefined)
			throw new Error('invalid json format, missing attribute unterrichtstunde');
		result.unterrichtstunde = obj.unterrichtstunde;
		result.stundenbeginn = (obj.stundenbeginn === undefined) ? null : obj.stundenbeginn === null ? null : obj.stundenbeginn;
		result.stundenende = (obj.stundenende === undefined) ? null : obj.stundenende === null ? null : obj.stundenende;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanZeitraster) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"wochentag" : ' + obj.wochentag.toString() + ',';
		result += '"unterrichtstunde" : ' + obj.unterrichtstunde.toString() + ',';
		result += '"stundenbeginn" : ' + ((!obj.stundenbeginn) ? 'null' : obj.stundenbeginn.toString()) + ',';
		result += '"stundenende" : ' + ((!obj.stundenende) ? 'null' : obj.stundenende.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanZeitraster>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.wochentag !== undefined) {
			result += '"wochentag" : ' + obj.wochentag.toString() + ',';
		}
		if (obj.unterrichtstunde !== undefined) {
			result += '"unterrichtstunde" : ' + obj.unterrichtstunde.toString() + ',';
		}
		if (obj.stundenbeginn !== undefined) {
			result += '"stundenbeginn" : ' + ((!obj.stundenbeginn) ? 'null' : obj.stundenbeginn.toString()) + ',';
		}
		if (obj.stundenende !== undefined) {
			result += '"stundenende" : ' + ((!obj.stundenende) ? 'null' : obj.stundenende.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanZeitraster(obj : unknown) : StundenplanZeitraster {
	return obj as StundenplanZeitraster;
}
