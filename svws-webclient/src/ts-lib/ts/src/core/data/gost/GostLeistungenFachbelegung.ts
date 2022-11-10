import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostLeistungenFachbelegung extends JavaObject {

	public id : number = 0;

	public schuljahr : number = 0;

	public halbjahrKuerzel : String | null = null;

	public abschnitt : number = 0;

	public abschnittGewertet : boolean = false;

	public jahrgang : String | null = null;

	public lehrer : Number | null = null;

	public notenKuerzel : String | null = null;

	public kursartKuerzel : String | null = null;

	public istSchriftlich : boolean = false;

	public bilingualeSprache : String | null = null;

	public wochenstunden : number = 0;

	public fehlstundenGesamt : number = 0;

	public fehlstundenUnentschuldigt : number = 0;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostLeistungenFachbelegung'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostLeistungenFachbelegung {
		const obj = JSON.parse(json);
		const result = new GostLeistungenFachbelegung();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.schuljahr === "undefined")
			 throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		result.halbjahrKuerzel = typeof obj.halbjahrKuerzel === "undefined" ? null : obj.halbjahrKuerzel === null ? null : String(obj.halbjahrKuerzel);
		if (typeof obj.abschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		if (typeof obj.abschnittGewertet === "undefined")
			 throw new Error('invalid json format, missing attribute abschnittGewertet');
		result.abschnittGewertet = obj.abschnittGewertet;
		result.jahrgang = typeof obj.jahrgang === "undefined" ? null : obj.jahrgang === null ? null : String(obj.jahrgang);
		result.lehrer = typeof obj.lehrer === "undefined" ? null : obj.lehrer === null ? null : Number(obj.lehrer);
		result.notenKuerzel = typeof obj.notenKuerzel === "undefined" ? null : obj.notenKuerzel === null ? null : String(obj.notenKuerzel);
		result.kursartKuerzel = typeof obj.kursartKuerzel === "undefined" ? null : obj.kursartKuerzel === null ? null : String(obj.kursartKuerzel);
		if (typeof obj.istSchriftlich === "undefined")
			 throw new Error('invalid json format, missing attribute istSchriftlich');
		result.istSchriftlich = obj.istSchriftlich;
		result.bilingualeSprache = typeof obj.bilingualeSprache === "undefined" ? null : obj.bilingualeSprache === null ? null : String(obj.bilingualeSprache);
		if (typeof obj.wochenstunden === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (typeof obj.fehlstundenGesamt === "undefined")
			 throw new Error('invalid json format, missing attribute fehlstundenGesamt');
		result.fehlstundenGesamt = obj.fehlstundenGesamt;
		if (typeof obj.fehlstundenUnentschuldigt === "undefined")
			 throw new Error('invalid json format, missing attribute fehlstundenUnentschuldigt');
		result.fehlstundenUnentschuldigt = obj.fehlstundenUnentschuldigt;
		return result;
	}

	public static transpilerToJSON(obj : GostLeistungenFachbelegung) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"schuljahr" : ' + obj.schuljahr + ',';
		result += '"halbjahrKuerzel" : ' + ((!obj.halbjahrKuerzel) ? 'null' : '"' + obj.halbjahrKuerzel.valueOf() + '"') + ',';
		result += '"abschnitt" : ' + obj.abschnitt + ',';
		result += '"abschnittGewertet" : ' + obj.abschnittGewertet + ',';
		result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : '"' + obj.jahrgang.valueOf() + '"') + ',';
		result += '"lehrer" : ' + ((!obj.lehrer) ? 'null' : obj.lehrer.valueOf()) + ',';
		result += '"notenKuerzel" : ' + ((!obj.notenKuerzel) ? 'null' : '"' + obj.notenKuerzel.valueOf() + '"') + ',';
		result += '"kursartKuerzel" : ' + ((!obj.kursartKuerzel) ? 'null' : '"' + obj.kursartKuerzel.valueOf() + '"') + ',';
		result += '"istSchriftlich" : ' + obj.istSchriftlich + ',';
		result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : '"' + obj.bilingualeSprache.valueOf() + '"') + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt + ',';
		result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostLeistungenFachbelegung>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.schuljahr !== "undefined") {
			result += '"schuljahr" : ' + obj.schuljahr + ',';
		}
		if (typeof obj.halbjahrKuerzel !== "undefined") {
			result += '"halbjahrKuerzel" : ' + ((!obj.halbjahrKuerzel) ? 'null' : '"' + obj.halbjahrKuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.abschnitt !== "undefined") {
			result += '"abschnitt" : ' + obj.abschnitt + ',';
		}
		if (typeof obj.abschnittGewertet !== "undefined") {
			result += '"abschnittGewertet" : ' + obj.abschnittGewertet + ',';
		}
		if (typeof obj.jahrgang !== "undefined") {
			result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : '"' + obj.jahrgang.valueOf() + '"') + ',';
		}
		if (typeof obj.lehrer !== "undefined") {
			result += '"lehrer" : ' + ((!obj.lehrer) ? 'null' : obj.lehrer.valueOf()) + ',';
		}
		if (typeof obj.notenKuerzel !== "undefined") {
			result += '"notenKuerzel" : ' + ((!obj.notenKuerzel) ? 'null' : '"' + obj.notenKuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.kursartKuerzel !== "undefined") {
			result += '"kursartKuerzel" : ' + ((!obj.kursartKuerzel) ? 'null' : '"' + obj.kursartKuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.istSchriftlich !== "undefined") {
			result += '"istSchriftlich" : ' + obj.istSchriftlich + ',';
		}
		if (typeof obj.bilingualeSprache !== "undefined") {
			result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : '"' + obj.bilingualeSprache.valueOf() + '"') + ',';
		}
		if (typeof obj.wochenstunden !== "undefined") {
			result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		}
		if (typeof obj.fehlstundenGesamt !== "undefined") {
			result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt + ',';
		}
		if (typeof obj.fehlstundenUnentschuldigt !== "undefined") {
			result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostLeistungenFachbelegung(obj : unknown) : GostLeistungenFachbelegung {
	return obj as GostLeistungenFachbelegung;
}
