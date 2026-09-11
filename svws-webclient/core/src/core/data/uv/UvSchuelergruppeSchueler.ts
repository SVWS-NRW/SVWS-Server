import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvSchuelergruppeSchueler extends JavaObject {

	/**
	 * Die ID der Schülergruppe.
	 */
	public idSchuelergruppe: number = -1;

	/**
	 * Die ID des Schülers.
	 */
	public idSchueler: number = -1;

	/**
	 * Die ID des Planungsabschnitts.
	 */
	public idPlanungsabschnitt: number = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Gibt eine String-Repräsentation des UvSchuelergruppeSchueler-Objekts zurück.
	 *
	 * @return die String-Darstellung der Zuordnung
	 */
	public toString(): string | null {
		return "UvSchuelergruppeSchueler{idSchuelergruppe=" + this.idSchuelergruppe + ", idSchueler=" + this.idSchueler + ", idPlanungsabschnitt=" + this.idPlanungsabschnitt + "}";
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another: unknown | null): boolean {
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvSchuelergruppeSchueler')))) && (this.idSchuelergruppe === (another as unknown as UvSchuelergruppeSchueler).idSchuelergruppe) && (this.idSchueler === (another as unknown as UvSchuelergruppeSchueler).idSchueler);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		const prime: number = 31;
		let result: number = 1;
		result = prime * result + JavaLong.hashCode((this.idSchuelergruppe));
		result = prime * result + JavaLong.hashCode((this.idSchueler));
		return result;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvSchuelergruppeSchueler';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvSchuelergruppeSchueler'].includes(name);
	}

	public static readonly class = new Class<UvSchuelergruppeSchueler>('de.svws_nrw.core.data.uv.UvSchuelergruppeSchueler');

	public static transpilerFromJSON(json: string): UvSchuelergruppeSchueler {
		const obj = JSON.parse(json) as Partial<UvSchuelergruppeSchueler>;
		const result = new UvSchuelergruppeSchueler();
		if (obj.idSchuelergruppe === undefined)
			throw new Error('invalid json format, missing attribute idSchuelergruppe');
		result.idSchuelergruppe = obj.idSchuelergruppe;
		if (obj.idSchueler === undefined)
			throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		return result;
	}

	public static transpilerToJSON(obj: UvSchuelergruppeSchueler): string {
		let result = '{';
		result += '"idSchuelergruppe" : ' + obj.idSchuelergruppe.toString() + ',';
		result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvSchuelergruppeSchueler>): string {
		let result = '{';
		if (obj.idSchuelergruppe !== undefined) {
			result += '"idSchuelergruppe" : ' + obj.idSchuelergruppe.toString() + ',';
		}
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		}
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvSchuelergruppeSchueler(obj: unknown): UvSchuelergruppeSchueler {
	return obj as UvSchuelergruppeSchueler;
}
