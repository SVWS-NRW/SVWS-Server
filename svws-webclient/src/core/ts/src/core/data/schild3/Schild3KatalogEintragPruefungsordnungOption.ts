import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragPruefungsordnungOption extends JavaObject {

	/**
	 * Schulformen für die die Optionen gelten
	 */
	public OP_Schulformen : string | null = null;

	/**
	 * Kürzel der Prüfungsordung
	 */
	public OP_POKrz : string | null = null;

	/**
	 * Angezeigter Kurztext in Schild-NRW
	 */
	public OP_Krz : string | null = null;

	/**
	 * Abgangsart in der Statistik B
	 */
	public OP_Abgangsart_B : string | null = null;

	/**
	 * Abgangsart in der Statistik NB
	 */
	public OP_Abgangsart_NB : string | null = null;

	/**
	 * A=Allgemein B=Berufsbildend
	 */
	public OP_Art : string | null = null;

	/**
	 * Abschlusskürzel in Schild-NRW
	 */
	public OP_Typ : string | null = null;

	/**
	 * Bildungsgang A oder B
	 */
	public OP_Bildungsgang : string | null = null;

	/**
	 * Text des Abschlusses
	 */
	public OP_Name : string | null = null;

	/**
	 * Paragraph in der BASS (veraltet?)
	 */
	public OP_Kommentar : string | null = null;

	/**
	 * Zulässig für diese Jahrgänge
	 */
	public OP_Jahrgaenge : string | null = null;

	/**
	 * Zulässig für BKIndex
	 */
	public OP_BKIndex : string | null = null;

	/**
	 * Zulässig für diese Gliederungen
	 */
	public OP_BKAnl_Typ : string | null = null;

	/**
	 * Reihenfolge
	 */
	public OP_Reihenfolge : number | null = null;

	/**
	 * Gültig ab Schuljahr
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gültig bis Schuljahr
	 */
	public gueltigBis : number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragPruefungsordnungOption'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragPruefungsordnungOption {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragPruefungsordnungOption();
		result.OP_Schulformen = typeof obj.OP_Schulformen === "undefined" ? null : obj.OP_Schulformen === null ? null : obj.OP_Schulformen;
		result.OP_POKrz = typeof obj.OP_POKrz === "undefined" ? null : obj.OP_POKrz === null ? null : obj.OP_POKrz;
		result.OP_Krz = typeof obj.OP_Krz === "undefined" ? null : obj.OP_Krz === null ? null : obj.OP_Krz;
		result.OP_Abgangsart_B = typeof obj.OP_Abgangsart_B === "undefined" ? null : obj.OP_Abgangsart_B === null ? null : obj.OP_Abgangsart_B;
		result.OP_Abgangsart_NB = typeof obj.OP_Abgangsart_NB === "undefined" ? null : obj.OP_Abgangsart_NB === null ? null : obj.OP_Abgangsart_NB;
		result.OP_Art = typeof obj.OP_Art === "undefined" ? null : obj.OP_Art === null ? null : obj.OP_Art;
		result.OP_Typ = typeof obj.OP_Typ === "undefined" ? null : obj.OP_Typ === null ? null : obj.OP_Typ;
		result.OP_Bildungsgang = typeof obj.OP_Bildungsgang === "undefined" ? null : obj.OP_Bildungsgang === null ? null : obj.OP_Bildungsgang;
		result.OP_Name = typeof obj.OP_Name === "undefined" ? null : obj.OP_Name === null ? null : obj.OP_Name;
		result.OP_Kommentar = typeof obj.OP_Kommentar === "undefined" ? null : obj.OP_Kommentar === null ? null : obj.OP_Kommentar;
		result.OP_Jahrgaenge = typeof obj.OP_Jahrgaenge === "undefined" ? null : obj.OP_Jahrgaenge === null ? null : obj.OP_Jahrgaenge;
		result.OP_BKIndex = typeof obj.OP_BKIndex === "undefined" ? null : obj.OP_BKIndex === null ? null : obj.OP_BKIndex;
		result.OP_BKAnl_Typ = typeof obj.OP_BKAnl_Typ === "undefined" ? null : obj.OP_BKAnl_Typ === null ? null : obj.OP_BKAnl_Typ;
		result.OP_Reihenfolge = typeof obj.OP_Reihenfolge === "undefined" ? null : obj.OP_Reihenfolge === null ? null : obj.OP_Reihenfolge;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragPruefungsordnungOption) : string {
		let result = '{';
		result += '"OP_Schulformen" : ' + ((!obj.OP_Schulformen) ? 'null' : '"' + obj.OP_Schulformen + '"') + ',';
		result += '"OP_POKrz" : ' + ((!obj.OP_POKrz) ? 'null' : '"' + obj.OP_POKrz + '"') + ',';
		result += '"OP_Krz" : ' + ((!obj.OP_Krz) ? 'null' : '"' + obj.OP_Krz + '"') + ',';
		result += '"OP_Abgangsart_B" : ' + ((!obj.OP_Abgangsart_B) ? 'null' : '"' + obj.OP_Abgangsart_B + '"') + ',';
		result += '"OP_Abgangsart_NB" : ' + ((!obj.OP_Abgangsart_NB) ? 'null' : '"' + obj.OP_Abgangsart_NB + '"') + ',';
		result += '"OP_Art" : ' + ((!obj.OP_Art) ? 'null' : '"' + obj.OP_Art + '"') + ',';
		result += '"OP_Typ" : ' + ((!obj.OP_Typ) ? 'null' : '"' + obj.OP_Typ + '"') + ',';
		result += '"OP_Bildungsgang" : ' + ((!obj.OP_Bildungsgang) ? 'null' : '"' + obj.OP_Bildungsgang + '"') + ',';
		result += '"OP_Name" : ' + ((!obj.OP_Name) ? 'null' : '"' + obj.OP_Name + '"') + ',';
		result += '"OP_Kommentar" : ' + ((!obj.OP_Kommentar) ? 'null' : '"' + obj.OP_Kommentar + '"') + ',';
		result += '"OP_Jahrgaenge" : ' + ((!obj.OP_Jahrgaenge) ? 'null' : '"' + obj.OP_Jahrgaenge + '"') + ',';
		result += '"OP_BKIndex" : ' + ((!obj.OP_BKIndex) ? 'null' : '"' + obj.OP_BKIndex + '"') + ',';
		result += '"OP_BKAnl_Typ" : ' + ((!obj.OP_BKAnl_Typ) ? 'null' : '"' + obj.OP_BKAnl_Typ + '"') + ',';
		result += '"OP_Reihenfolge" : ' + ((!obj.OP_Reihenfolge) ? 'null' : obj.OP_Reihenfolge) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragPruefungsordnungOption>) : string {
		let result = '{';
		if (typeof obj.OP_Schulformen !== "undefined") {
			result += '"OP_Schulformen" : ' + ((!obj.OP_Schulformen) ? 'null' : '"' + obj.OP_Schulformen + '"') + ',';
		}
		if (typeof obj.OP_POKrz !== "undefined") {
			result += '"OP_POKrz" : ' + ((!obj.OP_POKrz) ? 'null' : '"' + obj.OP_POKrz + '"') + ',';
		}
		if (typeof obj.OP_Krz !== "undefined") {
			result += '"OP_Krz" : ' + ((!obj.OP_Krz) ? 'null' : '"' + obj.OP_Krz + '"') + ',';
		}
		if (typeof obj.OP_Abgangsart_B !== "undefined") {
			result += '"OP_Abgangsart_B" : ' + ((!obj.OP_Abgangsart_B) ? 'null' : '"' + obj.OP_Abgangsart_B + '"') + ',';
		}
		if (typeof obj.OP_Abgangsart_NB !== "undefined") {
			result += '"OP_Abgangsart_NB" : ' + ((!obj.OP_Abgangsart_NB) ? 'null' : '"' + obj.OP_Abgangsart_NB + '"') + ',';
		}
		if (typeof obj.OP_Art !== "undefined") {
			result += '"OP_Art" : ' + ((!obj.OP_Art) ? 'null' : '"' + obj.OP_Art + '"') + ',';
		}
		if (typeof obj.OP_Typ !== "undefined") {
			result += '"OP_Typ" : ' + ((!obj.OP_Typ) ? 'null' : '"' + obj.OP_Typ + '"') + ',';
		}
		if (typeof obj.OP_Bildungsgang !== "undefined") {
			result += '"OP_Bildungsgang" : ' + ((!obj.OP_Bildungsgang) ? 'null' : '"' + obj.OP_Bildungsgang + '"') + ',';
		}
		if (typeof obj.OP_Name !== "undefined") {
			result += '"OP_Name" : ' + ((!obj.OP_Name) ? 'null' : '"' + obj.OP_Name + '"') + ',';
		}
		if (typeof obj.OP_Kommentar !== "undefined") {
			result += '"OP_Kommentar" : ' + ((!obj.OP_Kommentar) ? 'null' : '"' + obj.OP_Kommentar + '"') + ',';
		}
		if (typeof obj.OP_Jahrgaenge !== "undefined") {
			result += '"OP_Jahrgaenge" : ' + ((!obj.OP_Jahrgaenge) ? 'null' : '"' + obj.OP_Jahrgaenge + '"') + ',';
		}
		if (typeof obj.OP_BKIndex !== "undefined") {
			result += '"OP_BKIndex" : ' + ((!obj.OP_BKIndex) ? 'null' : '"' + obj.OP_BKIndex + '"') + ',';
		}
		if (typeof obj.OP_BKAnl_Typ !== "undefined") {
			result += '"OP_BKAnl_Typ" : ' + ((!obj.OP_BKAnl_Typ) ? 'null' : '"' + obj.OP_BKAnl_Typ + '"') + ',';
		}
		if (typeof obj.OP_Reihenfolge !== "undefined") {
			result += '"OP_Reihenfolge" : ' + ((!obj.OP_Reihenfolge) ? 'null' : obj.OP_Reihenfolge) + ',';
		}
		if (typeof obj.gueltigVon !== "undefined") {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragPruefungsordnungOption(obj : unknown) : Schild3KatalogEintragPruefungsordnungOption {
	return obj as Schild3KatalogEintragPruefungsordnungOption;
}
