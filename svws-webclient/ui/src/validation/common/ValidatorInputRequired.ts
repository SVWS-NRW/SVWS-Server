import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import { JavaString } from "@core/java/lang/JavaString";


/**
 * Ein universeller Validator, der prüft, ob ein Wert vorhanden ist.
 *
 * Funktioniert für unterschiedliche Datentypen:
 * - **String**: darf nicht leer oder nur aus Leerzeichen bestehen
 * - **Array**: darf nicht leer sein
 * - **null / undefined**: nicht erlaubt
 * - Andere Typen: Wert muss definiert sein
 */
export class ValidatorInputRequired<T> extends BasicValidator {

	private readonly data: () => T | null;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf die aktuellen Daten.
	 *
	 * @param data   die Funktion zum Zugriff auf die zu prüfenden Daten
	 */
	constructor(data: () => T | null | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.data = () => data() ?? null;
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor die aktuellen Daten ermittelt
	 * und überprüft, ob ein gültiger Wert vorliegt.
	 *
	 * Regeln:
	 * - Ist der Wert `null` oder `undefined`, wird ein Fehler ausgelöst.
	 * - Ist der Wert ein String, darf er nicht leer oder nur aus Leerzeichen bestehen.
	 * - Ist der Wert ein Array, darf es nicht leer sein.
	 *
	 * @returns true, wenn ein gültiger Wert vorhanden ist
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if (data === null) {
			this.addFehler(0, "Bitte geben Sie einen Wert an.");
			return false;
		}
		if ((typeof data === "string") && JavaString.isBlank(data)) {
			this.addFehler(0, "Bitte geben Sie einen Wert an.");
			return false;
		}
		if (Array.isArray(data) && (data.length === 0)) {
			this.addFehler(0, "Eine Option muss ausgewählt werden.");
			return false;
		}
		return true;
	}
}
