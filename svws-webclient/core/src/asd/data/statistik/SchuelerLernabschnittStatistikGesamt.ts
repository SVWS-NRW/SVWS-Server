import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import { SchuelerLeistungsdatenStatistikGesamt } from '../../../asd/data/statistik/SchuelerLeistungsdatenStatistikGesamt';
import type { List } from '../../../java/util/List';
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
	public idSchulgliederung: number | null = null;

	/**
	 * Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist
	 */
	public idJahrgang: number | null = null;

	/**
	 * Die bisherige Anzahl der Jahre in der Schuleingangssphase
	 */
	public idEpJahre: number | null = null;

	/**
	 * Die ID der Fachklasse des Schülers an einem Berufskolleg
	 */
	public idFachklasse: number | null = null;

	/**
	 * Die ID der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag - siehe Core-Type)
	 */
	public idOrganisationsform: number | null = null;

	/**
	 * Die ID der Klassenart in Bezug auf den Schüler (z.B. Regelklasse - siehe Core-Type)
	 */
	public idKlassenart: number | null = 7000;

	/**
	 * Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht
	 */
	public hatSchwerbehinderungsNachweis: boolean = false;

	/**
	 * Die ID des Hauptförderschwerpunktes des Schülers
	 */
	public idFoerderschwerpunkt1: number | null = null;

	/**
	 * Die ID des weiteren Förderschwerpunktes des Schülers
	 */
	public idFoerderschwerpunkt2: number | null = null;

	/**
	 * Die ID des Kürzels des Versetzungsvermerks
	 */
	public idVersetzungsvermerk: number | null = null;

	/**
	 * Die Leistungsdaten des Schülers in diesem Lernabschnitt.
	 */
	public leistungsdaten: List<SchuelerLeistungsdatenStatistikGesamt> = new ArrayList<SchuelerLeistungsdatenStatistikGesamt>();


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
		result.idSchulgliederung = (obj.idSchulgliederung === undefined) ? null : obj.idSchulgliederung === null ? null : obj.idSchulgliederung;
		result.idJahrgang = (obj.idJahrgang === undefined) ? null : obj.idJahrgang === null ? null : obj.idJahrgang;
		result.idEpJahre = (obj.idEpJahre === undefined) ? null : obj.idEpJahre === null ? null : obj.idEpJahre;
		result.idFachklasse = (obj.idFachklasse === undefined) ? null : obj.idFachklasse === null ? null : obj.idFachklasse;
		result.idOrganisationsform = (obj.idOrganisationsform === undefined) ? null : obj.idOrganisationsform === null ? null : obj.idOrganisationsform;
		result.idKlassenart = (obj.idKlassenart === undefined) ? null : obj.idKlassenart === null ? null : obj.idKlassenart;
		if (obj.hatSchwerbehinderungsNachweis === undefined)
			throw new Error('invalid json format, missing attribute hatSchwerbehinderungsNachweis');
		result.hatSchwerbehinderungsNachweis = obj.hatSchwerbehinderungsNachweis;
		result.idFoerderschwerpunkt1 = (obj.idFoerderschwerpunkt1 === undefined) ? null : obj.idFoerderschwerpunkt1 === null ? null : obj.idFoerderschwerpunkt1;
		result.idFoerderschwerpunkt2 = (obj.idFoerderschwerpunkt2 === undefined) ? null : obj.idFoerderschwerpunkt2 === null ? null : obj.idFoerderschwerpunkt2;
		result.idVersetzungsvermerk = (obj.idVersetzungsvermerk === undefined) ? null : obj.idVersetzungsvermerk === null ? null : obj.idVersetzungsvermerk;
		if (obj.leistungsdaten !== undefined) {
			for (const elem of obj.leistungsdaten) {
				result.leistungsdaten.add(SchuelerLeistungsdatenStatistikGesamt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: SchuelerLernabschnittStatistikGesamt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"idKlasse" : ' + ((obj.idKlasse === null) ? 'null' : obj.idKlasse.toString()) + ',';
		result += '"idSchulgliederung" : ' + ((obj.idSchulgliederung === null) ? 'null' : obj.idSchulgliederung.toString()) + ',';
		result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		result += '"idEpJahre" : ' + ((obj.idEpJahre === null) ? 'null' : obj.idEpJahre.toString()) + ',';
		result += '"idFachklasse" : ' + ((obj.idFachklasse === null) ? 'null' : obj.idFachklasse.toString()) + ',';
		result += '"idOrganisationsform" : ' + ((obj.idOrganisationsform === null) ? 'null' : obj.idOrganisationsform.toString()) + ',';
		result += '"idKlassenart" : ' + ((obj.idKlassenart === null) ? 'null' : obj.idKlassenart.toString()) + ',';
		result += '"hatSchwerbehinderungsNachweis" : ' + obj.hatSchwerbehinderungsNachweis.toString() + ',';
		result += '"idFoerderschwerpunkt1" : ' + ((obj.idFoerderschwerpunkt1 === null) ? 'null' : obj.idFoerderschwerpunkt1.toString()) + ',';
		result += '"idFoerderschwerpunkt2" : ' + ((obj.idFoerderschwerpunkt2 === null) ? 'null' : obj.idFoerderschwerpunkt2.toString()) + ',';
		result += '"idVersetzungsvermerk" : ' + ((obj.idVersetzungsvermerk === null) ? 'null' : obj.idVersetzungsvermerk.toString()) + ',';
		result += '"leistungsdaten" : [ ';
		for (let i = 0; i < obj.leistungsdaten.size(); i++) {
			const elem = obj.leistungsdaten.get(i);
			result += SchuelerLeistungsdatenStatistikGesamt.transpilerToJSON(elem);
			if (i < obj.leistungsdaten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
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
		if (obj.idSchulgliederung !== undefined) {
			result += '"idSchulgliederung" : ' + ((obj.idSchulgliederung === null) ? 'null' : obj.idSchulgliederung.toString()) + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		}
		if (obj.idEpJahre !== undefined) {
			result += '"idEpJahre" : ' + ((obj.idEpJahre === null) ? 'null' : obj.idEpJahre.toString()) + ',';
		}
		if (obj.idFachklasse !== undefined) {
			result += '"idFachklasse" : ' + ((obj.idFachklasse === null) ? 'null' : obj.idFachklasse.toString()) + ',';
		}
		if (obj.idOrganisationsform !== undefined) {
			result += '"idOrganisationsform" : ' + ((obj.idOrganisationsform === null) ? 'null' : obj.idOrganisationsform.toString()) + ',';
		}
		if (obj.idKlassenart !== undefined) {
			result += '"idKlassenart" : ' + ((obj.idKlassenart === null) ? 'null' : obj.idKlassenart.toString()) + ',';
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
		if (obj.idVersetzungsvermerk !== undefined) {
			result += '"idVersetzungsvermerk" : ' + ((obj.idVersetzungsvermerk === null) ? 'null' : obj.idVersetzungsvermerk.toString()) + ',';
		}
		if (obj.leistungsdaten !== undefined) {
			result += '"leistungsdaten" : [ ';
			for (let i = 0; i < obj.leistungsdaten.size(); i++) {
				const elem = obj.leistungsdaten.get(i);
				result += SchuelerLeistungsdatenStatistikGesamt.transpilerToJSON(elem);
				if (i < obj.leistungsdaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_SchuelerLernabschnittStatistikGesamt(obj: unknown): SchuelerLernabschnittStatistikGesamt {
	return obj as SchuelerLernabschnittStatistikGesamt;
}
