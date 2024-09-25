import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerKAoADaten extends JavaObject {

	/**
	 * Die ID der KAOA Daten in der Datenbank.
	 */
	public id : number = -1;

	/**
	 * Der Lernabschnitts des Schülers, zu dem diese KAOA Daten gehören.
	 */
	public idLernabschnitt : number = -1;

	/**
	 * Die ID des Jahrgangs des Schülers, zu dem diese KAOA Daten gehören.
	 */
	public idJahrgang : number = -1;

	/**
	 * Der Kategorie des Schülers, zu dem diese KAOA Daten gehören.
	 */
	public idKategorie : number = -1;

	/**
	 * Das Merkmal des Schülers, zu dem diese KAOA Daten gehören.
	 */
	public idMerkmal : number = -1;

	/**
	 * Das Zusatzmerkmal des Schülers, zu dem diese KAOA Daten gehören.
	 */
	public idZusatzmerkmal : number = -1;

	/**
	 * Die Anschlussoption des Schülers, zu dem diese KAOA Daten gehören.
	 */
	public idAnschlussoption : number | null = null;

	/**
	 * Das Berufsfeld des Schülers, zu dem diese KAOA Daten gehören.
	 */
	public idBerufsfeld : number | null = null;

	/**
	 * Ebene4 dieser KAOA Daten
	 */
	public idEbene4 : number | null = null;

	/**
	 * Die Bemerkung zu diesen KAOA Daten.
	 */
	public bemerkung : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerKAoADaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerKAoADaten'].includes(name);
	}

	public static class = new Class<SchuelerKAoADaten>('de.svws_nrw.core.data.schueler.SchuelerKAoADaten');

	public static transpilerFromJSON(json : string): SchuelerKAoADaten {
		const obj = JSON.parse(json) as Partial<SchuelerKAoADaten>;
		const result = new SchuelerKAoADaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idLernabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idLernabschnitt');
		result.idLernabschnitt = obj.idLernabschnitt;
		if (obj.idJahrgang === undefined)
			throw new Error('invalid json format, missing attribute idJahrgang');
		result.idJahrgang = obj.idJahrgang;
		if (obj.idKategorie === undefined)
			throw new Error('invalid json format, missing attribute idKategorie');
		result.idKategorie = obj.idKategorie;
		if (obj.idMerkmal === undefined)
			throw new Error('invalid json format, missing attribute idMerkmal');
		result.idMerkmal = obj.idMerkmal;
		if (obj.idZusatzmerkmal === undefined)
			throw new Error('invalid json format, missing attribute idZusatzmerkmal');
		result.idZusatzmerkmal = obj.idZusatzmerkmal;
		result.idAnschlussoption = (obj.idAnschlussoption === undefined) ? null : obj.idAnschlussoption === null ? null : obj.idAnschlussoption;
		result.idBerufsfeld = (obj.idBerufsfeld === undefined) ? null : obj.idBerufsfeld === null ? null : obj.idBerufsfeld;
		result.idEbene4 = (obj.idEbene4 === undefined) ? null : obj.idEbene4 === null ? null : obj.idEbene4;
		result.bemerkung = (obj.bemerkung === undefined) ? null : obj.bemerkung === null ? null : obj.bemerkung;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerKAoADaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idLernabschnitt" : ' + obj.idLernabschnitt.toString() + ',';
		result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		result += '"idKategorie" : ' + obj.idKategorie.toString() + ',';
		result += '"idMerkmal" : ' + obj.idMerkmal.toString() + ',';
		result += '"idZusatzmerkmal" : ' + obj.idZusatzmerkmal.toString() + ',';
		result += '"idAnschlussoption" : ' + ((!obj.idAnschlussoption) ? 'null' : obj.idAnschlussoption.toString()) + ',';
		result += '"idBerufsfeld" : ' + ((!obj.idBerufsfeld) ? 'null' : obj.idBerufsfeld.toString()) + ',';
		result += '"idEbene4" : ' + ((!obj.idEbene4) ? 'null' : obj.idEbene4.toString()) + ',';
		result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerKAoADaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idLernabschnitt !== undefined) {
			result += '"idLernabschnitt" : ' + obj.idLernabschnitt.toString() + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		}
		if (obj.idKategorie !== undefined) {
			result += '"idKategorie" : ' + obj.idKategorie.toString() + ',';
		}
		if (obj.idMerkmal !== undefined) {
			result += '"idMerkmal" : ' + obj.idMerkmal.toString() + ',';
		}
		if (obj.idZusatzmerkmal !== undefined) {
			result += '"idZusatzmerkmal" : ' + obj.idZusatzmerkmal.toString() + ',';
		}
		if (obj.idAnschlussoption !== undefined) {
			result += '"idAnschlussoption" : ' + ((!obj.idAnschlussoption) ? 'null' : obj.idAnschlussoption.toString()) + ',';
		}
		if (obj.idBerufsfeld !== undefined) {
			result += '"idBerufsfeld" : ' + ((!obj.idBerufsfeld) ? 'null' : obj.idBerufsfeld.toString()) + ',';
		}
		if (obj.idEbene4 !== undefined) {
			result += '"idEbene4" : ' + ((!obj.idEbene4) ? 'null' : obj.idEbene4.toString()) + ',';
		}
		if (obj.bemerkung !== undefined) {
			result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerKAoADaten(obj : unknown) : SchuelerKAoADaten {
	return obj as SchuelerKAoADaten;
}
