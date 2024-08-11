import { JavaObject } from '../../../java/lang/JavaObject';
import { KalenderEintrag } from '../../../core/data/kalender/KalenderEintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class Kalender extends JavaObject {

	/**
	 * ID des Kalenders
	 */
	public id : string = "";

	/**
	 * Anzeigename des Kalenders
	 */
	public displayname : string | null = null;

	/**
	 * Beschreibung des Kalenders
	 */
	public beschreibung : string | null = null;

	/**
	 * Versionskennzeichen des Kalenders
	 */
	public synctoken : number = 0;

	/**
	 *  der Typ des Kalenders
	 */
	public kalenderTyp : string = "";

	/**
	 *  eine Liste der Einträge des Kalenders
	 */
	public kalenderEintraege : List<KalenderEintrag> = new ArrayList<KalenderEintrag>();

	/**
	 * ob der angemeldete Nutzer Schreibrecht auf dem Kalender hat
	 */
	public darfSchreiben : boolean = false;

	/**
	 * ob der angemeldete Nutzer Leserecht auf dem Kalender hat
	 */
	public darfLesen : boolean = false;

	/**
	 * BenutzerId des Besitzers dieses Kalenders
	 */
	public besitzer : number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.kalender.Kalender';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kalender.Kalender'].includes(name);
	}

	public static transpilerFromJSON(json : string): Kalender {
		const obj = JSON.parse(json) as Partial<Kalender>;
		const result = new Kalender();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.displayname = (obj.displayname === undefined) ? null : obj.displayname === null ? null : obj.displayname;
		result.beschreibung = (obj.beschreibung === undefined) ? null : obj.beschreibung === null ? null : obj.beschreibung;
		if (obj.synctoken === undefined)
			throw new Error('invalid json format, missing attribute synctoken');
		result.synctoken = obj.synctoken;
		if (obj.kalenderTyp === undefined)
			throw new Error('invalid json format, missing attribute kalenderTyp');
		result.kalenderTyp = obj.kalenderTyp;
		if (obj.kalenderEintraege !== undefined) {
			for (const elem of obj.kalenderEintraege) {
				result.kalenderEintraege.add(KalenderEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.darfSchreiben === undefined)
			throw new Error('invalid json format, missing attribute darfSchreiben');
		result.darfSchreiben = obj.darfSchreiben;
		if (obj.darfLesen === undefined)
			throw new Error('invalid json format, missing attribute darfLesen');
		result.darfLesen = obj.darfLesen;
		if (obj.besitzer === undefined)
			throw new Error('invalid json format, missing attribute besitzer');
		result.besitzer = obj.besitzer;
		return result;
	}

	public static transpilerToJSON(obj : Kalender) : string {
		let result = '{';
		result += '"id" : ' + JSON.stringify(obj.id) + ',';
		result += '"displayname" : ' + ((!obj.displayname) ? 'null' : JSON.stringify(obj.displayname)) + ',';
		result += '"beschreibung" : ' + ((!obj.beschreibung) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		result += '"synctoken" : ' + obj.synctoken.toString() + ',';
		result += '"kalenderTyp" : ' + JSON.stringify(obj.kalenderTyp) + ',';
		result += '"kalenderEintraege" : [ ';
		for (let i = 0; i < obj.kalenderEintraege.size(); i++) {
			const elem = obj.kalenderEintraege.get(i);
			result += KalenderEintrag.transpilerToJSON(elem);
			if (i < obj.kalenderEintraege.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"darfSchreiben" : ' + obj.darfSchreiben.toString() + ',';
		result += '"darfLesen" : ' + obj.darfLesen.toString() + ',';
		result += '"besitzer" : ' + obj.besitzer.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Kalender>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + JSON.stringify(obj.id) + ',';
		}
		if (obj.displayname !== undefined) {
			result += '"displayname" : ' + ((!obj.displayname) ? 'null' : JSON.stringify(obj.displayname)) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + ((!obj.beschreibung) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		}
		if (obj.synctoken !== undefined) {
			result += '"synctoken" : ' + obj.synctoken.toString() + ',';
		}
		if (obj.kalenderTyp !== undefined) {
			result += '"kalenderTyp" : ' + JSON.stringify(obj.kalenderTyp) + ',';
		}
		if (obj.kalenderEintraege !== undefined) {
			result += '"kalenderEintraege" : [ ';
			for (let i = 0; i < obj.kalenderEintraege.size(); i++) {
				const elem = obj.kalenderEintraege.get(i);
				result += KalenderEintrag.transpilerToJSON(elem);
				if (i < obj.kalenderEintraege.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.darfSchreiben !== undefined) {
			result += '"darfSchreiben" : ' + obj.darfSchreiben.toString() + ',';
		}
		if (obj.darfLesen !== undefined) {
			result += '"darfLesen" : ' + obj.darfLesen.toString() + ',';
		}
		if (obj.besitzer !== undefined) {
			result += '"besitzer" : ' + obj.besitzer.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_kalender_Kalender(obj : unknown) : Kalender {
	return obj as Kalender;
}
