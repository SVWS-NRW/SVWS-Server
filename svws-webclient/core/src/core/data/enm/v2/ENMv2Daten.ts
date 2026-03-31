import { JavaObject } from '../../../../java/lang/JavaObject';
import { ENMv2Jahrgang } from '../../../../core/data/enm/v2/ENMv2Jahrgang';
import { ENMv2Teilleistungsart } from '../../../../core/data/enm/v2/ENMv2Teilleistungsart';
import { ENMv2Floskelgruppe } from '../../../../core/data/enm/v2/ENMv2Floskelgruppe';
import { ENMv2Schueler } from '../../../../core/data/enm/v2/ENMv2Schueler';
import { ENMv2Abteilung } from '../../../../core/data/enm/v2/ENMv2Abteilung';
import { ArrayList } from '../../../../java/util/ArrayList';
import { ENMv2Fach } from '../../../../core/data/enm/v2/ENMv2Fach';
import { ENMv2Klasse } from '../../../../core/data/enm/v2/ENMv2Klasse';
import { ENMv2AnkreuzkompetenzenKatalog } from '../../../../core/data/enm/v2/ENMv2AnkreuzkompetenzenKatalog';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { ENMv2Note } from '../../../../core/data/enm/v2/ENMv2Note';
import { ENMv2Lehrer } from '../../../../core/data/enm/v2/ENMv2Lehrer';
import { ENMv2Foerderschwerpunkt } from '../../../../core/data/enm/v2/ENMv2Foerderschwerpunkt';
import { ENMv2Lerngruppe } from '../../../../core/data/enm/v2/ENMv2Lerngruppe';

export class ENMv2Daten extends JavaObject {

	/**
	 * Die Revision des ENM-Datenformates, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1
	 */
	public enmRevision: number = 1;

	/**
	 * Die Schulnummer, für welche die ENM-Daten generiert wurden.
	 */
	public schulnummer: number = 0;

	/**
	 * Das Schuljahr, für welches die ENM-Daten generiert wurden.
	 */
	public schuljahr: number = 0;

	/**
	 * Die Anzahl der Abschnitte an der Schule (2: Halbjahrsmodus, 4: Quartalsmodus)
	 */
	public anzahlAbschnitte: number = 0;

	/**
	 * Gibt an, für welchen Abschnitt innerhalb des Schuljahres die ENM-Daten generiert wurden.
	 */
	public aktuellerAbschnitt: number = 0;

	/**
	 * Gibt die SVWS-ID des Lehrers an, für den die externe Notendatei generiert wurde. Ist die ID = NULL, enthält das Objekt alle Lehrerdaten.
	 */
	public lehrerID: number | null = null;

	/**
	 * Gibt die SVWS-ID des Schulleiters an.
	 */
	public idSchulleitung: number | null = null;

	/**
	 * Gibt die SVWS-ID des stellvertretenden Schulleiters an.
	 */
	public idSchulleitungStv: number | null = null;

	/**
	 * Gibt das Kürzel der Schulform der Schule an.
	 */
	public schulform: string | null = null;

	/**
	 * Der Katalog mit den gültigen Einträgen von Noten (als Übersicht für das ENM-Tool)
	 */
	public noten: List<ENMv2Note> = new ArrayList<ENMv2Note>();

	/**
	 * Der Katalog mit den gültigen Einträgen von Förderschwerpunkten (als Übersicht für das ENM-Tool)
	 */
	public foerderschwerpunkte: List<ENMv2Foerderschwerpunkt> = new ArrayList<ENMv2Foerderschwerpunkt>();

	/**
	 * Die Informationen zu den einzelnen Abteilungen.
	 */
	public abteilungen: List<ENMv2Abteilung> = new ArrayList<ENMv2Abteilung>();

	/**
	 * Die Informationen zu den einzelnen Jahrgängen, die in der Notendatei enthalten sind.
	 */
	public jahrgaenge: List<ENMv2Jahrgang> = new ArrayList<ENMv2Jahrgang>();

	/**
	 * Die Informationen zu den einzelnen Klassen, die in der Notendatei enthalten sind.
	 */
	public klassen: List<ENMv2Klasse> = new ArrayList<ENMv2Klasse>();

	/**
	 * Die Informationen der vordefinierten Floskelgruppen und deren Floskeln.
	 */
	public floskelgruppen: List<ENMv2Floskelgruppe> = new ArrayList<ENMv2Floskelgruppe>();

