import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class BetriebAnsprechpartner extends JavaObject {

	/**
	 * ID des Ansprechpartners
	 */
	public id : number = 0;

	/**
	 * ID des Betriebs, dem der Ansprechpartner zugeordnet ist
	 */
	public betrieb_id : number = 0;

	/**
	 * Name des Ansprechpartners im Betrieb
	 */
	public name : string | null = null;

	/**
	 * Vorname des Ansprechpartners im Betrieb
	 */
	public vorname : string | null = null;

	/**
	 * Anrede des Ansprechpartners im Betrieb
	 */
	public anrede : string | null = null;

	/**
	 * Telefonnummer des Ansprechpartners im Betrieb
	 */
	public telefon : string | null = null;

	/**
	 * Email-Adresse des Ansprechpartners im Betrieb
	 */
	public email : string | null = null;

	/**
	 * ggf Abteilung des Ansprechpartners im Betrieb
	 */
	public abteilung : string | null = null;

	/**
	 * Titel des Ansprechpartners im Betrieb
	 */
	public titel : string | null = null;

	/**
	 * GU_ID des Ansprechpartners im Betrieb
	 */
	public GU_ID : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.betrieb.BetriebAnsprechpartner';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.betrieb.BetriebAnsprechpartner'].includes(name);
	}

	public static class = new Class<BetriebAnsprechpartner>('de.svws_nrw.core.data.betrieb.BetriebAnsprechpartner');

	public static transpilerFromJSON(json : string): BetriebAnsprechpartner {
		const obj = JSON.parse(json) as Partial<BetriebAnsprechpartner>;
		const result = new BetriebAnsprechpartner();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.betrieb_id === undefined)
			throw new Error('invalid json format, missing attribute betrieb_id');
		result.betrieb_id = obj.betrieb_id;
		result.name = (obj.name === undefined) ? null : obj.name === null ? null : obj.name;
		result.vorname = (obj.vorname === undefined) ? null : obj.vorname === null ? null : obj.vorname;
		result.anrede = (obj.anrede === undefined) ? null : obj.anrede === null ? null : obj.anrede;
		result.telefon = (obj.telefon === undefined) ? null : obj.telefon === null ? null : obj.telefon;
		result.email = (obj.email === undefined) ? null : obj.email === null ? null : obj.email;
		result.abteilung = (obj.abteilung === undefined) ? null : obj.abteilung === null ? null : obj.abteilung;
		result.titel = (obj.titel === undefined) ? null : obj.titel === null ? null : obj.titel;
		result.GU_ID = (obj.GU_ID === undefined) ? null : obj.GU_ID === null ? null : obj.GU_ID;
		return result;
	}

	public static transpilerToJSON(obj : BetriebAnsprechpartner) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"betrieb_id" : ' + obj.betrieb_id.toString() + ',';
		result += '"name" : ' + ((obj.name === null) ? 'null' : JSON.stringify(obj.name)) + ',';
		result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		result += '"anrede" : ' + ((obj.anrede === null) ? 'null' : JSON.stringify(obj.anrede)) + ',';
		result += '"telefon" : ' + ((obj.telefon === null) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		result += '"email" : ' + ((obj.email === null) ? 'null' : JSON.stringify(obj.email)) + ',';
		result += '"abteilung" : ' + ((obj.abteilung === null) ? 'null' : JSON.stringify(obj.abteilung)) + ',';
		result += '"titel" : ' + ((obj.titel === null) ? 'null' : JSON.stringify(obj.titel)) + ',';
		result += '"GU_ID" : ' + ((obj.GU_ID === null) ? 'null' : JSON.stringify(obj.GU_ID)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BetriebAnsprechpartner>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.betrieb_id !== undefined) {
			result += '"betrieb_id" : ' + obj.betrieb_id.toString() + ',';
		}
		if (obj.name !== undefined) {
			result += '"name" : ' + ((obj.name === null) ? 'null' : JSON.stringify(obj.name)) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		}
		if (obj.anrede !== undefined) {
			result += '"anrede" : ' + ((obj.anrede === null) ? 'null' : JSON.stringify(obj.anrede)) + ',';
		}
		if (obj.telefon !== undefined) {
			result += '"telefon" : ' + ((obj.telefon === null) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		}
		if (obj.email !== undefined) {
			result += '"email" : ' + ((obj.email === null) ? 'null' : JSON.stringify(obj.email)) + ',';
		}
		if (obj.abteilung !== undefined) {
			result += '"abteilung" : ' + ((obj.abteilung === null) ? 'null' : JSON.stringify(obj.abteilung)) + ',';
		}
		if (obj.titel !== undefined) {
			result += '"titel" : ' + ((obj.titel === null) ? 'null' : JSON.stringify(obj.titel)) + ',';
		}
		if (obj.GU_ID !== undefined) {
			result += '"GU_ID" : ' + ((obj.GU_ID === null) ? 'null' : JSON.stringify(obj.GU_ID)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_betrieb_BetriebAnsprechpartner(obj : unknown) : BetriebAnsprechpartner {
	return obj as BetriebAnsprechpartner;
}
