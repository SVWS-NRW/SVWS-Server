import { JavaObject } from '../../../java/lang/JavaObject';
import { ENMKlasse } from '../../../core/data/enm/ENMKlasse';
import { ENMTeilleistungsart } from '../../../core/data/enm/ENMTeilleistungsart';
import { ENMFach } from '../../../core/data/enm/ENMFach';
import { ENMJahrgang } from '../../../core/data/enm/ENMJahrgang';
import { ArrayList } from '../../../java/util/ArrayList';
import { ENMLerngruppe } from '../../../core/data/enm/ENMLerngruppe';
import { ENMLehrer } from '../../../core/data/enm/ENMLehrer';
import { ENMSchueler } from '../../../core/data/enm/ENMSchueler';
import { ENMAnkreuzkompetenzenKatalog } from '../../../core/data/enm/ENMAnkreuzkompetenzenKatalog';
import { ENMFoerderschwerpunkt } from '../../../core/data/enm/ENMFoerderschwerpunkt';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ENMNote } from '../../../core/data/enm/ENMNote';
import { ENMFloskelgruppe } from '../../../core/data/enm/ENMFloskelgruppe';

export class ENMDaten extends JavaObject {

	/**
	 * Die Revision des ENM-Datenformates, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1
	 */
	public enmRevision : number = -1;

	/**
	 * Die Schulnummer, für welche die ENM-Daten generiert wurden.
	 */
	public schulnummer : number = 0;

	/**
	 * Das Schuljahr, für welches die ENM-Daten generiert wurden.
	 */
	public schuljahr : number = 0;

	/**
	 * Die Anzahl der Abschnitte an der Schule (2: Halbjahrsmodus, 4: Quartalsmodus)
	 */
	public anzahlAbschnitte : number = 0;

	/**
	 * Gibt an, für welchen Abschnitt innerhalb des Schuljahres die ENM-Daten generiert wurden.
	 */
	public aktuellerAbschnitt : number = 0;

	/**
	 * Gibt den öffentlichen Schlüssel an, welcher für die Verschlüsselung und den Rückversand der Datei genutzt werden soll.
	 */
	public publicKey : string | null = null;

	/**
	 * Gibt die SVWS-ID des Lehrers an, für den die externe Notendatei generiert wurde. Ist die ID = NULL, enthält das Objekt alle Lehrerdaten.
	 */
	public lehrerID : number | null = null;

	/**
	 * Gibt an, ob die Fehlstunden-Eingabe durch das externe Notenmodul erlaubt ist oder nicht.
	 */
	public fehlstundenEingabe : boolean = false;

	/**
	 * Gibt an, ob die Fehlstunden für die Sekundarstufe I fachbezogen eingetragen werden oder nicht.
	 */
	public fehlstundenSIFachbezogen : boolean = false;

	/**
	 * Gibt an, ob die Fehlstunden für die Sekundarstufe II fachbezogen eingetragen werden oder nicht.
	 */
	public fehlstundenSIIFachbezogen : boolean = false;

	/**
	 * Gibt das Kürzel der Schulform der Schule an.
	 */
	public schulform : string | null = null;

	/**
	 * Gibt die Mailadresse an, an welche die verschlüsselte Datei zurückgesendet werden soll (z.B. mail@schule.nrw.de).
	 */
	public mailadresse : string | null = null;

	/**
	 * Der Katalog mit den gültigen Einträgen von Noten (als Übersicht für das ENM-Tool)
	 */
	public noten : List<ENMNote> = new ArrayList<ENMNote>();

	/**
	 * Der Katalog mit den gültigen Einträgen von Förderschwerpunkten (als Übersicht für das ENM-Tool)
	 */
	public foerderschwerpunkte : List<ENMFoerderschwerpunkt> = new ArrayList<ENMFoerderschwerpunkt>();

	/**
	 * Die Informationen zu den einzelnen Jahrgängen, die in der Notendatei enthalten sind.
	 */
	public jahrgaenge : List<ENMJahrgang> = new ArrayList<ENMJahrgang>();

	/**
	 * Die Informationen zu den einzelnen Klassen, die in der Notendatei enthalten sind.
	 */
	public klassen : List<ENMKlasse> = new ArrayList<ENMKlasse>();

