import { JavaObject } from '../../../java/lang/JavaObject';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { CoreTypeData } from '../../../core/data/CoreTypeData';

export class SchulstufeKatalogEintrag extends CoreTypeData {

	/**
	 * Das eindeutige Kürzel des Katalog-Eintrags.
	 */
	public kuerzel : string = "";

	/**
	 * Die Bezeichnung des Katalog-Eintrags.
	 */
	public bezeichnung : string = "";

	/**
	 * Die Kürzel der Schulformen die bei der Schulstufe vorkommen.
	 */
	public schulformen : List<string> = new ArrayList();


	/**
	 * Erstellt einen Schulstufen-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Schulstufen-Eintrag mit den angegebenen Werten
	 *
	 * @param id              die ID
	 * @param kuerzel         das Kürzel
	 * @param bezeichnung     die Bezeichnung
	 * @param schulformen     die Kürzel der Schulformen, bei welchen die Schulgliederung vorkommt
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : string, bezeichnung : string, schulformen : List<Schulform>, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : string, __param3? : List<Schulform>, __param4? : null | number, __param5? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined")) {
			// empty method body
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string")) && ((typeof __param2 !== "undefined") && (typeof __param2 === "string")) && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && ((__param3 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param3 === null)) && ((typeof __param4 !== "undefined") && (typeof __param4 === "number") || (__param4 === null)) && ((typeof __param5 !== "undefined") && (typeof __param5 === "number") || (__param5 === null))) {
			const id : number = __param0 as number;
			const kuerzel : string = __param1;
			const bezeichnung : string = __param2;
			const schulformen : List<Schulform> = cast_java_util_List(__param3);
			const gueltigVon : number | null = __param4;
			const gueltigBis : number | null = __param5;
			this.id = id;
			this.kuerzel = kuerzel;
			this.bezeichnung = bezeichnung;
			for (const schulform of schulformen)
				this.schulformen.add(schulform.daten.kuerzel);
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.SchulstufeKatalogEintrag', 'de.svws_nrw.core.data.CoreTypeData'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchulstufeKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new SchulstufeKatalogEintrag();
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if ((obj.schulformen !== undefined) && (obj.schulformen !== null)) {
			for (const elem of obj.schulformen) {
				result.schulformen?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchulstufeKatalogEintrag) : string {
		let result = '{';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		if (!obj.schulformen) {
			result += '"schulformen" : []';
		} else {
			result += '"schulformen" : [ ';
			for (let i = 0; i < obj.schulformen.size(); i++) {
				const elem = obj.schulformen.get(i);
				result += '"' + elem + '"';
				if (i < obj.schulformen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchulstufeKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		if (typeof obj.schulformen !== "undefined") {
			if (!obj.schulformen) {
				result += '"schulformen" : []';
			} else {
				result += '"schulformen" : [ ';
				for (let i = 0; i < obj.schulformen.size(); i++) {
					const elem = obj.schulformen.get(i);
					result += '"' + elem + '"';
					if (i < obj.schulformen.size() - 1)
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

export function cast_de_svws_nrw_core_data_schule_SchulstufeKatalogEintrag(obj : unknown) : SchulstufeKatalogEintrag {
	return obj as SchulstufeKatalogEintrag;
}
