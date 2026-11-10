import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class GostZulassungSimulationNote extends JavaObject {

	/**
	 * Die ID des Faches
	 */
	public idFach: number = -1;

	/**
	 * Die ID des Halbjahres der gymnasialen Oberstufe (2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2)
	 */
	public halbjahr: number = -1;

	/**
	 * Die eingegebenen Notenpunkte oder null, wenn nichts eingegeben wurde
	 */
	public notenpunkte: number | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostZulassungSimulationNote';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.GostZulassungSimulationNote'].includes(name);
	}

	public static readonly class = new Class<GostZulassungSimulationNote>('de.svws_nrw.core.data.gost.GostZulassungSimulationNote');

	public static transpilerFromJSON(json: string): GostZulassungSimulationNote {
		const obj = JSON.parse(json) as Partial<GostZulassungSimulationNote>;
		const result = new GostZulassungSimulationNote();
		if (obj.idFach === undefined)
			throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (obj.halbjahr === undefined)
			throw new Error('invalid json format, missing attribute halbjahr');
		result.halbjahr = obj.halbjahr;
		result.notenpunkte = (obj.notenpunkte === undefined) ? null : obj.notenpunkte === null ? null : obj.notenpunkte;
		return result;
	}

	public static transpilerToJSON(obj: GostZulassungSimulationNote): string {
		let result = '{';
		result += '"idFach" : ' + obj.idFach.toString() + ',';
		result += '"halbjahr" : ' + obj.halbjahr.toString() + ',';
		result += '"notenpunkte" : ' + ((obj.notenpunkte === null) ? 'null' : obj.notenpunkte.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostZulassungSimulationNote>): string {
		let result = '{';
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach.toString() + ',';
		}
		if (obj.halbjahr !== undefined) {
			result += '"halbjahr" : ' + obj.halbjahr.toString() + ',';
		}
		if (obj.notenpunkte !== undefined) {
			result += '"notenpunkte" : ' + ((obj.notenpunkte === null) ? 'null' : obj.notenpunkte.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostZulassungSimulationNote(obj: unknown): GostZulassungSimulationNote {
	return obj as GostZulassungSimulationNote;
}
