import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class LehrerKatalogRechtsverhaeltnisEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel für das Rechtsverhältnis. 
	 */
	public kuerzel : string = "";

	/**
	 * Der Klartext des Rechtsverhältnisses. 
	 */
	public text : string = "";

	/**
	 * Gibt an, in welchem Schuljahr das Rechtsverhältnis einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. 
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr das Rechtsverhältnis gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. 
	 */
	public gueltigBis : number | null = null;


	/**
	 * Erstellt einen Rechtsverhältnis-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Rechtsverhältnis-Eintrag mit den angegebenen Werten
	 * 
	 * @param id           die ID
	 * @param kuerzel      das Kürzel 
	 * @param text         die textuelle Beschreibung
	 * @param gueltigVon   das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis   das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : string, text : string, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : string, __param3? : null | number, __param4? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string")) && ((typeof __param2 !== "undefined") && (typeof __param2 === "string")) && ((typeof __param3 !== "undefined") && (typeof __param3 === "number") || (__param3 === null)) && ((typeof __param4 !== "undefined") && (typeof __param4 === "number") || (__param4 === null))) {
			let id : number = __param0 as number;
			let kuerzel : string = __param1;
			let text : string = __param2;
			let gueltigVon : number | null = __param3;
			let gueltigBis : number | null = __param4;
			this.id = id;
			this.kuerzel = kuerzel;
			this.text = text;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.lehrer.LehrerKatalogRechtsverhaeltnisEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): LehrerKatalogRechtsverhaeltnisEintrag {
		const obj = JSON.parse(json);
		const result = new LehrerKatalogRechtsverhaeltnisEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.text === "undefined")
			 throw new Error('invalid json format, missing attribute text');
		result.text = obj.text;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : LehrerKatalogRechtsverhaeltnisEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"text" : ' + '"' + obj.text! + '"' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerKatalogRechtsverhaeltnisEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.text !== "undefined") {
			result += '"text" : ' + '"' + obj.text + '"' + ',';
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

export function cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogRechtsverhaeltnisEintrag(obj : unknown) : LehrerKatalogRechtsverhaeltnisEintrag {
	return obj as LehrerKatalogRechtsverhaeltnisEintrag;
}
