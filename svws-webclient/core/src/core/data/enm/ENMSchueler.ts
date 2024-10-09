import { JavaObject } from '../../../java/lang/JavaObject';
import { ENMLeistung } from '../../../core/data/enm/ENMLeistung';
import { ENMLernabschnitt } from '../../../core/data/enm/ENMLernabschnitt';
import { ENMLeistungBemerkungen } from '../../../core/data/enm/ENMLeistungBemerkungen';
import { ENMZP10 } from '../../../core/data/enm/ENMZP10';
import { ArrayList } from '../../../java/util/ArrayList';
import { ENMSchuelerAnkreuzkompetenz } from '../../../core/data/enm/ENMSchuelerAnkreuzkompetenz';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ENMBKAbschluss } from '../../../core/data/enm/ENMBKAbschluss';
import { ENMSprachenfolge } from '../../../core/data/enm/ENMSprachenfolge';

export class ENMSchueler extends JavaObject {

	/**
	 * Die ID des Schülers in der SVWS-DB
	 */
	public id : number = 0;

	/**
	 * Die ID des aktuellen Jahrgangs, in dem sich der Schüler befindet
	 */
	public jahrgangID : number = 0;

	/**
	 * Die ID der aktuellen Klasse, in der sich der Schüler befindet
	 */
	public klasseID : number = 0;

	/**
	 * Der Nachname des Schülers (z.B. Mustermann)
	 */
	public nachname : string | null = null;

	/**
	 * Der Vorname des Schülers (z.B. Max)
	 */
	public vorname : string | null = null;

	/**
	 * Das Geschlecht des Schülers (m,w,d,x)
	 */
	public geschlecht : string | null = null;

	/**
	 * Gibt an, ob sich der Schüler aktuell im bilingualen Bildungsgang befindet (wenn ja, z.B. F) oder nicht (null)
	 */
	public bilingualeSprache : string | null = null;

	/**
	 * Gibt an, ob der Schüler Ziel-different unterrichtet wird
	 */
	public istZieldifferent : boolean = false;

	/**
	 * Gibt an, ob der Schüler Deutsch-Förderung mit Deutsch als Zweitsprache (DaZ) bekommt (Seiteneinsteiger, z.B. Flüchtlingskinder)
	 */
	public istDaZFoerderung : boolean = false;

	/**
	 * Die Sprachenfolge des Schülers
	 */
	public sprachenfolge : List<ENMSprachenfolge> = new ArrayList<ENMSprachenfolge>();

	/**
	 * Informationen zum Lernabschnitt des Schülers in der Notendatei
	 */
	public lernabschnitt : ENMLernabschnitt = new ENMLernabschnitt();

	/**
	 * Die Leistungsdaten des Schülers in dem Lernabschnitt der Notendatei
	 */
	public leistungsdaten : List<ENMLeistung> = new ArrayList<ENMLeistung>();

	/**
	 * Die Ankreuzkompetenzen des Schülers in dem Lernabschnitt der Notendatei
	 */
	public ankreuzkompetenzen : List<ENMSchuelerAnkreuzkompetenz> = new ArrayList<ENMSchuelerAnkreuzkompetenz>();

	/**
	 * Die Bemerkungen bei dem Schüler in Bezug auf den Lernabschnitt der Notendatei
	 */
	public bemerkungen : ENMLeistungBemerkungen = new ENMLeistungBemerkungen();

	/**
	 * Die Informationen zu den Zentralen Prüfungen Klasse 10, sofern vorhanden - ansonsten null
	 */
	public zp10 : ENMZP10 | null = null;

