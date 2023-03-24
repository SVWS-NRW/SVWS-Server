import { JavaObject } from '../../../java/lang/JavaObject';
import { KAOAZusatzmerkmaleOptionsarten, cast_de_nrw_schule_svws_core_types_kaoa_KAOAZusatzmerkmaleOptionsarten } from '../../../core/types/kaoa/KAOAZusatzmerkmaleOptionsarten';
import { KAOAMerkmal, cast_de_nrw_schule_svws_core_types_kaoa_KAOAMerkmal } from '../../../core/types/kaoa/KAOAMerkmal';

export class KAOAZusatzmerkmalEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Zusatzmerkmals.
	 */
	public kuerzel : string = "";

	/**
	 * Die Beschreibung des Zusatzmerkmals.
	 */
	public beschreibung : string = "";

	/**
	 * Das Merkmal, welcher das Zusatzmerkmal zugeordnet ist.
	 */
	public merkmal : string = "";

	/**
	 * Die Optionsart des Zusatzmerkmals.
	 */
	public optionsart : string | null = null;

	/**
	 * Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	/**
	 * Erstellt einen KAoA-Zusatzmerkmal-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen KAoA-Zusatzmerkmal-Eintrag mit den angegebenen Werten
	 *
	 * @param id             die ID
	 * @param kuerzel        das Kürzel
	 * @param beschreibung   die Beschreibung
	 * @param merkmal        das Merkmal, dem das Zusatzmerkmal zugeordnet ist
	 * @param optionsart     die Optionsart bei dem Zusatzmerkmal
	 * @param gueltigVon     das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis     das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : string, beschreibung : string, merkmal : KAOAMerkmal, optionsart : KAOAZusatzmerkmaleOptionsarten, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : string, __param3? : KAOAMerkmal, __param4? : KAOAZusatzmerkmaleOptionsarten, __param5? : null | number, __param6? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined")) {
			// empty block
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string")) && ((typeof __param2 !== "undefined") && (typeof __param2 === "string")) && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.kaoa.KAOAMerkmal')))) && ((typeof __param4 !== "undefined") && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.kaoa.KAOAZusatzmerkmaleOptionsarten')))) && ((typeof __param5 !== "undefined") && (typeof __param5 === "number") || (__param5 === null)) && ((typeof __param6 !== "undefined") && (typeof __param6 === "number") || (__param6 === null))) {
			const id : number = __param0 as number;
			const kuerzel : string = __param1;
			const beschreibung : string = __param2;
			const merkmal : KAOAMerkmal = cast_de_nrw_schule_svws_core_types_kaoa_KAOAMerkmal(__param3);
			const optionsart : KAOAZusatzmerkmaleOptionsarten = cast_de_nrw_schule_svws_core_types_kaoa_KAOAZusatzmerkmaleOptionsarten(__param4);
			const gueltigVon : number | null = __param5;
			const gueltigBis : number | null = __param6;
			this.id = id;
			this.kuerzel = kuerzel;
			this.beschreibung = beschreibung;
			this.merkmal = merkmal.daten.kuerzel;
			this.optionsart = optionsart.kuerzel;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kaoa.KAOAZusatzmerkmalEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): KAOAZusatzmerkmalEintrag {
		const obj = JSON.parse(json);
		const result = new KAOAZusatzmerkmalEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.beschreibung === "undefined")
			 throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		if (typeof obj.merkmal === "undefined")
			 throw new Error('invalid json format, missing attribute merkmal');
		result.merkmal = obj.merkmal;
		result.optionsart = typeof obj.optionsart === "undefined" ? null : obj.optionsart === null ? null : obj.optionsart;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : KAOAZusatzmerkmalEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"beschreibung" : ' + '"' + obj.beschreibung! + '"' + ',';
		result += '"merkmal" : ' + '"' + obj.merkmal! + '"' + ',';
		result += '"optionsart" : ' + ((!obj.optionsart) ? 'null' : '"' + obj.optionsart + '"') + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KAOAZusatzmerkmalEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + '"' + obj.beschreibung + '"' + ',';
		}
		if (typeof obj.merkmal !== "undefined") {
			result += '"merkmal" : ' + '"' + obj.merkmal + '"' + ',';
		}
		if (typeof obj.optionsart !== "undefined") {
			result += '"optionsart" : ' + ((!obj.optionsart) ? 'null' : '"' + obj.optionsart + '"') + ',';
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

export function cast_de_nrw_schule_svws_core_data_kaoa_KAOAZusatzmerkmalEintrag(obj : unknown) : KAOAZusatzmerkmalEintrag {
	return obj as KAOAZusatzmerkmalEintrag;
}