	/**
	 * Die Informationen der vordefinierten Floskelgruppen und deren Floskeln.
	 */
	public floskelgruppen : List<ENMFloskelgruppe> = new ArrayList<ENMFloskelgruppe>();

	/**
	 * Die Informationen zu Lehrern, die in der Notendatei vorhanden sind.
	 */
	public lehrer : List<ENMLehrer> = new ArrayList<ENMLehrer>();

	/**
	 * Die Informationen zu den Fächern, die in der Notendatei vorhanden sind.
	 */
	public faecher : List<ENMFach> = new ArrayList<ENMFach>();

	/**
	 * Der Katalog der Ankreuzkompetenzen (Grundschulzeugnisse und Inklusionszeugnisse)
	 */
	public ankreuzkompetenzen : ENMAnkreuzkompetenzenKatalog = new ENMAnkreuzkompetenzenKatalog();

	/**
	 * Die Informationen zu den Teilleistungsarten, die in der Notendatei vorhanden sind.
	 */
	public teilleistungsarten : List<ENMTeilleistungsart> = new ArrayList<ENMTeilleistungsart>();

	/**
	 * Die Informationen zu den Lerngruppen (Klassen und Kurse), die in der Notendatei vorhanden sind.
	 */
	public lerngruppen : List<ENMLerngruppe> = new ArrayList<ENMLerngruppe>();

