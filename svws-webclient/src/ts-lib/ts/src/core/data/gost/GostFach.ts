import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostFach extends JavaObject {

	public id : number = -1;

	public kuerzel : String = "";

	public kuerzelAnzeige : String | null = null;

	public bezeichnung : String | null = null;

	public sortierung : number = 32000;

	public istFremdsprache : boolean = false;

	public istFremdSpracheNeuEinsetzend : boolean = false;

	public biliSprache : String | null = null;

	public istMoeglichAbiLK : boolean = false;

	public istMoeglichAbiGK : boolean = false;

	public istMoeglichEF1 : boolean = false;

	public istMoeglichEF2 : boolean = false;

	public istMoeglichQ11 : boolean = false;

	public istMoeglichQ12 : boolean = false;

	public istMoeglichQ21 : boolean = false;

	public istMoeglichQ22 : boolean = false;

	public wochenstundenQualifikationsphase : number = 3;

	public projektKursLeitfach1ID : Number | null = null;

	public projektKursLeitfach1Kuerzel : String | null = null;

	public projektKursLeitfach2ID : Number | null = null;

	public projektKursLeitfach2Kuerzel : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostFach'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostFach {
		const obj = JSON.parse(json);
		const result = new GostFach();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = String(obj.kuerzel);
		result.kuerzelAnzeige = typeof obj.kuerzelAnzeige === "undefined" ? null : obj.kuerzelAnzeige === null ? null : String(obj.kuerzelAnzeige);
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : String(obj.bezeichnung);
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.istFremdsprache === "undefined")
			 throw new Error('invalid json format, missing attribute istFremdsprache');
		result.istFremdsprache = obj.istFremdsprache;
		if (typeof obj.istFremdSpracheNeuEinsetzend === "undefined")
			 throw new Error('invalid json format, missing attribute istFremdSpracheNeuEinsetzend');
		result.istFremdSpracheNeuEinsetzend = obj.istFremdSpracheNeuEinsetzend;
		result.biliSprache = typeof obj.biliSprache === "undefined" ? null : obj.biliSprache === null ? null : String(obj.biliSprache);
		if (typeof obj.istMoeglichAbiLK === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichAbiLK');
		result.istMoeglichAbiLK = obj.istMoeglichAbiLK;
		if (typeof obj.istMoeglichAbiGK === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichAbiGK');
		result.istMoeglichAbiGK = obj.istMoeglichAbiGK;
		if (typeof obj.istMoeglichEF1 === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichEF1');
		result.istMoeglichEF1 = obj.istMoeglichEF1;
		if (typeof obj.istMoeglichEF2 === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichEF2');
		result.istMoeglichEF2 = obj.istMoeglichEF2;
		if (typeof obj.istMoeglichQ11 === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichQ11');
		result.istMoeglichQ11 = obj.istMoeglichQ11;
		if (typeof obj.istMoeglichQ12 === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichQ12');
		result.istMoeglichQ12 = obj.istMoeglichQ12;
		if (typeof obj.istMoeglichQ21 === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichQ21');
		result.istMoeglichQ21 = obj.istMoeglichQ21;
		if (typeof obj.istMoeglichQ22 === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichQ22');
		result.istMoeglichQ22 = obj.istMoeglichQ22;
		if (typeof obj.wochenstundenQualifikationsphase === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstundenQualifikationsphase');
		result.wochenstundenQualifikationsphase = obj.wochenstundenQualifikationsphase;
		result.projektKursLeitfach1ID = typeof obj.projektKursLeitfach1ID === "undefined" ? null : obj.projektKursLeitfach1ID === null ? null : Number(obj.projektKursLeitfach1ID);
		result.projektKursLeitfach1Kuerzel = typeof obj.projektKursLeitfach1Kuerzel === "undefined" ? null : obj.projektKursLeitfach1Kuerzel === null ? null : String(obj.projektKursLeitfach1Kuerzel);
		result.projektKursLeitfach2ID = typeof obj.projektKursLeitfach2ID === "undefined" ? null : obj.projektKursLeitfach2ID === null ? null : Number(obj.projektKursLeitfach2ID);
		result.projektKursLeitfach2Kuerzel = typeof obj.projektKursLeitfach2Kuerzel === "undefined" ? null : obj.projektKursLeitfach2Kuerzel === null ? null : String(obj.projektKursLeitfach2Kuerzel);
		return result;
	}

	public static transpilerToJSON(obj : GostFach) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		result += '"kuerzelAnzeige" : ' + ((!obj.kuerzelAnzeige) ? 'null' : '"' + obj.kuerzelAnzeige.valueOf() + '"') + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istFremdsprache" : ' + obj.istFremdsprache + ',';
		result += '"istFremdSpracheNeuEinsetzend" : ' + obj.istFremdSpracheNeuEinsetzend + ',';
		result += '"biliSprache" : ' + ((!obj.biliSprache) ? 'null' : '"' + obj.biliSprache.valueOf() + '"') + ',';
		result += '"istMoeglichAbiLK" : ' + obj.istMoeglichAbiLK + ',';
		result += '"istMoeglichAbiGK" : ' + obj.istMoeglichAbiGK + ',';
		result += '"istMoeglichEF1" : ' + obj.istMoeglichEF1 + ',';
		result += '"istMoeglichEF2" : ' + obj.istMoeglichEF2 + ',';
		result += '"istMoeglichQ11" : ' + obj.istMoeglichQ11 + ',';
		result += '"istMoeglichQ12" : ' + obj.istMoeglichQ12 + ',';
		result += '"istMoeglichQ21" : ' + obj.istMoeglichQ21 + ',';
		result += '"istMoeglichQ22" : ' + obj.istMoeglichQ22 + ',';
		result += '"wochenstundenQualifikationsphase" : ' + obj.wochenstundenQualifikationsphase + ',';
		result += '"projektKursLeitfach1ID" : ' + ((!obj.projektKursLeitfach1ID) ? 'null' : obj.projektKursLeitfach1ID.valueOf()) + ',';
		result += '"projektKursLeitfach1Kuerzel" : ' + ((!obj.projektKursLeitfach1Kuerzel) ? 'null' : '"' + obj.projektKursLeitfach1Kuerzel.valueOf() + '"') + ',';
		result += '"projektKursLeitfach2ID" : ' + ((!obj.projektKursLeitfach2ID) ? 'null' : obj.projektKursLeitfach2ID.valueOf()) + ',';
		result += '"projektKursLeitfach2Kuerzel" : ' + ((!obj.projektKursLeitfach2Kuerzel) ? 'null' : '"' + obj.projektKursLeitfach2Kuerzel.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostFach>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.kuerzelAnzeige !== "undefined") {
			result += '"kuerzelAnzeige" : ' + ((!obj.kuerzelAnzeige) ? 'null' : '"' + obj.kuerzelAnzeige.valueOf() + '"') + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.istFremdsprache !== "undefined") {
			result += '"istFremdsprache" : ' + obj.istFremdsprache + ',';
		}
		if (typeof obj.istFremdSpracheNeuEinsetzend !== "undefined") {
			result += '"istFremdSpracheNeuEinsetzend" : ' + obj.istFremdSpracheNeuEinsetzend + ',';
		}
		if (typeof obj.biliSprache !== "undefined") {
			result += '"biliSprache" : ' + ((!obj.biliSprache) ? 'null' : '"' + obj.biliSprache.valueOf() + '"') + ',';
		}
		if (typeof obj.istMoeglichAbiLK !== "undefined") {
			result += '"istMoeglichAbiLK" : ' + obj.istMoeglichAbiLK + ',';
		}
		if (typeof obj.istMoeglichAbiGK !== "undefined") {
			result += '"istMoeglichAbiGK" : ' + obj.istMoeglichAbiGK + ',';
		}
		if (typeof obj.istMoeglichEF1 !== "undefined") {
			result += '"istMoeglichEF1" : ' + obj.istMoeglichEF1 + ',';
		}
		if (typeof obj.istMoeglichEF2 !== "undefined") {
			result += '"istMoeglichEF2" : ' + obj.istMoeglichEF2 + ',';
		}
		if (typeof obj.istMoeglichQ11 !== "undefined") {
			result += '"istMoeglichQ11" : ' + obj.istMoeglichQ11 + ',';
		}
		if (typeof obj.istMoeglichQ12 !== "undefined") {
			result += '"istMoeglichQ12" : ' + obj.istMoeglichQ12 + ',';
		}
		if (typeof obj.istMoeglichQ21 !== "undefined") {
			result += '"istMoeglichQ21" : ' + obj.istMoeglichQ21 + ',';
		}
		if (typeof obj.istMoeglichQ22 !== "undefined") {
			result += '"istMoeglichQ22" : ' + obj.istMoeglichQ22 + ',';
		}
		if (typeof obj.wochenstundenQualifikationsphase !== "undefined") {
			result += '"wochenstundenQualifikationsphase" : ' + obj.wochenstundenQualifikationsphase + ',';
		}
		if (typeof obj.projektKursLeitfach1ID !== "undefined") {
			result += '"projektKursLeitfach1ID" : ' + ((!obj.projektKursLeitfach1ID) ? 'null' : obj.projektKursLeitfach1ID.valueOf()) + ',';
		}
		if (typeof obj.projektKursLeitfach1Kuerzel !== "undefined") {
			result += '"projektKursLeitfach1Kuerzel" : ' + ((!obj.projektKursLeitfach1Kuerzel) ? 'null' : '"' + obj.projektKursLeitfach1Kuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.projektKursLeitfach2ID !== "undefined") {
			result += '"projektKursLeitfach2ID" : ' + ((!obj.projektKursLeitfach2ID) ? 'null' : obj.projektKursLeitfach2ID.valueOf()) + ',';
		}
		if (typeof obj.projektKursLeitfach2Kuerzel !== "undefined") {
			result += '"projektKursLeitfach2Kuerzel" : ' + ((!obj.projektKursLeitfach2Kuerzel) ? 'null' : '"' + obj.projektKursLeitfach2Kuerzel.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostFach(obj : unknown) : GostFach {
	return obj as GostFach;
}
