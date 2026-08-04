import { BasicValidator } from "../../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Definition eines String-Musters mit Regex und Fehlermeldung.
 */
type StringPatternDefinition = {
	regex: RegExp;
	errorMessage: string;
};

/**
 * Aufzählung vordefinierter String-Muster.
 */
export enum StringPattern {
	NO_WHITESPACES,
	NO_LEADING_OR_TRAILING_WHITESPACES,
	IS_PHONE_NUMBER,
	IS_PHONE_NUMBER_OR_EXTENSION,
	IS_EMAIL
}

/**
 * Map der StringPattern-Werte auf ihre Regex-Definitionen und Fehlermeldungen.
 */
const STRING_PATTERN_MAP: Record<StringPattern, StringPatternDefinition> = {
	[StringPattern.NO_WHITESPACES]: {
		regex: /^\S*$/,
		errorMessage: "Der Wert darf keine Leerzeichen enthalten.",
	},
	[StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES]: {
		regex: /^(?:$|(?=.*\S)(?!\s|.*\s$).+)$/,
		errorMessage: "Der Wert darf keine führenden oder nachgestellten Leerzeichen enthalten.",
	},
	[StringPattern.IS_PHONE_NUMBER]: {
		regex: /^[+]?\d[-\d/() ]*\d$/,
		errorMessage: "Die angegebene Telefonnummer hat ein ungültiges Format. Zulässige Zeichen: Ziffern von 0-9, Plus(+) bei der Vorwahl, Minus(-), Schrägstrich(/), Leerzeichen und Klammern als Trennzeichen.",
	},
	[StringPattern.IS_PHONE_NUMBER_OR_EXTENSION]: {
		regex: /^(-?\d+|[+]?\d[-\d/() ]*\d)$/,
		errorMessage: "Die angegebene Durchwahl/Telefonnummer hat ein ungültiges Format. Durchwahl: Ziffern von 0-9, Minus(-) als vorangestelltes Trennzeichen. Telefonnummer: Ziffern von 0-9, Plus(+) bei der Vorwahl, Minus(-), Schrägstrich(/), Leerzeichen und Klammern als Trennzeichen.",
	},
	[StringPattern.IS_EMAIL]: {
		regex: /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))([^@]?|@((\[\d{1,3}(\.\d{1,3}){3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,})))$/,
		errorMessage: "Die angegebene E-Mail-Adresse hat ein ungültiges Format.",
	},
};

/**
 * Prüft, ob ein String einem definierten Muster entspricht.
 */
export class ValidatorStringMatchesPattern extends BasicValidator {

	private readonly pattern: StringPatternDefinition;
	private readonly data: () => string | null;

	/**
	 * Prüft, ob ein String einem definierten Muster entspricht.
	 *
	 * @param data Funktion zum Zugriff auf den String
	 * @param pattern Zu verwendendes Muster
	 */
	constructor(data: () => string | null, pattern: StringPattern) {
		super(ValidatorFehlerart.MUSS);
		this.pattern = STRING_PATTERN_MAP[pattern];
		this.data = data;
	}

	/**
	 * Prüft, ob der String dem Muster entspricht.
	 * @returns true, wenn gültig
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if ((data === null) || (data === '')) {
			return true;
		}
		if (!this.pattern.regex.test(data)) {
			this.addFehler(0, this.pattern.errorMessage);
			return false;
		}
		return true;
	};
}

