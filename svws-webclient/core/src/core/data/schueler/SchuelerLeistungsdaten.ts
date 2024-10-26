import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerLeistungsdaten extends JavaObject {

	/**
	 * Die ID der Leistungsdaten in der Datenbank.
	 */
	public id : number = -1;

	/**
	 * Die ID des Lernabschnitts des Schülers, zu dem diese Leistungsdaten gehören.
	 */
	public lernabschnittID : number = -1;

	/**
	 * Die ID des Faches, auf welches sich die Leistungsdaten beziehen.
	 */
	public fachID : number = -1;

	/**
	 * Die ID des Kurses, auf welches sich die Leistungsdaten beziehen - bei Klassen unterricht NULL.
	 */
	public kursID : number | null = null;

	/**
	 * Die spezielle Kursart des Schülers, sofern Kursunterricht vorliegt.
	 */
	public kursart : string | null = null;

	/**
	 * Gibt an, ob es sich bei der Fachbelegung um ein Abiturfach des Schülers handelt und wenn ja, um welches (NULL, 1, 2, 3, 4)
	 */
	public abifach : number | null = null;

	/**
	 * Gibt an, ob es sich um ein Fach der Zentralen Prüfungen 10 handelt oder um ein Fach der Zentralen Klausuren 10 (G8)
	 */
	public istZP10oderZK10 : boolean = false;

	/**
	 * Die Schulnummer, sofern es sich um Unterricht an einer kooperierenden Schule handelt, ansonsten NULL
	 */
	public koopSchule : number | null = null;

	/**
	 * Die ID des zugehörigen Fach-Lehrers.
	 */
	public lehrerID : number | null = null;

	/**
	 * Die Anzahl der Wochenstunden, welche das Fach unterrichtet wird.
	 */
	public wochenstunden : number = 0;

	/**
	 * Die ID der Zusatzkraft.
	 */
	public zusatzkraftID : number | null = null;

	/**
	 * Die Anzahl der Wochenstunden der Zusatzkraft.
	 */
	public zusatzkraftWochenstunden : number = 0;

	/**
	 * Gibt an, on das Fach auf dem Zeugnis erscheint oder nicht.
	 */
	public aufZeugnis : boolean = true;

	/**
	 * Das Kürzel der erteilten Note - es können auch Pseudonoten eingetragen werden (z.B. AT).
	 */
	public note : string | null = null;

	/**
	 * Das Kürzel der erteilten Qurtalsnote - es können auch Pseudonoten eingetragen werden (z.B. AT).
	 */
	public noteQuartal : string | null = null;

	/**
	 * Gibt an, ob die Leistung gemahnt wurde oder nicht.
	 */
	public istGemahnt : boolean = false;

	/**
	 * Das Datum, wann die Leistung gemahnt wurde oder null.
	 */
	public mahndatum : string | null = null;

	/**
	 * Gibt an, ob es sich um ein epochal unterrichtetes Fach handelt oder nicht.
	 */
	public istEpochal : boolean = false;

	/**
	 * Gibt an, ob es sich um eine Leistung handelt, welche über das "Holen von abgeschlossenen Fächern" in diesem Leistungsabschnitt bereitstehen. Wenn ja, dann ist hier der Jahrgang angegeben aus welchem die Daten geholt wurden
	 */
	public geholtJahrgangAbgeschlossen : string | null = null;

	/**
	 * Die Gewichtung für den allgemeinbildenden Teil (am Berufskolleg)
	 */
	public gewichtungAllgemeinbildend : number = 1;

	/**
	 * Die Berufsabschlussnote am Berufskolleg
	 */
	public noteBerufsabschluss : string | null = null;

	/**
	 * Der Text für die fachbezogene Lernentwicklung des Schülers
	 */
	public textFachbezogeneLernentwicklung : string = "";

	/**
	 * Die Facheigenschaft für die Lernstandberichte an Grundschulen (V = voller Umfang, R = reduzierter Umfang)
	 */
	public umfangLernstandsbericht : string = "";

	/**
	 * Die Gesamt-Anzahl der Fehlstunden für dieses Fach
	 */
	public fehlstundenGesamt : number = 0;

	/**
	 * Die Anzahl der unentschuldigten Fehlstunden für dieses Fach
	 */
	public fehlstundenUnentschuldigt : number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerLeistungsdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerLeistungsdaten'].includes(name);
	}

	public static class = new Class<SchuelerLeistungsdaten>('de.svws_nrw.core.data.schueler.SchuelerLeistungsdaten');

	public static transpilerFromJSON(json : string): SchuelerLeistungsdaten {
		const obj = JSON.parse(json) as Partial<SchuelerLeistungsdaten>;
		const result = new SchuelerLeistungsdaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.lernabschnittID === undefined)
			throw new Error('invalid json format, missing attribute lernabschnittID');
		result.lernabschnittID = obj.lernabschnittID;
		if (obj.fachID === undefined)
			throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.kursID = (obj.kursID === undefined) ? null : obj.kursID === null ? null : obj.kursID;
		result.kursart = (obj.kursart === undefined) ? null : obj.kursart === null ? null : obj.kursart;
		result.abifach = (obj.abifach === undefined) ? null : obj.abifach === null ? null : obj.abifach;
		if (obj.istZP10oderZK10 === undefined)
			throw new Error('invalid json format, missing attribute istZP10oderZK10');
		result.istZP10oderZK10 = obj.istZP10oderZK10;
		result.koopSchule = (obj.koopSchule === undefined) ? null : obj.koopSchule === null ? null : obj.koopSchule;
		result.lehrerID = (obj.lehrerID === undefined) ? null : obj.lehrerID === null ? null : obj.lehrerID;
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		result.zusatzkraftID = (obj.zusatzkraftID === undefined) ? null : obj.zusatzkraftID === null ? null : obj.zusatzkraftID;
		if (obj.zusatzkraftWochenstunden === undefined)
			throw new Error('invalid json format, missing attribute zusatzkraftWochenstunden');
		result.zusatzkraftWochenstunden = obj.zusatzkraftWochenstunden;
		if (obj.aufZeugnis === undefined)
			throw new Error('invalid json format, missing attribute aufZeugnis');
		result.aufZeugnis = obj.aufZeugnis;
		result.note = (obj.note === undefined) ? null : obj.note === null ? null : obj.note;
		result.noteQuartal = (obj.noteQuartal === undefined) ? null : obj.noteQuartal === null ? null : obj.noteQuartal;
		if (obj.istGemahnt === undefined)
			throw new Error('invalid json format, missing attribute istGemahnt');
		result.istGemahnt = obj.istGemahnt;
		result.mahndatum = (obj.mahndatum === undefined) ? null : obj.mahndatum === null ? null : obj.mahndatum;
		if (obj.istEpochal === undefined)
			throw new Error('invalid json format, missing attribute istEpochal');
		result.istEpochal = obj.istEpochal;
		result.geholtJahrgangAbgeschlossen = (obj.geholtJahrgangAbgeschlossen === undefined) ? null : obj.geholtJahrgangAbgeschlossen === null ? null : obj.geholtJahrgangAbgeschlossen;
		if (obj.gewichtungAllgemeinbildend === undefined)
			throw new Error('invalid json format, missing attribute gewichtungAllgemeinbildend');
		result.gewichtungAllgemeinbildend = obj.gewichtungAllgemeinbildend;
		result.noteBerufsabschluss = (obj.noteBerufsabschluss === undefined) ? null : obj.noteBerufsabschluss === null ? null : obj.noteBerufsabschluss;
		if (obj.textFachbezogeneLernentwicklung === undefined)
			throw new Error('invalid json format, missing attribute textFachbezogeneLernentwicklung');
		result.textFachbezogeneLernentwicklung = obj.textFachbezogeneLernentwicklung;
		if (obj.umfangLernstandsbericht === undefined)
			throw new Error('invalid json format, missing attribute umfangLernstandsbericht');
		result.umfangLernstandsbericht = obj.umfangLernstandsbericht;
		if (obj.fehlstundenGesamt === undefined)
			throw new Error('invalid json format, missing attribute fehlstundenGesamt');
		result.fehlstundenGesamt = obj.fehlstundenGesamt;
		if (obj.fehlstundenUnentschuldigt === undefined)
			throw new Error('invalid json format, missing attribute fehlstundenUnentschuldigt');
		result.fehlstundenUnentschuldigt = obj.fehlstundenUnentschuldigt;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerLeistungsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"lernabschnittID" : ' + obj.lernabschnittID.toString() + ',';
		result += '"fachID" : ' + obj.fachID.toString() + ',';
		result += '"kursID" : ' + ((obj.kursID === null) ? 'null' : obj.kursID.toString()) + ',';
		result += '"kursart" : ' + ((obj.kursart === null) ? 'null' : JSON.stringify(obj.kursart)) + ',';
		result += '"abifach" : ' + ((obj.abifach === null) ? 'null' : obj.abifach.toString()) + ',';
		result += '"istZP10oderZK10" : ' + obj.istZP10oderZK10.toString() + ',';
		result += '"koopSchule" : ' + ((obj.koopSchule === null) ? 'null' : obj.koopSchule.toString()) + ',';
		result += '"lehrerID" : ' + ((obj.lehrerID === null) ? 'null' : obj.lehrerID.toString()) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result += '"zusatzkraftID" : ' + ((obj.zusatzkraftID === null) ? 'null' : obj.zusatzkraftID.toString()) + ',';
		result += '"zusatzkraftWochenstunden" : ' + obj.zusatzkraftWochenstunden.toString() + ',';
		result += '"aufZeugnis" : ' + obj.aufZeugnis.toString() + ',';
		result += '"note" : ' + ((obj.note === null) ? 'null' : JSON.stringify(obj.note)) + ',';
		result += '"noteQuartal" : ' + ((obj.noteQuartal === null) ? 'null' : JSON.stringify(obj.noteQuartal)) + ',';
		result += '"istGemahnt" : ' + obj.istGemahnt.toString() + ',';
		result += '"mahndatum" : ' + ((obj.mahndatum === null) ? 'null' : JSON.stringify(obj.mahndatum)) + ',';
		result += '"istEpochal" : ' + obj.istEpochal.toString() + ',';
		result += '"geholtJahrgangAbgeschlossen" : ' + ((obj.geholtJahrgangAbgeschlossen === null) ? 'null' : JSON.stringify(obj.geholtJahrgangAbgeschlossen)) + ',';
		result += '"gewichtungAllgemeinbildend" : ' + obj.gewichtungAllgemeinbildend.toString() + ',';
		result += '"noteBerufsabschluss" : ' + ((obj.noteBerufsabschluss === null) ? 'null' : JSON.stringify(obj.noteBerufsabschluss)) + ',';
		result += '"textFachbezogeneLernentwicklung" : ' + JSON.stringify(obj.textFachbezogeneLernentwicklung) + ',';
		result += '"umfangLernstandsbericht" : ' + JSON.stringify(obj.umfangLernstandsbericht) + ',';
		result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt.toString() + ',';
		result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerLeistungsdaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.lernabschnittID !== undefined) {
			result += '"lernabschnittID" : ' + obj.lernabschnittID.toString() + ',';
		}
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + obj.fachID.toString() + ',';
		}
		if (obj.kursID !== undefined) {
			result += '"kursID" : ' + ((obj.kursID === null) ? 'null' : obj.kursID.toString()) + ',';
		}
		if (obj.kursart !== undefined) {
			result += '"kursart" : ' + ((obj.kursart === null) ? 'null' : JSON.stringify(obj.kursart)) + ',';
		}
		if (obj.abifach !== undefined) {
			result += '"abifach" : ' + ((obj.abifach === null) ? 'null' : obj.abifach.toString()) + ',';
		}
		if (obj.istZP10oderZK10 !== undefined) {
			result += '"istZP10oderZK10" : ' + obj.istZP10oderZK10.toString() + ',';
		}
		if (obj.koopSchule !== undefined) {
			result += '"koopSchule" : ' + ((obj.koopSchule === null) ? 'null' : obj.koopSchule.toString()) + ',';
		}
		if (obj.lehrerID !== undefined) {
			result += '"lehrerID" : ' + ((obj.lehrerID === null) ? 'null' : obj.lehrerID.toString()) + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		if (obj.zusatzkraftID !== undefined) {
			result += '"zusatzkraftID" : ' + ((obj.zusatzkraftID === null) ? 'null' : obj.zusatzkraftID.toString()) + ',';
		}
		if (obj.zusatzkraftWochenstunden !== undefined) {
			result += '"zusatzkraftWochenstunden" : ' + obj.zusatzkraftWochenstunden.toString() + ',';
		}
		if (obj.aufZeugnis !== undefined) {
			result += '"aufZeugnis" : ' + obj.aufZeugnis.toString() + ',';
		}
		if (obj.note !== undefined) {
			result += '"note" : ' + ((obj.note === null) ? 'null' : JSON.stringify(obj.note)) + ',';
		}
		if (obj.noteQuartal !== undefined) {
			result += '"noteQuartal" : ' + ((obj.noteQuartal === null) ? 'null' : JSON.stringify(obj.noteQuartal)) + ',';
		}
		if (obj.istGemahnt !== undefined) {
			result += '"istGemahnt" : ' + obj.istGemahnt.toString() + ',';
		}
		if (obj.mahndatum !== undefined) {
			result += '"mahndatum" : ' + ((obj.mahndatum === null) ? 'null' : JSON.stringify(obj.mahndatum)) + ',';
		}
		if (obj.istEpochal !== undefined) {
			result += '"istEpochal" : ' + obj.istEpochal.toString() + ',';
		}
		if (obj.geholtJahrgangAbgeschlossen !== undefined) {
			result += '"geholtJahrgangAbgeschlossen" : ' + ((obj.geholtJahrgangAbgeschlossen === null) ? 'null' : JSON.stringify(obj.geholtJahrgangAbgeschlossen)) + ',';
		}
		if (obj.gewichtungAllgemeinbildend !== undefined) {
			result += '"gewichtungAllgemeinbildend" : ' + obj.gewichtungAllgemeinbildend.toString() + ',';
		}
		if (obj.noteBerufsabschluss !== undefined) {
			result += '"noteBerufsabschluss" : ' + ((obj.noteBerufsabschluss === null) ? 'null' : JSON.stringify(obj.noteBerufsabschluss)) + ',';
		}
		if (obj.textFachbezogeneLernentwicklung !== undefined) {
			result += '"textFachbezogeneLernentwicklung" : ' + JSON.stringify(obj.textFachbezogeneLernentwicklung) + ',';
		}
		if (obj.umfangLernstandsbericht !== undefined) {
			result += '"umfangLernstandsbericht" : ' + JSON.stringify(obj.umfangLernstandsbericht) + ',';
		}
		if (obj.fehlstundenGesamt !== undefined) {
			result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt.toString() + ',';
		}
		if (obj.fehlstundenUnentschuldigt !== undefined) {
			result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerLeistungsdaten(obj : unknown) : SchuelerLeistungsdaten {
	return obj as SchuelerLeistungsdaten;
}
