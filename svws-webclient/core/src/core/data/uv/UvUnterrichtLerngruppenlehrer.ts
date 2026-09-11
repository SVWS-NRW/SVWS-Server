import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvUnterrichtLerngruppenlehrer extends JavaObject {

	/**
	 * Die ID des Planungsabschnitts, in dem die Zuordnung gilt.
	 */
	public idPlanungsabschnitt: number = -1;

	/**
	 * Die ID des Unterrichts, der dem Lerngruppenlehrer zugeordnet ist.
	 */
	public idUnterricht: number = -1;

	/**
	 * Die ID des Lerngruppenlehrers, dem der Unterricht zugeordnet ist.
	 */
	public idLerngruppenLehrer: number = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Gibt eine String-Repräsentation des UvUnterrichtLerngruppenlehrer-Objekts zurück.
	 *
	 * @return die String-Darstellung der Unterricht-Lerngruppenlehrer-Zuordnung
	 */
	public toString(): string | null {
		return "UvUnterrichtLerngruppenlehrer{idPlanungsabschnitt=" + this.idPlanungsabschnitt + ", idUnterricht=" + this.idUnterricht + ", idLerngruppenLehrer=" + this.idLerngruppenLehrer + "}";
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another: unknown | null): boolean {
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvUnterrichtLerngruppenlehrer')))) && (this.idPlanungsabschnitt === (another as unknown as UvUnterrichtLerngruppenlehrer).idPlanungsabschnitt) && (this.idUnterricht === (another as unknown as UvUnterrichtLerngruppenlehrer).idUnterricht) && (this.idLerngruppenLehrer === (another as unknown as UvUnterrichtLerngruppenlehrer).idLerngruppenLehrer);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der IDs.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		let result: number = JavaLong.hashCode((this.idPlanungsabschnitt));
		result = 31 * result + JavaLong.hashCode((this.idUnterricht));
		result = 31 * result + JavaLong.hashCode((this.idLerngruppenLehrer));
		return result;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvUnterrichtLerngruppenlehrer';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvUnterrichtLerngruppenlehrer'].includes(name);
	}

	public static readonly class = new Class<UvUnterrichtLerngruppenlehrer>('de.svws_nrw.core.data.uv.UvUnterrichtLerngruppenlehrer');

	public static transpilerFromJSON(json: string): UvUnterrichtLerngruppenlehrer {
		const obj = JSON.parse(json) as Partial<UvUnterrichtLerngruppenlehrer>;
		const result = new UvUnterrichtLerngruppenlehrer();
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.idUnterricht === undefined)
			throw new Error('invalid json format, missing attribute idUnterricht');
		result.idUnterricht = obj.idUnterricht;
		if (obj.idLerngruppenLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLerngruppenLehrer');
		result.idLerngruppenLehrer = obj.idLerngruppenLehrer;
		return result;
	}

	public static transpilerToJSON(obj: UvUnterrichtLerngruppenlehrer): string {
		let result = '{';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result += '"idUnterricht" : ' + obj.idUnterricht.toString() + ',';
		result += '"idLerngruppenLehrer" : ' + obj.idLerngruppenLehrer.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvUnterrichtLerngruppenlehrer>): string {
		let result = '{';
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		if (obj.idUnterricht !== undefined) {
			result += '"idUnterricht" : ' + obj.idUnterricht.toString() + ',';
		}
		if (obj.idLerngruppenLehrer !== undefined) {
			result += '"idLerngruppenLehrer" : ' + obj.idLerngruppenLehrer.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvUnterrichtLerngruppenlehrer(obj: unknown): UvUnterrichtLerngruppenlehrer {
	return obj as UvUnterrichtLerngruppenlehrer;
}
