import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvPlanungsabschnittLehrer extends JavaObject {

	/**
	 * Die ID des Planungsabschnitts.
	 */
	public idPlanungsabschnitt: number = -1;

	/**
	 * Die ID des UV-Lehrers.
	 */
	public idLehrer: number = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Gibt eine String-Repräsentation des UvPlanungsabschnittLehrer-Objekts zurück.
	 *
	 * @return die String-Darstellung der Zuordnung
	 */
	public toString(): string | null {
		return "UvPlanungsabschnittLehrer{idPlanungsabschnitt=" + this.idPlanungsabschnitt + ", idLehrer=" + this.idLehrer + "}";
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another: unknown | null): boolean {
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrer')))) && (this.idPlanungsabschnitt === (another as unknown as UvPlanungsabschnittLehrer).idPlanungsabschnitt) && (this.idLehrer === (another as unknown as UvPlanungsabschnittLehrer).idLehrer);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		const prime: number = 31;
		let result: number = 1;
		result = prime * result + JavaLong.hashCode((this.idPlanungsabschnitt));
		result = prime * result + JavaLong.hashCode((this.idLehrer));
		return result;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrer';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrer'].includes(name);
	}

	public static readonly class = new Class<UvPlanungsabschnittLehrer>('de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrer');

	public static transpilerFromJSON(json: string): UvPlanungsabschnittLehrer {
		const obj = JSON.parse(json) as Partial<UvPlanungsabschnittLehrer>;
		const result = new UvPlanungsabschnittLehrer();
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		return result;
	}

	public static transpilerToJSON(obj: UvPlanungsabschnittLehrer): string {
		let result = '{';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvPlanungsabschnittLehrer>): string {
		let result = '{';
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvPlanungsabschnittLehrer(obj: unknown): UvPlanungsabschnittLehrer {
	return obj as UvPlanungsabschnittLehrer;
}
