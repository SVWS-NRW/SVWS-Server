import { JavaObject } from '../../java/lang/JavaObject';
import { ValidatorFehlerart } from '../../asd/validate/ValidatorFehlerart';
import { Class } from '../../java/lang/Class';
import { ValidatorKontext } from '../../asd/validate/ValidatorKontext';
import { Validator, cast_de_svws_nrw_asd_validate_Validator } from '../../asd/validate/Validator';

export class ValidatorFehler extends JavaObject {

	/**
	 * Der Validator bei dem die Validierung fehlgeschlagen ist.
	 */
	private readonly _validator : Validator;

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
	public constructor(validator : Validator, fehlermeldung : string) {
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
	public getValidator() : Validator | null {
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
	public getValidatorClass() : Class<Validator> | null {
		return this._validator.getClass();
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
		return this._validator.getValidatorFehlerart();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.ValidatorFehler';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.ValidatorFehler'].includes(name);
	}

	public static class = new Class<ValidatorFehler>('de.svws_nrw.asd.validate.ValidatorFehler');

}

export function cast_de_svws_nrw_asd_validate_ValidatorFehler(obj : unknown) : ValidatorFehler {
	return obj as ValidatorFehler;
}
