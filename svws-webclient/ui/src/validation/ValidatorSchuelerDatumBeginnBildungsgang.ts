import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { DateManager } from "../../../core/src/asd/validate/DateManager";
import { InvalidDateException } from "../../../core/src/asd/validate/InvalidDateException";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher den Beginn eines Bildungsganges am Berufskolleg bei einem Schülers bei der Eingabe prüft.
 */
export class ValidatorSchuelerDatumBeginnBildungsgang extends BasicValidator {

	/** Eine Funktion, um auf das Datum des Beginns eines Bildungsganges eines Schülers zugreifen zu können. */
	private readonly datumBeginnBildungsgang: () => string | null | undefined;

	/** Eine Funktion, um auf das zu validierenden Aufnahmedatum zugreifen zu können. */
	private readonly datumAufnahme: () => string | null | undefined;


	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param datumBeginn     die Funktion zum Zugriff auf das Bildungsgangbeginn
	 * @param datumAufnahme   die Funktion zum Zugriff auf das Aufnahmedatum
	 */
	constructor(datumBeginn: () => string | null | undefined, datumAufnahme: () => string | null | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.datumBeginnBildungsgang = datumBeginn;
		this.datumAufnahme = datumAufnahme;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktionen das Datum des Bildungsgangbeginns und ggf. das Aufnahmedatum
	 * ermittelt und den Bildungsgangbeginn anschließend auf Gültigkeit prüft.
	 *
	 * @returns true, wenn der Bildungsgangbeginn gültig ist
	 */
	protected pruefe(): boolean {
		const strBeginnBildungsgang = this.datumBeginnBildungsgang();
		if ((strBeginnBildungsgang === undefined) || (strBeginnBildungsgang === null)) {
			return true;
		}
		let datumBeginnBildungsgang: DateManager | null = null;
		try {
			datumBeginnBildungsgang = DateManager.from(strBeginnBildungsgang);
		} catch (e) {
			if (e instanceof InvalidDateException) {
				this.addFehler(0, "Das Format des Datums für den Beginn des Bildungsganges bei dem Schüler ist fehlerhaft: " + e.getMessage());
			}
			return false;
		}

		const strAufnahmedatum = this.datumAufnahme();
		if ((strAufnahmedatum === undefined) || (strAufnahmedatum === null)) {
			return true;
		}
		let datumAufnahme: DateManager | null = null;
		try {
			datumAufnahme = DateManager.from(strAufnahmedatum);
		} catch (e) {
			if (e instanceof InvalidDateException) {
				// Das Aufnahmedatum ist zwar fehlerhaft, aber nicht in der Verantwortung dieses Validators
			}
			return true;
		}

		if (datumBeginnBildungsgang.compareTo(datumAufnahme) < 0) {
			this.addFehler(0, "Der Beginn des Bildungangs darf nicht vor dem Aufnahmedatum liegen.");
			return false;
		}
		return true;
	}

}
