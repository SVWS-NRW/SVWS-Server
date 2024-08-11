import { JavaObject } from '../../../java/lang/JavaObject';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { BenutzerKompetenzGruppe, cast_de_svws_nrw_core_types_benutzer_BenutzerKompetenzGruppe } from '../../../core/types/benutzer/BenutzerKompetenzGruppe';

export class BenutzerKompetenzKatalogEintrag extends JavaObject {

	/**
	 * Die ID der Benutzerkompetenz.
	 */
	public id : number = -1;

	/**
	 * Die ID der zugehörigen Benutzerkompetenzgruppe.
	 */
	public gruppe_id : number = -1;

	/**
	 * Die Bezeichnung der Benutzerkompetenz.
	 */
	public bezeichnung : string = "";

	/**
	 * Die Schulformen.
	 */
	public nurSchulformen : List<number> | null = null;


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
	 * @param schulformen    die Schulformen, bei denen die Kompetenz zulässig ist.
	 */
	public constructor(id : number, gruppe : BenutzerKompetenzGruppe, bezeichnung : string, schulformen : List<Schulform> | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : BenutzerKompetenzGruppe, __param2? : string, __param3? : List<Schulform> | null) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.types.benutzer.BenutzerKompetenzGruppe')))) && ((__param2 !== undefined) && (typeof __param2 === "string")) && ((__param3 !== undefined) && ((__param3 instanceof JavaObject) && ((__param3 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param3 === null))) {
			const id : number = __param0 as number;
			const gruppe : BenutzerKompetenzGruppe = cast_de_svws_nrw_core_types_benutzer_BenutzerKompetenzGruppe(__param1);
			const bezeichnung : string = __param2;
			const schulformen : List<Schulform> | null = cast_java_util_List(__param3);
			this.id = id;
			this.bezeichnung = bezeichnung;
			this.gruppe_id = gruppe.daten.id;
			if (schulformen !== null) {
				this.nurSchulformen = new ArrayList();
				for (const schulform of schulformen)
					this.nurSchulformen.add(schulform.daten.id);
			}
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.benutzer.BenutzerKompetenzKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.benutzer.BenutzerKompetenzKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): BenutzerKompetenzKatalogEintrag {
		const obj = JSON.parse(json) as Partial<BenutzerKompetenzKatalogEintrag>;
		const result = new BenutzerKompetenzKatalogEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.gruppe_id === undefined)
			throw new Error('invalid json format, missing attribute gruppe_id');
		result.gruppe_id = obj.gruppe_id;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if ((obj.nurSchulformen !== undefined) && (obj.nurSchulformen !== null)) {
			result.nurSchulformen = new ArrayList();
			for (const elem of obj.nurSchulformen) {
				result.nurSchulformen.add(elem);
			}
		} else {
			result.nurSchulformen = null;
		}
		return result;
	}

	public static transpilerToJSON(obj : BenutzerKompetenzKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"gruppe_id" : ' + obj.gruppe_id.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		if (!obj.nurSchulformen) {
			result += '"nurSchulformen" : null';
		} else {
			result += '"nurSchulformen" : [ ';
			for (let i = 0; i < obj.nurSchulformen.size(); i++) {
				const elem = obj.nurSchulformen.get(i);
				result += elem.toString();
				if (i < obj.nurSchulformen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzerKompetenzKatalogEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.gruppe_id !== undefined) {
			result += '"gruppe_id" : ' + obj.gruppe_id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.nurSchulformen !== undefined) {
			if (!obj.nurSchulformen) {
				result += '"nurSchulformen" : null';
			} else {
				result += '"nurSchulformen" : [ ';
				for (let i = 0; i < obj.nurSchulformen.size(); i++) {
					const elem = obj.nurSchulformen.get(i);
					result += elem.toString();
					if (i < obj.nurSchulformen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_benutzer_BenutzerKompetenzKatalogEintrag(obj : unknown) : BenutzerKompetenzKatalogEintrag {
	return obj as BenutzerKompetenzKatalogEintrag;
}
