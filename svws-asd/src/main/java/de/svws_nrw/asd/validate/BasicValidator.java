package de.svws_nrw.asd.validate;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ist die Grundlage für die Basisklasse für Validatoren
 * und kann im SVWS-Client für Validatoren genutzt werden, welche Kontext-unabhängig
 * sind und auch nicht für die amtliche Schulstatistik relevant sind.
 */
public abstract class BasicValidator {

	/** Eine Liste mit Fehlern bei der Validierung */
	protected final @NotNull List<ValidatorFehler> _fehler;

	/** Die stärkste Fehlerart die bei einem Lauf des Validators vorgekommen ist. */
	protected @NotNull ValidatorFehlerart _fehlerart = ValidatorFehlerart.UNGENUTZT;

	/** Die Default-Fehlerart für diesen Validator */
	protected @NotNull ValidatorFehlerart _defaultValidatorFehlerart;


	/**
	 * Erstellt einen einfach Validator mit einer leeren Fehlerliste
	 *
	 * @param defaultValidatorFehlerart   die Default-Fehlerart für diesen Validator
	 */
	protected BasicValidator(final @NotNull ValidatorFehlerart defaultValidatorFehlerart) {
		this._fehler = new ArrayList<>();
		this._defaultValidatorFehlerart = defaultValidatorFehlerart;
	}


	/**
	 * Führt die Prüfungen des Validators aus. Dabei wird zunächst die Fehlerliste
	 * geleert und durch die ausführenden Prüfroutinen befüllt.
	 *
	 * @return true, falls alle Prüfroutinen erfolgreich waren, und ansonsten false
	 */
	public boolean run() {
		boolean success = true;
		_fehler.clear();
		_fehlerart = ValidatorFehlerart.UNGENUTZT;
		// Berücksichtige auch Exceptions bei der Prüfung dieses Validators
		try {
			if (!this.pruefe())
				success = false;
		} catch (final Exception e) {
			addFehler(-1, "Unerwarteter Fehler bei der Validierung: " + e.getMessage());
			success = false;
		}
		return success;
	}


	/**
	 * Erstellt einen neuen Fehler mit der übergebenen Fehlermeldung
	 *
	 * @param pruefschritt    die Nummer des Prüfschrittes, bei welchem der Fehler aufgetreten ist
	 * @param fehlermeldung   die Fehlermeldung
	 */
	protected void addFehler(final int pruefschritt, final @NotNull String fehlermeldung) {
		_fehler.add(new ValidatorFehler(this, pruefschritt, fehlermeldung));
		updateFehlerart(this.getValidatorFehlerart());
	}


	/**
	 * Aktualisiert die Fehlerart, die durch den Lauf dieses Validators erzeugt wurde
	 * anhand der übergebenen Fehlerart. Wird null übergeben, so wird die Fehlerart genutzt, die
	 * diesem Validator zugeordnet ist.
	 *
	 * @param art   die Fehlerart, die für die Überprüfung genutzt wird, oder null
	 */
	protected void updateFehlerart(final @NotNull ValidatorFehlerart art) {
		if (this._fehlerart.ordinal() > art.ordinal())
			this._fehlerart = art;
	}


	/**
	 * Gibt die Fehler des Validators als unmodifiable List zurück.
	 *
	 * @return die Liste der Fehler als unmodifiable List
	 */
	public @NotNull List<ValidatorFehler> getFehler() {
		return new ArrayList<>(_fehler);
	}


	/**
	 * Die Fehlerart, welche diesem speziellen Validator zugeordnet ist.
	 *
	 * @return die Fehlerart
	 */
	public @NotNull ValidatorFehlerart getValidatorFehlerart() {
		return this._defaultValidatorFehlerart;
	}


	/**
	 * Gibt das Fehlercode-Präfix zurück, welcher diesem speziellen Validator zugeordnet ist.
	 *
	 * @return das Fehlercode-Präfix
	 */
	public @NotNull String getFehlercodePraefix() {
		return "";
	}


	/**
	 * Die Fehlerart, welche dem Validator nach dem Lauf der Validierung zugeordnet ist.
	 * Dabei sind die Ergebnisse von ggf. vorhandene Sub-Validatoren mit einbezogen.
	 * Es wird also die schwerwiegendste Fehlerart zurückgegeben.
	 *
	 * @return die Fehlerart
	 */
	public @NotNull ValidatorFehlerart getFehlerart() {
		return this._fehlerart;
	}


	/**
	 * Führt die Prüfung der Daten aus. Befüllt ggf. die Fehlerliste, falls
	 * es zu Fehlern kommt.
	 *
	 * @return true, falls die Prüfung erfolgreich war, und ansonsten false
	 */
	protected abstract boolean pruefe();

}
