import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Betriebsart extends JavaObject {

	/**
	 * Die ID der Betriebsart.
	 */
	public id: number = 0;

	/**
	 * Die Bezeichnung der Betriebsart.
	 */
	public bezeichnung: string = "";

	/**
	 * Gibt an, ob die Betriebsart in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar: boolean = false;

	/**
	 * Die Sortierreihenfolge der Betriebsart.
	 */
	public sortierung: number = 0;

	/**
	 * Gibt an, ob die Betriebsart in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen: boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Betriebsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.Betriebsart'].includes(name);
	}

	public static readonly class = new Class<Betriebsart>('de.svws_nrw.core.data.schule.Betriebsart');

	public static transpilerFromJSON(json: string): Betriebsart {
		const obj = JSON.parse(json) as Partial<Betriebsart>;
		const result = new Betriebsart();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.referenziertInAnderenTabellen === undefined)
			throw new Error('invalid json format, missing attribute referenziertInAnderenTabellen');
		result.referenziertInAnderenTabellen = obj.referenziertInAnderenTabellen;
		return result;
	}

	public static transpilerToJSON(obj: Betriebsart): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<Betriebsart>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
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

export function cast_de_svws_nrw_core_data_schule_Betriebsart(obj: unknown): Betriebsart {
	return obj as Betriebsart;
}
