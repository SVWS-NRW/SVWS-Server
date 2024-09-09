import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class VerkehrsspracheKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = 0;

	/**
	 * Der dreistellige ISO 639-2 code
	 */
	public kuerzel : string = "";

	/**
	 * Die ISO-Bezeichnung der Sprache.
	 */
	public bezeichnung : string = "";

	/**
	 * Der zweistellige ISO 639-1 code (wird aktuell für die Statistik verwendet - null entspricht dem Statistik-Code xn)
	 */
	public iso2 : string | null = null;

	/**
	 * Gibt an, in welchem Schuljahr die Verkehrssprache ergänzt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr die Verkehrssprache verwendet wird. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	/**
	 * Erstellt einen Katalog-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Katalog-Eintrag mit den angegebenen Werten
	 *
	 * @param id                    die ID
	 * @param kuerzel               der dreistellige ISO 639-2 code
	 * @param bezeichnung           die ISO-Bezeichnung der Sprache
	 * @param iso2                  der zweistellige ISO 639-1 code, sofern vorhanden
	 * @param gueltigVon            das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis            das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : string, bezeichnung : string, iso2 : string | null, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : string, __param3? : null | string, __param4? : null | number, __param5? : null | number) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined) && (__param5 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && (typeof __param2 === "string")) && ((__param3 !== undefined) && (typeof __param3 === "string") || (__param3 === null)) && ((__param4 !== undefined) && (typeof __param4 === "number") || (__param4 === null)) && ((__param5 !== undefined) && (typeof __param5 === "number") || (__param5 === null))) {
			const id : number = __param0 as number;
			const kuerzel : string = __param1;
			const bezeichnung : string = __param2;
			const iso2 : string | null = __param3;
			const gueltigVon : number | null = __param4;
			const gueltigBis : number | null = __param5;
			this.id = id;
			this.kuerzel = kuerzel;
			this.bezeichnung = bezeichnung;
			this.iso2 = iso2;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.VerkehrsspracheKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.VerkehrsspracheKatalogEintrag'].includes(name);
	}

	public static class = new Class<VerkehrsspracheKatalogEintrag>('de.svws_nrw.core.data.schule.VerkehrsspracheKatalogEintrag');

	public static transpilerFromJSON(json : string): VerkehrsspracheKatalogEintrag {
		const obj = JSON.parse(json) as Partial<VerkehrsspracheKatalogEintrag>;
		const result = new VerkehrsspracheKatalogEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		result.iso2 = (obj.iso2 === undefined) ? null : obj.iso2 === null ? null : obj.iso2;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : VerkehrsspracheKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"iso2" : ' + ((!obj.iso2) ? 'null' : JSON.stringify(obj.iso2)) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<VerkehrsspracheKatalogEintrag>) : string {
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
		if (obj.iso2 !== undefined) {
			result += '"iso2" : ' + ((!obj.iso2) ? 'null' : JSON.stringify(obj.iso2)) + ',';
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

export function cast_de_svws_nrw_core_data_schule_VerkehrsspracheKatalogEintrag(obj : unknown) : VerkehrsspracheKatalogEintrag {
	return obj as VerkehrsspracheKatalogEintrag;
}
