import { JavaLong } from '../../../../java/lang/JavaLong';
import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class GostSchuelerklausurTermin extends JavaObject {

	/**
	 * Die ID des Schülerklausurtermins.
	 */
	public id : number = -1;

	/**
	 * Die ID der zugehörigen Schülerklausur.
	 */
	public idSchuelerklausur : number = -1;

	/**
	 * Die Folgenummer der Schülerklausur, 0 falls es sich um den Haupttermin handelt, 1 der erste Nachschreibtermin ...
	 */
	public folgeNr : number = -1;

	/**
	 * Die ID des Klausurtermins, falls schon gesetzt.
	 */
	public idTermin : number | null = null;

	/**
	 * Die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins.
	 */
	public startzeit : number | null = null;

	/**
	 * Die textuelle Bemerkung zum Schülerklausurtermin, sofern vorhanden.
	 */
	public bemerkung : string | null = null;


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
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin')))) && (this.id === (cast_de_svws_nrw_core_data_gost_klausurplanung_GostSchuelerklausurTermin(another)).id);
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
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin'].includes(name);
	}

	public static class = new Class<GostSchuelerklausurTermin>('de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin');

	public static transpilerFromJSON(json : string): GostSchuelerklausurTermin {
		const obj = JSON.parse(json) as Partial<GostSchuelerklausurTermin>;
		const result = new GostSchuelerklausurTermin();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idSchuelerklausur === undefined)
			throw new Error('invalid json format, missing attribute idSchuelerklausur');
		result.idSchuelerklausur = obj.idSchuelerklausur;
		if (obj.folgeNr === undefined)
			throw new Error('invalid json format, missing attribute folgeNr');
		result.folgeNr = obj.folgeNr;
		result.idTermin = (obj.idTermin === undefined) ? null : obj.idTermin === null ? null : obj.idTermin;
		result.startzeit = (obj.startzeit === undefined) ? null : obj.startzeit === null ? null : obj.startzeit;
		result.bemerkung = (obj.bemerkung === undefined) ? null : obj.bemerkung === null ? null : obj.bemerkung;
		return result;
	}

	public static transpilerToJSON(obj : GostSchuelerklausurTermin) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchuelerklausur" : ' + obj.idSchuelerklausur.toString() + ',';
		result += '"folgeNr" : ' + obj.folgeNr.toString() + ',';
		result += '"idTermin" : ' + ((obj.idTermin === null) ? 'null' : obj.idTermin.toString()) + ',';
		result += '"startzeit" : ' + ((obj.startzeit === null) ? 'null' : obj.startzeit.toString()) + ',';
		result += '"bemerkung" : ' + ((obj.bemerkung === null) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostSchuelerklausurTermin>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idSchuelerklausur !== undefined) {
			result += '"idSchuelerklausur" : ' + obj.idSchuelerklausur.toString() + ',';
		}
		if (obj.folgeNr !== undefined) {
			result += '"folgeNr" : ' + obj.folgeNr.toString() + ',';
		}
		if (obj.idTermin !== undefined) {
			result += '"idTermin" : ' + ((obj.idTermin === null) ? 'null' : obj.idTermin.toString()) + ',';
		}
		if (obj.startzeit !== undefined) {
			result += '"startzeit" : ' + ((obj.startzeit === null) ? 'null' : obj.startzeit.toString()) + ',';
		}
		if (obj.bemerkung !== undefined) {
			result += '"bemerkung" : ' + ((obj.bemerkung === null) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostSchuelerklausurTermin(obj : unknown) : GostSchuelerklausurTermin {
	return obj as GostSchuelerklausurTermin;
}
