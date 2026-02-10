import { JavaString } from "@core";

export function emailIsValid(value: string | null, maxLength: number) {
	if ((value === null) || JavaString.isBlank(value)) {
		return true;
	}

	if (value.length > maxLength) {
		return false;
	}

	return /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(value) ||
			/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value);
}

export function phoneNumberIsValid(input: string | null, maxLength: number) {
	if ((input === null) || (JavaString.isBlank(input))) {
		return true;
	}
	// folgende Formate sind erlaubt: 0151123456, 0151/123456, 0151-123456, +49/176-456456 -> Buchstaben sind nicht erlaubt
	return /^\+?\d+([-/]?\d+)*$/.test(input) && input.length <= maxLength;
}

export function mandatoryInputIsValid(value: string | null, maxLength: number): value is string {
	return isNotNull(value) && (!JavaString.isBlank(value)) && (value.length <= maxLength);
}

export function optionalInputIsValid(input: string | null, maxLength: number): boolean {
	if ((input === null) || (JavaString.isBlank(input))) {
		return true;
	}

	return input.length <= maxLength;
}

/**
 * Prüft, ob ein gegebener String-Wert innerhalb einer Liste eindeutig ist.
 *
 * Diese Funktion wird typischerweise zur Validierung von Texteingaben verwendet,
 * um doppelte Werte (z. B. Namen oder Bezeichnungen) zu verhindern. Der Vergleich
 * erfolgt **case-insensitive** (Groß-/Kleinschreibung wird ignoriert) und
 * **whitespace-bereinigt**.
 *
 * Optional kann ein Eintrag über `idKey` und `currentId` von der Prüfung
 * ausgeschlossen werden, z. B. wenn ein bestehender Datensatz beim Bearbeiten
 * seinen eigenen Wert behalten darf.
 *
 * @template T            Typ der Listenelemente
 *
 * @param value           Zu prüfender Wert (z. B. Eingabe des Benutzers)
 * @param list            Sammlung von Objekten, in denen der Wert eindeutig sein soll
 * @param key             Schlüsselfeld, dessen Werte in der Liste geprüft werden
 * @param idKey           Optionales Feld, das den eindeutigen Identifikator beschreibt (z. B. "id")
 * @param currentId       Optionaler Identifikator des aktuellen Elements, das von der Prüfung ausgenommen wird
 *
 * @returns               `false`, falls der Wert bereits existiert, sonst `true`
 */
export function isUniqueInList<T>(value: string | null, list: Iterable<T>, key: keyof T, idKey?: keyof T, currentId?: T[keyof T]): boolean {
	if (value === null) {
		return true;
	}

	for (const entry of list) {
		const fieldValue = entry[key];
		if ((idKey !== undefined) && (entry[idKey] === currentId)) {
			continue;
		}

		if ((typeof fieldValue === "string") && (fieldValue.trim().toLowerCase() === value.trim().toLowerCase())) {
			return false;
		}
	}
	return true;
}

/**
 * Validiert eine numerische Eingabe hinsichtlich Pflichtfeld-, Minimal- und Maximalwert.
 *
 * Die Funktion prüft:
 * 1. Ob ein Wert vorhanden ist, falls `required = true`.
 * 2. Ob der Wert eine gültige Zahl ist.
 * 3. Ob der Wert größer oder gleich `min` ist (falls angegeben).
 * 4. Ob der Wert kleiner oder gleich `max` ist (falls angegeben).
 *
 * Typischer Anwendungsfall: Validierung von numerischen Formularfeldern wie
 * Alter, Menge, Preis oder Punktzahlen.
 *
 * @param value     Der zu prüfende Zahlenwert
 * @param required  Gibt an, ob das Feld einen Wert enthalten muss
 * @param min       Optional: Minimal erlaubter Wert
 * @param max       Optional: Maximal erlaubter Wert
 *
 * @returns         `false` falls ungültig; sonst `true`
 */
export function numberIsValid(value: number | null, required: boolean, min?: number, max?: number): boolean {
	if (value === null) {
		return !required;
	}

	if (Number.isNaN(value)) {
		return false;
	}

	if ((min !== undefined) && (value < min)) {
		return false;
	}

	if ((max !== undefined) && (value > max)) {
		return false;
	}

	return true;
}

/**
 * Prüft, ob die angegebene Zahl Dezimalstellen besitzt.
 *
 * Ist der Wert `null`, so gibt die Funktion `false` zurück.
 * Andernfalls wird die Zahl in einen String umgewandelt und überprüft,
 * ob sie einen Dezimaltrennpunkt (".") enthält.
 *
 * @param value die zu prüfende Zahl oder `null`
 * @return `true`, wenn die Zahl Dezimalstellen hat; sonst `false`
 */
export function numberHasDecimals(value: number | null): value is number {
	return isNotNull(value) && value.toString().includes(".");
}

/**
 * Prüft, ob der gegebene String ausschließlich Zahlen besiztz.
 *
 * @param value     Der zu prüfende Zahlenwert
 */
export function stringIsNumeric(value: string | null): value is string {
	return isNotNull(value) && /^\d+$/.test(value);
}

function isNotNull(value: string | number | null): value is string | number {
	return value !== null;
}

