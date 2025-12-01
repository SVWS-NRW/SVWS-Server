import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Lernplattform extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id: number = -1;

	/**
	 * Die Bezeichnung der Lernplattform.
	 */
	public bezeichnung: string = "";

	/**
	 * Gibt an, ob die Lernplattform in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen: boolean | null = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Lernplattform';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.Lernplattform'].includes(name);
	}

	public static readonly class = new Class<Lernplattform>('de.svws_nrw.core.data.schule.Lernplattform');

	public static transpilerFromJSON(json: string): Lernplattform {
		const obj = JSON.parse(json) as Partial<Lernplattform>;
		const result = new Lernplattform();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		result.referenziertInAnderenTabellen = (obj.referenziertInAnderenTabellen === undefined) ? null : obj.referenziertInAnderenTabellen === null ? null : obj.referenziertInAnderenTabellen;
		return result;
	}

	public static transpilerToJSON(obj: Lernplattform): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"referenziertInAnderenTabellen" : ' + ((obj.referenziertInAnderenTabellen === null) ? 'null' : obj.referenziertInAnderenTabellen.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<Lernplattform>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.referenziertInAnderenTabellen !== undefined) {
			result += '"referenziertInAnderenTabellen" : ' + ((obj.referenziertInAnderenTabellen === null) ? 'null' : obj.referenziertInAnderenTabellen.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Lernplattform(obj: unknown): Lernplattform {
	return obj as Lernplattform;
}
