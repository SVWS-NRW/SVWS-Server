import { JavaObject } from '../../../../java/lang/JavaObject';
import { LernplattformV1Lehrer } from '../../../../core/data/lernplattform/v1/LernplattformV1Lehrer';
import { LernplattformV1Fach } from '../../../../core/data/lernplattform/v1/LernplattformV1Fach';
import { LernplattformV1Lerngruppe } from '../../../../core/data/lernplattform/v1/LernplattformV1Lerngruppe';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { LernplattformV1Klasse } from '../../../../core/data/lernplattform/v1/LernplattformV1Klasse';
import { LernplattformV1Schueler } from '../../../../core/data/lernplattform/v1/LernplattformV1Schueler';
import { LernplattformV1Jahrgang } from '../../../../core/data/lernplattform/v1/LernplattformV1Jahrgang';

export class LernplattformV1Export extends JavaObject {

	/**
	 * Die Revision des Lernplattform-Datenformates, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und
	 *  ansonsten aufsteigend ab 1
	 */
	public revision : number = -1;

	/**
	 * Gibt die ID der Lernplattform an.
	 */
	public lernplattformId : number | null = null;

	/**
	 * Enthält die Bezeichnung der Lernplattform.
	 */
	public lernplattformBezeichnung : string | null = null;

	/**
	 * Der Titel des Inhalts der Datei.
	 */
	public titel : string | null = "JSON für Lernplattformen";

	/**
	 * Der Titel des Inhalts der Datei.
	 */
	public beschreibung : string | null = "Diese JSON beinhaltet alle Daten für die Synchronisation für eine Lernplattform.";

	/**
	 * Der Titel des Inhalts der Datei.
	 */
	public autor : string | null = "MSB SVWS";

	/**
	 * Der Zeitstempel für die Anfragen nach der Lernplattform-Datei.
	 */
	public anfrageZeitpunkt : string | null = null;

	/**
	 * Der Zeitstempel für die Antwort der Lernplattform-Datei.
	 */
	public antwortZeitpunkt : string | null = null;

	/**
	 * Die Schulnummer, für welche die Lernplattform-Daten generiert wurde.
	 */
	public schulnummer : number = 0;

	/**
	 * Das Schuljahr, für welches die Lernplattform-Daten generiert wurde.
	 */
	public schuljahr : number = 0;

	/**
	 * Gibt an, für welchen Abschnitt innerhalb des Schuljahres die Lernplattform-Daten generiert wurde.
	 */
	public schuljahresabschnitt : number = 0;

	/**
	 * Gibt an, für welchen Abschnitt innerhalb des Schuljahres die Lernplattform-Daten generiert wurden.
	 */
	public schulbezeichnung : string | null = null;

	/**
	 * Gibt an, welche E-Mail-Adresse für die Schule hinterlegt ist.
	 */
	public mailadresse : string | null = null;

	/**
	 * Die Informationen zu den einzelnen Jahrgängen.
	 */
	public readonly jahrgaenge : List<LernplattformV1Jahrgang> = new ArrayList<LernplattformV1Jahrgang>();

	/**
	 * Die Informationen zu den einzelnen Klassen.
	 */
	public readonly klassen : List<LernplattformV1Klasse> = new ArrayList<LernplattformV1Klasse>();

	/**
	 * Die Informationen zu Lehrern.
	 */
	public readonly lehrer : List<LernplattformV1Lehrer> = new ArrayList<LernplattformV1Lehrer>();

	/**
	 * Die Informationen zu den Fächern.
	 */
	public readonly faecher : List<LernplattformV1Fach> = new ArrayList<LernplattformV1Fach>();

	/**
	 * Die Informationen zu den Lerngruppen (Klassen und Kurse).
	 */
	public readonly lerngruppen : List<LernplattformV1Lerngruppe> = new ArrayList<LernplattformV1Lerngruppe>();

