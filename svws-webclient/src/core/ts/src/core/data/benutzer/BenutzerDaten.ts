import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { BenutzergruppeDaten, cast_de_nrw_schule_svws_core_data_benutzer_BenutzergruppeDaten } from '../../../core/data/benutzer/BenutzergruppeDaten';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class BenutzerDaten extends JavaObject {

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

	/**
	 * Die Daten der Benutzergruppen, denen dieser Benutzer zugeordnet ist. 
	 */
	public gruppen : List<BenutzergruppeDaten> = new Vector();

	/**
	 * Die Kompetenzen, die speziell diesem Benutzer zugeordnet sind. 
	 */
	public kompetenzen : Vector<number> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.benutzer.BenutzerDaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): BenutzerDaten {
		const obj = JSON.parse(json);
		const result = new BenutzerDaten();
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
		if (!!obj.gruppen) {
			for (let elem of obj.gruppen) {
				result.gruppen?.add(BenutzergruppeDaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.kompetenzen) {
			for (let elem of obj.kompetenzen) {
				result.kompetenzen?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BenutzerDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"typ" : ' + obj.typ + ',';
		result += '"typID" : ' + obj.typID + ',';
		result += '"anzeigename" : ' + '"' + obj.anzeigename! + '"' + ',';
		result += '"name" : ' + '"' + obj.name! + '"' + ',';
		result += '"istAdmin" : ' + obj.istAdmin + ',';
		result += '"idCredentials" : ' + obj.idCredentials + ',';
		if (!obj.gruppen) {
			result += '"gruppen" : []';
		} else {
			result += '"gruppen" : [ ';
			for (let i : number = 0; i < obj.gruppen.size(); i++) {
				let elem = obj.gruppen.get(i);
				result += BenutzergruppeDaten.transpilerToJSON(elem);
				if (i < obj.gruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.kompetenzen) {
			result += '"kompetenzen" : []';
		} else {
			result += '"kompetenzen" : [ ';
			for (let i : number = 0; i < obj.kompetenzen.size(); i++) {
				let elem = obj.kompetenzen.get(i);
				result += elem;
				if (i < obj.kompetenzen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzerDaten>) : string {
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
		if (typeof obj.gruppen !== "undefined") {
			if (!obj.gruppen) {
				result += '"gruppen" : []';
			} else {
				result += '"gruppen" : [ ';
				for (let i : number = 0; i < obj.gruppen.size(); i++) {
					let elem = obj.gruppen.get(i);
					result += BenutzergruppeDaten.transpilerToJSON(elem);
					if (i < obj.gruppen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.kompetenzen !== "undefined") {
			if (!obj.kompetenzen) {
				result += '"kompetenzen" : []';
			} else {
				result += '"kompetenzen" : [ ';
				for (let i : number = 0; i < obj.kompetenzen.size(); i++) {
					let elem = obj.kompetenzen.get(i);
					result += elem;
					if (i < obj.kompetenzen.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_benutzer_BenutzerDaten(obj : unknown) : BenutzerDaten {
	return obj as BenutzerDaten;
}
