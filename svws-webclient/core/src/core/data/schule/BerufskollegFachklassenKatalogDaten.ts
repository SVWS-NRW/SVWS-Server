import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogDaten'].includes(name);
	}

	public static class = new Class<BerufskollegFachklassenKatalogDaten>('de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogDaten');

	public static transpilerFromJSON(json : string): BerufskollegFachklassenKatalogDaten {
		const obj = JSON.parse(json) as Partial<BerufskollegFachklassenKatalogDaten>;
		const result = new BerufskollegFachklassenKatalogDaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.istAusgelaufen === undefined)
			throw new Error('invalid json format, missing attribute istAusgelaufen');
		result.istAusgelaufen = obj.istAusgelaufen;
		result.berufsfeldGruppe = (obj.berufsfeldGruppe === undefined) ? null : obj.berufsfeldGruppe === null ? null : obj.berufsfeldGruppe;
		result.berufsfeld = (obj.berufsfeld === undefined) ? null : obj.berufsfeld === null ? null : obj.berufsfeld;
		result.ebene1 = (obj.ebene1 === undefined) ? null : obj.ebene1 === null ? null : obj.ebene1;
		result.ebene2 = (obj.ebene2 === undefined) ? null : obj.ebene2 === null ? null : obj.ebene2;
		result.ebene3 = (obj.ebene3 === undefined) ? null : obj.ebene3 === null ? null : obj.ebene3;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.bezeichnungM === undefined)
			throw new Error('invalid json format, missing attribute bezeichnungM');
		result.bezeichnungM = obj.bezeichnungM;
		if (obj.bezeichnungW === undefined)
			throw new Error('invalid json format, missing attribute bezeichnungW');
		result.bezeichnungW = obj.bezeichnungW;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : BerufskollegFachklassenKatalogDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"istAusgelaufen" : ' + obj.istAusgelaufen.toString() + ',';
		result += '"berufsfeldGruppe" : ' + ((obj.berufsfeldGruppe === null) ? 'null' : JSON.stringify(obj.berufsfeldGruppe)) + ',';
		result += '"berufsfeld" : ' + ((obj.berufsfeld === null) ? 'null' : JSON.stringify(obj.berufsfeld)) + ',';
		result += '"ebene1" : ' + ((obj.ebene1 === null) ? 'null' : JSON.stringify(obj.ebene1)) + ',';
		result += '"ebene2" : ' + ((obj.ebene2 === null) ? 'null' : JSON.stringify(obj.ebene2)) + ',';
		result += '"ebene3" : ' + ((obj.ebene3 === null) ? 'null' : JSON.stringify(obj.ebene3)) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"bezeichnungM" : ' + JSON.stringify(obj.bezeichnungM) + ',';
		result += '"bezeichnungW" : ' + JSON.stringify(obj.bezeichnungW) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BerufskollegFachklassenKatalogDaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.istAusgelaufen !== undefined) {
			result += '"istAusgelaufen" : ' + obj.istAusgelaufen.toString() + ',';
		}
		if (obj.berufsfeldGruppe !== undefined) {
			result += '"berufsfeldGruppe" : ' + ((obj.berufsfeldGruppe === null) ? 'null' : JSON.stringify(obj.berufsfeldGruppe)) + ',';
		}
		if (obj.berufsfeld !== undefined) {
			result += '"berufsfeld" : ' + ((obj.berufsfeld === null) ? 'null' : JSON.stringify(obj.berufsfeld)) + ',';
		}
		if (obj.ebene1 !== undefined) {
			result += '"ebene1" : ' + ((obj.ebene1 === null) ? 'null' : JSON.stringify(obj.ebene1)) + ',';
		}
		if (obj.ebene2 !== undefined) {
			result += '"ebene2" : ' + ((obj.ebene2 === null) ? 'null' : JSON.stringify(obj.ebene2)) + ',';
		}
		if (obj.ebene3 !== undefined) {
			result += '"ebene3" : ' + ((obj.ebene3 === null) ? 'null' : JSON.stringify(obj.ebene3)) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.bezeichnungM !== undefined) {
			result += '"bezeichnungM" : ' + JSON.stringify(obj.bezeichnungM) + ',';
		}
		if (obj.bezeichnungW !== undefined) {
			result += '"bezeichnungW" : ' + JSON.stringify(obj.bezeichnungW) + ',';
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

export function cast_de_svws_nrw_core_data_schule_BerufskollegFachklassenKatalogDaten(obj : unknown) : BerufskollegFachklassenKatalogDaten {
	return obj as BerufskollegFachklassenKatalogDaten;
}