	/**
	 * Die Informationen zu den Abschlüssen am Berufskolleg, sofern vorhanden - ansonsten null
	 */
	public bkabschluss : ENMBKAbschluss | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMSchueler';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMSchueler'].includes(name);
	}

	public static class = new Class<ENMSchueler>('de.svws_nrw.core.data.enm.ENMSchueler');

	public static transpilerFromJSON(json : string): ENMSchueler {
		const obj = JSON.parse(json) as Partial<ENMSchueler>;
		const result = new ENMSchueler();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.jahrgangID === undefined)
			throw new Error('invalid json format, missing attribute jahrgangID');
		result.jahrgangID = obj.jahrgangID;
		if (obj.klasseID === undefined)
			throw new Error('invalid json format, missing attribute klasseID');
		result.klasseID = obj.klasseID;
		result.nachname = (obj.nachname === undefined) ? null : obj.nachname === null ? null : obj.nachname;
		result.vorname = (obj.vorname === undefined) ? null : obj.vorname === null ? null : obj.vorname;
		result.geschlecht = (obj.geschlecht === undefined) ? null : obj.geschlecht === null ? null : obj.geschlecht;
		result.bilingualeSprache = (obj.bilingualeSprache === undefined) ? null : obj.bilingualeSprache === null ? null : obj.bilingualeSprache;
		if (obj.istZieldifferent === undefined)
			throw new Error('invalid json format, missing attribute istZieldifferent');
		result.istZieldifferent = obj.istZieldifferent;
		if (obj.istDaZFoerderung === undefined)
			throw new Error('invalid json format, missing attribute istDaZFoerderung');
		result.istDaZFoerderung = obj.istDaZFoerderung;
		if (obj.sprachenfolge !== undefined) {
			for (const elem of obj.sprachenfolge) {
				result.sprachenfolge.add(ENMSprachenfolge.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lernabschnitt === undefined)
			throw new Error('invalid json format, missing attribute lernabschnitt');
		result.lernabschnitt = ENMLernabschnitt.transpilerFromJSON(JSON.stringify(obj.lernabschnitt));
		if (obj.leistungsdaten !== undefined) {
			for (const elem of obj.leistungsdaten) {
				result.leistungsdaten.add(ENMLeistung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.ankreuzkompetenzen !== undefined) {
			for (const elem of obj.ankreuzkompetenzen) {
				result.ankreuzkompetenzen.add(ENMSchuelerAnkreuzkompetenz.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.bemerkungen === undefined)
			throw new Error('invalid json format, missing attribute bemerkungen');
		result.bemerkungen = ENMLeistungBemerkungen.transpilerFromJSON(JSON.stringify(obj.bemerkungen));
		result.zp10 = ((obj.zp10 === undefined) || (obj.zp10 === null)) ? null : ENMZP10.transpilerFromJSON(JSON.stringify(obj.zp10));
		result.bkabschluss = ((obj.bkabschluss === undefined) || (obj.bkabschluss === null)) ? null : ENMBKAbschluss.transpilerFromJSON(JSON.stringify(obj.bkabschluss));
		return result;
	}

	public static transpilerToJSON(obj : ENMSchueler) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"jahrgangID" : ' + obj.jahrgangID.toString() + ',';
		result += '"klasseID" : ' + obj.klasseID.toString() + ',';
		result += '"nachname" : ' + ((!obj.nachname) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		result += '"vorname" : ' + ((!obj.vorname) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		result += '"geschlecht" : ' + ((!obj.geschlecht) ? 'null' : JSON.stringify(obj.geschlecht)) + ',';
		result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		result += '"istZieldifferent" : ' + obj.istZieldifferent.toString() + ',';
		result += '"istDaZFoerderung" : ' + obj.istDaZFoerderung.toString() + ',';
		result += '"sprachenfolge" : [ ';
		for (let i = 0; i < obj.sprachenfolge.size(); i++) {
			const elem = obj.sprachenfolge.get(i);
			result += ENMSprachenfolge.transpilerToJSON(elem);
			if (i < obj.sprachenfolge.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lernabschnitt" : ' + ENMLernabschnitt.transpilerToJSON(obj.lernabschnitt) + ',';
		result += '"leistungsdaten" : [ ';
		for (let i = 0; i < obj.leistungsdaten.size(); i++) {
			const elem = obj.leistungsdaten.get(i);
			result += ENMLeistung.transpilerToJSON(elem);
			if (i < obj.leistungsdaten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"ankreuzkompetenzen" : [ ';
		for (let i = 0; i < obj.ankreuzkompetenzen.size(); i++) {
			const elem = obj.ankreuzkompetenzen.get(i);
			result += ENMSchuelerAnkreuzkompetenz.transpilerToJSON(elem);
			if (i < obj.ankreuzkompetenzen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"bemerkungen" : ' + ENMLeistungBemerkungen.transpilerToJSON(obj.bemerkungen) + ',';
		result += '"zp10" : ' + ((!obj.zp10) ? 'null' : ENMZP10.transpilerToJSON(obj.zp10)) + ',';
		result += '"bkabschluss" : ' + ((!obj.bkabschluss) ? 'null' : ENMBKAbschluss.transpilerToJSON(obj.bkabschluss)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMSchueler>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.jahrgangID !== undefined) {
			result += '"jahrgangID" : ' + obj.jahrgangID.toString() + ',';
		}
		if (obj.klasseID !== undefined) {
			result += '"klasseID" : ' + obj.klasseID.toString() + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + ((!obj.nachname) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + ((!obj.vorname) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		}
		if (obj.geschlecht !== undefined) {
			result += '"geschlecht" : ' + ((!obj.geschlecht) ? 'null' : JSON.stringify(obj.geschlecht)) + ',';
		}
		if (obj.bilingualeSprache !== undefined) {
			result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		}
		if (obj.istZieldifferent !== undefined) {
			result += '"istZieldifferent" : ' + obj.istZieldifferent.toString() + ',';
		}
		if (obj.istDaZFoerderung !== undefined) {
			result += '"istDaZFoerderung" : ' + obj.istDaZFoerderung.toString() + ',';
		}
		if (obj.sprachenfolge !== undefined) {
			result += '"sprachenfolge" : [ ';
			for (let i = 0; i < obj.sprachenfolge.size(); i++) {
				const elem = obj.sprachenfolge.get(i);
				result += ENMSprachenfolge.transpilerToJSON(elem);
				if (i < obj.sprachenfolge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lernabschnitt !== undefined) {
			result += '"lernabschnitt" : ' + ENMLernabschnitt.transpilerToJSON(obj.lernabschnitt) + ',';
		}
		if (obj.leistungsdaten !== undefined) {
			result += '"leistungsdaten" : [ ';
			for (let i = 0; i < obj.leistungsdaten.size(); i++) {
				const elem = obj.leistungsdaten.get(i);
				result += ENMLeistung.transpilerToJSON(elem);
				if (i < obj.leistungsdaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.ankreuzkompetenzen !== undefined) {
			result += '"ankreuzkompetenzen" : [ ';
			for (let i = 0; i < obj.ankreuzkompetenzen.size(); i++) {
				const elem = obj.ankreuzkompetenzen.get(i);
				result += ENMSchuelerAnkreuzkompetenz.transpilerToJSON(elem);
				if (i < obj.ankreuzkompetenzen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.bemerkungen !== undefined) {
			result += '"bemerkungen" : ' + ENMLeistungBemerkungen.transpilerToJSON(obj.bemerkungen) + ',';
		}
		if (obj.zp10 !== undefined) {
			result += '"zp10" : ' + ((!obj.zp10) ? 'null' : ENMZP10.transpilerToJSON(obj.zp10)) + ',';
		}
		if (obj.bkabschluss !== undefined) {
			result += '"bkabschluss" : ' + ((!obj.bkabschluss) ? 'null' : ENMBKAbschluss.transpilerToJSON(obj.bkabschluss)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMSchueler(obj : unknown) : ENMSchueler {
	return obj as ENMSchueler;
}
