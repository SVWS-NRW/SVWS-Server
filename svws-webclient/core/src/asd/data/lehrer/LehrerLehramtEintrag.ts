import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { LehrerLehrbefaehigungEintrag } from '../../../asd/data/lehrer/LehrerLehrbefaehigungEintrag';
import { LehrerFachrichtungEintrag } from '../../../asd/data/lehrer/LehrerFachrichtungEintrag';

export class LehrerLehramtEintrag extends JavaObject {

	/**
	 * Die ID des Lehramt-Eintrags.
	 */
	public id : number = 0;

	/**
	 * Die ID des Lehrers.
	 */
	public idLehrer : number = 0;

	/**
	 * Die Katalog-ID des Lehramtes.
	 */
	public idKatalogLehramt : number = 0;

	/**
	 * Die ID des Anerkennungsgrund für das Lehramt.
	 */
	public idAnerkennungsgrund : number | null = null;

	/**
	 * Die Fachrichtungen des Lehrers für diesen Lehramteintrag.
	 */
	public readonly fachrichtungen : List<LehrerFachrichtungEintrag> = new ArrayList<LehrerFachrichtungEintrag>();

	/**
	 * Die Lehrbefähigungen des Lehrers für diesen Lehramteintrag.
	 */
	public readonly lehrbefaehigungen : List<LehrerLehrbefaehigungEintrag> = new ArrayList<LehrerLehrbefaehigungEintrag>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag'].includes(name);
	}

	public static class = new Class<LehrerLehramtEintrag>('de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag');

	public static transpilerFromJSON(json : string): LehrerLehramtEintrag {
		const obj = JSON.parse(json) as Partial<LehrerLehramtEintrag>;
		const result = new LehrerLehramtEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (obj.idKatalogLehramt === undefined)
			throw new Error('invalid json format, missing attribute idKatalogLehramt');
		result.idKatalogLehramt = obj.idKatalogLehramt;
		result.idAnerkennungsgrund = (obj.idAnerkennungsgrund === undefined) ? null : obj.idAnerkennungsgrund === null ? null : obj.idAnerkennungsgrund;
		if (obj.fachrichtungen !== undefined) {
			for (const elem of obj.fachrichtungen) {
				result.fachrichtungen.add(LehrerFachrichtungEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lehrbefaehigungen !== undefined) {
			for (const elem of obj.lehrbefaehigungen) {
				result.lehrbefaehigungen.add(LehrerLehrbefaehigungEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : LehrerLehramtEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		result += '"idKatalogLehramt" : ' + obj.idKatalogLehramt.toString() + ',';
		result += '"idAnerkennungsgrund" : ' + ((obj.idAnerkennungsgrund === null) ? 'null' : obj.idAnerkennungsgrund.toString()) + ',';
		result += '"fachrichtungen" : [ ';
		for (let i = 0; i < obj.fachrichtungen.size(); i++) {
			const elem = obj.fachrichtungen.get(i);
			result += LehrerFachrichtungEintrag.transpilerToJSON(elem);
			if (i < obj.fachrichtungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lehrbefaehigungen" : [ ';
		for (let i = 0; i < obj.lehrbefaehigungen.size(); i++) {
			const elem = obj.lehrbefaehigungen.get(i);
			result += LehrerLehrbefaehigungEintrag.transpilerToJSON(elem);
			if (i < obj.lehrbefaehigungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerLehramtEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		}
		if (obj.idKatalogLehramt !== undefined) {
			result += '"idKatalogLehramt" : ' + obj.idKatalogLehramt.toString() + ',';
		}
		if (obj.idAnerkennungsgrund !== undefined) {
			result += '"idAnerkennungsgrund" : ' + ((obj.idAnerkennungsgrund === null) ? 'null' : obj.idAnerkennungsgrund.toString()) + ',';
		}
		if (obj.fachrichtungen !== undefined) {
			result += '"fachrichtungen" : [ ';
			for (let i = 0; i < obj.fachrichtungen.size(); i++) {
				const elem = obj.fachrichtungen.get(i);
				result += LehrerFachrichtungEintrag.transpilerToJSON(elem);
				if (i < obj.fachrichtungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lehrbefaehigungen !== undefined) {
			result += '"lehrbefaehigungen" : [ ';
			for (let i = 0; i < obj.lehrbefaehigungen.size(); i++) {
				const elem = obj.lehrbefaehigungen.get(i);
				result += LehrerLehrbefaehigungEintrag.transpilerToJSON(elem);
				if (i < obj.lehrbefaehigungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_lehrer_LehrerLehramtEintrag(obj : unknown) : LehrerLehramtEintrag {
	return obj as LehrerLehramtEintrag;
}
