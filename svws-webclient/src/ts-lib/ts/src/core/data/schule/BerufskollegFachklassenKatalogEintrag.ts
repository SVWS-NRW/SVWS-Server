import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class BerufskollegFachklassenKatalogEintrag extends JavaObject {

	public id : number = 0;

	public index : number = 0;

	public schluessel : String | null = null;

	public schluessel2 : String | null = null;

	public istAusgelaufen : boolean = false;

	public berufsfeldGruppe : String | null = null;

	public berufsfeld : String | null = null;

	public ebene1 : String | null = null;

	public ebene2 : String | null = null;

	public ebene3 : String | null = null;

	public bezeichnung : String = "";

	public bezeichnungM : String = "";

	public bezeichnungW : String = "";

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	/**
	 * Erstellt einen Anlage-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Anlage-Eintrag mit den angegebenen Werten
	 * 
	 * @param id                 die ID
	 * @param index              der Index, welcher der Fachklasse für die Zuordnung zum Bildungsgang zugeordnet ist
	 * @param schluessel         der erste Teil des Fachklassenschlüssels
	 * @param schluessel2        der zweite Teil de Fachklassenschlüssels
	 * @param istAusgelaufen     gibt an, ob die Fachklasse ausgelaufen ist oder nicht
	 * @param berufsfeldGruppe   die Gruppe der Berufsfelder, welcher das Berufsfeld der Fachklasse
	 * @param berufsfeld         das Berufsfeld, welchem die Fachklasse zugeordnet ist
	 * @param ebene1             die Berufsebene 1
	 * @param ebene2             die Berufsebene 2
	 * @param ebene3             die Berufsebene 3
	 * @param bezeichnung        die Bezeichnung der Fachklasse
	 * @param bezeichnungM       die Bezeichnung der Fachklasse (Text in männlicher Form)
	 * @param bezeichnungW       die Bezeichnung der Fachklasse (Text in weiblicher Form)
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, index : number, schluessel : String | null, schluessel2 : String | null, istAusgelaufen : boolean, berufsfeldGruppe : String | null, berufsfeld : String | null, ebene1 : String | null, ebene2 : String | null, ebene3 : String | null, bezeichnung : String, bezeichnungM : String, bezeichnungW : String, gueltigVon : Number | null, gueltigBis : Number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : number, __param2? : String | null, __param3? : String | null, __param4? : boolean, __param5? : String | null, __param6? : String | null, __param7? : String | null, __param8? : String | null, __param9? : String | null, __param10? : String, __param11? : String, __param12? : String, __param13? : Number | null, __param14? : Number | null) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined") && (typeof __param7 === "undefined") && (typeof __param8 === "undefined") && (typeof __param9 === "undefined") && (typeof __param10 === "undefined") && (typeof __param11 === "undefined") && (typeof __param12 === "undefined") && (typeof __param13 === "undefined") && (typeof __param14 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number") && ((typeof __param2 !== "undefined") && ((__param2 instanceof String) || (typeof __param2 === "string")) || (__param2 === null)) && ((typeof __param3 !== "undefined") && ((__param3 instanceof String) || (typeof __param3 === "string")) || (__param3 === null)) && ((typeof __param4 !== "undefined") && typeof __param4 === "boolean") && ((typeof __param5 !== "undefined") && ((__param5 instanceof String) || (typeof __param5 === "string")) || (__param5 === null)) && ((typeof __param6 !== "undefined") && ((__param6 instanceof String) || (typeof __param6 === "string")) || (__param6 === null)) && ((typeof __param7 !== "undefined") && ((__param7 instanceof String) || (typeof __param7 === "string")) || (__param7 === null)) && ((typeof __param8 !== "undefined") && ((__param8 instanceof String) || (typeof __param8 === "string")) || (__param8 === null)) && ((typeof __param9 !== "undefined") && ((__param9 instanceof String) || (typeof __param9 === "string")) || (__param9 === null)) && ((typeof __param10 !== "undefined") && ((__param10 instanceof String) || (typeof __param10 === "string"))) && ((typeof __param11 !== "undefined") && ((__param11 instanceof String) || (typeof __param11 === "string"))) && ((typeof __param12 !== "undefined") && ((__param12 instanceof String) || (typeof __param12 === "string"))) && ((typeof __param13 !== "undefined") && ((__param13 instanceof Number) || (typeof __param13 === "number")) || (__param13 === null)) && ((typeof __param14 !== "undefined") && ((__param14 instanceof Number) || (typeof __param14 === "number")) || (__param14 === null))) {
			let id : number = __param0 as number;
			let index : number = __param1 as number;
			let schluessel : String | null = __param2;
			let schluessel2 : String | null = __param3;
			let istAusgelaufen : boolean = __param4 as boolean;
			let berufsfeldGruppe : String | null = __param5;
			let berufsfeld : String | null = __param6;
			let ebene1 : String | null = __param7;
			let ebene2 : String | null = __param8;
			let ebene3 : String | null = __param9;
			let bezeichnung : String = __param10;
			let bezeichnungM : String = __param11;
			let bezeichnungW : String = __param12;
			let gueltigVon : Number | null = cast_java_lang_Integer(__param13);
			let gueltigBis : Number | null = cast_java_lang_Integer(__param14);
			this.id = id;
			this.index = index;
			this.schluessel = schluessel;
			this.schluessel2 = schluessel2;
			this.istAusgelaufen = istAusgelaufen;
			this.berufsfeldGruppe = berufsfeldGruppe;
			this.berufsfeld = berufsfeld;
			this.ebene1 = ebene1;
			this.ebene2 = ebene2;
			this.ebene3 = ebene3;
			this.bezeichnung = bezeichnung;
			this.bezeichnungM = bezeichnungM;
			this.bezeichnungW = bezeichnungW;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.BerufskollegFachklassenKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): BerufskollegFachklassenKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new BerufskollegFachklassenKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.index === "undefined")
			 throw new Error('invalid json format, missing attribute index');
		result.index = obj.index;
		result.schluessel = typeof obj.schluessel === "undefined" ? null : obj.schluessel;
		result.schluessel2 = typeof obj.schluessel2 === "undefined" ? null : obj.schluessel2;
		if (typeof obj.istAusgelaufen === "undefined")
			 throw new Error('invalid json format, missing attribute istAusgelaufen');
		result.istAusgelaufen = obj.istAusgelaufen;
		result.berufsfeldGruppe = typeof obj.berufsfeldGruppe === "undefined" ? null : obj.berufsfeldGruppe;
		result.berufsfeld = typeof obj.berufsfeld === "undefined" ? null : obj.berufsfeld;
		result.ebene1 = typeof obj.ebene1 === "undefined" ? null : obj.ebene1;
		result.ebene2 = typeof obj.ebene2 === "undefined" ? null : obj.ebene2;
		result.ebene3 = typeof obj.ebene3 === "undefined" ? null : obj.ebene3;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.bezeichnungM === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnungM');
		result.bezeichnungM = obj.bezeichnungM;
		if (typeof obj.bezeichnungW === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnungW');
		result.bezeichnungW = obj.bezeichnungW;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : BerufskollegFachklassenKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"index" : ' + obj.index + ',';
		result += '"schluessel" : ' + ((!obj.schluessel) ? 'null' : '"' + obj.schluessel.valueOf() + '"') + ',';
		result += '"schluessel2" : ' + ((!obj.schluessel2) ? 'null' : '"' + obj.schluessel2.valueOf() + '"') + ',';
		result += '"istAusgelaufen" : ' + obj.istAusgelaufen + ',';
		result += '"berufsfeldGruppe" : ' + ((!obj.berufsfeldGruppe) ? 'null' : '"' + obj.berufsfeldGruppe.valueOf() + '"') + ',';
		result += '"berufsfeld" : ' + ((!obj.berufsfeld) ? 'null' : '"' + obj.berufsfeld.valueOf() + '"') + ',';
		result += '"ebene1" : ' + ((!obj.ebene1) ? 'null' : '"' + obj.ebene1.valueOf() + '"') + ',';
		result += '"ebene2" : ' + ((!obj.ebene2) ? 'null' : '"' + obj.ebene2.valueOf() + '"') + ',';
		result += '"ebene3" : ' + ((!obj.ebene3) ? 'null' : '"' + obj.ebene3.valueOf() + '"') + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		result += '"bezeichnungM" : ' + '"' + obj.bezeichnungM.valueOf() + '"' + ',';
		result += '"bezeichnungW" : ' + '"' + obj.bezeichnungW.valueOf() + '"' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BerufskollegFachklassenKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.index !== "undefined") {
			result += '"index" : ' + obj.index + ',';
		}
		if (typeof obj.schluessel !== "undefined") {
			result += '"schluessel" : ' + ((!obj.schluessel) ? 'null' : '"' + obj.schluessel.valueOf() + '"') + ',';
		}
		if (typeof obj.schluessel2 !== "undefined") {
			result += '"schluessel2" : ' + ((!obj.schluessel2) ? 'null' : '"' + obj.schluessel2.valueOf() + '"') + ',';
		}
		if (typeof obj.istAusgelaufen !== "undefined") {
			result += '"istAusgelaufen" : ' + obj.istAusgelaufen + ',';
		}
		if (typeof obj.berufsfeldGruppe !== "undefined") {
			result += '"berufsfeldGruppe" : ' + ((!obj.berufsfeldGruppe) ? 'null' : '"' + obj.berufsfeldGruppe.valueOf() + '"') + ',';
		}
		if (typeof obj.berufsfeld !== "undefined") {
			result += '"berufsfeld" : ' + ((!obj.berufsfeld) ? 'null' : '"' + obj.berufsfeld.valueOf() + '"') + ',';
		}
		if (typeof obj.ebene1 !== "undefined") {
			result += '"ebene1" : ' + ((!obj.ebene1) ? 'null' : '"' + obj.ebene1.valueOf() + '"') + ',';
		}
		if (typeof obj.ebene2 !== "undefined") {
			result += '"ebene2" : ' + ((!obj.ebene2) ? 'null' : '"' + obj.ebene2.valueOf() + '"') + ',';
		}
		if (typeof obj.ebene3 !== "undefined") {
			result += '"ebene3" : ' + ((!obj.ebene3) ? 'null' : '"' + obj.ebene3.valueOf() + '"') + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		}
		if (typeof obj.bezeichnungM !== "undefined") {
			result += '"bezeichnungM" : ' + '"' + obj.bezeichnungM.valueOf() + '"' + ',';
		}
		if (typeof obj.bezeichnungW !== "undefined") {
			result += '"bezeichnungW" : ' + '"' + obj.bezeichnungW.valueOf() + '"' + ',';
		}
		if (typeof obj.gueltigVon !== "undefined") {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schule_BerufskollegFachklassenKatalogEintrag(obj : unknown) : BerufskollegFachklassenKatalogEintrag {
	return obj as BerufskollegFachklassenKatalogEintrag;
}
