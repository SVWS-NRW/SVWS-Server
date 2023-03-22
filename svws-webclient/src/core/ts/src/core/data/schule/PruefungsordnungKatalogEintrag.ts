import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class PruefungsordnungKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Eintrags.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel der Ausbildungs und/oder Prüfungsordnung
	 */
	public kuerzel : string = "";

	/**
	 * Das Kürzel der Ausbildungs und/oder Prüfungsordnung, wie es in Schild NRW verwendet wird
	 */
	public kuerzelSchild : string | null = "";

	/**
	 * Die Bezeichnung der Verordnung.
	 */
	public bezeichnung : string = "";

	/**
	 * Gesetz- und Verordnungsblatt: Das Jahr in dem die Verordnung veröffentlich wurde
	 */
	public gvJahr : number | null = null;

	/**
	 * Gesetz- und Verordnungsblatt: Die Nummer im Jahr der Veröffentlichung
	 */
	public gvNr : string = "";

	/**
	 * Gesetz- und Verordnungsblatt: ggf. die Seitenangaben im Jahr der Veröffentlichung
	 */
	public gvSeiten : string = "";

	/**
	 * ggf. ein Link zu einer Version der Verordnung
	 */
	public link : string = "";

	/**
	 * Gibt an, in welchem Schuljahr die Verordnung einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr die Verordnung gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id                 die ID
	 * @param kuerzel            das Kürzel 
	 * @param kuerzelSchild      das Kürzel der Ausbildungs und/oder Prüfungsordnung, wie es in Schild NRW verwendet wird
	 * @param bezeichnung        die Bezeichnung
	 * @param gvJahr             Gesetz- und Verordnungsblatt: Das Jahr in dem die Verordnung veröffentlich wurde
	 * @param gvNr               Gesetz- und Verordnungsblatt: Die Nummer im Jahr der Veröffentlichung
	 * @param gvSeiten           Gesetz- und Verordnungsblatt: ggf. die Seitenangaben im Jahr der Veröffentlichung
	 * @param link               ggf. ein Link zu einer Version der Verordnung
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : string, kuerzelSchild : string | null, bezeichnung : string, gvJahr : number | null, gvNr : string, gvSeiten : string, link : string, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : null | string, __param3? : string, __param4? : null | number, __param5? : string, __param6? : string, __param7? : string, __param8? : null | number, __param9? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined") && (typeof __param7 === "undefined") && (typeof __param8 === "undefined") && (typeof __param9 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string")) && ((typeof __param2 !== "undefined") && (typeof __param2 === "string") || (__param2 === null)) && ((typeof __param3 !== "undefined") && (typeof __param3 === "string")) && ((typeof __param4 !== "undefined") && (typeof __param4 === "number") || (__param4 === null)) && ((typeof __param5 !== "undefined") && (typeof __param5 === "string")) && ((typeof __param6 !== "undefined") && (typeof __param6 === "string")) && ((typeof __param7 !== "undefined") && (typeof __param7 === "string")) && ((typeof __param8 !== "undefined") && (typeof __param8 === "number") || (__param8 === null)) && ((typeof __param9 !== "undefined") && (typeof __param9 === "number") || (__param9 === null))) {
			let id : number = __param0 as number;
			let kuerzel : string = __param1;
			let kuerzelSchild : string | null = __param2;
			let bezeichnung : string = __param3;
			let gvJahr : number | null = __param4;
			let gvNr : string = __param5;
			let gvSeiten : string = __param6;
			let link : string = __param7;
			let gueltigVon : number | null = __param8;
			let gueltigBis : number | null = __param9;
			this.id = id;
			this.kuerzel = kuerzel;
			this.kuerzelSchild = kuerzelSchild;
			this.bezeichnung = bezeichnung;
			this.gvJahr = gvJahr;
			this.gvNr = gvNr;
			this.gvSeiten = gvSeiten;
			this.link = link;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.PruefungsordnungKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): PruefungsordnungKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new PruefungsordnungKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		result.kuerzelSchild = typeof obj.kuerzelSchild === "undefined" ? null : obj.kuerzelSchild === null ? null : obj.kuerzelSchild;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		result.gvJahr = typeof obj.gvJahr === "undefined" ? null : obj.gvJahr === null ? null : obj.gvJahr;
		if (typeof obj.gvNr === "undefined")
			 throw new Error('invalid json format, missing attribute gvNr');
		result.gvNr = obj.gvNr;
		if (typeof obj.gvSeiten === "undefined")
			 throw new Error('invalid json format, missing attribute gvSeiten');
		result.gvSeiten = obj.gvSeiten;
		if (typeof obj.link === "undefined")
			 throw new Error('invalid json format, missing attribute link');
		result.link = obj.link;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : PruefungsordnungKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"kuerzelSchild" : ' + ((!obj.kuerzelSchild) ? 'null' : '"' + obj.kuerzelSchild + '"') + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung! + '"' + ',';
		result += '"gvJahr" : ' + ((!obj.gvJahr) ? 'null' : obj.gvJahr) + ',';
		result += '"gvNr" : ' + '"' + obj.gvNr! + '"' + ',';
		result += '"gvSeiten" : ' + '"' + obj.gvSeiten! + '"' + ',';
		result += '"link" : ' + '"' + obj.link! + '"' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<PruefungsordnungKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.kuerzelSchild !== "undefined") {
			result += '"kuerzelSchild" : ' + ((!obj.kuerzelSchild) ? 'null' : '"' + obj.kuerzelSchild + '"') + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung + '"' + ',';
		}
		if (typeof obj.gvJahr !== "undefined") {
			result += '"gvJahr" : ' + ((!obj.gvJahr) ? 'null' : obj.gvJahr) + ',';
		}
		if (typeof obj.gvNr !== "undefined") {
			result += '"gvNr" : ' + '"' + obj.gvNr + '"' + ',';
		}
		if (typeof obj.gvSeiten !== "undefined") {
			result += '"gvSeiten" : ' + '"' + obj.gvSeiten + '"' + ',';
		}
		if (typeof obj.link !== "undefined") {
			result += '"link" : ' + '"' + obj.link + '"' + ',';
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

export function cast_de_nrw_schule_svws_core_data_schule_PruefungsordnungKatalogEintrag(obj : unknown) : PruefungsordnungKatalogEintrag {
	return obj as PruefungsordnungKatalogEintrag;
}
