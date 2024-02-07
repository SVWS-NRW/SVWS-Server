import { JavaLong } from '../../../../java/lang/JavaLong';
import { JavaObject } from '../../../../java/lang/JavaObject';

export class GostKursklausur extends JavaObject {

	/**
	 * Die ID der Kursklausur.
	 */
	public id : number = -1;

	/**
	 * Die ID der Klausur-Vorgabe.
	 */
	public idVorgabe : number = -1;

	/**
	 * Die ID des Klausurkurses.
	 */
	public idKurs : number = -1;

	/**
	 * Die ID des Klausurtermins, sofern schon gesetzt.
	 */
	public idTermin : number | null = null;

	/**
	 * Die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins.
	 */
	public startzeit : number | null = null;

	/**
	 * Die textuelle Bemerkung zur Kursklausur, sofern vorhanden.
	 */
	public bemerkung : string | null = null;


	public constructor() {
		super();
	}

	/**
	 * Vergleicht, ob das akutelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte indentisch sind, sonst false
	 */
	public equals(another : unknown | null) : boolean {
		return another !== null && ((another instanceof JavaObject) && ((another as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur'))) && this.id === (cast_de_svws_nrw_core_data_gost_klausurplanung_GostKursklausur(another)).id;
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der id.
	 *
	 * @return den HashCode
	 */
	public hashCode() : number {
		return JavaLong.hashCode((this.id));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKursklausur {
		const obj = JSON.parse(json);
		const result = new GostKursklausur();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idVorgabe === "undefined")
			 throw new Error('invalid json format, missing attribute idVorgabe');
		result.idVorgabe = obj.idVorgabe;
		if (typeof obj.idKurs === "undefined")
			 throw new Error('invalid json format, missing attribute idKurs');
		result.idKurs = obj.idKurs;
		result.idTermin = typeof obj.idTermin === "undefined" ? null : obj.idTermin === null ? null : obj.idTermin;
		result.startzeit = typeof obj.startzeit === "undefined" ? null : obj.startzeit === null ? null : obj.startzeit;
		result.bemerkung = typeof obj.bemerkung === "undefined" ? null : obj.bemerkung === null ? null : obj.bemerkung;
		return result;
	}

	public static transpilerToJSON(obj : GostKursklausur) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idVorgabe" : ' + obj.idVorgabe + ',';
		result += '"idKurs" : ' + obj.idKurs + ',';
		result += '"idTermin" : ' + ((!obj.idTermin) ? 'null' : obj.idTermin) + ',';
		result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : obj.startzeit) + ',';
		result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKursklausur>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idVorgabe !== "undefined") {
			result += '"idVorgabe" : ' + obj.idVorgabe + ',';
		}
		if (typeof obj.idKurs !== "undefined") {
			result += '"idKurs" : ' + obj.idKurs + ',';
		}
		if (typeof obj.idTermin !== "undefined") {
			result += '"idTermin" : ' + ((!obj.idTermin) ? 'null' : obj.idTermin) + ',';
		}
		if (typeof obj.startzeit !== "undefined") {
			result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : obj.startzeit) + ',';
		}
		if (typeof obj.bemerkung !== "undefined") {
			result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKursklausur(obj : unknown) : GostKursklausur {
	return obj as GostKursklausur;
}
