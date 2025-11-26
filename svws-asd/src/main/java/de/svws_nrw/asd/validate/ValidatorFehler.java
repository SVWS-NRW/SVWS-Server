package de.svws_nrw.asd.validate;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt Fehler, welche durch Validatoren auftreten.
 */
public class ValidatorFehler {

	/** Der Validator bei dem die Validierung fehlgeschlagen ist. */
	private final @NotNull BasicValidator _validator;

	/** Die Nummer des Prüfschrittes, bei welchem der Fehler aufgetreten ist */
	private final int _pruefschritt;

	/** Die Fehlermeldung, welche vom Validator gemeldet wurde */
	private final @NotNull String _fehlermeldung;


	/**
	 * Erstellt einen neuen Validierungs-Fehler
	 *
	 * @param validator       der Validator bei dem die Validierung fehlgeschlagen ist
	 * @param pruefschritt    die Nummer des Prüfschrittes, bei welchem der Fehler aufgetreten ist
	 * @param fehlermeldung   die Fehlermeldung, welche vom Validator gemeldet wurde
	 */
	public ValidatorFehler(final @NotNull BasicValidator validator, final int pruefschritt, final @NotNull String fehlermeldung) {
		this._validator = validator;
		this._fehlermeldung = fehlermeldung;
		this._pruefschritt = pruefschritt;
	}


	/**
	 * Gibt die Schulnummer der Schule zurück, bei der die Validierung fehlgeschlagen ist
	 *
	 * @return die Schulnummer
	 */
	public long getSchulnummer() {
		return getKontext().getSchulnummer();
	}


	/**
	 * Gibt den Validator-Kontext zurück, bei dem der Fehler aufgetreten ist.
	 *
	 * @return der Kontext
	 */
	public @NotNull ValidatorKontext getKontext() {
		if (_validator instanceof final Validator validator)
			return validator.kontext();
		throw new ValidatorException("Der Validator ist nur ein BasicValidator und kein Validator, weshalb er keinen ValidatorKontext hat.");
	}


	/**
	 * Gibt den Validator zurück, bei dem die Validierung fehlgeschlagen ist
	 *
	 * @return der Validator
	 */
	public BasicValidator getValidator() {
		return _validator;
	}


	/**
	 * Gibt den Namen der Validator-Klasse zurück, bei der die Validierung fehlgeschlagen ist
	 *
	 * @return der Name der Validator-Klasse
	 */
	public @NotNull String getValidatorClassname() {
		return _validator.getClass().getCanonicalName();
	}


	/**
	 * Gibt die Validator-Klasse zurück, bei der die Validierung fehlgeschlagen ist
	 *
	 * @return die Validator-Klasse
	 */
	public Class<? extends BasicValidator> getValidatorClass() {
		return _validator.getClass();
	}


	/**
	 * Gibt die Nummer des Prüfschrittes zurück, bei welchem der Fehler aufgetreten ist
	 *
	 * @return die Nummer des Prüfschrittes
	 */
	public int getPruefschritt() {
		return _pruefschritt;
	}


	/**
	 * Gibt den ASD-Fehlercode für diesen Validator-Fehler zurück.
	 *
	 * @return der ASD-Fehlercode
	 */
	public @NotNull String getFehlercode() {
		return _validator.getFehlercodePraefix() + _pruefschritt;
	}


	/**
	 * Gibt die Fehlermeldung zurück, welche vom Validator erzeugt wurde
	 *
	 * @return die Fehlermeldung
	 */
	public String getFehlermeldung() {
		return _fehlermeldung;
	}


	/**
	 * Gibt die Fehlerart zurück, welche dem Fehler zugeordnet ist.
	 *
	 * @return die Fehlerart
	 */
	public @NotNull ValidatorFehlerart getFehlerart() {
		return _validator.getValidatorFehlerart();
	}

}
