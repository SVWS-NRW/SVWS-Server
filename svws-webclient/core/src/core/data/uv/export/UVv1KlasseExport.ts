import { JavaObject } from '../../../../java/lang/JavaObject';
import { UVv1KlassenLehrerExport } from '../../../../core/data/uv/export/UVv1KlassenLehrerExport';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class UVv1KlasseExport extends JavaObject {

	/**
	 *  Die UV-ID der Klasse (generiert, planungsspezifisch).
	 */
	public uvId: number = 0;

	/**
	 *  Die ID des Schuljahresabschnitts.
	 */
	public idSchuljahresabschnitt: number = 0;

	/**
	 *  Bezeichnender Text für die Klasse.
	 */
	public bezeichnung: string | null = null;

	/**
	 *  Das Kürzel der Klasse.
	 */
	public kuerzel: string = "";

	/**
	 *  Die Parallelität (z. B. a/b/c).
	 */
	public parallelitaet: string = "";

	/**
	 *  Die UV-ID der Stundentafel.
	 */
	public idStundentafel: number | null = null;

	/**
	 *  Die UV-ID der zugehörigen Schülergruppe.
	 */
	public schuelergruppeUvId: number = 0;

	/**
	 *  Das Kürzel der Organisationsform.
	 */
	public orgFormKrz: string | null = null;

	/**
	 *  Die ID der Fachklasse (nur BK SBK).
	 */
	public idFachklasse: number | null = null;

	/**
	 *  Die Schulgliederungsnummer (ASD-Schulform-Nr).
	 */
	public asdSchulformNr: string | null = null;

	/**
	 * Ein Array mit den Klassenlehrer-Zuordnungen der Klasse.
	 */
	public klassenlehrer: List<UVv1KlassenLehrerExport> = new ArrayList<UVv1KlassenLehrerExport>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1KlasseExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1KlasseExport'].includes(name);
	}

	public static readonly class = new Class<UVv1KlasseExport>('de.svws_nrw.core.data.uv.export.UVv1KlasseExport');

	public static transpilerFromJSON(json: string): UVv1KlasseExport {
		const obj = JSON.parse(json) as Partial<UVv1KlasseExport>;
		const result = new UVv1KlasseExport();
		if (obj.uvId === undefined)
			throw new Error('invalid json format, missing attribute uvId');
		result.uvId = obj.uvId;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.parallelitaet === undefined)
			throw new Error('invalid json format, missing attribute parallelitaet');
		result.parallelitaet = obj.parallelitaet;
		result.idStundentafel = (obj.idStundentafel === undefined) ? null : obj.idStundentafel === null ? null : obj.idStundentafel;
		if (obj.schuelergruppeUvId === undefined)
			throw new Error('invalid json format, missing attribute schuelergruppeUvId');
		result.schuelergruppeUvId = obj.schuelergruppeUvId;
		result.orgFormKrz = (obj.orgFormKrz === undefined) ? null : obj.orgFormKrz === null ? null : obj.orgFormKrz;
		result.idFachklasse = (obj.idFachklasse === undefined) ? null : obj.idFachklasse === null ? null : obj.idFachklasse;
		result.asdSchulformNr = (obj.asdSchulformNr === undefined) ? null : obj.asdSchulformNr === null ? null : obj.asdSchulformNr;
		if (obj.klassenlehrer !== undefined) {
			for (const elem of obj.klassenlehrer) {
				result.klassenlehrer.add(UVv1KlassenLehrerExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UVv1KlasseExport): string {
		let result = '{';
		result += '"uvId" : ' + obj.uvId.toString() + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"parallelitaet" : ' + JSON.stringify(obj.parallelitaet) + ',';
		result += '"idStundentafel" : ' + ((obj.idStundentafel === null) ? 'null' : obj.idStundentafel.toString()) + ',';
		result += '"schuelergruppeUvId" : ' + obj.schuelergruppeUvId.toString() + ',';
		result += '"orgFormKrz" : ' + ((obj.orgFormKrz === null) ? 'null' : JSON.stringify(obj.orgFormKrz)) + ',';
		result += '"idFachklasse" : ' + ((obj.idFachklasse === null) ? 'null' : obj.idFachklasse.toString()) + ',';
		result += '"asdSchulformNr" : ' + ((obj.asdSchulformNr === null) ? 'null' : JSON.stringify(obj.asdSchulformNr)) + ',';
		result += '"klassenlehrer" : [ ';
		for (let i = 0; i < obj.klassenlehrer.size(); i++) {
			const elem = obj.klassenlehrer.get(i);
			result += UVv1KlassenLehrerExport.transpilerToJSON(elem);
			if (i < obj.klassenlehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1KlasseExport>): string {
		let result = '{';
		if (obj.uvId !== undefined) {
			result += '"uvId" : ' + obj.uvId.toString() + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.parallelitaet !== undefined) {
			result += '"parallelitaet" : ' + JSON.stringify(obj.parallelitaet) + ',';
		}
		if (obj.idStundentafel !== undefined) {
			result += '"idStundentafel" : ' + ((obj.idStundentafel === null) ? 'null' : obj.idStundentafel.toString()) + ',';
		}
		if (obj.schuelergruppeUvId !== undefined) {
			result += '"schuelergruppeUvId" : ' + obj.schuelergruppeUvId.toString() + ',';
		}
		if (obj.orgFormKrz !== undefined) {
			result += '"orgFormKrz" : ' + ((obj.orgFormKrz === null) ? 'null' : JSON.stringify(obj.orgFormKrz)) + ',';
		}
		if (obj.idFachklasse !== undefined) {
			result += '"idFachklasse" : ' + ((obj.idFachklasse === null) ? 'null' : obj.idFachklasse.toString()) + ',';
		}
		if (obj.asdSchulformNr !== undefined) {
			result += '"asdSchulformNr" : ' + ((obj.asdSchulformNr === null) ? 'null' : JSON.stringify(obj.asdSchulformNr)) + ',';
		}
		if (obj.klassenlehrer !== undefined) {
			result += '"klassenlehrer" : [ ';
			for (let i = 0; i < obj.klassenlehrer.size(); i++) {
				const elem = obj.klassenlehrer.get(i);
				result += UVv1KlassenLehrerExport.transpilerToJSON(elem);
				if (i < obj.klassenlehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1KlasseExport(obj: unknown): UVv1KlasseExport {
	return obj as UVv1KlasseExport;
}
