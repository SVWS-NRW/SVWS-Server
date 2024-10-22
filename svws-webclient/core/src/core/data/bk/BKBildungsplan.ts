import { JavaObject } from '../../../java/lang/JavaObject';
import { BKFBFach } from '../../../core/data/bk/BKFBFach';
import { BKLernfeld } from '../../../core/data/bk/BKLernfeld';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { BKFachklassenSchluessel } from '../../../core/data/bk/BKFachklassenSchluessel';

export class BKBildungsplan extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Der zugehörige Bildungsgang.
	 */
	public fachklasse : BKFachklassenSchluessel = new BKFachklassenSchluessel();

	/**
	 * Die Dauer des Bildungsgangs in Halbjahren.
	 */
	public dauer : number = -1;

	/**
	 * Die zugehörigen Bündelfächer
	 */
	public fbFaecher : List<BKFBFach> = new ArrayList<BKFBFach>();

	/**
	 * Die zugehörige Liste der Lernfelder.
	 */
	public lernfelder : List<BKLernfeld> = new ArrayList<BKLernfeld>();

	/**
	 * Gibt an, in welchem Schuljahr der Historien-Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem (Einschulungs-)Schuljahr der Historien-Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.bk.BKBildungsplan';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.bk.BKBildungsplan'].includes(name);
	}

	public static class = new Class<BKBildungsplan>('de.svws_nrw.core.data.bk.BKBildungsplan');

	public static transpilerFromJSON(json : string): BKBildungsplan {
		const obj = JSON.parse(json) as Partial<BKBildungsplan>;
		const result = new BKBildungsplan();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.fachklasse === undefined)
			throw new Error('invalid json format, missing attribute fachklasse');
		result.fachklasse = BKFachklassenSchluessel.transpilerFromJSON(JSON.stringify(obj.fachklasse));
		if (obj.dauer === undefined)
			throw new Error('invalid json format, missing attribute dauer');
		result.dauer = obj.dauer;
		if (obj.fbFaecher !== undefined) {
			for (const elem of obj.fbFaecher) {
				result.fbFaecher.add(BKFBFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lernfelder !== undefined) {
			for (const elem of obj.lernfelder) {
				result.lernfelder.add(BKLernfeld.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : BKBildungsplan) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"fachklasse" : ' + BKFachklassenSchluessel.transpilerToJSON(obj.fachklasse) + ',';
		result += '"dauer" : ' + obj.dauer.toString() + ',';
		result += '"fbFaecher" : [ ';
		for (let i = 0; i < obj.fbFaecher.size(); i++) {
			const elem = obj.fbFaecher.get(i);
			result += BKFBFach.transpilerToJSON(elem);
			if (i < obj.fbFaecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lernfelder" : [ ';
		for (let i = 0; i < obj.lernfelder.size(); i++) {
			const elem = obj.lernfelder.get(i);
			result += BKLernfeld.transpilerToJSON(elem);
			if (i < obj.lernfelder.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BKBildungsplan>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.fachklasse !== undefined) {
			result += '"fachklasse" : ' + BKFachklassenSchluessel.transpilerToJSON(obj.fachklasse) + ',';
		}
		if (obj.dauer !== undefined) {
			result += '"dauer" : ' + obj.dauer.toString() + ',';
		}
		if (obj.fbFaecher !== undefined) {
			result += '"fbFaecher" : [ ';
			for (let i = 0; i < obj.fbFaecher.size(); i++) {
				const elem = obj.fbFaecher.get(i);
				result += BKFBFach.transpilerToJSON(elem);
				if (i < obj.fbFaecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lernfelder !== undefined) {
			result += '"lernfelder" : [ ';
			for (let i = 0; i < obj.lernfelder.size(); i++) {
				const elem = obj.lernfelder.get(i);
				result += BKLernfeld.transpilerToJSON(elem);
				if (i < obj.lernfelder.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
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

export function cast_de_svws_nrw_core_data_bk_BKBildungsplan(obj : unknown) : BKBildungsplan {
	return obj as BKBildungsplan;
}