	/**
	 * Die Informationen zu Lehrern, die in der Notendatei vorhanden sind.
	 */
	public lehrer: List<ENMv2Lehrer> = new ArrayList<ENMv2Lehrer>();

	/**
	 * Die Informationen zu den Fächern, die in der Notendatei vorhanden sind.
	 */
	public faecher: List<ENMv2Fach> = new ArrayList<ENMv2Fach>();

	/**
	 * Der Katalog der Ankreuzkompetenzen (Grundschulzeugnisse und Inklusionszeugnisse)
	 */
	public ankreuzkompetenzen: ENMv2AnkreuzkompetenzenKatalog = new ENMv2AnkreuzkompetenzenKatalog();

	/**
	 * Die Informationen zu den Teilleistungsarten, die in der Notendatei vorhanden sind.
	 */
	public teilleistungsarten: List<ENMv2Teilleistungsart> = new ArrayList<ENMv2Teilleistungsart>();

	/**
	 * Die Informationen zu den Lerngruppen (Klassen und Kurse), die in der Notendatei vorhanden sind.
	 */
	public lerngruppen: List<ENMv2Lerngruppe> = new ArrayList<ENMv2Lerngruppe>();

	/**
	 * Die Informationen zu den Schülern, deren Noten in dieser Notendatei verwaltet werden.
	 */
	public schueler: List<ENMv2Schueler> = new ArrayList<ENMv2Schueler>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.v2.ENMv2Daten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.v2.ENMv2Daten'].includes(name);
	}

	public static readonly class = new Class<ENMv2Daten>('de.svws_nrw.core.data.enm.v2.ENMv2Daten');

	public static transpilerFromJSON(json: string): ENMv2Daten {
		const obj = JSON.parse(json) as Partial<ENMv2Daten>;
		const result = new ENMv2Daten();
		if (obj.enmRevision === undefined)
			throw new Error('invalid json format, missing attribute enmRevision');
		result.enmRevision = obj.enmRevision;
		if (obj.schulnummer === undefined)
			throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		if (obj.schuljahr === undefined)
			throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (obj.anzahlAbschnitte === undefined)
			throw new Error('invalid json format, missing attribute anzahlAbschnitte');
		result.anzahlAbschnitte = obj.anzahlAbschnitte;
		if (obj.aktuellerAbschnitt === undefined)
			throw new Error('invalid json format, missing attribute aktuellerAbschnitt');
		result.aktuellerAbschnitt = obj.aktuellerAbschnitt;
		result.lehrerID = (obj.lehrerID === undefined) ? null : obj.lehrerID === null ? null : obj.lehrerID;
		result.idSchulleitung = (obj.idSchulleitung === undefined) ? null : obj.idSchulleitung === null ? null : obj.idSchulleitung;
		result.idSchulleitungStv = (obj.idSchulleitungStv === undefined) ? null : obj.idSchulleitungStv === null ? null : obj.idSchulleitungStv;
		result.schulform = (obj.schulform === undefined) ? null : obj.schulform === null ? null : obj.schulform;
		if (obj.noten !== undefined) {
			for (const elem of obj.noten) {
				result.noten.add(ENMv2Note.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.foerderschwerpunkte !== undefined) {
			for (const elem of obj.foerderschwerpunkte) {
				result.foerderschwerpunkte.add(ENMv2Foerderschwerpunkt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.abteilungen !== undefined) {
			for (const elem of obj.abteilungen) {
				result.abteilungen.add(ENMv2Abteilung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.jahrgaenge !== undefined) {
			for (const elem of obj.jahrgaenge) {
				result.jahrgaenge.add(ENMv2Jahrgang.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassen !== undefined) {
			for (const elem of obj.klassen) {
				result.klassen.add(ENMv2Klasse.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.floskelgruppen !== undefined) {
			for (const elem of obj.floskelgruppen) {
				result.floskelgruppen.add(ENMv2Floskelgruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lehrer !== undefined) {
			for (const elem of obj.lehrer) {
				result.lehrer.add(ENMv2Lehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(ENMv2Fach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.ankreuzkompetenzen === undefined)
			throw new Error('invalid json format, missing attribute ankreuzkompetenzen');
		result.ankreuzkompetenzen = ENMv2AnkreuzkompetenzenKatalog.transpilerFromJSON(JSON.stringify(obj.ankreuzkompetenzen));
		if (obj.teilleistungsarten !== undefined) {
			for (const elem of obj.teilleistungsarten) {
				result.teilleistungsarten.add(ENMv2Teilleistungsart.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lerngruppen !== undefined) {
			for (const elem of obj.lerngruppen) {
				result.lerngruppen.add(ENMv2Lerngruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schueler !== undefined) {
			for (const elem of obj.schueler) {
				result.schueler.add(ENMv2Schueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ENMv2Daten): string {
		let result = '{';
		result += '"enmRevision" : ' + obj.enmRevision.toString() + ',';
		result += '"schulnummer" : ' + obj.schulnummer.toString() + ',';
		result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		result += '"anzahlAbschnitte" : ' + obj.anzahlAbschnitte.toString() + ',';
		result += '"aktuellerAbschnitt" : ' + obj.aktuellerAbschnitt.toString() + ',';
		result += '"lehrerID" : ' + ((obj.lehrerID === null) ? 'null' : obj.lehrerID.toString()) + ',';
		result += '"idSchulleitung" : ' + ((obj.idSchulleitung === null) ? 'null' : obj.idSchulleitung.toString()) + ',';
		result += '"idSchulleitungStv" : ' + ((obj.idSchulleitungStv === null) ? 'null' : obj.idSchulleitungStv.toString()) + ',';
		result += '"schulform" : ' + ((obj.schulform === null) ? 'null' : JSON.stringify(obj.schulform)) + ',';
		result += '"noten" : [ ';
		for (let i = 0; i < obj.noten.size(); i++) {
			const elem = obj.noten.get(i);
			result += ENMv2Note.transpilerToJSON(elem);
			if (i < obj.noten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"foerderschwerpunkte" : [ ';
		for (let i = 0; i < obj.foerderschwerpunkte.size(); i++) {
			const elem = obj.foerderschwerpunkte.get(i);
			result += ENMv2Foerderschwerpunkt.transpilerToJSON(elem);
			if (i < obj.foerderschwerpunkte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"abteilungen" : [ ';
		for (let i = 0; i < obj.abteilungen.size(); i++) {
			const elem = obj.abteilungen.get(i);
			result += ENMv2Abteilung.transpilerToJSON(elem);
			if (i < obj.abteilungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"jahrgaenge" : [ ';
		for (let i = 0; i < obj.jahrgaenge.size(); i++) {
			const elem = obj.jahrgaenge.get(i);
			result += ENMv2Jahrgang.transpilerToJSON(elem);
			if (i < obj.jahrgaenge.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassen" : [ ';
		for (let i = 0; i < obj.klassen.size(); i++) {
			const elem = obj.klassen.get(i);
			result += ENMv2Klasse.transpilerToJSON(elem);
			if (i < obj.klassen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"floskelgruppen" : [ ';
		for (let i = 0; i < obj.floskelgruppen.size(); i++) {
			const elem = obj.floskelgruppen.get(i);
			result += ENMv2Floskelgruppe.transpilerToJSON(elem);
			if (i < obj.floskelgruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lehrer" : [ ';
		for (let i = 0; i < obj.lehrer.size(); i++) {
			const elem = obj.lehrer.get(i);
			result += ENMv2Lehrer.transpilerToJSON(elem);
			if (i < obj.lehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += ENMv2Fach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"ankreuzkompetenzen" : ' + ENMv2AnkreuzkompetenzenKatalog.transpilerToJSON(obj.ankreuzkompetenzen) + ',';
		result += '"teilleistungsarten" : [ ';
		for (let i = 0; i < obj.teilleistungsarten.size(); i++) {
			const elem = obj.teilleistungsarten.get(i);
			result += ENMv2Teilleistungsart.transpilerToJSON(elem);
			if (i < obj.teilleistungsarten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lerngruppen" : [ ';
		for (let i = 0; i < obj.lerngruppen.size(); i++) {
			const elem = obj.lerngruppen.get(i);
			result += ENMv2Lerngruppe.transpilerToJSON(elem);
			if (i < obj.lerngruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schueler" : [ ';
		for (let i = 0; i < obj.schueler.size(); i++) {
			const elem = obj.schueler.get(i);
			result += ENMv2Schueler.transpilerToJSON(elem);
			if (i < obj.schueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMv2Daten>): string {
		let result = '{';
		if (obj.enmRevision !== undefined) {
			result += '"enmRevision" : ' + obj.enmRevision.toString() + ',';
		}
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + obj.schulnummer.toString() + ',';
		}
		if (obj.schuljahr !== undefined) {
			result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		}
		if (obj.anzahlAbschnitte !== undefined) {
			result += '"anzahlAbschnitte" : ' + obj.anzahlAbschnitte.toString() + ',';
		}
		if (obj.aktuellerAbschnitt !== undefined) {
			result += '"aktuellerAbschnitt" : ' + obj.aktuellerAbschnitt.toString() + ',';
		}
		if (obj.lehrerID !== undefined) {
			result += '"lehrerID" : ' + ((obj.lehrerID === null) ? 'null' : obj.lehrerID.toString()) + ',';
		}
		if (obj.idSchulleitung !== undefined) {
			result += '"idSchulleitung" : ' + ((obj.idSchulleitung === null) ? 'null' : obj.idSchulleitung.toString()) + ',';
		}
		if (obj.idSchulleitungStv !== undefined) {
			result += '"idSchulleitungStv" : ' + ((obj.idSchulleitungStv === null) ? 'null' : obj.idSchulleitungStv.toString()) + ',';
		}
		if (obj.schulform !== undefined) {
			result += '"schulform" : ' + ((obj.schulform === null) ? 'null' : JSON.stringify(obj.schulform)) + ',';
		}
		if (obj.noten !== undefined) {
			result += '"noten" : [ ';
			for (let i = 0; i < obj.noten.size(); i++) {
				const elem = obj.noten.get(i);
				result += ENMv2Note.transpilerToJSON(elem);
				if (i < obj.noten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.foerderschwerpunkte !== undefined) {
			result += '"foerderschwerpunkte" : [ ';
			for (let i = 0; i < obj.foerderschwerpunkte.size(); i++) {
				const elem = obj.foerderschwerpunkte.get(i);
				result += ENMv2Foerderschwerpunkt.transpilerToJSON(elem);
				if (i < obj.foerderschwerpunkte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.abteilungen !== undefined) {
			result += '"abteilungen" : [ ';
			for (let i = 0; i < obj.abteilungen.size(); i++) {
				const elem = obj.abteilungen.get(i);
				result += ENMv2Abteilung.transpilerToJSON(elem);
				if (i < obj.abteilungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.jahrgaenge !== undefined) {
			result += '"jahrgaenge" : [ ';
			for (let i = 0; i < obj.jahrgaenge.size(); i++) {
				const elem = obj.jahrgaenge.get(i);
				result += ENMv2Jahrgang.transpilerToJSON(elem);
				if (i < obj.jahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassen !== undefined) {
			result += '"klassen" : [ ';
			for (let i = 0; i < obj.klassen.size(); i++) {
				const elem = obj.klassen.get(i);
				result += ENMv2Klasse.transpilerToJSON(elem);
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.floskelgruppen !== undefined) {
			result += '"floskelgruppen" : [ ';
			for (let i = 0; i < obj.floskelgruppen.size(); i++) {
				const elem = obj.floskelgruppen.get(i);
				result += ENMv2Floskelgruppe.transpilerToJSON(elem);
				if (i < obj.floskelgruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += ENMv2Lehrer.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.faecher !== undefined) {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += ENMv2Fach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.ankreuzkompetenzen !== undefined) {
			result += '"ankreuzkompetenzen" : ' + ENMv2AnkreuzkompetenzenKatalog.transpilerToJSON(obj.ankreuzkompetenzen) + ',';
		}
		if (obj.teilleistungsarten !== undefined) {
			result += '"teilleistungsarten" : [ ';
			for (let i = 0; i < obj.teilleistungsarten.size(); i++) {
				const elem = obj.teilleistungsarten.get(i);
				result += ENMv2Teilleistungsart.transpilerToJSON(elem);
				if (i < obj.teilleistungsarten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lerngruppen !== undefined) {
			result += '"lerngruppen" : [ ';
			for (let i = 0; i < obj.lerngruppen.size(); i++) {
				const elem = obj.lerngruppen.get(i);
				result += ENMv2Lerngruppe.transpilerToJSON(elem);
				if (i < obj.lerngruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schueler !== undefined) {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += ENMv2Schueler.transpilerToJSON(elem);
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

export function cast_de_svws_nrw_core_data_enm_v2_ENMv2Daten(obj: unknown): ENMv2Daten {
	return obj as ENMv2Daten;
}
