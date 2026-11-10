import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class GostZulassungSimulationAbiturfach extends JavaObject {

	/**
	 * Die ID des Faches
	 */
	public idFach: number = -1;

	/**
	 * Die Notenpunkte der Abiturprüfung oder null, wenn nichts eingegeben wurde
	 */
	public pruefungNotenpunkte: number | null = null;

	/**
	 * Gibt an, ob eine mündliche Bestehensprüfung angesetzt ist, oder null, wenn dies nicht festgelegt wurde
	 */
	public muendlichBestehen: boolean | null = null;

	/**
	 * Gibt an, ob eine freiwillige mündliche Prüfung angesetzt ist, oder null, wenn dies nicht festgelegt wurde
	 */
	public muendlichFreiwillig: boolean | null = null;

	/**
	 * Die Reihenfolge der mündlichen Prüfung oder null, wenn nichts eingegeben wurde
	 */
	public muendlichReihenfolge: number | null = null;

	/**
	 * Die Notenpunkte der mündlichen Prüfung oder null, wenn nichts eingegeben wurde
	 */
	public muendlichNotenpunkte: number | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostZulassungSimulationAbiturfach';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.GostZulassungSimulationAbiturfach'].includes(name);
	}

	public static readonly class = new Class<GostZulassungSimulationAbiturfach>('de.svws_nrw.core.data.gost.GostZulassungSimulationAbiturfach');

	public static transpilerFromJSON(json: string): GostZulassungSimulationAbiturfach {
		const obj = JSON.parse(json) as Partial<GostZulassungSimulationAbiturfach>;
		const result = new GostZulassungSimulationAbiturfach();
		if (obj.idFach === undefined)
			throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		result.pruefungNotenpunkte = (obj.pruefungNotenpunkte === undefined) ? null : obj.pruefungNotenpunkte === null ? null : obj.pruefungNotenpunkte;
		result.muendlichBestehen = (obj.muendlichBestehen === undefined) ? null : obj.muendlichBestehen === null ? null : obj.muendlichBestehen;
		result.muendlichFreiwillig = (obj.muendlichFreiwillig === undefined) ? null : obj.muendlichFreiwillig === null ? null : obj.muendlichFreiwillig;
		result.muendlichReihenfolge = (obj.muendlichReihenfolge === undefined) ? null : obj.muendlichReihenfolge === null ? null : obj.muendlichReihenfolge;
		result.muendlichNotenpunkte = (obj.muendlichNotenpunkte === undefined) ? null : obj.muendlichNotenpunkte === null ? null : obj.muendlichNotenpunkte;
		return result;
	}

	public static transpilerToJSON(obj: GostZulassungSimulationAbiturfach): string {
		let result = '{';
		result += '"idFach" : ' + obj.idFach.toString() + ',';
		result += '"pruefungNotenpunkte" : ' + ((obj.pruefungNotenpunkte === null) ? 'null' : obj.pruefungNotenpunkte.toString()) + ',';
		result += '"muendlichBestehen" : ' + ((obj.muendlichBestehen === null) ? 'null' : obj.muendlichBestehen.toString()) + ',';
		result += '"muendlichFreiwillig" : ' + ((obj.muendlichFreiwillig === null) ? 'null' : obj.muendlichFreiwillig.toString()) + ',';
		result += '"muendlichReihenfolge" : ' + ((obj.muendlichReihenfolge === null) ? 'null' : obj.muendlichReihenfolge.toString()) + ',';
		result += '"muendlichNotenpunkte" : ' + ((obj.muendlichNotenpunkte === null) ? 'null' : obj.muendlichNotenpunkte.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostZulassungSimulationAbiturfach>): string {
		let result = '{';
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach.toString() + ',';
		}
		if (obj.pruefungNotenpunkte !== undefined) {
			result += '"pruefungNotenpunkte" : ' + ((obj.pruefungNotenpunkte === null) ? 'null' : obj.pruefungNotenpunkte.toString()) + ',';
		}
		if (obj.muendlichBestehen !== undefined) {
			result += '"muendlichBestehen" : ' + ((obj.muendlichBestehen === null) ? 'null' : obj.muendlichBestehen.toString()) + ',';
		}
		if (obj.muendlichFreiwillig !== undefined) {
			result += '"muendlichFreiwillig" : ' + ((obj.muendlichFreiwillig === null) ? 'null' : obj.muendlichFreiwillig.toString()) + ',';
		}
		if (obj.muendlichReihenfolge !== undefined) {
			result += '"muendlichReihenfolge" : ' + ((obj.muendlichReihenfolge === null) ? 'null' : obj.muendlichReihenfolge.toString()) + ',';
		}
		if (obj.muendlichNotenpunkte !== undefined) {
			result += '"muendlichNotenpunkte" : ' + ((obj.muendlichNotenpunkte === null) ? 'null' : obj.muendlichNotenpunkte.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostZulassungSimulationAbiturfach(obj: unknown): GostZulassungSimulationAbiturfach {
	return obj as GostZulassungSimulationAbiturfach;
}
