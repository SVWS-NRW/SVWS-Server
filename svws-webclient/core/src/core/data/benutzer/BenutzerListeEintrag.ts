import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.benutzer.BenutzerListeEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.benutzer.BenutzerListeEintrag'].includes(name);
	}

	public static class = new Class<BenutzerListeEintrag>('de.svws_nrw.core.data.benutzer.BenutzerListeEintrag');

	public static transpilerFromJSON(json : string): BenutzerListeEintrag {
		const obj = JSON.parse(json) as Partial<BenutzerListeEintrag>;
		const result = new BenutzerListeEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.typ === undefined)
			throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		if (obj.typID === undefined)
			throw new Error('invalid json format, missing attribute typID');
		result.typID = obj.typID;
		if (obj.anzeigename === undefined)
			throw new Error('invalid json format, missing attribute anzeigename');
		result.anzeigename = obj.anzeigename;
		if (obj.name === undefined)
			throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (obj.istAdmin === undefined)
			throw new Error('invalid json format, missing attribute istAdmin');
		result.istAdmin = obj.istAdmin;
		if (obj.idCredentials === undefined)
			throw new Error('invalid json format, missing attribute idCredentials');
		result.idCredentials = obj.idCredentials;
		return result;
	}

	public static transpilerToJSON(obj : BenutzerListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"typ" : ' + obj.typ.toString() + ',';
		result += '"typID" : ' + obj.typID.toString() + ',';
		result += '"anzeigename" : ' + JSON.stringify(obj.anzeigename) + ',';
		result += '"name" : ' + JSON.stringify(obj.name) + ',';
		result += '"istAdmin" : ' + obj.istAdmin.toString() + ',';
		result += '"idCredentials" : ' + obj.idCredentials.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzerListeEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.typ !== undefined) {
			result += '"typ" : ' + obj.typ.toString() + ',';
		}
		if (obj.typID !== undefined) {
			result += '"typID" : ' + obj.typID.toString() + ',';
		}
		if (obj.anzeigename !== undefined) {
			result += '"anzeigename" : ' + JSON.stringify(obj.anzeigename) + ',';
		}
		if (obj.name !== undefined) {
			result += '"name" : ' + JSON.stringify(obj.name) + ',';
		}
		if (obj.istAdmin !== undefined) {
			result += '"istAdmin" : ' + obj.istAdmin.toString() + ',';
		}
		if (obj.idCredentials !== undefined) {
			result += '"idCredentials" : ' + obj.idCredentials.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_benutzer_BenutzerListeEintrag(obj : unknown) : BenutzerListeEintrag {
	return obj as BenutzerListeEintrag;
}
