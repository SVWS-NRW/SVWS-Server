import { JavaLong } from '../../../../java/lang/JavaLong';
import { JavaObject } from '../../../../java/lang/JavaObject';

export class GostSchuelerklausur extends JavaObject {

	/**
	 * Die ID des Stundenplans.
	 */
	public id : number = -1;

	/**
	 * Die textuelle Beschreibung des Stundenplans.
	 */
	public idKursklausur : number = -1;

	/**
	 * Das Zeitraster des Stundenplans.
	 */
	public idSchueler : number = -1;

	/**
	 * Die textuelle Bemerkung zur Schülerklausur, sofern vorhanden.
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
		return (another !== null) && (((another instanceof JavaObject) && ((another as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur')))) && (this.id === (cast_de_svws_nrw_core_data_gost_klausurplanung_GostSchuelerklausur(another)).id);
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
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostSchuelerklausur {
		const obj = JSON.parse(json) as Partial<GostSchuelerklausur>;
		const result = new GostSchuelerklausur();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idKursklausur === undefined)
			throw new Error('invalid json format, missing attribute idKursklausur');
		result.idKursklausur = obj.idKursklausur;
		if (obj.idSchueler === undefined)
			throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		result.bemerkung = (obj.bemerkung === undefined) ? null : obj.bemerkung === null ? null : obj.bemerkung;
		return result;
	}

	public static transpilerToJSON(obj : GostSchuelerklausur) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idKursklausur" : ' + obj.idKursklausur.toString() + ',';
		result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostSchuelerklausur>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idKursklausur !== undefined) {
			result += '"idKursklausur" : ' + obj.idKursklausur.toString() + ',';
		}
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		}
		if (obj.bemerkung !== undefined) {
			result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostSchuelerklausur(obj : unknown) : GostSchuelerklausur {
	return obj as GostSchuelerklausur;
}
