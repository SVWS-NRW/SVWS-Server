import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class UVv1KursExport extends JavaObject {

	/**
	 * Die UV-ID des Kurses.
	 */
	public uvId: number = -1;

	/**
	 * Die ID des Schuljahresabschnitts.
	 */
	public idSchuljahresabschnitt: number = -1;

	/**
	 * Die UV-ID des Faches.
	 */
	public fachUvId: number = -1;

	/**
	 * Die Kursart.
	 */
	public kursart: string = "";

	/**
	 * Die Kursnummer.
	 */
	public kursnummer: number = 0;

	/**
	 * Die UV-ID der zugehörigen Schülergruppe.
	 */
	public schuelergruppeUvId: number = -1;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1KursExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1KursExport'].includes(name);
	}

	public static readonly class = new Class<UVv1KursExport>('de.svws_nrw.core.data.uv.export.UVv1KursExport');

	public static transpilerFromJSON(json: string): UVv1KursExport {
		const obj = JSON.parse(json) as Partial<UVv1KursExport>;
		const result = new UVv1KursExport();
		if (obj.uvId === undefined)
			throw new Error('invalid json format, missing attribute uvId');
		result.uvId = obj.uvId;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (obj.fachUvId === undefined)
			throw new Error('invalid json format, missing attribute fachUvId');
		result.fachUvId = obj.fachUvId;
		if (obj.kursart === undefined)
			throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (obj.kursnummer === undefined)
			throw new Error('invalid json format, missing attribute kursnummer');
		result.kursnummer = obj.kursnummer;
		if (obj.schuelergruppeUvId === undefined)
			throw new Error('invalid json format, missing attribute schuelergruppeUvId');
		result.schuelergruppeUvId = obj.schuelergruppeUvId;
		return result;
	}

	public static transpilerToJSON(obj: UVv1KursExport): string {
		let result = '{';
		result += '"uvId" : ' + obj.uvId.toString() + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"fachUvId" : ' + obj.fachUvId.toString() + ',';
		result += '"kursart" : ' + JSON.stringify(obj.kursart) + ',';
		result += '"kursnummer" : ' + obj.kursnummer.toString() + ',';
		result += '"schuelergruppeUvId" : ' + obj.schuelergruppeUvId.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1KursExport>): string {
		let result = '{';
		if (obj.uvId !== undefined) {
			result += '"uvId" : ' + obj.uvId.toString() + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.fachUvId !== undefined) {
			result += '"fachUvId" : ' + obj.fachUvId.toString() + ',';
		}
		if (obj.kursart !== undefined) {
			result += '"kursart" : ' + JSON.stringify(obj.kursart) + ',';
		}
		if (obj.kursnummer !== undefined) {
			result += '"kursnummer" : ' + obj.kursnummer.toString() + ',';
		}
		if (obj.schuelergruppeUvId !== undefined) {
			result += '"schuelergruppeUvId" : ' + obj.schuelergruppeUvId.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1KursExport(obj: unknown): UVv1KursExport {
	return obj as UVv1KursExport;
}
