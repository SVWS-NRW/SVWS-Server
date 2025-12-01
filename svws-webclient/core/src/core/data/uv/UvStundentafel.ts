import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvStundentafel extends JavaObject {

	/**
	 * Die ID der Stundentafel.
	 */
	public id: number = -1;

	/**
	 * Die ID des zugehörigen Jahrgangs.
	 */
	public jahrgangId: number = -1;

	/**
	 * Die optionale Bezeichnung der Stundentafel.
	 */
	public bezeichnung: string = "";

	/**
	 * Das Datum, ab dem die Stundentafel gültig ist.
	 */
	public gueltigVon: string = "";

	/**
	 * Das Datum, bis wann die Stundentafel gültig ist.
	 */
	public gueltigBis: string | null = "";

	/**
	 * Die optionale Beschreibung oder Kommentar zur Stundentafel.
	 */
	public beschreibung: string | null = null;


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
	public equals(another: unknown | null): boolean {
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvStundentafel')))) && (this.id === (another as unknown as UvStundentafel).id);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der idVorgabe.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		return JavaLong.hashCode((this.id));
	}

	/**
	 * Returns a string representation of the object.
	 */
	public toString(): string | null {
		return this.id + "-" + this.jahrgangId + "-" + this.gueltigVon;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvStundentafel';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvStundentafel'].includes(name);
	}

	public static readonly class = new Class<UvStundentafel>('de.svws_nrw.core.data.uv.UvStundentafel');

	public static transpilerFromJSON(json: string): UvStundentafel {
		const obj = JSON.parse(json) as Partial<UvStundentafel>;
		const result = new UvStundentafel();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.jahrgangId === undefined)
			throw new Error('invalid json format, missing attribute jahrgangId');
		result.jahrgangId = obj.jahrgangId;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.gueltigVon === undefined)
			throw new Error('invalid json format, missing attribute gueltigVon');
		result.gueltigVon = obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		result.beschreibung = (obj.beschreibung === undefined) ? null : obj.beschreibung === null ? null : obj.beschreibung;
		return result;
	}

	public static transpilerToJSON(obj: UvStundentafel): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"jahrgangId" : ' + obj.jahrgangId.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvStundentafel>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.jahrgangId !== undefined) {
			result += '"jahrgangId" : ' + obj.jahrgangId.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvStundentafel(obj: unknown): UvStundentafel {
	return obj as UvStundentafel;
}
