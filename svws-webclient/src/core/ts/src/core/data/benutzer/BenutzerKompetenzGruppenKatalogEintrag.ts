import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class BenutzerKompetenzGruppenKatalogEintrag extends JavaObject {

	/**
	 * Die ID der Kompetenzgruppe. 
	 */
	public id : number = -1;

	/**
	 * Die Bezeichnung der Kompetenzgruppe. 
	 */
	public bezeichnung : string = "";

	/**
	 * Die Spalte bei der Darstellung der Benutzerverwaltung in Schild. 
	 */
	public spalte : number = -1;

	/**
	 * Die Zeile bei der Darstellung der Benutzerverwaltung in Schild. 
	 */
	public zeile : number = -1;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id             die ID der Benutzerkompetenz-Gruppe
	 * @param bezeichnung    die Bezeichnung der Benutzerkompetenz-Gruppe
	 * @param spalte         die Spalte bei der Darstellung der Benutzerverwaltung in Schild
	 * @param zeile          die Zeile bei der Darstellung der Benutzerverwaltung in Schild
	 *  
	 */
	public constructor(id : number, bezeichnung : string, spalte : number, zeile : number);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : number, __param3? : number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string")) && ((typeof __param2 !== "undefined") && typeof __param2 === "number") && ((typeof __param3 !== "undefined") && typeof __param3 === "number")) {
			let id : number = __param0 as number;
			let bezeichnung : string = __param1;
			let spalte : number = __param2 as number;
			let zeile : number = __param3 as number;
			this.id = id;
			this.bezeichnung = bezeichnung;
			this.spalte = spalte;
			this.zeile = zeile;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.benutzer.BenutzerKompetenzGruppenKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): BenutzerKompetenzGruppenKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new BenutzerKompetenzGruppenKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.spalte === "undefined")
			 throw new Error('invalid json format, missing attribute spalte');
		result.spalte = obj.spalte;
		if (typeof obj.zeile === "undefined")
			 throw new Error('invalid json format, missing attribute zeile');
		result.zeile = obj.zeile;
		return result;
	}

	public static transpilerToJSON(obj : BenutzerKompetenzGruppenKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung! + '"' + ',';
		result += '"spalte" : ' + obj.spalte + ',';
		result += '"zeile" : ' + obj.zeile + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzerKompetenzGruppenKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung + '"' + ',';
		}
		if (typeof obj.spalte !== "undefined") {
			result += '"spalte" : ' + obj.spalte + ',';
		}
		if (typeof obj.zeile !== "undefined") {
			result += '"zeile" : ' + obj.zeile + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_benutzer_BenutzerKompetenzGruppenKatalogEintrag(obj : unknown) : BenutzerKompetenzGruppenKatalogEintrag {
	return obj as BenutzerKompetenzGruppenKatalogEintrag;
}
