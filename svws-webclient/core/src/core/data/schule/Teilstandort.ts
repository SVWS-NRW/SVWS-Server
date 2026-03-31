import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Teilstandort extends JavaObject {

	/**
	 * Das Adressmerkmal des Teilstandortes (meist ein Buchstabe wie A, B, ...)
	 */
	public adrMerkmal: string | null = null;

	/**
	 * Die Postleitzahl des Teilstandortes
	 */
	public plz: string | null = null;

	/**
	 * Der Ort des Teilstandortes
	 */
	public ort: string | null = null;

	/**
	 * Der Straßenname des Teilstandortes
	 */
	public strassenname: string | null = null;

	/**
	 * Die Hausnummer des Teilstandortes
	 */
	public hausNr: string | null = null;

	/**
	 * Der Hausnummernzusatz des Teilstandortes
	 */
	public hausNrZusatz: string | null = null;

	/**
	 * Eine Bemerkung zum Teilstandort
	 */
	public bemerkung: string | null = null;

	/**
	 * Das Kürzel des Teilstandortes
	 */
	public kuerzel: string | null = null;

	/**
	 * Gibt an, ob der Eintrag in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen: boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Teilstandort';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.Teilstandort'].includes(name);
	}

	public static readonly class = new Class<Teilstandort>('de.svws_nrw.core.data.schule.Teilstandort');

	public static transpilerFromJSON(json: string): Teilstandort {
		const obj = JSON.parse(json) as Partial<Teilstandort>;
		const result = new Teilstandort();
		if (obj.adrMerkmal === undefined)
			throw new Error('invalid json format, missing attribute adrMerkmal');
		result.adrMerkmal = obj.adrMerkmal;
		result.plz = (obj.plz === undefined) ? null : obj.plz === null ? null : obj.plz;
		result.ort = (obj.ort === undefined) ? null : obj.ort === null ? null : obj.ort;
		result.strassenname = (obj.strassenname === undefined) ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausNr = (obj.hausNr === undefined) ? null : obj.hausNr === null ? null : obj.hausNr;
		result.hausNrZusatz = (obj.hausNrZusatz === undefined) ? null : obj.hausNrZusatz === null ? null : obj.hausNrZusatz;
		result.bemerkung = (obj.bemerkung === undefined) ? null : obj.bemerkung === null ? null : obj.bemerkung;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		if (obj.referenziertInAnderenTabellen === undefined)
			throw new Error('invalid json format, missing attribute referenziertInAnderenTabellen');
		result.referenziertInAnderenTabellen = obj.referenziertInAnderenTabellen;
		return result;
	}

	public static transpilerToJSON(obj: Teilstandort): string {
		let result = '{';
		result += '"adrMerkmal" : ' + JSON.stringify(obj.adrMerkmal) + ',';
		result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		result += '"hausNr" : ' + ((obj.hausNr === null) ? 'null' : JSON.stringify(obj.hausNr)) + ',';
		result += '"hausNrZusatz" : ' + ((obj.hausNrZusatz === null) ? 'null' : JSON.stringify(obj.hausNrZusatz)) + ',';
		result += '"bemerkung" : ' + ((obj.bemerkung === null) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<Teilstandort>): string {
		let result = '{';
		if (obj.adrMerkmal !== undefined) {
			result += '"adrMerkmal" : ' + JSON.stringify(obj.adrMerkmal) + ',';
		}
		if (obj.plz !== undefined) {
			result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		}
		if (obj.ort !== undefined) {
			result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		}
		if (obj.strassenname !== undefined) {
			result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		}
		if (obj.hausNr !== undefined) {
			result += '"hausNr" : ' + ((obj.hausNr === null) ? 'null' : JSON.stringify(obj.hausNr)) + ',';
		}
		if (obj.hausNrZusatz !== undefined) {
			result += '"hausNrZusatz" : ' + ((obj.hausNrZusatz === null) ? 'null' : JSON.stringify(obj.hausNrZusatz)) + ',';
		}
		if (obj.bemerkung !== undefined) {
			result += '"bemerkung" : ' + ((obj.bemerkung === null) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.referenziertInAnderenTabellen !== undefined) {
			result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Teilstandort(obj: unknown): Teilstandort {
	return obj as Teilstandort;
}
