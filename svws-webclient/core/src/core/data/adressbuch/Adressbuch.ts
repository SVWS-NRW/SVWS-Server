import { JavaObject } from '../../../java/lang/JavaObject';
import { AdressbuchEintrag } from '../../../core/data/adressbuch/AdressbuchEintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class Adressbuch extends JavaObject {

	/**
	 * ID des Adressbuchs
	 */
	public id : string = "";

	/**
	 * Anzeigename des Adressbuchs
	 */
	public displayname : string | null = null;

	/**
	 * Beschreibung des Adressbuchs
	 */
	public beschreibung : string | null = null;

	/**
	 * Versionskennzeichen des Adressbuchs
	 */
	public synctoken : number = 0;

	/**
	 * Der Typ des Adressbuchs
	 */
	public adressbuchTyp : string = "";

	/**
	 * Eine Liste der Einträge des Adressbuchs
	 */
	public adressbuchEintraege : List<AdressbuchEintrag> = new ArrayList<AdressbuchEintrag>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.adressbuch.Adressbuch';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.adressbuch.Adressbuch'].includes(name);
	}

	public static class = new Class<Adressbuch>('de.svws_nrw.core.data.adressbuch.Adressbuch');

	public static transpilerFromJSON(json : string): Adressbuch {
		const obj = JSON.parse(json) as Partial<Adressbuch>;
		const result = new Adressbuch();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.displayname = (obj.displayname === undefined) ? null : obj.displayname === null ? null : obj.displayname;
		result.beschreibung = (obj.beschreibung === undefined) ? null : obj.beschreibung === null ? null : obj.beschreibung;
		if (obj.synctoken === undefined)
			throw new Error('invalid json format, missing attribute synctoken');
		result.synctoken = obj.synctoken;
		if (obj.adressbuchTyp === undefined)
			throw new Error('invalid json format, missing attribute adressbuchTyp');
		result.adressbuchTyp = obj.adressbuchTyp;
		if (obj.adressbuchEintraege !== undefined) {
			for (const elem of obj.adressbuchEintraege) {
				result.adressbuchEintraege.add(AdressbuchEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : Adressbuch) : string {
		let result = '{';
		result += '"id" : ' + JSON.stringify(obj.id) + ',';
		result += '"displayname" : ' + ((obj.displayname === null) ? 'null' : JSON.stringify(obj.displayname)) + ',';
		result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		result += '"synctoken" : ' + obj.synctoken.toString() + ',';
		result += '"adressbuchTyp" : ' + JSON.stringify(obj.adressbuchTyp) + ',';
		result += '"adressbuchEintraege" : [ ';
		for (let i = 0; i < obj.adressbuchEintraege.size(); i++) {
			const elem = obj.adressbuchEintraege.get(i);
			result += AdressbuchEintrag.transpilerToJSON(elem);
			if (i < obj.adressbuchEintraege.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Adressbuch>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + JSON.stringify(obj.id) + ',';
		}
		if (obj.displayname !== undefined) {
			result += '"displayname" : ' + ((obj.displayname === null) ? 'null' : JSON.stringify(obj.displayname)) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		}
		if (obj.synctoken !== undefined) {
			result += '"synctoken" : ' + obj.synctoken.toString() + ',';
		}
		if (obj.adressbuchTyp !== undefined) {
			result += '"adressbuchTyp" : ' + JSON.stringify(obj.adressbuchTyp) + ',';
		}
		if (obj.adressbuchEintraege !== undefined) {
			result += '"adressbuchEintraege" : [ ';
			for (let i = 0; i < obj.adressbuchEintraege.size(); i++) {
				const elem = obj.adressbuchEintraege.get(i);
				result += AdressbuchEintrag.transpilerToJSON(elem);
				if (i < obj.adressbuchEintraege.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_adressbuch_Adressbuch(obj : unknown) : Adressbuch {
	return obj as Adressbuch;
}
