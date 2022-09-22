import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchuelerLeistungsdaten extends JavaObject {

	public id : number = -1;

	public lernabschnittID : number = -1;

	public fachID : number = -1;

	public kursID : Number | null = null;

	public kursart : String | null = null;

	public abifach : String | null = null;

	public istZP10oderZK10 : boolean = false;

	public koopSchule : Number | null = null;

	public lehrerID : Number | null = null;

	public wochenstunden : number = 0;

	public zusatzkraftID : Number | null = null;

	public zusatzkraftWochenstunden : number = 0;

	public aufZeugnis : boolean = true;

	public note : String | null = null;

	public istGemahnt : boolean = false;

	public Mahndatum : String | null = null;

	public istEpochal : boolean = false;

	public geholtJahrgangAbgeschlossen : String | null = null;

	public gewichtungAllgemeinbildend : number = 1;

	public noteBerufsabschluss : String | null = null;

	public textFachbezogeneLernentwicklung : String = "";

	public umfangLernstandsbericht : String = "";

	public fehlstundenGesamt : number = 0;

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
		result.kursID = typeof obj.kursID === "undefined" ? null : obj.kursID;
		result.kursart = typeof obj.kursart === "undefined" ? null : obj.kursart;
		result.abifach = typeof obj.abifach === "undefined" ? null : obj.abifach;
		if (typeof obj.istZP10oderZK10 === "undefined")
			 throw new Error('invalid json format, missing attribute istZP10oderZK10');
		result.istZP10oderZK10 = obj.istZP10oderZK10;
		result.koopSchule = typeof obj.koopSchule === "undefined" ? null : obj.koopSchule;
		result.lehrerID = typeof obj.lehrerID === "undefined" ? null : obj.lehrerID;
		if (typeof obj.wochenstunden === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		result.zusatzkraftID = typeof obj.zusatzkraftID === "undefined" ? null : obj.zusatzkraftID;
		if (typeof obj.zusatzkraftWochenstunden === "undefined")
			 throw new Error('invalid json format, missing attribute zusatzkraftWochenstunden');
		result.zusatzkraftWochenstunden = obj.zusatzkraftWochenstunden;
		if (typeof obj.aufZeugnis === "undefined")
			 throw new Error('invalid json format, missing attribute aufZeugnis');
		result.aufZeugnis = obj.aufZeugnis;
		result.note = typeof obj.note === "undefined" ? null : obj.note;
		if (typeof obj.istGemahnt === "undefined")
			 throw new Error('invalid json format, missing attribute istGemahnt');
		result.istGemahnt = obj.istGemahnt;
		result.Mahndatum = typeof obj.Mahndatum === "undefined" ? null : obj.Mahndatum;
		if (typeof obj.istEpochal === "undefined")
			 throw new Error('invalid json format, missing attribute istEpochal');
		result.istEpochal = obj.istEpochal;
		result.geholtJahrgangAbgeschlossen = typeof obj.geholtJahrgangAbgeschlossen === "undefined" ? null : obj.geholtJahrgangAbgeschlossen;
		if (typeof obj.gewichtungAllgemeinbildend === "undefined")
			 throw new Error('invalid json format, missing attribute gewichtungAllgemeinbildend');
		result.gewichtungAllgemeinbildend = obj.gewichtungAllgemeinbildend;
		result.noteBerufsabschluss = typeof obj.noteBerufsabschluss === "undefined" ? null : obj.noteBerufsabschluss;
		if (typeof obj.textFachbezogeneLernentwicklung === "undefined")
			 throw new Error('invalid json format, missing attribute textFachbezogeneLernentwicklung');
		result.textFachbezogeneLernentwicklung = obj.textFachbezogeneLernentwicklung;
		if (typeof obj.umfangLernstandsbericht === "undefined")
			 throw new Error('invalid json format, missing attribute umfangLernstandsbericht');
		result.umfangLernstandsbericht = obj.umfangLernstandsbericht;
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
