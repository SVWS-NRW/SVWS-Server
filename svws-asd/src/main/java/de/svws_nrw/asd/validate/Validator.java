package de.svws_nrw.asd.validate;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ist die Basisklasse für Validatoren.
 */
public abstract class Validator {

	/** Der vom Validator genutzte Kontext */
	private final @NotNull ValidatorKontext _kontext;

	/** Eine Liste von Validatoren, die bei diesem Validator mitgeprüft werden. */
	protected final @NotNull List<Validator> _validatoren = new ArrayList<>();

	/** Eine Liste mit Fehlern bei der Validierung */
	private final @NotNull List<ValidatorFehler> _fehler = new ArrayList<>();

	/** Die stärkste Fehlerart die bei einem Lauf des Validators vorgekommen ist. */
	private @NotNull ValidatorFehlerart _fehlerart = ValidatorFehlerart.UNGENUTZT;


	/**
	 * Erstellt einen neuen Validator in dem übegebenen Kontext
	 *
	 * @param kontext   der Kontext, in dem der Validator ausgeführt wird
	 */
	protected Validator(final @NotNull ValidatorKontext kontext) {
		this._kontext = kontext;
	}


	/**
	 * Gibt den Kontext des Validators zurück.
	 *
	 * @return der Kontext des Validators
	 */
	public @NotNull ValidatorKontext kontext() {
		return _kontext;
	}

	/**
	 * Gibt den zugehörigen ValidatorManager zurück.
	 *
	 * @return der ValidatorManager
	 */
	public @NotNull ValidatorManager getValidatorManager() {
		return _kontext.getValidatorManager();
	}


	/**
	 * Führt die Prüfungen des Validators aus. Dabei wird zunächst die Fehlerliste
	 * geleert und durch die ausführenden Prüfroutinen befüllt.
	 *
	 * @return true, falls alle Prüfroutinen erfolgreich waren, und ansonsten false
	 */
	public final boolean run() {
		boolean success = true;
		_fehler.clear();
		if (_kontext.getValidatorManager().isValidatorActiveInSchuljahr(_kontext.getSchuljahr(), this.getClass().getCanonicalName())) {
			for (final @NotNull Validator validator : _validatoren) {
				if (!validator.run())
					success = false;
				_fehler.addAll(validator._fehler);
				updateFehlerart(validator.getFehlerart());
			}
			// Berücksichtige auch Exceptions bei der Prüfung dieses Validators
			try {
				if (!this.pruefe())
					success = false;
			} catch (final Exception e) {
				addFehler("Unerwarteter Fehler bei der Validierung: " + e.getMessage());
			}
		}
		return success;
	}

	/**
	 * Aktualisiert die Fehlerart, die durch den Lauf dieses Validators erzeugt wurde
	 * anhand der übergebenen Fehlerart. Wird null übergeben, so wird die Fehlerart genutzt, die
	 * diesem Validator zugeordnet ist.
	 *
	 * @param art   die Fehlerart, die für die Überprüfung genutzt wird, oder null
	 */
	private void updateFehlerart(final ValidatorFehlerart art) {
		final @NotNull ValidatorFehlerart tmp = (art != null) ? art : this.getValidatorFehlerart();
		if (this._fehlerart.ordinal() > tmp.ordinal())
			this._fehlerart = tmp;
	}

	/**
	 * Erstellt einen neuen Fehler mit der übergebenen Fehlermeldung
	 *
	 * @param fehlermeldung   die Fehlermeldung
	 */
	protected void addFehler(final @NotNull String fehlermeldung) {
		_fehler.add(new ValidatorFehler(this, fehlermeldung));
		updateFehlerart(null);
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
		return _kontext.getValidatorManager().getFehlerartBySchuljahrAndValidatorClass(_kontext.getSchuljahr(), this.getClass());
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
