import { JavaObject } from '../../../java/lang/JavaObject';
import { Schulform, cast_de_svws_nrw_core_types_schule_Schulform } from '../../../core/types/schule/Schulform';

export class UebergangsempfehlungKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Das Kürzel der Empfehlung
	 */
	public kuerzel : string = "";

	/**
	 * Die Bezeichnung der Übergangsempfehlung.
	 */
	public bezeichnung : string = "";

	/**
	 * Der Statistikschlüssel der Übergangsempfehlung
	 */
	public schluessel : string = "";

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
	 * @param bezeichnung     die Bezeichnung
	 * @param empfehlung      die Empfehlung
	 * @param teilweise       die eingeschränkte Empfehlung
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : string, bezeichnung : string, empfehlung : Schulform | null, teilweise : Schulform | null, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : string, __param3? : Schulform | null, __param4? : Schulform | null, __param5? : null | number, __param6? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined")) {
			// empty method body
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string")) && ((typeof __param2 !== "undefined") && (typeof __param2 === "string")) && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && ((__param3 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.types.schule.Schulform'))) || (__param3 === null)) && ((typeof __param4 !== "undefined") && ((__param4 instanceof JavaObject) && ((__param4 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.types.schule.Schulform'))) || (__param4 === null)) && ((typeof __param5 !== "undefined") && (typeof __param5 === "number") || (__param5 === null)) && ((typeof __param6 !== "undefined") && (typeof __param6 === "number") || (__param6 === null))) {
			const id : number = __param0 as number;
			const kuerzel : string = __param1;
			const bezeichnung : string = __param2;
			const empfehlung : Schulform | null = cast_de_svws_nrw_core_types_schule_Schulform(__param3);
			const teilweise : Schulform | null = cast_de_svws_nrw_core_types_schule_Schulform(__param4);
			const gueltigVon : number | null = __param5;
			const gueltigBis : number | null = __param6;
			this.id = id;
			this.kuerzel = kuerzel;
			this.bezeichnung = bezeichnung;
			if (empfehlung === null) {
				this.schluessel = "****";
			} else {
				this.schluessel = empfehlung.daten.nummer;
				if (teilweise === null) {
					this.schluessel += "XX";
				} else {
					this.schluessel += teilweise.daten.nummer;
				}
			}
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.UebergangsempfehlungKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): UebergangsempfehlungKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new UebergangsempfehlungKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.schluessel === "undefined")
			 throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : UebergangsempfehlungKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel!) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<UebergangsempfehlungKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		if (typeof obj.schluessel !== "undefined") {
			result += '"schluessel" : ' + JSON.stringify(obj.schluessel!) + ',';
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

export function cast_de_svws_nrw_core_data_schueler_UebergangsempfehlungKatalogEintrag(obj : unknown) : UebergangsempfehlungKatalogEintrag {
	return obj as UebergangsempfehlungKatalogEintrag;
}
