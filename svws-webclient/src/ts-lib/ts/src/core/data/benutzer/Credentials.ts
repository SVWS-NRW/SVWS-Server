import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Credentials extends JavaObject {

	public benutzername : String = "";

	public password : String = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.benutzer.Credentials'].includes(name);
	}

	public static transpilerFromJSON(json : string): Credentials {
		const obj = JSON.parse(json);
		const result = new Credentials();
		if (typeof obj.benutzername === "undefined")
			 throw new Error('invalid json format, missing attribute benutzername');
		result.benutzername = String(obj.benutzername);
		if (typeof obj.password === "undefined")
			 throw new Error('invalid json format, missing attribute password');
		result.password = String(obj.password);
		return result;
	}

	public static transpilerToJSON(obj : Credentials) : string {
		let result = '{';
		result += '"benutzername" : ' + '"' + obj.benutzername.valueOf() + '"' + ',';
		result += '"password" : ' + '"' + obj.password.valueOf() + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Credentials>) : string {
		let result = '{';
		if (typeof obj.benutzername !== "undefined") {
			result += '"benutzername" : ' + '"' + obj.benutzername.valueOf() + '"' + ',';
		}
		if (typeof obj.password !== "undefined") {
			result += '"password" : ' + '"' + obj.password.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_benutzer_Credentials(obj : unknown) : Credentials {
	return obj as Credentials;
}
