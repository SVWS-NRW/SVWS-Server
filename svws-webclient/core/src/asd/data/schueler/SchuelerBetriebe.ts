import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerBetriebe extends JavaObject {

	/**
	 * Die ID des Betriebseintrags beim Schüler
	 */
	public id: number = 0;

	/**
	 * Die ID des Schülers
	 */
	public idSchueler: number = 0;

	/**
	 * Die ID des Betriebs
	 */
	public idBetrieb: number = 0;

	/**
	 * ID der Beschäftigungsart des Schülers
	 */
	public idBeschaeftigungsart: number | null = null;

	/**
	 * Das Datum des Vertragsbeginns
	 */
	public vertragsbeginn: string | null = null;

	/**
	 * Das Datum des Vertragsendes
	 */
	public vertragsende: string | null = null;

	/**
	 * Der Name des Ausbilders
	 */
	public nameAusbilder: string | null = null;

	/**
	 * Betrieb erhält Anschreiben
	 */
	public erhaeltAnschreiben: boolean = false;

	/**
	 * Gibt an ob es ein Praktikum ist
	 */
	public istPraktikum: boolean = false;

	/**
	 * Die Sortierung des Betriebseintrags
	 */
	public sortierung: number | null = null;

	/**
	 * Die ID des Ansprechpartners
	 */
	public idAnsprechpartner: number | null = null;

	/**
	 * Die ID des Betreuungslehrers
	 */
	public idBetreuungslehrer: number | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schueler.SchuelerBetriebe';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.schueler.SchuelerBetriebe'].includes(name);
	}

	public static readonly class = new Class<SchuelerBetriebe>('de.svws_nrw.asd.data.schueler.SchuelerBetriebe');

	public static transpilerFromJSON(json: string): SchuelerBetriebe {
		const obj = JSON.parse(json) as Partial<SchuelerBetriebe>;
		const result = new SchuelerBetriebe();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idSchueler === undefined)
			throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		if (obj.idBetrieb === undefined)
			throw new Error('invalid json format, missing attribute idBetrieb');
		result.idBetrieb = obj.idBetrieb;
		result.idBeschaeftigungsart = (obj.idBeschaeftigungsart === undefined) ? null : obj.idBeschaeftigungsart === null ? null : obj.idBeschaeftigungsart;
		result.vertragsbeginn = (obj.vertragsbeginn === undefined) ? null : obj.vertragsbeginn === null ? null : obj.vertragsbeginn;
		result.vertragsende = (obj.vertragsende === undefined) ? null : obj.vertragsende === null ? null : obj.vertragsende;
		result.nameAusbilder = (obj.nameAusbilder === undefined) ? null : obj.nameAusbilder === null ? null : obj.nameAusbilder;
		if (obj.erhaeltAnschreiben === undefined)
			throw new Error('invalid json format, missing attribute erhaeltAnschreiben');
		result.erhaeltAnschreiben = obj.erhaeltAnschreiben;
		if (obj.istPraktikum === undefined)
			throw new Error('invalid json format, missing attribute istPraktikum');
		result.istPraktikum = obj.istPraktikum;
		result.sortierung = (obj.sortierung === undefined) ? null : obj.sortierung === null ? null : obj.sortierung;
		result.idAnsprechpartner = (obj.idAnsprechpartner === undefined) ? null : obj.idAnsprechpartner === null ? null : obj.idAnsprechpartner;
		result.idBetreuungslehrer = (obj.idBetreuungslehrer === undefined) ? null : obj.idBetreuungslehrer === null ? null : obj.idBetreuungslehrer;
		return result;
	}

	public static transpilerToJSON(obj: SchuelerBetriebe): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		result += '"idBetrieb" : ' + obj.idBetrieb.toString() + ',';
		result += '"idBeschaeftigungsart" : ' + ((obj.idBeschaeftigungsart === null) ? 'null' : obj.idBeschaeftigungsart.toString()) + ',';
		result += '"vertragsbeginn" : ' + ((obj.vertragsbeginn === null) ? 'null' : JSON.stringify(obj.vertragsbeginn)) + ',';
		result += '"vertragsende" : ' + ((obj.vertragsende === null) ? 'null' : JSON.stringify(obj.vertragsende)) + ',';
		result += '"nameAusbilder" : ' + ((obj.nameAusbilder === null) ? 'null' : JSON.stringify(obj.nameAusbilder)) + ',';
		result += '"erhaeltAnschreiben" : ' + obj.erhaeltAnschreiben + ',';
		result += '"istPraktikum" : ' + obj.istPraktikum + ',';
		result += '"sortierung" : ' + ((obj.sortierung === null) ? 'null' : obj.sortierung.toString()) + ',';
		result += '"idAnsprechpartner" : ' + ((obj.idAnsprechpartner === null) ? 'null' : obj.idAnsprechpartner.toString()) + ',';
		result += '"idBetreuungslehrer" : ' + ((obj.idBetreuungslehrer === null) ? 'null' : obj.idBetreuungslehrer.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuelerBetriebe>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		}
		if (obj.idBetrieb !== undefined) {
			result += '"idBetrieb" : ' + obj.idBetrieb.toString() + ',';
		}
		if (obj.idBeschaeftigungsart !== undefined) {
			result += '"idBeschaeftigungsart" : ' + ((obj.idBeschaeftigungsart === null) ? 'null' : obj.idBeschaeftigungsart.toString()) + ',';
		}
		if (obj.vertragsbeginn !== undefined) {
			result += '"vertragsbeginn" : ' + ((obj.vertragsbeginn === null) ? 'null' : JSON.stringify(obj.vertragsbeginn)) + ',';
		}
		if (obj.vertragsende !== undefined) {
			result += '"vertragsende" : ' + ((obj.vertragsende === null) ? 'null' : JSON.stringify(obj.vertragsende)) + ',';
		}
		if (obj.nameAusbilder !== undefined) {
			result += '"nameAusbilder" : ' + ((obj.nameAusbilder === null) ? 'null' : JSON.stringify(obj.nameAusbilder)) + ',';
		}
		if (obj.erhaeltAnschreiben !== undefined) {
			result += '"erhaeltAnschreiben" : ' + obj.erhaeltAnschreiben + ',';
		}
		if (obj.istPraktikum !== undefined) {
			result += '"istPraktikum" : ' + obj.istPraktikum + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + ((obj.sortierung === null) ? 'null' : obj.sortierung.toString()) + ',';
		}
		if (obj.idAnsprechpartner !== undefined) {
			result += '"idAnsprechpartner" : ' + ((obj.idAnsprechpartner === null) ? 'null' : obj.idAnsprechpartner.toString()) + ',';
		}
		if (obj.idBetreuungslehrer !== undefined) {
			result += '"idBetreuungslehrer" : ' + ((obj.idBetreuungslehrer === null) ? 'null' : obj.idBetreuungslehrer.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schueler_SchuelerBetriebe(obj: unknown): SchuelerBetriebe {
	return obj as SchuelerBetriebe;
}
