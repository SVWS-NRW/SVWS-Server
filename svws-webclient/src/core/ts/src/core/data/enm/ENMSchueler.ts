import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { ENMLeistung, cast_de_nrw_schule_svws_core_data_enm_ENMLeistung } from '../../../core/data/enm/ENMLeistung';
import { ENMLernabschnitt, cast_de_nrw_schule_svws_core_data_enm_ENMLernabschnitt } from '../../../core/data/enm/ENMLernabschnitt';
import { ENMLeistungBemerkungen, cast_de_nrw_schule_svws_core_data_enm_ENMLeistungBemerkungen } from '../../../core/data/enm/ENMLeistungBemerkungen';
import { ENMZP10, cast_de_nrw_schule_svws_core_data_enm_ENMZP10 } from '../../../core/data/enm/ENMZP10';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { ENMBKAbschluss, cast_de_nrw_schule_svws_core_data_enm_ENMBKAbschluss } from '../../../core/data/enm/ENMBKAbschluss';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { ENMSprachenfolge, cast_de_nrw_schule_svws_core_data_enm_ENMSprachenfolge } from '../../../core/data/enm/ENMSprachenfolge';

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
	public sprachenfolge : Vector<ENMSprachenfolge> = new Vector();

	/**
	 * Informationen zum Lernabschnitt des Schülers in der Notendatei
	 */
	public lernabschnitt : ENMLernabschnitt = new ENMLernabschnitt();

	/**
	 * Die Leistungsdaten des Schülers in dem Lernabschnitt der Notendatei
	 */
	public readonly leistungsdaten : Vector<ENMLeistung> = new Vector();

	/**
	 * Die Bemerkungen bei dem Schüler in Bezug auf den Lernabschnitt der Notendatei
	 */
	public bemerkungen : ENMLeistungBemerkungen | null = new ENMLeistungBemerkungen();

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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMSchueler'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMSchueler {
		const obj = JSON.parse(json);
		const result = new ENMSchueler();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.jahrgangID === "undefined")
			 throw new Error('invalid json format, missing attribute jahrgangID');
		result.jahrgangID = obj.jahrgangID;
		if (typeof obj.klasseID === "undefined")
			 throw new Error('invalid json format, missing attribute klasseID');
		result.klasseID = obj.klasseID;
		result.nachname = typeof obj.nachname === "undefined" ? null : obj.nachname === null ? null : obj.nachname;
		result.vorname = typeof obj.vorname === "undefined" ? null : obj.vorname === null ? null : obj.vorname;
		result.geschlecht = typeof obj.geschlecht === "undefined" ? null : obj.geschlecht === null ? null : obj.geschlecht;
		result.bilingualeSprache = typeof obj.bilingualeSprache === "undefined" ? null : obj.bilingualeSprache === null ? null : obj.bilingualeSprache;
		if (typeof obj.istZieldifferent === "undefined")
			 throw new Error('invalid json format, missing attribute istZieldifferent');
		result.istZieldifferent = obj.istZieldifferent;
		if (typeof obj.istDaZFoerderung === "undefined")
			 throw new Error('invalid json format, missing attribute istDaZFoerderung');
		result.istDaZFoerderung = obj.istDaZFoerderung;
		if (!((obj.sprachenfolge === undefined) || (obj.sprachenfolge === null))) {
			for (const elem of obj.sprachenfolge) {
				result.sprachenfolge?.add(ENMSprachenfolge.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (typeof obj.lernabschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute lernabschnitt');
		result.lernabschnitt = ENMLernabschnitt.transpilerFromJSON(JSON.stringify(obj.lernabschnitt));
		if (!((obj.leistungsdaten === undefined) || (obj.leistungsdaten === null))) {
			for (const elem of obj.leistungsdaten) {
				result.leistungsdaten?.add(ENMLeistung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.bemerkungen = ((typeof obj.bemerkungen === "undefined") || (obj.bemerkungen === null)) ? null : ENMLeistungBemerkungen.transpilerFromJSON(JSON.stringify(obj.bemerkungen));
		result.zp10 = ((typeof obj.zp10 === "undefined") || (obj.zp10 === null)) ? null : ENMZP10.transpilerFromJSON(JSON.stringify(obj.zp10));
		result.bkabschluss = ((typeof obj.bkabschluss === "undefined") || (obj.bkabschluss === null)) ? null : ENMBKAbschluss.transpilerFromJSON(JSON.stringify(obj.bkabschluss));
		return result;
	}

	public static transpilerToJSON(obj : ENMSchueler) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"jahrgangID" : ' + obj.jahrgangID + ',';
		result += '"klasseID" : ' + obj.klasseID + ',';
		result += '"nachname" : ' + ((!obj.nachname) ? 'null' : '"' + obj.nachname + '"') + ',';
		result += '"vorname" : ' + ((!obj.vorname) ? 'null' : '"' + obj.vorname + '"') + ',';
		result += '"geschlecht" : ' + ((!obj.geschlecht) ? 'null' : '"' + obj.geschlecht + '"') + ',';
		result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : '"' + obj.bilingualeSprache + '"') + ',';
		result += '"istZieldifferent" : ' + obj.istZieldifferent + ',';
		result += '"istDaZFoerderung" : ' + obj.istDaZFoerderung + ',';
		if (!obj.sprachenfolge) {
			result += '"sprachenfolge" : []';
		} else {
			result += '"sprachenfolge" : [ ';
			for (let i = 0; i < obj.sprachenfolge.size(); i++) {
				const elem = obj.sprachenfolge.get(i);
				result += ENMSprachenfolge.transpilerToJSON(elem);
				if (i < obj.sprachenfolge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"lernabschnitt" : ' + ENMLernabschnitt.transpilerToJSON(obj.lernabschnitt) + ',';
		if (!obj.leistungsdaten) {
			result += '"leistungsdaten" : []';
		} else {
			result += '"leistungsdaten" : [ ';
			for (let i = 0; i < obj.leistungsdaten.size(); i++) {
				const elem = obj.leistungsdaten.get(i);
				result += ENMLeistung.transpilerToJSON(elem);
				if (i < obj.leistungsdaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : ENMLeistungBemerkungen.transpilerToJSON(obj.bemerkungen)) + ',';
		result += '"zp10" : ' + ((!obj.zp10) ? 'null' : ENMZP10.transpilerToJSON(obj.zp10)) + ',';
		result += '"bkabschluss" : ' + ((!obj.bkabschluss) ? 'null' : ENMBKAbschluss.transpilerToJSON(obj.bkabschluss)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMSchueler>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.jahrgangID !== "undefined") {
			result += '"jahrgangID" : ' + obj.jahrgangID + ',';
		}
		if (typeof obj.klasseID !== "undefined") {
			result += '"klasseID" : ' + obj.klasseID + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + ((!obj.nachname) ? 'null' : '"' + obj.nachname + '"') + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + ((!obj.vorname) ? 'null' : '"' + obj.vorname + '"') + ',';
		}
		if (typeof obj.geschlecht !== "undefined") {
			result += '"geschlecht" : ' + ((!obj.geschlecht) ? 'null' : '"' + obj.geschlecht + '"') + ',';
		}
		if (typeof obj.bilingualeSprache !== "undefined") {
			result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : '"' + obj.bilingualeSprache + '"') + ',';
		}
		if (typeof obj.istZieldifferent !== "undefined") {
			result += '"istZieldifferent" : ' + obj.istZieldifferent + ',';
		}
		if (typeof obj.istDaZFoerderung !== "undefined") {
			result += '"istDaZFoerderung" : ' + obj.istDaZFoerderung + ',';
		}
		if (typeof obj.sprachenfolge !== "undefined") {
			if (!obj.sprachenfolge) {
				result += '"sprachenfolge" : []';
			} else {
				result += '"sprachenfolge" : [ ';
				for (let i = 0; i < obj.sprachenfolge.size(); i++) {
					const elem = obj.sprachenfolge.get(i);
					result += ENMSprachenfolge.transpilerToJSON(elem);
					if (i < obj.sprachenfolge.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.lernabschnitt !== "undefined") {
			result += '"lernabschnitt" : ' + ENMLernabschnitt.transpilerToJSON(obj.lernabschnitt) + ',';
		}
		if (typeof obj.leistungsdaten !== "undefined") {
			if (!obj.leistungsdaten) {
				result += '"leistungsdaten" : []';
			} else {
				result += '"leistungsdaten" : [ ';
				for (let i = 0; i < obj.leistungsdaten.size(); i++) {
					const elem = obj.leistungsdaten.get(i);
					result += ENMLeistung.transpilerToJSON(elem);
					if (i < obj.leistungsdaten.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.bemerkungen !== "undefined") {
			result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : ENMLeistungBemerkungen.transpilerToJSON(obj.bemerkungen)) + ',';
		}
		if (typeof obj.zp10 !== "undefined") {
			result += '"zp10" : ' + ((!obj.zp10) ? 'null' : ENMZP10.transpilerToJSON(obj.zp10)) + ',';
		}
		if (typeof obj.bkabschluss !== "undefined") {
			result += '"bkabschluss" : ' + ((!obj.bkabschluss) ? 'null' : ENMBKAbschluss.transpilerToJSON(obj.bkabschluss)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMSchueler(obj : unknown) : ENMSchueler {
	return obj as ENMSchueler;
}
