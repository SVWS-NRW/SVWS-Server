import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

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
	public kursID : Number | null = null;

	/**
	 * Die spezielle Kursart des Schülers, sofern Kursunterricht vorliegt. 
	 */
	public kursart : String | null = null;

	/**
	 * Gibt an, ob es sich bei der Fachbelegung um ein Abiturfach des Schülers handelt und wenn ja, um welches (NULL, 1, 2, 3, 4) 
	 */
	public abifach : String | null = null;

	/**
	 * Gibt an, ob es sich um ein Fach der Zentralen Prüfungen 10 handelt oder um ein Fach der Zentralen Klausuren 10 (G8) 
	 */
	public istZP10oderZK10 : boolean = false;

	/**
	 * Die Schulnummer, sofern es sich um Unterricht an einer kooperierenden Schule handelt, ansonsten NULL 
	 */
	public koopSchule : Number | null = null;

	/**
	 * Die ID des zugehörigen Fach-Lehrers. 
	 */
	public lehrerID : Number | null = null;

	/**
	 * Die Anzahl der Wochenstunden, welche das Fach unterrichtet wird. 
	 */
	public wochenstunden : number = 0;

	/**
	 * Die ID der Zusatzkraft. 
	 */
	public zusatzkraftID : Number | null = null;

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
	public note : String | null = null;

	/**
	 * Gibt an, ob die Leistung gemahnt wurde oder nicht. 
	 */
	public istGemahnt : boolean = false;

	/**
	 * Das Datum, wann die Leistung gemahnt wurde oder null. 
	 */
	public Mahndatum : String | null = null;

	/**
	 * Gibt an, ob es sich um ein epochal unterrichtetes Fach handelt oder nicht. 
	 */
	public istEpochal : boolean = false;

	/**
	 * Gibt an, ob es sich um eine Leistung handelt, welche über das "Holen von abgeschlossenen Fächern" in diesem Leistungsabschnitt bereitstehen. Wenn ja, dann ist hier der Jahrgang angegeben aus welchem die Daten geholt wurden 
	 */
	public geholtJahrgangAbgeschlossen : String | null = null;

	/**
	 * Die Gewichtung für den allgemeinbildenden Teil (am Berufskolleg) 
	 */
	public gewichtungAllgemeinbildend : number = 1;

	/**
	 * Die Berufsabschlussnote am Berufskolleg 
	 */
	public noteBerufsabschluss : String | null = null;

	/**
	 * Der Text für die fachbezogene Lernentwicklung des Schülers 
	 */
	public textFachbezogeneLernentwicklung : String = "";

	/**
	 * Die Facheigenschaft für die Lernstandberichte an Grundschulen (V = voller Umfang, R = reduzierter Umfang) 
	 */
	public umfangLernstandsbericht : String = "";

	/**
	 * Die Gesamt-Anzahl der Fehlstunden für dieses Fach 
	 */
	public fehlstundenGesamt : number = 0;

	/**
	 * Die Anzahl der unentschuldigten Fehlstunden für dieses Fach 
	 */
	public fehlstundenUnentschuldigt : number = 0;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schueler.SchuelerLeistungsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerLeistungsdaten {
		const obj = JSON.parse(json);
		const result = new SchuelerLeistungsdaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.lernabschnittID === "undefined")
			 throw new Error('invalid json format, missing attribute lernabschnittID');
		result.lernabschnittID = obj.lernabschnittID;
		if (typeof obj.fachID === "undefined")
			 throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.kursID = typeof obj.kursID === "undefined" ? null : obj.kursID === null ? null : Number(obj.kursID);
		result.kursart = typeof obj.kursart === "undefined" ? null : obj.kursart === null ? null : String(obj.kursart);
		result.abifach = typeof obj.abifach === "undefined" ? null : obj.abifach === null ? null : String(obj.abifach);
		if (typeof obj.istZP10oderZK10 === "undefined")
			 throw new Error('invalid json format, missing attribute istZP10oderZK10');
		result.istZP10oderZK10 = obj.istZP10oderZK10;
		result.koopSchule = typeof obj.koopSchule === "undefined" ? null : obj.koopSchule === null ? null : Number(obj.koopSchule);
		result.lehrerID = typeof obj.lehrerID === "undefined" ? null : obj.lehrerID === null ? null : Number(obj.lehrerID);
		if (typeof obj.wochenstunden === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		result.zusatzkraftID = typeof obj.zusatzkraftID === "undefined" ? null : obj.zusatzkraftID === null ? null : Number(obj.zusatzkraftID);
		if (typeof obj.zusatzkraftWochenstunden === "undefined")
			 throw new Error('invalid json format, missing attribute zusatzkraftWochenstunden');
		result.zusatzkraftWochenstunden = obj.zusatzkraftWochenstunden;
		if (typeof obj.aufZeugnis === "undefined")
			 throw new Error('invalid json format, missing attribute aufZeugnis');
		result.aufZeugnis = obj.aufZeugnis;
		result.note = typeof obj.note === "undefined" ? null : obj.note === null ? null : String(obj.note);
		if (typeof obj.istGemahnt === "undefined")
			 throw new Error('invalid json format, missing attribute istGemahnt');
		result.istGemahnt = obj.istGemahnt;
		result.Mahndatum = typeof obj.Mahndatum === "undefined" ? null : obj.Mahndatum === null ? null : String(obj.Mahndatum);
		if (typeof obj.istEpochal === "undefined")
			 throw new Error('invalid json format, missing attribute istEpochal');
		result.istEpochal = obj.istEpochal;
		result.geholtJahrgangAbgeschlossen = typeof obj.geholtJahrgangAbgeschlossen === "undefined" ? null : obj.geholtJahrgangAbgeschlossen === null ? null : String(obj.geholtJahrgangAbgeschlossen);
		if (typeof obj.gewichtungAllgemeinbildend === "undefined")
			 throw new Error('invalid json format, missing attribute gewichtungAllgemeinbildend');
		result.gewichtungAllgemeinbildend = obj.gewichtungAllgemeinbildend;
		result.noteBerufsabschluss = typeof obj.noteBerufsabschluss === "undefined" ? null : obj.noteBerufsabschluss === null ? null : String(obj.noteBerufsabschluss);
		if (typeof obj.textFachbezogeneLernentwicklung === "undefined")
			 throw new Error('invalid json format, missing attribute textFachbezogeneLernentwicklung');
		result.textFachbezogeneLernentwicklung = String(obj.textFachbezogeneLernentwicklung);
		if (typeof obj.umfangLernstandsbericht === "undefined")
			 throw new Error('invalid json format, missing attribute umfangLernstandsbericht');
		result.umfangLernstandsbericht = String(obj.umfangLernstandsbericht);
		if (typeof obj.fehlstundenGesamt === "undefined")
			 throw new Error('invalid json format, missing attribute fehlstundenGesamt');
		result.fehlstundenGesamt = obj.fehlstundenGesamt;
		if (typeof obj.fehlstundenUnentschuldigt === "undefined")
			 throw new Error('invalid json format, missing attribute fehlstundenUnentschuldigt');
		result.fehlstundenUnentschuldigt = obj.fehlstundenUnentschuldigt;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerLeistungsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"lernabschnittID" : ' + obj.lernabschnittID + ',';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"kursID" : ' + ((!obj.kursID) ? 'null' : obj.kursID.valueOf()) + ',';
		result += '"kursart" : ' + ((!obj.kursart) ? 'null' : '"' + obj.kursart.valueOf() + '"') + ',';
		result += '"abifach" : ' + ((!obj.abifach) ? 'null' : '"' + obj.abifach.valueOf() + '"') + ',';
		result += '"istZP10oderZK10" : ' + obj.istZP10oderZK10 + ',';
		result += '"koopSchule" : ' + ((!obj.koopSchule) ? 'null' : obj.koopSchule.valueOf()) + ',';
		result += '"lehrerID" : ' + ((!obj.lehrerID) ? 'null' : obj.lehrerID.valueOf()) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		result += '"zusatzkraftID" : ' + ((!obj.zusatzkraftID) ? 'null' : obj.zusatzkraftID.valueOf()) + ',';
		result += '"zusatzkraftWochenstunden" : ' + obj.zusatzkraftWochenstunden + ',';
		result += '"aufZeugnis" : ' + obj.aufZeugnis + ',';
		result += '"note" : ' + ((!obj.note) ? 'null' : '"' + obj.note.valueOf() + '"') + ',';
		result += '"istGemahnt" : ' + obj.istGemahnt + ',';
		result += '"Mahndatum" : ' + ((!obj.Mahndatum) ? 'null' : '"' + obj.Mahndatum.valueOf() + '"') + ',';
		result += '"istEpochal" : ' + obj.istEpochal + ',';
		result += '"geholtJahrgangAbgeschlossen" : ' + ((!obj.geholtJahrgangAbgeschlossen) ? 'null' : '"' + obj.geholtJahrgangAbgeschlossen.valueOf() + '"') + ',';
		result += '"gewichtungAllgemeinbildend" : ' + obj.gewichtungAllgemeinbildend + ',';
		result += '"noteBerufsabschluss" : ' + ((!obj.noteBerufsabschluss) ? 'null' : '"' + obj.noteBerufsabschluss.valueOf() + '"') + ',';
		result += '"textFachbezogeneLernentwicklung" : ' + '"' + obj.textFachbezogeneLernentwicklung.valueOf() + '"' + ',';
		result += '"umfangLernstandsbericht" : ' + '"' + obj.umfangLernstandsbericht.valueOf() + '"' + ',';
		result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt + ',';
		result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerLeistungsdaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.lernabschnittID !== "undefined") {
			result += '"lernabschnittID" : ' + obj.lernabschnittID + ',';
		}
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + obj.fachID + ',';
		}
		if (typeof obj.kursID !== "undefined") {
			result += '"kursID" : ' + ((!obj.kursID) ? 'null' : obj.kursID.valueOf()) + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + ((!obj.kursart) ? 'null' : '"' + obj.kursart.valueOf() + '"') + ',';
		}
		if (typeof obj.abifach !== "undefined") {
			result += '"abifach" : ' + ((!obj.abifach) ? 'null' : '"' + obj.abifach.valueOf() + '"') + ',';
		}
		if (typeof obj.istZP10oderZK10 !== "undefined") {
			result += '"istZP10oderZK10" : ' + obj.istZP10oderZK10 + ',';
		}
		if (typeof obj.koopSchule !== "undefined") {
			result += '"koopSchule" : ' + ((!obj.koopSchule) ? 'null' : obj.koopSchule.valueOf()) + ',';
		}
		if (typeof obj.lehrerID !== "undefined") {
			result += '"lehrerID" : ' + ((!obj.lehrerID) ? 'null' : obj.lehrerID.valueOf()) + ',';
		}
		if (typeof obj.wochenstunden !== "undefined") {
			result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		}
		if (typeof obj.zusatzkraftID !== "undefined") {
			result += '"zusatzkraftID" : ' + ((!obj.zusatzkraftID) ? 'null' : obj.zusatzkraftID.valueOf()) + ',';
		}
		if (typeof obj.zusatzkraftWochenstunden !== "undefined") {
			result += '"zusatzkraftWochenstunden" : ' + obj.zusatzkraftWochenstunden + ',';
		}
		if (typeof obj.aufZeugnis !== "undefined") {
			result += '"aufZeugnis" : ' + obj.aufZeugnis + ',';
		}
		if (typeof obj.note !== "undefined") {
			result += '"note" : ' + ((!obj.note) ? 'null' : '"' + obj.note.valueOf() + '"') + ',';
		}
		if (typeof obj.istGemahnt !== "undefined") {
			result += '"istGemahnt" : ' + obj.istGemahnt + ',';
		}
		if (typeof obj.Mahndatum !== "undefined") {
			result += '"Mahndatum" : ' + ((!obj.Mahndatum) ? 'null' : '"' + obj.Mahndatum.valueOf() + '"') + ',';
		}
		if (typeof obj.istEpochal !== "undefined") {
			result += '"istEpochal" : ' + obj.istEpochal + ',';
		}
		if (typeof obj.geholtJahrgangAbgeschlossen !== "undefined") {
			result += '"geholtJahrgangAbgeschlossen" : ' + ((!obj.geholtJahrgangAbgeschlossen) ? 'null' : '"' + obj.geholtJahrgangAbgeschlossen.valueOf() + '"') + ',';
		}
		if (typeof obj.gewichtungAllgemeinbildend !== "undefined") {
			result += '"gewichtungAllgemeinbildend" : ' + obj.gewichtungAllgemeinbildend + ',';
		}
		if (typeof obj.noteBerufsabschluss !== "undefined") {
			result += '"noteBerufsabschluss" : ' + ((!obj.noteBerufsabschluss) ? 'null' : '"' + obj.noteBerufsabschluss.valueOf() + '"') + ',';
		}
		if (typeof obj.textFachbezogeneLernentwicklung !== "undefined") {
			result += '"textFachbezogeneLernentwicklung" : ' + '"' + obj.textFachbezogeneLernentwicklung.valueOf() + '"' + ',';
		}
		if (typeof obj.umfangLernstandsbericht !== "undefined") {
			result += '"umfangLernstandsbericht" : ' + '"' + obj.umfangLernstandsbericht.valueOf() + '"' + ',';
		}
		if (typeof obj.fehlstundenGesamt !== "undefined") {
			result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt + ',';
		}
		if (typeof obj.fehlstundenUnentschuldigt !== "undefined") {
			result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schueler_SchuelerLeistungsdaten(obj : unknown) : SchuelerLeistungsdaten {
	return obj as SchuelerLeistungsdaten;
}
