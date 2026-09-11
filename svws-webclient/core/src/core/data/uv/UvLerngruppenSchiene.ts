import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvLerngruppenSchiene extends JavaObject {

	/**
	 * Die ID des Planungsabschnitts, in dem die Zuordnung gilt.
	 */
	public idPlanungsabschnitt: number = -1;

	/**
	 * Die ID der Lerngruppe, die der Schiene zugeordnet ist.
	 */
	public idLerngruppe: number = -1;

	/**
	 * Die ID der Schiene, der die Lerngruppe zugeordnet ist.
	 */
	public idSchiene: number = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Gibt eine String-Repräsentation des UvLerngruppenSchiene-Objekts zurück.
	 *
	 * @return die String-Darstellung der Lerngruppe-Schiene-Zuordnung
	 */
	public toString(): string | null {
		return "UvLerngruppenSchiene{idPlanungsabschnitt=" + this.idPlanungsabschnitt + ", idLerngruppe=" + this.idLerngruppe + ", idSchiene=" + this.idSchiene + "}";
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another: unknown | null): boolean {
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvLerngruppenSchiene')))) && (this.idPlanungsabschnitt === (another as unknown as UvLerngruppenSchiene).idPlanungsabschnitt) && (this.idLerngruppe === (another as unknown as UvLerngruppenSchiene).idLerngruppe) && (this.idSchiene === (another as unknown as UvLerngruppenSchiene).idSchiene);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der IDs.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		let result: number = JavaLong.hashCode((this.idPlanungsabschnitt));
		result = 31 * result + JavaLong.hashCode((this.idLerngruppe));
		result = 31 * result + JavaLong.hashCode((this.idSchiene));
		return result;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvLerngruppenSchiene';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvLerngruppenSchiene'].includes(name);
	}

	public static readonly class = new Class<UvLerngruppenSchiene>('de.svws_nrw.core.data.uv.UvLerngruppenSchiene');

	public static transpilerFromJSON(json: string): UvLerngruppenSchiene {
		const obj = JSON.parse(json) as Partial<UvLerngruppenSchiene>;
		const result = new UvLerngruppenSchiene();
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.idLerngruppe === undefined)
			throw new Error('invalid json format, missing attribute idLerngruppe');
		result.idLerngruppe = obj.idLerngruppe;
		if (obj.idSchiene === undefined)
			throw new Error('invalid json format, missing attribute idSchiene');
		result.idSchiene = obj.idSchiene;
		return result;
	}

	public static transpilerToJSON(obj: UvLerngruppenSchiene): string {
		let result = '{';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result += '"idLerngruppe" : ' + obj.idLerngruppe.toString() + ',';
		result += '"idSchiene" : ' + obj.idSchiene.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvLerngruppenSchiene>): string {
		let result = '{';
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		if (obj.idLerngruppe !== undefined) {
			result += '"idLerngruppe" : ' + obj.idLerngruppe.toString() + ',';
		}
		if (obj.idSchiene !== undefined) {
			result += '"idSchiene" : ' + obj.idSchiene.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvLerngruppenSchiene(obj: unknown): UvLerngruppenSchiene {
	return obj as UvLerngruppenSchiene;
}
