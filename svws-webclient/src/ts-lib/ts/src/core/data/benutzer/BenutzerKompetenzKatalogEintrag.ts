import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { BenutzerKompetenzGruppe, cast_de_nrw_schule_svws_core_types_benutzer_BenutzerKompetenzGruppe } from '../../../core/types/benutzer/BenutzerKompetenzGruppe';

export class BenutzerKompetenzKatalogEintrag extends JavaObject {

	public id : number = -1;

	public gruppe_id : number = -1;

	public bezeichnung : String = "";


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id             die ID
	 * @param gruppe         die Gruppe, welcher die Benutzerkompetenz zugeordnet ist
	 * @param bezeichnung    die Bezeichnung der Benutzerkompetenz
	 */
	public constructor(id : number, gruppe : BenutzerKompetenzGruppe, bezeichnung : String);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : BenutzerKompetenzGruppe, __param2? : String) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenzGruppe')))) && ((typeof __param2 !== "undefined") && ((__param2 instanceof String) || (typeof __param2 === "string")))) {
			let id : number = __param0 as number;
			let gruppe : BenutzerKompetenzGruppe = cast_de_nrw_schule_svws_core_types_benutzer_BenutzerKompetenzGruppe(__param1);
			let bezeichnung : String = __param2;
			this.id = id;
			this.bezeichnung = bezeichnung;
			this.gruppe_id = gruppe.daten.id;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.benutzer.BenutzerKompetenzKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): BenutzerKompetenzKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new BenutzerKompetenzKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.gruppe_id === "undefined")
			 throw new Error('invalid json format, missing attribute gruppe_id');
		result.gruppe_id = obj.gruppe_id;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = String(obj.bezeichnung);
		return result;
	}

	public static transpilerToJSON(obj : BenutzerKompetenzKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"gruppe_id" : ' + obj.gruppe_id + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzerKompetenzKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.gruppe_id !== "undefined") {
			result += '"gruppe_id" : ' + obj.gruppe_id + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_benutzer_BenutzerKompetenzKatalogEintrag(obj : unknown) : BenutzerKompetenzKatalogEintrag {
	return obj as BenutzerKompetenzKatalogEintrag;
}
