import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import { ENMTeilleistung } from '../../../core/data/enm/ENMTeilleistung';
import type { List } from '../../../java/util/List';

export class ENMLeistung extends JavaObject {

	/**
	 * Die ID der Leistungsdaten des Schülers in der SVWS-DB (z.B. 307956)
	 */
	public id : number = 0;

	/**
	 * Die eindeutige ID der Lerngruppe, der der Schüler zugeordnet ist. (Klasse oder Kurs wird erst
	 *  in der Lerngruppe unterschieden!)
	 */
	public lerngruppenID : number = 0;

	/**
	 * Das Kürzel der Note, die vergeben wurde.
	 */
	public note : string | null = null;

	/**
	 * Der Zeitstempel der letzten Änderung an der erteilten Note
	 */
	public tsNote : string | null = null;

	/**
	 * Das Kürzel der Quartals-Note, die vergeben wurde.
	 */
	public noteQuartal : string | null = null;

	/**
	 * Der Zeitstempel der letzten Änderung an der erteilten Quartals-Note
	 */
	public tsNoteQuartal : string | null = null;

	/**
	 * Gibt bei Oberstufenkursen an, ob das Fach schriftlich belegt wurde oder nicht.
	 */
	public istSchriftlich : boolean | null = null;

	/**
	 * Gibt an, ob es sich um ein Abiturfach handelt (1,2,3 oder 4) oder nicht (null)
	 */
	public abiturfach : number | null = null;

	/**
	 * Gibt die Anzahl der gesamten Fehlstunden an, sofern diese fachbezogen ermittelt werden.
	 */
	public fehlstundenFach : number | null = null;

	/**
	 * Der Zeitstempel der letzten Änderung an Anzahl der gesamten Fehlstunden an, sofern diese fachbezogen ermittelt werden
	 */
	public tsFehlstundenFach : string | null = null;

	/**
	 * Gibt die Anzahl der unentschuldigten Fehlstunden an, sofern diese fachbezogen ermittelt werden.
	 */
	public fehlstundenUnentschuldigtFach : number | null = null;

	/**
	 * Der Zeitstempel der letzten Änderung an Anzahl der unentschuldigten Fehlstunden an, sofern diese fachbezogen ermittelt werden
	 */
	public tsFehlstundenUnentschuldigtFach : string | null = null;

	/**
	 * Die fachbezogenen Bemerkungen bzw. das Thema bei Projektkursen
	 */
	public fachbezogeneBemerkungen : string | null = null;

	/**
	 * Der Zeitstempel der letzten Änderung an Anzahl den fachbezogenen Bemerkungen bzw. dem Thema bei Projektkursen
	 */
	public tsFachbezogeneBemerkungen : string | null = null;

	/**
	 * Die Kurszuweisung, die auf dem Zeugnis erscheinen soll für den nächsten Kursabschnitt (z.B. E oder G-Kurs, z.B. an der Gesamtschule)
	 */
	public neueZuweisungKursart : string | null = null;

	/**
	 * Gibt an, ob ein Fach gemahnt wurde oder nicht.
	 */
	public istGemahnt : boolean | null = null;

	/**
	 * Der Zeitstempel, wann gesetzt wurde, ob die Leistung gemahnt wurde
	 */
	public tsIstGemahnt : string | null = null;

	/**
	 * Das Mahndatum bei erfolgter Mahnung.
	 */
	public mahndatum : string | null = null;

