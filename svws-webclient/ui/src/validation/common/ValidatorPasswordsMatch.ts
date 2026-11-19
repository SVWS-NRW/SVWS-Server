import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob die Wiederholung eines Passworts mit dem ersten Passwort übereinstimmt.
 * Die Prüfung findet erst statt, sobald die Wiederholung nicht leer ist.
 */
export class ValidatorPasswordsMatch extends BasicValidator {

	/** Eine Funktion, um auf das erste Passwort zugreifen zu können */
	private readonly password: () => string;

	/** Eine Funktion, um auf die Wiederholung des Passworts zugreifen zu können */
	private readonly repeat: () => string;


	/**
	 * Erzeugt einen neuen Validator mit den Funktionen zum Zugriff auf das erste Passwort und dessen Wiederholung.
	 *
	 * @param password   die Funktion zum Zugriff auf das erste Passwort
	 * @param repeat     die Funktion zum Zugriff auf die Wiederholung des Passworts
	 */
	constructor(password: () => string, repeat: () => string) {
		super(ValidatorFehlerart.MUSS);
		this.password = password;
		this.repeat = repeat;
	}

	/**
	 * Die Prüfroutine, welche prüft, ob die Wiederholung mit dem ersten Passwort übereinstimmt.
	 *
	 * @returns true, wenn die Wiederholung leer ist oder mit dem ersten Passwort übereinstimmt
	 */
	protected pruefe(): boolean {
		const repeat = this.repeat();
		if ((repeat === "") || (repeat === this.password())) {
			return true;
		}
		this.addFehler(0, "Die Passwörter müssen übereinstimmen");
		return false;
	}

}
