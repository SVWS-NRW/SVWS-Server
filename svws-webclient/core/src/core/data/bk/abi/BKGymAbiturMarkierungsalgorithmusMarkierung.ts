import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class BKGymAbiturMarkierungsalgorithmusMarkierung extends JavaObject {

	/**
	 * Die ID des Faches, für welches die Markierung vorgenommen wurde
	 */
	public fachID: number = -1;

	/**
	 * Das Halbjahr der Qualifikationsphase, für welches die Markierung vorgenommen oder nicht vorgenommen wurde (2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2)
	 */
	public halbjahrID: number = -1;

	/**
	 * Die Punkte, die erreicht wurden
	 */
	public punkte: number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsalgorithmusMarkierung>('de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung');

	public static transpilerFromJSON(json: string): BKGymAbiturMarkierungsalgorithmusMarkierung {
		const obj = JSON.parse(json) as Partial<BKGymAbiturMarkierungsalgorithmusMarkierung>;
		const result = new BKGymAbiturMarkierungsalgorithmusMarkierung();
		if (obj.fachID === undefined)
			throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		if (obj.halbjahrID === undefined)
			throw new Error('invalid json format, missing attribute halbjahrID');
		result.halbjahrID = obj.halbjahrID;
		result.punkte = (obj.punkte === undefined) ? null : obj.punkte === null ? null : obj.punkte;
		return result;
	}

	public static transpilerToJSON(obj: BKGymAbiturMarkierungsalgorithmusMarkierung): string {
		let result = '{';
		result += '"fachID" : ' + obj.fachID.toString() + ',';
		result += '"halbjahrID" : ' + obj.halbjahrID.toString() + ',';
		result += '"punkte" : ' + ((obj.punkte === null) ? 'null' : obj.punkte.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<BKGymAbiturMarkierungsalgorithmusMarkierung>): string {
		let result = '{';
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + obj.fachID.toString() + ',';
		}
		if (obj.halbjahrID !== undefined) {
			result += '"halbjahrID" : ' + obj.halbjahrID.toString() + ',';
		}
		if (obj.punkte !== undefined) {
			result += '"punkte" : ' + ((obj.punkte === null) ? 'null' : obj.punkte.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_bk_abi_BKGymAbiturMarkierungsalgorithmusMarkierung(obj: unknown): BKGymAbiturMarkierungsalgorithmusMarkierung {
	return obj as BKGymAbiturMarkierungsalgorithmusMarkierung;
}
