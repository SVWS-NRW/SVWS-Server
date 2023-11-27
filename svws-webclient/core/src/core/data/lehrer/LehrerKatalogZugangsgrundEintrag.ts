import { JavaObject } from '../../../java/lang/JavaObject';

export class LehrerKatalogZugangsgrundEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel für den Zugangsgrund.
	 */
	public kuerzel : string = "";

	/**
	 * Kartext des Zugangsgrunds.
	 */
	public text : string = "";

	/**
	 * Der Schlüssel für die ASD-Schulstatistik.
	 */
	public schluessel : string = "";

	/**
	 * Gibt an, in welchem Schuljahr der Zugangsgrund einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr der Zugangsgrund gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	/**
	 * Erstellt einen Zugangsgrund-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Zugangsgrund-Eintrag mit den angegebenen Werten
	 *
	 * @param id           die ID
	 * @param kuerzel      das Kürzel
	 * @param text         die textuelle Beschreibung
	 * @param schluessel   der Schlüssel für die ASD-Schulstatistik
	 * @param gueltigVon   das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis   das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : string, text : string, schluessel : string, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : string, __param3? : string, __param4? : null | number, __param5? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined")) {
			// empty method body
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string")) && ((typeof __param2 !== "undefined") && (typeof __param2 === "string")) && ((typeof __param3 !== "undefined") && (typeof __param3 === "string")) && ((typeof __param4 !== "undefined") && (typeof __param4 === "number") || (__param4 === null)) && ((typeof __param5 !== "undefined") && (typeof __param5 === "number") || (__param5 === null))) {
			const id : number = __param0 as number;
			const kuerzel : string = __param1;
			const text : string = __param2;
			const schluessel : string = __param3;
			const gueltigVon : number | null = __param4;
			const gueltigBis : number | null = __param5;
			this.id = id;
			this.kuerzel = kuerzel;
			this.text = text;
			this.schluessel = schluessel;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lehrer.LehrerKatalogZugangsgrundEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): LehrerKatalogZugangsgrundEintrag {
		const obj = JSON.parse(json);
		const result = new LehrerKatalogZugangsgrundEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.text === "undefined")
			 throw new Error('invalid json format, missing attribute text');
		result.text = obj.text;
		if (typeof obj.schluessel === "undefined")
			 throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : LehrerKatalogZugangsgrundEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		result += '"text" : ' + JSON.stringify(obj.text!) + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel!) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerKatalogZugangsgrundEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		}
		if (typeof obj.text !== "undefined") {
			result += '"text" : ' + JSON.stringify(obj.text!) + ',';
		}
		if (typeof obj.schluessel !== "undefined") {
			result += '"schluessel" : ' + JSON.stringify(obj.schluessel!) + ',';
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

export function cast_de_svws_nrw_core_data_lehrer_LehrerKatalogZugangsgrundEintrag(obj : unknown) : LehrerKatalogZugangsgrundEintrag {
	return obj as LehrerKatalogZugangsgrundEintrag;
}
