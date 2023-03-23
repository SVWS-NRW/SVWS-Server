import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class NotenKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Eine ID, die der Sortierung der Noteneinträge in einer Anwendung vorgibt
	 */
	public sortierung : number = -1;

	/**
	 * Die Notenpunkte, die dieser Note zugeordnet sind
	 */
	public notenpunkte : number | null = null;

	/**
	 * Die Kurzschreibweise der Note als Zahl ggf. mit Tendenz (+/-)
	 */
	public kuerzel : string = "";

	/**
	 * Die Note in ausführlicher Textform ggf. mit Tendenz (plus/minus)
	 */
	public text : string = "";

	/**
	 * Die Note in ausführlicher Textform, wie sie auf einem Zeugnis dargestellt wird.
	 */
	public textZeugnis : string = "";

	/**
	 * Gibt an, in welchem Schuljahr die Note einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr die Note gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


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
	public constructor(id : number, sortierung : number, notenpunkte : number | null, kuerzel : string, text : string, textZeugnis : string, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : number, __param2? : null | number, __param3? : string, __param4? : string, __param5? : string, __param6? : null | number, __param7? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined") && (typeof __param7 === "undefined")) {
			// empty method body
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number") && ((typeof __param2 !== "undefined") && (typeof __param2 === "number") || (__param2 === null)) && ((typeof __param3 !== "undefined") && (typeof __param3 === "string")) && ((typeof __param4 !== "undefined") && (typeof __param4 === "string")) && ((typeof __param5 !== "undefined") && (typeof __param5 === "string")) && ((typeof __param6 !== "undefined") && (typeof __param6 === "number") || (__param6 === null)) && ((typeof __param7 !== "undefined") && (typeof __param7 === "number") || (__param7 === null))) {
			let id : number = __param0 as number;
			let sortierung : number = __param1 as number;
			let notenpunkte : number | null = __param2;
			let kuerzel : string = __param3;
			let text : string = __param4;
			let textZeugnis : string = __param5;
			let gueltigVon : number | null = __param6;
			let gueltigBis : number | null = __param7;
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
		result.notenpunkte = typeof obj.notenpunkte === "undefined" ? null : obj.notenpunkte === null ? null : obj.notenpunkte;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.text === "undefined")
			 throw new Error('invalid json format, missing attribute text');
		result.text = obj.text;
		if (typeof obj.textZeugnis === "undefined")
			 throw new Error('invalid json format, missing attribute textZeugnis');
		result.textZeugnis = obj.textZeugnis;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : NotenKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"notenpunkte" : ' + ((!obj.notenpunkte) ? 'null' : obj.notenpunkte) + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"text" : ' + '"' + obj.text! + '"' + ',';
		result += '"textZeugnis" : ' + '"' + obj.textZeugnis! + '"' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
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
			result += '"notenpunkte" : ' + ((!obj.notenpunkte) ? 'null' : obj.notenpunkte) + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.text !== "undefined") {
			result += '"text" : ' + '"' + obj.text + '"' + ',';
		}
		if (typeof obj.textZeugnis !== "undefined") {
			result += '"textZeugnis" : ' + '"' + obj.textZeugnis + '"' + ',';
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

export function cast_de_nrw_schule_svws_core_data_schule_NotenKatalogEintrag(obj : unknown) : NotenKatalogEintrag {
	return obj as NotenKatalogEintrag;
}
