import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerSchulbesuchSchule extends JavaObject {

	/**
	 * Die ID der Informationen zum vorigen Schulbesuch in der Datenbank.
	 */
	public id: number = 0;

	/**
	 * Die ID des Schülers.
	 */
	public idSchueler: number | null = null;

	/**
	 * Die ID der Schule.
	 */
	public idSchule: number | null = null;

	/**
	 * Der Schlüssel des Bildungsganges/Schulgliederung an der Schule.
	 */
	public schluesselSchulgliederung: string | null = null;

	/**
	 * Die ID des Grundes für die Entlassung von der Schule.
	 */
	public idEntlassgrund: number | null = null;

	/**
	 * Die ID des Abschlusses, welcher an der Schule erworben wurde.
	 */
	public idAbschlussart: string | null = null;

	/**
	 * Die ID der Organisationsform der Schule (z.B. für Halbtagsunterricht).
	 */
	public idOrganisationsform: string | null = null;

	/**
	 * Das Datum, ab dem die Schule besucht wurde.
	 */
	public datumVon: string | null = null;

	/**
	 * Das Datum, bis wann die Schule besucht wurde.
	 */
	public datumBis: string | null = null;

	/**
	 * Der Jahrgang, ab dem die Schule besucht wurde.
	 */
	public jahrgangVon: string | null = null;

	/**
	 * Der Jahrgang, bis zu dem die Schule besucht wurde.
	 */
	public jahrgangBis: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule'].includes(name);
	}

	public static readonly class = new Class<SchuelerSchulbesuchSchule>('de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule');

	public static transpilerFromJSON(json: string): SchuelerSchulbesuchSchule {
		const obj = JSON.parse(json) as Partial<SchuelerSchulbesuchSchule>;
		const result = new SchuelerSchulbesuchSchule();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.idSchueler = (obj.idSchueler === undefined) ? null : obj.idSchueler === null ? null : obj.idSchueler;
		result.idSchule = (obj.idSchule === undefined) ? null : obj.idSchule === null ? null : obj.idSchule;
		result.schluesselSchulgliederung = (obj.schluesselSchulgliederung === undefined) ? null : obj.schluesselSchulgliederung === null ? null : obj.schluesselSchulgliederung;
		result.idEntlassgrund = (obj.idEntlassgrund === undefined) ? null : obj.idEntlassgrund === null ? null : obj.idEntlassgrund;
		result.idAbschlussart = (obj.idAbschlussart === undefined) ? null : obj.idAbschlussart === null ? null : obj.idAbschlussart;
		result.idOrganisationsform = (obj.idOrganisationsform === undefined) ? null : obj.idOrganisationsform === null ? null : obj.idOrganisationsform;
		result.datumVon = (obj.datumVon === undefined) ? null : obj.datumVon === null ? null : obj.datumVon;
		result.datumBis = (obj.datumBis === undefined) ? null : obj.datumBis === null ? null : obj.datumBis;
		result.jahrgangVon = (obj.jahrgangVon === undefined) ? null : obj.jahrgangVon === null ? null : obj.jahrgangVon;
		result.jahrgangBis = (obj.jahrgangBis === undefined) ? null : obj.jahrgangBis === null ? null : obj.jahrgangBis;
		return result;
	}

	public static transpilerToJSON(obj: SchuelerSchulbesuchSchule): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchueler" : ' + ((obj.idSchueler === null) ? 'null' : obj.idSchueler.toString()) + ',';
		result += '"idSchule" : ' + ((obj.idSchule === null) ? 'null' : obj.idSchule.toString()) + ',';
		result += '"schluesselSchulgliederung" : ' + ((obj.schluesselSchulgliederung === null) ? 'null' : JSON.stringify(obj.schluesselSchulgliederung)) + ',';
		result += '"idEntlassgrund" : ' + ((obj.idEntlassgrund === null) ? 'null' : obj.idEntlassgrund.toString()) + ',';
		result += '"idAbschlussart" : ' + ((obj.idAbschlussart === null) ? 'null' : JSON.stringify(obj.idAbschlussart)) + ',';
		result += '"idOrganisationsform" : ' + ((obj.idOrganisationsform === null) ? 'null' : JSON.stringify(obj.idOrganisationsform)) + ',';
		result += '"datumVon" : ' + ((obj.datumVon === null) ? 'null' : JSON.stringify(obj.datumVon)) + ',';
		result += '"datumBis" : ' + ((obj.datumBis === null) ? 'null' : JSON.stringify(obj.datumBis)) + ',';
		result += '"jahrgangVon" : ' + ((obj.jahrgangVon === null) ? 'null' : JSON.stringify(obj.jahrgangVon)) + ',';
		result += '"jahrgangBis" : ' + ((obj.jahrgangBis === null) ? 'null' : JSON.stringify(obj.jahrgangBis)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuelerSchulbesuchSchule>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + ((obj.idSchueler === null) ? 'null' : obj.idSchueler.toString()) + ',';
		}
		if (obj.idSchule !== undefined) {
			result += '"idSchule" : ' + ((obj.idSchule === null) ? 'null' : obj.idSchule.toString()) + ',';
		}
		if (obj.schluesselSchulgliederung !== undefined) {
			result += '"schluesselSchulgliederung" : ' + ((obj.schluesselSchulgliederung === null) ? 'null' : JSON.stringify(obj.schluesselSchulgliederung)) + ',';
		}
		if (obj.idEntlassgrund !== undefined) {
			result += '"idEntlassgrund" : ' + ((obj.idEntlassgrund === null) ? 'null' : obj.idEntlassgrund.toString()) + ',';
		}
		if (obj.idAbschlussart !== undefined) {
			result += '"idAbschlussart" : ' + ((obj.idAbschlussart === null) ? 'null' : JSON.stringify(obj.idAbschlussart)) + ',';
		}
		if (obj.idOrganisationsform !== undefined) {
			result += '"idOrganisationsform" : ' + ((obj.idOrganisationsform === null) ? 'null' : JSON.stringify(obj.idOrganisationsform)) + ',';
		}
		if (obj.datumVon !== undefined) {
			result += '"datumVon" : ' + ((obj.datumVon === null) ? 'null' : JSON.stringify(obj.datumVon)) + ',';
		}
		if (obj.datumBis !== undefined) {
			result += '"datumBis" : ' + ((obj.datumBis === null) ? 'null' : JSON.stringify(obj.datumBis)) + ',';
		}
		if (obj.jahrgangVon !== undefined) {
			result += '"jahrgangVon" : ' + ((obj.jahrgangVon === null) ? 'null' : JSON.stringify(obj.jahrgangVon)) + ',';
		}
		if (obj.jahrgangBis !== undefined) {
			result += '"jahrgangBis" : ' + ((obj.jahrgangBis === null) ? 'null' : JSON.stringify(obj.jahrgangBis)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schueler_SchuelerSchulbesuchSchule(obj: unknown): SchuelerSchulbesuchSchule {
	return obj as SchuelerSchulbesuchSchule;
}
