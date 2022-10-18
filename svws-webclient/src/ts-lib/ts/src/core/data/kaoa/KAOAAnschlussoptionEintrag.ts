import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { Schulstufe, cast_de_nrw_schule_svws_core_types_schule_Schulstufe } from '../../../core/types/schule/Schulstufe';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { KAOAZusatzmerkmal, cast_de_nrw_schule_svws_core_types_kaoa_KAOAZusatzmerkmal } from '../../../core/types/kaoa/KAOAZusatzmerkmal';

export class KAOAAnschlussoptionEintrag extends JavaObject {

	public id : number = 0;

	public kuerzel : String = "";

	public beschreibung : String = "";

	public stufen : List<String> = new Vector();

	public anzeigeZusatzmerkmal : List<String> = new Vector();

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	/**
	 * Erstellt einen KAoA-Anschlussoption-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen KAoA-Anschlussoption-Eintrag mit den angegebenen Werten
	 * 
	 * @param id             die ID
	 * @param kuerzel        das Kürzel 
	 * @param beschreibung   die Beschreibung
	 * @param stufen         die Jahrgangsstufen in denen der Eintrag gemacht werden darf (SI bzw. SII)
	 * @param anzeigeZusatzmerkmal
	 *                       gibt an bei welchen Anschlussvereinbarungen SBO10.7 die Optionen angezeigt werden
	 * @param gueltigVon     das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis     das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : String, beschreibung : String, stufen : List<Schulstufe>, anzeigeZusatzmerkmal : List<KAOAZusatzmerkmal>, gueltigVon : Number | null, gueltigBis : Number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : String, __param2? : String, __param3? : List<Schulstufe>, __param4? : List<KAOAZusatzmerkmal>, __param5? : Number | null, __param6? : Number | null) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && ((__param1 instanceof String) || (typeof __param1 === "string"))) && ((typeof __param2 !== "undefined") && ((__param2 instanceof String) || (typeof __param2 === "string"))) && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('java.util.List'))) || (__param3 === null)) && ((typeof __param4 !== "undefined") && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('java.util.List'))) || (__param4 === null)) && ((typeof __param5 !== "undefined") && ((__param5 instanceof Number) || (typeof __param5 === "number")) || (__param5 === null)) && ((typeof __param6 !== "undefined") && ((__param6 instanceof Number) || (typeof __param6 === "number")) || (__param6 === null))) {
			let id : number = __param0 as number;
			let kuerzel : String = __param1;
			let beschreibung : String = __param2;
			let stufen : List<Schulstufe> = cast_java_util_List(__param3);
			let anzeigeZusatzmerkmal : List<KAOAZusatzmerkmal> = cast_java_util_List(__param4);
			let gueltigVon : Number | null = cast_java_lang_Integer(__param5);
			let gueltigBis : Number | null = cast_java_lang_Integer(__param6);
			this.id = id;
			this.kuerzel = kuerzel;
			this.beschreibung = beschreibung;
			for (let stufe of stufen) 
				this.stufen.add(stufe.daten.kuerzel);
			for (let m of anzeigeZusatzmerkmal) 
				this.anzeigeZusatzmerkmal.add(m.daten.kuerzel);
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kaoa.KAOAAnschlussoptionEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): KAOAAnschlussoptionEintrag {
		const obj = JSON.parse(json);
		const result = new KAOAAnschlussoptionEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.beschreibung === "undefined")
			 throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		if (!!obj.stufen) {
			for (let elem of obj.stufen) {
				result.stufen?.add(elem);
			}
		}
		if (!!obj.anzeigeZusatzmerkmal) {
			for (let elem of obj.anzeigeZusatzmerkmal) {
				result.anzeigeZusatzmerkmal?.add(elem);
			}
		}
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : KAOAAnschlussoptionEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		result += '"beschreibung" : ' + '"' + obj.beschreibung.valueOf() + '"' + ',';
		if (!obj.stufen) {
			result += '"stufen" : []';
		} else {
			result += '"stufen" : [ ';
			for (let i : number = 0; i < obj.stufen.size(); i++) {
				let elem = obj.stufen.get(i);
				result += '"' + elem + '"';
				if (i < obj.stufen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.anzeigeZusatzmerkmal) {
			result += '"anzeigeZusatzmerkmal" : []';
		} else {
			result += '"anzeigeZusatzmerkmal" : [ ';
			for (let i : number = 0; i < obj.anzeigeZusatzmerkmal.size(); i++) {
				let elem = obj.anzeigeZusatzmerkmal.get(i);
				result += '"' + elem + '"';
				if (i < obj.anzeigeZusatzmerkmal.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KAOAAnschlussoptionEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + '"' + obj.beschreibung.valueOf() + '"' + ',';
		}
		if (typeof obj.stufen !== "undefined") {
			if (!obj.stufen) {
				result += '"stufen" : []';
			} else {
				result += '"stufen" : [ ';
				for (let i : number = 0; i < obj.stufen.size(); i++) {
					let elem = obj.stufen.get(i);
					result += '"' + elem + '"';
					if (i < obj.stufen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.anzeigeZusatzmerkmal !== "undefined") {
			if (!obj.anzeigeZusatzmerkmal) {
				result += '"anzeigeZusatzmerkmal" : []';
			} else {
				result += '"anzeigeZusatzmerkmal" : [ ';
				for (let i : number = 0; i < obj.anzeigeZusatzmerkmal.size(); i++) {
					let elem = obj.anzeigeZusatzmerkmal.get(i);
					result += '"' + elem + '"';
					if (i < obj.anzeigeZusatzmerkmal.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
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

export function cast_de_nrw_schule_svws_core_data_kaoa_KAOAAnschlussoptionEintrag(obj : unknown) : KAOAAnschlussoptionEintrag {
	return obj as KAOAAnschlussoptionEintrag;
}
