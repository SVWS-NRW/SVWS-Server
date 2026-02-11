import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvZeitrasterEintrag extends JavaObject {

	/**
	 * Die ID des Zeitraster-Eintrags.
	 */
	public id: number = -1;

	/**
	 * Die ID des übergeordneten Zeitrasters.
	 */
	public idZeitraster: number = -1;

	/**
	 * Der Wochentag (z. B. 1 = Montag).
	 */
	public tag: number = -1;

	/**
	 * Die Stunde (z. B. 1 = erste Stunde).
	 */
	public stunde: number = -1;

	/**
	 * Beginn der Stunde (als Minuten seit Mitternacht).
	 */
	public beginn: number = -1;

	/**
	 * Ende der Stunde (als Minuten seit Mitternacht).
	 */
	public ende: number = -1;


	/**
	 *Leerer Standardkonstruktor.
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
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvZeitrasterEintrag')))) && (this.id === (another as unknown as UvZeitrasterEintrag).id);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt auf Basis der ID.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		return JavaLong.hashCode((this.id));
	}

	/**
	 * Gibt eine String-Repräsentation des Objekts zurück.
	 *
	 * @return die String-Darstellung
	 */
	public toString(): string | null {
		return this.id + "-" + this.tag + "-" + this.stunde + "-" + this.beginn + "-" + this.ende;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvZeitrasterEintrag';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvZeitrasterEintrag'].includes(name);
	}

	public static readonly class = new Class<UvZeitrasterEintrag>('de.svws_nrw.core.data.uv.UvZeitrasterEintrag');

	public static transpilerFromJSON(json: string): UvZeitrasterEintrag {
		const obj = JSON.parse(json) as Partial<UvZeitrasterEintrag>;
		const result = new UvZeitrasterEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idZeitraster === undefined)
			throw new Error('invalid json format, missing attribute idZeitraster');
		result.idZeitraster = obj.idZeitraster;
		if (obj.tag === undefined)
			throw new Error('invalid json format, missing attribute tag');
		result.tag = obj.tag;
		if (obj.stunde === undefined)
			throw new Error('invalid json format, missing attribute stunde');
		result.stunde = obj.stunde;
		if (obj.beginn === undefined)
			throw new Error('invalid json format, missing attribute beginn');
		result.beginn = obj.beginn;
		if (obj.ende === undefined)
			throw new Error('invalid json format, missing attribute ende');
		result.ende = obj.ende;
		return result;
	}

	public static transpilerToJSON(obj: UvZeitrasterEintrag): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idZeitraster" : ' + obj.idZeitraster.toString() + ',';
		result += '"tag" : ' + obj.tag.toString() + ',';
		result += '"stunde" : ' + obj.stunde.toString() + ',';
		result += '"beginn" : ' + obj.beginn.toString() + ',';
		result += '"ende" : ' + obj.ende.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvZeitrasterEintrag>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idZeitraster !== undefined) {
			result += '"idZeitraster" : ' + obj.idZeitraster.toString() + ',';
		}
		if (obj.tag !== undefined) {
			result += '"tag" : ' + obj.tag.toString() + ',';
		}
		if (obj.stunde !== undefined) {
			result += '"stunde" : ' + obj.stunde.toString() + ',';
		}
		if (obj.beginn !== undefined) {
			result += '"beginn" : ' + obj.beginn.toString() + ',';
		}
		if (obj.ende !== undefined) {
			result += '"ende" : ' + obj.ende.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvZeitrasterEintrag(obj: unknown): UvZeitrasterEintrag {
	return obj as UvZeitrasterEintrag;
}