	/**
	 * Die Informationen zu den Schülern.
	 */
	public readonly schueler : List<LernplattformV1Schueler> = new ArrayList<LernplattformV1Schueler>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Export';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Export'].includes(name);
	}

	public static class = new Class<LernplattformV1Export>('de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Export');

	public static transpilerFromJSON(json : string): LernplattformV1Export {
		const obj = JSON.parse(json) as Partial<LernplattformV1Export>;
		const result = new LernplattformV1Export();
		if (obj.revision === undefined)
			throw new Error('invalid json format, missing attribute revision');
		result.revision = obj.revision;
		result.lernplattformId = (obj.lernplattformId === undefined) ? null : obj.lernplattformId === null ? null : obj.lernplattformId;
		result.lernplattformBezeichnung = (obj.lernplattformBezeichnung === undefined) ? null : obj.lernplattformBezeichnung === null ? null : obj.lernplattformBezeichnung;
		result.titel = (obj.titel === undefined) ? null : obj.titel === null ? null : obj.titel;
		result.beschreibung = (obj.beschreibung === undefined) ? null : obj.beschreibung === null ? null : obj.beschreibung;
		result.autor = (obj.autor === undefined) ? null : obj.autor === null ? null : obj.autor;
		result.anfrageZeitpunkt = (obj.anfrageZeitpunkt === undefined) ? null : obj.anfrageZeitpunkt === null ? null : obj.anfrageZeitpunkt;
		result.antwortZeitpunkt = (obj.antwortZeitpunkt === undefined) ? null : obj.antwortZeitpunkt === null ? null : obj.antwortZeitpunkt;
		if (obj.schulnummer === undefined)
			throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		if (obj.schuljahr === undefined)
			throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (obj.schuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute schuljahresabschnitt');
		result.schuljahresabschnitt = obj.schuljahresabschnitt;
		result.schulbezeichnung = (obj.schulbezeichnung === undefined) ? null : obj.schulbezeichnung === null ? null : obj.schulbezeichnung;
		result.mailadresse = (obj.mailadresse === undefined) ? null : obj.mailadresse === null ? null : obj.mailadresse;
		if (obj.jahrgaenge !== undefined) {
			for (const elem of obj.jahrgaenge) {
				result.jahrgaenge.add(LernplattformV1Jahrgang.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassen !== undefined) {
			for (const elem of obj.klassen) {
				result.klassen.add(LernplattformV1Klasse.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lehrer !== undefined) {
			for (const elem of obj.lehrer) {
				result.lehrer.add(LernplattformV1Lehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(LernplattformV1Fach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lerngruppen !== undefined) {
			for (const elem of obj.lerngruppen) {
				result.lerngruppen.add(LernplattformV1Lerngruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schueler !== undefined) {
			for (const elem of obj.schueler) {
				result.schueler.add(LernplattformV1Schueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : LernplattformV1Export) : string {
		let result = '{';
		result += '"revision" : ' + obj.revision.toString() + ',';
		result += '"lernplattformId" : ' + ((obj.lernplattformId === null) ? 'null' : obj.lernplattformId.toString()) + ',';
		result += '"lernplattformBezeichnung" : ' + ((obj.lernplattformBezeichnung === null) ? 'null' : JSON.stringify(obj.lernplattformBezeichnung)) + ',';
		result += '"titel" : ' + ((obj.titel === null) ? 'null' : JSON.stringify(obj.titel)) + ',';
		result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		result += '"autor" : ' + ((obj.autor === null) ? 'null' : JSON.stringify(obj.autor)) + ',';
		result += '"anfrageZeitpunkt" : ' + ((obj.anfrageZeitpunkt === null) ? 'null' : JSON.stringify(obj.anfrageZeitpunkt)) + ',';
		result += '"antwortZeitpunkt" : ' + ((obj.antwortZeitpunkt === null) ? 'null' : JSON.stringify(obj.antwortZeitpunkt)) + ',';
		result += '"schulnummer" : ' + obj.schulnummer.toString() + ',';
		result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		result += '"schuljahresabschnitt" : ' + obj.schuljahresabschnitt.toString() + ',';
		result += '"schulbezeichnung" : ' + ((obj.schulbezeichnung === null) ? 'null' : JSON.stringify(obj.schulbezeichnung)) + ',';
		result += '"mailadresse" : ' + ((obj.mailadresse === null) ? 'null' : JSON.stringify(obj.mailadresse)) + ',';
		result += '"jahrgaenge" : [ ';
		for (let i = 0; i < obj.jahrgaenge.size(); i++) {
			const elem = obj.jahrgaenge.get(i);
			result += LernplattformV1Jahrgang.transpilerToJSON(elem);
			if (i < obj.jahrgaenge.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassen" : [ ';
		for (let i = 0; i < obj.klassen.size(); i++) {
			const elem = obj.klassen.get(i);
			result += LernplattformV1Klasse.transpilerToJSON(elem);
			if (i < obj.klassen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lehrer" : [ ';
		for (let i = 0; i < obj.lehrer.size(); i++) {
			const elem = obj.lehrer.get(i);
			result += LernplattformV1Lehrer.transpilerToJSON(elem);
			if (i < obj.lehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += LernplattformV1Fach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lerngruppen" : [ ';
		for (let i = 0; i < obj.lerngruppen.size(); i++) {
			const elem = obj.lerngruppen.get(i);
			result += LernplattformV1Lerngruppe.transpilerToJSON(elem);
			if (i < obj.lerngruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schueler" : [ ';
		for (let i = 0; i < obj.schueler.size(); i++) {
			const elem = obj.schueler.get(i);
			result += LernplattformV1Schueler.transpilerToJSON(elem);
			if (i < obj.schueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LernplattformV1Export>) : string {
		let result = '{';
		if (obj.revision !== undefined) {
			result += '"revision" : ' + obj.revision.toString() + ',';
		}
		if (obj.lernplattformId !== undefined) {
			result += '"lernplattformId" : ' + ((obj.lernplattformId === null) ? 'null' : obj.lernplattformId.toString()) + ',';
		}
		if (obj.lernplattformBezeichnung !== undefined) {
			result += '"lernplattformBezeichnung" : ' + ((obj.lernplattformBezeichnung === null) ? 'null' : JSON.stringify(obj.lernplattformBezeichnung)) + ',';
		}
		if (obj.titel !== undefined) {
			result += '"titel" : ' + ((obj.titel === null) ? 'null' : JSON.stringify(obj.titel)) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		}
		if (obj.autor !== undefined) {
			result += '"autor" : ' + ((obj.autor === null) ? 'null' : JSON.stringify(obj.autor)) + ',';
		}
		if (obj.anfrageZeitpunkt !== undefined) {
			result += '"anfrageZeitpunkt" : ' + ((obj.anfrageZeitpunkt === null) ? 'null' : JSON.stringify(obj.anfrageZeitpunkt)) + ',';
		}
		if (obj.antwortZeitpunkt !== undefined) {
			result += '"antwortZeitpunkt" : ' + ((obj.antwortZeitpunkt === null) ? 'null' : JSON.stringify(obj.antwortZeitpunkt)) + ',';
		}
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + obj.schulnummer.toString() + ',';
		}
		if (obj.schuljahr !== undefined) {
			result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		}
		if (obj.schuljahresabschnitt !== undefined) {
			result += '"schuljahresabschnitt" : ' + obj.schuljahresabschnitt.toString() + ',';
		}
		if (obj.schulbezeichnung !== undefined) {
			result += '"schulbezeichnung" : ' + ((obj.schulbezeichnung === null) ? 'null' : JSON.stringify(obj.schulbezeichnung)) + ',';
		}
		if (obj.mailadresse !== undefined) {
			result += '"mailadresse" : ' + ((obj.mailadresse === null) ? 'null' : JSON.stringify(obj.mailadresse)) + ',';
		}
		if (obj.jahrgaenge !== undefined) {
			result += '"jahrgaenge" : [ ';
			for (let i = 0; i < obj.jahrgaenge.size(); i++) {
				const elem = obj.jahrgaenge.get(i);
				result += LernplattformV1Jahrgang.transpilerToJSON(elem);
				if (i < obj.jahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassen !== undefined) {
			result += '"klassen" : [ ';
			for (let i = 0; i < obj.klassen.size(); i++) {
				const elem = obj.klassen.get(i);
				result += LernplattformV1Klasse.transpilerToJSON(elem);
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += LernplattformV1Lehrer.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.faecher !== undefined) {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += LernplattformV1Fach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lerngruppen !== undefined) {
			result += '"lerngruppen" : [ ';
			for (let i = 0; i < obj.lerngruppen.size(); i++) {
				const elem = obj.lerngruppen.get(i);
				result += LernplattformV1Lerngruppe.transpilerToJSON(elem);
				if (i < obj.lerngruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schueler !== undefined) {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += LernplattformV1Schueler.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lernplattform_v1_LernplattformV1Export(obj : unknown) : LernplattformV1Export {
	return obj as LernplattformV1Export;
}
