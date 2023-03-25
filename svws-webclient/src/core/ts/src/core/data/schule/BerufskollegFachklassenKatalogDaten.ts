import { JavaObject } from '../../../java/lang/JavaObject';

export class BerufskollegFachklassenKatalogDaten extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = 0;

	/**
	 * Gibt an, ob die Fachklassen ausgelaufen ist oder nicht
	 */
	public istAusgelaufen : boolean = false;

	/**
	 * Die Gruppe des Berufsfeldes.
	 */
	public berufsfeldGruppe : string | null = null;

	/**
	 * Das Berufsfeld.
	 */
	public berufsfeld : string | null = null;

	/**
	 * Ebene 1 des Berufsfeldes
	 */
	public ebene1 : string | null = null;

	/**
	 * Ebene 2 des Berufsfeldes
	 */
	public ebene2 : string | null = null;

	/**
	 * Ebene 3 des Berufsfeldes
	 */
	public ebene3 : string | null = null;

	/**
	 * Die Bezeichnung der Fachklasse
	 */
	public bezeichnung : string = "";

	/**
	 * Die Bezeichnung der Fachklasse (männlich)
	 */
	public bezeichnungM : string = "";

	/**
	 * Die Bezeichnung der Fachklasse (weiblich)
	 */
	public bezeichnungW : string = "";

	/**
	 * Gibt an, in welchem Schuljahr die Fachklasse einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr die Fachklasse gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogDaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): BerufskollegFachklassenKatalogDaten {
		const obj = JSON.parse(json);
		const result = new BerufskollegFachklassenKatalogDaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.istAusgelaufen === "undefined")
			 throw new Error('invalid json format, missing attribute istAusgelaufen');
		result.istAusgelaufen = obj.istAusgelaufen;
		result.berufsfeldGruppe = typeof obj.berufsfeldGruppe === "undefined" ? null : obj.berufsfeldGruppe === null ? null : obj.berufsfeldGruppe;
		result.berufsfeld = typeof obj.berufsfeld === "undefined" ? null : obj.berufsfeld === null ? null : obj.berufsfeld;
		result.ebene1 = typeof obj.ebene1 === "undefined" ? null : obj.ebene1 === null ? null : obj.ebene1;
		result.ebene2 = typeof obj.ebene2 === "undefined" ? null : obj.ebene2 === null ? null : obj.ebene2;
		result.ebene3 = typeof obj.ebene3 === "undefined" ? null : obj.ebene3 === null ? null : obj.ebene3;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.bezeichnungM === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnungM');
		result.bezeichnungM = obj.bezeichnungM;
		if (typeof obj.bezeichnungW === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnungW');
		result.bezeichnungW = obj.bezeichnungW;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : BerufskollegFachklassenKatalogDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"istAusgelaufen" : ' + obj.istAusgelaufen + ',';
		result += '"berufsfeldGruppe" : ' + ((!obj.berufsfeldGruppe) ? 'null' : '"' + obj.berufsfeldGruppe + '"') + ',';
		result += '"berufsfeld" : ' + ((!obj.berufsfeld) ? 'null' : '"' + obj.berufsfeld + '"') + ',';
		result += '"ebene1" : ' + ((!obj.ebene1) ? 'null' : '"' + obj.ebene1 + '"') + ',';
		result += '"ebene2" : ' + ((!obj.ebene2) ? 'null' : '"' + obj.ebene2 + '"') + ',';
		result += '"ebene3" : ' + ((!obj.ebene3) ? 'null' : '"' + obj.ebene3 + '"') + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung! + '"' + ',';
		result += '"bezeichnungM" : ' + '"' + obj.bezeichnungM! + '"' + ',';
		result += '"bezeichnungW" : ' + '"' + obj.bezeichnungW! + '"' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BerufskollegFachklassenKatalogDaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.istAusgelaufen !== "undefined") {
			result += '"istAusgelaufen" : ' + obj.istAusgelaufen + ',';
		}
		if (typeof obj.berufsfeldGruppe !== "undefined") {
			result += '"berufsfeldGruppe" : ' + ((!obj.berufsfeldGruppe) ? 'null' : '"' + obj.berufsfeldGruppe + '"') + ',';
		}
		if (typeof obj.berufsfeld !== "undefined") {
			result += '"berufsfeld" : ' + ((!obj.berufsfeld) ? 'null' : '"' + obj.berufsfeld + '"') + ',';
		}
		if (typeof obj.ebene1 !== "undefined") {
			result += '"ebene1" : ' + ((!obj.ebene1) ? 'null' : '"' + obj.ebene1 + '"') + ',';
		}
		if (typeof obj.ebene2 !== "undefined") {
			result += '"ebene2" : ' + ((!obj.ebene2) ? 'null' : '"' + obj.ebene2 + '"') + ',';
		}
		if (typeof obj.ebene3 !== "undefined") {
			result += '"ebene3" : ' + ((!obj.ebene3) ? 'null' : '"' + obj.ebene3 + '"') + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung + '"' + ',';
		}
		if (typeof obj.bezeichnungM !== "undefined") {
			result += '"bezeichnungM" : ' + '"' + obj.bezeichnungM + '"' + ',';
		}
		if (typeof obj.bezeichnungW !== "undefined") {
			result += '"bezeichnungW" : ' + '"' + obj.bezeichnungW + '"' + ',';
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

export function cast_de_svws_nrw_core_data_schule_BerufskollegFachklassenKatalogDaten(obj : unknown) : BerufskollegFachklassenKatalogDaten {
	return obj as BerufskollegFachklassenKatalogDaten;
}
