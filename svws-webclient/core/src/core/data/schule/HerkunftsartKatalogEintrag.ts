import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { HerkunftsartKatalogEintragBezeichnung } from '../../../core/data/schule/HerkunftsartKatalogEintragBezeichnung';

export class HerkunftsartKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = 0;

	/**
	 * Das 2-stellige Kürzel der Herkunftsart
	 */
	public kuerzel : string = "";

	/**
	 * Die Bezeichnungen bei den jeweils zulässigen Schulformen.
	 */
	public bezeichnungen : List<HerkunftsartKatalogEintragBezeichnung> = new ArrayList<HerkunftsartKatalogEintragBezeichnung>();

	/**
	 * Gibt an, in welchem Schuljahr die Herkunftsart ergänzt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr die Herkunftsart verwendet wird. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	/**
	 * Erstellt einen Katalog-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Katalog-Eintrag mit den angegebenen Werten
	 *
	 * @param id              die ID des Katalog-Eintrags
	 * @param kuerzel         das 2-stellige Kürzel der Herkunftsart
	 * @param bezeichnungen   die Bezeichnungen bei den jeweils zulässigen Schulformen
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : string, bezeichnungen : List<HerkunftsartKatalogEintragBezeichnung>, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : List<HerkunftsartKatalogEintragBezeichnung>, __param3? : null | number, __param4? : null | number) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && ((__param2 instanceof JavaObject) && (__param2.isTranspiledInstanceOf('java.util.List'))) || (__param2 === null)) && ((__param3 !== undefined) && (typeof __param3 === "number") || (__param3 === null)) && ((__param4 !== undefined) && (typeof __param4 === "number") || (__param4 === null))) {
			const id : number = __param0 as number;
			const kuerzel : string = __param1;
			const bezeichnungen : List<HerkunftsartKatalogEintragBezeichnung> = cast_java_util_List(__param2);
			const gueltigVon : number | null = __param3;
			const gueltigBis : number | null = __param4;
			this.id = id;
			this.kuerzel = kuerzel;
			this.bezeichnungen = bezeichnungen;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.HerkunftsartKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.HerkunftsartKatalogEintrag'].includes(name);
	}

	public static class = new Class<HerkunftsartKatalogEintrag>('de.svws_nrw.core.data.schule.HerkunftsartKatalogEintrag');

	public static transpilerFromJSON(json : string): HerkunftsartKatalogEintrag {
		const obj = JSON.parse(json) as Partial<HerkunftsartKatalogEintrag>;
		const result = new HerkunftsartKatalogEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.bezeichnungen !== undefined) {
			for (const elem of obj.bezeichnungen) {
				result.bezeichnungen.add(HerkunftsartKatalogEintragBezeichnung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : HerkunftsartKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"bezeichnungen" : [ ';
		for (let i = 0; i < obj.bezeichnungen.size(); i++) {
			const elem = obj.bezeichnungen.get(i);
			result += HerkunftsartKatalogEintragBezeichnung.transpilerToJSON(elem);
			if (i < obj.bezeichnungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<HerkunftsartKatalogEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.bezeichnungen !== undefined) {
			result += '"bezeichnungen" : [ ';
			for (let i = 0; i < obj.bezeichnungen.size(); i++) {
				const elem = obj.bezeichnungen.get(i);
				result += HerkunftsartKatalogEintragBezeichnung.transpilerToJSON(elem);
				if (i < obj.bezeichnungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_HerkunftsartKatalogEintrag(obj : unknown) : HerkunftsartKatalogEintrag {
	return obj as HerkunftsartKatalogEintrag;
}
