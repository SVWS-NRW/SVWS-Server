import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class NotenKatalogEintrag extends JavaObject {

	public id : number = -1;

	public sortierung : number = -1;

	public notenpunkte : Number | null = null;

	public kuerzel : String = "";

	public text : String = "";

	public textZeugnis : String = "";

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	/**
	 * Erstellt einen Noten-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Noten-Eintrag mit den angegebenen Werten
	 * 
	 * @param id              die ID
	 * @param sortierung      die Nummer für die Sortierung der Noten-Einträge
	 * @param notenpunkte     die Notenpunkte 
	 * @param kuerzel         das Kürzel 
	 * @param text            der Text für die Notenbeschreibung 
	 * @param textZeugnis     das Text, welcher auf dem Zeugnis erscheint
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, sortierung : number, notenpunkte : Number | null, kuerzel : String, text : String, textZeugnis : String, gueltigVon : Number | null, gueltigBis : Number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : number, __param2? : Number | null, __param3? : String, __param4? : String, __param5? : String, __param6? : Number | null, __param7? : Number | null) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined") && (typeof __param7 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number") && ((typeof __param2 !== "undefined") && ((__param2 instanceof Number) || (typeof __param2 === "number")) || (__param2 === null)) && ((typeof __param3 !== "undefined") && ((__param3 instanceof String) || (typeof __param3 === "string"))) && ((typeof __param4 !== "undefined") && ((__param4 instanceof String) || (typeof __param4 === "string"))) && ((typeof __param5 !== "undefined") && ((__param5 instanceof String) || (typeof __param5 === "string"))) && ((typeof __param6 !== "undefined") && ((__param6 instanceof Number) || (typeof __param6 === "number")) || (__param6 === null)) && ((typeof __param7 !== "undefined") && ((__param7 instanceof Number) || (typeof __param7 === "number")) || (__param7 === null))) {
			let id : number = __param0 as number;
			let sortierung : number = __param1 as number;
			let notenpunkte : Number | null = cast_java_lang_Integer(__param2);
			let kuerzel : String = __param3;
			let text : String = __param4;
			let textZeugnis : String = __param5;
			let gueltigVon : Number | null = cast_java_lang_Integer(__param6);
			let gueltigBis : Number | null = cast_java_lang_Integer(__param7);
			this.id = id;
			this.sortierung = sortierung;
			this.notenpunkte = notenpunkte;
			this.kuerzel = kuerzel;
			this.text = text;
			this.textZeugnis = textZeugnis;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.NotenKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): NotenKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new NotenKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		result.notenpunkte = typeof obj.notenpunkte === "undefined" ? null : obj.notenpunkte === null ? null : Number(obj.notenpunkte);
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = String(obj.kuerzel);
		if (typeof obj.text === "undefined")
			 throw new Error('invalid json format, missing attribute text');
		result.text = String(obj.text);
		if (typeof obj.textZeugnis === "undefined")
			 throw new Error('invalid json format, missing attribute textZeugnis');
		result.textZeugnis = String(obj.textZeugnis);
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : Number(obj.gueltigVon);
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : Number(obj.gueltigBis);
		return result;
	}

	public static transpilerToJSON(obj : NotenKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"notenpunkte" : ' + ((!obj.notenpunkte) ? 'null' : obj.notenpunkte.valueOf()) + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		result += '"text" : ' + '"' + obj.text.valueOf() + '"' + ',';
		result += '"textZeugnis" : ' + '"' + obj.textZeugnis.valueOf() + '"' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<NotenKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.notenpunkte !== "undefined") {
			result += '"notenpunkte" : ' + ((!obj.notenpunkte) ? 'null' : obj.notenpunkte.valueOf()) + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.text !== "undefined") {
			result += '"text" : ' + '"' + obj.text.valueOf() + '"' + ',';
		}
		if (typeof obj.textZeugnis !== "undefined") {
			result += '"textZeugnis" : ' + '"' + obj.textZeugnis.valueOf() + '"' + ',';
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

export function cast_de_nrw_schule_svws_core_data_schule_NotenKatalogEintrag(obj : unknown) : NotenKatalogEintrag {
	return obj as NotenKatalogEintrag;
}
