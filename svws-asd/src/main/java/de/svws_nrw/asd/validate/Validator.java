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
				addFehler(-1, "Unerwarteter Fehler bei der Validierung: " + e.getMessage());
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
	private void updateFehlerart(final @NotNull ValidatorFehlerart art) {
		if (this._fehlerart.ordinal() > art.ordinal())
			this._fehlerart = art;
	}

	/**
	 * Erstellt einen neuen Fehler mit der übergebenen Fehlermeldung
	 *
	 * @param pruefschritt    die Nummer des Prüfschrittes, bei welchem der Fehler aufgetreten ist
	 * @param fehlermeldung   die Fehlermeldung
	 */
	protected void addFehler(final int pruefschritt, final @NotNull String fehlermeldung) {
		_fehler.add(new ValidatorFehler(this, pruefschritt, fehlermeldung));
		updateFehlerart(this.getValidatorFehlerart(pruefschritt));
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
	 * @param pruefschritt   der Prüfschritt, bei welchem der Fehler aufgetreten ist
	 *
	 * @return die Fehlerart
	 */
	public @NotNull ValidatorFehlerart getValidatorFehlerart(final int pruefschritt) {
		return _kontext.getValidatorManager().getFehlerartBySchuljahrAndValidatorClassAndPruefschritt(_kontext.getSchuljahr(), this.getClass(), pruefschritt);
	}


	/**
	 * Gibt das Fehlercode-Präfix zurück, welcher diesem speziellen Validator zugeordnet ist.
	 *
	 * @return das Fehlercode-Präfix
	 */
	public @NotNull String getFehlercodePraefix() {
		return _kontext.getValidatorManager().getFehlercodePraefixBySchuljahrAndValidatorClass(_kontext.getSchuljahr(), this.getClass());
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
