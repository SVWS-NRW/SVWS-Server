import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class BenutzerListeEintrag extends JavaObject {

	/**
	 * Die ID des Benutzers. 
	 */
	public id : number = -1;

	/**
	 * Der Typ des Benutzers. 
	 */
	public typ : number = 0;

	/**
	 * die ID des Benutzers in der Typ-spezifischen-Tabelle (z.B. Lehrer-ID) 
	 */
	public typID : number = -1;

	/**
	 * Der Anzeigename des Benutzers. 
	 */
	public anzeigename : string = "";

	/**
	 * Der Anmeldename des Benutzers 
	 */
	public name : string = "";

	/**
	 * Gibt an, ob es sich um einen Administrativen Benutzer handelt oder nicht. 
	 */
	public istAdmin : boolean = false;

	/**
	 * Die ID der Credentials des Benutzers. 
	 */
	public idCredentials : number = -1;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.benutzer.BenutzerListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): BenutzerListeEintrag {
		const obj = JSON.parse(json);
		const result = new BenutzerListeEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.typ === "undefined")
			 throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		if (typeof obj.typID === "undefined")
			 throw new Error('invalid json format, missing attribute typID');
		result.typID = obj.typID;
		if (typeof obj.anzeigename === "undefined")
			 throw new Error('invalid json format, missing attribute anzeigename');
		result.anzeigename = obj.anzeigename;
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (typeof obj.istAdmin === "undefined")
			 throw new Error('invalid json format, missing attribute istAdmin');
		result.istAdmin = obj.istAdmin;
		if (typeof obj.idCredentials === "undefined")
			 throw new Error('invalid json format, missing attribute idCredentials');
		result.idCredentials = obj.idCredentials;
		return result;
	}

	public static transpilerToJSON(obj : BenutzerListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"typ" : ' + obj.typ + ',';
		result += '"typID" : ' + obj.typID + ',';
		result += '"anzeigename" : ' + '"' + obj.anzeigename! + '"' + ',';
		result += '"name" : ' + '"' + obj.name! + '"' + ',';
		result += '"istAdmin" : ' + obj.istAdmin + ',';
		result += '"idCredentials" : ' + obj.idCredentials + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzerListeEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.typ !== "undefined") {
			result += '"typ" : ' + obj.typ + ',';
		}
		if (typeof obj.typID !== "undefined") {
			result += '"typID" : ' + obj.typID + ',';
		}
		if (typeof obj.anzeigename !== "undefined") {
			result += '"anzeigename" : ' + '"' + obj.anzeigename + '"' + ',';
		}
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + '"' + obj.name + '"' + ',';
		}
		if (typeof obj.istAdmin !== "undefined") {
			result += '"istAdmin" : ' + obj.istAdmin + ',';
		}
		if (typeof obj.idCredentials !== "undefined") {
			result += '"idCredentials" : ' + obj.idCredentials + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_benutzer_BenutzerListeEintrag(obj : unknown) : BenutzerListeEintrag {
	return obj as BenutzerListeEintrag;
}
