import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob ein String eine Name für ein MariaDB-Schema ist.
 *
 * Er testet nach den offizellen Vorgaben zu gültigen Identifiern innerhalb von MariaDB:
 * https://mariadb.com/docs/server/reference/sql-structure/sql-language-structure/identifier-names
 * ASCII: [0-9,a-z,A-Z$_] (numerals 0-9, basic Latin letters, both lowercase and uppercase, dollar sign, underscore)
 */
export class ValidatorSchemaNameMariaDB extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => string | null | undefined;

	/** Gibt an, ob die erweiterete Benennung erlaubt wird, welche auch weitere Unicode-Zeichen erlaubt */
	private readonly allowExtended: boolean;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data            die Funktion zum Zugriff auf die Daten
	 * @param allowExtended   gibt an, ob die erweiterete Benennung erlaubt wird, welche auch weitere Unicode-Zeichen erlaubt
	 */
	constructor(data: () => string | null | undefined, allowExtended: boolean = false) {
		super(ValidatorFehlerart.MUSS);
		this.data = data;
		this.allowExtended = allowExtended;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor die Daten ermittelt und auf einen
	 * gültigen Schemanamen prüft.
	 *
	 * @returns true, wenn der String einen gültigen Schemanamen beinhaltet.
	 */
	protected pruefe(): boolean {
		const data = this.data();

		// Prüfe, ob überhaupt ein Schemaname angegeben wurde
		if ((data === undefined) || (data === null) || (data === '')) {
			this.addFehler(0, "Eine leerer Schema-Name ist nicht zulässig.");
			return false;
		}

		// Prüfe die Zeichen, welche im Schema-Namen verwendet wurden.
		if (/[^0-9,a-z,A-Z$_]/.test(data)) {
			return true;
		}
		this.addFehler(0, "Der Schema-Name enthält unerlaubte Zeichen.");
		return false;
	};

}
