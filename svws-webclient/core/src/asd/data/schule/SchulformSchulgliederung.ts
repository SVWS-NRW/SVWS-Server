import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchulformSchulgliederung extends JavaObject {

	/**
	 * Das Kürzel der Schulform
	 */
	public schulform : string = "GY";

	/**
	 * Das Kürzel der Schulgliederung bzw. des Bildungsganges. Null, falls alle Gliederungen der Schulform gemeint sind.
	 */
	public gliederung : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schule.SchulformSchulgliederung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.schule.SchulformSchulgliederung'].includes(name);
	}

	public static class = new Class<SchulformSchulgliederung>('de.svws_nrw.asd.data.schule.SchulformSchulgliederung');

	public static transpilerFromJSON(json : string): SchulformSchulgliederung {
		const obj = JSON.parse(json) as Partial<SchulformSchulgliederung>;
		const result = new SchulformSchulgliederung();
		if (obj.schulform === undefined)
			throw new Error('invalid json format, missing attribute schulform');
		result.schulform = obj.schulform;
		result.gliederung = (obj.gliederung === undefined) ? null : obj.gliederung === null ? null : obj.gliederung;
		return result;
	}

	public static transpilerToJSON(obj : SchulformSchulgliederung) : string {
		let result = '{';
		result += '"schulform" : ' + JSON.stringify(obj.schulform) + ',';
		result += '"gliederung" : ' + ((obj.gliederung === null) ? 'null' : JSON.stringify(obj.gliederung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchulformSchulgliederung>) : string {
		let result = '{';
		if (obj.schulform !== undefined) {
			result += '"schulform" : ' + JSON.stringify(obj.schulform) + ',';
		}
		if (obj.gliederung !== undefined) {
			result += '"gliederung" : ' + ((obj.gliederung === null) ? 'null' : JSON.stringify(obj.gliederung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schule_SchulformSchulgliederung(obj : unknown) : SchulformSchulgliederung {
	return obj as SchulformSchulgliederung;
}
