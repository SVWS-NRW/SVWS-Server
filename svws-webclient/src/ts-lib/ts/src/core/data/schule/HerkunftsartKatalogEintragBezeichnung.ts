import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { Schulform, cast_de_nrw_schule_svws_core_types_schule_Schulform } from '../../../core/types/schule/Schulform';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class HerkunftsartKatalogEintragBezeichnung extends JavaObject {

	/**
	 * Das Kürzel der Schulform 
	 */
	public schulform : String = "";

	/**
	 * Die Kurz-Bezeichnung der Herkunftsart 
	 */
	public kurzBezeichnung : String = "";

	/**
	 * Die Bezeichnung der Herkunftsart 
	 */
	public bezeichnung : String = "";


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 *
	 * @param schulform         die Schulform
	 * @param kurzBezeichnung   die Kurz-Bezeichnung der Herkunftsart
	 * @param bezeichnung       die Bezeichnung der Herkunftsart
	 */
	public constructor(schulform : Schulform, kurzBezeichnung : String, bezeichnung : String);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : Schulform, __param1? : String, __param2? : String) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.schule.Schulform')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof String) || (typeof __param1 === "string"))) && ((typeof __param2 !== "undefined") && ((__param2 instanceof String) || (typeof __param2 === "string")))) {
			let schulform : Schulform = cast_de_nrw_schule_svws_core_types_schule_Schulform(__param0);
			let kurzBezeichnung : String = __param1;
			let bezeichnung : String = __param2;
			this.schulform = schulform.daten.kuerzel;
			this.kurzBezeichnung = kurzBezeichnung;
			this.bezeichnung = bezeichnung;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.HerkunftsartKatalogEintragBezeichnung'].includes(name);
	}

	public static transpilerFromJSON(json : string): HerkunftsartKatalogEintragBezeichnung {
		const obj = JSON.parse(json);
		const result = new HerkunftsartKatalogEintragBezeichnung();
		if (typeof obj.schulform === "undefined")
			 throw new Error('invalid json format, missing attribute schulform');
		result.schulform = String(obj.schulform);
		if (typeof obj.kurzBezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute kurzBezeichnung');
		result.kurzBezeichnung = String(obj.kurzBezeichnung);
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = String(obj.bezeichnung);
		return result;
	}

	public static transpilerToJSON(obj : HerkunftsartKatalogEintragBezeichnung) : string {
		let result = '{';
		result += '"schulform" : ' + '"' + obj.schulform.valueOf() + '"' + ',';
		result += '"kurzBezeichnung" : ' + '"' + obj.kurzBezeichnung.valueOf() + '"' + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<HerkunftsartKatalogEintragBezeichnung>) : string {
		let result = '{';
		if (typeof obj.schulform !== "undefined") {
			result += '"schulform" : ' + '"' + obj.schulform.valueOf() + '"' + ',';
		}
		if (typeof obj.kurzBezeichnung !== "undefined") {
			result += '"kurzBezeichnung" : ' + '"' + obj.kurzBezeichnung.valueOf() + '"' + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schule_HerkunftsartKatalogEintragBezeichnung(obj : unknown) : HerkunftsartKatalogEintragBezeichnung {
	return obj as HerkunftsartKatalogEintragBezeichnung;
}
