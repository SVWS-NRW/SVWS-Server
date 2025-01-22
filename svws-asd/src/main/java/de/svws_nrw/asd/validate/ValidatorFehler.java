package de.svws_nrw.asd.validate;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt Fehler, welche durch Validatoren auftreten.
 */
public class ValidatorFehler {

	/** Der Validator bei dem die Validierung fehlgeschlagen ist. */
	private final @NotNull Validator _validator;

    /** Die Fehlermeldung, welche vom Validator gemeldet wurde */
    private final @NotNull String _fehlermeldung;


    /**
     * Erstellt einen neuen Validierungs-Fehler
     *
     * @param validator       der Validator bei dem die Validierung fehlgeschlagen ist
     * @param fehlermeldung   die Fehlermeldung, welche vom Validator gemeldet wurde
     */
    public ValidatorFehler(final @NotNull Validator validator, final @NotNull String fehlermeldung) {
    	this._validator = validator;
    	this._fehlermeldung = fehlermeldung;
    }


    /**
     * Die Schulnummer der Schule, bei der die Validierung fehlgeschlagen ist
     *
     * @return die Schulnummer
     */
	public long getSchulnummer() {
		return _validator.kontext().getSchulnummer();
	}


	/**
	 * Gibt den Validator-Kontext zurück, bei dem der Fehler aufgetreten ist.
	 *
	 * @return der Kontext
	 */
	public @NotNull ValidatorKontext getKontext() {
		return _validator.kontext();
	}


	/**
	 * Der Validator, bei dem die Validierung fehlgeschlagen ist
	 *
	 * @return der Validator
	 */
	public Validator getValidator() {
		return _validator;
	}


	/**
	 * Der Name der Validator-Klasse, bei der die Validierung fehlgeschlagen ist
	 *
	 * @return der Name der Validator-Klasse
	 */
	public @NotNull String getValidatorClassname() {
		return _validator.getClass().getCanonicalName();
	}


	/**
	 * Die Validator-Klasse, bei der die Validierung fehlgeschlagen ist
	 *
	 * @return die Validator-Klasse
	 */
	public Class<? extends Validator> getValidatorClass() {
		return _validator.getClass();
	}


	/**
	 * Die Fehlermeldung, welche vom Validator erzeugt wurde
	 *
	 * @return die Fehlermeldung
	 */
	public String getFehlermeldung() {
		return _fehlermeldung;
	}


	/**
	 * Die Fehlerart, welcher der Fehler zugeordnet ist.
	 *
	 * @return die Fehlerart
	 */
	public @NotNull ValidatorFehlerart getFehlerart() {
		return _validator.getValidatorFehlerart();
	}

}
