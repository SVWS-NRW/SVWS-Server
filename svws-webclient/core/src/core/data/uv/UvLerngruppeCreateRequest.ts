import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvLerngruppeCreateRequest extends JavaObject {

	/**
	 * Die negative temporäre ID beim Sammelimport.
	 */
	public id: number = 0;

	/**
	 * Die ID des Planungsabschnitts.
	 */
	public idPlanungsabschnitt: number = 0;

	/**
	 * Die ID der Klasse.
	 */
	public idKlasse: number | null = null;

	/**
	 * Die ID des Fachs.
	 */
	public idFach: number | null = null;

	/**
	 * Die ID des Kurses.
	 */
	public idKurs: number | null = null;

	/**
	 * Die Wochenstunden.
	 */
	public wochenstunden: number = 0.0;

	/**
	 * Die unterrichteten Wochenstunden.
	 */
	public wochenstundenUnterrichtet: number = 0.0;

	/**
	 * Die Kooperationsschulnummer.
	 */
	public koopSchulNr: string | null = null;

	/**
	 * Die Anzahl externer Schülerinnen und Schüler.
	 */
	public koopAnzahlExterne: number | null = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvLerngruppeCreateRequest';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvLerngruppeCreateRequest'].includes(name);
	}

	public static readonly class = new Class<UvLerngruppeCreateRequest>('de.svws_nrw.core.data.uv.UvLerngruppeCreateRequest');

	public static transpilerFromJSON(json: string): UvLerngruppeCreateRequest {
		const obj = JSON.parse(json) as Partial<UvLerngruppeCreateRequest>;
		const result = new UvLerngruppeCreateRequest();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		result.idKlasse = (obj.idKlasse === undefined) ? null : obj.idKlasse === null ? null : obj.idKlasse;
		result.idFach = (obj.idFach === undefined) ? null : obj.idFach === null ? null : obj.idFach;
		result.idKurs = (obj.idKurs === undefined) ? null : obj.idKurs === null ? null : obj.idKurs;
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (obj.wochenstundenUnterrichtet === undefined)
			throw new Error('invalid json format, missing attribute wochenstundenUnterrichtet');
		result.wochenstundenUnterrichtet = obj.wochenstundenUnterrichtet;
		result.koopSchulNr = (obj.koopSchulNr === undefined) ? null : obj.koopSchulNr === null ? null : obj.koopSchulNr;
		result.koopAnzahlExterne = (obj.koopAnzahlExterne === undefined) ? null : obj.koopAnzahlExterne === null ? null : obj.koopAnzahlExterne;
		return result;
	}

	public static transpilerToJSON(obj: UvLerngruppeCreateRequest): string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		result += '"idKlasse" : ' + ((obj.idKlasse === null) ? 'null' : obj.idKlasse.toString()) + ',';
		result += '"idFach" : ' + ((obj.idFach === null) ? 'null' : obj.idFach.toString()) + ',';
		result += '"idKurs" : ' + ((obj.idKurs === null) ? 'null' : obj.idKurs.toString()) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		result += '"wochenstundenUnterrichtet" : ' + obj.wochenstundenUnterrichtet + ',';
		result += '"koopSchulNr" : ' + ((obj.koopSchulNr === null) ? 'null' : JSON.stringify(obj.koopSchulNr)) + ',';
		result += '"koopAnzahlExterne" : ' + ((obj.koopAnzahlExterne === null) ? 'null' : obj.koopAnzahlExterne.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvLerngruppeCreateRequest>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id + ',';
		}
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		}
		if (obj.idKlasse !== undefined) {
			result += '"idKlasse" : ' + ((obj.idKlasse === null) ? 'null' : obj.idKlasse.toString()) + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + ((obj.idFach === null) ? 'null' : obj.idFach.toString()) + ',';
		}
		if (obj.idKurs !== undefined) {
			result += '"idKurs" : ' + ((obj.idKurs === null) ? 'null' : obj.idKurs.toString()) + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		}
		if (obj.wochenstundenUnterrichtet !== undefined) {
			result += '"wochenstundenUnterrichtet" : ' + obj.wochenstundenUnterrichtet + ',';
		}
		if (obj.koopSchulNr !== undefined) {
			result += '"koopSchulNr" : ' + ((obj.koopSchulNr === null) ? 'null' : JSON.stringify(obj.koopSchulNr)) + ',';
		}
		if (obj.koopAnzahlExterne !== undefined) {
			result += '"koopAnzahlExterne" : ' + ((obj.koopAnzahlExterne === null) ? 'null' : obj.koopAnzahlExterne.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvLerngruppeCreateRequest(obj: unknown): UvLerngruppeCreateRequest {
	return obj as UvLerngruppeCreateRequest;
}
