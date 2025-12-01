import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class FoerderschwerpunktEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id: number = 0;

	/**
	 * Das Kürzel des Eintrags.
	 */
	public kuerzel: string = "";

	/**
	 * Das Kürzel des Eintrags im Rahmen der amtlichen Schulstatisik.
	 */
	public kuerzelStatistik: string = "";

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar: boolean = false;

	/**
	 * Die Sortierreihenfolge des Förderschwerpunkt-Eintrags.
	 */
	public sortierung: number = 0;

	/**
	 * Gibt an, ob der Förderschwerpunkt in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen: boolean | null = false;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag'].includes(name);
	}

	public static readonly class = new Class<FoerderschwerpunktEintrag>('de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag');

	public static transpilerFromJSON(json: string): FoerderschwerpunktEintrag {
		const obj = JSON.parse(json) as Partial<FoerderschwerpunktEintrag>;
		const result = new FoerderschwerpunktEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.kuerzelStatistik === undefined)
			throw new Error('invalid json format, missing attribute kuerzelStatistik');
		result.kuerzelStatistik = obj.kuerzelStatistik;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		result.referenziertInAnderenTabellen = (obj.referenziertInAnderenTabellen === undefined) ? null : obj.referenziertInAnderenTabellen === null ? null : obj.referenziertInAnderenTabellen;
		return result;
	}

	public static transpilerToJSON(obj: FoerderschwerpunktEintrag): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"kuerzelStatistik" : ' + JSON.stringify(obj.kuerzelStatistik) + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"referenziertInAnderenTabellen" : ' + ((obj.referenziertInAnderenTabellen === null) ? 'null' : obj.referenziertInAnderenTabellen.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<FoerderschwerpunktEintrag>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.kuerzelStatistik !== undefined) {
			result += '"kuerzelStatistik" : ' + JSON.stringify(obj.kuerzelStatistik) + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.referenziertInAnderenTabellen !== undefined) {
			result += '"referenziertInAnderenTabellen" : ' + ((obj.referenziertInAnderenTabellen === null) ? 'null' : obj.referenziertInAnderenTabellen.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_FoerderschwerpunktEintrag(obj: unknown): FoerderschwerpunktEintrag {
	return obj as FoerderschwerpunktEintrag;
}
