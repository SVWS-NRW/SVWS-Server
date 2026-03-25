import { JavaObject } from '../../../../java/lang/JavaObject';
import { ENMv1Schueler } from '../../../../core/data/enm/v1/ENMv1Schueler';
import { ENMv1Fach } from '../../../../core/data/enm/v1/ENMv1Fach';
import { ENMv1Jahrgang } from '../../../../core/data/enm/v1/ENMv1Jahrgang';
import { ENMv1AnkreuzkompetenzenKatalog } from '../../../../core/data/enm/v1/ENMv1AnkreuzkompetenzenKatalog';
import { ENMv1Foerderschwerpunkt } from '../../../../core/data/enm/v1/ENMv1Foerderschwerpunkt';
import { ENMv1Lerngruppe } from '../../../../core/data/enm/v1/ENMv1Lerngruppe';
import { ArrayList } from '../../../../java/util/ArrayList';
import { ENMv1Lehrer } from '../../../../core/data/enm/v1/ENMv1Lehrer';
import { ENMv1Floskelgruppe } from '../../../../core/data/enm/v1/ENMv1Floskelgruppe';
import { ENMv1Note } from '../../../../core/data/enm/v1/ENMv1Note';
import { ENMv1Klasse } from '../../../../core/data/enm/v1/ENMv1Klasse';
import { ENMv1Teilleistungsart } from '../../../../core/data/enm/v1/ENMv1Teilleistungsart';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class ENMv1Daten extends JavaObject {

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
	 * Gibt den öffentlichen Schlüssel an, welcher für die Verschlüsselung und den Rückversand der Datei genutzt werden soll.
	 */
	public publicKey: string | null = null;

	/**
	 * Gibt die SVWS-ID des Lehrers an, für den die externe Notendatei generiert wurde. Ist die ID = NULL, enthält das Objekt alle Lehrerdaten.
	 */
	public lehrerID: number | null = null;

	/**
	 * Gibt an, ob die Fehlstunden-Eingabe durch das externe Notenmodul erlaubt ist oder nicht.
	 */
	public fehlstundenEingabe: boolean = false;

	/**
	 * Gibt an, ob die Fehlstunden für die Sekundarstufe I fachbezogen eingetragen werden oder nicht.
	 */
	public fehlstundenSIFachbezogen: boolean = false;

	/**
	 * Gibt an, ob die Fehlstunden für die Sekundarstufe II fachbezogen eingetragen werden oder nicht.
	 */
	public fehlstundenSIIFachbezogen: boolean = false;

	/**
	 * Gibt das Kürzel der Schulform der Schule an.
	 */
	public schulform: string | null = null;

	/**
	 * Gibt die Mailadresse an, an welche die verschlüsselte Datei zurückgesendet werden soll (z.B. mail@schule.nrw.de).
	 */
	public mailadresse: string | null = null;

	/**
	 * Der Katalog mit den gültigen Einträgen von Noten (als Übersicht für das ENM-Tool)
	 */
	public noten: List<ENMv1Note> = new ArrayList<ENMv1Note>();

	/**
	 * Der Katalog mit den gültigen Einträgen von Förderschwerpunkten (als Übersicht für das ENM-Tool)
	 */
	public foerderschwerpunkte: List<ENMv1Foerderschwerpunkt> = new ArrayList<ENMv1Foerderschwerpunkt>();

	/**
	 * Die Informationen zu den einzelnen Jahrgängen, die in der Notendatei enthalten sind.
	 */
	public jahrgaenge: List<ENMv1Jahrgang> = new ArrayList<ENMv1Jahrgang>();

	/**
	 * Die Informationen zu den einzelnen Klassen, die in der Notendatei enthalten sind.
	 */
	public klassen: List<ENMv1Klasse> = new ArrayList<ENMv1Klasse>();

	/**
	 * Die Informationen der vordefinierten Floskelgruppen und deren Floskeln.
	 */
	public floskelgruppen: List<ENMv1Floskelgruppe> = new ArrayList<ENMv1Floskelgruppe>();

	/**
	 * Die Informationen zu Lehrern, die in der Notendatei vorhanden sind.
	 */
	public lehrer: List<ENMv1Lehrer> = new ArrayList<ENMv1Lehrer>();

	/**
	 * Die Informationen zu den Fächern, die in der Notendatei vorhanden sind.
	 */
	public faecher: List<ENMv1Fach> = new ArrayList<ENMv1Fach>();

	/**
	 * Der Katalog der Ankreuzkompetenzen (Grundschulzeugnisse und Inklusionszeugnisse)
	 */
	public ankreuzkompetenzen: ENMv1AnkreuzkompetenzenKatalog = new ENMv1AnkreuzkompetenzenKatalog();

	/**
	 * Die Informationen zu den Teilleistungsarten, die in der Notendatei vorhanden sind.
	 */
	public teilleistungsarten: List<ENMv1Teilleistungsart> = new ArrayList<ENMv1Teilleistungsart>();

	/**
	 * Die Informationen zu den Lerngruppen (Klassen und Kurse), die in der Notendatei vorhanden sind.
	 */
	public lerngruppen: List<ENMv1Lerngruppe> = new ArrayList<ENMv1Lerngruppe>();

	/**
	 * Die Informationen zu den Schülern, deren Noten in dieser Notendatei verwaltet werden.
	 */
	public schueler: List<ENMv1Schueler> = new ArrayList<ENMv1Schueler>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.v1.ENMv1Daten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.v1.ENMv1Daten'].includes(name);
	}

	public static readonly class = new Class<ENMv1Daten>('de.svws_nrw.core.data.enm.v1.ENMv1Daten');

	public static transpilerFromJSON(json: string): ENMv1Daten {
		const obj = JSON.parse(json) as Partial<ENMv1Daten>;
		const result = new ENMv1Daten();
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
		result.publicKey = (obj.publicKey === undefined) ? null : obj.publicKey === null ? null : obj.publicKey;
		result.lehrerID = (obj.lehrerID === undefined) ? null : obj.lehrerID === null ? null : obj.lehrerID;
		if (obj.fehlstundenEingabe === undefined)
			throw new Error('invalid json format, missing attribute fehlstundenEingabe');
		result.fehlstundenEingabe = obj.fehlstundenEingabe;
		if (obj.fehlstundenSIFachbezogen === undefined)
			throw new Error('invalid json format, missing attribute fehlstundenSIFachbezogen');
		result.fehlstundenSIFachbezogen = obj.fehlstundenSIFachbezogen;
		if (obj.fehlstundenSIIFachbezogen === undefined)
			throw new Error('invalid json format, missing attribute fehlstundenSIIFachbezogen');
		result.fehlstundenSIIFachbezogen = obj.fehlstundenSIIFachbezogen;
		result.schulform = (obj.schulform === undefined) ? null : obj.schulform === null ? null : obj.schulform;
		result.mailadresse = (obj.mailadresse === undefined) ? null : obj.mailadresse === null ? null : obj.mailadresse;
		if (obj.noten !== undefined) {
			for (const elem of obj.noten) {
				result.noten.add(ENMv1Note.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.foerderschwerpunkte !== undefined) {
			for (const elem of obj.foerderschwerpunkte) {
				result.foerderschwerpunkte.add(ENMv1Foerderschwerpunkt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.jahrgaenge !== undefined) {
			for (const elem of obj.jahrgaenge) {
				result.jahrgaenge.add(ENMv1Jahrgang.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassen !== undefined) {
			for (const elem of obj.klassen) {
				result.klassen.add(ENMv1Klasse.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.floskelgruppen !== undefined) {
			for (const elem of obj.floskelgruppen) {
				result.floskelgruppen.add(ENMv1Floskelgruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lehrer !== undefined) {
			for (const elem of obj.lehrer) {
				result.lehrer.add(ENMv1Lehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(ENMv1Fach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.ankreuzkompetenzen === undefined)
			throw new Error('invalid json format, missing attribute ankreuzkompetenzen');
		result.ankreuzkompetenzen = ENMv1AnkreuzkompetenzenKatalog.transpilerFromJSON(JSON.stringify(obj.ankreuzkompetenzen));
		if (obj.teilleistungsarten !== undefined) {
			for (const elem of obj.teilleistungsarten) {
				result.teilleistungsarten.add(ENMv1Teilleistungsart.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lerngruppen !== undefined) {
			for (const elem of obj.lerngruppen) {
				result.lerngruppen.add(ENMv1Lerngruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schueler !== undefined) {
			for (const elem of obj.schueler) {
				result.schueler.add(ENMv1Schueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ENMv1Daten): string {
		let result = '{';
		result += '"enmRevision" : ' + obj.enmRevision.toString() + ',';
		result += '"schulnummer" : ' + obj.schulnummer.toString() + ',';
		result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		result += '"anzahlAbschnitte" : ' + obj.anzahlAbschnitte.toString() + ',';
		result += '"aktuellerAbschnitt" : ' + obj.aktuellerAbschnitt.toString() + ',';
		result += '"publicKey" : ' + ((obj.publicKey === null) ? 'null' : JSON.stringify(obj.publicKey)) + ',';
		result += '"lehrerID" : ' + ((obj.lehrerID === null) ? 'null' : obj.lehrerID.toString()) + ',';
		result += '"fehlstundenEingabe" : ' + obj.fehlstundenEingabe.toString() + ',';
		result += '"fehlstundenSIFachbezogen" : ' + obj.fehlstundenSIFachbezogen.toString() + ',';
		result += '"fehlstundenSIIFachbezogen" : ' + obj.fehlstundenSIIFachbezogen.toString() + ',';
		result += '"schulform" : ' + ((obj.schulform === null) ? 'null' : JSON.stringify(obj.schulform)) + ',';
		result += '"mailadresse" : ' + ((obj.mailadresse === null) ? 'null' : JSON.stringify(obj.mailadresse)) + ',';
		result += '"noten" : [ ';
		for (let i = 0; i < obj.noten.size(); i++) {
			const elem = obj.noten.get(i);
			result += ENMv1Note.transpilerToJSON(elem);
			if (i < obj.noten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"foerderschwerpunkte" : [ ';
		for (let i = 0; i < obj.foerderschwerpunkte.size(); i++) {
			const elem = obj.foerderschwerpunkte.get(i);
			result += ENMv1Foerderschwerpunkt.transpilerToJSON(elem);
			if (i < obj.foerderschwerpunkte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"jahrgaenge" : [ ';
		for (let i = 0; i < obj.jahrgaenge.size(); i++) {
			const elem = obj.jahrgaenge.get(i);
			result += ENMv1Jahrgang.transpilerToJSON(elem);
			if (i < obj.jahrgaenge.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassen" : [ ';
		for (let i = 0; i < obj.klassen.size(); i++) {
			const elem = obj.klassen.get(i);
			result += ENMv1Klasse.transpilerToJSON(elem);
			if (i < obj.klassen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"floskelgruppen" : [ ';
		for (let i = 0; i < obj.floskelgruppen.size(); i++) {
			const elem = obj.floskelgruppen.get(i);
			result += ENMv1Floskelgruppe.transpilerToJSON(elem);
			if (i < obj.floskelgruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lehrer" : [ ';
		for (let i = 0; i < obj.lehrer.size(); i++) {
			const elem = obj.lehrer.get(i);
			result += ENMv1Lehrer.transpilerToJSON(elem);
			if (i < obj.lehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += ENMv1Fach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"ankreuzkompetenzen" : ' + ENMv1AnkreuzkompetenzenKatalog.transpilerToJSON(obj.ankreuzkompetenzen) + ',';
		result += '"teilleistungsarten" : [ ';
		for (let i = 0; i < obj.teilleistungsarten.size(); i++) {
			const elem = obj.teilleistungsarten.get(i);
			result += ENMv1Teilleistungsart.transpilerToJSON(elem);
			if (i < obj.teilleistungsarten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lerngruppen" : [ ';
		for (let i = 0; i < obj.lerngruppen.size(); i++) {
			const elem = obj.lerngruppen.get(i);
			result += ENMv1Lerngruppe.transpilerToJSON(elem);
			if (i < obj.lerngruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schueler" : [ ';
		for (let i = 0; i < obj.schueler.size(); i++) {
			const elem = obj.schueler.get(i);
			result += ENMv1Schueler.transpilerToJSON(elem);
			if (i < obj.schueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMv1Daten>): string {
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
		if (obj.publicKey !== undefined) {
			result += '"publicKey" : ' + ((obj.publicKey === null) ? 'null' : JSON.stringify(obj.publicKey)) + ',';
		}
		if (obj.lehrerID !== undefined) {
			result += '"lehrerID" : ' + ((obj.lehrerID === null) ? 'null' : obj.lehrerID.toString()) + ',';
		}
		if (obj.fehlstundenEingabe !== undefined) {
			result += '"fehlstundenEingabe" : ' + obj.fehlstundenEingabe.toString() + ',';
		}
		if (obj.fehlstundenSIFachbezogen !== undefined) {
			result += '"fehlstundenSIFachbezogen" : ' + obj.fehlstundenSIFachbezogen.toString() + ',';
		}
		if (obj.fehlstundenSIIFachbezogen !== undefined) {
			result += '"fehlstundenSIIFachbezogen" : ' + obj.fehlstundenSIIFachbezogen.toString() + ',';
		}
		if (obj.schulform !== undefined) {
			result += '"schulform" : ' + ((obj.schulform === null) ? 'null' : JSON.stringify(obj.schulform)) + ',';
		}
		if (obj.mailadresse !== undefined) {
			result += '"mailadresse" : ' + ((obj.mailadresse === null) ? 'null' : JSON.stringify(obj.mailadresse)) + ',';
		}
		if (obj.noten !== undefined) {
			result += '"noten" : [ ';
			for (let i = 0; i < obj.noten.size(); i++) {
				const elem = obj.noten.get(i);
				result += ENMv1Note.transpilerToJSON(elem);
				if (i < obj.noten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.foerderschwerpunkte !== undefined) {
			result += '"foerderschwerpunkte" : [ ';
			for (let i = 0; i < obj.foerderschwerpunkte.size(); i++) {
				const elem = obj.foerderschwerpunkte.get(i);
				result += ENMv1Foerderschwerpunkt.transpilerToJSON(elem);
				if (i < obj.foerderschwerpunkte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.jahrgaenge !== undefined) {
			result += '"jahrgaenge" : [ ';
			for (let i = 0; i < obj.jahrgaenge.size(); i++) {
				const elem = obj.jahrgaenge.get(i);
				result += ENMv1Jahrgang.transpilerToJSON(elem);
				if (i < obj.jahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassen !== undefined) {
			result += '"klassen" : [ ';
			for (let i = 0; i < obj.klassen.size(); i++) {
				const elem = obj.klassen.get(i);
				result += ENMv1Klasse.transpilerToJSON(elem);
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.floskelgruppen !== undefined) {
			result += '"floskelgruppen" : [ ';
			for (let i = 0; i < obj.floskelgruppen.size(); i++) {
				const elem = obj.floskelgruppen.get(i);
				result += ENMv1Floskelgruppe.transpilerToJSON(elem);
				if (i < obj.floskelgruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += ENMv1Lehrer.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.faecher !== undefined) {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += ENMv1Fach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.ankreuzkompetenzen !== undefined) {
			result += '"ankreuzkompetenzen" : ' + ENMv1AnkreuzkompetenzenKatalog.transpilerToJSON(obj.ankreuzkompetenzen) + ',';
		}
		if (obj.teilleistungsarten !== undefined) {
			result += '"teilleistungsarten" : [ ';
			for (let i = 0; i < obj.teilleistungsarten.size(); i++) {
				const elem = obj.teilleistungsarten.get(i);
				result += ENMv1Teilleistungsart.transpilerToJSON(elem);
				if (i < obj.teilleistungsarten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lerngruppen !== undefined) {
			result += '"lerngruppen" : [ ';
			for (let i = 0; i < obj.lerngruppen.size(); i++) {
				const elem = obj.lerngruppen.get(i);
				result += ENMv1Lerngruppe.transpilerToJSON(elem);
				if (i < obj.lerngruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schueler !== undefined) {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += ENMv1Schueler.transpilerToJSON(elem);
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

export function cast_de_svws_nrw_core_data_enm_v1_ENMv1Daten(obj: unknown): ENMv1Daten {
	return obj as ENMv1Daten;
}
