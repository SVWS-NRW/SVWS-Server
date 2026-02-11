import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvLehrer extends JavaObject {

	/**
	 * Die eindeutige ID des Lehrers im Planungsabschnitt (planungsspezifisch).
	 */
	public id: number = -1;

	/**
	 * Die ID des Lehrers als Fremdschlüssel auf die Tabelle K_Lehrer.
	 */
	public idKLehrer: number | null = null;

	/**
	 * Das Lehrer-Kürzel für eine eindeutige Identifikation.
	 */
	public kuerzel: string | null = null;

	/**
	 * Der Nachname des Lehrers.
	 */
	public nachname: string | null = null;

	/**
	 * Der Vorname (bzw. Rufname) des Lehrers.
	 */
	public vorname: string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another: unknown | null): boolean {
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvLehrer')))) && (this.id === (another as unknown as UvLehrer).id);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der ID.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		return JavaLong.hashCode((this.id));
	}

	/**
	 * Gibt eine String-Repräsentation des Objekts zurück.
	 *
	 * @return die String-Darstellung des Lehrers
	 */
	public toString(): string | null {
		return this.id + "-" + (this.kuerzel !== null ? this.kuerzel : "") + " (" + (this.nachname !== null ? this.nachname : "") + ", " + (this.vorname !== null ? this.vorname : "") + ")";
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvLehrer';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvLehrer'].includes(name);
	}

	public static readonly class = new Class<UvLehrer>('de.svws_nrw.core.data.uv.UvLehrer');

	public static transpilerFromJSON(json: string): UvLehrer {
		const obj = JSON.parse(json) as Partial<UvLehrer>;
		const result = new UvLehrer();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.idKLehrer = (obj.idKLehrer === undefined) ? null : obj.idKLehrer === null ? null : obj.idKLehrer;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.nachname = (obj.nachname === undefined) ? null : obj.nachname === null ? null : obj.nachname;
		result.vorname = (obj.vorname === undefined) ? null : obj.vorname === null ? null : obj.vorname;
		return result;
	}

	public static transpilerToJSON(obj: UvLehrer): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idKLehrer" : ' + ((obj.idKLehrer === null) ? 'null' : obj.idKLehrer.toString()) + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"nachname" : ' + ((obj.nachname === null) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvLehrer>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idKLehrer !== undefined) {
			result += '"idKLehrer" : ' + ((obj.idKLehrer === null) ? 'null' : obj.idKLehrer.toString()) + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + ((obj.nachname === null) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvLehrer(obj: unknown): UvLehrer {
	return obj as UvLehrer;
}
