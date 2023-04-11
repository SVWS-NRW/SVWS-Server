import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBelegpruefungsArt, cast_de_svws_nrw_core_abschluss_gost_GostBelegpruefungsArt } from '../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostBelegungsfehler, cast_de_svws_nrw_core_abschluss_gost_GostBelegungsfehler } from '../../../core/abschluss/gost/GostBelegungsfehler';

export class GostBelegpruefungErgebnisFehler extends JavaObject {

	/**
	 * Ein eindeutiger Fehlercode für den Fehler
	 */
	public code : string = "";

	/**
	 * Die Art des Belegungsfehlers (siehe {@link GostBelegungsfehlerArt}).
	 */
	public art : string = "";

	/**
	 * Eine textuelle Beschreibung des Fehlers.
	 */
	public beschreibung : string = "";


	/**
	 * Erzeugt eine neue Instanz eines Fehlers beim Ergebnis der Belegprüfung.
	 *
	 * @param f           der Typ des Belegungsfehlers (siehe {@link GostBelegungsfehler})
	 * @param pruefArt    die Art der durchgeführten Belegungsprüfung (siehe {@link GostBelegpruefungsArt}), um
	 *                    die konkrete Ausprägung des Textinformationen bestimmen zu können.
	 */
	public constructor(f : GostBelegungsfehler, pruefArt : GostBelegpruefungsArt);

	/**
	 * Erzeugt eine neue Instanz eines Fehlers beim Ergebnis der Belegprüfung.
	 * Dieser Konstruktor dient als Default-Konstruktor für die Serialisierung / Deserialisierung
	 * aus JSON-Dateien.
	 */
	public constructor();

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : GostBelegungsfehler, __param1? : GostBelegpruefungsArt) {
		super();
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.abschluss.gost.GostBelegungsfehler')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt'))))) {
			const f : GostBelegungsfehler = cast_de_svws_nrw_core_abschluss_gost_GostBelegungsfehler(__param0);
			const pruefArt : GostBelegpruefungsArt = cast_de_svws_nrw_core_abschluss_gost_GostBelegpruefungsArt(__param1);
			this.code = f.toString();
			this.art = f.getArt().kuerzel;
			this.beschreibung = f.getText(pruefArt);
		} else if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined")) {
			// empty method body
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnisFehler'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBelegpruefungErgebnisFehler {
		const obj = JSON.parse(json);
		const result = new GostBelegpruefungErgebnisFehler();
		if (typeof obj.code === "undefined")
			 throw new Error('invalid json format, missing attribute code');
		result.code = obj.code;
		if (typeof obj.art === "undefined")
			 throw new Error('invalid json format, missing attribute art');
		result.art = obj.art;
		if (typeof obj.beschreibung === "undefined")
			 throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		return result;
	}

	public static transpilerToJSON(obj : GostBelegpruefungErgebnisFehler) : string {
		let result = '{';
		result += '"code" : ' + '"' + obj.code! + '"' + ',';
		result += '"art" : ' + '"' + obj.art! + '"' + ',';
		result += '"beschreibung" : ' + '"' + obj.beschreibung! + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBelegpruefungErgebnisFehler>) : string {
		let result = '{';
		if (typeof obj.code !== "undefined") {
			result += '"code" : ' + '"' + obj.code + '"' + ',';
		}
		if (typeof obj.art !== "undefined") {
			result += '"art" : ' + '"' + obj.art + '"' + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + '"' + obj.beschreibung + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_GostBelegpruefungErgebnisFehler(obj : unknown) : GostBelegpruefungErgebnisFehler {
	return obj as GostBelegpruefungErgebnisFehler;
}
