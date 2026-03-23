import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerSchwerpunkt extends JavaObject {

	/**
	 *  Die ID des Schwerpunktes
	 */
	public id: number = -1;

	/**
	 *  Die Bezeichnung des Schwerpunktes.
	 */
	public bezeichnung: string = "";

	/**
	 *  Die Sortierung des Schwerpunktes.
	 */
	public sortierung: number = 32000;

	/**
	 *  Gibt an, ob der Schwerpunkt in der UI sichtbar ist.
	 */
	public istSichtbar: boolean = true;

	/**
	 *  Gibt an, ob der Schwerpunkt in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen: boolean = false;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.kataloge.SchuelerSchwerpunkt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.kataloge.SchuelerSchwerpunkt'].includes(name);
	}

	public static readonly class = new Class<SchuelerSchwerpunkt>('de.svws_nrw.core.data.kataloge.SchuelerSchwerpunkt');

	public static transpilerFromJSON(json: string): SchuelerSchwerpunkt {
		const obj = JSON.parse(json) as Partial<SchuelerSchwerpunkt>;
		const result = new SchuelerSchwerpunkt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (obj.referenziertInAnderenTabellen === undefined)
			throw new Error('invalid json format, missing attribute referenziertInAnderenTabellen');
		result.referenziertInAnderenTabellen = obj.referenziertInAnderenTabellen;
		return result;
	}

	public static transpilerToJSON(obj: SchuelerSchwerpunkt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuelerSchwerpunkt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		if (obj.referenziertInAnderenTabellen !== undefined) {
			result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_kataloge_SchuelerSchwerpunkt(obj: unknown): SchuelerSchwerpunkt {
	return obj as SchuelerSchwerpunkt;
}
