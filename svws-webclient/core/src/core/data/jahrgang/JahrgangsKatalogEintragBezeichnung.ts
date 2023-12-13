import { JavaObject } from '../../../java/lang/JavaObject';
import { Schulform, cast_de_svws_nrw_core_types_schule_Schulform } from '../../../core/types/schule/Schulform';

export class JahrgangsKatalogEintragBezeichnung extends JavaObject {

	/**
	 * Das Kürzel der Schulform
	 */
	public schulform : string = "";

	/**
	 * Die Bezeichnung des Jahrgangs
	 */
	public bezeichnung : string = "";


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
	public constructor(schulform : Schulform, bezeichnung : string);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : Schulform, __param1? : string) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined")) {
			// empty method body
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.types.schule.Schulform')))) && ((typeof __param1 !== "undefined") && (typeof __param1 === "string"))) {
			const schulform : Schulform = cast_de_svws_nrw_core_types_schule_Schulform(__param0);
			const bezeichnung : string = __param1;
			this.schulform = schulform.daten.kuerzel;
			this.bezeichnung = bezeichnung;
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.jahrgang.JahrgangsKatalogEintragBezeichnung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.jahrgang.JahrgangsKatalogEintragBezeichnung'].includes(name);
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
		result += '"schulform" : ' + JSON.stringify(obj.schulform!) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<JahrgangsKatalogEintragBezeichnung>) : string {
		let result = '{';
		if (typeof obj.schulform !== "undefined") {
			result += '"schulform" : ' + JSON.stringify(obj.schulform!) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_jahrgang_JahrgangsKatalogEintragBezeichnung(obj : unknown) : JahrgangsKatalogEintragBezeichnung {
	return obj as JahrgangsKatalogEintragBezeichnung;
}
