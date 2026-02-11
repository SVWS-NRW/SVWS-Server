import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Kindergarten extends JavaObject {

	/**
	 * Die ID des Kindergartens.
	 */
	public id: number = 0;

	/**
	 * Die Bezeichnung des Kindergartens.
	 */
	public bezeichnung: string = "";

	/**
	 * Bemerkung zum Kindergartens.
	 */
	public bemerkung: string | null = null;

	/**
	 * Die Telefonnummer des Kindergartens.
	 */
	public tel: string | null = null;

	/**
	 * Die E-Mail des Kindergartens.
	 */
	public email: string | null = null;

	/**
	 * Der Strassenname des Kindergartens.
	 */
	public strassenname: string | null = null;

	/**
	 * Die Hausnummer des Kindergartens.
	 */
	public hausNr: string | null = null;

	/**
	 * der Hausnummerzusatz des Kindergartens.
	 */
	public hausNrZusatz: string | null = null;

	/**
	 * Die PLZ des Kindergartens.
	 */
	public plz: string | null = null;

	/**
	 * Der Ort des Kindergartens.
	 */
	public ort: string | null = null;

	/**
	 * Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an.
	 */
	public sortierung: number = 0;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar: boolean = false;

	/**
	 * Gibt an, ob der Kindergarten in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen: boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Kindergarten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.Kindergarten'].includes(name);
	}

	public static readonly class = new Class<Kindergarten>('de.svws_nrw.core.data.schule.Kindergarten');

	public static transpilerFromJSON(json: string): Kindergarten {
		const obj = JSON.parse(json) as Partial<Kindergarten>;
		const result = new Kindergarten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		result.bemerkung = (obj.bemerkung === undefined) ? null : obj.bemerkung === null ? null : obj.bemerkung;
		result.tel = (obj.tel === undefined) ? null : obj.tel === null ? null : obj.tel;
		result.email = (obj.email === undefined) ? null : obj.email === null ? null : obj.email;
		result.strassenname = (obj.strassenname === undefined) ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausNr = (obj.hausNr === undefined) ? null : obj.hausNr === null ? null : obj.hausNr;
		result.hausNrZusatz = (obj.hausNrZusatz === undefined) ? null : obj.hausNrZusatz === null ? null : obj.hausNrZusatz;
		result.plz = (obj.plz === undefined) ? null : obj.plz === null ? null : obj.plz;
		result.ort = (obj.ort === undefined) ? null : obj.ort === null ? null : obj.ort;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (obj.referenziertInAnderenTabellen === undefined)
			throw new Error('invalid json format, missing attribute referenziertInAnderenTabellen');
		result.referenziertInAnderenTabellen = obj.referenziertInAnderenTabellen;
		return result;
	}

	public static transpilerToJSON(obj: Kindergarten): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"bemerkung" : ' + ((obj.bemerkung === null) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result += '"tel" : ' + ((obj.tel === null) ? 'null' : JSON.stringify(obj.tel)) + ',';
		result += '"email" : ' + ((obj.email === null) ? 'null' : JSON.stringify(obj.email)) + ',';
		result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		result += '"hausNr" : ' + ((obj.hausNr === null) ? 'null' : JSON.stringify(obj.hausNr)) + ',';
		result += '"hausNrZusatz" : ' + ((obj.hausNrZusatz === null) ? 'null' : JSON.stringify(obj.hausNrZusatz)) + ',';
		result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<Kindergarten>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.bemerkung !== undefined) {
			result += '"bemerkung" : ' + ((obj.bemerkung === null) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		}
		if (obj.tel !== undefined) {
			result += '"tel" : ' + ((obj.tel === null) ? 'null' : JSON.stringify(obj.tel)) + ',';
		}
		if (obj.email !== undefined) {
			result += '"email" : ' + ((obj.email === null) ? 'null' : JSON.stringify(obj.email)) + ',';
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
		if (obj.plz !== undefined) {
			result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		}
		if (obj.ort !== undefined) {
			result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		if (obj.referenziertInAnderenTabellen !== undefined) {
			result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Kindergarten(obj: unknown): Kindergarten {
	return obj as Kindergarten;
}
