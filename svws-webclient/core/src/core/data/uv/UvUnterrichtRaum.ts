import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvUnterrichtRaum extends JavaObject {

	/**
	 * Die ID des Planungsabschnitts, in dem die Zuordnung gilt.
	 */
	public idPlanungsabschnitt: number = -1;

	/**
	 * Die ID des Unterrichts, der dem Raum zugeordnet ist.
	 */
	public idUnterricht: number = -1;

	/**
	 * Die ID des Raums, dem der Unterricht zugeordnet ist.
	 */
	public idRaum: number = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Gibt eine String-Repräsentation des UvUnterrichtRaum-Objekts zurück.
	 *
	 * @return die String-Darstellung der Unterricht-Raum-Zuordnung
	 */
	public toString(): string | null {
		return "UvUnterrichtRaum{idPlanungsabschnitt=" + this.idPlanungsabschnitt + ", idUnterricht=" + this.idUnterricht + ", idRaum=" + this.idRaum + "}";
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another: unknown | null): boolean {
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvUnterrichtRaum')))) && (this.idPlanungsabschnitt === (another as unknown as UvUnterrichtRaum).idPlanungsabschnitt) && (this.idUnterricht === (another as unknown as UvUnterrichtRaum).idUnterricht) && (this.idRaum === (another as unknown as UvUnterrichtRaum).idRaum);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der IDs.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		let result: number = JavaLong.hashCode((this.idPlanungsabschnitt));
		result = 31 * result + JavaLong.hashCode((this.idUnterricht));
		result = 31 * result + JavaLong.hashCode((this.idRaum));
		return result;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvUnterrichtRaum';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvUnterrichtRaum'].includes(name);
	}

	public static readonly class = new Class<UvUnterrichtRaum>('de.svws_nrw.core.data.uv.UvUnterrichtRaum');

	public static transpilerFromJSON(json: string): UvUnterrichtRaum {
		const obj = JSON.parse(json) as Partial<UvUnterrichtRaum>;
		const result = new UvUnterrichtRaum();
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.idUnterricht === undefined)
			throw new Error('invalid json format, missing attribute idUnterricht');
		result.idUnterricht = obj.idUnterricht;
		if (obj.idRaum === undefined)
			throw new Error('invalid json format, missing attribute idRaum');
		result.idRaum = obj.idRaum;
		return result;
	}

	public static transpilerToJSON(obj: UvUnterrichtRaum): string {
		let result = '{';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result += '"idUnterricht" : ' + obj.idUnterricht.toString() + ',';
		result += '"idRaum" : ' + obj.idRaum.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvUnterrichtRaum>): string {
		let result = '{';
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		if (obj.idUnterricht !== undefined) {
			result += '"idUnterricht" : ' + obj.idUnterricht.toString() + ',';
		}
		if (obj.idRaum !== undefined) {
			result += '"idRaum" : ' + obj.idRaum.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvUnterrichtRaum(obj: unknown): UvUnterrichtRaum {
	return obj as UvUnterrichtRaum;
}
