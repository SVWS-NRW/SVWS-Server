import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchuelerLernabschnittListeEintrag extends JavaObject {

	public id : number = 0;

	public schuelerID : number = 0;

	public schuljahresabschnitt : number = 0;

	public wechselNr : Number | null = null;

	public istGewertet : boolean = true;

	public istWiederholung : boolean = false;

	public pruefungsOrdnung : String = "";

	public klassenID : number = -1;

	public klasse : String = "";

	public klasseStatistik : String = "";

	public jahrgangID : number = -1;

	public jahrgang : String = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schueler.SchuelerLernabschnittListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerLernabschnittListeEintrag {
		const obj = JSON.parse(json);
		const result = new SchuelerLernabschnittListeEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.schuelerID === "undefined")
			 throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (typeof obj.schuljahresabschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute schuljahresabschnitt');
		result.schuljahresabschnitt = obj.schuljahresabschnitt;
		result.wechselNr = typeof obj.wechselNr === "undefined" ? null : obj.wechselNr;
		if (typeof obj.istGewertet === "undefined")
			 throw new Error('invalid json format, missing attribute istGewertet');
		result.istGewertet = obj.istGewertet;
		if (typeof obj.istWiederholung === "undefined")
			 throw new Error('invalid json format, missing attribute istWiederholung');
		result.istWiederholung = obj.istWiederholung;
		if (typeof obj.pruefungsOrdnung === "undefined")
			 throw new Error('invalid json format, missing attribute pruefungsOrdnung');
		result.pruefungsOrdnung = obj.pruefungsOrdnung;
		if (typeof obj.klassenID === "undefined")
			 throw new Error('invalid json format, missing attribute klassenID');
		result.klassenID = obj.klassenID;
		if (typeof obj.klasse === "undefined")
			 throw new Error('invalid json format, missing attribute klasse');
		result.klasse = obj.klasse;
		if (typeof obj.klasseStatistik === "undefined")
			 throw new Error('invalid json format, missing attribute klasseStatistik');
		result.klasseStatistik = obj.klasseStatistik;
		if (typeof obj.jahrgangID === "undefined")
			 throw new Error('invalid json format, missing attribute jahrgangID');
		result.jahrgangID = obj.jahrgangID;
		if (typeof obj.jahrgang === "undefined")
			 throw new Error('invalid json format, missing attribute jahrgang');
		result.jahrgang = obj.jahrgang;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerLernabschnittListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"schuelerID" : ' + obj.schuelerID + ',';
		result += '"schuljahresabschnitt" : ' + obj.schuljahresabschnitt + ',';
		result += '"wechselNr" : ' + ((!obj.wechselNr) ? 'null' : obj.wechselNr.valueOf()) + ',';
		result += '"istGewertet" : ' + obj.istGewertet + ',';
		result += '"istWiederholung" : ' + obj.istWiederholung + ',';
		result += '"pruefungsOrdnung" : ' + '"' + obj.pruefungsOrdnung.valueOf() + '"' + ',';
		result += '"klassenID" : ' + obj.klassenID + ',';
		result += '"klasse" : ' + '"' + obj.klasse.valueOf() + '"' + ',';
		result += '"klasseStatistik" : ' + '"' + obj.klasseStatistik.valueOf() + '"' + ',';
		result += '"jahrgangID" : ' + obj.jahrgangID + ',';
		result += '"jahrgang" : ' + '"' + obj.jahrgang.valueOf() + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerLernabschnittListeEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.schuelerID !== "undefined") {
			result += '"schuelerID" : ' + obj.schuelerID + ',';
		}
		if (typeof obj.schuljahresabschnitt !== "undefined") {
			result += '"schuljahresabschnitt" : ' + obj.schuljahresabschnitt + ',';
		}
		if (typeof obj.wechselNr !== "undefined") {
			result += '"wechselNr" : ' + ((!obj.wechselNr) ? 'null' : obj.wechselNr.valueOf()) + ',';
		}
		if (typeof obj.istGewertet !== "undefined") {
			result += '"istGewertet" : ' + obj.istGewertet + ',';
		}
		if (typeof obj.istWiederholung !== "undefined") {
			result += '"istWiederholung" : ' + obj.istWiederholung + ',';
		}
		if (typeof obj.pruefungsOrdnung !== "undefined") {
			result += '"pruefungsOrdnung" : ' + '"' + obj.pruefungsOrdnung.valueOf() + '"' + ',';
		}
		if (typeof obj.klassenID !== "undefined") {
			result += '"klassenID" : ' + obj.klassenID + ',';
		}
		if (typeof obj.klasse !== "undefined") {
			result += '"klasse" : ' + '"' + obj.klasse.valueOf() + '"' + ',';
		}
		if (typeof obj.klasseStatistik !== "undefined") {
			result += '"klasseStatistik" : ' + '"' + obj.klasseStatistik.valueOf() + '"' + ',';
		}
		if (typeof obj.jahrgangID !== "undefined") {
			result += '"jahrgangID" : ' + obj.jahrgangID + ',';
		}
		if (typeof obj.jahrgang !== "undefined") {
			result += '"jahrgang" : ' + '"' + obj.jahrgang.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schueler_SchuelerLernabschnittListeEintrag(obj : unknown) : SchuelerLernabschnittListeEintrag {
	return obj as SchuelerLernabschnittListeEintrag;
}
