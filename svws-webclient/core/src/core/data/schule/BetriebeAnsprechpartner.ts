import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class BetriebeAnsprechpartner extends JavaObject {

	/**
	 * Die ID des Ansprechpartners.
	 */
	public id: number = 0;

	/**
	 * Die ID des Betriebs.
	 */
	public idBetrieb: number = 0;

	/**
	 * Das Anrede des Ansprechpartners.
	 */
	public anrede: string | null = null;

	/**
	 * Der Name des Ansprechpartners.
	 */
	public name: string | null = null;

	/**
	 * Der Rufname des Ansprechpartners.
	 */
	public rufname: string | null = null;

	/**
	 * Die Telefonnummer des Ansprechpartners.
	 */
	public telefon: string | null = null;

	/**
	 * Die eMail des Ansprechpartners.
	 */
	public eMail: string | null = null;

	/**
	 * Gibt an, ob der Ansprechpartner in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen: boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.BetriebeAnsprechpartner';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.BetriebeAnsprechpartner'].includes(name);
	}

	public static readonly class = new Class<BetriebeAnsprechpartner>('de.svws_nrw.core.data.schule.BetriebeAnsprechpartner');

	public static transpilerFromJSON(json: string): BetriebeAnsprechpartner {
		const obj = JSON.parse(json) as Partial<BetriebeAnsprechpartner>;
		const result = new BetriebeAnsprechpartner();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idBetrieb === undefined)
			throw new Error('invalid json format, missing attribute idBetrieb');
		result.idBetrieb = obj.idBetrieb;
		result.anrede = (obj.anrede === undefined) ? null : obj.anrede === null ? null : obj.anrede;
		result.name = (obj.name === undefined) ? null : obj.name === null ? null : obj.name;
		result.rufname = (obj.rufname === undefined) ? null : obj.rufname === null ? null : obj.rufname;
		result.telefon = (obj.telefon === undefined) ? null : obj.telefon === null ? null : obj.telefon;
		result.eMail = (obj.eMail === undefined) ? null : obj.eMail === null ? null : obj.eMail;
		if (obj.referenziertInAnderenTabellen === undefined)
			throw new Error('invalid json format, missing attribute referenziertInAnderenTabellen');
		result.referenziertInAnderenTabellen = obj.referenziertInAnderenTabellen;
		return result;
	}

	public static transpilerToJSON(obj: BetriebeAnsprechpartner): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idBetrieb" : ' + obj.idBetrieb.toString() + ',';
		result += '"anrede" : ' + ((obj.anrede === null) ? 'null' : JSON.stringify(obj.anrede)) + ',';
		result += '"name" : ' + ((obj.name === null) ? 'null' : JSON.stringify(obj.name)) + ',';
		result += '"rufname" : ' + ((obj.rufname === null) ? 'null' : JSON.stringify(obj.rufname)) + ',';
		result += '"telefon" : ' + ((obj.telefon === null) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		result += '"eMail" : ' + ((obj.eMail === null) ? 'null' : JSON.stringify(obj.eMail)) + ',';
		result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<BetriebeAnsprechpartner>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idBetrieb !== undefined) {
			result += '"idBetrieb" : ' + obj.idBetrieb.toString() + ',';
		}
		if (obj.anrede !== undefined) {
			result += '"anrede" : ' + ((obj.anrede === null) ? 'null' : JSON.stringify(obj.anrede)) + ',';
		}
		if (obj.name !== undefined) {
			result += '"name" : ' + ((obj.name === null) ? 'null' : JSON.stringify(obj.name)) + ',';
		}
		if (obj.rufname !== undefined) {
			result += '"rufname" : ' + ((obj.rufname === null) ? 'null' : JSON.stringify(obj.rufname)) + ',';
		}
		if (obj.telefon !== undefined) {
			result += '"telefon" : ' + ((obj.telefon === null) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		}
		if (obj.eMail !== undefined) {
			result += '"eMail" : ' + ((obj.eMail === null) ? 'null' : JSON.stringify(obj.eMail)) + ',';
		}
		if (obj.referenziertInAnderenTabellen !== undefined) {
			result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_BetriebeAnsprechpartner(obj: unknown): BetriebeAnsprechpartner {
	return obj as BetriebeAnsprechpartner;
}
