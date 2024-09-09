import { ArrayList } from '../../../java/util/ArrayList';
import { SchulgliederungGueltigerAbschluss } from '../../../asd/data/schule/SchulgliederungGueltigerAbschluss';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { CoreTypeData } from '../../../asd/data/CoreTypeData';

export class SchulgliederungKatalogEintrag extends CoreTypeData {

	/**
	 * Gibt an, ob es sich um einen Bildungsgang am Berufskolleg handelt.
	 */
	public istBK : boolean = false;

	/**
	 * Die Kürzel der Schulformen, bei welchen die Schulgliederung vorkommt.
	 */
	public schulformen : List<string> = new ArrayList<string>();

	/**
	 * Gibt an, ob es sich um eine auslaufende Schulgliederung oder einen auslaufenden Bildungsgang handelt.
	 */
	public istAuslaufend : boolean = false;

	/**
	 * Gibt an, ob es sich um eine ausgelaufene Schulgliederung oder einen ausgelaufenen Bildungsgang handelt.
	 */
	public istAusgelaufen : boolean = false;

	/**
	 * Die Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt.
	 */
	public bkAnlage : string | null = null;

	/**
	 * Der Typ der Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt.
	 */
	public bkTyp : string | null = null;

	/**
	 * Der Index für den Zugriff auf die Fachklassen am Berufskolleg. Dieser kann bei unterschiedlichen Gliederungen identisch sein.
	 */
	public bkIndex : number | null = null;

	/**
	 * Gibt an, ob es sich um einen Bildungsgang in Vollzeit handelt oder nicht
	 */
	public istVZ : boolean = false;

	/**
	 * Gibt eine Liste von Abschlusskombinationen aus beruflichen und allgemeinbildenden Abschluss an, mit Angabe der zulässigen Jahrgänge
	 */
	public abschluesse : List<SchulgliederungGueltigerAbschluss> = new ArrayList<SchulgliederungGueltigerAbschluss>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.CoreTypeData', 'de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag'].includes(name);
	}

	public static class = new Class<SchulgliederungKatalogEintrag>('de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag');

	public static transpilerFromJSON(json : string): SchulgliederungKatalogEintrag {
		const obj = JSON.parse(json) as Partial<SchulgliederungKatalogEintrag>;
		const result = new SchulgliederungKatalogEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schluessel === undefined)
			throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.text === undefined)
			throw new Error('invalid json format, missing attribute text');
		result.text = obj.text;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		if (obj.istBK === undefined)
			throw new Error('invalid json format, missing attribute istBK');
		result.istBK = obj.istBK;
		if (obj.schulformen !== undefined) {
			for (const elem of obj.schulformen) {
				result.schulformen.add(elem);
			}
		}
		if (obj.istAuslaufend === undefined)
			throw new Error('invalid json format, missing attribute istAuslaufend');
		result.istAuslaufend = obj.istAuslaufend;
		if (obj.istAusgelaufen === undefined)
			throw new Error('invalid json format, missing attribute istAusgelaufen');
		result.istAusgelaufen = obj.istAusgelaufen;
		result.bkAnlage = (obj.bkAnlage === undefined) ? null : obj.bkAnlage === null ? null : obj.bkAnlage;
		result.bkTyp = (obj.bkTyp === undefined) ? null : obj.bkTyp === null ? null : obj.bkTyp;
		result.bkIndex = (obj.bkIndex === undefined) ? null : obj.bkIndex === null ? null : obj.bkIndex;
		if (obj.istVZ === undefined)
			throw new Error('invalid json format, missing attribute istVZ');
		result.istVZ = obj.istVZ;
		if (obj.abschluesse !== undefined) {
			for (const elem of obj.abschluesse) {
				result.abschluesse.add(SchulgliederungGueltigerAbschluss.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchulgliederungKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"istBK" : ' + obj.istBK.toString() + ',';
		result += '"schulformen" : [ ';
		for (let i = 0; i < obj.schulformen.size(); i++) {
			const elem = obj.schulformen.get(i);
			result += '"' + elem + '"';
			if (i < obj.schulformen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"istAuslaufend" : ' + obj.istAuslaufend.toString() + ',';
		result += '"istAusgelaufen" : ' + obj.istAusgelaufen.toString() + ',';
		result += '"bkAnlage" : ' + ((!obj.bkAnlage) ? 'null' : JSON.stringify(obj.bkAnlage)) + ',';
		result += '"bkTyp" : ' + ((!obj.bkTyp) ? 'null' : JSON.stringify(obj.bkTyp)) + ',';
		result += '"bkIndex" : ' + ((!obj.bkIndex) ? 'null' : obj.bkIndex.toString()) + ',';
		result += '"istVZ" : ' + obj.istVZ.toString() + ',';
		result += '"abschluesse" : [ ';
		for (let i = 0; i < obj.abschluesse.size(); i++) {
			const elem = obj.abschluesse.get(i);
			result += SchulgliederungGueltigerAbschluss.transpilerToJSON(elem);
			if (i < obj.abschluesse.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchulgliederungKatalogEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schluessel !== undefined) {
			result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.text !== undefined) {
			result += '"text" : ' + JSON.stringify(obj.text) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		}
		if (obj.istBK !== undefined) {
			result += '"istBK" : ' + obj.istBK.toString() + ',';
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
		if (obj.istAuslaufend !== undefined) {
			result += '"istAuslaufend" : ' + obj.istAuslaufend.toString() + ',';
		}
		if (obj.istAusgelaufen !== undefined) {
			result += '"istAusgelaufen" : ' + obj.istAusgelaufen.toString() + ',';
		}
		if (obj.bkAnlage !== undefined) {
			result += '"bkAnlage" : ' + ((!obj.bkAnlage) ? 'null' : JSON.stringify(obj.bkAnlage)) + ',';
		}
		if (obj.bkTyp !== undefined) {
			result += '"bkTyp" : ' + ((!obj.bkTyp) ? 'null' : JSON.stringify(obj.bkTyp)) + ',';
		}
		if (obj.bkIndex !== undefined) {
			result += '"bkIndex" : ' + ((!obj.bkIndex) ? 'null' : obj.bkIndex.toString()) + ',';
		}
		if (obj.istVZ !== undefined) {
			result += '"istVZ" : ' + obj.istVZ.toString() + ',';
		}
		if (obj.abschluesse !== undefined) {
			result += '"abschluesse" : [ ';
			for (let i = 0; i < obj.abschluesse.size(); i++) {
				const elem = obj.abschluesse.get(i);
				result += SchulgliederungGueltigerAbschluss.transpilerToJSON(elem);
				if (i < obj.abschluesse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schule_SchulgliederungKatalogEintrag(obj : unknown) : SchulgliederungKatalogEintrag {
	return obj as SchulgliederungKatalogEintrag;
}
