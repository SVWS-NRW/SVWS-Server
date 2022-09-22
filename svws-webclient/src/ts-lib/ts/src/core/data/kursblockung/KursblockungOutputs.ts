import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { KursblockungOutput, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungOutput } from '../../../core/data/kursblockung/KursblockungOutput';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class KursblockungOutputs extends JavaObject {

	public blockungsRevision : number = -1;

	public outputs : Vector<KursblockungOutput> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputs'].includes(name);
	}

	public static transpilerFromJSON(json : string): KursblockungOutputs {
		const obj = JSON.parse(json);
		const result = new KursblockungOutputs();
		if (typeof obj.blockungsRevision === "undefined")
			 throw new Error('invalid json format, missing attribute blockungsRevision');
		result.blockungsRevision = obj.blockungsRevision;
		if (!!obj.outputs) {
			for (let elem of obj.outputs) {
				result.outputs?.add(KursblockungOutput.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : KursblockungOutputs) : string {
		let result = '{';
		result += '"blockungsRevision" : ' + obj.blockungsRevision + ',';
		if (!obj.outputs) {
			result += '"outputs" : []';
		} else {
			result += '"outputs" : [ ';
			for (let i : number = 0; i < obj.outputs.size(); i++) {
				let elem = obj.outputs.get(i);
				result += KursblockungOutput.transpilerToJSON(elem);
				if (i < obj.outputs.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KursblockungOutputs>) : string {
		let result = '{';
		if (typeof obj.blockungsRevision !== "undefined") {
			result += '"blockungsRevision" : ' + obj.blockungsRevision + ',';
		}
		if (typeof obj.outputs !== "undefined") {
			if (!obj.outputs) {
				result += '"outputs" : []';
			} else {
				result += '"outputs" : [ ';
				for (let i : number = 0; i < obj.outputs.size(); i++) {
					let elem = obj.outputs.get(i);
					result += KursblockungOutput.transpilerToJSON(elem);
					if (i < obj.outputs.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungOutputs(obj : unknown) : KursblockungOutputs {
	return obj as KursblockungOutputs;
}
