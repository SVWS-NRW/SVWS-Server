import type { BenutzerListeEintrag } from "../../../core/src";
import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob Benutzername gültig ist oder nicht.
 * Dabei wird auch case-insensitiv geprüft, ob er in der Liste der vorhandenen Benutzer enthalten ist oder nicht.
 */
export class ValidatorBenutzerAnmeldenameNeu extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => string | null | undefined;

	/** Die Menge der existierenden Anmeldenamen aus der Benutzerliste in lower case. */
	private readonly menge: Set<string> = new Set<string>;


	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data    die Funktion zum Zugriff auf die Daten
	 * @param menge   die Menge der existierenden Anmeldenamen aus der Benutzerliste
	 */
	constructor(data: () => string | null | undefined, list: Iterable<BenutzerListeEintrag>) {
		super(ValidatorFehlerart.MUSS);
		this.data = data;
		for (const b of list) {
			this.menge.add(b.name.toLocaleLowerCase('de'));
		}
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor den Anmeldenamen
	 * für die Prüfung ermittelt und anschließend prüft, ob dieser gültig ist und
	 * nicht in der Menge der bereits existierenden Anmeldenamen enthalten ist.
	 *
	 * @returns true, wenn der Anmeldename gültig ist und nicht bereits existiert.
	 */
	protected pruefe(): boolean {
		// Ermittle die Daten und prüfe, ob der String angegeben und nicht leer ist
		const data = this.data();
		if ((data === undefined) || (data === null) || (data === "")) {
			this.addFehler(0, "Der Anmeldename muss angegeben werden.");
			return false;
		}

		// Prüft auf Whitespace Character
		if (/\s/.exec(data) !== null) {
			this.addFehler(0, "Der Anmeldename darf keine Leerzeichen, Tabs, etc. enthalten.");
			return false;
		}

		// Prüfe, ob der Anmeldename bereits vorhanden ist
		if (this.menge.has(data.toLocaleLowerCase('de'))) {
			this.addFehler(0, "Der Anmeldename '" + data + "' wurde bereits an einen anderen Benutzer vergeben und ist daher nicht mehr verfügbar.");
			return false;
		}
		return true;
	}

}
