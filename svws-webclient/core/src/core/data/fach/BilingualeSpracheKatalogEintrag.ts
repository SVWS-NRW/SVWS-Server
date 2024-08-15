import { JavaObject } from '../../../java/lang/JavaObject';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import { ZulaessigesFach, cast_de_svws_nrw_core_types_fach_ZulaessigesFach } from '../../../core/types/fach/ZulaessigesFach';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';

export class BilingualeSpracheKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Das einstellige Kürzel der Fremdsprache
	 */
	public kuerzel : string = "";

	/**
	 * Die Kürzel der Schulformen, wo die Sprache als bilinguale Fremdsprache zulässig ist.
	 */
	public schulformen : List<string> = new ArrayList<string>();

	/**
	 * Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 *
	 * @param id                    die ID
	 * @param fach                  das Fremdsprachenfach
	 * @param schulformen           die Schulformen, wo die Sprache als bilinguale Fremdsprache zulässig ist
	 * @param gueltigVon            das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und
	 *                              "schon immer gültig war"
	 * @param gueltigBis            das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, fach : ZulaessigesFach, schulformen : List<Schulform>, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : ZulaessigesFach, __param2? : List<Schulform>, __param3? : null | number, __param4? : null | number) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.svws_nrw.core.types.fach.ZulaessigesFach')))) && ((__param2 !== undefined) && ((__param2 instanceof JavaObject) && (__param2.isTranspiledInstanceOf('java.util.List'))) || (__param2 === null)) && ((__param3 !== undefined) && (typeof __param3 === "number") || (__param3 === null)) && ((__param4 !== undefined) && (typeof __param4 === "number") || (__param4 === null))) {
			const id : number = __param0 as number;
			const fach : ZulaessigesFach = cast_de_svws_nrw_core_types_fach_ZulaessigesFach(__param1);
			const schulformen : List<Schulform> = cast_java_util_List(__param2);
			const gueltigVon : number | null = __param3;
			const gueltigBis : number | null = __param4;
			this.id = id;
			this.kuerzel = fach.daten.kuerzel;
			for (const schulform of schulformen)
				this.schulformen.add(schulform.daten.kuerzel);
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.fach.BilingualeSpracheKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.fach.BilingualeSpracheKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): BilingualeSpracheKatalogEintrag {
		const obj = JSON.parse(json) as Partial<BilingualeSpracheKatalogEintrag>;
		const result = new BilingualeSpracheKatalogEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.schulformen !== undefined) {
			for (const elem of obj.schulformen) {
				result.schulformen.add(elem);
			}
		}
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : BilingualeSpracheKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"schulformen" : [ ';
		for (let i = 0; i < obj.schulformen.size(); i++) {
			const elem = obj.schulformen.get(i);
			result += '"' + elem + '"';
			if (i < obj.schulformen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BilingualeSpracheKatalogEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.schulformen !== undefined) {
			result += '"schulformen" : [ ';
			for (let i = 0; i < obj.schulformen.size(); i++) {
				const elem = obj.schulformen.get(i);
				result += '"' + elem + '"';
				if (i < obj.schulformen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_fach_BilingualeSpracheKatalogEintrag(obj : unknown) : BilingualeSpracheKatalogEintrag {
	return obj as BilingualeSpracheKatalogEintrag;
}
