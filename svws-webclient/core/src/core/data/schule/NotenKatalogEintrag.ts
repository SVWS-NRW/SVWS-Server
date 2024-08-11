import { JavaObject } from '../../../java/lang/JavaObject';

export class NotenKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Eine ID, die der Sortierung der Noteneinträge in einer Anwendung vorgibt
	 */
	public sortierung : number = -1;

	/**
	 * Die Notenpunkte, die dieser Note zugeordnet sind
	 */
	public notenpunkte : number | null = null;

	/**
	 * Die Kurzschreibweise der Note als Zahl ggf. mit Tendenz (+/-)
	 */
	public kuerzel : string = "";

	/**
	 * Die Note in ausführlicher Textform ggf. mit Tendenz (plus/minus)
	 */
	public text : string = "";

	/**
	 * Die Note in ausführlicher Textform, wie sie auf einem Zeugnis dargestellt wird.
	 */
	public textZeugnis : string = "";

	/**
	 * Gibt an, in welchem Schuljahr die Note einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr die Note gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	/**
	 * Erstellt einen Noten-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Noten-Eintrag mit den angegebenen Werten
	 *
	 * @param id              die ID
	 * @param sortierung      die Nummer für die Sortierung der Noten-Einträge
	 * @param notenpunkte     die Notenpunkte
	 * @param kuerzel         das Kürzel
	 * @param text            der Text für die Notenbeschreibung
	 * @param textZeugnis     das Text, welcher auf dem Zeugnis erscheint
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, sortierung : number, notenpunkte : number | null, kuerzel : string, text : string, textZeugnis : string, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : number, __param2? : null | number, __param3? : string, __param4? : string, __param5? : string, __param6? : null | number, __param7? : null | number) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined) && (__param5 === undefined) && (__param6 === undefined) && (__param7 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && typeof __param1 === "number") && ((__param2 !== undefined) && (typeof __param2 === "number") || (__param2 === null)) && ((__param3 !== undefined) && (typeof __param3 === "string")) && ((__param4 !== undefined) && (typeof __param4 === "string")) && ((__param5 !== undefined) && (typeof __param5 === "string")) && ((__param6 !== undefined) && (typeof __param6 === "number") || (__param6 === null)) && ((__param7 !== undefined) && (typeof __param7 === "number") || (__param7 === null))) {
			const id : number = __param0 as number;
			const sortierung : number = __param1 as number;
			const notenpunkte : number | null = __param2;
			const kuerzel : string = __param3;
			const text : string = __param4;
			const textZeugnis : string = __param5;
			const gueltigVon : number | null = __param6;
			const gueltigBis : number | null = __param7;
			this.id = id;
			this.sortierung = sortierung;
			this.notenpunkte = notenpunkte;
			this.kuerzel = kuerzel;
			this.text = text;
			this.textZeugnis = textZeugnis;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.NotenKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.NotenKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): NotenKatalogEintrag {
		const obj = JSON.parse(json) as Partial<NotenKatalogEintrag>;
		const result = new NotenKatalogEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		result.notenpunkte = (obj.notenpunkte === undefined) ? null : obj.notenpunkte === null ? null : obj.notenpunkte;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.text === undefined)
			throw new Error('invalid json format, missing attribute text');
		result.text = obj.text;
		if (obj.textZeugnis === undefined)
			throw new Error('invalid json format, missing attribute textZeugnis');
		result.textZeugnis = obj.textZeugnis;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : NotenKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"notenpunkte" : ' + ((!obj.notenpunkte) ? 'null' : obj.notenpunkte.toString()) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"textZeugnis" : ' + JSON.stringify(obj.textZeugnis) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<NotenKatalogEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.notenpunkte !== undefined) {
			result += '"notenpunkte" : ' + ((!obj.notenpunkte) ? 'null' : obj.notenpunkte.toString()) + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.text !== undefined) {
			result += '"text" : ' + JSON.stringify(obj.text) + ',';
		}
		if (obj.textZeugnis !== undefined) {
			result += '"textZeugnis" : ' + JSON.stringify(obj.textZeugnis) + ',';
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

export function cast_de_svws_nrw_core_data_schule_NotenKatalogEintrag(obj : unknown) : NotenKatalogEintrag {
	return obj as NotenKatalogEintrag;
}
