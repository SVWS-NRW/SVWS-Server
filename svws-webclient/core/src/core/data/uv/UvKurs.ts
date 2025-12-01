import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvKurs extends JavaObject {

	/**
	 *  Die ID des Kurses.
	 */
	public id: number = 0;

	/**
	 *  Die ID des Planungsabschnitts.
	 */
	public idPlanungsabschnitt: number = 0;

	/**
	 *  Die ID des Schuljahresabschnitts.
	 */
	public idSchuljahresabschnitt: number = 0;

	/**
	 *  Die ID des Faches.
	 */
	public idFach: number = 0;

	/**
	 *  Die Kursart (siehe {@link GostKursart}).
	 */
	public kursart: string = "";

	/**
	 *  Die Kursnummer.
	 */
	public kursnummer: number = 0;

	/**
	 *  Die ID der zugehörigen Schülergruppe.
	 */
	public idSchuelergruppe: number = 0;


	/**
	 * Default-Konstruktor.
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
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvKurs')))) && (this.id === (another as unknown as UvKurs).id);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt auf Basis der ID.
	 *
	 * @return den Hashcode
	 */
	public hashCode(): number {
		return JavaLong.hashCode((this.id));
	}

	/**
	 * Gibt eine String-Repräsentation des Objekts zurück.
	 *
	 * @return eine lesbare Beschreibung des Objekts
	 */
	public toString(): string | null {
		return this.id + "-" + this.idFach + "-" + this.kursart + "-" + this.kursnummer;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvKurs';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvKurs'].includes(name);
	}

	public static readonly class = new Class<UvKurs>('de.svws_nrw.core.data.uv.UvKurs');

	public static transpilerFromJSON(json: string): UvKurs {
		const obj = JSON.parse(json) as Partial<UvKurs>;
		const result = new UvKurs();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (obj.idFach === undefined)
			throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (obj.kursart === undefined)
			throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (obj.kursnummer === undefined)
			throw new Error('invalid json format, missing attribute kursnummer');
		result.kursnummer = obj.kursnummer;
		if (obj.idSchuelergruppe === undefined)
			throw new Error('invalid json format, missing attribute idSchuelergruppe');
		result.idSchuelergruppe = obj.idSchuelergruppe;
		return result;
	}

	public static transpilerToJSON(obj: UvKurs): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"idFach" : ' + obj.idFach.toString() + ',';
		result += '"kursart" : ' + JSON.stringify(obj.kursart) + ',';
		result += '"kursnummer" : ' + obj.kursnummer.toString() + ',';
		result += '"idSchuelergruppe" : ' + obj.idSchuelergruppe.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvKurs>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach.toString() + ',';
		}
		if (obj.kursart !== undefined) {
			result += '"kursart" : ' + JSON.stringify(obj.kursart) + ',';
		}
		if (obj.kursnummer !== undefined) {
			result += '"kursnummer" : ' + obj.kursnummer.toString() + ',';
		}
		if (obj.idSchuelergruppe !== undefined) {
			result += '"idSchuelergruppe" : ' + obj.idSchuelergruppe.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvKurs(obj: unknown): UvKurs {
	return obj as UvKurs;
}
