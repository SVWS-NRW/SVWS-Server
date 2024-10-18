import { JavaObject } from '../../../java/lang/JavaObject';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class AllgemeineMerkmaleKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Das Kürzel des allgemeinen Merkmals
	 */
	public kuerzel : string = "";

	/**
	 * Eine kurze Bezeichnung für das allgemeine Merkmal.
	 */
	public bezeichnung : string = "";

	/**
	 * Gibt an, das das Merkmal bei der Schule gesetzt werden kann.
	 */
	public beiSchule : boolean = false;

	/**
	 * Gibt an, das das Merkmal bei einem Schüler gesetzt werden kann.
	 */
	public beiSchueler : boolean = false;

	/**
	 * Ggf. ein Kürzel, welches im Rahmen der amtlichen Schulstatistik verwendet wird.
	 */
	public kuerzelASD : string | null = "";

	/**
	 * Die Kürzel der Schulformen, bei welchen das allgemeine Merkmal vorkommen kann.
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
	 * @param id              die ID
	 * @param kuerzel         das Kürzel
	 * @param bezeichnung     die Bezeichnung des Merkmals
	 * @param beiSchule       gibt an, das das Merkmal bei der Schule gesetzt werden kann
	 * @param beiSchueler     gibt an, das das Merkmal bei einem Schüler gesetzt werden kann
	 * @param kuerzelASD      ggf. ein Kürzel, welches im Rahmen der amtlichen Schulstatistik verwendet wird
	 * @param schulformen     die Schulformen, bei welchen das allgemeine Merkmal vorkommen kann
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : string, bezeichnung : string, beiSchule : boolean, beiSchueler : boolean, kuerzelASD : string | null, schulformen : List<Schulform>, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : string, __param3? : boolean, __param4? : boolean, __param5? : null | string, __param6? : List<Schulform>, __param7? : null | number, __param8? : null | number) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined) && (__param5 === undefined) && (__param6 === undefined) && (__param7 === undefined) && (__param8 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && (typeof __param2 === "string")) && ((__param3 !== undefined) && typeof __param3 === "boolean") && ((__param4 !== undefined) && typeof __param4 === "boolean") && ((__param5 !== undefined) && (typeof __param5 === "string") || (__param5 === null)) && ((__param6 !== undefined) && ((__param6 instanceof JavaObject) && (__param6.isTranspiledInstanceOf('java.util.List'))) || (__param6 === null)) && ((__param7 !== undefined) && (typeof __param7 === "number") || (__param7 === null)) && ((__param8 !== undefined) && (typeof __param8 === "number") || (__param8 === null))) {
			const id : number = __param0 as number;
			const kuerzel : string = __param1;
			const bezeichnung : string = __param2;
			const beiSchule : boolean = __param3 as boolean;
			const beiSchueler : boolean = __param4 as boolean;
			const kuerzelASD : string | null = __param5;
			const schulformen : List<Schulform> = cast_java_util_List(__param6);
			const gueltigVon : number | null = __param7;
			const gueltigBis : number | null = __param8;
			this.id = id;
			this.kuerzel = kuerzel;
			this.bezeichnung = bezeichnung;
			this.beiSchule = beiSchule;
			this.beiSchueler = beiSchueler;
			this.kuerzelASD = kuerzelASD;
			for (const sf of schulformen)
				if (!this.schulformen.contains(sf.name()))
					this.schulformen.add(sf.name());
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.AllgemeineMerkmaleKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.AllgemeineMerkmaleKatalogEintrag'].includes(name);
	}

	public static class = new Class<AllgemeineMerkmaleKatalogEintrag>('de.svws_nrw.core.data.schule.AllgemeineMerkmaleKatalogEintrag');

	public static transpilerFromJSON(json : string): AllgemeineMerkmaleKatalogEintrag {
		const obj = JSON.parse(json) as Partial<AllgemeineMerkmaleKatalogEintrag>;
		const result = new AllgemeineMerkmaleKatalogEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.beiSchule === undefined)
			throw new Error('invalid json format, missing attribute beiSchule');
		result.beiSchule = obj.beiSchule;
		if (obj.beiSchueler === undefined)
			throw new Error('invalid json format, missing attribute beiSchueler');
		result.beiSchueler = obj.beiSchueler;
		result.kuerzelASD = (obj.kuerzelASD === undefined) ? null : obj.kuerzelASD === null ? null : obj.kuerzelASD;
		if (obj.schulformen !== undefined) {
			for (const elem of obj.schulformen) {
				result.schulformen.add(elem);
			}
		}
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : AllgemeineMerkmaleKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"beiSchule" : ' + obj.beiSchule.toString() + ',';
		result += '"beiSchueler" : ' + obj.beiSchueler.toString() + ',';
		result += '"kuerzelASD" : ' + ((!obj.kuerzelASD) ? 'null' : JSON.stringify(obj.kuerzelASD)) + ',';
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

	public static transpilerToJSONPatch(obj : Partial<AllgemeineMerkmaleKatalogEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.beiSchule !== undefined) {
			result += '"beiSchule" : ' + obj.beiSchule.toString() + ',';
		}
		if (obj.beiSchueler !== undefined) {
			result += '"beiSchueler" : ' + obj.beiSchueler.toString() + ',';
		}
		if (obj.kuerzelASD !== undefined) {
			result += '"kuerzelASD" : ' + ((!obj.kuerzelASD) ? 'null' : JSON.stringify(obj.kuerzelASD)) + ',';
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

export function cast_de_svws_nrw_core_data_schule_AllgemeineMerkmaleKatalogEintrag(obj : unknown) : AllgemeineMerkmaleKatalogEintrag {
	return obj as AllgemeineMerkmaleKatalogEintrag;
}
