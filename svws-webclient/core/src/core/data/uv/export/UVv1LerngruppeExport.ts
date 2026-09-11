import { JavaObject } from '../../../../java/lang/JavaObject';
import { UVv1LerngruppenLehrerExport } from '../../../../core/data/uv/export/UVv1LerngruppenLehrerExport';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class UVv1LerngruppeExport extends JavaObject {

	/**
	 * Die eindeutige UV-ID der Lerngruppe.
	 */
	public uvId: number = -1;

	/**
	 * Die UV-ID der Klasse, zu der die Lerngruppe gehört.
	 */
	public klasseUvId: number | null = null;

	/**
	 * Die UV-ID des Faches, das in der Lerngruppe unterrichtet wird.
	 */
	public fachUvId: number | null = null;

	/**
	 * Die UV-ID des Kurses, der mit dieser Lerngruppe verknüpft ist.
	 */
	public kursUvId: number | null = null;

	/**
	 * Die Anzahl der vorgesehenen Wochenstunden für die Lerngruppe.
	 */
	public wochenstunden: number = 0.0;

	/**
	 * Die Anzahl der tatsächlich unterrichteten Wochenstunden der Lerngruppe.
	 */
	public wochenstundenUnterrichtet: number = 0.0;

	/**
	 * Die Schulnummer einer möglichen Koop-Schule.
	 */
	public koopSchulNr: string | null = null;

	/**
	 * Die Anzahl der externen Schüler von Koop-Schulen.
	 */
	public koopAnzahlExterne: number = 0;

	/**
	 * Ein Array mit den Lehrer-Zuordnungen der Lerngruppe.
	 */
	public lehrer: List<UVv1LerngruppenLehrerExport> = new ArrayList<UVv1LerngruppenLehrerExport>();

	/**
	 * Ein Array mit den UV-IDs der Schienen dieser Lerngruppe.
	 */
	public schienenUvIds: List<number> = new ArrayList<number>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1LerngruppeExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1LerngruppeExport'].includes(name);
	}

	public static readonly class = new Class<UVv1LerngruppeExport>('de.svws_nrw.core.data.uv.export.UVv1LerngruppeExport');

	public static transpilerFromJSON(json: string): UVv1LerngruppeExport {
		const obj = JSON.parse(json) as Partial<UVv1LerngruppeExport>;
		const result = new UVv1LerngruppeExport();
		if (obj.uvId === undefined)
			throw new Error('invalid json format, missing attribute uvId');
		result.uvId = obj.uvId;
		result.klasseUvId = (obj.klasseUvId === undefined) ? null : obj.klasseUvId === null ? null : obj.klasseUvId;
		result.fachUvId = (obj.fachUvId === undefined) ? null : obj.fachUvId === null ? null : obj.fachUvId;
		result.kursUvId = (obj.kursUvId === undefined) ? null : obj.kursUvId === null ? null : obj.kursUvId;
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (obj.wochenstundenUnterrichtet === undefined)
			throw new Error('invalid json format, missing attribute wochenstundenUnterrichtet');
		result.wochenstundenUnterrichtet = obj.wochenstundenUnterrichtet;
		result.koopSchulNr = (obj.koopSchulNr === undefined) ? null : obj.koopSchulNr === null ? null : obj.koopSchulNr;
		if (obj.koopAnzahlExterne === undefined)
			throw new Error('invalid json format, missing attribute koopAnzahlExterne');
		result.koopAnzahlExterne = obj.koopAnzahlExterne;
		if (obj.lehrer !== undefined) {
			for (const elem of obj.lehrer) {
				result.lehrer.add(UVv1LerngruppenLehrerExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schienenUvIds !== undefined) {
			for (const elem of obj.schienenUvIds) {
				result.schienenUvIds.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UVv1LerngruppeExport): string {
		let result = '{';
		result += '"uvId" : ' + obj.uvId.toString() + ',';
		result += '"klasseUvId" : ' + ((obj.klasseUvId === null) ? 'null' : obj.klasseUvId.toString()) + ',';
		result += '"fachUvId" : ' + ((obj.fachUvId === null) ? 'null' : obj.fachUvId.toString()) + ',';
		result += '"kursUvId" : ' + ((obj.kursUvId === null) ? 'null' : obj.kursUvId.toString()) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result += '"wochenstundenUnterrichtet" : ' + obj.wochenstundenUnterrichtet.toString() + ',';
		result += '"koopSchulNr" : ' + ((obj.koopSchulNr === null) ? 'null' : JSON.stringify(obj.koopSchulNr)) + ',';
		result += '"koopAnzahlExterne" : ' + obj.koopAnzahlExterne.toString() + ',';
		result += '"lehrer" : [ ';
		for (let i = 0; i < obj.lehrer.size(); i++) {
			const elem = obj.lehrer.get(i);
			result += UVv1LerngruppenLehrerExport.transpilerToJSON(elem);
			if (i < obj.lehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schienenUvIds" : [ ';
		for (let i = 0; i < obj.schienenUvIds.size(); i++) {
			const elem = obj.schienenUvIds.get(i);
			result += elem.toString();
			if (i < obj.schienenUvIds.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1LerngruppeExport>): string {
		let result = '{';
		if (obj.uvId !== undefined) {
			result += '"uvId" : ' + obj.uvId.toString() + ',';
		}
		if (obj.klasseUvId !== undefined) {
			result += '"klasseUvId" : ' + ((obj.klasseUvId === null) ? 'null' : obj.klasseUvId.toString()) + ',';
		}
		if (obj.fachUvId !== undefined) {
			result += '"fachUvId" : ' + ((obj.fachUvId === null) ? 'null' : obj.fachUvId.toString()) + ',';
		}
		if (obj.kursUvId !== undefined) {
			result += '"kursUvId" : ' + ((obj.kursUvId === null) ? 'null' : obj.kursUvId.toString()) + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		if (obj.wochenstundenUnterrichtet !== undefined) {
			result += '"wochenstundenUnterrichtet" : ' + obj.wochenstundenUnterrichtet.toString() + ',';
		}
		if (obj.koopSchulNr !== undefined) {
			result += '"koopSchulNr" : ' + ((obj.koopSchulNr === null) ? 'null' : JSON.stringify(obj.koopSchulNr)) + ',';
		}
		if (obj.koopAnzahlExterne !== undefined) {
			result += '"koopAnzahlExterne" : ' + obj.koopAnzahlExterne.toString() + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += UVv1LerngruppenLehrerExport.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schienenUvIds !== undefined) {
			result += '"schienenUvIds" : [ ';
			for (let i = 0; i < obj.schienenUvIds.size(); i++) {
				const elem = obj.schienenUvIds.get(i);
				result += elem.toString();
				if (i < obj.schienenUvIds.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1LerngruppeExport(obj: unknown): UVv1LerngruppeExport {
	return obj as UVv1LerngruppeExport;
}
