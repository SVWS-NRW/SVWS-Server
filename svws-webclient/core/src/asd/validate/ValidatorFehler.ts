import { ValidatorFehlerart } from '../../asd/validate/ValidatorFehlerart';
import { Class, cast_java_lang_Class } from '../../java/lang/Class';
import { JavaObject } from '../../java/lang/JavaObject';
import { ValidatorKontext } from '../../asd/validate/ValidatorKontext';
import { Validator, cast_de_svws_nrw_asd_validate_Validator } from '../../asd/validate/Validator';

export class ValidatorFehler<T extends JavaObject> extends JavaObject {

	/**
	 * Der Validator bei dem die Validierung fehlgeschlagen ist.
	 */
	private readonly _validator : Validator<T>;

	/**
	 * Die Fehlermeldung, welche vom Validator gemeldet wurde
	 */
	private readonly _fehlermeldung : string;


	/**
	 * Erstellt einen neuen Validierungs-Fehler
	 *
	 * @param validator       der Validator bei dem die Validierung fehlgeschlagen ist
	 * @param fehlermeldung   die Fehlermeldung, welche vom Validator gemeldet wurde
	 */
	public constructor(validator : Validator<T>, fehlermeldung : string) {
		super();
		this._validator = validator;
		this._fehlermeldung = fehlermeldung;
	}

	/**
	 * Die Schulnummer der Schule, bei der die Validierung fehlgeschlagen ist
	 *
	 * @return die Schulnummer
	 */
	public getSchulnummer() : number {
		return this._validator.kontext().getSchulnummer();
	}

	/**
	 * Gibt den Validator-Kontext zurück, bei dem der Fehler aufgetreten ist.
	 *
	 * @return der Kontext
	 */
	public getKontext() : ValidatorKontext {
		return this._validator.kontext();
	}

	/**
	 * Der Validator, bei dem die Validierung fehlgeschlagen ist
	 *
	 * @return der Validator
	 */
	public getValidator() : Validator<T> | null {
		return this._validator;
	}

	/**
	 * Der Name der Validator-Klasse, bei der die Validierung fehlgeschlagen ist
	 *
	 * @return der Name der Validator-Klasse
	 */
	public getValidatorClassname() : string {
		return this._validator.getClass().getCanonicalName();
	}

	/**
	 * Die Validator-Klasse, bei der die Validierung fehlgeschlagen ist
	 *
	 * @return die Validator-Klasse
	 */
	public getValidatorClass() : Class<Validator<T>> | null {
		return cast_java_lang_Class(this._validator.getClass());
	}

	/**
	 * Das DTO, bei dem die Validierung fehlgeschlagen ist
	 *
	 * @return das DTO
	 */
	public getDTO() : T | null {
		return this._validator.daten();
	}

	/**
	 * Der Name der DTO-Klasse, bei der die Validierung fehlgeschlagen ist
	 *
	 * @return der Name der DTO-Klasse
	 */
	public getDTOClassname() : string | null {
		return this._validator.getDTOClass().getCanonicalName();
	}

	/**
	 * Die DTO-Klasse, bei der die Validierung fehlgeschlagen ist
	 *
	 * @return die DTO-Klasse
	 */
	public getDTOClass() : Class<T> | null {
		return this._validator.getDTOClass();
	}

	/**
	 * Die Fehlermeldung, welche vom Validator erzeugt wurde
	 *
	 * @return die Fehlermeldung
	 */
	public getFehlermeldung() : string | null {
		return this._fehlermeldung;
	}

	/**
	 * Die Fehlerart, welcher der Fehler zugeordnet ist.
	 *
	 * @return die Fehlerart
	 */
	public getFehlerart() : ValidatorFehlerart {
		return this._validator.getFehlerart();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.ValidatorFehler';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.ValidatorFehler'].includes(name);
	}

	public static class = new Class<ValidatorFehler<any>>('de.svws_nrw.asd.validate.ValidatorFehler');

}

export function cast_de_svws_nrw_asd_validate_ValidatorFehler<T extends JavaObject>(obj : unknown) : ValidatorFehler<T> {
	return obj as ValidatorFehler<T>;
}
