import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher darauf hinweist, dass für den Versand von E-Mails ein SMTP-Passwort
 * erforderlich ist, sobald ein SMTP-Username angegeben wurde, aber kein Passwort hinterlegt ist.
 */
export class ValidatorBenutzerEMailDatenPasswort extends BasicValidator {

	/** Eine Funktion, um auf das hinterlegte SMTP-Passwort zugreifen zu können */
	private readonly password: () => string;

	/** Eine Funktion, um auf den SMTP-Username zugreifen zu können */
	private readonly username: () => string;


	/**
	 * Erzeugt einen neuen Validator mit den Funktionen zum Zugriff auf das SMTP-Passwort und den SMTP-Username.
	 *
	 * @param password   die Funktion zum Zugriff auf das hinterlegte SMTP-Passwort, leer wenn keines hinterlegt ist
	 * @param username   die Funktion zum Zugriff auf den SMTP-Username
	 */
	constructor(password: () => string, username: () => string) {
		super(ValidatorFehlerart.MUSS);
		this.password = password;
		this.username = username;
	}

	/**
	 * Die Prüfroutine, welche prüft, ob bei angegebenem SMTP-Username auch ein Passwort hinterlegt ist.
	 *
	 * @returns true, wenn kein SMTP-Username angegeben oder ein Passwort hinterlegt ist
	 */
	protected pruefe(): boolean {
		if ((this.username().trim() === "") || (this.password() !== "")) {
			return true;
		}
		this.addFehler(0, "Für den Versand von E-Mails ist ein SMTP-Passwort erforderlich.");
		return false;
	}

}