	/**
	 * Die Informationen zu den Schülern, deren Noten in dieser Notendatei verwaltet werden.
	 */
	public schueler : List<ENMSchueler> = new ArrayList<ENMSchueler>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMDaten'].includes(name);
	}

	public static class = new Class<ENMDaten>('de.svws_nrw.core.data.enm.ENMDaten');

	public static transpilerFromJSON(json : string): ENMDaten {
		const obj = JSON.parse(json) as Partial<ENMDaten>;
		const result = new ENMDaten();
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
				result.noten.add(ENMNote.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.foerderschwerpunkte !== undefined) {
			for (const elem of obj.foerderschwerpunkte) {
				result.foerderschwerpunkte.add(ENMFoerderschwerpunkt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.jahrgaenge !== undefined) {
			for (const elem of obj.jahrgaenge) {
				result.jahrgaenge.add(ENMJahrgang.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassen !== undefined) {
			for (const elem of obj.klassen) {
				result.klassen.add(ENMKlasse.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.floskelgruppen !== undefined) {
			for (const elem of obj.floskelgruppen) {
				result.floskelgruppen.add(ENMFloskelgruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lehrer !== undefined) {
			for (const elem of obj.lehrer) {
				result.lehrer.add(ENMLehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(ENMFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.ankreuzkompetenzen === undefined)
			throw new Error('invalid json format, missing attribute ankreuzkompetenzen');
		result.ankreuzkompetenzen = ENMAnkreuzkompetenzenKatalog.transpilerFromJSON(JSON.stringify(obj.ankreuzkompetenzen));
		if (obj.teilleistungsarten !== undefined) {
			for (const elem of obj.teilleistungsarten) {
				result.teilleistungsarten.add(ENMTeilleistungsart.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lerngruppen !== undefined) {
			for (const elem of obj.lerngruppen) {
				result.lerngruppen.add(ENMLerngruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schueler !== undefined) {
			for (const elem of obj.schueler) {
				result.schueler.add(ENMSchueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : ENMDaten) : string {
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
			result += ENMNote.transpilerToJSON(elem);
			if (i < obj.noten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"foerderschwerpunkte" : [ ';
		for (let i = 0; i < obj.foerderschwerpunkte.size(); i++) {
			const elem = obj.foerderschwerpunkte.get(i);
			result += ENMFoerderschwerpunkt.transpilerToJSON(elem);
			if (i < obj.foerderschwerpunkte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"jahrgaenge" : [ ';
		for (let i = 0; i < obj.jahrgaenge.size(); i++) {
			const elem = obj.jahrgaenge.get(i);
			result += ENMJahrgang.transpilerToJSON(elem);
			if (i < obj.jahrgaenge.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassen" : [ ';
		for (let i = 0; i < obj.klassen.size(); i++) {
			const elem = obj.klassen.get(i);
			result += ENMKlasse.transpilerToJSON(elem);
			if (i < obj.klassen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"floskelgruppen" : [ ';
		for (let i = 0; i < obj.floskelgruppen.size(); i++) {
			const elem = obj.floskelgruppen.get(i);
			result += ENMFloskelgruppe.transpilerToJSON(elem);
			if (i < obj.floskelgruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lehrer" : [ ';
		for (let i = 0; i < obj.lehrer.size(); i++) {
			const elem = obj.lehrer.get(i);
			result += ENMLehrer.transpilerToJSON(elem);
			if (i < obj.lehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += ENMFach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"ankreuzkompetenzen" : ' + ENMAnkreuzkompetenzenKatalog.transpilerToJSON(obj.ankreuzkompetenzen) + ',';
		result += '"teilleistungsarten" : [ ';
		for (let i = 0; i < obj.teilleistungsarten.size(); i++) {
			const elem = obj.teilleistungsarten.get(i);
			result += ENMTeilleistungsart.transpilerToJSON(elem);
			if (i < obj.teilleistungsarten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lerngruppen" : [ ';
		for (let i = 0; i < obj.lerngruppen.size(); i++) {
			const elem = obj.lerngruppen.get(i);
			result += ENMLerngruppe.transpilerToJSON(elem);
			if (i < obj.lerngruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schueler" : [ ';
		for (let i = 0; i < obj.schueler.size(); i++) {
			const elem = obj.schueler.get(i);
			result += ENMSchueler.transpilerToJSON(elem);
			if (i < obj.schueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMDaten>) : string {
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
				result += ENMNote.transpilerToJSON(elem);
				if (i < obj.noten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.foerderschwerpunkte !== undefined) {
			result += '"foerderschwerpunkte" : [ ';
			for (let i = 0; i < obj.foerderschwerpunkte.size(); i++) {
				const elem = obj.foerderschwerpunkte.get(i);
				result += ENMFoerderschwerpunkt.transpilerToJSON(elem);
				if (i < obj.foerderschwerpunkte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.jahrgaenge !== undefined) {
			result += '"jahrgaenge" : [ ';
			for (let i = 0; i < obj.jahrgaenge.size(); i++) {
				const elem = obj.jahrgaenge.get(i);
				result += ENMJahrgang.transpilerToJSON(elem);
				if (i < obj.jahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassen !== undefined) {
			result += '"klassen" : [ ';
			for (let i = 0; i < obj.klassen.size(); i++) {
				const elem = obj.klassen.get(i);
				result += ENMKlasse.transpilerToJSON(elem);
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.floskelgruppen !== undefined) {
			result += '"floskelgruppen" : [ ';
			for (let i = 0; i < obj.floskelgruppen.size(); i++) {
				const elem = obj.floskelgruppen.get(i);
				result += ENMFloskelgruppe.transpilerToJSON(elem);
				if (i < obj.floskelgruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += ENMLehrer.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.faecher !== undefined) {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += ENMFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.ankreuzkompetenzen !== undefined) {
			result += '"ankreuzkompetenzen" : ' + ENMAnkreuzkompetenzenKatalog.transpilerToJSON(obj.ankreuzkompetenzen) + ',';
		}
		if (obj.teilleistungsarten !== undefined) {
			result += '"teilleistungsarten" : [ ';
			for (let i = 0; i < obj.teilleistungsarten.size(); i++) {
				const elem = obj.teilleistungsarten.get(i);
				result += ENMTeilleistungsart.transpilerToJSON(elem);
				if (i < obj.teilleistungsarten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lerngruppen !== undefined) {
			result += '"lerngruppen" : [ ';
			for (let i = 0; i < obj.lerngruppen.size(); i++) {
				const elem = obj.lerngruppen.get(i);
				result += ENMLerngruppe.transpilerToJSON(elem);
				if (i < obj.lerngruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schueler !== undefined) {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += ENMSchueler.transpilerToJSON(elem);
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

export function cast_de_svws_nrw_core_data_enm_ENMDaten(obj : unknown) : ENMDaten {
	return obj as ENMDaten;
}
