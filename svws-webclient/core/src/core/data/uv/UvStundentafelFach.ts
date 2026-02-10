import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvStundentafelFach extends JavaObject {

	/**
	 * Die ID des Stundentafel_Fachs.
	 */
	public id: number = -1;

	/**
	 * Die ID der Stundentafel, zu der das Fach gehört.
	 */
	public idStundentafel: number = -1;

	/**
	 * Der Abschnitt des Schuljahres (z. B. 1 oder 2).
	 */
	public abschnitt: number = 1;

	/**
	 * Die ID des Faches (Fremdschlüssel auf die Tabelle UV_Faecher).
	 */
	public idFach: number = -1;

	/**
	 * Die Anzahl der Wochenstunden für das Fach.
	 */
	public wochenstunden: number = 0.0;

	/**
	 * Die Anzahl der Ergänzungsstunden für das Fach (in den Wochenstunden enthalten).
	 */
	public davonErgaenzungsstunden: number = 0.0;


	/**
	 * Default-Konstruktor
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
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvStundentafelFach')))) && (this.idStundentafel === (another as unknown as UvStundentafelFach).idStundentafel) && (this.idFach === (another as unknown as UvStundentafelFach).idFach) && (this.abschnitt === (another as unknown as UvStundentafelFach).abschnitt);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der ID-Kombination.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		let result: number = JavaLong.hashCode((this.idStundentafel));
		result = 31 * result + JavaLong.hashCode((this.idFach));
		return result;
	}

	/**
	 * Gibt eine String-Repräsentation des Objekts zurück.
	 *
	 * @return die String-Darstellung des Fach-Eintrags der Stundentafel
	 */
	public toString(): string | null {
		return this.id + "-" + this.idStundentafel + "-" + this.abschnitt + "-" + this.idFach;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvStundentafelFach';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvStundentafelFach'].includes(name);
	}

	public static readonly class = new Class<UvStundentafelFach>('de.svws_nrw.core.data.uv.UvStundentafelFach');

	public static transpilerFromJSON(json: string): UvStundentafelFach {
		const obj = JSON.parse(json) as Partial<UvStundentafelFach>;
		const result = new UvStundentafelFach();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idStundentafel === undefined)
			throw new Error('invalid json format, missing attribute idStundentafel');
		result.idStundentafel = obj.idStundentafel;
		if (obj.abschnitt === undefined)
			throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		if (obj.idFach === undefined)
			throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (obj.davonErgaenzungsstunden === undefined)
			throw new Error('invalid json format, missing attribute davonErgaenzungsstunden');
		result.davonErgaenzungsstunden = obj.davonErgaenzungsstunden;
		return result;
	}

	public static transpilerToJSON(obj: UvStundentafelFach): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idStundentafel" : ' + obj.idStundentafel.toString() + ',';
		result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		result += '"idFach" : ' + obj.idFach.toString() + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result += '"davonErgaenzungsstunden" : ' + obj.davonErgaenzungsstunden.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvStundentafelFach>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idStundentafel !== undefined) {
			result += '"idStundentafel" : ' + obj.idStundentafel.toString() + ',';
		}
		if (obj.abschnitt !== undefined) {
			result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach.toString() + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		if (obj.davonErgaenzungsstunden !== undefined) {
			result += '"davonErgaenzungsstunden" : ' + obj.davonErgaenzungsstunden.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvStundentafelFach(obj: unknown): UvStundentafelFach {
	return obj as UvStundentafelFach;
}
