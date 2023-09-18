import { JavaObject } from '../../../java/lang/JavaObject';

export class Schild3KatalogEintragUnicodeUmwandlung extends JavaObject {

	/**
	 * ID für den Primärschlüssel der Tabelle UnicodeUmwandlung
	 */
	public ID : number | null = null;

	/**
	 * Unicodezeichen das umgewandelt werden muss
	 */
	public Unicodezeichen : string | null = null;

	/**
	 * Ersatzzeichen für das Unicodezeichen
	 */
	public Ersatzzeichen : string | null = null;

	/**
	 * Unicodezeichen in Dezimaldarstellung
	 */
	public DecimalZeichen : string | null = null;

	/**
	 * Ersatzzeichen in Dezimaldarstellung (bei zwei Zeichen mit + getrennt)
	 */
	public DecimalErsatzzeichen : string | null = null;

	/**
	 * Hexdarstellung des Unicodezeichen das gewandelt werden muss
	 */
	public Hexzeichen : string | null = null;

	/**
	 * Hexdarstellung des Ersatzzeichens das gewandelt werden muss (bei zwei Zeichen mit + getrennt)
	 */
	public HexErsatzzeichen : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.Schild3KatalogEintragUnicodeUmwandlung'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragUnicodeUmwandlung {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragUnicodeUmwandlung();
		result.ID = typeof obj.ID === "undefined" ? null : obj.ID === null ? null : obj.ID;
		result.Unicodezeichen = typeof obj.Unicodezeichen === "undefined" ? null : obj.Unicodezeichen === null ? null : obj.Unicodezeichen;
		result.Ersatzzeichen = typeof obj.Ersatzzeichen === "undefined" ? null : obj.Ersatzzeichen === null ? null : obj.Ersatzzeichen;
		result.DecimalZeichen = typeof obj.DecimalZeichen === "undefined" ? null : obj.DecimalZeichen === null ? null : obj.DecimalZeichen;
		result.DecimalErsatzzeichen = typeof obj.DecimalErsatzzeichen === "undefined" ? null : obj.DecimalErsatzzeichen === null ? null : obj.DecimalErsatzzeichen;
		result.Hexzeichen = typeof obj.Hexzeichen === "undefined" ? null : obj.Hexzeichen === null ? null : obj.Hexzeichen;
		result.HexErsatzzeichen = typeof obj.HexErsatzzeichen === "undefined" ? null : obj.HexErsatzzeichen === null ? null : obj.HexErsatzzeichen;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragUnicodeUmwandlung) : string {
		let result = '{';
		result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID) + ',';
		result += '"Unicodezeichen" : ' + ((!obj.Unicodezeichen) ? 'null' : JSON.stringify(obj.Unicodezeichen)) + ',';
		result += '"Ersatzzeichen" : ' + ((!obj.Ersatzzeichen) ? 'null' : JSON.stringify(obj.Ersatzzeichen)) + ',';
		result += '"DecimalZeichen" : ' + ((!obj.DecimalZeichen) ? 'null' : JSON.stringify(obj.DecimalZeichen)) + ',';
		result += '"DecimalErsatzzeichen" : ' + ((!obj.DecimalErsatzzeichen) ? 'null' : JSON.stringify(obj.DecimalErsatzzeichen)) + ',';
		result += '"Hexzeichen" : ' + ((!obj.Hexzeichen) ? 'null' : JSON.stringify(obj.Hexzeichen)) + ',';
		result += '"HexErsatzzeichen" : ' + ((!obj.HexErsatzzeichen) ? 'null' : JSON.stringify(obj.HexErsatzzeichen)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragUnicodeUmwandlung>) : string {
		let result = '{';
		if (typeof obj.ID !== "undefined") {
			result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID) + ',';
		}
		if (typeof obj.Unicodezeichen !== "undefined") {
			result += '"Unicodezeichen" : ' + ((!obj.Unicodezeichen) ? 'null' : JSON.stringify(obj.Unicodezeichen)) + ',';
		}
		if (typeof obj.Ersatzzeichen !== "undefined") {
			result += '"Ersatzzeichen" : ' + ((!obj.Ersatzzeichen) ? 'null' : JSON.stringify(obj.Ersatzzeichen)) + ',';
		}
		if (typeof obj.DecimalZeichen !== "undefined") {
			result += '"DecimalZeichen" : ' + ((!obj.DecimalZeichen) ? 'null' : JSON.stringify(obj.DecimalZeichen)) + ',';
		}
		if (typeof obj.DecimalErsatzzeichen !== "undefined") {
			result += '"DecimalErsatzzeichen" : ' + ((!obj.DecimalErsatzzeichen) ? 'null' : JSON.stringify(obj.DecimalErsatzzeichen)) + ',';
		}
		if (typeof obj.Hexzeichen !== "undefined") {
			result += '"Hexzeichen" : ' + ((!obj.Hexzeichen) ? 'null' : JSON.stringify(obj.Hexzeichen)) + ',';
		}
		if (typeof obj.HexErsatzzeichen !== "undefined") {
			result += '"HexErsatzzeichen" : ' + ((!obj.HexErsatzzeichen) ? 'null' : JSON.stringify(obj.HexErsatzzeichen)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_Schild3KatalogEintragUnicodeUmwandlung(obj : unknown) : Schild3KatalogEintragUnicodeUmwandlung {
	return obj as Schild3KatalogEintragUnicodeUmwandlung;
}
