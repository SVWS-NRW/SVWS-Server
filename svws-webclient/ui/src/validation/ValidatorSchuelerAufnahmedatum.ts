import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { DateManager } from "../../../core/src/asd/validate/DateManager";
import { InvalidDateException } from "../../../core/src/asd/validate/InvalidDateException";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher das Aufnahmedatum eines Schülers bei der Eingabe prüft.
 */
export class ValidatorSchuelerAufnahmedatum extends BasicValidator {

	/** Eine Funktion, um auf das zu validierenden Aufnahmedatum zugreifen zu können. */
	private readonly datumAufnahme: () => string | null | undefined;

	/** Eine Funktion, um auf das zu zugehörige Anmeldedatum zugreifen zu können. */
	private readonly datumAnmeldung: () => string | null | undefined;


	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param datumAufnahme    die Funktion zum Zugriff auf das Aufnahmendatum
	 * @param datumAnmeldung   die Funktion zum Zugriff auf das Anmeldedatum
	 */
	constructor(datumAufnahme: () => string | null | undefined, datumAnmeldung: () => string | null | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.datumAufnahme = datumAufnahme;
		this.datumAnmeldung = datumAnmeldung;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktionen das Aufnahmedatum und ggf. das Anmeldedatum
	 * ermittelt und das Aufnahmedatum anschließend auf Gültigkeit prüft.
	 *
	 * @returns true, wenn das Aufnahmedatum gültig ist
	 */
	protected pruefe(): boolean {
		const strAufnahmedatum = this.datumAufnahme();
		if ((strAufnahmedatum === undefined) || (strAufnahmedatum === null)) {
			return true;
		}
		let datumAufnahme: DateManager | null = null;
		try {
			datumAufnahme = DateManager.from(strAufnahmedatum);
		} catch (e) {
			if (e instanceof InvalidDateException) {
				this.addFehler(0, "Das Format des Aufnahmedatums ist fehlerhaft: " + e.getMessage());
			}
			return false;
		}

		const strAnmeldedatum = this.datumAnmeldung();
		if ((strAnmeldedatum === undefined) || (strAnmeldedatum === null)) {
			return true;
		}

		let datumAnmeldung: DateManager | null = null;
		try {
			datumAnmeldung = DateManager.from(strAnmeldedatum);
		} catch (e) {
			if (e instanceof InvalidDateException) {
				// Das Anmeldedatum ist zwar fehlerhaft, aber nicht in der Verantwortung dieses Validators
			}
			return true;
		}

		// Das Aufnahmedatum darf nicht vor dem Anmeldedatum liegen
		if (datumAufnahme.compareTo(datumAnmeldung) < 0) {
			this.addFehler(0, "Das Aufnahmedatum darf nicht vor dem Anmeldedatum liegen.");
			return false;
		}
		return true;
	}

}
