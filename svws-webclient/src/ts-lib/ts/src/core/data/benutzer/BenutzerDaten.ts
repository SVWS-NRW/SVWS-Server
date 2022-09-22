import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { BenutzerKompetenzKatalogEintrag, cast_de_nrw_schule_svws_core_data_benutzer_BenutzerKompetenzKatalogEintrag } from '../../../core/data/benutzer/BenutzerKompetenzKatalogEintrag';
import { BenutzergruppeDaten, cast_de_nrw_schule_svws_core_data_benutzer_BenutzergruppeDaten } from '../../../core/data/benutzer/BenutzergruppeDaten';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class BenutzerDaten extends JavaObject {

	public id : number = -1;

	public typ : number = 0;

	public typID : number = -1;

	public anzeigename : String = "";

	public name : String = "";

	public istAdmin : boolean = false;

	public idCredentials : number = -1;

	public gruppen : List<BenutzergruppeDaten> = new Vector();

	public kompetenzen : List<BenutzerKompetenzKatalogEintrag> = new Vector();

	public kompetenzenAlle : List<BenutzerKompetenzKatalogEintrag> = new Vector();


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
				result.kompetenzen?.add(BenutzerKompetenzKatalogEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.kompetenzenAlle) {
			for (let elem of obj.kompetenzenAlle) {
				result.kompetenzenAlle?.add(BenutzerKompetenzKatalogEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BenutzerDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"typ" : ' + obj.typ + ',';
		result += '"typID" : ' + obj.typID + ',';
		result += '"anzeigename" : ' + '"' + obj.anzeigename.valueOf() + '"' + ',';
		result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
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
				result += BenutzerKompetenzKatalogEintrag.transpilerToJSON(elem);
				if (i < obj.kompetenzen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.kompetenzenAlle) {
			result += '"kompetenzenAlle" : []';
		} else {
			result += '"kompetenzenAlle" : [ ';
			for (let i : number = 0; i < obj.kompetenzenAlle.size(); i++) {
				let elem = obj.kompetenzenAlle.get(i);
				result += BenutzerKompetenzKatalogEintrag.transpilerToJSON(elem);
				if (i < obj.kompetenzenAlle.size() - 1)
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
			result += '"anzeigename" : ' + '"' + obj.anzeigename.valueOf() + '"' + ',';
		}
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
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
					result += BenutzerKompetenzKatalogEintrag.transpilerToJSON(elem);
					if (i < obj.kompetenzen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.kompetenzenAlle !== "undefined") {
			if (!obj.kompetenzenAlle) {
				result += '"kompetenzenAlle" : []';
			} else {
				result += '"kompetenzenAlle" : [ ';
				for (let i : number = 0; i < obj.kompetenzenAlle.size(); i++) {
					let elem = obj.kompetenzenAlle.get(i);
					result += BenutzerKompetenzKatalogEintrag.transpilerToJSON(elem);
					if (i < obj.kompetenzenAlle.size() - 1)
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
