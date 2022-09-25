import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchuelerblockungInputKurs extends JavaObject {

	public id : number = 0;

	public fach : number = 0;

	public kursart : number = 0;

	public istGesperrt : boolean = false;

	public istFixiert : boolean = false;

	public anzahlSuS : number = -1;

	public schienen : Array<number> = [-1];

	public representation : String = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kursblockung.SchuelerblockungInputKurs'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerblockungInputKurs {
		const obj = JSON.parse(json);
		const result = new SchuelerblockungInputKurs();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.fach === "undefined")
			 throw new Error('invalid json format, missing attribute fach');
		result.fach = obj.fach;
		if (typeof obj.kursart === "undefined")
			 throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (typeof obj.istGesperrt === "undefined")
			 throw new Error('invalid json format, missing attribute istGesperrt');
		result.istGesperrt = obj.istGesperrt;
		if (typeof obj.istFixiert === "undefined")
			 throw new Error('invalid json format, missing attribute istFixiert');
		result.istFixiert = obj.istFixiert;
		if (typeof obj.anzahlSuS === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlSuS');
		result.anzahlSuS = obj.anzahlSuS;
		for (let i : number = 0; i < obj.schienen.length; i++) {
			result.schienen[i] = obj.schienen[i];
		}
		if (typeof obj.representation === "undefined")
			 throw new Error('invalid json format, missing attribute representation');
		result.representation = obj.representation;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerblockungInputKurs) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"fach" : ' + obj.fach + ',';
		result += '"kursart" : ' + obj.kursart + ',';
		result += '"istGesperrt" : ' + obj.istGesperrt + ',';
		result += '"istFixiert" : ' + obj.istFixiert + ',';
		result += '"anzahlSuS" : ' + obj.anzahlSuS + ',';
		if (!obj.schienen) {
			result += '"schienen" : []';
		} else {
			result += '"schienen" : [ ';
			for (let i : number = 0; i < obj.schienen.length; i++) {
				let elem = obj.schienen[i];
				result += JSON.stringify(elem);
				if (i < obj.schienen.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"representation" : ' + '"' + obj.representation.valueOf() + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerblockungInputKurs>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.fach !== "undefined") {
			result += '"fach" : ' + obj.fach + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + obj.kursart + ',';
		}
		if (typeof obj.istGesperrt !== "undefined") {
			result += '"istGesperrt" : ' + obj.istGesperrt + ',';
		}
		if (typeof obj.istFixiert !== "undefined") {
			result += '"istFixiert" : ' + obj.istFixiert + ',';
		}
		if (typeof obj.anzahlSuS !== "undefined") {
			result += '"anzahlSuS" : ' + obj.anzahlSuS + ',';
		}
		if (typeof obj.schienen !== "undefined") {
			let a = obj.schienen;
			if (!a) {
				result += '"schienen" : []';
			} else {
				result += '"schienen" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += JSON.stringify(elem);
					if (i < a.length - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.representation !== "undefined") {
			result += '"representation" : ' + '"' + obj.representation.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungInputKurs(obj : unknown) : SchuelerblockungInputKurs {
	return obj as SchuelerblockungInputKurs;
}
