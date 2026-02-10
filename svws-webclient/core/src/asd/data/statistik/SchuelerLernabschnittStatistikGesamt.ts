import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerLernabschnittStatistikGesamt extends JavaObject {

	/**
	 * Die ID des Lernabschnitts in der Datenbank.
	 */
	public id: number = 0;

	/**
	 * Die ID des Schuljahresabschnitts, zu welchem diese Lernabschnittdaten gehören.
	 */
	public idSchuljahresabschnitt: number = 0;

	/**
	 * Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist.
	 */
	public idKlasse: number | null = null;

	/**
	 * Das Kürzel der Schulgliederung bzw. des Bildungsgangs des Schülers.
	 */
	public schulgliederung: string | null = null;

	/**
	 * Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist
	 */
	public idJahrgang: number | null = null;

	/**
	 * Die bisherige Anzahl der Jahre in der Schuleingangssphase
	 */
	public epJahre: number | null = null;

	/**
	 * Die ID der Fachklasse des Schülers an einem Berufskolleg
	 */
	public idFachklasse: number | null = null;

	/**
	 * Das Kürzel der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag - siehe Core-Type)
	 */
	public organisationsform: string | null = null;

	/**
	 * Das Kürzel der Klassenart in Bezug auf den Schüler (z.B. Regelklasse - siehe Core-Type)
	 */
	public Klassenart: string | null = "RK";

	/**
	 * Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht
	 */
	public hatSchwerbehinderungsNachweis: boolean = false;

	/**
	 * Die ID des Haupförderschwerpunktes des Schülers
	 */
	public idFoerderschwerpunkt1: number | null = null;

	/**
	 * Die ID des weiteren Förderschwerpunktes des Schülers
	 */
	public idFoerderschwerpunkt2: number | null = null;

	/**
	 * Das Kürzel des Versetzungsvermerks
	 */
	public versetzungsvermerk: string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<SchuelerLernabschnittStatistikGesamt>('de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt');

	public static transpilerFromJSON(json: string): SchuelerLernabschnittStatistikGesamt {
		const obj = JSON.parse(json) as Partial<SchuelerLernabschnittStatistikGesamt>;
		const result = new SchuelerLernabschnittStatistikGesamt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		result.idKlasse = (obj.idKlasse === undefined) ? null : obj.idKlasse === null ? null : obj.idKlasse;
		result.schulgliederung = (obj.schulgliederung === undefined) ? null : obj.schulgliederung === null ? null : obj.schulgliederung;
		result.idJahrgang = (obj.idJahrgang === undefined) ? null : obj.idJahrgang === null ? null : obj.idJahrgang;
		result.epJahre = (obj.epJahre === undefined) ? null : obj.epJahre === null ? null : obj.epJahre;
		result.idFachklasse = (obj.idFachklasse === undefined) ? null : obj.idFachklasse === null ? null : obj.idFachklasse;
		result.organisationsform = (obj.organisationsform === undefined) ? null : obj.organisationsform === null ? null : obj.organisationsform;
		result.Klassenart = (obj.Klassenart === undefined) ? null : obj.Klassenart === null ? null : obj.Klassenart;
		if (obj.hatSchwerbehinderungsNachweis === undefined)
			throw new Error('invalid json format, missing attribute hatSchwerbehinderungsNachweis');
		result.hatSchwerbehinderungsNachweis = obj.hatSchwerbehinderungsNachweis;
		result.idFoerderschwerpunkt1 = (obj.idFoerderschwerpunkt1 === undefined) ? null : obj.idFoerderschwerpunkt1 === null ? null : obj.idFoerderschwerpunkt1;
		result.idFoerderschwerpunkt2 = (obj.idFoerderschwerpunkt2 === undefined) ? null : obj.idFoerderschwerpunkt2 === null ? null : obj.idFoerderschwerpunkt2;
		result.versetzungsvermerk = (obj.versetzungsvermerk === undefined) ? null : obj.versetzungsvermerk === null ? null : obj.versetzungsvermerk;
		return result;
	}

	public static transpilerToJSON(obj: SchuelerLernabschnittStatistikGesamt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"idKlasse" : ' + ((obj.idKlasse === null) ? 'null' : obj.idKlasse.toString()) + ',';
		result += '"schulgliederung" : ' + ((obj.schulgliederung === null) ? 'null' : JSON.stringify(obj.schulgliederung)) + ',';
		result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		result += '"epJahre" : ' + ((obj.epJahre === null) ? 'null' : obj.epJahre.toString()) + ',';
		result += '"idFachklasse" : ' + ((obj.idFachklasse === null) ? 'null' : obj.idFachklasse.toString()) + ',';
		result += '"organisationsform" : ' + ((obj.organisationsform === null) ? 'null' : JSON.stringify(obj.organisationsform)) + ',';
		result += '"Klassenart" : ' + ((obj.Klassenart === null) ? 'null' : JSON.stringify(obj.Klassenart)) + ',';
		result += '"hatSchwerbehinderungsNachweis" : ' + obj.hatSchwerbehinderungsNachweis.toString() + ',';
		result += '"idFoerderschwerpunkt1" : ' + ((obj.idFoerderschwerpunkt1 === null) ? 'null' : obj.idFoerderschwerpunkt1.toString()) + ',';
		result += '"idFoerderschwerpunkt2" : ' + ((obj.idFoerderschwerpunkt2 === null) ? 'null' : obj.idFoerderschwerpunkt2.toString()) + ',';
		result += '"versetzungsvermerk" : ' + ((obj.versetzungsvermerk === null) ? 'null' : JSON.stringify(obj.versetzungsvermerk)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuelerLernabschnittStatistikGesamt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.idKlasse !== undefined) {
			result += '"idKlasse" : ' + ((obj.idKlasse === null) ? 'null' : obj.idKlasse.toString()) + ',';
		}
		if (obj.schulgliederung !== undefined) {
			result += '"schulgliederung" : ' + ((obj.schulgliederung === null) ? 'null' : JSON.stringify(obj.schulgliederung)) + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		}
		if (obj.epJahre !== undefined) {
			result += '"epJahre" : ' + ((obj.epJahre === null) ? 'null' : obj.epJahre.toString()) + ',';
		}
		if (obj.idFachklasse !== undefined) {
			result += '"idFachklasse" : ' + ((obj.idFachklasse === null) ? 'null' : obj.idFachklasse.toString()) + ',';
		}
		if (obj.organisationsform !== undefined) {
			result += '"organisationsform" : ' + ((obj.organisationsform === null) ? 'null' : JSON.stringify(obj.organisationsform)) + ',';
		}
		if (obj.Klassenart !== undefined) {
			result += '"Klassenart" : ' + ((obj.Klassenart === null) ? 'null' : JSON.stringify(obj.Klassenart)) + ',';
		}
		if (obj.hatSchwerbehinderungsNachweis !== undefined) {
			result += '"hatSchwerbehinderungsNachweis" : ' + obj.hatSchwerbehinderungsNachweis.toString() + ',';
		}
		if (obj.idFoerderschwerpunkt1 !== undefined) {
			result += '"idFoerderschwerpunkt1" : ' + ((obj.idFoerderschwerpunkt1 === null) ? 'null' : obj.idFoerderschwerpunkt1.toString()) + ',';
		}
		if (obj.idFoerderschwerpunkt2 !== undefined) {
			result += '"idFoerderschwerpunkt2" : ' + ((obj.idFoerderschwerpunkt2 === null) ? 'null' : obj.idFoerderschwerpunkt2.toString()) + ',';
		}
		if (obj.versetzungsvermerk !== undefined) {
			result += '"versetzungsvermerk" : ' + ((obj.versetzungsvermerk === null) ? 'null' : JSON.stringify(obj.versetzungsvermerk)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_SchuelerLernabschnittStatistikGesamt(obj: unknown): SchuelerLernabschnittStatistikGesamt {
	return obj as SchuelerLernabschnittStatistikGesamt;
}
