import { JavaObject } from '../../../../java/lang/JavaObject';
import { BKGymBelegungsfehler, cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegungsfehler } from '../../../../core/abschluss/bk/d/BKGymBelegungsfehler';
import { Class } from '../../../../java/lang/Class';

export class BKGymBelegpruefungErgebnisFehler extends JavaObject {

	/**
	 * Ein eindeutiger Fehlercode für den Fehler
	 */
	public code : string = "";

	/**
	 * Die Art des Belegungsfehlers.
	 */
	public art : string = "";

	/**
	 * Eine textuelle Beschreibung des Fehlers.
	 */
	public beschreibung : string = "";


	/**
	 * Erzeugt eine neue Instanz eines Fehlers beim Ergebnis der Belegprüfung.
	 *
	 * @param f           der Typ des Belegungsfehlers
	 */
	public constructor(f : BKGymBelegungsfehler);

	/**
	 * Erzeugt eine neue Instanz eines Fehlers beim Ergebnis der Belegprüfung.
	 * Dieser Konstruktor dient als Default-Konstruktor für die Serialisierung / Deserialisierung
	 * aus JSON-Dateien.
	 */
	public constructor();

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : BKGymBelegungsfehler) {
		super();
		if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.abschluss.bk.d.BKGymBelegungsfehler'))))) {
			const f : BKGymBelegungsfehler = cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegungsfehler(__param0);
			this.code = f.toString();
			this.art = f.getArt().kuerzel;
			this.beschreibung = f.getText();
		} else if ((__param0 === undefined)) {
			// empty method body
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungErgebnisFehler';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungErgebnisFehler'].includes(name);
	}

	public static class = new Class<BKGymBelegpruefungErgebnisFehler>('de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungErgebnisFehler');

	public static transpilerFromJSON(json : string): BKGymBelegpruefungErgebnisFehler {
		const obj = JSON.parse(json) as Partial<BKGymBelegpruefungErgebnisFehler>;
		const result = new BKGymBelegpruefungErgebnisFehler();
		if (obj.code === undefined)
			throw new Error('invalid json format, missing attribute code');
		result.code = obj.code;
		if (obj.art === undefined)
			throw new Error('invalid json format, missing attribute art');
		result.art = obj.art;
		if (obj.beschreibung === undefined)
			throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		return result;
	}

	public static transpilerToJSON(obj : BKGymBelegpruefungErgebnisFehler) : string {
		let result = '{';
		result += '"code" : ' + JSON.stringify(obj.code) + ',';
		result += '"art" : ' + JSON.stringify(obj.art) + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BKGymBelegpruefungErgebnisFehler>) : string {
		let result = '{';
		if (obj.code !== undefined) {
			result += '"code" : ' + JSON.stringify(obj.code) + ',';
		}
		if (obj.art !== undefined) {
			result += '"art" : ' + JSON.stringify(obj.art) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegpruefungErgebnisFehler(obj : unknown) : BKGymBelegpruefungErgebnisFehler {
	return obj as BKGymBelegpruefungErgebnisFehler;
}
