import { JavaObject } from '../../../java/lang/JavaObject';

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
		if ((__param0 === undefined) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined) && (__param5 === undefined) && (__param6 === undefined) && (__param7 === undefined) && (__param8 === undefined) && (__param9 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && (typeof __param2 === "string") || (__param2 === null)) && ((__param3 !== undefined) && (typeof __param3 === "string")) && ((__param4 !== undefined) && (typeof __param4 === "number") || (__param4 === null)) && ((__param5 !== undefined) && (typeof __param5 === "string")) && ((__param6 !== undefined) && (typeof __param6 === "string")) && ((__param7 !== undefined) && (typeof __param7 === "string")) && ((__param8 !== undefined) && (typeof __param8 === "number") || (__param8 === null)) && ((__param9 !== undefined) && (typeof __param9 === "number") || (__param9 === null))) {
			const id : number = __param0 as number;
			const kuerzel : string = __param1;
			const kuerzelSchild : string | null = __param2;
			const bezeichnung : string = __param3;
			const gvJahr : number | null = __param4;
			const gvNr : string = __param5;
			const gvSeiten : string = __param6;
			const link : string = __param7;
			const gueltigVon : number | null = __param8;
			const gueltigBis : number | null = __param9;
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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.PruefungsordnungKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.PruefungsordnungKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): PruefungsordnungKatalogEintrag {
		const obj = JSON.parse(json) as Partial<PruefungsordnungKatalogEintrag>;
		const result = new PruefungsordnungKatalogEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		result.kuerzelSchild = (obj.kuerzelSchild === undefined) ? null : obj.kuerzelSchild === null ? null : obj.kuerzelSchild;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		result.gvJahr = (obj.gvJahr === undefined) ? null : obj.gvJahr === null ? null : obj.gvJahr;
		if (obj.gvNr === undefined)
			throw new Error('invalid json format, missing attribute gvNr');
		result.gvNr = obj.gvNr;
		if (obj.gvSeiten === undefined)
			throw new Error('invalid json format, missing attribute gvSeiten');
		result.gvSeiten = obj.gvSeiten;
		if (obj.link === undefined)
			throw new Error('invalid json format, missing attribute link');
		result.link = obj.link;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : PruefungsordnungKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"kuerzelSchild" : ' + ((!obj.kuerzelSchild) ? 'null' : JSON.stringify(obj.kuerzelSchild)) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"gvJahr" : ' + ((!obj.gvJahr) ? 'null' : obj.gvJahr.toString()) + ',';
		result += '"gvNr" : ' + JSON.stringify(obj.gvNr) + ',';
		result += '"gvSeiten" : ' + JSON.stringify(obj.gvSeiten) + ',';
		result += '"link" : ' + JSON.stringify(obj.link) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<PruefungsordnungKatalogEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.kuerzelSchild !== undefined) {
			result += '"kuerzelSchild" : ' + ((!obj.kuerzelSchild) ? 'null' : JSON.stringify(obj.kuerzelSchild)) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.gvJahr !== undefined) {
			result += '"gvJahr" : ' + ((!obj.gvJahr) ? 'null' : obj.gvJahr.toString()) + ',';
		}
		if (obj.gvNr !== undefined) {
			result += '"gvNr" : ' + JSON.stringify(obj.gvNr) + ',';
		}
		if (obj.gvSeiten !== undefined) {
			result += '"gvSeiten" : ' + JSON.stringify(obj.gvSeiten) + ',';
		}
		if (obj.link !== undefined) {
			result += '"link" : ' + JSON.stringify(obj.link) + ',';
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

export function cast_de_svws_nrw_core_data_schule_PruefungsordnungKatalogEintrag(obj : unknown) : PruefungsordnungKatalogEintrag {
	return obj as PruefungsordnungKatalogEintrag;
}
