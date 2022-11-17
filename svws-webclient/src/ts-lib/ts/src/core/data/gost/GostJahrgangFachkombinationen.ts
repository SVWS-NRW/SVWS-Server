import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';

export class GostJahrgangFachkombinationen extends JavaObject {

	public id : number = 0;

	public abiturjahr : Number | null = null;

	public fachID1 : number = 0;

	public kursart1 : String | null = null;

	public fachID2 : number = 0;

	public kursart2 : String | null = null;

	public gueltigInHalbjahr : Array<boolean> = Array(6).fill(false);

	public typ : number = 0;

	public hinweistext : String = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostJahrgangFachkombinationen'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostJahrgangFachkombinationen {
		const obj = JSON.parse(json);
		const result = new GostJahrgangFachkombinationen();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.abiturjahr = typeof obj.abiturjahr === "undefined" ? null : obj.abiturjahr === null ? null : Number(obj.abiturjahr);
		if (typeof obj.fachID1 === "undefined")
			 throw new Error('invalid json format, missing attribute fachID1');
		result.fachID1 = obj.fachID1;
		result.kursart1 = typeof obj.kursart1 === "undefined" ? null : obj.kursart1 === null ? null : String(obj.kursart1);
		if (typeof obj.fachID2 === "undefined")
			 throw new Error('invalid json format, missing attribute fachID2');
		result.fachID2 = obj.fachID2;
		result.kursart2 = typeof obj.kursart2 === "undefined" ? null : obj.kursart2 === null ? null : String(obj.kursart2);
		for (let i : number = 0; i < obj.gueltigInHalbjahr.length; i++) {
			result.gueltigInHalbjahr[i] = obj.gueltigInHalbjahr[i];
		}
		if (typeof obj.typ === "undefined")
			 throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		if (typeof obj.hinweistext === "undefined")
			 throw new Error('invalid json format, missing attribute hinweistext');
		result.hinweistext = String(obj.hinweistext);
		return result;
	}

	public static transpilerToJSON(obj : GostJahrgangFachkombinationen) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"abiturjahr" : ' + ((!obj.abiturjahr) ? 'null' : obj.abiturjahr.valueOf()) + ',';
		result += '"fachID1" : ' + obj.fachID1 + ',';
		result += '"kursart1" : ' + ((!obj.kursart1) ? 'null' : '"' + obj.kursart1.valueOf() + '"') + ',';
		result += '"fachID2" : ' + obj.fachID2 + ',';
		result += '"kursart2" : ' + ((!obj.kursart2) ? 'null' : '"' + obj.kursart2.valueOf() + '"') + ',';
		if (!obj.gueltigInHalbjahr) {
			result += '"gueltigInHalbjahr" : []';
		} else {
			result += '"gueltigInHalbjahr" : [ ';
			for (let i : number = 0; i < obj.gueltigInHalbjahr.length; i++) {
				let elem = obj.gueltigInHalbjahr[i];
				result += JSON.stringify(elem);
				if (i < obj.gueltigInHalbjahr.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"typ" : ' + obj.typ + ',';
		result += '"hinweistext" : ' + '"' + obj.hinweistext.valueOf() + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostJahrgangFachkombinationen>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.abiturjahr !== "undefined") {
			result += '"abiturjahr" : ' + ((!obj.abiturjahr) ? 'null' : obj.abiturjahr.valueOf()) + ',';
		}
		if (typeof obj.fachID1 !== "undefined") {
			result += '"fachID1" : ' + obj.fachID1 + ',';
		}
		if (typeof obj.kursart1 !== "undefined") {
			result += '"kursart1" : ' + ((!obj.kursart1) ? 'null' : '"' + obj.kursart1.valueOf() + '"') + ',';
		}
		if (typeof obj.fachID2 !== "undefined") {
			result += '"fachID2" : ' + obj.fachID2 + ',';
		}
		if (typeof obj.kursart2 !== "undefined") {
			result += '"kursart2" : ' + ((!obj.kursart2) ? 'null' : '"' + obj.kursart2.valueOf() + '"') + ',';
		}
		if (typeof obj.gueltigInHalbjahr !== "undefined") {
			let a = obj.gueltigInHalbjahr;
			if (!a) {
				result += '"gueltigInHalbjahr" : []';
			} else {
				result += '"gueltigInHalbjahr" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += JSON.stringify(elem);
					if (i < a.length - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.typ !== "undefined") {
			result += '"typ" : ' + obj.typ + ',';
		}
		if (typeof obj.hinweistext !== "undefined") {
			result += '"hinweistext" : ' + '"' + obj.hinweistext.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostJahrgangFachkombinationen(obj : unknown) : GostJahrgangFachkombinationen {
	return obj as GostJahrgangFachkombinationen;
}
