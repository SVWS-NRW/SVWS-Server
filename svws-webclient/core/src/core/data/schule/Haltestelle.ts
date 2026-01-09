import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Haltestelle extends JavaObject {

	/**
	 * Die ID der Haltestelle
	 */
	public id: number = 0;

	/**
	 * Die Bezeichnung der Haltestelle
	 */
	public bezeichnung: string | null = null;

	/**
	 * Die Entfernung zwischen Schule und Haltestelle
	 */
	public entfernungSchule: number | null = null;

	/**
	 * Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an.
	 */
	public sortierung: number = 0;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar: boolean = false;

	/**
	 * Gibt an, ob die Haltestelle in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen: boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Haltestelle';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.Haltestelle'].includes(name);
	}

	public static readonly class = new Class<Haltestelle>('de.svws_nrw.core.data.schule.Haltestelle');

	public static transpilerFromJSON(json: string): Haltestelle {
		const obj = JSON.parse(json) as Partial<Haltestelle>;
		const result = new Haltestelle();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		result.entfernungSchule = (obj.entfernungSchule === undefined) ? null : obj.entfernungSchule === null ? null : obj.entfernungSchule;
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

	public static transpilerToJSON(obj: Haltestelle): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"entfernungSchule" : ' + ((obj.entfernungSchule === null) ? 'null' : obj.entfernungSchule.toString()) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<Haltestelle>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (obj.entfernungSchule !== undefined) {
			result += '"entfernungSchule" : ' + ((obj.entfernungSchule === null) ? 'null' : obj.entfernungSchule.toString()) + ',';
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

export function cast_de_svws_nrw_core_data_schule_Haltestelle(obj: unknown): Haltestelle {
	return obj as Haltestelle;
}
