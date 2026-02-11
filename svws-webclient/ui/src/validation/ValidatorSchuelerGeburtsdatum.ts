import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { DateManager } from "../../../core/src/asd/validate/DateManager";
import { InvalidDateException } from "../../../core/src/asd/validate/InvalidDateException";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welches das Alter eines Schülers anhand seines Geburtsdatum bei dem angegebenen Bezugsdatum prüft.
 * Wird kein Bezugsdatum angegeben, so wird das aktuelle Datum als Bezugsdatum gewählt.
 */
export class ValidatorSchuelerGeburtsdatum extends BasicValidator {

	/** Eine Funktion, um auf das Geburtsdatum eines Schülers zugreifen zu können. */
	private readonly geburtsdatum: () => string | null | undefined;

	/** Eine Funktion, um auf das Bezugsdatum zugreifen zu können. */
	private readonly datum: () => string | null | undefined;

	/** Das minimal erlaubte Alter für die Überprüfung */
	private readonly minAge;

	/** Das maximal erlaubte Alter für die Überprüfung */
	private readonly maxAge;


	/**
	 * Erzeugt einen neuen Validator
	 *
	 * @param geburtsdatum     die Funktion zum Zugriff auf das Bildungsgangbeginn
	 * @param datum            die Funktion zum Zugriff auf das Bezugsdatum
	 */
	constructor(geburtsdatum: () => string | null | undefined, datum: () => string | null | undefined, istWeiterbildungskolleg: boolean) {
		super(ValidatorFehlerart.MUSS);
		this.geburtsdatum = geburtsdatum;
		this.datum = datum;
		this.minAge = 3;
		this.maxAge = istWeiterbildungskolleg ? 100 : 50;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktionen das Alter eines Schülers bei dem angegeben Bezugsdatum oder
	 * dem aktuellen Datum validiert.
	 *
	 * @returns true, wenn das Alter in einem sinnvollen Bereich liegt oder das Geburtsdatum noch nicht gesetzt ist
	 */
	protected pruefe(): boolean {
		const strGeburtsdatum = this.geburtsdatum();
		if ((strGeburtsdatum === undefined) || (strGeburtsdatum === null)) {
			this.addFehler(0, "Ein Geburtsdatum muss eingegeben werden.");
			return false;
		}
		let geburtsdatum: DateManager | null = null;
		try {
			geburtsdatum = DateManager.from(strGeburtsdatum);
		} catch (e) {
			if (e instanceof InvalidDateException) {
				this.addFehler(0, "Das Format des Geburtsdatums ist fehlerhaft: " + e.getMessage());
			}
			return false;
		}

		const strDatum = this.datum();
		let datum: DateManager;
		if ((strDatum === undefined) || (strDatum === null)) {
			const now = new Date();
			datum = DateManager.fromValues(now.getFullYear(), now.getMonth() + 1, now.getDate());
		} else {
			try {
				datum = DateManager.from(strDatum);
			} catch (e) {
				if (e instanceof InvalidDateException) {
					// Das Bezugsdatum ist fehlerhaft, aber nicht in der Verantwortung dieses Validators, nimm das aktuelle Datum
				}
				const now = new Date();
				datum = DateManager.fromValues(now.getFullYear(), now.getMonth() + 1, now.getDate());
			}
		}

		try {
			const alter = geburtsdatum.getAlter(datum);
			if (alter < this.minAge) {
				this.addFehler(0, "Das Alter " + alter + " ist zu niedrig und nicht plausibel für den Besuch einer Schule.");
				return false;
			}
			if (alter > this.maxAge) {
				this.addFehler(0, "Das Alter " + alter + " ist zu hoch und nicht plausibel für den Besuch einer Schule.");
				return false;
			}
		} catch (e) {
			if (e instanceof InvalidDateException) {
				// Ein Fehler tritt nur auf, wenn das Geburtdatum nach dem Bezugsdatum liegt
			}
			this.addFehler(0, "Das Bezugsdatum liegt vor dem eingegebenen Geburtsdatum. Es liegt kein gültiges Alter vor.");
			return false;
		}
		return true;
	}

}
