import { JavaObject } from '../../../../java/lang/JavaObject';
import { ENMv2Sprachenfolge } from '../../../../core/data/enm/v2/ENMv2Sprachenfolge';
import { ENMv2Leistung } from '../../../../core/data/enm/v2/ENMv2Leistung';
import { ENMv2ZP10 } from '../../../../core/data/enm/v2/ENMv2ZP10';
import { ENMv2SchuelerAnkreuzkompetenz } from '../../../../core/data/enm/v2/ENMv2SchuelerAnkreuzkompetenz';
import { ArrayList } from '../../../../java/util/ArrayList';
import { ENMv2LeistungBemerkungen } from '../../../../core/data/enm/v2/ENMv2LeistungBemerkungen';
import { ENMv2Lernabschnitt } from '../../../../core/data/enm/v2/ENMv2Lernabschnitt';
import { ENMv2BKAbschluss } from '../../../../core/data/enm/v2/ENMv2BKAbschluss';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class ENMv2Schueler extends JavaObject {

	/**
	 * Die ID des Schülers in der SVWS-DB
	 */
	public id: number = 0;

	/**
	 * Die ID des aktuellen Jahrgangs, in dem sich der Schüler befindet
	 */
	public jahrgangID: number = 0;

	/**
	 * Die ID der aktuellen Klasse, in der sich der Schüler befindet
	 */
	public klasseID: number = 0;

	/**
	 * Der Nachname des Schülers (z.B. Mustermann)
	 */
	public nachname: string | null = null;

	/**
	 * Der Vorname des Schülers (z.B. Max)
	 */
	public vorname: string | null = null;

	/**
	 * Das Geschlecht des Schülers (m,w,d,x)
	 */
	public geschlecht: string | null = null;

	/**
	 * Gibt an, ob sich der Schüler aktuell im bilingualen Bildungsgang befindet (wenn ja, z.B. F) oder nicht (null)
	 */
	public bilingualeSprache: string | null = null;

	/**
	 * Gibt an, ob der Schüler Ziel-different unterrichtet wird
	 */
	public istZieldifferent: boolean = false;

	/**
	 * Gibt an, ob der Schüler Deutsch-Förderung mit Deutsch als Zweitsprache (DaZ) bekommt (Seiteneinsteiger, z.B. Flüchtlingskinder)
	 */
	public istDaZFoerderung: boolean = false;

	/**
	 * Die Sprachenfolge des Schülers
	 */
	public sprachenfolge: List<ENMv2Sprachenfolge> = new ArrayList<ENMv2Sprachenfolge>();

	/**
	 * Informationen zum Lernabschnitt des Schülers in der Notendatei
	 */
	public lernabschnitt: ENMv2Lernabschnitt = new ENMv2Lernabschnitt();

	/**
	 * Die Leistungsdaten des Schülers in dem Lernabschnitt der Notendatei
	 */
	public leistungsdaten: List<ENMv2Leistung> = new ArrayList<ENMv2Leistung>();

	/**
	 * Die Ankreuzkompetenzen des Schülers in dem Lernabschnitt der Notendatei
	 */
	public ankreuzkompetenzen: List<ENMv2SchuelerAnkreuzkompetenz> = new ArrayList<ENMv2SchuelerAnkreuzkompetenz>();

	/**
	 * Die Bemerkungen bei dem Schüler in Bezug auf den Lernabschnitt der Notendatei
	 */
	public bemerkungen: ENMv2LeistungBemerkungen = new ENMv2LeistungBemerkungen();

	/**
	 * Die Informationen zu den Zentralen Prüfungen Klasse 10, sofern vorhanden - ansonsten null
	 */
	public zp10: List<ENMv2ZP10> = new ArrayList<ENMv2ZP10>();

	/**
	 * Die Informationen zu den Abschlüssen am Berufskolleg, sofern vorhanden - ansonsten null
	 */
	public bkabschluss: ENMv2BKAbschluss | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.v2.ENMv2Schueler';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.v2.ENMv2Schueler'].includes(name);
	}

	public static readonly class = new Class<ENMv2Schueler>('de.svws_nrw.core.data.enm.v2.ENMv2Schueler');

	public static transpilerFromJSON(json: string): ENMv2Schueler {
		const obj = JSON.parse(json) as Partial<ENMv2Schueler>;
		const result = new ENMv2Schueler();
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
				result.sprachenfolge.add(ENMv2Sprachenfolge.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lernabschnitt === undefined)
			throw new Error('invalid json format, missing attribute lernabschnitt');
		result.lernabschnitt = ENMv2Lernabschnitt.transpilerFromJSON(JSON.stringify(obj.lernabschnitt));
		if (obj.leistungsdaten !== undefined) {
			for (const elem of obj.leistungsdaten) {
				result.leistungsdaten.add(ENMv2Leistung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.ankreuzkompetenzen !== undefined) {
			for (const elem of obj.ankreuzkompetenzen) {
				result.ankreuzkompetenzen.add(ENMv2SchuelerAnkreuzkompetenz.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.bemerkungen === undefined)
			throw new Error('invalid json format, missing attribute bemerkungen');
		result.bemerkungen = ENMv2LeistungBemerkungen.transpilerFromJSON(JSON.stringify(obj.bemerkungen));
		if (obj.zp10 !== undefined) {
			for (const elem of obj.zp10) {
				result.zp10.add(ENMv2ZP10.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.bkabschluss = ((obj.bkabschluss === undefined) || (obj.bkabschluss === null)) ? null : ENMv2BKAbschluss.transpilerFromJSON(JSON.stringify(obj.bkabschluss));
		return result;
	}

	public static transpilerToJSON(obj: ENMv2Schueler): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"jahrgangID" : ' + obj.jahrgangID.toString() + ',';
		result += '"klasseID" : ' + obj.klasseID.toString() + ',';
		result += '"nachname" : ' + ((obj.nachname === null) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		result += '"geschlecht" : ' + ((obj.geschlecht === null) ? 'null' : JSON.stringify(obj.geschlecht)) + ',';
		result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		result += '"istZieldifferent" : ' + obj.istZieldifferent.toString() + ',';
		result += '"istDaZFoerderung" : ' + obj.istDaZFoerderung.toString() + ',';
		result += '"sprachenfolge" : [ ';
		for (let i = 0; i < obj.sprachenfolge.size(); i++) {
			const elem = obj.sprachenfolge.get(i);
			result += ENMv2Sprachenfolge.transpilerToJSON(elem);
			if (i < obj.sprachenfolge.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lernabschnitt" : ' + ENMv2Lernabschnitt.transpilerToJSON(obj.lernabschnitt) + ',';
		result += '"leistungsdaten" : [ ';
		for (let i = 0; i < obj.leistungsdaten.size(); i++) {
			const elem = obj.leistungsdaten.get(i);
			result += ENMv2Leistung.transpilerToJSON(elem);
			if (i < obj.leistungsdaten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"ankreuzkompetenzen" : [ ';
		for (let i = 0; i < obj.ankreuzkompetenzen.size(); i++) {
			const elem = obj.ankreuzkompetenzen.get(i);
			result += ENMv2SchuelerAnkreuzkompetenz.transpilerToJSON(elem);
			if (i < obj.ankreuzkompetenzen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"bemerkungen" : ' + ENMv2LeistungBemerkungen.transpilerToJSON(obj.bemerkungen) + ',';
		result += '"zp10" : [ ';
		for (let i = 0; i < obj.zp10.size(); i++) {
			const elem = obj.zp10.get(i);
			result += ENMv2ZP10.transpilerToJSON(elem);
			if (i < obj.zp10.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"bkabschluss" : ' + ((obj.bkabschluss === null) ? 'null' : ENMv2BKAbschluss.transpilerToJSON(obj.bkabschluss)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMv2Schueler>): string {
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
			result += '"nachname" : ' + ((obj.nachname === null) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		}
		if (obj.geschlecht !== undefined) {
			result += '"geschlecht" : ' + ((obj.geschlecht === null) ? 'null' : JSON.stringify(obj.geschlecht)) + ',';
		}
		if (obj.bilingualeSprache !== undefined) {
			result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
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
				result += ENMv2Sprachenfolge.transpilerToJSON(elem);
				if (i < obj.sprachenfolge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lernabschnitt !== undefined) {
			result += '"lernabschnitt" : ' + ENMv2Lernabschnitt.transpilerToJSON(obj.lernabschnitt) + ',';
		}
		if (obj.leistungsdaten !== undefined) {
			result += '"leistungsdaten" : [ ';
			for (let i = 0; i < obj.leistungsdaten.size(); i++) {
				const elem = obj.leistungsdaten.get(i);
				result += ENMv2Leistung.transpilerToJSON(elem);
				if (i < obj.leistungsdaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.ankreuzkompetenzen !== undefined) {
			result += '"ankreuzkompetenzen" : [ ';
			for (let i = 0; i < obj.ankreuzkompetenzen.size(); i++) {
				const elem = obj.ankreuzkompetenzen.get(i);
				result += ENMv2SchuelerAnkreuzkompetenz.transpilerToJSON(elem);
				if (i < obj.ankreuzkompetenzen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.bemerkungen !== undefined) {
			result += '"bemerkungen" : ' + ENMv2LeistungBemerkungen.transpilerToJSON(obj.bemerkungen) + ',';
		}
		if (obj.zp10 !== undefined) {
			result += '"zp10" : [ ';
			for (let i = 0; i < obj.zp10.size(); i++) {
				const elem = obj.zp10.get(i);
				result += ENMv2ZP10.transpilerToJSON(elem);
				if (i < obj.zp10.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.bkabschluss !== undefined) {
			result += '"bkabschluss" : ' + ((obj.bkabschluss === null) ? 'null' : ENMv2BKAbschluss.transpilerToJSON(obj.bkabschluss)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_v2_ENMv2Schueler(obj: unknown): ENMv2Schueler {
	return obj as ENMv2Schueler;
}
