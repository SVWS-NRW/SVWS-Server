import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class StundenplanListeEintrag extends JavaObject {

	public id : number = -1;

	public bezeichnung : String = "";

	public idSchuljahresabschnitt : number = -1;

	public schuljahr : number = -1;

	public abschnitt : number = -1;

	public gueltigAb : String = "";

	public gueltigBis : String = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.stundenplan.StundenplanListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanListeEintrag {
		const obj = JSON.parse(json);
		const result = new StundenplanListeEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = String(obj.bezeichnung);
		if (typeof obj.idSchuljahresabschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (typeof obj.schuljahr === "undefined")
			 throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (typeof obj.abschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		if (typeof obj.gueltigAb === "undefined")
			 throw new Error('invalid json format, missing attribute gueltigAb');
		result.gueltigAb = String(obj.gueltigAb);
		if (typeof obj.gueltigBis === "undefined")
			 throw new Error('invalid json format, missing attribute gueltigBis');
		result.gueltigBis = String(obj.gueltigBis);
		return result;
	}

	public static transpilerToJSON(obj : StundenplanListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		result += '"schuljahr" : ' + obj.schuljahr + ',';
		result += '"abschnitt" : ' + obj.abschnitt + ',';
		result += '"gueltigAb" : ' + '"' + obj.gueltigAb.valueOf() + '"' + ',';
		result += '"gueltigBis" : ' + '"' + obj.gueltigBis.valueOf() + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanListeEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		}
		if (typeof obj.idSchuljahresabschnitt !== "undefined") {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		}
		if (typeof obj.schuljahr !== "undefined") {
			result += '"schuljahr" : ' + obj.schuljahr + ',';
		}
		if (typeof obj.abschnitt !== "undefined") {
			result += '"abschnitt" : ' + obj.abschnitt + ',';
		}
		if (typeof obj.gueltigAb !== "undefined") {
			result += '"gueltigAb" : ' + '"' + obj.gueltigAb.valueOf() + '"' + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + '"' + obj.gueltigBis.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_stundenplan_StundenplanListeEintrag(obj : unknown) : StundenplanListeEintrag {
	return obj as StundenplanListeEintrag;
}