	/**
	 * Die Teilleistungen, sofern welche vordefiniert sind.
	 */
	public teilleistungen : List<ENMTeilleistung> = new ArrayList();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMLeistung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMLeistung'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMLeistung {
		const obj = JSON.parse(json);
		const result = new ENMLeistung();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.lerngruppenID === "undefined")
			 throw new Error('invalid json format, missing attribute lerngruppenID');
		result.lerngruppenID = obj.lerngruppenID;
		result.note = typeof obj.note === "undefined" ? null : obj.note === null ? null : obj.note;
		result.tsNote = typeof obj.tsNote === "undefined" ? null : obj.tsNote === null ? null : obj.tsNote;
		result.noteQuartal = typeof obj.noteQuartal === "undefined" ? null : obj.noteQuartal === null ? null : obj.noteQuartal;
		result.tsNoteQuartal = typeof obj.tsNoteQuartal === "undefined" ? null : obj.tsNoteQuartal === null ? null : obj.tsNoteQuartal;
		result.istSchriftlich = typeof obj.istSchriftlich === "undefined" ? null : obj.istSchriftlich === null ? null : obj.istSchriftlich;
		result.abiturfach = typeof obj.abiturfach === "undefined" ? null : obj.abiturfach === null ? null : obj.abiturfach;
		result.fehlstundenFach = typeof obj.fehlstundenFach === "undefined" ? null : obj.fehlstundenFach === null ? null : obj.fehlstundenFach;
		result.tsFehlstundenFach = typeof obj.tsFehlstundenFach === "undefined" ? null : obj.tsFehlstundenFach === null ? null : obj.tsFehlstundenFach;
		result.fehlstundenUnentschuldigtFach = typeof obj.fehlstundenUnentschuldigtFach === "undefined" ? null : obj.fehlstundenUnentschuldigtFach === null ? null : obj.fehlstundenUnentschuldigtFach;
		result.tsFehlstundenUnentschuldigtFach = typeof obj.tsFehlstundenUnentschuldigtFach === "undefined" ? null : obj.tsFehlstundenUnentschuldigtFach === null ? null : obj.tsFehlstundenUnentschuldigtFach;
		result.fachbezogeneBemerkungen = typeof obj.fachbezogeneBemerkungen === "undefined" ? null : obj.fachbezogeneBemerkungen === null ? null : obj.fachbezogeneBemerkungen;
		result.tsFachbezogeneBemerkungen = typeof obj.tsFachbezogeneBemerkungen === "undefined" ? null : obj.tsFachbezogeneBemerkungen === null ? null : obj.tsFachbezogeneBemerkungen;
		result.neueZuweisungKursart = typeof obj.neueZuweisungKursart === "undefined" ? null : obj.neueZuweisungKursart === null ? null : obj.neueZuweisungKursart;
		result.istGemahnt = typeof obj.istGemahnt === "undefined" ? null : obj.istGemahnt === null ? null : obj.istGemahnt;
		result.tsIstGemahnt = typeof obj.tsIstGemahnt === "undefined" ? null : obj.tsIstGemahnt === null ? null : obj.tsIstGemahnt;
		result.mahndatum = typeof obj.mahndatum === "undefined" ? null : obj.mahndatum === null ? null : obj.mahndatum;
		if ((obj.teilleistungen !== undefined) && (obj.teilleistungen !== null)) {
			for (const elem of obj.teilleistungen) {
				result.teilleistungen?.add(ENMTeilleistung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : ENMLeistung) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"lerngruppenID" : ' + obj.lerngruppenID + ',';
		result += '"note" : ' + ((!obj.note) ? 'null' : JSON.stringify(obj.note)) + ',';
		result += '"tsNote" : ' + ((!obj.tsNote) ? 'null' : JSON.stringify(obj.tsNote)) + ',';
		result += '"noteQuartal" : ' + ((!obj.noteQuartal) ? 'null' : JSON.stringify(obj.noteQuartal)) + ',';
		result += '"tsNoteQuartal" : ' + ((!obj.tsNoteQuartal) ? 'null' : JSON.stringify(obj.tsNoteQuartal)) + ',';
		result += '"istSchriftlich" : ' + ((!obj.istSchriftlich) ? 'null' : obj.istSchriftlich) + ',';
		result += '"abiturfach" : ' + ((!obj.abiturfach) ? 'null' : obj.abiturfach) + ',';
		result += '"fehlstundenFach" : ' + ((!obj.fehlstundenFach) ? 'null' : obj.fehlstundenFach) + ',';
		result += '"tsFehlstundenFach" : ' + ((!obj.tsFehlstundenFach) ? 'null' : JSON.stringify(obj.tsFehlstundenFach)) + ',';
		result += '"fehlstundenUnentschuldigtFach" : ' + ((!obj.fehlstundenUnentschuldigtFach) ? 'null' : obj.fehlstundenUnentschuldigtFach) + ',';
		result += '"tsFehlstundenUnentschuldigtFach" : ' + ((!obj.tsFehlstundenUnentschuldigtFach) ? 'null' : JSON.stringify(obj.tsFehlstundenUnentschuldigtFach)) + ',';
		result += '"fachbezogeneBemerkungen" : ' + ((!obj.fachbezogeneBemerkungen) ? 'null' : JSON.stringify(obj.fachbezogeneBemerkungen)) + ',';
		result += '"tsFachbezogeneBemerkungen" : ' + ((!obj.tsFachbezogeneBemerkungen) ? 'null' : JSON.stringify(obj.tsFachbezogeneBemerkungen)) + ',';
		result += '"neueZuweisungKursart" : ' + ((!obj.neueZuweisungKursart) ? 'null' : JSON.stringify(obj.neueZuweisungKursart)) + ',';
		result += '"istGemahnt" : ' + ((!obj.istGemahnt) ? 'null' : obj.istGemahnt) + ',';
		result += '"tsIstGemahnt" : ' + ((!obj.tsIstGemahnt) ? 'null' : JSON.stringify(obj.tsIstGemahnt)) + ',';
		result += '"mahndatum" : ' + ((!obj.mahndatum) ? 'null' : JSON.stringify(obj.mahndatum)) + ',';
		if (!obj.teilleistungen) {
			result += '"teilleistungen" : []';
		} else {
			result += '"teilleistungen" : [ ';
			for (let i = 0; i < obj.teilleistungen.size(); i++) {
				const elem = obj.teilleistungen.get(i);
				result += ENMTeilleistung.transpilerToJSON(elem);
				if (i < obj.teilleistungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMLeistung>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.lerngruppenID !== "undefined") {
			result += '"lerngruppenID" : ' + obj.lerngruppenID + ',';
		}
		if (typeof obj.note !== "undefined") {
			result += '"note" : ' + ((!obj.note) ? 'null' : JSON.stringify(obj.note)) + ',';
		}
		if (typeof obj.tsNote !== "undefined") {
			result += '"tsNote" : ' + ((!obj.tsNote) ? 'null' : JSON.stringify(obj.tsNote)) + ',';
		}
		if (typeof obj.noteQuartal !== "undefined") {
			result += '"noteQuartal" : ' + ((!obj.noteQuartal) ? 'null' : JSON.stringify(obj.noteQuartal)) + ',';
		}
		if (typeof obj.tsNoteQuartal !== "undefined") {
			result += '"tsNoteQuartal" : ' + ((!obj.tsNoteQuartal) ? 'null' : JSON.stringify(obj.tsNoteQuartal)) + ',';
		}
		if (typeof obj.istSchriftlich !== "undefined") {
			result += '"istSchriftlich" : ' + ((!obj.istSchriftlich) ? 'null' : obj.istSchriftlich) + ',';
		}
		if (typeof obj.abiturfach !== "undefined") {
			result += '"abiturfach" : ' + ((!obj.abiturfach) ? 'null' : obj.abiturfach) + ',';
		}
		if (typeof obj.fehlstundenFach !== "undefined") {
			result += '"fehlstundenFach" : ' + ((!obj.fehlstundenFach) ? 'null' : obj.fehlstundenFach) + ',';
		}
		if (typeof obj.tsFehlstundenFach !== "undefined") {
			result += '"tsFehlstundenFach" : ' + ((!obj.tsFehlstundenFach) ? 'null' : JSON.stringify(obj.tsFehlstundenFach)) + ',';
		}
		if (typeof obj.fehlstundenUnentschuldigtFach !== "undefined") {
			result += '"fehlstundenUnentschuldigtFach" : ' + ((!obj.fehlstundenUnentschuldigtFach) ? 'null' : obj.fehlstundenUnentschuldigtFach) + ',';
		}
		if (typeof obj.tsFehlstundenUnentschuldigtFach !== "undefined") {
			result += '"tsFehlstundenUnentschuldigtFach" : ' + ((!obj.tsFehlstundenUnentschuldigtFach) ? 'null' : JSON.stringify(obj.tsFehlstundenUnentschuldigtFach)) + ',';
		}
		if (typeof obj.fachbezogeneBemerkungen !== "undefined") {
			result += '"fachbezogeneBemerkungen" : ' + ((!obj.fachbezogeneBemerkungen) ? 'null' : JSON.stringify(obj.fachbezogeneBemerkungen)) + ',';
		}
		if (typeof obj.tsFachbezogeneBemerkungen !== "undefined") {
			result += '"tsFachbezogeneBemerkungen" : ' + ((!obj.tsFachbezogeneBemerkungen) ? 'null' : JSON.stringify(obj.tsFachbezogeneBemerkungen)) + ',';
		}
		if (typeof obj.neueZuweisungKursart !== "undefined") {
			result += '"neueZuweisungKursart" : ' + ((!obj.neueZuweisungKursart) ? 'null' : JSON.stringify(obj.neueZuweisungKursart)) + ',';
		}
		if (typeof obj.istGemahnt !== "undefined") {
			result += '"istGemahnt" : ' + ((!obj.istGemahnt) ? 'null' : obj.istGemahnt) + ',';
		}
		if (typeof obj.tsIstGemahnt !== "undefined") {
			result += '"tsIstGemahnt" : ' + ((!obj.tsIstGemahnt) ? 'null' : JSON.stringify(obj.tsIstGemahnt)) + ',';
		}
		if (typeof obj.mahndatum !== "undefined") {
			result += '"mahndatum" : ' + ((!obj.mahndatum) ? 'null' : JSON.stringify(obj.mahndatum)) + ',';
		}
		if (typeof obj.teilleistungen !== "undefined") {
			if (!obj.teilleistungen) {
				result += '"teilleistungen" : []';
			} else {
				result += '"teilleistungen" : [ ';
				for (let i = 0; i < obj.teilleistungen.size(); i++) {
					const elem = obj.teilleistungen.get(i);
					result += ENMTeilleistung.transpilerToJSON(elem);
					if (i < obj.teilleistungen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMLeistung(obj : unknown) : ENMLeistung {
	return obj as ENMLeistung;
}
