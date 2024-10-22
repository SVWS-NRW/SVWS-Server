import { JavaObject } from '../../../java/lang/JavaObject';
import { Schueler } from '../../../core/data/schueler/Schueler';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class KlassenDaten extends JavaObject {

	/**
	 * Die ID der Klasse.
	 */
	public id : number = 0;

	/**
	 * Die ID des Schuljahresabschnittes des Kurses.
	 */
	public idSchuljahresabschnitt : number = 0;

	/**
	 * Das Kürzel der Klasse.
	 */
	public kuerzel : string | null = null;

	/**
	 * Die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist
	 */
	public idJahrgang : number | null = null;

	/**
	 * Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z).
	 */
	public parallelitaet : string | null = null;

	/**
	 * Die Sortierreihenfolge des Klassenlisten-Eintrags.
	 */
	public sortierung : number = 0;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar : boolean = false;

	/**
	 * Die Liste der IDs der Klassenleitungen der Klasse.
	 */
	public klassenLeitungen : List<number> = new ArrayList<number>();

	/**
	 * Die Schüler der Klasse.
	 */
	public schueler : List<Schueler> = new ArrayList<Schueler>();

	/**
	 * Adressmerkmal des Teilstandorts für die Klasse
	 */
	public teilstandort : string = "";

	/**
	 * Eine zusätzliche Beschreibung zu der Klasse
	 */
	public beschreibung : string = "";

	/**
	 * Die ID der Vorgängerklasse, sofern im vorigen Schuljahresabschnitt definiert - ansonsten null
	 */
	public idVorgaengerklasse : number | null = null;

	/**
	 * Das Kürzel der Vorgängerklasse vor der letzen Versetzung.
	 */
	public kuerzelVorgaengerklasse : string | null = null;

	/**
	 * Die ID der Folgeklasse, sofern im folgenden Schuljahresabschnitt definiert - ansonsten null
	 */
	public idFolgeklasse : number | null = null;

	/**
	 * Das Kürzel der Folgeklasse nach der nächsten Versetzung.
	 */
	public kuerzelFolgeklasse : string | null = null;

	/**
	 * Die ID für die Organisationsform der Klasse im allgemeinbildenden Bereich
	 */
	public idAllgemeinbildendOrganisationsform : number | null = null;

	/**
	 * Die ID für die Organisationsform der Klasse im berufsbildenden Bereich
	 */
	public idBerufsbildendOrganisationsform : number | null = null;

	/**
	 * Die ID für die Organisationsform der Klasse im Weiterbildungsbereich
	 */
	public idWeiterbildungOrganisationsform : number | null = null;

	/**
	 * Die zugewiesene Prüfungsordnung, welche in Schild 3 genutzt wird.
	 */
	public pruefungsordnung : string | null = null;

	/**
	 * Die ID für die Schulgliederung der Klasse oder -1, wenn der Klasse keine eindeutige Schulgliederung zugeordnet ist.
	 */
	public idSchulgliederung : number = -1;

	/**
	 * Die ID für Klassenart
	 */
	public idKlassenart : number = -1;

	/**
	 * Gibt an, ob die Noteneingabe gesperrt ist
	 */
	public noteneingabeGesperrt : boolean = false;

	/**
	 * Gibt an, ob Ankreuzkompetenzen für die Klasse verwendet werden.
	 */
	public verwendungAnkreuzkompetenzen : boolean = false;

	/**
	 * Die ID der Fachklasse, falls es sich um eine Klasse an einem Berufskolleg handelt oder null
	 */
	public idFachklasse : number | null = null;

	/**
	 * Gibt am WBK an, ob die Klassen im Sommersemester angefangen hat.
	 */
	public beginnSommersemester : boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.klassen.KlassenDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.klassen.KlassenDaten'].includes(name);
	}

	public static class = new Class<KlassenDaten>('de.svws_nrw.core.data.klassen.KlassenDaten');

	public static transpilerFromJSON(json : string): KlassenDaten {
		const obj = JSON.parse(json) as Partial<KlassenDaten>;
		const result = new KlassenDaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.idJahrgang = (obj.idJahrgang === undefined) ? null : obj.idJahrgang === null ? null : obj.idJahrgang;
		result.parallelitaet = (obj.parallelitaet === undefined) ? null : obj.parallelitaet === null ? null : obj.parallelitaet;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (obj.klassenLeitungen !== undefined) {
			for (const elem of obj.klassenLeitungen) {
				result.klassenLeitungen.add(elem);
			}
		}
		if (obj.schueler !== undefined) {
			for (const elem of obj.schueler) {
				result.schueler.add(Schueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.teilstandort === undefined)
			throw new Error('invalid json format, missing attribute teilstandort');
		result.teilstandort = obj.teilstandort;
		if (obj.beschreibung === undefined)
			throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		result.idVorgaengerklasse = (obj.idVorgaengerklasse === undefined) ? null : obj.idVorgaengerklasse === null ? null : obj.idVorgaengerklasse;
		result.kuerzelVorgaengerklasse = (obj.kuerzelVorgaengerklasse === undefined) ? null : obj.kuerzelVorgaengerklasse === null ? null : obj.kuerzelVorgaengerklasse;
		result.idFolgeklasse = (obj.idFolgeklasse === undefined) ? null : obj.idFolgeklasse === null ? null : obj.idFolgeklasse;
		result.kuerzelFolgeklasse = (obj.kuerzelFolgeklasse === undefined) ? null : obj.kuerzelFolgeklasse === null ? null : obj.kuerzelFolgeklasse;
		result.idAllgemeinbildendOrganisationsform = (obj.idAllgemeinbildendOrganisationsform === undefined) ? null : obj.idAllgemeinbildendOrganisationsform === null ? null : obj.idAllgemeinbildendOrganisationsform;
		result.idBerufsbildendOrganisationsform = (obj.idBerufsbildendOrganisationsform === undefined) ? null : obj.idBerufsbildendOrganisationsform === null ? null : obj.idBerufsbildendOrganisationsform;
		result.idWeiterbildungOrganisationsform = (obj.idWeiterbildungOrganisationsform === undefined) ? null : obj.idWeiterbildungOrganisationsform === null ? null : obj.idWeiterbildungOrganisationsform;
		result.pruefungsordnung = (obj.pruefungsordnung === undefined) ? null : obj.pruefungsordnung === null ? null : obj.pruefungsordnung;
		if (obj.idSchulgliederung === undefined)
			throw new Error('invalid json format, missing attribute idSchulgliederung');
		result.idSchulgliederung = obj.idSchulgliederung;
		if (obj.idKlassenart === undefined)
			throw new Error('invalid json format, missing attribute idKlassenart');
		result.idKlassenart = obj.idKlassenart;
		if (obj.noteneingabeGesperrt === undefined)
			throw new Error('invalid json format, missing attribute noteneingabeGesperrt');
		result.noteneingabeGesperrt = obj.noteneingabeGesperrt;
		if (obj.verwendungAnkreuzkompetenzen === undefined)
			throw new Error('invalid json format, missing attribute verwendungAnkreuzkompetenzen');
		result.verwendungAnkreuzkompetenzen = obj.verwendungAnkreuzkompetenzen;
		result.idFachklasse = (obj.idFachklasse === undefined) ? null : obj.idFachklasse === null ? null : obj.idFachklasse;
		if (obj.beginnSommersemester === undefined)
			throw new Error('invalid json format, missing attribute beginnSommersemester');
		result.beginnSommersemester = obj.beginnSommersemester;
		return result;
	}

	public static transpilerToJSON(obj : KlassenDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		result += '"parallelitaet" : ' + ((obj.parallelitaet === null) ? 'null' : JSON.stringify(obj.parallelitaet)) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"klassenLeitungen" : [ ';
		for (let i = 0; i < obj.klassenLeitungen.size(); i++) {
			const elem = obj.klassenLeitungen.get(i);
			result += elem.toString();
			if (i < obj.klassenLeitungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schueler" : [ ';
		for (let i = 0; i < obj.schueler.size(); i++) {
			const elem = obj.schueler.get(i);
			result += Schueler.transpilerToJSON(elem);
			if (i < obj.schueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"teilstandort" : ' + JSON.stringify(obj.teilstandort) + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		result += '"idVorgaengerklasse" : ' + ((obj.idVorgaengerklasse === null) ? 'null' : obj.idVorgaengerklasse.toString()) + ',';
		result += '"kuerzelVorgaengerklasse" : ' + ((obj.kuerzelVorgaengerklasse === null) ? 'null' : JSON.stringify(obj.kuerzelVorgaengerklasse)) + ',';
		result += '"idFolgeklasse" : ' + ((obj.idFolgeklasse === null) ? 'null' : obj.idFolgeklasse.toString()) + ',';
		result += '"kuerzelFolgeklasse" : ' + ((obj.kuerzelFolgeklasse === null) ? 'null' : JSON.stringify(obj.kuerzelFolgeklasse)) + ',';
		result += '"idAllgemeinbildendOrganisationsform" : ' + ((obj.idAllgemeinbildendOrganisationsform === null) ? 'null' : obj.idAllgemeinbildendOrganisationsform.toString()) + ',';
		result += '"idBerufsbildendOrganisationsform" : ' + ((obj.idBerufsbildendOrganisationsform === null) ? 'null' : obj.idBerufsbildendOrganisationsform.toString()) + ',';
		result += '"idWeiterbildungOrganisationsform" : ' + ((obj.idWeiterbildungOrganisationsform === null) ? 'null' : obj.idWeiterbildungOrganisationsform.toString()) + ',';
		result += '"pruefungsordnung" : ' + ((obj.pruefungsordnung === null) ? 'null' : JSON.stringify(obj.pruefungsordnung)) + ',';
		result += '"idSchulgliederung" : ' + obj.idSchulgliederung.toString() + ',';
		result += '"idKlassenart" : ' + obj.idKlassenart.toString() + ',';
		result += '"noteneingabeGesperrt" : ' + obj.noteneingabeGesperrt.toString() + ',';
		result += '"verwendungAnkreuzkompetenzen" : ' + obj.verwendungAnkreuzkompetenzen.toString() + ',';
		result += '"idFachklasse" : ' + ((obj.idFachklasse === null) ? 'null' : obj.idFachklasse.toString()) + ',';
		result += '"beginnSommersemester" : ' + obj.beginnSommersemester.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KlassenDaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		}
		if (obj.parallelitaet !== undefined) {
			result += '"parallelitaet" : ' + ((obj.parallelitaet === null) ? 'null' : JSON.stringify(obj.parallelitaet)) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		if (obj.klassenLeitungen !== undefined) {
			result += '"klassenLeitungen" : [ ';
			for (let i = 0; i < obj.klassenLeitungen.size(); i++) {
				const elem = obj.klassenLeitungen.get(i);
				result += elem.toString();
				if (i < obj.klassenLeitungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schueler !== undefined) {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += Schueler.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.teilstandort !== undefined) {
			result += '"teilstandort" : ' + JSON.stringify(obj.teilstandort) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		}
		if (obj.idVorgaengerklasse !== undefined) {
			result += '"idVorgaengerklasse" : ' + ((obj.idVorgaengerklasse === null) ? 'null' : obj.idVorgaengerklasse.toString()) + ',';
		}
		if (obj.kuerzelVorgaengerklasse !== undefined) {
			result += '"kuerzelVorgaengerklasse" : ' + ((obj.kuerzelVorgaengerklasse === null) ? 'null' : JSON.stringify(obj.kuerzelVorgaengerklasse)) + ',';
		}
		if (obj.idFolgeklasse !== undefined) {
			result += '"idFolgeklasse" : ' + ((obj.idFolgeklasse === null) ? 'null' : obj.idFolgeklasse.toString()) + ',';
		}
		if (obj.kuerzelFolgeklasse !== undefined) {
			result += '"kuerzelFolgeklasse" : ' + ((obj.kuerzelFolgeklasse === null) ? 'null' : JSON.stringify(obj.kuerzelFolgeklasse)) + ',';
		}
		if (obj.idAllgemeinbildendOrganisationsform !== undefined) {
			result += '"idAllgemeinbildendOrganisationsform" : ' + ((obj.idAllgemeinbildendOrganisationsform === null) ? 'null' : obj.idAllgemeinbildendOrganisationsform.toString()) + ',';
		}
		if (obj.idBerufsbildendOrganisationsform !== undefined) {
			result += '"idBerufsbildendOrganisationsform" : ' + ((obj.idBerufsbildendOrganisationsform === null) ? 'null' : obj.idBerufsbildendOrganisationsform.toString()) + ',';
		}
		if (obj.idWeiterbildungOrganisationsform !== undefined) {
			result += '"idWeiterbildungOrganisationsform" : ' + ((obj.idWeiterbildungOrganisationsform === null) ? 'null' : obj.idWeiterbildungOrganisationsform.toString()) + ',';
		}
		if (obj.pruefungsordnung !== undefined) {
			result += '"pruefungsordnung" : ' + ((obj.pruefungsordnung === null) ? 'null' : JSON.stringify(obj.pruefungsordnung)) + ',';
		}
		if (obj.idSchulgliederung !== undefined) {
			result += '"idSchulgliederung" : ' + obj.idSchulgliederung.toString() + ',';
		}
		if (obj.idKlassenart !== undefined) {
			result += '"idKlassenart" : ' + obj.idKlassenart.toString() + ',';
		}
		if (obj.noteneingabeGesperrt !== undefined) {
			result += '"noteneingabeGesperrt" : ' + obj.noteneingabeGesperrt.toString() + ',';
		}
		if (obj.verwendungAnkreuzkompetenzen !== undefined) {
			result += '"verwendungAnkreuzkompetenzen" : ' + obj.verwendungAnkreuzkompetenzen.toString() + ',';
		}
		if (obj.idFachklasse !== undefined) {
			result += '"idFachklasse" : ' + ((obj.idFachklasse === null) ? 'null' : obj.idFachklasse.toString()) + ',';
		}
		if (obj.beginnSommersemester !== undefined) {
			result += '"beginnSommersemester" : ' + obj.beginnSommersemester.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_klassen_KlassenDaten(obj : unknown) : KlassenDaten {
	return obj as KlassenDaten;
}
