import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import { JavaString } from "@core/java/lang/JavaString";

/**
 * Prüft, ob ein String-Feld in einer Liste eindeutig ist.
 */
export class ValidatorStringIsUniqueInList<T> extends BasicValidator {

	private readonly value: () => T;
	private readonly getId: (data: T) => any;
	private readonly getField: (data: T) => string | null;
	private readonly list: () => Iterable<T>;
	private readonly caseSensitive: boolean;

	/**
	 * Prüft, ob ein String-Feld in einer Liste eindeutig ist.
	 *
	 * @param value Funktion zum aktuellen Eintrag
	 * @param getId Feldname der ID
	 * @param getField Feldname des zu prüfenden Strings
	 * @param list Funktion zur Liste der Einträge
	 * @param caseSensitive Ob Groß-/Kleinschreibung beachtet wird
	 */
	constructor(value: () => T, getId: (data: T) => any, getField: (data: T) => string | null, list: () => Iterable<T>, caseSensitive: boolean) {
		super(ValidatorFehlerart.MUSS);
		this.value = value;
		this.getId = getId;
		this.getField = getField;
		this.list = list;
		this.caseSensitive = caseSensitive;
	}

	/**
	 * Prüft die Eindeutigkeit des Strings.
	 *
	 * @returns true, wenn der Wert eindeutig ist
	 */
	protected pruefe(): boolean {
		const value = this.value();

		const currentId = this.getId(value);
		const currentValue = this.getField(value)?.trim();
		if (currentValue === undefined) {
			return true;
		}

		for (const entry of this.list()) {
			// Eigene ID ignorieren
			if ((currentId !== null) && (currentId !== undefined) && (this.getId(entry) === currentId)) {
				continue;
			}

			const entryValue = this.getField(entry)?.trim();
			if (entryValue === undefined) {
				continue;
			}

			const isEqual = this.caseSensitive ? JavaString.compareTo(currentValue, entryValue) : JavaString.compareToIgnoreCase(currentValue, entryValue);
			if (isEqual === 0) {
				this.addFehler(0, "Der Wert ist bereits vergeben.");
				return false;
			}
		}

		return true;
	}
}
