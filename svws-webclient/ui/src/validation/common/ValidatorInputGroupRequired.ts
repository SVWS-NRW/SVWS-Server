import { BasicValidator } from "../../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";
import { JavaString } from "../../../../core/src/java/lang/JavaString";

/**
 * Modus zur Steuerung der Gruppenvalidierung.
 *
 * - EXACTLY_ONE: Genau ein Wert muss befüllt sein (nicht keiner, nicht mehrere)
 * - AT_LEAST_ONE: Mindestens ein Wert muss befüllt sein (mehrere erlaubt)
 * - AT_MOST_ONE: Höchstens ein Wert darf befüllt sein (keiner oder einer erlaubt)
 */
export enum ValidatorInputGroupRequiredModus {
	EXACTLY_ONE = "EXACTLY_ONE",
	AT_LEAST_ONE = "AT_LEAST_ONE",
	AT_MOST_ONE = "AT_MOST_ONE"
}

export type GroupField = { fieldName: string, fieldData: () => any };

/**
 * Ein Validator, der eine Gruppe von Inputs abhängig vom gewählten Modus auf Befüllung prüft.
 *
 */
export class ValidatorInputGroupRequired extends BasicValidator {

	private readonly groupFields: GroupField[];
	private readonly modus: ValidatorInputGroupRequiredModus;

	/**
	 * Erzeugt einen neuen Gruppenvalidator.
	 *
	 * @param groupFields   die Felder der Gruppe, die gemeinsam validiert werden sollen.
	 * @param modus         der Prüfmodus (EXACTLY_ONE, AT_LEAST_ONE oder AT_MOST_ONE)
	 */
	constructor(groupFields: GroupField[], modus: ValidatorInputGroupRequiredModus) {
		super(ValidatorFehlerart.MUSS);
		this.groupFields = groupFields;
		this.modus = modus;
	}

	/**
	 * Prüft, ob ein einzelner Wert als "befüllt" gilt.
	 *
	 * @param value   der zu prüfende Wert
	 *
	 * @returns true, wenn der Wert als befüllt gilt
	 */
	private isBefuellt(value: any): boolean {
		if ((value === null) || (value === undefined)) {
			return false;
		}
		if ((typeof value === "string") && JavaString.isBlank(value)) {
			return false;
		}
		return !(Array.isArray(value) && (value.length === 0));
	}

	/**
	 * Die Prüfroutine, welche die Gruppe von Werten gemäß dem konfigurierten Modus prüft.
	 *
	 * @returns true, wenn die Gruppe die Bedingung des Modus erfüllt
	 */
	protected pruefe(): boolean {
		const anzahlBefuellt = this.groupFields
			.filter(v => this.isBefuellt(v.fieldData()))
			.length;

		switch (this.modus) {
			case ValidatorInputGroupRequiredModus.AT_LEAST_ONE:
				return this.pruefeMindestensEins(anzahlBefuellt);

			case ValidatorInputGroupRequiredModus.AT_MOST_ONE:
				return this.pruefeHoechstensEins(anzahlBefuellt);

			case ValidatorInputGroupRequiredModus.EXACTLY_ONE:
				return this.pruefeGenauEins(anzahlBefuellt);
		}
	}

	private pruefeMindestensEins(anzahlBefuellt: number): boolean {
		return this.pruefeAnzahl(
			anzahlBefuellt > 0,
			`Es muss mindestens ein Wert für eines der folgenden Felder gesetzt werden:`
		);
	}

	private pruefeHoechstensEins(anzahlBefuellt: number): boolean {
		return this.pruefeAnzahl(
			anzahlBefuellt <= 1,
			`Es darf höchstens ein Wert für eines der folgenden Felder gesetzt werden:`
		);
	}

	private pruefeGenauEins(anzahlBefuellt: number): boolean {
		return this.pruefeAnzahl(
			anzahlBefuellt === 1,
			`Es muss genau ein Wert für eines der folgenden Felder gesetzt werden:`
		);
	}

	private pruefeAnzahl(
		istGueltig: boolean,
		fehlermeldungsAnfang: string
	): boolean {
		if (istGueltig) {
			return true;
		}

		this.addFehler(
			0,
			`${fehlermeldungsAnfang}
         ${this.groupFields.map(f => f.fieldName).join(', ')}`
		);

		return false;
	}

}
