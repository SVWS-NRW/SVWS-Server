import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class LernplattformV1 extends JavaObject {

	/**
	 * Die ID einer Lernplattform
	 */
	public id: number = 0;

	/**
	 * Die Bezeichnung einer Lernplattform
	 */
	public bezeichnung: string | null = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.lernplattform.v1.LernplattformV1';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.lernplattform.v1.LernplattformV1'].includes(name);
	}

	public static readonly class = new Class<LernplattformV1>('de.svws_nrw.core.data.lernplattform.v1.LernplattformV1');

	public static transpilerFromJSON(json: string): LernplattformV1 {
		const obj = JSON.parse(json) as Partial<LernplattformV1>;
		const result = new LernplattformV1();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		return result;
	}

	public static transpilerToJSON(obj: LernplattformV1): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<LernplattformV1>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lernplattform_v1_LernplattformV1(obj: unknown): LernplattformV1 {
	return obj as LernplattformV1;
}
