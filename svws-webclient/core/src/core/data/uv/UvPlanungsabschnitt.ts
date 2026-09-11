import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvPlanungsabschnitt extends JavaObject {

	/**
	 * Die ID des Planungsabschnitts.
	 */
	public id: number = -1;

	/**
	 * Das Schuljahr, zu dem der Planungsabschnitt gehört.
	 */
	public schuljahr: number = -1;

	/**
	 * Gibt an, ob der Planungsabschnitt aktiv ist. An einem Stichtag darf nur ein Planungsabschnitt aktiv sein.
	 */
	public aktiv: boolean = false;

	/**
	 * Das Datum des Gültigkeitsbeginns des Planungsabschnitts.
	 */
	public gueltigVon: string = "";

	/**
	 * Das Datum des Gültigkeitsendes des Planungsabschnitts.
	 */
	public gueltigBis: string | null = "";

	/**
	 * Die optionale Beschreibung oder Kommentar zum Planungsabschnitt.
	 */
	public beschreibung: string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Gibt eine String-Repräsentation des UvPlanungsabschnitt-Objekts zurück.
	 */
	public toString(): string | null {
		return "UvPlanungsabschnitt{id=" + this.id + ", schuljahr=" + this.schuljahr + ", aktiv=" + this.aktiv + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ", beschreibung=" + this.beschreibung + "}";
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another: unknown | null): boolean {
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvPlanungsabschnitt')))) && (this.id === (another as unknown as UvPlanungsabschnitt).id);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt auf Basis der id.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		return JavaLong.hashCode((this.id));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvPlanungsabschnitt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvPlanungsabschnitt'].includes(name);
	}

	public static readonly class = new Class<UvPlanungsabschnitt>('de.svws_nrw.core.data.uv.UvPlanungsabschnitt');

	public static transpilerFromJSON(json: string): UvPlanungsabschnitt {
		const obj = JSON.parse(json) as Partial<UvPlanungsabschnitt>;
		const result = new UvPlanungsabschnitt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schuljahr === undefined)
			throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (obj.aktiv === undefined)
			throw new Error('invalid json format, missing attribute aktiv');
		result.aktiv = obj.aktiv;
		if (obj.gueltigVon === undefined)
			throw new Error('invalid json format, missing attribute gueltigVon');
		result.gueltigVon = obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		result.beschreibung = (obj.beschreibung === undefined) ? null : obj.beschreibung === null ? null : obj.beschreibung;
		return result;
	}

	public static transpilerToJSON(obj: UvPlanungsabschnitt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		result += '"aktiv" : ' + obj.aktiv.toString() + ',';
		result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvPlanungsabschnitt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schuljahr !== undefined) {
			result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		}
		if (obj.aktiv !== undefined) {
			result += '"aktiv" : ' + obj.aktiv.toString() + ',';
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

export function cast_de_svws_nrw_core_data_uv_UvPlanungsabschnitt(obj: unknown): UvPlanungsabschnitt {
	return obj as UvPlanungsabschnitt;
}
