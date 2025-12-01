import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Einwilligungsart extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id: number = -1;

	/**
	 * Die Bezeichnung der Einwilligungsart.
	 */
	public bezeichnung: string = "";

	/**
	 * Der Schlüssel der Einwilligungsart.
	 */
	public schluessel: string | null = "";

	/**
	 * Eine ausführliche Beschreibung der Einwilligungsart.
	 */
	public beschreibung: string | null = "";

	/**
	 * Die Id des PersonTyps der Einwilligungsart.
	 */
	public idPersonTyp: number = -1;

	/**
	 * Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an.
	 */
	public sortierung: number = 32000;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar: boolean = true;

	/**
	 * Gibt an, ob die Einwilligungsart in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen: boolean | null = false;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Einwilligungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.Einwilligungsart'].includes(name);
	}

	public static readonly class = new Class<Einwilligungsart>('de.svws_nrw.core.data.schule.Einwilligungsart');

	public static transpilerFromJSON(json: string): Einwilligungsart {
		const obj = JSON.parse(json) as Partial<Einwilligungsart>;
		const result = new Einwilligungsart();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		result.schluessel = (obj.schluessel === undefined) ? null : obj.schluessel === null ? null : obj.schluessel;
		result.beschreibung = (obj.beschreibung === undefined) ? null : obj.beschreibung === null ? null : obj.beschreibung;
		if (obj.idPersonTyp === undefined)
			throw new Error('invalid json format, missing attribute idPersonTyp');
		result.idPersonTyp = obj.idPersonTyp;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		result.referenziertInAnderenTabellen = (obj.referenziertInAnderenTabellen === undefined) ? null : obj.referenziertInAnderenTabellen === null ? null : obj.referenziertInAnderenTabellen;
		return result;
	}

	public static transpilerToJSON(obj: Einwilligungsart): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"schluessel" : ' + ((obj.schluessel === null) ? 'null' : JSON.stringify(obj.schluessel)) + ',';
		result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		result += '"idPersonTyp" : ' + obj.idPersonTyp.toString() + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"referenziertInAnderenTabellen" : ' + ((obj.referenziertInAnderenTabellen === null) ? 'null' : obj.referenziertInAnderenTabellen.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<Einwilligungsart>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.schluessel !== undefined) {
			result += '"schluessel" : ' + ((obj.schluessel === null) ? 'null' : JSON.stringify(obj.schluessel)) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		}
		if (obj.idPersonTyp !== undefined) {
			result += '"idPersonTyp" : ' + obj.idPersonTyp.toString() + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		if (obj.referenziertInAnderenTabellen !== undefined) {
			result += '"referenziertInAnderenTabellen" : ' + ((obj.referenziertInAnderenTabellen === null) ? 'null' : obj.referenziertInAnderenTabellen.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Einwilligungsart(obj: unknown): Einwilligungsart {
	return obj as Einwilligungsart;
}
