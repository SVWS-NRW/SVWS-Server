import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { Schulstufe, cast_de_nrw_schule_svws_core_types_schule_Schulstufe } from '../../../core/types/schule/Schulstufe';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { KAOAZusatzmerkmal, cast_de_nrw_schule_svws_core_types_kaoa_KAOAZusatzmerkmal } from '../../../core/types/kaoa/KAOAZusatzmerkmal';

export class KAOAAnschlussoptionEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags. 
	 */
	public id : number = 0;

	/**
	 * Das Kürzel der Anschlussoption. 
	 */
	public kuerzel : string = "";

	/**
	 * Die Beschreibung der Anschlussoption. 
	 */
	public beschreibung : string = "";

	/**
	 * Jahrgangsstufen in denen der Eintrag gemacht werden darf (SI bzw. SII) 
	 */
	public stufen : List<string> = new Vector();

	/**
	 * Gibt an bei welchen Anschlussvereinbarungen SBO10.7 die Optionen angezeigt werden 
	 */
	public anzeigeZusatzmerkmal : List<string> = new Vector();

	/**
	 * Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. 
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. 
	 */
	public gueltigBis : number | null = null;


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
	public constructor(id : number, kuerzel : string, beschreibung : string, stufen : List<Schulstufe>, anzeigeZusatzmerkmal : List<KAOAZusatzmerkmal>, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : string, __param3? : List<Schulstufe>, __param4? : List<KAOAZusatzmerkmal>, __param5? : null | number, __param6? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string")) && ((typeof __param2 !== "undefined") && (typeof __param2 === "string")) && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('java.util.List'))) || (__param3 === null)) && ((typeof __param4 !== "undefined") && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('java.util.List'))) || (__param4 === null)) && ((typeof __param5 !== "undefined") && (typeof __param5 === "number") || (__param5 === null)) && ((typeof __param6 !== "undefined") && (typeof __param6 === "number") || (__param6 === null))) {
			let id : number = __param0 as number;
			let kuerzel : string = __param1;
			let beschreibung : string = __param2;
			let stufen : List<Schulstufe> = cast_java_util_List(__param3);
			let anzeigeZusatzmerkmal : List<KAOAZusatzmerkmal> = cast_java_util_List(__param4);
			let gueltigVon : number | null = __param5;
			let gueltigBis : number | null = __param6;
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
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : KAOAAnschlussoptionEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"beschreibung" : ' + '"' + obj.beschreibung! + '"' + ',';
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
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
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
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + '"' + obj.beschreibung + '"' + ',';
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
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_kaoa_KAOAAnschlussoptionEintrag(obj : unknown) : KAOAAnschlussoptionEintrag {
	return obj as KAOAAnschlussoptionEintrag;
}
