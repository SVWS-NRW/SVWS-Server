import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ErzieherListeEintrag extends JavaObject {

	public id : number = 0;

	public idSchueler : number = 0;

	public idErzieherArt : Number | null = null;

	public anrede : String | null = null;

	public name : String | null = null;

	public vorname : String | null = null;

	public email : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.erzieher.ErzieherListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): ErzieherListeEintrag {
		const obj = JSON.parse(json);
		const result = new ErzieherListeEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idSchueler === "undefined")
			 throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		result.idErzieherArt = typeof obj.idErzieherArt === "undefined" ? null : obj.idErzieherArt;
		result.anrede = typeof obj.anrede === "undefined" ? null : obj.anrede;
		result.name = typeof obj.name === "undefined" ? null : obj.name;
		result.vorname = typeof obj.vorname === "undefined" ? null : obj.vorname;
		result.email = typeof obj.email === "undefined" ? null : obj.email;
		return result;
	}

	public static transpilerToJSON(obj : ErzieherListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idSchueler" : ' + obj.idSchueler + ',';
		result += '"idErzieherArt" : ' + ((!obj.idErzieherArt) ? 'null' : obj.idErzieherArt.valueOf()) + ',';
		result += '"anrede" : ' + ((!obj.anrede) ? 'null' : '"' + obj.anrede.valueOf() + '"') + ',';
		result += '"name" : ' + ((!obj.name) ? 'null' : '"' + obj.name.valueOf() + '"') + ',';
		result += '"vorname" : ' + ((!obj.vorname) ? 'null' : '"' + obj.vorname.valueOf() + '"') + ',';
		result += '"email" : ' + ((!obj.email) ? 'null' : '"' + obj.email.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ErzieherListeEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idSchueler !== "undefined") {
			result += '"idSchueler" : ' + obj.idSchueler + ',';
		}
		if (typeof obj.idErzieherArt !== "undefined") {
			result += '"idErzieherArt" : ' + ((!obj.idErzieherArt) ? 'null' : obj.idErzieherArt.valueOf()) + ',';
		}
		if (typeof obj.anrede !== "undefined") {
			result += '"anrede" : ' + ((!obj.anrede) ? 'null' : '"' + obj.anrede.valueOf() + '"') + ',';
		}
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + ((!obj.name) ? 'null' : '"' + obj.name.valueOf() + '"') + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + ((!obj.vorname) ? 'null' : '"' + obj.vorname.valueOf() + '"') + ',';
		}
		if (typeof obj.email !== "undefined") {
			result += '"email" : ' + ((!obj.email) ? 'null' : '"' + obj.email.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_erzieher_ErzieherListeEintrag(obj : unknown) : ErzieherListeEintrag {
	return obj as ErzieherListeEintrag;
}
