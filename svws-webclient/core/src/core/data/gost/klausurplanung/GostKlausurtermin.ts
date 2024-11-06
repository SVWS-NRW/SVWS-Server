import { JavaLong } from '../../../../java/lang/JavaLong';
import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class GostKlausurtermin extends JavaObject {

	/**
	 * Die ID des Klausurtermins.
	 */
	public id : number = -1;

	/**
	 * Das Jahr, in welchem der Jahrgang Abitur machen wird.
	 */
	public abijahr : number = -1;

	/**
	 * Das Gost-Halbjahr, in dem die Klausurg geschrieben wird.
	 */
	public halbjahr : number = -1;

	/**
	 * Das Quartal, in welchem die Klausur gechrieben wird.
	 */
	public quartal : number = -1;

	/**
	 * Das Datum des Klausurtermins, falls schon gesetzt.
	 */
	public datum : string | null = null;

	/**
	 * Die Startzeit des Klausurtermins in Minuten seit 0 Uhr, falls schon gesetzt.
	 */
	public startzeit : number | null = null;

	/**
	 * Die Bezeichnung des Klausurtermins, falls schon gesetzt.
	 */
	public bezeichnung : string | null = null;

	/**
	 * Die textuelle Bemerkung zum Termin, sofern vorhanden.
	 */
	public bemerkung : string | null = null;

	/**
	 * Die Information, ob es sich um einen Haupttermin handelt oder nicht.
	 */
	public istHaupttermin : boolean = false;

	/**
	 * Die Information, ob es bei einen Haupttermin Nachschreibklausuren zugelassen sind oder nicht.
	 */
	public nachschreiberZugelassen : boolean = false;


	/**
	 * Default-Konstruktor
	 */
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
		return (another !== null) && (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin')))) && (this.id === (cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurtermin(another)).id);
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
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin'].includes(name);
	}

	public static class = new Class<GostKlausurtermin>('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin');

	public static transpilerFromJSON(json : string): GostKlausurtermin {
		const obj = JSON.parse(json) as Partial<GostKlausurtermin>;
		const result = new GostKlausurtermin();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.abijahr === undefined)
			throw new Error('invalid json format, missing attribute abijahr');
		result.abijahr = obj.abijahr;
		if (obj.halbjahr === undefined)
			throw new Error('invalid json format, missing attribute halbjahr');
		result.halbjahr = obj.halbjahr;
		if (obj.quartal === undefined)
			throw new Error('invalid json format, missing attribute quartal');
		result.quartal = obj.quartal;
		result.datum = (obj.datum === undefined) ? null : obj.datum === null ? null : obj.datum;
		result.startzeit = (obj.startzeit === undefined) ? null : obj.startzeit === null ? null : obj.startzeit;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		result.bemerkung = (obj.bemerkung === undefined) ? null : obj.bemerkung === null ? null : obj.bemerkung;
		if (obj.istHaupttermin === undefined)
			throw new Error('invalid json format, missing attribute istHaupttermin');
		result.istHaupttermin = obj.istHaupttermin;
		if (obj.nachschreiberZugelassen === undefined)
			throw new Error('invalid json format, missing attribute nachschreiberZugelassen');
		result.nachschreiberZugelassen = obj.nachschreiberZugelassen;
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurtermin) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"abijahr" : ' + obj.abijahr.toString() + ',';
		result += '"halbjahr" : ' + obj.halbjahr.toString() + ',';
		result += '"quartal" : ' + obj.quartal.toString() + ',';
		result += '"datum" : ' + ((obj.datum === null) ? 'null' : JSON.stringify(obj.datum)) + ',';
		result += '"startzeit" : ' + ((obj.startzeit === null) ? 'null' : obj.startzeit.toString()) + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"bemerkung" : ' + ((obj.bemerkung === null) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result += '"istHaupttermin" : ' + obj.istHaupttermin.toString() + ',';
		result += '"nachschreiberZugelassen" : ' + obj.nachschreiberZugelassen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurtermin>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.abijahr !== undefined) {
			result += '"abijahr" : ' + obj.abijahr.toString() + ',';
		}
		if (obj.halbjahr !== undefined) {
			result += '"halbjahr" : ' + obj.halbjahr.toString() + ',';
		}
		if (obj.quartal !== undefined) {
			result += '"quartal" : ' + obj.quartal.toString() + ',';
		}
		if (obj.datum !== undefined) {
			result += '"datum" : ' + ((obj.datum === null) ? 'null' : JSON.stringify(obj.datum)) + ',';
		}
		if (obj.startzeit !== undefined) {
			result += '"startzeit" : ' + ((obj.startzeit === null) ? 'null' : obj.startzeit.toString()) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (obj.bemerkung !== undefined) {
			result += '"bemerkung" : ' + ((obj.bemerkung === null) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		}
		if (obj.istHaupttermin !== undefined) {
			result += '"istHaupttermin" : ' + obj.istHaupttermin.toString() + ',';
		}
		if (obj.nachschreiberZugelassen !== undefined) {
			result += '"nachschreiberZugelassen" : ' + obj.nachschreiberZugelassen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurtermin(obj : unknown) : GostKlausurtermin {
	return obj as GostKlausurtermin;
}
