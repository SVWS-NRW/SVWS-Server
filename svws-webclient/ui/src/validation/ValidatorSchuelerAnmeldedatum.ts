import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { DateManager } from "../../../core/src/asd/validate/DateManager";
import { InvalidDateException } from "../../../core/src/asd/validate/InvalidDateException";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher das Anmeldedatum eines Schülers bei der Eingabe prüft.
 */
export class ValidatorSchuelerAnmeldedatum extends BasicValidator {

	/** Eine Funktion, um auf das zu zugehörige Anmeldedatum zugreifen zu können. */
	private readonly datumAnmeldung: () => string | null | undefined;


	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param datumAnmeldung   die Funktion zum Zugriff auf das Anmeldedatum
	 */
	constructor(datumAnmeldung: () => string | null | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.datumAnmeldung = datumAnmeldung;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor das Anmeldedatum
	 * ermittelt und dieses anschließend auf Gültigkeit prüft.
	 *
	 * @returns true, wenn das Anmeldedatum gültig ist.
	 */
	protected pruefe(): boolean {
		const strAnmeldedatum = this.datumAnmeldung();
		if ((strAnmeldedatum === undefined) || (strAnmeldedatum === null)) {
			return true;
		}
		let datumAnmeldung: DateManager | null = null;
		try {
			datumAnmeldung = DateManager.from(strAnmeldedatum);
		} catch (e) {
			if (e instanceof InvalidDateException) {
				this.addFehler(0, "Das Format des Anmeldedatums ist fehlerhaft: " + e.getMessage());
			}
			return false;
		}

		// Das Anmeldedatum darf nicht in der Zukunft liegen
		const now = new Date();
		const heute = DateManager.fromValues(now.getFullYear(), now.getMonth() + 1, now.getDate());
		if (datumAnmeldung.compareTo(heute) > 0) {
			this.addFehler(0, "Das Anmeldedatum darf nicht in der Zukunft liegen.");
			return false;
		}
		return true;
	}

}
