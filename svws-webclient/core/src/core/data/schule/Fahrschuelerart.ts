import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Fahrschuelerart extends JavaObject {

	/**
	 * Die ID der Fahrschülerart.
	 */
	public id: number = 0;

	/**
	 * Die Bezeichnung der Fahrschülerart.
	 */
	public bezeichnung: string | null = null;

	/**
	 * gibt an, ob die Fahrschülerart in der Anwendung sichtbar sein soll oder nicht
	 */
	public istSichtbar: boolean = false;

	/**
	 * Gibt an, ob die Fahrschülerart in der Anwendung änderbar sein soll oder nicht.
	 */
	public istAenderbar: boolean = false;

	/**
	 * Gibt die Position in der Sortierreihenfolge für die Fahrschülerarten an.
	 */
	public sortierung: number = 0;

	/**
	 * Gibt an, ob die Fahrschuelerart in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen: boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Fahrschuelerart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.Fahrschuelerart'].includes(name);
	}

	public static readonly class = new Class<Fahrschuelerart>('de.svws_nrw.core.data.schule.Fahrschuelerart');

	public static transpilerFromJSON(json: string): Fahrschuelerart {
		const obj = JSON.parse(json) as Partial<Fahrschuelerart>;
		const result = new Fahrschuelerart();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (obj.istAenderbar === undefined)
			throw new Error('invalid json format, missing attribute istAenderbar');
		result.istAenderbar = obj.istAenderbar;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.referenziertInAnderenTabellen === undefined)
			throw new Error('invalid json format, missing attribute referenziertInAnderenTabellen');
		result.referenziertInAnderenTabellen = obj.referenziertInAnderenTabellen;
		return result;
	}

	public static transpilerToJSON(obj: Fahrschuelerart): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"istAenderbar" : ' + obj.istAenderbar.toString() + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<Fahrschuelerart>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		if (obj.istAenderbar !== undefined) {
			result += '"istAenderbar" : ' + obj.istAenderbar.toString() + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.referenziertInAnderenTabellen !== undefined) {
			result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Fahrschuelerart(obj: unknown): Fahrschuelerart {
	return obj as Fahrschuelerart;
}
