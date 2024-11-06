package de.svws_nrw.asd.validate;

import de.svws_nrw.transpiler.annotations.TsObject;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt Fehler, welche durch Validatoren auftreten.
 *
 * @param <T>   der Typ der zu validierenden Daten
 */
public class ValidatorFehler<@TsObject T> {

	/** Der Validator bei dem die Validierung fehlgeschlagen ist. */
	private final @NotNull Validator<T> _validator;

    /** Die Fehlermeldung, welche vom Validator gemeldet wurde */
    private final @NotNull String _fehlermeldung;


    /**
     * Erstellt einen neuen Validierungs-Fehler
     *
     * @param validator       der Validator bei dem die Validierung fehlgeschlagen ist
     * @param fehlermeldung   die Fehlermeldung, welche vom Validator gemeldet wurde
     */
    public ValidatorFehler(final @NotNull Validator<T> validator, final @NotNull String fehlermeldung) {
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
	public Validator<T> getValidator() {
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
	@SuppressWarnings("unchecked")
	public Class<? extends Validator<T>> getValidatorClass() {
		return (Class<? extends Validator<T>>) _validator.getClass();
	}


	/**
	 * Das DTO, bei dem die Validierung fehlgeschlagen ist
	 *
	 * @return das DTO
	 */
	public T getDTO() {
		return _validator.daten();
	}


	/**
	 * Der Name der DTO-Klasse, bei der die Validierung fehlgeschlagen ist
	 *
	 * @return der Name der DTO-Klasse
	 */
	public String getDTOClassname() {
		return _validator.getDTOClass().getCanonicalName();
	}


	/**
	 * Die DTO-Klasse, bei der die Validierung fehlgeschlagen ist
	 *
	 * @return die DTO-Klasse
	 */
	public Class<T> getDTOClass() {
		return _validator.getDTOClass();
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
		return _validator.getFehlerart();
	}

}
