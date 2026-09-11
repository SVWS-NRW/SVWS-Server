import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { DateUtils } from '../../../core/utils/DateUtils';

export class UvStundentafelImportOptions extends JavaObject {

	/**
	 * Die ID des Jahrgangs.
	 */
	public idJahrgang: number = 0;

	/**
	 * Die Bezeichnung der Stundentafel.
	 */
	public bezeichnung: string = "";

	/**
	 * Das Datum, ab dem die Stundentafel gültig ist.
	 */
	public gueltigVon: string = "";

	/**
	 * Das Datum, bis zu dem die Stundentafel gültig ist.
	 */
	public gueltigBis: string | null = null;

	/**
	 * Eine optionale Beschreibung.
	 */
	public beschreibung: string | null = null;

	/**
	 * Das Schuljahr mit den Quelldaten.
	 */
	public schuljahr: number = 0;

	/**
	 * Die ID der Schild-Klasse mit den Quelldaten.
	 */
	public idKlasse: number = 0;

	/**
	 * Gibt an, ob für fehlende Schild-Fächer neue UV-Fächer angelegt werden sollen.
	 */
	public fehlendeUvFaecherAnlegen: boolean = true;


	/**
	 *Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Prüft, ob die angegebenen Gültigkeitsdaten gültig und in der richtigen Reihenfolge sind.
	 *
	 * @return true, wenn die Gültigkeitsdaten gültig sind
	 */
	public isGueltigkeitszeitraumGueltig(): boolean {
		if (this.gueltigVon === null) {
			return true;
		}
		if (!DateUtils.isValidDate(this.gueltigVon)) {
			return false;
		}
		if (this.gueltigBis === null) {
			return true;
		}
		return DateUtils.isValidDate(this.gueltigBis) && (JavaString.compareTo(this.gueltigVon, this.gueltigBis) <= 0);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvStundentafelImportOptions';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvStundentafelImportOptions'].includes(name);
	}

	public static readonly class = new Class<UvStundentafelImportOptions>('de.svws_nrw.core.data.uv.UvStundentafelImportOptions');

	public static transpilerFromJSON(json: string): UvStundentafelImportOptions {
		const obj = JSON.parse(json) as Partial<UvStundentafelImportOptions>;
		const result = new UvStundentafelImportOptions();
		if (obj.idJahrgang === undefined)
			throw new Error('invalid json format, missing attribute idJahrgang');
		result.idJahrgang = obj.idJahrgang;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.gueltigVon === undefined)
			throw new Error('invalid json format, missing attribute gueltigVon');
		result.gueltigVon = obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		result.beschreibung = (obj.beschreibung === undefined) ? null : obj.beschreibung === null ? null : obj.beschreibung;
		if (obj.schuljahr === undefined)
			throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (obj.idKlasse === undefined)
			throw new Error('invalid json format, missing attribute idKlasse');
		result.idKlasse = obj.idKlasse;
		if (obj.fehlendeUvFaecherAnlegen === undefined)
			throw new Error('invalid json format, missing attribute fehlendeUvFaecherAnlegen');
		result.fehlendeUvFaecherAnlegen = obj.fehlendeUvFaecherAnlegen;
		return result;
	}

	public static transpilerToJSON(obj: UvStundentafelImportOptions): string {
		let result = '{';
		result += '"idJahrgang" : ' + obj.idJahrgang + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		result += '"schuljahr" : ' + obj.schuljahr + ',';
		result += '"idKlasse" : ' + obj.idKlasse + ',';
		result += '"fehlendeUvFaecherAnlegen" : ' + obj.fehlendeUvFaecherAnlegen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvStundentafelImportOptions>): string {
		let result = '{';
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + obj.idJahrgang + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		}
		if (obj.schuljahr !== undefined) {
			result += '"schuljahr" : ' + obj.schuljahr + ',';
		}
		if (obj.idKlasse !== undefined) {
			result += '"idKlasse" : ' + obj.idKlasse + ',';
		}
		if (obj.fehlendeUvFaecherAnlegen !== undefined) {
			result += '"fehlendeUvFaecherAnlegen" : ' + obj.fehlendeUvFaecherAnlegen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvStundentafelImportOptions(obj: unknown): UvStundentafelImportOptions {
	return obj as UvStundentafelImportOptions;
}
