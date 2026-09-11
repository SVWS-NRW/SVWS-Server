import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvKursCreateRequest extends JavaObject {

	/**
	 * Die negative temporäre ID beim Sammelimport.
	 */
	public id: number = 0;

	/**
	 * Die ID des Planungsabschnitts, in dem der Kurs gilt.
	 */
	public idPlanungsabschnitt: number = 0;

	/**
	 * Die ID des Schuljahresabschnitts.
	 */
	public idSchuljahresabschnitt: number = 0;

	/**
	 * Die ID des Faches.
	 */
	public idFach: number = 0;

	/**
	 * Das Kürzel der Kursart.
	 */
	public kursart: string = "";

	/**
	 * Die Kursnummer.
	 */
	public kursnummer: number = 0;

	/**
	 * Die ID der Schülergruppe.
	 */
	public idSchuelergruppe: number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvKursCreateRequest';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvKursCreateRequest'].includes(name);
	}

	public static readonly class = new Class<UvKursCreateRequest>('de.svws_nrw.core.data.uv.UvKursCreateRequest');

	public static transpilerFromJSON(json: string): UvKursCreateRequest {
		const obj = JSON.parse(json) as Partial<UvKursCreateRequest>;
		const result = new UvKursCreateRequest();
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

	public static transpilerToJSON(obj: UvKursCreateRequest): string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		result += '"idFach" : ' + obj.idFach + ',';
		result += '"kursart" : ' + JSON.stringify(obj.kursart) + ',';
		result += '"kursnummer" : ' + obj.kursnummer + ',';
		result += '"idSchuelergruppe" : ' + obj.idSchuelergruppe + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvKursCreateRequest>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id + ',';
		}
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach + ',';
		}
		if (obj.kursart !== undefined) {
			result += '"kursart" : ' + JSON.stringify(obj.kursart) + ',';
		}
		if (obj.kursnummer !== undefined) {
			result += '"kursnummer" : ' + obj.kursnummer + ',';
		}
		if (obj.idSchuelergruppe !== undefined) {
			result += '"idSchuelergruppe" : ' + obj.idSchuelergruppe + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvKursCreateRequest(obj: unknown): UvKursCreateRequest {
	return obj as UvKursCreateRequest;
}
