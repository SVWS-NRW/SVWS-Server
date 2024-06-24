import { JavaObject } from '../../../java/lang/JavaObject';
import { Schulform, cast_de_svws_nrw_core_types_schule_Schulform } from '../../../core/types/schule/Schulform';
import { Schulgliederung, cast_de_svws_nrw_core_types_schule_Schulgliederung } from '../../../core/types/schule/Schulgliederung';

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
	 * Erstellt ein Objekt mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt ein Objekt mit den angegebenen Werten
	 *
	 * @param schulform      die Schulform
	 * @param gliederung     die Schulgliederung oder null
	 */
	public constructor(schulform : Schulform, gliederung : Schulgliederung | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : Schulform, __param1? : Schulgliederung | null) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.types.schule.Schulform')))) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.types.schule.Schulgliederung'))) || (__param1 === null))) {
			const schulform : Schulform = cast_de_svws_nrw_core_types_schule_Schulform(__param0);
			const gliederung : Schulgliederung | null = cast_de_svws_nrw_core_types_schule_Schulgliederung(__param1);
			this.schulform = schulform.daten.kuerzel;
			this.gliederung = (gliederung === null) ? null : gliederung.daten.kuerzel;
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.SchulformSchulgliederung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.SchulformSchulgliederung'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchulformSchulgliederung {
		const obj = JSON.parse(json);
		const result = new SchulformSchulgliederung();
		if (obj.schulform === undefined)
			 throw new Error('invalid json format, missing attribute schulform');
		result.schulform = obj.schulform;
		result.gliederung = (obj.gliederung === undefined) ? null : obj.gliederung === null ? null : obj.gliederung;
		return result;
	}

	public static transpilerToJSON(obj : SchulformSchulgliederung) : string {
		let result = '{';
		result += '"schulform" : ' + JSON.stringify(obj.schulform!) + ',';
		result += '"gliederung" : ' + ((!obj.gliederung) ? 'null' : JSON.stringify(obj.gliederung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchulformSchulgliederung>) : string {
		let result = '{';
		if (obj.schulform !== undefined) {
			result += '"schulform" : ' + JSON.stringify(obj.schulform!) + ',';
		}
		if (obj.gliederung !== undefined) {
			result += '"gliederung" : ' + ((!obj.gliederung) ? 'null' : JSON.stringify(obj.gliederung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_SchulformSchulgliederung(obj : unknown) : SchulformSchulgliederung {
	return obj as SchulformSchulgliederung;
}
