import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { GostStatistikFachwahlHalbjahr, cast_de_nrw_schule_svws_core_data_gost_GostStatistikFachwahlHalbjahr } from '../../../core/data/gost/GostStatistikFachwahlHalbjahr';

export class GostStatistikFachwahl extends JavaObject {

	public abiturjahr : number = 0;

	public id : number = 0;

	public kuerzel : String | null = null;

	public bezeichnung : String | null = null;

	public kuerzelStatistik : String | null = null;

	public fachwahlen : Array<GostStatistikFachwahlHalbjahr> = Array(6).fill(null);


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostStatistikFachwahl'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostStatistikFachwahl {
		const obj = JSON.parse(json);
		const result = new GostStatistikFachwahl();
		if (typeof obj.abiturjahr === "undefined")
			 throw new Error('invalid json format, missing attribute abiturjahr');
		result.abiturjahr = obj.abiturjahr;
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel;
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung;
		result.kuerzelStatistik = typeof obj.kuerzelStatistik === "undefined" ? null : obj.kuerzelStatistik;
		for (let i : number = 0; i < obj.fachwahlen.length; i++) {
			result.fachwahlen[i] = (GostStatistikFachwahlHalbjahr.transpilerFromJSON(JSON.stringify(obj.fachwahlen[i])));
		}
		return result;
	}

	public static transpilerToJSON(obj : GostStatistikFachwahl) : string {
		let result = '{';
		result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		result += '"kuerzelStatistik" : ' + ((!obj.kuerzelStatistik) ? 'null' : '"' + obj.kuerzelStatistik.valueOf() + '"') + ',';
		if (!obj.fachwahlen) {
			result += '"fachwahlen" : []';
		} else {
			result += '"fachwahlen" : [ ';
			for (let i : number = 0; i < obj.fachwahlen.length; i++) {
				let elem = obj.fachwahlen[i];
				result += GostStatistikFachwahlHalbjahr.transpilerToJSON(elem);
				if (i < obj.fachwahlen.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostStatistikFachwahl>) : string {
		let result = '{';
		if (typeof obj.abiturjahr !== "undefined") {
			result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		}
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		}
		if (typeof obj.kuerzelStatistik !== "undefined") {
			result += '"kuerzelStatistik" : ' + ((!obj.kuerzelStatistik) ? 'null' : '"' + obj.kuerzelStatistik.valueOf() + '"') + ',';
		}
		if (typeof obj.fachwahlen !== "undefined") {
			let a = obj.fachwahlen;
			if (!a) {
				result += '"fachwahlen" : []';
			} else {
				result += '"fachwahlen" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += GostStatistikFachwahlHalbjahr.transpilerToJSON(elem);
					if (i < a.length - 1)
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

export function cast_de_nrw_schule_svws_core_data_gost_GostStatistikFachwahl(obj : unknown) : GostStatistikFachwahl {
	return obj as GostStatistikFachwahl;
}
