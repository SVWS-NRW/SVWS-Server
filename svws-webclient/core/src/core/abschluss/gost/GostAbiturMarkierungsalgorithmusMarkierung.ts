import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class GostAbiturMarkierungsalgorithmusMarkierung extends JavaObject {

	/**
	 * Die ID des Faches, für welches die Markierung vorgenommen wurde
	 */
	public idFach : number = -1;

	/**
	 * Das Halbjahr der Qualifikationsphase, für welches die Markierung vorgenommen oder nicht vorgenommen wurde (2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2)
	 */
	public idHalbjahr : number = -1;

	/**
	 * Gibt an, ob der Algorithmus die Belgung markiert hat oder nicht
	 */
	public markiert : boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungsalgorithmusMarkierung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungsalgorithmusMarkierung'].includes(name);
	}

	public static class = new Class<GostAbiturMarkierungsalgorithmusMarkierung>('de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungsalgorithmusMarkierung');

	public static transpilerFromJSON(json : string): GostAbiturMarkierungsalgorithmusMarkierung {
		const obj = JSON.parse(json) as Partial<GostAbiturMarkierungsalgorithmusMarkierung>;
		const result = new GostAbiturMarkierungsalgorithmusMarkierung();
		if (obj.idFach === undefined)
			throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (obj.idHalbjahr === undefined)
			throw new Error('invalid json format, missing attribute idHalbjahr');
		result.idHalbjahr = obj.idHalbjahr;
		if (obj.markiert === undefined)
			throw new Error('invalid json format, missing attribute markiert');
		result.markiert = obj.markiert;
		return result;
	}

	public static transpilerToJSON(obj : GostAbiturMarkierungsalgorithmusMarkierung) : string {
		let result = '{';
		result += '"idFach" : ' + obj.idFach.toString() + ',';
		result += '"idHalbjahr" : ' + obj.idHalbjahr.toString() + ',';
		result += '"markiert" : ' + obj.markiert.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostAbiturMarkierungsalgorithmusMarkierung>) : string {
		let result = '{';
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach.toString() + ',';
		}
		if (obj.idHalbjahr !== undefined) {
			result += '"idHalbjahr" : ' + obj.idHalbjahr.toString() + ',';
		}
		if (obj.markiert !== undefined) {
			result += '"markiert" : ' + obj.markiert.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_GostAbiturMarkierungsalgorithmusMarkierung(obj : unknown) : GostAbiturMarkierungsalgorithmusMarkierung {
	return obj as GostAbiturMarkierungsalgorithmusMarkierung;
}
