import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class BenutzergruppeListeEintrag extends JavaObject {

	/**
	 * Die ID der Benutzergruppe. 
	 */
	public id : number = -1;

	/**
	 * Die Bezeichnung der Benutzergruppe. 
	 */
	public bezeichnung : String = "";

	/**
	 * Gibt an, ob es sich um eine Administrative Benutzergruppe handelt oder nicht. 
	 */
	public istAdmin : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.benutzer.BenutzergruppeListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): BenutzergruppeListeEintrag {
		const obj = JSON.parse(json);
		const result = new BenutzergruppeListeEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = String(obj.bezeichnung);
		if (typeof obj.istAdmin === "undefined")
			 throw new Error('invalid json format, missing attribute istAdmin');
		result.istAdmin = obj.istAdmin;
		return result;
	}

	public static transpilerToJSON(obj : BenutzergruppeListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		result += '"istAdmin" : ' + obj.istAdmin + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzergruppeListeEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		}
		if (typeof obj.istAdmin !== "undefined") {
			result += '"istAdmin" : ' + obj.istAdmin + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_benutzer_BenutzergruppeListeEintrag(obj : unknown) : BenutzergruppeListeEintrag {
	return obj as BenutzergruppeListeEintrag;
}
