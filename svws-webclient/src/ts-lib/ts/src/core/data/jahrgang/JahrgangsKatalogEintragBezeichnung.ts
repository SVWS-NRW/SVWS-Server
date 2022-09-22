import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { Schulform, cast_de_nrw_schule_svws_core_types_statkue_Schulform } from '../../../core/types/statkue/Schulform';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class JahrgangsKatalogEintragBezeichnung extends JavaObject {

	public schulform : String = "";

	public bezeichnung : String = "";


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 *
	 * @param schulform     die Schulform
	 * @param bezeichnung   die Bezeichnung des Jahrgangs
	 */
	public constructor(schulform : Schulform, bezeichnung : String);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : Schulform, __param1? : String) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.statkue.Schulform')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof String) || (typeof __param1 === "string")))) {
			let schulform : Schulform = cast_de_nrw_schule_svws_core_types_statkue_Schulform(__param0);
			let bezeichnung : String = __param1;
			this.schulform = schulform.daten.kuerzel;
			this.bezeichnung = bezeichnung;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.jahrgang.JahrgangsKatalogEintragBezeichnung'].includes(name);
	}

	public static transpilerFromJSON(json : string): JahrgangsKatalogEintragBezeichnung {
		const obj = JSON.parse(json);
		const result = new JahrgangsKatalogEintragBezeichnung();
		if (typeof obj.schulform === "undefined")
			 throw new Error('invalid json format, missing attribute schulform');
		result.schulform = obj.schulform;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		return result;
	}

	public static transpilerToJSON(obj : JahrgangsKatalogEintragBezeichnung) : string {
		let result = '{';
		result += '"schulform" : ' + '"' + obj.schulform.valueOf() + '"' + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<JahrgangsKatalogEintragBezeichnung>) : string {
		let result = '{';
		if (typeof obj.schulform !== "undefined") {
			result += '"schulform" : ' + '"' + obj.schulform.valueOf() + '"' + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_jahrgang_JahrgangsKatalogEintragBezeichnung(obj : unknown) : JahrgangsKatalogEintragBezeichnung {
	return obj as JahrgangsKatalogEintragBezeichnung;
}
