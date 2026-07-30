import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class FachklasseEintrag extends JavaObject {

	/**
	 * Die ID
	 */
	public id: number = 0;

	/**
	 * Die Bezeichnung
	 */
	public bezeichnung: string | null = null;

	/**
	 * Das Kürzel
	 */
	public kuerzel: string | null = null;

	/**
	 * Die ID der Fachklasse (CoreType)
	 */
	public idFachklasse: number | null = null;

	/**
	 * Die ID der Schulgliederung
	 */
	public idSchulgliederung: number | null = null;

	/**
	 * Die Sichtbarkeit
	 */
	public istSichtbar: boolean = false;

	/**
	 * Die Sortierung
	 */
	public sortierung: number = 0;

	/**
	 *  Gibt an, ob die Fachklasse in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen: boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.FachklasseEintrag';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.FachklasseEintrag'].includes(name);
	}

	public static readonly class = new Class<FachklasseEintrag>('de.svws_nrw.core.data.schule.FachklasseEintrag');

	public static transpilerFromJSON(json: string): FachklasseEintrag {
		const obj = JSON.parse(json) as Partial<FachklasseEintrag>;
		const result = new FachklasseEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.idFachklasse = (obj.idFachklasse === undefined) ? null : obj.idFachklasse === null ? null : obj.idFachklasse;
		result.idSchulgliederung = (obj.idSchulgliederung === undefined) ? null : obj.idSchulgliederung === null ? null : obj.idSchulgliederung;
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

	public static transpilerToJSON(obj: FachklasseEintrag): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"idFachklasse" : ' + ((obj.idFachklasse === null) ? 'null' : obj.idFachklasse.toString()) + ',';
		result += '"idSchulgliederung" : ' + ((obj.idSchulgliederung === null) ? 'null' : obj.idSchulgliederung.toString()) + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<FachklasseEintrag>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.idFachklasse !== undefined) {
			result += '"idFachklasse" : ' + ((obj.idFachklasse === null) ? 'null' : obj.idFachklasse.toString()) + ',';
		}
		if (obj.idSchulgliederung !== undefined) {
			result += '"idSchulgliederung" : ' + ((obj.idSchulgliederung === null) ? 'null' : obj.idSchulgliederung.toString()) + ',';
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

export function cast_de_svws_nrw_core_data_schule_FachklasseEintrag(obj: unknown): FachklasseEintrag {
	return obj as FachklasseEintrag;
}
